package pst.arm.client.modules.datagrid.domain;

import pst.arm.client.modules.datagrid.domain.*;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import java.io.Serializable;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.ui.form.VType;
import pst.arm.client.common.ui.form.VTypeValidator;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyTextAreaBig extends DColumnPropertyTextField implements Serializable {
    protected Boolean isNotFirstEmpty = Boolean.FALSE;
    public DColumnPropertyTextAreaBig() {
        super();
    }

    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {
        TextArea field = new TextArea();
        field.setMaxLength(getMaxLength());
        field.setMinLength(getMinLength());
        if (isNotFirstEmpty) {
            field.setValidator(new VTypeValidator(VType.NOTFIRSTEMPTY));
        }
        field.setEmptyText(null);
        field.setAllowBlank(!isNotNull); //позволять пусто нет(false) 
        field.setHeight(450);
        return field;
    }

    public Boolean getIsNotFirstEmpty() {
        return isNotFirstEmpty;
    }

    public void setIsNotFirstEmpty(Boolean isFirstEmpty) {
        this.isNotFirstEmpty = isFirstEmpty;
    }

    @Override
    public boolean isNotChanges(String val1, String val2) {
        String val_1 = ( val1 == null ) ? "" :  val1.replaceAll("\n|\r\n", " ");;
        String val_2 = ( val2 == null ) ? "" : val2.replaceAll("\n|\r\n", " ");
        return DataUtil.compare(val_1, val_2);        
    }
    
    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public String getValueFromFormat(String valueFormat) {
        if (valueFormat != null) {
            return valueFormat.replaceAll("\\s+$", "");//убираем последний перенос строки или пробел если он есть
        } else {
            return valueFormat;
        }
    }
}