package pst.arm.client.modules.depart.domain;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class Depart  implements Serializable {

    private Integer departId = null;  //Код подразделения
    private Integer parentDepartId = null;  //Код старшего подразделения
    private String  code; //Код подразделения
    private String  name; //Название подразделения
    
    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public Integer getDepartId() {
        return departId;
    }
    
    public void setParentDepartId(Integer parentDepartId) {
        this.parentDepartId = parentDepartId;
    }

    public Integer getParentDepartId() {
        return parentDepartId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public void setName(String  name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
  