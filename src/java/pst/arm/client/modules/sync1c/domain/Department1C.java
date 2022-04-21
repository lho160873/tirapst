package pst.arm.client.modules.sync1c.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by akozhin on 09.05.15.
 */
public class Department1C extends Object1C {

    private Integer departmentId;   //DEPART_1C_ID
    private String code;            //CODE
    private String name;            //NAME
    private Integer companyId;      //COMPANY_ID
    private Long userId;         //USER_ID
    private Date modificationDate;  //DATE_CORR
    private Integer enabled;        //ENABLED
    private String pmascCode;       //PMASC_DEPART_CODE
    private Department1C parent;    //PARENT_DEPART_CODE
    private List<Department1C> departments;

    public Department1C() {
    }

    @Override
    public String toString() {
        return departmentId != null ? String.format("%s(#%d, %s)", name, departmentId, code) : String.format("%s(%s)", name, code);
    }

    public Department1C(Department1C parent) {
        this.parent = parent;
    }

    public boolean hasDepartments() {
        return departments != null && !departments.isEmpty();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getPmascCode() {
        return pmascCode;
    }

    public void setPmascCode(String pmascCode) {
        this.pmascCode = pmascCode;
    }

    public Department1C getParent() {
        return parent;
    }

    public void setParent(Department1C parent) {
        this.parent = parent;
    }

    public List<Department1C> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department1C> departments) {
        this.departments = departments;
    }

    public void addChild(Department1C child) {
        if (departments == null) {
            departments = new ArrayList<Department1C>();
        }
        departments.add(child);
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public Integer getId() {
        return getDepartmentId();
    }

    @Override
    public String getText() {
        return toString();
    }

}
