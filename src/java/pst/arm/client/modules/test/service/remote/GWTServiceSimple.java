package pst.arm.client.modules.test.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.BusinessException;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/gwtservicesimple")
public interface GWTServiceSimple extends RemoteService {

    public String myMethod(String s) throws BusinessException;
    public String myMethod_1(String s);
    public List<String> myMethodGetAllUsers();

}
