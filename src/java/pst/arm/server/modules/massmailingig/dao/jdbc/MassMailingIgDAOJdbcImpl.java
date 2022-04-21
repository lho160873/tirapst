/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.massmailingig.dao.jdbc;

import java.util.HashMap;
import org.apache.log4j.Logger;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Igor
 */
@Repository
public class MassMailingIgDAOJdbcImpl extends ArmNamedJdbcImpl implements pst.arm.server.modules.massmailingig.dao.MassMailingIgDao {

    private static final Logger log = Logger.getLogger("MassMailingIgDAOJdbcImpl");

    @Override
    public void addWithCopy(Integer userId, Integer massMailingId) {
        String sql = "EXEC [dbo].[MASS_MAILING_ADD_WITH_COPY] " + userId + ", " + massMailingId;
        log.warn("MassMailingIgDAOJdbcImpl::addWithCopy(" + userId + ", " + massMailingId + " )");
        getJdbcTemplate().update(sql, new HashMap());
    }
}
