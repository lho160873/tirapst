package pst.arm.client.modules.datagrid.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.widgets.DataBasePanel;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataBasePanelEvent extends BaseEvent{
    private DataBasePanel panel;
    
    public DataBasePanelEvent(Object source){
        super(source);
    }
    
    public DataBasePanelEvent(Object source, DataBasePanel panel){
        super(source);
        this.panel = panel;
    }

    public DataBasePanel getPanel() {
        return panel;
    }
    
    
}
