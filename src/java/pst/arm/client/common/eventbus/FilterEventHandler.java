package pst.arm.client.common.eventbus;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface FilterEventHandler extends EventHandler{
    public void handleFilterVisibilityEvent(FilterVisibilityEvent event);
}
