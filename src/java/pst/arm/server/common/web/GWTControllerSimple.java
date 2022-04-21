package pst.arm.server.common.web;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


/**
 * Simple spring controller that merges GWT's {@link RemoteServiceServlet},
 * the {@link Controller} and also implements the {@link RemoteService}
 * interface so as to be able to directly delegate RPC calls to extending
 * classes.
 * 
 * @author Amigos Team
 * 
 */

public class GWTControllerSimple extends RemoteServiceServlet implements
ServletContextAware, Controller, RemoteService/*, GWTSerializer*/ {

    private static final Logger log = Logger.getLogger(GWTControllerSimple.class);
    private static final Logger statLog = Logger.getLogger("StatisticLogger");

    private static final long serialVersionUID = 5399966488983189122L;

    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try{
            //if( userService != null ){
            //    statLog.info( "SERVICE " + request.getRequestedSessionId() + " " + userService.getCurrentUser().getUserName() + " " + getClass().getSimpleName() + " "+request.getRemoteAddr());
            //}
            doPost(request, response);
            return null;
        }catch (Exception ex) {
            log.error("handleRequest error "+ex);
            //ex.printStackTrace();
            return null;
        }
    }

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }
    

}