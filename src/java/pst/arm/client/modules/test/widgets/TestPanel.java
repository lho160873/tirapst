package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import pst.arm.client.modules.test.DashboardExample;

/**
 *
 * @author Администратор
 */

public class TestPanel extends ContentPanel {

    public TestPanel() {
        setScrollMode(Scroll.AUTOY);
        setLayout(new FitLayout());
        //setHeading("Договора");
        //setBorders(false);
        
        DashboardExample pnl = new DashboardExample();
        //BarExample pnl= new BarExample();
        //add(pnl.asWidget(), new BorderLayoutData(LayoutRegion.CENTER));;
        add(pnl.asWidget());

    }
}
