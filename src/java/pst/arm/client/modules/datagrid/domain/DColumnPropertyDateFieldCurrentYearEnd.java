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
public class DColumnPropertyDateFieldCurrentYearEnd extends DColumnPropertyDateField {

    @Override
    public void setDefaultValueForFiltr(Date defaultValueForFiltr) {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        date.setMonth(11);
        date.setDate(31);

        this.defaultValueForFiltr = date;
    }
    
     @Override
    public Date getDefaultValueForFiltr() {
       Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        date.setMonth(11);
        date.setDate(31);

        this.defaultValueForFiltr = date;
       return date;
    }
}