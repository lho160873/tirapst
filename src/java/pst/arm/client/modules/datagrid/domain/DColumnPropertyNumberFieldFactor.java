package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;
import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyNumberFieldFactor extends DColumnPropertyNumberField{
    protected Double factor;
    protected EColumnType typeEditor;    
    
    public EColumnType getTypeEditor() {
        return typeEditor;
    }

    public void setTypeEditor(EColumnType typeEditor) {
        this.typeEditor = typeEditor;
    }
    
   
    public void setFactor( Double f )
    {
        factor = f;
    }
    public Double getFactor()
    {
        return factor;
    }
    
    public DColumnPropertyNumberFieldFactor() {
         format = "";
         type = EColumnType.NUMBER;
         typeEditor = EColumnType.NUMBER;         
         factor = 1.0;
    }
    
     @Override
       public  Field createField(Boolean isNotNull) //простой построитель для полей ввода
       {
           NumberField field = new NumberField();
           field.setMaxLength(maxLength);
           field.setMinLength(minLength);
           field.setMaxValue(maxValue);
           field.setMinValue(minValue);
           field.setEmptyText(null);
           field.setAllowBlank(!isNotNull); //позволять пусто нет(false) 
           field.setAllowNegative(allowNegative);
           field.setAllowDecimals(allowDecimals);
           String f = getFormat();
           //задаем форматирование для поля ввода если оно задано
           if (!f.isEmpty()) {
               field.setFormat(NumberFormat.getFormat(f));
           }
           else
               field.setFormat(NumberFormat.getDecimalFormat());

           Class<?> t = this.typeEditor.getTypeClass();
           if (t == Short.class) {
            field.setPropertyEditorType(Short.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Short.MAX_VALUE-1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Short.MIN_VALUE+1);
            }
        } else if (t == Integer.class) {
            field.setPropertyEditorType(Integer.class);
            field.setAllowDecimals(false);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Integer.MAX_VALUE-1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Integer.MIN_VALUE+1);
             }
        } else if (t == Long.class) {
            field.setPropertyEditorType(Long.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Long.MAX_VALUE-1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Long.MIN_VALUE+1);
            }

        } else if (t == Float.class) {
            field.setPropertyEditorType(Float.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Float.MAX_VALUE-1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Float.MIN_VALUE+1);
            }

        } else {
            field.setPropertyEditorType(Double.class);
        }
        return field;
    }
     
    @Override
    public String valueToString(Number value) {
       // return getFormatFromValue(value).toString();
       // Number val = getFormatFromValue(value); //делаем над значением нужные преобразования
        if (getFormatFromValue(value)==null) 
        {
            String str = null;
            return str;
        }
        Class<?> t = this.type.getTypeClass();
        if (t != java.lang.Float.class && t != java.lang.Double.class) {
            return getFormatFromValue(value).toString();//val.toString();
        } else {
            String f = getFormat();
            //задаем форматирование для поля ввода если оно задано
            if (!f.isEmpty()) {
                return NumberFormat.getFormat(f).format(getFormatFromValue(value));
            } else {
                return NumberFormat.getDecimalFormat().format(getFormatFromValue(value));
            }
        }
    }
    
     /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public Number getFormatFromValue(Number value) 
    {
        Class<?> t = this.type.getTypeClass();
        if ( value == null ) return nullVal(t);
        if ( factor == null || factor == 0.0 || factor.isNaN() ) return nullVal(t);
        //return returnTypedValue(value,type);
        //Class<?> type = this.typeEditor.getTypeClass();
        
        Double val = value.doubleValue() / factor;       
        return returnTypedValue(val,t);
    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */    
    @Override
    public Number getValueFromFormat(Number valueFormat) 
    {
        if (valueFormat==null) return null;
        //return valueFormat;
                
        Class<?> t = this.type.getTypeClass();
        Double val = valueFormat.doubleValue() * factor;
        return returnTypedValue(val, t);
    }
    
    @Override
    public boolean isNotChanges(Number val_1, Number val_2) {
        if (val_1 == null) {
            return (val_2 == null);
        }
        if (val_2 == null) {
            return (val_1 == null);
        }

        return DataUtil.compare(valueToString(val_1), valueToString(val_2));
    }
}
