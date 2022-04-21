package pst.arm.client.modules.controlproducton.domain;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class DepartCapacity implements Serializable {

    private String departCode;
    private Long departId;
    private Double capacity;
    private Double plan;
    private Double additional;
    private Double monthResource;

    public DepartCapacity() {
    }

    /**
     * @return the departId
     */
    public Long getDepartId() {
        return departId;
    }

    /**
     * @param departId the departId to set
     */
    public void setDepartId(Long departId) {
        this.departId = departId;
    }

    /**
     * @return the capacity
     */
    public Double getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the plan
     */
    public Double getPlan() {
        return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(Double plan) {
        this.plan = plan;
    }

    /**
     * @return the additional
     */
    public Double getAdditional() {
        return additional;
    }

    /**
     * @param additional the additional to set
     */
    public void setAdditional(Double additional) {
        this.additional = additional;
    }

    /**
     * @return the monthResource
     */
    public Double getMonthResource() {
        return monthResource;
    }

    /**
     * @param monthResource the monthResource to set
     */
    public void setMonthResource(Double monthResource) {
        this.monthResource = monthResource;
    }

    /**
     * @return the departCode
     */
    public String getDepartCode() {
        return departCode;
    }

    /**
     * @param departCode the departCode to set
     */
    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

}
