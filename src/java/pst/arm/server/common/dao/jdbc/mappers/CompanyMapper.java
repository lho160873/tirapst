package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.Company;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class CompanyMapper implements ParameterizedRowMapper<Company> {

    @Override
    public Company mapRow(ResultSet rs, int i) throws SQLException {
        Company c = new Company();
        c.setCompanyId(rs.getInt("COMPANY_ID"));
        c.setName(rs.getString("NAME"));
        c.setShortName(rs.getString("SHORT_NAME"));
        return c;
    }
}
