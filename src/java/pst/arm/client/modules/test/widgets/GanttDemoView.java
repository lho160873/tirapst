package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
import pst.gwt.gwtgantt.gantt.GanttChart;
import pst.gwt.gwtgantt.gantt.ProvidesTask;
import pst.gwt.gwtgantt.gantt.ProvidesTaskImpl;
import pst.gwt.gwtgantt.model.Predecessor;
import pst.gwt.gwtgantt.model.PredecessorType;
import pst.gwt.gwtgantt.model.Task;
import pst.gwt.gwtgantt.table.GridView;
import pst.gwt.gwtgantt.table.TaskGridColumn;
import pst.gwt.gwtgantt.table.TaskGridNameCell;
import pst.gwt.gwtgantt.table.override.CellDateImpl;
import pst.gwt.gwtgantt.table.override.CellTextImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GanttDemoView extends Composite {

    private ListDataProvider<Task> listDataProvider = null;
    private SelectionModel<Task> selectionModel = null;
    private ProvidesTask<Task> provider = null;
    private GanttChart<Task> ganttChart = null;
    private GridView<Task> gridView = null;
    private List<Task> taskList = null;
    private List<String> resourceList = null;
    private DockLayoutPanel mainPanel = null;
    private SplitLayoutPanel ganttPanel = null;
    private FlowPanel buttonsPanel = null;
    private FlowPanel detailsPanel = null;
    private FlowPanel resourcePanel = null;

    private FlowPanel getMainButtonsPanel() {

        buttonsPanel = new FlowPanel();

        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");
        Button shiftLeftButton = new Button("Сдвинуть Влево");
        Button shiftRightButton = new Button("Сдвинуть Вправо");
        Button moveUpButton = new Button("Переместить Вверх");
        Button moveDownButton = new Button("Переместить Вниз");

        Button detailsButton = new Button("Изменить");

        Button resourcesButton = new Button("Ресурсы");

        Button zoomInButton = new Button("Увеличить");
        Button zoomOutButton = new Button("Уменьшить");

        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(detailsButton);
        buttonsPanel.add(shiftLeftButton);
        buttonsPanel.add(shiftRightButton);
        buttonsPanel.add(moveUpButton);
        buttonsPanel.add(moveDownButton);
        buttonsPanel.add(resourcesButton);
        buttonsPanel.add(zoomInButton);
        buttonsPanel.add(zoomOutButton);


        addButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        addButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        addButton.getElement().getStyle().setLeft(0, Style.Unit.PX);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onAddClick(event);
            }
        });

        removeButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        removeButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        removeButton.getElement().getStyle().setLeft(72, Style.Unit.PX);
        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onRemoveClick(event);
            }
        });

        detailsButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        detailsButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        detailsButton.getElement().getStyle().setLeft(632, Style.Unit.PX);
        detailsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDetailsClick(event);
            }
        });

        shiftLeftButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        shiftLeftButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        shiftLeftButton.getElement().getStyle().setLeft(137, Style.Unit.PX);
        shiftLeftButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onShiftLeftClick(event);
            }
        });

        shiftRightButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        shiftRightButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        shiftRightButton.getElement().getStyle().setLeft(251, Style.Unit.PX);
        shiftRightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onShiftRightClick(event);
            }
        });

        moveUpButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        moveUpButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        moveUpButton.getElement().getStyle().setLeft(372, Style.Unit.PX);
        moveUpButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onMoveUpClick(event);
            }
        });

        moveDownButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        moveDownButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        moveDownButton.getElement().getStyle().setLeft(506, Style.Unit.PX);
        moveDownButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onMoveDownClick(event);
            }
        });

        resourcesButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        resourcesButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        resourcesButton.getElement().getStyle().setLeft(720, Style.Unit.PX);
        resourcesButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mainPanel.remove(ganttPanel);
                mainPanel.remove(buttonsPanel);
                resourcePanel = getResourcesPanel();
                mainPanel.add(resourcePanel);
                listDataProvider.refresh();
            }
        });

        zoomInButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        zoomInButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        zoomInButton.getElement().getStyle().setRight(84, Style.Unit.PX);
        zoomInButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onZoomInClick(event);
            }
        });

        zoomOutButton.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        zoomOutButton.getElement().getStyle().setTop(0, Style.Unit.PX);
        zoomOutButton.getElement().getStyle().setRight(0, Style.Unit.PX);
        zoomOutButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onZoomOutClick(event);
            }
        });

        return buttonsPanel;
    }

    private FlowPanel getDetailsPanel(final Task task) {

        detailsPanel = new FlowPanel();

        Button saveButton = new Button("Сохранить Изменения");


        Button backButton = new Button("Вернуться К Списку Задач");


        Label detailsLabel = new Label("Сведения О Задаче");


        Label nameLabel = new Label("Название Задачи: ");
        final TextBox nameTextBox = new TextBox();
        nameTextBox.setText(task.getName());

        Label durationLabel = new Label("Длительность: ");
        final TextBox durationTextBox = new TextBox();
        durationTextBox.setValue(String.valueOf(task.getDuration()));

        Label completeLabel = new Label("Завершено(%): ");
        final TextBox completeTextBox = new TextBox();
        completeTextBox.setValue(String.valueOf(task.getPercentComplete()));

        Label startLabel = new Label("Начало: ");
        final DateBox startDateBox = new DateBox();
        startDateBox.setValue(task.getStart());


        Label finishLabel = new Label("Окончание: ");
        final DateBox finishDateBox = new DateBox();
        finishDateBox.setValue(task.getFinish());

        Label predecessorsLabel = new Label("Предшественники");

//        final ComboBox predecessorsComboBox = new ComboBox();

//        predecessorsComboBox.setSelection(taskList);
        Button addPredButton = new Button("Добавить");
        addPredButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
//                task.addPredecessor(((Task)predecessorsComboBox.getValue()).getUID());
            }
        });
        Button removePredButton = new Button("Удалить");
        addPredButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
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
        predCellTable.addColumn(nameColumn, "Имя");
        predCellTable.addColumn(typeColumn, "Тип");
        predCellTable.setRowData(task.getPredecessors());


        Label resourcesLabel = new Label("Названия Ресурсов");

        ComboBox resourcesComboBox = new ComboBox();
