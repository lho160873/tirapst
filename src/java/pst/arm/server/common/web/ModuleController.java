package pst.arm.server.common.web;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.service.UserService;

/**
 * 
 * @author Sergey Smirnov
 * 
 */
abstract public class ModuleController {

    protected static final String BASE_MODULES_CFG_PATH = "config.modules";
    public static final String MODULE_VIEW = "secure/module";
    public static final String DEFAULT_MODULE = "main";
    protected User currentUser;
    protected UserService userService;
    protected MessageSource messageSource;
    private Map<String, String> cfgMap,facilitiesMap;
    protected static Logger log = Logger.getLogger("ModuleController");
    protected static Logger statLog = Logger.getLogger("StatisticLogger");
    

    @RequestMapping(method = RequestMethod.GET)
    public String loadHandler(HttpServletRequest req, Model model) {
        currentUser = userService.getCurrentUser();
        //prepare model map
        ModelMap modelMap = ControllerUtil.getModelMap(req, userService);
        cfgMap = new HashMap<String, String>();
        
        if (checkAccessModule(currentUser, getModuleName())) {
            modelMap.addAttribute("defaultLocale", messageSource.getMessage("config.defaultLocale", null, null));
            modelMap.addAttribute("moduleName", getModuleName());
            //Set page configuration
            cfgMap.putAll(getBaseConfig(getModuleName()));
            cfgMap.putAll(getModuleConfig());
            cfgMap.putAll(getArchiveSpecificConfig(getModuleName()));

        } else {
            modelMap.addAttribute("defaultLocale", messageSource.getMessage("config.defaultLocale", null, null));
            modelMap.addAttribute("moduleName", DEFAULT_MODULE);
            cfgMap.putAll(getBaseConfig(DEFAULT_MODULE));
            cfgMap.putAll(getModuleConfig());
            cfgMap.putAll(getArchiveSpecificConfig(DEFAULT_MODULE));
            //cfgMap.putAll(currentUser.getUserCredentials(DEFAULT_MODULE));
            //cfgMap.put("config.currentUser", currentUser.getUserName());
            //cfgMap.put("config.currentUserId", Long.toString(currentUser.getId()));
        }
        
        modelMap.addAttribute("config", cfgMap);

        model.addAllAttributes(modelMap);
        
        statLog.info("PAGE " + req.getRequestedSessionId() + " " + currentUser.getUserLogin() + " " + getModuleName() + " " + req.getRemoteAddr() + " " + req.getRequestURL() + "?" + req.getQueryString());
        return MODULE_VIEW;
    }
    

    private boolean checkAccessModule(  User currentUser,  String current_module ) {
        List<String> facilitiesLst = currentUser.getFacilities();
        Iterator<String> it = facilitiesLst.iterator();
        while( it.hasNext() )
        {
            String s = it.next();
            if ( s.equals( current_module ) )
                return true;           
        }              
        return false;
    }
    
    private Map<String, String> getBaseConfig(String moduleName) {
        Map<String, String> baseCfgMap = new HashMap<String, String>();
        //Join user facilities and archive config
        Properties baseProps = new Properties();
        try {
            //Load base properties
            baseProps.load(this.getClass().getResourceAsStream("/configs/base.properties"));
            Enumeration e = baseProps.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (!key.startsWith("config.modules") || key.startsWith("config.modules." + moduleName)) {
                    baseCfgMap.put(key, baseProps.getProperty(key));
                }
            }
        } catch (IOException e) {
            log.error("Error load config : " + e.getMessage());
        }

        //Add user credentials (available modules and current module operations)
        facilitiesMap = currentUser.getUserCredentials(moduleName);
        baseCfgMap.putAll(facilitiesMap);
        baseCfgMap.putAll( getConfigRoles() );
        baseCfgMap.put("config.currentUser", currentUser.getUserLogin());
        baseCfgMap.put("config.currentUserId", Long.toString(currentUser.getId()) );
        baseCfgMap.put("config.currentArchiveId", Integer.toString(currentUser.getArchiveId()));
        baseCfgMap.put("config.currentUserName", currentUser.getUserName());
        baseCfgMap.put("config.currentWorkerName", currentUser.getWorkerName());
        return baseCfgMap;
    }
    
    private Map<String, String> getConfigRoles() {
        Map<String, String> map = new HashMap<String, String>();
        StringBuilder roleIds = new StringBuilder();
        for ( Iterator it = currentUser.getRoles().iterator(); it.hasNext(); ) {
            Role r = (Role)it.next();
            if ( r != null )
                roleIds.append( r.getRoleId().toString() ).append(",");
        }
        if ( roleIds.length() > 0)
            map.put("config.currentUserRoleIds",  roleIds.substring( 0, roleIds.length() - 1) );
        
        return map;
    }
    
    private Map<String, String> getArchiveSpecificConfig(String moduleName) {
        Map<String, String> archiveCfgMap = new HashMap<String, String>();
        Properties archiveProps = new Properties();
        try {
            //Load special for archive properties(if exists)
            String archivePropsName = "/configs/" + currentUser.getArchiveId() + ".properties";
            URL resource = this.getClass().getResource(archivePropsName);
            if (resource != null) {
                archiveProps.load(this.getClass().getResourceAsStream(archivePropsName));
            }
            //Merge base and archive properties. Base props will be overwritten
            Enumeration e = archiveProps.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (!key.startsWith("config.modules") || key.startsWith("config.modules." + moduleName)) {
                    archiveCfgMap.put(key, archiveProps.getProperty(key));
                }
            }
        } catch (IOException e) {
            log.error("Error load config : " + e.getMessage());
        }
        return archiveCfgMap;
    }
    
    /**
     * No bean definition in dispatcher-servlet.xml since we'll be found
     * with the scan for (AT)Controller because of <context:component-scan
     * base-package="ru.spb.iac.archives.arm.server.web.controllers" />
     * 
     * @param userService
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param messageSource the messageSource to set
     */
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Returned value must be equal to request url of module controller
     * annotation @RequestMapping("/secure/module_name.htm")
     * @return 
     */
    abstract protected String getModuleName();

    abstract protected Map<String, String> getModuleConfig();

    /**
     * Getting start part of module configuration key. Should be user in child
     * controllers to customize module (in getModuleConfig method)
     * 
     * @return Start part of config key
     */
    protected String getCfgKeyStart() {
        return BASE_MODULES_CFG_PATH + "." + getModuleName() + ".";
    }

    /**
     * Get inner module facility(operation) as boolean
     * 
     * @param key Property name without base part
     * @return True if property exists and equals true, else false
     */
    protected Boolean getFacility(String key) {
        return Boolean.valueOf(facilitiesMap.get(getCfgKeyStart() + key));
    }
    
    /**
     * Add module property(key&value) to client configuration map
     * 
     * @param key Property name (without base common part)
     * @param value Property value
     */
    protected void setConfig(String key, String value){
        cfgMap.put( getCfgKeyStart() + key, value);
    }
}
