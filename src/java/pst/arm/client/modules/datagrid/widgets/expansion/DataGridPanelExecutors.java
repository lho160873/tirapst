package pst.arm.client.modules.datagrid.widgets.expansion;

import java.util.Map;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author n
 */
public class DataGridPanelExecutors extends DataGridPanel {

    public DataGridPanelExecutors(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelExecutors(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelExecutors(String tableName) {
        super(tableName);
    }

    public DataGridPanelExecutors(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        btnView.setVisible(false);
        btnRefr.setVisible(false);
        btnEdit.setVisible(false);
    }

    public DataGridPanelExecutors(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
    
    @Override
    protected void onAdd() {
        DDataGrid domain = createDomain();

        if (condition.getFilters().size() > 0) {
            //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
            for (Map.Entry filtr : condition.getFilters().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) filtr.getKey();
                IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (isOpenAsSub && passedFields != null) {
            for (Map.Entry passField : passedFields.entrySet()) {
                SKeyForColumn key = (SKeyForColumn) passField.getKey();
                IRowColumnVal val = (IRowColumnVal) passField.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (condition.getValDefault().size() > 0) {
            //если есть значения по умолчанию задаем их
            for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) defaultVal.getKey();
                IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                domain.getRows().put(key, val);
            }
        }

        DataGridEditWindow window = new DataGridEditWindowExecutors(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }
    
    
}
