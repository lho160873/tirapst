package pst.arm.client.modules.cabinet.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import pst.arm.client.common.domain.User;

/**
 *
 * @author Ratmir Slepenkov
 * @since 0.15.2
 */
public class UserEvent extends BaseEvent{
    User user;

    public UserEvent(EventType type) {
        super(type);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
