package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.store.StoreItem;

/**
 * Class FileMapper maps different types of files
 *
 * @author Alexandr Kozhin
 * @since 0.13.0
 * @see pst.arm.client.common.domain.generated.FileObjectDescriptor
 */
public class StoreItemMapper implements ParameterizedRowMapper<StoreItem> {

    @Override
    public StoreItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        StoreItem item = new StoreItem();
        item.setStoreId(rs.getInt("STORE_ITEM_ID"));
        item.setFormat(rs.getString("ITEM_TYPE"));
        item.setSourceName(rs.getString("ITEM_STATUS"));
        item.setPath(rs.getString("PATH"));

        return item;
    }
}