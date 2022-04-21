package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/sessionService")
public interface GWTSessionService extends RemoteService {

    public Boolean addInSession(String key, String value);

    public String getFromSession(String key);
}
