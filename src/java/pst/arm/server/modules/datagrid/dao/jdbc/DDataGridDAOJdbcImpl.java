package pst.arm.server.modules.datagrid.dao.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.User;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.common.DTableMapManager;
import pst.arm.server.common.service.StaticContextHolder;
import pst.arm.server.common.service.UserService;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;
import pst.arm.server.modules.datagrid.dao.jdbc.mappers.DDataGridMapper;
import pst.arm.server.modules.datagrid.dao.jdbc.mappers.DDataGridMapperSumm;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
@Repository
public class DDataGridDAOJdbcImpl implements DDataGridDAO {

    private static Logger log = Logger.getLogger("DDataGridDAOJdbcImpl");
    private NamedParameterJdbcTemplate jdbcTemplate;
    public static final String DATETIME_FORMAT_DEFAULT_SQL = "DD.MM.YY HH24:MI";
    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy HH:mm";
    public static final String DATE_FORMAT_DEFAULT_SQL = "DD.MM.YY";
    public static final String DATE_FORMAT_DEFAULT = "dd.MM.yyyy 12:00";
    public static final String DATE_SEARCH_FORMAT_DEFAULT = "dd.MM.yyyy";
    private StaticContextHolder context;

    @Autowired
    public void setContext(StaticContextHolder context) {
        //  logger.warn("GWTServiceSimpleImpl::setTestService");
        this.context = context;
    }

    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /* public void changeDataSource(DTable table) {
     //DTable table = DTableMapManager.getInstance().getTable(tableName);
     log.warn("DDataGridDAOJdbcImpl::changeDataSource  = " + table.getDataSourceName());
     ComboPooledDataSource ds = (ComboPooledDataSource) context.getBean(table.getDataSourceName());
     this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
     }*/
    public NamedParameterJdbcTemplate getJdbcTemplate(DTable table) {
        log.warn("DDataGridDAOJdbcImpl::getJdbcTemplate  = " + table.getDataSourceName());
        ComboPooledDataSource ds = (ComboPooledDataSource) context.getBean(table.getDataSourceName());
        return new NamedParameterJdbcTemplate(ds);
    }

    private String getQuerySelect(DTable table) {
        String strSelect = "";
        String strBeforSelect = "";

        if (table.getStrBeforSelect() != null) {
            strBeforSelect = table.getStrBeforSelect();
        }
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                //IColumnBuilder col = (IColumnBuilder)colEntry.getValue();
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                //String colName = key.getTableAlias() + "." + key.getColumnName() + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                String colName = key.getTableAlias() + "." + builder.getColumn(key).getName() + " AS " + key.getTableAlias() + "_" + key.getColumnName();

                DColumn col = (DColumn) colEntry.getValue();
                String sqlForCol = col.getSqlForColumn();

                if (sqlForCol != null && !sqlForCol.isEmpty()) {
                    //colName = sqlForCol + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                    colName = sqlForCol + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                }

                String distinct = table.getIsDistinct() ? " DISTINCT" : "";

                strSelect += (strSelect.isEmpty()) ? " SELECT " + distinct + strBeforSelect + " " + colName : ", " + colName;
            }
        }

