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
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
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
public class DataGridEditWindowWorkPlanExecutorNew extends DataGridEditWindow {

    // Для панели "Исполнители"
    private ContentPanel executorsContentPanel;
    private ListStore<Executor> execStore = new ListStore<Executor>();
    private Map<Integer, Boolean> workers = new HashMap<Integer, Boolean>();
    private Button execDelButton;
    private Button execAddButton;
    // Для панели "Предшествуют"
    private ContentPanel previousesContentPanel;
    private ListStore<Work> prevStore = new ListStore<Work>();
    private Map<Integer, Boolean> previouses = new HashMap<Integer, Boolean>();
    private Button prevDelButton;
    private Button prevAddButton;
    // Для панели "Обеспечивают"
    private ContentPanel resContentPanel;
    private ListStore<Work> resStore = new ListStore<Work>();
    private Map<Integer, Boolean> resources = new HashMap<Integer, Boolean>();
    private Button resDelButton;
    private Button resAddButton;
    // Флаг отсутствия изменений в табличных данных панели редактирования
    private boolean isNotChanged = true;
    // Для панелей "Исполнители", "Предшествуют", "Обеспечивают"
    protected TabPanel tabPanelSub;
    // Для указания типа зависимости в WORK_DEPENDENCE
    private final static Integer PARENT_DEPENDENCE_TYPE = 1;
    private final static Integer RESOURCE_DEPENDENCE_TYPE = 2;
    // Даты ограничения начала и конца из Календарного плана
    protected Date planBegDate;
    protected Date planEndDate;

    public DataGridEditWindowWorkPlanExecutorNew(DDataGrid domain, DTable table, EditState state, EWindowType windowType, List<DCondition> conditions, Date planBegDate, Date planEndDate) {
        super(domain, table, state, windowType);
        this.planBegDate = planBegDate;
        this.planEndDate = planEndDate;
        pnlEdit = new DataGridEditableFormWorkPlanExecutorNew(table, state, conditions, planBegDate, planEndDate);
        registerEditForm(pnlEdit);
        initPanelSub();
    }

