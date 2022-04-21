package pst.arm.client.modules.ganttchart.domain;

import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author nikita
 */
public class JobPlan implements Serializable, EditableDomain<JobPlan> {

    private Integer id;
    private Integer lineno;
    private Double price;
    private Date dateStart;
    private Date dateEnd;
    private Double nTime;
    private String name;
    private Double prepareTime;
//    private Integer idDepart;
//    private Double quantityPlan;
//    private Integer idOperKind;
//    private Integer parentId;
    private Double unitTime;
    private Integer idWorker;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("id=");
        sb.append(this.getId());
        sb.append(" lineno=");
        sb.append(this.getLineno());
        sb.append(" price=");
        sb.append(this.getPrice());
        sb.append(" dateStart=");
        sb.append(this.getDateStart());
        sb.append(" dateEnd=");
        sb.append(this.getDateEnd());
        sb.append(" nTime=");
        sb.append(this.getnTime());
        sb.append(" name=");
        sb.append(this.getName());
        sb.append(" prepareTime=");
        sb.append(this.getPrepareTime());
        sb.append(" unitTime=");
        sb.append(this.getUnitTime());
        sb.append(" idWorker=");
        sb.append(this.getIdWorker());
//        sb.append(" idDepart=");
//        sb.append(this.getIdDepart());
//        sb.append(" quantityPlan=");
//        sb.append(this.getQuantityPlan());
//        sb.append(" idOperKind=");
//        sb.append(this.getIdOperKind());
//        sb.append(" parentId=");
//        sb.append(this.getParentId());
        return sb.toString();
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
     * @return the lineno
     */
    public Integer getLineno() {

        return lineno;
    }

    /**
     * @param lineno the lineno to set
     */
    public void setLineno(Integer lineno) {

        this.lineno = lineno;
    }

    /**
     * @return the price
     */
    public Double getPrice() {

        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {

        this.price = price;
    }

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
     * @return the prepareTime
     */
    public Double getPrepareTime() {

        return prepareTime;
    }

    /**
     * @param prepareTime the prepareTime to set
     */
    public void setPrepareTime(Double prepareTime) {

        this.prepareTime = prepareTime;
    }

    /**
     * @return the nTime
     */
    public Double getnTime() {

        return nTime;
    }

    /**
     * @param nTime the nTime to set
     */
    public void setnTime(Double nTime) {

        this.nTime = nTime;
    }

    /**
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return the idDepart
     */
//    public Integer getIdDepart() {
//        return idDepart;
//    }
    /**
     * @param idDepart the idDepart to set
     */
//    public void setIdDepart(Integer idDepart) {
//        this.idDepart = idDepart;
//    }
    /**
     * @return the quantityPlan
     */
//    public Double getQuantityPlan() {
//        return quantityPlan;
//    }
    /**
     * @param quantityPlan the quantityPlan to set
     */
//    public void setQuantityPlan(Double quantityPlan) {
//        this.quantityPlan = quantityPlan;
//    }
    /**
     * @return the idOperKind
     */
//    public Integer getIdOperKind() {
//        return idOperKind;
//    }
    /**
     * @param idOperKind the idOperKind to set
     */
//    public void setIdOperKind(Integer idOperKind) {
//        this.idOperKind = idOperKind;
//    }
    /**
     * @return the parentId
     */
//    public Integer getParentId() {
//        return parentId;
//    }
    /**
     * @param parentId the parentId to set
     */
//    public void setParentId(Integer parentId) {
//        this.parentId = parentId;
//    }
    @Override
    public JobPlan newInstance() {

        return new JobPlan();
    }

    @Override
    public void copy(JobPlan domain) {

        this.setId(domain.getId());
        this.setLineno(domain.getLineno());
        this.setPrice(domain.getPrice());
        this.setDateStart(domain.getDateStart());
        this.setDateEnd(domain.getDateEnd());
        this.setnTime(domain.getnTime());
        this.setName(domain.getName());
        this.setPrepareTime(domain.getPrepareTime());
//        this.setIdDepart(domain.getIdDepart());
//        this.setQuantityPlan(domain.getQuantityPlan());
//        this.setIdOperKind(domain.getIdOperKind());
//        this.setParentId(domain.getParentId());
        this.setUnitTime(domain.getUnitTime());
        this.setIdWorker(domain.getIdWorker());
    }

    @Override
    public Boolean isDomainFull() {
//        return (this.getId() != null) & (this.getLineno() != null) & (this.getPrice() != null)
//                & (this.getDateStart() != null) & (this.getDateEnd() != null) & (this.getPrepareTime() != null)
//                & (this.getnTime() != null) & (this.getName() != null) & (this.getIdDepart() != null)
//                & (this.getQuantityPlan() != null) & (this.getIdOperKind() != null) & (this.getParentId() != null);
        return (this.getId() != null) & (this.getLineno() != null) & (this.getPrice() != null)
                & (this.getDateStart() != null) & (this.getDateEnd() != null) & (this.getPrepareTime() != null)
                & (this.getnTime() != null) & (this.getName() != null) & (this.getUnitTime() != null)
                & (this.getIdWorker() != null);

    }

    @Override
    public Long getDomainId() {

        return this.getId().longValue();
    }

    @Override
    public Boolean isDomainEquals(JobPlan domain) {
//        return DataUtil.compare(this.getId(), domain.getId()) & DataUtil.compare(this.getLineno(), domain.getLineno())
//                & DataUtil.compare(this.getPrice(), domain.getPrice()) & DataUtil.compare(this.getDateStart(), domain.getDateStart())
//                & DataUtil.compare(this.getDateEnd(), domain.getDateEnd()) & DataUtil.compare(this.getPrepareTime(), domain.getPrepareTime());
//                & DataUtil.compare(this.getnTime(), domain.getnTime()) & DataUtil.compare(this.getName(), domain.getName())
//                & DataUtil.compare(this.getIdDepart(), domain.getIdDepart()) & DataUtil.compare(this.getQuantityPlan(), domain.getQuantityPlan())
//                & DataUtil.compare(this.getIdOperKind(), domain.getIdOperKind()) & DataUtil.compare(this.getParentId(), domain.getParentId());
        return DataUtil.compare(this.getId(), domain.getId()) & DataUtil.compare(this.getLineno(), domain.getLineno())
                & DataUtil.compare(this.getPrice(), domain.getPrice()) & DataUtil.compare(this.getDateStart(), domain.getDateStart())
                & DataUtil.compare(this.getDateEnd(), domain.getDateEnd()) & DataUtil.compare(this.getPrepareTime(), domain.getPrepareTime())
                & DataUtil.compare(this.getnTime(), domain.getnTime()) & DataUtil.compare(this.getName(), domain.getName())
                & DataUtil.compare(this.getUnitTime(), domain.getUnitTime()) & DataUtil.compare(this.getIdWorker(), domain.getIdWorker());
    }

    /**
     * @return the unitTime
     */
    public Double getUnitTime() {

        return unitTime;
    }

    /**
     * @param unitTime the unitTime to set
     */
    public void setUnitTime(Double unitTime) {

        this.unitTime = unitTime;
    }

    /**
     * @return the idWorker
     */
    public Integer getIdWorker() {

        return idWorker;
    }

    /**
     * @param idWorker the idWorker to set
     */
    public void setIdWorker(Integer idWorker) {

        this.idWorker = idWorker;
    }
}
