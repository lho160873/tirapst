package pst.arm.client.modules.ganttchart;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import pst.gwt.gwtgantt.DateUtil;
import pst.gwt.gwtgantt.TaskUtil;
import pst.gwt.gwtgantt.gantt.ProvidesTask;
import pst.gwt.gwtgantt.gantt.ProvidesTaskImpl;
import pst.gwt.gwtgantt.model.Predecessor;
import pst.gwt.gwtgantt.model.PredecessorType;
import pst.gwt.gwtgantt.model.Task;
import pst.gwt.gwtgantt.table.TaskGridColumn;
import pst.gwt.gwtgantt.table.TaskGridNameCell;
import pst.gwt.gwtgantt.table.override.CellTextImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;
import pst.arm.client.modules.ganttchart.images.GanttChartImages;
import pst.arm.client.modules.ganttchart.lang.GanttChartConstants;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartPriboyService;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartPriboyServiceAsync;
import pst.arm.client.modules.ganttchart.portable.DetailsGanttChartWindow;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartMartService;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartMartServiceAsync;
import pst.arm.client.modules.ganttchart.widgets.OrderWindow;
import pst.gwt.gwtgantt.gantt.GanttChart;
import pst.gwt.gwtgantt.table.GridView;

/**
 * @version 1.0
 * @author nikita
 */
public class GanttChartPanel extends ContentPanel {

    private boolean flag = false;
    // Строковые константы
    private GanttChartConstants constants = (GanttChartConstants) GWT.create(GanttChartConstants.class);
    // Ресурсы изображения
    private GanttChartImages images = (GanttChartImages) GWT.create(GanttChartImages.class);
    // Связвает данные и их представление
    private ListDataProvider<Task> listDataProvider;
    // Взаимодейтсвие с выбором пользователя
    private SelectionModel<Task> selectionModel;
    // 
    private ProvidesTask<Task> providesTask;
    // Диаграмма Ганта
    private GanttChart<Task> ganttChart;
    // Таблица с данными о диограмме
    private GridView<Task> gridView;
    // Список задач
    private static List<Task> taskList;
    // Корень
    private DockLayoutPanel mainPanel;
    // Диаграмма Ганта и таблица с данными
    private SplitLayoutPanel ganttChartPanel;
    // Основная панель управления 
    private ToolBar toolBarForButtons;
    // Панель редактирования задачи
    private FlowPanel editPanel;
    // Панель работы с ресурсами
    private FlowPanel resourcePanel;
    // Получение данных с БД
    private GWTGanttChartPriboyServiceAsync service;
    private GWTGanttChartMartServiceAsync service2;
    // Прокси для получения данных с БД
    private RpcProxy<List<Job>> proxyForJob;
    private RpcProxy<List<JobPlan>> proxyForJobPlan;
    private RpcProxy<List<Order>> proxyForOrder;
    // Читатели для получения данных с БД
    private BeanModelReader readerForJob;
    private BeanModelReader readerForJobPlan;
    private BeanModelReader readerForOrder;
    // Загрузчики для получения данных с БД
    private ListLoader<ListLoadResult<ModelData>> loaderForJob;
    private ListLoader<ListLoadResult<ModelData>> loaderForJobPlan;
    private ListLoader<ListLoadResult<ModelData>> loaderForOrder;
    // Хранилища для получения данных с БД
    private ListStore<BeanModel> storeForJob;
    private ListStore<BeanModel> storeForJobPlan;
    private ListStore<BeanModel> storeForOrder;
    // Таблица выбора заказа
    private Grid<BeanModel> grid;
    // Панель выбора заказа
    private final ContentPanel orderPanel = new ContentPanel();
    // 
    private Window testWindow;
    private ToolBar toolBar;
//////////////////////////////////////

