package pst.arm.client.common.eventbus;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface CurrentIdHandler extends EventHandler{


    public void handleCurrentIdEvent(CurrentIdEvent event);

}
