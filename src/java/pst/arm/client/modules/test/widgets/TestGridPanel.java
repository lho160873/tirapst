package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author Admin
 */
public class TestGridPanel extends ContentPanel{
    
    protected ToolBar toolbar;
    protected Button addButton;

    protected CommonConstants constants = GWT.create(CommonConstants.class);
    protected CommonImages images = GWT.create(CommonImages.class);

    private GWTDDataGridServiceAsync dservice = GWT.create(GWTDDataGridService.class);
    
    protected Grid<BeanModel> grid;
    protected ListStore store;
    protected ListLoader<ListLoadResult<ModelData>> loader;
    protected BeanModelReader reader;
    protected PagingToolBar tbPaging;
    protected DTable table;

    protected final String TABLE_NAME = "COMPANY";
    
    public TestGridPanel() {
        addButton = new Button(constants.add());
        addButton.setIcon(AbstractImagePrototype.create(images.add()));
        addButton.setIconAlign(IconAlign.LEFT);
        addButton.setScale(ButtonScale.SMALL);
        
        addButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {
                initStore();
                initGrid();  
                loader.load();
                grid.mask("Первоначальная загрузка данных");
            }
        });
                       
        toolbar = new ToolBar();
        toolbar.add(addButton);
        
        setTopComponent(toolbar);
        initTableInfo();

        setHeading("Заголовок таблицы");
        setLayout(new FitLayout());
        
    }
    

            
    protected void initTableInfo() 
    {
      // Create an asynchronous callback to handle the result.
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            public void onSuccess(DTable result) {
                table = result;
                initStore();
                initGrid();  
                loader.load();
                grid.mask("Первоначальная загрузка данных");
            }
            
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught);
            }
        };
        dservice.getTable(TABLE_NAME, callback_getTable);
        
        //table = dserviceSync.getTable("ARCHIVE");
        //initGrid();
    }
    
    protected void initStore()
    {
            // proxy and reader
        RpcProxy<List<DDataGrid>> proxy = new RpcProxy<List<DDataGrid>>() {            
            @Override
            public void load(Object loadConfig, AsyncCallback<List<DDataGrid>> callback) {
                dservice.getAllDataGrid(TABLE_NAME,callback);
            }
        };
                
        reader = new BeanModelReader();
        loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
                
        loader.addLoadListener(new LoadListener() {

            @Override
            public void handleEvent(LoadEvent e) {
                loader.setSortField((String) grid.getState().get("sortField"));
                super.handleEvent(e);
                grid.unmask();
            }

            @Override
            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);
                
            }
            @Override
            public void loaderLoadException ( LoadEvent le )
            {
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }            
        });
        store = new ListStore<BeanModel>(loader);
    }
    
     protected ColumnModel createColumModel() {
        GridCellRenderer<BeanModel> renderer = new GridCellRenderer<BeanModel>() 
        {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) 
            {
                DDataGrid row = model.getBean();               
                ColumnConfig columnConfig = grid.getColumnModel().getColumn(colIndex);     
                SKeyForColumn key = new SKeyForColumn(property);
                if ( !row.getRows().isEmpty() && row.getRows().containsKey(key) )
                 return row.getRows().get(key).getVal();
                else 
                 return "";  
            }
        };
        
         List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
         for (IColumnBuilder builder : table.getColumnBuilders()) {
             for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                 //IColumnBuilder col = (IColumnBuilder)colEntry.getValue();
                 SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                 String colName = builder.getColumn(key).getName();
                 String colCaption = builder.getColumn(key).getCaption();
                 ColumnConfig config = new ColumnConfig();
                 //MessageBox.info("", key.getTableAlias()+"."+key.getColumnName(), null);
                 config.setId(key.getTableAlias() + ":" + key.getColumnName());
                 config.setHeader(colCaption);
                 //column.setWidth(60);

                 config.setRenderer(renderer);
                 columns.add(config);
             }
         }
        
        return new ColumnModel(columns);
    }
     
    protected void initGrid()
    {
        MessageBox.info("","initGrid 1",null);
        ColumnModel cm = createColumModel();
        
        grid = new Grid(store, cm); 
        
        grid.setSize(100, 100);
        grid.setStateful(true);
        grid.setAutoExpandColumn("description"); // #NLS
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
        grid.mask("Загрузка данных");
        grid.setStripeRows(true);
        //grid.setContextMenu(menu);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        //grid.set
          MessageBox.info("","initGrid 2",null);
         add(grid);
           MessageBox.info("","initGrid 3",null);
         layout(); 
           MessageBox.info("","initGrid 4",null);
        
    }
    
    //при отрисовке формы
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        //loader.load();
        //grid.mask("Первоначальная загрузка данных");
    }
    
}
