/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author Igor
 */
public class DataGridPanelMassMailingListIg extends DataGridPanel {

    public DataGridPanelMassMailingListIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelMassMailingListIg(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelMassMailingListIg(String tableName) {
        super(tableName);
    }

    public DataGridPanelMassMailingListIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelMassMailingListIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        String datagridName = tableName.toUpperCase();
        isDictAdd = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName));
        isDictEdit = false;
        isDictDelete = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName));
        isDictView = (ConfigurationManager.getPropertyAsBoolean("dictView")
                || ConfigurationManager.getPropertyAsBoolean("dictView_" + datagridName));
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

    @Override
    protected void onDelete() {
        MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                    List<DDataGrid> domains = new ArrayList<DDataGrid>();
                    for (final BeanModel beanModel : selection) {
                        DDataGrid domain = beanModel.getBean();
                        domains.add(domain);
                    }
                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            reloadGrid();
                            for (final BeanModel beanModel : selection) {
                                grid.getStore().remove(beanModel);
                            }
//                            loader2.load();
                            selectCurrentRow();
                        }
                    };
                    getService().deleteDomains(tableName, domains, callback);
                }
            }
        });
        
    }
}
