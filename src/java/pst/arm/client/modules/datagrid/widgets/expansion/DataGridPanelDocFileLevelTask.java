/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.widget.MessageBox;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelDocFileLevelTask extends DataGridPanelDocFileElaboration{
    
    public DataGridPanelDocFileLevelTask(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDocFileLevelTask(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDocFileLevelTask(String tableName) {
        super(tableName);
    }

    public DataGridPanelDocFileLevelTask(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDocFileLevelTask(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
  
    @Override
    public void enabledBtn() {
        //настройка пров доступа        
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

        String datagridName = tableName.toUpperCase();
        isCancell = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictCancel")
                || ConfigurationManager.getPropertyAsBoolean("dictCancel_" + datagridName));

        btnCancell.setVisible(isCancell);
        menuItemCancell.setVisible(isCancell);
                
        super.updateToolBar();
    }
    
}
