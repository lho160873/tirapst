package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.logging.Logger;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.common.service.remote.GWTCommonServiceAsync;

/**
 *
 * @author vorontsov
 */
public class PortalPanel extends Portal {

    Logger logger = Logger.getLogger(getClass().getName());
    protected GWTCommonServiceAsync commonService = GWT.create(GWTCommonService.class);
    static final int columnCount = 1;
    private int panelCount;
    private List<String> lst;
    private boolean bRPCDone;

    public PortalPanel() {
        super(columnCount);
        bRPCDone = false;
        panelCount = 0;
        for (int i = 0; i < columnCount; i++) {
            setColumnWidth(i, (double) 1.0 / columnCount);
        }
        addPortlets();

    }

    protected boolean isShowPanel(String strOriginalPanelID) {
        for (int i = 0; i < lst.size(); i++) {

            if ((lst.get(i)).equalsIgnoreCase(strOriginalPanelID)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isShowRequest() {
        if (hasFacility("requests") || hasFacility("requestsreport") || hasFacility("reqconfig")) {
            return true;
        }
        return false;
    }

    protected boolean isShowMkfBooks() {
        return isShowPanel("MFK_BOOKS");
    }

    protected boolean isShowMkfSearch() {
        return isShowPanel("MFK_SEARCH");
    }

    protected boolean isShowMkfReports() {
        return isShowPanel("MFK_REPORTS");
    }

    protected boolean isShowFunds() {
        return isShowPanel("FUNDS");
    }

    protected boolean isShowFundStat() {
        return isShowPanel("FUND_STAT");
    }

    protected boolean isShowPointers() {
        return isShowPanel("POINTERS");
    }

    protected boolean isShowSearchFunds() {
        return isShowPanel("SEARCH_FUNDS");
    }

    protected boolean isShowSearchCards() {
        return isShowPanel("SEARCH_CARDS");
    }

    protected boolean isShowSearchCardFiles() {
        return isShowPanel("SEARCH_CARDFILES");
    }

    protected boolean isShowStatCards() {
        return isShowPanel("STAT_CARDS");
    }

    private boolean isShowLinks() {
        String portlets = ConfigurationManager.getProperty("portlets");
        return (portlets != null && !portlets.isEmpty() && portlets.contains("links"));
    }

    private boolean isShowChangeLog() {
        String portlets = ConfigurationManager.getProperty("portlets");
        return (portlets != null && !portlets.isEmpty() && portlets.contains("changelog"));
    }

    protected void addPortlets() {
        //addListLinks();
        //addChangeLog();
        
        //addMessagesIn();
        //addMessagesOut();
        //addMessagesReglament();
 
    }

    protected void addListLinks() {
//        if (bRPCDone == false) {
//            return;
//        }
        //create links store
        final ListStore<PortletLink> store = new ListStore<PortletLink>();
//        String[][] links = {{"Архивный фонд ПК 4 версия", null},
//            {"Карты (схемы) Петрограда - Ленинграда - Санкт-Петербурга 1917-1990 гг.", "page.htm?name=imagegallery"},
//            {"\"Медаль За доблестный труд в Великой Отечественной войне 1941 - 1945 гг.\" БД", null},
//            {"\"Медаль За оборону Ленинграда\" БД", null},
//            {"Методические и справочные пособия (журнал регистрации)", null},
//            {"Правила организации хранения, комплектования, учета и использования документов АФ РФ", null},
//            {"Путеводитель ЦГА 2001 г.", AppHelper.getInstance().getConfiguration().baseUrl()
//                + AppHelper.getInstance().getConfiguration().resourcesCgaPutevoditelPath()},
//            {"Справочник по истории административно-территориального деления Ленинградской области 1917-1969 гг.", null},
//            {"БД Гарант", null},
//            {"БД Желтые страницы", null}};
//        for (String[] link : links) {
//            PortletLink pl = new PortletLink();
//            pl.setText(link[0]);
//            pl.setHref(link[1]);
//            store.add(pl);
//        }
        if (isShowLinks()) {
            final PortletListLinks portlet = new PortletListLinks(store);
            commonService.getPortletLinks(new AsyncCallback<List<PortletLink>>() {
                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("onFailure", "Error get links", null);
                }

                @Override
                public void onSuccess(List<PortletLink> result) {
                    if (result != null && result.size() > 0) {
                        store.add(result);
                        portlet.setHeight(result.size() * 45);
                    }
                }
            });
            add(portlet, panelCount++ % columnCount);
        }
    }

    protected void addChangeLog() {
        if (isShowChangeLog()) {
            final PortletChangeLog portlet = new PortletChangeLog();
            add(portlet, panelCount++ % columnCount);
        }
    }

    protected void addMessagesIn() {
        //if (isShowMessagesIn()) {
        final MessagesInPortlet portlet = new MessagesInPortlet();
        add(portlet, panelCount++ % columnCount);
        /*portlet.add(new Button("send", new SelectionListener<ButtonEvent>() {
         @Override
         public void componentSelected(ButtonEvent ce) {
         sendMessage();
         }
         }));*/

        
        //}
    }

    protected void addMessagesOut() {
        //if (isShowMessagesIn()) {
        final MessagesOutPortlet portlet = new MessagesOutPortlet();
        add(portlet, panelCount++ % columnCount);
        //}
    }

    protected void addMessagesReglament() {
        //if (isShowMessagesIn()) {
        final MessagesReglamentPortlet portlet = new MessagesReglamentPortlet();
        add(portlet, panelCount++ % columnCount);
        //}
    }



    protected boolean hasFacility(String module) {
        return ConfigurationManager.isModuleAvailable(module);
    }




}
