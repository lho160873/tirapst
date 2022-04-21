package pst.arm.client.modules.aiscontracts.sync;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
class BaseLayoutContainerImpl extends LayoutContainer implements BaseContainer {

    public BaseLayoutContainerImpl() {
        initObjects();
        initControls();
        initEvents();
        initGWTEvents();

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        initUI();
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initControls() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initGWTEvents() {

    }

    @Override
    public void initUI() {

    }

}
