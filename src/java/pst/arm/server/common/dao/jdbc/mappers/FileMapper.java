package pst.arm.server.common.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;

/**
 * Class FileMapper maps different types of files
 * 
 * @author Alexandr Kozhin
 * @since 0.13.0
 * @see pst.arm.client.common.domain.generated.FileObjectDescriptor
 */
public class FileMapper implements ParameterizedRowMapper<FileObjectDescriptor> {

    @Override
    public FileObjectDescriptor mapRow(ResultSet rs, int rowNum) throws SQLException {
        FileObjectDescriptor file = new FileObjectDescriptor();
        file.setFileName(rs.getString("NAME"));
        file.setFileContentType(rs.getString("TYPE"));
        file.setFileContent(rs.getBytes("FILE_DATA"));
        return file;
    }
}