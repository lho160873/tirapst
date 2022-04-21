package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.MessageBox;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DDataGrid implements Serializable, EditableDomain<DDataGrid>{
   
   private String name; //имя Таблицы (Запроса) для которого эти данные были получены
   private HashMap<SKeyForColumn, IRowColumnVal> rows; //

private Boolean hasChildren = false;

public Boolean getHasChildren() {
    return hasChildren;
}

public void setHasChildren(Boolean hasChildren) {
    this.hasChildren = hasChildren;
}


   public DDataGrid() {
       rows = new  HashMap<SKeyForColumn,IRowColumnVal>(); 
       name = "";
    }
    
   public DDataGrid( String name) {
       rows = new  HashMap<SKeyForColumn,IRowColumnVal>(); 
       setName(name);
    }
    
    public String getName() {
        return name;
    }

    public void setName( String n ) {
        this.name = n;
    }
   
    public void setRows(  HashMap<SKeyForColumn,IRowColumnVal> rows )
    {
        this.rows = rows;
    }
    
    public  HashMap<SKeyForColumn, IRowColumnVal> getRows()
    {
        return rows;
    }

    @Override
    public DDataGrid newInstance() {
        DDataGrid row = new DDataGrid();
        row.name = name;
        row.rows = new HashMap<SKeyForColumn,IRowColumnVal>();
        return row;
    }

    @Override
    public void copy(DDataGrid domain) {
        this.name = domain.name;
        this.rows = new  HashMap<SKeyForColumn, IRowColumnVal>();
        if(domain.rows != null && !domain.rows.isEmpty())
        {
            this.rows.putAll(domain.rows);    
        }
    }

    @Override
    public Boolean isDomainFull() {
        return true;
    }

    @Override
    //TODO
    public Long getDomainId() {
       Long id = -1L; 
       /*for ( Map.Entry colEntry : this.rows.entrySet() )
       {
            IRowColumnVal colVal = (IRowColumnVal)colEntry.getValue();
            IColumnBuilder colBuilder = colVal.getColumnBuilder();
            DColumn col = colBuilder.getColumn();
            if ( col.getIsKey() )
                id = (Long)colVal.getVal(); //пока предполагаем что ключевое поле одно и обязательно целого типа.
       }*/
         return id;
    }

    public Boolean isDomainEquals(DDataGrid domain) {
        if(this.rows.size() != domain.rows.size()){
            return false;
        }
        if((this.rows == null) != (domain.rows == null)){
            return false;
        }
        //if( this.rows == domain.rows && this.name.equals(domain.name) ){
        if( this.name.equals(domain.name) ){
             for (Map.Entry rowEntry : this.rows.entrySet()) {
                 SKeyForColumn key = (SKeyForColumn)rowEntry.getKey();
                 IRowColumnVal val = (IRowColumnVal)rowEntry.getValue();
                 if ( domain.rows.containsKey(key) ){
                     if ((this.rows.get(key).getVal() == null) && (domain.rows.get(key).getVal() == null)) {
                         continue;
                     }
                     if ((this.rows.get(key).getVal() == null) != (domain.rows.get(key).getVal() == null)) {
                         return false;
                     }
                    
                     if ( DataUtil.compare(val.getVal(),domain.rows.get(key).getVal()) || domain.rows.get(key).getVal().equals(val.getVal()) || domain.rows.get(key).getVal() == val.getVal() )
                     {
                         continue;
                     }  
                     else
                     {
                         return false;    
                     }                     
                     
                 }
                 else
                 {
                     return false;
                 }                 
             }
             return true;
        }
              
        return false;
    }

}