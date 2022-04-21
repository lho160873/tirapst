package pst.arm.client.modules.sync1c;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.sync1c.widgets.Sync1CPanel;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class Sync1CPage extends BasePageNew {

    public Sync1CPage() {
    }

    @Override
    protected LayoutContainer getContentPanel() {
        return new Sync1CPanel();
    }

}
