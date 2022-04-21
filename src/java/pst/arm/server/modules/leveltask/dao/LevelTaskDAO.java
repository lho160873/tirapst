package pst.arm.server.modules.leveltask.dao;


import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;

import java.util.List;

//import pst.arm.client.modules.contracts.domain.search.ContractSearchCondition;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public interface LevelTaskDAO {
    public List<LevelTask> select(LevelTaskSearchCondition condition, Boolean isPaging);
    public List<LevelTask> selectAll();
    public List<LevelTask> selectAllUnsent();
    
    public LevelTask insert(LevelTask domain);
    public boolean update(LevelTask domain);
    public boolean delete(long id);
    
    public LevelTask selectRow(long id);

    public int count(LevelTaskSearchCondition condition);

    public Boolean isHasNewMsgIn(Integer currentUser);

    public Boolean isHasNewMsgOut(Integer currentUser);

    public Boolean markOfficeDocContr(List<Integer> ids);

    public Boolean sendAnswer(List<Integer> ids);
    
     public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc);
    
    public Integer getDocFileId(Integer id);
}