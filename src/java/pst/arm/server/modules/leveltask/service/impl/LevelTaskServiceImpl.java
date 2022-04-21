package pst.arm.server.modules.leveltask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
import pst.arm.server.modules.leveltask.dao.LevelTaskDAO;
import pst.arm.server.modules.leveltask.service.LevelTaskService;

import java.util.List;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
@Service
public class LevelTaskServiceImpl implements LevelTaskService{
    private LevelTaskDAO dao;
    
    @Autowired
    public void setDao(LevelTaskDAO dao) {
        this.dao = dao;
    }
    
    @Override
    public List<LevelTask> getLevelTask(LevelTaskSearchCondition condition) {
         List<LevelTask> list = dao.select(condition,Boolean.TRUE);
                
        return list;
    }

    @Override
    public List<LevelTask> getAllLevelTask() {
         List<LevelTask> list = dao.selectAll();                
        return list;
    }

    @Override
    public List<LevelTask> getUnsentLevelTasks() {
        List<LevelTask> list = dao.selectAllUnsent();
        return list;
    }

    @Override
    public int getLevelTaskCount(LevelTaskSearchCondition condition) {
      return dao.count(condition);
    }

    @Override
    public LevelTask saveLevelTask(LevelTask domain, Boolean isNew) {
          if ( isNew )
        {
            return dao.insert(domain);
        }
        else
        {
            dao.update(domain);
            return domain;
        }
    }

    @Override
    public LevelTask getLevelTask(Long id) {
          return dao.selectRow(id);
    }

    @Override
    public Boolean deleteLevelTask(List<Integer> ids) {
         for (Integer id : ids) {
            dao.delete(id);
        }
        return true;
    }
    
    @Override
    public Boolean isHasNewMsgIn(Integer currentUser) {
        return dao.isHasNewMsgIn(currentUser);
    }

    @Override
    public Boolean isHasNewMsgOut(Integer currentUser) {
        return dao.isHasNewMsgOut(currentUser);
    }

    @Override
    public Boolean markOfficeDocContr(List<Integer> ids) {
        return dao.markOfficeDocContr(ids);
    }

    @Override
    public Boolean sendAnswer(List<Integer> ids) {
        return dao.sendAnswer(ids);
    }
    
     @Override
    public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc) {
        return dao.sendCancelClose(ida,idc);
    }
     
    @Override
    public Integer getDocFileId(Integer id) {
        return dao.getDocFileId(id);
    }
    
}
