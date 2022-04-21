package pst.arm.client.modules.ganttchart.domain;

import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author nikita
 */
public class Job implements Serializable, EditableDomain<Job> {

    private Integer id;
//    private String numDoc;
//    private Integer idNomenclature;
//    private Integer idDesignVersion;
    private Integer idOrder;
    private Integer idDepart;
    private Integer idParentJob;
    private Double quantityPlan;
    private Date dateStart;
    private Date dateEnd;
    private String name;
//    private String info;
    private Double sumDoc;
//    private Integer idInternalJob;
//    private String urgency;
    private Integer typeExec;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("id=");
        sb.append(this.getId());
//        sb.append(" numDoc=");
//        sb.append(this.getNumDoc());
//        sb.append(" idNomenclature=");
//        sb.append(this.getIdNomenclature());
//        sb.append(" idDesignVersion=");
//        sb.append(this.getIdDesignVersion());
        sb.append(" idOrder=");
        sb.append(this.getIdOrder());
        sb.append(" idDepart=");
        sb.append(this.getIdDepart());
        sb.append(" idParentJob=");
        sb.append(this.getIdParentJob());
        sb.append(" quantityPlan=");
        sb.append(this.getQuantityPlan());
        sb.append(" dateStart=");
        sb.append(this.getDateStart());
        sb.append(" dateEnd=");
        sb.append(this.getDateEnd());
        sb.append(" name=");
        sb.append(this.getName());
//        sb.append(" info=");
//        sb.append(this.getInfo());
        sb.append(" sumDoc=");
        sb.append(this.getSumDoc());
        sb.append(" typeExec=");
        sb.append(this.getTypeExec());
//        sb.append(" idInteranalJob=");
//        sb.append(this.getIdInternalJob());
//        sb.append(" urgency=");
//        sb.append(this.getUrgency());
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
     * @return the idNomenclature
     */
//    public Integer getIdNomenclature() {
//        return idNomenclature;
//    }
    /**
     * @param idNomenclature the idNomenclature to set
     */
//    public void setIdNomenclature(Integer idNomenclature) {
//        this.idNomenclature = idNomenclature;
//    }
    /**
     * @return the idDesignVersion
     */
//    public Integer getIdDesignVersion() {
//        return idDesignVersion;
//    }
    /**
     * @param idDesignVersion the idDesignVersion to set
     */
//    public void setIdDesignVersion(Integer idDesignVersion) {
//        this.idDesignVersion = idDesignVersion;
//    }
    /**
     * @return the idOrder
     */
    public Integer getIdOrder() {

        return idOrder;
    }

    /**
     * @param idOrder the idOrder to set
     */
    public void setIdOrder(Integer idOrder) {

        this.idOrder = idOrder;
    }

    /**
     * @return the idDepart
     */
    public Integer getIdDepart() {

        return idDepart;
    }

    /**
     * @param idDepart the idDepart to set
     */
    public void setIdDepart(Integer idDepart) {

        this.idDepart = idDepart;
    }

    /**
     * @return the idParentJob
     */
    public Integer getIdParentJob() {

        return idParentJob;
    }

