package pst.arm.server.common.service.gwt;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.modules.admin.stat.domain.StatItem;
import pst.arm.server.common.service.CommonService;
import pst.arm.server.common.web.GWTController;

/**
 *
 * @author kozhin
 */
public class GWTCommonServiceImpl extends GWTController implements GWTCommonService {

    private CommonService commonService;
    private static Logger log = Logger.getLogger("GWTCommonServiceImpl");
    public List<PortletLink> getPortletLinks() {
        return getCommonService().getPortletLinks();
    }

    /**
     * @return the commonService
     */
    public CommonService getCommonService() {
        return commonService;
    }

    /**
     * @param commonService the commonService to set
     */
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public void stat(String message) {
        this.commonService.stat(message);
    }

    public List<StatItem> getStatistic() {      
            return this.commonService.getStatistic();    
    }

    public String getChangeLog() {
        log.warn("getChangeLog");
        return this.commonService.getChangeLog();
    }
    
    @Override
     public HashMap< String, Boolean > getOpenTable(String key) {
       //return DTableMapManager.getInstance().getOpenTable();
        
        HttpServletRequest request = this.getThreadLocalRequest();        
        HttpSession session = request.getSession();

        //String id = UUID.randomUUID().toString();
        //condition.setConditionId(Long.getLong(id));

       // byte[] bytes = reportServiceProxy.getReportAsBytes(condition);
        
        //FileObjectDescriptor fod = new FileObjectDescriptor(bytes);
        
        //session.setAttribute("tableName", tableName);
         return getHashMapFromSession(key,session);
     }
    
    @Override
     public void addOpenTable(String key, String tableName) {
        HttpServletRequest request = this.getThreadLocalRequest();
        
        HttpSession session = request.getSession();
        HashMap map = getHashMapFromSession(key,session);
        
        if (!map.containsKey(tableName)) {
            map.put(tableName, Boolean.FALSE);
        } 
        setActivOpenTable(map, tableName);
     }
    
     @Override
     public void delOpenTable(String key, String tableName) {
        HttpServletRequest request = this.getThreadLocalRequest();        
        HttpSession session = request.getSession();
        HashMap map = getHashMapFromSession(key,session);
        
         if (map.containsKey(tableName)) {
             log.warn("delOpenTable::remove tableName = " + tableName);
             map.remove(tableName);
         }
     }
    
    public static HashMap getHashMapFromSession(String key, HttpSession session) {
        // If the session does not contain anything, create a new HashMap
        if (session.getAttribute(key) == null) {
            session.setAttribute(key, new HashMap());
        }
        return (HashMap) session.getAttribute(key);
    }
     
   
   private void setActivOpenTable( HashMap<String,Boolean> map, String tableName )
   {     
       for (Map.Entry<String, Boolean> entry : map.entrySet()) {           
               if ( entry.getKey().equalsIgnoreCase(tableName) )
                 {
                   entry.setValue(Boolean.TRUE);
               } else {
                   entry.setValue(Boolean.FALSE);
               }
           }
   }

}
