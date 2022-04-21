/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author Igor
 */
public class DataGridPanelOrderDIg extends DataGridPanel {

    public DataGridPanelOrderDIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        removeButtons();
    }

    public DataGridPanelOrderDIg(DTable table, String tn) {
        super(table, tn);
        removeButtons();
    }

    public DataGridPanelOrderDIg(String tableName) {
        super(tableName);
        removeButtons();
    }

    public DataGridPanelOrderDIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        removeButtons();

    }

    public DataGridPanelOrderDIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        removeButtons();
    }

    private void removeButtons() {
        toolBar.remove(btnAdd);
        toolBar.remove(btnEdit);
        toolBar.remove(btnDelete);
        toolBar.remove(btnView);
        toolBar.remove(sprEdit);
        toolBar.remove(sprReport);
        toolBar.remove(btnReportSplit);
        menu.remove(sprMenuItemEdit);
        menu.remove(menuItemAdd);
        menu.remove(menuItemEdit);
        menu.remove(menuItemDelete);
        menu.remove(menuItemView);
    }
}
