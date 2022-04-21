package pst.arm.server.modules.datagrid.dao.jdbc.expansion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.tree.RowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.common.service.UserService;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridExpansionDAO;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Repository
public class DDataGridExpansionDAOJdbcImpl implements DDataGridExpansionDAO {

    private static Logger log = Logger.getLogger("DDataGridExpansionDAOJdbcImpl");
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    @Transactional("txArm")
    public Boolean updatePlanNiokrApproved(Integer id, Boolean isApproved) {//, Boolean isApproved) {
        String query = "UPDATE PLAN_NIOKR SET "
                + "     APPROVED        = :isApproved,"
                + "     USER_ID         = :userId,  "
                + "     DATE_CORR       = :corrDate"
                + " WHERE "
                + "     PLAN_NIOKR_ID   = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        if (isApproved) {
            params.put("isApproved", 1);
        } else {
            params.put("isApproved", 0);
        }
        UserService userService = pst.arm.server.common.ConfigurationManager.getInstance().getUserService();
        User user = userService.getCurrentUser(true);
        params.put("userId", user.getId());
        params.put("corrDate", new Date());

        params.put("id", id);
        jdbcTemplate.update(query, params);
        return true;

    }

    @Override
    @Transactional("txArm")
    public Boolean insertOrDeleteUserDepart(Integer id, Boolean isAdd) {
        String query = " DELETE FROM USER_DEPART WHERE USER_ID = :id;";
        if (isAdd)
               query += " INSERT INTO USER_DEPART (USER_ID, DEPART_ID)"
                + " SELECT :id, DEPART_ID FROM DEPART";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        jdbcTemplate.update(query, params);
        return true;
    }
    
    @Override
    public Boolean checkAccessDepartExecutorFact(Integer csId, Integer userId) {
        String query =
                "       SELECT CASE WHEN "
                + ""
                + "     ((SELECT COUNT (*) FROM OCP WHERE USER_ID = " + userId + " AND  OCP_ID "
                + "     IN (SELECT SERV_CONTRACT_STAGE.OCP_ID FROM SERV_CONTRACT_STAGE WHERE SERV_CONTRACT_STAGE.CONTRACT_STAGE_ID = " + csId + ")) > 0) "
                + ""
                + "     OR "
                + ""
                + "     ((SELECT COUNT (*) FROM OCP_RIGHTS_FOR_REC WHERE USER_ID = " + userId + " AND OCP_ID "
                + "     IN (SELECT SERV_CONTRACT_STAGE.OCP_ID FROM SERV_CONTRACT_STAGE WHERE SERV_CONTRACT_STAGE.CONTRACT_STAGE_ID = " + csId + ")) > 0)"
                + ""
                + "     THEN 1 ELSE 0 END  ";

        return jdbcTemplate.queryForInt(query, new MapSqlParameterSource()) == 1;
    }
    
    @Override
    public String getUserDepartExecutorFact(Integer csId) {
        String query = 
                "SELECT NAME FROM USERS WHERE USER_ID IN (SELECT USER_ID FROM OCP WHERE OCP_ID "
                + ""
                + "IN (SELECT SERV_CONTRACT_STAGE.OCP_ID FROM SERV_CONTRACT_STAGE WHERE SERV_CONTRACT_STAGE.CONTRACT_STAGE_ID = " + csId + "))";
        return jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), String.class);
    }
    
    @Override
    public Boolean checkPanelOcpContractStageId (Integer csId) {
        String query = 
                "SELECT COUNT (*) FROM WORK_PLAN WHERE WORK_PLAN.CONTRACT_STAGE_ID = " + csId;
        return jdbcTemplate.queryForInt(query, new MapSqlParameterSource()) > 0;
    }
}
