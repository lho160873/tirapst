package pst.arm.client.modules.sync1c.domain;

import java.util.Date;

/**
 * Created by akozhin on 30.04.15.
 */
public class Worker1C extends Object1C {

    private Integer workerId;
    private String name;
    private String personalNumber;  //PERS_NUMBER	worker code
    private Integer companyId;      //COMPANY_ID	codeorg
    private Date dateIn;            //DATE_IN	datein
    private Date datePost;          //DATE_POST	datepost
    private Date dateOut;           //DATE_OUT	dateout
    private String postCode;        //PMASC_POST_CODE	post
    private String departmentCode;  //PMASC_DEPART_CODE	depart
    private Double quantity;       //QUANTITY	rate
    private Integer combinationSign;//COMBINATION_SIGN	coop
    private Integer sex;            //GENDER	sex
    private Integer isFolder;       //IS_FOLDER	is_folder
    private String ident;           //IDENT_1C	ident
    private Long userId;            //USER_ID	USER_ID
    private Date modificationDate;  //DATE_CORR
    private String personalCode;    //CODE_FIZ	codefiz
    private Integer interactingSysId;//INTERACTING_SYST_ID
    private String inn;             //INN
    private String identfiz;        //identfiz
    private Date birthday;          //BIRTHDAY
    private String reason;          //reason

    //Rel
    private Department1C department;
    private Post1C post;

    public Worker1C() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Worker1C worker1C = (Worker1C) o;

        if (name != null ? !name.equalsIgnoreCase(worker1C.name) : worker1C.name != null) {
            return false;
        }
        if (personalNumber != null ? !personalNumber.equalsIgnoreCase(worker1C.personalNumber) : worker1C.personalNumber != null) {
            return false;
        }
        if (companyId != null ? !companyId.equals(worker1C.companyId) : worker1C.companyId != null) {
            return false;
        }
        if (dateOut != null ? worker1C.dateOut == null || dateOut.getTime() != worker1C.dateOut.getTime() : worker1C.dateOut != null) {
            return false;
        }

        if (dateIn != null ? worker1C.dateIn == null || dateIn.getTime() != worker1C.dateIn.getTime() : worker1C.dateIn != null) {
            return false;
        }

        if (datePost != null ? worker1C.datePost == null || datePost.getTime() != worker1C.datePost.getTime() : worker1C.datePost != null) {
            return false;
        }
        if (postCode != null ? !postCode.equalsIgnoreCase(worker1C.postCode) : worker1C.postCode != null) {
            return false;
        }
        if (departmentCode != null ? !departmentCode.equalsIgnoreCase(worker1C.departmentCode) : worker1C.departmentCode != null) {
            return false;
        }
        if (quantity != null ? !quantity.equals(worker1C.quantity) : worker1C.quantity != null) {
            return false;
        }
        if (sex != null ? !sex.equals(worker1C.sex) : worker1C.sex != null) {
            return false;
        }
        if (combinationSign != null ? !combinationSign.equals(worker1C.combinationSign) : worker1C.combinationSign != null) {
            return false;
        }
        if (isFolder != null ? !isFolder.equals(worker1C.isFolder) : worker1C.isFolder != null) {
            return false;
        }
        if (personalCode != null ? !personalCode.equalsIgnoreCase(worker1C.personalCode) : worker1C.personalCode != null) {
            return false;
        }
        if (inn != null ? !inn.equalsIgnoreCase(worker1C.inn) : worker1C.inn != null) {
            return false;
        }
        if (identfiz != null ? !identfiz.equalsIgnoreCase(worker1C.identfiz) : worker1C.identfiz != null) {
            return false;
        }
        if (birthday != null ? worker1C.birthday == null || birthday.getTime() != worker1C.birthday.getTime() : worker1C.birthday != null) {
            return false;
        }
        if (reason != null ? !reason.equalsIgnoreCase(worker1C.reason) : worker1C.reason != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (personalNumber != null ? personalNumber.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (dateIn != null ? dateIn.hashCode() : 0);
        result = 31 * result + (datePost != null ? datePost.hashCode() : 0);
        result = 31 * result + (dateOut != null ? dateOut.hashCode() : 0);
        result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
        result = 31 * result + (departmentCode != null ? departmentCode.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (combinationSign != null ? combinationSign.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (isFolder != null ? isFolder.hashCode() : 0);
        result = 31 * result + (personalCode != null ? personalCode.hashCode() : 0);
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        result = 31 * result + (identfiz != null ? identfiz.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (ident != null) {
            return name + " (" + ident + ")";
        }

        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getCombinationSign() {
        return combinationSign;
    }

    public void setCombinationSign(Integer combinationSign) {
        this.combinationSign = combinationSign;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Integer isFolder) {
        this.isFolder = isFolder;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public Integer getInteractingSysId() {
        return interactingSysId;
    }

    public void setInteractingSysId(Integer interactingSysId) {
        this.interactingSysId = interactingSysId;
    }

    public String getIdentfiz() {
        return identfiz;
    }

    public void setIdentfiz(String identfiz) {
        this.identfiz = identfiz;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the inn
     */
    public String getInn() {
        return inn;
    }

    /**
     * @param inn the inn to set
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    public Department1C getDepartment() {
        return department;
    }

    public Post1C getPost() {
        return post;
    }

    public void setDepartment(Department1C department) {
        this.department = department;
    }

    public void setPost(Post1C post) {
        this.post = post;
    }

    @Override
    public Integer getId() {
        return getWorkerId();
    }

    @Override
    public String getText() {
        return toString();
    }
}
