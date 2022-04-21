package pst.arm.server.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller handles 404, 500 server errors.
 * In case of error it displays message for user.
 * 
 * @author ssmirnov
 */
@Controller
@RequestMapping("/servletErrorView.htm")
public class ErrorController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelMap errorHandler(HttpServletRequest req,
            HttpServletResponse arg1) throws Exception {
        ModelMap m = new ModelMap();
        String code = req.getParameter("code");
        if (code != null) {
            Object uri = req.getAttribute("javax.servlet.error.request_ uri");
            m.addAttribute("message", code + " error for page " + uri);
        }
        return m;
    }
}
