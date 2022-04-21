package pst.arm.client.modules.nullpage;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class NullPage extends BasePageNew {

    protected BaseConstants constants = (BaseConstants) GWT.create(BaseConstants.class);

    @Override
    protected LayoutContainer getContentPanel() {
        LayoutContainer lc = new LayoutContainer();
        lc.setLayout(new FitLayout());
        Text txt = new Text();
        txt.setText(constants.nullpage());
        lc.add(txt);
        return lc;
    }
}
