package pst.arm.client.modules.aiscontracts.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.modules.aiscontracts.util.FieldsUtil;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisContractStage implements Serializable {

    private Integer contractId, stageId;
    private Map<Field, String> fieldValues;

    public AisContractStage() {
    }

    /**
     * @return the stageId
     */
    public Integer getStageId() {
        return stageId;
    }

    /**
     * @param stageId the stageId to set
     */
    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Object getValue(Field field) {
        if (field != null) {
            return FieldsUtil.getValue(field.getType(), fieldValues.get(field));
        }
        return null;
    }

    public enum Field {

        contract("Договор", "CONTRACT_ID", FieldType.Integer),
        stageName("НаименованиеЭтапа", "NAME", 200),
        description("СодержаниеРабот", "DESCRIPTION", 255),
        start("СрокНачала", "BEG_DATE", FieldType.Date),
        end("СрокОкончания", "END_DATE", FieldType.Date),
        //        dates("СрокПеревода", "", FieldType.Date),
        cost("Сумма", "SUMM_COST", FieldType.Double),
        sumNds("СуммаСНДС", "", FieldType.Double),
        stageNumber("Этап", "STAGE_NUMBER", 10),
        lineNumber("НомерСтроки", "LINE_AIS", 9);

        private String name, column;
        private FieldType type = FieldType.String;
        Integer maxLength;

        Field(String name, String column) {
            this.name = name;
            this.column = column;
        }

        Field(String name, String column, Integer maxLength) {
            this.name = name;
            this.column = column;
            this.maxLength = maxLength;
        }

        Field(String name, String column, FieldType type) {
            this.name = name;
            this.column = column;
            this.type = type;
        }

        public static Field fromName(String name) {
            for (Field field : values()) {
                if (field.name.equals(name)) {
                    return field;
                }
            }
            return null;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public String getColumn() {
            return column;
        }

        public FieldType getType() {
            return type;
        }

        private String getName() {
            return name;
        }

        public boolean isDate() {
            return type.equals(FieldType.Date);
        }

        public boolean isDouble() {
            return type.equals(FieldType.Double);
        }

    }

    public Map<Field, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Map<Field, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public void addFieldValue(Field name, String value) {
        if (fieldValues == null) {
            fieldValues = new HashMap<Field, String>();
        }
        fieldValues.put(name, value);
    }

    public String getField(Field field) {
        if (fieldValues != null && fieldValues.containsKey(field)) {
            return fieldValues.get(field);
        }
        return null;
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
        if (contractId != null) {
            addFieldValue(Field.contract, contractId.toString());
        }
    }

    public String asString() {
        String str = "";
        if (stageId != null) {
            str += "#" + stageId + " ";
        }
        str += fieldValues.get(AisContractStage.Field.lineNumber) + " ";
        str += fieldValues.get(AisContractStage.Field.stageName);
        str += ", " + fieldValues.get(AisContractStage.Field.description);
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AisContractStage) {
            for (Map.Entry<Field, String> entrySet : fieldValues.entrySet()) {
                if (entrySet.getKey().getColumn().isEmpty()) {
                    continue;
                }
                AisContractStage.Field key = entrySet.getKey();
                Object value = getValue(key);
                Object valueToCompare = ((AisContractStage) obj).getValue(key);
                if (!value.equals(valueToCompare)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.contractId != null ? this.contractId.hashCode() : 0);
        hash = 79 * hash + (this.stageId != null ? this.stageId.hashCode() : 0);
        hash = 79 * hash + (this.fieldValues != null ? this.fieldValues.hashCode() : 0);
        return hash;
    }

}
