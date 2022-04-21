package pst.arm.client.modules.test.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTServiceSimpleAsync {

    public void myMethod(String s, AsyncCallback<String> callback);
    public void myMethod_1(String s, AsyncCallback<String> callback);
    public void myMethodGetAllUsers(AsyncCallback<List<String>> callback);
}
