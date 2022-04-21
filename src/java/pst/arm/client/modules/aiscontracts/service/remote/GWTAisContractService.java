package pst.arm.client.modules.aiscontracts.service.remote;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTEditService;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/aisContractService")
public interface GWTAisContractService extends RemoteService, GWTEditService<AisContract> {

    public List<AisContract> getContract(AisContractSearchCondition condition, Boolean isPaging) throws RpcServiceException;

    public List<AisContract> getAllContract() throws RpcServiceException;

    public PagingLoadResult<BeanModel> getPage(AisContractSearchCondition condition) throws RpcServiceException;
    
}