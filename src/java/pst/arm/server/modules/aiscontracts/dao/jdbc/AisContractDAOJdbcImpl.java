package pst.arm.server.modules.aiscontracts.dao.jdbc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractField;
import pst.arm.client.modules.aiscontracts.domain.AisContractRefTable;
import pst.arm.client.modules.aiscontracts.domain.AisContractStage;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.client.modules.aiscontracts.domain.FieldType;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.aiscontracts.dao.AisContractDAO;
import pst.arm.server.modules.aiscontracts.dao.jdbc.mappers.AisContractMapper;
import pst.arm.server.modules.aiscontracts.dao.jdbc.mappers.AisContractStageMapper;
import pst.arm.server.modules.aiscontracts.dao.jdbc.mappers.AisContractSyncMapper;
import pst.arm.server.modules.aiscontracts.dao.jdbc.mappers.AisOrganisationMapper;
import static pst.arm.server.utils.ServerUtil.isNotEmpty;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Repository
public class AisContractDAOJdbcImpl extends ArmNamedJdbcImpl implements AisContractDAO {

    private static Logger log = Logger.getLogger("AisContractDAOJdbcImpl");
    private static Map<String, String> mapFields2Column = null;

    private static final String DATETIME_FORMAT_DEFAULT_SQL = "DD.MM.YY";
    private static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy";

    private static final String YEAR_FORMAT_DEFAULT_SQL = "YYYY";

    private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy H:mm:ss");

    private String getQuerySelect() {
        String sql = " SELECT "
                + "  M.CONTRACT_ID AS M_CONTRACT_ID,"
                + "  M.CONTRACT_NUMB AS M_CONTRACT_NUMB,"
                + "  M.CONTRACT_TYPE_ID AS M_CONTRACT_TYPE_ID,"
                + "  M.CONTRACT_STAT_ID AS M_CONTRACT_STAT_ID,"
                + "  M.ORG_EXECUTOR_ID AS M_ORG_EXECUTOR_ID,"
                + "  M.CONTRACT_DATE AS M_CONTRACT_DATE,"
                + "  M.COST AS M_COST,"
                + "  M.CUST_NDS AS M_CUST_NDS,"
                + "  M.BEG_DATE AS M_BEG_DATE,"
                + "  M.END_DATE AS M_END_DATE,"
                + "  O.COMPANY_ID AS O_COMPANY_ID";

        return sql;

    }

    private String getQueryFrom() {
        String sql = " FROM CONTRACT M INNER JOIN ORGANIZATION O ON M.ORG_EXECUTOR_ID=O.ORGANIZATION_ID ";

        return sql;
    }

    private String getQueryWhereJoin() {
        String sql = "";
        return sql;

    }

    private String getQueryWhereSearch(AisContractSearchCondition condition, Boolean isWhere) {

        String sql = "";
        if (condition == null) {
            return sql;
        }

        for (Map.Entry colEntry : condition.getSearches().entrySet()) {
            {
                if (colEntry.getValue() == null) {
                    continue;
                }
                String field = (String) colEntry.getKey();
                String where = " UPPER(" + field2column(field) + ") LIKE UPPER('%" + String.valueOf(colEntry.getValue()) + "%') ";

                sql += (sql.isEmpty() && !isWhere) ? " WHERE " : " AND ";
                sql += where;
            }
        }
        return sql;
    }

    private String getQueryWhereFiltr(AisContractSearchCondition condition, Boolean isWhere) {
        if (condition == null) {
            return "";
        }
        String sql = "";
        if (condition.getIsDateNotNull()) {
            sql += ((sql.isEmpty() && !isWhere) ? " WHERE " : " AND ") + "M.CONTRACT_DATE IS NOT NULL AND M.BEG_DATE IS NOT NULL AND M.END_DATE IS NOT NULL ";
        }
        String year = condition.getYear();
        if (!year.isEmpty()) {
            sql += ((sql.isEmpty() && !isWhere) ? " WHERE " : " AND ") + " ( YEAR(M.BEG_DATE) <= " + year + " OR M.BEG_DATE IS NULL ) AND ( YEAR(M.END_DATE) >= " + year + " OR M.END_DATE IS NULL )";

        }
        for (Map.Entry colEntry : condition.getFilters().entrySet()) {
            {
                if (colEntry.getValue() == null) {
                    continue;
                }
                String field = (String) colEntry.getKey();
                String where = field2column(field) + " = '" + String.valueOf(colEntry.getValue()) + "' ";
                sql += (sql.isEmpty() && !isWhere) ? " WHERE " : " AND ";
                sql += where;
            }
        }
        return sql;
    }

