package pst.arm.client.modules.aiscontracts.domain;

/**
 * Created by akozhin on 23.12.14.
 */
public enum AisContractSupplyAdditionalField implements Field{
    NUMB_("Номер", "NUMB_", 50), //varchar(50) NULL
    CONTRACT_DATE("Дата", "CONTRACT_DATE", FieldType.Date), //datetime NULL
    DEL_("ПометкаУдаления", "DEL_", FieldType.BooleanInt), //integer NULL
    LINK_("Ссылка", "LINK_", 255), //varchar(255) NULL
    HOLD_("Проведен", "HOLD_", FieldType.BooleanInt), //integer NULL
    WARRANTY("ГарантийныйСрок", "WARRANTY", 255), //varchar(255) NULL
    NDS_DOC_OUT_("ДокументОбОсвобожденииНДС", "NDS_DOC_OUT_", 100), //varchar(100) NULL
    CUSTOMER("Заказчик", "CUSTOMER", 70), //varchar(70) NULL
    ORG_EXECUTOR("Исполнитель", "ORG_EXECUTOR", 70), //varchar(70) NULL
    THEME("КраткоеНаименованиеТемы", "THEME", 100), //varchar(100) NULL
    CORR_("Корректировка", "CORR_", FieldType.BooleanInt), //integer NULL
    ND_("НД", "ND_", 50), //varchar(50) NULL
    CONTRACT_NUMB("НомерДоговора", "CONTRACT_NUMB", 255), //varchar(255) NULL
    STORAGE_PLACE("НомерПапки", "STORAGE_PLACE", 10), //varchar(10) NULL
    DP_NUMBER_("НомерСоглашения", "DP_NUMBER_", 50), //varchar(50) NULL
    COST("ОбщаяСуммаБезНДС", "COST", FieldType.Double), //numeric(20,2) NULL
    END_DATE("ОбщийСрок", "END_DATE", FieldType.Date), //datetime NULL
    COMPANY("Организация", "COMPANY", 30), //varchar(30) NULL
    REST_("Остаток", "REST_", FieldType.Double), //numeric(20,2) NULL
    REST_NDS_("ОстатокНДС", "REST_NDS_", FieldType.Double), //numeric(20,2) NULL
    PERSON_("Ответственный", "PERSON_", 150), //varchar(150) NULL
    ADVANCE("ПолученныйАванс", "ADVANCE", FieldType.Double), //numeric(20,2) NULL
    ADVANCE_REC_NDS_("ПоученныйАвансНДС", "ADVANCE_REC_NDS_", FieldType.Double), //numeric(20,2) NULL
    NDS("Признак /НДС/", "NDS", 10), //varchar(10) NULL
    MILITARY_REPR("ПризнакПриемка", "MILITARY_REPR", 4), //varchar(4) NULL
    REMARK_("Примечание", "REMARK_", 500), //varchar(500) NULL
    R_S_CUST_("РасчетныйСчетЗаказчик", "R_S_CUST_", 20), //varchar(20) NULL
    R_S_EXEC_("РасчетныйСчетИсполнитель", "R_S_EXEC_", 20), //varchar(20) NULL
    RECKONING_DATE_("СрокОплатыРасчета", "RECKONING_DATE_", FieldType.Date), //datetime NULL
    ADVANCE_DATE_("СрокПлоступленияАванса", "ADVANCE_DATE_", FieldType.Date), //datetime NULL
    CONTRACT_STAT("Статус", "CONTRACT_STAT", 11), //varchar(11) NULL
    ADVANCE_SUMM_("СуммаАванса", "ADVANCE_SUMM_", FieldType.Double), //numeric(20,2) NULL
    CUST_NDS("СуммаСНДС", "CUST_NDS", FieldType.Double), //numeric(20,2) NULL
    TRANSPORT_("Транспорт", "TRANSPORT_", 200), //varchar(200) NULL
    VIEW_("Представление", "VIEW_", 100), //varchar(100) NULL
    TIME_POINT_("МоментВремени", "TIME_POINT_", 100); //varchar(100) NULL


    private String name;
    private String column;
    private FieldType type = FieldType.String;
    private Integer maxLength;

    AisContractSupplyAdditionalField(String name, String column) {
        this.name = name;
        this.column = column;
    }

    AisContractSupplyAdditionalField(String name, String column, Integer maxLength) {
        this.name = name;
        this.column = column;
        this.maxLength = maxLength;
    }

    AisContractSupplyAdditionalField(String name, String column, FieldType type) {
        this.name = name;
        this.column = column;
        this.type = type;
    }

    public FieldType getType() {
        return type;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public static AisContractSupplyAdditionalField fromName(String name) {
        for (AisContractSupplyAdditionalField field : values()) {
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
}
