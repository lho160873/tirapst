/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.service.impl.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridDHDTDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridOcpDAO;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceDHDT;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceOcp;

/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
@Service
public class DDataGridServiceOcpImpl implements DDataGridServiceOcp {

    private DDataGridOcpDAO dao;
    private static Logger log = Logger.getLogger("DDataGridServiceOcpImpl");

    @Autowired
    public void setDataGridDAO(DDataGridOcpDAO dao) {
        this.dao = dao;
    }

    @Override
    public Boolean copyOcp(Integer ocpId, Integer userId) {
        log.warn("copyOcp " + ocpId.toString()+", "+userId.toString());
        return dao.copyOcp(ocpId, userId);
    }

   
   
}
