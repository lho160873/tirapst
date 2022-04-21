package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

/**
 *
 * @author n
 */
public class DataGridEditWindowWorkPlanExecutor extends DataGridEditWindow {
    
    private ListStore<Executor> store = new ListStore<Executor>();
    private Map<Integer, Boolean> workers = new HashMap<Integer, Boolean>();
    private ContentPanel contentPanel;
    private boolean isNotChanged = true;
    private Button delButton;
    private Button addButton;
    
    public DataGridEditWindowWorkPlanExecutor(DDataGrid domain, EditState state, EWindowType windowType) {
        super(domain, state, windowType);
    }
    
    public DataGridEditWindowWorkPlanExecutor(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
        initPanelSub();
    }
    
    public DataGridEditWindowWorkPlanExecutor(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
        initPanelSub();
    }
    
    public DataGridEditWindowWorkPlanExecutor(DDataGrid domain, DTable table, EditState state, EWindowType windowType, List<DCondition> conditions, Date planBegDate, Date planEndDate) {
        super(domain, table, state, windowType);
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormWorkPlanExecutor(table, state, conditions, planBegDate, planEndDate);
        registerEditForm(pnlEdit);
        initPanelSub();
    }
    
    public DataGridEditWindowWorkPlanExecutor(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
    }
    
    protected class Executor extends BeanModel {
        
        public Executor(Integer id, String name) {
            set("id", id);
            set("name", name);
        }
    }
    
    protected final void initPanelSub() {
        BorderLayoutData panelDataSub = new BorderLayoutData(Style.LayoutRegion.SOUTH, 250);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        
        final String tabName = "WP_EXECUTORS";
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        
        SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        DRowColumnValNumber vv = new DRowColumnValNumber();
        vv.setVal((Number) domain.getRows().get(key).getFormatVal(key, table.getColumnBuilder(key)));
        cnd.getFilters().put(key, vv);
        
        store.addListener(Events.Clear, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (delButton != null) {
                    delButton.setEnabled(false);
                }
            }
        });
        
        store.addListener(Events.Add, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (delButton != null) {
                    delButton.setEnabled(true);
                }
            }
        });
        
        
        contentPanel = new ContentPanel();
        contentPanel.setBodyBorder(true);
        contentPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);
        contentPanel.setLayout(new FitLayout());
        contentPanel.setHeading("Исполнители");
        
        if (state != EditState.NEW) {
            contentPanel.mask();
            ((GWTDDataGridServiceAsync) getService()).getDataGrid(tabName, cnd, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                    contentPanel.unmask();
                }
                
                @Override
                public void onSuccess(final List<DDataGrid> result) {
                    for (DDataGrid d : result) {
                        SKeyForColumn key2 = new SKeyForColumn("WORKER:NAME");
                        String name = (String) d.getRows().get(key2).getVal();
                        
                        SKeyForColumn key3 = new SKeyForColumn("MAIN:WORKER_ID");
                        int workerId = (Integer) d.getRows().get(key3).getVal();
                        workers.put(workerId, false);
                        
                        Executor newExecutor = new Executor(workerId, name);
                        store.add(newExecutor);
                        if (delButton != null) {
                            delButton.setEnabled(true);
                        }
                    }
                    contentPanel.unmask();
                }
            });
        }
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        
        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("Идентификатор");
        column.setWidth(200);
        column.setRowHeader(true);
        column.setHidden(true);
        configs.add(column);
        
        column = new ColumnConfig();
        column.setId("name");
        column.setHeader("ФИО");
        column.setWidth(100);
        configs.add(column);
        
        ColumnModel cm = new ColumnModel(configs);
        
        final Grid<Executor> grid = new Grid<Executor>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("name");
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setColumnReordering(true);
        contentPanel.add(grid);
        
        ToolBar mainToolBar = new ToolBar();
        
        final DataGridWindow page;
        page = new DataGridWindow("WORKER_FOR_EXECUTOR", true, true);
        
        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                String id = be.getButton().getItemId();
                if (id.equals("open")) {
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {
                            page.hide();
                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            SKeyForColumn idKey = new SKeyForColumn("MAIN:WORKER_ID");
                            SKeyForColumn nameKey = new SKeyForColumn("MAIN:NAME");
                            Integer workerId = (Integer) rows.get(idKey).getFormatVal(idKey, table.getColumnBuilder(idKey));
                            String workerName = (String) rows.get(nameKey).getFormatVal(nameKey, table.getColumnBuilder(nameKey));
                            Executor newExecutor = new Executor(workerId, workerName);
                            
                            boolean isNew = true;
                            
                            for (Executor executor : store.getModels()) {
                                if (executor.get("id").equals(newExecutor.get("id"))) {
                                    isNew = false;
                                    break;
                                }
                            }
                            
                            if (isNew) {
                                store.add(newExecutor);
                                StoreEvent e = new StoreEvent(store);
                                store.fireEvent(Events.Add, e);
                                isNotChanged = false;
                            }
                            page.removeDataGridListener(this);
                        }
                    };
                    page.addDataGridListener(listenerGetDomain);
                    page.show();
                }
                if (id.equals("delete")) {
                    Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                                store.remove(grid.getSelectionModel().getSelectedItem());
                                isNotChanged = false;
                                if (store.getCount() < 1) {
                                    StoreEvent e = new StoreEvent(store);
                                    store.fireEvent(Events.Clear, e);
                                }
                            }
                        }
                    };
                    if (grid.getSelectionModel().getSelectedItem() != null) {
                        MessageBox.confirm(constants.confirm(), "Удалить исполнителя?", l);
                    }
                }
            }
        };
        
        delButton = new Button(constants.delete(), AbstractImagePrototype.create(images.delete()), buttonEventListener);
        delButton.setItemId("delete");
        delButton.setEnabled(false);
        addButton = new Button(constants.add(), AbstractImagePrototype.create(images.add()), buttonEventListener);
        addButton.setItemId("open");
        mainToolBar.add(addButton);
        mainToolBar.add(delButton);
        contentPanel.setTopComponent(mainToolBar);
        
        main.add(contentPanel, panelDataSub);
        
    }
    
    @Override
    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        contentPanel.mask();
        if (domain == null) {
            domain = createDomain();
        }
        
        final DDataGrid oldDomain = domain.newInstance();
        oldDomain.copy(domain);
        
        editForms.fillDomain(getDomain());
