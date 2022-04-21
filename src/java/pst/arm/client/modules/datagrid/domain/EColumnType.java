package pst.arm.client.modules.datagrid.domain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum EColumnType {

    STRING(java.lang.String.class),
    NUMBER(java.lang.Number.class),
    LONG(java.lang.Long.class),
    INTEGER(java.lang.Integer.class),
    DOUBLE(java.lang.Double.class),
    FLOAT(java.lang.Float.class),
    BOOLEAN(java.lang.Boolean.class),
    DATE(java.util.Date.class);
    private final Class type;

    private EColumnType(Class type) {
        this.type = type;
    }

    public Class getTypeClass() {
        return type;
    }
}
