package pst.arm.server.modules.leveltask.dao.jdbc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.User;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.common.service.UserService;
import pst.arm.server.modules.leveltask.dao.LevelTaskDAO;
import pst.arm.server.modules.leveltask.dao.jdbc.mappers.LevelTaskIdMapper;
import pst.arm.server.modules.leveltask.dao.jdbc.mappers.LevelTaskMapper;
import static pst.arm.server.utils.ServerUtil.isNotEmpty;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
@Repository
public class LevelTaskDAOJdbcImpl extends ArmNamedJdbcImpl implements LevelTaskDAO {

    private static Logger log = Logger.getLogger("LevelTaskDAOJdbcImpl");
    private static Map<String, String> mapFields2Column = null;
    
    //private static final String DATETIME_FORMAT_DEFAULT_SQL = "DD.MM.YY";
    //private static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy";
   private UserService userService;
 private User currentUser = null;
 
    @Autowired
    public void setUserService(UserService service) {
        this.userService = service;
    }
    
    private String getQuerySelectTop( LevelTaskSearchCondition condition) {
        Integer top = condition.getOffset() + condition.getLimit();
        String strSelect = " SELECT  TOP " + String.valueOf(top)  + getFieldSelect();
         
        strSelect+=" , ROW_NUMBER() OVER ( "+ getOrderBy(condition)+") AS RNUM " ;
        return strSelect;
    }
    
    private String getQuerySelect() {
        String sql = " SELECT "+getFieldSelect();

        return sql;

    }
    private String getQuerySelectTop3() {
        String sql = " SELECT TOP 3 "+getFieldSelect();
        return sql;
    }
    
    private String getFieldSelect() {
        String sql = " M.LEVEL_TASK_ID AS M_LEVEL_TASK_ID,"
                + "  M.TASK_TYPE_ID AS M_TASK_TYPE_ID,"
                + "  T.NAME AS T_NAME,"
                + "  M.DEPART_ID AS M_DD,"
                + "  M.DEPART_ID M_LL,"
                + "  M.TASK_STATE AS M_TASK_STATE,"
                + "  M.REPLY AS M_REPLY,"
                + "  M.REPLY_DATE AS M_REPLY_DATE,"
                + "  M.REPLY_NUMBER AS M_REPLY_NUMBER,"
                + "  S.NAME AS S_NAME,"
                + "  M.CREATE_DATE AS M_CREATE_DATE,"
                + "  M.EVENT_TIME AS M_EVENT_TIME,"
                + "  V.NAME AS V_WPINDEX, "
                + "  VV.NAME AS VV_WPINDEX, "
                + "  M.DESCRIPTION M_DESCRIPTION, "
                + "  SUBSTRING(M.DESCRIPTION,0,120) +  "
                + "  CASE "
                + "  WHEN LEN(M.DESCRIPTION)>120 THEN ' ...' "
                + "  ELSE '' "
                + "  END  M_DESCRIPTION_SHORT, "          
                + "  M.PRIORITY M_PRIORITY, "
                + "  M.USER_ID_FROM AS M_USER_ID_FROM, "
                + "  M.MODULE AS M_MODULE, "
                + "  M.ID AS M_ID, "
                + "  M.SEND_SIGN AS M_SEND_SIGN, "
                + "  M.USER_ID_TO AS M_USER_ID_TO ";

        return sql;

    }

    private String getQueryFrom() {
        String sql = " FROM LEVEL_TASK M LEFT JOIN TASK_TYPE T ON T.TASK_TYPE_ID = M.TASK_TYPE_ID"
                + " LEFT JOIN TASK_STATE S ON S.TASK_STATE = M.TASK_STATE"
                + " LEFT JOIN USERS V ON V.USER_ID = M.USER_ID_FROM"
                + " LEFT JOIN USERS VV ON VV.USER_ID = M.USER_ID_TO";
        return sql;
    }

   

