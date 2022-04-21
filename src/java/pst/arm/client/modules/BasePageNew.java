package pst.arm.client.modules;


/**
 *
 * @author vorontsov
 */
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.header.MainNavigationPanel;
import pst.arm.client.modules.header.TopPanelToolbar;

public abstract class BasePageNew extends Viewport {

    protected TopPanelToolbar toolbarPanel;
    protected BorderLayoutData toolbarPanelData;
    protected String widgetId;
    protected ContentPanel centerPanel;
    protected MainNavigationPanel mainNavigationPanel; //hlv
    protected BorderLayoutData mainNavigationPanelData;//hlv
    
    protected FloatNavigationPanel floatNavigationPanel;
    protected BorderLayoutData floatBorderLayoutData;
    protected EventBus eventBus;
    protected BorderLayout mainLayout;
        
    public BasePageNew() {
        //hasFacility();
        initEventBus();
    }

    public BasePageNew(String id) {
        //hasFacility();
        widgetId = id;
        initEventBus();
    }

    private void initEventBus() {
        eventBus = new SimpleEventBus();
    }
    /*protected void hasFacility() {
        String page = ConfigurationManager.getModuleName();
        if (page == null || !ConfigurationManager.isModuleAvailable(page)) {
            Window.Location.assign(getHomeUrl());
        }
    }
    

    protected String getHomeUrl() {
        return GWT.getHostPageBaseURL() + "main.htm?" + AppHelper.getInstance().debugUrl();
    }*/


    @Override
    protected void onRender(Element parent, int index) {
       
        super.onRender(parent, index);
        RootPanel.get(getPreLoadID(getWidgetId())).setVisible(false);
        
        mainLayout = new BorderLayout();
        setLayout( mainLayout );


        toolbarPanelData = new BorderLayoutData(LayoutRegion.NORTH, 28);
        toolbarPanelData.setCollapsible(false);
        toolbarPanel = new TopPanelToolbar();
        add(toolbarPanel, toolbarPanelData);    
        
        floatBorderLayoutData = new BorderLayoutData( LayoutRegion.EAST, 40 );
        floatBorderLayoutData.setHideCollapseTool( true );
        floatNavigationPanel = new FloatNavigationPanel( this,eventBus );
        //floatNavigationPanel.setEventBus( eventBus );
        
        add( floatNavigationPanel, floatBorderLayoutData );
        
        mainNavigationPanelData = new BorderLayoutData(LayoutRegion.WEST, 300, 100, 400);  
        mainNavigationPanelData.setMargins(new Margins(0, 3, 0, 0));  
        mainNavigationPanelData.setCollapsible(true);
        mainNavigationPanel = new MainNavigationPanel(this);
        add(mainNavigationPanel, mainNavigationPanelData);       
        //requestResultPanel.setHeading("requestResultPanel");
        BorderLayoutData contentPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        //contentPanelData.setMargins(new Margins(0, 0, 0, 0));
        LayoutContainer contentPanel = getContentPanel();
        add(contentPanel, contentPanelData);
                
    }

    protected abstract LayoutContainer getContentPanel();

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    private static String getPreLoadID(String id) {
        return "gwt-loading-" + id;
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
  
}
