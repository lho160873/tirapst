package pst.arm.server.modules.ganttchart.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.ganttchart.domain.JobPlan;

public class JobPlanGanttChartMapper implements ParameterizedRowMapper<JobPlan> {

    @Override
    public JobPlan mapRow(ResultSet rs, int rowNum) throws SQLException {

        JobPlan o = new JobPlan();
        o.setId(rs.getInt("id"));
        o.setLineno(rs.getInt("lineno"));
        o.setPrice(rs.getDouble("price"));
        o.setDateStart(rs.getDate("date_start"));
        o.setDateEnd(rs.getDate("date_end"));
        o.setnTime(rs.getDouble("ntime"));
        o.setName(rs.getString("name"));
        o.setPrepareTime(rs.getDouble("preparetime"));
//        o.setIdDepart(rs.getInt("iddepart"));
//        o.setQuantityPlan(rs.getDouble("quantityplan"));
//        o.setIdOperKind(rs.getInt("idoperkind"));
//        o.setParentId(rs.getInt("parentid"));
        o.setUnitTime(rs.getDouble("unittime"));
        o.setIdWorker(rs.getInt("id_worker"));
        return o;
    }
}
