package pst.arm.server.modules.tablegrid.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;
import pst.arm.server.modules.tablegrid.dao.TableGridDAO;
import pst.arm.server.modules.tablegrid.dao.jdbc.mappers.TableGridMapper;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Repository
public class TableGridDAOJdbcImpl implements TableGridDAO{
    private static Logger log = Logger.getLogger("TableGridDAOJdbcImpl");
    private static final Map<String, String> fields = new HashMap();    
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    static{
        fields.put("name", "NAME");             
    }
    
    @Autowired
    @Qualifier("dsArm")
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    

    @Override
    public List<TableGrid> getTableGrid(TableGridSearchCondition condition) {
        List<TableGrid> domain;
        String query = "SELECT USER_ID, LAST_NAME, RN FROM ( " +
                "SELECT A1.*, ROWNUM RN "
                + "FROM ( " +
                "SELECT * FROM USERS " 
                + getWhere(condition)
                + getOrderBy(condition) 
                + ") A1 )" 
                + getPagingWhere(condition);
        
        log.warn(query);
        domain = jdbcTemplate.query(query.toString(), new HashMap(), new TableGridMapper());
        return domain;
    }

    @Override
    public List<TableGrid> getAllTableGrid() {
     TableGridSearchCondition condition = new TableGridSearchCondition();
        condition.setSortFieldId("name");
        condition.setSortDir(TableGridSearchCondition.SortDir.ASC);
        List<TableGrid> domain;
        String query = "SELECT USER_ID, LAST_NAME FROM " +
                " USERS " 
                + getOrderBy(condition);
        log.warn(query);
        domain = jdbcTemplate.query(query.toString(), new HashMap(), new TableGridMapper());
        return domain;
    }
    
    private String getPagingWhere(TableGridSearchCondition condition){
        if(condition != null && condition.getLimit() != null && condition.getOffset() != null){
            return " WHERE RN BETWEEN " + (condition.getOffset()+1) + " AND " +
                    (condition.getOffset() + condition.getLimit());
        }else{
            return " ";
        }
    }
    
    private String getOrderBy(TableGridSearchCondition condition){
        if(condition != null && !condition.getSortDir().equals(TableGridSearchCondition.SortDir.NONE)){
            return "ORDER BY " +
                    fields.get(condition.getSortFieldId()) + " " + condition.getSortDir().getDir();
        }else{
            return "";
        }
    }

    @Override
    public int getTotalCount(TableGridSearchCondition condition) {
        return jdbcTemplate.queryForInt("select count(*) from USERS "+getWhere(condition), new HashMap());
    }

    private String getWhere(TableGridSearchCondition condition) {
        StringBuffer buffer = new StringBuffer();
        String[] keys = condition.getFilters().keySet().toArray(new String[0]);
        Boolean isWhereAdd = false;
        if (keys.length>0)
        {
         buffer.append(" WHERE ");
         isWhereAdd = true;
        }
        for(int i = 0; i < keys.length; i++){
            buffer.append(fields.get(keys[i]));
            buffer.append(" = :");
            buffer.append(keys[i]);
            if(i < keys.length - 1){
                buffer.append(" AND ");
            }
        }
        
        if (condition.getName() != null && !condition.getName().isEmpty()) {
            String subsql = "";
            for (String token : condition.getName().split(" ")) {
                subsql += " AND (UPPER(NAME) LIKE UPPER('%" + token + "%'))";
            }
            buffer.append( (isWhereAdd) ? " AND " : " WHERE " + subsql.substring(4));
        }        
        return buffer.toString();
    }
    
    
    
    @Override
    @Transactional("txArm")
    public TableGrid insert(TableGrid domain) {
        log.warn("insert");
        TableGrid insertedDomain = null;
        String query = "INSERT INTO USERS(USER_ID, LAST_NAME) "
                + "VALUES(:id, :name)"; 
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        
        log.warn("id = "+domain.getId());
        log.warn("name = "+domain.getName());
        
        params.put("id", domain.getId());
        params.put("name", domain.getName());
       
        SqlParameterSource source = new MapSqlParameterSource(params);
        
        //KeyHolder keyHolder = new GeneratedKeyHolder();
        log.warn(query);
        jdbcTemplate.update(query, params);   
        //jdbcTemplate.update(query, source, keyHolder, new String[]{"DICT_ID"}); 
        //keyHolder.getKey().longValue()
        insertedDomain = new TableGrid();
                insertedDomain.setId(domain.getId());
                insertedDomain.setName(domain.getName());
        
        return insertedDomain;
    }
    
    @Override
    @Transactional(value="txArm" ,rollbackFor=Throwable.class)
    public boolean update(TableGrid domain) {
        String query = "UPDATE USERS "
                + "SET LAST_NAME = :name "
                + "WHERE USER_ID = :id";
        
        HashMap<String, Object> params = new HashMap();
        log.warn("id = "+domain.getId());
        log.warn("name = "+domain.getName());
        params.put("id", domain.getId());
        params.put("name", domain.getName());
      
        log.warn(query);
        jdbcTemplate.update(query, params);   
        return true;
    }

    @Override
    @Transactional(value="txArm" ,rollbackFor=Throwable.class)
    public boolean delete(long id) {
        String query = "DELETE USERS "
                + "WHERE USER_ID = :id";
        
        HashMap<String, Long> params = new HashMap();
        params.put("id", id);
        
        jdbcTemplate.update(query, params);   
        return true;
    }
    
    @Override
    public TableGrid getTableGrid(long id) 
    {
         log.warn("getTableGridForId");
        TableGrid domain = null;
        
        String query = "SELECT * "
                + "FROM USERS "
                + "WHERE USER_ID = :id";
//        Object[] params = new Object[]{Long.valueOf(id)};
        HashMap<String, Long> params = new HashMap();
        params.put("id", Long.valueOf(id));
        log.warn(query);
        List<TableGrid> res = jdbcTemplate.query(query, params, new TableGridMapper());
        
        if(!res.isEmpty()){
            domain = res.get(0);
        }
        return domain;
    }
    

}