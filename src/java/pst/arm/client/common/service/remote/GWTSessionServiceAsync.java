package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTSessionServiceAsync {

    public void addInSession(String key, String value, AsyncCallback<Boolean> callback);

    public void getFromSession(String key, AsyncCallback<String> callback);
}
