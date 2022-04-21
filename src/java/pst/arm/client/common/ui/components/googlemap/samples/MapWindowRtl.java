package pst.arm.client.common.ui.components.googlemap.samples;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import pst.arm.client.common.ui.components.googlemap.basics.MapPanel;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerShape;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MapWindowRtl extends Window{
    private MapPanel mapPanel;
    private static final int TILE_SIZE = 256;
    private GoogleMap map;
   
    public MapWindowRtl()
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
                MarkerOptions markerOpts = MarkerOptions.create();
                MarkerImage icon = MarkerImage.create("http://localhost:8084/tirapst/images/map/beachflag.png", Size.create(20, 32),
                        Point.create(0, 0), Point.create(0, 32));
                markerOpts.setIcon(icon);
                markerOpts.setPosition(mapPanel.getMapCenter());
                markerOpts.setMap(map);
                final Marker marker = Marker.create(markerOpts);

                InfoWindowOptions infoWindowOpts = InfoWindowOptions.create();
                infoWindowOpts.setContent("<b>Center Piter</b>");

                final InfoWindow infoWindow = InfoWindow.create(infoWindowOpts);
                //infoWindow.open(map);

                marker.addClickListener(new ClickHandler() {
                    public void handle(MouseEvent event) {
                        infoWindow.open(map, marker);
                    }
                });
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