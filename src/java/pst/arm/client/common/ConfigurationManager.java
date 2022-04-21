package pst.arm.client.common;

import com.google.gwt.i18n.client.Dictionary;

/**
 * ConfigurationManager is a class for manage client settings
 * 
 * @author Alexandr Kozhin
 * @since 0.10.0
 */
public class ConfigurationManager {

    public static String CONFIG_SECTION = "config";
    public static String MODULES_SECTION = "config.modules";

    /**
     * Check module availability by name
     * @param module Module name
     * @return True if module available
     */
    public static Boolean isModuleAvailable(String module) {
        Dictionary dictionary = Dictionary.getDictionary("Vars");
        String modules = dictionary.get(MODULES_SECTION);
        if (modules != null) {
            String[] split = modules.split(",");
            for (String m : split) {
                if (m.equalsIgnoreCase(module))
                {
                    return Boolean.TRUE;
                }
            }
        }       
        return Boolean.FALSE;
    }

    /**
     * Get property of current module from client configuration
     * 
     * @param property Property name (key)
     * @return Property value for module
     */
    public static String getProperty(String property) {
        Dictionary dictionary = Dictionary.getDictionary("Vars");
        try {
            String module = getModuleName();
            if (module != null) {
                return dictionary.get(MODULES_SECTION + "." + module + "." + property);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Get property of current module from client configuration in boolean
     * 
     * @param property Property name (key)
     * @return True if property exists and equals true
     */
    public static Boolean getPropertyAsBoolean(String property) {
        return Boolean.valueOf(getProperty(property));
    }
    
    /**
     * Get global property from client configuration
     * 
     * @param property Property name (key)
     * @return Global property value
     */
    public static String getGlobalProperty(String property) {
        try {
            Dictionary dictionary = Dictionary.getDictionary("Vars");
            return dictionary.get(CONFIG_SECTION + "." + property);
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean isMultiArchive() {
        return Boolean.valueOf(getGlobalProperty("global.multiArchiveSearch"));
    }

    /**
     * 
     * @return true 
     */
    public static Boolean isModuleEditor() {
        return Boolean.valueOf(getProperty("edit"))
                || Boolean.valueOf(getProperty("editAll"))
                || Boolean.valueOf(getProperty("approve"));
    }

    public static Integer getArchiveId() {
        if (isMultiArchive()) {
            return 0;
        }
        return Integer.valueOf(getGlobalProperty("currgetQueryStringentArchiveId"));
    }

    public static Long getUserId() {
        return Long.valueOf(getGlobalProperty("currentUserId"));
    }
    
    public static String getUserName() {
        return String.valueOf(getGlobalProperty("currentUserName"));
    }
    
     public static String getWorkerName() {
        return String.valueOf(getGlobalProperty("currentWorkerName"));
    }

    public static String getModuleName() {
        Dictionary dictionary = Dictionary.getDictionary("Vars");
        return dictionary.get("currentModule");
    }
    

}