        return strSelect;
    }

    private String getQuerySelectSumm(DTable table) {
        String strSelectSumm = "";
        String strBeforSelect = "";

        if (table.getStrBeforSelect() != null) {
            strBeforSelect = table.getStrBeforSelect();
        }
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                DColumn col = (DColumn) colEntry.getValue();

//                Смотрим на свойство isSumm в DColumn, если оно TRUE, то выбираем поле для суммирования  
                if (col.getIsSumm()) {
                    String colName = " SUM(" + key.getTableAlias() + "." + builder.getColumn(key).getName() + ")" + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                    String distinct = table.getIsDistinct() ? " DISTINCT" : "";
                    strSelectSumm += (strSelectSumm.isEmpty()) ? " SELECT " + distinct + strBeforSelect + " " + colName : ", " + colName;
                }
            }
        }
        return strSelectSumm;
    }

    private String getQuerySelectTop(DTable table, DataGridSearchCondition condition) {
        String strSelect = "";
        String strBeforSelect = "";
        if (table.getStrBeforSelect() != null) {
            strBeforSelect = table.getStrBeforSelect();
        }
        Integer top = condition.getOffset() + condition.getLimit();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                //IColumnBuilder col = (IColumnBuilder)colEntry.getValue();
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                //String colName = key.getTableAlias() + "." + key.getColumnName() + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                String colName = key.getTableAlias() + "." + builder.getColumn(key).getName() + " AS " + key.getTableAlias() + "_" + key.getColumnName();

                DColumn col = (DColumn) colEntry.getValue();
                String sqlForCol = col.getSqlForColumn();

                if (sqlForCol != null && !sqlForCol.isEmpty()) {
                    //colName = sqlForCol + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                    colName = sqlForCol + " AS " + key.getTableAlias() + "_" + key.getColumnName();
                }
                String distinct = table.getIsDistinct() ? " DISTINCT" : "";
                strSelect += (strSelect.isEmpty()) ? " SELECT " + distinct + strBeforSelect + " TOP " + String.valueOf(top) + " " + colName : ", " + colName;
            }
        }

        if (table.getDenseRNUM() != null) {
            String strDense = getOrderBy(table, condition, true);
            if (strDense.equals(" ORDER BY ( SELECT 1)")) {
                strDense = " ORDER BY";
            } else {
                strDense += ",";
            }

            strSelect += " , DENSE_RANK() OVER ( " + strDense + " " + table.getDenseRNUM() + ") AS RNUM ";
        } else {
            strSelect += " , ROW_NUMBER() OVER ( " + getOrderBy(table, condition, true) + ") AS RNUM ";
        }

        return strSelect;
    }

    private String getQueryFrom(DTable table, DataGridSearchCondition condition) {
        String strFrom = "";
        String tables = "";

        for (Map.Entry tableEntry : table.getAddTables().entrySet()) {
            String tName = (String) tableEntry.getValue();
            String tAlias = (String) tableEntry.getKey();
            tables += tName + " " + tAlias + ", ";
        }

        String tableName = table.getTables().get("MAIN");
        String tableAlias = "MAIN";
        tables += tableName + " " + tableAlias;

        strFrom += (strFrom.isEmpty()) ? " FROM " + tables : ", " + tables;

        strFrom += getQueryJoin(table, condition);
        return strFrom;
    }

    private String getOrderBy(DTable table, DataGridSearchCondition condition, Boolean isOver) {
        String strWhere = "";
        if (condition != null
                && condition.getSortFieldId() != null
                && !condition.getSortFieldId().isEmpty()
                && !condition.getSortDir().equals(DataGridSearchCondition.SortDir.NONE)) {

            strWhere = " ORDER BY "
                    + condition.getSortFieldId().replace(":", ".")
                    + " "
                    + condition.getSortDir().getDir();

            // Данное if-условие в будущем можно будет убрать, т.е. делать эти действия всегда.
            // Ибо они лишь добавляют правильную обработку при сортировке полей, которые представляют собой значения функций
            // (а такие значения нам будут в будущем попадаться, коль скоро мы всё чаще используем вычисляемые через функции бд значения)

            if (table.getQueryName().equals("SPR_DEPART_PRIBOY_REF_IG") || table.getQueryName().equals("SPR_DEPART_MART_REF_IG")) {

                String sqlForColumn = null;

                for (IColumnBuilder builder : table.getColumnBuilders()) {
                    for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        String colName = builder.getColumn(key).getName();

                        // Подстрока строки ИМЯ_ТАБЛИЦЫ:ИМЯ_КОЛОНКИ , содержащая ИМЯ_КОЛОНКИ
                        String sub = condition.getSortFieldId().substring(condition.getSortFieldId().lastIndexOf(":") + 1);

                        if (sub.equals(colName)) {
                            sqlForColumn = table.getColumnBuilder(key).getColumn(key).getSqlForColumn();
                            break; // нашли такую колонку
                        }
                    }
                }

                // Если данное условие ненулевое, то сортируем по sql значению данной колонки
                if (sqlForColumn != null) {
                    strWhere = " ORDER BY "
                            + sqlForColumn
                            + " "
                            + condition.getSortDir().getDir();
                }
            }

            log.warn("-----------------------------------------");
            log.warn("DDataGridDAOJdbcImpl::getOrderBy  condition.getSortDir().getSortFieldId() = " + condition.getSortFieldId());

        } else if (table.getStrOrderByStart() != null
                && !table.getStrOrderByStart().isEmpty()) {
            strWhere = " ORDER BY "
                    + table.getStrOrderByStart();
        } else {

            strWhere = " ORDER BY " + (isOver ? "( SELECT 1)" : "1");
        }
        return strWhere;
    }

    public String getQueryWhere(DTable table, DataGridSearchCondition condition) {
        String strWhere = "";
        String strwheretable = "";
        //strWhere = getQueryWhereJoin(table);
        boolean isWhere = strWhere.contains("WHERE");

        if (table.getStrWhere() != null && table.getStrWhere().length() > 0 && table.getStrWhere().contains("%CURRENTUSERID%")) {
            try {
                UserService userService = pst.arm.server.common.ConfigurationManager.getInstance().getUserService();
                User user = userService.getCurrentUser(true);
                String userIdstr = user.getId().toString();
                strwheretable = table.getStrWhere().replace("%CURRENTUSERID%", userIdstr);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        } else if (table.getStrWhere() != null && table.getStrWhere().length() > 0) {
            strwheretable = table.getStrWhere();
        }

        if (table.getStrWhere() != null && !table.getStrWhere().isEmpty()) {
            strWhere += ((!isWhere) ? " WHERE " : " AND ") + strwheretable;
        }
        isWhere = strWhere.contains("WHERE");
        strWhere += getQueryWhereSearch(table, condition, isWhere);
        isWhere = strWhere.contains("WHERE");
        strWhere += getQueryWhereFiltr(table, condition, isWhere);
        return strWhere;
    }

    @Override
    public void execProcedure(String sql) {
        jdbcTemplate.update(sql, new HashMap<String, Object>());
    }

    @Override
    public void execProcedure(String sql, String dbName) {
        ComboPooledDataSource ds = (ComboPooledDataSource) StaticContextHolder.getBean(dbName);
        String dbActualName = ds.getJdbcUrl().substring(ds.getJdbcUrl().indexOf("databaseName=") + 13);
        String newSql = sql.replace("{db}", dbActualName);
        jdbcTemplate.update(newSql, new HashMap<String, Object>());
    }

    private String getQueryJoin(DTable table, DataGridSearchCondition condition) {
        String strJoin = "";
        for (DTableJoin join : table.getTableJoins()) {

            String subJoin = "";
            String tableOne = join.getTableOne();
            String tableTwo = join.getTableTwo();

            String tableName = table.getTables().get(tableTwo);
            ArrayList< DColumnJoin> columns = join.getColumnJoins();
            ERelationType relationType = join.getRelationType();

            String strJoinHeader = "";

            switch (relationType) {
                case INNER: {
                    strJoinHeader += " INNER JOIN ";
                    break;
                }
                case LEFT: {
                    strJoinHeader += " LEFT JOIN ";
                    break;
                }
                case RIGHT: {
                    strJoinHeader += " RIGHT JOIN ";
                    break;
                }
            }

            strJoinHeader += tableName + " AS " + tableTwo + " ON ";

            for (DColumnJoin col : columns) {
                String colOne;
                String colTwo;
                if (col instanceof DColumnJoinWithValue) {
                    if (((DColumnJoinWithValue) col).isFirstIsValue()) {
                        colOne = col.getColumnNameTableOne();
                        colTwo = tableTwo + "." + col.getColumnNameTableTwo();
                    } else {
                        colOne = tableOne + "." + col.getColumnNameTableOne();
                        colTwo = col.getColumnNameTableTwo();
                    }
                } else {
                    colOne = tableOne + "." + col.getColumnNameTableOne();
                    colTwo = tableTwo + "." + col.getColumnNameTableTwo();
                }

                String statement;
                if (StringUtils.isNotEmpty(col.getOn())) {
                    statement = col.getOn();
                    if (condition != null
                            && condition.getSearches().get(new SKeyForColumn("DUMMY:INFO")) != null) {
                        statement = " MAIN.ID = info.id_elaboration_of_dd and info.id = (select top 1 id from DT_ELABORATION_OF_DD where id_elaboration_of_dd = main.id and info like ('%"
                                + condition.getSearches().get(new SKeyForColumn("DUMMY:INFO")).getVal() + "%'))";
                    }
                } else {
                    statement = colOne + "=" + colTwo;
                }
                String colRelationType = " " + col.getColumnRelationType().getRelationType() + " ";

                subJoin += (subJoin.isEmpty()) ? strJoinHeader + statement : colRelationType + statement;
            }

            strJoin += subJoin;
        }
        return strJoin;
    }

    private String getQueryWhereSearch(DTable table, DataGridSearchCondition condition, Boolean isWhere) {
        log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch START");
        String strWhere = "";
        if (condition.getSearches().isEmpty()) {
            for (IColumnBuilder builder : table.getColumnBuilders()) {
                for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                    if (table.getColumnBuilder(key).getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()) {
                        String where = key.getTableAlias() + "." + table.getColumnBuilder(key).getColumn(key).getName() + " is NULL";
                        strWhere += (strWhere.isEmpty() && !isWhere) ? " WHERE " + where : " AND " + where;
                    }
                }
            }
        } else {
            for (Map.Entry colEntry : condition.getSearches().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = (IRowColumnVal) colEntry.getValue();

                log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch key = " + key.getColumnName());

                IColumnBuilder builder = table.getColumnBuilder(key);
                if (!builder.getNotSearch()) {
                    String valForSql = "";

                    // Если для поля указан признак isNullWhenEmptySearch, то добавляем в запрос where поле = NULL
                    if (table.getColumnBuilder(key).getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()
                            && (val.getVal() == null || val.getVal().toString().trim().isEmpty())) {
                        valForSql = "NULL";
                        DColumn column = table.getColumnBuilder(key).getColumn(key);
                        Boolean columnAlreadySearched = false;

                        for (Map.Entry colEntry2 : condition.getSearches().entrySet()) {
                            SKeyForColumn key2 = (SKeyForColumn) colEntry2.getKey();

                            if (key != key2 && table.getColumnBuilder(key2).getColumn(key2).getName().equals(column.getName())) {
                                columnAlreadySearched = true;
                                break;
                            }
                        }

                        if (!columnAlreadySearched) {
                            String where = key.getTableAlias() + "." + table.getColumnBuilder(key).getColumn(key).getName() + " is NULL";
                            //String where = builder.getSqlWhereSearch(key, valForSql);

                            strWhere += (strWhere.isEmpty() && !isWhere) ? " WHERE " + where : " AND " + where;
                        }
                        continue;
                    }

                    if (val.getVal() == null || val.getVal().toString().trim().isEmpty()) {
                        continue; //даннх нет и нечего их искать
                    }
                    log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch  val type=  " + val.getVal().getClass().getName());
                    log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch  val =  " + val.getVal());
                    String where = "";

                    if (table.getColumnBuilder(key).getColumn(key).getColumnProperty().getType() == EColumnType.DATE) {
                        Date d = (Date) val.getVal();
                        SimpleDateFormat sdf;
                        sdf = new SimpleDateFormat(builder.getColumn(key).getColumnProperty().getFormat());
                        valForSql = sdf.format(d);
                    } else {
                        valForSql = String.valueOf(val.getVal());
                    }

                    if (table.getQueryName().equals("ORDER_D_VO_DS")) {
                        if (key.equals(new SKeyForColumn("NOTIF_OPENING:IS_ACTUAL")) && valForSql.equals("0")) {
                            where = " ( MAIN.NOTIF_OPENING_ID is NULL OR " + builder.getSqlWhereSearch(key, valForSql) + " ) ";
                        } else if (key.equals(new SKeyForColumn("MAIN:NOTIF_CLOSING_ID")) && (valForSql.equals("0")) || valForSql.equals("-1")) {
                            where = " ( MAIN.NOTIF_CLOSING_ID is NULL ) ";
                        } else if (key.equals(new SKeyForColumn("MAIN:NOTIF_CLOSING_ID")) && valForSql.equals("1")) {
                            where = " ( MAIN.NOTIF_CLOSING_ID is NOT NULL ) ";
                        } else {
                            where = builder.getSqlWhereSearch(key, valForSql);
                        }
                    } else {
                        if (key.equals(new SKeyForColumn("MAIN:FILE_FILTER"))) {
                            if ((Integer) val.getVal() == 1) {
                                where = builder.getSqlWhereSearch(key, valForSql);
                            } else {
                                if (condition.getSearches().size() == 1) {
                                    where = " MAIN.DATE_CANCELLED IS NULL ";
                                } else {
                                    where = " 1=1 ";
                                }
                            }
                        } else {
                            where = builder.getSqlWhereSearch(key, valForSql);
                        }
                    }

                    if (key.equals(new SKeyForColumn("INFO:DUMMY"))) {
                        where = " INFO.INFO is not null ";
                    }

                    /* String where = " ";          
                     if (table.getColumnBuilder(key).getColumn(key).getColumnProperty().getType() == EColumnType.DATE) {
                     Date d = (Date) val.getVal();
                     SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT);
                     String dateVal = sdf.format(d);
                     //where += " to_date(" + key.getTableAlias() + "." + key.getColumnName() + ", '" + DATETIME_FORMAT_DEFAULT_SQL + "') = to_date('" + dateVal + "', '" + DATETIME_FORMAT_DEFAULT_SQL + "') ";
                     where += " to_date(" + key.getTableAlias() + "." + builder.getColumn(key).getName() + ", '" + DATETIME_FORMAT_DEFAULT_SQL + "') = to_date('" + dateVal + "', '" + DATETIME_FORMAT_DEFAULT_SQL + "') ";                
                     } else {
                     //where = " UPPER(" + key.getTableAlias() + "." + key.getColumnName() + ") LIKE UPPER('%" + String.valueOf(val.getVal()) + "%') ";
                     where = " UPPER(" + key.getTableAlias() + "." + builder.getColumn(key).getName() + ") LIKE UPPER('%" + String.valueOf(val.getVal()) + "%') ";
                     }*/
                    strWhere += (strWhere.isEmpty() && !isWhere) ? " WHERE " + where : " AND " + where;
                }
            }
        }
        log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch strWhere = " + strWhere);
        log.warn("DDataGridDAOJdbcImpl::getQueryWhereSearch END");
        return strWhere;
    }

    private Date addHoursToDate(int hours, Date old) {
        final long ONE_HOUR_IN_MS = 60000 * 60;
        long oldInMs = old.getTime();
        Date after = new Date(oldInMs + (hours * ONE_HOUR_IN_MS));
        return after;
    }

    private String getQueryWhereFiltr(DTable table, DataGridSearchCondition condition, Boolean isWhere) {
        String strWhere = "";
        for (Map.Entry colEntry : condition.getFilters().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            IRowColumnVal val = (IRowColumnVal) colEntry.getValue();
            IColumnBuilder builder = table.getColumnBuilder(key);
            String where = " ";
            log.warn("DDataGridDAOJdbcImpl::getQueryWhereFiltr  1 getColumnName = " + builder.getColumn(key).getName());
            if (val.getVal() != null) {
                log.warn("DDataGridDAOJdbcImpl::getQueryWhereFiltr  val type=  " + val.getVal().getClass().getName());
                log.warn("DDataGridDAOJdbcImpl::getQueryWhereFiltr  val =  " + val.getVal());
            }
            String valForSql = null;
            if (val.getVal() != null) {
                if (table.getColumnBuilder(key).getColumn(key).getColumnProperty().getType() == EColumnType.DATE) {
                    Date d = (Date) val.getVal();
                    SimpleDateFormat sdf;
                    sdf = new SimpleDateFormat(builder.getColumn(key).getColumnProperty().getFormat());
                    valForSql = sdf.format(d);
                } else {
                    valForSql = String.valueOf(val.getVal());
                }
            }
            if (table.getQueryName().equals("DEPART_STRUCTURE_N") && key.equals(new SKeyForColumn("MAIN:DEPART_ID"))) {
                where += key.getTableAlias() + "." + key.getColumnName() + " != " + valForSql;
            } else if (table.getQueryName().equals("WORK_PLAN_EXECUTOR_LIST") && key.equals(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"))) {
                if (valForSql != null) {
                    where += key.getTableAlias() + "." + key.getColumnName() + " NOT IN (SELECT * FROM dbo.GET_WORK_PLAN_EXECUTORS_IDS(" + valForSql + "))";
                } else {
                    where += key.getTableAlias() + "." + key.getColumnName() + " IS NOT NULL";
                }
            } else if (table.getQueryName().equals("WORK_PLAN_EXECUTOR_NEW_LIST") && key.equals(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"))) {
                if (valForSql != null) {
                    where += key.getTableAlias() + "." + key.getColumnName() + " NOT IN (SELECT * FROM dbo.GET_WORK_PLAN_EXECUTORS_IDS_FULL(" + valForSql + ", 1))";
                } else {
                    where += key.getTableAlias() + "." + key.getColumnName() + " IS NOT NULL";
                }            
            } else if (table.getQueryName().equals("WORK_PLAN_EXECUTOR_RES_LIST") && key.equals(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"))) {
                if (valForSql != null) {
                    where += key.getTableAlias() + "." + key.getColumnName() + " NOT IN (SELECT * FROM dbo.GET_WORK_PLAN_EXECUTORS_IDS_FULL(" + valForSql + ", 2))";
                } else {
                    where += key.getTableAlias() + "." + key.getColumnName() + " IS NOT NULL";
                }            
            } else {
                where += builder.getStirngForFiltr(key, valForSql);//" = '" + String.valueOf(val.getVal()) + "' ";
            }


            strWhere += (strWhere.isEmpty() && !isWhere) ? " WHERE " + where : " AND " + where;
        }
        return strWhere;
    }

    @Override
    public List<DDataGrid> select(String tableName) {
        log.warn("DDataGridDAOJdbcImpl::select(" + tableName + ")  BEGIN ");
        DTable table = DTableMapManager.getInstance().getTable(tableName);


        String strwheretable = "";
        if (table.getStrWhere() != null && table.getStrWhere().length() > 0 && table.getStrWhere().contains("%CURRENTUSERID%")) {
            try {
                UserService userService = pst.arm.server.common.ConfigurationManager.getInstance().getUserService();
                User user = userService.getCurrentUser(true);
                String userIdstr = user.getId().toString();
                strwheretable = table.getStrWhere().replace("%CURRENTUSERID%", userIdstr);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        } else if (table.getStrWhere() != null && table.getStrWhere().length() > 0) {
            strwheretable = table.getStrWhere();
        }

        String query = getQuerySelect(table) + getQueryFrom(table, null) + (table.getStrWhere() != null ? " WHERE " + strwheretable : "") + getGroupBy(table);

        HashMap<String, Object> params = new HashMap();
        DDataGridMapper mapper = new DDataGridMapper();
        mapper.setTable(table);
        //try {            
        //changeDataSource(table);
        log.warn("DDataGridDAOJdbcImpl::select  = " + query);
        List<DDataGrid> domain = getJdbcTemplate(table).query(query.toString(), params, mapper);
        log.warn("DDataGridDAOJdbcImpl::select(" + tableName + ")  END ");
        return domain;
        //} catch (Exception e) {
        //    log.warn(e.getMessage());
        //}
        //log.warn("DDataGridDAOJdbcImpl::select("+tableName+")  END ");
        //return null;
    }

    private void checkParams(DataGridSearchCondition c, DTable t) {
        if (t.getFunctionParams().size() > 0) {
            SKeyForColumn key = t.getFunctionParams().get(0);
            if (c.getFilters().containsKey(key)) {
                Integer val = (Integer) c.getFilters().get(key).getVal();
                String s = t.getTables().get("MAIN");
                if (s.indexOf("(") > 0) {
                    s = s.substring(0, s.indexOf("("));
                }
                s += "(" + val + ")";

                t.getTables().remove("MAIN");

                t.getTables().put("MAIN", s);

                c.getFilters().remove(key);
            }
        }
    }

    private void checkParamsFromSearch(DataGridSearchCondition c, DTable t) {
        if (t.getFunctionParamsFromSearch().size() == 0) {
            return;
        }
        if (t.getFunctionParamsFromSearch().size() == 0) {
            return;
        }
        String s = t.getTables().get("MAIN");
        if (s.indexOf("(") > 0) {
            s = s.substring(0, s.indexOf("("));
        }
        s += "(";
        int n = 0;
        for (SKeyForColumn key : t.getFunctionParamsFromSearch()) {
            String strVal = "";
            if (!c.getSearches().containsKey(key)) {
                strVal = "NULL";
            } else {
                IColumnBuilder b = t.getColumnBuilder(key);
                IRowColumnVal val = c.getSearches().get(key);
                if (val.getVal() == null) {
                    strVal = "NULL";
                } else {
                    if (t.getColumnBuilder(key).getColumn(key).getColumnProperty().getType() == EColumnType.DATE) {
                        Date d = (Date) val.getVal();
                        SimpleDateFormat sdf;
                        sdf = new SimpleDateFormat(b.getColumn(key).getColumnProperty().getFormat());
                        strVal = sdf.format(d);
                    } else {
                        strVal = String.valueOf(val.getVal());
                    }
                    strVal = "'" + strVal + "'";
                }
                //c.getSearches().remove(key);
            }
            s += ((n == 0) ? "" : ",") + strVal;
            n++;
        }
        s += ")";
        t.getTables().remove("MAIN");
        t.getTables().put("MAIN", s);
    }

    @Override
    public List<DDataGrid> select(String tableName, DataGridSearchCondition condition, Boolean isPaging) {
        log.warn("DDataGridDAOJdbcImpl::select(" + tableName + ", condition)  BEGIN ");
        DTable table = DTableMapManager.getInstance().getTable(tableName);
        Boolean tableNameChanged = false;
        String mainTable = table.getTables().get("MAIN");

        if (table.getQueryName().equals("DH_ELABORATION_OF_DD_HLV") && condition.getSortFieldId().equals("MAIN:INFO")) {
            condition.setSortFieldId(null);
        }

        if (table.getFunctionParams() != null) {
            checkParams(condition, table);
        }

        if (table.getFunctionParamsFromSearch() != null) {
            checkParamsFromSearch(condition, table);
        }

        if (condition.getFunctionParams().size() > 0) {
            tableNameChanged = true;
            putFunctionParams(condition, table);
        }

        String querySelect = (isPaging ? getQuerySelectTop(table, condition) : getQuerySelect(table));

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String par = "NULL";
        if (table.getQueryName().equals("ORDER_BLANK_WORK3") || table.getQueryName().equals("ORDER_BLANK_WORK4")) {
            if (!condition.getSearches().isEmpty()) {
                if (condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")) != null) {
                    if (condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")).getVal() != null) {
                        par = "'" + format.format((Date) condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")).getVal()) + "'";
                    }
                }
            }
            querySelect = querySelect.replace("%dateFrom%", par);
        }

        String querySub = querySelect + getQueryFrom(table, condition) + getQueryWhere(table, condition) + getGroupBy(table) + getOrderBy(table, condition, false);
        String query = "";

        if (isPaging) {

            query = "SELECT * FROM ( "
                    + querySub
                    + ") A1 " + getPagingWhere(condition);
        } else {
            query = querySub;
        }
        query = getQueryWhithParams(query, condition);
        HashMap<String, Object> params = new HashMap();
        DDataGridMapper mapper = new DDataGridMapper();
        mapper.setTable(table);
        //changeDataSource(table);
        log.warn("DDataGridDAOJdbcImpl::select  = " + query);
        List<DDataGrid> domain = getJdbcTemplate(table).query(query.toString(), params, mapper);
        log.warn("DDataGridDAOJdbcImpl::select(" + tableName + ", condition)  END ");
        if (tableNameChanged) {
            table.getTables().remove("MAIN");
            table.getTables().put("MAIN", mainTable);
        }
        return domain;

    }

    private void putFunctionParams(DataGridSearchCondition condition, DTable table) {
        String func = table.getTables().get("MAIN");
        String[] params = func.split(",");
        String finalFunc = "";
        for (int i = 0; i < params.length; i++) {
            if (condition.getFunctionParams().containsKey(i) & condition.getFunctionParams().get(i).getVal() != null) {
                String val;
                if (condition.getFunctionParams().get(i).getVal() instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
                    val = sdf.format(condition.getFunctionParams().get(i).getVal());
                } else {
                    val = (String) condition.getFunctionParams().get(i).getVal();
                }

                finalFunc += params[i].replace("NULL", "'" + val + "'") + ",";
            } else {
                finalFunc += params[i] + ",";
            }
        }

        finalFunc = finalFunc.substring(0, finalFunc.length() - 1);

        table.getTables().remove("MAIN");
        table.getTables().put("MAIN", finalFunc);
    }

    @Override
    public DDataGrid selectSumm(String tableName, DataGridSearchCondition condition) {
        log.warn("DDataGridDAOJdbcImpl::selectSumm(" + tableName + ", condition)  BEGIN ");
        DTable table = DTableMapManager.getInstance().getTable(tableName);
        String querySumm = getQuerySelectSumm(table) + getQueryFrom(table, condition) + getQueryWhere(table, condition);
        HashMap<String, Object> params = new HashMap();
        DDataGridMapperSumm mapperSumm = new DDataGridMapperSumm();
        mapperSumm.setTable(table);
        //changeDataSource(table);
        log.warn("DDataGridDAOJdbcImpl::select  = " + querySumm);
        List<DDataGrid> domain = getJdbcTemplate(table).query(querySumm.toString(), params, mapperSumm);
        log.warn("DDataGridDAOJdbcImpl::selectSumm(" + tableName + ", condition)  END ");
        return domain.get(0);
    }

    private String getGroupBy(DTable table) {
        if (table.isGroupByAll()) {
            String groupByStr = "  GROUP BY  ";
            for (IColumnBuilder b : table.getColumnBuilders()) {
                for (Map.Entry e : b.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) e.getKey();
                    if (!b.getColumn(key).getName().isEmpty()) {
                        groupByStr += key.getTableAlias() + "." + b.getColumn(key).getName() + ", ";
                    }
                }
            }
            groupByStr = groupByStr.substring(1, groupByStr.length() - 2);
            return groupByStr;
            //return "";
        } else {
            return "";
        }
    }

    @Override
    public int count(String tableName, DataGridSearchCondition condition) {
        DTable table = DTableMapManager.getInstance().getTable(tableName);

        if (table.getFunctionParamsFromSearch() != null) {
            checkParamsFromSearch(condition, table);
        }


        String querySelect = getQuerySelect(table);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String par = "NULL";
        if (table.getQueryName().equals("ORDER_BLANK_WORK3") || table.getQueryName().equals("ORDER_BLANK_WORK4")) {
            if (!condition.getSearches().isEmpty()) {
                if (condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")) != null) {
                    if (condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")).getVal() != null) {
                        par = "'" + format.format((Date) condition.getSearches().get(new SKeyForColumn("DEPART_EXECUTOR_FACT:END_DATE_FROM")).getVal()) + "'";
                    }
                }
            }
            querySelect = querySelect.replace("%dateFrom%", par);
        }

        String query = "SELECT COUNT(*) FROM (" + querySelect + getQueryFrom(table, condition) + getQueryWhere(table, condition) + getGroupBy(table)
                //+ getQueryFrom(table) + getQueryWhere(table, condition)
                + ") AS T";
        query = getQueryWhithParams(query, condition);
        //changeDataSource(table);
        log.warn("DDataGridDAOJdbcImpl::count  = " + query);
        return getJdbcTemplate(table).queryForInt(query, new HashMap());
    }

    //TODO надо избавляться от этой функции
    public DDataGrid selectRow(String tableName, long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DDataGrid selectRow(String tableName, DDataGrid domain) {
        log.warn("DDataGridDAOJdbcImpl::selectRow(" + tableName + ", domain)  BEGIN ");
        DDataGrid d = null;
        DTable table = DTableMapManager.getInstance().getTable(tableName);


        String where = "";//getQueryWhereJoin(table);
        String query = getQuerySelect(table) + getQueryFrom(table, null);

        HashMap<String, Object> params = new HashMap();
        DDataGridMapper mapper = new DDataGridMapper();

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                String colName = builder.getColumn(key).getName();
                if (!key.getTableAlias().equals(DTable.MAIN_TABLE)) {
                    continue;//поле принадлежит не главной таблице
                }
                if (builder.getColumn(key).getIsKey()) { //поле  ключевое   
                    where += (where.isEmpty()) ? " WHERE " : " AND ";
                    where += key.getTableAlias() + "." + colName + " = :W_" + colName;
                    log.warn("DDataGrid::selectRow val_id = " + ((domain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(domain.getRows().get(key).getVal())));
                    params.put("W_" + colName, domain.getRows().get(key).getVal());
                    log.warn("DDataGrid::selectRow val_id = " + ((domain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(domain.getRows().get(key).getVal())));
                }
            }
        }

        query += where;
        mapper.setTable(table);
        //changeDataSource(table);
        log.warn("DDataGridDAOJdbcImpl::selectRow  query = " + query);
        List<DDataGrid> res = getJdbcTemplate(table).query(query.toString(), params, mapper);
        log.warn("DDataGridDAOJdbcImpl::selectRow(" + tableName + ", domain)  END ");
        if (!res.isEmpty()) {
            d = res.get(0);
        }
        return d;
    }

    private String getPagingWhere(DataGridSearchCondition condition) {
        /* if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
         return " LIMIT " + ( condition.getOffset() + 1 ) + ", "
         + (condition.getOffset() + condition.getLimit());
         } else {
         return " ";
         }*/
        /*if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
         return " WHERE RN BETWEEN " + (condition.getOffset() + 1) + " AND "
         + (condition.getOffset() + condition.getLimit());
         } else {
         return " ";
         }*/
        if (condition != null && condition.getLimit() != null && condition.getOffset() != null) {
            return " WHERE RNUM > " + condition.getOffset().toString();
        } else {
            return " ";
        }
    }

    @Override
    @Transactional("txArm")
    public DDataGrid insert(String tableName, DDataGrid domain) {
        DTable table = DTableMapManager.getInstance().getTable(tableName);

        String query = "";
        String values = "";
        SKeyForColumn autoincrementFieldKey = null;
        String dbTableName = table.getName(); //имя главной таблицы
        HashMap<String, Object> params = new HashMap();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                String colName = builder.getColumn(key).getName();
                if (!key.getTableAlias().equals(DTable.MAIN_TABLE)) {
                    continue;
                }
                if (!builder.getColumn(key).getIsInserted() && builder.getColumn(key).getIsEditableAdd()) {
                    if (builder.getColumn(key).getIsAutoincrement())//запомним его имя
                    {
                        autoincrementFieldKey = key;
                    }
                    continue; //поле было не редактируемым и нечего его править в БД
                }
                if (!builder.getColumn(key).getIsAutoincrement()) //поле не автоинкрементное
                {
                    query += (query.isEmpty()) ? "INSERT INTO " + dbTableName + "(" : ", ";
                    query += colName;
                    values += (values.isEmpty()) ? " VALUES(" : ", ";
                    values += ":";
                    values += colName;
                } else //запомним его имя
                {
                    autoincrementFieldKey = key;
                }
                log.warn("DDataGrid::insert colName= " + colName);
                log.warn("DDataGrid::insert val type class= " + ((domain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(domain.getRows().get(key).getVal().getClass())));
                log.warn("DDataGrid::insert val = " + ((domain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(domain.getRows().get(key).getVal())));

                //Для полей типа дат пока особый разбор ( не придумала как по другому )
                if (builder.getColumn(key).getColumnProperty() instanceof DColumnPropertyTextArea) {
                    if (domain.getRows().get(key).getVal() == null) {
                        params.put(colName, "");
                    } else {
                        params.put(colName, domain.getRows().get(key).getVal());
                    }
                } else if (builder.getColumn(key).getColumnProperty().getType() != EColumnType.DATE) {
                    params.put(colName, domain.getRows().get(key).getVal());
                } else {
                    if (domain.getRows().get(key).getVal() != null) {
                        Date d = (Date) domain.getRows().get(key).getVal();
                        SimpleDateFormat sdf;
                        if (builder.getColumn(key).getColumnProperty() instanceof DColumnPropertyCurrentTimeWhenEdit) {
                            sdf = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT);
                        } else {
                            if (builder.getColumn(key).getColumnProperty().getFormat().equals("dd.MM.yyyy HH:mm")) {
                                sdf = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT);
                            } else {
                                //d = addHoursToDate(12, d);
                                sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
                            }
                        }

                        String val = sdf.format(d);
                        params.put(colName, val);
                    } else {
                        params.put(colName, null);
                    }
                }

            }
        }

        query += ") " + values + ")";


        if (autoincrementFieldKey != null) {
            SqlParameterSource namedParameters = new MapSqlParameterSource(params);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            //changeDataSource(table);
            log.warn(query);
            Integer rows = getJdbcTemplate(table).update(query, namedParameters, keyHolder, new String[]{autoincrementFieldKey.getColumnName()});
            domain.getRows().get(autoincrementFieldKey).setVal(keyHolder.getKey());
        } else {
            //changeDataSource(table);
            log.warn(query);
            getJdbcTemplate(table).update(query, params);
        }

        //если есть автоинкрементное поле и информация по последовательности которая его заполняет получаем его значение после вставки записи
        //TODO надо потестировать
        /*if ( autoincrementFieldKey!=null && !table.getSequence().isEmpty()) {

         Long id = jdbcTemplate.queryForLong("SELECT SEQ_CURRVAL('" + table.getSequence() + "') FROM DUAL", new HashMap());
         log.warn("SELECT SEQ_CURRVAL('" + table.getSequence() + "') FROM DUAL");
         //SKeyForColumn key = new SKeyForColumn(DTable.MAIN_TABLE, autoincrementFieldName);
         log.warn("CURRVAL ID = " + id.toString());
         domain.getRows().get(autoincrementFieldKey).setVal(id);
         }*/
        return domain;
    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean update(String tableName, DDataGrid domain, DDataGrid oldDomain) {
        DTable table = DTableMapManager.getInstance().getTable(tableName);
        String query = "";
        String where = "";
        String dbTableName = table.getName();
        HashMap<String, Object> params = new HashMap();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                String colName = builder.getColumn(key).getName();
                if (!key.getTableAlias().equals(DTable.MAIN_TABLE)) {
                    continue;//поле принадлежит не главной таблице
                }
                if (!builder.getColumn(key).getIsKey() && !builder.getColumn(key).getIsEditable() && !builder.getColumn(key).getIsAlwaysUpdated()) {
                    continue; //поле было не редактируемым и нечего его править в БД
                }
                if (!builder.getColumn(key).getIsAutoincrement() && builder.getColumn(key).getIsEditable() || builder.getColumn(key).getIsAlwaysUpdated()) //поле не автоинкрементное
                {
                    query += (query.isEmpty()) ? "UPDATE " + dbTableName + " SET " : ", ";
                    query += colName + " =";//
                    query += ":";
                    query += colName;
                    //Для полей типа дат пока особый разбор ( не придумала как по другому )
                    if (builder.getColumn(key).getColumnProperty() instanceof DColumnPropertyTextArea) {
                        if (domain.getRows().get(key).getVal() == null) {
                            params.put(colName, "");
                        } else {
                            params.put(colName, domain.getRows().get(key).getVal());
                        }
                    } else if (builder.getColumn(key).getColumnProperty().getType() != EColumnType.DATE) {
                        params.put(colName, domain.getRows().get(key).getVal());
                        log.warn("DDataGrid::update  colName = " + ((domain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(domain.getRows().get(key).getVal())));
                    } else {
                        if (domain.getRows().get(key).getVal() != null) {
                            Date d = (Date) domain.getRows().get(key).getVal();
                            SimpleDateFormat sdf;
                            if (builder.getColumn(key).getColumnProperty() instanceof DColumnPropertyCurrentTimeWhenEdit) {
                                sdf = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT);
                            } else {
                                if (builder.getColumn(key).getColumnProperty().getFormat().equals("dd.MM.yyyy HH:mm")) {
                                    sdf = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT);
                                } else {
                                    //d = addHoursToDate(12, d);
                                    sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
                                }
                            }
                            String val = sdf.format(d);
                            params.put(colName, val);
                            log.warn("DDataGrid::update colName(date) = " + val);
                        } else {
                            params.put(colName, null);
                        }
                    }
                }
                if (builder.getColumn(key).getIsKey()) { //поле  ключевое   
                    where += (where.isEmpty()) ? " WHERE " : " AND ";
                    where += colName + " = :W_" + colName;
                    params.put("W_" + colName, oldDomain.getRows().get(key).getVal());
                    log.warn("DDataGrid::update val_id = " + ((oldDomain.getRows().get(key).getVal() == null) ? "null" : String.valueOf(oldDomain.getRows().get(key).getVal())));
                }
            }
        }
        query += where;

        //changeDataSource(table);
        log.warn(query);
        getJdbcTemplate(table).update(query, params);
        return true;
    }

    @Override
    @Transactional(value = "txArm", rollbackFor = Throwable.class)
    public boolean delete(String tableName, DDataGrid domain) {
        DTable table = DTableMapManager.getInstance().getTable(tableName);

        String dbTableName = table.getName();
        String query = "DELETE " + dbTableName;
        String where = "";
        HashMap<String, Object> params = new HashMap();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                String colName = builder.getColumn(key).getName();
                if (!key.getTableAlias().equals(DTable.MAIN_TABLE)) {
                    continue;//колонка принадлежит главной таблице
                }
                if (builder.getColumn(key).getIsKey()) //поле ключевое
                {
                    where += (where.isEmpty()) ? " WHERE " : " AND ";
                    where += colName + " = :" + colName;
                }
                params.put(colName, domain.getRows().get(key).getVal());
            }
        }
        query += where;

        //changeDataSource(table);
        log.warn(query);
        getJdbcTemplate(table).update(query, params);
        return true;
    }

    private String getQueryWhithParams(String sql, DataGridSearchCondition condition) {
        if (condition.getParams().isEmpty()) {
            return sql;
        }
        for (Map.Entry paramEntry : condition.getParams().entrySet()) {
            String key = (String) paramEntry.getKey();
            String val = (String) paramEntry.getValue();
            sql = sql.replace(key, val);

        }
        return sql;
    }

