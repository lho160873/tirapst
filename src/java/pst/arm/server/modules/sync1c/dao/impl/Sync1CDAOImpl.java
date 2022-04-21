package pst.arm.server.modules.sync1c.dao.impl;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Worker;
import pst.arm.client.modules.sync1c.domain.Worker1C;
import pst.arm.client.modules.sync1c.domain.search.WorkerSearchCondition;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.sync1c.dao.Sync1CDAO;
import pst.arm.server.modules.sync1c.dao.impl.mappers.Post1CRowMapper;
import pst.arm.server.modules.sync1c.dao.impl.mappers.Worker1CRowMapper;
import pst.arm.server.modules.sync1c.dao.impl.mappers.WorkerRowMapper;

/**
 * Created by akozhin on 05.05.15.
 */
@Repository
public class Sync1CDAOImpl extends ArmNamedJdbcImpl implements Sync1CDAO {

    private static Logger log = Logger.getLogger(Sync1CDAOImpl.class);

    @Override
    public Post1C createPost(Post1C post) {
        String sql = "INSERT INTO POST_1C(NAME,CODE,ENABLED,PMASC_POST_CODE,USER_ID,INTERACTING_SYST_ID) "
                + "VALUES(:name,:code,1,:pmascCode,:userId,:interactingSysId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", post.getName());
        params.addValue("code", post.getCode());
        params.addValue("pmascCode", post.getPmascCode());
        params.addValue("userId", post.getUserId());
        params.addValue("interactingSysId", post.getInteractingSysId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, params, keyHolder, new String[]{"POST_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        post.setPostId(keyHolder.getKey().intValue());
        return post;
    }

    @Override
    public Post1C updatePost(Post1C post) {
        String sql = "UPDATE POST_1C "
                + "SET NAME = :name, "
                + "CODE = :code, USER_ID = :userId, "
                + "DATE_CORR = sysdatetime() WHERE POST_ID = :postId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", post.getName());
        params.addValue("code", post.getCode());
        params.addValue("postId", post.getPostId());
        params.addValue("userId", post.getUserId());
        getJdbcTemplate().update(sql, params);
        return post;
    }

