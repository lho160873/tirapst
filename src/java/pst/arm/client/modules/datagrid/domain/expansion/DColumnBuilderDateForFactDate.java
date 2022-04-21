package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.DColumnBuilderDate;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderDateForFactDate extends DColumnBuilderDate{
    
    @Override
    public String getStirngForFiltr(SKeyForColumn key, String val) {
        String sqlForCol = "";
        String result = "";
        if (val != null) {
            /*result = "((SELECT     SUM(FACT_LABOUR) AS SUMM_FACT_LABOUR "
                    + " FROM          DEPART_EXECUTOR_FACT "
                    + " WHERE      (WORK_PLAN.WORK_PLAN_ID = DEPART_EXECUTOR_FACT.DEP_EX_PLAN_ID "
                    + " AND DEPART_EXECUTOR_FACT.END_DATE<" + "'" + val + "')"
                    + " )) < WORK_PLAN.PLAN_LABOUR ";*/
            result = " dbo.SUMM_FACT_LABOUR(MAIN.WORK_PLAN_ID," + "'" + val + "') <  MAIN.PLAN_LABOUR ";       
        }
        return result;

    }
}
