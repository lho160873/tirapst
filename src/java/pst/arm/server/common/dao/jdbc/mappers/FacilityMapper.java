package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.Facility;

/**
 *
 * @author Artem Vorontsov
 */
public class FacilityMapper implements ParameterizedRowMapper<Facility> {

    @Override
    public Facility mapRow(ResultSet rs, int i) throws SQLException {
        Facility f = new Facility();
        f.setFacilityId(rs.getLong("FACILITY_ID"));
        f.setType(rs.getString("TYPE"));
        f.setModule(rs.getString("MODULE"));
        f.setName(rs.getString("NAME"));
        f.setDescription(rs.getString("DESCRIPTION"));
        return f;
    }
    
}
