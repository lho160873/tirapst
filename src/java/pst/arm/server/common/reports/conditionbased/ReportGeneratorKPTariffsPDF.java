package pst.arm.server.common.reports.conditionbased;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pst.arm.client.Interactive;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;

/**
 *
 * @author Mikhail Zakharov, Alexandr Kozhin
 * @since 0.0.1
 */
@Service
public class ReportGeneratorKPTariffsPDF 
{
    private MessageSource messageSource;
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("ReportGeneratorKPTariffsPDF");
    protected Document document;
    protected Font fontHeader;
    protected Font fontText;
    protected BaseFont fontBase;
    protected OutputStream outputStream;
    protected Table table;
    protected ReportKpTariffs report;
    
    public ReportGeneratorKPTariffsPDF() throws IOException, DocumentException 
    {
    }

    protected void initFonts() throws DocumentException, IOException 
    {
        fontBase = BaseFont.createFont("/fonts/arial.ttf", "Cp1251", BaseFont.EMBEDDED); // #NLS
        fontHeader = new Font(fontBase, 10, Font.BOLD);
        fontText = new Font(fontBase, 10);
    }

    public FileObjectDescriptor generateReport(ReportKpTariffs report)
            throws FileNotFoundException, DocumentException, IOException, DocumentException 
    {
        initFonts();

        this.report = report;
        FileObjectDescriptor fod = new FileObjectDescriptor();
        fod.setFileName("report");

        initByteOutputStream();
        
        switch (report.getReportType()) 
        {
            case PDF:
                generatePdfReport();
                fod.setFileContentType("application/pdf");
                fod.setFileExt("pdf");
                break;
            default:
                MessageBox.alert("Ошибка", "Данный класс допускает создание отчетов, только в формате PDF", null);
                generatePdfReport();
                fod.setFileContentType("application/pdf");
                fod.setFileExt("pdf");
                break;
        }

        ByteArrayOutputStream ret = (ByteArrayOutputStream) outputStream;
        fod.setFileContent(ret.toByteArray());
        
        return fod;
    }

    protected void initByteOutputStream()
    {
        outputStream = new ByteArrayOutputStream();
    }
    
    protected void generatePdfReport() throws FileNotFoundException, DocumentException 
    {
        Rectangle r = PageSize.A4;
        if ((report.getVertical() == null) || (report.getVertical() == Boolean.FALSE))
        {
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

    protected void generateDocument() throws DocumentException 
    {
        generateDocumentImage();
        generateDocumentCaption();
        generateDocumentHeaders();
        generateDocumentTable();
        generateDocumentFooters();
    }

    protected void generateDocumentImage() throws DocumentException 
    {
        try
        {
            Chapter baseChapter = new Chapter(0);
            baseChapter.setNumberDepth(0);
            Section baseSection = baseChapter.addSection("");
            baseSection.setNumberDepth(0);
            log.warn("Current URL is " + Interactive.getRelativeURLForImageForReport("/images/default/logoMM.png"));
            // TODO : Проверить корректность генерации изображения на финальном сервере (может не подходить адрес изображения)
            Image img = Image.getInstance(new URL(Interactive.getRelativeURLForImageForReport("/images/default/logoMM.png")));
            baseChapter.setIndentation(document.getPageSize().getWidth() - img.getWidth() - 2 * document.rightMargin());
            baseSection.add(img);
            document.add(baseChapter);
        }
        catch (BadElementException ex)
        {
            Logger.getLogger(ReportGeneratorKPTariffsPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(ReportGeneratorKPTariffsPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ReportGeneratorKPTariffsPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void generateDocumentCaption() throws DocumentException 
    {
        Paragraph p = new Paragraph(report.getCaption(), fontHeader);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(10.0f);
        document.add(p);
    }

    protected void generateDocumentHeaders() throws DocumentException
    {
        Paragraph p = new Paragraph(report.getInfo(), fontText);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(5.0f);
        document.add(p);
    }

    protected void generateDocumentFooters() throws DocumentException 
    {
        if ((report.getText() != null) && (report.getText().isEmpty() == false)) 
        {
            Paragraph p = new Paragraph(report.getText(), fontText);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(5.0f);
            document.add(p);
        }
    }

    protected void generateDocumentTable() throws BadElementException, DocumentException 
    {
        ReportColumns columns = report.getColumns();
        if (columns == null || (columns.getColumnCount() == 0))
        {
            return;
        }
        int nColumnCount = columns.getColumnCount();
        float[] widths = new float[nColumnCount];

        table = new Table(nColumnCount);
        table.setWidth(100);
        boolean badWidths = false;
        
        for (int i = 0; i < nColumnCount; i++) 
        {
            Float f = columns.getColumnWidth(i);
            if (f == null)
            {
                badWidths = true;
            }
            else
            {
                widths[i] = f.floatValue();
            }
        }
        
        if (!badWidths)
        {
            table.setWidths(widths);
        }

        generateTableHeader();
        generateTableContent();
        document.add(table);
    }

    protected void generateTableHeader()
    {
        ReportColumns columns = report.getColumns();
        for (int i = 0; i < columns.getColumnCount(); i++)
        {
            String a = columns.getColumnName(i);
            Cell cell = new Cell();
            Paragraph p = new Paragraph(a, fontHeader);
            p.setAlignment(Element.ALIGN_TOP);
            p.setIndentationLeft(2f);
            cell.add(p);
            table.addCell(cell);
        }
    }

    protected void generateTableContent() 
    {
        ReportColumns columns = report.getColumns();
        ReportData data = report.getData();
        for (int i = 0; i < data.getRecordCount(); i++)
        {
            for (int j = 0; j < columns.getColumnCount(); j++)
            {
                Cell cell = new Cell();
                String value = data.getValueAt(i, j);
                Paragraph p = new Paragraph(value, fontText);
                p.setAlignment(Element.ALIGN_TOP);
                p.setIndentationLeft(2f);
                cell.add(p);
                table.addCell(cell);
            }
        }
    }
}