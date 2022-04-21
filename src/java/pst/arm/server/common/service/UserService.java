package pst.arm.server.common.service;

import java.util.Date;
import java.util.List;
import org.springframework.security.userdetails.UsernameNotFoundException;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Owner;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.BusinessException;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.admin.domain.UserSearchCondition;

/**
 *
 * @author Alexandr Kozhin
 */
public interface UserService {

    User createUser(String user, String pass) throws BusinessException;

    User getCurrentUser() throws UsernameNotFoundException;

    User getCurrentUser(boolean useUserCache) throws UsernameNotFoundException;

    List<User> getAllUsers();
    
    List<User> getUsers(UserSearchCondition condition);
    
    List<User> getActiveUsers();
    
    User getUserByNicknameFullFetch(String nickname);

    Long saveUser(User user);

    Long deleteUser(User user);

    User getUser(long id);

    Long setPassword(User user);

    public List<Role> getRoles();

    public List<Role> getUserRoles(Long id);
    
    public List<Facility> getFacilities();

    public void saveRoles(List<Role> roles);

    public void addRoles(List<Role> roles);

    public void deleteRoles(List<Role> roles);

    public Owner fillOwnerWithUser(Owner owner);
    
    public Date getTodayDate() throws RpcServiceException;
    
    public List<Company> getCompanies();

    
}
