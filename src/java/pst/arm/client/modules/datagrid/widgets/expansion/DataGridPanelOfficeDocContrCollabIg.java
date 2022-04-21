package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 * @author Igor
 */
public class DataGridPanelOfficeDocContrCollabIg extends DataGridPanel {

    public DataGridPanelOfficeDocContrCollabIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOfficeDocContrCollabIg(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOfficeDocContrCollabIg(String tableName) {
        super(tableName);
    }

    public DataGridPanelOfficeDocContrCollabIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOfficeDocContrCollabIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        super.enabledBtn();
       
        // Делаем все эти кнопки доступными всегда (описано в постановке)
        isDictAdd = true;
        isDictEdit = true;
        isDictDelete = true;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
                        
        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);


        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);


        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        updateToolBar();
    }
}
