package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import java.io.Serializable;
import pst.arm.client.common.ui.form.VType;
import pst.arm.client.common.ui.form.VTypeValidator;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyTextField extends IColumnProperty<String, String> implements Serializable {

    protected int maxLength = Integer.MAX_VALUE;
    protected int minLength = 0;

    public DColumnPropertyTextField() {
        format = "";
        type = EColumnType.STRING;
    }

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
    public String valueToString(String value) {

        return getFormatFromValue(value);
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
        return valueFormat;
    }

    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {
        TextField<String> field = new TextField<String>();
        field.setMaxLength(getMaxLength());
        field.setMinLength(getMinLength());
        field.setValidator(new VTypeValidator(VType.NOTFIRSTEMPTY));
        field.setEmptyText(null);
        field.setAllowBlank(!isNotNull); //позволять пусто нет(false) 
        return field;
    }

    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        if (defaultValue == null) {
            return new DRowColumnValString();
        }
        DRowColumnValString val = new DRowColumnValString();
        val.setVal( (String)defaultValue );   
        return val;
    }
}