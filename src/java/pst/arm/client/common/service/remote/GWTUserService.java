package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.exception.BusinessException;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.admin.domain.UserSearchCondition;

@RemoteServiceRelativePath("service/userService")
public interface GWTUserService extends RemoteService {

    public User getCurrentUser() throws BusinessException;

    public List<User> getAllUsers() throws BusinessException;

    public List<User> getUsers(UserSearchCondition condition) throws BusinessException;

    public List<User> getActiveUsers() throws BusinessException;

    public List<Role> getRoles();

    public List<Facility> getFacilities();

    public void saveRoles(List<Role> updRoles, List<Role> addRoles, List<Role> delRoles);

    public Long saveUser(User user) throws RpcServiceException;

    public Long deleteUser(User user) throws RpcServiceException;

    public Long setPassword(User user) throws RpcServiceException;
    
    public Long saveUserWithPassword(User user) throws RpcServiceException;
    
    public Date getTodayDate() throws RpcServiceException;
    
    public List<Company> getCompanies();

}
