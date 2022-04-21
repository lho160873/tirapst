package pst.arm.server.modules.aiscontracts.dao.utils;

import pst.arm.client.modules.aiscontracts.domain.AisSyncObject;
import pst.arm.client.modules.aiscontracts.domain.Field;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import pst.arm.server.modules.aiscontracts.util.DataUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

public class JdbcUtil {
    private static Logger logger = Logger.getLogger("JdbcUtil");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy H:mm:ss");

    public static String generateInsertQueryAndFillParametersMap(AisSyncObject object, MapSqlParameterSource map) {
        String columns = "";
        String params = "";
        for (Object field : object.getFields().entrySet()) {
            Map.Entry<Field,String> entry = (Map.Entry<Field,String>)field;
            Field key = entry.getKey();
            String value = entry.getValue();
            if (!isBlank(key.getColumn()) && !isBlank(value)) {
                columns += "," + key.getColumn();
                params += ", :" + key.toString();
                Object castedValue = DataUtil.getCastedValue(key.getType(), object.getField(key));
                if (castedValue != null) {
                    map.addValue(key.toString(), castedValue);
                }
            }
        }
        columns = columns.replaceFirst(",", "");
        params = params.replaceFirst(",", "");
        String sql = " INSERT INTO " + object.getTableName() + "("
                + columns
                + " )VALUES("
                + params
                + ")";
        logger.debug("[SQL] " + sql);
        return sql;
    }

    public static String generateUpdateQueryAndFillParametersMap(AisSyncObject object, MapSqlParameterSource map) {
        String sqlColumns = "";
        for (Object field : object.getFields().entrySet()) {
            Map.Entry<Field,String> entry = (Map.Entry<Field,String>)field;
            Field key = entry.getKey();
            String value = entry.getValue();
            if (!isBlank(key.getColumn()) && !isBlank(value)) {
                Object castedValue = DataUtil.getCastedValue(key.getType(), object.getField(key));
                if (castedValue != null) {
                    map.addValue(key.toString(), castedValue);
                    sqlColumns += ", " + key.getColumn() + " = :" + key.toString() + " ";
                }
            }
        }
        map.addValue("id", object.getId());
        sqlColumns = sqlColumns.replaceFirst(",", "");
        String sql = "UPDATE " + object.getTableName()+ " SET " + sqlColumns + " WHERE " + object.getPK()+ " = :id";

        logger.debug("[SQL] " + sql);
        return sql;
    }
}
