package pst.arm.server.modules.aiscontracts.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.server.common.dao.jdbc.JdbcHelper;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisOrganisationMapper implements ParameterizedRowMapper<AisOrganization> {

    @Override
    public AisOrganization mapRow(ResultSet rs, int i) throws SQLException {
        AisOrganization org = new AisOrganization();
        org.setId(JdbcHelper.getInteger(rs.getObject("ORGANIZATION_ID")));
        addFieldValue(org, AisOrganization.Field.id, rs.getString(AisOrganization.Field.id.getColumn()));
        addFieldValue(org, AisOrganization.Field.fullName, rs.getString(AisOrganization.Field.fullName.getColumn()));
        addFieldValue(org, AisOrganization.Field.shortName, rs.getString(AisOrganization.Field.shortName.getColumn()));
        addFieldValue(org, AisOrganization.Field.address, rs.getString(AisOrganization.Field.address.getColumn()));
        addFieldValue(org, AisOrganization.Field.inn, rs.getString(AisOrganization.Field.inn.getColumn()));
        addFieldValue(org, AisOrganization.Field.cpp, rs.getString(AisOrganization.Field.cpp.getColumn()));
        addFieldValue(org, AisOrganization.Field.site, rs.getString(AisOrganization.Field.site.getColumn()));
        return org;
    }

    private void addFieldValue(AisOrganization org, AisOrganization.Field field, String value) {
        if (StringUtils.isNotBlank(value)) {
            org.addFieldValue(field, value);
        }
    }

}
