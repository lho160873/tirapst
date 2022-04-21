package pst.arm.client.modules.mainpage.event;

import java.io.Serializable;

/**
 * @author p.havelaar
 */
public class Event implements Serializable {

    private long code;
    private String data;

    public Event() {

    }

    public Event(long code, String data) {
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
        return getCode() + ": " + getData();
    }


}