//        resourcesComboBox.setSelection(resourceList);
        Button addResButton = new Button("Добавить");
        addResButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });
        Button removeResButton = new Button("Удалить");
        CellTable resCellTable = new CellTable();
//        
//        Label notesLabel = new Label("Заметка");
//        TextArea notesTextArea = new TextArea();
//        
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

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

        backButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mainPanel.addNorth(buttonsPanel, 28);
                mainPanel.add(ganttPanel);
                mainPanel.remove(detailsPanel);
            }
        });

        detailsPanel.getElement().getStyle().setBackgroundColor("white");

        detailsPanel.add(detailsLabel);

        detailsPanel.add(saveButton);
        detailsPanel.add(backButton);

        detailsPanel.add(nameLabel);
        detailsPanel.add(nameTextBox);

        detailsPanel.add(durationLabel);
        detailsPanel.add(durationTextBox);

        detailsPanel.add(completeLabel);
        detailsPanel.add(completeTextBox);

        detailsPanel.add(startLabel);
        detailsPanel.add(startDateBox);

        detailsPanel.add(finishLabel);
        detailsPanel.add(finishDateBox);

        detailsPanel.add(predecessorsLabel);

//        detailsPanel.add(predecessorsComboBox);
        detailsPanel.add(addPredButton);
        detailsPanel.add(removePredButton);
        detailsPanel.add(predCellTable);
//        
        detailsPanel.add(resourcesLabel);
//        
//        detailsPanel.add(resourcesComboBox);
        detailsPanel.add(addResButton);
        detailsPanel.add(removeResButton);
        detailsPanel.add(resCellTable);
