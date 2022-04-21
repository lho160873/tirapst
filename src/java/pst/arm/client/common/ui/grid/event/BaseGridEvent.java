package pst.arm.client.common.ui.grid.event;

import com.extjs.gxt.ui.client.event.BaseEvent;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class BaseGridEvent<T>  extends BaseEvent{
    private T domain;
    
    public BaseGridEvent(Object source){
        super(source);
    }
    
    public BaseGridEvent(Object source, T domain){
        super(source);
        this.domain = domain;
    }

    public T getDomain() {
        return domain;
    }
}