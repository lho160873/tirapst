package pst.arm.client.common.service.remote.comet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author root
 */

@RemoteServiceRelativePath("cometService")

public interface GWTCometService extends RemoteService {
     
    public boolean createSession();
     
    public boolean invalidateSession();
     
}
