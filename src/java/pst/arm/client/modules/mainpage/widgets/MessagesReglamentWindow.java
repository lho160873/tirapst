package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import pst.arm.client.common.lang.BaseConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MessagesReglamentWindow extends Window{
    protected BaseConstants constants = GWT.create(BaseConstants.class);
    private MessagesReglamentPanel panel;
    public MessagesReglamentWindow() {
        setHeading(constants.portletMessagesReglamentCaption());
        setLayout(new FitLayout());
        setSize(600,250);
        setModal(true);
        panel = new MessagesReglamentPanel();


        LayoutContainer main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);
        main.add(panel, new BorderLayoutData(LayoutRegion.CENTER));
        add(main);
    }
}