    private ToolBar getButtonsToolBar() {

        toolBarForButtons = new ToolBar();
        toolBarForButtons.setSpacing(6);
        Button addButton = new Button(constants.btnAddTask(), AbstractImagePrototype.create(images.addTask()));
        Button removeButton = new Button(constants.btnRemTask(), AbstractImagePrototype.create(images.removeTask()));
        Button shiftLeftButton = new Button(constants.btnShiftLeftTask(), AbstractImagePrototype.create(images.shiftLeft()));
        Button shiftRightButton = new Button(constants.btnShiftRightTask(), AbstractImagePrototype.create(images.shiftRight()));
        Button moveUpButton = new Button(constants.btnMoveUpTask(), AbstractImagePrototype.create(images.moveUp()));
        Button moveDownButton = new Button(constants.btnMoveDownTask(), AbstractImagePrototype.create(images.moveDown()));
        Button detailsButton = new Button(constants.btnDetailsTask(), AbstractImagePrototype.create(images.detailsTask()));
        Button resourcesButton = new Button(constants.btnResourcesTask());
        Button zoomInButton = new Button(constants.btnZoomInChart(), AbstractImagePrototype.create(images.zoomIn()));
        Button zoomOutButton = new Button(constants.btnZoomOutChart(), AbstractImagePrototype.create(images.zoomOut()));



        Button loadOrderButton = new Button("Выбрать Заказ");
        loadOrderButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {

                MessageBox.alert("Test0", storeForOrder.getModels().size() + "", null);
                List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

                ColumnConfig column = new ColumnConfig();
                column.setId("id");
                column.setWidth(20);
                column.setHeader("");
                column.setMenuDisabled(true);
                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("contract");
                //                column.setWidth(110);
                //                column.setHeader("Заказ");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("numDoc");
                //                column.setWidth(95);
                //                column.setHeader("Номенклатура");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                column = new ColumnConfig();
                column.setId("dateStart");
                column.setWidth(80);
                column.setHeader("Дата начала выполнения");
                column.setMenuDisabled(true);
                column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
                columns.add(column);

                column = new ColumnConfig();
                column.setId("dateEnd");
                column.setWidth(80);
                column.setHeader("Дата завершения");
                column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
                column.setMenuDisabled(true);
                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("companyName");
                //                column.setWidth(100);
                //                column.setHeader("Название компании");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("sumDoc");
                //                column.setWidth(60);
                //                column.setHeader("Сумма");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                column = new ColumnConfig();
                column.setId("orderName");
                column.setWidth(120);
                column.setHeader("Название заказа");
                column.setMenuDisabled(true);
                columns.add(column);

                column = new ColumnConfig();
                column.setId("departName");
                column.setWidth(50);
                column.setHeader("Название подразделения");
                column.setMenuDisabled(true);
                columns.add(column);

                column = new ColumnConfig();
                column.setId("typeExec");
                column.setWidth(50);
                column.setHeader("Признак выполнения");
                column.setMenuDisabled(true);
                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("info");
                //                column.setWidth(150);
                //                column.setHeader("Коментарий");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                //                column = new ColumnConfig();
                //                column.setId("urgency");
                //                column.setWidth(100);
                //                column.setHeader("Признак срочности");
                //                column.setMenuDisabled(true);
                //                columns.add(column);

                ColumnModel cm = new ColumnModel(columns);
                MessageBox.alert(("Test"), storeForOrder.getModels().size() + "", null);


                grid = new Grid<BeanModel>(storeForOrder, cm);
                grid.setStripeRows(true);
                grid.setLoadMask(true);
                grid.setAutoExpandColumn("id");
                grid.setBorders(true);

                grid.getView().setForceFit(true);
                grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
                HBoxLayoutData hBoxLayoutDataPanelChart = new HBoxLayoutData(new Margins(20, 20, 20, 20));
                hBoxLayoutDataPanelChart.setFlex(2);


                orderPanel.setHeading("Заказы");


                grid.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        final int idOrder = ((Order) grid.getSelectionModel().getSelectedItem().getBean()).getId();
                        //                        MessageBox.info("id of order", "id = " + idOrder, GanttChartPanel.this);

                        proxyForJob = new RpcProxy<List<Job>>() {
                            @Override
                            public void load(Object loadConfig, AsyncCallback<List<Job>> callback) {
                                GanttChartSearchCondition jsc = new GanttChartSearchCondition();
                                jsc.setId(idOrder);
                                service.getAllJobs(jsc, callback);
                            }
                        };

                        proxyForJobPlan = new RpcProxy<List<JobPlan>>() {
                            @Override
                            public void load(Object loadConfig, AsyncCallback<List<JobPlan>> callback) {
                                GanttChartSearchCondition jpsc = new GanttChartSearchCondition();
                                jpsc.setId(idOrder);
                                service.getAllJobPlans(jpsc, callback);
                            }
                        };

                        readerForJob = new BeanModelReader();
                        readerForJobPlan = new BeanModelReader();

                        loaderForJob = new BaseListLoader<ListLoadResult<ModelData>>(proxyForJob, readerForJob);
                        loaderForJobPlan = new BaseListLoader<ListLoadResult<ModelData>>(proxyForJobPlan, readerForJobPlan);

                        MessageBox.alert(("Test2"), storeForOrder.getModels().size() + "", null);
                        storeForJob = new ListStore<BeanModel>(loaderForJob);
                        storeForJobPlan = new ListStore<BeanModel>(loaderForJobPlan);

                        loaderForJob.load();
                        loaderForJobPlan.load();

                        listDataProvider.refresh();
                        MessageBox.alert(("Test3"), storeForOrder.getModels().size() + "", null);
                    }
                });

