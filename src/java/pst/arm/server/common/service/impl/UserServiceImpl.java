package pst.arm.server.common.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Owner;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.BusinessException;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.admin.domain.UserSearchCondition;
import pst.arm.server.common.dao.UserDAO;
import pst.arm.server.common.service.UserService;


/**
 * 
 * @author Sergey Smirnov
 * 
 */
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * match with applicationContext-acegi-security.xml <bean
     * id="anonymousProcessingFilter">
     */
    public static final String ANONYMOUS = "anonymousUser";
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);
    private MessageSource messageSource;
    private PasswordEncoder passwordEncoder;
    private SaltSource saltSource;
    private UserDAO userDAO;
    //private UserHib userHib;
    private AuthenticationManager authMgr;

    /*public void setUserHib( UserHib userHib ) {
        this.userHib= userHib;
    }*/
    
    @Override
    public User createUser(String username, String userpass) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void createPassWord(User user, String userpass) {
        passwordEncoder = new Md5PasswordEncoder();
        String unencodedPassword = user.getSecurityData();
        String encodedPassword = passwordEncoder.encodePassword(unencodedPassword, null);
        user.setUserLogin(user.getUserLogin().trim().toLowerCase());
        user.setPassword(encodedPassword);
    }

    @Override
    public User getCurrentUser() throws UsernameNotFoundException {
        //userHib.Test();
        return getCurrentUser(false);
    }

    @Override
    public User getCurrentUser(boolean useCache)
            throws UsernameNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (null == auth) {
            throw new UsernameNotFoundException("No Authentications");
        }

        Object obj = auth.getPrincipal();
        String username = "";

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }

        if (username.equals(ANONYMOUS)) {
            log.debug("Anonymous return null user");
            return null;
        }

        User u;
        u = userDAO.getUserByUsername(username);
        return u;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public List<User> getUsers(UserSearchCondition condition) {
        return userDAO.getUsers(condition);
    }

    @Override
    public List<User> getActiveUsers() {
        return userDAO.getActiveUsers();
    }

    @Override
    public User getUserByNicknameFullFetch(String nickname) {
        return userDAO.getUserByNicknameFetchAll(nickname);
    }

    private String gm(String messageName) {
        return messageSource.getMessage(messageName, null, null);
    }

    @Override
    public Long saveUser(User user) {
        if (user.getId() == null) {
            createPassWord(user, user.getSecurityData());
        }
        userDAO.saveUser(user);
        return 0L;
    }

    @Override
    public Owner fillOwnerWithUser(Owner owner) {
        owner.setUser(userDAO.getUserByIdAndArchiveId(owner.getId(), owner.getArchiveId()));
        return owner;
    }

    @Override
    public Long deleteUser(User user) {
        return userDAO.deleteUser(user);
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    public void setAuthMgr(AuthenticationManager authMgr) {
        this.authMgr = authMgr;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getUser(long id) {
        return this.userDAO.getUserForId(id);
    }

    @Override
    public List<Role> getRoles() {
        return this.userDAO.getRoles();
    }

    @Override
    public List<Facility> getFacilities() {
        return this.userDAO.getFacilities();
    }

    @Override
    public void saveRoles(List<Role> roles) {
        userDAO.saveRoles(roles);
    }

    @Override
    public void addRoles(List<Role> roles) {
        userDAO.saveRoles(roles);
    }

    @Override
    public void deleteRoles(List<Role> roles) {
        userDAO.daleteRoles(roles);
    }

    @Override
    public Long setPassword(User user) {
        createPassWord(user, user.getSecurityData());
        return userDAO.setPassword(user);
    }

    @Override
    public List<Role> getUserRoles(Long id) {
        return this.userDAO.getRoles(id);
    }

    @Override
    public Date getTodayDate() throws RpcServiceException {
        return userDAO.getTodayDate();
    }
    
    @Override
    public List<Company> getCompanies() {
        return this.userDAO.getCompanies();
    }
}
