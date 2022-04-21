package pst.arm.client.modules.sync1c.domain;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class Sync1CResult implements Serializable {

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    public enum SyncStatus {

        Created, Updated, Deleted, Error, Equals, None
    }
    private SyncStatus status = SyncStatus.None;
    private Object1C object;
    private String error = "";
    private String text = "";

    public Sync1CResult() {
    }

    public Sync1CResult(Object1C object) {
        this.object = object;
    }

    public Sync1CResult(SyncStatus status, Object1C object) {
        this.status = status;
        this.object = object;
    }

    public Sync1CResult(SyncStatus status, Object1C object, String error) {
        this.status = status;
        this.object = object;
        this.error = error;
    }

    /**
     * @return the status
     */
    public SyncStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(SyncStatus status) {
        this.status = status;
    }

    /**
     * @return the object
     */
    public Object1C getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object1C object) {
        this.object = object;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
