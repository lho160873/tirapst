package pst.arm.client.modules.sync1c.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class Worker extends Worker1C {

    private Integer postWorkerId;
    private Integer postId;
    private Integer departmentId;

    public Worker() {
    }

    public Worker(Worker1C worker1C) {
        copy(worker1C);
    }

    public void copy(Worker1C worker1C) {
        setQuantity(worker1C.getQuantity());
        setCombinationSign(worker1C.getCombinationSign());
        setPersonalNumber(worker1C.getPersonalNumber());
        setDatePost(worker1C.getDatePost());
        setDateIn(worker1C.getDateIn());
        setDateOut(worker1C.getDateOut());
        setUserId(worker1C.getUserId());
        setIdent(worker1C.getIdent());
        setName(worker1C.getName());
        setIdentfiz(worker1C.getIdentfiz());
        setReason(worker1C.getReason());
        setBirthday(worker1C.getBirthday());
    }

    /**
     * @return the postWorkerId
     */
    public Integer getPostWorkerId() {
        return postWorkerId;
    }

    /**
     * @param postWorkerId the postWorkerId to set
     */
    public void setPostWorkerId(Integer postWorkerId) {
        this.postWorkerId = postWorkerId;
    }

    /**
     * @return the postId
     */
    public Integer getPostId() {
        return postId;
    }

    /**
     * @param postId the postId to set
     */
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    /**
     * @return the departmentId
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Замещение должностей: " + super.toString();
    }
}
