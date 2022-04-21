package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import java.io.Serializable;
import pst.arm.client.common.ui.form.VType;
import pst.arm.client.common.ui.form.VTypeValidator;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;

/**
 *
 * @author erusakov
* @since 0.0.1
 */
public class DColumnPropertyTextFieldNumberInn extends IColumnProperty<String,String> implements Serializable {
    
    protected int maxLength = Integer.MAX_VALUE;  
    protected int minLength = 0;
    protected VType validator = VType.DIGITINN;
    
    public DColumnPropertyTextFieldNumberInn() {
        format = "";
        type = EColumnType.STRING;
    }

        public int getMaxLength() {
        return maxLength;
    }

            public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public VType getValidator() {
        return validator;
    }

    public void setValidator(VType validator) {
        this.validator = validator;
    }

    @Override
    public String valueToString(String value) {

        return getFormatFromValue(value);
    }

     /*
     * функция возвращает отформатированные данные из оригинальных
     */
    @Override
    public String getFormatFromValue(String value) 
    {
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
        DataGridConstants constants = GWT.create(DataGridConstants.class);
        TextField<String> field = new TextField<String>();
        field.setMaxLength(getMaxLength());
        field.setMinLength(getMinLength());
        //field.setRegex(".+@.+\\.[a-z]+");
        field.setValidator(new VTypeValidator(validator));
        //field.getMessages().setRegexText(constants.errorNotValidDigit());
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