//        
//        detailsPanel.add(notesLabel);
//        detailsPanel.add(notesTextArea);

        return detailsPanel;

    }

    private FlowPanel getResourcesPanel() {

        resourcePanel = new FlowPanel();

        Label resourceLabel = new Label("Ресурсы");

        Button backButton = new Button("Вернуться К Списку Задач");
        backButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mainPanel.addNorth(buttonsPanel, 28);
                mainPanel.add(ganttPanel);
                mainPanel.remove(resourcePanel);
                listDataProvider.refresh();
            }
        });

        Button addResButton = new Button("Добавить");
        Button removeResButton = new Button("Удалить");
        CellTable<String> resourcesCellTable = new CellTable<String>();

        resourcePanel.add(backButton);
        resourcePanel.add(resourceLabel);
        resourcePanel.add(addResButton);
        resourcePanel.add(removeResButton);
        resourcePanel.add(resourcesCellTable);

        return resourcePanel;

    }

    private void callDetailsPanel(Task task) {
        mainPanel.remove(ganttPanel);
        mainPanel.remove(buttonsPanel);
        detailsPanel = getDetailsPanel(task);
        mainPanel.add(detailsPanel);
    }

    public GanttDemoView() {

        listDataProvider = new ListDataProvider<Task>();
        selectionModel = new SingleSelectionModel();

        provider = new ProvidesTaskImpl();
        ganttChart = new GanttChart<Task>(provider);
        gridView = new GridView<Task>();

        gridView.addColumns(getColumns());
        taskList = new ArrayList<Task>();

        gridView.setSelectionModel(selectionModel);
        ganttChart.setSelectionModel(selectionModel);

        listDataProvider.addDataDisplay(gridView);
        listDataProvider.addDataDisplay(ganttChart);
        listDataProvider.setList(taskList);

        ganttPanel = new SplitLayoutPanel();
        ganttPanel.addWest(gridView, 570);
        ganttPanel.add(ganttChart);

        mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.addNorth(getMainButtonsPanel(), 28);
        mainPanel.add(ganttPanel);

        initWidget(mainPanel);

    }

    void onAddClick(ClickEvent e) {

        Task newTask = new Task();

        newTask.setName("Новое задание");
        newTask.setOrder(1);
        newTask.setUID(1);
        newTask.setStart(new Date());
        newTask.setFinish(new Date());

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

    void onRemoveClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                TaskUtil.delete(task, taskList);
                break;
            }

        }

        listDataProvider.setList(taskList);
        listDataProvider.refresh();

    }

    void onDetailsClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                callDetailsPanel(task);
                break;
            }

        }

    }

    void onShiftLeftClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                TaskUtil.shiftLeft(task, taskList);
                break;
            }

        }

        listDataProvider.refresh();

    }

    void onShiftRightClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                TaskUtil.shiftRight(task, taskList);
                break;
            }

        }

        listDataProvider.refresh();

    }

    void onMoveUpClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                TaskUtil.moveUp(task, taskList);
                break;
            }

        }

        listDataProvider.refresh();

    }

    void onMoveDownClick(ClickEvent e) {

        for (Task task : taskList) {

            if (selectionModel.isSelected(task)) {
                TaskUtil.moveDown(task, taskList);
                break;
            }

        }

        listDataProvider.refresh();

    }

    void onZoomInClick(ClickEvent e) {

        ganttChart.zoomIn();
        listDataProvider.refresh();

    }

    void onZoomOutClick(ClickEvent e) {

        ganttChart.zoomOut();
        listDataProvider.refresh();

    }

    private List<TaskGridColumn> getColumns() {

        String dateFormatPattern = "EEE dd.MM.yy";
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
                        return newValue; // was oldValue. Why?
                    }
                }) {
            @Override
            public String getValue(Task object) {
                return String.valueOf(object.getDuration() + " дней");
            }
        };
        durationColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
            @Override
            public void update(int index, Task object, String value) {
                value = value.replaceAll("дней", "");
                value = value.replaceAll("день", "");
                value = value.replaceAll("дня", "");
                value = value.replaceAll("дн", "");
                value = value.replaceAll("д", "");
                if (Double.parseDouble(value) < 0) {
                    return;
                }
                object.setDuration(Double.parseDouble(value));
                Date startDate = object.getStart();
                object.setFinish(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate() + (int) object.getDuration()));
                listDataProvider.refresh();
            }
        });

        Column<Task, Date> startColumn = new Column<Task, Date>(
                //                new DateCell(format)) {
                new CellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
            @Override
            public Date getValue(Task object) {
                return object.getStart();
            }
        };
        startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
            @Override
            public void update(int index, Task object, Date value) {
                if (value.getTime() > object.getFinish().getTime()) {
                    return;
                }
                object.setStart(value);
                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
                listDataProvider.refresh();
            }
        });

        Column<Task, Date> finishColumn = new Column<Task, Date>(
                //new DateCell(format)) {
                new CellDateImpl(dateTimeFormat)) { // Not sure why this one wasn't used.
            @Override
            public Date getValue(Task object) {
                return object.getFinish();
            }
        };
        finishColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
            @Override
            public void update(int index, Task object, Date value) {
                if (value.getTime() < object.getStart().getTime()) {
                    return;
                }
                object.setFinish(value);
                object.setDuration(DateUtil.differenceInDays(object.getFinish(), object.getStart()));
                listDataProvider.refresh();
            }
        });

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
                if (resourceList != null) {
                    return resourceList.get(object.getUID());
                } else {
                    return "";
                }
            }
        };

        colums.add(new TaskGridColumn<Task>(orderColumn, "", 30));
        colums.add(new TaskGridColumn<Task>(nameColumn, "Название задачи", 220));
        colums.add(new TaskGridColumn<Task>(durationColumn, "Длительность", 100));
        colums.add(new TaskGridColumn<Task>(startColumn, "Начало", 110));
        colums.add(new TaskGridColumn<Task>(finishColumn, "Окончание", 110));
        colums.add(new TaskGridColumn<Task>(predecessorColumn, "Предшественники", 100));
        colums.add(new TaskGridColumn<Task>(resourceColumn, "Названия ресурсов", 140));
        return colums;

    }
}
