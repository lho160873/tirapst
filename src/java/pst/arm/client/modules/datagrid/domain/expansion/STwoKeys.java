package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

import java.io.Serializable;

/**
 * Created by wesStyle on 12.01.2015.
 */
public class STwoKeys implements Serializable{
    private String first, second;
    private SKeyForColumn firstKey, secondKey;
    private String message;

    public STwoKeys() {
    }

    public STwoKeys(String first, String second, String message) {
        this.first = first;
        this.second = second;
        this.message = message;

        firstKey = new SKeyForColumn(first);
        secondKey = new SKeyForColumn(second);
    }

    public SKeyForColumn getFirstKey() {
        return firstKey;
    }

    public SKeyForColumn getSecondKey() {
        return secondKey;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
        firstKey = new SKeyForColumn(first);
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
        secondKey = new SKeyForColumn(second);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
