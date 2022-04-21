package pst.arm.client.modules.ganttchart.portable;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.images.GanttChartImages;
import pst.arm.client.modules.ganttchart.lang.GanttChartConstants;
import pst.gwt.gwtgantt.DateUtil;
import pst.gwt.gwtgantt.TaskUtil;
import pst.gwt.gwtgantt.gantt.GanttChart;
import pst.gwt.gwtgantt.gantt.ProvidesTask;
import pst.gwt.gwtgantt.gantt.ProvidesTaskImpl;
import pst.gwt.gwtgantt.model.Predecessor;
import pst.gwt.gwtgantt.model.PredecessorType;
import pst.gwt.gwtgantt.model.Task;
import pst.gwt.gwtgantt.table.GridView;
import pst.gwt.gwtgantt.table.TaskGridColumn;
import pst.gwt.gwtgantt.table.TaskGridNameCell;
import pst.gwt.gwtgantt.table.override.CellTextImpl;

/**
 * @version 1.0
 * @author nikita
 */
public class GanttChartTest extends ContentPanel {

    private int orderWidth = 0;
    private int nameWidth = 0;
    private int durationWidth = 0;
    private int startWidth = 0;
    private int finishWidth = 0;
    private int predWidth = 50;
    private int resourceWidth = 0;
    private int sumWidth = 0;
    private int taskNumberWidth = 0;
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
    private List<Task> taskList;
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
    // Хранилища для получения данных с БД
    private ListStore<BeanModel> storeTest;
    List<DDataGrid> dataTest;

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

    private ToolBar getButtonsToolBarReadOnly() {

        toolBarForButtons = new ToolBar();
        toolBarForButtons.setSpacing(6);

        Button zoomInButton = new Button(constants.btnZoomInChart(), AbstractImagePrototype.create(images.zoomIn()));
        Button zoomOutButton = new Button(constants.btnZoomOutChart(), AbstractImagePrototype.create(images.zoomOut()));

        toolBarForButtons.add(zoomInButton);
        toolBarForButtons.add(zoomOutButton);

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

    private void callDetailsPanel(Task task) {
        mainPanel.remove(ganttChartPanel);
        mainPanel.remove(toolBarForButtons);
        editPanel = getDetailsPanel(task);
        mainPanel.add(editPanel);
    }

    public GanttChartTest(List<DDataGrid> data, int type) {

        setHeading(constants.heading());
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        setBorders(false);

        listDataProvider = new ListDataProvider<Task>();
        selectionModel = new SingleSelectionModel();
        providesTask = new ProvidesTaskImpl();
        ganttChart = new GanttChart<Task>(providesTask);
        gridView = new GridView<Task>();


//        taskList = new ArrayList<Task>();
        ////////////////////////

        switch (type) {
            case GanttChartPortablePanel.WORK_PLAN:
                createTaskList4(data);
                gridView.addColumns(getColumns());
                break;
            case GanttChartPortablePanel.WORK_PLAN_EXECUTOR:
                createTaskListForWorkPlanExecutor(data);
                gridView.addColumns(getColumnsForWorkPlanExecutor());
                break;
            case GanttChartPortablePanel.WORK_PLAN_EXECUTOR_NEW:
                createTaskListForWorkPlanExecutorNew(data);
                gridView.addColumns(getColumnsForWorkPlanExecutor());
                break;
        }

        gridView.enableVerticalScrolling(false);
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
//        listDataProvider.setList(taskList);
        ganttChartPanel = new SplitLayoutPanel();

        /////////west
        sumWidth = orderWidth + nameWidth + durationWidth + startWidth
                + finishWidth + predWidth + resourceWidth;
        sumWidth = sumWidth > com.google.gwt.user.client.Window.getClientWidth() - 14 ? com.google.gwt.user.client.Window.getClientWidth() - 14 : sumWidth;


        int maxWidth = (com.google.gwt.user.client.Window.getClientWidth() - 14) * 2 / 3;
        ganttChartPanel.addWest(gridView, (sumWidth > maxWidth) ? maxWidth : sumWidth);
        ganttChartPanel.add(ganttChart);

        ((Element) ganttChartPanel.getElement().getChildNodes().getItem(2)).getStyle().setCursor(Style.Cursor.COL_RESIZE);
        mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.addNorth(getButtonsToolBar(), 28);
        mainPanel.add(ganttChartPanel);
        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER));
//                MessageBox.alert("", ""+storeTest.getCount(), null);
        listDataProvider.setList(taskList);

