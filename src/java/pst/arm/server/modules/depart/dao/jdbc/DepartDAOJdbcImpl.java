package pst.arm.server.modules.depart.dao.jdbc;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import pst.arm.client.modules.depart.domain.Depart;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.depart.dao.DepartDAO;
import pst.arm.server.modules.depart.dao.jdbc.mappers.DepartMapper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DepartDAOJdbcImpl extends ArmNamedJdbcImpl implements DepartDAO {

    private static Logger log = Logger.getLogger("DepartDAOJdbcImpl");

    @Override
    public List<Depart> select() {
        String sql = "SELECT * FROM DEPART";

        log.warn("DepartDAOJdbcImpl::select  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();

        return getJdbcTemplate().query(sql, params, new DepartMapper());
    }

    @Override
    public Depart insert(Depart domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Depart domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
