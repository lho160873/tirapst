package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author root
 */
public class DateMapper implements ParameterizedRowMapper<Date> {

    @Override
    public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date date = rs.getDate("NOW");
        return date;
    }
    
}