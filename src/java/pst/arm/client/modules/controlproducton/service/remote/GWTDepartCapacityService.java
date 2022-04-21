package pst.arm.client.modules.controlproducton.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Map;
import pst.arm.client.modules.controlproducton.domain.CompanyCapacity;
import pst.arm.client.modules.controlproducton.domain.ECompany;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@RemoteServiceRelativePath("service/departCapacityService")
public interface GWTDepartCapacityService extends RemoteService {

    public Map<ECompany, CompanyCapacity> getCompaniesCapacity(ECompany[] companies);
}
