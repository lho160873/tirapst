package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author Alexandr Kozhin
 */
public class UserMapper implements ParameterizedRowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("USER_ID"));
        u.setUserLogin(rs.getString("USER_LOGIN"));
        u.setPassword(rs.getString("PASSWORD"));
        u.setUserName(rs.getString("NAME"));
        u.setDateCreated(rs.getTimestamp("DATE_CREATED"));
        u.setArchiveId(rs.getInt("ARCHIVE_ID"));
        u.setDescription(rs.getString("DESCRIPTION"));
        u.setEnabled(rs.getInt("ENABLED"));
        u.setDeleted(rs.getBoolean("DELETED"));
        u.setWorkerId(JdbcHelper.getInteger(rs.getObject("WORKER_ID")));
        u.setWorkerName(rs.getString("WORKER_NAME"));
        return u;
    }
    
}
