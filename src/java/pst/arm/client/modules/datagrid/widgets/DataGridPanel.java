package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.grid.GridTools;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.expansion.*;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridPanel extends DataBasePanel {

    protected SelectionChangedListener gridListener;
    protected Listener<GridEvent> doubleClickListener;
    protected SelectionListener<ButtonEvent> tbListener;
    protected Listener<DataGridEvent> subFiltersListener;
    protected SRelationInfo subRalationInfo = null; //если форму открыть в режиме отображения подчиненных данных(isOpenAsSub == true) в этом поле будет храниться информация о связях
    protected DDataGrid domainForSub = null;  //данные переданные из основой таблицы в режиме отображения подчиненных данных(isOpenAsSub == true)
    protected Grid grid;
    protected ListStore store;
//    protected ListStore store2;
    protected PagingLoader loader;
//    protected PagingLoader loader2;
    protected PagingToolBar tbPaging;
    protected BeanModelReader reader;
    protected Integer selectedRow = null;
    protected Integer currentRow = null;
    protected DDataGrid currentDomain = null;
    protected DomainSaveSuccesedListener<DDataGrid> saveDataGridListener;
    protected ContentPanel panelGrid; //панель с центральной таблицей
    protected TabPanel tabPanelSub; //панель с закладками для отображения подчиненных данных (дополнительная информация)
    protected HashMap<String, DataBasePanel> subDataGridPanels; //массив панелей(таблиц) с подчиненными данными
    protected HashMap<String, TabItem> subTabItems; //массив панелей(таблиц) с подчиненными данными
    protected Menu subMenu; //меню для выбора подчиненных данных
    protected BorderLayoutData panelDataSub;
    protected Boolean isOpenAsSub = false;
    protected DCondition additionalCondition = null;
    protected List<DCondition> conditions;
    protected LoadListener mainLoadListener; 

    public List<DCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DCondition> conditions) {
        this.conditions = conditions;
    }
    
    public DDataGrid getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(DDataGrid currentDomain) {
        this.currentDomain = currentDomain;
    }

    public HashMap<String, DataBasePanel> getSubDataGridPanels() {
        return subDataGridPanels;
    }

    public void setSubDataGridPanels(HashMap<String, DataBasePanel> subDataGridPanels) {
        this.subDataGridPanels = subDataGridPanels;
    }

    public DCondition getAdditionalCondition() {
        return additionalCondition;
    }

    public void setAdditionalCondition(DCondition additionalCondition) {
        this.additionalCondition = additionalCondition;
    }

    public DataGridPanel(DTable table, String tn, DCondition cnd) {
        this(table, tn);
        this.additionalCondition = cnd;
    }
    
    public DataGridPanel(DTable table, String tn, List<DCondition> conditions) {
        this(table, tn);
        this.conditions = conditions;

        for (DCondition con : conditions) {
            DRowColumnValString val = new DRowColumnValString();
            val.setVal(con.getVal());
            this.condition.getFilters().put(con.getKey(), val);
        }
    }

    public DataGridPanel(DTable table, String tn) {
        setTableName(tn);
        
        this.setBorders(false);
        this.setBodyBorder(false);
        this.setHeaderVisible(true);

        setLayout(new FitLayout());

        condition = new DataGridSearchCondition();
        //condition.setLimit(Integer.parseInt(datagridConstants.recordCount()));

        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        panelGrid = new ContentPanel();
        panelGrid.setHeaderVisible(false);
        panelGrid.setBorders(false);
        panelGrid.setLayout(new FitLayout());
        BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        main.add(panelGrid, resultPanelData);

        initDefaultListeners();
        initListeners();   
        initStore();
        initPagingToolBar();

        toolBar = createToolBar(); //создаем панель с кнопками управления
        updateToolBar();
        panelGrid.setTopComponent(toolBar);

        initPanelFiltr();
        
        
        initTabPanelSub();

        if (table == null) {
            initTableInfo();
        } else {
            this.table = table;
            //initPagingToolBar();
            setRecordCount();
            putGridSearches();
            initGrid();
            createMenuSub();
            createAllSub();
            initIsFiltrShow();
            initIsHelpManualShow();
            panelDataSub.setSize(table.getSubSize());
            fireDataGridEventLoad(DataGridEvents.DataGridLoad); //посылаем сигнал о том что данные были получены
        }

        add(main);
        getAndSetColumnsVisibility();
    }

    public DataGridPanel(String tableName) {
        this(null, tableName);
    }

    //конструктор класса который вызывается при создании таблицы как подчиненной како-то другой
    public DataGridPanel(String tableName, SRelationInfo relationInfo) {
        //передаем информацию о связи с основной таблицей и создаем слушателя событий происходящих в основой таблице (изменение теущей строки и может быть еще что-то)        
        this(null, tableName);
        createAsSub(relationInfo);
    }

    //конструктор класса который вызывается при создании таблицы как подчиненной како-то другой
    public DataGridPanel(DTable table, String tableName, SRelationInfo relationInfo) {
        //передаем информацию о связи с основной таблицей и создаем слушателя событий происходящих в основой таблице (изменение теущей строки и может быть еще что-то)        
        this(table, tableName);
        createAsSub(relationInfo);
    }

    protected void updateTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                table = result;
                putGridSearches();
                reloadGrid();
                initIsFiltrShow();
                initIsHelpManualShow();
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        getService().getTable(tableName, callback_getTable);
    }

    protected void initTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                table = result;

                if (table.getIsViewOnly()) {
                    setMode(EditMode.VIEWONLY);
                    enabledBtn();
                }
                //initPagingToolBar();
                setRecordCount();
                putGridSearches();
                initGrid();
                createMenuSub();
                createAllSub();
                initIsFiltrShow();
                initIsHelpManualShow();
                fireDataGridEventLoad(DataGridEvents.DataGridLoad); //посылаем сигнал о том что данные были получены
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        getService().getTable(tableName, callback_getTable);
    }

    protected ArrayList convertDomainToListOfHashMaps(List<DDataGrid> domains, DTable table) {
        ArrayList data = new ArrayList();
        //PagingLoadConfig config = new BasePagingLoadConfig();
        //Map<String, Object> state = grid.getState(); //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
        for (DDataGrid domain : domains) {
            Map map = new HashMap();
            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                IColumnBuilder builder = table.getColumnBuilder(key);
                Boolean isVisibleInTable = builder.getColumn(key).getIsVisible();
                //смотрим не убирал ли пользователь колонку из таблицы или может быть какую-нибудь добавил
                //убрала это условия для отчета в журнале "Заявки проработки КД на возможность изготовления"(DH_ELABORATION_OF_DD_HLV)
                if (!table.getQueryName().equals("DH_ELABORATION_OF_DD_HLV")&&!table.getQueryName().equals("APP_PRODUCTION_HLV")) {
                    //String keyForState = "hidden" + key.getTableAlias() + ":" + key.getColumnName();//hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
                    String keyForState_1 = key.getTableAlias() + ":" + key.getColumnName();
                   //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (новое)
                    if (grid.getColumnModel().getColumnById(keyForState_1) != null) {
                        if (grid.getColumnModel().getColumnById(keyForState_1).isHidden()) 
                        {
                            isVisibleInTable = false;
                        } else {
                            isVisibleInTable = true;
                        }
                    }
                    //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
                    /*if (state.containsKey(keyForState)) {
                        //MessageBox.info("",keyForState,null);
                        if (state.get(keyForState).toString().equals("true")) {
                            isVisibleInTable = false;
                        }
                        if (state.get(keyForState).toString().equals("false")) {
                            isVisibleInTable = true;
                        }
                    }*/
                }                
                String valStr;
                //if (isVisibleInTable || key.equals(new SKeyForColumn("MAIN:URGENCY_CODE2")) || key.equals(new SKeyForColumn("MAIN:STATUS_TIME"))) {
                if (isVisibleInTable) {
                    valStr = ((IRowColumnVal) entry.getValue()).getStringFromVal(domain, key, table.getColumnBuilder(key));
                    if (valStr == null) {
                        valStr = "";
                    }
                    map.put(valKey, valStr);
                }
            }
            data.add(map);
        }
        return data;
    }

    @Override
    protected void showReport(final String reportType) {
        if (table.getReportTemplate() == null) {
            final Component cmp = this;

            getService().getDataGrid(tableName, condition, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(List<DDataGrid> result) {
                    getReportService().generateHtmlArchiveStoreEntityReport(tableName, convertDomainToListOfHashMaps(result, table), reportType, new FileReportServiceCallback(cmp));
                }
            });


        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (table.getReportParams() != null) {
                for (Map.Entry<String, String> tp : table.getReportParams().entrySet()) {
                    for (Map.Entry filtr : condition.getFilters().entrySet()) {
                        SKeyForColumn fKey = (SKeyForColumn) filtr.getKey();
                        IRowColumnVal fVal = (IRowColumnVal) filtr.getValue();
                        String fStr = fVal.getStringFromVal(fKey, table.getColumnBuilder(fKey));
                        if (fKey.equals(new SKeyForColumn(tp.getValue()))) {
                            if (fStr == null) {
                                params.put(tp.getKey(), "");
                            } else {
                                params.put(tp.getKey(), fStr);
                            }
                        }
                    }
                }
            }

            getReportService().generateJasperReport(table.getReportTemplate(), reportType, params, condition, table, new FileReportServiceCallback(this));
        }

    }

    //инициализация слушателя событий 
    //hlv comment(в первую колонку добавляем стрелку активности строки, используется в DomainEditWindow при определении номера активной строки)
    protected void initDefaultListeners() {
        gridListener = new SelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent se) {
                /*int pointerColumnIndex = grid.getColumnModel().getIndexById("");
                 if (pointerColumnIndex != -1) {
                 for (int i = 0; i < grid.getStore().getCount(); i++) {
                 grid.getView().getCell(i, pointerColumnIndex).setInnerHTML("");
                 }
                 if (se.getSelection().size() == 1) {
                 int pointerIndex = grid.getStore().indexOf(se.getSelectedItem());
                 grid.getView().getCell(pointerIndex, pointerColumnIndex).setInnerHTML("<div class='selected-row'></div>");
                 }
                 }*/

                if (selectedRow != null && selectedRow >= 0 && grid.getView() != null && grid.getView().getCell(selectedRow, 0) != null) {
                    grid.getView().getCell(selectedRow, 0).setInnerHTML("");                    
                }
                if (grid.getSelectionModel() != null) {
                    selectedRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                    if (grid.getSelectionModel().getSelectedItem() != null) {
                        currentDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
                    }
                }

                if (selectedRow >= 0 && grid.getView() != null && grid.getView().getCell(selectedRow, 0) != null) {
                    grid.getView().getCell(selectedRow, 0).setInnerHTML("<div class='selected-row'></div>");                    
                    grid.getView().focusCell(selectedRow, 0,true); //+hlv                     
                }
                gridSelectionChanged();
            }
        };

        doubleClickListener = new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                gridRowDoubleClick(be);
            }
        };
    }

    protected void initTabPanelSub() {
        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        tabPanelSub.addListener(Events.Remove, new Listener<TabPanelEvent>() {
            @Override
            public void handleEvent(TabPanelEvent be) {
                TabItem item = be.getItem();
                if (item != null) {
                    //если нет ни одной new BorderLayoutDataоткрытой подчиненной таблицы прячем всю панель предназанченную для подчиненных данных
                    if (tabPanelSub.getItemCount() == 0) {
                        tabPanelSub.hide();
                    }
                }
            }
        });
        subMenu = new Menu();
        btnSub.setMenu(subMenu);
        subDataGridPanels = new HashMap<String, DataBasePanel>(); //массив панелей с подчиненными данными
        subTabItems = new HashMap<String, TabItem>();
        tabPanelSub.hide(); //изначально панель с подчиненными данными не показываем
        panelDataSub = new BorderLayoutData(LayoutRegion.SOUTH, 0.5f);
        panelDataSub.setMaxSize(1200);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        main.add(tabPanelSub, panelDataSub);
    }

    protected void setAllSubDomain(DDataGrid domain) {

        //Данные передаем всегда
        for (SRelationInfo subTable : table.getSubTables()) {
            String tabName = subTable.getQueryName();

            if (subDataGridPanels.containsKey(tabName)) {
                if (tabName.equals("SERV_CONTRACT_STAGE_VO_FOR_OCP")) {
                    DataGridPanelOCP panelSub = (DataGridPanelOCP) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_VO")) {
                    DataGridPanelWorkPlan panelSub = (DataGridPanelWorkPlan) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("OCP_TRIP_VO")) {
                    DataGridPanelTrip panelSub = (DataGridPanelTrip) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DEPART_EXECUTOR_PLAN_VO")) {
                    DataGridPanelWD panelSub = (DataGridPanelWD) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("CONTRACTS_VO_DOP")) {
                    DataGridPanelContractDop panelSub = (DataGridPanelContractDop) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("HLV_USER_DEPART")) {
                    DataGridPanelUserDepart panelSub = (DataGridPanelUserDepart) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_OCP_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_IG")) {
                    DataGridPanelDocFileIg panelSub = (DataGridPanelDocFileIg) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_DH_ELABORATION_OF_DD_HLV") || tabName.equals("DOC_FILE_APP_PRODUCTION_HLV")  ) {
                    DataGridPanelDocFileElaboration panelSub = (DataGridPanelDocFileElaboration) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if ( tabName.equals("DOC_FILE_LEVEL_TASK_HLV") ) {
                    DataGridPanelDocFileLevelTask panelSub = (DataGridPanelDocFileLevelTask) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("OFFICE_DOC_CONTR_VO")) {
                    DataGridPanelOfficeDocContrVO panelSub = (DataGridPanelOfficeDocContrVO) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_EXEC_PERIOD_FULL_VO")) {
                    DataGridPanelExecPeriod panelSub = (DataGridPanelExecPeriod) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("POST_WORKER_N_VO")) {
                    DataGridPanelPostWorkerN panelSub = (DataGridPanelPostWorkerN) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("PLAN_NTO_YEAR_VO")) {
                    DataGridPanelPlanNTOYear panelSub = (DataGridPanelPlanNTOYear) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                }  else if (tabName.equals("ORDER_BLANK_WORK")) {
                    DataGridPanelOrderBlankWork panelSub = (DataGridPanelOrderBlankWork) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                }  else if (tabName.equals("ORDER_BLANK_WORK2")) {
                    DataGridPanelOrderBlankWork2 panelSub = (DataGridPanelOrderBlankWork2) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK3")) {
                    DataGridPanelOrderBlankWork3 panelSub = (DataGridPanelOrderBlankWork3) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK4")) {
                    DataGridPanelOrderBlankWork4 panelSub = (DataGridPanelOrderBlankWork4) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_EXECUTOR")) {
                    DataGridPanelWorkPlanExecutor panelSub = (DataGridPanelWorkPlanExecutor) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_EXECUTOR_NEW")) {
                    DataGridPanelWorkPlanExecutorNew panelSub = (DataGridPanelWorkPlanExecutorNew) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WP_EXECUTORS")) {
                    DataGridPanelExecutors panelSub = (DataGridPanelExecutors) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WP_EXECUTORS_NEW")) {
                    DataGridPanelExecutorsNew panelSub = (DataGridPanelExecutorsNew) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                }
//                else if (tabName.equals("OFFICE_DOC_EXEC_IG")) {
//                    DataGridPanelOfficeDocContrIgAction panelSub = (DataGridPanelOfficeDocContrIgAction) (subDataGridPanels.get(tabName));
//                    panelSub.putGridSubFilters(domain);
//                } 
                else {
                    DataGridPanel panelSub = (DataGridPanel) subDataGridPanels.get(tabName);
                    panelSub.putGridSubFilters(domain);
                }
            }
        }

        //А перечитывать будем только если панель с подчиненными данными видна (открыта)
        if (!tabPanelSub.isVisible()) {
            return;
        }
        for (SRelationInfo subTable : table.getSubTables()) {
            final String tabName = subTable.getQueryName();
            if (!subDataGridPanels.containsKey(tabName) || !subTabItems.containsKey(tabName)) {
                return;
            }

            //нужная нам страничка нашлась
            if (tabPanelSub.findItem(tabName, true) != null) {
                if (tabName.equals("SERV_CONTRACT_STAGE_VO_FOR_OCP")) {
                    DataGridPanelOCP panelSub = (DataGridPanelOCP) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_VO")) {
                    DataGridPanelWorkPlan panelSub = (DataGridPanelWorkPlan) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DEPART_EXECUTOR_PLAN_VO")) {
                    DataGridPanelWD panelSub = (DataGridPanelWD) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("CONTRACTS_VO_DOP")) {
                    DataGridPanelContractDop panelSub = (DataGridPanelContractDop) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("OCP_TRIP_VO")) {
                    DataGridPanelTrip panelSub = (DataGridPanelTrip) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("HLV_USER_DEPART")) {
                    DataGridPanelUserDepart panelSub = (DataGridPanelUserDepart) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_OCP_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_IG")) {
                    DataGridPanelDocFileIg panelSub = (DataGridPanelDocFileIg) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_DH_ELABORATION_OF_DD_HLV")|| tabName.equals("DOC_FILE_APP_PRODUCTION_HLV") ) {
                    DataGridPanelDocFileElaboration panelSub = (DataGridPanelDocFileElaboration) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                }  else if ( tabName.equals("DOC_FILE_LEVEL_TASK_HLV") ) {
                    DataGridPanelDocFileLevelTask panelSub = (DataGridPanelDocFileLevelTask) (subDataGridPanels.get(tabName));
                    panelSub.reloadGrid();
                } else if (tabName.equals("OFFICE_DOC_CONTR_VO")) {
                    DataGridPanelOfficeDocContrVO panelSub = (DataGridPanelOfficeDocContrVO) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_EXEC_PERIOD_FULL_VO")) {
                    DataGridPanelExecPeriod panelSub = (DataGridPanelExecPeriod) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("POST_WORKER_N_VO")) {
                    DataGridPanelPostWorkerN panelSub = (DataGridPanelPostWorkerN) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("PLAN_NTO_YEAR_VO")) {
                    DataGridPanelPlanNTOYear panelSub = (DataGridPanelPlanNTOYear) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK")) {
                    DataGridPanelOrderBlankWork panelSub = (DataGridPanelOrderBlankWork) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK2")) {
                    DataGridPanelOrderBlankWork2 panelSub = (DataGridPanelOrderBlankWork2) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK3")) {
                    DataGridPanelOrderBlankWork3 panelSub = (DataGridPanelOrderBlankWork3) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK4")) {
                    DataGridPanelOrderBlankWork4 panelSub = (DataGridPanelOrderBlankWork4) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK5")) {
                    DataGridPanelOrderBlankWork5 panelSub = (DataGridPanelOrderBlankWork5) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_EXECUTOR")) {
                    DataGridPanelWorkPlanExecutor panelSub = (DataGridPanelWorkPlanExecutor) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_EXECUTOR_NEW")) {
                    DataGridPanelWorkPlanExecutorNew panelSub = (DataGridPanelWorkPlanExecutorNew) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WP_EXECUTORS")) {
                    DataGridPanelExecutors panelSub = (DataGridPanelExecutors) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WP_EXECUTORS_NEW")) {
                    DataGridPanelExecutorsNew panelSub = (DataGridPanelExecutorsNew) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                }
//                else if (tabName.equals("OFFICE_DOC_EXEC_IG")) {
//                    DataGridPanelOfficeDocContrIgAction panelSub = (DataGridPanelOfficeDocContrIgAction) subDataGridPanels.get(tabName);
//                    panelSub.reloadGrid();
//                } 
                else {
                    DataGridPanel panelSub = ((DataGridPanel) subDataGridPanels.get(tabName));
                    panelSub.reloadGrid();
                }
            }
        }
    }

    //создаем все необходимые подчиненные таблицы
    protected void createAllSub() {
        //надо ли показывать сразу
        if (table.getIsSubShow()) {
            //panelDataSub.setSize(main.getHeight() / 2); //????????????????????????????????
            tabPanelSub.show();
        }

        for (SRelationInfo subTable : table.getSubTables()) {
            final String tabName = subTable.getQueryName();
            final String caption = subTable.getCaption();
            if (!subDataGridPanels.containsKey(tabName)) {
                DataBasePanel panelSub = DataGridPanelBuilder.createDataGridPanel(tabName, subTable);
                TabItem item = new TabItem();
                item.setClosable(true);
                item.setBorders(false);
                item.setItemId(tabName);
                item.setText(caption);
                item.setLayout(new FitLayout());
                item.add(panelSub);

                item.addListener(Events.Close, new Listener<TabPanelEvent>() {
                    @Override
                    public void handleEvent(TabPanelEvent be) {
                        TabItem item = be.getItem();
                        if (item != null) {
                            ((CheckMenuItem) subMenu.getItemByItemId(tabName)).setChecked(false);
                        }
                    }
                });
                
                subDataGridPanels.put(tabName, panelSub); //добавляем в массив созданных подчиненных таблиц
                subTabItems.put(tabName, item);

                if (table.getIsSubShow()) {
                    tabPanelSub.add(subTabItems.get(tabName));
                    ((CheckMenuItem) subMenu.getItemByItemId(tabName)).setChecked(true);
                }
            }
        }
        main.layout(true);
    }

    protected void createMenuSub() {
        Integer i = 0;
        btnReport.setVisible(false);  //не используемая теперь кнопка для одного типа отчета
        btnReportSplit.setVisible(table.getIsShowReport());
        sprReport.setVisible(table.getIsShowReport());
        //menuItemReport.setVisible(table.getIsShowReport());
        //sprMenuItemReport.setVisible(table.getIsShowReport());
        btnSub.setVisible(table.getSubTables().size() > 0);
        sprSub.setVisible(table.getSubTables().size() > 0);
        for (SRelationInfo subTable : table.getSubTables()) {
            String tabName = subTable.getQueryName();
            String caption = subTable.getCaption();
            CheckMenuItem editItem = new CheckMenuItem(caption);
            editItem.setId(tabName);
            editItem.addSelectionListener(new SelectionListener<MenuEvent>() {
                @Override
                public void componentSelected(MenuEvent ce) {
                    Integer ind = ce.getMenu().indexOf(ce.getItem());
                    String tabName = ce.getItem().getId();
                    String caption = ((CheckMenuItem) ce.getItem()).getText();

                    showSub(ind, tabName, caption, ((CheckMenuItem) ce.getItem()).isChecked());
                }
            });
            subMenu.insert(editItem, i);
            i++;
        }
        
        if (table.getSubTables().size() <= 0) {
            toolBar.remove(btnSub);
            toolBar.remove(sprSub);
        }
        else {
            if (toolBar.indexOf(btnSub) == -1) {
                toolBar.add(btnSub);
            }
            if (toolBar.indexOf(sprSub) == -1) {
                toolBar.add(sprSub);
            }
        }
    }

    //открываем таблицу с подчиненными данными
    public void showSub(Integer ind, final String tabName, final String caption, Boolean isShow) {
        if (!subDataGridPanels.containsKey(tabName) || !subTabItems.containsKey(tabName)) {
            return;
        }

        //открываем подчиненную таблицу первый раз
        if (isShow) {
            subDataGridPanels.get(tabName).clearStore();//перед показом данные очищаем, что бы не видеть предыдущие данные
            //открываем панель для подчиненныех данных
            if (!tabPanelSub.isVisible()) {
                panelDataSub.setSize(table.getSubSize());
                tabPanelSub.show();
            }
            //добавляем закладку с подчиненной таблицей
            tabPanelSub.add(subTabItems.get(tabName));
            //делаем открытую закладку текущей
            tabPanelSub.setSelection(subTabItems.get(tabName));

        } else {
            tabPanelSub.setSelection(subTabItems.get(tabName));
            tabPanelSub.remove(subTabItems.get(tabName));
        }

        //если нет ни одной открытой подчиненной таблицы прячем всю панель предназанченную для подчиненных данных
        if (tabPanelSub.getItemCount() == 0) {
            tabPanelSub.hide();
        }
        main.layout(true);
    }

    public void putGridSubFilters(DDataGrid domain) {
        if (!isOpenAsSub) {
            return;
        }
        clearSelectionModel();
                
        HashMap<SKeyForColumn, IRowColumnVal> filters = condition.getFilters();
        filters.clear();

        domainForSub = domain;

        if (domain == null)
            return;

        //если переданы нулевые данные то будем показывать пустую таблицу (в запрос передадим null в условия WHERE
        HashMap< SKeyForColumn, SKeyForColumn> relations = subRalationInfo.getRelations();
        passedFields = new HashMap<SKeyForColumn, IRowColumnVal>();
        for (Map.Entry key : relations.entrySet()) {
            SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
            SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
            IRowColumnVal val = null;
            if (domainForSub != null) {
                val = domainForSub.getRows().get(key_1);
            }
            filters.put(key_2, val);
        }
        if (subRalationInfo.getPassedFieds() != null) {
            for (Map.Entry key : subRalationInfo.getPassedFieds().entrySet()) {
                SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
                SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
                IRowColumnVal val = null;
                if (domainForSub != null) {
                    val = domainForSub.getRows().get(key_1);
                }
                passedFields.put(key_2, val);
            }
        }
        getAndSetColumnsVisibility();
    }

    public Listener<DataGridEvent> getSubFiltersListener() {
        return subFiltersListener;
    }

    //передаем информацию о связи с основной таблицей и создаем слушателя событий происходящих в основой таблице (изменение теущей строки и может быть еще что-то)
    protected void createAsSub(SRelationInfo relationInfo) {
        isOpenAsSub = true;

        subRalationInfo = relationInfo;   //если форма открыта в режиме отображения подчиненных данных передаем ей информацию о связях
        subFiltersListener = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(DataGridEvent be) {
                DDataGrid domain = be.getDomain();
                putGridSubFilters(domain);
                reloadGrid();
            }
        };
        setHeaderVisible(false);
        setBorders(false);
    }

    protected void initPanelFiltr() {
        panelFiltr = new DataGridFiltr(this);
        panelFiltr.hide(); //изначально делаем не видимой
        //создаем слушателя события окончания загрузки данны
        this.addListener(DataGridEvents.DataGridLoad, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                panelFiltr.repaintAllComponents();
            }
        });

        requestPanelData = new BorderLayoutData(LayoutRegion.NORTH, 0.5f);//Integer.parseInt(datagridConstants.filtrpanelMinHeight()));
        requestPanelData.setMaxSize(1200);
        requestPanelData.setMargins(new Margins(0, 0, 1, 0));
        requestPanelData.setSplit(true);
        requestPanelData.setCollapsible(false); //возможность закрыть панель до заголовка
        panelFiltr.setStyleAttribute("padding", "0px");
        main.add(panelFiltr, requestPanelData);
    }

    protected void initPagingToolBar() {
        condition.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        tbPaging = new PagingToolBar(Integer.parseInt(datagridConstants.recordCount()));
        tbPaging.bind(loader);       
        panelGrid.setBottomComponent(tbPaging);
    }

    protected void setRecordCount() {
        condition.setLimit(table.getRecordCount());
        tbPaging.setPageSize(table.getRecordCount());
    }

    //инициализация хранилища данных для грида
    protected void initStore() {
        RpcProxy<PagingLoadResult<DDataGrid>> proxy = new RpcProxy<PagingLoadResult<DDataGrid>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<DDataGrid>> callback) {
                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
                condition.setOffset(config.getOffset());
                condition.setLimit(config.getLimit());
                condition.setSortFieldId(config.getSortField());
                DataGridSearchCondition.SortDir sortDir;
                switch (config.getSortDir()) {
                    case ASC:
                        sortDir = DataGridSearchCondition.SortDir.ASC;
                        break;

                    case DESC:
                        sortDir = DataGridSearchCondition.SortDir.DESC;
                        break;

                    default:
                        sortDir = DataGridSearchCondition.SortDir.NONE;
                }
                condition.setSortDir(sortDir);

                if (additionalCondition != null) {
                    DCondition a = new DCondition();

                    a.setKey(additionalCondition.getKey());
                    a.setVal(additionalCondition.getVal());
                    getService().getPage(tableName, condition, a, callback);
                } else {
                    getService().getPage(tableName, condition, callback);
                }
            }
        };

