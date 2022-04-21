package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
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
public class MessagesOutPortlet extends Portlet{
    protected BaseConstants constants = GWT.create(BaseConstants.class);
    private MessagesOutPanel panel;
    public MessagesOutPortlet() {
        setHeading(constants.portletMessagesOutCaption());
        setCollapsible(true);
        setLayout(new FitLayout());
        setHeight(300);
        panel = new MessagesOutPanel();
        LayoutContainer main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);
        main.add(panel, new BorderLayoutData(Style.LayoutRegion.CENTER));
        add(main);

    }

}
