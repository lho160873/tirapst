package pst.arm.client.modules.aiscontracts.service.remote;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTAisContractServiceAsync extends GWTEditServiceAsync<AisContract> {

    public void getContract(AisContractSearchCondition condition, Boolean isPaging, AsyncCallback<List<AisContract>> callback);

    public void getAllContract(AsyncCallback<List<AisContract>> callback);

    public void getPage(AisContractSearchCondition condition, AsyncCallback<PagingLoadResult<BeanModel>> callback);

    //public void  saveWithDeleteContructNorm(AisContract domain, AsyncCallback<AisContract> callback);
    
    //public void  getListImages(Integer id, AsyncCallback<List<ImageInfo>> callback);
}
