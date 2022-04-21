package pst.arm.client.modules.ganttchart.domain;

import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author nikita
 */
public class Order implements Serializable, EditableDomain<Order> {

    private Integer id;
//    private String contract;
//    private String numDoc;
    private Date dateStart;
    private Date dateEnd;
//    private String companyName;
//    private Double sumDoc;
    private String orderName;
    private String departName;
    private Integer typeExec;
//    private String info;
//    private Integer urgency;
    private Double quantityPlan;
    private Double cost;
    private Integer isInternal;
    private Integer isComplete;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("id=");
        sb.append(this.getId());
//        sb.append(" contract=");
//        sb.append(this.getContract());
//        sb.append(" numDoc=");
//        sb.append(this.getNumDoc());
        sb.append(" dateStart=");
        sb.append(this.getDateStart());
        sb.append(" dateEnd=");
        sb.append(this.getDateEnd());
//        sb.append(" companyName=");
//        sb.append(this.getCompanyName());
//        sb.append(" sumDoc=");
//        sb.append(this.getSumDoc());
        sb.append(" orderName=");
        sb.append(this.getOrderName());
        sb.append(" departName=");
        sb.append(this.getDepartName());
        sb.append(" typeExec=");
        sb.append(this.getTypeExec());
        sb.append(" typeQuantityPlan=");
        sb.append(this.getQuantityPlan());
        sb.append(" cost=");
        sb.append(this.getCost());
        sb.append(" isInternal=");
        sb.append(this.getIsInternal());
        sb.append(" isComplete=");
        sb.append(this.getIsComplete());
//        sb.append(" info=");
//        sb.append(this.getInfo());
//        sb.append(" urgency=");
//        sb.append(this.getUrgency());
        return sb.toString();
    }

    @Override
    public Order newInstance() {

        return new Order();
    }

    @Override
    public void copy(Order domain) {

        this.setId(domain.getId());
//        this.setContract(domain.getContract());
//        this.setNumDoc(domain.getNumDoc());
        this.setDateStart(domain.getDateStart());
        this.setDateEnd(domain.getDateEnd());
//        this.setCompanyName(domain.getCompanyName());
//        this.setSumDoc(domain.getSumDoc());
        this.setOrderName(domain.getOrderName());
        this.setDepartName(domain.getDepartName());
        this.setTypeExec(domain.getTypeExec());
        this.setQuantityPlan(domain.getQuantityPlan());
        this.setCost(domain.getCost());
        this.setIsInternal(domain.getIsInternal());
        this.setIsComplete(domain.getIsComplete());
//        this.setInfo(domain.getInfo());
//        this.setUrgency(domain.getUrgency());
    }

    @Override
    public Boolean isDomainFull() {
//        return (this.getId() != null) & (this.getContract() != null) & (this.getNumDoc() != null)
//                & (this.getDateStart() != null) & (this.getDateEnd() != null) & (this.getCompanyName() != null)
//                & (this.getSumDoc() != null) & (this.getOrderName() != null) & (this.getDepartName() != null)
//                & (this.getTypeExec() != null) & (this.getInfo() != null) & (this.getUrgency() != null);
        return (this.getId() != null) & (this.getQuantityPlan() != null) & (this.getCost() != null)
                & (this.getDateStart() != null) & (this.getDateEnd() != null) & (this.getIsInternal() != null)
                & (this.getIsComplete() != null) & (this.getDepartName() != null)
                & (this.getTypeExec() != null) & (this.getOrderName() != null);
    }

    @Override
    public Long getDomainId() {

        return this.getId().longValue();
    }

    @Override
    public Boolean isDomainEquals(Order domain) {
//        return (DataUtil.compare(this.getId(), domain.getId())) & (DataUtil.compare(this.getContract(), domain.getContract()))
//                & (DataUtil.compare(this.getNumDoc(), domain.getNumDoc())) & (DataUtil.compare(this.getDateStart(), domain.getDateStart()))
//                & (DataUtil.compare(this.getDateEnd(), domain.getDateEnd())) & (DataUtil.compare(this.getCompanyName(), domain.getCompanyName()))
//                & (DataUtil.compare(this.getSumDoc(), domain.getSumDoc())) & (DataUtil.compare(this.getOrderName(), domain.getOrderName()))
//                & (DataUtil.compare(this.getDepartName(), domain.getDepartName())) & (DataUtil.compare(this.getTypeExec(), domain.getTypeExec()))
//                & (DataUtil.compare(this.getInfo(), domain.getInfo())) & (DataUtil.compare(this.getUrgency(), domain.getUrgency()));
        return (DataUtil.compare(this.getId(), domain.getId())) & (DataUtil.compare(this.getQuantityPlan(), domain.getQuantityPlan()))
                & (DataUtil.compare(this.getCost(), domain.getCost())) & (DataUtil.compare(this.getDateStart(), domain.getDateStart()))
                & (DataUtil.compare(this.getDateEnd(), domain.getDateEnd())) & (DataUtil.compare(this.getIsInternal(), domain.getIsInternal()))
                & (DataUtil.compare(this.getIsComplete(), domain.getIsComplete())) & (DataUtil.compare(this.getOrderName(), domain.getOrderName()))
                & (DataUtil.compare(this.getDepartName(), domain.getDepartName())) & (DataUtil.compare(this.getTypeExec(), domain.getTypeExec()));
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
     * @return the contract
     */
//    public String getContract() {
//        return contract;
//    }
    /**
     * @param contract the contract to set
     */
//    public void setContract(String contract) {
//        this.contract = contract;
//    }
    /**
     * @return the numDoc
     */
//    public String getNumDoc() {
//        return numDoc;
//    }
    /**
     * @param numDoc the numDoc to set
     */
//    public void setNumDoc(String numDoc) {
//        this.numDoc = numDoc;
//    }
    /**
     * @return the dateStart
     */
    public Date getDateStart() {

        return dateStart;
    }

    /**
     * @param dateStart the dateStart to set
     */
    public void setDateStart(Date dateStart) {

        this.dateStart = dateStart;
    }

    /**
     * @return the dateEnd
     */
    public Date getDateEnd() {

        return dateEnd;
    }

    /**
     * @param dateEnd the dateEnd to set
     */
    public void setDateEnd(Date dateEnd) {

        this.dateEnd = dateEnd;
    }

    /**
     * @return the companyName
     */
//    public String getCompanyName() {
//        return companyName;
//    }
    /**
     * @param companyName the companyName to set
     */
//    public void setCompanyName(String companyName) {
//        this.companyName = companyName;
//    }
    /**
     * @return the sumDoc
     */
//    public Double getSumDoc() {
//        return sumDoc;
//    }
    /**
     * @param sumDoc the sumDoc to set
     */
//    public void setSumDoc(Double sumDoc) {
//        this.sumDoc = sumDoc;
//    }
    /**
     * @return the orderName
     */
    public String getOrderName() {

        return orderName;
    }

    /**
     * @param orderName the orderName to set
     */
    public void setOrderName(String orderName) {

        this.orderName = orderName;
    }

    /**
     * @return the departName
     */
    public String getDepartName() {

        return departName;
    }

    /**
     * @param departName the departName to set
     */
    public void setDepartName(String departName) {

        this.departName = departName;
    }

    /**
     * @return the typeExec
     */
    public Integer getTypeExec() {

        return typeExec;
    }

    /**
     * @param typeExec the typeExec to set
     */
    public void setTypeExec(Integer typeExec) {

        this.typeExec = typeExec;
    }

    /**
     * @return the quantityPlan
     */
    public Double getQuantityPlan() {

        return quantityPlan;
    }

    /**
     * @param quantityPlan the quantityPlan to set
     */
    public void setQuantityPlan(Double quantityPlan) {

        this.quantityPlan = quantityPlan;
    }

    /**
     * @return the cost
     */
    public Double getCost() {

        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(Double cost) {

        this.cost = cost;
    }

    /**
     * @return the isInternal
     */
    public Integer getIsInternal() {

        return isInternal;
    }

    /**
     * @param isInternal the isInternal to set
     */
    public void setIsInternal(Integer isInternal) {

        this.isInternal = isInternal;
    }

    /**
     * @return the isComplete
     */
    public Integer getIsComplete() {

        return isComplete;
    }

    /**
     * @param isComplete the isComplete to set
     */
    public void setIsComplete(Integer isComplete) {

        this.isComplete = isComplete;
    }
    /**
     * @return the info
     */
//    public String getInfo() {
//        return info;
//    }
    /**
     * @param info the info to set
     */
//    public void setInfo(String info) {
//        this.info = info;
//    }
    /**
     * @return the urgency
     */
//    public Integer getUrgency() {
//        return urgency;
//    }
    /**
     * @param urgency the urgency to set
     */
//    public void setUrgency(Integer urgency) {
//        this.urgency = urgency;
//    }
}
