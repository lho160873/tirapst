package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pst.arm.client.common.exception.RpcServiceException;

/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/ddataGridServiceOcp")
public interface GWTDDataGridServiceOcp extends RemoteService {
    public Boolean copyOcp(Integer ocpId, Integer userId)throws RpcServiceException;
}