        listDataProvider.refresh();
    }

    public GanttChartTest(List<DDataGrid> data) {

        setHeading(constants.heading());
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        setBorders(false);

        listDataProvider = new ListDataProvider<Task>();
        selectionModel = new SingleSelectionModel();
        providesTask = new ProvidesTaskImpl();
        ganttChart = new GanttChart<Task>(providesTask);
        gridView = new GridView<Task>();

        ///////////////
        createTaskList2(data);
        gridView.addColumns(getColumns2());

        gridView.enableVerticalScrolling(false);
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
        ganttChartPanel = new SplitLayoutPanel();
        /////////west

        sumWidth = orderWidth + nameWidth + durationWidth + startWidth
                + finishWidth + predWidth + resourceWidth;
        sumWidth = sumWidth > com.google.gwt.user.client.Window.getClientWidth() - 14 ? com.google.gwt.user.client.Window.getClientWidth() - 14 : sumWidth;


        int maxWidth = (com.google.gwt.user.client.Window.getClientWidth() - 14) * 2 / 3;
        ganttChartPanel.addWest(gridView, (sumWidth > maxWidth) ? maxWidth : sumWidth);
        ganttChartPanel.add(ganttChart);

        ((Element) ganttChartPanel.getElement().getChildNodes().getItem(2)).getStyle().setCursor(Style.Cursor.COL_RESIZE);
        mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.addNorth(getButtonsToolBarReadOnly(), 28);
        mainPanel.add(ganttChartPanel);
        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER));

        listDataProvider.setList((List<Task>) taskList);

        listDataProvider.refresh();

    }

    /**
     *
     * @param data
     */
    public GanttChartTest(List<Job2> data, String text) {
        setHeading(constants.heading());
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        setBorders(false);

        listDataProvider = new ListDataProvider<Task>();
        selectionModel = new SingleSelectionModel();
        providesTask = new ProvidesTaskImpl();
        ganttChart = new GanttChart<Task>(providesTask);
        gridView = new GridView<Task>();

        //////////
        createTaskList3(data);
        gridView.addColumns(getColumns3(text));


        gridView.enableVerticalScrolling(false);
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
        ganttChartPanel = new SplitLayoutPanel();
        /////////west
        sumWidth = orderWidth + nameWidth + durationWidth + startWidth
                + finishWidth + predWidth + resourceWidth + taskNumberWidth;
        sumWidth = sumWidth > com.google.gwt.user.client.Window.getClientWidth() - 14 ? com.google.gwt.user.client.Window.getClientWidth() - 14 : sumWidth;


        int maxWidth = (com.google.gwt.user.client.Window.getClientWidth() - 14) * 2 / 3;
        ganttChartPanel.addWest(gridView, (sumWidth > maxWidth) ? maxWidth : sumWidth);
        ganttChartPanel.add(ganttChart);

        ((Element) ganttChartPanel.getElement().getChildNodes().getItem(2)).getStyle().setCursor(Style.Cursor.COL_RESIZE);
        mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.addNorth(getButtonsToolBarReadOnly(), 28);
        mainPanel.add(ganttChartPanel);
        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER));

        listDataProvider.setList((List<Task>) taskList);

        listDataProvider.refresh();

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
                return String.valueOf(object.getNotes().substring(0, object.getNotes().indexOf(" ")));
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
                Task task = object.clone();
                task.setName(task.getName().substring(task.getName().indexOf(" "), task.getName().indexOf(". Подр:")));
                return task;
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
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getStart();
//            }
//        };
//        startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() > object.getFinish().getTime()) {
//                    return;
//                }
//                object.setStart(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                //new DateCell(format)) {
                new DateCell(dateTimeFormat)) {
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getFinish();
//            }
//        };
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
                return String.valueOf(object.getNotes().substring(object.getNotes().indexOf(" ") + 1));
            }
        };

        orderWidth = orderWidth * 7;
        nameWidth = (nameWidth + 3) * 7;
        durationWidth = (durationWidth + 3) * 7;
        startWidth *= 7;
        finishWidth *= 7;
        resourceWidth *= 7;
        orderWidth = orderWidth > 80 ? 80 : orderWidth;
        nameWidth = nameWidth > 220 ? 220 : nameWidth;
        durationWidth = durationWidth > 80 ? 80 : durationWidth;
        startWidth = startWidth > 105 ? 105 : startWidth;
        finishWidth = finishWidth > 105 ? 105 : finishWidth;
        resourceWidth = resourceWidth > 700 ? 700 : resourceWidth;

        colums.add(new TaskGridColumn<Task>(orderColumn, "", orderWidth));
        colums.add(new TaskGridColumn<Task>(nameColumn, "Содержание работ", nameWidth));
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длит., дней", durationWidth));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", startWidth));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", finishWidth));
        colums.add(new TaskGridColumn<Task>(predecessorColumn, "Предшественники", predWidth));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", resourceWidth));

        return colums;
    }

    private List<TaskGridColumn> getColumns2() {

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
                Task task = object.clone();
                task.setName(task.getName());
                return task;
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
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getStart();
//            }
//        };
//        startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() > object.getFinish().getTime()) {
//                    return;
//                }
//                object.setStart(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                //new DateCell(format)) {
                new DateCell(dateTimeFormat)) {
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getFinish();
//            }
//        };
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
                return String.valueOf(object.getNotes());
            }
        };

        orderWidth = orderWidth * 7;
        nameWidth = (nameWidth + 3) * 7;
        durationWidth = (durationWidth + 3) * 7;
        startWidth *= 7;
        finishWidth *= 7;
        resourceWidth *= 7;
        orderWidth = orderWidth > 80 ? 80 : orderWidth;
        nameWidth = nameWidth > 220 ? 220 : nameWidth;
        durationWidth = durationWidth > 80 ? 80 : durationWidth;
        startWidth = startWidth > 105 ? 105 : startWidth;
        finishWidth = finishWidth > 105 ? 105 : finishWidth;
        resourceWidth = resourceWidth > 700 ? 700 : resourceWidth;

        colums.add(new TaskGridColumn<Task>(orderColumn, "", orderWidth));
        colums.add(new TaskGridColumn<Task>(nameColumn, "Номер заказа", nameWidth));
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длит., дней", durationWidth));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", startWidth));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", finishWidth));
        colums.add(new TaskGridColumn<Task>(predecessorColumn, "Предшественники", predWidth));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", resourceWidth));

        return colums;
    }

    private List<TaskGridColumn> getColumns3(String text) {

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
                Task task = object.clone();
                task.setName(task.getName().substring(0, task.getName().lastIndexOf(" ")));
                return task;
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
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getStart();
//            }
//        };
//        startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() > object.getFinish().getTime()) {
//                    return;
//                }
//                object.setStart(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                //new DateCell(format)) {
                new DateCell(dateTimeFormat)) {
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getFinish();
//            }
//        };
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
                return String.valueOf(object.getNotes().substring(0, object.getNotes().lastIndexOf(" ", object.getNotes().lastIndexOf(" ") - 1)));
            }
        };

        Column<Task, String> numdocColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task object) {
                return String.valueOf(object.getNotes().substring(object.getNotes().lastIndexOf(" ", object.getNotes().lastIndexOf(" ") - 1), object.getNotes().lastIndexOf(" ")));
            }
        };

        Column<Task, String> parentNumdocColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task object) {
                String text = String.valueOf(object.getNotes().substring(object.getNotes().lastIndexOf(" ") + 1));
                if (text.equals("null")) {
                    return "";
                } else {
                    return text;
                }
            }
        };

        orderWidth = orderWidth * 7;
        nameWidth = (nameWidth + 3) * 7;
        durationWidth = (durationWidth + 3) * 7;
        startWidth *= 7;
        finishWidth *= 7;
        predWidth *= 7;
        resourceWidth = (resourceWidth + 19) * 7;
        taskNumberWidth *= 7;
        orderWidth = orderWidth > 80 ? 80 : orderWidth;
        nameWidth = nameWidth > 220 ? 220 : nameWidth;
        durationWidth = durationWidth > 80 ? 80 : durationWidth;
        startWidth = startWidth > 105 ? 105 : startWidth;
        finishWidth = finishWidth > 105 ? 105 : finishWidth;
        resourceWidth = resourceWidth > 700 ? 700 : resourceWidth;
        predWidth = predWidth > 100 ? 100 : predWidth;
        taskNumberWidth = taskNumberWidth > 70 ? 70 : taskNumberWidth;

        colums.add(new TaskGridColumn<Task>(orderColumn, "", orderWidth));
        colums.add(new TaskGridColumn<Task>(numdocColumn, "Номер задания", taskNumberWidth));
        if (text != null) {
            colums.add(new TaskGridColumn<Task>(nameColumn, "Чертеж (наименование) (" + text + ")", nameWidth));
        } else {
            colums.add(new TaskGridColumn<Task>(nameColumn, "Чертеж (наименование)", nameWidth));
        }
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длит., дней", durationWidth));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", startWidth));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", finishWidth));
        colums.add(new TaskGridColumn<Task>(parentNumdocColumn, "Предшественники", predWidth));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", resourceWidth));

        return colums;
    }

    private List<TaskGridColumn> getColumnsForWorkPlanExecutor() {

        String dateFormatPattern = "EEE dd.MM.yyyy";
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(dateFormatPattern);

        List<TaskGridColumn> colums = new ArrayList<TaskGridColumn>();

        Column<Task, String> orderColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task task) {
                return ((TaskForWorkPlanExecutor) task).getWorkNumber();
            }
        };


//        TaskGridNameCell cell = new TaskGridNameCell() {
//            @Override
//            public void onExpandCollapse(Task task) {
//                listDataProvider.refresh();
//            }
//
//            @Override
//            public boolean isEditing(Context context, com.google.gwt.dom.client.Element element, Task value) {
//                listDataProvider.refresh();
//                return false;
//            }
//
//            @Override
//            public boolean resetFocus(Context context, com.google.gwt.dom.client.Element parent, Task value) {
//                listDataProvider.refresh();
//                return true;
//            }
//         
//        };


//        Column<Task, Task> nameColumn = new Column<Task, Task>(cell) {
//            @Override
//            public Task getValue(Task task) {
//                return task;
//            }
//        };

