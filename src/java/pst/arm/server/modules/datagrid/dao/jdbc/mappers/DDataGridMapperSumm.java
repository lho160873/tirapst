package pst.arm.server.modules.datagrid.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.datagrid.domain.*;

/**
 *
 * @author Igor
 */
public class DDataGridMapperSumm implements ParameterizedRowMapper<DDataGrid> {

    private DTable table;

    public void setTable(DTable t) {
        this.table = t;
    }

    public DTable getTable() {
        return table;
    }
    private static Logger log = Logger.getLogger("DDataGridMapperSumm");

    @Override
    public DDataGrid mapRow(ResultSet rs, int i) throws SQLException {
        log.warn("DDataGridMapperSumm::mapRow BEGIN");
        DDataGrid domain = new DDataGrid();
        domain.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {

                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                DColumn col = (DColumn) colEntry.getValue();

//                Если для поля в DColumn указано свойство isSumm, то отправляем его
                if (col.getIsSumm()) {
                    IRowColumnVal val = builder.createRowColumnVal(key);
                    String s = rs.getString(key.getTableAlias() + "_" + key.getColumnName());

                    val.setValFromString(key, s, builder);
                    domain.getRows().put(key, val);
                }
            }
        }
        return domain;
    }
}