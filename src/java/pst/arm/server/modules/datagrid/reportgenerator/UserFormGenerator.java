package pst.arm.server.modules.datagrid.reportgenerator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;

/**
 *
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public class UserFormGenerator {
    ResourceBundle messages;
    List<String> errMessages;

    public UserFormGenerator() {
        messages = ResourceBundle.getBundle("/modules/datagrid/messages", Locale.getDefault());
        errMessages = new ArrayList<String>();
    }
    
    public FileObjectDescriptor generateReport(){

        
        byte[] data = createForm();
        
        FileObjectDescriptor fod = new FileObjectDescriptor(data);
        fod.setFileContentType("application/msword");
        fod.setFileName("anketa-test");// + Translit.toTranslit(user.getFio()));
        fod.setFileExt("doc");
        
        return fod;
    }
    
//пример заполнения word-го шаблона переданными данными
    private byte[] createForm(){
        try {
            String createdDateStr = "";
            //Date createdDate = new Date();//user.getDateCreated();
            //if(createdDate != null) {
                createdDateStr = "";//new SimpleDateFormat("dd.MM.yyyy").format(createdDate);
            //}
            
            Map<String, String> dict = new HashMap<String, String>();
            dict.put(messages.getString("rh.userform.archivename"),"");// archiveName);
            dict.put(messages.getString("rh.userform.lastname"), "Иванов");//user.getLastName());
            dict.put(messages.getString("rh.userform.firstname"), "Иван");//user.getFirstName());
            dict.put(messages.getString("rh.userform.patronimic"), "Иванович");//user.getPatronymic());
            dict.put(messages.getString("rh.userform.jobandposition"), "");//user.getJobAndPosition());
            dict.put(messages.getString("rh.userform.fromorganization"), "");//.getFromOrganization());
            dict.put(messages.getString("rh.userform.education"), "");//user.getEducation());
            dict.put(messages.getString("rh.userform.degree"), "");//user.getDegree());
            dict.put(messages.getString("rh.userform.subjectandyears"), "");//user.getSubjectAndYears());
            dict.put(messages.getString("rh.userform.adress"),"");// user.getAddress());
            dict.put(messages.getString("rh.userform.phone"), "");//user.getPhone());
            dict.put(messages.getString("rh.userform.officephone"), "");//user.getOfficePhone());
            dict.put(messages.getString("rh.userform.allpassportinfo"), "");//user.getAllPassportInfo());
            dict.put(messages.getString("rh.userform.date"), createdDateStr);

            String path = "/rhuserform/anketa-table.doc";
            InputStream inputStream = getClass().getResourceAsStream(path);
            HWPFDocument document = new HWPFDocument(inputStream);

            Range range = document.getRange();
            int numParagraph = range.numParagraphs();
            for (int j = 0; j < numParagraph; j++) {
                Paragraph paragraph = range.getParagraph(j);
                String text = paragraph.text();
                for (String key : dict.keySet()) {
                    if (text.contains(key)) {
                        paragraph.replaceText(key, dict.get(key) != null ? dict.get(key) : "");
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return outputStream.toByteArray();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public List<String> getMessages() {
        return errMessages;
    }
}
