package pst.arm.client.common.eventbus;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class FilterVisibilityEvent extends GwtEvent<FilterEventHandler> {

    public static final GwtEvent.Type<FilterEventHandler> TYPE = new GwtEvent.Type<FilterEventHandler>();
    protected Boolean visible;

    public FilterVisibilityEvent(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public Type<FilterEventHandler> getAssociatedType() {
        return FilterVisibilityEvent.TYPE;
    }

    @Override
    protected void dispatch(FilterEventHandler handler) {
        handler.handleFilterVisibilityEvent(this);
    }

    public Boolean isVisible() {
        return this.visible;
    }
}
