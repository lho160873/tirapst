package pst.arm.client.common.domain.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Report generation result. If generation is failed contains list of error messages.
 * Messages separated by new line symbol.
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public class ReportGeneratorResult implements Serializable{
    /** Id of report in session*/
    String id;
    
    /** Generated errors*/
    List<String> errMessages;

    public ReportGeneratorResult() {
        id = null;
        errMessages = new ArrayList<String>();
    }

    public ReportGeneratorResult(String id) {
        this();
        this.id = id;
    }
    
    public String getId(){
        return id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public void addMessage(String message){
        errMessages.add(message);
    }
    
    public String getMessages(){
        StringBuilder builder = new StringBuilder();
        for(String message : errMessages){
            builder.append(message).append("\n");
        }
        return builder.toString();
    }
    
    /**
     * 
     * @return true if no error messages
     */
    public boolean isSuccess(){
        return errMessages.isEmpty();
    }

    public void setMessages(List<String> messages) {
        if(messages == null || messages.isEmpty()){
            errMessages.clear();
        }else{
            errMessages = messages;
        }
    }
}
