package pst.arm.server.modules.payment.dao.jdbc;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.payment.dao.PaymentDAO;
import pst.arm.server.modules.payment.dao.jdbc.mappers.PaymentMapper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Repository
public class PaymentDAOImpl extends ArmNamedJdbcImpl implements PaymentDAO {

    private static Logger log = Logger.getLogger("PaymentDAOImpl");

    @Override
    public List<Payment> getPaymentSumm(PaymentSearchCondition condition) {    
        Date now= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(now);

        if (condition.getYear() != null && !condition.getYear().isEmpty()) {
            year = condition.getYear();
        }
                
        String sql = " SELECT  CASE WHEN YEAR_PLAN IS NOT NULL THEN YEAR_PLAN ELSE YEAR_FACT END  AS M_YEAR, "
                + " CASE WHEN MONTH_PLAN IS NOT NULL THEN MONTH_PLAN ELSE MONTH_FACT END  AS M_MONTH, "
                + " SUMM_FACT, "
                + " SUMM_PLAN  "
                + " FROM "
                + " (( SELECT year(PAYMENT_DATE) as YEAR_FACT"
                + "             , month(PAYMENT_DATE) as MONTH_FACT"
                + "             , SUM(SUMM_COST) as SUMM_FACT"
                + "     FROM PAYMENT_FACT  WHERE year(PAYMENT_DATE)="+year
                + "     GROUP BY year(PAYMENT_DATE),month(PAYMENT_DATE) )"
                + "          )A1 LEFT JOIN"
                + " ((   SELECT year(PAYMENT_DATE) as YEAR_PLAN"
                + "              , month(PAYMENT_DATE) as MONTH_PLAN"
                + "              , SUM(SUMM_COST) as SUMM_PLAN"
                + "       FROM  PAYMENT_PLAN WHERE year(PAYMENT_DATE)="+year
                + "       GROUP BY year(PAYMENT_DATE),month(PAYMENT_DATE)    "
                + " )            )A2 ON MONTH_FACT=MONTH_PLAN"
                + " UNION "
                + " SELECT  CASE WHEN YEAR_PLAN IS NOT NULL THEN YEAR_PLAN ELSE YEAR_FACT END  AS M_YEAR, "
                + " CASE WHEN MONTH_PLAN IS NOT NULL THEN MONTH_PLAN ELSE MONTH_FACT END  AS M_MONTH, "
                + " SUMM_FACT, "
                + " SUMM_PLAN  "
                + " FROM "
                + " (( SELECT year(PAYMENT_DATE) as YEAR_FACT"
                + "              , month(PAYMENT_DATE) as MONTH_FACT"
                + "              , SUM(SUMM_COST) as SUMM_FACT"
                + "     FROM PAYMENT_FACT  WHERE year(PAYMENT_DATE)="+year
                + "     GROUP BY year(PAYMENT_DATE),month(PAYMENT_DATE) )"
                + "           )A1 RIGHT JOIN"
                + " ((   SELECT year(PAYMENT_DATE) as YEAR_PLAN"
                + "              , month(PAYMENT_DATE) as MONTH_PLAN"
                + "              , SUM(SUMM_COST) as SUMM_PLAN"
                + "       FROM  PAYMENT_PLAN WHERE year(PAYMENT_DATE)="+year
                + "       GROUP BY year(PAYMENT_DATE),month(PAYMENT_DATE)    "
                + " )            )A2 ON MONTH_FACT=MONTH_PLAN ORDER BY M_MONTH;";

        log.warn("PaymentDAOImpl::getPaymentSumm  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();
        return getJdbcTemplate().query(sql, params, new PaymentMapper());
    }
    
}
