package pst.arm.client.common.ui.grid;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.domain.search.SearchCondition;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.grid.lang.GridConstants;
import pst.arm.client.common.ui.grid.lang.GridMessages;
import pst.arm.client.modules.images.ArmImages;

/**
 * BaseGrid is the base class for a tabular representation of data from
 * database.
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public abstract class BaseGrid extends LayoutContainer {

    private static final String MULTI_LINE_COMMON = "multiline_common";
    private static final String MULTI_HEADER_GRID_STYLE = "multi_grid_header";
    protected final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected final CommonImages commonImages = GWT.create(CommonImages.class);
    protected Integer currentColumn = null;
    protected ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    protected GridConstants gridConstants = (GridConstants) GWT.create(GridConstants.class);
    protected GridMessages messages = (GridMessages) GWT.create(GridMessages.class);
    //Event bus can communicate with other widgets
    protected EventBus eventBus;
    //Grid
    protected Grid<BeanModel> grid = null;
    protected GridView gridView = null;
    protected GridConfig gridConfig = null;
    protected List<ColumnConfig> gridColumnsConfig = null;
    //Store
    protected ListStore<BeanModel> store;
    protected BasePagingLoader<PagingLoadResult<ModelData>> loader;
    protected RpcProxy proxy;
    //Toolbar
    protected PagingToolBar pagingToolbar;
    private ToolBar topToolbar = null;
    private SearchCondition condition = null;
    protected Integer selectedRow = null;
    protected int totalRecords = 0;
    protected int limitRecords = 2500;
    private ColumnModel columnModel = null;
    protected Button btnAdd, btnOpen, btnEdit, btnDelete, btnCreateCopy, btnRefresh;
    protected MenuItem mAdd, mOpen, mEdit, mDelete, mRefresh;
    private Menu menu;
    private SelectionListener<MenuEvent> menuListener;

    public BaseGrid() {
    }

    /**
     * Default constructor creates and loads default grid gridConfig
     */
    public BaseGrid(EventBus eventBus) {
        this(new GridConfig());
        this.eventBus = eventBus;
    }

   
    public BaseGrid(GridConfig cfg) {
        this.gridConfig = cfg;
        // init base toolbar button
        initTopToolbarButtons();
        initMenu();
        //grid config
        initGridConfig();
        //condition
        initCondition();
        //proxy
        proxy = new RpcProxy<PagingLoadResult<BeanModel>>() {
            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BeanModel>> callback) {
                loadData((PagingLoadConfig) loadConfig, callback);
            }
        };

        // loader
        initLoader();
        //store
        initStore();

        sinkEvents(Event.BUTTON_LEFT);

    }

    /**
     * Load data into store with some search condition.
     *
     * @param cond Search condition
     */
    public void load(SearchCondition cond) {
        load();
    }

    /**
     * Public method to load data into store.
     */
    public void load() {
        PagingLoadConfig pageConfig = new BasePagingLoadConfig();
        pageConfig.setOffset(0);
        pageConfig.setLimit(getGridConfig().getPageSize());

        if (getGrid() != null) {
            Map<String, Object> state = getGrid().getState();
            if (state.containsKey("offset")) {
                int offset = (Integer) state.get("offset");
                int limit = (Integer) state.get("limit");
                pageConfig.setOffset(offset);
                pageConfig.setLimit(limit);
            }

            if (state.containsKey("sortField")) {
                pageConfig.setSortField((String) state.get("sortField"));
                pageConfig.setSortDir(SortDir.valueOf((String) state.get("sortDir")));
            }
        }
        loader.load(pageConfig);
    }

    protected void initGridConfig() {
        if (getGridConfig() == null) {
            setGridConfig(new GridConfig());
        }
        getGridConfig().setPageSize(Integer.parseInt(AppHelper.getInstance().getConfiguration().pageRecordCount()));
    }

    protected void initCondition() {
        setCondition(new SearchCondition());
    }

    /**
     * Method for load data into store, it should be overridden in the child
     * classes.
     *
     * @param loadConfig Offset, limit, sort directions and other options
     * @param callback The method will be called after loading data
     */
    protected void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<BeanModel>> callback) {
        getCondition().setPagingConfig(loadConfig.getLimit(), loadConfig.getOffset(), loadConfig.getSortDir().name(), loadConfig.getSortField());
    }

    /**
     * Virtual method for show empty result help message
     *
     * @return Empty result message
     */
    protected String getEmptyText() {
        return gridConstants.emptyText();
    }

    /**
     * Base columns gridConfig initialization. Creates only empty array for
     * gridConfig data. It should be overridden in the child classes.
     */
    protected void initColumnsConfig() {
        setGridColumnsConfig(new ArrayList<ColumnConfig>());
    }

    /**
     * Enable header view as multiline
     */
    public void enableGridMultiHeading() {
        if (grid != null) {
            if (GXT.isIE) {
                grid.addListener(Events.Render, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        int totalColumns = grid.getColumnModel().getColumnCount();
                        for (int i = 0; i < totalColumns; i++) {
                            com.google.gwt.dom.client.Element elem = (com.google.gwt.dom.client.Element) grid.getView().getHeaderCell(i).getFirstChild();
                            elem.addClassName(MULTI_LINE_COMMON);
                        }
                    }
                });
            } else {
                grid.addStyleName(MULTI_HEADER_GRID_STYLE);
            }
        }
    }

    /**
     * Method to create grid column gridConfig, it can be overridden in the
     * child classes
     *
     * @return Grid columns properties (model)
     */
    public ColumnModel getColumnModel() {
        if (columnModel == null) {
            initColumnsConfig();
            if (getGridColumnsConfig() != null && !gridColumnsConfig.isEmpty()) {
                columnModel = new ColumnModel(getGridColumnsConfig());
            } else {
                columnModel = new ColumnModel(new ArrayList<ColumnConfig>());
            }
        }
        return columnModel;
    }

    private void initTopToolbarButtons() {
        btnRefresh = new Button(commonConstants.reset(), AbstractImagePrototype.create(commonImages.reset()));
        btnAdd = new Button(commonConstants.add(), AbstractImagePrototype.create(commonImages.add()));
        btnEdit = new Button(commonConstants.edit(), AbstractImagePrototype.create(commonImages.edit()));
        btnDelete = new Button(commonConstants.delete(), AbstractImagePrototype.create(commonImages.delete()));

        btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                load(getCondition());
            }
        });

        btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onCreate();
            }
        });

        btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onEdit();
            }
        });

        btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onDelete();
            }
        });
    }

    protected void initMenu() {
        menuListener = new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onMenuSelected(((MenuItem) ce.getItem()).getId());
            }

            private void onMenuSelected(String id) {
                if (id.equalsIgnoreCase("add")) {
                    onCreate();
                } else if (id.equalsIgnoreCase("edit")) {
                    onEdit();
                } else if (id.equalsIgnoreCase("delete")) {
                    onDelete();
                } else if (id.equalsIgnoreCase("refresh")) {
                    load(getCondition());
                }
            }
        };
        setMenu(new Menu());
        mRefresh = new MenuItem(commonConstants.reset(), AbstractImagePrototype.create(commonImages.reset()), menuListener);
        mRefresh.setId("refresh");
        mAdd = new MenuItem(commonConstants.add(), AbstractImagePrototype.create(commonImages.add()), menuListener);
        mAdd.setId("add");
        mEdit = new MenuItem(commonConstants.edit(), AbstractImagePrototype.create(commonImages.edit()), menuListener);
        mEdit.setId("edit");
        mDelete = new MenuItem(commonConstants.delete(), AbstractImagePrototype.create(commonImages.delete()), menuListener);
        mDelete.setId("delete");
        getMenu().add(mRefresh);
        getMenu().add(mAdd);
        getMenu().add(mEdit);
        getMenu().add(mDelete);
    }

    protected void updateTopToolBar() {
        if (getTopToolbar() == null) {
            setTopToolbar(new ToolBar());
            getTopToolbar().setBorders(false);
            getTopToolbar().setSpacing(getGridConfig().getTopToolbarSpacing());
        } else {
            getTopToolbar().removeAll();
        }
        getTopToolbar().add(btnRefresh);
        getTopToolbar().add(btnAdd);
        getTopToolbar().add(btnEdit);
        getTopToolbar().add(btnDelete);
    }

    /**
     * Initializations of top toolbar component
     */
    protected void initTopToolbar() {
        updateTopToolBar();

    }

    protected void enableToolbarControls(Boolean htmlView) {
    }

    /**
     * Initializations of bottom(paging) toolbar component
     */
    protected void initPagingToolbar() {
        pagingToolbar = new PagingToolBar(getGridConfig().getPageSize()) {
            @Override
            protected void onLoad(LoadEvent event) {
                super.onLoad(event);
                PagingLoadResult<?> result = event.getData();
                totalRecords = result.getTotalLength();
                if (!this.isEnabled()) {
                    this.enable();
                }
            }
        };
        pagingToolbar.disable();
        pagingToolbar.bind(loader);
        pagingToolbar.setBorders(false);
    }

    /**
     * Initializations of data store component
     */
    protected void initStore() {
        store = new ListStore<BeanModel>(loader);
    }

    /**
     * Initializations of loader
     */
    protected void initLoader() {
        loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, new BeanModelReader()) {
            @Override
            protected void onLoadFailure(Object loadConfig, Throwable t) {
                String error = getErrorText((Exception) t);
                getGrid().unmask();
                store.removeAll();
                gridView.setEmptyText(error);
                gridView.refresh(false);
                super.onLoadFailure(loadConfig, t);
            }

            @Override
            protected void onLoadSuccess(Object loadConfig, PagingLoadResult<ModelData> result) {

                if ((result.getData() == null) || (result.getData().isEmpty())) {
                    gridView.setEmptyText("<h1 style=\"font-size:120%;\">" + getEmptyText() + "</h1>");
                }
                gridView.refresh(false);
                super.onLoadSuccess(loadConfig, result);

                if ((result.getData() == null) || (result.getData().isEmpty())) {
                    onLoadEmpty();
                }
                onLoadData();
            }
        };
        loader.setRemoteSort(Boolean.TRUE);
        loader.setSortDir(SortDir.ASC);
        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                gridView.setEmptyText("");
                gridView.refresh(false);
            }
        });
    }

    /**
     * Initializations and configuration grid component
     */
    protected void initGrid() {
        grid = new Grid<BeanModel>(store, getColumnModel());
        GridTools.enableGridMultiHeading(grid);
        GridTools.enableGridMultiColumn(grid);

        grid.getSelectionModel().setSelectionMode(getGridConfig().getSelectionMode());

        getGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                if (selectedRow != null && selectedRow >= 0) {
                    getGrid().getView().getCell(selectedRow, 0).setInnerHTML("");
                }
                selectedRow = getGrid().getStore().indexOf(getGrid().getSelectionModel().getSelectedItem());
                if (selectedRow >= 0) {
                    getGrid().getView().getCell(selectedRow, 0).setInnerHTML("<div class='selected-row'></div>");
                }
                onSelectionChanged();
            }
        });

        loader.addLoadListener(new LoadListener() {
            @Override
            public void handleEvent(LoadEvent e) {
                super.handleEvent(e);
                getGrid().getSelectionModel().select(0, Boolean.TRUE);
            }
        });



        gridView = new GridView();
        gridView.setForceFit(Boolean.TRUE);
        gridView.setAutoFill(Boolean.TRUE);
        gridView.setEmptyText(gridConstants.emptyText());
        grid.setContextMenu(getMenu());
        grid.setView(gridView);

        if (getGridConfig().isAutoLoad()) {
            getGrid().addListener(Events.Attach, new Listener<GridEvent<BeanModel>>() {
                @Override
                public void handleEvent(GridEvent<BeanModel> be) {
                    loader.load(new BasePagingLoadConfig(0, getGridConfig().getPageSize()));
                }
            });
        }
        getGrid().setLoadMask(Boolean.TRUE);
        getGrid().setBorders(Boolean.FALSE);
        if (getGridConfig().getAutoExpandColumn() != null) {
            getGrid().setAutoExpandColumn(getGridConfig().getAutoExpandColumn());
        }

        getGrid().addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {
            @Override
            public void handleEvent(GridEvent<BeanModel> be) {
                currentColumn = be.getColIndex();
                onRowDoubleClick();
            }
        });
        getGrid().addListener(Events.KeyUp, new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                switch (event.getKeyCode()) {
                    case KeyCodes.KEY_DELETE:
                        onDelete();
                        break;
                    case KeyCodes.KEY_ENTER:
                        onRowDoubleClick();
                        break;
                    case 155:
                        onCreate();
                        break;
                }
            }
        });
    }


    protected void initView() {
        setLayout(new FitLayout());
        ContentPanel panel = new ContentPanel(new AbsoluteLayout());
        panel.setHeaderVisible(false);
        panel.setIcon(images.table());
        if (getGridConfig().getPagingToolbarVisible()) {
            panel.setBottomComponent(pagingToolbar);
        }
        if (getGridConfig().getTopToolbarVisible()) {
            panel.setTopComponent(getTopToolbar());
        }
        AbsoluteData layoutData = new AbsoluteData(0, 0);
        layoutData.setAnchorSpec("100% 100%");
        panel.add(getGrid(), layoutData);

        add(panel);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        //paging bar
        initPagingToolbar();
        //paging bar
        updateTopToolBar();
        //grid
        initGrid();

        //layout
        initView();
    }

    /**
     * @return the gridColumnsConfig
     */
    public List<ColumnConfig> getGridColumnsConfig() {
        return gridColumnsConfig;
    }

    /**
     * @param gridColumnsConfig the gridColumnsConfig to set
     */
    public void setGridColumnsConfig(List<ColumnConfig> gridColumnsConfig) {
        this.gridColumnsConfig = gridColumnsConfig;
    }

    /**
     * @return the condition
     */
    public SearchCondition getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(SearchCondition condition) {
        this.condition = condition;
    }

    public Object getBean() {
        if (getGrid().getSelectionModel().getSelectedItem() != null) {
            return getGrid().getSelectionModel().getSelectedItem().getBean();
        }
        return null;
    }

    public List<Object> getBeans() {
        List<Object> beans = null;
        if (getGrid().getSelectionModel().getSelectedItems() != null) {
            beans = new ArrayList<Object>();
            for (BeanModel model : getGrid().getSelectionModel().getSelectedItems()) {
                beans.add(model.getBean());
            }
        }
        return beans;
    }

    /**
     * @return the grid
     */
    public Grid<BeanModel> getGrid() {
        return grid;
    }

    /**
     * @return the gridConfig
     */
    public GridConfig getGridConfig() {
        return gridConfig;
    }

    /**
     * @param gridConfig the gridConfig to set
     */
    public void setGridConfig(GridConfig gridConfig) {
        this.gridConfig = gridConfig;
    }

    protected String getErrorText(Exception exception) {
        String ret = gridConstants.serverText();
        String message = exception.getMessage();

        if ((message != null) && (!message.isEmpty())) {
            ret += "<font color='red'>" + message + "</font><br>";
        }

        if (exception instanceof RpcServiceException) {
            String hint = ((RpcServiceException) exception).getHint();
            if ((hint != null) && (!hint.isEmpty())) {
                ret += gridConstants.serverHint() + "<font color='brown'>" + hint + "</font>";
            }
        }

        return ret;
    }

    protected String addStyleForErrorText(String errorText) {
        return errorText;
    }

    protected void saveReport() {
    }

    protected void showAllResults() {
    }

    public void onSaveAllResults() {
        if (grid.getStore().getCount() > 0) {
            final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                        saveReport();
                    }
                }
            };

            if (grid.getStore().getModels() != null && !grid.getStore().getModels().isEmpty()) {
                if (totalRecords < limitRecords) {
                    saveReport();
                } else {
                    MessageBox.confirm(gridConstants.warn(),
                            messages.searchResultReportLimitWarn(limitRecords), l);
                }
            }
        }
    }

    public void onViewAllResults() {
        if (grid.getStore().getCount() > 0) {
            final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                        showAllResults();
                    }
                }
            };

            if (grid.getStore().getModels() != null && !grid.getStore().getModels().isEmpty()) {
                if (totalRecords < limitRecords) {
                    showAllResults();
                } else {
                    MessageBox.confirm(gridConstants.warn(),
                            messages.searchResultReportLimitWarn(limitRecords), l);
                }
            }
        }
    }

    public void updateBean() {
        if (selectedRow != null) {
            store.update(store.getAt(selectedRow));
        }
    }

    protected void onRowDoubleClick() {
    }

    protected void onSelectionChanged() {
    }

    protected void onLoadData() {
    }

    /**
     * @return the eventBus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * @param eventBus the eventBus to set
     */
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public interface IPermissionManager {

        public Boolean canDelete(String entityKind, List<Integer> ids);

        public void updateGridsAfterDeletingEntities(String entityKind);
    }

    /**
     * @return the topToolbar
     */
    public ToolBar getTopToolbar() {
        return topToolbar;
    }

    /**
     * @param topToolbar the topToolbar to set
     */
    public void setTopToolbar(ToolBar topToolbar) {
        this.topToolbar = topToolbar;
    }

    protected void onLoadEmpty() {
    }

    protected void onCreate() {
    }

    protected void onEdit() {
    }

    protected void onDelete() {
    }
}
