package pst.arm.server.common.service.gwt;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.userdetails.UsernameNotFoundException;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.BusinessException;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.modules.admin.domain.UserSearchCondition;
import pst.arm.server.common.service.UserService;
import pst.arm.server.common.web.GWTController;

public class GWTUserServiceImpl extends GWTController implements
        GWTUserService {

    private static final Logger log = Logger.getLogger(GWTUserServiceImpl.class);
    private UserService userService;

    @Override
    public User getCurrentUser() throws BusinessException {

        try {
            User rtn = userService.getCurrentUser();
            return rtn;

        } catch (UsernameNotFoundException u) {
            log.warn("No User Found " + u);
            throw new BusinessException(u);
        }
    }

    @Override
    public List<User> getAllUsers() throws BusinessException {
        return userService.getAllUsers();
    }

    @Override
    public List<User> getUsers(UserSearchCondition condition) throws BusinessException {
        return userService.getUsers(condition);
    }

    @Override
    public List<User> getActiveUsers() throws BusinessException {
        return userService.getActiveUsers();
    }

    //@Override
    public void setUserService(UserService userService) {
        log.warn("GWTUserServiceImpl::setUserService");
        this.userService = userService;
    }

    @Override
    public List<Role> getRoles() {
        return this.userService.getRoles();
    }

    @Override
    public List<Facility> getFacilities() {
        return this.userService.getFacilities();
    }

    @Override
    public void saveRoles(List<Role> updRoles, List<Role> addRoles, List<Role> delRoles) {
        this.userService.saveRoles(updRoles);
        this.userService.saveRoles(addRoles);
        this.userService.deleteRoles(delRoles);
    }

    @Override
    public Long saveUser(User user) throws RpcServiceException{
        try {
        return userService.saveUser(user);
         } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при сохранении пользователя", ex.getMessage());
        }
    }

    @Override
    public Long deleteUser(User user) {
        return userService.deleteUser(user);
    }

    @Override
    public Long setPassword(User user) throws RpcServiceException {
        return userService.setPassword(user);
    }

    @Override
    public Long saveUserWithPassword(User user) throws RpcServiceException {
        Long id = userService.saveUser(user);
        if(!StringUtils.isEmpty(user.getSecurityData())){
            setPassword(user);
        }
        return id;
    }

    @Override
    public Date getTodayDate() throws RpcServiceException {
        return userService.getTodayDate();
    }

    @Override
    public List<Company> getCompanies() {
        return this.userService.getCompanies();
    }
}