//        RpcProxy<PagingLoadResult<DDataGrid>> proxy2 = new RpcProxy<PagingLoadResult<DDataGrid>>() {
//            @Override
//            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<DDataGrid>> callback) {
//                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
//                condition.setOffset(config.getOffset());
//                condition.setLimit(config.getLimit());
//                condition.setSortFieldId(config.getSortField());
//                DataGridSearchCondition.SortDir sortDir;
//                switch (config.getSortDir()) {
//                    case ASC:
//                        sortDir = DataGridSearchCondition.SortDir.ASC;
//                        break;
//
//                    case DESC:
//                        sortDir = DataGridSearchCondition.SortDir.DESC;
//                        break;
//
//                    default:
//                        sortDir = DataGridSearchCondition.SortDir.NONE;
//                }
//                condition.setSortDir(sortDir);
//
//                if (additionalCondition != null) {
//                    DCondition a = new DCondition();
//
//                    a.setKey(additionalCondition.getKey());
//                    a.setVal(additionalCondition.getVal());
//                    getService().getPage(tableName, condition, a, callback);
//                } else {
//                    getService().getPage(tableName, condition, callback);
//                }
//            }
//        };

        BeanModelReader reader = new BeanModelReader();
//        BeanModelReader reader2 = new BeanModelReader();
        if (reader != null) {
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, reader);
            store = new ListStore<BeanModel>(loader);
        } else {
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
            store = new ListStore<ModelData>(loader);
        }
