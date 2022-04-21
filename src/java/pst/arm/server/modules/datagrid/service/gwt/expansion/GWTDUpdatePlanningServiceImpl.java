/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.service.gwt.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDUpdatePlanningService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridExpansionService;
import pst.arm.server.modules.datagrid.service.expansion.DUpdatePlanningExpansionService;

/**
 *
 * @author Igor
 */
@Service("GWTDUpdatePlanningService")
public class GWTDUpdatePlanningServiceImpl extends GWTController implements GWTDUpdatePlanningService {
    
    private DUpdatePlanningExpansionService service;
    private static Logger log = Logger.getLogger("GWTDUpdatePlanningServiceImpl");
    
    @Autowired
    public void setDataGridService(DUpdatePlanningExpansionService service) {
        this.service = service;
    }

    @Override
    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::updatePlanningSprDepart");
        service.updatePlanningSprDepart(interactingSystId, dbSource);
    }

    @Override
    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::updatePlanningSprOperkind");
        service.updatePlanningSprOperkind(interactingSystId, dbSource);
    }

    @Override
    public void updateDesign(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::updateDesign");
        service.updateDesign(interactingSystId, dbSource);
    }

    @Override
    public void updateDesignVer(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::updateDesignVer");
        service.updateDesignVer(interactingSystId, dbSource);
    }

    @Override
    public void addDesign(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::addDesign");
        service.addDesign(interactingSystId, dbSource);
    }

    @Override
    public void addDesignVer(Integer interactingSystId, String dbSource) {
        log.warn("GWTDUpdatePlanningServiceImpl::addDesignVer");
        service.addDesignVer(interactingSystId, dbSource);
    }
    
}
