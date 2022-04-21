package pst.arm.client.modules.datagrid.domain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum EColumnRelationType {
    AND("AND"),
    OR("OR");    
    private final String type;

    private EColumnRelationType(String type) {
        this.type = type;
    }

    public String getRelationType() {
        return type;
    }
}
