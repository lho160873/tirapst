package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnJoin implements Serializable{
    protected String columnNameTableOne; ///< имя поля в первой таблице
    protected String columnNameTableTwo; ///< имя поля во второй таблице
    protected String on; /// Переопределение ON в JOIN
    EColumnRelationType columnRelationType; ///< тип связи (AND или OR)
    
    public DColumnJoin( )
    {
        columnNameTableOne = "";
        columnNameTableTwo = "";
        columnRelationType = EColumnRelationType.AND;
    }
    public DColumnJoin( String one, String two)
    {
        columnNameTableOne = one;
        columnNameTableTwo = two;
        columnRelationType = EColumnRelationType.AND;
    }
    public void setColumnNameTableOne( String f )
    {
        columnNameTableOne = f;
    }
    
    public String getColumnNameTableOne( )
    {
        return columnNameTableOne;
    }
    
    public void setColumnNameTableTwo( String f )
    {
        columnNameTableTwo = f;
    }
    
    public String getColumnNameTableTwo( )
    {
        return columnNameTableTwo;
    }
    
     public void setColumnRelationType( EColumnRelationType t )
    {
        columnRelationType = t;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public EColumnRelationType getColumnRelationType( )
    {
        return columnRelationType;
    }
}
