package pst.arm.client.modules.aiscontracts.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public enum AisContractRefTable {

    organization("ORGANIZATION", "SHORT_NAME", "ORGANIZATION_ID", null),
    stat("CONTRACT_STAT", "NAME", "CONTRACT_STAT_ID", 0),
    source("FUNDING_SOURCE", "NAME", "FUNDING_SOURCE_ID", 1),
    company("COMPANY", "SHORT_NAME", "COMPANY_ID", 1),
    military("MILITARY_REPR", "NAME", "MILITARY_REPR_ID", null),
    nds("NDS", "NAME", "NDS_ID", 1);
    private String table;
    private String name;
    private String id;
    private Object defaultValue;

    private AisContractRefTable(String table, String name, String id, Object defaultValue) {
        this.table = table;
        this.name = name;
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

}
