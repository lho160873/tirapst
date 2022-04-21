package pst.arm.client.modules.admin.users;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.GWT;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.admin.lang.AdminConstants;

/**
 *
 * @author vorontsov
 */
public class UsersPage extends BasePageNew {

    private AdminConstants constants = GWT.create(AdminConstants.class);
    private UserConditionPanel conditionPanel;
    private UsersResultPanel resultPanel;

    @Override
    protected LayoutContainer getContentPanel() {
        return getPanel();
    }

    private LayoutContainer getPanel() {
        resultPanel = new UsersResultPanel();
        conditionPanel = new UserConditionPanel(resultPanel);

        LayoutContainer main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        BorderLayoutData requestPanelData = new BorderLayoutData(LayoutRegion.NORTH, 120);
        BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        requestPanelData.setCollapsible(true);

        main.add(conditionPanel, requestPanelData);
        main.add(resultPanel, resultPanelData);

        return main;
    }
}
