package pst.arm.server.modules.leveltask.service.gwt;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.leveltask.service.LevelTaskService;
/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service("GWTLevelTaskService")
public class GWTLevelTaskServiceImpl extends GWTController implements GWTLevelTaskService {

    private LevelTaskService service;
    private static Logger log = Logger.getLogger("GWTLevelTaskServiceImpl");

    @Autowired
    public void setService(LevelTaskService service) {
        this.service = service;
    }
    
    @Override
    public List<LevelTask> getLevelTask(LevelTaskSearchCondition condition) throws RpcServiceException {

        try {
            return service.getLevelTask(condition);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка", ex.getMessage());
        }
    }

    @Override
    public List<LevelTask> getAllLevelTask() throws RpcServiceException {
        try {
            return service.getAllLevelTask();
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка", ex.getMessage());
        }
    }

    @Override
    public PagingLoadResult<BeanModel> getPage(LevelTaskSearchCondition condition) throws RpcServiceException {
        try {
            PagingLoadResult<BeanModel> result = null;
            List list = service.getLevelTask(condition);
            result = new BasePagingLoadResult<BeanModel>(list);
            int total = service.getLevelTaskCount(condition);
            result.setOffset(condition.getOffset());
            result.setTotalLength(total);
            return result;
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка", ex.getMessage());
        }
    }

    @Override
    public LevelTask save(LevelTask domain, Boolean isNew) throws RpcServiceException {
        try {
            log.warn("save");
            LevelTask c = service.saveLevelTask(domain, isNew);
            //c = service.getLevelTask(c.getDomainId());
            return c;
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка " + (isNew ? "добавления" : "обновления"), ex.getMessage());
        }
    }
    
    @Override
    public LevelTask getDomainById(Long id) throws RpcServiceException {
        try {
            return service.getLevelTask(id);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении", ex.getMessage());
        }
    }

    @Override
    public Boolean deleteDomainsByIds(List<Integer> ids) throws RpcServiceException {
        try {
            return service.deleteLevelTask(ids);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка удаления", ex.getMessage());
        }
    }

    @Override
    public Boolean isHasNewMsgIn(Integer currentUser) throws RpcServiceException {
        try {
            return service.isHasNewMsgIn(currentUser);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка получения информации о наличии новых сообщений", ex.getMessage());
        }
    }
    
     @Override
    public Boolean isHasNewMsgOut(Integer currentUser) throws RpcServiceException {
        try {
            return service.isHasNewMsgOut(currentUser);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка получения информации о наличии новых сообщений", ex.getMessage());
        }
    }
     
      @Override
    public Boolean markOfficeDocContr(List<Integer> ids) throws RpcServiceException {
       try{
        return service.markOfficeDocContr(ids);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при установке отметки о выполнении!", ex.getMessage());
        }
    }

    @Override
    public Boolean sendAnswer(List<Integer> ids) throws RpcServiceException {
        try{
        return service.sendAnswer(ids);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при отправке ответа!", ex.getMessage());
        }
    }
    
    @Override
    public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc) throws RpcServiceException {
        try{
        return service.sendCancelClose(ida,idc);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка!", ex.getMessage());
        }
    }
    

    @Override
    public Integer getDocFileId(Integer id) throws RpcServiceException {
        try {
            return service.getDocFileId(id);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при открытии файла!", ex.getMessage());
        }
    }
}
