package pst.arm.server.modules.aiscontracts.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class AisContractMapper implements ParameterizedRowMapper<AisContract> {

    @Override
    public AisContract mapRow(ResultSet rs, int i) throws SQLException {
        AisContract d = new AisContract();
        d.setContractId(JdbcHelper.getInteger(rs.getObject("M_CONTRACT_ID")));
        d.setOrgExecutorId(JdbcHelper.getInteger(rs.getObject("M_ORG_EXECUTOR_ID")));
        d.setCompanyId(JdbcHelper.getInteger(rs.getObject("O_COMPANY_ID")));
        d.setContractNumb(rs.getString("M_CONTRACT_NUMB"));
        d.setContractTypeId(JdbcHelper.getInteger(rs.getObject("M_CONTRACT_TYPE_ID")));
          d.setContractStatId(JdbcHelper.getInteger(rs.getObject("M_CONTRACT_STAT_ID")));
        d.setContractDate(rs.getTimestamp("M_CONTRACT_DATE"));
        d.setContractDateBegin(rs.getTimestamp("M_BEG_DATE"));
        d.setContractDateEnd(rs.getTimestamp("M_END_DATE"));         
        d.setCost(JdbcHelper.getDouble(rs.getObject("M_COST")));
        d.setCustNds(JdbcHelper.getDouble(rs.getObject("M_CUST_NDS")));
        return d;
    }
}
