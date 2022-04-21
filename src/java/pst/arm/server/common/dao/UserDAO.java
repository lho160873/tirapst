package pst.arm.server.common.dao;

import java.util.Date;
import java.util.List;
import org.springframework.security.userdetails.UsernameNotFoundException;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.admin.domain.UserSearchCondition;

public interface UserDAO {

    void delete(User user);

    List<User> getAllUsers();

    List<User> getUsers(UserSearchCondition condition);

    List<User> getActiveUsers();

    User getForPaypalID(String paypalID);

    User getUserByUsername(String username)
            throws UsernameNotFoundException;

    User getUserForId(long id);

    User getUserByIdAndArchiveId(long userId, long archiveId);

    Long saveUser(User user);

    Long insertUser(User user);

    Long updateUser(User user);

    Long deleteUser(User user);

    Long setPassword(User user);

    long getUserCount();

    User getUserByUsernameFetchAll(String username);

    User getUserByNicknameFetchAll(String nickname);

    public List<Role> getRoles();
    
    public List<Role> getRoles(Long id);

    public void saveRoles(List<Role> roles);

    public void daleteRoles(List<Role> roles);

    public List<Facility> getFacilities();
    
    public Date getTodayDate() throws RpcServiceException;
        
     public List<Company> getCompanies(Long id);
     
      public List<Company> getCompanies();
          
}
