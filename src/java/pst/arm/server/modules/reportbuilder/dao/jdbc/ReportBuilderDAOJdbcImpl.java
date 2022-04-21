package pst.arm.server.modules.reportbuilder.dao.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pst.arm.server.common.service.StaticContextHolder;
import pst.arm.server.modules.reportbuilder.dao.ReportBuilderDAO;
import pst.arm.server.modules.reportbuilder.dao.jdbc.mapper.RowReportBuilderMapper;
import pst.arm.server.modules.reportbuilder.object.Row;
import pst.arm.server.modules.reportbuilder.object.search.ReportBuilderSearchCondition;

@Repository
public class ReportBuilderDAOJdbcImpl implements ReportBuilderDAO {

//    private NamedParameterJdbcTemplate jdbcTemplate;
//    @Autowired
//    @Qualifier("dsArm")
//    public void setDataSource(DataSource dataSource) {
//        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
//    }
    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean("dataSourceArm");
        return new NamedParameterJdbcTemplate(ds);
    }
    private static Logger log = Logger.getLogger("ReportBuilderDAOJdbcImpl");

    @Override
    public List<Row> getAllRows() {
        String sql = "SELECT [pm4].[dbo].[HEADER_SPEC].[HEADER_SPEC_ID]"
                + ",[pm4].[dbo].[HEADER_SPEC].[V_POS_ID]"
                + ",[pm4].[dbo].[HEADER_SPEC].[G_POS_ID]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[LINE_NO]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[CONT]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[FONT_SIZE]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[IS_FAT]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[ALIGNMENT]"
                + " FROM [pm4].[dbo].[HEADER_SPEC]"
                + " INNER JOIN [pm4].[dbo].[DT_HEADER_SPEC]"
                + " ON [pm4].[dbo].[DT_HEADER_SPEC].[HEADER_SPEC_ID] = [pm4].[dbo].[HEADER_SPEC].[HEADER_SPEC_ID]"
                + " WHERE [pm4].[dbo].[HEADER_SPEC].[COMPANY_ID] = 1"
                + " AND [pm4].[dbo].[HEADER_SPEC].[DOC_TYPE_ID] = 1"
                + " AND [pm4].[dbo].[HEADER_SPEC].[IS_HEADER] = 1"
                + " ORDER BY [pm4].[dbo].[HEADER_SPEC].[V_POS_ID],[pm4].[dbo].[HEADER_SPEC].[G_POS_ID], [pm4].[dbo].[DT_HEADER_SPEC].[LINE_NO]";
        HashMap<String, Object> params = new HashMap();
        return getJdbcTemplate().query(sql, params, new RowReportBuilderMapper());
    }

    @Override
    public List<Row> getRows(ReportBuilderSearchCondition condition) {
        String sql = "SELECT [pm4].[dbo].[HEADER_SPEC].[HEADER_SPEC_ID]"
                + ",[pm4].[dbo].[HEADER_SPEC].[V_POS_ID]"
                + ",[pm4].[dbo].[HEADER_SPEC].[G_POS_ID]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[LINE_NO]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[CONT]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[FONT_SIZE]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[IS_FAT]"
                + ",[pm4].[dbo].[DT_HEADER_SPEC].[ALIGNMENT]"
                + " FROM [pm4].[dbo].[HEADER_SPEC]"
                + " INNER JOIN [pm4].[dbo].[DT_HEADER_SPEC]"
                + " ON [pm4].[dbo].[DT_HEADER_SPEC].[HEADER_SPEC_ID] = [pm4].[dbo].[HEADER_SPEC].[HEADER_SPEC_ID]"
                + " WHERE [pm4].[dbo].[HEADER_SPEC].[COMPANY_ID] = " + (condition != null ? condition.getCompanyId() : -1)
                + " AND [pm4].[dbo].[HEADER_SPEC].[DOC_TYPE_ID] = 1"
                + " AND [pm4].[dbo].[HEADER_SPEC].[IS_HEADER] = 1"
                + " ORDER BY [pm4].[dbo].[HEADER_SPEC].[V_POS_ID],[pm4].[dbo].[HEADER_SPEC].[G_POS_ID], [pm4].[dbo].[DT_HEADER_SPEC].[LINE_NO]";
        HashMap<String, Object> params = new HashMap();
        return getJdbcTemplate().query(sql, params, new RowReportBuilderMapper());
    }
}
