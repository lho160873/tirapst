package pst.arm.client.modules.admin.users;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.modules.admin.domain.UserSearchCondition;
import pst.arm.client.modules.admin.lang.AdminConstants;
import pst.arm.client.modules.admin.lang.AdminMessages;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridReportService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridReportServiceAsync;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author vorontsov
 */
public class UsersResultPanel extends ContentPanel {

    protected ToolBar toolbar;
    protected Button addButton;
    protected Button editButton;
    protected Button passwordButton;
    protected Button removeButton;
    protected Button btnReportSplit;
    protected Grid<BeanModel> grid;
    protected ListLoader<ListLoadResult<ModelData>> loader;
    protected GWTUserServiceAsync service;
    protected ArmImages images;
    protected AdminConstants constants;
    protected AdminMessages messages;
    protected User user;
    protected Menu menu;
    protected UserSearchCondition condition = new UserSearchCondition();
    protected Boolean deleting = false;
    protected String deletingUserName;
    private DateTimeFormat dateFormat;
    protected DTable table = null;
    protected GWTDDataGridServiceAsync serviceDataGrid = GWT.create(GWTDDataGridService.class);
    protected GWTDDataGridReportServiceAsync reportService = GWT.create(GWTDDataGridReportService.class);
    protected String tableName = "HLV_USERS_REPORT";
    protected final DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected final DataGridImages datagridImages = GWT.create(DataGridImages.class);
    protected Menu reportMenuSplit;
    

