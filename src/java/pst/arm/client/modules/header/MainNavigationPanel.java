package pst.arm.client.modules.header;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.common.service.remote.GWTCommonServiceAsync;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.datagrid.pages.*;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MainNavigationPanel extends ContentPanel {

    private static final Logger logger = Logger.getLogger("MainNavigationPanel");
    private ArmImages images;
    protected BaseConstants constants;
    private static TreePanel<ModelData> tree;
    private ToolBar buttonBar;
    private BasePageNew parent;
    private GWTCommonServiceAsync service = null; //сервис отвечающий за обработку данных

    public static void clearTreeState() {
        tree.clearState();
    }

    public MainNavigationPanel(BasePageNew p) {
        setBorders(false);
        images = (ArmImages) GWT.create(ArmImages.class);
        constants = (BaseConstants) GWT.create(BaseConstants.class);
        parent = p;
        setHeading(constants.headerNavigation());
        setLayout(new FitLayout());
        initView();
        togleCurrentModule(); //находим текущий пункт
        initListener();
    }

    private void initListener() {
        Menu menu = new Menu();
        MenuItem menuItemOpen = new MenuItem("Открыть в новой вкладке");
       
        menuItemOpen.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                if (tree.getStore().getChildCount(tree.getSelectionModel().getSelectedItem()) > 0) {
                    MessageBox.info("", "Выберите пункт меню.", null);
                    return; //папка
                }
                String page = tree.getSelectionModel().getSelectedItem().get("page").toString();
                String query = tree.getSelectionModel().getSelectedItem().get("query").toString();
                final String url = page + ".htm?";

                if (page == null || page.equalsIgnoreCase("main")) {
                    Window.open(getHomeUrl(), "", "");
                    return;
                }

                final AsyncCallback<Void> callback_addOpenTable = new AsyncCallback<Void>() {
                    public void onSuccess(Void result) {
                        Window.open(GWT.getHostPageBaseURL() + url, "", "");
                    }

                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }
                };
                getService().addOpenTable(page, query, callback_addOpenTable);

                togleCurrentModule();
            }
        });            
        menu.add(menuItemOpen);
        tree.setContextMenu(menu);
                
        tree.addListener( Events.OnClick, new Listener<BaseEvent>() {
             @Override
            public void handleEvent(BaseEvent be) {
             if (tree.getSelectionModel().getSelectedItem() != null) {
                    if (tree.getStore().getChildCount(tree.getSelectionModel().getSelectedItem()) > 0) {
                        return; //папка
                    }
                    String page = ConfigurationManager.getModuleName();
                    String newPage = tree.getSelectionModel().getSelectedItem().get("page").toString();
                    String query = tree.getSelectionModel().getSelectedItem().get("query").toString();
                    String url = newPage + ".htm?";
                    if (page == null || (newPage.equalsIgnoreCase("main") && !page.equalsIgnoreCase(newPage))) {
                        Window.Location.assign(getHomeUrl());
                    } else if (newPage.equalsIgnoreCase("datagridcontractstat"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridcontracttype"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridpost"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddepart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridcompany"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridworkers"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddepartpost"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridordertype"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridmilitaryrepr"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorganization"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredfundingsource"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridrednds"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredevalofgetting"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridrednormavsalary"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridchoicenormavsalary"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridrednormavsalaryaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridreddepartpostaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredcontractaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredleveltaskaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredorderdaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredorganizationaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredpostworkeraudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredpostworkernaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredservcontractstageaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredworkplanaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredworkeraudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredworker1caudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredpost1caudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredpostaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridreddepart1caudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridreddepartaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridreddepartexecutorfactaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredocpaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridrednotifopeningaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredofficedocaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredofficedoccontraudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredusersloginaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredappproductionaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridreddhelaborationofddaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridredusersaudit"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridcontracts"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridnotifopening"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridocp"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridplanniokr"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridplanniokrimpl"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridplanntoyear"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderblankwork"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderblankwork2"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderblankwork3"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderblankwork4"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderblankwork5"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } 
                    else if (newPage.equalsIgnoreCase("datagridcontractsdop"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddepworkload"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridorderds"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridinteractingsyst"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridpossstep"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridfileroot"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridfacilities"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridofficedoc"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddhorderpriboy"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddhordermart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridstorepartrestpriboy"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridstorepartrestmart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    }else if (newPage.equalsIgnoreCase("datagriddhfacilitiespriboy"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddhfacilitiesmart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridcommander"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddoctypeig"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridofficedoccontrig"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridpost1c"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridworker1c"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddepart1c"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddeparttype"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridocprightsforrecig2"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagriddocperiodtypeig"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridtypeofdepart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridworkshoppriboy"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                    } else if (newPage.equalsIgnoreCase("datagridworkshopmart"))// && page.equalsIgnoreCase(newPage) )  //отдельно расписываем обработку модуль datagrid
                    {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridworkshoppriboyref")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridworkshopmartref")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriddhorder1")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriddesignig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridoperationstypeig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriddepartmentsig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridmassmailingig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridprodcalendarig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriduserrolesig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridrolefacilitiesig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridnews")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridnewstype")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridworkshopload")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriddhelaborationofdd")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridappproduction")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridnotifclosingig")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagriddepartforprocess")) {
                        addOpenTable(newPage, query);
                    } else if (newPage.equalsIgnoreCase("datagridheaderspec")) {
                        addOpenTable(newPage, query);
                    }
                    else if (newPage.equalsIgnoreCase("datagriduserdepart"))
                    {
                        addOpenTable(newPage, query); //кладу в глобальный массив название открываемой таблицы
                        //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
                    } else if (newPage.equalsIgnoreCase("datagridlaborandpayrollanalytics")) {
                        addOpenTable(newPage, query);
                    }
                    else if (!page.equalsIgnoreCase(newPage) && !tree.getSelectionModel().getSelectedItem().get("parent").toString().isEmpty())//не папка
                    {
                        Window.Location.assign(GWT.getHostPageBaseURL() + url);
                    }
                }
            }
        });
    }

    // Про ModelKeyProvider: https://code.google.com/p/gxt-rtl/source/browse/src/com/extjs/gxt/samples/client/examples/treepanel/AsyncTreePanelExample.java?r=c5a981e9d464b132903eefc2446673c7d9b12a21
    public void initView() {
        TreeStore<ModelData> store = new TreeStore<ModelData>();
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {

            @Override
            public String getKey(ModelData model) {
                return "node_" + model.get("id");
            }
        });


        NavigationModel model = getTreeModel();

        store.add(model.getChildren(), true);

        tree = new TreePanel<ModelData>(store);
        tree.setDisplayProperty("name");
        tree.getStyle().setLeafIcon(images.card());
        tree.setAutoExpand(false);
        tree.setStateful(true); // поставить false или просто убрать, если не хотим, чтобы при открытии нового пункта меню сохранялись открытые item-ы дерева меню
        tree.setId("mainmenutree"); // tree.setId() необходим обязательно, используется браузером, без него сохраняться просмотренные item-ы дерева будут с глюками

        buttonBar = new ToolBar();
        buttonBar.setStyleAttribute("padding", "0 0 0 3");
        //buttonBar.setBorders(false);
        buttonBar.setBorders(true);

        Button btnCollapseAll = new Button(null, new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                tree.collapseAll();
            }
        });
        btnCollapseAll.setIcon(images.tree_minus());
        buttonBar.add(btnCollapseAll);

        Button btnExpandAll = new Button(null, new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                tree.expandAll();
            }
        });
        btnExpandAll.setIcon(images.tree_plus());
        buttonBar.add(btnExpandAll);

        setTopComponent(buttonBar);
        add(tree);

    }

    class NavigationModel extends BaseTreeModel {

        private Boolean isVisible;

        public Boolean getIsVisible() {
            return isVisible;
        }

        public NavigationModel(String id, String page, String name, String query) {

            set("id", id);
            set("page", page);
            set("name", name);
            set("query", query);
            set("parent", "");
            isVisible = hasFacility(page);//Boolean.TRUE;
        }

        public NavigationModel(String id, String page, String name) {

            set("id", id);
            set("page", page);
            set("name", name);
            set("query", "");
            set("parent", "");
            isVisible = hasFacility(page);//Boolean.TRUE;
        }

        private Boolean isVisible(BaseTreeModel children) {
            Boolean rc = Boolean.FALSE;
            if (children.getChildCount() == 0) //срашиваем только не у папок
            {
                if (hasFacility(children.get("page").toString())) {
                    rc = Boolean.TRUE;
                }
            } else //если папка идем глубже
            {
                for (int j = 0; j < children.getChildCount(); j++) {
                    if (isVisible((BaseTreeModel) children.getChild(j))) {
                        rc = Boolean.TRUE;
                        break;
                    }
                }
            }
            return rc;
        }

        public NavigationModel(String id, String page, String name, BaseTreeModel[] children) {

            set("id", id);
            set("page", page);
            set("name", name);
            set("query", "");
            set("parent", "");
            isVisible = Boolean.FALSE;
            for (int i = 0; i < children.length; i++) {
                if (isVisible(children[i])) {
                    isVisible = Boolean.TRUE;
                    break;
                }
            }
            for (int i = 0; i < children.length; i++) {
                if ((children[i].getChildCount() > 0 && ((NavigationModel) children[i]).getIsVisible()) //если папка то для нее уже должно быть определено видима ли она
                        || hasFacility(children[i].get("page").toString())) //если лист то проверяем можно ли его показывать
                {
                    children[i].set("parent", id);
                    add(children[i]);
                }

            }
        }
    }

    private NavigationModel getTreeModel() {

        NavigationModel[] folders = new NavigationModel[]{
            new NavigationModel("1", "main", constants.headerGroupGoTo()),//main

            new NavigationModel("2", "groupdataais", "Контроль производства", new NavigationModel[]{
                new NavigationModel("21", "powerproduction", "Потенциальные возможности производства цехов", ""),
                new NavigationModel("25", "controlproduction", "Контроль", ""),
                new NavigationModel("26", "datagriddepworkload", constants.headerDepWorkload(), "DEP_WORKLOAD_HLV"),
                new NavigationModel("27", "datagridlaborandpayrollanalytics", constants.headerLaborAndPayrollAnalytics(), "labor_and_payroll_analytics"),
                new NavigationModel("22", "groupdataaisyear", "Графики выполнения договоров", new NavigationModel[]{
                    new NavigationModel("221", "aiscontracts", constants.headerBtnAisContractrs(), ""),
                    new NavigationModel("222", "generalgraphic", "Генеральный график", ""),
                    new NavigationModel("223", "nullpage", "Оперативный график", ""),
                    new NavigationModel("224", "ganttchart", constants.headerGanttChart(), "")
                }),
                new NavigationModel("23", "groupdataaisdiagram", "Диаграммы состояния", new NavigationModel[]{
                    new NavigationModel("231", "workcontent", "Трудоемкость", ""),
                    new NavigationModel("232", "nullpage", "Рабочие планы", ""),
                    new NavigationModel("233", "nullpage", "Производственные планы", ""),
                    new NavigationModel("234", "nullpage", "Отставание от планов", ""),
                    new NavigationModel("235", "nullpage", "Перенос сроков", "")
                }),
                new NavigationModel("24", "groupdataaisdiagram", "Финансовые диаграммы", new NavigationModel[]{
                    new NavigationModel("241", "payment", "Доходы", ""),
                    new NavigationModel("242", "financplangraphic", "Финансовый план", ""),
                    new NavigationModel("243", "nullpage", "Прибыль", ""),
                    new NavigationModel("244", "nullpage", "Плановые затраты", "")
                })}),
            new NavigationModel("3", "groupjournals", constants.headerJournals(), new NavigationModel[]{
                new NavigationModel("31", "datagridcontracts", constants.headerContracts(), "contracts"),
                new NavigationModel("32", "datagridcontractsdop", constants.headerContractsDop(), "CONTRACTS_VO_DOP"),
                new NavigationModel("33", "datagridnotifopening", constants.headerNotifOpening(), "NOTIF_OPENING_VO"),
                new NavigationModel("331", "datagridnotifclosingig", constants.headerNotifClosingIg(), "NOTIF_CLOSING_IG"),
                new NavigationModel("34", "datagridorderds", constants.headerOrderDs(), "ORDER_D_VO_DS"),
                new NavigationModel("35", "datagridocp", constants.headerOCP(), "ocp_vo"),
                new NavigationModel("36", "datagridplanniokr", constants.headerPlanNiokr(), "plan_niokr"),
                new NavigationModel("37", "datagridplanniokrimpl", constants.headerPlanNiokrImpl(), "plan_niokr_impl"),
                new NavigationModel("38", "datagridplanntoyear", constants.headerPlanNtoYear(), "plan_nto_year_vo"),
                new NavigationModel("380", "groupdatagridorderblank", constants.headerOrderBlank(), new NavigationModel[]{
                    new NavigationModel("381", "datagridorderblankwork", constants.headerOrderBlankWork(), "order_blank_work"),
                    new NavigationModel("382", "datagridorderblankwork2", constants.headerOrderBlankWork2(), "order_blank_work2"),
                    new NavigationModel("383", "datagridorderblankwork3", constants.headerOrderBlankWork3(), "order_blank_work3"),
                    new NavigationModel("384", "datagridorderblankwork4", constants.headerOrderBlankWork4(), "order_blank_work4"),
                    new NavigationModel("385", "datagridorderblankwork5", constants.headerOrderBlankWork5(), "order_blank_work5"),}),
                new NavigationModel("39", "datagridofficedoc", constants.headerOfficeDoc(), "office_doc_vo"),
                new NavigationModel("40", "datagridofficedoccontrig", constants.headerOfficeDocContrIg(), "office_doc_contr_ig")
            }),
            new NavigationModel("4", "productionpriboy", constants.headerProductionPriboy(), new NavigationModel[]{
                new NavigationModel("41", "datagriddhorderpriboy", constants.headerDhOrderPriboy(), "dh_order_priboy_hlv"),
                new NavigationModel("42", "datagriddhfacilitiespriboy", constants.headerDhFacilitiesPriboy(), "dh_facilities_priboy_ig"),
                new NavigationModel("43", "datagridworkshoppriboy", constants.headerWorkshopPriboy(), "workshop_priboy_ig"),
                new NavigationModel("44", "datagridworkshoppriboyref", constants.headerWorkshopPriboyRef(), "spr_depart_priboy_ref_ig"),
                new NavigationModel("45", "updateplanningpriboy", "Обновление справочников для планирования", ""),
                new NavigationModel("46", "datagriddhorder1", "Планирование выполнения заказов", "dh_order_1_vo"),
                new NavigationModel("47", "datagridworkshopload", "Плановая загруженность производства", "workshop_load_vo"),
                new NavigationModel("48", "datagriddhelaborationofdd", constants.headerDhElaborationOfDd(), "dh_elaboration_of_dd_hlv"),
                new NavigationModel("49", "datagridappproduction", constants.headerAppProduction(), "app_production_hlv"),
                new NavigationModel("410", "datagridstorepartrestpriboy", "Наличие номенклатуры на складах", "store_part_rest_priboy_vo"),
            }),
            new NavigationModel("5", "productionmart", constants.headerProductionMart(), new NavigationModel[]{
                new NavigationModel("51", "datagriddhordermart", constants.headerDhOrderMart(), "dh_order_mart_hlv"),
                new NavigationModel("52", "datagriddhfacilitiesmart", constants.headerDhFacilitiesMart(), "dh_facilities_mart_ig"),
                new NavigationModel("53", "datagridworkshopmart", constants.headerWorkshopMart(), "workshop_mart_ig"),
                new NavigationModel("54", "datagridworkshopmartref", constants.headerWorkshopMartRef(), "spr_depart_mart_ref_ig"),
                new NavigationModel("55", "updateplanningmart", "Обновление справочников для планирования", ""),
                new NavigationModel("56", "datagridstorepartrestmart", "Наличие номенклатуры на складах", "store_part_rest_mart_vo"),
            }),
            new NavigationModel("6", "planningcatalogs", constants.headerPlanningCatalogs(), new NavigationModel[]{
                new NavigationModel("61", "datagriddesignig", constants.headerDesignIg(), "design_ig"),
                new NavigationModel("62", "datagridoperationstypeig", constants.headerOperationsTypeIg(), "operations_type_ig"),
                new NavigationModel("63", "datagriddepartmentsig", constants.headerDepartmentsIg(), "departments_ig"),
                new NavigationModel("64", "datagridprodcalendarig", constants.headerProdCalendarIg(), "prod_calendar_ig")
            }),
            new NavigationModel("8", "facilitiesandworkers", constants.headerFacilitiesAndWorkers(), new NavigationModel[]{
                new NavigationModel("81", "datagridworkers", constants.headerWorkers(), "worker_extended"),
                new NavigationModel("82", "datagriddepartpost", constants.headerDepartPost(), "depart_post"),
                new NavigationModel("83", "datagridpost", constants.headerPost(), "post"),
                new NavigationModel("84", "datagriddepart", constants.headerDepart(), "depart_structure"),
                new NavigationModel("85", "datagridcompany", constants.headerCompany(), "company"),
                new NavigationModel("86", "datagridorganization", constants.headerOrganization(), "organization"),
                new NavigationModel("87", "datagriddeparttype", constants.departType(), "depart_type_vo")
            }),
            new NavigationModel("98", "admin", constants.headerGroupAdministration(), new NavigationModel[]{ //admin
                new NavigationModel("981", "admstat", constants.headerBtnAdmStat(), ""),
                new NavigationModel("982", "admroles", constants.headerBtnAdmRoles(), ""),
                new NavigationModel("983", "admusers", constants.headerBtnAdmUsers(), ""),
                new NavigationModel("9832", "datagriduserrolesig", constants.headerUserRolesIg(), "user_roles_ig"),
                new NavigationModel("98321", "datagridrolefacilitiesig", constants.headerRoleFacilitiesIg(), "role_facilities_ig"),
                new NavigationModel("9833", "datagridcommander", constants.headerCommander(), "commander"),
                new NavigationModel("9834", "datagridocprightsforrecig2", constants.headerOcpRightsForRecIg2(), "ocp_rights_for_rec_ig2"),
                new NavigationModel("9835", "datagriduserdepart", constants.headerUserDepart(), "hlv_user_depart"),
                new NavigationModel("9836", "datagriddepartforprocess", constants.headerDepartForProcess(), "user_depart_for_process_vo"),
                new NavigationModel("984", "systemdicts", "Системные справочники", new NavigationModel[]{
                    new NavigationModel("9846", "datagriddoctypeig", constants.headerDocTypeIg(), "doc_type_ig"),
                    new NavigationModel("9847", "datagridcontracttype", constants.headerContractType(), "contract_type"),
                    new NavigationModel("9841", "datagridordertype", constants.headerOrderType(), "order_type"),
                    new NavigationModel("9849", "datagridmilitaryrepr", constants.headerMilitaryRepr(), "military_repr"),
                    new NavigationModel("98410", "datagridredfundingsource", constants.headerRedFundingSource(), "redfunding_source"),
                    new NavigationModel("98411", "datagridrednds", constants.headerRedNds(), "rednds"),
                    new NavigationModel("9842", "datagridcontractstat", constants.headerContractStat(), "contract_stat"),
                    new NavigationModel("9848", "datagridredevalofgetting", constants.headerRedEvalOfGetting(), "redeval_of_getting"),
                    new NavigationModel("98488", "datagridinteractingsyst", constants.headerInteractingSyst(), "interacting_syst"),
                    new NavigationModel("98412", "datagridpossstep", constants.headerPossStep(), "poss_step_hlv"),
                    new NavigationModel("98413", "datagridfileroot", constants.headerFileRoot(), "file_root_hlv"),
                    new NavigationModel("98414", "datagridfacilities", constants.headerFacilities(), "facilities_hlv"),
                    new NavigationModel("98415", "datagriddocperiodtypeig", constants.headerDocPeriodTypeIg(), "doc_period_type_ig"),
                    new NavigationModel("98416", "datagridtypeofdepart", constants.headerTypeOfDepart(), "type_of_depart"),
                    new NavigationModel("98417", "datagridmassmailingig", constants.headerMassMailingIg(), "mass_mailing_ig"),
                    new NavigationModel("98418", "datagridnews", constants.headerNews(), "news_hlv"),
                    new NavigationModel("98419", "datagridnewstype", constants.headerNewsType(), "news_type_hlv"),
                    new NavigationModel("98420", "datagridheaderspec", constants.headerHeaderSpec(), "header_spec")
                }),
                new NavigationModel("985", "systemnorm", "Нормативы", new NavigationModel[]{
                    new NavigationModel("9851", "datagridrednormavsalary", constants.headerRedNormAvSalary(), "rednorm_av_salary"),
                    new NavigationModel("9853", "datagridchoicenormavsalary", constants.headerChoiceNormAvSalary(), "choicenorm_av_salary")
                }),
                new NavigationModel("986", "tablesaudit", "История изменений", new NavigationModel[]{
                    new NavigationModel("9860", "datagridredusersloginaudit", constants.headerRedUsersLoginAudit(), "redusers_login_audit"),
                    new NavigationModel("9861", "datagridredcontractaudit", constants.headerRedContractAudit(), "redcontract_audit"),
                    new NavigationModel("9862", "datagridreddepartpostaudit", constants.headerRedDepartPostAudit(), "reddepart_post_audit"),
                    new NavigationModel("9863", "datagridredleveltaskaudit", constants.headerRedLevelTaskAudit(), "redlevel_task_audit"),
                    new NavigationModel("9864", "datagridrednormavsalaryaudit", constants.headerRedNormAvSalaryAudit(), "rednorm_av_salary_audit"),
                    new NavigationModel("9865", "datagridredorderdaudit", constants.headerRedOrderDAudit(), "redorder_d_audit"),
                    new NavigationModel("9866", "datagridredorganizationaudit", constants.headerRedOrganizationAudit(), "redorganization_audit"),
                    new NavigationModel("9867", "datagridredpostworkeraudit", constants.headerRedPostWorkerAudit(), "redpost_worker_audit"),
                    new NavigationModel("9868", "datagridredpostworkernaudit", constants.headerRedPostWorkerNAudit(), "redpost_worker_n_audit"),
                    new NavigationModel("9869", "datagridredservcontractstageaudit", constants.headerRedServContractStageAudit(), "redserv_contract_stage_audit"),
                    new NavigationModel("98610", "datagridredworkplanaudit", constants.headerRedWorkPlanAudit(), "redwork_plan_audit"),
                    new NavigationModel("98611", "datagridredworkeraudit", constants.headerRedWorkerAudit(), "redworker_audit"),
                    new NavigationModel("98612", "datagridredocpaudit", constants.headerRedOCPAudit(), "redocp_audit"),
                    new NavigationModel("98613", "datagridrednotifopeningaudit", constants.headerRedNotifOpeningAudit(), "rednotif_opening_audit"),
                    new NavigationModel("98614", "datagridredofficedocaudit", constants.headerRedOfficeDocAudit(), "redoffice_doc_audit"),
                    new NavigationModel("98615", "datagridredofficedoccontraudit", constants.headerRedOfficeDocContrAudit(), "redoffice_doc_contr_audit"),
                    new NavigationModel("98616", "datagridredworker1caudit", constants.headerRedWorker1cAudit(), "redworker_1c_audit"),
                    new NavigationModel("98617", "datagridredpost1caudit", constants.headerRedPost1cAudit(), "redpost_1c_audit"),
                    new NavigationModel("98618", "datagridredpostaudit", constants.headerRedPostAudit(), "redpost_audit"),
                    new NavigationModel("98619", "datagridredappproductionaudit", constants.headerRedAppProductionAudit(), "redapp_production_audit"),
                    new NavigationModel("98620", "datagridreddepartaudit", constants.headerRedDepartAudit(), "reddepart_audit"),
                    new NavigationModel("98621", "datagridreddepartexecutorfactaudit", constants.headerRedDepartExecutorFactAudit(), "reddepart_executor_fact_audit"),
                    new NavigationModel("98622", "datagridreddepart1caudit", constants.headerRedDepart1cAudit(), "reddepart_1c_audit"),
                    new NavigationModel("98623", "datagridreddhelaborationofddaudit", constants.headerRedDhElaborationOfDdAudit(), "reddh_elaboration_of_dd_audit"),
                    new NavigationModel("98624", "datagridredusersaudit", constants.headerRedUsersAudit(), "redusers_audit")
                })
            }),
            new NavigationModel("99", "technology", constants.headerGroupTechnology(), new NavigationModel[]{ //technology
                new NavigationModel("991", "nomenclature", constants.headerBtnNomenclatureMatching(), ""),
                new NavigationModel("992", "nomenclatureresults", constants.headerBtnNomenclatureMatchingResults(), ""),
                new NavigationModel("993", "aiscontractssync", constants.headerBtnAISContractsSync(), new NavigationModel[]{
                    new NavigationModel("9931", "aiscontractssyncservices", constants.headerBtnAISContractsSyncServices(), ""),
                    new NavigationModel("9932", "aiscontractssyncsupply", constants.headerBtnAISContractsSyncSupply(), ""),
                    new NavigationModel("9933", "aissyncsupplyadditional", constants.headerBtnAISContractsSyncSupplyAdditional(), "")
                }),
                new NavigationModel("990", "datafrom1c", constants.headerDataFrom1c(), new NavigationModel[]{
                    new NavigationModel("9901", "datagridpost1c", constants.headerPost1c(), "post_1c"),
                    new NavigationModel("9902", "datagridworker1c", constants.headerWorker1c(), "worker_1c"),
                    new NavigationModel("9903", "sync1c", constants.headerSync1c(), ""),
                    new NavigationModel("9904", "datagriddepart1c", constants.headerDepart1c(), "depart_1c")
                })
            }),
            new NavigationModel("100", "test", constants.headerGroupTest(), new NavigationModel[]{ //test
                //new NavigationModel("971", "test", constants.headerGroupRequest(), ""),
                new NavigationModel("1001", "test", "Тестовая страница", ""),
                new NavigationModel("1002", "mymodule", "Тестируем дерево", ""),
                new NavigationModel("1003", "imagegallery", "imagegallery", ""),
                new NavigationModel("1004", "tablegrid", constants.headerBtnTestRequestStatus(), "")
            })
        };

        NavigationModel root = new NavigationModel("0", "root", "");
        for (int i = 0; i < folders.length; i++) {
            if (((NavigationModel) folders[i]).getIsVisible()) {
                root.add((NavigationModel) folders[i]);
            }
        }

        return root;
    }

    protected boolean hasFacility(String module) {
        //return true;
        return ConfigurationManager.isModuleAvailable(module) || ConfigurationManager.getPropertyAsBoolean(module);
    }

    protected String getHomeUrl() {
        return GWT.getHostPageBaseURL() + "main.htm?" + AppHelper.getInstance().debugUrl();
    }

    protected void togleCurrentModule() {
        tree.getSelectionModel().deselectAll();
        String page = ConfigurationManager.getModuleName();
        if (page == null) {
            return;
        }
        String url = page + ".htm?";

        List<ModelData> list = tree.getStore().getAllItems();
        for (int i = 0; i < list.size(); i++) {
            ModelData m = list.get(i);
            if (m == null) {
                continue;
            }
            String newPage = m.get("page");
            String newUrl = newPage + ".htm?";

            // if (m != null && m.get("page").toString().equalsIgnoreCase(page)) {
            if (url.equalsIgnoreCase(newUrl)) {
                List<ModelData> sel = new ArrayList<ModelData>();
                sel.add(m);
                tree.getSelectionModel().setSelection(sel);
            }
        }
    }

    protected GWTCommonServiceAsync getService() {
        if (service == null) {
            service = GWT.create(GWTCommonService.class);
        }
        return service;
    }

    //кладу в глобальный массив название открываемой таблицы
    //жду ожидания окончания отого процесса и только потом загружаю соответсвущую страницу
    protected void addOpenTable(String page, String tableName) {
        // Create an asynchronous callback to handle the result.
        final AsyncCallback<Void> callback_addOpenTable = new AsyncCallback<Void>() {

            public void onSuccess(Void result) {
                if (tree.getSelectionModel().getSelectedItem() != null) {
                    if (tree.getStore().getChildCount(tree.getSelectionModel().getSelectedItem()) > 0) {
                        return; //папка
                    }
                    String page = ConfigurationManager.getModuleName();
                    String newPage = tree.getSelectionModel().getSelectedItem().get("page").toString();
                    String query = tree.getSelectionModel().getSelectedItem().get("query").toString();
                    String url = newPage + ".htm?";
                    if (!page.equalsIgnoreCase(newPage)) //первое открытие словаря (datagrid)
                    {
                        Window.Location.assign(GWT.getHostPageBaseURL() + url);
                    } else {
                        if (parent instanceof DataGridContractStat) { //повторное открытие ( добавляем закладки )
                            ((DataGridContractStat) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridContractType) { //повторное открытие ( добавляем закладки )
                            ((DataGridContractType) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPost) { //повторное открытие ( добавляем закладки )
                            ((DataGridPost) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOrderType) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderType) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridMilitaryRepr) { //повторное открытие ( добавляем закладки )
                            ((DataGridMilitaryRepr) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridOrganization) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrganization) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedFundingSource) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedFundingSource) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedNds) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedNds) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedEvalOfGetting) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedEvalOfGetting) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedNormAvSalary) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedNormAvSalary) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        
                        if (parent instanceof DataGridChoiceNormAvSalary) { //повторное открытие ( добавляем закладки )
                            ((DataGridChoiceNormAvSalary) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedContractAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedContractAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedLevelTaskAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedLevelTaskAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedOrderDAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedOrderDAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedDepartPostAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedDepartPostAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedNormAvSalaryAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedNormAvSalaryAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedOrganizationAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedOrganizationAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedPostWorkerAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedPostWorkerAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedPostWorkerNAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedPostWorkerNAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedServContractStageAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedServContractStageAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedWorkPlanAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedWorkPlanAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedWorkerAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedWorkerAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedWorker1cAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedWorker1cAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedPost1cAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedPost1cAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedPostAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedPostAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedDepart1cAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedDepart1cAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedDepartAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedDepartAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedDepartExecutorFactAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedDepartExecutorFactAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedOCPAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedOCPAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedNotifOpeningAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedNotifOpeningAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedOfficeDocAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedOfficeDocAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedOfficeDocContrAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedOfficeDocContrAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedUsersLoginAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedUsersLoginAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedAppProductionAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedAppProductionAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedDhElaborationOfDdAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedDhElaborationOfDdAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridRedUsersAudit) { //повторное открытие ( добавляем закладки )
                            ((DataGridRedUsersAudit) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridDepart) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridCompany) { //повторное открытие ( добавляем закладки )
                            ((DataGridCompany) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridDepartPost) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepartPost) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridWorkers) { //повторное открытие ( добавляем закладки )
                            ((DataGridWorkers) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridContracts) { //повторное открытие ( добавляем закладки )
                            ((DataGridContracts) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDepWorkload) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepWorkload) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridNotifOpening) { //повторное открытие ( добавляем закладки )
                            ((DataGridNotifOpening) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }

                        if (parent instanceof DataGridOCP) { //повторное открытие ( добавляем закладки )
                            ((DataGridOCP) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPlanNiokr) { //повторное открытие ( добавляем закладки )
                            ((DataGridPlanNiokr) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPlanNiokrImpl) { //повторное открытие ( добавляем закладки )
                            ((DataGridPlanNiokrImpl) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPlanNtoYear) { //повторное открытие ( добавляем закладки )
                            ((DataGridPlanNtoYear) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOrderBlankWork) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderBlankWork) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }     
                        if (parent instanceof DataGridOrderBlankWork2) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderBlankWork2) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }  
                        if (parent instanceof DataGridOrderBlankWork3) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderBlankWork3) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }   
                        if (parent instanceof DataGridOrderBlankWork4) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderBlankWork4) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        } 
                        if (parent instanceof DataGridOrderBlankWork5) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderBlankWork5) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }    
                        if (parent instanceof DataGridOrderDs) { //повторное открытие ( добавляем закладки )
                            ((DataGridOrderDs) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridInteractingSyst) { //повторное открытие ( добавляем закладки )
                            ((DataGridInteractingSyst) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPossStep) { //повторное открытие ( добавляем закладки )
                            ((DataGridPossStep) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridFileRoot) { //повторное открытие ( добавляем закладки )
                            ((DataGridFileRoot) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridFacilities) { //повторное открытие ( добавляем закладки )
                            ((DataGridFacilities) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridCommander) { //повторное открытие ( добавляем закладки )
                            ((DataGridCommander) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOfficeDoc) { //повторное открытие ( добавляем закладки )
                            ((DataGridOfficeDoc) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhOrderPriboy) { //повторное открытие ( добавляем закладки )
                            ((DataGridDhOrderPriboy) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhOrderMart) { //повторное открытие ( добавляем закладки )
                            ((DataGridDhOrderMart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhFacilitiesPriboy) { //повторное открытие ( добавляем закладки )
                            ((DataGridDhFacilitiesPriboy) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhFacilitiesMart) { //повторное открытие ( добавляем закладки )
                            ((DataGridDhFacilitiesMart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDocTypeIg) { //повторное открытие ( добавляем закладки )
                            ((DataGridDocTypeIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOfficeDocContrIg) { //повторное открытие ( добавляем закладки )
                            ((DataGridOfficeDocContrIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridPost1c) { //повторное открытие ( добавляем закладки )
                            ((DataGridPost1c) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorker1c) { //повторное открытие ( добавляем закладки )
                            ((DataGridWorker1c) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDepart1c) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepart1c) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOcpRightsForRecIg2) { //повторное открытие ( добавляем закладки )
                            ((DataGridOcpRightsForRecIg2) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridUserDepart) { //повторное открытие ( добавляем закладки )
                            ((DataGridUserDepart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDepartForProcess) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepartForProcess) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDocPeriodTypeIg) { //повторное открытие ( добавляем закладки )
                            ((DataGridDocPeriodTypeIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridTypeOfDepart) { //повторное открытие ( добавляем закладки )
                            ((DataGridTypeOfDepart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDepartType) { //повторное открытие ( добавляем закладки )
                            ((DataGridDepartType) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorkshopPriboy) { //повторное открытие ( добавляем закладки )
                            ((DataGridWorkshopPriboy) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorkshopMart) { //повторное открытие ( добавляем закладки )
                            ((DataGridWorkshopMart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorkshopPriboyRef) {
                            ((DataGridWorkshopPriboyRef) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorkshopMartRef) {
                            ((DataGridWorkshopMartRef) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhOrder1) {
                            ((DataGridDhOrder1) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridWorkshopLoad) {
                            ((DataGridWorkshopLoad) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDhElaborationOfDd) {
                            ((DataGridDhElaborationOfDd) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridAppProduction) {
                            ((DataGridAppProduction) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDesignIg) {
                            ((DataGridDesignIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridOperationsTypeIg) {
                            ((DataGridOperationsTypeIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridDepartmentsIg) {
                            ((DataGridDepartmentsIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridMassMailingIg) {
                            ((DataGridMassMailingIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridProdCalendarIg) {
                            ((DataGridProdCalendarIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridNews) {
                            ((DataGridNews) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridNewsType) {
                            ((DataGridNewsType) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridHeaderSpec) {
                            ((DataGridHeaderSpec) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridUserRolesIg) {
                            ((DataGridUserRolesIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridRoleFacilitiesIg) {
                            ((DataGridRoleFacilitiesIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridNotifClosingIg) {
                            ((DataGridRoleFacilitiesIg) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridStorePartRestMart) { //повторное открытие ( добавляем закладки )
                            ((DataGridStorePartRestMart) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridStorePartRestPriboy) { //повторное открытие ( добавляем закладки )
                            ((DataGridStorePartRestPriboy) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                        if (parent instanceof DataGridLaborAndPayrollAnalytics) {
                            ((DataGridLaborAndPayrollAnalytics) parent).addTabItem(query, true);
                            parent.getLayout().layout();
                        }
                    }
                }

            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        getService().addOpenTable(page, tableName, callback_addOpenTable);
    }

    public void selectCurrentItem(String currPage, String currQuery) {
        tree.getSelectionModel().deselectAll();

        if (currPage == null) {
            return;
        }

        List<ModelData> list = tree.getStore().getAllItems();
        for (int i = 0; i < list.size(); i++) {
            ModelData m = list.get(i);
            if (m == null) {
                continue;
            }
            String page = m.get("page");
            String query = m.get("query");
            if (page.equalsIgnoreCase(currPage) && query.equalsIgnoreCase(currQuery)) {
                List<ModelData> sel = new ArrayList<ModelData>();
                sel.add(m);
                tree.getSelectionModel().setSelection(sel);
                tree.scrollIntoView(m);
                break;
            }
        }
    }
}