    private String getQueryWhereSearch(LevelTaskSearchCondition condition, Boolean isWhere) {
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
                String where = "";
                if (field.equals("begDate")) {
                    where = "CONVERT(DATETIME,CONVERT(VARCHAR,"+field2column(field)+",104))" + " >= CONVERT(DATETIME,'" + String.valueOf(colEntry.getValue())+"')";

                } else if (field.equals("endDate")) {
                    where = "CONVERT(DATETIME,CONVERT(VARCHAR,"+field2column(field)+",104))" + " <= CONVERT(DATETIME,'" + String.valueOf(colEntry.getValue())+"')";

                } else {
                    where = " UPPER(" + field2column(field) + ") LIKE UPPER('%" + String.valueOf(colEntry.getValue()) + "%') ";
                }
                sql += (sql.isEmpty() && !isWhere) ? " WHERE " : " AND ";
                sql += where;
                
            }
        }
        return sql;
    }
     
    private String getQueryWhereFiltr(LevelTaskSearchCondition condition, Boolean isWhere) {
        Boolean w = isWhere;
        String sql = "";
        for (Map.Entry colEntry : condition.getFilters().entrySet()) {
            {
                if (colEntry.getValue() == null) {
                    continue;
                }
                String field = (String) colEntry.getKey();
                String where = field2column(field) + " = '" + String.valueOf(colEntry.getValue()) + "' ";

                sql += (sql.isEmpty() && !w) ? " WHERE " : " AND ";
                sql += where;
            }
        }

        if (!condition.getIsAll() && condition.getSearches().get("stateName") == null) {
            sql += (sql.isEmpty() && !w) ? " WHERE " : " AND ";
            if (condition.getFilters().containsKey("userIdFrom"))
                sql += "M.TASK_STATE <= 2";
            else
                sql += "M.TASK_STATE < 2";
        }

        return sql;
    }
  
    private String getQueryWhere(LevelTaskSearchCondition condition) {
        String sql = "";//getQueryWhereJoin();
        boolean isWhere = sql.contains("WHERE");
        sql += getQueryWhereSearch(condition,isWhere);       
        isWhere = sql.contains("WHERE");
        sql += getQueryWhereFiltr(condition,isWhere);                
        return sql;
    }
   
     private String getOrderBy(LevelTaskSearchCondition condition) {
        String sql = "";
        if (condition != null && condition.getSortField() != null && !condition.getSortField().isEmpty() && !condition.getSortDir().equals("NONE")) {
            sql = field2column(condition.getSortField()) + " " + condition.getSortDir();
            log.warn("-----------------------------------------");
            log.warn("LevelTaskDAOJdbcImpl::getOrderBy  condition.getSortDir().getSortField() = " + condition.getSortField());
        } else {
            sql += "M.CREATE_DATE DESC";
        }
        if (!sql.isEmpty()) {
            sql = " ORDER BY " + sql;            
        }
        return sql.toUpperCase();
    }
    
    @Override
    public List<LevelTask> select(LevelTaskSearchCondition condition, Boolean isPaging) {
        //String querySub = getQuerySelect() + getQueryFrom() + getQueryWhere(condition) + getOrderBy(condition);
        String querySub = ( isPaging ? getQuerySelectTop(condition) : getQuerySelect() ) + getQueryFrom() + getQueryWhere( condition) + getOrderBy( condition);
        String sql = "";
        if (isPaging) {
            /*sql = "SELECT A1.* FROM ( "
                    + "SELECT A2.*, ROWNUM RN "
                    + "FROM ( " + querySub
                    + ") A2 ) A1 " + getPagingWhere(condition);*/
            sql = "SELECT * FROM ( "+
                    querySub +
                    ") A1 " + getPagingWhere(condition);
        } else {
            sql = querySub;
        }
        log.warn("LevelTaskDAOJdbcImpl::select  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();
        return getJdbcTemplate().query(sql, params, new LevelTaskMapper());
    }

    @Override
    public List<LevelTask> selectAll() {
        String sql = getQuerySelect() + getQueryFrom()  + getOrderBy(new LevelTaskSearchCondition());
        MapSqlParameterSource params = new MapSqlParameterSource();
        return getJdbcTemplate().query(sql, params, new LevelTaskMapper());
    }

    @Override
    public List<LevelTask> selectAllUnsent() {
        String sql = getQuerySelectTop3() + getQueryFrom() + " WHERE SEND_SIGN = 0 AND (M.TASK_STATE = 0 OR M.TASK_STATE = 2) " + getOrderBy(new LevelTaskSearchCondition());
        MapSqlParameterSource params = new MapSqlParameterSource();
        return getJdbcTemplate().query(sql, params, new LevelTaskMapper());
    }

    private String getPagingWhere(LevelTaskSearchCondition condition) {
        /*if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
            return " WHERE RN BETWEEN " + (condition.getOffset() + 1) + " AND "
                    + (condition.getOffset() + condition.getLimit());
        } else {
            return " ";
        }*/
         if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
            return " WHERE RNUM > " + condition.getOffset().toString() ;
        } else {
            return " ";
        }
    }

    @Override
    public int count(LevelTaskSearchCondition condition) {
        String sql = "SELECT COUNT(*) " + getQueryFrom() + getQueryWhere(condition);
        log.warn("LevelTaskDAOJdbcImpl::count  = " + sql);
        return getJdbcTemplate().queryForInt(sql, new MapSqlParameterSource());

    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public LevelTask insert(final LevelTask domain) {
        log.warn("LevelTaskDAOJdbcImpl::insert BEGIN ");
        Date now = new Date();
        domain.setEventTime(now);
        domain.setCreateDate(now);
        String sql = " INSERT INTO LEVEL_TASK("
                + "     TASK_TYPE_ID,"
                + "     TASK_STATE,"
                + "     DESCRIPTION,"
                + "     PRIORITY,"
                + "     EVENT_TIME,"
                + "     DEPART_ID,"
                //+ "     LL,"
                + "     MODULE,"
                + "     ID,"
                + "     USER_ID_FROM,"
                + "     SEND_SIGN,"
                + "     USER_ID_TO"
                + " )VALUES("
                + "     :taskId,"
                + "     :taskState,"
                + "     :description,"
                + "     :priority,"
                + "     :eventTime,"
                + "     :dd,"
                //+ "     :ll,"
                + "     :module,"
                + "     :currentId,"
                + "     :userIdFrom,"
                + "     :sendSign,"
                + "     :userIdTo)";
        log.warn("LevelTaskDAOJdbcImpl::insert TASK_STATE = " + domain.getTaskState().toString());
         log.warn("LevelTaskDAOJdbcImpl::insert sql = " + sql);

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(domain);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Integer rows = getJdbcTemplate().update(sql, namedParameters, keyHolder, new String[]{"LEVEL_TASK_ID"});
        if (!rows.equals(1)) {
            return null;
        }
        Integer id = keyHolder.getKey().intValue();
        domain.setId(keyHolder.getKey().intValue());
       
       // if (!insertTaskHist(domain))
        //     return null;

        return domain;
              
    }
    
   /* @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean insertTaskHist(final LevelTask domain)
    {
        String sql = " INSERT INTO TASK_HIST("
                + "     LEVEL_TASK_ID,"
                + "     TASK_TYPE_ID,"
                + "     TASK_STATE,"
                + "     CREATE_DATE,"
                + "     EVENT_TIME,"
                + "     DEPART_ID,"
                //+ "     LL,"
                + "     REPLY,"
                + "     REPLY_DATE,"
                + "     REPLY_NUMBER,"                
               // + "     MODULE,"
               // + "     ID,"
                + "     USER_ID_FROM,"                
                + "     SEND_SIGN,"
                + "     USER_ID_TO"
                + " )VALUES("
                + "     :id,"
                + "     :taskId,"
                + "     :taskState,"
                + "     :createDate,"
                + "     :eventTime,"
                + "     :dd,"
               // + "     :ll,"   
                + "     :reply,"   
                + "     :replyDate,"   
                + "     :replyNumber,"   
               // + "     :module,"
               // + "     :currentId,"
                + "     :userIdFrom,"
                + "     :sendSign,"
                + "     :userIdTo)";
        log.warn("LevelTaskDAOJdbcImpl::insert hist sql = " + sql);
        SqlParameterSource namedParametersHist = new BeanPropertySqlParameterSource(domain);
        KeyHolder keyHolderHist = new GeneratedKeyHolder();
        Integer rowsHist = getJdbcTemplate().update(sql, namedParametersHist, keyHolderHist, new String[]{"TASK_HIST_ID"});
        if (!rowsHist.equals(1)) {
            return false;
        }
        return true;
    }*/
  
    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean update(final LevelTask domain) {
        Date now = new Date();
        domain.setEventTime(now);
        String sql = "UPDATE LEVEL_TASK SET "
                + "     TASK_TYPE_ID            = :taskId, "
                + "     TASK_STATE              = :taskState, "
                + "     DESCRIPTION             = :description, "                
                + "     PRIORITY                = :priority, "
                + "     EVENT_TIME              = :eventTime, "
                + "     DEPART_ID                      = :dd, "
                //+ "     LL                      = :ll, "
                + "     REPLY                   = :reply, "                
                + "     MODULE                  = :module, "                
                + "     ID                      = :currentId, "                
                + "     USER_ID_FROM            = :userIdFrom, "                
                + "     USER_ID_TO              = :userIdTo, "
                + "     SEND_SIGN              = :sendSign "
                + " WHERE "
                + "     LEVEL_TASK_ID           = :id ";

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(domain);
        getJdbcTemplate().update(sql, namedParameters);
        //любое изменение в таблице LEVEL_TASK фиксируем новой записью в таблице с историей изменения TASK_HIST
        //if (!insertTaskHist(domain)) {
        //    return false;
        //}        
        return true;
    }


    @Override
    @Transactional(value="txArm" ,rollbackFor=Throwable.class)
    public boolean delete(long id) {
        String query = "DELETE LEVEL_TASK "
                + "WHERE LEVEL_TASK_ID = :id";
        
        HashMap<String, Long> params = new HashMap();
        params.put("id", id);
        log.warn("LevelTaskDAOJdbcImpl::delete hist query = " + query);
        getJdbcTemplate().update(query, params);
         
        //query = "DELETE TASK_HIST "
        //        + "WHERE LEVEL_TASK_ID = :id";                      
        //getJdbcTemplate().update(query, params);   
        return true;
    }
    
    @Override
    public Boolean isHasNewMsgIn(Integer currentUser) {
        //if (currentUser == null) {
         //   currentUser = userService.getCurrentUser();
        //}

        String sql = "SELECT M.LEVEL_TASK_ID AS M_LEVEL_TASK_ID FROM LEVEL_TASK M WHERE M.TASK_STATE = 0 AND M.USER_ID_TO = " + String.valueOf(currentUser);

        log.warn("LevelTaskDAOJdbcImpl::isHasNewMsgIn select  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();
        List<LevelTask> res = getJdbcTemplate().query(sql, params, new LevelTaskIdMapper());
        if (!res.isEmpty()) {
            return true;
        }
        return false;
    }
    
    @Override
    public Boolean isHasNewMsgOut(Integer currentUser) {
        //if (currentUser == null) {
         //   currentUser = userService.getCurrentUser();
        //}

        String sql = "SELECT M.LEVEL_TASK_ID AS M_LEVEL_TASK_ID FROM LEVEL_TASK M WHERE M.TASK_STATE = 2 AND M.USER_ID_FROM = " + String.valueOf(currentUser);

        log.warn("LevelTaskDAOJdbcImpl::isHasNewMsgOut select  = " + sql);
        MapSqlParameterSource params = new MapSqlParameterSource();
        List<LevelTask> res = getJdbcTemplate().query(sql, params, new LevelTaskIdMapper());
        if (!res.isEmpty()) {
            return true;
        }
        return false;
    }
     
    @Override
    public LevelTask selectRow(long id) 
    {
         log.warn("getLevelTaskForId");
        LevelTask domain = null;
        
        String querySub = getQuerySelect() + getQueryFrom() ;
        
        querySub += " AND M.LEVEL_TASK_ID = :id";
        HashMap<String, Long> params = new HashMap();
        params.put("id", Long.valueOf(id));
        log.warn(querySub);
        List<LevelTask> res = getJdbcTemplate().query(querySub, params, new LevelTaskMapper());
        if(!res.isEmpty()){
            domain = res.get(0);
        }
        return domain;
    }

    private static String field2column(String field) {
        if (mapFields2Column == null) {
            mapFields2Column = new HashMap<String, String>();
            mapFields2Column.put("id", "M.LEVEL_TASK_ID");           
            mapFields2Column.put("userIdFrom", "M.USER_ID_FROM");
            mapFields2Column.put("userIdTo", "M.USER_ID_TO");
            mapFields2Column.put("createDate", "M.CREATE_DATE");
            mapFields2Column.put("taskId", "M.TASK_TYPE_ID");
            mapFields2Column.put("taskName", "T.NAME");
            mapFields2Column.put("taskState", "M.TASK_STATE");
            mapFields2Column.put("stateName", "S.NAME");
            mapFields2Column.put("userNameTo", "VV.NAME");
            mapFields2Column.put("userNameFrom", "V.NAME");
            mapFields2Column.put("description", "M.DESCRIPTION");
            mapFields2Column.put("descriptionShort", "SUBSTRING(M.DESCRIPTION,0,120)");
            mapFields2Column.put("priority", "M.PRIORITY");
            mapFields2Column.put("dd", "M.DD");
            mapFields2Column.put("ll", "M.LL");
            mapFields2Column.put("reply", "M.REPLY");
            mapFields2Column.put("replyDate", "M.REPLY_DATE");
            mapFields2Column.put("replyNumber", "M.REPLY_NUMBER");
            mapFields2Column.put("eventTime", "M.EVENT_TIME");
            mapFields2Column.put("module", "M.MODULE");
            mapFields2Column.put("currentId", "M.ID");
            mapFields2Column.put("sendSign", "M.SEND_SIGN");
            mapFields2Column.put("begDate", "M.CREATE_DATE");
            mapFields2Column.put("endDate", "M.CREATE_DATE");
            
        }
        return mapFields2Column.get(field);

    }

 @Override
    public Boolean markOfficeDocContr(List<Integer> ids) {

        if (ids.isEmpty()) {
            return true;
        }

        String strForIn = "(";
        for (Integer id : ids) {
            strForIn += ((strForIn.equals("(")) ? "" : ",") + id.toString();
        }
        strForIn += ")";

        String query = "UPDATE OFFICE_DOC_CONTR SET "
                + "     DATE_FACT  = GETDATE()"
                + " WHERE "
                + "      OFFICE_DOC_CONTR_ID  IN " + strForIn;
        log.warn("DDataGridDAOJdbcImpl::markOfficeDocContr  query= " + query);
        Map<String, Object> params = new HashMap<String, Object>();
        getJdbcTemplate().update(query, params);
        return true;

    }

    @Override
    public Boolean sendAnswer(List<Integer> ids) {
        if (ids.isEmpty()) {
            return true;
        }

        String strForIn = "(";
        for (Integer id : ids) {
            strForIn += ((strForIn.equals("(")) ? "" : ",") + id.toString();
        }
        strForIn += ")";

        String query = "UPDATE LEVEL_TASK SET "
                + "     TASK_STATE  = 2, SEND_SIGN = 1"
                + " WHERE "
                + "      TASK_STATE <2 AND LEVEL_TASK_ID  IN " + strForIn;


        log.warn("DDataGridDAOJdbcImpl::sendAnswer  query1= " + query);
        Map<String, Object> params = new HashMap<String, Object>();
        getJdbcTemplate().update(query, params);

        //Если сообщение связано с документом "Приказ и распоряжение", то сразу ставим отметку о его выплонении
        query = "UPDATE OFFICE_DOC_CONTR SET "
                + "     DATE_FACT  = GETDATE()"
                + " WHERE "
                + "      OFFICE_DOC_CONTR_ID  IN ( SELECT ID FROM LEVEL_TASK WHERE MODULE LIKE 'OFFICE_DOC_CONTR_IG' AND LEVEL_TASK_ID  IN " + strForIn + ")";
        log.warn("DDataGridDAOJdbcImpl::sendAnswer  query2= " + query);
        params = new HashMap<String, Object>();
        getJdbcTemplate().update(query, params);
        return true;
    }
    
    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public Boolean sendCancelClose(List<Integer> ida, List<Integer> idc) {
        Boolean rc = true;
        if (!ida.isEmpty()) {
            rc = sendCC(ida,"4");
        }
        if (!idc.isEmpty()) {
            rc = sendCC(idc,"3");
        }
        return rc;
    }
    
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    private Boolean sendCC(List<Integer> idc, String state) {
        {
            String strForIn = "(";
            for (Integer id : idc) {
                strForIn += ((strForIn.equals("(")) ? "" : ",") + id.toString();
            }
            strForIn += ")";

            String query = "UPDATE LEVEL_TASK SET "
                    + "     TASK_STATE  = " + state + ", SEND_SIGN = 1"
                    + " WHERE "
                    + "      LEVEL_TASK_ID  IN " + strForIn;


            log.warn("DDataGridDAOJdbcImpl::sendAnswer  query1= " + query);
            Map<String, Object> params = new HashMap<String, Object>();
            getJdbcTemplate().update(query, params);
           
            return true;
        }
    }

    public Integer getDocFileId(Integer id) {
        String sql = "SELECT TOP 1 DOC_FILE.FILE_ID AS FILE_ID"
                + " FROM DOC_FILE, LEVEL_TASK,OFFICE_DOC_CONTR"
                + " WHERE LEVEL_TASK.LEVEL_TASK_ID = " + id.toString()
                + " AND LEVEL_TASK.ID = OFFICE_DOC_CONTR.OFFICE_DOC_CONTR_ID"
                + " AND OFFICE_DOC_CONTR.OFFICE_DOC_ID = DOC_FILE.DOC_ID"
                + " ORDER BY DOC_FILE.FILE_ID";
        log.warn("LevelTaskDAOJdbcImpl::getDocFileId select  = " + sql);
        
         List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, new MapSqlParameterSource());
        if (isNotEmpty(list)) {
            return (Integer) list.get(0).get("FILE_ID");
        }
        return -1;
    }
}

