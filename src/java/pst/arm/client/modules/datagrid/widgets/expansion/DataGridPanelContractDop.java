package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author wesStyle
 */
public class DataGridPanelContractDop extends DataGridPanel {

    public DataGridPanelContractDop(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelContractDop(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelContractDop(String tableName) {
        super(tableName);
    }

    public DataGridPanelContractDop(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelContractDop(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        super.enabledBtn();
        
        //не зависимо от прав - убираем возможность добавлять записи
        isDictAdd = false;
        isDictView = true;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
        
        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);
        
        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);
                
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
        
         updateToolBar();
    }
    
    
}
