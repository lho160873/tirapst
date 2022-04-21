package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author wesStyle
 */
public class DataGridPanelExecPeriod extends DataGridPanel {

    public DataGridPanelExecPeriod(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelExecPeriod(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelExecPeriod(String tableName) {
        super(tableName);
    }

    public DataGridPanelExecPeriod(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelExecPeriod(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        mode = EditMode.VIEWONLY;
        super.enabledBtn();
        
        String datagridName = tableName.toUpperCase();
        isDictDelete = ((ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName)));
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
         
        
        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        sprEdit.setVisible(isDictDelete);
        sprMenuItemEdit.setVisible(isDictDelete);

        updateToolBar();
    }

    protected void onDelete() {
        MessageBox.confirm(commonConstants.confirm(), "Вы действительно хотите прекратить действие данного периодического назначения?", new Listener<MessageBoxEvent>() {
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
                            //MessageBox.alert(commonConstants.error(), datagridMessages.errorDelete(), null).show();
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            //reloadGrid();
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
