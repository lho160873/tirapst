package pst.arm.server.modules.tablegrid.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.tablegrid.domain.TableGrid;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridMapper implements ParameterizedRowMapper<TableGrid>{

    //column names
    private static final String ID_COL = "USER_ID";
    private static final String NAME_COL = "LAST_NAME";


    @Override
    public TableGrid mapRow(ResultSet rs, int i) throws SQLException {
        
        TableGrid d = new TableGrid();
        d.setId(rs.getLong("USER_ID"));
        d.setName(rs.getString("LAST_NAME"));
        return d;        
    
    }
    
}