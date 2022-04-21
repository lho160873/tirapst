package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.header.FloatNavigationPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MessagesOutWindow extends Window {

    protected BaseConstants constants = GWT.create(BaseConstants.class);
    private MessagesOutPanel panel;

    public MessagesOutPanel getPanel() {
        return panel;
    }

    public MessagesOutWindow() {

        panel = new MessagesOutPanel();
        initView();
    }

    public MessagesOutWindow(FloatNavigationPanel np, String currentId) {

        panel = new MessagesOutPanel(np,currentId);
        initView();
    }

    private void initView() {
        setHeading(constants.portletMessagesOutCaption());
        setLayout(new FitLayout());
        setSize(800, 400);
        setModal(true);
        LayoutContainer main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);
        main.add(panel, new BorderLayoutData(LayoutRegion.CENTER));
        add(main);
    }
}

