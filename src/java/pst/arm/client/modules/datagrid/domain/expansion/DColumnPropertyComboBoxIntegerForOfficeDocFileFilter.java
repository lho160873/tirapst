package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Validator;
import pst.arm.client.common.DataUtil;
import pst.arm.client.modules.datagrid.domain.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1getFormatFromValue
 */
public class DColumnPropertyComboBoxIntegerForOfficeDocFileFilter extends IColumnPropertyComboBox<Integer,String> implements Serializable{
    private ArrayList<DListValIntegerString> list; //список возможных значений для выпадающего списка

     @Override
     public void initList(ArrayList<SListVal> list) {
        this.list.clear();
        //this.list = list;
        for( SListVal val : list )
        {
            this.list.add(new DListValIntegerString(val));
        }
    }
     public void setList(ArrayList<DListValIntegerString> list) {
        this.list = list;
    }

    public ArrayList<DListValIntegerString> getList() {
        return list;
    }

    public DColumnPropertyComboBoxIntegerForOfficeDocFileFilter() {
        super();
        list = new ArrayList<DListValIntegerString>();
        isNewVal = Boolean.FALSE;
        type = EColumnType.INTEGER;
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
        for (DListValIntegerString val : list) {
            cb.add( val.getVal() );
        }
        //cb.recalculate();
        //cb.repaint();
                //cb.
    }

    @Override
    public Field createField(Boolean isNotNull) //построитель для полей ввода
    {

        SimpleComboBox field = new SimpleComboBox<String>();
        field.setAllowBlank(!isNotNull);
        field.setEmptyText(null);
        //field.setEditable(false);
        field.setTriggerAction(ComboBox.TriggerAction.ALL);
        for (DListValIntegerString val : list) {
            field.add(val.getVal());
        }

        field.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (((SimpleComboBox<String>) field).isEditable()) {
                    if ( getValueFromFormat(((SimpleComboBox<String>) field).getRawValue()) == null) {
                        return CB_VALID;
                    }
                }
                return null;
            }
        });

        return field;
    }
    
    @Override
    public String valueToString(Integer value) {
        return getFormatFromValue(value);
    }
    /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public String getFormatFromValue(Integer value) {
        for (DListValIntegerString val : list) {

            if ((value != null) && (value.intValue() == val.getKey().intValue())) {
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
    public Integer getValueFromFormat(String value) {
        for (DListValIntegerString val : list) {
            if (value.equals(val.getVal())) {
                return val.getKey();
            }
        }
        Integer rc = null;
        return rc;
    }

    @Override
    public IRowColumnVal createRowColumnVal() {
        if (defaultValue == null) {
            return new DRowColumnValIntegerString();
        }
        DRowColumnValIntegerString val = new DRowColumnValIntegerString();
        val.setVal((Integer) defaultValue);
        return val;
    }

    @Override
    public Integer stringToValue(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
        }
        Integer rc = null;
        return rc;
    }
    
    @Override
    public boolean isNotChanges(Integer val_1, Integer val_2) {
        if (val_1 == null) {
            if (val_2 != null) {
                if (valueToString(val_2) != null) {
                    return (valueToString(val_2).isEmpty());
                }
            }
            return true;

        }
        if (val_2 == null) {
            if (val_1 != null) {
                 if (valueToString(val_1) != null) {
                    return (valueToString(val_1).isEmpty());
                }
            }
            return true;
        }     
        return DataUtil.compare(valueToString(val_1), valueToString(val_2));
    }
       
}