//                mainPanel.add(orderPanel);
//                grid.setHeight(orderPanel.getParent().getOffsetHeight() - orderPanel.getParent().getAbsoluteTop());
                ToolBar toolBar = new ToolBar();
                Button buildButton = new Button(constants.btnBuild(), AbstractImagePrototype.create(images.bAddResource()), new SelectionListener() {
                    @Override
                    public void componentSelected(ComponentEvent ce) {
                        List<BeanModel> jobs = storeForJob.getModels();
                        List<BeanModel> jobPlans = storeForJobPlan.getModels();
                        List<Job> jobList = new ArrayList<Job>();
                        List<JobPlan> jobPlanList = new ArrayList<JobPlan>();
                        for (BeanModel bm : jobs) {
                            Job job = (Job) bm.getBean();
                            jobList.add(job);
                        }
                        for (BeanModel bm : jobPlans) {
                            JobPlan jobPlan = (JobPlan) bm.getBean();
                            jobPlanList.add(jobPlan);
                        }
                        //                mainPanel.remove(orderPanel);
                        listDataProvider.setList(createTaskList(jobList, jobPlanList));
                        listDataProvider.refresh();
                    }
                });
                toolBar.add(buildButton);
                BorderLayoutData d = new BorderLayoutData(Style.LayoutRegion.SOUTH);
//                d.setMargins(new Margins(0, 0, 3, 0));
                OrderWindow window = new OrderWindow(toolBar, grid);
                window.show();

//                orderPanel.add(grid, d);
//                orderPanel.layout();
//                d.setSplit(true);
                loaderForOrder.load();

            }
        });

        toolBarForButtons.add(addButton);
        toolBarForButtons.add(removeButton);
        toolBarForButtons.add(detailsButton);
        toolBarForButtons.add(shiftLeftButton);
        toolBarForButtons.add(shiftRightButton);
        toolBarForButtons.add(moveUpButton);
        toolBarForButtons.add(moveDownButton);
        toolBarForButtons.add(resourcesButton);
        toolBarForButtons.add(zoomInButton);
        toolBarForButtons.add(zoomOutButton);
        toolBarForButtons.add(loadOrderButton);

        addButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                Task newTask = new Task();

                newTask.setName("Новое задание");
                newTask.setOrder(1);
                newTask.setUID(1);
                newTask.setStart(new Date());
                newTask.setFinish(DateUtil.addDays(newTask.getStart(), 1));

                if (taskList == null) {
                    taskList = new ArrayList<Task>();
                }

                if (taskList.isEmpty()) {
                    taskList.add(newTask);
                } else {
                    Task selectedTask = null;

                    for (Task task : taskList) {
                        if (selectionModel.isSelected(task)) {
                            selectedTask = task;
                            break;
                        }
                    }

                    TaskUtil.insertAbove(newTask, selectedTask, taskList);
                }

                listDataProvider.refresh();
            }
        });
        removeButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        TaskUtil.delete(task, taskList);
                        break;
                    }
                }

                listDataProvider.setList(taskList);
                listDataProvider.refresh();
            }
        });

        detailsButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        DetailsGanttChartWindow dgcw = new DetailsGanttChartWindow(task);
                        dgcw.show();
