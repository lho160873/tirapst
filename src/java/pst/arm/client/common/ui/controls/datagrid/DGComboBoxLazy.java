package pst.arm.client.common.ui.controls.datagrid;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.*;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DGComboBoxLazy<T> extends ComboBox<BeanModel> {

    private BeanModelFactory cbItemFactory = (BeanModelFactory) BeanModelLookup.get().getFactory(SListVal.class);
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    private String displayField = "val";
    private String valueField = "key";
    protected String tableName = "";
    private SKeyForColumn keyForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    private SKeyForColumn keyForName;//ключ к полю, которое хотим помещать в данные 
    protected DataGridSearchCondition condition; //условия фильтрации данных
    protected DataGridSearchCondition conditionExt; //дополнительные условия фильтрации данных
    
    private  MyRpcProxy<List<DDataGrid>> proxy;
    private  BaseListLoader<ListLoadResult<BeanModel>> loader;
    protected DTable table = null;
    protected final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    
    private T currentId = null;
    private Boolean isFindCurrentId = Boolean.FALSE;
    public static final EventType TableReady = new EventType();
    private Boolean isValidate = Boolean.FALSE; //признак необходимости установки валидатора с проверкой наличия введенного значения в списке комбобокса

    public DGComboBoxLazy(EDGComboBox name) {
        keyForId = name.keyForId();
        keyForName = name.keyForName();
        tableName = name.tableName();       
        initAll();
    }

    public DGComboBoxLazy(String strTableName, SKeyForColumn ki, SKeyForColumn kn) {
        keyForId = ki;
        keyForName = kn;
        tableName = strTableName;
        initAll();
    }

    public DGComboBoxLazy(String strTableName, String strKeyForId, String strKeyForName) {
        keyForId = new SKeyForColumn(strKeyForId);
        keyForName = new SKeyForColumn(strKeyForName);
        tableName = strTableName;
        initAll();
    }
    
    public void initAll() {
        condition = new DataGridSearchCondition();
        conditionExt = new DataGridSearchCondition();
        initTableInfo();
        initProxy();
        initLoader();
        initStory();
        initView();
    }
      
    /**
     * class DDataGridReaderForComboBox Вспомогательный класс для формирования
     * списка данных типа SListVal для комбобокса из полученного с свервера
     * списка данных типа DDataGrid
     *
     */
    protected class DDataGridReaderForComboBox implements DataReader<ListLoadResult<ModelData>> {

        @SuppressWarnings({"unchecked", "rawtypes"})
        public ListLoadResult<ModelData> read(Object loadConfig, Object data) {
            if (data instanceof List) {
                List<Object> beans = (List) data;
                if (beans.size() > 0) {
                    List models = new ArrayList(beans.size());
                    for (Object o : beans) {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = ((DDataGrid) o).getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName)) {
                            //MessageBox.info("add 1",String.valueOf(rows.get(keyForName).getVal()),null);
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), rows.get(keyForName).getVal());
                            BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(SListVal.class);
                            assert factory != null : "No BeanModelFactory found for " + o.getClass();
                            models.add(factory.createModel(listVal));
                        }
                    }
                    return newLoadResult(loadConfig, models);
                }
                return newLoadResult(loadConfig, (List) beans);

            } else if (data instanceof ListLoadResult) {
                ListLoadResult result = (ListLoadResult) data;
                List beans = result.getData();
                if (beans.size() > 0) {
                    List converted;
                    converted = new ArrayList(beans.size());
                    for (Object o : beans) {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = ((DDataGrid) o).getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName)) {
                            //MessageBox.info("add 2",String.valueOf(rows.get(keyForName).getVal()),null);
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), rows.get(keyForName).getVal());
                            BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(SListVal.class);
                            assert factory != null : "No BeanModelFactory found for " + o.getClass();
                            converted.add(factory.createModel(listVal));
                        }
                    }
                    beans.clear();
                    beans.addAll(converted);
                }
                return (ListLoadResult) data;
            }
            assert false : "Error converting data";
            return null;
        }

        protected ListLoadResult<ModelData> newLoadResult(Object loadConfig, List<ModelData> models) {
            return new BaseListLoadResult<ModelData>(models);
        }
    }

    protected void initTableInfo() {
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            public void onSuccess(DTable result) {
                table = result;
                fireEventTableReady(TableReady); //посылаем сигнал о том что данные были получены
                setEmptyText(null);
            }

            public void onFailure(Throwable caught) {
                setEmptyText(commonConstants.errorLoading());
            }
        };
        service.getTable(tableName, callback_getTable);
    }
    
     //сигнал посылаеться(создается) и тут же обрабатываеться(handleEvent) только в том случае если на него были подписчики (this.addListener(eventType, listener);
    private void fireEventTableReady(EventType eventType) {
        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                BaseEvent event = new BaseEvent(this);
                //event.
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }
    
    public void addListenerTableReady(Listener<? extends BaseEvent> listener) {
        this.addListener(TableReady, listener);
    }
    /*
     * Вспомогательный класс только для того, что бы определить момент окончания
     * загрузки данных с сервера для вызова метода setCurrentId();
     *
     */
    public abstract class MyRpcProxy<D> implements DataProxy<D> {

        public void load(final DataReader<D> reader, final Object loadConfig,
                final AsyncCallback<D> callback) {
            load(loadConfig, new AsyncCallback<D>() {

                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @SuppressWarnings("unchecked")
                public void onSuccess(Object result) {
                    try {
                        D data = null;
                        if (reader != null) {
                            data = reader.read(loadConfig, result);
                        } else {
                            data = (D) result;
                        }
                        callback.onSuccess(data);
                        setCurrentId();

                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                }
            });
        }
        protected abstract void load(Object loadConfig, AsyncCallback<D> callback);
    }
  
    public void initProxy() {
        proxy = new MyRpcProxy<List<DDataGrid>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback callback) {
                condition.getSearches().clear();
                condition.getFilters().clear();
                initCondition();//инициализируем услови поиска и фильтра из вне
                if (table != null && !getRawValue().isEmpty()) {
                    for (IColumnBuilder builder : table.getColumnBuilders()) {
                        for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                            if (currentId == null || !isFindCurrentId) { //если не был передан id конкретной записи ищем по тому что пользователь ввел в поле комба бокса (getRawValue())
                                if (!key.equals(keyForName)) {
                                    continue;
                                }
                                IRowColumnVal val = table.createRowColumnVal(keyForName);
                                if (val == null) {
                                    continue;
                                }
                                val.setVal(getRawValue());
                                condition.getSearches().put(keyForName, val);
                            } else { //был передан id нужно найти для него  запись в БД, как только найдем isFindCurrentId переведем в False и комбо бокс буде работать в нормальном режиме поиска
                                if (!key.equals(keyForId)) {
                                    continue;
                                }
                                IRowColumnVal val = table.createRowColumnVal(keyForId);
                                if (val == null) {
                                    continue;
                                }
                                val.setVal(currentId);
                                condition.getFilters().put(keyForId, val);
                            }

                        }
                    }
                }
                service.getDataGrid(tableName, condition, callback);
            }
        };

    }

    public void initLoader() {
        loader = new BaseListLoader<ListLoadResult<BeanModel>>(proxy, new DDataGridReaderForComboBox());//keyForId, keyForName));

        loader.addLoadListener(new LoadListener() {

            @Override
            public void loaderLoadException(LoadEvent le) {
                AppHelper.showMsgRpcServiceError(le.exception);
            }

            @Override
            public void handleEvent(LoadEvent le) {
                super.handleEvent(le);
            }
        });

    }

    public void initStory() {
        store = new ListStore<BeanModel>(loader);
    }

    public void initView() {
        setDisplayField(displayField);
        setValueField(valueField);
        setStore(store);
        setMinChars(1);
        setLoadingText(commonConstants.loading());
        setTriggerAction(TriggerAction.ALL);
        setEmptyText(null);
        //setHideTrigger(Boolean.TRUE);
        setQueryDelay(25);
        //setItemSelector("div.search-item");
    }

    
    public void reloadList() {
        loader.load();
    }
  

    @Override
    public void setRawValue(String text) {
        super.setRawValue(text);
        //loader.load();
    }
    private void initValidator() {
        if (!isValidate) {
            return;
        }
        setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (((DGComboBoxLazy<?>) field).isEditable()) {
                    if (!field.getRawValue().trim().isEmpty() && ((DGComboBoxLazy<?>) field).getValueId() == null) {
                        return commonConstants.notComboBoxValid();
                    }
                }
                return null;
            }
        });
    }
    public void setIsValidate( Boolean isValidate )
    {
        this.isValidate = isValidate;
        initValidator();
    }
    
    public T getValueId() {
        if (getValue() != null) {
            SListVal item = (SListVal) getValue().getBean();
            return (T) item.getKey();
        } else {
            loader.load();
            List<BeanModel> models = this.getStore().getModels();
            for (BeanModel bean : models) {
                SListVal val = ((SListVal) bean.getBean());
                if (getRawValue().equals(val.getVal())) {
                    return (T) val.getKey();
                }
            }
        }
        return null;
    }

    public void setValueId(T id) {
        currentId = id;
        isFindCurrentId = Boolean.TRUE;
        loader.load();
    }

    protected void setCurrentId() {
        //MessageBox.info("","setCurrentId",null);
        if (currentId == null || !isFindCurrentId) {
            return;
        }
        isFindCurrentId = Boolean.FALSE; //ищем один раз до следующего требования.
        List<BeanModel> models = this.getStore().getModels();
        for (BeanModel bean : models) {
            SListVal val = ((SListVal) bean.getBean());
            if (currentId.equals(val.getKey())) {
                setSelection(Arrays.asList(bean));
                break;
            }
        }
    }
    
    public DataGridSearchCondition getConditionExt()
    {
        return conditionExt;
    }
    
    protected void initCondition()
    {
        for ( Map.Entry filters: conditionExt.getFilters().entrySet() )
        {
            condition.getFilters().put((SKeyForColumn)filters.getKey(), (IRowColumnVal)filters.getValue()); //добавляем условия из вне
        }
        
        for ( Map.Entry searches: conditionExt.getSearches().entrySet() )
        {
            condition.getSearches().put((SKeyForColumn)searches.getKey(), (IRowColumnVal)searches.getValue()); //добавляем условия из вне
        }
    }
    
    public DTable getTable()
    {
        return table;
    }
    
   /*@Override
    public String getRawValue() {
        String v = super.getRawValue();
        if (v.isEmpty()) {
            return null;
        }
        return v;
    }*/

}
