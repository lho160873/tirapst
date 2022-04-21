/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.dao.jdbc.expansion;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.server.common.service.StaticContextHolder;
import pst.arm.server.modules.datagrid.dao.expansion.DUpdatePlanningExpansionDAO;

/**
 *
 * @author Igor
 */
@Repository
public class DUpdatePlanningExpansionDAOJdbcImpl implements DUpdatePlanningExpansionDAO {
    
    private static Logger log = Logger.getLogger("DUpdatePlanningExpansionDAOJdbcImpl");
    private NamedParameterJdbcTemplate jdbcTemplate;
    private StaticContextHolder context;

    @Autowired
    public void setContext(StaticContextHolder context) {
        //  logger.warn("GWTServiceSimpleImpl::setTestService");
        this.context = context;
    }

    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void changeDataSource(DTable table) {
        //DTable table = DTableMapManager.getInstance().getTable(tableName);
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::changeDataSource  = " + table.getDataSourceName());
        ComboPooledDataSource ds = (ComboPooledDataSource) context.getBean(table.getDataSourceName());
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

     /**
     *
     * @author Igor Обновление данных о подразделениях и видах операций -
     * обновление SPR_DEPART 
     * Форматирование строки с помощью http://www.freeformatter.com/java-dotnet-escape.html
     */
    @Override
    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_SPR_DEPART] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::updatePlanningSprDepart(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }

    /**
     *
     * @author Igor Обновление данных о подразделениях и видах операций -
     * обновление SPR_OPERKIND 
     */
    @Override
    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_SPR_OPERKIND] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::updatePlanningSprOperkind(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }

    /**
     *
     * @author Igor 
     * Полностью обновить данные о чертежах 
     */
    @Override
    public void updateDesign(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_SPR_DESIGN] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::updateDesign(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }

    /**
     *
     * @author Igor Полностью обновить данные о версиях чертежей 
     */
    @Override
    public void updateDesignVer(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_SSP_DESIGNVERSION] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::updateDesignVer(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }

    /**
     *
     * @author Igor Добавить новые записи о чертежах 
     */
    @Override
    public void addDesign(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_ADD_SPR_DESIGN] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::addDesign(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }

    /**
     *
     * @author Igor Добавить новые записи о версиях чертежей 
     */
    @Override
    public void addDesignVer(Integer interactingSystId, String dbSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbSource);
        this.setDataSource(ds);

        String sql = "EXEC [dbo].[UPDATEPLANNING_ADD_SSP_DESIGNVERSION] " + interactingSystId;
        log.warn("DUpdatePlanningExpansionDAOJdbcImpl::addDesignVer(Integer interactingSystId, String dbSource)");
        jdbcTemplate.update(sql, new HashMap());
    }
    
}
