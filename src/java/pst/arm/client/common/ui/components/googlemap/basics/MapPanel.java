package pst.arm.client.common.ui.components.googlemap.basics;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Element;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeControlOptions;
import com.google.maps.gwt.client.MapTypeControlStyle;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.ZoomControlOptions;
import com.google.maps.gwt.client.ZoomControlStyle;



/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MapPanel extends LayoutContainer {

    private GoogleMap map;
    private MapOptions mapOptions = null;
    private LatLng mapCenter = null;
    private Integer mapZoom = 11;
    private static final LatLng PITER = LatLng.create(59.936667, 30.36094);
    public static final EventType MapCreate = new EventType();
    
    public MapPanel() {
    }

    public GoogleMap getMap() {
        if ( !afterRender ) {
            return map;
        } else {
            return null;
        }
    }

    public MapOptions getMapOptions() {
        return mapOptions;
    }

    public LatLng getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(LatLng ll) {
        mapCenter = ll;
    }

    public void setMapZoop(Integer z) {
        mapZoom = z;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        if (mapCenter == null) {
            mapCenter = PITER;
        }
        if (mapOptions == null) {
            mapOptions = MapOptions.create();
            mapOptions.setZoom(mapZoom);
            mapOptions.setCenter(mapCenter);
            mapOptions.setMapTypeId(MapTypeId.ROADMAP);
            mapOptions.setMapTypeControl(true);
            //mapOptions.setDisableDefaultUi(true);
            
            
            mapOptions.setMapTypeControl(true);
            MapTypeControlOptions myControlOptions = MapTypeControlOptions.create();
            myControlOptions.setStyle(MapTypeControlStyle.DROPDOWN_MENU);
            mapOptions.setMapTypeControlOptions(myControlOptions);

            mapOptions.setZoomControl(true);
            ZoomControlOptions myZoomOptions = ZoomControlOptions.create();
            myZoomOptions.setStyle(ZoomControlStyle.SMALL);
            mapOptions.setZoomControlOptions(myZoomOptions);

    
 
            
        }
        map = GoogleMap.create(parent, mapOptions);           
        fireEvent(MapCreate);
    }
    
   
}
