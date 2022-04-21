package pst.arm.client.modules.ganttchart.portable;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import java.util.List;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.ganttchart.domain.Job2;

public class GanttChartPortablePanel extends Window {
    
    public static final int WORK_PLAN = 0;
    public static final int WORK_PLAN_EXECUTOR = 1;
    public static final int WORK_PLAN_EXECUTOR_NEW = 2;
    
    

    public GanttChartPortablePanel(List<DDataGrid> data, int type) {
        setHeading("Диаграмма Ганта");
        setModal(true);
        setMaximizable(true);
        setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
        initComponents(data, type);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        GanttChartPortablePanel.this.setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
                    }
                });

            }
        });
    }

    public GanttChartPortablePanel(List<DDataGrid> data) {
        setHeading("Диаграмма Ганта");
        setModal(true);
        setMaximizable(true);
        setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
        initComponents(data);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        GanttChartPortablePanel.this.setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
                    }
                });

            }
        });
    }
    
    public GanttChartPortablePanel(List<Job2> data, String text) {
        setHeading("Диаграмма Ганта");
        setModal(true);
        setMaximizable(true);
        setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
        initComponents(data, text);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        GanttChartPortablePanel.this.setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
                    }
                });

            }
        });
    }
    
    private void initComponents(List<Job2> data, String text) {
        setLayout(new BorderLayout());
        add(new GanttChartTest(data, text), new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void initComponents(List<DDataGrid> data) {
        setLayout(new BorderLayout());
        add(new GanttChartTest(data), new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void initComponents(List<DDataGrid> data, int type) {
        setLayout(new BorderLayout());
        add(new GanttChartTest(data, type), new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
}