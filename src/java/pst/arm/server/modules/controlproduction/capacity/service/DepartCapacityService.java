package pst.arm.server.modules.controlproduction.capacity.service;

import java.util.List;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface DepartCapacityService {

    public List<DepartCapacity> getDepartmentsCapacityList(Integer companyId);

}
