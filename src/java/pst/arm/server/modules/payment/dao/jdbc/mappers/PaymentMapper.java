package pst.arm.server.modules.payment.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PaymentMapper implements ParameterizedRowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int i) throws SQLException {
        Payment d = new Payment();
        d.setSummFact(JdbcHelper.getDouble(rs.getObject("SUMM_FACT")));
        d.setMonth(JdbcHelper.getInteger(rs.getObject("M_MONTH")));
        d.setSummPlan(JdbcHelper.getDouble(rs.getObject("SUMM_PLAN")));
        return d;
    }
}
