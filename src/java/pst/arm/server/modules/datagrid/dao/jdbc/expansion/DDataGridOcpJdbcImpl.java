package pst.arm.server.modules.datagrid.dao.jdbc.expansion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridDHDTDAO;
import pst.arm.server.modules.datagrid.dao.expansion.DDataGridOcpDAO;

import javax.sql.DataSource;

/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
@Repository
public class DDataGridOcpJdbcImpl implements DDataGridOcpDAO {

    private static Logger log = Logger.getLogger("DDataGridOcpJdbcImpl");
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
     
    @Override
    @Transactional("txArm")
    public Boolean copyOcp(Integer ocpId, Integer userId) {
        log.warn("EXEC dbo.OCP_COPY ocpId = " + ocpId.toString()+", userId  = "+userId.toString());
        jdbcTemplate.execute("EXEC dbo.OCP_COPY " + ocpId.toString()+", "+userId.toString());
        return true;

    }
}
