package pst.arm.client.modules.leveltask.service.remote;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTLevelTaskServiceAsync extends GWTEditServiceAsync<LevelTask>{
    
    public void getLevelTask(LevelTaskSearchCondition condition, AsyncCallback<List<LevelTask>> callback);

    public void getAllLevelTask(AsyncCallback<List<LevelTask>> callback);

    public void getPage(LevelTaskSearchCondition condition, AsyncCallback<PagingLoadResult<BeanModel>> callback);

    public void isHasNewMsgIn(Integer currentUser, AsyncCallback<Boolean> callback);
  
    public void isHasNewMsgOut(Integer currentUser, AsyncCallback<Boolean> callback);

    public void markOfficeDocContr(List<Integer> ids, AsyncCallback<Boolean> callback);
  
    public void sendAnswer(List<Integer> ids, AsyncCallback<Boolean> callback);
    
    public void sendCancelClose(List<Integer> ida, List<Integer> idc, AsyncCallback<Boolean> callback);

    public void getDocFileId(Integer id, AsyncCallback<Integer> callback);

}
