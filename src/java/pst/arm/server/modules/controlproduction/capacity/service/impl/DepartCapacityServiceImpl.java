package pst.arm.server.modules.controlproduction.capacity.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;
import pst.arm.server.modules.controlproduction.capacity.dao.DepartCapacityDAO;
import pst.arm.server.modules.controlproduction.capacity.service.DepartCapacityService;

@Service
public class DepartCapacityServiceImpl implements DepartCapacityService {

    private DepartCapacityDAO dao;

    @Override
    public List<DepartCapacity> getDepartmentsCapacityList(Integer companyId) {
        List<DepartCapacity> ntoCapacityList = dao.getDepartmentsCapacityList(companyId);
        for (DepartCapacity nto : ntoCapacityList) {
            nto.setCapacity((nto.getPlan() + nto.getAdditional()) / nto.getMonthResource());
        }
        return ntoCapacityList;
    }

    /**
     * @return the dao
     */
    public DepartCapacityDAO getDao() {
        return dao;
    }

    @Autowired
    public void setDao(DepartCapacityDAO dao) {
        this.dao = dao;
    }

}
