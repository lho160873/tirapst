package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnJoinWithValue extends DColumnJoin implements Serializable{
    
    protected boolean firstIsValue; // первый столбец это значение (есл нет, то второй)

    public boolean isFirstIsValue() {
        return firstIsValue;
    }

    public void setFirstIsValue(boolean firstIsValue) {
        this.firstIsValue = firstIsValue;
    }   
    
    public DColumnJoinWithValue( )
    {
        super();
        firstIsValue = false;
    }
    public DColumnJoinWithValue( String one, String two, boolean bool)
    {
        super(one, two);
        firstIsValue = bool;
    }
   
}
