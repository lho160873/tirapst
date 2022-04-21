package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.domain.Company;
import pst.arm.client.modules.admin.domain.UserSearchCondition;

public interface GWTUserServiceAsync {

    public void getCurrentUser(AsyncCallback<User> callback);

    public void getAllUsers(AsyncCallback<List<User>> asyncCallback);

    public void getUsers(UserSearchCondition condition, AsyncCallback<List<User>> asyncCallback);

    public void getActiveUsers(AsyncCallback<List<User>> asyncCallback);

    public void getRoles(AsyncCallback<List<Role>> asyncCallback);

    public void getFacilities(AsyncCallback<List<Facility>> asyncCallback);

    public void saveRoles(List<Role> updRoles, List<Role> addRoles, List<Role> delRoles, AsyncCallback<Void> asyncCallback);

    public void saveUser(User user, AsyncCallback<Long> asyncCallback);

    public void deleteUser(User user, AsyncCallback<Long> asyncCallback);

    public void setPassword(User user, AsyncCallback<Long> asyncCallback);
    
    public void saveUserWithPassword(User user, AsyncCallback<Long> asyncCallback);
    
    public void getTodayDate( AsyncCallback<Date> asyncCallback );
    
    public void getCompanies(AsyncCallback<List<Company>> asyncCallback);
}
