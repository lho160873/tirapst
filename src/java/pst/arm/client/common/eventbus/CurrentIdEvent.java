package pst.arm.client.common.eventbus;


import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author LKHorosheva
 * @since  0.0.2
 */
public class CurrentIdEvent extends GwtEvent<CurrentIdHandler> {

    public static final GwtEvent.Type<CurrentIdHandler> TYPE = new GwtEvent.Type<CurrentIdHandler>();
    protected String id;

    public CurrentIdEvent(String id) {
        this.id = id;
    }



    @Override
    public Type<CurrentIdHandler> getAssociatedType() {
        return CurrentIdEvent.TYPE;
    }

    @Override
    protected void dispatch(CurrentIdHandler handler) {
        handler.handleCurrentIdEvent(this);
    }

    public String getId() {
        return this.id;
    }

}
