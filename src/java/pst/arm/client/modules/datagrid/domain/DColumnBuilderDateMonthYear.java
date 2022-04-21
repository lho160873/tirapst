package pst.arm.client.modules.datagrid.domain;

/**
 *
 * @author LKHorosheva
 */

public class DColumnBuilderDateMonthYear extends DColumnBuilderDate {

    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return sqlForCol + "='" + val + "'";
        }
        return "CONVERT(VARCHAR," + key.getTableAlias() + "." + this.getColumn(key).getName()
                + ",104)" + "='" + "01." + val + "'";

    }
}
