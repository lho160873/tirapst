package pst.arm.client.modules.admin.stat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kozhin
 */
public class StatItem implements Serializable {

    private String sessionId = null;
    private String login = null;
    private String ip = null;
    private Date startDate, endDate;
    private List<StatAction> actions = null;

    public StatItem() {
        actions = new ArrayList<StatAction>();
    }

    public StatItem(String sessionId, String login, String ip) {
        this.sessionId = sessionId;
        this.login = login;
        this.ip = ip;
        actions = new ArrayList<StatAction>();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (StatAction statAction : actions) {
            str.append(statAction.getStrDate());
            str.append("\t");
            str.append(statAction.getType());
            str.append("\t");
            str.append(sessionId);
            str.append(" ");
            str.append(login);
            str.append("\t");
            str.append(statAction.getName());
            str.append(" ");
            str.append(ip);
            str.append("\t");
            if (statAction.getUrl() != null) {
                str.append(statAction.getFullUrl());
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the actions
     */
    public List<StatAction> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(List<StatAction> actions) {
        this.actions = actions;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
