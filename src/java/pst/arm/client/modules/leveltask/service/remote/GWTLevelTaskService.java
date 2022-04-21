package pst.arm.client.modules.leveltask.service.remote;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTEditService;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/leveltaskService")
public interface GWTLevelTaskService extends RemoteService, GWTEditService<LevelTask> {

    public List<LevelTask> getLevelTask(LevelTaskSearchCondition condition) throws RpcServiceException;

    public List<LevelTask> getAllLevelTask() throws RpcServiceException;

    public PagingLoadResult<BeanModel> getPage(LevelTaskSearchCondition condition) throws RpcServiceException;
    
    public Boolean isHasNewMsgIn(Integer currentUser)throws RpcServiceException;
    
    public Boolean isHasNewMsgOut(Integer currentUser)throws RpcServiceException;
    
    public Boolean markOfficeDocContr(List<Integer> ids) throws RpcServiceException;
    
    public Boolean sendAnswer(List<Integer> ids) throws RpcServiceException;
    
    public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc) throws RpcServiceException;
    
    public Integer getDocFileId(Integer id)throws RpcServiceException;

}