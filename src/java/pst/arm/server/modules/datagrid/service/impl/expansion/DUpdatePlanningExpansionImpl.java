/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.service.impl.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridExpansionDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DUpdatePlanningExpansionDAO;
import pst.arm.server.modules.datagrid.service.expansion.DUpdatePlanningExpansionService;

/**
 *
 * @author Igor
 */
@Service
public class DUpdatePlanningExpansionImpl implements DUpdatePlanningExpansionService {

    private DUpdatePlanningExpansionDAO dao;
    private static Logger log = Logger.getLogger("GWTDUpdatePlanningExpansionServiceImpl");
    
    @Autowired
    public void setUpdatePlanningDAO(DUpdatePlanningExpansionDAO dao) {
        this.dao = dao;
    }

    @Override
    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::updatePlanningSprDepart");
        dao.updatePlanningSprDepart(interactingSystId, dbSource);
    }

    @Override
    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::updatePlanningSprOperkind");
        dao.updatePlanningSprOperkind(interactingSystId, dbSource);
    }

    @Override
    public void updateDesign(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::updateDesign");
        dao.updateDesign(interactingSystId, dbSource);
    }

    @Override
    public void updateDesignVer(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::updateDesignVer");
        dao.updateDesignVer(interactingSystId, dbSource);
    }

    @Override
    public void addDesign(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::addDesign");
        dao.addDesign(interactingSystId, dbSource);
    }

    @Override
    public void addDesignVer(Integer interactingSystId, String dbSource) {
        log.warn("DUpdatePlanningExpansionImpl::addDesignVer");
        dao.addDesignVer(interactingSystId, dbSource);
    }
}
