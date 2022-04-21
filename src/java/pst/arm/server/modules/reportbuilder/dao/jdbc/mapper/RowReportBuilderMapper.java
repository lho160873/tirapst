package pst.arm.server.modules.reportbuilder.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.server.modules.reportbuilder.object.Row;

public class RowReportBuilderMapper implements ParameterizedRowMapper<Row> {

    @Override
    public Row mapRow(ResultSet rs, int i) throws SQLException {
        Row row = new Row();

        row.setHeaderSpecId(rs.getInt("HEADER_SPEC_ID"));
        row.setX(rs.getInt("G_POS_ID"));
        row.setY(rs.getInt("V_POS_ID"));
        row.setLineNumber(rs.getInt("LINE_NO"));
        row.setContent(rs.getString("CONT"));
        row.setFontSize(rs.getInt("FONT_SIZE"));
        row.setFat(rs.getInt("IS_FAT"));
        row.setAlignment(rs.getInt("ALIGNMENT"));

        return row;
    }
}
