package pst.arm.client.common.ui.controls.editdomain.listener;

import pst.arm.client.common.ui.form.Editable.EditState;

/**
 * Listener for observing of changing edit state
 * 
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
public interface EditStateChangedListener {
    public void onEditStatusChanges(EditState newState);
}
