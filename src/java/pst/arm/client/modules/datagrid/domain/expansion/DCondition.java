package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

import java.io.Serializable;

/**
 * Created by wesStyle on 21/08/14.
 */
public class DCondition implements Serializable{

    private SKeyForColumn key;
    private String val;


    public DCondition() {
    }

    public DCondition(SKeyForColumn key, String val) {
        this.key = key;
        this.val = val;
    }

    public SKeyForColumn getKey() {
        return key;
    }

    public void setKey(SKeyForColumn key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
