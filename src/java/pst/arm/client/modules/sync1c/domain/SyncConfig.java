package pst.arm.client.modules.sync1c.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class SyncConfig {

    private Boolean showOnlyChanged = true;

    public SyncConfig() {
    }

    public boolean showOnlyChanged() {
        return showOnlyChanged;
    }

    public void setShowOnlyChanged(Boolean showOnlyChanged) {
        this.showOnlyChanged = showOnlyChanged;
    }

}