//                        callDetailsPanel(task);
                        break;
                    }
                }
            }
        });

        shiftLeftButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        TaskUtil.shiftLeft(task, taskList);
                        break;
                    }
                }

                listDataProvider.refresh();
            }
        });

        shiftRightButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        TaskUtil.shiftRight(task, taskList);
                        break;
                    }
                }

                listDataProvider.refresh();
            }
        });

        moveUpButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        TaskUtil.moveUp(task, taskList);
                        break;
                    }
                }

                listDataProvider.refresh();
            }
        });

        moveDownButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                for (Task task : taskList) {
                    if (selectionModel.isSelected(task)) {
                        TaskUtil.moveDown(task, taskList);
                        break;
                    }
                }

                listDataProvider.refresh();
            }
        });


        resourcesButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                mainPanel.remove(ganttChartPanel);
                mainPanel.remove(toolBarForButtons);
                resourcePanel = getResourcesPanel();
                mainPanel.add(resourcePanel);
                listDataProvider.refresh();
            }
        });

        zoomInButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                ganttChart.zoomIn();
                listDataProvider.refresh();
            }
        });

        zoomOutButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                ganttChart.zoomOut();
                listDataProvider.refresh();
            }
        });

        return toolBarForButtons;
    }
////////////////////////////////////

    private FlowPanel getDetailsPanel(final Task task) {

        editPanel = new FlowPanel();

        Button saveButton = new Button(constants.btnSaveDetails(), AbstractImagePrototype.create(images.save()));
        Button backButton = new Button(constants.btnBackDetails(), AbstractImagePrototype.create(images.back()));

        Label detailsLabel = new Label(constants.lblDetails());

        Label nameLabel = new Label(constants.lblTaskName());
        final TextBox nameTextBox = new TextBox();
        nameTextBox.setText(task.getName());

        Label durationLabel = new Label(constants.lblTaskDuration());
        final TextBox durationTextBox = new TextBox();
        durationTextBox.setValue(String.valueOf(task.getDuration()));

        Label completeLabel = new Label(constants.lblTaskComplete());
        final TextBox completeTextBox = new TextBox();
        completeTextBox.setValue(String.valueOf(task.getPercentComplete()));

        Label startLabel = new Label(constants.lblTaskStart());
        final DateBox startDateBox = new DateBox();
        startDateBox.setValue(task.getStart());


        Label finishLabel = new Label(constants.lblTaskFinish());
        final DateBox finishDateBox = new DateBox();
        finishDateBox.setValue(task.getFinish());

        Label predecessorsLabel = new Label(constants.lblTaskPredecessors());

//        final ComboBox predecessorsComboBox = new ComboBox();
//        predecessorsComboBox.setSelection(taskList);

        Button addPredButton = new Button(constants.btnAddPredecessor());
        addPredButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                //   task.addPredecessor(((Task)predecessorsComboBox.getValue()).getUID());
            }
        });

        Button removePredButton = new Button(constants.btnRemPredecessor());
        addPredButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                //   task.addPredecessor(((Task)predecessorsComboBox.getValue()).getUID());
            }
        });
        CellTable<Predecessor> predCellTable = new CellTable<Predecessor>();
        TextColumn<Predecessor> nameColumn = new TextColumn<Predecessor>() {
            @Override
            public String getValue(Predecessor object) {
                return String.valueOf(object.getUID());
            }
        };
        TextColumn<Predecessor> typeColumn = new TextColumn<Predecessor>() {
            @Override
            public String getValue(Predecessor object) {
                return object.getType().toString();
            }
        };
        predCellTable.addColumn(nameColumn, constants.colPredecessorName());
        predCellTable.addColumn(typeColumn, constants.colPredecessorType());
        predCellTable.setRowData(task.getPredecessors());

        Label resourcesLabel = new Label(constants.lblTaskResourcesName());

        ComboBox resourcesComboBox = new ComboBox();
//        resourcesComboBox.setSelection(resourceList);
        Button addResButton = new Button(constants.btnTaskAddResource(), AbstractImagePrototype.create(images.addResource()));
        addResButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                task.setDuration(Double.parseDouble(durationTextBox.getValue()));
                task.setFinish(finishDateBox.getValue());
                task.setName(nameTextBox.getValue());
                task.setPercentComplete(Integer.valueOf(completeTextBox.getValue()));
                task.setPredecessors(null);
                task.setStart(startDateBox.getValue());
                task.setNotes(null);
                listDataProvider.addDataDisplay(gridView);
                listDataProvider.getList().clear();
                listDataProvider.setList(taskList);
                listDataProvider.refresh();

            }
        });
        Button removeResButton = new Button(constants.btnTaskRemResource(), AbstractImagePrototype.create(images.remResource()));
        CellTable resCellTable = new CellTable();
