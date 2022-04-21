package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pst.arm.client.common.exception.RpcServiceException;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/ddataGridServiceDHDT")
public interface GWTDDataGridServiceDHDT extends RemoteService {
    public String updateJobs(Integer orderID);
    public Boolean calcCosts(Integer interactingSystId, Integer orderID)throws RpcServiceException;
}
