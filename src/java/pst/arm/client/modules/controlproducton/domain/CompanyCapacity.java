package pst.arm.client.modules.controlproducton.domain;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class CompanyCapacity implements Serializable {

    private ECompany company;
    private List<DepartCapacity> departmentsCapacityList;
    private Double avg = 0d;

    public CompanyCapacity() {
    }

    public CompanyCapacity(List<DepartCapacity> departmentsCapacityLust) {
        this.departmentsCapacityList = departmentsCapacityLust;
    }

    public CompanyCapacity(ECompany company, List<DepartCapacity> departmentsCapacityLust) {
        this.company = company;
        this.departmentsCapacityList = departmentsCapacityLust;
    }

    public Double avg() {
        calculateAvg();
        return avg;

    }

    /**
     * @return the departmentsCapacityLust
     */
    public List<DepartCapacity> getDepartmentsCapacityList() {
        return departmentsCapacityList;
    }

    /**
     * @param departmentsCapacityLust the departmentsCapacityLust to set
     */
    public void setDepartmentsCapacityLust(List<DepartCapacity> departmentsCapacityList) {
        this.departmentsCapacityList = departmentsCapacityList;
    }

    public ECompany getCompany() {
        return company;
    }

    public void setCompany(ECompany company) {
        this.company = company;
    }

    private void calculateAvg() {
        if (departmentsCapacityList.isEmpty()) {
            return;
        }
        Double capacity = 0.;
        for (DepartCapacity nto : departmentsCapacityList) {
            capacity += nto.getCapacity();
        }
        avg = capacity / departmentsCapacityList.size();
    }

}
