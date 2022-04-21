package pst.arm.client.modules.tablegrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;
import pst.arm.client.modules.tablegrid.service.remote.GWTTableGridService;
import pst.arm.client.modules.tablegrid.service.remote.GWTTableGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridPanel extends ContentPanel {
 
    //protected DictionaryConstants constants = GWT.create(DictionaryConstants.class);
    //protected DictionaryImages images = GWT.create(DictionaryImages.class);
    
    protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected CommonImages commonImages = GWT.create(CommonImages.class);
    
    //protected DictionaryMessages messages = GWT.create(DictionaryMessages.class);
    
    private SelectionChangedListener gridListener;
    private Listener<GridEvent> doubleClickListener;
    
    protected Grid grid;
    protected ListStore store;
    protected PagingLoader loader;
    
    protected PagingToolBar tbPaging;
    
    private int selectedRow = -1;
    
    
    protected TableGridSearchCondition condition = new TableGridSearchCondition();
        
    //private GWTTableGridServiceAsync service = GWT.create(GWTTableGridService.class); //hlv
    private GWTTableGridServiceAsync service = GWT.create(GWTTableGridService.class);
    
    private SelectionListener<ButtonEvent> tbListener;
    private DomainSaveSuccesedListener<TableGrid> saveTableGridListener;
    
    private Button btnAdd, btnDelete, btnEdit;
    
    //private Map<String, Dictionary> openedDictionaries;
    //конструктор классаsetLayout
    public TableGridPanel(){
       
        setLayout(new FitLayout());
        
        initDefaultListeners();
        initListeners();
        
        initStore();
        initPagingToolBar();
        initGrid();
        
        ToolBar toolBar = createToolBar();
        
        setTopComponent(toolBar);
       
        add(grid);
        setBottomComponent(tbPaging);
        
    }   
    //инициализация слушателя событий 
    //hlv comment(в первую колонку добавляем стрелку активности строки, используется в DomainEditWindow при определении номера активной строки)
    protected void initDefaultListeners(){
        gridListener = new SelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent se) {
                int pointerColumnIndex = grid.getColumnModel().getIndexById("");
                if (pointerColumnIndex != -1) {
                    for (int i = 0; i < grid.getStore().getCount(); i++) {
                        grid.getView().getCell(i, pointerColumnIndex).setInnerHTML("");
                    }
                    if (se.getSelection().size() == 1) {
                        int pointerIndex = grid.getStore().indexOf(se.getSelectedItem());
                        grid.getView().getCell(pointerIndex, pointerColumnIndex).setInnerHTML("<div class='selected-row'></div>");
                    }
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

    protected void initPagingToolBar(){
        tbPaging = new PagingToolBar(50);
        tbPaging.bind(loader);
    }
    //инициализация хранилища данных для грида
    protected void initStore()
    {
        RpcProxy<PagingLoadResult<TableGrid>> proxy = new RpcProxy<PagingLoadResult<TableGrid>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<TableGrid>> callback) {
                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
                
                //TableGridSearchCondition condition = new TableGridSearchCondition();
                condition.setOffset(config.getOffset());
                condition.setLimit(config.getLimit());
                condition.setSortFieldId(config.getSortField());
                TableGridSearchCondition.SortDir sortDir;
                switch(config.getSortDir()){
                    case ASC: 
                        sortDir = TableGridSearchCondition.SortDir.ASC;
                        break;
                        
                    case DESC: 
                        sortDir = TableGridSearchCondition.SortDir.DESC;
                        break;
                        
                    default:
                        sortDir = TableGridSearchCondition.SortDir.NONE;
                }
                condition.setSortDir(sortDir);
                putGridSearchFilters(condition.getFilters());
                //getService().getTableGrid(condition, callback);
               //getService().getAllTableGrid(callback);
                getService().getPage(condition,callback);

            }
        };
        
        BeanModelReader reader = getBeanModelReader();
        
        if(reader != null){
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, reader);
            store = new ListStore<BeanModel>(loader);        
        }else{
            loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
            store = new ListStore<ModelData>(loader); 
        }
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
         loader.setRemoteSort(true);
        
    }
    //инициализация грида
    protected void initGrid(){
        ColumnModel cm = createColumModel();
        
        grid = new Grid(store, cm); 
        //grid.setSize(300,120);
        grid.setStateful(true);
        grid.setAutoExpandColumn("name"); // #NLS
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
        //grid.mask(constants.loadUsersMask());
        grid.setStripeRows(true);
        //grid.setContextMenu(menu);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
         
         //grid.mask(constants.loadUsersMask());

        GridView view = new GridView();
        view.setForceFit(true);
        view.setAutoFill(true);
        grid.addListener(Events.Attach, new Listener<GridEvent>() {

            @Override
            public void handleEvent(GridEvent be) {
                reloadGrid();
            }
            
            
        });
        grid.getSelectionModel().addSelectionChangedListener(gridListener);
        grid.addListener(Events.RowDoubleClick, doubleClickListener);
        grid.setView(view);
       
    }
    //перезагрузка данных 
    public void reloadGrid() {
        /*PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        config.setLimit(50);
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
        }*/

        loader.load();//config);
    }
    //создаем панель управления
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();
        
        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);        
        btnAdd.addSelectionListener(tbListener);
        
        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(tbListener);
        
        btnEdit = new Button(commonConstants.edit());
        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));        
        btnEdit.setIconAlign(Style.IconAlign.LEFT);
        btnEdit.setEnabled(false);
        btnEdit.addSelectionListener(tbListener);
        
       
        
        tb.add(btnAdd);
        tb.add(btnDelete);
        tb.add(btnEdit);

        
        return tb;
    }

    
    protected ColumnModel createColumModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();  
        
        //column for pointer from DomainEditWindoe
        ColumnConfig config = new ColumnConfig("", "", 16);
        config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        config.setFixed(true);
        columns.add(config);
        
        config = new ColumnConfig("id", "Код", 16);
        columns.add(config);
        
        config = new ColumnConfig("name", "Наименование", 200);
        columns.add(config);
        
        return new ColumnModel(columns);        
    }


    //protected GWTDataGridServiceAsync getService() {
     protected GWTTableGridServiceAsync getService()
     {
        return service;
     }

   
    protected void gridSelectionChanged() {
        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
    }

    
    protected void gridRowDoubleClick(GridEvent event) {
        //Dictionary selectedDictionary = ((BeanModel)event.getModel()).getBean();
       // fireDictionaryEvent(DictionaryEvents.DictionaryOpened, selectedDictionary);
    }

    
    protected BeanModelReader getBeanModelReader() {
        return new BeanModelReader();
    }
    
    

    protected void initListeners(){
        tbListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if(ce.getButton() == btnAdd){
                    //MessageBox.info("","add",null);                    
                    TableGridEditWindow window = new TableGridEditWindow(new TableGrid());
                    window.addDomainSaveSuccesedListener( saveTableGridListener );
                    window.show();    
                }else if(ce.getButton() == btnEdit){
                     //MessageBox.info("","edit",null);
                    TableGrid domain = ((BeanModel)grid.getSelectionModel().getSelectedItem()).getBean();
                    TableGridEditWindow window = new TableGridEditWindow( domain, grid );
                    //Dictionary dictionary = ((BeanModel)grid.getSelectionModel().getSelectedItem()).getBean();
                    //DictionaryEditWindow window = new DictionaryEditWindow(dictionary, grid);
                    window.addDomainSaveSuccesedListener( saveTableGridListener );
                    window.show();
                }else if(ce.getButton() == btnDelete){    
                   //  MessageBox.info("","delete",null);
                    
                    
                     //delete dictionaries
                    MessageBox.confirm( commonConstants.confirm(),"TODO", new Listener<MessageBoxEvent>() 
                    //MessageBox.confirm( commonConstants.confirm(), messages.confirmDeleteDictionary(), new Listener<MessageBoxEvent>() 
                    {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                                List<BeanModel> selection = grid.getSelectionModel().getSelection();
                                List<Integer> ids = new ArrayList<Integer>();
                               
                                for (final BeanModel beanModel : selection) {
                                    TableGrid domain = beanModel.getBean(); 
                                    //service.deleteTableGrid((TableGrid) domain, callback);
                                     ids.add(domain.getId().intValue());
                                }                               
                                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                             AppHelper.showMsgRpcServiceError(caught);
                                            //MessageBox.alert(commonConstants.error(), messages.errCantDeleteDictionary(), null).show();
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                        }
                                    };
                                   
                                    service.deleteDomainsByIds(ids, callback);
                                
                                reloadGrid();
                            }
                        }
                    });
                   /*
                     //test if dictionaries opened
                    boolean someDictOpened = false;
                    
                    List<BeanModel> selection = grid.getSelectionModel().getSelection();
                    for(BeanModel bm : selection){
                        if(openedDictionaries.get(((Dictionary)bm.getBean()).getName()) != null){
                            someDictOpened = true;
                        }
                    }
                    if(someDictOpened){
                        MessageBox.alert(commonConstants.error(), messages.errDeleteOpenedDictionary(), null);
                        return;
                    }
                    
                    //delete dictionaries
                    MessageBox.confirm(commonConstants.confirm(), messages.confirmDeleteDictionary(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                                List<BeanModel> selection = grid.getSelectionModel().getSelection();
                                for (final BeanModel beanModel : selection) {
                                    Dictionary dictionary = beanModel.getBean();

                                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert(commonConstants.error(), messages.errCantDeleteDictionary(), null).show();
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                        }
                                    };
                                    service.deleteDictionary((Dictionary) dictionary, callback);
                                }
                                reloadGrid();
                            }
                        }
                    });
                 */   
                }
            }
        };
        
        saveTableGridListener = new DomainSaveSuccesedListener<TableGrid>() {

            @Override
            public void onDomainSaveSucceed(TableGrid domain) {
                reloadGrid();
                //fireTableGridEvent(TableGridEvents.TableGridChanged, domain);
            }
        };
    }
    
    //public void addDictionaryListener(Listener<? extends DictionaryEvent> listener){
        //this.addListener(DictionaryEvents.DictionaryOpened, listener);
        //this.addListener(DictionaryEvents.DictionaryDeleted, listener);
        //this.addListener(DictionaryEvents.DictionaryChanged, listener);
   // }
    
    //public void removeDictionaryListener(Listener<? extends DictionaryEvent> listener){
       // this.removeListener(DictionaryEvents.DictionaryOpened, listener);
       // this.removeListener(DictionaryEvents.DictionaryDeleted, listener);
       // this.removeListener(DictionaryEvents.DictionaryChanged, listener);
    //}
    
   /* private void fireTableGridEvent(EventType eventType, TableGrid domain) {
        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                TableGridEvent event = new TableGridEvent(this, domain);
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }*/
    
   /* public void setDictionaryOpen(Dictionary dictionary, boolean open){
        if(open){
            openedDictionaries.put(dictionary.getName(), dictionary);
        }else{
            openedDictionaries.remove(dictionary.getName());
        }
    }    */
    
   
    protected void putGridSearchFilters(Map<String, Object> filters) 
    {        
    }
        
    void search() 
    {
        loader.load();
    }
    
    public TableGridSearchCondition getCondition()
    {
        return condition;
    }
}