//        Column<Task, Task> nameColumn = new Column<Task, Task>(cell) {
//            @Override
//            public Task getValue(Task task) {
//                return task;
//            }
//        };

//        nameColumn.setFieldUpdater(new FieldUpdater<Task, Task>() {
//            @Override
//            public void update(int index, Task object, Task value) {
//                object.setName(value.getName());
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, String> nameColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task task) {
                return task.getName();
            }
        };

        Column<Task, String> durationColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task task) {
                return String.valueOf(task.getDuration());
            }
        };
        durationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        durationColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
//            @Override
//            public void update(int index, Task object, String value) {
//                double d = Double.parseDouble(value);
//
//                if (d < 0) {
//                    return;
//                }
//
//                object.setDuration(d);
//                Date startDate = object.getStart();
//                object.setFinish(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate() + (int) object.getDuration()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, Date> startColumn = new Column<Task, Date>(
                new DateCell(dateTimeFormat)) {
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
        startColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getStart();
//            }
//        };
//        startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//            @Override
//            public void update(int index, Task object, Date value) {
//                if (value.getTime() > object.getFinish().getTime()) {
//                    return;
//                }
//                object.setStart(value);
//                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                //new DateCell(format)) {
                new DateCell(dateTimeFormat)) {
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
        finishColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//                new NewCellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
//            @Override
//            public Date getValue(Task object) {
//                return object.getFinish();
//            }
//        };
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
                new TextCell()) {
            @Override
            public String getValue(Task task) {
                return ((TaskForWorkPlanExecutor) task).getPrevWorkNumber();
//                StringBuilder sb = new StringBuilder("");
//                List<Predecessor> predecessorsList = object.getPredecessors();
//                for (Predecessor predecessor : predecessorsList) {
//                    for (Task task : taskList) {
//                        if (task.getUID() == predecessor.getUID()) {
//                            sb.append(task.getOrder());
//                            sb.append(" ");
//                            break;
//                        }
//                    }
//                }
//                if (sb.length() > 0) {
//                    return sb.substring(0, sb.length() - 1);
//                } else {
//                    return "0";
//                }

            }
        };
        predecessorColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        predecessorColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
