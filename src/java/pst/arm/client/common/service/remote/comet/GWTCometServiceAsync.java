package pst.arm.client.common.service.remote.comet;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author root
 */
public interface GWTCometServiceAsync {
    
    public void createSession(AsyncCallback<Boolean> callback);
    
    public void invalidateSession(AsyncCallback<Boolean> callback);
    
}
