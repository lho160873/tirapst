package pst.arm.server.modules.aiscontracts.dao.jdbc.mappers;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractField;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisContractSyncMapper implements ParameterizedRowMapper<AisContract> {

    private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy H:mm:ss");
    private boolean dopContract = false;

    @Override
    public AisContract mapRow(ResultSet rs, int i) throws SQLException {
        AisContract contract = new AisContract();
        contract.setContractId(JdbcHelper.getInteger(rs.getObject("CONTRACT_ID")));
        for (AisContractField field : AisContractField.values()) {
            if (isNotBlank(field.getColumn())) {
                addFieldValue(contract, field, getValue(rs, field));
            }
        }
        return contract;
    }

    private void addFieldValue(AisContract contract, AisContractField field, String value) {
        if (StringUtils.isNotBlank(value)) {
            contract.addFieldValue(field, value);
        }
    }

    private String getValue(ResultSet rs, AisContractField field) throws SQLException {
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
