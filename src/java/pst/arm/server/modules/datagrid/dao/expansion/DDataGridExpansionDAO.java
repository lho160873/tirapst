package pst.arm.server.modules.datagrid.dao.expansion;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DDataGridExpansionDAO {
     public Boolean updatePlanNiokrApproved(Integer id, Boolean isApproved);
     public Boolean checkAccessDepartExecutorFact(Integer csId, Integer userId);
     public String getUserDepartExecutorFact(Integer csId);
     public Boolean checkPanelOcpContractStageId(Integer csId);
     public Boolean insertOrDeleteUserDepart(Integer id, Boolean isAdd);
}
