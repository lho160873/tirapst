package pst.arm.client.modules.controlproducton.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Map;
import pst.arm.client.modules.controlproducton.domain.CompanyCapacity;
import pst.arm.client.modules.controlproducton.domain.ECompany;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface GWTDepartCapacityServiceAsync {

    public void getCompaniesCapacity(ECompany[] companies, AsyncCallback<Map<ECompany, CompanyCapacity>> asyncCallback);
}
