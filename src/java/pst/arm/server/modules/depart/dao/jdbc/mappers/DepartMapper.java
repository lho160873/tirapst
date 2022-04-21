package pst.arm.server.modules.depart.dao.jdbc.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.depart.domain.Depart;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DepartMapper implements ParameterizedRowMapper<Depart> {

    @Override
    public Depart mapRow(ResultSet rs, int i) throws SQLException {
        Depart d = new Depart();
        d.setDepartId(JdbcHelper.getInteger(rs.getObject("DEPART_ID")));
        d.setParentDepartId(JdbcHelper.getInteger(rs.getObject("PARENT_DEPART_ID")));
        d.setName(rs.getString("NAME"));
        d.setCode(rs.getString("CODE"));

        return d;
    }
}
