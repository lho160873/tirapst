package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.grid.GridTools;
import pst.arm.client.common.utils.BeanModelListReader;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.expansion.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wesStyle
 */
public class DataTreePanel extends DataBasePanel {

    private SelectionChangedListener gridListener;
    private Listener<GridEvent> doubleClickListener;
    private SelectionListener<ButtonEvent> tbListener;
    private BeanModelFactory factory = BeanModelLookup.get().getFactory(DDataGrid.class);
    protected TreeGrid2 grid;
    protected TreeStore store;
    protected TreeLoader loader;
    Button btnAddNear;
    MenuItem menuItemAddNear;
    private Boolean isExpandCalled = false;
    private Integer selectedRow = null;
    private Integer currentRow = null;
    private BeanModel lastEdited = null;
    private List<DDataGrid> lastEditedParents = new ArrayList<DDataGrid>();
    private DomainSaveSuccesedListener<DDataGrid> saveDataGridListener;
    private ContentPanel panelGrid; //панель с центральной таблицей
    protected TabPanel tabPanelSub; //панель с закладками для отображения подчиненных данных (дополнительная информация)
    protected HashMap<String, DataBasePanel> subDataGridPanels; //массив панелей(таблиц) с подчиненными данными
    protected HashMap<String, TabItem> subTabItems; //массив панелей(таблиц) с подчиненными данными
    protected Menu subMenu; //меню для выбора подчиненных данных
    protected BorderLayoutData panelDataSub;
    protected Boolean isOpenAsSub = false;
    protected Listener<DataGridEvent> subFiltersListener;
    protected SRelationInfo subRalationInfo = null; //если форму открыть в режиме отображения подчиненных данных(isOpenAsSub == true) в этом поле будет храниться информация о связях
    protected DDataGrid domainForSub = null;  //данные переданные из основой таблицы в режиме отображения подчиненных данных(isOpenAsSub == true)
    protected DCondition additionalCondition = null;
    protected List<DCondition> conditions;

    public HashMap<String, DataBasePanel> getSubDataGridPanels() {
        return subDataGridPanels;
    }

    public void setSubDataGridPanels(HashMap<String, DataBasePanel> subDataGridPanels) {
        this.subDataGridPanels = subDataGridPanels;
    }

    public DataTreePanel(DTable table, String tn, DCondition cnd) {
        this(table, tn);
        this.additionalCondition = cnd;
        DRowColumnValString val = new DRowColumnValString();
        val.setVal(cnd.getVal());
        this.condition.getFilters().put(cnd.getKey(), val);
    }

    public DataTreePanel(DTable table, String tn, List<DCondition> conditions) {
        this(table, tn);
        this.conditions = conditions;

        for (DCondition con : conditions) {
            DRowColumnValString val = new DRowColumnValString();
            val.setVal(con.getVal());
            this.condition.getFilters().put(con.getKey(), val);
        }
    }

    public DataTreePanel(DTable table, String tn) {
        setTableName(tn);

        this.setBorders(false);
        this.setBodyBorder(false);
        this.setHeaderVisible(true);

        setLayout(new FitLayout());
        condition = new DataGridSearchCondition();
        condition.setLimit(Integer.parseInt(datagridConstants.recordCount()));

        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        panelGrid = new ContentPanel();
        panelGrid.setHeaderVisible(false);
        panelGrid.setBorders(false);
        panelGrid.setLayout(new FitLayout());
        initPanelFiltr();


        initDefaultListeners();
        initListeners();
        initStore();


        toolBar = createToolBar(); //создаем панель с кнопками управления
        initTabPanelSub();
        panelGrid.setTopComponent(toolBar);


        if (table == null) {
            initTableInfo();
        } else {
            this.table = table;
            putGridSearches();
            initGrid();
            createMenuSub();
            createAllSub();
            //reloadGrid();
            initIsFiltrShow();
            //grid.mask(datagridMessages.infoLoading());
            panelDataSub.setSize(table.getSubSize());
            fireDataGridEventLoad(DataGridEvents.DataGridLoad); //посылаем сигнал о том что данные были получены
        }

        BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);



        main.add(panelGrid, resultPanelData);

