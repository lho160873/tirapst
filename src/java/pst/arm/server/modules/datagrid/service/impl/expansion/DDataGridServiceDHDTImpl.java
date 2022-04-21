/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.service.impl.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridDHDTDAO;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceDHDT;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class DDataGridServiceDHDTImpl implements DDataGridServiceDHDT {

    private DDataGridDHDTDAO dao;
    private static Logger log = Logger.getLogger("DDataGridServiceDHDTImpl");

    @Autowired
    public void setDataGridDAO(DDataGridDHDTDAO dao) {
        this.dao = dao;
    }

    @Override
    public String updateJobs(Integer orderID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean calcCosts(Integer interactingSystId, Integer orderID) {
        log.warn("calcCosts " + interactingSystId.toString()+", "+orderID.toString());
        return dao.calcCosts(interactingSystId, orderID);
    }

   
   
}
