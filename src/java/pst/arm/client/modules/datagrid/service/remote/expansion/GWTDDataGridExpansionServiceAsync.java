
package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.exception.RpcServiceException;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTDDataGridExpansionServiceAsync {
   
 public void updatePlanNiokrApproved(Integer id, Boolean isApproved, AsyncCallback<Boolean> callback); 
 
 public void checkAccessDepartExecutorFact(Integer csId, Integer userId, AsyncCallback<Boolean> callback);
 
 public void getUserDepartExecutorFact(Integer csId, AsyncCallback<String> callback);
 
 public void checkPanelOcpContractStageId(Integer csId, AsyncCallback<Boolean> callback);
 
 public void insertOrDeleteUserDepart(Integer id, Boolean isAdd, AsyncCallback<Boolean> callback); 
    
}
