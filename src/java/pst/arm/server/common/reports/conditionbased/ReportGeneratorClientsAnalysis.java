package pst.arm.server.common.reports.conditionbased;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;

/**
 *
 * @author Mikhail Zakharov
 * @since 0.0.3
 */
@Service
public class ReportGeneratorClientsAnalysis 
{
    private MessageSource messageSource;
    
    @Autowired
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("ReportGeneratorClientsAnalysis");
    protected Document document;
    protected Font fontHeader;
    protected Font fontText;
    protected BaseFont fontBase;
    protected OutputStream outputStream;
    protected Table table;
    protected Report report;
    
    public ReportGeneratorClientsAnalysis() throws IOException, DocumentException 
    {
    }

    protected void initFonts() throws DocumentException, IOException 
    {
        fontBase = BaseFont.createFont("/fonts/arial.ttf", "Cp1251", BaseFont.EMBEDDED); // #NLS
        fontHeader = new Font(fontBase, 10, Font.BOLD);
        fontText = new Font(fontBase, 10);
    }

    public FileObjectDescriptor generateReport(Report report)
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
            case RTF:
                generateRtfReport();
                fod.setFileContentType("application/msword");
                fod.setFileExt("doc");
                break;
            default:
                MessageBox.alert("Ошибка", "Неизвестный формат", null);
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
    
    protected void generateRtfReport() throws FileNotFoundException, DocumentException 
    {
        Rectangle r = PageSize.A4;
        if ((report.getVertical() == null) || (report.getVertical() == Boolean.FALSE))
        {
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

    protected void generateDocument() throws DocumentException 
    {
        generateDocumentCaption();
        generateDocumentTable();
    }

    protected void generateDocumentCaption() throws DocumentException 
    {
        Paragraph p = new Paragraph(report.getCaption(), fontHeader);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(10.0f);
        document.add(p);
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