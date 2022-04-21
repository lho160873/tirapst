package pst.arm.server.common.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by vikto on 23.05.2016.
 */
public class GWTCacheControlFilter implements Filter {
    public void destroy() {
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
      
        if (requestURI.contains(".nocache.")) {
            Date now = new Date();
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setDateHeader("Date", now.getTime());
            // one day old
            httpResponse.setDateHeader("Expires", now.getTime() - 86400000L);
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
        }
        //add LKHorosheva
        /*if (requestURI.contains("api")) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, my-cool-header");
            httpResponse.addHeader("Access-Control-Max-Age", "60"); // seconds to cache preflight request --> less OPTIONS traffic
            httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
            
         }*/
        
            
        if (httpRequest.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(httpRequest.getMethod())) {
            // CORS "pre-flight" request
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
            httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
            httpResponse.addHeader("Access-Control-Max-Age", "1800");//30 min
        }
        
        filterChain.doFilter(request, response);
        
        
    }
}
