package pst.arm.client.common.ui.grid.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PanelChangeEvent extends BaseEvent{
    private LayoutContainer panel;
    
    public PanelChangeEvent(Object source){
        super(source);
    }
    
    public PanelChangeEvent(Object source, LayoutContainer panel){
        super(source);
        this.panel = panel;
    }

    public LayoutContainer getPanel() {
        return panel;
    }
}
