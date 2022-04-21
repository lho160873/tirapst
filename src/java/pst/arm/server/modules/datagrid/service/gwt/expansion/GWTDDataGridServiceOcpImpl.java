package pst.arm.server.modules.datagrid.service.gwt.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDT;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceOcp;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.DDataGridService;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceDHDT;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceOcp;

import java.util.List;

@Service("GWTDDataGridServiceOcp")
public class GWTDDataGridServiceOcpImpl extends GWTController implements GWTDDataGridServiceOcp {

    @Autowired
    DDataGridService gridService;
    
    @Autowired
    DDataGridServiceOcp gridServiceDHDT;
    
    private static Logger log = Logger.getLogger("GWTDDataGridServiceOcpImpl");

    @Override
    public Boolean copyOcp(Integer ocpId, Integer userId) throws RpcServiceException{
        try {
            log.warn("GWTDDataGridServiceOcpImpl copyOcp " + ocpId.toString()+", "+userId.toString());
            gridServiceDHDT.copyOcp(ocpId, userId);
        } catch (Exception e) {
             throw new RpcServiceException("Error copyOcp", e.getMessage());
        }
        return true;
    }
}
