package pst.arm.client.modules.admin.domain;

import pst.arm.client.common.domain.search.SearchCondition;

/**
 *
 * @author maslov
 */
public class UserSearchCondition extends SearchCondition {
    private String fio;
    private String login;
    private Boolean searchNotActive = Boolean.FALSE;

    /**
     * @return the fio
     */
    public String getFio() {
        return fio;
    }

    /**
     * @param fio the fio to set
     */
    public void setFio(String fio) {
        this.fio = fio;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the searchNotActive
     */
    public Boolean getSearchNotActive() {
        return searchNotActive;
    }

    /**
     * @param searchNotActive the searchNotActive to set
     */
    public void setSearchNotActive(Boolean searchNotActive) {
        this.searchNotActive = searchNotActive;
    }
}