//    @Override
//    public Boolean getCheckBoxState(int userId, String tablesNames) {
//        log.warn("SELECT COUNT(COLUMNS_VISIBILITY_ID) FROM Pm4.dbo.COLUMNS_VISIBILITY WHERE USER_ID=" + userId);
//        String query = "SELECT COUNT(COLUMNS_VISIBILITY_ID) FROM Pm4.dbo.COLUMNS_VISIBILITY WHERE USER_ID=" + userId;
//        return (jdbcTemplate.queryForInt(query, new HashMap()) > 0);
//    }
    @Override
    public void updateColumnsState(int userId, String tablesNames, String columnsStates) {
        String query = "IF EXISTS(SELECT * FROM Pm4.dbo.COLUMNS_VISIBILITY WHERE USER_ID=" + userId + " AND TABLES_NAMES='" + tablesNames + "')"
                + " UPDATE Pm4.dbo.COLUMNS_VISIBILITY SET COLUMNS_NUMBERS='" + columnsStates + "' WHERE USER_ID=" + userId + " AND TABLES_NAMES='" + tablesNames + "'"
                + " ELSE"
                + " INSERT INTO Pm4.dbo.COLUMNS_VISIBILITY(USER_ID, TABLES_NAMES, COLUMNS_NUMBERS) VALUES (" + userId + ",'" + tablesNames + "','" + columnsStates + "')";
        log.warn(query);
        jdbcTemplate.update(query, new HashMap());
    }

    @Override
    public void deleteColumnsState(int userId, String tablesNames) {
        String query = "DELETE FROM Pm4.dbo.COLUMNS_VISIBILITY WHERE USER_ID = " + userId + " AND TABLES_NAMES ='" + tablesNames + "'";
        log.warn(query);
        jdbcTemplate.update(query, new HashMap());
    }

    @Override
    public String getColumnsState(int userId, String tablesNames) {
        String query = "SELECT COLUMNS_NUMBERS FROM PM4.dbo.COLUMNS_VISIBILITY WHERE USER_ID='" + userId + "' AND TABLES_NAMES = '" + tablesNames + "'";
        log.warn(query);
        return jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), String.class);
    }
}
