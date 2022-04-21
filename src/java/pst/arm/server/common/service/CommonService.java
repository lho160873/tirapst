package pst.arm.server.common.service;

import java.util.List;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.modules.admin.stat.domain.StatItem;

/**
 *
 * @author Alexandr Kozhin
 */
public interface CommonService {
    public List<PortletLink> getPortletLinks();
    public void stat(String message);
    public List<StatItem> getStatistic();
    public String getChangeLog();
}
