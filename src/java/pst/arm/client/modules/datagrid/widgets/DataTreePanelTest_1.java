/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.Joint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.images.ArmImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Администратор
 */
public class DataTreePanelTest_1 extends DataBasePanel {
    
    protected TreeGrid<BeanModel> grid;
    protected TreeStore store;
    protected TreeLoader loader;
    private SelectionListener<ButtonEvent> tbListener;
    protected ContentPanel panelGrid;
    private ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    private BeanModelFactory factory = BeanModelLookup.get().getFactory(DDataGrid.class);

     public DataTreePanelTest_1(String tn) {
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
        setTableName(tn);

        /*toolBar = new ToolBar(); //создаем панель с кнопками управления

        btnRefr = new Button("Перечитать");
        btnRefr.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                reloadGrid();
            }
        });
        
        toolBar.add(btnRefr);
        panelGrid.setTopComponent(toolBar);*/
        initListeners();
        toolBar = createToolBar(); //создаем панель с кнопками управления
        panelGrid.setTopComponent(toolBar);
        initStore();
        initTableInfo();

        BorderLayoutData resultPanelData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        initPanelFiltr();
        requestPanelData = new BorderLayoutData(Style.LayoutRegion.NORTH, Integer.parseInt(datagridConstants.filtrpanelMinHeight()));
        requestPanelData.setMargins(new Margins(0, 0, 1, 0));
        requestPanelData.setSplit(true);
        requestPanelData.setCollapsible(false); //возможность закрыть панель до заголовка
        panelFiltr.setStyleAttribute("padding", "0px");
        main.add(panelFiltr, requestPanelData);
        main.add(panelGrid, resultPanelData);
        add(main);
    }

    //инициализация хранилища данных для грида
    private void initStore() {
        RpcProxy<List<BeanModel>> proxy = new RpcProxy<List<BeanModel>>() {
            @Override
            protected void load(Object loadConfig, final AsyncCallback<List<BeanModel>> callback) {
                if (loadConfig == null) {
                    getService().getTreeChildrenData(null, tableName, condition, new AsyncCallback<List<DDataGrid>>() {
                        @Override
                        public void onSuccess(List<DDataGrid> result) {
                            callback.onSuccess(factory.createModel(result));
                        }

                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }
                    });
                    
                } else {
                    BeanModel bm = (BeanModel) loadConfig;
                    DDataGrid row = (DDataGrid) bm.getBean();
                    getService().getTreeChildrenData(row, tableName, condition, new AsyncCallback<List<DDataGrid>>() {
                        @Override
                        public void onSuccess(List<DDataGrid> result) {
                            callback.onSuccess(factory.createModel(result));
                        }

                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }
                    });
                }
            }
        };

        loader = new BaseTreeLoader<BeanModel>(proxy) {
            @Override
            public boolean hasChildren(BeanModel parent) {
                 DDataGrid dt = parent.getBean();
                 return dt.getHasChildren();
            }
        };
        
        
         
        store = new TreeStore(loader);
         //loader. addLoadHandler(new ChildTreeStoreBinding<BeanModel>(store));
    }

    private void initTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            public void onSuccess(DTable result) {
                table = result;
                initGrid();
                fireDataGridEventLoad(DataGridEvents.DataGridLoad);
            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        getService().getTable(tableName, callback_getTable);
    }
    
    //инициализация грида
    protected void initGrid() {
        this.setHeading(table.getCaption());
        ColumnModel cm = createColumModel();

        grid = new TreeGrid<BeanModel>(store, cm);
        grid.setStateful(true);
        grid.setStateId(tableName);
        grid.setId(tableName);  

        /*store.setKeyProvider(new ModelKeyProvider<BeanModel>() {
            public String getKey(BeanModel model) {
                DDataGrid dt = model.getBean();
                SKeyForColumn key = new SKeyForColumn("MAIN:DEPART_ID");
                IColumnBuilder builder = table.getColumnBuilder(key);
                return dt.getRows().get(key).getStringFromVal(key, builder);
            }
        });*/
        
        grid.getStyle().setLeafIcon(images.card());
         
        //grid.setAutoExpandColumn("MAIN:DEPART_ID");
        grid.setTrackMouseOver(false);
        
        grid.getView().setForceFit(true);
        grid.addListener(Events.Attach, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                reloadGrid();
            }
        });
        panelGrid.add(grid);
        panelGrid.layout(true);
    }
    //перезагрузка данных 

    public void reloadGrid() {
        PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        config.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        config.setSortField("");
        Map<String, Object> state = grid.getState();
        if (state.containsKey("offset")) {
            int offset = (Integer) state.get("offset");
            int limit = (Integer) state.get("limit");
            config.setOffset(offset);
            config.setLimit(limit);
        }        
        loader.load();//config);
    }

    protected ColumnModel createColumModel() {
        TreeGridCellRenderer<BeanModel> treeRenderer = new TreeGridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                config.css = "x-treegrid-column";
                assert grid instanceof TreeGrid : "TreeGridCellRenderer can only be used in a TreeGrid";
                TreeGrid tree = (TreeGrid) grid;
                TreeStore ts = tree.getTreeStore();
                int level = ts.getDepth(model);
                AbstractImagePrototype icon = calculateIconStyle(tree, model, property, rowIndex, colIndex);
                Joint j = calcualteJoint(tree, model, property, rowIndex, colIndex);
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);
                String id = getId(tree, model, property, rowIndex, colIndex);  
                String text = "";
                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    text = AppHelper.getInstance().getStringNotNull(row.getRows().get(key).getStringFromVal(key, builder));
                }
                return tree.getTreeView().getTemplate(model, id, text, icon, false, j, level - 1);
            }
        };
        
        GridCellRenderer<BeanModel> renderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);
                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    return AppHelper.getInstance().getStringNotNull( row.getRows().get(key).getStringFromVal(key, builder) );                    
                } else {
                    return "";
                }
            }
        };

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig config;
        SKeyForColumn treeKey = table.getTreeColumnID();
        
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                config = builder.createTreeColumnConfig(key,table,getId());
                /*if (key.equals(treeKey)) {
                   // config.setRenderer(new TreeGridCellRenderer<ModelData>());//treeRenderer);
                     config.setRenderer(treeRenderer);
                } else {
                    config.setRenderer(renderer);
                }*/
                columns.add(config);
            }
        }        
        return new ColumnModel(columns);
    }

    public void search() {
        loader.load();
    }

    @Override
    public void enabledBtn() {

    }

    private void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                }
                else if(ce.getButton() == btnRefr) {
                    reloadGrid();//hlv
                }
            }
        };
    }

    private void initPanelFiltr() {
        panelFiltr = new DataGridFiltr(this);
        panelFiltr.show();
        //создаем слушателя события окончания загрузки данны
        this.addListener(DataGridEvents.DataGridLoad, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                panelFiltr.repaintAllComponents();
            }
        });

    }

    private ToolBar createToolBar() {
        ToolBar tb = new ToolBar();
        
        btnFiltr = new Button();
        //btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        //btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(true); //изначально делаем не видимойbtnFiltr

        btnRefr = new Button(datagridConstants.btnRefr());
        btnRefr.setIcon(AbstractImagePrototype.create(datagridImages.reset()));
        btnRefr.setIconAlign(Style.IconAlign.LEFT);
        btnRefr.setEnabled(true);
        btnRefr.setVisible(true);
        btnRefr.addSelectionListener(tbListener);
        tb.add(btnRefr);

        ftiFiltr = new FillToolItem();
        tb.add(ftiFiltr);
        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(false);
        tb.add(sprFiltr);
        tb.add(btnFiltr);
        return tb;
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
