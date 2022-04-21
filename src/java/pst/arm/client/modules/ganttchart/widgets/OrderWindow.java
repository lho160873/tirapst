package pst.arm.client.modules.ganttchart.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;

public class OrderWindow extends Window {

    public OrderWindow(ToolBar toolBar, Grid<BeanModel> grid) {
        setHeading("Диаграмма Ганта");
        setModal(true);
        setMaximizable(true);
//        setSize(200, 200);
//        MessageBox.alert(com.google.gwt.user.client.Window.getClientWidth()+"", com.google.gwt.user.client.Window.getClientHeight()+"", null);
        setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
        initComponents(toolBar, grid);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        OrderWindow.this.setSize(com.google.gwt.user.client.Window.getClientWidth() - 14, com.google.gwt.user.client.Window.getClientHeight() - 14);
                    }
                });

            }
        });
    }

    private void initComponents(ToolBar toolBar, Grid<BeanModel> grid) {
        setLayout(new BorderLayout());
        this.add(toolBar, new BorderLayoutData(Style.LayoutRegion.NORTH));
        add(grid, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
}