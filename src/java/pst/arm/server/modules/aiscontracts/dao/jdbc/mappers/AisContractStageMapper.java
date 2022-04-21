package pst.arm.server.modules.aiscontracts.dao.jdbc.mappers;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.aiscontracts.domain.AisContractStage;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisContractStageMapper implements ParameterizedRowMapper<AisContractStage> {

    private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy H:mm:ss");

    @Override
    public AisContractStage mapRow(ResultSet rs, int i) throws SQLException {
        AisContractStage stage = new AisContractStage();
        stage.setStageId(JdbcHelper.getInteger(rs.getObject("CONTRACT_STAGE_ID")));
        for (AisContractStage.Field field : AisContractStage.Field.values()) {
            if (isNotBlank(field.getColumn())) {
                addField(stage, field, getValue(rs, field));
            }
        }
        return stage;
    }

    private void addField(AisContractStage stage, AisContractStage.Field field, String value) {
        if (StringUtils.isNotBlank(value)) {
            stage.addFieldValue(field, value);
        }
    }

    private String getValue(ResultSet rs, AisContractStage.Field field) throws SQLException {
        if (field.isDate()) {
            java.sql.Date date = rs.getDate(field.getColumn());
            if (date != null) {
                return df.format(date);
            } else {
                return "";
            }
        } else if (field.isDouble()) {
            return new BigDecimal(rs.getDouble(field.getColumn())).toPlainString();
        }
        return rs.getString(field.getColumn());
    }

}