    /**
     * @param idParentJob the idParentJob to set
     */
    public void setIdParentJob(Integer idParentJob) {

        this.idParentJob = idParentJob;
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
     * @return the sumDoc
     */
    public Double getSumDoc() {

        return sumDoc;
    }

    /**
     * @param sumDoc the sumDoc to set
     */
    public void setSumDoc(Double sumDoc) {

        this.sumDoc = sumDoc;
    }

    /**
     * @return the idInternalJob
     */
//    public Integer getIdInternalJob() {
//        return idInternalJob;
//    }
    /**
     * @param idInternalJob the idInternalJob to set
     */
//    public void setIdInternalJob(Integer idInternalJob) {
//        this.idInternalJob = idInternalJob;
//    }
    /**
     * @return the urgency
     */
//    public String getUrgency() {
//        return urgency;
//    }
    /**
     * @param urgency the urgency to set
     */
//    public void setUrgency(String urgency) {
//        this.urgency = urgency;
//    }
    @Override
    public Job newInstance() {

        return new Job();
    }

    @Override
    public void copy(Job domain) {

        this.setId(domain.getId());
//        this.setNumDoc(domain.getNumDoc());
//        this.setIdNomenclature(domain.getIdNomenclature());
//        this.setIdDesignVersion(domain.getIdDesignVersion());
        this.setIdOrder(domain.getIdOrder());
        this.setIdDepart(domain.getIdDepart());
        this.setIdParentJob(domain.getIdParentJob());
        this.setQuantityPlan(domain.getQuantityPlan());
        this.setDateStart(domain.getDateStart());
        this.setDateEnd(domain.getDateEnd());
        this.setName(domain.getName());
//        this.setInfo(domain.getInfo());
        this.setSumDoc(domain.getSumDoc());
        this.setTypeExec(domain.getTypeExec());
//        this.setIdInternalJob(domain.getIdInternalJob());
//        this.setUrgency(domain.getUrgency());
    }

    @Override
    public Boolean isDomainFull() {
//        return (this.getId() != null) & (this.getNumDoc() != null) & (this.getIdNomenclature() != null) & (this.getIdDesignVersion() != null)
//                & (this.getIdOrder() != null) & (this.getIdDepart() != null) & (this.getIdParentJob() != null) & (this.getName() != null)
//                & (this.getQuantityPlan() != null) & (this.getDateStart() != null) & (this.getDateEnd() != null) & (this.getInfo() != null)
//                & (this.getSumDoc() != null) & (this.getIdInternalJob() != null) & (this.getUrgency() != null);
        return (this.getId() != null)
                & (this.getIdOrder() != null) & (this.getIdDepart() != null) & (this.getIdParentJob() != null) & (this.getName() != null)
                & (this.getQuantityPlan() != null) & (this.getDateStart() != null) & (this.getDateEnd() != null)
                & (this.getSumDoc() != null) & (this.getTypeExec() != null);

    }

    @Override
    public Long getDomainId() {

        return this.getId().longValue();
    }

    @Override
    public Boolean isDomainEquals(Job domain) {
//        return DataUtil.compare(this.getId(), domain.getId()) & DataUtil.compare(this.getNumDoc(), domain.getNumDoc())
//                & DataUtil.compare(this.getIdNomenclature(), domain.getIdNomenclature()) & DataUtil.compare(this.getIdDesignVersion(), domain.getIdDesignVersion())
//                & DataUtil.compare(this.getIdOrder(), domain.getIdOrder()) & DataUtil.compare(this.getIdDepart(), domain.getIdDepart())
//                & DataUtil.compare(this.getIdParentJob(), domain.getIdParentJob()) & DataUtil.compare(this.getName(), domain.getName())
//                & DataUtil.compare(this.getQuantityPlan(), domain.getQuantityPlan()) & DataUtil.compare(this.getDateStart(), domain.getDateStart())
//                & DataUtil.compare(this.getDateEnd(), domain.getDateEnd()) & DataUtil.compare(this.getInfo(), domain.getInfo())
//                & DataUtil.compare(this.getSumDoc(), domain.getSumDoc()) & DataUtil.compare(this.getIdInternalJob(), domain.getIdInternalJob())
//                & DataUtil.compare(this.getUrgency(), domain.getUrgency());
        return DataUtil.compare(this.getId(), domain.getId())
                & DataUtil.compare(this.getIdOrder(), domain.getIdOrder()) & DataUtil.compare(this.getIdDepart(), domain.getIdDepart())
                & DataUtil.compare(this.getIdParentJob(), domain.getIdParentJob()) & DataUtil.compare(this.getName(), domain.getName())
                & DataUtil.compare(this.getQuantityPlan(), domain.getQuantityPlan()) & DataUtil.compare(this.getDateStart(), domain.getDateStart())
                & DataUtil.compare(this.getDateEnd(), domain.getDateEnd())
                & DataUtil.compare(this.getSumDoc(), domain.getSumDoc())
                & DataUtil.compare(this.getTypeExec(), domain.getTypeExec());
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
}
