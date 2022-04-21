package pst.arm.server.modules.controlproduction.capacity.dao;

import java.util.List;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface DepartCapacityDAO {

    public List<DepartCapacity> getDepartmentsCapacityList(Integer companyId);
}
