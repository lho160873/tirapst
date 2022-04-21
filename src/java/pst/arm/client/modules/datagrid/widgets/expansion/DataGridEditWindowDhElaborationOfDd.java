package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.widget.MessageBox;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

/**
 *
 * @author  LKHorosheva
 * @date    29.05.2017
 */
public class DataGridEditWindowDhElaborationOfDd extends DataGridEditWindow {
   


    public DataGridEditWindowDhElaborationOfDd(DDataGrid domain, DTable table, EditState state, EWindowType windowType)
    {         
        super(domain, table, state, windowType);        
        pnlEdit = new DataGridEditableFormDhElaborationOfDd(table, state);
        registerEditForm(pnlEdit); 
    }

   /*public DataGridEditWindowDhElaborationOfDd(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType,cnd);
        pnlEdit = new DataGridEditableFormDhElaborationOfDd(table, state, cnd);
        registerEditForm(pnlEdit);
    }

    public DataGridEditWindowDhElaborationOfDd(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType,cnd,customHeader);
        pnlEdit = new DataGridEditableFormDhElaborationOfDd(table, state, cnd, customHeader);
        registerEditForm(pnlEdit);
    }
    */
    
    /*@Override
    protected void initData() {
        updateFormsFromDomain();
        Boolean isReadOnly = false;
        if (state == EditState.EDIT) {
            SKeyForColumn key = new SKeyForColumn("MAIN:REGISTERED_USER_ID");
            Integer userId = (Integer) domain.getRows().get(key).getVal();
            Integer currUserId = ConfigurationManager.getUserId().intValue();
            isReadOnly = (currUserId != userId);
        }
        if (isReadOnly) {
            ((DataGridEditableFormDhElaborationOfDd) pnlEdit).enbl();
        }
    }*/

}