    private String getQueryWhere(AisContractSearchCondition condition) {
        String sql = getQueryWhereJoin();
        boolean isWhere = sql.contains("WHERE");
        sql += getQueryWhereSearch(condition, isWhere);
        isWhere = sql.contains("WHERE");
        sql += getQueryWhereFiltr(condition, isWhere);
        return sql;
    }

    private String getOrderBy(AisContractSearchCondition condition) {
        String sql = "";
        if (condition != null && condition.getSortField() != null && !condition.getSortField().isEmpty() && !condition.getSortDir().equals("NONE")) {
            sql = field2column(condition.getSortField()) + " " + condition.getSortDir();
            log.warn("-----------------------------------------");
            log.warn("DDataGridDAOJdbcImpl::getOrderBy  condition.getSortDir().getSortField() = " + condition.getSortField());
        } else {
            sql += "M.CONTRACT_DATE DESC";
        }
        if (!sql.isEmpty()) {
            sql = " ORDER BY " + sql;
        }
        return sql.toUpperCase();
    }

    @Override
    public List<AisContract> select(AisContractSearchCondition condition, Boolean isPaging) {
        String querySub = getQuerySelect() + getQueryFrom() + getQueryWhere(condition) + getOrderBy(condition);
        String sql = "";
        if (isPaging) {
            sql = querySub + getPagingWhere(condition);
        } else {
            sql = querySub;
        }
        log.warn("AisContractDAOJdbcImpl::select  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();

        return getJdbcTemplate().query(sql, params, new AisContractMapper());
    }

    @Override
    public List<AisContract> selectAll() {
        String sql = getQuerySelect() + getQueryFrom() + getQueryWhereJoin() + getOrderBy(new AisContractSearchCondition()) + ";";
        log.warn("AisContractDAOJdbcImpl::selectAll  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();
        return getJdbcTemplate().query(sql, params, new AisContractMapper());
    }

    private String getPagingWhere(AisContractSearchCondition condition) {
        if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
            return " LIMIT " + (condition.getOffset()) + ", "
                    + (condition.getOffset() + condition.getLimit() - 1);
        } else {
            return " ";
        }
    }

    @Override
    public int count(AisContractSearchCondition condition) {
        String sql = "SELECT COUNT(*) " + getQueryFrom() + getQueryWhere(condition);
        log.warn("DDataGridDAOJdbcImpl::count  = " + sql);
        return getJdbcTemplate().queryForInt(sql, new MapSqlParameterSource());

    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public AisContract insert(final AisContract domain) {
        log.warn("AisContractDAOJdbcImpl::insert BEGIN ");
        String sql = " INSERT INTO CONTRACT("
                + "     CLIENT_ID,"
                + "     CONTRACT_NUMB,"
                + "     CONTRACT_DATE,"
                + "     BEG_DATE,"
                + "     END_DATE,"
                + "     CURRENCY_ID,"
                + "     BANK_DAY,"
                + "     CALENDAR_DAY,"
                + "     CONTRACT_TYPE_ID,"
                + "     PAYMENT_FORM_ID,"
                + "     PATH_TO_FOLDER,"
                + "     USER_ID"
                + " )VALUES("
                + "     :clientId, "
                + "     :contractNumb, "
                + "     :contractDate, "
                + "     :begDate, "
                + "     :endDate, "
                + "     :currencyId, "
                + "     :bankDay, "
                + "     :calendarDay, "
                + "     :contractTypeId, "
                + "     :paymentFormId, "
                + "      :pathToFolder,"
                + "     :userId)";
        log.warn("AisContractDAOJdbcImpl::insert sql = " + sql);

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(domain);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Integer rows = getJdbcTemplate().update(sql, namedParameters, keyHolder, new String[]{"CONTRACT_ID"});
        if (!rows.equals(1)) {
            return null;
        }

        domain.setContractId(keyHolder.getKey().intValue());

        return domain;

    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean update(final AisContract domain) {
        String sql = "UPDATE CONTRACT SET "
                + "     CLIENT_ID            = :clientId, "
                + "     CONTRACT_NUMB        = :contractNumb, "
                + "     CONTRACT_DATE        = :contractDate, "
                + "     BEG_DATE             = :begDate, "
                + "     END_DATE             = :endDate, "
                + "     CURRENCY_ID          = :currencyId, "
                + "     BANK_DAY             = :bankDay, "
                + "     CALENDAR_DAY         = :calendarDay, "
                + "     CONTRACT_TYPE_ID     = :contractTypeId, "
                + "     PAYMENT_FORM_ID      = :paymentFormId, "
                + "     PATH_TO_FOLDER       = :pathToFolder, "
                + "     USER_ID              = :userId "
                + " WHERE "
                + "     CONTRACT_ID           = :contractId ";

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(domain);
        getJdbcTemplate().update(sql, namedParameters);

        return true;
    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean delete(long id) {
        String query = "DELETE CONTRACT "
                + "WHERE CONTRACT_ID = :contractId";

        HashMap<String, Long> params = new HashMap();
        params.put("contractId", id);

        getJdbcTemplate().update(query, params);
        return true;
    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean saveWithDeleteContructNorm(final AisContract domain) {

        update(domain);

        String query = "DELETE CONTRACT_NORM "
                + "WHERE CONTRACT_ID = :contractId";
        long id = (long) domain.getContractId();
        HashMap<String, Long> params = new HashMap();
        params.put("contractId", id);

        getJdbcTemplate().update(query, params);
        return true;
    }

    @Override
    public AisContract selectRow(long id) {
        log.warn("getContractForId");
        AisContract domain = null;

        String querySub = getQuerySelect() + getQueryFrom() + getQueryWhereJoin();

        querySub += " AND M.CONTRACT_ID = :id";
        HashMap<String, Long> params = new HashMap();
        params.put("id", Long.valueOf(id));
        log.warn(querySub);
        List<AisContract> res = getJdbcTemplate().query(querySub, params, new AisContractMapper());

        if (!res.isEmpty()) {
            domain = res.get(0);
        }
        return domain;
    }

    private static String field2column(String field) {
        if (mapFields2Column == null) {
            mapFields2Column = new HashMap<String, String>();
            mapFields2Column.put("contractId", "M.CONTRACT_ID");
            mapFields2Column.put("contractTypeId", "M.CONTRACT_TYPE_ID");
            mapFields2Column.put("contractStatId", "M.CONTRACT_STAT_ID");
            mapFields2Column.put("orgExecutorId", "M.ORG_EXECUTOR_ID");
            mapFields2Column.put("companyId", "O.COMPANY_ID");
            mapFields2Column.put("cost", "M.COST");
            mapFields2Column.put("custNds", "M.CUST_NDS");
        }
        return mapFields2Column.get(field);
    }

    @Override
    public AisContract create(AisContract domain) {
        try {
            String columns = "";
            String params = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisContractField, String> field : domain.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    columns += "," + field.getKey().getColumn();
                    params += ", :" + field.getKey().toString();
                    Object castedValue = getCastedValue(field.getKey().getType(), domain.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                    }
                }
            }
            columns = columns.replaceFirst(",", "");
            params = params.replaceFirst(",", "");
            String sql = " INSERT INTO CONTRACT("
                    + columns
                    + " )VALUES("
                    + params
                    + ")";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            Integer rows = getJdbcTemplate().update(sql, map, keyHolder, new String[]{"CONTRACT_ID"});
            if (!rows.equals(1)) {
                return null;
            }
            domain.setContractId(keyHolder.getKey().intValue());
        } catch (Exception e) {
            log.error("Error creating contract(" + domain.asString(false) + ") " + "//" + e.getMessage());
        }
        return domain;
    }

    @Override
    public AisContractStage createStage(AisContractStage stage) {
        try {
            String columns = "";
            String params = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisContractStage.Field, String> field : stage.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    columns += "," + field.getKey().getColumn();
                    params += ", :" + field.getKey().toString();
                    Object castedValue = getCastedValue(field.getKey().getType(), stage.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                    }

                }
            }

            columns = columns.replaceFirst(",", "");
            params = params.replaceFirst(",", "");
            String sql = " INSERT INTO SERV_CONTRACT_STAGE("
                    + columns
                    + " )VALUES("
                    + params
                    + ")";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            Integer rows = getJdbcTemplate().update(sql, map, keyHolder, new String[]{"CONTRACT_STAGE_ID"});
            if (!rows.equals(1)) {
                return null;
            }
            stage.setStageId(keyHolder.getKey().intValue());
//            log.info("Created " + stage.asString());
        } catch (Exception e) {
            log.error("Error creating (" + stage.asString() + ") " + "//" + e.getMessage());

        }
        return stage;
    }

    @Override
    public Object getRefRecordId(AisContractRefTable ref, String value) {
        String sql = "SELECT " + ref.getId() + " FROM " + ref.getTable() + " WHERE " + ref.getName() + " = :value";
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, new MapSqlParameterSource("value", value));
        if (isNotEmpty(list)) {
            return list.get(0).get(ref.getId());
        }
        return ref.getDefaultValue();
    }