//        
//        Label notesLabel = new Label("Заметка");
//        TextArea notesTextArea = new TextArea();
//        
        saveButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                //                task.addPredecessor(((Task)predecessorsComboBox.getValue()).getUID());
            }
        });


        backButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                mainPanel.addNorth(toolBarForButtons, 40);
                mainPanel.add(ganttChartPanel);
                mainPanel.remove(editPanel);
            }
        });


        editPanel.getElement().getStyle().setBackgroundColor("white");

        editPanel.add(detailsLabel);

        editPanel.add(saveButton);
        editPanel.add(backButton);

        editPanel.add(nameLabel);
        editPanel.add(nameTextBox);

        editPanel.add(durationLabel);
        editPanel.add(durationTextBox);

        editPanel.add(completeLabel);
        editPanel.add(completeTextBox);

        editPanel.add(startLabel);
        editPanel.add(startDateBox);

        editPanel.add(finishLabel);
        editPanel.add(finishDateBox);

        editPanel.add(predecessorsLabel);

//        detailsPanel.add(predecessorsComboBox);
        editPanel.add(addPredButton);
        editPanel.add(removePredButton);
        editPanel.add(predCellTable);
//        
        editPanel.add(resourcesLabel);
//        
//        detailsPanel.add(resourcesComboBox);
        editPanel.add(addResButton);
        editPanel.add(removeResButton);
        editPanel.add(resCellTable);
//        
//        detailsPanel.add(notesLabel);
//        detailsPanel.add(notesTextArea);


        return editPanel;

    }
/////////////////////////////////

    private FlowPanel getResourcesPanel() {

        resourcePanel = new FlowPanel();

        Label resourceLabel = new Label(constants.lblResourcesName());

        Button backButton = new Button(constants.btnBackResource(), AbstractImagePrototype.create(images.back()));
        backButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                mainPanel.addNorth(toolBarForButtons, 28);
                mainPanel.add(ganttChartPanel);
                mainPanel.remove(resourcePanel);
                listDataProvider.refresh();
            }
        });


        Button addResButton = new Button(constants.btnAddResource(), AbstractImagePrototype.create(images.addResource()));
        Button removeResButton = new Button(constants.btnRemResource(), AbstractImagePrototype.create(images.remResource()));
        CellTable<String> resourcesCellTable = new CellTable<String>();

        resourcePanel.add(backButton);
        resourcePanel.add(resourceLabel);
        resourcePanel.add(addResButton);
        resourcePanel.add(removeResButton);
        resourcePanel.add(resourcesCellTable);

        return resourcePanel;

    }
//////////////////////////

    private void callDetailsPanel(Task task) {
        mainPanel.remove(ganttChartPanel);
        mainPanel.remove(toolBarForButtons);
        editPanel = getDetailsPanel(task);
        mainPanel.add(editPanel);
    }
