package pst.arm.client.common.ui.grid;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.domain.search.SearchConditionSimple;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.controls.editdomain.listener.WindowHideListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.PanelFiltrSimple;
import pst.arm.client.common.ui.grid.event.BaseGridEvent;
import pst.arm.client.common.ui.grid.event.BaseGridEvents;
import pst.arm.client.common.ui.grid.event.PanelChangeEvent;
import com.google.gwt.event.shared.EventBus;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.eventbus.CurrentIdEvent;
import pst.arm.client.common.eventbus.CurrentIdHandler;
//import pst.arm.client.modules.inquiries.events.ClientChangedEvent;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class BaseGridSimple<T> extends ContentPanel {

    protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected CommonImages commonImages = GWT.create(CommonImages.class);
    protected Button btnRefr, btnAdd, btnDelete, btnEdit, btnView, btnSend; //кнопки управления
    protected MenuItem menuItemAdd, menuItemDelete, menuItemRefr, menuItemEdit, menuItemView;
    protected SeparatorToolItem sprEdit, sprFiltr, sprSend;
    protected FillToolItem fillTool;
    protected SeparatorMenuItem sprMenuItemEdit, sprMenuItemFiltr, sprMenuItemSend;
    protected ToggleButton btnFiltr;
    protected LayoutContainer main; //основной лейоут
    protected BorderLayoutData requestPanelData;
    protected ContentPanel panelGrid; //панель с центральной таблицей
    protected ToolBar toolBar = null;  //панель с кнопками управления
    protected PagingToolBar tbPaging = null; //панель под гридом переход по страницам
    protected Integer selectedRow = null; //номер выделенной строки
    protected Integer currentRow = null;  //номер текущей перед перечиткой строки
    protected Boolean isBtnSendEnabled = false; //доступна ли передача данных
    protected Menu menu;
    protected Grid grid = null; //грид с данными
    protected ListStore store = null;
    ; //хранилище для грида 
    protected PagingLoader loader = null; //загрузчик данных для грида
    protected ColumnModel columnModel = null; //модель колонок
    protected List<ColumnConfig> gridColumnsConfig = null; //конфигурации колонок   
    protected PanelChangeEvent panelChangeEvent;
    protected EventType panelChangeEventType = new EventType();
    protected EWindowType windowType = EWindowType.EWT_WINDOW;
    protected Editable.EditMode mode = Editable.EditMode.VIEWEDIT;
    protected SearchConditionSimple condition = null;
    protected PanelFiltrSimple panelFiltr = null; //панель с полями поиска
    protected SelectionListener<ButtonEvent> tbListener; //cлушатели событий нажания на кнопки
    protected DomainSaveSuccesedListener<T> saveDomainListener;
    protected WindowHideListener windowHideListener;
    protected EventBus eventBus;
    protected CheckBoxSelectionModel<BeanModel> cbMultilineColumn;
    protected String currentId = null; 
    public void setEWindowType(EWindowType t) {
        windowType = t;
    }

    public EWindowType getEWindowType() {
        return windowType;
    }

    public void setMode(Editable.EditMode mode) {
        this.mode = mode;
        enabledBtn();
    }

    public BaseGridSimple() {
        initLoader();
        initStore();
        initPagingToolBar();
        initToolBar();
        initGrid();
        initPanelFiltr();
        initView();

    }

    public BaseGridSimple(EWindowType windowType) {
        this.windowType = windowType;
        initLoader();
        initStore();
        initPagingToolBar();
        initToolBar();
        initGrid();
        initPanelFiltr();
        initView();
    }
    
    public BaseGridSimple(EventBus eventBus) {
        this.eventBus = eventBus;
        initLoader();
        initStore();
        initPagingToolBar();
        initToolBar();
        initGrid();
        initPanelFiltr();
        initView();
    }

    public BaseGridSimple(EWindowType windowType, EventBus eventBus) {
        this.windowType = windowType;
        this.eventBus = eventBus;
        initLoader();
        initStore();
        initPagingToolBar();
        initToolBar();
        initGrid();
        initPanelFiltr();
        initView();
    }
    
    
     
    private void initView() {
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(true);
        createHeader();
        setLayout(new FitLayout());

        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        panelGrid = new ContentPanel();
        panelGrid.setHeaderVisible(false);
        panelGrid.setBorders(false);
        panelGrid.setLayout(new FitLayout());

        BorderLayoutData resultPanelData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        main.add(panelGrid, resultPanelData);
        add(main);
        
         //on view
        
        
        
        requestPanelData = new BorderLayoutData(Style.LayoutRegion.NORTH, Integer.parseInt(commonConstants.filtrpanelMinHeight()));
        requestPanelData.setMargins(new Margins(0, 0, 1, 0));
        requestPanelData.setSplit(true);
        requestPanelData.setCollapsible(false); //возможность закрыть панель до заголовка       
        main.add(panelFiltr, requestPanelData);
        
        panelGrid.setTopComponent(toolBar);
        panelGrid.add(grid);
        panelGrid.setBottomComponent(tbPaging);
        
        

    }

    private void initPanelFiltr() {
        panelFiltr = createPanelFiltr();
        if (panelFiltr == null) {
            return;
        }
    }

    private void initPagingToolBar() {
        tbPaging = new PagingToolBar(Integer.parseInt(commonConstants.recordCount()));
        tbPaging.bind(loader);
    }
    
    //инициализация загрузчика данных для грида
    private void initLoader() {
        
        RpcProxy<PagingLoadResult<BeanModel>> proxy = new RpcProxy<PagingLoadResult<BeanModel>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<BeanModel>> callback) {
                loadData((PagingLoadConfig)loadConfig, callback);
            }
        };

        BeanModelReader reader = getBeanModelReader();

        if (reader != null) {
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, reader);
        } else {
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
        }
        loader.addLoadListener(new LoadListener() {

            @Override
            public void handleEvent(LoadEvent e) {
                super.handleEvent(e);
                grid.unmask();
                resizePanelFiltr();
                selectCurrentRow(); 
            }

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                if (grid.getSelectionModel() != null) {
                    currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                super.loaderBeforeLoad(le);
            }


            @Override
            public void loaderLoadException(LoadEvent le) {
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }
        });
        

        loader.setRemoteSort(true);
    }

    //инициализация хранилица данных
    private void initStore() {
        store = new ListStore<BeanModel>(loader);
        store.addStoreListener(new StoreListener<BeanModel>() {
            @Override
            public void handleEvent(StoreEvent<BeanModel> e) {
                selectCurrentRow();
            }
        });
    }

    //инициализация самого грида
    private void initGrid() {
        grid = new Grid(store, getColumModel());        
        GridTools.enableGridMultiHeading(grid);
        GridTools.enableGridMultiColumn(grid);
        //MessageBox.info("getGridId()", getGridId(), null);
        //grid.setStateId(getGridId());
        grid.setStateful(true);
        grid.clearState();
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setContextMenu(menu);       
        if (cbMultilineColumn != null) {
            grid.addPlugin(cbMultilineColumn);
            grid.setSelectionModel(cbMultilineColumn);
            //grid.addStyleName("multilineColumn");
        } else {
            grid.setSelectionModel(new GridSelectionModel<BeanModel>());
            grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        }
        
        grid.addListener(Events.Attach, new Listener<GridEvent>() {

            @Override
            public void handleEvent(GridEvent be) {
                reloadGrid();
            }
        });
        
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                if (selectedRow != null && selectedRow >= 0 && getGrid().getView() != null && getGrid().getView().getCell(selectedRow, 0) != null) {
                    getGrid().getView().getCell(selectedRow, 0).setInnerHTML("");
                }
                if (getGrid().getSelectionModel() != null) {
                    selectedRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                if (selectedRow >= 0 && getGrid().getView() != null && getGrid().getView().getCell(selectedRow, 0) != null) {
                    getGrid().getView().getCell(selectedRow, 0).setInnerHTML("<div class='selected-row'></div>");
                }
                gridSelectionChanged();
            }
        });
        grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {

            @Override
            public void handleEvent(GridEvent<BeanModel> be) {
                gridRowDoubleClick();
            }
        });
    }

    //инициализация панели с управляющими кнопками
    private void initToolBar() {
        initListeners();      //создаем слушателей событий нажатий на кнопки         
        toolBar = new ToolBar();

        btnRefr = new Button(commonConstants.reset());
        btnRefr.setIcon(AbstractImagePrototype.create(commonImages.reset()));
        btnRefr.setIconAlign(Style.IconAlign.LEFT);
        btnRefr.setEnabled(true);
        btnRefr.setVisible(true);
        btnRefr.addSelectionListener(tbListener);
        menuItemRefr = new MenuItem(commonConstants.reset());
        menuItemRefr.setIcon(AbstractImagePrototype.create(commonImages.reset()));
        menuItemRefr.setEnabled(true);
        menuItemRefr.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                reloadGrid();
            }
        });

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onAdd();
            }
        });

        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(tbListener);
        menuItemDelete = new MenuItem(commonConstants.delete());
        menuItemDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        menuItemDelete.setEnabled(false);
        menuItemDelete.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onDelete();
            }
        });

        btnEdit = new Button(commonConstants.edit());
        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnEdit.setIconAlign(Style.IconAlign.LEFT);
        btnEdit.setEnabled(false);
        btnEdit.addSelectionListener(tbListener);
        
        menuItemEdit = new MenuItem(commonConstants.edit());
        menuItemEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        menuItemEdit.setEnabled(false);
        menuItemEdit.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onEdit();
            }
        });

        btnView = new Button(commonConstants.view());
        btnView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnView.setIconAlign(Style.IconAlign.LEFT);
        btnView.setEnabled(false);
        btnView.addSelectionListener(tbListener);
        menuItemView = new MenuItem(commonConstants.view());
        menuItemView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuItemView.setEnabled(false);
        menuItemView.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onView();
            }
        });

        btnSend = new Button(commonConstants.send());
        btnSend.setIcon(AbstractImagePrototype.create(commonImages.check()));
        btnSend.setIconAlign(Style.IconAlign.LEFT);
        btnSend.setEnabled(false);
        btnSend.setVisible(false);
        btnSend.addSelectionListener(tbListener);

        btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(commonConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(commonImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(true); //изначально делаем  видимой

        toolBar.add(btnRefr);
        sprEdit = new SeparatorToolItem();
        toolBar.add(sprEdit);
        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnView);

        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        toolBar.add(sprSend);
        toolBar.add(btnSend);

         fillTool =  new FillToolItem();
        toolBar.add(fillTool);
        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(true);
        toolBar.add(sprFiltr);
        toolBar.add(btnFiltr);

        menu = new Menu();
        menu.add(menuItemRefr);
        sprMenuItemEdit = new SeparatorMenuItem();
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);

        enabledBtn(); //настройка пров доступа
    }

    protected void showSelf() {
        firePanelChangeEvent(panelChangeEventType, this);
    }

    protected void initListeners() {
        saveDomainListener = new DomainSaveSuccesedListener<T>() {

            @Override
            public void onDomainSaveSucceed(T domain) {
                onDomainSaveOk(domain);
            }
        };

        windowHideListener = new WindowHideListener() {

            @Override
            public void onWindowHide() {
                showSelf();
            }
        };
        tbListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    onAdd();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(btnFiltr.isPressed());
                } else if (ce.getButton() == btnRefr) {
                    reloadGrid();
                } else if (ce.getButton() == btnEdit) {
                    onEdit();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnDelete) {
                    onDelete();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                }
            }
        };
    }

    public void fillPanel() {
        panelFiltr.fillPanel();
    }
    //перезагрузка данных 

    public void reloadGrid() {
        PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        config.setLimit(Integer.parseInt(commonConstants.recordCount()));
        config.setSortField("");
        if ( getGrid() != null) {
            Map<String, Object> state = grid.getState();

            if (state.containsKey("offset")) {
                int offset = (Integer) state.get("offset");
                int limit = (Integer) state.get("limit");
                config.setOffset(offset);
                config.setLimit(limit);
            }
           
            if (state.containsKey("sortField")) {
                config.setSortField((String) state.get("sortField"));
                config.setSortDir(Style.SortDir.valueOf((String) state.get("sortDir")));
            }
        }
        if (grid.getSelectionModel() != null && grid.getStore() != null) {
            currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
        }
        loader.load(config);
    }

    public void search() {
        loader.load();
        //reloadGrid();
    }

    public ColumnModel getColumModel() {
        if (columnModel == null) {
            createColumnsConfig();
            if (getGridColumnsConfig() != null && !gridColumnsConfig.isEmpty()) {
                columnModel = new ColumnModel(getGridColumnsConfig());
            } else {
                columnModel = new ColumnModel(new ArrayList<ColumnConfig>());
            }
        }
        return columnModel;
    }

    public Grid<BeanModel> getGrid() {
        return grid;
    }

    public SearchConditionSimple getCondition() {
        createCondition();
        return condition;
    }

    public void setGridColumnsConfig(List<ColumnConfig> gridColumnsConfig) {
        this.gridColumnsConfig = gridColumnsConfig;
    }

    public List<ColumnConfig> getGridColumnsConfig() {
        return gridColumnsConfig;
    }

    protected BeanModelReader getBeanModelReader() {
        return new BeanModelReader();
    }

    public void setIsShowPanelFiltr(Boolean isShow) {
        if (panelFiltr == null) {
            return;
        }
        if (isShow) {
            panelFiltr.show();
            resizePanelFiltr();
        } else {
            panelFiltr.hide();
        }
        btnFiltr.toggle(isShow);
        if (!isShow) {
            btnFiltr.setToolTip(commonConstants.btnShowFiltr());
        } else {
            btnFiltr.setToolTip(commonConstants.btnHideFiltr());
        }
    }

    public void selectCurrentRow() {
        
        List<BeanModel> models = grid.getStore().getModels();
        if (currentRow == null || currentRow < 0) {
            currentRow = 0;
        }
        if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                grid.getSelectionModel().select(currentRow, false);
            }
        }
    }

    protected void gridSelectionChanged() {
        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        menuItemDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        btnSend.setEnabled(grid.getSelectionModel().getSelection().size() == 1);

        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);

        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        
    }

    protected void gridRowDoubleClick() {
        if (btnSend.isVisible() && btnSend.isEnabled()) {
            onSend();
        } else if (btnEdit.isVisible() && btnEdit.isEnabled()) {
            onEdit();
        } else if (btnView.isVisible() && btnView.isEnabled()) {
            onView();
        }
    }

    public void resizePanelFiltr() {
        if (panelFiltr != null && panelFiltr.isVisible()) {
            requestPanelData.setSize(panelFiltr.getEnoughHeight());
            main.layout(true); //перерисовываем   
        }
    }

    public void setIsBtnSendEnabled(Boolean b) {
        isBtnSendEnabled = b;
        btnSend.setVisible(b);
        sprSend.setVisible(b);
    }

    public Button getBtnSend() {
        return btnSend;
    }

    public T getSelectedDomain() {
        if (grid.getSelectionModel() == null) {
            return null;
        }
        Integer selectItem = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());

        if (selectItem == null || selectItem < 0) {
            return null;
        }
        T domain = (T)((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        return domain;
    }

    public PanelFiltrSimple getPanelFiltr() {
        return panelFiltr;

    }

    public void setIsStartWithShowPanelFiltr(Boolean b) {
        setIsShowPanelFiltr(b);
        main.layout(true);
    }

    protected void firePanelChangeEvent(EventType eventType, LayoutContainer panel) {
        if (windowType != EWindowType.EWT_PANEL) {
            return;
        }

        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                PanelChangeEvent event = new PanelChangeEvent(this, panel);
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }

    public void addPanelChangeListener(Listener<? extends PanelChangeEvent> listener) {
        this.addListener(panelChangeEventType, listener);
    }
    
   /* @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        initPagingToolBar();
        initToolBar();
        initGrid();
        initPanelFiltr();
        initView();
    }*/
 
    public abstract void loadData(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BeanModel>> callback);

    public abstract void createColumnsConfig();

    public abstract void enabledBtn();

    protected abstract void createHeader();

    protected abstract PanelFiltrSimple createPanelFiltr();

    protected abstract String getGridId();

    protected abstract void createCondition();

    protected abstract void onDomainSaveOk(T domain);

    protected abstract void onAdd();

    protected abstract void onEdit();

    protected abstract void onDelete();
    
    //protected abstract void onSend();

    protected abstract void onView();
    
    protected void onSend() {
        T domain = (T)((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        fireGridEvent(BaseGridEvents.BaseGridSend, domain);
    }

    private void fireGridEvent(EventType eventType, T domain) {
        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                BaseGridEvent event = new BaseGridEvent(this, domain);
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }
  
    public void addBaseGridSendEventListener(Listener<? extends BaseGridEvent> listener) {
        this.addListener(BaseGridEvents.BaseGridSend, listener);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        initHandlers();
    }
    
     private void initHandlers() {                   
        if (getEventBus() != null) {
            getEventBus().addHandler(CurrentIdEvent.TYPE, new CurrentIdHandler() {
                @Override
                public void handleCurrentIdEvent(CurrentIdEvent event) {
                    currentId = event.getId();                 
                }
            });
        }
    }
}
