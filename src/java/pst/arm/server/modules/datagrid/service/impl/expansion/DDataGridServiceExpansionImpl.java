/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.service.impl.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridExpansionDAO;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridExpansionService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class DDataGridServiceExpansionImpl implements DDataGridExpansionService {

    private DDataGridExpansionDAO dao;
    private static Logger log = Logger.getLogger("GWTDDataGridExpansionServiceImpl");

    @Autowired
    public void setDataGridDAO(DDataGridExpansionDAO dao) {
        this.dao = dao;
    }

    @Override
    public Boolean updatePlanNiokrApproved(Integer id, Boolean isApproved) {
        return dao.updatePlanNiokrApproved(id, isApproved);
    }
    
    @Override
    public Boolean insertOrDeleteUserDepart(Integer id, Boolean isAdd) {
        return dao.insertOrDeleteUserDepart( id,  isAdd);
    }

    @Override
    public Boolean checkAccessDepartExecutorFact(Integer csId, Integer userId) {
        return dao.checkAccessDepartExecutorFact(csId, userId);
    }

    @Override
    public String getUserDepartExecutorFact(Integer csId) {
        return dao.getUserDepartExecutorFact(csId);
    }

    @Override
    public Boolean checkPanelOcpContractStageId(Integer csId) {
        return dao.checkPanelOcpContractStageId(csId);
    }
}
