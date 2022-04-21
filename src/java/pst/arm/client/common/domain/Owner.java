package pst.arm.client.common.domain;

import java.io.Serializable;

/**
 * Abstract owner
 * @author Alexandr Kozhin
 * @since 0.11.0
 */
public class Owner implements Serializable{
    private Integer archiveId;
    private Integer id;
    private User user;

    public Owner() {
    }

    public Owner(User user) {
        this.user = user;
    }

    
    public Owner(Integer archiveId, Integer id) {
        this.archiveId = archiveId;
        this.id = id;
    }

    /**
     * @return the archiveId
     */
    public Integer getArchiveId() {
        return archiveId;
    }

    /**
     * @param archiveId the archiveId to set
     */
    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        return ( obj != null && obj instanceof Owner &&
                ((Owner)obj).getArchiveId().equals(this.getArchiveId()) &&
                ((Owner)obj).getId().equals(this.getId()) );
    }
    

    
}