    @Override
    public AisOrganization saveOrganisation(AisOrganization org) {
        try {
            String columns = "";
            String params = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisOrganization.Field, String> field : org.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    columns += "," + field.getKey().getColumn();
                    params += ", :" + field.getKey().toString();
                    Object castedValue = getCastedValue(field.getKey().getType(), org.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                    }
                }
            }

            columns = columns.replaceFirst(",", "");
            params = params.replaceFirst(",", "");
            String sql = " INSERT INTO ORGANIZATION("
                    + columns
                    + " )VALUES("
                    + params
                    + ")";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            Integer rows = getJdbcTemplate().update(sql, map, keyHolder, new String[]{"CONTRACT_STAGE_ID"});
            if (!rows.equals(1)) {
                return null;
            }
            org.setId(keyHolder.getKey().intValue());
//            log.info("Created " + org.asString());
        } catch (Exception e) {
            log.error("Error creating (" + org.asString() + ") " + "//" + e.getMessage());

        }
        return org;
    }

    @Override
    public Integer getContractIdByNum(String num) {
        String sql = "SELECT MIN(CONTRACT_ID) FROM CONTRACT WHERE CONTRACT_NUMB = :num AND PARENT_ID is NULL";
        return getJdbcTemplate().queryForObject(sql, new MapSqlParameterSource("num", num), Integer.class);
    }

    @Override
    public AisOrganization getOrganisationByIdAis(String idAis) {
        String sql = "SELECT * FROM ORGANIZATION WHERE ID_AIS = :id";
        List<AisOrganization> organisations = getJdbcTemplate().query(sql, new MapSqlParameterSource("id", idAis), new AisOrganisationMapper());
        if (organisations != null && organisations.size() > 0) {
            return organisations.get(0);
        }
        return null;
    }

    @Override
    public AisContract getAisContractByIdAis(String idAis) {
        String sql = "SELECT * FROM CONTRACT WHERE ID_AIS = :id";
        List<AisContract> contracts = getJdbcTemplate().query(sql, new MapSqlParameterSource("id", idAis), new AisContractSyncMapper());
        if (contracts != null && contracts.size() > 0) {
            return contracts.get(0);
        }
        return null;
    }

    @Override
    public AisOrganization updateOrganisation(AisOrganization org) {
        try {
            String sqlColumns = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisOrganization.Field, String> field : org.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    Object castedValue = getCastedValue(field.getKey().getType(), org.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                        sqlColumns += ", " + field.getKey().getColumn() + " = :" + field.getKey().toString() + " ";
                    }
                }
            }
            map.addValue("orgId", org.getId());
            sqlColumns = sqlColumns.replaceFirst(",", "");
            String sql = "UPDATE ORGANIZATION SET " + sqlColumns + " WHERE ORGANIZATION_ID = :orgId";
            getJdbcTemplate().update(sql, map);
