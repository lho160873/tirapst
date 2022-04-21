package pst.arm.client.modules.ganttchart.portable;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.modules.ganttchart.images.GanttChartImages;
import pst.arm.client.modules.ganttchart.lang.GanttChartConstants;
import pst.gwt.gwtgantt.DateUtil;
import pst.gwt.gwtgantt.TaskUtil;
import pst.gwt.gwtgantt.gantt.GanttChart;
import pst.gwt.gwtgantt.gantt.ProvidesTask;
import pst.gwt.gwtgantt.model.Task;
import pst.gwt.gwtgantt.table.GridView;

/**
 * @version 1.0
 * @author nikita
 */
public class DetailsTest extends ContentPanel {

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
    // Хранилища для получения данных с БД
    private ListStore<BeanModel> storeTest;

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
        Button buildButton = new Button(constants.btnBuild(), AbstractImagePrototype.create(images.bAddResource()), new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                List<BeanModel> jobsTest = storeTest.getModels();
//                MessageBox.alert("", ""+storeTest.getCount(), null);
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
//        toolBarForButtons.add(buildButton);

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

    public DetailsTest(Task task) {
        setHeading(constants.heading());
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        setBorders(false);


        mainPanel = new DockLayoutPanel(Unit.PX);
//        mainPanel.addNorth(getButtonsToolBar(), 28);
        com.extjs.gxt.ui.client.widget.Text text1 = new com.extjs.gxt.ui.client.widget.Text();
        com.extjs.gxt.ui.client.widget.Text text2 = new com.extjs.gxt.ui.client.widget.Text();
        com.extjs.gxt.ui.client.widget.Text text3 = new com.extjs.gxt.ui.client.widget.Text();
        com.extjs.gxt.ui.client.widget.Text text4 = new com.extjs.gxt.ui.client.widget.Text();
        String[] data = task.getNotes().split("\\|");
        text1.setText(data[0].substring(data[0].indexOf(" "), data[0].length() - 1));
        text2.setText(data[1]);
        text3.setText(data[2]);
        text4.setText(data[3]);
        FlowPanel fp = new FlowPanel();
        fp.add(text1);
        fp.add(text2);
        fp.add(text3);
        fp.add(text4);
        mainPanel.add(fp);
        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER));

    }
}
