package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author Igor 
 * Окно в Контроль производства - Контроль - Кол-во невып. приказов
 * и распоряжений - Невыполненные прикащы и распоряжения
 */
public class DataGridPanelOfficeDocContrIgControl extends DataGridPanel {

    public DataGridPanelOfficeDocContrIgControl(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOfficeDocContrIgControl(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOfficeDocContrIgControl(String tableName) {
        super(tableName);
    }

    public DataGridPanelOfficeDocContrIgControl(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOfficeDocContrIgControl(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    protected void gridRowDoubleClick(GridEvent event) {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindowOfficeDocContrIgControl(domain, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }
}
