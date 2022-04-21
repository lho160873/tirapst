package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DListValString extends SListVal<String,String> implements Serializable{

    public DListValString( )
    {
       super();
    }
    
    public DListValString(String key, String val) {
        this.key = key;
        this.val = val;
    }
    public DListValString(SListVal<String,String> val) {
       // super(key,val);
        this.key = val.getKey();
        this.val = val.getVal();
    }
    
}