/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.massmailingig.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.massmailingig.MassMailingIgService;

/**
 *
 * @author Igor
 */
@Service
public class MassMailingIgServiceImpl implements MassMailingIgService {
    
    private pst.arm.server.modules.massmailingig.dao.MassMailingIgDao dao;
    private static final Logger log = Logger.getLogger("MassMailingIgServiceImpl");
    
    @Autowired
    public void setMassMailingIgDAO(pst.arm.server.modules.massmailingig.dao.MassMailingIgDao dao) {
        this.dao = dao;
    }

    @Override
    public void addWithCopy(Integer userId, Integer massMailingId) {
        log.warn("MassMailingIgServiceImpl::addWithCopy");
        dao.addWithCopy(userId, massMailingId);
    }
    
}
