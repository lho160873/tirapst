package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyDateStr extends IColumnProperty<String,String> implements Serializable {
       
    private Date minDate, maxDate;

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public String valueToString(String value) {

        return getFormatFromValue(value);
    }
    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy HH:mm";

    public DColumnPropertyDateStr() {
        format = DATETIME_FORMAT_DEFAULT;
    }
    
    /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public String getFormatFromValue(String value) {       
        return value;
    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public String getValueFromFormat(String valueFormat) {
        if (valueFormat.isEmpty()) {
            return (String) null;
        }
        return valueFormat;
    }

    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {
        DateField field = new DateField();
        field.setAllowBlank(!isNotNull); //позволять пусто нет(false)        
        field.getPropertyEditor().setFormat(DateTimeFormat.getFormat(format));
        field.setWidth(60);
        field.setEmptyText(null);

        if (minDate != null) {
            field.setMinValue(minDate);
        }
        else
        {
            field.setMinValue(DateTimeFormat.getFormat("dd.MM.yyyy").parse("01.01.1753"));
        }
        if (maxDate != null) {
            field.setMaxValue(maxDate);
        }
         field.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (!format.toUpperCase().equals("DD.MM.YYYY")) {
                    return null;
                }
                if (value.length() > 6 && value.length() < 10) {
                    return value + " недопустимая дата - она должна быть в формате DD.MM.YYYY";
                }
                return null;
            }
        });
        return field;
    }

    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        if (getDefaultValue() == null) {
            return new DRowColumnValString();
        }
        DRowColumnValString val = new DRowColumnValString();
        val.setVal( (String)defaultValue );   
        return val;
    }
    
   
    
}