//        if (reader2 != null) {
//            loader2 = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy2, reader2);
//            store2 = new ListStore<BeanModel>(loader2);
//        } else {
//            loader2 = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy2);
//            store2 = new ListStore<ModelData>(loader2);
//        }
      mainLoadListener =  new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                panelGrid.unmask();
            }

            @Override
            public void handleEvent(LoadEvent le) {
                //loader.setSortField((String) grid.getState().get("sortField"));
                super.handleEvent(le);
                resizePanelFiltr();
                selectCurrentRow();
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
            }};
        loader.addLoadListener(mainLoadListener);
        
        loader.setRemoteSort(true);
//        loader2.setRemoteSort(true);
    }

    //инициализация грида
    protected void initGrid() {
        this.setHeading(table.getCaption());
        ColumnModel cm = createColumModel();
        grid = new Grid(store, cm);

        initGridPlagin();

        GridTools.enableGridMultiHeading(grid);
        GridTools.enableGridMultiColumn(grid);
        grid.setStateId(tableName);
        grid.setStateful(true);
        grid.clearState();
        grid.setLoadMask(true);
        grid.setContextMenu(menu);
        grid.setStripeRows(true);
        grid.setSelectionModel(new GridSelectionModel<BeanModel>());
        if (table.getIsMulti()) {
            grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        } else {
            grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
        grid.addListener(Events.Attach, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                reloadGrid();
                //updateTableInfo(); //hlv
            }
        });
        grid.getSelectionModel().addSelectionChangedListener(gridListener);
        grid.addListener(Events.RowDoubleClick, doubleClickListener);
        panelGrid.add(grid);
        panelGrid.layout(true);
        panelGrid.getBottomComponent().enable();
    }

    //очистка данных 
    @Override
    public void clearStore() {
        btnAdd.setEnabled(false);
        btnRefr.setEnabled(false);
        btnFiltr.setEnabled(false);
        menuItemAdd.setEnabled(false);
        menuItemRefr.setEnabled(false);
        store.removeAll();
//        store2.removeAll();
    }

    @Override
    public void clearSelectionModel() {
        if (grid == null) return;
       
        if (grid.getState() != null) {
            grid.getState().clear();
        }
        if (grid.getSelectionModel() != null) {
            grid.getSelectionModel().deselectAll();
        }
        setAllSubDomain(null);
    }

    @Override
    public void search() {
        //loader.load();
        reloadGrid();
    }

    //перезагрузка данных 
    @Override
    public void reloadGrid() {
        panelGrid.mask(datagridConstants.treeLoading());
        if (isOpenAsSub && domainForSub == null) {
            btnAdd.setEnabled(false);
            btnColumnsVisibility.setEnabled(false);
            btnRefr.setEnabled(false);
            btnFiltr.setEnabled(false);
            menuItemAdd.setEnabled(false);
            menuItemRefr.setEnabled(false);

            /*btnDelete.setEnabled(false);
             menuItemDelete.setEnabled(false);
             btnEdit.setEnabled(false);
             menuItemEdit.setEnabled(false);
             btnView.setEnabled(false);
             menuItemView.setEnabled(false);
             btnSub.setEnabled(false);
             btnSend.setEnabled(false);*/
            store.removeAll();
//            store2.removeAll();
            panelGrid.unmask();
            return;
        }
        if (isOpenAsSub && domainForSub != null) {
            btnAdd.setEnabled(true);
            btnColumnsVisibility.setEnabled(true);
            btnRefr.setEnabled(true);
            btnFiltr.setEnabled(true);
            menuItemAdd.setEnabled(true);
            menuItemRefr.setEnabled(true);
        }
        if (grid == null) {
            return;
        }
        PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        //config.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        config.setLimit(table.getRecordCount());
        
        config.setSortField("");
        if (grid.isViewReady()) {
            Map<String, Object> state = grid.getState();
            if (state.containsKey("offset")) {
                int offset = (Integer) state.get("offset");
                int limit = (Integer) state.get("limit");
                config.setOffset(offset);
                config.setLimit(limit);
            }
            if (state.containsKey("sortField")) {
                config.setSortField((String) state.get("sortField"));
                config.setSortDir(Style.SortDir.valueOf((String) state.get("sortDir")));
            }
            if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null) {
                currentDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            }
        }
        
        loader.load(config);
        
        PagingLoadConfig config2 = new BasePagingLoadConfig();
        config2.setOffset(0);
        //config2.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        config2.setLimit(table.getRecordCount());
        config2.setSortField("");
        if (grid.isViewReady()) {
            Map<String, Object> state2 = grid.getState();
            if (state2.containsKey("offset")) {
                int offset = (Integer) state2.get("offset");
                int limit = (Integer) state2.get("limit");
                config2.setOffset(offset);
                config2.setLimit(limit);
            }
            if (state2.containsKey("sortField")) {
                config2.setSortField((String) state2.get("sortField"));
                config2.setSortDir(Style.SortDir.valueOf((String) state2.get("sortDir")));
            }

            if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null) {
                currentDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            }
        }
