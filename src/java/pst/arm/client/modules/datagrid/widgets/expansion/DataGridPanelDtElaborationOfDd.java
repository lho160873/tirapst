package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValDate;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.DRowColumnValString;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelDtElaborationOfDd extends DataGridPanel {

    private int currentIndex;
    private LoadListener newLoadListener;
    private Boolean isBtn = false;
  
    public DataGridPanelDtElaborationOfDd(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDtElaborationOfDd(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDtElaborationOfDd(String tableName) {
        super(tableName);
    }

    public DataGridPanelDtElaborationOfDd(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDtElaborationOfDd(DTable table, String tableName, SRelationInfo relationInfo) {
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
        isDictEdit = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName));
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

        isDictView = true;
        
        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        updateToolBar();
    }
    
    @Override
    protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.setWidth(680);
        window.setHeight(440);
        window.show();
    }
//перезагрузка данных 

    @Override
    public void reloadGrid() {
        isBtn = false;
        super.reloadGrid();
    }

    @Override
    protected void initStore() {
        super.initStore();
        newLoadListener = new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                panelGrid.unmask();
            }

            @Override
            public void handleEvent(LoadEvent le) {
                super.handleEvent(le);
                resizePanelFiltr();
                if (isBtn) {
                    grid.getSelectionModel().select(currentIndex, false);
                } else {
                    selectCurrentRow();
                }
            }

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                if (grid.getSelectionModel() != null) {
                    currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                super.loaderBeforeLoad(le);
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                panelGrid.unmask();
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }
        };
        loader.removeLoadListener(mainLoadListener);
        loader.addLoadListener(newLoadListener);
    }
   
    @Override
    protected void onDelete() {
       /* DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        SKeyForColumn keyStatus = new SKeyForColumn("MAIN:ID_STATUS");
        Integer idStatus = (Integer) domain.getRows().get(keyStatus).getVal();
        if (idStatus != 0) {
            MessageBox.alert("Внимание!", "Данную заявку удалять нельзя.", null);
            return;
        }
        super.onDelete();*/
        
        MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    isBtn = true;
                    final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

                    //Определение индекса текущей строки
                    List<BeanModel> models = grid.getStore().getModels();
                    if (grid.getSelectionModel() != null) {
                        for (BeanModel bm : models) {
                            DDataGrid dg = bm.getBean();
                            if (dg.isDomainEquals(domain)) {
                                currentIndex = models.indexOf(bm);
                                break;
                            }
                        }
                    }

                    DDataGrid oldDomain = new DDataGrid();
                    oldDomain.copy(domain);

                    String format = "dd.MM.yyyy HH:mm";
                    Date date = DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(new Date()));

                    SKeyForColumn key;
                    if (tableName.equals("dt_elaboration_of_dd_hlv")) {
                        key = new SKeyForColumn("MAIN:DATE_DEL");
                        IRowColumnVal val = new DRowColumnValDate();
                        val.setVal(date);
                        domain.getRows().put(key, val);

                        key = new SKeyForColumn("MAIN:DELETED");
                        val = new DRowColumnValString();
                        val.setVal(ConfigurationManager.getWorkerName());
                        domain.getRows().put(key, val);


                        key = new SKeyForColumn("MAIN:IS_ACTUAL");
                        val = new DRowColumnValString();
                        val.setVal("0");
                        domain.getRows().put(key, val);
                    } else if (tableName.equals("app_prod_comment_hlv")) {
                        key = new SKeyForColumn("MAIN:DATE_DELETE");
                        IRowColumnVal val = new DRowColumnValDate();
                        val.setVal(date);
                        domain.getRows().put(key, val);
                    }


                    key = new SKeyForColumn("MAIN:DELETED_USER_ID");
                    DRowColumnValNumber valUserId = new DRowColumnValNumber();
                    valUserId.setVal(ConfigurationManager.getUserId());
                    domain.getRows().put(key, valUserId);


                    AsyncCallback<DDataGrid> callback = new AsyncCallback<DDataGrid>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(DDataGrid result) {
                            loader.load();
                        }
                    };
                    getService().save(tableName, domain, oldDomain, false, callback);
                }
            }
        });

        
    }
   
}
