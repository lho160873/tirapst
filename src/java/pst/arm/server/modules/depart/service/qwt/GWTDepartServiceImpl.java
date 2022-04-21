package pst.arm.server.modules.depart.service.qwt;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.depart.domain.Depart;
import pst.arm.client.modules.depart.service.remote.GWTDepartService;
import pst.arm.server.common.web.GWTControllerSimple;


@Service("GWTDepartService")
public class GWTDepartServiceImpl extends GWTControllerSimple implements GWTDepartService{
    private static Logger logger=Logger.getLogger("GWTDepartServiceImpl");

    @Override
    public List<Depart> getDepart() throws RpcServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
