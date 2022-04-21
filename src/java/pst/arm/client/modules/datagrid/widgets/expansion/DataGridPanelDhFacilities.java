/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
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
 */
public class DataGridPanelDhFacilities extends DataGridPanel {
    
    public DataGridPanelDhFacilities(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDhFacilities(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDhFacilities(String tableName) {
        super(tableName);
    }

    public DataGridPanelDhFacilities(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDhFacilities(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
    
    @Override
    protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.setWidth(680);
        window.setHeight(580);
        window.show();
    }
}