    /**
     * Создает панель "Исполнители"
     */
    private void createExecutorsContentPanel() {
        // Чтобы получить данные для таблицы "Исполнители"
        final String tabName = "WP_EXECUTORS_NEW";

        // Получаем id выбранной в таблице "Исполнители" записи
        SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        DRowColumnValNumber vv = new DRowColumnValNumber();
        vv.setVal((Number) domain.getRows().get(key).getFormatVal(key, table.getColumnBuilder(key)));

        // Передаем id из таблицы "Исполнители"
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        cnd.getFilters().put(key, vv);

        // Включение/отключение кнопки "Удалить"
        execStore.addListener(Events.Clear, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (execDelButton != null) {
                    execDelButton.setEnabled(false);
                }
            }
        });
        execStore.addListener(Events.Add, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (execDelButton != null) {
                    execDelButton.setEnabled(true);
                }
            }
        });

        executorsContentPanel = new ContentPanel();
        executorsContentPanel.setBodyBorder(true);
        executorsContentPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);
        executorsContentPanel.setLayout(new FitLayout());
        executorsContentPanel.setHeaderVisible(false);

        // Если это не добавление, то загрузить имеющиеся данные
        if (state != EditState.NEW) {
            executorsContentPanel.mask();
            ((GWTDDataGridServiceAsync) getService()).getDataGrid(tabName, cnd, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                    executorsContentPanel.unmask();
                }

                @Override
                public void onSuccess(final List<DDataGrid> result) {
                    for (DDataGrid d : result) {
                        // Получаем имя и id исполнителя
                        SKeyForColumn key = new SKeyForColumn("WORKER:NAME");
                        String name = (String) d.getRows().get(key).getVal();

                        key = new SKeyForColumn("MAIN:WORKER_ID");
                        int workerId = (Integer) d.getRows().get(key).getVal();

                        // Добавляем в map, которая используется при сохранении данных
                        workers.put(workerId, false);

                        // Добавляем в хранилище для таблицы
                        Executor newExecutor = new Executor(workerId, name);
                        execStore.add(newExecutor);

                        // Включаем кнопку удаления
                        if (execDelButton != null) {
                            execDelButton.setEnabled(true);
                        }
                    }
                    executorsContentPanel.unmask();
                }
            });
        }

        // Создаем колонки для таблицы
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

        // Создаем таблицу
        final Grid<Executor> grid = new Grid<Executor>(execStore, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("name");
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setColumnReordering(true);

        executorsContentPanel.add(grid);

        // Создаем страницу для отображения списка работников при нажатии по кнопке "Добавить"
        final DataGridWindow page = new DataGridWindow("WORKER_FOR_EXECUTOR", true, true);

        // Создаем слушатель для кнопок "Удалить" и "Добавить"
        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {

                String id = be.getButton().getItemId();

                if (id.equals("open")) {
                    // Создаем слушатель для получения данных из таблицы со списком работников
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {

                            page.hide();

                            // Получаем данные исполнителя
                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            SKeyForColumn idKey = new SKeyForColumn("MAIN:WORKER_ID");
                            SKeyForColumn nameKey = new SKeyForColumn("MAIN:NAME");

                            Integer workerId = (Integer) rows.get(idKey).getFormatVal(idKey, table.getColumnBuilder(idKey));
                            String workerName = (String) rows.get(nameKey).getFormatVal(nameKey, table.getColumnBuilder(nameKey));

                            Executor newExecutor = new Executor(workerId, workerName);

                            // Проверяем, что выбранного исполнителя еще нет в списке
                            boolean isNew = true;

                            for (Executor executor : execStore.getModels()) {
                                if (executor.get("id").equals(newExecutor.get("id"))) {
                                    isNew = false;
                                    break;
                                }
                            }

                            // Если исполнителя еще нет, добавляем его
                            if (isNew) {
                                execStore.add(newExecutor);
                                StoreEvent e = new StoreEvent(execStore);
                                execStore.fireEvent(Events.Add, e);
                                isNotChanged = false;
                            }
                            page.removeDataGridListener(this);
                        }
                    };

                    page.addDataGridListener(listenerGetDomain);
                    page.show();

                } else if (id.equals("delete")) {
                    // Создаем слушатель подтверждения удаления выбранной записи
                    Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                                execStore.remove(grid.getSelectionModel().getSelectedItem());

                                isNotChanged = false;

                                if (execStore.getCount() < 1) {
                                    StoreEvent e = new StoreEvent(execStore);
                                    execStore.fireEvent(Events.Clear, e);
                                }

                            }
                        }
                    };

                    if (grid.getSelectionModel().getSelectedItem() != null) {
                        MessageBox.confirm(constants.confirm(), "Удалить исполнителя?", listener);
                    }

                }

            }
        };

        // Создаем панель для кнопок "Удалить" и "Добавить"
        ToolBar mainToolBar = new ToolBar();

        execAddButton = new Button(constants.add(), AbstractImagePrototype.create(images.add()), buttonEventListener);
        execAddButton.setItemId("open");

        mainToolBar.add(execAddButton);

        execDelButton = new Button(constants.delete(), AbstractImagePrototype.create(images.delete()), buttonEventListener);
        execDelButton.setItemId("delete");
        execDelButton.setEnabled(false);

        mainToolBar.add(execDelButton);

        executorsContentPanel.setTopComponent(mainToolBar);
    }

    /**
     * Создает панель "Предшествуют"
     */
    private void createPreviousesContentPanel() {
        // Чтобы получить данные для таблицы "Предшествуют"
        final String tabName = "WORK_PLAN_EXECUTOR_NEW_PREVIOUSES";

        // Получаем id выбранной в таблице "Исполнители" записи
        SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        DRowColumnValNumber vv = new DRowColumnValNumber();
        vv.setVal((Number) domain.getRows().get(key).getFormatVal(key, table.getColumnBuilder(key)));

        // Передаем id из таблицы "Исполнители", как ID2, и тип связи=1 (Вторая работа следует за первой)
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        SKeyForColumn putKey = new SKeyForColumn("MAIN:ID2");
        cnd.getFilters().put(putKey, vv);
        putKey = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
        vv = new DRowColumnValNumber();
        vv.setVal(PARENT_DEPENDENCE_TYPE);
        cnd.getFilters().put(putKey, vv);

        // Включение/отключение кнопки "Удалить" и передача максимальной конечной даты из списка предшествующих работ в поле "Дата начала"
        prevStore.addListener(Events.Clear, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (prevDelButton != null) {
                    prevDelButton.setEnabled(false);
                }
                if (prevStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinBegDate(planBegDate, true);
                }
            }
        });
        prevStore.addListener(Events.Add, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (prevDelButton != null) {
                    prevDelButton.setEnabled(true);
                }
                Date newDate = null;

                if (prevStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinBegDate(planBegDate, true);
                } else {
                    for (Work el : prevStore.getModels()) {
                        if (newDate == null) {
                            newDate = (Date) el.get("endDate");
                        } else if (newDate.before((Date) el.get("endDate"))) {
                            newDate = (Date) el.get("endDate");
                        }
                    }
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinBegDate(newDate, false);
                }
            }
        });
        prevStore.addListener(Events.Remove, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                Date newDate = null;

                if (prevStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinBegDate(planBegDate, true);
                } else {
                    for (Work el : prevStore.getModels()) {
                        if (newDate == null) {
                            newDate = (Date) el.get("endDate");
                        } else if (newDate.before((Date) el.get("endDate"))) {
                            newDate = (Date) el.get("endDate");
                        }
                    }
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinBegDate(newDate, false);
                }
            }
        });

        previousesContentPanel = new ContentPanel();
        previousesContentPanel.setBodyBorder(true);
        previousesContentPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);
        previousesContentPanel.setLayout(new FitLayout());
        previousesContentPanel.setHeaderVisible(false);

        // Если это не добавление, то загрузить имеющиеся данные
        if (state != EditState.NEW) {
            previousesContentPanel.mask();
            ((GWTDDataGridServiceAsync) getService()).getDataGrid(tabName, cnd, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                    previousesContentPanel.unmask();
                }

                @Override
                public void onSuccess(final List<DDataGrid> result) {

                    for (DDataGrid d : result) {
                        // Получаем номер работы и id работы
                        SKeyForColumn key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:WORK_NUMBER");
                        String workNumber = "";
                        if (d.getRows().get(key) != null) {
                            workNumber = (String) d.getRows().get(key).getVal();
                        }

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:WORK_NAME");
                        String workName = "";
                        if (d.getRows().get(key) != null) {
                            workName = (String) d.getRows().get(key).getVal();
                        }

                        key = new SKeyForColumn("MAIN:ID1");
                        int workId = (Integer) d.getRows().get(key).getVal();

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:BEG_DATE");
                        Date begDate = (Date) d.getRows().get(key).getVal();

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:END_DATE");
                        Date endDate = (Date) d.getRows().get(key).getVal();

                        // Добавляем в map, которая используется при сохранении данных
                        previouses.put(workId, false);

                        // Добавляем в хранилище для таблицы
                        Work newPreviousWork = new Work(workId, workNumber, begDate, endDate, workName);
                        prevStore.add(newPreviousWork);

                        // Включаем кнопку удаления
                        if (prevDelButton != null) {
                            prevDelButton.setEnabled(true);
                        }
                    }

                    previousesContentPanel.unmask();
                }
            });
        }

        // Создаем колонки для таблицы
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("Идентификатор");
        column.setWidth(20);
        column.setRowHeader(true);
        column.setHidden(true);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("workNum");
        column.setHeader("Номер работы");
        column.setWidth(80);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("begDate");
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        column.setHeader("Дата начала");
        column.setWidth(100);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("endDate");
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        column.setHeader("Дата окончания");
        column.setWidth(100);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("workName");
        column.setHeader("Детализация работы");
        column.setWidth(200);

        configs.add(column);

        ColumnModel cm = new ColumnModel(configs);

        // Создаем таблицу
        final Grid<Work> grid = new Grid<Work>(prevStore, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("workNum");
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setColumnReordering(true);

        previousesContentPanel.add(grid);


        List<DCondition> prevConditions = new ArrayList<DCondition>();
        key = new SKeyForColumn("MAIN:GROUP_ID");
        String strVal = domain.getRows().get(key).getStringFromVal(key, table.getColumnBuilder(key));
        prevConditions.add(new DCondition(key, strVal));
        key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        strVal = domain.getRows().get(key).getStringFromVal(key, table.getColumnBuilder(key));
        prevConditions.add(new DCondition(key, strVal));

        // Создаем страницу для отображения списка работ, которые не являются потомками, при нажатии по кнопке "Добавить"        
        final DataGridWindow page;
        page = new DataGridWindow("WORK_PLAN_EXECUTOR_NEW_LIST", true, true, prevConditions);

        // Создаем слушатель для кнопок "Удалить" и "Добавить"
        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {

                String id = be.getButton().getItemId();

                if (id.equals("open")) {
                    // Создаем слушатель для получения данных из таблицы со списком работ
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {

                            page.hide();

                            // Получаем данные работы
                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");

                            Integer workId = (Integer) rows.get(key).getFormatVal(key, table.getColumnBuilder(key));

                            SKeyForColumn workNumberKey = new SKeyForColumn("MAIN:WORK_NUMBER");
                            String workNumber = "";
                            if (rows.get(workNumberKey) != null) {
                                workNumber = (String) rows.get(workNumberKey).getFormatVal(workNumberKey, table.getColumnBuilder(workNumberKey));
                            }

                            key = new SKeyForColumn("MAIN:WORK_NAME");
                            String workName = "";
                            if (rows.get(key) != null) {
                                workName = (String) rows.get(key).getVal();
                            }

                            key = new SKeyForColumn("MAIN:BEG_DATE");
                            Date begDate = (Date) rows.get(key).getVal();

                            key = new SKeyForColumn("MAIN:END_DATE");
                            Date endDate = (Date) rows.get(key).getVal();

                            // Добавляем в хранилище для таблицы
                            Work newPreviousWork = new Work(workId, workNumber, begDate, endDate, workName);

                            // Проверяем, что выбранной работы еще нет в списке                            
                            boolean isNew = true;

                            for (Work previousWork : prevStore.getModels()) {
                                if (previousWork.get("id").equals(newPreviousWork.get("id"))) {
                                    isNew = false;
                                    break;
                                }
                            }

                            // Если работы еще нет, добавляем ее
                            if (isNew) {
                                prevStore.add(newPreviousWork);
                                StoreEvent e = new StoreEvent(prevStore);
                                prevStore.fireEvent(Events.Add, e);
                                isNotChanged = false;
                            }
                            page.removeDataGridListener(this);
                        }
                    };
                    page.addDataGridListener(listenerGetDomain);
                    page.show();
                }
                if (id.equals("delete")) {
                    // Создаем слушатель подтверждения удаления выбранной записи                    
                    Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            if (ce.getButtonClicked().getItemId().equals("yes") == true) {

                                prevStore.remove(grid.getSelectionModel().getSelectedItem());

                                isNotChanged = false;

                                StoreEvent e = new StoreEvent(prevStore);
                                prevStore.fireEvent(Events.Remove, e);

                                if (prevStore.getCount() < 1) {
                                    prevStore.fireEvent(Events.Clear, e);
                                }
                            }
                        }
                    };

                    if (grid.getSelectionModel().getSelectedItem() != null) {
                        MessageBox.confirm(constants.confirm(), "Удалить предшествующую работу?", listener);
                    }

                }
            }
        };

        // Создаем панель для кнопок "Удалить" и "Добавить"
        ToolBar mainToolBar = new ToolBar();

        prevAddButton = new Button(constants.add(), AbstractImagePrototype.create(images.add()), buttonEventListener);
        prevAddButton.setItemId("open");

        mainToolBar.add(prevAddButton);

        prevDelButton = new Button(constants.delete(), AbstractImagePrototype.create(images.delete()), buttonEventListener);
        prevDelButton.setItemId("delete");
        prevDelButton.setEnabled(false);

        mainToolBar.add(prevDelButton);

        previousesContentPanel.setTopComponent(mainToolBar);
    }

    /**
     * Создает панель "Обеспечивают"
     */
    private void createResourcesContentPanel() {
        // Чтобы получить данные для таблицы "Обеспечивают"
        final String tabName = "WORK_PLAN_EXECUTOR_NEW_PREVIOUSES";

        // Получаем id выбранной в таблице "Исполнители" записи
        SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        DRowColumnValNumber vv = new DRowColumnValNumber();
        vv.setVal((Number) domain.getRows().get(key).getFormatVal(key, table.getColumnBuilder(key)));

        // Передаем id из таблицы "Исполнители", как ID2, и тип связи=2 (Первая работа обеспечивает вторую)
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        SKeyForColumn putKey = new SKeyForColumn("MAIN:ID2");
        cnd.getFilters().put(putKey, vv);
        putKey = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
        vv = new DRowColumnValNumber();
        vv.setVal(RESOURCE_DEPENDENCE_TYPE);
        cnd.getFilters().put(putKey, vv);

        // Включение/отключение кнопки "Удалить" и передача максимальной конечной даты из списка обеспечивающих работ в поле "Дата окончания"
        resStore.addListener(Events.Clear, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (resDelButton != null) {
                    resDelButton.setEnabled(false);
                }
                if (resStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinEndDate(planEndDate, true);
                }
            }
        });
        resStore.addListener(Events.Add, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                if (resDelButton != null) {
                    resDelButton.setEnabled(true);
                }
                Date newDate = null;

                if (resStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinEndDate(planEndDate, true);
                } else {
                    for (Work el : resStore.getModels()) {
                        if (newDate == null) {
                            newDate = (Date) el.get("endDate");
                        } else if (newDate.before((Date) el.get("endDate"))) {
                            newDate = (Date) el.get("endDate");
                        }
                    }
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinEndDate(newDate, false);
                }
            }
        });
        resStore.addListener(Events.Remove, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                Date newDate = null;

                if (resStore.getModels().isEmpty()) {
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinEndDate(planEndDate, true);
                } else {
                    for (Work el : resStore.getModels()) {
                        if (newDate == null) {
                            newDate = (Date) el.get("endDate");
                        } else if (newDate.before((Date) el.get("endDate"))) {
                            newDate = (Date) el.get("endDate");
                        }
                    }
                    ((DataGridEditableFormWorkPlanExecutorNew) pnlEdit).updateMinEndDate(newDate, false);
                }
            }
        });

        resContentPanel = new ContentPanel();
        resContentPanel.setBodyBorder(true);
        resContentPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);
        resContentPanel.setLayout(new FitLayout());
        resContentPanel.setHeaderVisible(false);

        // Если это не добавление, то загрузить имеющиеся данные
        if (state != EditState.NEW) {
            resContentPanel.mask();
            ((GWTDDataGridServiceAsync) getService()).getDataGrid(tabName, cnd, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                    resContentPanel.unmask();
                }

                @Override
                public void onSuccess(final List<DDataGrid> result) {

                    for (DDataGrid d : result) {
                        // Получаем номер работы и id работы
                        SKeyForColumn key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:WORK_NUMBER");
                        String workNumber = "";
                        if (d.getRows().get(key) != null) {
                            workNumber = (String) d.getRows().get(key).getVal();
                        }

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:WORK_NAME");
                        String workName = "";
                        if (d.getRows().get(key) != null) {
                            workName = (String) d.getRows().get(key).getVal();
                        }

                        key = new SKeyForColumn("MAIN:ID1");
                        int workId = (Integer) d.getRows().get(key).getVal();

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:BEG_DATE");
                        Date begDate = (Date) d.getRows().get(key).getVal();

                        key = new SKeyForColumn("WORK_PLAN_EXECUTOR1:END_DATE");
                        Date endDate = (Date) d.getRows().get(key).getVal();

                        // Добавляем в map, которая используется при сохранении данных
                        resources.put(workId, false);

                        // Добавляем в хранилище для таблицы
                        Work newResWork = new Work(workId, workNumber, begDate, endDate, workName);
                        resStore.add(newResWork);

                        // Включаем кнопку удаления
                        if (resDelButton != null) {
                            resDelButton.setEnabled(true);
                        }
                    }

                    resContentPanel.unmask();
                }
            });
        }

        // Создаем колонки для таблицы
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("Идентификатор");
        column.setWidth(20);
        column.setRowHeader(true);
        column.setHidden(true);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("workNum");
        column.setHeader("Номер работы");
        column.setWidth(80);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("begDate");
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        column.setHeader("Дата начала");
        column.setWidth(100);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("endDate");
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        column.setHeader("Дата окончания");
        column.setWidth(100);

        configs.add(column);

        column = new ColumnConfig();
        column.setId("workName");
        column.setHeader("Детализация работы");
        column.setWidth(200);

        configs.add(column);

        ColumnModel cm = new ColumnModel(configs);

        // Создаем таблицу
        final Grid<Work> grid = new Grid<Work>(resStore, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("workNum");
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setColumnReordering(true);

        resContentPanel.add(grid);


        List<DCondition> resConditions = new ArrayList<DCondition>();
        key = new SKeyForColumn("MAIN:GROUP_ID");
        String strVal = domain.getRows().get(key).getStringFromVal(key, table.getColumnBuilder(key));
        resConditions.add(new DCondition(key, strVal));
        key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");
        strVal = domain.getRows().get(key).getStringFromVal(key, table.getColumnBuilder(key));
        resConditions.add(new DCondition(key, strVal));

        // Создаем страницу для отображения списка ресурсов при нажатии по кнопке "Добавить"        
        final DataGridWindow page;
        page = new DataGridWindow("WORK_PLAN_EXECUTOR_NEW_LIST", true, true, resConditions);

        // Создаем слушатель для кнопок "Удалить" и "Добавить"
        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {

                String id = be.getButton().getItemId();

                if (id.equals("open")) {
                    // Создаем слушатель для получения данных из таблицы со списком работ
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {

                            page.hide();

                            // Получаем данные работы
                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            SKeyForColumn key = new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID");

                            Integer workId = (Integer) rows.get(key).getFormatVal(key, table.getColumnBuilder(key));

                            SKeyForColumn workNumberKey = new SKeyForColumn("MAIN:WORK_NUMBER");
                            String workNumber = "";
                            if (rows.get(workNumberKey) != null) {
                                workNumber = (String) rows.get(workNumberKey).getFormatVal(workNumberKey, table.getColumnBuilder(workNumberKey));
                            }

                            key = new SKeyForColumn("MAIN:WORK_NAME");
                            String workName = "";
                            if (rows.get(key) != null) {
                                workName = (String) rows.get(key).getVal();
                            }

                            key = new SKeyForColumn("MAIN:BEG_DATE");
                            Date begDate = (Date) rows.get(key).getVal();

                            key = new SKeyForColumn("MAIN:END_DATE");
                            Date endDate = (Date) rows.get(key).getVal();

                            // Добавляем в хранилище для таблицы
                            Work work = new Work(workId, workNumber, begDate, endDate, workName);

                            // Проверяем, что выбранной работы еще нет в списке                            
                            boolean isNew = true;

                            for (Work previousWork : resStore.getModels()) {
                                if (previousWork.get("id").equals(work.get("id"))) {
                                    isNew = false;
                                    break;
                                }
                            }

                            // Если работы еще нет, добавляем ее
                            if (isNew) {
                                resStore.add(work);
                                StoreEvent e = new StoreEvent(resStore);
                                resStore.fireEvent(Events.Add, e);
                                isNotChanged = false;
                            }
                            page.removeDataGridListener(this);
                        }
                    };
                    page.addDataGridListener(listenerGetDomain);
                    page.show();
                }
                if (id.equals("delete")) {
                    // Создаем слушатель подтверждения удаления выбранной записи                    
                    Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            if (ce.getButtonClicked().getItemId().equals("yes") == true) {

                                resStore.remove(grid.getSelectionModel().getSelectedItem());

                                isNotChanged = false;

                                StoreEvent e = new StoreEvent(resStore);
                                resStore.fireEvent(Events.Remove, e);

                                if (resStore.getCount() < 1) {
                                    resStore.fireEvent(Events.Clear, e);
                                }
                            }
                        }
                    };

                    if (grid.getSelectionModel().getSelectedItem() != null) {
                        MessageBox.confirm(constants.confirm(), "Удалить предшествующую работу?", listener);
                    }

                }
            }
        };

        // Создаем панель для кнопок "Удалить" и "Добавить"
        ToolBar mainToolBar = new ToolBar();

        resAddButton = new Button(constants.add(), AbstractImagePrototype.create(images.add()), buttonEventListener);
        resAddButton.setItemId("open");

        mainToolBar.add(resAddButton);

        resDelButton = new Button(constants.delete(), AbstractImagePrototype.create(images.delete()), buttonEventListener);
        resDelButton.setItemId("delete");
        resDelButton.setEnabled(false);

        mainToolBar.add(resDelButton);

        resContentPanel.setTopComponent(mainToolBar);
    }

    /**
     * Создаем вкладку
     *
     * @param itemId id
     * @param caption заголовок
     * @param panel панель с контентом
     * @return элемент для панели вкладок
     */
    private TabItem createTabItem(String itemId, String caption, ContentPanel panel) {
        TabItem item = new TabItem();
        item.setClosable(false);
        item.setBorders(false);
        item.setItemId(itemId);
        item.setText(caption);
        item.setLayout(new FitLayout());
        item.add(panel);
        return item;
    }

    // Тип для таблицы "Исполнители"
    protected class Executor extends BeanModel {

        public Executor(Integer id, String name) {
            set("id", id);
            set("name", name);
        }
    }

    // Тип для таблицы "Предшествуют"
    protected class Work extends BeanModel {

        public Work(Integer id, String workNum, Date begDate, Date endDate, String workName) {
            set("id", id);
            set("workNum", workNum);
            set("begDate", begDate);
            set("endDate", endDate);
            set("workName", workName);
        }
    }

    /**
     * Создает панель вкладок для панелей "Исполнители", "Предшествуют",
     * "Обеспечивают"
     */
    protected final void initPanelSub() {

        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        BorderLayoutData panelDataSub = new BorderLayoutData(Style.LayoutRegion.SOUTH, 250);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        main.add(tabPanelSub, panelDataSub);

        createExecutorsContentPanel();
        TabItem item = createTabItem("WP_EXECUTORS_NEW", "Исполнители", executorsContentPanel);
        tabPanelSub.add(item);

        createPreviousesContentPanel();
        item = createTabItem("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", "Предшествуют", previousesContentPanel);
        tabPanelSub.add(item);

        createResourcesContentPanel();
        item = createTabItem("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", "Обеспечивают", resContentPanel);
        tabPanelSub.add(item);
    }

    /**
     *
     * @param actionListener
     */
    @Override
    protected void save(final Listener actionListener) {

        getEditPanel().mask(constants.saving());
        tabPanelSub.mask();

        if (domain == null) {
            domain = createDomain();
        }

        final DDataGrid oldDomain = domain.newInstance();
        oldDomain.copy(domain);

        editForms.fillDomain(getDomain());


        String message = checkBegEndDates();

        if (message != null) {

            getEditPanel().unmask();
            tabPanelSub.unmask();

            MessageBox.alert(commonConstants.error(), message, null);

        } else {

            ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, (state == EditState.NEW), new AsyncCallback<DDataGrid>() {
                @Override
                public void onFailure(Throwable thrwbl) {
                    getDomain().copy(oldDomain);
                    getEditPanel().unmask();
                    tabPanelSub.unmask();
                    AppHelper.showMsgRpcServiceError(thrwbl);
                }

                @Override
                public void onSuccess(final DDataGrid t) {
                    if (t == null) {
                        onFailure(null);
                    } else {
                        saveExecutors(t);
                    }
                }

                private void saveExecutors(final DDataGrid t) {
                    List<DDataGrid> domainsForInsert = new ArrayList<DDataGrid>();
                    final List<DDataGrid> domainsForDelete = new ArrayList<DDataGrid>();

                    Integer workPlanExecutorId = (Integer) t.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal();
                    for (Executor el : execStore.getModels()) {
                        Integer workerId = (Integer) el.get("id");
                        if (workers.containsKey(workerId)) {
                            workers.put(workerId, true);
                        } else {
                            DDataGrid data = new DDataGrid();
                            data.setName("WP_EXECUTORS_NEW");

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
                            data.setName("WP_EXECUTORS_NEW");

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
                        savePreviouses(t);
                    }

                    if (!domainsForInsert.isEmpty()) {
                        ((GWTDDataGridServiceAsync) getService()).insert("WP_EXECUTORS_NEW", domainsForInsert, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {

                                if (!domainsForDelete.isEmpty()) {
                                    ((GWTDDataGridServiceAsync) getService()).deleteDomains("WP_EXECUTORS_NEW", domainsForDelete, new AsyncCallback<Boolean>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            AppHelper.showMsgRpcServiceError(caught);
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                            savePreviouses(t);
                                        }
                                    });
                                } else {
                                    savePreviouses(t);
                                }
                            }
                        });
                    } else {
                        if (!domainsForDelete.isEmpty()) {
                            ((GWTDDataGridServiceAsync) getService()).deleteDomains("WP_EXECUTORS_NEW", domainsForDelete, new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    AppHelper.showMsgRpcServiceError(caught);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    savePreviouses(t);
                                }
                            });
                        }
                    }
                }

                private void savePreviouses(final DDataGrid t) {
                    List<DDataGrid> domainsForInsert = new ArrayList<DDataGrid>();
                    final List<DDataGrid> domainsForDelete = new ArrayList<DDataGrid>();

                    Integer workPlanExecutorId = (Integer) t.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal();
                    for (Work el : prevStore.getModels()) {
                        Integer workId = (Integer) el.get("id");
                        if (previouses.containsKey(workId)) {
                            previouses.put(workId, true);
                        } else {
                            DDataGrid data = new DDataGrid();
                            data.setName("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES");

                            SKeyForColumn id2 = new SKeyForColumn("MAIN:ID2");
                            DRowColumnValNumber id2Val = new DRowColumnValNumber();
                            id2Val.setVal(workPlanExecutorId);
                            data.getRows().put(id2, id2Val);

                            SKeyForColumn id1 = new SKeyForColumn("MAIN:ID1");
                            DRowColumnValNumber id1Val = new DRowColumnValNumber();
                            id1Val.setVal(workId);
                            data.getRows().put(id1, id1Val);

                            SKeyForColumn dependenceType = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
                            DRowColumnValNumber dependenceTypeVal = new DRowColumnValNumber();
                            dependenceTypeVal.setVal(PARENT_DEPENDENCE_TYPE);
                            data.getRows().put(dependenceType, dependenceTypeVal);

                            domainsForInsert.add(data);
                        }
                    }

                    for (Integer workId : previouses.keySet()) {
                        if (!previouses.get(workId)) {
                            DDataGrid data = new DDataGrid();
                            data.setName("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES");

                            SKeyForColumn id2 = new SKeyForColumn("MAIN:ID2");
                            DRowColumnValNumber id2Val = new DRowColumnValNumber();
                            id2Val.setVal(workPlanExecutorId);
                            data.getRows().put(id2, id2Val);

                            SKeyForColumn id1 = new SKeyForColumn("MAIN:ID1");
                            DRowColumnValNumber id1Val = new DRowColumnValNumber();
                            id1Val.setVal(workId);
                            data.getRows().put(id1, id1Val);

                            SKeyForColumn dependenceType = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
                            DRowColumnValNumber dependenceTypeVal = new DRowColumnValNumber();
                            dependenceTypeVal.setVal(PARENT_DEPENDENCE_TYPE);
                            data.getRows().put(dependenceType, dependenceTypeVal);

                            domainsForDelete.add(data);
                        }
                    }

                    if (domainsForDelete.isEmpty() && domainsForInsert.isEmpty()) {
                        saveResources(t);
                    }

                    if (!domainsForInsert.isEmpty()) {
                        ((GWTDDataGridServiceAsync) getService()).insert("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForInsert, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {

                                if (!domainsForDelete.isEmpty()) {
                                    ((GWTDDataGridServiceAsync) getService()).deleteDomains("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForDelete, new AsyncCallback<Boolean>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            AppHelper.showMsgRpcServiceError(caught);
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                            saveResources(t);
                                        }
                                    });
                                } else {
                                    saveResources(t);
                                }
                            }
                        });
                    } else {
                        if (!domainsForDelete.isEmpty()) {
                            ((GWTDDataGridServiceAsync) getService()).deleteDomains("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForDelete, new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    AppHelper.showMsgRpcServiceError(caught);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    saveResources(t);
                                }
                            });
                        }
                    }
                }

                private void saveResources(final DDataGrid t) {
                    List<DDataGrid> domainsForInsert = new ArrayList<DDataGrid>();
                    final List<DDataGrid> domainsForDelete = new ArrayList<DDataGrid>();

                    Integer workPlanExecutorId = (Integer) t.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal();
                    for (Work el : resStore.getModels()) {
                        Integer workId = (Integer) el.get("id");
                        if (resources.containsKey(workId)) {
                            resources.put(workId, true);
                        } else {
                            DDataGrid data = new DDataGrid();
                            data.setName("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES");

                            SKeyForColumn id2 = new SKeyForColumn("MAIN:ID2");
                            DRowColumnValNumber id2Val = new DRowColumnValNumber();
                            id2Val.setVal(workPlanExecutorId);
                            data.getRows().put(id2, id2Val);

                            SKeyForColumn id1 = new SKeyForColumn("MAIN:ID1");
                            DRowColumnValNumber id1Val = new DRowColumnValNumber();
                            id1Val.setVal(workId);
                            data.getRows().put(id1, id1Val);

                            SKeyForColumn dependenceType = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
                            DRowColumnValNumber dependenceTypeVal = new DRowColumnValNumber();
                            dependenceTypeVal.setVal(RESOURCE_DEPENDENCE_TYPE);
                            data.getRows().put(dependenceType, dependenceTypeVal);

                            domainsForInsert.add(data);
                        }
                    }

                    for (Integer workId : resources.keySet()) {
                        if (!resources.get(workId)) {
                            DDataGrid data = new DDataGrid();
                            data.setName("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES");

                            SKeyForColumn id2 = new SKeyForColumn("MAIN:ID2");
                            DRowColumnValNumber id2Val = new DRowColumnValNumber();
                            id2Val.setVal(workPlanExecutorId);
                            data.getRows().put(id2, id2Val);

                            SKeyForColumn id1 = new SKeyForColumn("MAIN:ID1");
                            DRowColumnValNumber id1Val = new DRowColumnValNumber();
                            id1Val.setVal(workId);
                            data.getRows().put(id1, id1Val);

                            SKeyForColumn dependenceType = new SKeyForColumn("MAIN:DEPENDENCE_TYPE");
                            DRowColumnValNumber dependenceTypeVal = new DRowColumnValNumber();
                            dependenceTypeVal.setVal(RESOURCE_DEPENDENCE_TYPE);
                            data.getRows().put(dependenceType, dependenceTypeVal);

                            domainsForDelete.add(data);
                        }
                    }

                    if (domainsForDelete.isEmpty() && domainsForInsert.isEmpty()) {
                        executeAfterSave(actionListener, t);
                    }

                    if (!domainsForInsert.isEmpty()) {
                        ((GWTDDataGridServiceAsync) getService()).insert("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForInsert, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {

                                if (!domainsForDelete.isEmpty()) {
                                    ((GWTDDataGridServiceAsync) getService()).deleteDomains("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForDelete, new AsyncCallback<Boolean>() {
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
                            ((GWTDDataGridServiceAsync) getService()).deleteDomains("WORK_PLAN_EXECUTOR_NEW_PREVIOUSES", domainsForDelete, new AsyncCallback<Boolean>() {
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
            });
        }
    }

    private void executeAfterSave(final Listener actionListener, DDataGrid t) {

        getDomain().copy(t);
        updateFormsFromDomain();
        notifyDomainSaveSuccesed();
        getEditPanel().unmask();
        tabPanelSub.unmask();
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
