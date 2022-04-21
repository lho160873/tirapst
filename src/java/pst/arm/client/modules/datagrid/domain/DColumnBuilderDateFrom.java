package pst.arm.client.modules.datagrid.domain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderDateFrom extends DColumnBuilderDate{
         
    @Override
    public String getSqlWhereSearch(SKeyForColumn key,  String val) {
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return sqlForCol + ">='" + val+ "'";
        }
        //return key.getTableAlias() + "." + this.getColumn(key).getName() + ">='" + val+ "'";
        String colName = key.getTableAlias() + "." + this.getColumn(key).getName();        
        return "CONVERT(DATETIME,CONVERT(VARCHAR," + colName + ",104))" + " >= CONVERT(DATETIME,'" + val + "')";
    }
}
