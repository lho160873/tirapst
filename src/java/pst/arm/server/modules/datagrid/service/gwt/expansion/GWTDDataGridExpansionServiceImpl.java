package pst.arm.server.modules.datagrid.service.gwt.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridExpansionService;

/**
 *
 * @author LKHorosheva
 * @version 0.1.0
 */
@Service("GWTDDataGridExpansionService")
public class GWTDDataGridExpansionServiceImpl extends GWTController implements GWTDDataGridExpansionService {

    private DDataGridExpansionService service;
    private static Logger log = Logger.getLogger("GWTDDataGridReportServiceImpl");

    @Autowired
    public void setDataGridService(DDataGridExpansionService service) {
        this.service = service;
    }

    @Override
    public Boolean updatePlanNiokrApproved(Integer id, Boolean isApproved) throws RpcServiceException {
        try {
            log.warn("updatePlanNiokrApproved");
            return service.updatePlanNiokrApproved(id, isApproved);
        } catch (Exception ex) {
            throw new RpcServiceException("Error updatePlanNiokrApproved", ex.getMessage());
        }
    }

    @Override
    public Boolean checkAccessDepartExecutorFact(Integer csId, Integer userId) throws RpcServiceException {
        try {
            log.warn("checkAccess");
            return service.checkAccessDepartExecutorFact(csId, userId);
        } catch (Exception ex) {
            throw new RpcServiceException("Error checkAccessDepartExecutorFact", ex.getMessage());
        }
    }

    @Override
    public String getUserDepartExecutorFact(Integer csId) throws RpcServiceException {
        try {
            log.warn("getUserDepartExecutorFact");
            return service.getUserDepartExecutorFact(csId);
        } catch (Exception ex) {
            throw new RpcServiceException("Error getUserDepartExecutorFact", ex.getMessage());
        }
    }
    
    @Override
    public Boolean checkPanelOcpContractStageId(Integer csId) throws RpcServiceException {
        try {
            log.warn("checkPanelOcpContractStageId");
            return service.checkPanelOcpContractStageId(csId);
        } catch (Exception ex) {
            throw new RpcServiceException("Error getUserDepartExecutorFact", ex.getMessage());
        }
    }
    
     @Override
    public Boolean insertOrDeleteUserDepart(Integer id, Boolean isAdd) throws RpcServiceException {
        try {
            log.warn("insertUserDepart");
            return service.insertOrDeleteUserDepart( id,  isAdd);
        } catch (Exception ex) {
            throw new RpcServiceException("Error insertUserDepart", ex.getMessage());
        }
    }
}
