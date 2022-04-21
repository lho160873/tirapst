package pst.arm.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.domain.Owner;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
/**
 *
 * @author vorontsov
 */
public class AppCache {

    private static volatile AppCache inctance;
    protected User user = null;
    //protected Map<Long, String> clssList;
    protected final GWTUserServiceAsync service;
    protected BaseConstants constants;
    //protected String GWTServerParam = null;

    private AppCache() {
        constants = (BaseConstants) GWT.create(BaseConstants.class);
        service = GWT.create(GWTUserService.class);
    }

    public static AppCache getInstance() {
        if (inctance == null) {
            synchronized (AppCache.class) {
                if (inctance == null) {
                    inctance = new AppCache();
                }
            }
        }
        return inctance;
    }

    public User getUser() {
        return user;
    }

    public void obtainUserInformation(final AsyncCallback<User> callback) {
        if (user != null) {
            callback.onSuccess(user);
        } else {
            service.getCurrentUser(new AsyncCallback<User>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(User usr) {
                    user = usr;
                    callback.onSuccess(user);
                }
            });
        }
    }

    
    /**
     * Create Owner object from user
     * @return new Owner object
     */
    public Owner userAsOwner() {
        return new Owner( getUser().getArchiveId(), getUser().getId().intValue() );
    }
    

}
