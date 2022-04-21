package pst.arm.client.common.domain;

import com.extjs.gxt.ui.client.data.BaseModel;
import java.io.Serializable;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class Company extends BaseModel implements Serializable {

    private Integer companyId;
    private String name;
    private String shortName;

    public Company() {
    }
    

    public Integer getCompanyId() {
        return companyId;
    }


    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
     public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    
    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
