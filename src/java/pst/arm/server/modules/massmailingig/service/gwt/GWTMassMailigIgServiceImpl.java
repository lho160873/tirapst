/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.massmailingig.service.gwt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.massmailingig.MassMailingIgService;
/**
 *
 * @author Igor
 */
@Service("GWTMassMailingIgService")
public class GWTMassMailigIgServiceImpl extends GWTController implements pst.arm.client.modules.massmailingig.service.remote.GWTMassMailingIgService {

    private MassMailingIgService service;
    private static final Logger log = Logger.getLogger("GWTMassMailigIgServiceImpl");
    
    @Autowired
    public void setService(MassMailingIgService service) {
        this.service = service;
    }

    @Override
    public void addWithCopy(Integer userId, Integer massMailingId) {
        log.warn("GWTMassMailigIgServiceImpl::addWithCopy");
        service.addWithCopy(userId, massMailingId);
    }
}