//        loader2.load(config2);
    }

    public void putGridSearches() {
        condition.getSearches().clear();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = table.createRowColumnVal(key);
                if (builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr() == null && !builder.getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()) {
                    continue;
                }
                val.setVal(builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr());
                condition.getSearches().put(key, val);
            }
        }
    }

    //создаем панель с кнопками управления
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();
        
        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAdd();
            }
        });

        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(tbListener);
        menuItemDelete = new MenuItem(commonConstants.delete());
        menuItemDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        menuItemDelete.setEnabled(false);
        menuItemDelete.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDelete();
            }
        });

        btnEdit = new Button(commonConstants.edit());
        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnEdit.setIconAlign(Style.IconAlign.LEFT);
        btnEdit.setEnabled(false);
        btnEdit.addSelectionListener(tbListener);
        menuItemEdit = new MenuItem(commonConstants.edit());
        menuItemEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        menuItemEdit.setEnabled(false);
        menuItemEdit.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onEdit();
            }
        });

        btnView = new Button(commonConstants.view());
        btnView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnView.setIconAlign(Style.IconAlign.LEFT);
        btnView.setEnabled(false);
        btnView.addSelectionListener(tbListener);
        menuItemView = new MenuItem(commonConstants.view());
        menuItemView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuItemView.setEnabled(false);
        menuItemView.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onView();
            }
        });
        btnFiltr = new Button();
        //btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        //btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(false); //изначально делаем не видимойbtnFiltr
        
        btnSend = new Button(datagridConstants.btnSend());
        btnSend.setIcon(AbstractImagePrototype.create(datagridImages.check()));
        btnSend.setIconAlign(Style.IconAlign.LEFT);
        btnSend.setEnabled(false);
        btnSend.setVisible(false);
        btnSend.addSelectionListener(tbListener);


        btnSub = new Button(datagridConstants.btnSub());
        btnSub.setIcon(AbstractImagePrototype.create(datagridImages.form()));
        btnSub.setIconAlign(Style.IconAlign.LEFT);
        btnSub.setEnabled(false);
        btnSub.setVisible(false);
        btnSub.addSelectionListener(tbListener);


        btnRefr = new Button(datagridConstants.btnRefr());
        btnRefr.setIcon(AbstractImagePrototype.create(datagridImages.reset()));
        btnRefr.setIconAlign(Style.IconAlign.LEFT);
        btnRefr.setEnabled(true);
        btnRefr.setVisible(true);
        btnRefr.addSelectionListener(tbListener);
        menuItemRefr = new MenuItem(commonConstants.reset());
        menuItemRefr.setIcon(AbstractImagePrototype.create(commonImages.reset()));
        menuItemRefr.setEnabled(true);
        menuItemRefr.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                reloadGrid();
            }
        });
        
        btnReport = new Button(datagridConstants.btnReport());
        btnReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReport.setIconAlign(Style.IconAlign.LEFT);
        btnReport.setEnabled(true);
        btnReport.setVisible(false);
        btnReport.addSelectionListener(tbListener);
        menuItemReport = new MenuItem(datagridConstants.btnReport());
        menuItemReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        menuItemReport.setEnabled(true);
        menuItemReport.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
