package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class SListVal<T,D> implements Serializable {
    protected T key;   ///< значение поля в БД
    protected D val;   ///< подстановочное значение (например 0 - нет, 1 - да)
    
    public SListVal()
    {
     key = null;
     val = null;
    }
    public SListVal (T key, D val)
    {
        this.key = key;
        this.val = val;
    }
    
    public void setKey(T key) 
    {
        this.key = key;
    }
    
    public T getKey() 
    {
       return key;
    }
    
    public void setVal(D val) 
    {
        this.val = val;
    }
    
    public D getVal() 
    {
       return val;
    }
    
}
