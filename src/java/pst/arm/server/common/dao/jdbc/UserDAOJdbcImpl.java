package pst.arm.server.common.dao.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.admin.domain.UserSearchCondition;
import pst.arm.server.common.ConfigurationManager;
import pst.arm.server.common.dao.UserDAO;
import pst.arm.server.common.dao.jdbc.mappers.CompanyMapper;
import pst.arm.server.common.dao.jdbc.mappers.DateMapper;
import pst.arm.server.common.dao.jdbc.mappers.FacilityMapper;
import pst.arm.server.common.dao.jdbc.mappers.IntegerMapper;
import pst.arm.server.common.dao.jdbc.mappers.RoleMapper;
import pst.arm.server.common.dao.jdbc.mappers.UserMapper;
import pst.arm.server.common.domain.ServerSideUser;
import pst.arm.server.common.service.StaticContextHolder;

/**
 *
 * @author Alexandr Kozhin
 */
public class UserDAOJdbcImpl extends JdbcDaoSupport implements UserDAO, UserDetailsService {

    private TransactionTemplate txTemplate;
    private static Logger log = Logger.getLogger("UserDAOJdbcImpl");

    public void setTransactionManager(PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.txTemplate.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
    }

    @Override
    public void delete(User user) {
        //String sql = "DELETE FROM USER_ROLES WHERE USER_ID = ?";
        //getJdbcTemplate().update(sql, user.getId());
        //sql = "DELETE FROM USERS U WHERE USER_ID = ?";
        String sql = " UPDATE USERS SET DELETED = 1, DATE_DELETED = GETDATE() WHERE USER_ID = " + user.getId();
        getJdbcTemplate().update(sql);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String sql = ""
                + " SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID "
                + " WHERE U.DELETED = 0 OR U.DELETED IS NULL"
                + " ORDER BY U.NAME  ";
        log.warn("getAllUsers sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper());
        if (users != null) {
            for (User user : users) {
                user.setRoles(getRoles(user.getId()));
                user.setCompanies(getCompanies(user.getId()));
            }
        }
        return users;
    }

    @Override
    public List<User> getUsers(UserSearchCondition condition) {
        List<User> users = new ArrayList<User>();
        String where = condition2sql(condition);
        String sql = ""
                + " SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID "
                + where
                + " ORDER BY U.NAME  ";
        log.warn("getUsers sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper());
        if (users != null) {
            for (User user : users) {
                user.setRoles(getRoles(user.getId()));
                 user.setCompanies(getCompanies(user.getId()));
            }
        }
        return users;
    }

    private String condition2sql(UserSearchCondition condition) {
        String sql = " WHERE (U.DELETED = 0 OR U.DELETED IS NULL)";
        /*if (condition.getFio() != null && !condition.getFio().isEmpty()) {
            String subsql = "";
            for (String token : condition.getFio().split(" ")) {
                subsql += " AND (UPPER(U.FIRST_NAME) LIKE UPPER('%" + token + "%')";
                subsql += " OR UPPER(U.LAST_NAME) LIKE UPPER('%" + token + "%')";
                subsql += " OR UPPER(U.PATRONYMIC) LIKE UPPER('%" + token + "%'))";
            }
            sql += " AND (" + subsql.substring(4) + ")";
        }*/
        if (condition.getFio() != null && !condition.getFio().isEmpty()) {
            sql += " AND UPPER(U.NAME) LIKE UPPER('%" + condition.getFio() + "%')";
        }
        if (condition.getLogin() != null && !condition.getLogin().isEmpty()) {
            sql += " AND UPPER(U.USER_LOGIN) LIKE UPPER('%" + condition.getLogin() + "%')";
        }
        if (condition.getSearchNotActive() != null && !condition.getSearchNotActive()) {
            sql += " AND U.ENABLED = 1";
        }
        return sql;
    }

    @Override
    public List<User> getActiveUsers() {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM USERS WHERE ENABLED = 1 AND DELETED = 0 OR DELETED IS NULL";
        if (ConfigurationManager.getInstance().getArchiveId() != null) {
            sql += " AND ARCHIVE_ID = " + ConfigurationManager.getInstance().getArchiveId();
        }
        sql += " ORDER BY NAME  ";
        users = getJdbcTemplate().query(sql, new UserMapper()) ;
        if (users != null) {
            for (User user : users) {
                user.setRoles(getRoles(user.getId()));
                 user.setCompanies(getCompanies(user.getId()));
            }
        }
        return users;
    }

    @Override
    public User getForPaypalID(String paypalID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

@Override
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID WHERE U.USER_LOGIN=? AND U.DELETED != 1";
        log.warn("getUserByUsername sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper(), username);
        if (users.size() != 1) {
            if (!users.isEmpty()) {
                throw new UsernameNotFoundException("Duplicate Username Problem: " + username);
            } else {
                throw new UsernameNotFoundException("Username not found: " + username);
            }
        } else if (users.get(0).getDeleted()) {
            throw new UsernameNotFoundException("User deleted: " + username);
        }

        User user = users.get(0);
        user.setRoles(getRoles(user.getId()));
        user.setCompanies(getCompanies(user.getId()));
        //log.warn("!!!!!!!!!!!!!");
        
        getJdbcTemplate().execute("EXEC dbo.CURRENT_USER_ID " + user.getId().toString());
        
        //getJdbcTemplate().execute("EXEC priboy_db5.dbo.CURRENT_USER_ID " + user.getId().toString());
        
        ComboPooledDataSource ds_priboy = (ComboPooledDataSource) StaticContextHolder.getBean("dataSourcePriboy");
        JdbcTemplate jdbcTemplate_priboy = new JdbcTemplate(ds_priboy);        
        jdbcTemplate_priboy.execute("EXEC dbo.CURRENT_USER_ID " + user.getId().toString());
        
        
        
        
        //Integer s =  ((List<Integer>)getJdbcTemplate().query("SELECT  cast( cast(substring(CONTEXT_INFO(), 1, charindex(0x00, CONTEXT_INFO())-1) as varchar(128)) as int) as ID",new IntegerMapper())).get(0);
        //log.warn("TEST s ="+s);
        
        //Integer ss =  ((List<Integer>)jdbcTemplate_priboy.query("SELECT  cast( cast(substring(CONTEXT_INFO(), 1, charindex(0x00, CONTEXT_INFO())-1) as varchar(128)) as int) as ID",new IntegerMapper())).get(0);
        //log.warn("TEST ss ="+ss);
         
        
        //log.warn("TEST EXEC dbo.CURRENT_USER_ID "+user.getId().toString());
        return user;
    }

    @Override
    public User getUserForId(long id) {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID WHERE U.USER_ID = ? AND U.DELETED != 1";
        log.warn("getUserForId sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper(), id);
        User user = users.get(0);
        user.setRoles(getRoles(user.getId()));
         user.setCompanies(getCompanies(user.getId()));
        return user;
    }

    @Override
    public User getUserByIdAndArchiveId(long userId, long archiveId) {
        List<User> users = new ArrayList<User>();
        User user = null;
        String sql = "SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID WHERE U.USER_ID = ? AND U.ARCHIVE_ID= ? AND U.DELETED != 1";
        log.warn("getUserByIdAndArchiveId sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper(), userId, archiveId);
        if (users.size() > 0) {
            user = users.get(0);
            user.setRoles(getRoles(user.getId()));
             user.setCompanies(getCompanies(user.getId()));
        }
        return user;
    }

    @Override
    public ServerSideUser loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT U.*, ISNULL(W.NAME,'') AS WORKER_NAME FROM USERS U LEFT JOIN WORKER W  ON  U.WORKER_ID=W.WORKER_ID WHERE U.USER_LOGIN=? AND U.DELETED != 1";
        log.warn("loadUserByUsername sql = "+sql);
        users = getJdbcTemplate().query(sql, new UserMapper(), username);

        if (users.size() == 0)
            throw new UsernameNotFoundException("");
        User user = users.get(0);
        String sqll = "INSERT INTO USERS_LOGIN (USER_ID) VALUES (" + user.getId() + ")";
        getJdbcTemplate().update(sqll);
        return new ServerSideUser(getUserByUsername(username));
    }

    @Override
    public Long deleteUser(User user) {
        delete(user);
        return 0L;
    }

    @Override
    public Long setPassword(User user) {
        String sql = " UPDATE USERS SET PASSWORD = ? WHERE USER_ID = ? ";
        getJdbcTemplate().update(sql, user.getPassword(), user.getId());
        return 0L;
    }

    @Override
    public long getUserCount() {
        String sql = "SELECT COUNT(*) FROM USERS U WHERE U.ENABLED = 1 AND (DELETED = 0 OR DELETED IS NULL)";
        return getJdbcTemplate().queryForInt(sql);
    }

    @Override
    public User getUserByUsernameFetchAll(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserByNicknameFetchAll(String nickname) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Role> getRoles() {
        return getRoles(null);
    }

    
    @Override
    public List<Role> getRoles(Long userId) {
        String sql = "SELECT R.ROLE_ID, R.ROLE_NAME, DESCRIPTION FROM ROLES R";

        if (userId != null) {
            sql += " JOIN USER_ROLES UR ON R.ROLE_ID = UR.ROLE_ID WHERE UR.USER_ID = " + userId;
        }

        List<Role> list = getJdbcTemplate().query(sql, new RoleMapper());

        if (list != null && !list.isEmpty()) {

            sql = "SELECT F.FACILITY_ID, F.TYPE, F.MODULE, F.NAME, F.DESCRIPTION "
                    + " FROM ROLE_FACILITIES R"
                    + " LEFT JOIN FACILITIES F ON (F.FACILITY_ID = R.FACILITY_ID)"
                    + " WHERE R.ROLE_ID = ?";

            for (Role role : list) {
                role.setFasilities(getJdbcTemplate().query(sql, new FacilityMapper(), role.getRoleId()));
            }
        }

        return list;
    }

    @Override
    public Long saveUser(User user) {
        Long ret = null;
        if (user.getId() == null) {
            ret = insertUser(user);
        } else {
            ret = updateUser(user);
        }

        return ret;
    }

    @Override
    public Long insertUser(final User user) {
        
        
        return txTemplate.execute(new TransactionCallback<Long>() {

            @Override
            public Long doInTransaction(TransactionStatus ts) {
                try {

                    String sqlBefor = "SELECT COUNT(*) FROM USERS "
                            + " WHERE USERS.USER_LOGIN like '"+ user.getUserLogin()+"'"
                            + " AND USERS.DELETED<>1";

                    if (getJdbcTemplate().queryForInt(sqlBefor) > 0) {
                        ts.setRollbackOnly();
                        throw new RpcServiceException("USERS_EXISTSLOGIN");
                    }
                    
                    
                    String sql = " INSERT INTO USERS ( "
                            + "DATE_CREATED, USER_LOGIN, PASSWORD, ARCHIVE_ID, ENABLED,"
                            + " NAME, "
                            + " DESCRIPTION, DELETED, WORKER_ID ) VALUES ( GETDATE(), "
                            + " :userLogin, :password, :archiveId, :enabled, :userName,"
                            + " :description, :deleted, :workerId)";
                    NamedParameterJdbcTemplate jdbcTemplateNamed;
                    jdbcTemplateNamed = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource()); 
                   
                   SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    Integer rows = jdbcTemplateNamed.update(sql, namedParameters, keyHolder, new String[]{"USER_ID"});
                    
                    if (!rows.equals(1)) {
                        return null;
                    }
                    user.setId(keyHolder.getKey().longValue());

                    insertRoles(user);
                    insertCompanies(user);

                } catch (Exception ex) {
                    System.err.println("ERROR insert user");
                    System.err.println(ex.getMessage());
                    ts.setRollbackOnly();
                    throw new TransactionException("ERROR insert user: " + ex.getMessage()) {
                    };
                }

                return user.getId();
            }
        });
    }

    @Override
    public Long updateUser(final User user) {
        return txTemplate.execute(new TransactionCallback<Long>() {

            @Override
            public Long doInTransaction(TransactionStatus ts) {
                try {
                    String sql = " UPDATE USERS SET "
                            + "USER_LOGIN = ?, "
                            + "ENABLED = ?, "
                            + "NAME = ?, "
                            + "DESCRIPTION = ?, "
                            + "DELETED = ?, "
                            + "WORKER_ID = ? "
                            + "WHERE USER_ID = ? ";

                    getJdbcTemplate().update(sql,
                            user.getUserLogin(), user.getEnabled(),
                            user.getUserName(),
                            user.getDescription(), user.getDeleted(),
                            user.getWorkerId(), user.getId());

                    insertRoles(user);
                    insertCompanies(user);

                } catch (Exception ex) {
                    System.err.println("ERROR update user");
                    System.err.println(ex.getMessage());
                    ts.setRollbackOnly();
                }
                return user.getId();
            }
        });
    }

    protected void insertRoles(User user) {
        // always make clear insert roles with remove all existing ones
        if ((user == null) || (user.getId() == null)) {
            return;
        }

        String sqlDel = " DELETE FROM USER_ROLES WHERE USER_ID = " + user.getId();
        getJdbcTemplate().update(sqlDel);

        List<Role> roles = user.getRoles();
        if (roles != null) {
            String sqlIns = " INSERT INTO USER_ROLES (USER_ID, ROLE_ID) VALUES (?, ?) ";
            for (Role role : roles) {
                getJdbcTemplate().update(sqlIns, user.getId(), role.getRoleId());
            }
        }
    }

    @Override
    public List<Facility> getFacilities() {
        return getJdbcTemplate().query(""
                + "SELECT FACILITY_ID, TYPE, MODULE, NAME, DESCRIPTION "
                + "FROM FACILITIES "
                + "ORDER BY TYPE, DESCRIPTION, FACILITY_ID   ",
                new FacilityMapper());
    }
    
   

    @Override
    public void saveRoles(List<Role> roles) {
        for (Role role : roles) {
            saveRole(role);
        }
    }

    /**
     * updates role in DB.
     * if the role already exists, then the record is updated.
     * if the roles do not exist, then adds a new.
     * And updated facility (ROLE_FACILITIES). Removed current facility and add new ones.
     * @param role
     */
    public void saveRole(final Role role) {
        txTemplate.execute(new TransactionCallback<Void>() {

            @Override
            public Void doInTransaction(TransactionStatus ts) {
                try {
                    if (role.getRoleId() != null) {
                        getJdbcTemplate().update(""
                                + "UPDATE ROLES "
                                + "SET ROLE_NAME = ?, DESCRIPTION = ? "
                                + "WHERE ROLE_ID = ?",
                                role.getRoleName(), role.getDescription(),
                                role.getRoleId());
                    } else {
                        String sql = "INSERT INTO ROLES "
                                + "(ROLE_NAME, DESCRIPTION) "
                                + "VALUES (:roleName, :description )";
                    NamedParameterJdbcTemplate jdbcTemplateNamed;
                    jdbcTemplateNamed = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource()); 
                   
                    SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(role);
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    Integer rows = jdbcTemplateNamed.update(sql, namedParameters, keyHolder, new String[]{"USER_ID"});
                    
                    if (!rows.equals(1)) {
                        return null;
                    }
                     role.setRoleId(keyHolder.getKey().longValue());
                    }

                    if (role.getFasilities() != null) {

                        getJdbcTemplate().update("DELETE FROM ROLE_FACILITIES WHERE ROLE_ID = ?", role.getRoleId());

                        for (Facility f : role.getFasilities()) {
                            getJdbcTemplate().update(
                                    "INSERT INTO ROLE_FACILITIES (ROLE_ID, FACILITY_ID) VALUES (?, ?)",
                                    role.getRoleId(), f.getFacilityId());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("ERROR save role");
                    System.err.println(ex.getMessage());
                    ts.setRollbackOnly();
                }
                return null;
            }
        });
    }

    @Override
    public void daleteRoles(List<Role> roles) {
        for (Role role : roles) {
            deleteRole(role);
        }
    }

    private void deleteRole(final Role role) {
        txTemplate.execute(new TransactionCallback<Void>() {

            @Override
            public Void doInTransaction(TransactionStatus ts) {
                if (role.getRoleId() != null) {
                    try {
                        getJdbcTemplate().update("DELETE FROM ROLE_FACILITIES WHERE ROLE_ID = ?", role.getRoleId());

                        getJdbcTemplate().update("DELETE FROM ROLES WHERE ROLE_ID = ?", role.getRoleId());

                    } catch (Exception ex) {
                        System.err.println("ERROR delete role");
                        System.err.println(ex.getMessage());
                        ts.setRollbackOnly();
                    }
                }
                return null;
            }
        });
    }

    @Override
    public Date getTodayDate() throws RpcServiceException {
        String sql = " SELECT SYSDATE NOW FROM DUAL";
        Date date;
        try {
            date = ( (List<Date>)getJdbcTemplate().query( sql, new DateMapper() ) ).get(0);            
        } catch (Exception ex) {
            throw new RpcServiceException("error get today date", ex.getMessage() );
        }
        
        if( date != null )
            return date;        
        return new Date();
    }
    
    @Override
    public List<Company> getCompanies() {
        return getJdbcTemplate().query(""
                + "SELECT COMPANY_ID, NAME, SHORT_NAME "
                + "FROM COMPANY "
                + "ORDER BY NAME, SHORT_NAME ",
                new CompanyMapper());
    }

    @Override
    public List<Company> getCompanies(Long userId) {

        String sql = "SELECT C.COMPANY_ID COMPANY_ID, C.NAME NAME, C.SHORT_NAME SHORT_NAME FROM COMPANY C";

        if (userId != null) {
            sql += " JOIN USER_COMPANY UC ON C.COMPANY_ID = UC.COMPANY_ID WHERE UC.USER_ID = " + userId;
        }

        List<Company> list = getJdbcTemplate().query(sql, new CompanyMapper());


        return list;
    }
    
    protected void insertCompanies(User user) {
        // always make clear insert roles with remove all existing ones
        if ((user == null) || (user.getId() == null)) {
            return;
        }
        String sqlDel = " DELETE FROM USER_COMPANY WHERE USER_ID = " + user.getId();
        getJdbcTemplate().update(sqlDel);
        List<Company> companies = user.getCompanies();
        if (companies != null) {
            String sqlIns = " INSERT INTO USER_COMPANY (USER_ID, COMPANY_ID) VALUES (?, ?) ";
            for (Company company : companies) {
                getJdbcTemplate().update(sqlIns, user.getId(), company.getCompanyId());
            }
        }
    }
   
}