        add(main);
    }

    //создаем все необходимые подчиненные таблицы
    protected void createAllSub() {
        //надо ли показывать сразу
        if (table.getIsSubShow()) {
            //panelDataSub.setSize(main.getHeight() / 2); //????????????????????????????????
            tabPanelSub.show();
        }

        for (SRelationInfo subTable : table.getSubTables()) {
            final String tabName = subTable.getQueryName();
            final String caption = subTable.getCaption();
            if (!subDataGridPanels.containsKey(tabName)) {
                DataBasePanel panelSub = DataGridPanelBuilder.createDataGridPanel(tabName, subTable);
                TabItem item = new TabItem();
                item.setClosable(false);
                item.setBorders(false);
                item.setItemId(tabName);
                item.setText(caption);
                item.setLayout(new FitLayout());
                item.add(panelSub);

                subDataGridPanels.put(tabName, panelSub); //добавляем в массив созданных подчиненных таблиц
                subTabItems.put(tabName, item);

                if (table.getIsSubShow()) {
                    tabPanelSub.add(subTabItems.get(tabName));
                    ((CheckMenuItem) subMenu.getItemByItemId(tabName)).setChecked(true);
                }
            }
        }
        main.layout(true);
    }

    //открываем таблицу с подчиненными данными
    public void showSub(Integer ind, final String tabName, final String caption, Boolean isShow) {
        if (!subDataGridPanels.containsKey(tabName) || !subTabItems.containsKey(tabName)) {
            return;
        }

        //открываем подчиненную таблицу первый раз
        if (isShow) {
            subDataGridPanels.get(tabName).clearStore();//перед показом данные очищаем, что бы не видеть предыдущие данные
            //открываем панель для подчиненныех данных
            if (!tabPanelSub.isVisible()) {
                panelDataSub.setSize(table.getSubSize());
                tabPanelSub.show();
            }
            //добавляем закладку с подчиненной таблицей
            tabPanelSub.add(subTabItems.get(tabName));
            //делаем открытую закладку текущей
            tabPanelSub.setSelection(subTabItems.get(tabName));

        } else {
            tabPanelSub.setSelection(subTabItems.get(tabName));
            tabPanelSub.remove(subTabItems.get(tabName));
        }

        //если нет ни одной открытой подчиненной таблицы прячем всю панель предназанченную для подчиненных данных
        if (tabPanelSub.getItemCount() == 0) {
            tabPanelSub.hide();
        }
        main.layout(true);
    }

    //передаем информацию о связи с основной таблицей и создаем слушателя событий происходящих в основой таблице (изменение теущей строки и может быть еще что-то)
    protected void createMenuSub() {
        Integer i = 0;
        btnSub.setVisible(table.getSubTables().size() > 0);
        sprSub.setVisible(table.getSubTables().size() > 0);
        for (SRelationInfo subTable : table.getSubTables()) {
            String tabName = subTable.getQueryName();
            String caption = subTable.getCaption();
            CheckMenuItem editItem = new CheckMenuItem(caption);
            editItem.setId(tabName);
            editItem.addSelectionListener(new SelectionListener<MenuEvent>() {
                @Override
                public void componentSelected(MenuEvent ce) {
                    Integer ind = ce.getMenu().indexOf(ce.getItem());
                    String tabName = ce.getItem().getId();
                    String caption = ((CheckMenuItem) ce.getItem()).getText();

                    showSub(ind, tabName, caption, ((CheckMenuItem) ce.getItem()).isChecked());
                }
            });
            subMenu.insert(editItem, i);
            i++;
        }

        if (table.getSubTables().size() <= 0) {
            toolBar.remove(btnSub);
            toolBar.remove(sprSub);
        } else {
            if (toolBar.indexOf(btnSub) == -1) {
                toolBar.add(btnSub);
            }
            if (toolBar.indexOf(sprSub) == -1) {
                toolBar.add(sprSub);
            }
        }
    }

    public void putGridSubFilters(DDataGrid domain) {
        if (!isOpenAsSub) {
            return;
        }
        domainForSub = domain;
        HashMap<SKeyForColumn, IRowColumnVal> filters = condition.getFilters();
        filters.clear();

        if (domain == null) {
            return;
        }

        //если переданы нулевые данные то будем показывать пустую таблицу (в запрос передадим null в условия WHERE
        HashMap< SKeyForColumn, SKeyForColumn> relations = subRalationInfo.getRelations();
        passedFields = new HashMap<SKeyForColumn, IRowColumnVal>();
        for (Map.Entry key : relations.entrySet()) {
            SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
            SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
            IRowColumnVal val = null;
            if (domainForSub != null) {
                val = domainForSub.getRows().get(key_1);
            }
            filters.put(key_2, val);
        }
        if (subRalationInfo.getPassedFieds() != null) {
            for (Map.Entry key : subRalationInfo.getPassedFieds().entrySet()) {
                SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
                SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
                IRowColumnVal val = null;
                if (domainForSub != null) {
                    val = domainForSub.getRows().get(key_1);
                }
                passedFields.put(key_2, val);
            }
        }
    }

    public Listener<DataGridEvent> getSubFiltersListener() {
        return subFiltersListener;
    }

    //передаем информацию о связи с основной таблицей и создаем слушателя событий происходящих в основой таблице (изменение теущей строки и может быть еще что-то)
    protected void createAsSub(SRelationInfo relationInfo) {
        isOpenAsSub = true;

        subRalationInfo = relationInfo;   //если форма открыта в режиме отображения подчиненных данных передаем ей информацию о связях
        subFiltersListener = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(DataGridEvent be) {
                DDataGrid domain = be.getDomain();
                putGridSubFilters(domain);
                reloadGrid();
            }
        };
        setHeaderVisible(false);
        setBorders(false);
    }

    public DataTreePanel(String tableName) {
        this(null, tableName);
    }

    private void initPanelFiltr() {
        panelFiltr = new DataGridFiltr(this);
        panelFiltr.hide(); //изначально делаем не видимой
        //создаем слушателя события окончания загрузки данны
        this.addListener(DataGridEvents.DataGridLoad, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                panelFiltr.repaintAllComponents();
            }
        });
        requestPanelData = new BorderLayoutData(Style.LayoutRegion.NORTH, Integer.parseInt(datagridConstants.filtrpanelMinHeight()));
        requestPanelData.setMargins(new Margins(0, 0, 1, 0));
        requestPanelData.setMaxSize(1200);
        requestPanelData.setSplit(true);
        requestPanelData.setCollapsible(false); //возможность закрыть панель до заголовка
        panelFiltr.setStyleAttribute("padding", "0px");
        main.add(panelFiltr, requestPanelData);

    }

    protected void updateTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            public void onSuccess(DTable result) {
                table = result;
                putGridSearches();
                reloadGrid();
            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        getService().getTable(tableName, callback_getTable);
    }

    private void initTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            public void onSuccess(DTable result) {
                table = result;
                putGridSearches();
                initGrid();
                //reloadGrid();
                initIsFiltrShow();
                fireDataGridEventLoad(DataGridEvents.DataGridLoad); //посылаем сигнал о том что данные были получены
            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        getService().getTable(tableName, callback_getTable);
    }

    public void putGridSearches() {
        condition.getSearches().clear();
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = table.createRowColumnVal(key);
                if (builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr() == null) {
                    continue;
                }
                val.setVal(builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr());
                condition.getSearches().put(key, val);
            }
        }
    }

    //инициализация слушателя событий
    //hlv comment(в первую колонку добавляем стрелку активности строки, используется в DomainEditWindow при определении номера активной строки)
    private void initDefaultListeners() {
        gridListener = new SelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent se) {
                if (selectedRow != null && selectedRow >= 0 && grid.getView() != null && grid.getView().getCell(selectedRow, 0) != null) {
                    grid.getView().getCell(selectedRow, 0).setInnerHTML("");
                }
                if (grid.getSelectionModel() != null) {
                    selectedRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                if (selectedRow >= 0 && grid.getView() != null && grid.getView().getCell(selectedRow, 0) != null) {
                    grid.getView().getCell(selectedRow, 0).setInnerHTML("<div class='selected-row'></div>");
                }
                gridSelectionChanged();
            }
        };

        doubleClickListener = new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                gridRowDoubleClick(be);
            }
        };
    }
    //инициализация хранилища данных для грида

    private void initStore() {
        RpcProxy<List<DDataGrid>> proxy = new RpcProxy<List<DDataGrid>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<DDataGrid>> callback) {
                if (loadConfig == null) {
                    getService().getTreeChildrenData(null, tableName, condition, callback);
                } else {
                    BeanModel bm = (BeanModel) loadConfig;
                    DDataGrid row = (DDataGrid) bm.getBean();
                    getService().getTreeChildrenData(row, tableName, condition, callback);
                }

            }
        };

        BeanModelListReader reader = new BeanModelListReader();

        if (reader != null) {
            loader = new BaseTreeLoader<BeanModel>(proxy, reader) {
                @Override
                public boolean hasChildren(BeanModel parent) {
                    DDataGrid dt = parent.getBean();
                    return dt.getHasChildren();
                }
            };
            store = new TreeStore(loader);
        } else {
            loader = new BaseTreeLoader<BeanModel>(proxy) {
                @Override
                public boolean hasChildren(BeanModel parent) {
                    DDataGrid dt = parent.getBean();
                    return dt.getHasChildren();
                }
            };
            store = new TreeStore(loader);
        }

        loader.addLoadListener(new LoadListener() {
            @Override
            public void handleEvent(LoadEvent le) {
                resizePanelFiltr();
            }
        });
    }

    //инициализация грида
    protected void initGrid() {
        this.setHeading(table.getCaption());
        ColumnModel cm = createColumModel();

        grid = new TreeGrid2(store, cm);
        initGridPlagin();

        GridTools.enableGridMultiHeading(grid);
        GridTools.enableGridMultiColumn(grid);
        grid.setStateId(tableName);
        grid.getView().setForceFit(true);
        grid.setStateful(true);
        grid.setLoadMask(true);
        grid.setContextMenu(menu);
        grid.setStripeRows(true);
        grid.setSelectionModel(new GridSelectionModel<BeanModel>());
        if (table.getIsMulti()) {
            grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        } else {
            grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
        grid.addListener(Events.Attach, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                reloadGrid();
            }
        });
        grid.getSelectionModel().addSelectionChangedListener(gridListener);
        grid.addListener(Events.RowDoubleClick, doubleClickListener);
        panelGrid.add(grid);
        panelGrid.layout(true);
    }

    //перезагрузка данных
    public void reloadGrid() {
        if (isOpenAsSub && domainForSub == null) {
            btnAdd.setEnabled(false);
            btnRefr.setEnabled(false);
            btnFiltr.setEnabled(false);
            menuItemAdd.setEnabled(false);
            menuItemRefr.setEnabled(false);

            /*btnDelete.setEnabled(false);
             menuItemDelete.setEnabled(false);
             btnEdit.setEnabled(false);
             menuItemEdit.setEnabled(false);
             btnView.setEnabled(false);
             menuItemView.setEnabled(false);
             btnSub.setEnabled(false);
             btnSend.setEnabled(false);*/
            store.removeAll();
//            store2.removeAll();
            panelGrid.unmask();
            return;
        }
        if (isOpenAsSub && domainForSub != null) {
            btnAdd.setEnabled(true);
            btnRefr.setEnabled(true);
            btnFiltr.setEnabled(true);
            menuItemAdd.setEnabled(true);
            menuItemRefr.setEnabled(true);
        }
        loader.load();
    }

    //создаем панель с кнопками управления
    private ToolBar createToolBar() {
        ToolBar tb = new ToolBar();

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAdd(false);
            }
        });

        btnAddNear = new Button(commonConstants.addNear());
        btnAddNear.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAddNear.setIconAlign(Style.IconAlign.LEFT);
        btnAddNear.addSelectionListener(tbListener);
        menuItemAddNear = new MenuItem(commonConstants.addNear());
        menuItemAddNear.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAddNear.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAdd(true);
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

        btnFiltr = new Button();
        //btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        //btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(false); //изначально делаем не видимойbtnFiltr

        btnSend = new Button(datagridConstants.btnSend());
        btnSend.setIcon(AbstractImagePrototype.create(datagridImages.check()));
        btnSend.setIconAlign(Style.IconAlign.LEFT);
        btnSend.setEnabled(false);
        btnSend.setVisible(false);
        btnSend.addSelectionListener(tbListener);

        btnSub = new Button(datagridConstants.btnSub());
        btnSub.setIcon(AbstractImagePrototype.create(datagridImages.form()));
        btnSub.setIconAlign(Style.IconAlign.LEFT);
        btnSub.setEnabled(false);
        btnSub.setVisible(false);
        btnSub.addSelectionListener(tbListener);

        btnRefr = new Button(datagridConstants.btnRefr());
        btnRefr.setIcon(AbstractImagePrototype.create(datagridImages.reset()));
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

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка пров доступа
        tb.add(sprEdit);
        tb.add(btnAdd);
        tb.add(btnAddNear);
        tb.add(btnEdit);
        tb.add(btnDelete);
        tb.add(btnView);
        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        tb.add(sprSend);
        tb.add(btnSend);

        sprSub = new SeparatorToolItem();
        sprSub.setVisible(false);
        tb.add(sprSub);
        tb.add(btnSub);


        ftiFiltr = new FillToolItem();
        tb.add(ftiFiltr);

        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(false);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        menu = new Menu();
        menu.add(menuItemRefr);
        sprMenuItemEdit = new SeparatorMenuItem();
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemAddNear);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
        enabledBtn();
        return tb;
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        String datagridName = tableName.toUpperCase();
        sprEdit.setVisible(
                (mode != EditMode.VIEWONLY)
                && ((ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName))
                || ConfigurationManager.getPropertyAsBoolean("dictView")
                || ConfigurationManager.getPropertyAsBoolean("dictView_" + datagridName)));
        sprMenuItemEdit.setVisible(
                (mode != EditMode.VIEWONLY)
                && ((ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName))
                || ConfigurationManager.getPropertyAsBoolean("dictView")
                || ConfigurationManager.getPropertyAsBoolean("dictView_" + datagridName)));

        btnAdd.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)));
        menuItemAdd.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)));

        btnAddNear.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)));
        menuItemAddNear.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName)));

        btnEdit.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName)));
        menuItemEdit.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName)));
        btnDelete.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName)));
        menuItemDelete.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName)));
    }

    protected ColumnModel createColumModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        //пустая колонка для индикации
        ColumnConfig config = new ColumnConfig("nullCloumn", "", 16);
        config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        config.setFixed(true);
        columns.add(config);


        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                config = builder.createTreeColumnConfig(key, table, getId());
                columns.add(config);
            }
        }
        return new ColumnModel(columns);
    }

    /*
     * добавляем в грид объекты плагины (особенности грида, смотри документацию, например колонки типа чек бокс)
     */
    protected void initGridPlagin() {
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                ColumnConfig config = builder.createTreeColumnConfig(key, table, getId());
                if (config instanceof CheckColumnConfig) {
                    grid.addPlugin((CheckColumnConfig) config);
                }
            }
        }
    }

    protected void initTabPanelSub() {
        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        subMenu = new Menu();
        btnSub.setMenu(subMenu);
        subDataGridPanels = new HashMap<String, DataBasePanel>(); //массив панелей с подчиненными данными
        subTabItems = new HashMap<String, TabItem>();
        tabPanelSub.hide(); //изначально панель с подчиненными данными не показываем
        panelDataSub = new BorderLayoutData(LayoutRegion.SOUTH, 0.5f);
        panelDataSub.setMaxSize(1200);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        main.add(tabPanelSub, panelDataSub);
    }

    protected void gridSelectionChanged() {
        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        if (isOpenAsSub) {
            btnAdd.setEnabled(domainForSub != null);
            btnRefr.setEnabled(domainForSub != null);
            btnFiltr.setEnabled(domainForSub != null);
            menuItemAdd.setEnabled(domainForSub != null);
            menuItemRefr.setEnabled(domainForSub != null);
        }

        menuItemDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSub.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSend.setEnabled(isBtnSendEnabled && grid.getSelectionModel().getSelection().size() == 1);

        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        fireDataGridEvent(DataGridEvents.DataGridSelectionChanged, domain, table);

        setAllSubDomain(domain);
    }

    protected void setAllSubDomain(DDataGrid domain) {

        //Данные передаем всегда
        for (SRelationInfo subTable : table.getSubTables()) {
            String tabName = subTable.getQueryName();

            if (subDataGridPanels.containsKey(tabName)) {
                if (tabName.equals("SERV_CONTRACT_STAGE_VO_FOR_OCP")) {
                    DataGridPanelOCP panelSub = (DataGridPanelOCP) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_VO")) {
                    DataGridPanelWorkPlan panelSub = (DataGridPanelWorkPlan) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("OCP_TRIP_VO")) {
                    DataGridPanelTrip panelSub = (DataGridPanelTrip) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DEPART_EXECUTOR_PLAN_VO")) {
                    DataGridPanelWD panelSub = (DataGridPanelWD) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("CONTRACTS_VO_DOP")) {
                    DataGridPanelContractDop panelSub = (DataGridPanelContractDop) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_OCP_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_IG")) {
                    DataGridPanelDocFileIg panelSub = (DataGridPanelDocFileIg) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_DH_ELABORATION_OF_DD_HLV") || tabName.equals("DOC_FILE_APP_PRODUCTUI_HLV")) {
                    DataGridPanelDocFileElaboration panelSub = (DataGridPanelDocFileElaboration) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_FILE_LEVEL_TASK_HLV")) {
                    DataGridPanelDocFileLevelTask panelSub = (DataGridPanelDocFileLevelTask) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("OFFICE_DOC_CONTR_VO")) {
                    DataGridPanelOfficeDocContrVO panelSub = (DataGridPanelOfficeDocContrVO) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DOC_EXEC_PERIOD_FULL_VO")) {
                    DataGridPanelExecPeriod panelSub = (DataGridPanelExecPeriod) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("POST_WORKER_N_VO")) {
                    DataGridPanelPostWorkerN panelSub = (DataGridPanelPostWorkerN) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("PLAN_NTO_YEAR_VO")) {
                    DataGridPanelPlanNTOYear panelSub = (DataGridPanelPlanNTOYear) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK")) {
                    DataGridPanelOrderBlankWork panelSub = (DataGridPanelOrderBlankWork) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK2")) {
                    DataGridPanelOrderBlankWork2 panelSub = (DataGridPanelOrderBlankWork2) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK3")) {
                    DataGridPanelOrderBlankWork3 panelSub = (DataGridPanelOrderBlankWork3) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK4")) {
                    DataGridPanelOrderBlankWork4 panelSub = (DataGridPanelOrderBlankWork4) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("ORDER_BLANK_WORK5")) {
                    DataGridPanelOrderBlankWork5 panelSub = (DataGridPanelOrderBlankWork5) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("DEPART_POST_WORKER_VO")) {
                    DataGridPanelDepartPostWorker panelSub = (DataGridPanelDepartPostWorker) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_EXECUTOR")) {
                    DataGridPanelWorkPlanExecutor panelSub = (DataGridPanelWorkPlanExecutor) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WORK_PLAN_EXECUTOR_NEW")) {
                    DataGridPanelWorkPlanExecutorNew panelSub = (DataGridPanelWorkPlanExecutorNew) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WP_EXECUTORS")) {
                    DataGridPanelExecutors panelSub = (DataGridPanelExecutors) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } else if (tabName.equals("WP_EXECUTORS_NEW")) {
                    DataGridPanelExecutorsNew panelSub = (DataGridPanelExecutorsNew) (subDataGridPanels.get(tabName));
                    panelSub.putGridSubFilters(domain);
                } //                else if (tabName.equals("OFFICE_DOC_EXEC_IG")) {
                //                    DataGridPanelOfficeDocContrIgAction panelSub = (DataGridPanelOfficeDocContrIgAction) (subDataGridPanels.get(tabName));
                //                    panelSub.putGridSubFilters(domain);
                //                }
                else {
                    DataGridPanel panelSub = (DataGridPanel) subDataGridPanels.get(tabName);
                    panelSub.putGridSubFilters(domain);
                }
            }
        }

        //А перечитывать будем только если панель с подчиненными данными видна (открыта)
        if (!tabPanelSub.isVisible()) {
            return;
        }
        for (SRelationInfo subTable : table.getSubTables()) {
            final String tabName = subTable.getQueryName();
            if (!subDataGridPanels.containsKey(tabName) || !subTabItems.containsKey(tabName)) {
                return;
            }

            //нужная нам страничка нашлась
            if (tabPanelSub.findItem(tabName, true) != null) {
                if (tabName.equals("SERV_CONTRACT_STAGE_VO_FOR_OCP")) {
                    DataGridPanelOCP panelSub = (DataGridPanelOCP) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_VO")) {
                    DataGridPanelWorkPlan panelSub = (DataGridPanelWorkPlan) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DEPART_EXECUTOR_PLAN_VO")) {
                    DataGridPanelWD panelSub = (DataGridPanelWD) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("CONTRACTS_VO_DOP")) {
                    DataGridPanelContractDop panelSub = (DataGridPanelContractDop) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("OCP_TRIP_VO")) {
                    DataGridPanelTrip panelSub = (DataGridPanelTrip) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_OCP_VO")) {
                    DataGridPanelDocFile panelSub = (DataGridPanelDocFile) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_IG")) {
                    DataGridPanelDocFileIg panelSub = (DataGridPanelDocFileIg) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_DH_ELABORATION_OF_DD_HLV") || tabName.equals("APP_PRODUCTION")) {
                    DataGridPanelDocFileElaboration panelSub = (DataGridPanelDocFileElaboration) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_FILE_LEVEL_TASK_HLV")) {
                    DataGridPanelDocFileLevelTask panelSub = (DataGridPanelDocFileLevelTask) (subDataGridPanels.get(tabName));
                    panelSub.reloadGrid();
                } else if (tabName.equals("OFFICE_DOC_CONTR_VO")) {
                    DataGridPanelOfficeDocContrVO panelSub = (DataGridPanelOfficeDocContrVO) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DOC_EXEC_PERIOD_FULL_VO")) {
                    DataGridPanelExecPeriod panelSub = (DataGridPanelExecPeriod) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("POST_WORKER_N_VO")) {
                    DataGridPanelPostWorkerN panelSub = (DataGridPanelPostWorkerN) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("PLAN_NTO_YEAR_VO")) {
                    DataGridPanelPlanNTOYear panelSub = (DataGridPanelPlanNTOYear) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK")) {
                    DataGridPanelOrderBlankWork panelSub = (DataGridPanelOrderBlankWork) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK2")) {
                    DataGridPanelOrderBlankWork2 panelSub = (DataGridPanelOrderBlankWork2) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK3")) {
                    DataGridPanelOrderBlankWork3 panelSub = (DataGridPanelOrderBlankWork3) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK4")) {
                    DataGridPanelOrderBlankWork4 panelSub = (DataGridPanelOrderBlankWork4) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("ORDER_BLANK_WORK5")) {
                    DataGridPanelOrderBlankWork5 panelSub = (DataGridPanelOrderBlankWork5) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("DEPART_POST_WORKER_VO")) {
                    DataGridPanelDepartPostWorker panelSub = (DataGridPanelDepartPostWorker) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_EXECUTOR")) {
                    DataGridPanelWorkPlanExecutor panelSub = (DataGridPanelWorkPlanExecutor) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WORK_PLAN_EXECUTOR_NEW")) {
                    DataGridPanelWorkPlanExecutorNew panelSub = (DataGridPanelWorkPlanExecutorNew) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WP_EXECUTORS")) {
                    DataGridPanelExecutors panelSub = (DataGridPanelExecutors) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } else if (tabName.equals("WP_EXECUTORS_NEW")) {
                    DataGridPanelExecutorsNew panelSub = (DataGridPanelExecutorsNew) subDataGridPanels.get(tabName);
                    panelSub.reloadGrid();
                } //                else if (tabName.equals("OFFICE_DOC_EXEC_IG")) {
                //                    DataGridPanelOfficeDocContrIgAction panelSub = (DataGridPanelOfficeDocContrIgAction) subDataGridPanels.get(tabName);
                //                    panelSub.reloadGrid();
                //                }
                else {
                    DataGridPanel panelSub = ((DataGridPanel) subDataGridPanels.get(tabName));
                    panelSub.reloadGrid();
                }
            }
        }
    }

    public TreeLoader getLoader() {
        return loader;
    }

    protected void gridRowDoubleClick(GridEvent event) {
        if (btnSend.isVisible() && btnSend.isEnabled()) {
            onSend();
        } else if (btnEdit.isVisible() && btnEdit.isEnabled()) {
            onEdit();
        } else if (btnView.isVisible() && btnView.isEnabled()) {
            onView();
        }
    }

    public DDataGrid createDomain() {
        DDataGrid domain = new DDataGrid();
        domain.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = builder.createRowColumnVal(key);
                domain.getRows().put(key, val);
                //builder.setDomainValueFromField(key, fields.get(key), domain );                
            }
        }
        return domain;
    }

    protected void onAdd(Boolean isNear) {
        DDataGrid domain = createDomain();//new DDataGrid();
        if (grid.getSelectionModel().getSelectedItem() != null) {
            DDataGrid selectedDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            /*MessageBox.alert("jui", selectedDomain.getRows().get(table.getParentKeyID()).getStringFromVal(
             table.getParentKeyID(), table.getColumnBuilder(table.getParentKeyID())
             ), null);*/

            if (isNear) {
                domain.getRows().put(table.getParentKeyID(), selectedDomain.getRows().get(table.getParentKeyID()));
                domain.getRows().put(table.getParentNameID(), selectedDomain.getRows().get(table.getParentNameID()));
            } else {
                domain.getRows().put(table.getParentKeyID(), selectedDomain.getRows().get(table.getKeyID()));
                domain.getRows().put(table.getParentNameID(), selectedDomain.getRows().get(table.getTreeColumnID()));
            }
        }

        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DCondition dc = new DCondition();
        dc.setKey(new SKeyForColumn("MAIN:DEPART_ID"));

        DDataGrid selectedDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        dc.setVal(selectedDomain.getRows().get(dc.getKey()).getStringFromVal(dc.getKey(), table.getColumnBuilder(dc.getKey())));

        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.EDIT, EWindowType.EWT_WINDOW, dc);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onDelete() {
        MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                    List<DDataGrid> domains = new ArrayList<DDataGrid>();
                    for (final BeanModel beanModel : selection) {
                        DDataGrid domain = beanModel.getBean();
                        domains.add(domain);
                    }
                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            panelGrid.mask(datagridConstants.treeLoading());
                            BeanModel parent = (BeanModel) grid.getTreeStore().getParent(selection.get(0));
                            if (grid.getTreeStore().getChildCount(parent) > 1) {
                                lastEdited = (BeanModel) grid.getTreeStore().getFirstChild(parent);
                            } else {
                                lastEdited = parent;
                            }

                            grid.getTreeStore().addStoreListener(sl2);
                            loader.load();
                        }
                    };
                    getService().deleteDomains(tableName, domains, callback);
                }
            }
        });
    }

    protected void onSend() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        fireDataGridEvent(DataGridEvents.DataGridSend, domain, table);
    }

    public void setFiltrInfo(SRelationInfo relationInfo) {
        isOpenAsSub = true;
        subRalationInfo = relationInfo;   //если форма открыта в режиме отображения подчиненных данных передаем ей информацию о связях
        setHeaderVisible(false);
        setBorders(false);
    }

    private void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    onAdd(false);
                } else if (ce.getButton() == btnAddNear) {
                    onAdd(true);
                } else if (ce.getButton() == btnRefr) {
                    reloadGrid();
//                    updateTableInfo();//hlv
                } else if (ce.getButton() == btnEdit) {
                    onEdit();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                } else if (ce.getButton() == btnDelete) {
                    onDelete();
                }
            }
        };

        saveDataGridListener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid domain) {
                BeanModel model = factory.createModel(domain);
                if (grid.getStore() == null) {
                    return;
                }
                panelGrid.mask(datagridConstants.treeLoading());
                lastEdited = model;
                grid.getTreeStore().addStoreListener(sl2);
                loader.load();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }
    private StoreListener<BeanModel> sl2 = new StoreListener<BeanModel>() {
        @Override
        public void storeDataChanged(StoreEvent<BeanModel> se) {
            AsyncCallback<List<DDataGrid>> callback = new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(List<DDataGrid> result) {
                    lastEditedParents.clear();
                    lastEditedParents.addAll(result);
                    isExpandCalled = true;
                    if (result.size() == 0) {
                        rootExpand();
                    } else {
                        expandToLastEdited();
                    }
                }
            };

            rootExpand();

            if (lastEditedParents.size() == 0 && !isExpandCalled) {
                getService().getTreeChildAllParents((DDataGrid) lastEdited.getBean(), tableName, callback);
            } else {
                expandToLastEdited();
            }
        }
    };

    private void rootExpand() {
        if (lastEditedParents.size() == 0 && isExpandCalled) {
            for (Object obj2 : grid.getTreeStore().getAllItems()) {
                BeanModel model2 = (BeanModel) obj2;
                DDataGrid md2 = model2.getBean();
                DDataGrid lm = lastEdited.getBean();
                String val2 = md2.getRows().get(table.getPk()).getStringFromVal(table.getPk(), table.getColumnBuilder(table.getPk()));
                String lastVal = lm.getRows().get(table.getPk()).getStringFromVal(table.getPk(), table.getColumnBuilder(table.getPk()));
                if (val2.equals(lastVal)) {
                    grid.getSelectionModel().select(model2, false);
                    lastEdited = null;
                    isExpandCalled = false;
                    break;
                }
            }
            panelGrid.unmask();
            grid.getTreeStore().removeStoreListener(sl2);
        }
    }

    private void expandToLastEdited() {
        for (Object obj : grid.getTreeStore().getAllItems()) {
            BeanModel model = (BeanModel) obj;
            DDataGrid md = model.getBean();
            if (lastEditedParents.size() > 0) {
                if (lastEditedParents.get(lastEditedParents.size() - 1).isDomainEquals(md)) {
                    grid.setExpanded(model, true);
                    lastEditedParents.remove(lastEditedParents.size() - 1);
                    break;
                }
            }
        }
    }
    private StoreListener<BeanModel> sl = new StoreListener<BeanModel>() {
        @Override
        public void storeDataChanged(StoreEvent<BeanModel> se) {
            for (Object model : grid.getTreeStore().getRootItems()) {
                grid.setExpanded((BeanModel) model, true, true);
            }
        }
    };

    public void search() {
        if (condition.getSearches().size() != 0) {
            grid.getTreeStore().addStoreListener(sl);
        } else {
            grid.getTreeStore().removeStoreListener(sl);
        }

        loader.load();
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

    public DDataGrid getSelectedDomain() {
        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        return domain;
    }

    @Override
    public void clearStore() {
        btnAdd.setEnabled(false);
        btnRefr.setEnabled(false);
        btnFiltr.setEnabled(false);
        menuItemAdd.setEnabled(false);
        menuItemRefr.setEnabled(false);
        store.removeAll();
    }
}
