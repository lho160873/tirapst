package pst.arm.client.modules.datagrid.widgets.expansion;

   

import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridEditWindowAppProduction extends DataGridEditWindow{


 public DataGridEditWindowAppProduction(DDataGrid domain, DTable table, EditState state, EWindowType windowType)
    {         
        super(domain, state, windowType);    
      
        setTable(table);
        pnlEdit = new DataGridEditableFormAppProduction(table, state);
        registerEditForm(pnlEdit); 
    }

    public DataGridEditWindowAppProduction(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, state, windowType);
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormAppProduction(table, state, cnd);
        registerEditForm(pnlEdit);
    }

}
