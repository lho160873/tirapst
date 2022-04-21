package pst.arm.client.common.service.remote.trftempl;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author root
 */

@RemoteServiceRelativePath("service/trftemplService")

public interface GWTTrfTemplService extends RemoteService {
     
    public boolean Update1(String s);
    public boolean Update0(String s);
     
     
}
