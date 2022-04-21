package pst.arm.client.modules.aiscontracts.domain;

/**
 * Created by akozhin on 19.12.14.
 */
public interface Field {
    public String getName();
    public FieldType getType();
    public String getColumn();
    public Integer getMaxLength();
}
