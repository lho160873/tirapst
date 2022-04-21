package pst.arm.client.modules.aiscontracts.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public enum AisContractField {

    id("Номер", "ID_AIS", 9),
    idDop("Шифр работы", "CIPHER", FieldType.Integer),
    contractNum("НомерДоговора", "CONTRACT_NUMB", 40),
    contractNumDop("Предмет договора", "SUBJECT", 255),
    agreementNum("НомерСоглашения", "CONTRACT_NUMB", 40),
    contractDate("Дата", "CONTRACT_DATE", FieldType.Date),
    customer("Заказчик", "CUSTOMER_ID", AisContractRefTable.organization),
    executor("Исполнитель", "ORG_EXECUTOR_ID", AisContractRefTable.organization),
    nds("Признак", "NDS_ID", AisContractRefTable.nds),
    status("Статус", "CONTRACT_STAT_ID", AisContractRefTable.stat),
    theme("КраткоеНаименованиеТемы", "THEME", 1000),
    cost("ОбщаяСуммаБезНДС", "COST", FieldType.Double),
    costNds("СуммаСНДС", "CUST_NDS", FieldType.Double),
    advance("ПолученныйАванс", "ADVANCE", FieldType.Double),
    advanceNds("ПолученныйАвансНДС", "", FieldType.Double),
    militaryRepr("ПризнакПриемка", "MILITARY_REPR_ID", AisContractRefTable.military),
    evalOfGetting("Вероятность", "EVAL_OF_GETTING_ID", FieldType.Integer),
    endDate("ОбщийСрок", "END_DATE", FieldType.Date),
    storagePlace("НомерПапки", "STORAGE_PLACE", 100),
    comment("Комментарий", "COMMENT", 255),
    source("Источник", "FUNDING_SOURCE_ID", AisContractRefTable.source),
    gc("ОсновнойГосКонтракт", "G_C", 255),
    warranty("ГарантийныйСрок", "WARRANTY", 100),
    organisation("Организация", "COMPANY_ID", AisContractRefTable.company),
    contractType("Код типа договора", "CONTRACT_TYPE_ID", FieldType.Integer),
    performed("Проведен", "", FieldType.Boolean),
    deleted("ПометкаУдаления", "", FieldType.Boolean),
    //Dop
    parentContractNum("НД", "PARENT_ID", FieldType.Integer),
    note("Примечание", "COMMENT", 255);

    private String name;
    private String column;
    private AisContractRefTable ref;
    private FieldType type = FieldType.String;
    private Integer maxLength;

    AisContractField(String name, String column) {
        this.name = name;
        this.column = column;
    }

    AisContractField(String name, String column, Integer maxLength) {
        this.name = name;
        this.column = column;
        this.maxLength = maxLength;
    }

    AisContractField(String name, String column, FieldType type) {
        this.name = name;
        this.column = column;
        this.type = type;
    }

    AisContractField(String name, String column, AisContractRefTable ref) {
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

    public static AisContractField fromName(String name) {
        for (AisContractField field : values()) {
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
