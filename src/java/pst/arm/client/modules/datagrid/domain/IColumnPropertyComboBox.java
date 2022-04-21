package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class IColumnPropertyComboBox<T,D> extends IColumnProperty<T,D> implements  Serializable {
    protected Boolean isNewVal;
        //признак того, что если заданное значение не найдено в списке возвращаем не пусто а само значение
    public static final String CB_VALID =
        "Поле заполнено некорректно, значение должно выбираться из выпадающего списка";
    Integer maxHeight = 300; // максимальная высота в пикселях, при которой появляется прокрутка

    public void setIsNewVal(Boolean b) {
        isNewVal = b;
    }

    public Boolean getIsNewVal() {
        return isNewVal;
    }

    public abstract void initList(ArrayList<SListVal> list);

    public abstract void updateData(Field field);

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }
}
