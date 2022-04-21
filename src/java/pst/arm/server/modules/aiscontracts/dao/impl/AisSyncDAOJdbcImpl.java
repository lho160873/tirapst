package pst.arm.server.modules.aiscontracts.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.aiscontracts.domain.AisSyncObject;
import pst.arm.client.modules.aiscontracts.domain.Field;
import pst.arm.client.modules.aiscontracts.domain.Instanciable;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.aiscontracts.dao.AisSyncDAO;
import pst.arm.server.modules.aiscontracts.dao.impl.mappers.AisSyncObjectRowMapper;
import pst.arm.server.modules.aiscontracts.dao.utils.JdbcUtil;
import pst.arm.server.modules.aiscontracts.util.DataUtil;

/**
 * Created by akozhin on 16.12.14.
 */
@Repository
public class AisSyncDAOJdbcImpl extends ArmNamedJdbcImpl implements AisSyncDAO {

    private static Logger log = Logger.getLogger("AisSyncDAOJdbcImpl");

    public <T extends AisSyncObject> T create(T object) {
        try {
            MapSqlParameterSource map = new MapSqlParameterSource();
            String sql = JdbcUtil.generateInsertQueryAndFillParametersMap(object, map);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            Integer rows = getJdbcTemplate().update(sql, map, keyHolder, new String[]{object.getPK()});
            if (!rows.equals(1)) {
                return null;
            }
            object.setId(keyHolder.getKey().intValue());
        } catch (Exception e) {
            log.error("Error creating object " + object.getClass().getName() + " '" + object.asString() + "' //" + e.getClass());
            return null;
        }
        return object;
    }

    public <T extends AisSyncObject> T update(T object) {
        try {
            MapSqlParameterSource map = new MapSqlParameterSource();
            String sql = JdbcUtil.generateUpdateQueryAndFillParametersMap(object, map);
            Integer rows = getJdbcTemplate().update(sql, map);
            if (!rows.equals(1)) {
                return null;
            }
        } catch (Exception e) {
            log.error("Error updating object " + object.asString() + "' //" + e.getClass());
            return null;
        }
        return object;
    }

    @Override
    public <T extends AisSyncObject & Instanciable> T get(T object, Field... fields) {
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            String sql = "SELECT * FROM " + object.getTableName() + " WHERE ";
            String columns = "";
            for (Field field : fields) {
                Object castedValue = DataUtil.getCastedValue(field.getType(), object.getField(field));
                if (castedValue != null) {
                    parameterSource.addValue(field.toString(), castedValue);
                    columns += " AND " + field.getColumn() + " = :" + field.toString() + " ";
                }

            }
            sql += columns.replaceFirst("AND", "");
            return getJdbcTemplate().queryForObject(sql, parameterSource, new AisSyncObjectRowMapper<T>(object));
        } catch (EmptyResultDataAccessException e) {
            log.error("Object not found");
            return null;
        } catch (Exception e) {
            log.error("Error get object by params", e);
            return null;
        }
    }
}
