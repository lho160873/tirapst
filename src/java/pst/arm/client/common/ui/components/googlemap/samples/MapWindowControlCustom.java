package pst.arm.client.common.ui.components.googlemap.samples;

 
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import pst.arm.client.common.ui.components.googlemap.basics.MapPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MapWindowControlCustom extends Window {

    private MapPanel mapPanel;
    private static final int TILE_SIZE = 256;
    private GoogleMap map;

    class StatefulButtonPanel extends VerticalPanel {

        private LatLng home = LatLng.create(59.936667, 30.36094);
        double zoom = 10;

        public StatefulButtonPanel() {
            Button homeBtn = new Button("Home", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    map.setCenter(home);
                    map.setZoom(zoom);
                }
            });

      Button setBtn = new Button("Set Home", new ClickHandler() {
        public void onClick(ClickEvent event) {
          home = map.getCenter();
          zoom = map.getZoom();
        }
      });

      // Add button to the root panel. (register it on the GWT side)
      add(homeBtn);
      add(setBtn);
    }
  }
    public MapWindowControlCustom()
    {
        setHeading("Карта ");
        setModal(true);
        setSize(800,600);
        initComponents();
        
        mapPanel.addListener(MapPanel.MapCreate, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (mapPanel.getMap() == null) {
                    return;
                }

                map = mapPanel.getMap();
                final Panel panel = new StatefulButtonPanel();

                RootPanel.get().add(panel);

                // Add button as a map control.
                map.getControls().get(
                        new Double(ControlPosition.TOP_RIGHT.getValue()).intValue()).push(
                        panel.getElement());
            }
        });

    }
    private void initComponents()
    {
        setLayout(new BorderLayout());
        mapPanel = new MapPanel();
        add(mapPanel,new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
    
}