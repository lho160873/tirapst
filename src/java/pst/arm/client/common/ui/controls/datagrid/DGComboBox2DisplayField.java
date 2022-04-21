package pst.arm.client.common.ui.controls.datagrid;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SListVal;

/**
 * 
 * @author Михаил
 */
public class DGComboBox2DisplayField<T> extends DGComboBox<T>
{
    /**
     * Второе выводимое поле в выпадающем списке
     */
    private SKeyForColumn keyForName2;
    /**
     * Выделенный элемент
     */
    private BeanModel selectedItem;
    
    /**
     * Конструктор.
     * @param name Принимает значение перечисления, в котором указываются имя таблицы и столбцов
     */
    public DGComboBox2DisplayField(EDGComboBox2DisplayFields name)
    {
        super(name.tableName(), name.keyForId(), name.keyForName(), 0);
        keyForName2 = name.keyForName2();
    }
    
    /**
     * Переопределяет инициализцию выпадающего списка.
     * Именно здесь добавляется второе выводимое поле в выпадающем списке
     */
    @Override
    public void initStore()
    {
        final int minChars = getMinChars();
        mask();
        final ListStore<BeanModel> str = new ListStore<BeanModel>()
        {
            @Override
            protected boolean isFiltered(ModelData record, String property)
            {
                if (filterBeginsWith != null && property != null)
                {
                    if (filterBeginsWith.length() < minChars)
                    {
                        return true;
                    }

                    Object o = record.get(property);
                    if (o != null)
                    {
                        if (filterBeginsWith.contains(" "))
                        {
                            if (o.toString().toLowerCase().contains(filterBeginsWith.toLowerCase()))
                            {
                                return false;
                            }
                        }
                        else
                        {
                            String[] split = o.toString().toLowerCase().split(" ");
                            for (int i = 0; i < split.length; i++)
                            {
                                if (split[i].startsWith(filterBeginsWith.toLowerCase()))
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        };

        if (conditionExt != null)
        {
            service.getDataGrid(tableName, conditionExt, new AsyncCallback<List<DDataGrid>>()
            {
                @Override
                public void onFailure(Throwable thrwbl)
                {
                    unmask();
                    setEmptyText(commonConstants.errorLoading());
                }

                @Override
                public void onSuccess(List<DDataGrid> listData)
                {
                    unmask();
                    setEmptyText(null);
                    //заполняем массив данными
                    for (DDataGrid domain : listData)
                    {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName)) 
                        {
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), ((String)rows.get(keyForName).getVal()) + "\t\t" + ((String)rows.get(keyForName2).getVal()));
                            str.add(cbItemFactory.createModel(listVal));
                        }
                    }
                    setCurrentId();
                }
            });
        }
        else
        {
            service.getAllDataGrid(tableName, new AsyncCallback<List<DDataGrid>>()
            {
                @Override
                public void onFailure(Throwable thrwbl)
                {
                    unmask();
                    setEmptyText(commonConstants.errorLoading());
                }

                @Override
                public void onSuccess(List<DDataGrid> listData)
                {
                    unmask();
                    setEmptyText(null);
                    //заполняем массив данными
                    for (DDataGrid domain : listData)
                    {
                        HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                        if (rows.containsKey(keyForId) && rows.containsKey(keyForName))
                        {
                            SListVal listVal = new SListVal(rows.get(keyForId).getVal(), ((String)rows.get(keyForName).getVal()) + "\t\t" + ((String)rows.get(keyForName2).getVal()));
                            str.add(cbItemFactory.createModel(listVal));
                        }
                    }
                    setCurrentId();
                }
            });
        }
        this.setStore(str);
    }
    
    /**
     * Переопределенный метод для выпадающего списка.
     * Позволяет выбрать элемент выпадающего списка нажатием стрелок и Enter.
     * Здесь задается, что именно будет установлено в textbox
     * @param sel Выбранный элемент
     */
    @Override
    public void select(BeanModel sel)
    {
        SListVal listVal = ((SListVal)sel.getBean());
        SListVal txt = new SListVal(listVal.getKey(), ((String)listVal.getVal()).substring(0, 2));
        BeanModel ret = cbItemFactory.createModel(txt);
        super.select(ret);
    }
    
    /**
     * Переопределнный "обработчик события" выбора следующего элемента из списка (стрелка вниз)
     */
    @Override
    protected void selectNext()
    {
        int count = store.getCount();
        if (count > 0)
        {
            T id = getValueId();
            if (selectedItem != null)
            {
                id = (T)((SListVal)selectedItem.getBean()).getKey();
            }
            int selectedIndex = -1;
            for (int i = 0; i < count; i++)
            {
                if (((T)((SListVal)store.getAt(i).getBean()).getKey()).equals(id))
                {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex < count - 1)
            {
                selectedItem = store.getAt(selectedIndex + 1);
                super.select(store.getAt(selectedIndex + 1));
            }
        }
    }
    
    /**
     * Переопределнный "обработчик события" выбора предыдущего элемента из списка (стрелка вверх)
     */
    @Override
    protected void selectPrev()
    {
        int count = store.getCount();
        if (count > 0)
        {
            T id = getValueId();
            if (selectedItem != null)
            {
                id = (T)((SListVal)selectedItem.getBean()).getKey();
            }
            int selectedIndex = -1;
            for (int i = 0; i < count; i++)
            {
                if (((T)((SListVal)store.getAt(i).getBean()).getKey()).equals(id))
                {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex == -1)
            {
                selectedItem = store.getAt(0);
                super.select(store.getAt(0));
            }
            else
                if (selectedIndex != 0)
                {
                    selectedItem = store.getAt(selectedIndex - 1);
                    super.select(store.getAt(selectedIndex - 1));
                }
        }
    }
    
    /**
     * Переопределенный метод для выпадающего списка.
     * Позволяет выбрать элемент выпадающего списка щелчком мыши.
     * Здесь задается, что именно будет установлено в textbox
     * @param value Выбранный элемент
     */
    @Override
    public void setValue(BeanModel value)
    {
        if (value != null)
        {
            SListVal listVal = ((SListVal)value.getBean());
            SListVal txt = new SListVal(listVal.getKey(), ((String)listVal.getVal()).substring(0, 2));
            BeanModel ret = cbItemFactory.createModel(txt);
            super.setValue(ret);
        }
        else
        {
            super.setValue(value);
        }
    }
    
    /**
     * Инициализация валидатора
     */
    private void initValidator()
    {
        if (!isValidate)
        {
            return;
        } 
        setValidator(new Validator()
        {
            @Override
            public String validate(Field<?> field, String value)
            {
                if (((DGComboBox2DisplayField<?>) field).isEditable())
                {
                    if (!field.getRawValue().trim().isEmpty() && ((DGComboBox2DisplayField<?>) field).getValueId() == null)
                    {
                        return commonConstants.notComboBoxValid();
                    }
                }
                return null;
            }
        });
    }
    
    /**
     * Установка состояния валидатора (включен/выключен)
     * @param isValidate Состояние валидатора
     */
    @Override
    public void setIsValidate( Boolean isValidate )
    {
        this.isValidate = isValidate;
        initValidator();
    }
    
    /**
     * Получение ID текущего выбранного элемента
     * @return ID
     */
    @Override
    public T getValueId()
    {
        List<BeanModel> models = this.getStore().getModels();
        for (BeanModel bean : models)
        {
            SListVal val = ((SListVal) bean.getBean());
            if (getRawValue().equals(((String)val.getVal()).substring(0, 2)))
            {
                return (T) val.getKey();
            }
        }
        return null;
    }
      
    public BeanModel getBeanModel(String value) {
        for (BeanModel model : store.getModels()) {
            Map<String, Object> map = model.getProperties();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(entry.getValue().toString().substring(0, 2).equalsIgnoreCase(value)){
                    return model;
                }
            }
        }
        return null;
    }
}