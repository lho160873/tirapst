/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.common.ui.components.googlemap.samples;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Point;
import pst.arm.client.common.ui.components.googlemap.basics.MapPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MapWindowCoordinates extends Window{
    private MapPanel mapPanel;
    private GoogleMap map;
    
    private static final int TILE_SIZE = 256;

   
    public MapWindowCoordinates()
    {
        setHeading("Карта ");
        setModal(true);
        setSize(800,600);
        initComponents();
        
        mapPanel.addListener(MapPanel.MapCreate, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (mapPanel.getMap() != null) {
                    map = mapPanel.getMap();
                    map.addZoomChangedListener(new GoogleMap.ZoomChangedHandler() {
                        public void handle() {
                            final InfoWindow coordInfoWindow = InfoWindow.create();
                            coordInfoWindow.setContent(createInfoWindowContent());
                            coordInfoWindow.setPosition(mapPanel.getMapCenter());
                            coordInfoWindow.open(map);
                        }
                    });
                }
            }
        });
              
    }
    private void initComponents()
    {
        setLayout(new BorderLayout());
        mapPanel = new MapPanel();
        add(mapPanel,new BorderLayoutData(Style.LayoutRegion.CENTER));
    }
    
    /**
     * Simple Projections for use by sample app.
     */
    public static class MercatorProjection {

        private static final Point PIXEL_ORIGIN = Point.create(TILE_SIZE / 2,
                TILE_SIZE / 2);
        private static final double PIXELS_PER_LON_DEGREE = MapWindowCoordinates.TILE_SIZE / 360;
        private static final double PIXELS_PER_LON_RADIAN = MapWindowCoordinates.TILE_SIZE
                / (2 * Math.PI);

        public MercatorProjection() {
        }

        public static final double degreesToRadians(double deg) {
            return deg * (Math.PI / 180);
        }

        public static final double radiansToDegrees(double rad) {
            return rad / (Math.PI / 180);
        }

        public static final double bound(double value, double min, double max) {
            value = Math.max(value, min);
            value = Math.min(value, max);
            return value;
        }

        public Point fromLatLngToPoint(LatLng loc) {
            Point point = Point.create(0, 0);
            Point origin = PIXEL_ORIGIN;

            point.setX(origin.getX() + loc.lng() * PIXELS_PER_LON_DEGREE);

            // NOTE: Truncating to 0.9999 effectively limits latitude to 89.189. This
            // is about a third of a tile past the edge of the world tile.
            double siny = bound(Math.sin(degreesToRadians(loc.lat())), -0.9999,
                    0.9999);
            point.setY(origin.getY() + 0.5 * (Math.log((1 + siny) / (1 - siny)))
                    - PIXELS_PER_LON_RADIAN);
            return point;
        }

        public LatLng fromPointToLatLng(Point point) {
            double lng = (point.getX() - PIXEL_ORIGIN.getX()) / PIXELS_PER_LON_DEGREE;
            double latRadians = (point.getY() - PIXEL_ORIGIN.getY())
                    / PIXELS_PER_LON_RADIAN;
            double lat = radiansToDegrees(2 + Math.atan(Math.exp(latRadians))
                    - Math.PI / 2);
            return LatLng.create(lat, lng);
        }
    }

    private String createInfoWindowContent() {
        if (map == null) {
            return "";
        }
        int numTiles = 1 << (int) map.getZoom();
        MapWindowCoordinates.MercatorProjection projection = new MapWindowCoordinates.MercatorProjection();
        Point worldCoordinate = projection.fromLatLngToPoint(mapPanel.getMapCenter());
        Point pixelCoordinate = Point.create(worldCoordinate.getX() * numTiles,
                worldCoordinate.getY() * numTiles);
        Point tileCoordinate = Point.create(
                Math.floor(pixelCoordinate.getX() / TILE_SIZE),
                Math.floor(pixelCoordinate.getY() / TILE_SIZE));

        SafeHtmlBuilder returnString = new SafeHtmlBuilder();
        returnString.appendHtmlConstant("Piter, IL<br>");
        returnString.appendHtmlConstant("LatLng: ");
        returnString.append(mapPanel.getMapCenter().lat());
        returnString.appendHtmlConstant(", ");
        returnString.append(mapPanel.getMapCenter().lng());
        returnString.appendHtmlConstant("<br />");
        returnString.appendHtmlConstant("World Coordinate: ");
        returnString.append(worldCoordinate.getX());
        returnString.appendHtmlConstant(", ");
        returnString.append(worldCoordinate.getY());
        returnString.appendHtmlConstant("<br />");

        returnString.appendHtmlConstant("Pixel Coordinate: ");
        returnString.append(Math.floor(pixelCoordinate.getX()));
        returnString.appendHtmlConstant(", ");
        returnString.append(Math.floor(pixelCoordinate.getY()));
        returnString.appendHtmlConstant("<br />");

        returnString.appendHtmlConstant("Tile Coordinate: ");
        returnString.append(Math.floor(tileCoordinate.getX()));
        returnString.appendHtmlConstant(", ");
        returnString.append(Math.floor(tileCoordinate.getY()));
        returnString.appendHtmlConstant("<br />");

        returnString.appendHtmlConstant("at Zoom Level: ");
        returnString.append(map.getZoom());
         
        return returnString.toSafeHtml().asString();
    }
}