package pst.arm.server.common.reports.conditionbased;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Header;
import com.lowagie.text.MarkedObject;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class ReportGenerator {

    protected Document document;
    protected Font fontHeader;
    protected Font fontText;
    protected BaseFont fontBase;
    protected OutputStream outputStream;
    protected Table table;
    protected Report report;
    protected int shift = 8;

    public ReportGenerator() throws IOException, DocumentException {
    }

    protected void initFonts() throws DocumentException, IOException {
        fontBase = BaseFont.createFont("/fonts/arial.ttf", "Cp1251", BaseFont.EMBEDDED); // #NLS

        fontHeader = new Font(fontBase, 10, Font.BOLD);
        fontText = new Font(fontBase, 10);
    }

    public FileObjectDescriptor generateReport(Report report)
            throws FileNotFoundException, DocumentException, IOException, DocumentException {
        initFonts();

        this.report = report;
        FileObjectDescriptor fod = new FileObjectDescriptor();
        fod.setFileName("report");

        initByteOutputStream();

        switch (report.getReportType()) {
            case RTF:
                generateRtfReport();
                fod.setFileContentType("application/msword");
                fod.setFileExt("doc");
                break;
            case PDF:
                generatePdfReport();
                fod.setFileContentType("application/pdf");
                fod.setFileExt("pdf");
                break;
            case HTML:
                generateHtmlReport();
                fod.setFileContentType("text/html");
                fod.setFileExt("html");
                break;
            case XLS:
                generateXlsReport();
                fod.setFileContentType("application/x-excel");
                fod.setFileExt("xls");
                break;
            default:
                generateRtfReport();
                fod.setFileContentType("application/msword");
                fod.setFileExt("doc");
                break;
        }

        ByteArrayOutputStream ret = (ByteArrayOutputStream) outputStream;
        fod.setFileContent(ret.toByteArray());

        return fod;
    }

    protected void initByteOutputStream() {
        outputStream = new ByteArrayOutputStream();
    }

    protected void generateRtfReport() throws FileNotFoundException, DocumentException {
        // step 1: creation of a document-object
        Rectangle r = PageSize.A4;
        if ((report.getVertical() == null) || (report.getVertical() == Boolean.FALSE)) {
            r = PageSize.A4.rotate();
        }
        // step 1: create document
        document = new Document(r, 25, 25, 25, 25);

        // step 2:
        RtfWriter2.getInstance(document, outputStream);

        // step 3: we open the document
        document.open();

        // step 4: we fill document
        generateDocument();

        // step 5: we close the document
        document.close();
    }

    protected void generatePdfReport() throws FileNotFoundException, DocumentException {
        Rectangle r = PageSize.A4;
        if ((report.getVertical() == null) || (report.getVertical() == Boolean.FALSE)) {
            r = PageSize.A4.rotate();
        }
        // step 1: create document
        document = new Document(r, 25, 25, 25, 25);

        // step 2:
        PdfWriter.getInstance(document, outputStream);

        // step 3: we open the document
        document.open();

        // step 4: we fill document
        generateDocument();

        // step 5: we close the document
        document.close();
    }

    protected void generateHtmlReport() throws FileNotFoundException, DocumentException {
        // step 1: creation of a document-object
        Rectangle r = PageSize.A4;
        if ((report.getVertical() == null) || (report.getVertical() == Boolean.FALSE)) {
            r = PageSize.A4.rotate();
        }
        // step 1: create document
        int topOfset = 25;
        if (report.getAddSearchPanel()) {
            topOfset = 45;
        }
        document = new Document(r, 25, 25, topOfset, 25);

        // step 2:
        HtmlWriter.getInstance(document, outputStream);

        generateHtmlHeader();

        // step 3: we open the document
        document.open();

        // step 4: add search panel if needed
        generateSearchPanel();

        // step 5: we fill document
        generateDocument();

        // step 6: we close the document
        document.close();
    }

    protected void generateSearchPanel() throws DocumentException {
        if (report.getAddSearchPanel()) {
            Paragraph p = new Paragraph("");
            MarkedObject mo = new MarkedObject(p);
            mo.setMarkupAttribute("id", "alwaysVisibleDiv");
            mo.setMarkupAttribute("class", "alwaysVisibleDiv");
            mo.setMarkupAttribute("style", "position: fixed; top: 0px; left: 0px; right: 0px; background: white; height: 30px; padding-top: 10px; padding-left: 25px;");
            document.add(mo);

            mo = new MarkedObject(p);
            mo.setMarkupAttribute("id", "alwaysVisibleBottomDiv");
            mo.setMarkupAttribute("class", "alwaysVisibleBottomDiv");
            mo.setMarkupAttribute("style", "position: fixed; top: 40px; left: 25px; right: 25px; background: white; height: 5px; opacity: 0.7;");
            document.add(mo);
        }
    }

    protected void generateDocument() throws DocumentException {
        generateDocumentHeader();

        generateDocumentTimeStamp();

        generateDocumentText();

        generateTable();
    }

    protected void generateHtmlHeader() throws DocumentException {
        generateJsCode();
    }

    protected void generateJsCode() throws DocumentException {
        String code = "";

        if (report.getDisableCopy()) {
            code += getDisableCtrlJSCode() + getDisableRightClickJSCode();
        }

        if (report.getAddSearchPanel()) {
            code += getSearchPanelJsCode();
        }

        if (!code.isEmpty()) {
            Header h = new Header(HtmlTags.JAVASCRIPT, code);
            document.add(h);
        }
    }

    /*
     protected void generateCss() throws DocumentException {
     if (report.getAddSearchPanel()) {
     String css = getSearchPanelCss();
     Header h = new Header(HtmlTags.STYLE, css);
     document.add(h);
     }
     }
     *
     */
    protected void generateDocumentHeader() throws DocumentException {//tested
        Paragraph p = new Paragraph(report.getCaption(), fontHeader);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(10.0f);
        if (report.getCaption().contains("Доставка до Терминала") || report.getCaption().contains("Доставка от Терминала")) {
            int col = 2;

            Paragraph beg = new Paragraph("Действует с: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(beg);
            Paragraph end = new Paragraph("Действует по: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(end);
            Paragraph alpha2 = new Paragraph("Терминал: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(alpha2);
            Paragraph terminal = new Paragraph("Страна: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(terminal);
            Paragraph cur = new Paragraph("Валюта: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(cur);
            Paragraph pallet = new Paragraph("Вид/тип упаковки: " + excludeTags(report.getData().getValueAt(1, col++)), fontHeader);
            document.add(pallet);

        }
        document.add(p);
    }

    protected void generateDocumentTimeStamp() throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar c = Calendar.getInstance();
        Date d1 = c.getTime();

        Paragraph p = new Paragraph("По состоянию на: " + sdf.format(d1), fontText);
        p.setSpacingAfter(5.0f);

        document.add(p);
    }

    protected void generateDocumentText() throws DocumentException {
        if ((report.getText() != null) && (report.getText().isEmpty() == false)) {
            Paragraph p = new Paragraph(report.getText(), fontText);
            p.setSpacingAfter(5.0f);

            document.add(p);
        }
    }

    protected void generateTable() throws BadElementException, DocumentException {
        //int nColumnCount = descriptor.getColumnCount();
        ReportColumns columns = report.getColumns();
        if (columns == null || (columns.getColumnCount() == 0)) {
            return;
        }

        int nColumnCount = columns.getColumnCount();

        if (report.getCaption().contains("Доставка до Терминала") || report.getCaption().contains("Доставка от Терминала")) {
            nColumnCount -= shift;
        }

        float[] widths = new float[nColumnCount];

        table = new Table(nColumnCount);
        table.setWidth(100);

        boolean badWidths = false;

        for (int i = 0; i < nColumnCount; i++) {
            Float f = columns.getColumnWidth(i);
            if (f == null) {
                badWidths = true;
            } else {
                widths[i] = f.floatValue();
            }
        }

        if (badWidths == false) {
            table.setWidths(widths);
        }

        generateTableHeader();

        generateTableContent();

        document.add(table);
    }

    protected void generateTableHeader() {//tested
        ReportColumns columns = report.getColumns();
        int startPosition = 0;

        if (report.getCaption().contains("Доставка до Терминала") || report.getCaption().contains("Доставка от Терминала")) {
            startPosition = shift;
        }

        for (int i = startPosition; i < columns.getColumnCount(); i++) {
            String a = columns.getColumnName(i);
            Cell cell = new Cell();
            Paragraph p = new Paragraph(a, fontHeader);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.add(p);
            table.addCell(cell);
        }
    }

    protected void generateTableContent() { //tested
        ReportColumns columns = report.getColumns();
        ReportData data = report.getData();
        if (report.getCaption().contains("Доставка до Терминала") || report.getCaption().contains("Доставка от Терминала")) {
            for (int i = 0; i < data.getRecordCount(); i++) {
                for (int j = shift; j < columns.getColumnCount(); j++) {
                    Cell cell = new Cell();
                    String value = excludeTags(data.getValueAt(i, j));
                    cell.add(new Paragraph(value, fontText));
                    table.addCell(cell);
                }
            }
        } else {
            for (int i = 0; i < data.getRecordCount(); i++) {
                for (int j = 0; j < columns.getColumnCount(); j++) {
                    Cell cell = new Cell();
                    String value = excludeTags(data.getValueAt(i, j));
                    cell.add(new Paragraph(value, fontText));
                    table.addCell(cell);
                }
            }
        }
    }

    private String excludeTags(String sourceString) {
        String patternString = "(<.+?>)";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sourceString);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group != null) {
                matcher.appendReplacement(sb, "");
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private String getDisableRightClickJSCode() {
        return "     function mousehandler(e){\n"
                + "         var myevent = (isNS) ? e : event;\n"
                + "         var eventbutton = (isNS) ? myevent.which : myevent.button;\n"
                + "         if((eventbutton==2)||(eventbutton==3)) return false;\n"
                + "     }\n"
                + "     document.oncontextmenu = mischandler;\n"
                + "     document.onmousedown = mousehandler;\n"
                + "     document.onmouseup = mousehandler;\n";

    }

    private String getDisableCtrlJSCode() {
        return " function hookKeyboardEvents(e) {\n"
                + "         var key_code = (window.event) ? event.keyCode : e.which;\n"
                + "         if (window.event) {\n"
                + "             if (!event.shiftKey && !event.ctrlKey) {\n"
                + "                 window.event.returnValue = null;\n"
                + "                 event.keyCode = 0;\n"
                + "             }\n"
                + "         } else {\n"
                + "		e.preventDefault();\n"
                + "         }\n"
                + "     }\n"
                + "     window.document.onkeydown = hookKeyboardEvents;\n"
                + "     var isNS = (navigator.appName == 'Netscape') ? 1 : 0;\n"
                + "     if(navigator.appName == 'Netscape') document.captureEvents(Event.MOUSEDOWN||Event.MOUSEUP);\n"
                + "         function mischandler() {\n"
                + "         return false;\n"
                + "	}\n";
    }

    private String getSearchPanelJsCode() {
        return " window.onload = function () { \n"
                + "   document.getElementById('alwaysVisibleDiv').innerHTML = \"<button style='width: 80px' type='button' onClick='window.find()' >&#1055;&#1086;&#1080;&#1089;&#1082;</button>\"; \n"
                + "   window.find(); \n"
                + "}\n";
    }
    /*

     private String getSearchPanelCss() {
     return ".alwaysVisibleDiv { position: fixed; top: 0px; left: 0px; right: 0px; background: white; height: 30px; padding-top: 10px; padding-left: 25px; } "
     + " .alwaysVisibleBottomDiv { position: fixed; top: 40px; left: 25px; right: 25px; background: white; height: 5px; opacity: 0.7; } ";
     }
     *
     */

    /**
     * Генерирует *.xls отчёт
     *
     * @author Igor
     */
    private void generateXlsReport() {
        ReportColumns columns = report.getColumns();
        ReportData data = report.getData();

        if (columns == null || (columns.getColumnCount() == 0)) {
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("Excel report");

        // Массив строк
        HSSFRow[] contentRow = new HSSFRow[1 + data.getRecordCount()]; // 1 + для шапки таблицы

        // Генерируем содержание таблицы
        for (int i = 0; i < contentRow.length; i++) {
            contentRow[i] = worksheet.createRow((short) i);

            for (int j = 0; j < columns.getColumnCount(); j++) {
                // Берем значение в текущей ячейке и записываем его в ячейку .xls-файла
                String value;

                if (i == 0) {
                    value = columns.getColumnName(j);
                } else {
                    value = excludeTags(data.getValueAt(i - 1, j));
                }
                HSSFCell cell = contentRow[i].createCell((short) j);
                cell.setCellValue(value);
            }
        }

        // Форматирование ячеек по содержимому
        for (int i = 0; i < columns.getColumnCount(); i++) {
            worksheet.autoSizeColumn(i);
        }

        // Пишем полученные данные в выходной поток
        try {
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
