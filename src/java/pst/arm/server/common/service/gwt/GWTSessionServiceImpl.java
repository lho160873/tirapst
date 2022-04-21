package pst.arm.server.common.service.gwt;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import pst.arm.client.common.service.remote.GWTSessionService;
import pst.arm.server.common.web.GWTController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service("GWTSessionService")
public class GWTSessionServiceImpl extends GWTController implements GWTSessionService {

    @Override
    public Boolean addInSession(String key, String value) {
        HttpServletRequest request = this.getThreadLocalRequest();        
        HttpSession session = request.getSession();
        
        if (session.getAttribute(key) == null) {
            session.setAttribute(key, value);
        }
       
        return true;
    }

    @Override
    public String getFromSession(String key) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        if (session.getAttribute(key) != null) {
            return (String) session.getAttribute(key);
        } else {
            return null;
        }
    }
    
   
}
