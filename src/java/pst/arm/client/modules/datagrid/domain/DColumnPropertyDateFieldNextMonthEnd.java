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
public class DColumnPropertyDateFieldNextMonthEnd extends DColumnPropertyDateField {
    
    private int[] months = new int[13];

    public DColumnPropertyDateFieldNextMonthEnd() {
        months[0] = 31;
        months[1] = 28;
        months[2] = 31;
        months[3] = 30;
        months[4] = 31;
        months[5] = 30;
        months[6] = 31;
        months[7] = 31;
        months[8] = 30;
        months[9] = 31;
        months[10] = 30;
        months[11] = 31;
        months[12] = 29;        
    }    
    
    
    @Override
    public void setDefaultValueForFiltr(Date defaultValueForFiltr) {
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDate();
        if (month == 11) {
            year += 1;
            month = 0;
            day = months[month];
        } else {
            month += 1;
            day = months[month];
            if ((month == 1) && ((year % 400 == 0) || ((year % 4 == 0) &&  (year % 100 != 0)))) {
                day = months[12];       
            }
        }
        date = new Date(year - 1900, month, day);//, 23, 59, 59);
        this.defaultValueForFiltr = date;
    }
    
     @Override
    public Date getDefaultValueForFiltr() {
       Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDate();
        if (month == 11) {
            year += 1;
            month = 0;
            day = months[month];
        } else {
            month += 1;
            day = months[month];
            if ((month == 1) && ((year % 400 == 0) || ((year % 4 == 0) &&  (year % 100 != 0)))) {
                day = months[12];       
            }
        }
        date = new Date(year - 1900, month, day);//, 23, 59, 59);
        this.defaultValueForFiltr = date;
       return date;
    }
}