//            @Override
//            public void update(int index, Task object, String value) {
//                List<Predecessor> predecessorsList = new ArrayList<Predecessor>();
//                object.setPredecessors(predecessorsList);
//                String[] predecessors = value.split(" ");
//                for (String predecessor : predecessors) {
//                    int order = Integer.parseInt(predecessor);
//                    int UID = 0;
//                    for (Task task : taskList) {
//                        if (task.getOrder() == order) {
//                            UID = task.getUID();
//                            break;
//                        }
//                    }
//                    if (UID > 0) {
//                        object.addPredecessor(UID, PredecessorType.FS);
//                    }
//                }
//
//                listDataProvider.refresh();
//            }
//        });

        Column<Task, String> resourceColumn = new Column<Task, String>(
                new TextCell()) {
            @Override
            public String getValue(Task task) {
                return task.getNotes();
            }
        };

        orderWidth = orderWidth * 7;
        nameWidth = (nameWidth + 3) * 10;
        durationWidth = (durationWidth + 3) * 7;
        startWidth *= 7;
        finishWidth *= 7;
        resourceWidth *= 7;

        orderWidth = orderWidth < 50 ? 50 : orderWidth;
        nameWidth = nameWidth < 100 ? 100 : nameWidth;
        durationWidth = durationWidth < 50 ? 50 : durationWidth;
        startWidth = startWidth < 90 ? 90 : startWidth;
        finishWidth = finishWidth < 90 ? 90 : finishWidth;
        predWidth = predWidth < 50 ? 50 : predWidth;
        resourceWidth = resourceWidth < 200 ? 200 : resourceWidth;

        orderWidth = orderWidth > 80 ? 80 : orderWidth;
        nameWidth = nameWidth > 220 ? 220 : nameWidth;
        durationWidth = durationWidth > 80 ? 80 : durationWidth;
        startWidth = startWidth > 105 ? 105 : startWidth;
        finishWidth = finishWidth > 105 ? 105 : finishWidth;
        resourceWidth = resourceWidth > 700 ? 700 : resourceWidth;

        colums.add(new TaskGridColumn<Task>(orderColumn, "", orderWidth));
        colums.add(new TaskGridColumn<Task>(nameColumn, "Содержание работ", nameWidth));
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длит., дней", durationWidth));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", startWidth));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", finishWidth));
        colums.add(new TaskGridColumn<Task>(predecessorColumn, "Предшественники", predWidth));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", resourceWidth));

        return colums;
    }

    private class Work {

        private String workName;
        private String shortName;
        private Date startDate;
        private Date finishDate;
        private String workNumber;
        private String departId;
        private double planLabour;
        private double planCost;
        private double planCostNorm;

        /**
         * @return the workName
         */
        public String getWorkName() {
            return workName;
        }

        /**
         * @param workName the workName to set
         */
        public void setWorkName(String workName) {
            this.workName = workName;
        }

        /**
         * @return the shortName
         */
        public String getShortName() {
            return shortName;
        }

        /**
         * @param shortName the shortName to set
         */
        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        /**
         * @return the startDate
         */
        public Date getStartDate() {
            return startDate;
        }

        /**
         * @param startDate the startDate to set
         */
        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        /**
         * @return the finishDate
         */
        public Date getFinishDate() {
            return finishDate;
        }

        /**
         * @param finishDate the finishDate to set
         */
        public void setFinishDate(Date finishDate) {
            this.finishDate = finishDate;
        }

        /**
         * @return the workNumber
         */
        public String getWorkNumber() {
            return workNumber;
        }

        /**
         * @param workNumber the workNumber to set
         */
        public void setWorkNumber(String workNumber) {
            this.workNumber = workNumber;
        }

        /**
         * @return the departId
         */
        public String getDepartId() {
            return departId;
        }

        /**
         * @param departId the departId to set
         */
        public void setDepartId(String departId) {
            this.departId = departId;
        }

        /**
         * @return the planLabour
         */
        public double getPlanLabour() {
            return planLabour;
        }

        /**
         * @param planLabour the planLabour to set
         */
        public void setPlanLabour(double planLabour) {
            this.planLabour = planLabour;
        }

        /**
         * @return the planCost
         */
        public double getPlanCost() {
            return planCost;
        }

        /**
         * @param planCost the planCost to set
         */
        public void setPlanCost(double planCost) {
            this.planCost = planCost;
        }

        /**
         * @return the planCostNumber
         */
        public double getPlanCostNorm() {
            return planCostNorm;
        }

        /**
         * @param planCostNumber the planCostNumber to set
         */
        public void setPlanCostNorm(double planCostNorm) {
            this.planCostNorm = planCostNorm;
        }

        public Work(String workName, String shortName, Date startDate, Date finishDate, String workNumber, String departId, double planLabour, double planCost, double planCostNorm) {
            this.workName = workName;
            this.shortName = shortName;
            this.startDate = startDate;
            this.finishDate = finishDate;
            this.workNumber = workNumber;
            this.departId = departId;
            this.planLabour = planLabour;
            this.planCost = planCost;
            this.planCostNorm = planCostNorm;
        }

        public Work() {
        }
    }

    private List<Task> createTaskList4(List<DDataGrid> data) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        ArrayList<Work> combinedList = new ArrayList<Work>();

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {

                DDataGrid ddg = data.get(i);
                Map<SKeyForColumn, IRowColumnVal> rows = ddg.getRows();
                Work work = new Work();

                SKeyForColumn testKey = new SKeyForColumn();

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("WORK_NAME");
                String work_name = rows.get(testKey).getVal().toString();

                work.setWorkName(work_name);

                testKey.setTableAlias("DEPART");
                testKey.setColumnName("SHORT_NAME");
                String short_name;
                if (rows.get(testKey).getVal() != null) {
                    short_name = rows.get(testKey).getVal().toString();
                } else {
                    short_name = "";
                }

                work.setShortName(short_name);

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("BEG_DATE");
                Date dateStart = null;

                boolean flag = false;
                try {
                    dateStart = (Date) rows.get(testKey).getVal();
                } catch (Exception e) {
                    flag = true;
                }
                testKey.setColumnName("END_DATE");
                Date dateFinish = null;
                try {
                    dateFinish = (Date) rows.get(testKey).getVal();
                    if (flag) {
                        dateStart = dateFinish;
                    }
                } catch (Exception e) {
                    if (flag) {
                        dateStart = dateFinish = new Date();
                    } else {
                        dateFinish = dateStart;
                    }
                }

                work.setStartDate(DateUtil.reset(dateStart));
                work.setFinishDate(DateUtil.reset(dateFinish));

                testKey.setColumnName("WORK_NUMBER");
                String work_number;
                if (rows.get(testKey).getVal() != null) {
                    work_number = rows.get(testKey).getVal().toString();
                } else {
                    work_number = "";
                }

                work.setWorkNumber(work_number);

                testKey.setColumnName("DEPART_ID");
                String departId;
                if (rows.get(testKey).getVal() != null) {
                    departId = rows.get(testKey).getVal().toString();
                } else {
                    departId = "";
                }

                work.setDepartId(departId);

                testKey.setColumnName("PLAN_LABOUR");
                double planLabour;
                if (rows.get(testKey).getVal() != null) {
                    planLabour = (Double) rows.get(testKey).getVal();
                } else {
                    planLabour = 0;
                }

                work.setPlanLabour(planLabour);

                testKey.setColumnName("PLAN_COST");
                double planCost;
                if (rows.get(testKey).getVal() != null) {
                    planCost = (Double) rows.get(testKey).getVal();
                } else {
                    planCost = 0;
                }

                work.setPlanCost(planCost);

                testKey.setColumnName("PLAN_COST_NORM");
                double planCostNorm;
                if (rows.get(testKey).getVal() != null) {
                    planCostNorm = (Double) rows.get(testKey).getVal();
                } else {
                    planCostNorm = 0;
                }

                work.setPlanCostNorm(planCostNorm);

                boolean isNew = true;

                for (int j = 0; j < combinedList.size(); ++j) {
                    Work alreadyExistingWork = combinedList.get(j);
                    if (alreadyExistingWork.getDepartId().equals(work.getDepartId()) && alreadyExistingWork.getWorkName().equals(work.getWorkName())
                            && alreadyExistingWork.getWorkNumber().equals(work.getWorkNumber())) {

                        if (alreadyExistingWork.getStartDate().compareTo(work.getStartDate()) > 0) {
                            alreadyExistingWork.setStartDate(work.getStartDate());
                        }
                        if (alreadyExistingWork.getFinishDate().compareTo(work.getFinishDate()) < 0) {
                            alreadyExistingWork.setFinishDate(work.getFinishDate());
                        }
                        alreadyExistingWork.setPlanLabour(alreadyExistingWork.getPlanLabour() + work.getPlanLabour());
                        alreadyExistingWork.setPlanCost(alreadyExistingWork.getPlanCost() + work.getPlanCost());
                        alreadyExistingWork.setPlanCostNorm(alreadyExistingWork.getPlanCostNorm() + work.getPlanCostNorm());
                        isNew = false;
                        break;
                    }
                }

                if (combinedList.isEmpty() || isNew) {
                    combinedList.add(work);
                }

            }
            for (int i = 0; i < combinedList.size(); i++) {
                Work work = combinedList.get(i);

                Task task = new Task();
                task.setOrder(i + 1);
                task.setUID(i + 1);
                task.setLevel(0);

                String work_name = work.getWorkName();
                if (work_name != null) {
                    nameWidth = nameWidth < work_name.length() ? work_name.length() : nameWidth;
                } else {
                    nameWidth = nameWidth == 0 ? 15 : nameWidth;
                }
                String short_name = work.getShortName();

                Date dateStart = work.getStartDate();
                Date dateFinish = work.getFinishDate();

                task.setStart(DateUtil.reset(dateStart));
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }
                task.setFinish(DateUtil.reset(dateFinish));
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }

                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);

                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;

                String work_number = work.getWorkNumber();

                task.setNotes(work_number);
                orderWidth = orderWidth < work_number.length() ? work_number.length() : orderWidth;

                task.setName(work_number + " " + work_name + ". Подр:" + short_name);

                String departId = work.getDepartId();

                String planLabour = NumberFormat.getFormat("0.00").format(work.getPlanLabour());

                String planCost = NumberFormat.getFormat("0.00").format(work.getPlanCost());

                String planCostNorm = NumberFormat.getFormat("0.00").format(work.getPlanCostNorm());

                String var = " Подр. " + short_name + " | ч/м " + planLabour
                        + " | РОТ руб " + planCost + " | РОТ норм. Руб " + planCostNorm;

                task.setNotes(task.getNotes() + var);

                resourceWidth = resourceWidth < var.length() ? var.length() : resourceWidth;

                taskList.add(task);
            }
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }
        return taskList;
    }

    private class WorkExecutor {

        private Integer id;
        private Integer parentId;
        private String workPrevNum;
        private String workNum;
        private String name;
        private Date begDate;
        private Date endDate;
        private String workName;
        private String executors;
        private String workIds;
        private String resIds;

        public String getWorkIds() {
            return workIds;
        }

        public void setWorkIds(String workIds) {
            this.workIds = workIds;
        }

        public String getResIds() {
            return resIds;
        }

        public void setResIds(String resIds) {
            this.resIds = resIds;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getParentId() {
            return parentId;
        }

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }

        public WorkExecutor() {
        }

        public String getWorkPrevNum() {
            return workPrevNum;
        }

        public void setWorkPrevNum(String workPrevNum) {
            this.workPrevNum = workPrevNum;
        }

        public String getWorkNum() {
            return workNum;
        }

        public void setWorkNum(String workNum) {
            this.workNum = workNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBegDate() {
            return begDate;
        }

        public void setBegDate(Date begDate) {
            this.begDate = begDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getWorkName() {
            return workName;
        }

        public void setWorkName(String workName) {
            this.workName = workName;
        }

        public String getExecutors() {
            return executors;
        }

        public void setExecutors(String executors) {
            this.executors = executors;
        }
    }

    public class TaskForWorkPlanExecutor extends Task {

        private String workNumber;
        private String prevWorkNumber;

        public String getPrevWorkNumber() {
            return prevWorkNumber;
        }

        public void setPrevWorkNumber(String prevWorkNumber) {
            this.prevWorkNumber = prevWorkNumber;
        }

        public String getWorkNumber() {
            return workNumber;
        }

        public void setWorkNumber(String workNumber) {
            this.workNumber = workNumber;
        }
    }

    private List<Task> createTaskListForWorkPlanExecutor(List<DDataGrid> data) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        ArrayList<WorkExecutor> combinedList = new ArrayList<WorkExecutor>();

        List<Integer> parents = new ArrayList<Integer>(data.size());

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {

                DDataGrid ddg = data.get(i);
                Map<SKeyForColumn, IRowColumnVal> rows = ddg.getRows();
                WorkExecutor workExecutor = new WorkExecutor();

                SKeyForColumn testKey = new SKeyForColumn();

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("WORK_NUMBER");

                String workNum = rows.get(testKey).getVal().toString();

                workExecutor.setWorkNum(workNum);

                testKey.setColumnName("WORK_PLAN_EXECUTOR_ID");

                Integer id = (Integer) rows.get(testKey).getVal();

                workExecutor.setId(id);
                parents.add(id);


                testKey.setColumnName("PARENT_ID");

                Integer parentId;
                if (rows.get(testKey).getVal() != null) {
                    parentId = (Integer) rows.get(testKey).getVal();
                } else {
                    parentId = null;
                }

                workExecutor.setParentId(parentId);

                testKey.setColumnName("WORK_NAME");

                String workName = rows.get(testKey).getVal().toString();

                workExecutor.setWorkName(workName);

                testKey.setTableAlias("WORKER");
                testKey.setColumnName("NAME");
                String workerName = rows.get(testKey).getVal().toString();

                workExecutor.setName(workerName);

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("BEG_DATE");
//                Date begDate = null;
//                boolean flag = false;
//                try {
                Date begDate = (Date) rows.get(testKey).getVal();
//                } catch (Exception e) {
//                    flag = true;
//                }
                testKey.setColumnName("END_DATE");
//                Date endDate = null;
//                try {
                Date endDate = (Date) rows.get(testKey).getVal();
//                    if (flag) {
//                        begDate = endDate;
//                    }
//                } catch (Exception e) {
//                    if (flag) {
//                        begDate = endDate = new Date();
//                    } else {
//                        endDate = begDate;
//                    }
//                }

                workExecutor.setBegDate(DateUtil.reset(begDate));
                workExecutor.setEndDate(DateUtil.reset(endDate));

                testKey.setTableAlias("WORK_PLAN_EXECUTOR_FOR_PREVIOUS");
                testKey.setColumnName("WORK_NUMBER");
                String prevWorkNum;
                if (rows.get(testKey).getVal() != null) {
                    prevWorkNum = rows.get(testKey).getVal().toString();
                } else {
                    prevWorkNum = "";
                }

                workExecutor.setWorkPrevNum(prevWorkNum);

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("EXECUTORS");
                String executors;
                if (rows.get(testKey).getVal() != null) {
                    executors = rows.get(testKey).getVal().toString();
                } else {
                    executors = "";
                }

                workExecutor.setExecutors(executors);

                boolean isNew = true;

//                for (int j = 0; j < combinedList.size(); ++j) {
//                    WorkExecutor alreadyExistingWork = combinedList.get(j);
//                    if (alreadyExistingWork.getDepartId().equals(work.getDepartId()) && alreadyExistingWork.getWorkName().equals(work.getWorkName())
//                            && alreadyExistingWork.getWorkNumber().equals(work.getWorkNumber())) {
//
//                        if (alreadyExistingWork.getStartDate().compareTo(work.getStartDate()) > 0) {
//                            alreadyExistingWork.setStartDate(work.getStartDate());
//                        }
//                        if (alreadyExistingWork.getFinishDate().compareTo(work.getFinishDate()) < 0) {
//                            alreadyExistingWork.setFinishDate(work.getFinishDate());
//                        }
//                        alreadyExistingWork.setPlanLabour(alreadyExistingWork.getPlanLabour() + work.getPlanLabour());
//                        alreadyExistingWork.setPlanCost(alreadyExistingWork.getPlanCost() + work.getPlanCost());
//                        alreadyExistingWork.setPlanCostNorm(alreadyExistingWork.getPlanCostNorm() + work.getPlanCostNorm());
//                        isNew = false;
//                        break;
//                    }
//                }

                if (combinedList.isEmpty() || isNew) {
                    combinedList.add(workExecutor);
                }

            }


            for (int i = 0; i < combinedList.size(); i++) {
                WorkExecutor workExecutor = combinedList.get(i);

                TaskForWorkPlanExecutor task = new TaskForWorkPlanExecutor();
                task.setOrder(i + 1);
                task.setUID(i + 1);
                task.setLevel(0);


                String workName = workExecutor.getWorkName();
                if (workName != null) {
                    nameWidth = nameWidth < workName.length() ? workName.length() : nameWidth;
                } else {
                    nameWidth = nameWidth == 0 ? 15 : nameWidth;
                }

                Date dateStart = workExecutor.getBegDate();
                Date dateFinish = workExecutor.getEndDate();

                task.setStart(DateUtil.reset(dateStart));
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }
                task.setFinish(DateUtil.reset(dateFinish));
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }

                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);

                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;

                String workNum = workExecutor.getWorkNum();

                task.setWorkNumber(workNum);

                String prevWorkNum = workExecutor.getWorkPrevNum();

                task.setPrevWorkNumber(prevWorkNum);

                orderWidth = orderWidth < workNum.length() ? workNum.length() : orderWidth;

                String executors = "Отв. " + workExecutor.getName();
                if (workExecutor.getExecutors().length() > 0) {
                    executors += ". Исп.: " + workExecutor.getExecutors();
                }

                task.setNotes(executors);
                resourceWidth = resourceWidth < executors.length() ? executors.length() : resourceWidth;

                task.setName(workName);
