package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.DateTimeFormat;
import pst.arm.client.common.ui.controls.datagrid.DMonthYearField;

import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DColumnPropertyMonthYearField extends IColumnProperty<Date,Date> implements Serializable {

    private Date minDate = null, maxDate = null;
    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy";
    
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

    public DColumnPropertyMonthYearField() {
        format = DATETIME_FORMAT_DEFAULT;
        type = EColumnType.DATE;
    }
   
    @Override
    public String valueToString(Date value) {
        Date valFormat = getFormatFromValue(value);
        String ret = "";
        if (valFormat != null) {
            ret = DateTimeFormat.getFormat(format).format(valFormat);
        }
        return ret;
    }    
    
    /*
     * функция без форматирования преобразует строку в данные заданного типа
     */
    public Date stringToValue(String str) {
        if (str == null) {
            Date rc = null;
            return rc;
        }
        try {
            Date parse = DateTimeFormat.getFormat(format).parse(str);
            return parse;
        } catch (Exception e) {
            Date rc = null;
            return rc;
        }
    }
    
    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {        
        DMonthYearField field = new DMonthYearField();
        field.setAllowBlank(!isNotNull); //позволять пусто нет(false)        
        field.getPropertyEditor().setFormat(DateTimeFormat.getFormat(format));
        //field.setWidth(30);
        field.setEmptyText(null);
        field.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (!format.toUpperCase().equals("MM.YYYY")) {
                    return null;
                }
                if (value.length() > 3 && value.length() < 7) {
                    return value + " недопустимая дата - она должна быть в формате MM.YYYY";
                }
                return null;
            }
        });

        if (minDate != null)
        {
            field.setMinValue(minDate);
        }
        else
        {
            field.setMinValue(DateTimeFormat.getFormat("dd.MM.yyyy").parse("01.01.1753"));
        }
        if (maxDate != null)
            field.setMaxValue(maxDate);

        return field;
    }

    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        if (defaultValue == null) {
            return new DRowColumnValDate();
        }
        DRowColumnValDate val = new DRowColumnValDate();
        val.setVal( (Date)defaultValue );   
        return val;
    }

    /*
     * функция возвращает отформатированные данные из оригинальных(полученных из
     * БД в запросе), т.е. которые необходимо отображать в полях
     */
    @Override
    public Date getFormatFromValue(Date value) {
        return (Date) value;
    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public Date getValueFromFormat(Date valueFormat) {
        if (valueFormat == null) {
            return valueFormat;
        }

        String sd = DateTimeFormat.getFormat("MM.yyyy").format(valueFormat);
        Date parse = DateTimeFormat.getFormat("dd.MM.yyyy").parse("01." + sd);
        
        return parse;
    }
    
    @Override
     public ColumnConfig createColumnConfig( ) //построитель для полей отображения в таблице
    {
         ColumnConfig config = new ColumnConfig("", "", 16); 
         config.setWidth(getWidthColumn());
         config.setDateTimeFormat(DateTimeFormat.getFormat(format));
         return config;
    }
    
    @Override
    public boolean isNotChanges(Date val_1, Date val_2) {
      if (val_1 == null) {
            return (val_2 == null);
        }

        if (val_2 == null) {
            return (val_1 == null);
        }
        Date d1 = (Date) DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(val_1));
        Date d2 = (Date) DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(val_2));
        return DataUtil.compare(d1, d2);
    }
               
}