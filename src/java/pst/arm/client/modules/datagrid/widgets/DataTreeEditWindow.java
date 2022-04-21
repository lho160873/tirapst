package pst.arm.client.modules.datagrid.widgets;

import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;

/**
 *
 * @author wesStyle
 */
public class DataTreeEditWindow extends DataGridEditWindow{

    public DataTreeEditWindow(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }
}
