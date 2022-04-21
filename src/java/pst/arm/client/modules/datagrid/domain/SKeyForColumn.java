package pst.arm.client.modules.datagrid.domain;
import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class SKeyForColumn implements Serializable {
    private String columnName;   ///< имя поля таблицы
    private String tableAlias;   ///< алиас таблицы в запросе
    
    public SKeyForColumn()
    {
     columnName = "";
     tableAlias = "MAIN";
    }
    
    public SKeyForColumn(String ta, String cn)
    {
     tableAlias = ta;
     columnName = cn;     
    }
    //key дожен иметь формат "TABLE_ALIAS:COLUMN_NAME"
    public SKeyForColumn(String key)
    {
      String[] s = key.split(":");
      if (s.length == 2)
      {
        tableAlias = s[0];
        columnName = s[1];        
      }
    }
   
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String t) {
        this.columnName = t;
    }
      
    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String s) {
        this.tableAlias = s;
    }
   
    
   @Override
    public int hashCode() {
       int hash = 0; 
       hash = hash + (this.tableAlias != null ? this.tableAlias.hashCode() : 0);
       hash = hash + (this.columnName != null ? this.columnName.hashCode() : 0);
       return hash;
    }
   
   @Override
	public boolean equals(Object object) {
		if(object == null || !(object instanceof SKeyForColumn) ) {
			return false;			
		}
		SKeyForColumn other = (SKeyForColumn)object;
                return other.hashCode() == this.hashCode();
		//return ( this.columnName.equals(other.getColumnName()) && this.tableAlias.equals(other.getTableAlias()));//other.hashCode() == this.hashCode();
	}

    @Override
    public String toString() {
        return this.tableAlias + "." + this.columnName;
    }

}
