package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.NumberFormat;
import java.io.Serializable;
import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyNumberField extends IColumnProperty<Number, Number> implements Serializable {

    protected Number maxValue = Double.MAX_VALUE;
    protected Number minValue = Double.NEGATIVE_INFINITY;
    protected String strMaxValue = "";
    protected String strMinValue = "";
    protected int maxLength = Integer.MAX_VALUE;
    protected int minLength = 0;
    Boolean allowNegative = true; //может ли значение поля быть отрицательным
    Boolean allowDecimals = true;
    protected int precision = 17;
    protected int scale = 4;
    protected String emptyVal = null;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setEmptyVal(String emptyVal) {
        this.emptyVal = emptyVal;
    }

    public String getEmptyVal() {
        return emptyVal;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getPrecision() {
        return precision;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public void setStrMaxValue(String mv) {
        this.strMaxValue = mv;
    }

    public String getStrMaxValue() {
        return this.strMaxValue;
    }

    public Number getMinValue() {
        return minValue;
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public void setStrMinValue(String mv) {
        this.strMinValue = mv;
    }

    public String getStrMinValue() {
        return this.strMinValue;
    }

    public void setAllowDecimals(Boolean n) {
        allowDecimals = n;
    }

    public Boolean getAllowDecimals() {
        return allowDecimals;
    }

    public void setAllowNegative(Boolean n) {
        allowNegative = n;
    }

    public Boolean getAllowNegative() {
        return allowNegative;
    }

    public DColumnPropertyNumberField() {
        format = "";
        strMinValue = "";
        strMaxValue = "";
        type = EColumnType.DOUBLE;
    }

    @Override
    public String valueToString(Number value) {
        Number val = getFormatFromValue(value); //делаем над значением нужные преобразования
        if (val == null) {
            String str = null;
            return str;
        }
        Class<?> t = this.type.getTypeClass();
        if (t != java.lang.Float.class && t != java.lang.Double.class) {
            return val.toString();
        } else {
            String f = getFormat();
            //задаем форматирование для поля ввода если оно задано
            if (!f.isEmpty()) {
                return NumberFormat.getFormat(f).format(val);
            } else {
                return NumberFormat.getDecimalFormat().format(val);
            }
        }
    }

    protected Number nullVal(Class<?> type) {
        if (type == java.lang.Short.class) {
            Short rc = null;
            return rc;
        } else if (type == java.lang.Integer.class) {
            Integer rc = null;
            return rc;
        } else if (type == java.lang.Long.class) {
            Long rc = null;
            return rc;
        } else if (type == java.lang.Float.class) {
            Float rc = null;
            return rc;
        }
        return null;
    }

    @Override
    public Number stringToValue(String value) {
        Class<?> type = this.type.getTypeClass();
        if (value == null) {
            return nullVal(type);
        }

        try {
            if (type == java.lang.Short.class) {
                return Short.valueOf(value);
            } else if (type == java.lang.Integer.class) {
                return Integer.valueOf(value);
            } else if (type == java.lang.Long.class) {
                return Long.valueOf(value);
            } else if (type == java.lang.Float.class) {
                return Float.valueOf(value);
            } else {
                return Double.valueOf(value);
            }
        } catch (Exception e) {
        }
        Double d = NumberFormat.getDecimalFormat().parse(value);
        return returnTypedValue(d, type);
    }

    protected Number returnTypedValue(Number number, Class<?> type) {
        //Class<?> type = this.type.getTypeClass();
        if (type == java.lang.Short.class) {
            return number.shortValue();
        } else if (type == java.lang.Integer.class) {
            return number.intValue();
        } else if (type == java.lang.Long.class) {
            return number.longValue();
        } else if (type == java.lang.Float.class) {
            return number.floatValue();
        } else if (type == java.lang.Double.class) {
            return number.doubleValue();
        }
        return number;
    }

    /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public Number getFormatFromValue(Number value) {
        Class<?> type = this.type.getTypeClass();
        if (value == null) {
            return nullVal(type);
        }
        return returnTypedValue(value, type);

    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public Number getValueFromFormat(Number valueFormat) {
        if (valueFormat == null) {
            return null;
        }
        //return valueFormat;
        Class<?> type = this.type.getTypeClass();
        return returnTypedValue(valueFormat, type);
    }

    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {
        if (strMaxValue != null && !strMaxValue.isEmpty()) {
            maxValue = stringToValue(strMaxValue);
        }
        if (strMinValue != null && !strMinValue.isEmpty()) {
            minValue = stringToValue(strMinValue);
        }
        final NumberField field = new NumberField();
        if (format.equals("#,##0.00")) { //для величин сумм
            field.addListener(Events.OnKeyUp, new Listener<FieldEvent>() {
                @Override
                public void handleEvent(FieldEvent fe) {
                    int oldLength = field.getRawValue().length();
                    int currPos = field.getCursorPos();
                    int z = field.getRawValue().indexOf(",");
                    if (z != -1 && currPos > z) {
                        return;
                    }
                    field.setValue(field.getValue());
                    int newLength = field.getRawValue().length();
                    if ((newLength - oldLength) >= 3) { //добавился ,00
                        currPos = currPos;
                    } else {
                        if (newLength > oldLength) {
                            currPos = currPos + 1;
                        }
                        if (newLength < oldLength) {
                            currPos = currPos - 1;
                        }
                    }
                    field.setCursorPos(currPos);
                }
            });
        }
        field.setFireChangeEventOnSetValue(Boolean.TRUE);
        field.addListener(Events.OnChange, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
                field.setValue(field.getValue());
            }
        });
        //TODO
        //NumberField field = new DoubleFieldExt(precision, scale);

        field.setMaxLength(maxLength);
        field.setMinLength(minLength);
        field.setMaxValue(maxValue);
        field.setMinValue(minValue);
        field.setEmptyText(emptyVal);
        field.setAllowBlank(!isNotNull); //позволять пусто нет(false) 
        field.setAllowNegative(allowNegative);
        field.setAllowDecimals(allowDecimals);
        //задаем форматирование для поля ввода если оно задано
        if (!format.isEmpty()) {
            field.setFormat(NumberFormat.getFormat(format));
        } else {
            field.setFormat(NumberFormat.getDecimalFormat());
        }

        Class<?> type = this.type.getTypeClass();
        if (type == Short.class) {
            field.setPropertyEditorType(Short.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Short.MAX_VALUE - 1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Short.MIN_VALUE + 1);
            }
        } else if (type == Integer.class) {
            field.setPropertyEditorType(Integer.class);
            field.setAllowDecimals(false);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Integer.MAX_VALUE - 1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Integer.MIN_VALUE + 1);
            }
        } else if (type == Long.class) {
            field.setPropertyEditorType(Long.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Long.MAX_VALUE - 1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Long.MIN_VALUE + 1);
            }

        } else if (type == Float.class) {
            field.setPropertyEditorType(Float.class);
            if (maxValue.doubleValue() == Double.MAX_VALUE) //не переопределяли
            {
                field.setMaxValue(Float.MAX_VALUE - 1);
            }
            if (minValue.doubleValue() == Double.NEGATIVE_INFINITY) //не переопределяли
            {
                field.setMinValue(Float.MIN_VALUE + 1);
            }

        } else {
            field.setPropertyEditorType(Double.class);
        }

        return field;
    }

    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        if (defaultValue == null) {
            return new DRowColumnValNumber();
        }
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number) defaultValue);
        return val;
    }

    @Override
    public ColumnConfig createColumnConfig() //построитель для полей отображения в таблице
    {
        ColumnConfig config = new ColumnConfig("", "", 16);
        config.setWidth(getWidthColumn());
        //задаем форматирование для поля ввода если оно задано
        if (!format.isEmpty()) {
            config.setNumberFormat(NumberFormat.getFormat(format));
        } else {
            config.setNumberFormat(NumberFormat.getDecimalFormat());
        }
        return config;
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
