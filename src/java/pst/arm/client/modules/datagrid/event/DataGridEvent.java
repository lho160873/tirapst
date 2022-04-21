package pst.arm.client.modules.datagrid.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridEvent extends BaseEvent{
    private DDataGrid domain;
    private DTable table; //список полей в таблице
    
    public DataGridEvent(Object source){
        super(source);
    }
    
    public DataGridEvent(Object source, DDataGrid domain, DTable table){
        super(source);
        this.domain = domain;
        this.table = table;
    }

    public DDataGrid getDomain() {
        return domain;
    }
    
     public DTable getTable() {
        return table;
    }
}
