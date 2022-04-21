package pst.arm.server.modules.sync1c.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pst.arm.client.modules.sync1c.domain.Worker1C;

/**
 * Created by akozhin on 07.05.15.
 */
public class Worker1CRowMapper implements RowMapper<Worker1C> {

    @Override
    public Worker1C mapRow(ResultSet rs, int rowNum) throws SQLException {
        Worker1C worker = new Worker1C();
        worker.setWorkerId(rs.getInt("WORKER_1C_ID"));
        worker.setName(rs.getString("NAME"));
        worker.setPersonalNumber(rs.getString("PERS_NUMBER"));
        worker.setCompanyId(rs.getInt("COMPANY_ID"));
        worker.setDateIn(rs.getTimestamp("DATE_IN"));
        worker.setDateOut(rs.getTimestamp("DATE_OUT"));
        worker.setDatePost(rs.getTimestamp("DATE_POST"));
        worker.setPostCode(rs.getString("PMASC_POST_CODE"));
        worker.setDepartmentCode(rs.getString("PMASC_DEPART_CODE"));
        worker.setQuantity(rs.getDouble("QUANTITY"));
        worker.setCombinationSign(rs.getInt("COMBINATION_SIGN"));
        worker.setSex(rs.getInt("GENDER"));
        worker.setIsFolder(rs.getInt("IS_FOLDER"));
        worker.setIdent(rs.getString("IDENT_1C"));
        worker.setUserId(rs.getLong("USER_ID"));
        worker.setPersonalCode(rs.getString("CODE_FIZ"));
        worker.setInteractingSysId(rs.getInt("INTERACTING_SYST_ID"));
        worker.setModificationDate(rs.getDate("DATE_CORR"));
        worker.setInn(rs.getString("INN"));
        worker.setIdentfiz(rs.getString("identfiz"));
        worker.setReason(rs.getString("REASON"));
        worker.setBirthday(rs.getTimestamp("BIRTHDAY"));
        return worker;
    }
}
