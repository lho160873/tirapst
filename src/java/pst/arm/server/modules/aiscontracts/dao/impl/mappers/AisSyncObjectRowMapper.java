package pst.arm.server.modules.aiscontracts.dao.impl.mappers;

import pst.arm.server.modules.aiscontracts.dao.utils.JdbcUtil;
import pst.arm.client.modules.aiscontracts.domain.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class AisSyncObjectRowMapper<T extends AisSyncObject & Instanciable> implements RowMapper<T> {

    T factory;

    public AisSyncObjectRowMapper(T factory) {
        this.factory = factory;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T object = (T) factory.newInstance();
        object.setId( rs.getInt(object.getPK()));
        for (Field field : factory.getAvailableFields()) {
            if (isNotBlank(field.getColumn())) {
                addFieldValue(object, field, getValue(rs, field));
            }
        }
        return object;
    }

    private void addFieldValue(T object, Field field, String value) {
        if (StringUtils.isNotBlank(value)) {
            object.addFieldValue(field, value);
        }
    }

    private String getValue(ResultSet rs, Field field) throws SQLException {
        if (field.getType().equals(FieldType.Date)) {
            java.sql.Date date = rs.getDate(field.getColumn());
            if (date != null) {
                return JdbcUtil.DATE_FORMAT.format(date);
            } else {
                return "";
            }
        } else if (field.getType().equals(FieldType.Double)) {
            BigDecimal bigDecimal = rs.getBigDecimal(field.getColumn());
            if( bigDecimal != null){
                return bigDecimal.toPlainString();
            }
            return null;
        }
        return rs.getString(field.getColumn());
    }
}