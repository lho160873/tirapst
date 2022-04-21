package pst.arm.client.modules.ganttchart.portable;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import pst.gwt.gwtgantt.model.Task;

public class DetailsGanttChartWindow extends Window {

    public DetailsGanttChartWindow(Task task) {
        setHeading("Диаграмма Ганта");
        setModal(true);
        setMaximizable(true);
        setSize(com.google.gwt.user.client.Window.getClientWidth() - com.google.gwt.user.client.Window.getClientWidth() * 2 / 3, com.google.gwt.user.client.Window.getClientHeight() - com.google.gwt.user.client.Window.getClientHeight() * 2 / 3);
        initComponents(task);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        DetailsGanttChartWindow.this.setSize(com.google.gwt.user.client.Window.getClientWidth() - com.google.gwt.user.client.Window.getClientWidth() * 2 / 3, com.google.gwt.user.client.Window.getClientHeight() - com.google.gwt.user.client.Window.getClientHeight() * 2 / 3);
                    }
                });

            }
        });
    }

    private void initComponents(Task store) {
        setLayout(new BorderLayout());
        add(new DetailsTest(store), new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
}