package pst.arm.server.modules.sync1c.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pst.arm.client.modules.sync1c.domain.Worker;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class WorkerRowMapper implements RowMapper<Worker> {

    @Override
    public Worker mapRow(ResultSet rs, int rowNum) throws SQLException {
        Worker worker = new Worker();
        worker.setPostWorkerId(rs.getInt("POST_WORKER_ID"));
        worker.setDepartmentId(rs.getInt("DEPART_ID"));
        worker.setPostId(rs.getInt("POST_ID"));
        return worker;
    }
}
