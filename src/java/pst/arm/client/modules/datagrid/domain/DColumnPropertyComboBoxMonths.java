package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.DateTimeFormat;
import pst.arm.client.common.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyComboBoxMonths extends IColumnPropertyComboBox<String,String> implements Serializable {

    private ArrayList<DListValString> list; //список возможных значений для выпадающего списка
    protected int maxLength = Integer.MAX_VALUE;
    protected int minLength = 0;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void initList(ArrayList<SListVal> list) {
        //this.list.clear();
        for( SListVal val : list )
        {
            this.list.add(new DListValString(val));
        }
    }

    protected String monthName(int m) {
        switch (m) {
            case 1: return "Январь";
            case 2: return "Февраль";
            case 3: return "Март";
            case 4: return "Апрель";
            case 5: return "Май";
            case 6: return "Июнь";
            case 7: return "Июль";
            case 8: return "Август";
            case 9: return "Сентябрь";
            case 10: return "Октябрь";
            case 11: return "Ноябрь";
            case 12: return "Декабрь";
        }
        return "";
    }

     public void setList(ArrayList<DListValString> list) {
        this.list = list;
    }

    public ArrayList<DListValString> getList() {
        return list;
    }

    public DColumnPropertyComboBoxMonths() {
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

        cb.removeAll();
        for (DListValString val : list) {
            cb.add( val.getVal() );
        }
    }

    @Override
    public Field createField(Boolean isNotNull) //построитель для полей ввода
    {
        SimpleComboBox field = new SimpleComboBox<String>();
        field.setMaxLength( getMaxLength());
        field.setMinLength(getMinLength());        
        field.setEmptyText(null);
        field.setAllowBlank(!isNotNull);
        //field.setEditable(false);
        field.setTriggerAction(ComboBox.TriggerAction.ALL);

        for (int i = -13 ; i <= 11; i++) {

            Date c = new Date();
            c.setMonth(Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c )) + i);

            Integer d = Integer.parseInt(DateTimeFormat.getFormat( "d" ).format( c ));
            Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

            SListVal val = new DListValString();
            val.setVal(monthName(m) + " " + y);
            val.setKey(y+"-"+m+"-"+d);
            this.list.add(new DListValString(val));
        }

        Date def = new Date();
        Integer d = Integer.parseInt(DateTimeFormat.getFormat( "d" ).format( def ));
        Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( def ));
        Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( def ));


        SListVal dval = new DListValString();
        dval.setVal(monthName(m) + " " + y);

        field.setEmptyText(monthName(m) + " " + y);


        for (DListValString val : list) {
            field.add( val.getVal() );
        }      
        
        field.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (((SimpleComboBox<String>) field).isEditable()) {
                    if (getValueFromFormat(((SimpleComboBox<String>) field).getRawValue()) == null) {
                        return CB_VALID;
                    }
                }
                return null;
            }
        });
        
        return field;
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
            if (value.equals(val.getKey())) {
                return val.getVal();
            }
        }
        String rc = isNewVal ? value : null;      
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
        String rc = isNewVal ? value : null;      
        return rc;
    }

    @Override
    public IRowColumnVal createRowColumnVal() {
        if (defaultValue == null) {
            return new DRowColumnValString();
        }
        DRowColumnValString val = new DRowColumnValString();
        val.setVal( (String)defaultValue );
        return val;
    }
    
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
}