    @Override
    public List<Post1C> getPostsByCode(String code) {
        String sql = "SELECT * FROM POST_1C WHERE PMASC_POST_CODE = :code";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource("code", code), new Post1CRowMapper());
    }

    @Override
    public List<Worker1C> getWorkers1CByIdent(String ident) {
        String sql = "SELECT * FROM WORKER_1C WHERE IDENT_1C = :ident";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource("ident", ident), new Worker1CRowMapper());
    }

    @Override
    public Worker1C createWorker1C(Worker1C worker) {
        String sql = "INSERT INTO WORKER_1C("
                + "NAME,PERS_NUMBER,COMPANY_ID,DATE_IN,DATE_POST,"
                + "DATE_OUT,PMASC_POST_CODE,PMASC_DEPART_CODE,"
                + "QUANTITY,COMBINATION_SIGN,GENDER,IS_FOLDER,IDENT_1C,"
                + "USER_ID,CODE_FIZ,INTERACTING_SYST_ID,INN, identfiz, BIRTHDAY, REASON "
                + ") "
                + "VALUES("
                + ":name, :personalNumber,:companyId,:dateIn,:datePost,"
                + ":dateOut,:postCode,:departmentCode,"
                + ":quantity,:combinationSign,:sex,:isFolder,:ident,"
                + ":userId,:personalCode, :interactingSysId, :inn, :identfiz, :birthday, :reason"
                + ")";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", worker.getName());
        params.addValue("personalNumber", worker.getPersonalNumber());
        params.addValue("companyId", worker.getCompanyId());
        params.addValue("dateIn", worker.getDateIn());
        params.addValue("datePost", worker.getDatePost());
        params.addValue("dateOut", worker.getDateOut());
        params.addValue("postCode", worker.getPostCode());
        params.addValue("departmentCode", worker.getDepartmentCode());
        params.addValue("quantity", worker.getQuantity());
        params.addValue("combinationSign", worker.getCombinationSign());
        params.addValue("sex", worker.getSex());
        params.addValue("ident", worker.getIdent());
        params.addValue("isFolder", worker.getIsFolder());
        params.addValue("userId", worker.getUserId());
        params.addValue("personalCode", worker.getPersonalCode());
        params.addValue("interactingSysId", worker.getInteractingSysId());
        params.addValue("inn", worker.getInn());
        params.addValue("identfiz", worker.getIdentfiz());
        params.addValue("birthday", worker.getBirthday());
        params.addValue("reason", worker.getReason());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, params, keyHolder, new String[]{"WORKER_1C_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        worker.setWorkerId(keyHolder.getKey().intValue());
        return worker;
    }

    @Override
    public Worker1C updateWorker1C(Worker1C worker) {
        String sql = "UPDATE WORKER_1C SET "
                + "NAME = :name,"
                + "PERS_NUMBER = :personalNumber,"
                + "COMPANY_ID = :companyId,"
                + "DATE_IN = :dateIn,"
                + "DATE_POST = :datePost,"
                + "DATE_OUT = :dateOut,"
                + "PMASC_POST_CODE = :postCode,"
                + "PMASC_DEPART_CODE = :departmentCode,"
                + "QUANTITY = :quantity,"
                + "COMBINATION_SIGN = :combinationSign,"
                + "GENDER = :sex,"
                + "IS_FOLDER = :isFolder,"
                + "USER_ID = :userId,"
                + "DATE_CORR = sysdatetime(),"
                + "CODE_FIZ = :personalCode,"
                + "INTERACTING_SYST_ID = :interactingSysId,"
                + "INN = :inn, "
                + "identfiz = :identfiz, "
                + "BIRTHDAY = :birthday, "
                + "REASON = :reason "
                + "WHERE WORKER_1C_ID = :workerId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("workerId", worker.getWorkerId());
        params.addValue("name", worker.getName());
        params.addValue("personalNumber", worker.getPersonalNumber());
        params.addValue("companyId", worker.getCompanyId());
        params.addValue("dateIn", worker.getDateIn());
        params.addValue("datePost", worker.getDatePost());
        params.addValue("dateOut", worker.getDateOut());
        params.addValue("postCode", worker.getPostCode());
        params.addValue("departmentCode", worker.getDepartmentCode());
        params.addValue("quantity", worker.getQuantity());
        params.addValue("combinationSign", worker.getCombinationSign());
        params.addValue("sex", worker.getSex());
        params.addValue("isFolder", worker.getIsFolder());
        params.addValue("userId", worker.getUserId());
        params.addValue("personalCode", worker.getPersonalCode());
        params.addValue("interactingSysId", worker.getInteractingSysId());
        params.addValue("inn", worker.getInn());
        params.addValue("identfiz", worker.getIdentfiz());
        params.addValue("birthday", worker.getBirthday());
        params.addValue("reason", worker.getReason());
        getJdbcTemplate().update(sql, params);
        return worker;
    }

    @Override
    public boolean isDepartmentExists(Department1C department) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", department.getName());
        params.addValue("code", department.getCode());
        params.addValue("companyId", department.getCompanyId());

        String sql = "SELECT COUNT(*) FROM DEPART_1C WHERE "
                + " NAME = :name AND COMPANY_ID = :companyId AND "
                + "PMASC_DEPART_CODE = :code AND PARENT_DEPART_CODE ";
        if (department.hasParent()) {
            sql += "= :parentCode ";
            params.addValue("parentCode", department.getParent().getCode());
        } else {
            sql += " IS NULL ";
        }
        int count = getJdbcTemplate().queryForObject(sql, params, Integer.class);
        return count == 1;
    }

    @Override
    public Department1C createDepartment(Department1C department) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", department.getName());
        params.addValue("code", department.getCode());
        params.addValue("companyId", department.getCompanyId());
        params.addValue("userId", department.getUserId());
        String parentCodeParam = " NULL ";
        if (department.hasParent()) {
            params.addValue("parentCode", department.getParent().getCode());
            parentCodeParam = ":parentCode";
        }
        String sql = "INSERT INTO DEPART_1C(NAME, COMPANY_ID, PMASC_DEPART_CODE, PARENT_DEPART_CODE,USER_ID) "
                + "VALUES(:name,:companyId,:code," + parentCodeParam + ", :userId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, params, keyHolder, new String[]{"DEPART_1C_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        department.setDepartmentId(keyHolder.getKey().intValue());
        return department;
    }

    @Override
    public List<Worker> getWorkers(WorkerSearchCondition condition) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "SELECT * FROM V_POST_WORKER_N "
                + "WHERE ";
        switch (condition.getSearchType()) {
            case base:
                sql += " PMASC_POST_CODE = :postCode AND "
                        + " PMASC_DEPART_CODE = :departCode AND "
                        + " COMPANY_ID = :companyId AND"
                        + " NAME = :name ";
                params.addValue("postCode", condition.getWorker1C().getPostCode());
                params.addValue("departCode", condition.getWorker1C().getDepartmentCode());
                params.addValue("companyId", condition.getWorker1C().getCompanyId());
                params.addValue("name", condition.getWorker1C().getName());
                if (StringUtils.isNotBlank(condition.getWorker1C().getInn())) {
                    sql += " AND INN = :inn ";
                    params.addValue("inn", condition.getWorker1C().getInn());
                } else {
                    sql += " AND INN IS NULL ";
                }
                break;
            case extended:
                sql += " POST_WORKER_ID = :postWorkerId AND "
                        + " QUANTITY = :rate AND "
                        + " COMBINATION_SIGN = :combinationSign AND "
                        + " PERS_NUMBER = :persNumber AND "
                        + " IDENT_1C = :ident ";
                params.addValue("postWorkerId", condition.getPostWorkerId());
                params.addValue("rate", condition.getWorker1C().getQuantity());
                params.addValue("combinationSign", condition.getWorker1C().getCombinationSign());
                params.addValue("persNumber", condition.getWorker1C().getPersonalNumber());
                params.addValue("ident", condition.getWorker1C().getIdent());
                if (condition.getWorker1C().getDatePost() != null) {
                    sql += " AND DATE_IN = :datePost ";
                    params.addValue("datePost", condition.getWorker1C().getDatePost());
                } else {
                    sql += " AND DATE_IN IS NULL ";
                }
                if (condition.getWorker1C().getDateOut() != null) {
                    sql += " AND DATE_OUT = :dateOut ";
                    params.addValue("dateOut", condition.getWorker1C().getDateOut());
                } else {
                    sql += " AND DATE_OUT IS NULL ";
                }
                break;
        }

        List<Worker> list = getJdbcTemplate().query(sql, params, new WorkerRowMapper());
        if (list != null) {
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Worker createWorker(Worker worker) {
        String sql = "INSERT INTO POST_WORKER_N("
                + "QUANTITY,COMBINATION_SIGN,PERS_NUMBER,DATE_IN,DATE_OUT,IDENT_1C,"
                + "USER_ID,ENABLED,IS_HEAD,DEPART_ID,POST_ID,WORKER_ID"
                + ") "
                + "VALUES("
                + ":rate,:combinationSign,:persNumber,:datePost,:dateOut,:ident,"
                + ":userId,1,0,:departId,:postId,:workerId"
                + ")";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("rate", worker.getQuantity());
        params.addValue("combinationSign", worker.getCombinationSign());
        params.addValue("persNumber", worker.getPersonalNumber());
        params.addValue("datePost", worker.getDatePost());
        params.addValue("dateOut", worker.getDateOut());
        params.addValue("ident", worker.getIdent());
        params.addValue("userId", worker.getUserId());
        params.addValue("departId", worker.getDepartmentId());
        params.addValue("postId", worker.getPostId());
        params.addValue("workerId", worker.getWorkerId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, params, keyHolder, new String[]{"POST_WORKER_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        worker.setPostWorkerId(keyHolder.getKey().intValue());
        return worker;
    }

    @Override
    public Worker createWorkerTransfer(Worker worker) {
        String sql = "INSERT INTO POST_WORKER_N("
                + "QUANTITY,COMBINATION_SIGN,PERS_NUMBER,DATE_IN,IDENT_1C,"
                + "USER_ID,ENABLED,IS_HEAD,DEPART_ID,POST_ID,WORKER_ID"
                + ") "
                + "VALUES("
                + ":rate,:combinationSign,:persNumber,:datePost,:ident,"
                + ":userId,1,0,:departId,:postId,:workerId"
                + ")";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("rate", worker.getQuantity());
        params.addValue("combinationSign", worker.getCombinationSign());
        params.addValue("persNumber", worker.getPersonalNumber());
        params.addValue("datePost", worker.getDatePost());
        params.addValue("dateOut", worker.getDateOut());
        params.addValue("ident", worker.getIdent());
        params.addValue("userId", worker.getUserId());
        params.addValue("departId", worker.getDepartmentId());
        params.addValue("postId", worker.getPostId());
        params.addValue("workerId", worker.getWorkerId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, params, keyHolder, new String[]{"POST_WORKER_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        worker.setPostWorkerId(keyHolder.getKey().intValue());
        return worker;
    }

    @Override
    public Worker updateWorker(Worker worker) {
        String sql = "UPDATE POST_WORKER_N SET "
                + " QUANTITY = :rate,"
                + " COMBINATION_SIGN = :combinationSign,"
                + " PERS_NUMBER = :persNumber,"
                + " DATE_IN = :datePost,"
                + " DATE_OUT = :dateOut,"
                + " IDENT_1C = :ident,"
                + " USER_ID = :userId "
                + "WHERE POST_WORKER_ID = :postWorkerId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("postWorkerId", worker.getPostWorkerId());
        params.addValue("rate", worker.getQuantity());
        params.addValue("combinationSign", worker.getCombinationSign());
        params.addValue("persNumber", worker.getPersonalNumber());
        params.addValue("datePost", worker.getDatePost());
        params.addValue("dateOut", worker.getDateOut());
        params.addValue("ident", worker.getIdent());
        params.addValue("userId", worker.getUserId());
        getJdbcTemplate().update(sql, params);
        return worker;
    }

    @Override
    public List<Integer> getWorkerIdsByNameAndInn(String name, String inn) {
        try {
            String sql = "SELECT DISTINCT WORKER_ID FROM WORKER WHERE NAME = :name";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", name);
            if (StringUtils.isNotBlank(inn)) {
                sql += " AND INN = :inn ";
                params.addValue("inn", inn);
            } else {
                sql += " AND INN IS NULL ";
            }
            return getJdbcTemplate().queryForList(sql, params, Integer.class);
        } catch (Exception e) {
            log.error(String.format("Error get workerid by name %s and inn %s", name, inn));
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Integer> getDepartmentIdsByCodeAndCompany(String departmentCode, Integer companyId) {
        try {
            String sql = "SELECT DISTINCT DEPART_ID FROM DEPART WHERE PMASC_DEPART_CODE=:departmentCode AND "
                    + " COMPANY_ID=:companyId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("departmentCode", departmentCode);
            params.addValue("companyId", companyId);
            return getJdbcTemplate().queryForList(sql, params, Integer.class);

        } catch (Exception e) {
            log.error(String.format("Error get departId by code %s and company %s", departmentCode, companyId));
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Integer> getPostIdsByCode(String postCode) {
        try {
            String sql = "SELECT DISTINCT POST_ID FROM POST WHERE PMASC_POST_CODE = :postCode";
            return getJdbcTemplate().queryForList(sql, new MapSqlParameterSource("postCode", postCode), Integer.class);

        } catch (Exception e) {
            log.error(String.format("Error get postId by postCode %s", postCode));
        }
        return Collections.EMPTY_LIST;
    }
}
