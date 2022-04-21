package pst.arm.server.modules.leveltask.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.server.common.dao.jdbc.JdbcHelper;
/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class LevelTaskIdMapper implements ParameterizedRowMapper<LevelTask>{

    @Override
    public LevelTask mapRow(ResultSet rs, int i) throws SQLException {
        LevelTask d = new LevelTask();
        d.setId(JdbcHelper.getInteger(rs.getObject("M_LEVEL_TASK_ID")));

        return d;
    }
    
}

