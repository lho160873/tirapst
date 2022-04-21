package pst.arm.client.common.ui.controls.datagrid;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SListVal;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DGComboBox<T> extends ComboBox<BeanModel> {

    //private BeanModelFactory cbItemFactory = (BeanModelFactory) BeanModelLookup.get().getFactory(SListVal.class);
    protected BeanModelFactory cbItemFactory = (BeanModelFactory) BeanModelLookup.get().getFactory(SListVal.class);
    protected GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    //private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    protected String displayField = "val";
    protected String valueField = "key";
    //private String displayField = "val";
    //private String valueField = "key";
    protected String tableName = "";
    //private SKeyForColumn keyForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    //private SKeyForColumn keyForName;//ключ к полю, которое хотим помещать в данные 
    protected SKeyForColumn keyForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyForName;//ключ к полю, которое хотим помещать в данные 
    protected final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    //private Boolean isValidate = Boolean.FALSE; //признак необходимости установки валидатора с проверкой наличия введенного значения в списке комбобокса
    protected Boolean isValidate = Boolean.FALSE; //признак необходимости установки валидатора с проверкой наличия введенного значения в списке комбобокса
    protected DataGridSearchCondition conditionExt=null; //дополнительные условия фильтрации данных
      
    protected T currentId = null;
    //private T currentId = null;
    //private Boolean isFindCurrentId = Boolean.FALSE;
    

    public DGComboBox(EDGComboBox name, final int minChars) {
        keyForId = name.keyForId();
        keyForName = name.keyForName();
        tableName = name.tableName();        
        conditionExt = new DataGridSearchCondition();
        initDGComboBox(minChars);
    }

    public DGComboBox(EDGComboBox name) {
        this(name, 0);
    }

    public DGComboBox(String strTableName, String strKeyForId, String strKeyForName, final int minChars) {
        keyForId = new SKeyForColumn(strKeyForId);
        keyForName = new SKeyForColumn(strKeyForName);
        tableName = strTableName;
        conditionExt = new DataGridSearchCondition();
        initDGComboBox(minChars);
    }
        
    public DGComboBox(String strTableName, String strKeyForId, String strKeyForName) {
        this(strTableName, strKeyForId, strKeyForName, 0);
    }
    
    public DGComboBox(String strTableName, SKeyForColumn keyForId, SKeyForColumn keyForName, final int minChars) {
        this.keyForId = keyForId;
        this.keyForName = keyForName;
        tableName = strTableName;
        conditionExt = new DataGridSearchCondition();
        initDGComboBox(minChars);
    }
    
    public DGComboBox(String strTableName, SKeyForColumn keyForId, SKeyForColumn keyForName) {
        this(strTableName, keyForId, keyForName, 0);
    }
    
     public DGComboBox(String strTableName, SKeyForColumn keyForId, SKeyForColumn keyForName,DataGridSearchCondition condition) {
        this(strTableName, keyForId, keyForName, 0);
    }
    
    public DataGridSearchCondition getConditionExt() {
        return conditionExt;
    }
    
     public void setConditionExt(DataGridSearchCondition c) {
         conditionExt = c;
    }
    
    private void initValidator() {
        if (!isValidate) return; 
        setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (((DGComboBox<?>) field).isEditable()) {
                    if (!field.getRawValue().trim().isEmpty() && ((DGComboBox<?>) field).getValueId() == null) {
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
    
    
    public void initDGComboBox(final int minChars) {
        setDisplayField(displayField);
        setValueField(valueField);
        setTriggerAction(TriggerAction.ALL);
        setMinChars(minChars);
        setQueryDelay(0);
        //setItemSelector("div.search-item");
        //setHideTrigger(true);
        initStore();
    }
    
    public void initStore() {
        final int minChars = getMinChars();
        mask();
        final ListStore<BeanModel> str = new ListStore<BeanModel>() {

            @Override
            protected boolean isFiltered(ModelData record, String property) {
                if (filterBeginsWith != null && property != null) {
                    if (filterBeginsWith.length() < minChars) {
                        return true;
                    }

                    Object o = record.get(property);
                    if (o != null) {
                        if (filterBeginsWith.contains(" ")) {
                            if (o.toString().toLowerCase().contains(filterBeginsWith.toLowerCase())) {
                                return false;
                            }
                        } else {
                            String[] split = o.toString().toLowerCase().split(" ");
                            for (int i = 0; i < split.length; i++) {
                                if (split[i].startsWith(filterBeginsWith.toLowerCase())) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        };

        if (conditionExt != null) {
            service.getDataGrid(tableName, conditionExt, new AsyncCallback<List<DDataGrid>>() {

                @Override
                public void onFailure(Throwable thrwbl) {
                    unmask();
                    setEmptyText(commonConstants.errorLoading());
                }

                @Override
                public void onSuccess(List<DDataGrid> listData) {
                    unmask();
                    setEmptyText(null);
                    //заполняем массив данными
                    for (DDataGrid domain : listData) {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName)) {
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), rows.get(keyForName).getVal());
                            str.add(cbItemFactory.createModel(listVal));
                        }
                    }
                    setCurrentId();
                }
            });
        } else {
            service.getAllDataGrid(tableName, new AsyncCallback<List<DDataGrid>>() {

                @Override
                public void onFailure(Throwable thrwbl) {
                    unmask();
                    setEmptyText(commonConstants.errorLoading());
                }

                @Override
                public void onSuccess(List<DDataGrid> listData) {
                    unmask();
                    setEmptyText(null);
                    //заполняем массив данными
                    for (DDataGrid domain : listData) {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName)) {
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), rows.get(keyForName).getVal());
                            str.add(cbItemFactory.createModel(listVal));
                        }
                    }
                    setCurrentId();
                }
            });
        }
        this.setStore(str);
    }

    public T getValueId() {

        /*
         * if (getValue() != null) { SListVal item = (SListVal)
         * getValue().getBean(); return (T) item.getKey(); } else {
         */
        List<BeanModel> models = this.getStore().getModels();
        for (BeanModel bean : models) {
            SListVal val = ((SListVal) bean.getBean());
            if (getRawValue().equals(val.getVal())) {
                return (T) val.getKey();
            }
        }
        // }
        return null;
    }
    
     public void setValueId( T id ) {
        currentId = id;
        //isFindCurrentId = Boolean.TRUE;
        setCurrentId();
         /*this.clear();
        this.clearSelections();
         
        List<BeanModel> models = this.getStore().getModels();
        for (BeanModel bean : models) {
             SListVal val = ((SListVal) bean.getBean());
            if ( id.equals(val.getKey()) )
            {
                setSelection(Arrays.asList(bean));
                break;
            }       
        }*/
    }

    protected void setCurrentId() {
        if (currentId == null){// || !isFindCurrentId) {
            return;
        }
        List<BeanModel> models = this.getStore().getModels();
        for (BeanModel bean : models) {
            SListVal val = ((SListVal) bean.getBean());
            if (currentId.equals(val.getKey())) {
                setSelection(Arrays.asList(bean));
                break;
            }
        }
    } 
    
    /*@Override
    public String getRawValue() {
        String v = super.getRawValue();
        if (v.isEmpty()| v.equals(emptyText)) {
            return null;
        }
        return v;
    }*/
  
}
