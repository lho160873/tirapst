package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DListValIntegerString extends SListVal<Integer,String> implements Serializable{

    public DListValIntegerString( )
    {
       super();
    }
    
     public DListValIntegerString(Integer key, String val) {
        super(key,val);
        //this.key = key;
        //this.val = val;
    }
    public DListValIntegerString(SListVal<Integer,String> val) {
       // super(key,val);
        this.key = val.getKey();
        this.val = val.getVal();
    }
    
}
