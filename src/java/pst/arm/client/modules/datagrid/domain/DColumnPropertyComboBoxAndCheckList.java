package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Validator;
import java.io.Serializable;
import java.util.ArrayList;
import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva
 */
public class DColumnPropertyComboBoxAndCheckList extends IColumnPropertyComboBox<String,String> implements Serializable{
    private ArrayList<DListValString> list; //список возможных значений для выпадающего списка

     @Override
     public void initList(ArrayList<SListVal> list) {
        this.list.clear();
        //this.list = list;
        for( SListVal val : list )
        {
            this.list.add(new DListValString(val));
        }
    }
     public void setList(ArrayList<DListValString> list) {
        this.list = list;
    }

    public ArrayList<DListValString> getList() {
        return list;
    }
    
    public DColumnPropertyComboBoxAndCheckList() {
        super();
        list = new ArrayList<DListValString>();
        isNewVal = Boolean.FALSE;
        type = EColumnType.STRING;
    }
    
    @Override
    public void updateData(Field field) //построитель для полей ввода
    {

        if (field.getClass() != SimpleComboBox.class) {
            return;
        }
        SimpleComboBox cb = (SimpleComboBox) field;

        //cb.clear();
        cb.removeAll();
        for (DListValString val : list) {
            cb.add( val.getVal() );
        }
        //cb.recalculate();
        //cb.repaint();
                //cb.
    }

    @Override
    public BoxComponent createFieldForFiltr(Boolean isNotNull) //построитель для полей ввода
    {
        ListStore<BeanModel> store = new ListStore<BeanModel>();

        BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(DListValString.class);
        for (DListValString val : list) {
            store.add(factory.createModel(val));
        }


        CheckBoxListView<BeanModel> view = new CheckBoxListView<BeanModel>();
        view.setStore(store);
        view.setDisplayProperty("val");

        return view;
    }
    
    @Override
    public Field createField(Boolean isNotNull) //построитель для полей ввода
    {
        return null;
    }
    
    @Override
    public String valueToString(String value) {
        return getFormatFromValue(value);
    }
    /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public String getFormatFromValue(String value) {
        for (DListValString val : list) {

            if ((value != null) && (value.equals(val.getKey()))) {
                return val.getVal();
            }
        }           
        String rc = null;
        return rc;
    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public String getValueFromFormat(String value) {
        for (DListValString val : list) {
            if (value.equals(val.getVal())) {
                return val.getKey();
            }
        }
        String rc = null;
        return rc;
    }

    @Override
    public IRowColumnVal createRowColumnVal() {
        if (defaultValue == null) {
            return new DRowColumnValString();
        }
        DRowColumnValString val = new DRowColumnValString();
        val.setVal(defaultValue);
        return val;
    }

    /*@Override
    public String stringToValue(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
        }
        Integer rc = null;
        return rc;
    }*/
    
    @Override
    public boolean isNotChanges(String val_1, String val_2) {
        if (val_1 == null) {
            if (val_2 != null) {
                return (val_2.isEmpty());
            }
            return true;
        }

        if (val_2 == null) {
            if (val_1 != null) {
                return (val_1.isEmpty());
            }
            return true;
        }
        return DataUtil.compare(val_1, val_2);
    }
    
   /*@Override
     public void setDefaultValueForFiltr(String defaultValueForFiltr) {
        this.defaultValueForFiltr = "1,0";
    }*/
   
    /*   @Override
    public Integer getDefaultValueForFiltr() {
       return 2;
       }*/
}
