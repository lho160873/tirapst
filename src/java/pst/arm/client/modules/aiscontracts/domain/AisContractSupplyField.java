package pst.arm.client.modules.aiscontracts.domain;

public enum AisContractSupplyField implements Field {

    NUMB_("Номер", "NUMB_", 50), //varchar(50) NULL
    CONTRACT_DATE("Дата", "CONTRACT_DATE", FieldType.Date), //datetime NULL
    DEL_("ПометкаУдаления", "DEL_", FieldType.BooleanInt), //integer NULL
    LINK_("Ссылка", "LINK_", 255), //varchar(255) NULL
    HOLD_("Проведен", "HOLD_", FieldType.BooleanInt), //integer NULL
    PROBABILITY_("Вероятность", "PROBABILITY_", FieldType.Integer), //integer NULL
    EVAL_OF_GETTING("ВероятностьПолученияЗаказа", "EVAL_OF_GETTING", 70), //varchar(70) NULL
    WARRANTY("ГарантийныйСрок", "WARRANTY", 255), //varchar(255) NULL
    NDS_DOC_OUT_("ДокументОбОсвобожденииНДС", "NDS_DOC_OUT_", 100), //varchar(100) NULL
    CUSTOMER("Заказчик", "CUSTOMER", 70), //varchar(70) NULL
    ORG_EXECUTOR("Исполнитель", "ORG_EXECUTOR", 70), //varchar(70) NULL
    THEME("КраткоеНаименованиеТемы", "THEME", 100), //varchar(100) NULL
    CONTRACT_NUMB("НомерДоговора", "CONTRACT_NUMB", 50), //varchar(50) NULL
    STORAGE_PLACE("НомерПапки", "STORAGE_PLACE", 10), //varchar(10) NULL
    COST("ОбщаяСуммаБезНДС", "COST", FieldType.Double), //numeric(20,2) NULL
    END_DATE("ОбщийСрок", "END_DATE", FieldType.Date), //datetime NULL
    COMPANY("Организация", "COMPANY", 30), //varchar(30) NULL
    G_C("ОсновнойГосКонтракт", "G_C", 60), //varchar(60) NULL
    REST_("Остаток", "REST_", FieldType.Double), //numeric(20,2) NULL
    REST_NDS_("ОстатокНДС", "REST_NDS_", FieldType.Double), //numeric(20,2) NULL
    PERSON_("Ответственный", "PERSON_", 150), //varchar(150) NULL
    SEND_("Отправка", "SEND_", 200), //varchar(200) NULL
    ADVANCE("ПолученныйАванс", "ADVANCE", FieldType.Double), //numeric(20,2) NULL
    ADVANCE_REC_NDS_("ПоученныйАвансНДС", "ADVANCE_REC_NDS_", FieldType.Double), //numeric(20,2) NULL
    NDS("Признак", "NDS", 10), //varchar(10) NULL
    MILITARY_REPR("ПризнакПриемка", "MILITARY_REPR", 4), //varchar(4) NULL
    REMARK_("Примечание", "REMARK_", 500), //varchar(500) NULL
    R_S_CUST_("РасчетныйСчетЗаказчик", "R_S_CUST_", 20), //varchar(20) NULL
    R_S_EXEC_("РасчетныйСчетИсполнитель", "R_S_EXEC_", 20), //varchar(20) NULL
    CONTRACT_STAT("Статус", "CONTRACT_STAT", 11), //varchar(11) NULL
    CUST_NDS("СуммаСНДС", "CUST_NDS", FieldType.Double), //numeric(20,2) NULL
    TRANSPORT_("Транспорт", "TRANSPORT_", 200), //varchar(200) NULL
    PORTFOLIO_("ЗаметкаДляПортфеля", "PORTFOLIO_", 20), //varchar(20) NULL
    REQUEST_("Заявка", "REQUEST_", 50), //_ NULL
    COMMENT("Комментарий", "COMMENT", 500), //varchar(500) NULL
    CANCEL_BAS_("ОснованиеОтмены", "CANCEL_BAS_", 250), //varchar(250) NULL
    CALC_DATE_END_("РассчитыватьДатуОкончанияДоговора", "CALC_DATE_END_", FieldType.BooleanInt), //integer NULL
    BEG_PERC_("ПроцентДляНачалаРабот", "BEG_PERC_", FieldType.Integer), //integer NULL
    Q_DAYS_("КоличествоДнейВыполненияДоговора", "Q_DAYS_", FieldType.Integer), //integer NULL
    MILIT_("Военный", "MILIT_", FieldType.BooleanInt), //integer NULL
    V_CH_("ВоинскаяЧасть", "V_CH_", 70), //varchar(70) NULL
    PROJ_("Проект", "PROJ_", 10), //varchar(10) NULL
    REQ_CONTR_("ЗаявкаДоговор", "REQ_CONTR_", 255), //varchar(255) NULL
    IS_REQ_("ЭтоЗаявка", "IS_REQ_", FieldType.BooleanInt), //integer NULL
    VIEW_("Представление", "VIEW_", 100), //varchar(100) NULL
    TIME_POINT_("МоментВремени", "TIME_POINT_", 100);

    private String name;
    private String column;
    private AisContractRefTable ref;
    private FieldType type = FieldType.String;
    private Integer maxLength;

    AisContractSupplyField(String name, String column) {
        this.name = name;
        this.column = column;
    }

    AisContractSupplyField(String name, String column, Integer maxLength) {
        this.name = name;
        this.column = column;
        this.maxLength = maxLength;
    }

    AisContractSupplyField(String name, String column, FieldType type) {
        this.name = name;
        this.column = column;
        this.type = type;
    }

    AisContractSupplyField(String name, String column, AisContractRefTable ref) {
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

    public static AisContractSupplyField fromName(String name) {
        for (AisContractSupplyField field : values()) {
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
