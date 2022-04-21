package pst.arm.server.common;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.service.UserService;

/**
 *
 * @author Artem Vorontsov
 * @since 0.14.1
 */
public class ConfigurationManager {

    public static String CONFIG_SECTION = "config";
    public static String MODULES_SECTION = "config.modules";
    private HashMap<String, String> params;
    private User currentUser;
    private Logger log;
    private UserService userService;
    private volatile static ConfigurationManager instance = null;

    private ConfigurationManager() {
        log = Logger.getLogger("ConfigurationManager");
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }

        return instance;
    }

    public String getProperty(String module, String property) {
        initParams();
        if (params != null) {
            return params.get(MODULES_SECTION + "." + module + "." + property);
        }
        return null;
    }

    private void initParams() {
        if (currentUser == null) {
            currentUser = userService.getCurrentUser();
        }
        if (params == null) {
            params = new HashMap<String, String>();
            Properties archiveProps = new Properties();
            try {
                Properties baseProps = new Properties();

                //Load base properties
                baseProps.load(this.getClass().getResourceAsStream("/configs/base.properties"));
                Enumeration e = baseProps.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    params.put(key, baseProps.getProperty(key));
                }
                String archivePropsName = "/configs/" + currentUser.getArchiveId() + ".properties";
                URL resource = this.getClass().getResource(archivePropsName);
                if (resource != null) {
                    archiveProps.load(this.getClass().getResourceAsStream(archivePropsName));
                }

                e = archiveProps.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    params.put(key, archiveProps.getProperty(key));
                }
            } catch (IOException e) {
                log.error("Error load config : " + e.getMessage());
            }
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Integer getArchiveId() {
        return getUserService().getCurrentUser().getArchiveId();
    }
    
    public Date getFixedDate(Date d) {
        return new Date(d.getTime() - (d.getTimezoneOffset() * 60000));
    }
    
    public String formatDateOnly(Date d) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(getFixedDate(d));
    }

}

/*


public class MySingleton {

private static MySingleton INSTANCE = null;
private MySingleton(){}


public static MySingleton getInstance(){
if(INSTANCE == null){
synchronized(MySingleton.class){
if(INSTANCE == null){
try{
doWork()
}catch(Exception e){
throw new IllegalStateException("xyz", e);
}
INSTANCE = new MySingleton();
}
}
}

return INSTANCE;
}

private static void doWork() {
// do some work
}

}






 */
