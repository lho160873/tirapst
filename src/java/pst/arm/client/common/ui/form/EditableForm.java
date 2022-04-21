package pst.arm.client.common.ui.form;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.13.0
 */
public interface EditableForm<T> {
    public Boolean validate();
    public void clear();
    public void setDomain(T object);
    public void fillDomain(T object);
    
}
