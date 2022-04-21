package pst.arm.client.modules.aiscontracts.domain;

/**
* Created by akozhin on 20.12.14.
*/
public enum AisContractStageField implements Field {

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

    AisContractStageField(String name, String column) {
        this.name = name;
        this.column = column;
    }

    AisContractStageField(String name, String column, Integer maxLength) {
        this.name = name;
        this.column = column;
        this.maxLength = maxLength;
    }

    AisContractStageField(String name, String column, FieldType type) {
        this.name = name;
        this.column = column;
        this.type = type;
    }

    public static AisContractStageField fromName(String name) {
        for (AisContractStageField field : values()) {
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

    public String getName() {
        return name;
    }

    public boolean isDate() {
        return type.equals(FieldType.Date);
    }

    public boolean isDouble() {
        return type.equals(FieldType.Double);
    }

}
