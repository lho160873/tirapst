package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderText extends DColumnBuilderString implements Serializable
{       
    @Override
     public String getSqlWhereSearch(SKeyForColumn key, String val) {
        String upper = "%" + val + "%";
        String sqlForCol = this.getColumn(key).getSqlForColumn();

        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return   sqlForCol + " LIKE " + "'" + upper + "'";
        }
        return  key.getTableAlias() + "." + this.getColumn(key).getName() + " LIKE '" + upper + "'";
    }
}


