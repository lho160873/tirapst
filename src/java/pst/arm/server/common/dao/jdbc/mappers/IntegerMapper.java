package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author root
 */
public class IntegerMapper implements ParameterizedRowMapper<Integer> {

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("ID");
        return id;
    }
    
}