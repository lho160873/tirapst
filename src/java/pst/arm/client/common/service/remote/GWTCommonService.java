package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.modules.admin.stat.domain.StatItem;

/**
 *
 * @author Alexandr Kozhin
 */
@RemoteServiceRelativePath("service/commonService")
public interface GWTCommonService extends RemoteService {

    public List<PortletLink> getPortletLinks();

    public void stat(String message);

    public List<StatItem> getStatistic();

    public String getChangeLog();

    public HashMap< String, Boolean> getOpenTable(String key);

    public void addOpenTable(String key, String tableName);

    public void delOpenTable(String key, String tableName);
}
