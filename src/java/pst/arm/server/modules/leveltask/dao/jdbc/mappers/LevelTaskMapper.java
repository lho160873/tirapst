package pst.arm.server.modules.leveltask.dao.jdbc.mappers;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class LevelTaskMapper implements ParameterizedRowMapper<LevelTask>{

    @Override
    public LevelTask mapRow(ResultSet rs, int i) throws SQLException {
        LevelTask d = new LevelTask();
        d.setId(JdbcHelper.getInteger(rs.getObject("M_LEVEL_TASK_ID")));
        d.setCreateDate(rs.getTimestamp("M_CREATE_DATE"));
        d.setTaskId(JdbcHelper.getInteger(rs.getObject("M_TASK_TYPE_ID")));
        d.setTaskName(rs.getString("T_NAME"));
        d.setTaskState(JdbcHelper.getInteger(rs.getObject("M_TASK_STATE")));
        d.setStateName(rs.getString("S_NAME"));
        d.setUserIdFrom(JdbcHelper.getInteger(rs.getObject("M_USER_ID_FROM")));
        d.setUserIdTo(JdbcHelper.getInteger(rs.getObject("M_USER_ID_TO")));
        d.setUserNameTo(rs.getString("VV_WPINDEX"));
        d.setUserNameFrom(rs.getString("V_WPINDEX"));
        d.setDescription(rs.getString("M_DESCRIPTION"));
        d.setDescriptionShort(rs.getString("M_DESCRIPTION_SHORT"));
        d.setPriority(JdbcHelper.getInteger(rs.getObject("M_PRIORITY")));
        d.setDd(rs.getString("M_DD"));
        d.setLl(rs.getString("M_LL"));
        d.setReplyDate(rs.getTimestamp("M_REPLY_DATE"));
        d.setReply(rs.getString("M_REPLY"));
        d.setReplyNumber(JdbcHelper.getInteger(rs.getObject("M_REPLY_NUMBER")));
        d.setEventTime(rs.getTimestamp("M_EVENT_TIME"));
        d.setModule(rs.getString("M_MODULE"));
        d.setCurrentId(JdbcHelper.getInteger(rs.getObject("M_ID")));
        d.setSendSign(JdbcHelper.getInteger(rs.getObject("M_SEND_SIGN")));
        return d;
    }
    
}
