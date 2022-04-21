package pst.arm.server.modules.aiscontracts.service.gwt;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.ui.controls.imagenavigator.ImageConfig;
import pst.arm.client.common.ui.controls.imagenavigator.domain.ImageInfo;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;
import pst.arm.client.modules.aiscontracts.service.remote.GWTAisContractService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.aiscontracts.service.AisContractService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service("GWTAisContractService")
public class GWTAisContractServiceImpl extends GWTController implements GWTAisContractService{
    private AisContractService service;
    private static Logger log = Logger.getLogger("GWTAisContractServiceImpl");
  
     
    @Autowired
    public void setContractService(AisContractService service) {
        this.service = service;
    }


    @Override
    public List<AisContract> getContract(AisContractSearchCondition condition, Boolean isPaging) throws RpcServiceException{
        try{
        return service.getContract(condition,isPaging);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка масок договоров", ex.getMessage());
        }
    }
    
    @Override
    public List<AisContract> getAllContract()throws RpcServiceException{
        try{
        return service.getAllContract();
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка договоров", ex.getMessage());
        }
    }

    
    @Override
    public PagingLoadResult<BeanModel> getPage(AisContractSearchCondition condition) throws RpcServiceException {
        try {
            PagingLoadResult<BeanModel> result = null;
            List list = service.getContract(condition,Boolean.TRUE);
            result = new BasePagingLoadResult<BeanModel>(list);
            int total = service.getContractCount(condition);
            result.setOffset(condition.getOffset());
            result.setTotalLength(total);
            return result;
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка масок договоров", ex.getMessage());
        }
    }
    
    @Override
    public AisContract save(AisContract domain, Boolean isNew) throws RpcServiceException {
        try {
            AisContract c = service.saveContract(domain, isNew);
            c = service.getContract(c.getDomainId());
            return c;
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка " + ( isNew ? "добавления" : "обновления" ) + " договора", ex.getMessage());
        }
    }

    @Override
    public AisContract getDomainById(Long id) throws RpcServiceException{
        try{
        return service.getContract(id);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении договора", ex.getMessage());
        }
    }

    @Override
    public Boolean deleteDomainsByIds(List<Integer> ids) throws RpcServiceException {
        try{
        return service.deleteContract(ids);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка удаления договора", ex.getMessage());
        }    
    }
}


