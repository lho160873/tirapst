package pst.arm.server.modules.test.service.gwt;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.BusinessException;
import pst.arm.client.modules.test.service.remote.GWTServiceSimple;
import pst.arm.server.common.web.GWTControllerSimple;


@Service("GWTServiceSimple")
public class GWTServiceSimpleImpl extends GWTControllerSimple implements GWTServiceSimple{
    private static Logger logger=Logger.getLogger("GWTServiceSimpleImpl");
    //public static BeanFactory CONTEXT;
    //private StaticContextHolder context;

     @Override
    public String myMethod_1(String s) 
    {
       
       return "";
     }
    
    @Override
    public String myMethod(String s) throws BusinessException {
       
      /* Map<String, IRowColumnVal> map = new HashMap<String,IRowColumnVal>();
        
       IRowColumnVal lng = new DRowColumnVal<Integer>();
       lng.setValFromString(s);

       IRowColumnVal str = new DRowColumnVal<String>();
       str.setValFromString(s);

       map.put("1",lng);
       map.put("2",str);
       return map.get("1").getVal().toString()+" "+map.get("2").getVal().toString() ;*/
        return s;
       //return "";
        /*
        logger.warn("GWTServiceSimpleImpl::myMethod");
        // Do something interesting with 's' here on the server.
        String name = "error";
        try {
            ClBaseData table = ClDataMapManager.getInstance().getData(s);
        } catch (BeansException e) {
            logger.warn(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
        return ClDataMapManager.getInstance().getData(s).getTableName();*/
     
    }
    
    @Override
    public List<String> myMethodGetAllUsers( ) {
        logger.warn("GWTServiceSimpleImpl::myMethodGetAllUsers");
        // Do something interesting with 's' here on the server.
         //TestService testService = new TestServiceImpl();
        //List<String> temp =  this.testService.getAllUsers();
        return new ArrayList<String>() ;
    }
 
   
   




    
}
