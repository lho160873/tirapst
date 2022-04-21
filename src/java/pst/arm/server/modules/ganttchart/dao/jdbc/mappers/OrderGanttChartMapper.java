package pst.arm.server.modules.ganttchart.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.ganttchart.domain.Order;

public class OrderGanttChartMapper implements ParameterizedRowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

        Order o = new Order();
        o.setId(rs.getInt("id"));
//        o.setContract(rs.getString("contract"));
//        o.setNumDoc(rs.getString("numdoc"));
        o.setDateStart(rs.getDate("datestart"));
        o.setDateEnd(rs.getDate("dateend"));
//        o.setCompanyName(rs.getString("companyname"));
//        o.setSumDoc(rs.getDouble("sumdoc"));
        o.setOrderName(rs.getString("name"));
        o.setDepartName(rs.getString("id_depart"));
        o.setTypeExec(rs.getInt("type_exec"));
//        o.setInfo(rs.getString("info"));
//        o.setUrgency(rs.getInt("urgency"));
        o.setQuantityPlan(rs.getDouble("quantity_plan"));
        o.setCost(rs.getDouble("cost"));
        o.setIsInternal(rs.getInt("is_internal"));
        o.setIsComplete(rs.getInt("is_complete"));
        return o;
    }
}