//                showReport();
            }
        });

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка пров доступа
        tb.add(sprEdit);
        tb.add(btnAdd);
        tb.add(btnEdit);
        tb.add(btnDelete);
        tb.add(btnView);

        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        tb.add(sprSend);
        tb.add(btnSend);

        sprSub = new SeparatorToolItem();
        sprSub.setVisible(false);
        tb.add(sprSub);
        tb.add(btnSub);

        sprReport = new SeparatorToolItem();
        sprReport.setVisible(false);
        tb.add(sprReport);
        
        //Кнопка для выбора типа сформированного отчета (PDF, DOC или XLS)
        btnReportSplit = new Button(datagridConstants.btnReport());//"Сформировать отчёт")
        btnReportSplit.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSplit.setIconAlign(Style.IconAlign.LEFT);
        btnReportSplit.setEnabled(true);
        btnReportSplit.setVisible(true);

        MenuItem itemPdf = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdf.setId("0");
        itemPdf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("PDF");
            }
        });


        MenuItem itemRtf = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtf.setId("1");
        itemRtf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("RTF");
            }
        });

        MenuItem itemXls = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXls.setId("2");
        itemXls.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("XLS");
            }
        });
        
        reportMenuSplit = new Menu();
        reportMenuSplit.insert(itemXls, 0);
        reportMenuSplit.insert(itemRtf, 0);
        reportMenuSplit.insert(itemPdf, 0);

        
        btnReportSplit.setMenu(reportMenuSplit);
        
        // Добавляем split-кнопку для формирования разных типов отчетов
        tb.add(btnReportSplit);

        ftiFiltr = new FillToolItem();
        tb.add(ftiFiltr);
        
        btnColumnsVisibility = new Button();
        btnColumnsVisibility.setToolTip(commonConstants.columnsVisibility());
        btnColumnsVisibility.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibility()));
        btnColumnsVisibility.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibility.addSelectionListener(tbListener);
        sprColumnsVisibility = new SeparatorToolItem();
        sprColumnsVisibility.setVisible(true);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        
        btnColumnsVisibilityDel = new Button();
        btnColumnsVisibilityDel.setToolTip(commonConstants.columnsVisibilityDel());
        btnColumnsVisibilityDel.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibilityDel()));
        btnColumnsVisibilityDel.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibilityDel.addSelectionListener(tbListener);
        sprColumnsVisibilityDel = new SeparatorToolItem();
        sprColumnsVisibilityDel.setVisible(true);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);

        btnHelpManual = new Button();
        btnHelpManual.setToolTip(datagridConstants.btnHelpManual());        
        btnHelpManual.setIcon(AbstractImagePrototype.create(datagridImages.helpmanual()));
        btnHelpManual.setIconAlign(Style.IconAlign.LEFT);
        btnHelpManual.setEnabled(true);
        btnHelpManual.addSelectionListener(tbListener);
        btnHelpManual.setVisible(false); //изначально делаем не видимой

        sprHelpManual = new SeparatorToolItem();
        sprHelpManual.setVisible(false);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(false);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        menu = new Menu();
        menu.add(menuItemRefr);
        sprMenuItemEdit = new SeparatorMenuItem();
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

        enabledBtn();
        
        return tb;
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        String datagridName = tableName.toUpperCase();
        isDictAdd = (mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName));
        isDictEdit = (mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName));
        isDictDelete = (mode != EditMode.VIEWONLY)
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
    public void updateToolBar() {
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(isDictAdd, btnAdd);
        removeOrAddBtnOnToolBar(isDictEdit, btnEdit);
        removeOrAddBtnOnToolBar(isDictDelete, btnDelete);
        removeOrAddBtnOnToolBar(isDictView, btnView);
        removeOrAddBtnOnToolBar(isSprDictEdit, sprEdit);
        //removeOrAddBtnOnToolBar(isBtnSendEnabled, btnSend);
        //removeOrAddBtnOnToolBar(isBtnSendEnabled,sprSend);

    }
    

    protected ColumnModel createColumModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        //пустая колонка для индикации
        ColumnConfig config = new ColumnConfig("nullCloumn", "", 16);
        config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        config.setFixed(true);
        columns.add(config);

        summary = new AggregationRowConfig<BeanModel>();

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                config = builder.createColumnConfig(key, table, getId());
                setAggregations(config);
                columns.add(config);
            }
        }

        ColumnModel cm = new ColumnModel(columns);
        cm.addAggregationRow(summary);
        return cm;
    }

    protected AggregationRowConfig<BeanModel> summary;

    protected void setAggregations(ColumnConfig cc) {
        // for override
    }

    ;

    /*
     * добавляем в грид объекты плагины (особенности грида, смотри документацию, например колонки типа чек бокс)
     */
    protected void initGridPlagin() {
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                ColumnConfig config = builder.createColumnConfig(key,table,getId());
                if (config instanceof CheckColumnConfig) {
                    grid.addPlugin((CheckColumnConfig) config);
                }
            }
        }
    }

    protected void gridSelectionChanged() {

        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        if (isOpenAsSub) {
            btnAdd.setEnabled(domainForSub != null);
            btnRefr.setEnabled(domainForSub != null);
            btnFiltr.setEnabled(domainForSub != null);
            menuItemAdd.setEnabled(domainForSub != null);
            menuItemRefr.setEnabled(domainForSub != null);
        }

        menuItemDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSub.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSend.setEnabled(isBtnSendEnabled && grid.getSelectionModel().getSelection().size() == 1);

        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        fireDataGridEvent(DataGridEvents.DataGridSelectionChanged, domain, table);

        setAllSubDomain(domain);


    }

    public PagingLoader getLoader() {
        return loader;
    }

    protected void gridRowDoubleClick(GridEvent event) {
        if (btnSend.isVisible() && btnSend.isEnabled()) {
            onSend();
        } else if (btnEdit.isVisible() && btnEdit.isEnabled()) {
            onEdit();
        } else if (btnView.isVisible() && btnView.isEnabled()) {
            onView();
        }
    }

    public DDataGrid createDomain() {
        DDataGrid domain = new DDataGrid();
        domain.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = builder.createRowColumnVal(key);
                domain.getRows().put(key, val);
                //builder.setDomainValueFromField(key, fields.get(key), domain );                
            }
        }
        return domain;
    }

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

        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
         
        
        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

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
                            //reloadGrid();
                            for (final BeanModel beanModel : selection) {
                                grid.getStore().remove(beanModel);
                                //condition.setLimit(tbPaging.getPageSize()-1);
                                //tbPaging.setPageSize(tbPaging.getPageSize()-1);
                                tbPaging.refresh();
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

    protected void onSend() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        fireDataGridEvent(DataGridEvents.DataGridSend, domain, table);
    }

    protected void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    onAdd();
                } else if (ce.getButton() == btnColumnsVisibility) {
                    saveColumnsVisibility();
                } else if (ce.getButton() == btnColumnsVisibilityDel) {
                    delColumnsVisibility();                    
                } else if (ce.getButton() == btnRefr) {
                    //updateTableInfo();//hlv
                    reloadGrid();
                } else if (ce.getButton() == btnEdit) {
                    onEdit();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                } else if (ce.getButton() == btnDelete) {
                    onDelete();
                } else if (ce.getButton() == btnHelpManual) {
                    showHelpManual();
                }
            }
        };

        saveDataGridListener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid domain) {
                BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(DDataGrid.class);
                ModelData model = factory.createModel(domain);
                if (grid.getStore() == null) {
                    return;
                }
                if (grid.getStore() == null) {
                    return;
                }
                if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null && (((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean() == domain)) {

                    grid.getStore().update(model);
                    grid.getSelectionModel().select(model, true);
                    setAllSubDomain(domain);
                } else {
                    grid.getStore().insert(model, grid.getStore().getCount());
                    grid.getSelectionModel().select(model, true);
                }
                reloadGrid();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }

    public void selectCurrentRow() {
        List<BeanModel> models = grid.getStore().getModels();
         Boolean wasFound = false;
        if (currentDomain == null) {
            grid.getSelectionModel().select(0, false);
        }
        else if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                for (BeanModel bm : models) {
                    DDataGrid dg = bm.getBean();
                    if (dg.isDomainEquals(currentDomain)) {
                        grid.getSelectionModel().select(models.indexOf(bm), false);
                        wasFound = true;
                        break;
                    }
                }
                if (!wasFound)
                    grid.getSelectionModel().select(0, false);
            }
        } else {
            setAllSubDomain(null);
        }        
    }

    //передаем информацию о необходимости фильтрации данных, 
    //сами значения фильтра должны передаваться методом putGridSubFilters()с последующим вызовом метода reloadGrid()
    public void setFiltrInfo(SRelationInfo relationInfo) {
        isOpenAsSub = true;
        subRalationInfo = relationInfo;   //если форма открыта в режиме отображения подчиненных данных передаем ей информацию о связях        
        setHeaderVisible(false);
        setBorders(false);
    }

    public DDataGrid getSelectedDomain() {
        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        return domain;
    }
    
    // Устанавливаем профиль видимых/скрытых колонок, которые сохранил пользователь
    protected void getAndSetColumnsVisibility() {
      
       
        //isColumnsVisibilitySet = true;
        int userId = ConfigurationManager.getUserId().intValue();
        String tablesNames = tableName;
        if (domainForSub != null) {
            tablesNames += "+" + domainForSub.getName();
        }
        service.getColumnsState(userId, tablesNames, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                //MessageBox.alert("Ошибка", "Не удалось загрузить профиль видимости полей" + caught.getMessage(), null);
            }
            @Override
            public void onSuccess(String result) {
                ArrayList<String> a = new ArrayList<String>();
                for (int i = 0; i < result.length(); i++) {
                    if ("0".equals(String.valueOf(result.charAt(i))) || "1".equals(String.valueOf(result.charAt(i)))) {
                        a.add(String.valueOf(result.charAt(i)));
                    }
                }
                for (int i = 0; i < a.size(); i++) {
                    if ("0".equals(a.get(i))) {
                        grid.getColumnModel().setHidden(i, true);
                    } else if ("1".equals(a.get(i))) {
                        grid.getColumnModel().setHidden(i, false);
                    }
                }
                grid.getView().refresh(true);
//                MessageBox.alert("Сообщение", "Профиль видимости полей загружен успешно", null);
            }
        });
        
    }
    
  // Выполняется при сохранении пользователем профиля видимых/скрытых колонок
    protected void delColumnsVisibility() {
         int userId = ConfigurationManager.getUserId().intValue();
        String tablesNames = tableName;
        if (domainForSub != null) {
            tablesNames += "+" + domainForSub.getName();
        }
        
        service.deleteColumnsState(userId, tablesNames, new AsyncCallback<DDataGrid>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("Ошибка", "Не удалось очистить профиль видимости полей" + caught.getMessage(), null);
            }
            @Override
            public void onSuccess(DDataGrid result) {
                MessageBox.alert("Сообщение", "Профиль видимости полей успешно очищен", null);
                //reloadGrid();
                //getAndSetColumnsVisibility();
                ColumnModel cm = createColumModel();                
                for (int i = 0; i < grid.getColumnModel().getColumnCount(); i++) {
                    grid.getColumnModel().setHidden(i, cm.isHidden(i));
                }
                 grid.getView().refresh(true);
            }
        });
    }
    
    // Выполняется при сохранении пользователем профиля видимых/скрытых колонок
    protected void saveColumnsVisibility() {
        int userId = ConfigurationManager.getUserId().intValue();
        String tablesNames = tableName;
        if (domainForSub != null) {
            tablesNames += "+" + domainForSub.getName();
        }
        ArrayList<String> columnsStates = new ArrayList<String>();
        for (int i = 0; i < grid.getColumnModel().getColumnCount(); i++) {
            if (grid.getColumnModel().isHidden(i)) {
                columnsStates.add("0");
            } else {
                columnsStates.add("1");
            }
        }
        service.updateColumnsState(userId, tablesNames, columnsStates.toString(), new AsyncCallback<DDataGrid>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("Ошибка", "Не удалось сохранить профиль видимости полей" + caught.getMessage(), null);
            }
            @Override
            public void onSuccess(DDataGrid result) {
                MessageBox.alert("Сообщение", "Профиль видимости полей сохранен успешно", null);
            }
        });
    }
}
