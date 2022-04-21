package pst.arm.server.modules.controlproduction.capacity.service.gwt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.controlproducton.domain.CompanyCapacity;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;
import pst.arm.client.modules.controlproducton.domain.ECompany;
import pst.arm.client.modules.controlproducton.service.remote.GWTDepartCapacityService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.controlproduction.capacity.service.DepartCapacityService;

@Service("GWTDepartCapacityService")
public class GWTDepartCapacityServiceImpl extends GWTController implements GWTDepartCapacityService {

    private DepartCapacityService service;

    @Override
    public Map<ECompany, CompanyCapacity> getCompaniesCapacity(ECompany[] companies) {
        Map<ECompany, CompanyCapacity> result = new HashMap<ECompany, CompanyCapacity>();
        for (ECompany company : companies) {
            List<DepartCapacity> departmentsCapacityList = service.getDepartmentsCapacityList(company.getId());
            result.put(company, new CompanyCapacity(company, departmentsCapacityList));
        }
        return result;
    }

    /**
     * @return the service
     */
    public DepartCapacityService getService() {
        return service;
    }

    @Autowired
    public void setService(DepartCapacityService service) {
        this.service = service;
    }

}
