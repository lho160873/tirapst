package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyDateFieldNextMonthStart extends DColumnPropertyDateField {

    @Override
    public void setDefaultValueForFiltr(Date defaultValueForFiltr) {       
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        if (month == 11) {
            year += 1;
            month = 0;
        } else {
            month += 1;
        }
        date = new Date(year - 1900, month, 1, 0, 0, 1);
        this.defaultValueForFiltr = date;
    }
    
      @Override
    public Date getDefaultValueForFiltr() {
       Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        if (month == 11) {
            year += 1;
            month = 0;
        } else {
            month += 1;
        }
        date = new Date(year - 1900, month, 1, 0, 0, 1);
        this.defaultValueForFiltr = date;
       return date;
    }
}