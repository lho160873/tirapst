package pst.arm.server.modules.leveltask.service;

import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;

import java.util.List;


/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public interface LevelTaskService {

    public List<LevelTask> getLevelTask(LevelTaskSearchCondition condition);

    public List<LevelTask> getAllLevelTask();

    public List<LevelTask> getUnsentLevelTasks();

    public int getLevelTaskCount(LevelTaskSearchCondition condition);

    public LevelTask saveLevelTask(LevelTask domain, Boolean isNew);

    public LevelTask getLevelTask(Long id);

    public Boolean deleteLevelTask(List<Integer> ids);

    public Boolean isHasNewMsgIn(Integer currentUser);
    
     public Boolean isHasNewMsgOut(Integer currentUser);
     
     public Boolean markOfficeDocContr(List<Integer> ids);
     
     public Boolean sendAnswer(List<Integer> ids);
     
     public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc);
     
     public Integer getDocFileId(Integer id);
}