//                if (workExecutor.getParentId() != null) {
//                    int pId = parents.indexOf(workExecutor.getParentId()) + 1;
//                    if (pId > 0) {
//                        task.getPredecessors().add(new Predecessor(pId, PredecessorType.FS));
//                    }
//                }

                predWidth = predWidth < prevWorkNum.length() ? prevWorkNum.length() : predWidth;

                taskList.add(task);
            }
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }
        return taskList;
    }

    private List<Task> createTaskListForWorkPlanExecutorNew(List<DDataGrid> data) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        ArrayList<WorkExecutor> combinedList = new ArrayList<WorkExecutor>();

        List<Integer> parents = new ArrayList<Integer>(data.size());

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {

                DDataGrid ddg = data.get(i);
                Map<SKeyForColumn, IRowColumnVal> rows = ddg.getRows();
                WorkExecutor workExecutor = new WorkExecutor();

                SKeyForColumn testKey = new SKeyForColumn();

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("WORK_NUMBER");

                String workNum = rows.get(testKey).getVal().toString();

                workExecutor.setWorkNum(workNum);

                testKey.setColumnName("WORK_PLAN_EXECUTOR_ID");

                Integer id = (Integer) rows.get(testKey).getVal();

                workExecutor.setId(id);
                parents.add(id);

//                testKey.setColumnName("PARENT_ID");
//                Integer parentId;
//                if (rows.get(testKey).getVal() != null) {
//                    parentId = (Integer) rows.get(testKey).getVal();
//                } else {
//                    parentId = null;
//                }
//
//                workExecutor.setParentId(parentId);
                testKey.setColumnName("WORK_IDS");

                String workIds;
                if (rows.get(testKey).getVal() != null) {
                    workIds = (String) rows.get(testKey).getVal();
                } else {
                    workIds = null;
                }

                workExecutor.setWorkIds(workIds);

                testKey.setColumnName("RES_IDS");

                String resIds;
                if (rows.get(testKey).getVal() != null) {
                    resIds = (String) rows.get(testKey).getVal();
                } else {
                    resIds = null;
                }

                workExecutor.setResIds(resIds);

                testKey.setColumnName("WORK_NAME");

                String workName = rows.get(testKey).getVal().toString();

                workExecutor.setWorkName(workName);

                testKey.setTableAlias("WORKER");
                testKey.setColumnName("NAME");
                String workerName = rows.get(testKey).getVal().toString();

                workExecutor.setName(workerName);

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("BEG_DATE");
//                Date begDate = null;
//                boolean flag = false;
//                try {
                Date begDate = (Date) rows.get(testKey).getVal();
//                } catch (Exception e) {
//                    flag = true;
//                }
                testKey.setColumnName("END_DATE");
//                Date endDate = null;
//                try {
                Date endDate = (Date) rows.get(testKey).getVal();
//                    if (flag) {
//                        begDate = endDate;
//                    }
//                } catch (Exception e) {
//                    if (flag) {
//                        begDate = endDate = new Date();
//                    } else {
//                        endDate = begDate;
//                    }
//                }

                workExecutor.setBegDate(DateUtil.reset(begDate));
                workExecutor.setEndDate(DateUtil.reset(endDate));

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("WORKS");
                String prevWorkNum;
                if (rows.get(testKey).getVal() != null) {
                    prevWorkNum = rows.get(testKey).getVal().toString();
                } else {
                    prevWorkNum = "";
                }

                workExecutor.setWorkPrevNum(prevWorkNum);

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("EXECUTORS");
                String executors;
                if (rows.get(testKey).getVal() != null) {
                    executors = rows.get(testKey).getVal().toString();
                } else {
                    executors = "";
                }

                workExecutor.setExecutors(executors);

                boolean isNew = true;

//                for (int j = 0; j < combinedList.size(); ++j) {
//                    WorkExecutor alreadyExistingWork = combinedList.get(j);
//                    if (alreadyExistingWork.getDepartId().equals(work.getDepartId()) && alreadyExistingWork.getWorkName().equals(work.getWorkName())
//                            && alreadyExistingWork.getWorkNumber().equals(work.getWorkNumber())) {
//
//                        if (alreadyExistingWork.getStartDate().compareTo(work.getStartDate()) > 0) {
//                            alreadyExistingWork.setStartDate(work.getStartDate());
//                        }
//                        if (alreadyExistingWork.getFinishDate().compareTo(work.getFinishDate()) < 0) {
//                            alreadyExistingWork.setFinishDate(work.getFinishDate());
//                        }
//                        alreadyExistingWork.setPlanLabour(alreadyExistingWork.getPlanLabour() + work.getPlanLabour());
//                        alreadyExistingWork.setPlanCost(alreadyExistingWork.getPlanCost() + work.getPlanCost());
//                        alreadyExistingWork.setPlanCostNorm(alreadyExistingWork.getPlanCostNorm() + work.getPlanCostNorm());
//                        isNew = false;
//                        break;
//                    }
//                }

                if (combinedList.isEmpty() || isNew) {
                    combinedList.add(workExecutor);
                }

            }

            for (int i = 0; i < combinedList.size(); i++) {
                WorkExecutor workExecutor = combinedList.get(i);

                TaskForWorkPlanExecutor task = new TaskForWorkPlanExecutor();
                task.setOrder(i + 1);
                task.setUID(i + 1);
                task.setLevel(0);


                String workName = workExecutor.getWorkName();
                if (workName != null) {
                    nameWidth = nameWidth < workName.length() ? workName.length() : nameWidth;
                } else {
                    nameWidth = nameWidth == 0 ? 15 : nameWidth;
                }

                Date dateStart = workExecutor.getBegDate();
                Date dateFinish = workExecutor.getEndDate();

                task.setStart(DateUtil.reset(dateStart));
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }
                task.setFinish(DateUtil.reset(dateFinish));
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }

                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);

                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;

                String workNum = workExecutor.getWorkNum();

                task.setWorkNumber(workNum);

                String prevWorkNum = workExecutor.getWorkPrevNum();

                task.setPrevWorkNumber(prevWorkNum);

                orderWidth = orderWidth < workNum.length() ? workNum.length() : orderWidth;

                String executors = "Отв. " + workExecutor.getName();
                if (workExecutor.getExecutors().length() > 0) {
                    executors += ". Исп.: " + workExecutor.getExecutors();
                }

                task.setNotes(executors);
                resourceWidth = resourceWidth < executors.length() ? executors.length() : resourceWidth;

                task.setName(workName);
