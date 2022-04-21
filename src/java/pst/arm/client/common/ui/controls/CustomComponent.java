package pst.arm.client.common.ui.controls;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Created by wesStyle on 25/09/14.
 */
public class CustomComponent extends Component {
    @Override
    public void onAttach() {
        // added to a gwt panel, not rendered
        if (!rendered) {
            // render and swap the proxy element
            String widgetIndex = dummy.getPropertyString("__uiObjectID");
            Element parent = DOM.getParent(dummy);
            int index = DOM.getChildIndex(parent, dummy);
            parent.removeChild(dummy);
            render(parent, index);
            if (widgetIndex != null) {
                getElement().setPropertyInt("__uiObjectID", Integer.parseInt(widgetIndex));
            }
        }
        super.onAttach();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDetachHelper();
    }
}
