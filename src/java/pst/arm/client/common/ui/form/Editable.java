package pst.arm.client.common.ui.form;

/**
 * Common interface for editable forms
 * 
 * @author Alexandr Kozhin
 * @since 0.14.0
 * @see pst.arm.client.common.ui.controls.editdomain.DomainEditWindow
 */
public interface Editable {
    public enum EditMode{ VIEWONLY, EDITONLY, VIEWEDIT }
    public enum EditState{ VIEW, EDIT, NEW}
    public void setState(EditState state);
}