//                if (workExecutor.getParentId() != null) {
//                    int pId = parents.indexOf(workExecutor.getParentId()) + 1;
//                    if (pId > 0) {
//                        task.getPredecessors().add(new Predecessor(pId, PredecessorType.FS));
//                    }
//                }

                if (workExecutor.getWorkIds() != null) {
                    String[] ids = workExecutor.getWorkIds().split(",");
                    int pId = -1;
                    for (int k = 0; k < ids.length; ++k) {
                        pId = parents.indexOf(Integer.parseInt(ids[k])) + 1;
                        if (pId > 0) {
                            task.getPredecessors().add(new Predecessor(pId, PredecessorType.FS));
                        }
                    }
                }

                if (workExecutor.getResIds() != null) {

                    String[] ids = workExecutor.getResIds().split(",");
                    int pId = -1;
                    for (int k = 0; k < ids.length; ++k) {
                        pId = parents.indexOf(Integer.parseInt(ids[k])) + 1;
                        if (pId > 0) {
                            task.getPredecessors().add(new Predecessor(pId, PredecessorType.CC));
                        }
                    }
                }

                predWidth = predWidth < prevWorkNum.length() ? prevWorkNum.length() : predWidth;

                taskList.add(task);
            }
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }
        return taskList;
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
    private List<Task> createTaskList(List<DDataGrid> data) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        if (data != null) {


            for (int i = 0; i < data.size(); i++) {
                DDataGrid ddg = data.get(i);
                Map<SKeyForColumn, IRowColumnVal> rows = ddg.getRows();
//                Set<SKeyForColumn> set = rows.keySet();
//                Iterator<SKeyForColumn> it = set.iterator();
//                for (int k = 0; k < set.size(); k++) {
//                    MessageBox.alert("" + it.next(), "", null);
//                }
                Task task = new Task();
                task.setOrder(i + 1);
                task.setUID(i + 1);
                task.setLevel(0);
                SKeyForColumn testKey = new SKeyForColumn();

                testKey.setTableAlias("MAIN");
                testKey.setColumnName("WORK_NAME");
                String work_name = rows.get(testKey).getVal().toString();
                if (work_name != null) {
                    nameWidth = nameWidth < work_name.length() ? work_name.length() : nameWidth;
                } else {
                    nameWidth = nameWidth == 0 ? 15 : nameWidth;
                }
                testKey.setTableAlias("DEPART");
                testKey.setColumnName("SHORT_NAME");
//                MessageBox.alert("", ""+((DDataGrid) bm.getBean()).getRows().get(key).getVal(), null);
                String short_name;
                if (rows.get(testKey).getVal() != null) {
                    short_name = rows.get(testKey).getVal().toString();
                } else {
                    short_name = "";
                }
                testKey.setTableAlias("MAIN");
                testKey.setColumnName("BEG_DATE");
                Date dateStart = null;
//                String dateFormatPattern = "dd.MM.yyyy";
//                DateTimeFormat dateFormat = DateTimeFormat.getFormat(dateFormatPattern);
                boolean flag = false;
                try {
//                    varDate = (Date) rows.get(testKey).getVal();
//                    dateStart = DateUtil.reset(varDate);
                    dateStart = (Date) rows.get(testKey).getVal();
                } catch (Exception e) {
//                    dateStart = DateUtil.reset(new Date());
                    flag = true;
                }
                testKey.setColumnName("END_DATE");
                Date dateFinish = null;
                try {
//                    varDate = (Date) rows.get(testKey).getVal();
//                    dateFinish = DateUtil.reset(varDate);
                    dateFinish = (Date) rows.get(testKey).getVal();
                    if (flag) {
                        dateStart = dateFinish;
                    }
                } catch (Exception e) {
//                    dateFinish = DateUtil.reset(new Date());
                    if (flag) {
                        dateStart = dateFinish = new Date();
                    } else {
                        dateFinish = dateStart;
                    }
                }

                task.setStart(DateUtil.reset(dateStart));
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }
                task.setFinish(DateUtil.reset(dateFinish));
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }
//                task.setFinish(dateFinish);
                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);

                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;

                testKey.setColumnName("WORK_NUMBER");
                String work_number;
                if (rows.get(testKey).getVal() != null) {
                    work_number = rows.get(testKey).getVal().toString();
                } else {
                    work_number = "";
                }

                task.setNotes(work_number);
                orderWidth = orderWidth < work_number.length() ? work_number.length() : orderWidth;



                task.setName(work_number + " " + work_name + ". Подр:" + short_name);

                testKey.setColumnName("DEPART_ID");
                String departId;
                if (rows.get(testKey).getVal() != null) {
                    departId = rows.get(testKey).getVal().toString();
                } else {
                    departId = "";
                }


                testKey.setColumnName("PLAN_LABOUR");

                String planLabour;
                if (rows.get(testKey).getVal() != null) {
                    planLabour = rows.get(testKey).getVal().toString();
                } else {
                    planLabour = "";
                }

                testKey.setColumnName("PLAN_COST");
                String planCost;
                if (rows.get(testKey).getVal() != null) {
                    planCost = rows.get(testKey).getVal().toString();
                } else {
                    planCost = "";
                }

                testKey.setColumnName("PLAN_COST_NORM");
                String planCostNorm;
                if (rows.get(testKey).getVal() != null) {
                    planCostNorm = rows.get(testKey).getVal().toString();
                } else {
                    planCostNorm = "";
                }

                String var = " Подр. " + short_name + " | ч/м " + planLabour
                        + " | РОТ руб " + planCost + " | РОТ норм. Руб " + planCostNorm;

                task.setNotes(task.getNotes() + var);

                resourceWidth = resourceWidth < var.length() ? var.length() : resourceWidth;


