package pst.arm.client.common.domain;

import com.extjs.gxt.ui.client.data.BaseModel;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author maslov
 */
public class Role extends BaseModel implements Serializable {

    private Long roleId;
    private String roleName;
    private String description;
    private List<Facility> fasilities;
    // поля для взаимодейсвия с GUI вкладки "администрорование->роли"
    private Boolean delete;
    private Boolean add;

    public Role() {
    }

    public Role(Long roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    /**
     * @return the roleId
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fasilities
     */
    public List<Facility> getFasilities() {
        return fasilities;
    }

    /**
     * @param fasilities the fasilities to set
     */
    public void setFasilities(List<Facility> fasilities) {
        this.fasilities = fasilities;
    }

    /**
     * @return the delete
     */
    public Boolean getDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    /**
     * @return the add
     */
    public Boolean getAdd() {
        return add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(Boolean add) {
        this.add = add;
    }
}