//////////////////////////////////////////

    public GanttChartPanel() {
        setHeading(constants.heading());
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        setBorders(false);

        toolBar = new ToolBar();
        service2 = GWT.create(GWTGanttChartMartService.class);
        service = GWT.create(GWTGanttChartPriboyService.class);
        proxyForOrder = new RpcProxy<List<Order>>() {
            @Override
            public void load(Object loadConfig, AsyncCallback<List<Order>> callback) {
                service.getAllOrders(callback);
            }
        };
        readerForOrder = new BeanModelReader();
        loaderForOrder = new BaseListLoader<ListLoadResult<ModelData>>(proxyForOrder, readerForOrder);
        storeForOrder = new ListStore<BeanModel>(loaderForOrder);
        loaderForOrder.addLoadListener(new LoadListener() {
            @Override
            public void handleEvent(LoadEvent e) {
                loaderForOrder.setSortField((String) grid.getState().get("sortField"));
                super.handleEvent(e);
                grid.unmask();

            }

            @Override
            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                super.loaderLoadException(le);
            }
        });
        loaderForOrder.setRemoteSort(true);

        listDataProvider = new ListDataProvider<Task>();
        selectionModel = new SingleSelectionModel();
        providesTask = new ProvidesTaskImpl();
        ganttChart = new GanttChart<Task>(providesTask);
        gridView = new GridView<Task>();
        gridView.addColumns(getColumns());
        gridView.enableVerticalScrolling(false);
        taskList = new ArrayList<Task>();
        gridView.setSelectionModel(selectionModel);
        ganttChart.setSelectionModel(selectionModel);
        ganttChart.addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                gridView.setVerticalScrollPosition(event.getRelativeElement().getScrollTop());
            }
        });
        listDataProvider.addDataDisplay(gridView);
        listDataProvider.addDataDisplay(ganttChart);
        listDataProvider.setList(taskList);
        ganttChartPanel = new SplitLayoutPanel();
        ganttChartPanel.addWest(gridView, 570);
        ganttChartPanel.add(ganttChart);
        ((Element) ganttChartPanel.getElement().getChildNodes().getItem(2)).getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.COL_RESIZE);
        mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.addNorth(getButtonsToolBar(), 28);
        mainPanel.add(ganttChartPanel);
        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER));
    }

    /**
     * <p>Возвращает список столбцов таблицы для Диаграммы Ганта. Столбцы:
     * порядок, название задачи, длительность, начало, окончание,
     * предшественники, названия ресурсов</p>
     *
     * @return список столбцов таблицы
     * @since 1.0
     */
    private List<TaskGridColumn> getColumns() {

        String dateFormatPattern = "EEE dd.MM.yyyy";
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(dateFormatPattern);

        List<TaskGridColumn> colums = new ArrayList<TaskGridColumn>();

        Column<Task, String> orderColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task object) {
                return String.valueOf(object.getOrder());
            }
        };

        Column<Task, Task> nameColumn = new Column<Task, Task>(
                new TaskGridNameCell() {
                    @Override
                    public void onExpandCollapse(Task task) {
                        listDataProvider.refresh();
                    }
                }) {
            @Override
            public Task getValue(Task object) {
                return object;
            }
        };

        nameColumn.setFieldUpdater(new FieldUpdater<Task, Task>() {
            @Override
            public void update(int index, Task object, Task value) {
                object.setName(value.getName());
                listDataProvider.refresh();
            }
        });

        Column<Task, String> durationColumn = new Column<Task, String>(
                new CellTextImpl(CellTextImpl.ALIGN_RIGHT) {
                    @Override
                    public String validate(String newValue, String oldValue) {
                        return newValue;
                    }
                }) {
            @Override
            public String getValue(Task object) {
                return String.valueOf(object.getDuration());
            }
        };
        durationColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
            @Override
            public void update(int index, Task object, String value) {
                double d = Double.parseDouble(value);

                if (d < 0) {
                    return;
                }

                object.setDuration(d);
                Date startDate = object.getStart();
                object.setFinish(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate() + (int) object.getDuration()));
                listDataProvider.refresh();
            }
        });

        Column<Task, Date> startColumn = new Column<Task, Date>(
                new DateCell(dateTimeFormat)) {
//  *              new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
//   +     startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() > object.getFinish().getTime()) {
//                    return;
//                }
//                object.setStart(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//     +   });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                new DateCell(dateTimeFormat)) {
//          *      new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
//        finishColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() < object.getStart().getTime()) {
//                    return;
//                }
//                object.setFinish(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, String> predecessorColumn = new Column<Task, String>(
                new EditTextCell()) {
            @Override
            public String getValue(Task object) {
                StringBuilder sb = new StringBuilder("");
                List<Predecessor> predecessorsList = object.getPredecessors();
                for (Predecessor predecessor : predecessorsList) {
                    for (Task task : taskList) {
                        if (task.getUID() == predecessor.getUID()) {
                            sb.append(task.getOrder());
                            sb.append(" ");
                            break;
                        }
                    }
                }
                if (sb.length() > 0) {
                    return sb.substring(0, sb.length() - 1);
                } else {
                    return "0";
                }
            }
        };
        predecessorColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
            @Override
            public void update(int index, Task object, String value) {
                List<Predecessor> predecessorsList = new ArrayList<Predecessor>();
                object.setPredecessors(predecessorsList);
                String[] predecessors = value.split(" ");
                for (String predecessor : predecessors) {
                    int order = Integer.parseInt(predecessor);
                    int UID = 0;
                    for (Task task : taskList) {
                        if (task.getOrder() == order) {
                            UID = task.getUID();
                            break;
                        }
                    }
                    if (UID > 0) {
                        object.addPredecessor(UID, PredecessorType.FS);
                    }
                }

                listDataProvider.refresh();
            }
        });

        Column<Task, String> resourceColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task object) {
                return "";
            }
        };

        colums.add(new TaskGridColumn<Task>(orderColumn, "", 45));
        colums.add(new TaskGridColumn<Task>(nameColumn, "Название задачи", 220));
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длит., дней", 45));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", 105));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", 105));
        colums.add(new TaskGridColumn<Task>(predecessorColumn, "Предшественники", 100));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", 300));

        return colums;
    }

    /**
     * <p>Возвращает список задач, который синтезируется в результате обработки
     * списков сущностей, полученных из БД. Список задач может быть использован
     * для построения Диаграммы Ганта.</p>
     *
     * @param jobList список задач (данные из DH_JOB)
     * @param listOfJobPlans список плановых операций (данные из DT_JOB_PLAN)
     * @return список задач
     * @since 1.0
     */
    private List<Task> createTaskList(List<Job> jobList, List<JobPlan> listOfJobPlans) {

        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }

        boolean error = false;
        Date tempDate = new Date();

        if (jobList != null) {
            for (int i = 0; i < jobList.size(); i++) {
                Job job = jobList.get(i);
                Task task = new Task();
                task.setLevel(getLevelOfParentTask(job.getIdParentJob()) + 1);
                task.setUID(job.getId());
                task.setName(job.getName());
                Date dateStart = job.getDateStart();
                Date dateFinish = job.getDateEnd();
                if ((dateStart == null | dateFinish != null)) {
                    dateStart = dateFinish;
                }
                if ((dateStart != null | dateFinish == null)) {
                    dateFinish = dateStart;
                }
                if ((dateStart == null | dateFinish == null)) {

                    dateStart = tempDate;
                    dateFinish = tempDate;
                }
                task.setStart(new Date(dateStart.getYear(), dateStart.getMonth(), dateStart.getDay()));
                task.setFinish(new Date(dateFinish.getYear(), dateFinish.getMonth(), dateFinish.getDay()));
                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart));
                taskList.add(task);
            }
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }

        error = false;

        if (listOfJobPlans != null) {
            for (int i = 0; i < listOfJobPlans.size(); i++) {
                JobPlan jobPlan = listOfJobPlans.get(i);
                int id = jobPlan.getId();
                int positionToPaste = 0;
                int level = 0;

                for (int j = 0; j < taskList.size(); j++) {
                    Task task = taskList.get(j);
                    if (task.getUID() == id) {
                        positionToPaste = j + 1;
                        task.setSummary(true);
                        level = task.getLevel() + 1;
                        break;
                    }
                }

                Task task = new Task();
                task.setUID(id + i * 1000);
                Date dateStart = jobPlan.getDateStart();
                Date dateFinish = jobPlan.getDateEnd();

                if ((dateStart == null | dateFinish != null)) {
                    dateStart = dateFinish;
                }
                if ((dateStart != null | dateFinish == null)) {
                    dateFinish = dateStart;
                }
                if ((dateStart == null | dateFinish == null)) {

                    dateStart = tempDate;
                    dateFinish = tempDate;
                }

                task.setStart(dateStart);
                task.setFinish(dateFinish);
                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart));
                task.setName(jobPlan.getName());
                task.setLevel(level);

                if (taskList.size() > positionToPaste) {
                    taskList.add(positionToPaste, task);
                } else {
                    taskList.add(task);
                }
            }
        } else {
            MessageBox.alert("Ошибка", "Список плановых операций не получен", null);
            return null;
        }

        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setOrder(i + 1);
        }

        return taskList;
    }

    /**
     * <p>Возвращает уровень родительской задачи, id которой передается методу.
     * Также, положительно устанвливает для родительской задачи флаг наличия
     * детей. В случае пустого списка задач или отсутствия в этом списке задачи
     * с заданным id возвращается значение равное -1</p>
     *
     * @param idOfParentTask id родительской задачи
     * @return уровень родительской задачи
     * @since 1.0
     */
    private static int getLevelOfParentTask(int idOfParentTask) {

        if (taskList != null) {
            for (Task task : taskList) {
                if (task.getUID() == idOfParentTask) {
                    task.setSummary(true);
                    return task.getLevel();
                }
            }
        }

        return -1;
    }
}