//                task.setLevel(getLevelOfParentTask(job.getIdParentJob()) + 1);
//                task.setUID(job.getId());

//                MessageBox.alert("Ошибка", o[0].getVal() + " " + o[1].getVal() + " " + 
//                        o[2].getVal() + " " + o[3].getVal() + " " + o[4].getVal() + " " + o[5].getVal() + " " + 
//                        o[6].getVal() + " " + o[7].getVal() + " " + o[8].getVal() + " " + o[9].getVal() + " " + 
//                        o[10].getVal() + " " + o[11].getVal() + " " + o[12].getVal() + " " + o[13].getVal() + " " + 
//                        o[14].getVal() + " " + o[15].getVal() + " ", null);


                taskList.add(task);
            }
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }

//        error = false;

//        if (listOfJobPlans != null) {
//            for (int i = 0; i < listOfJobPlans.size(); i++) {
//                JobPlan jobPlan = listOfJobPlans.get(i);
//                int id = jobPlan.getId();
//                int positionToPaste = 0;
//                int level = 0;
//
//                for (int j = 0; j < taskList.size(); j++) {
//                    Task task = taskList.get(j);
//                    if (task.getUID() == id) {
//                        positionToPaste = j + 1;
//                        task.setSummary(true);
//                        level = task.getLevel() + 1;
//                        break;
//                    }
//                }
//
//                Task task = new Task();
//                task.setUID(id + i * 1000);
//                Date dateStart = jobPlan.getDateStart();
//                Date dateFinish = jobPlan.getDateEnd();
//
//                if (dateStart == null | dateFinish == null) {
//                    if (!error) {
//                        MessageBox.alert("Ошибка", "У плановой(ых) операции(й) отсутсвует дата запуска или окончания, будут использованы тестовые даты", null);
//                        error = true;
//                    }
//                    dateStart = new Date();
//                    dateFinish = new Date();
//                }
//
//                task.setStart(dateStart);
//                task.setFinish(dateFinish);
//                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart));
//                task.setName(jobPlan.getName());
//                task.setLevel(level);
//
//                if (taskList.size() > positionToPaste) {
//                    taskList.add(positionToPaste, task);
//                } else {
//                    taskList.add(task);
//                }
//            }
//        } else {
//            MessageBox.alert("Ошибка", "Список плановых операций не получен", null);
//            return null;
//        }

//        for (int i = 0; i < taskList.size(); i++) {
//            taskList.get(i).setOrder(i + 1);
//        }

        return taskList;
    }

    private List<Task> createTaskList2(List<DDataGrid> data) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                DDataGrid ddg = data.get(i);
                Map<SKeyForColumn, IRowColumnVal> rows = ddg.getRows();
                Task task = new Task();
                task.setOrder(i + 1);
                orderWidth = orderWidth < String.valueOf(task.getOrder()).length() ? String.valueOf(task.getOrder()).length() : orderWidth;
                task.setUID(i + 1);
                task.setLevel(0);

                SKeyForColumn key = new SKeyForColumn();

                key.setTableAlias("MAIN");
                key.setColumnName("NUMDOC");
                try {
                    task.setName(rows.get(key).getVal().toString());
                } catch (Exception e) {
                    task.setName("");
                }
                nameWidth = nameWidth < task.getName().length() ? task.getName().length() : nameWidth;

                key.setColumnName("DATESTART");
                Date dateStart = null;
                String dateFormatPattern = "dd.MM.yyyy";
                DateTimeFormat dateFormat = DateTimeFormat.getFormat(dateFormatPattern);

                try {
                    dateStart = dateFormat.parse(rows.get(key).getVal().toString());
                } catch (Exception e) {
                }

                key.setColumnName("DATEEND");
                Date dateFinish = null;
                try {
                    dateFinish = dateFormat.parse(rows.get(key).getVal().toString());
                } catch (Exception e) {
                }

