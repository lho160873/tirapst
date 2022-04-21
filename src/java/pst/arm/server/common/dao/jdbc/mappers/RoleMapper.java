package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.Role;

/**
 *
 * @author Artem Vorontsov
 */
public class RoleMapper implements ParameterizedRowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int i) throws SQLException {
        Role r = new Role();
        r.setRoleId(rs.getLong("ROLE_ID"));
        r.setRoleName(rs.getString("ROLE_NAME"));
        r.setDescription(rs.getString("DESCRIPTION"));
        return r;
    }
    
}