    public UsersResultPanel() {
        service = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
        constants = (AdminConstants) GWT.create(AdminConstants.class);
        messages = (AdminMessages) GWT.create(AdminMessages.class);
        images = (ArmImages) GWT.create(ArmImages.class);

        dateFormat = DateTimeFormat.getFormat(constants.timeformat());

        addButton = new Button(constants.create());
        addButton.setIcon(images.user_add());
        addButton.setIconAlign(IconAlign.LEFT);
        addButton.setScale(ButtonScale.SMALL);
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                addUser();
            }
        });

        editButton = new Button(constants.edit());
        editButton.setIcon(images.user_edit());
        editButton.setIconAlign(IconAlign.LEFT);
        editButton.setScale(ButtonScale.SMALL);
        editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                editUser();
            }
        });

        passwordButton = new Button(constants.setPassword());
        passwordButton.setIcon(images.user_go());
        passwordButton.setIconAlign(IconAlign.LEFT);
        passwordButton.setScale(ButtonScale.SMALL);
        passwordButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                setPassword();
            }
        });

        removeButton = new Button(constants.delete());
        removeButton.setIcon(images.user_delete());
        removeButton.setIconAlign(IconAlign.LEFT);
        removeButton.setScale(ButtonScale.SMALL);
        removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                removeUser();
            }
        });

        
        //Кнопка для выбора типа сформированного отчета (PDF, DOC или XLS)
        btnReportSplit = new Button(datagridConstants.btnReport());//"Сформировать отчёт")
        btnReportSplit.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSplit.setIconAlign(Style.IconAlign.LEFT);
        btnReportSplit.setEnabled(true);
        btnReportSplit.setVisible(true);

        MenuItem itemPdf = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdf.setId("0");
        itemPdf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("PDF");
            }
        });


        MenuItem itemRtf = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtf.setId("1");
        itemRtf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("RTF");
            }
        });

        MenuItem itemXls = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXls.setId("2");
        itemXls.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("XLS");
            }
        });
        
        reportMenuSplit = new Menu();
        reportMenuSplit.insert(itemXls, 0);
        reportMenuSplit.insert(itemRtf, 0);
        reportMenuSplit.insert(itemPdf, 0);
        
        btnReportSplit.setMenu(reportMenuSplit);
      

        
        toolbar = new ToolBar();
        toolbar.add(addButton);
        toolbar.add(new SeparatorToolItem());
        toolbar.add(editButton);
        toolbar.add(passwordButton);
        toolbar.add(new SeparatorToolItem());
        toolbar.add(removeButton);
        toolbar.add(btnReportSplit);
        setTopComponent(toolbar);

        MenuItem addItem = new MenuItem(constants.create());
        addItem.setIcon(images.user_add());
        addItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                addUser();
            }
        });

        MenuItem editItem = new MenuItem(constants.edit());
        editItem.setIcon(images.user_edit());
        editItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                editUser();
            }
        });

        MenuItem passwordItem = new MenuItem(constants.setPassword());
        passwordItem.setIcon(images.user_go());
        passwordItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                setPassword();
            }
        });

        MenuItem removeItem = new MenuItem(constants.delete());
        removeItem.setIcon(images.user_delete());
        removeItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                removeUser();
            }
        });

        menu = new Menu();
        menu.add(addItem);
        menu.add(new SeparatorMenuItem());
        menu.add(editItem);
        menu.add(passwordItem);
        menu.add(new SeparatorMenuItem());
        menu.add(removeItem);

        // proxy and reader
        RpcProxy<List<User>> proxy = new RpcProxy<List<User>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<List<User>> callback) {
                service.getUsers(getCondition(), callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        // loader and store
        loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        loader.addLoadListener(new LoadListener() {

//            @Override
//            public void handleEvent(LoadEvent e) {
//                super.handleEvent(e);
//                grid.unmask();
//            }
            @Override
            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);
                if (deleting) {
                    deleting = false;
                    MessageBox.info(constants.deleteUserTitle(), messages.userDeletedMessage(deletingUserName), null);
                }
            }
        });

        ListStore<BeanModel> store = new ListStore<BeanModel>();
        store.setDefaultSort("fio", Style.SortDir.DESC);
        store.addStoreListener(new StoreListener<BeanModel>() {

            @Override
            public void handleEvent(StoreEvent<BeanModel> e) {

                selectLastAccesedUser();
            }
        });

        // column model
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig ccArchiveId = new ColumnConfig("archiveId", constants.archiveIdColumn(), 100);
        ccArchiveId.setHidden(true);
        columns.add(ccArchiveId);
        columns.add(new ColumnConfig("id", constants.userIdColumn(), 150));
        columns.add(new ColumnConfig("userLogin", constants.userNameColumn(), 200));
        columns.add(new ColumnConfig("fio", constants.userFioColumn(), 300));
        columns.add(new ColumnConfig("dateCreated", constants.dateCreatedColumn(), 100));
        ColumnConfig cfg = new ColumnConfig("description", constants.descriptionColumn(), 300);       
        cfg.setSortable(false);
        columns.add(cfg);
        columns.add(new ColumnConfig("enabled", constants.enabledColumn(), 300));
        for (ColumnConfig columnConfig : columns) {
            columnConfig.setMenuDisabled(true);
            if (columnConfig.getId().equalsIgnoreCase("dateCreated")) { // #NLS
                columnConfig.setDateTimeFormat(DateTimeFormat.getFormat(constants.timeformat()));
            }

            if (columnConfig.getId().equalsIgnoreCase("userFio")) { // #NLS
                columnConfig.setRenderer(new CellUserFioRenderer());
            }

            if (columnConfig.getId().equalsIgnoreCase("userName")) { // #NLS
                columnConfig.setRenderer(new CellUserNameRenderer());
            }
            if (columnConfig.getId().equalsIgnoreCase("enabled")) { // #NLS
                columnConfig.setRenderer(new CellEnabledRenderer());
            }
        }

        ColumnModel cm = new ColumnModel(columns);

        grid = new Grid<BeanModel>(store, cm);
        grid.setStateId("user_grid");
        grid.setStateful(true);
        grid.setAutoExpandColumn("description"); // #NLS
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
        grid.mask(constants.loadUsersMask());
        grid.setStripeRows(true);
        grid.setContextMenu(menu);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {

            @Override
            public void handleEvent(GridEvent<BeanModel> be) {
                editUser();
            }
        });

        KeyNav keyNav = new KeyNav<ComponentEvent>(grid) {

            @Override
            public void onKeyPress(ComponentEvent ce) {
                if (ce.getKeyCode() == KeyCodes.KEY_ENTER) {
                    editUser();
                }
            }
        };

        setHeading(constants.userPanelHeader());
        setLayout(new FitLayout());
        add(grid);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        loadData();
    }

    private UserSearchCondition getCondition() {
        return condition;
    }

    protected void addUser() {
        UserDialog dialog = new UserDialog(this);
        dialog.show();
    }

    protected void editUser() {
        BeanModel selectedItem = grid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            User selectedUser = (User) selectedItem.getBean();
            UserDialog dialog = new UserDialog(this, selectedUser);
            dialog.show();
        }
    }

    private void setPassword() {
        BeanModel selectedItem = grid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            User selectedUser = (User) selectedItem.getBean();
            PasswordDialog dialog = new PasswordDialog(selectedUser);
            dialog.show();
        }
    }

    protected void removeUser() {
        BeanModel selectedItem = grid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            final User selectedUser = (User) selectedItem.getBean();
            if (selectedUser.getUserLogin().equalsIgnoreCase(ConfigurationManager.getGlobalProperty("currentUser"))) { // #NLS
                MessageBox.alert(constants.warn(), constants.deleteMyselfWarn(), null);
            } else {

                final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent ce) {
                        if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                            service.deleteUser(selectedUser, new AsyncCallback<Long>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    MessageBox.alert(constants.error(), constants.errorDeleteUser(), null);
                                }

                                @Override
                                public void onSuccess(Long result) {
                                    deleting = true;
                                    deletingUserName = selectedUser.getUserLogin();
                                    //loader.load();
                                    loadData();
                                }
                            });
                        }
                    }
                };
                MessageBox.confirm(constants.warn(),
                        constants.deleteUserConfirmation(), l);
            }
        }
    }

    protected boolean isUserExist(String userName) {
        List<BeanModel> selectedItems = grid.getStore().getModels();
        if (selectedItems != null) {
            for (BeanModel model : selectedItems) {
                User selectedUser = (User) model.getBean();
                if (userName.equals(selectedUser.getUserLogin())) {
                    return true;
                }
            }
        }

        return false;
    }

    public ListLoader<ListLoadResult<ModelData>> getLoader() {
        return loader;
    }

    public void selectLastAccesedUser() {
        List<BeanModel> models = grid.getStore().getModels();

        if (user == null) {
            if (models.size() > 0) {
                grid.getSelectionModel().select(0, false);
            }
        } else if (models != null) {
            for (BeanModel model : models) {
                User tempUser = (User) model.getBean();
                if (tempUser.getUserLogin().equalsIgnoreCase(user.getUserLogin())) {
                    grid.getSelectionModel().select(model, true);
                    return;
                }
            }

            if (models.size() > 0) {
                grid.getSelectionModel().select(0, false);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User lastAccessUser) {
        this.user = lastAccessUser;
    }

    void search(UserSearchCondition condition) {
        this.condition = condition;
        loadData();
    }

    public void loadData() {
        grid.mask(constants.loadUsersMask());
        service.getUsers(getCondition(), new AsyncCallback<List<User>>() {

            @Override
            public void onFailure(Throwable caught) {
                grid.unmask();
            }

            @Override
            public void onSuccess(List<User> result) {
                grid.getStore().removeAll();

                if (result != null && !result.isEmpty()) {
                    grid.getStore().add(BeanModelLookup.get().getFactory(User.class).createModel(result));
                    grid.getSelectionModel().select(0, false);

                }
                if (deleting) {
                    deleting = false;
                    MessageBox.info(constants.deleteUserTitle(), messages.userDeletedMessage(deletingUserName), null);
                }
                grid.unmask();
            }
        });
    }

    /**
     * Renderer for userFio column users grid.
     */
    private class CellUserFioRenderer implements GridCellRenderer<BeanModel> {

        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
            User user = (User) model.getBean();
            return user.getFio();
        }
    }

    /**
     * Renderer for userFio column users grid.
     */
    private class CellUserNameRenderer implements GridCellRenderer<BeanModel> {

        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
            User user = (User) model.getBean();
            return user.getUserLogin().replace("<", "&lt;").replace(">", "&gt;");
        }
    }
    
    /**
     * Renderer for userFio column users grid.
     */
    private class CellEnabledRenderer implements GridCellRenderer<BeanModel> {

        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
            User user = (User) model.getBean();
            if (user.getEnabled()==1)
                return "нет";
            return "да";
        }
    }

    
    
    protected void initTableInfo(final String reportType) {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                table = result;
                generateReport(reportType);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        serviceDataGrid.getTable(tableName, callback_getTable);
    }

     protected void generateReport(final String reportType) {
         DataGridSearchCondition conditionDataGrid = new DataGridSearchCondition(); //условия фильтрации данных
       
         PagingLoadConfig config = new BasePagingLoadConfig();
         Map<String, Object> state = grid.getState();
         if (state.containsKey("sortField")) {
             config.setSortField((String) state.get("sortField"));
             config.setSortDir(Style.SortDir.valueOf((String) state.get("sortDir")));

             DataGridSearchCondition.SortDir sortDir;
             switch (config.getSortDir()) {
                 case ASC:
                     sortDir = DataGridSearchCondition.SortDir.ASC;
                     break;

                 case DESC:
                     sortDir = DataGridSearchCondition.SortDir.DESC;
                     break;

                 default:
                     sortDir = DataGridSearchCondition.SortDir.NONE;
             }
             conditionDataGrid.setSortDir(sortDir);
             if (config.getSortField().equals("id")) {
                 conditionDataGrid.setSortFieldId("MAIN_USER_ID");
             } else if (config.getSortField().equals("fio")) {
                 conditionDataGrid.setSortFieldId("MAIN_NAME");
             } else if (config.getSortField().equals("userLogin")) {
                 conditionDataGrid.setSortFieldId("MAIN_USER_LOGIN");
             } else if (config.getSortField().equals("userLogin")) {
                 conditionDataGrid.setSortFieldId("MAIN_USER_LOGIN");
             } else if (config.getSortField().equals("dateCreated")) {
                 conditionDataGrid.setSortFieldId("MAIN_DATE_CREATED");
             } else if (config.getSortField().equals("description")) {
                 conditionDataGrid.setSortFieldId("MAIN_DESCRIPTION");
             } else if (config.getSortField().equals("enabled")) {
                 conditionDataGrid.setSortFieldId("MAIN_ENABLED");
             }
         }
                 
          SKeyForColumn keyForName = new SKeyForColumn("MAIN:ENABLED");
         IRowColumnVal val = table.createRowColumnVal(keyForName);
         val.setVal(1);
         conditionDataGrid.getSearches().put(keyForName, val);

         if (condition != null) {
            
             if (condition.getSearchNotActive() != null) {
                 if (condition.getSearchNotActive()) {
                     conditionDataGrid.getSearches().remove(keyForName);
                 }
             }
             if (condition.getFio() != null) {
                 if (!condition.getFio().isEmpty()) {
                      keyForName = new SKeyForColumn("MAIN:NAME");
                      val = table.createRowColumnVal(keyForName);
                     val.setVal(condition.getFio());
                     conditionDataGrid.getSearches().put(keyForName, val);
                 }
             }

             if (condition.getLogin() != null) {
                 if (!condition.getLogin().isEmpty()) {
                      keyForName = new SKeyForColumn("MAIN:USER_LOGIN");
                      val = table.createRowColumnVal(keyForName);
                     val.setVal(condition.getLogin());
                     conditionDataGrid.getSearches().put(keyForName, val);
                 }
             }
         }

         if (table.getReportTemplate() == null) {
            final Component cmp = this;

            serviceDataGrid.getDataGrid(tableName, conditionDataGrid, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("Внимание!", "Ошибка формирования отчета.", null);
                }

                @Override
                public void onSuccess(List<DDataGrid> result) {
                    reportService.generateHtmlArchiveStoreEntityReport(tableName, convertDomainToListOfHashMaps(result, table), reportType, new FileReportServiceCallback(cmp));
                }
            });

        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (table.getReportParams() != null) {
                for (Map.Entry<String, String> tp : table.getReportParams().entrySet()) {
                    for (Map.Entry filtr : conditionDataGrid.getFilters().entrySet()) {
                        SKeyForColumn fKey = (SKeyForColumn) filtr.getKey();
                        IRowColumnVal fVal = (IRowColumnVal) filtr.getValue();
                        String fStr = fVal.getStringFromVal(fKey, table.getColumnBuilder(fKey));
                        if (fKey.equals(new SKeyForColumn(tp.getValue()))) {
                            if (fStr == null) {
                                params.put(tp.getKey(), "");
                            } else {
                                params.put(tp.getKey(), fStr);
                            }
                        }
                    }
                }
            }
            reportService.generateJasperReport(table.getReportTemplate(), reportType, params, conditionDataGrid, table, new FileReportServiceCallback(this));
        }
     }        

    protected void showReport(final String reportType) {       
        if (table == null) {
            initTableInfo(reportType);
        } else {
            generateReport(reportType);
        }
    }
    
    private ArrayList convertDomainToListOfHashMaps(List<DDataGrid> domains, DTable table) {
        ArrayList data = new ArrayList();
        for (DDataGrid domain : domains) {
            Map map = new HashMap();
            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                IColumnBuilder builder = table.getColumnBuilder(key);
                String valStr;
                if (builder.getColumn(key).getIsVisible()) {
                    valStr = ((IRowColumnVal) entry.getValue()).getStringFromVal(key, table.getColumnBuilder(key));
                    if (valStr == null) {
                        valStr = "";
                    }
                    map.put(valKey, valStr);
                }
            }
            data.add(map);
        }
        return data;
    }
}
