package pst.arm.client.modules.ganttchart;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author nikita
 */
public class GanttChartPage extends BasePageNew {

    @Override
    protected LayoutContainer getContentPanel() {

        return new GanttChartPanel();
    }
}
