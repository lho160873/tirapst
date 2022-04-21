package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DTableJoin implements Serializable{
    protected String tableOne; //алиас первой таблицы
    protected String tableTwo; //алиас второй таблицы
    protected ERelationType relationType; //тип связи двух таблиц
    protected ArrayList< DColumnJoin > columnJoins; //список полей для связи

    
    public DTableJoin()
    {
        tableOne = "";
        tableTwo = "";
        relationType = ERelationType.INNER;
        columnJoins = new ArrayList< DColumnJoin >();
    }
    
    public DTableJoin(String one,String two)
    {
        tableOne = one;
        tableTwo = two;
        relationType = ERelationType.INNER;
        columnJoins = new ArrayList< DColumnJoin >();
    }
    public String getTableOne( )
    {
        return tableOne;
    }
    
    public void setTableOne( String s )
    {
         tableOne = s;
    }
    public String getTableTwo( )
    {
        return tableTwo;
    }
    
    public void setTableTwo( String s )
    {
        tableTwo = s;
    }
    
     public ERelationType getRelationType( )
    {
        return relationType;
    }
    
    public void setRelationType( ERelationType t )
    {
        relationType = t;
    }
    
    public ArrayList< DColumnJoin > getColumnJoins( )
    {
        return columnJoins;
    }
    
    public void setColumnJoins( ArrayList< DColumnJoin > t )
    {
        columnJoins = t;
    }
    
    public void addColunmInJoin(  DColumnJoin  f )
    {
       columnJoins.add( f );
    }

}
