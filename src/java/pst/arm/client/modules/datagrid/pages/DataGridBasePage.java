package pst.arm.client.modules.datagrid.pages;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.common.service.remote.GWTCommonServiceAsync;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.DataBasePanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;

import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.modules.datagrid.event.DataBasePanelEvent;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridBasePage extends BasePageNew {

    private GWTCommonServiceAsync service = null; //сервис отвечающий за обработку данных
    private TabPanel tabPanel;
    private LayoutContainer main;
    protected BaseConstants constants;
    protected HashMap<TabItem, DataBasePanel> dataPanels;

    @Override
    protected LayoutContainer getContentPanel() {
        return getPanel();
    }

    protected LayoutContainer getPanel() {
        this.setBorders(false);
        tabPanel = new TabPanel();
        tabPanel.setBorders(false);
        tabPanel.setBodyBorder(false);
        tabPanel.setAnimScroll(true);
        tabPanel.setTabScroll(true);
        
        
        constants = (BaseConstants) GWT.create(BaseConstants.class);
        dataPanels = new HashMap<TabItem, DataBasePanel>();

        tabPanel.addListener(Events.Remove, new Listener<TabPanelEvent>() {

            @Override
            public void handleEvent(TabPanelEvent be) {
                TabItem item = be.getItem();
                if (item != null && dataPanels.containsKey(item)) {
                    delOpenTable(dataPanels.get(item).getTableName());
                }
            }
        });

        tabPanel.addListener(Events.Select, new Listener<TabPanelEvent>() {

            @Override
            public void handleEvent(TabPanelEvent be) {
                TabItem item = be.getItem();
                if (item != null && dataPanels.containsKey(item)) {
                    String page = ConfigurationManager.getModuleName();
                    mainNavigationPanel.selectCurrentItem(page, dataPanels.get(item).getTableName());
                    dataPanels.get(item).resizePanelFiltr();
                }

            }
        });


        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);


        BorderLayoutData panelData = new BorderLayoutData(LayoutRegion.CENTER);
        panelData.setMargins(new Margins(1, 0, 0, 0));
        //panelData.setSplit(true);

        main.add(tabPanel, panelData);

        getOpenTable(); //строим tabitem для ранее отрктытых таблиц

        
        return main;
    }

    public void addTabItem(String tableName, final Boolean isActive) {
        if (tabPanel.getItemByItemId(tableName) != null)// .containsKey(tableName))
        {
            if (isActive) {
                tabPanel.setSelection(tabPanel.getItemByItemId(tableName));
            }
        } else {
            final TabItem item = new TabItem();
            //item.setClosable(true);
            item.setBorders(false);
            item.setItemId(tableName);
            item.setText("          ");
            
            Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
                @Override
                public void handleEvent(DataBasePanelEvent be) {
                    DataBasePanel panelSub = be.getPanel();
                    panelSub.setHeaderVisible(false);
                    panelSub.setBorders(false);
                    dataPanels.put(item, panelSub);
                    item.setText(panelSub.getCaption());
                    dataPanels.get(item).resizePanelFiltr();
                    item.setLayout(new FitLayout());
                    item.add(panelSub);

                    tabPanel.add(item);
                    if (isActive) {
                        tabPanel.setSelection(item);
                    }

                }
            };

            DataGridPanelBuilder.createDataGridOrTreePanel(tableName, createListener);

        }
    }

    protected GWTCommonServiceAsync getService() {
        if (service == null) {
            service = GWT.create(GWTCommonService.class);
        }
        return service;
    }

    protected void addOpenTable(String tableName) {
        // Create an asynchronous callback to handle the result.
        final AsyncCallback<Void> callback_addOpenTable = new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                //MessageBox.alert("","onSuccess",null);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        String page = ConfigurationManager.getModuleName();
        getService().addOpenTable(page, tableName, callback_addOpenTable);
    }
    
    /* @author akorenev
     * add protected for override
     */
    protected void getOpenTable() {
        // Create an asynchronous callback to handle the result.
        final AsyncCallback<HashMap< String, Boolean>> callback_getOpenTable = new AsyncCallback<HashMap< String, Boolean>>() {

            @Override
            public void onSuccess(HashMap< String, Boolean> result) {
                //MessageBox.alert("",String.valueOf(result.size()),null);
                //currTableName = "";
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    addTabItem((String) entry.getKey(), entry.getValue());
                }

            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        String page = ConfigurationManager.getModuleName();
        getService().getOpenTable(page, callback_getOpenTable);
    }
    
   
    protected void delOpenTable(String tableName) {
        // Create an asynchronous callback to handle the result.
        final AsyncCallback<Void> callback_delOpenTable = new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                //MessageBox.alert("","onSuccess",null);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        String page = ConfigurationManager.getModuleName();
        getService().delOpenTable(page, tableName, callback_delOpenTable);
    }
    
    /* @author akorenev
     * add return main layout container
     */
    public LayoutContainer getMainContainer() {
        return main;
    }
    
    /* @author akorenev
     * add return tabs panel
     */
    public TabPanel getTabPanel() {
        return tabPanel;
    }
    
}
