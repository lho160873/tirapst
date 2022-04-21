package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.modules.admin.stat.domain.StatItem;

/**
 *
 * @author kozhin
 */
public interface GWTCommonServiceAsync {
    
    public void getPortletLinks(AsyncCallback<List<PortletLink>> asyncCallback);

    public void stat(String message, AsyncCallback<Void> asyncCallback);

    public void getStatistic(AsyncCallback<List<StatItem>> asyncCallback);

    public void getChangeLog(AsyncCallback<String> asyncCallback);

    public void getOpenTable(String key, AsyncCallback<HashMap< String, Boolean>> callback);

    public void addOpenTable(String key, String tableName, AsyncCallback<Void> callback);

    public void delOpenTable(String key, String tableName, AsyncCallback<Void> callback);
}
