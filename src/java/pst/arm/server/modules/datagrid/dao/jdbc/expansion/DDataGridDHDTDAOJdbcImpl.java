package pst.arm.server.modules.datagrid.dao.jdbc.expansion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.dao.UserDAO;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.common.service.UserService;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridDHDTDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridExpansionDAO;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Repository
public class DDataGridDHDTDAOJdbcImpl implements DDataGridDHDTDAO {

    private static Logger log = Logger.getLogger("DDataGridDHDTDAOJdbcImpl");
   private JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
     
    @Override
    @Transactional("txArm")
    public Boolean calcCosts(Integer interactingSystId, Integer id) {
        log.warn("EXEC dbo.DH_JOB_CALC_COSTS " + interactingSystId.toString()+", "+id.toString());
        jdbcTemplate.execute("EXEC dbo.DH_JOB_CALC_COSTS " + interactingSystId.toString()+", "+id.toString());
        return true;

    }
}