//            log.info("Updated " + org.asString());
        } catch (Exception e) {
            log.error("Error updating (" + org.asString() + ") " + "//" + e.getMessage());

        }
        return org;
    }

    @Override
    public AisContract updateAisContract(AisContract domain) {
        try {
            String sqlColumns = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisContractField, String> field : domain.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    Object castedValue = getCastedValue(field.getKey().getType(), domain.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                        sqlColumns += ", " + field.getKey().getColumn() + " = :" + field.getKey().toString() + " ";
                    }
                }
            }
            map.addValue("contractId", domain.getContractId());
            sqlColumns = sqlColumns.replaceFirst(",", "");
            String sql = "UPDATE CONTRACT SET " + sqlColumns + " WHERE CONTRACT_ID = :contractId";
            getJdbcTemplate().update(sql, map);
//            log.info("Updated " + domain.asString(false));
        } catch (Exception e) {
            log.error("Error updating contract(" + domain.asString(false) + ") " + "//" + e.getMessage());
            e.printStackTrace(System.err);
        }
        return domain;
    }

    @Override
    public AisContractStage getAisContractStageByContractAndLine(Integer contractId, String lineNumber) {
        String sql = "SELECT * FROM SERV_CONTRACT_STAGE WHERE CONTRACT_ID = :contractId AND LINE_AIS = :lineNumber";
        MapSqlParameterSource paramsMap = new MapSqlParameterSource("contractId", contractId);
        paramsMap.addValue("lineNumber", lineNumber);
        List<AisContractStage> list = getJdbcTemplate().query(sql, paramsMap, new AisContractStageMapper());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;

    }

    @Override
    public void updateStage(AisContractStage stage) {
        try {
            String sqlColumns = "";
            MapSqlParameterSource map = new MapSqlParameterSource();
            for (Map.Entry<AisContractStage.Field, String> field : stage.getFieldValues().entrySet()) {
                if (!StringUtils.isBlank(field.getKey().getColumn()) && !StringUtils.isBlank(field.getValue())) {
                    Object castedValue = getCastedValue(field.getKey().getType(), stage.getField(field.getKey()));
                    if (castedValue != null) {
                        map.addValue(field.getKey().toString(), castedValue);
                        sqlColumns += ", " + field.getKey().getColumn() + " = :" + field.getKey().toString() + " ";
                    }
                }
            }
            map.addValue("contractStageId", stage.getStageId());
            sqlColumns = sqlColumns.replaceFirst(",", "");
            String sql = "UPDATE SERV_CONTRACT_STAGE SET " + sqlColumns + " WHERE CONTRACT_STAGE_ID = :contractStageId";
            getJdbcTemplate().update(sql, map);
//            log.info("Updated " + stage.asString());
        } catch (Exception e) {
            log.error("Error updating stage(" + stage.asString() + ") " + "//" + e.getMessage());
        }
    }

    private Object getCastedValue(FieldType type, String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
            try {
                Object value = null;
                switch (type) {
                    case String:
                        value = stringValue;
                        break;
                    case Boolean:
                        if (stringValue != null && stringValue.equalsIgnoreCase("Да")) {
                            value = Boolean.TRUE;
                        } else {
                            value = Boolean.valueOf(stringValue);
                        }
                        break;
                    case Integer:
                        value = Integer.valueOf(stringValue.replaceAll("\\D", ""));
                        break;
                    case Double:
                        value = Double.valueOf(stringValue.replaceAll("\\D", ""));
                        break;
                    case Date:
                        value = df.parse(stringValue);
                        break;
                }
                return value;
            } catch (Exception e) {
                System.out.println("Error casting value");
                e.printStackTrace(System.out);
            }
        }
        return null;
    }
}