//        for (Map.Entry key : getDomain().getRows().entrySet()) {
//            SKeyForColumn keyS = (SKeyForColumn) key.getKey();
//            if (table.getColumnBuilder(keyS) instanceof DColumnBuilderDate) {
//                if (table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyDateField
//                        || table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyMonthYearField) {
//                    Date dd = (Date) getDomain().getRows().get(keyS).getVal();
//                   
//                }
//            }
//        }

        String message = checkBegEndDates();
        if (message != null) {
            getEditPanel().unmask();
            contentPanel.unmask();
            MessageBox.alert(commonConstants.error(), message, null);
        } else {
            ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, (state == EditState.NEW), new AsyncCallback<DDataGrid>() {
                @Override
                public void onFailure(Throwable thrwbl) {
                    getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                    getEditPanel().unmask();
                    contentPanel.unmask();
                    AppHelper.showMsgRpcServiceError(thrwbl);
                }
                
                @Override
                public void onSuccess(final DDataGrid t) {
                    if (t == null) {
                        onFailure(null);
                    } else {
                        List<DDataGrid> domainsForInsert = new ArrayList<DDataGrid>();
                        final List<DDataGrid> domainsForDelete = new ArrayList<DDataGrid>();
                        Integer workPlanExecutorId = (Integer) t.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal();
                        for (Executor el : store.getModels()) {
                            Integer workerId = (Integer) el.get("id");
                            if (workers.containsKey(workerId)) {
                                workers.put(workerId, true);
                            } else {
                                DDataGrid data = new DDataGrid();
                                data.setName("WP_EXECUTORS");
                                
                                SKeyForColumn exId = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
                                DRowColumnValNumber valExId = new DRowColumnValNumber();
                                valExId.setVal(workPlanExecutorId);
                                data.getRows().put(exId, valExId);
                                
                                SKeyForColumn workerIdKey = new SKeyForColumn("MAIN:WORKER_ID");
                                DRowColumnValNumber valWorkerId = new DRowColumnValNumber();
                                valWorkerId.setVal(workerId);
                                data.getRows().put(workerIdKey, valWorkerId);
                                domainsForInsert.add(data);
                            }
                        }
                        
                        for (Integer workerId : workers.keySet()) {
                            if (!workers.get(workerId)) {
                                DDataGrid data = new DDataGrid();
                                data.setName("WP_EXECUTORS");
                                
                                SKeyForColumn exId = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
                                DRowColumnValNumber valExId = new DRowColumnValNumber();
                                valExId.setVal(workPlanExecutorId);
                                data.getRows().put(exId, valExId);
                                
                                SKeyForColumn workerIdKey = new SKeyForColumn("MAIN:WORKER_ID");
                                DRowColumnValNumber valWorkerId = new DRowColumnValNumber();
                                valWorkerId.setVal(workerId);
                                data.getRows().put(workerIdKey, valWorkerId);
                                domainsForDelete.add(data);
                            }
                        }
                        
                        if (domainsForDelete.isEmpty() && domainsForInsert.isEmpty()) {
                            executeAfterSave(actionListener, t);
                        }
                        
                        if (!domainsForInsert.isEmpty()) {
                            ((GWTDDataGridServiceAsync) getService()).insert("WP_EXECUTORS", domainsForInsert, new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    AppHelper.showMsgRpcServiceError(caught);
                                }
                                
                                @Override
                                public void onSuccess(Boolean result) {
                                    
                                    if (!domainsForDelete.isEmpty()) {
                                        ((GWTDDataGridServiceAsync) getService()).deleteDomains("WP_EXECUTORS", domainsForDelete, new AsyncCallback<Boolean>() {
                                            @Override
                                            public void onFailure(Throwable caught) {
                                                AppHelper.showMsgRpcServiceError(caught);
                                            }
                                            
                                            @Override
                                            public void onSuccess(Boolean result) {
                                                executeAfterSave(actionListener, t);
                                            }
                                        });
                                    } else {
                                        executeAfterSave(actionListener, t);
                                    }
                                }
                            });
                        } else {
                            if (!domainsForDelete.isEmpty()) {
                                ((GWTDDataGridServiceAsync) getService()).deleteDomains("WP_EXECUTORS", domainsForDelete, new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        AppHelper.showMsgRpcServiceError(caught);
                                    }
                                    
                                    @Override
                                    public void onSuccess(Boolean result) {
                                        executeAfterSave(actionListener, t);
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
    
    private void executeAfterSave(final Listener actionListener, DDataGrid t) {
        
        getDomain().copy(t);
        updateFormsFromDomain();
        notifyDomainSaveSuccesed();
        getEditPanel().unmask();
        contentPanel.unmask();
        setHeading(getHeaderViewEdit());
        if (state.equals(EditState.NEW)) {
            state = EditState.EDIT;
            updateControlsVisibility();
        }
        if (actionListener != null) {
            actionListener.handleEvent(new BaseEvent(Save));
        }
    }
    
    @Override
    protected void closeWindow() {
        checkUnsavedChanged(new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeAllListeners();
                hide();
                if (wnd != null) {
                    wnd.removeAllListeners();
                    wnd.hide();
                }
                notifyWindowHide();
            }
        });
    }
    
    @Override
    protected void checkUnsavedChanged(final Listener actionListener) {
        if (state.equals(EditState.VIEW)) {
            actionListener.handleEvent(null);
        } else {
            if (!hasUnsavedChanges() && isNotChanged) {
                actionListener.handleEvent(null);
            } else {
                MessageBox box = new MessageBox();
                box.setButtons(MessageBox.YESNOCANCEL);
                box.setIcon(MessageBox.QUESTION);
                box.setTitle(constants.save());
                box.addCallback(new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(final MessageBoxEvent be) {
                        if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                            if (isValid()) {
                                save(actionListener);
                            }
                        } else if (be.getButtonClicked().getItemId().equals(Dialog.NO)) {
                            actionListener.handleEvent(be);
                        }
                    }
                });
                box.setMessage(constants.saveChanges());
                box.show();
            }
        }
    }
}
