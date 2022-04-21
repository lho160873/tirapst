package pst.arm.client.modules.aiscontracts.sync;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface BaseContainer {
    public void initObjects();
    public void initControls();
    public void initEvents();
    public void initGWTEvents();
    public void initUI();
}
