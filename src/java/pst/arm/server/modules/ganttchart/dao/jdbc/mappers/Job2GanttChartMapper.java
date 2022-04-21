package pst.arm.server.modules.ganttchart.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.ganttchart.domain.Job2;

public class Job2GanttChartMapper implements ParameterizedRowMapper<Job2> {

    @Override
    public Job2 mapRow(ResultSet rs, int rowNum) throws SQLException {

        Job2 o = new Job2();
        o.setId(rs.getInt("id"));
        o.setNumDoc(rs.getString("numdoc"));
        o.setNumDoc2(rs.getString("numdoc2"));
//        o.setIdNomenclature(rs.getInt("idnomenclature"));
//        o.setIdDesignVersion(rs.getInt("iddesignversion"));
        o.setIdOrder(rs.getInt("id_order"));
        o.setIdDepart(rs.getInt("id_depart"));
        o.setIdParentJob(rs.getInt("id_parentjob"));
        o.setQuantityPlan(rs.getDouble("quantity_plan"));
        o.setDateStart(rs.getTimestamp("date_start"));
        o.setDateEnd(rs.getTimestamp("date_end"));
        o.setName(rs.getString("name"));
        o.setName2(rs.getString("name2"));
//        o.setInfo(rs.getString("info"));
        o.setSumDoc(rs.getDouble("sumdoc"));
//        o.setIdInternalJob(rs.getInt("idinternaljob"));
//        o.setUrgency(rs.getString("urgency"));
        o.setTypeExec(rs.getInt("type_exec"));
        return o;
    }
}
