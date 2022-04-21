package pst.arm.server.modules.datagrid.dao.jdbc.mappers;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.datagrid.domain.*;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DDataGridMapper implements ParameterizedRowMapper<DDataGrid>{
 private DTable table;
 
 public void setTable(DTable t)
 {
     this.table = t;
 }
    public DTable getTable() {
        return table;
    }
    private static Logger log = Logger.getLogger("DDataGridMapper");

    @Override
    public DDataGrid mapRow(ResultSet rs, int i) throws SQLException {
        //log.warn("DDataGridMapper::mapRow BEGIN");
        DDataGrid d = new DDataGrid();
        d.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                if (!builder.getNotSearch()) {
                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                    IRowColumnVal val = builder.createRowColumnVal(key);
                    //log.warn("DDataGridMapper::mapRow BEFOR setValFromString colName = " + key.getColumnName() + " s = " + s);
                    //пока для данных типа Date особый разбор, так как не знаю что придумать для единообразия
                    if (val.getType(key, builder) != EColumnType.DATE) {
                        String s = rs.getString(key.getTableAlias() + "_" + key.getColumnName());
                        val.setValFromString(key, s, builder);
                    } else {
                        val.setVal(rs.getTimestamp(key.getTableAlias() + "_" + key.getColumnName()));
                    }
                    //log.warn("DDataGridMapper::mapRow val = " + String.valueOf(val.getVal()));
                    d.getRows().put(key, val);
                }
            }
        }
        //log.warn("DDataGridMapper::mapRow END");
        return d;
    }
}