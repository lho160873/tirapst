package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author Alexandr Kozhin
 */
public class AdminStatPage extends BasePageNew {
 @Override
    protected LayoutContainer getContentPanel() {
        return new AdminStatContainer();
    }
}