/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author Igor
 */
public class DataGridPanelOfficeDocExecIg extends DataGridPanel{
    
    public DataGridPanelOfficeDocExecIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOfficeDocExecIg(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOfficeDocExecIg(String tableName) {
        super(tableName);
    }

    public DataGridPanelOfficeDocExecIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);

    }

    public DataGridPanelOfficeDocExecIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
    
    @Override
    public void enabledBtn() {
        isDictAdd = true;
        isDictEdit = true;
        isDictDelete = true;
        isDictView = true;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);

        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);

        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);

        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        updateToolBar();
    }
    
}
