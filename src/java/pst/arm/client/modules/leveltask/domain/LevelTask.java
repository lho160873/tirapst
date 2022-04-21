package pst.arm.client.modules.leveltask.domain;

import pst.arm.client.common.domain.EditableDomain;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class LevelTask implements Serializable, EditableDomain<LevelTask> {

    private Integer id = null;  //код
    private Date createDate;
    private Date replyDate;
    private Integer replyNumber;
    private String reply;
    private Date eventTime;
    private Integer userIdFrom = null;
    private Integer userIdTo = null;
    private Integer taskId = 0;
    private String taskName;
    private Integer taskState = null;
    private String stateName;
    private String userNameTo;
    private String userNameFrom;
    private String description;
    private String descriptionShort;
    private Integer priority = null;
    private String dd;
    private String ll;
    private Integer currentId = null;  //код записи связанной с задачей
    private String module;
    private Integer sendSign = null;

    public Integer getSendSign() {
        return sendSign;
    }

    public void setSendSign(Integer sendSign) {
        this.sendSign = sendSign;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

     public void setCurrentId(Integer id) {
        this.currentId = id;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setModule( String m ) {
        this.module = m;
    }

    public String getModule() {
        return module;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setReplyDate(Date date) {
        this.replyDate = date;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setEventTime(Date time) {
        this.eventTime = time;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setUserIdFrom(Integer userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public Integer getUserIdFrom() {
        return userIdFrom;
    }
    
     public void setPriority(Integer prt) {
        this.priority = prt;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setUserIdTo(Integer userIdTo) {
        this.userIdTo = userIdTo;
    }

    public Integer getUserIdTo() {
        return userIdTo;
    }
    public void setTaskId(Integer id) {
        this.taskId = id;
    }

    public Integer getTaskId() {
        return taskId;
    }
    
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }
    
    public void setUserNameTo(String name) {
        this.userNameTo = name;
    }

    public String getUserNameTo() {
        return userNameTo;
    }
    
    public void setReplyNumber(Integer number) {
        this.replyNumber = number;
    }

    public Integer getReplyNumber() {
        return replyNumber;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }
    
    public void setUserNameFrom(String name) {
        this.userNameFrom = name;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }
    
   public void setDd(String d) {
        this.dd = d;
    }

    public String getDd() {
        return dd;
    }
    public void setLl(String l) {
        this.ll = l;
    }

    public String getLl() {
        return ll;
    }
    

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescriptionShort(String desc) {
        this.descriptionShort = desc;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }
    
    public void setTaskState(Integer id) {
        this.taskState = id;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public void setStateName(String name) {
        this.stateName = name;
    }

    public String getStateName() {
        return stateName;
    }
    
    @Override
    public LevelTask newInstance() {
         return new LevelTask();
    }

    @Override
    public void copy(LevelTask domain) {
        this.id = domain.id;
        this.taskId = domain.taskId;
        this.taskName = domain.taskName;
        this.taskState = domain.taskState;
        this.stateName = domain.stateName;
        this.userNameFrom = domain.userNameFrom;
        this.userNameTo = domain.userNameTo;              
        this.userIdFrom = domain.userIdFrom;
        this.userIdTo = domain.userIdTo;
        this.description = domain.description;
        this.priority = domain.priority;
        this.ll = domain.ll;
        this.dd = domain.dd;
        this.createDate = domain.createDate;
        this.replyDate = domain.replyDate;
        this.eventTime = domain.eventTime;
        this.reply = domain.reply;
        this.replyNumber = domain.replyNumber;
        this.sendSign = domain.sendSign;
    }

    @Override
    public Boolean isDomainFull() {
       return true;
    }

    @Override
    public Long getDomainId() {
        return new Long(getId());
    }

    @Override
    public Boolean isDomainEquals(LevelTask domain) {
        if (domain == null) {
            return false;
        }
        return true;
        /*return (DataUtil.compare(this.taskId, domain.taskId)
                //&&DataUtil.compare(this.id, domain.id)               
                //&& DataUtil.compare(this.userIdFrom, domain.userIdFrom)
                //&& DataUtil.compare(this.userIdTo, domain.userIdTo)
                //&& DataUtil.compare(this.taskState, domain.taskState)
                && DataUtil.compare(this.description, domain.description)
                //&& DataUtil.compare(this.priority, domain.priority)
                //&& DataUtil.compare(this.replyDate, domain.replyDate)
                //&& DataUtil.compare(this.eventTime, domain.eventTime)
                && DataUtil.compare(this.replyNumber, domain.replyNumber)
                && DataUtil.compare(this.reply, domain.reply));
                //&& DataUtil.compare(this.createDate, domain.createDate));
    */}


}
            
    

