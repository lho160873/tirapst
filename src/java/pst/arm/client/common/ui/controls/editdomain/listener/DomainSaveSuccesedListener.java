package pst.arm.client.common.ui.controls.editdomain.listener;

/**
 * Listener for observing of successful domain save.
 * 
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
public interface DomainSaveSuccesedListener<T> {

    /**
     * Fires after domain save succeed.
     * 
     * @param domain Domain object.
     */
    void onDomainSaveSucceed(T domain);
}
