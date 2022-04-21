package pst.arm.client.modules.sync1c.domain;

/**
 * Created by akozhin on 28.04.15.
 */
public class Post1C extends Object1C {

    private Long userId;
    private Integer postId;
    private String code;
    private String pmascCode;
    private String name;
    private Long interactingSysId;

    public Post1C() {
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

    public String getPmascCode() {
        return pmascCode;
    }

    public void setPmascCode(String pmascCode) {
        this.pmascCode = pmascCode;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInteractingSysId() {
        return interactingSysId;
    }

    public void setInteractingSysId(Long interactingSysId) {
        this.interactingSysId = interactingSysId;
    }

    @Override
    public String toString() {
        return name + "(" + pmascCode + ")";
    }

    @Override
    public Integer getId() {
        return getPostId();
    }

    @Override
    public String getText() {
        return toString();
    }

}
