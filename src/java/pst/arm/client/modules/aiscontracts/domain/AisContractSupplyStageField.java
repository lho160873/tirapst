package pst.arm.client.modules.aiscontracts.domain;

/**
 * Created by akozhin on 16.12.14.
 */
public enum AisContractSupplyStageField implements Field{
    LINK_("Ссылка", "LINK_", 255), //varchar(255) NULL
    LINE_AIS("НомерСтроки", "LINE_AIS", FieldType.Integer), //integer NULL
    BEG_DATE("СрокНачала", "BEG_DATE", FieldType.Date), //datetime NULL
    END_DATE("СрокОкончания", "END_DATE", FieldType.Date), //datetime NULL
    SUMM_COST("Сумма", "SUMM_COST", FieldType.Double), //numeric(20,2) NULL
    CUST_NDS("СуммаСНДС", "CUST_NDS", FieldType.Double), //numeric(20,2) NULL
    STAGE_NUMBER("Этап", "STAGE_NUMBER", 100), //varchar(100) NULL
    NAME("НаименованиеЭтапа", "NAME", 250), //varchar(250) NULL
    MANUAL_CALC("РучнойРасчет", "MANUAL_CALC", FieldType.BooleanInt); //integer NULL

    private String name;
    private String column;
    private AisContractRefTable ref;
    private FieldType type = FieldType.String;
    private Integer maxLength;

    AisContractSupplyStageField(String name, String column) {
        this.name = name;
        this.column = column;
    }

    AisContractSupplyStageField(String name, String column, Integer maxLength) {
        this.name = name;
        this.column = column;
        this.maxLength = maxLength;
    }

    AisContractSupplyStageField(String name, String column, FieldType type) {
        this.name = name;
        this.column = column;
        this.type = type;
    }

    AisContractSupplyStageField(String name, String column, AisContractRefTable ref) {
        this.name = name;
        this.column = column;
        this.ref = ref;
        this.type = FieldType.Integer;
    }

    public FieldType getType() {
        return type;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public static AisContractSupplyStageField fromName(String name) {
        for (AisContractSupplyStageField field : values()) {
            if (field.name.equals(name)) {
                return field;
            }
        }
        return null;
    }

    public String getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public AisContractRefTable getRef() {
        return ref;
    }

    public boolean isString() {
        return type.equals(FieldType.String);
    }

    public boolean isDate() {
        return type.equals(FieldType.Date);
    }

    public boolean isDouble() {
        return type.equals(FieldType.Double);
    }
}
