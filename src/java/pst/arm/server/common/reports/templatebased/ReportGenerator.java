package pst.arm.server.common.reports.templatebased;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class ReportGenerator {

    protected TemplateSelector selector;
    protected ReplacementMapper mapper;
    protected boolean removePlaceHolder;

    public ReportGenerator() {
        removePlaceHolder = false;
    }

    public ReportGenerator(TemplateSelector selector, ReplacementMapper mapper) {
        this();
        this.selector = selector;
        this.mapper = mapper;
    }

    public byte[] generateToBytes() {
        byte ret[] = null;
        
        try {
            HWPFDocument document = generate();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            outputStream.flush();
            outputStream.close();
            ret = outputStream.toByteArray();
        } catch (Exception ex) {
            System.out.println("Exception while generating document from template: " + ex.getMessage());
        }
        
        return ret;
    }

    public void generateToFile(String fileName) {
        throw new UnsupportedOperationException();
    }

    public HWPFDocument generate() throws IOException {
        HashMap<String,String> map = mapper.getReplaceMap();
        HWPFDocument document = new HWPFDocument(selector.getTemplate());
        Range range = document.getRange();
        int numParagraph = range.numParagraphs();
        for (int j = 0; j < numParagraph; j++) {
            Paragraph paragraph = range.getParagraph(j);
            String paragraphText = paragraph.text();
            for (String key : map.keySet()) {
                if (paragraphText.contains(key)) {
                    String value = map.get(key);
                    if(value != null) {
                        paragraph.replaceText(key, value);
                    } else if (removePlaceHolder) {
                        paragraph.replaceText(key, "");
                    }
                }
            }
        }
        return document;
    }
}
