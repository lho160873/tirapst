package pst.arm.client.modules.mymodule;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.datagrid.widgets.DataBasePanel;
import pst.arm.client.modules.datagrid.widgets.DataTreePanel;

/**
 * Created by wesStyle on 01/04/14.
 */
public class MyModule extends BasePageNew {
    @Override
    protected LayoutContainer getContentPanel() {
        LayoutContainer lc = new LayoutContainer();
        lc.setLayout(new FitLayout());
        DataBasePanel panel = new DataTreePanel("DEPART_STRUCTURE");
        lc.add(panel);
        return lc;
    }
}
