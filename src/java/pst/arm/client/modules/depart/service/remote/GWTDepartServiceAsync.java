package pst.arm.client.modules.depart.service.remote;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.modules.depart.domain.Depart;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTDepartServiceAsync  {

    public void getDepart(AsyncCallback<List<Depart>> callback);

}