//                MessageBox.alert("Ошибка"+i, "3"+dateStart, null);
//                MessageBox.alert("Ошибка"+i, "4"+dateFinish, null);
                task.setStart(DateUtil.reset(dateStart));
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }
                task.setFinish(DateUtil.reset(dateFinish));
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }
                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);
                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;
                key.setColumnName("TYPE_EXEC");
                String typeExec;
                if (rows.get(key).getVal() != null) {
                    typeExec = rows.get(key).getVal().toString();
                } else {
                    typeExec = "";
                }

                key.setColumnName("STATUS");
                String status;
                if (rows.get(key).getVal() != null) {
                    status = rows.get(key).getVal().toString();
                } else {
                    status = "";
                }
                key.setTableAlias("SPR_DEPART");
                key.setColumnName("NAME");
                String name;
                if (rows.get(key).getVal() != null) {
                    name = rows.get(key).getVal().toString();
                } else {
                    name = "";
                }

                SKeyForColumn keyDateEnd = new SKeyForColumn("MAIN", "DATEEND");
                SKeyForColumn keyIsVirt = new SKeyForColumn("MAIN", "IS_VIRT");
                SKeyForColumn keyIsBill = new SKeyForColumn("MAIN", "IS_BILL");
                SKeyForColumn keyTypeExec = new SKeyForColumn("MAIN", "TYPE_EXEC");
                Date curr = new Date();
                curr = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(curr));

                String strDateEnd = (String) null;
                Date dateEnd = (Date) null;

                Integer isVirt = 0;
                Integer isBill = 0;
                Integer typeExec2 = 0;

                if (rows.get(keyIsVirt).getVal() != null) {
                    isVirt = (Integer) rows.get(keyIsVirt).getVal();
                }
                if (rows.get(keyIsBill).getVal() != null) {
                    isBill = (Integer) rows.get(keyIsBill).getVal();
                }
                if (rows.get(keyTypeExec).getVal() != null) {
                    typeExec2 = (Integer) rows.get(keyTypeExec).getVal();
                }
                if (rows.get(keyDateEnd).getVal() != null) {
                    strDateEnd = (String) rows.get(keyDateEnd).getVal();
                }
                if (strDateEnd != null && !strDateEnd.isEmpty()) {
                    dateEnd = DateTimeFormat.getFormat("dd.MM.yyyy").parse(strDateEnd);
                }

                status = "Не определен";

                if (isVirt != 1 && isBill == 0 && typeExec2 != 4) {
                    if (dateEnd != null) {
                        if (dateEnd.before(curr) || dateEnd.equals(curr)) {
                            status = "«Реальный» заказ, не завершен, срок прошел";
                        } else if (dateEnd.after(curr)) {
                            status = "«Реальный» заказ, не выполнен, срок завершения не прошел";

                        }
                    }
                } else if (isVirt != 1 && isBill == 0 && typeExec2 == 4) {
                    status = "«Реальный» заказ, завершен";
                } else if (isVirt == 1 || isBill == 1) {
                    status = "Виртуальный или Счет";
                } else {
                    status = "Не определен";
                }

                switch (typeExec2) {
                    case 1:
                        typeExec = "Предварительный";
                        break;
                    case 2:
                        typeExec = "Не запущен";
                        break;
                    case 3:
                        typeExec = "В работе";
                        break;
                    case 4:
                        typeExec = "Завершен";
                        break;
                    case 5:
                        typeExec = "Приостановлен";
                        break;
                    default:
                        typeExec = "Не определен";

                }

                task.setNotes("Подр.: " + name + " | " + typeExec
                        + " | состояние: " + status);
                resourceWidth = resourceWidth < task.getNotes().length() ? task.getNotes().length() : resourceWidth;
                taskList.add(task);
            }

//            MessageBox.alert("1 " + testDateS1 + " 2 " + testDateS2, "1 " + testDateF1 + " 2 " + testDateF2, null);
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }

        return taskList;
    }

    private List<Task> createTaskList3(List<Job2> data) {

        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Job2 job = data.get(i);
                Task task = new Task();
                task.setOrder(i + 1);
                orderWidth = orderWidth < String.valueOf(task.getOrder()).length() ? String.valueOf(task.getOrder()).length() : orderWidth;

                task.setUID(job.getId());
                task.setLevel(getLevelOfParentTask(job.getIdParentJob(), taskList) + 1);

                try {
                    task.setName(job.getName() + " (" + job.getId() + ")");
                } catch (Exception e) {
                    task.setName("");
                }
                if (job.getName() != null) {
                    nameWidth = nameWidth < job.getName().length() ? job.getName().length() : nameWidth;
                } else {
                    nameWidth = nameWidth == 0 ? 15 : nameWidth;
                }

                Date dateStart = job.getDateStart();
                Date dateFinish = job.getDateEnd();

//                MessageBox.alert("Ошибка"+i, "3"+dateStart, null);
//                MessageBox.alert("Ошибка"+i, "4"+dateFinish, null);
                task.setStart(dateStart);
                if (task.getStart() != null) {
                    startWidth = startWidth < task.getStart().toString().length() ? task.getStart().toString().length() : startWidth;
                } else {
                    startWidth = startWidth == 0 ? 15 : startWidth;
                }

                task.setFinish(dateFinish);
                if (task.getFinish() != null) {
                    finishWidth = finishWidth < task.getFinish().toString().length() ? task.getFinish().toString().length() : finishWidth;
                } else {
                    finishWidth = finishWidth == 0 ? 15 : finishWidth;
                }
                if (dateFinish == null && dateStart != null) {
                    task.setFinish(dateStart);
                }

                task.setDuration(DateUtil.differenceInDays(dateFinish, dateStart) + 1);

                durationWidth = durationWidth < String.valueOf(task.getDuration()).length() ? String.valueOf(task.getDuration()).length() : durationWidth;

                String typeExec;
                if (job.getTypeExec() != null) {
                    typeExec = job.getTypeExec().toString();
                } else {
                    typeExec = "";
                }

//                String status;
//                if (job.getName() != null) {
//                    status = job.getName();
//                } else {
//                    status = "";
//                }

                String name;
                if (job.getIdDepart() != null) {
                    name = job.getName2();
                } else {
                    name = "";
                }

                task.setNotes("Подр.: " + name + " | выполнен "/* + typeExec
                         + " | состояние: " + status*/ + job.getNumDoc() + " " + job.getNumDoc2());
                if (name != null) {
                    resourceWidth = resourceWidth < name.length() ? name.length() : resourceWidth;
                } else {
                    resourceWidth = resourceWidth == 0 ? 15 : resourceWidth;
                }

                if (job.getNumDoc() != null) {

                    taskNumberWidth = taskNumberWidth < job.getNumDoc().length() ? job.getNumDoc().length() : taskNumberWidth;
                } else {
                    taskNumberWidth = taskNumberWidth == 0 ? 15 : taskNumberWidth;
                }
                if (job.getNumDoc2() != null) {
                    predWidth = predWidth < job.getNumDoc2().length() ? job.getNumDoc2().length() : predWidth;
                } else {
                    predWidth = predWidth == 0 ? 15 : predWidth;
                }
                taskList.add(task);
            }

//            for (int i = 0; i < data.size(); i++) {
//                taskList.get(i).
//            }

//            MessageBox.alert("1 " + testDateS1 + " 2 " + testDateS2, "1 " + testDateF1 + " 2 " + testDateF2, null);
        } else {
            MessageBox.alert("Ошибка", "Список задач не получен", null);
            return null;
        }



        return taskList;
    }

    private static int getLevelOfParentTask(int idOfParentTask, List<Task> taskList) {

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
