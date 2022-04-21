package pst.arm.client.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;

/**
 *
 * @author Alexandr Kozhin
 */
public class User implements Serializable {

    protected Long id;
    protected Integer archiveId;
    protected String userLogin;
    protected String password;
    protected String securityData;
    protected Date dateCreated;
    protected String userName;
    protected String description;
    protected int enabled;
    protected Boolean deleted = Boolean.FALSE;
    protected List<Role> roles;
    protected Integer workerId;
    protected String workerName;
    protected List<Company> companies;
 
    /** default constructor */
    public User() {
    }

    /** full constructor */
    public User(String user_login, String password) {
        this.userLogin = user_login;
        this.password = password;
//        this.user_type_id = type;
    }

    public String getLogin() {
        if (getFioShort() != null) {
            return getFioShort();
        }
        return userLogin;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountDisabled() {
        if (enabled == 0) {
            return true;
        }

        return false;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return the user_login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param user_login
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the userName
     */
    public void setUserName(String name) {
        this.userName = name;
    }

    /**
     * @return the user_id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the date_created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDateCreatedStr() {
        return AppHelper.getInstance().shortDateFormat(getDateCreated());
    }

    /**
     * @param date_created the date_created to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
  
    /**
     * @return the enabled
     */
    public int getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enabled = 1;
        } else {
            this.enabled = 0;
        }
    }

    public String getFioShort() {
        /*String fioShort = lastName;
        if (firstName != null) {
            fioShort += ' ' + firstName.substring(0, 1) + '.';
            if (patronymic != null) {
                fioShort += patronymic.substring(0, 1) + '.';
            }
        }
        return fioShort;*/
        return userName;
    }

    public String getFioShortOrLogin() {
        String ret = getFioShort();
        if ((ret == null) || (ret.isEmpty())) {
            ret = userLogin;
        }
        return ret;
    }

    public String getFio() {
        return userName;
        /*String ret = "";
        if ((getLastName() != null) && (!getLastName().isEmpty())) {
            ret += getLastName();
        }

        if ((getFirstName() != null) && (!getFirstName().isEmpty())) {
            if (!ret.isEmpty()) {
                ret += " ";
            }
            ret += getFirstName();
        }

        if ((getPatronymic() != null) && (!getPatronymic().isEmpty())) {
            if (!ret.isEmpty()) {
                ret += " ";
            }
            ret += getPatronymic();
        }
        return ret;*/
    }

    /**
     * @return the facilities
     */
    public List<String> getFacilities() {
        List facilities = new ArrayList<String>();
        if (roles != null) {
            for (Role role : roles) {
                if (role.getFasilities() != null) {
                    for (Facility facility : role.getFasilities()) {
                        String type = (String) facility.getType();
                        if (type.equalsIgnoreCase("MODULE")) {
                            facilities.add(facility.getModule());
                        } else if (type.equalsIgnoreCase("MODULE_OPERATION")) {
                            facilities.add(facility.getModule() + "." + facility.getName());
                        }
                    }
                }
            }
        }

        return facilities;
    }

    public Map<String, String> getUserCredentials(String module) {
        Map<String, String> map = new HashMap<String, String>();
        StringBuilder modules = new StringBuilder();
        if (roles != null) {
            for (Role role : roles) {
                if (role.getFasilities() != null) {
                    for (Facility facility : role.getFasilities()) {
                        String type = (String) facility.getType();
                        if (type.equalsIgnoreCase("MODULE")) {
                            modules.append(facility.getModule()).append(",");
                        } else if (type.equalsIgnoreCase("MODULE_OPERATION") && facility.getModule().equalsIgnoreCase(module)) {
                            map.put("config.modules." + module + "." + facility.getName(), "true");
                        }
                    }
                }
            }
        }

        if (modules.length() > 0) {
            map.put("config.modules", modules.substring(0, modules.length() - 1).toLowerCase());
        }
        return map;
    }

    public boolean isAdmin() {
        if (roles != null) {
            for (Role role : roles) {
                if (role.getRoleName().equalsIgnoreCase("ROLE_ADMIN")) {
                    return true;
                }
            }
        }

        return false;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }
    
    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }
    
    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
    
    public String getSecurityData() {
        return securityData;
    }

    public void setSecurityData(String securityData) {
        this.securityData = securityData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the deleted
     */
    public Boolean getDeleted() {
        return isDeleted();
    }

    public Boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


}
