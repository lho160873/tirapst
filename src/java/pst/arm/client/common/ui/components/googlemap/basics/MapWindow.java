package pst.arm.client.common.ui.components.googlemap.basics;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MapWindow extends Window{
    private MapPanel mapPanel;
    public MapWindow()
    {
        setHeading("Карта");
        setModal(true);
        setSize(800,600);
        initComponents();
    }
    private void initComponents()
    {
        setLayout(new BorderLayout());
        mapPanel = new MapPanel();
        add(mapPanel,new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
}
