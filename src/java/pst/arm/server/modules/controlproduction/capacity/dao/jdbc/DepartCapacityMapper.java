package pst.arm.server.modules.controlproduction.capacity.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class DepartCapacityMapper implements ParameterizedRowMapper<DepartCapacity> {

    @Override
    public DepartCapacity mapRow(ResultSet rs, int i) throws SQLException {
        DepartCapacity dp = new DepartCapacity();
        dp.setDepartId(rs.getLong("DEPART_ID"));
        dp.setDepartCode(rs.getString("CODE"));
        dp.setMonthResource(rs.getDouble("RESOURCE"));
        dp.setPlan(rs.getDouble("PLAN_LAB"));
        dp.setAdditional(rs.getDouble("ADDITIONAL"));
        return dp;
    }
}
