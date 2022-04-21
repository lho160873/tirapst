

package pst.arm.server.common.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pst.arm.client.common.exception.InfrastructureException;

@Controller
@RequestMapping("/login.htm")
public class LoginController {

    private static Logger log = Logger.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelMap loginHandler(HttpServletRequest req,
            @RequestParam(value = "access_error", required = false) String access_error,
            @RequestParam(value = "login_error", required = false) String login_error, ModelMap map)
            throws InfrastructureException {

        AuthenticationException authExcept = (AuthenticationException) req.getSession().getAttribute(
                AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);

        // parameter may be on param line if we're redirect:ed here
        map.addAttribute("message", req.getParameter("message"));

        if (authExcept != null) {
            String message = authExcept.getMessage();
            log.info("Login Error " + message + " uname: "
                    + req.getParameter("j_username"));
            if (message.equals("Bad credentials")) {
                message = "Логин или пароль не подходят!";
            }
            map.addAttribute("login_error", message);
        }
        if (access_error != null) {
            map.addAttribute("login_error", "Доступ запрещен!");
        }

        return map;
    }
}
