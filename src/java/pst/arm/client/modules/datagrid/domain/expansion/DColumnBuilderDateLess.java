package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.DColumnBuilderDate;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderDateLess extends DColumnBuilderDate{
    
    @Override
    public String getStirngForFiltr(SKeyForColumn key, String val) {
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol==null)
            sqlForCol = key.getTableAlias() + "." + this.getColumn(key).getName();
        String result = " ";
        if (val != null) {
            result += "( " + sqlForCol + "<'" + val + "' OR " + sqlForCol + " IS NULL )";
        } else {
            result += sqlForCol + " IS NULL ";

        }
        return result;
    }
    
}
