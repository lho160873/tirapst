/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.user.client.ui.TextBox;
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
 * @author Igor
 */
public class DataGridPanelCommander extends DataGridPanel {
    
    public DataGridPanelCommander(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelCommander(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelCommander(String tableName) {
        super(tableName);
    }

    public DataGridPanelCommander(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelCommander(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
    
    @Override
    protected void onAdd() {
        DDataGrid domain = createDomain();//new DDataGrid();
        //domain.setName(table.getQueryName());

        if (condition.getFilters().size() > 0) {
            //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
            //domain.setName(table.getQueryName());
            for (Map.Entry filtr : condition.getFilters().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (isOpenAsSub && passedFields != null) {
            for (Map.Entry passField : passedFields.entrySet()) {
                SKeyForColumn key = (SKeyForColumn) passField.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) passField.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (condition.getValDefault().size() > 0) {
            //если есть значения по умолчанию задаем их
            //domain.setName(table.getQueryName());
            for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                domain.getRows().put(key, val);
            }
        }

        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.setWidth(680);
        window.setHeight(580);
        window.show();
    }
    
    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();  
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.setWidth(680);
        window.setHeight(580);
        window.show();
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
