package pst.arm.client.modules.depart.service.remote;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTEditService;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.depart.domain.Depart;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/departService")
public interface GWTDepartService extends RemoteService {

    public List<Depart> getDepart() throws RpcServiceException;

    
    
}

