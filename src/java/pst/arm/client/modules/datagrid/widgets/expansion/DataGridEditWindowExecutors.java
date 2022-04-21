package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

/**
 *
 * @author n
 */
public class DataGridEditWindowExecutors extends DataGridEditWindow {

    public DataGridEditWindowExecutors(DDataGrid domain, EditState state, EWindowType windowType) {
        super(domain, state, windowType);
    }

    public DataGridEditWindowExecutors(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }

    public DataGridEditWindowExecutors(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
    }

    public DataGridEditWindowExecutors(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
    }
    
}
