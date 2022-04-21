package pst.arm.client.modules.admin.roles;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author Alexandr Kozhin
 */
public class AdminRolesPage extends BasePageNew {
 @Override
    protected LayoutContainer getContentPanel() {
        return new AdminRolesPanel();
    }
}