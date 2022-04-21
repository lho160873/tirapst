package pst.arm.server.modules.controlproduction.capacity.dao.jdbc;

import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.controlproduction.capacity.dao.DepartCapacityDAO;

@Repository
public class DepartCapacityDAOJdbcImpl extends ArmNamedJdbcImpl implements DepartCapacityDAO {

    @Override
    public List<DepartCapacity> getDepartmentsCapacityList(Integer companyId) {
        String sql = ""
                + "SELECT "
                + "     DEPART_ID, CODE, "
                + "     dbo.DEP_SUM_PLAN_LAB( DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0) , DATEADD(d, -1, DATEADD(m, DATEDIFF(m, 0, GETDATE()) + 1, 0)), DEPART_ID) PLAN_LAB,"
                + "     dbo.DEP_SUM_PLAN_LAB_D1( DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0) , DEPART_ID) ADDITIONAL,"
                + "     dbo.DEP_RES_LAB(DEPART_ID) RESOURCE "
                + "FROM "
                + "     NTO "
                + "WHERE "
                + "     COMPANY_ID=:companyId AND ENABLED=1";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource("companyId", companyId), new DepartCapacityMapper());
    }

}
