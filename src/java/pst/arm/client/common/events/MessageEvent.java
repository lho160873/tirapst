package pst.arm.client.common.events;

import java.io.Serializable;

/**
 * @author p.havelaar
 */
public class MessageEvent implements Serializable {

    private long code;
    private String data;
    private Integer userIdFrom = null;
    private Integer userIdTo = null;
    private EMessageType msgType; //1 входящие 2 исходящие
    
    public MessageEvent() {

    }
    
     public MessageEvent(long codgetUserIdFore, String data,Integer userIdFrom,Integer userIdTo,EMessageType m) {
        this.code = code;
        this.data = data;
        this.userIdFrom = userIdFrom;
        this.userIdTo = userIdTo;
         this.msgType = m;
    }

    public void setUserIdFrom(Integer userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public Integer getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdTo(Integer userIdTo) {
        this.userIdTo = userIdTo;
    }

    public Integer getUserIdTo() {
        return userIdTo;
    }
    public void setMsgType(EMessageType m) {
        this.msgType = m;
    }

    public EMessageType getMsgType() {
        return msgType;
    }

    public MessageEvent(long code, String data) {
        this.code = code;
        this.data = data;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getData();
    }

    public static enum EMessageType {
        MSG_IN,
        MSG_OUT
    }
}
