package pst.arm.server.common.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.ui.ModelMap;
import pst.arm.client.common.domain.User;
import pst.arm.server.common.service.UserService;

/**
 * useful functionality for controllers.
 *
 * @author Sergey Smirnov
 *
 */
public class ControllerUtil {

    private static final Logger log = Logger.getLogger(ControllerUtil.class);

    /**
     *
     * @param req
     * @param userService
     * @return
     */
    public static Map<String, Object> getDefaultModel(
            HttpServletRequest req, UserService userService) {
        Map<String, Object> model = new HashMap<String, Object>();
        User su = null;
        model.put("message", req.getParameter("message"));
        try {
            su = userService.getCurrentUser();
            model.put("user", su);
        } catch (UsernameNotFoundException e) {
//            log.debug("No user logged in.");
        }

        if (req != null) {
            // IE < 7 check. used in common.ftl PNGImage
            String userAgent = req.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("MSIE") && !userAgent.contains("MSIE 7")) {
                model.put("iePre7", true);
            }
        }

        return model;
    }

    public static ModelMap getModelMap(HttpServletRequest req,
            UserService userService) {
        ModelMap rtn = new ModelMap();
        rtn.addAllAttributes(getDefaultModel(req, userService));
        return rtn;
    }

    public static void updateModelMapWithDefaults(ModelMap map,
            HttpServletRequest req, UserService userService) {
        map.addAllAttributes(getDefaultModel(req, userService));
    }
}
