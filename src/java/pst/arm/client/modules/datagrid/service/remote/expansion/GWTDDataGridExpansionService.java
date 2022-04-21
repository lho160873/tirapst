/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTEditService;
import pst.arm.client.modules.datagrid.domain.DDataGrid;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/ddataGridExpansionService")
public interface GWTDDataGridExpansionService extends RemoteService {

      public Boolean updatePlanNiokrApproved(Integer id, Boolean isApproved) throws RpcServiceException; 
      
      public Boolean checkAccessDepartExecutorFact(Integer csId, Integer userId) throws RpcServiceException;
      
      public String getUserDepartExecutorFact(Integer csId) throws RpcServiceException;
      
      public Boolean checkPanelOcpContractStageId(Integer csId) throws RpcServiceException;
      
      public Boolean insertOrDeleteUserDepart(Integer id, Boolean isAdd) throws RpcServiceException; 
}
