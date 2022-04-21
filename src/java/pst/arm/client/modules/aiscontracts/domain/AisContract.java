package pst.arm.client.modules.aiscontracts.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;
import pst.arm.client.modules.aiscontracts.util.FieldsUtil;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class AisContract implements Serializable, EditableDomain<AisContract> {

    private Integer contractId = null;  //Код договора
    private String contractNumb;        //Номер договора
    private Integer contractTypeId = null;
    private Integer contractStatId = null;
    private Integer orgExecutorId;        //Код исполнителя
    private Integer companyId;        //Код исполнителя
    private String contractTypeName;    //Тип договора
    private Date contractDate;
    private Date contractDateBegin;
    private Date contractDateEnd;
    private Double cost;                //Сумма без НДС
    private Double custNds;              //Сумма с НДС
    //Этапы
    private List<AisContractStage> stages = new ArrayList<AisContractStage>();
    private Map<AisContractField, String> fieldValues = new HashMap<AisContractField, String>();

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractTypeId(Integer contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public Integer getContractTypeId() {
        return contractTypeId;
    }

    public void setContractStatId(Integer contractStatId) {
        this.contractStatId = contractStatId;
    }

    public Integer getContractStatId() {
        return contractStatId;
    }

    public void setContractTypeName(String contractTypeName) {
        this.contractTypeName = contractTypeName;
    }

    public Integer getOrgExecutorId() {
        return orgExecutorId;
    }

    public void setOrgExecutorId(Integer orgExecutorId) {
        this.orgExecutorId = orgExecutorId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getContractTypeName() {
        return contractTypeName;
    }

    public void setContractNumb(String contractNumb) {
        this.contractNumb = contractNumb;
    }

    public String getContractNumb() {
        return contractNumb;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDateBegin(Date contractDate) {
        this.contractDateBegin = contractDate;
    }

    public Date getContractDateBegin() {
        return contractDateBegin;
    }

    public void setContractDateEnd(Date contractDate) {
        this.contractDateEnd = contractDate;
    }

    public Date getContractDateEnd() {
        return contractDateEnd;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    public void setCustNds(Double cusNds) {
        this.custNds = cusNds;
    }

    public Double getCustNds() {
        return custNds;
    }

    @Override
    public AisContract newInstance() {
        return new AisContract();
    }

    @Override
    public void copy(AisContract domain) {
        this.contractId = domain.contractId;
        this.contractNumb = domain.contractNumb;
        this.contractTypeId = domain.contractTypeId;
        this.contractStatId = domain.contractStatId;
        this.contractTypeName = domain.contractTypeName;
        this.contractDate = domain.contractDate;
        this.contractDateBegin = domain.contractDateBegin;
        this.contractDateEnd = domain.contractDateEnd;
        this.cost = domain.cost;
        this.custNds = domain.custNds;
        this.orgExecutorId = domain.orgExecutorId;
    }

    @Override
    public Boolean isDomainFull() {
        return true;
    }

    @Override
    public Long getDomainId() {
        return new Long(getContractId());
    }

    @Override
    public Boolean isDomainEquals(AisContract domain) {
        if (domain == null) {
            return false;
        }
        return (DataUtil.compare(this.contractNumb, domain.contractNumb)
                && DataUtil.compare(this.contractTypeId, domain.contractTypeId)
                && DataUtil.compare(this.orgExecutorId, domain.orgExecutorId)
                && DataUtil.compare(this.cost, domain.cost)
                && DataUtil.compare(this.custNds, domain.custNds)
                //&& DataUtil.compare(this.contractTypeName, domain.contractTypeName)
                && DataUtil.compare(this.contractDateBegin, domain.contractDateBegin)
                && DataUtil.compare(this.contractDateEnd, domain.contractDateEnd)
                && DataUtil.compare(this.contractDate, domain.contractDate));
    }

    public void addFieldValue(AisContractField name, String value) {
        getFieldValues().put(name, value);
    }

    public void addStage(AisContractStage stage) {
        getStages().add(stage);
    }

    /**
     * @return the stages
     */
    public List<AisContractStage> getStages() {
        return stages;
    }

    /**
     * @param stages the stages to set
     */
    public void setStages(List<AisContractStage> stages) {
        this.stages = stages;
    }

    public int getStagesCount() {
        return (stages != null) ? stages.size() : 0;
    }

    /**
     * @return the fieldValues
     */
    public Map<AisContractField, String> getFieldValues() {
        return fieldValues;
    }

    /**
     * @param fieldValues the fieldValues to set
     */
    public void setFieldValues(Map<AisContractField, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getField(AisContractField field) {
        if (fieldValues != null && fieldValues.containsKey(field)) {
            return fieldValues.get(field);
        }
        return null;
    }

    public boolean isDeleted() {
        return Boolean.valueOf(getField(AisContractField.deleted));
    }

    public boolean isPerformed() {
        return Boolean.valueOf(getField(AisContractField.performed));
    }

    public String asString() {
        return asString(true);
    }

    public String asString(boolean withStages) {
        String str = "";
        if (contractId != null) {
            str += "#" + contractId + " ";
        }
        if (getFieldValues().containsKey(AisContractField.contractNum)) {
            str += getFieldValues().get(AisContractField.contractNum) + ", ";
        }

        if (getFieldValues().containsKey(AisContractField.contractDate)) {
            str += getFieldValues().get(AisContractField.contractDate) + " ";
        }

        if (getFieldValues().containsKey(AisContractField.theme)) {
            str += getFieldValues().get(AisContractField.theme) + " ";
        }
        if (withStages && getStages() != null && !getStages().isEmpty()) {
            str += "\n";
            for (AisContractStage stage : stages) {
                str += "  " + stage.asString() + "\n";
            }
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AisContract) {
            for (Map.Entry<AisContractField, String> entrySet : fieldValues.entrySet()) {
                AisContractField key = entrySet.getKey();
                if (key.getRef() != null || key.getColumn().isEmpty()) {
                    continue;
                }
                Object value = getValue(key);
                Object compareValue = ((AisContract) obj).getValue(key);

                if (!value.equals(compareValue)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Object getValue(AisContractField field) {
        if (field != null) {
            return FieldsUtil.getValue(field.getType(), fieldValues.get(field));
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.contractId != null ? this.contractId.hashCode() : 0);
        hash = 13 * hash + (this.contractNumb != null ? this.contractNumb.hashCode() : 0);
        hash = 13 * hash + (this.contractTypeId != null ? this.contractTypeId.hashCode() : 0);
        hash = 13 * hash + (this.contractStatId != null ? this.contractStatId.hashCode() : 0);
        hash = 13 * hash + (this.orgExecutorId != null ? this.orgExecutorId.hashCode() : 0);
        hash = 13 * hash + (this.contractTypeName != null ? this.contractTypeName.hashCode() : 0);
        hash = 13 * hash + (this.contractDate != null ? this.contractDate.hashCode() : 0);
        hash = 13 * hash + (this.contractDateBegin != null ? this.contractDateBegin.hashCode() : 0);
        hash = 13 * hash + (this.contractDateEnd != null ? this.contractDateEnd.hashCode() : 0);
        hash = 13 * hash + (this.cost != null ? this.cost.hashCode() : 0);
        hash = 13 * hash + (this.custNds != null ? this.custNds.hashCode() : 0);
        hash = 13 * hash + (this.fieldValues != null ? this.fieldValues.hashCode() : 0);
        return hash;
    }

}
