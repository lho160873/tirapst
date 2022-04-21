/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.utils.BeanModelListReader;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.images.ArmImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Администратор
 */
public class DataTreePanelTest extends DataBasePanel {

    protected TreeGrid grid;
    protected TreeStore store;
    protected TreeLoader loader;
    protected DataGridSearchCondition condition;
    protected ContentPanel panelGrid;
       private ArmImages images = (ArmImages) GWT.create(ArmImages.class);

    @Override
    public void search() {

    }

    @Override
    public void enabledBtn() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
     public DataTreePanelTest(String tn) {
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

        toolBar = new ToolBar(); //создаем панель с кнопками управления

        btnRefr = new Button("Перечитать");       
        btnRefr.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                reloadGrid();
            }
        });


        toolBar.add(btnRefr);


        panelGrid.setTopComponent(toolBar);


        initStore();
        initTableInfo();

        BorderLayoutData resultPanelData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        main.add(panelGrid, resultPanelData);

        add(main);

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
        
        loader = new BaseTreeLoader<BeanModel>(proxy, reader) {
            @Override
            public boolean hasChildren(BeanModel parent) {
                DDataGrid dt = parent.getBean();
                return dt.getHasChildren();
            }
        };
        store = new TreeStore(loader);

    }

    private void initTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            public void onSuccess(DTable result) {
                table = result;
                initGrid();
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

        grid = new TreeGrid(store, cm);
        
        grid.getStyle().setLeafIcon(images.card());
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
        loader.load();
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
                TreePanel.Joint j = calcualteJoint(tree, model, property, rowIndex, colIndex);
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
                    return AppHelper.getInstance().getStringNotNull(row.getRows().get(key).getStringFromVal(key, builder));
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
                    config.setRenderer(treeRenderer);
                } else {
                    config.setRenderer(renderer);
                }*/
                columns.add(config);
            }
        }
        
        return new ColumnModel(columns);
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
