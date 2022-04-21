package pst.arm.server.common.dao.jdbc;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Base jdbc impl class with NamedParameterJdbcTemplate
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
@Repository
public class ArmNamedJdbcImpl {
    
    private NamedParameterJdbcTemplate jdbcTemplate;
    protected static final String DATE_FORMAT = "DD.MM.YY";
    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected String addSqlPart(String query, String queryTail) {
        if ((queryTail == null) || (queryTail.isEmpty())) {
            if (queryTail != null) {
                return query;
            } else {
                return "";
            }
        } else {
            if ((query != null) && (query.isEmpty() == false)) {
                return query + " AND " + queryTail;
            } else {
                return queryTail;
            }
        }
    }
    
    protected String addPeriodPart(String where, String column, String from, String to) {
        if (from != null) {
            where += (!where.isEmpty()) ? " AND " : "";
            where += " to_date(" + column + ", '"+DATE_FORMAT+"') >= to_date('" + from + "', '"+DATE_FORMAT+"') ";
        }
        if (to != null) {
            where += (!where.isEmpty()) ? " AND " : "";
            where += " to_date(" + column + ", '"+DATE_FORMAT+"') <= to_date('" + to + "', '"+DATE_FORMAT+"') ";
        }
        return where;
    }
}
