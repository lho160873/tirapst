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
 */
public class DColumnPropertyDateFieldCurrentDay extends DColumnPropertyDateField {

    @Override
    public void setDefaultValueForFiltr(Date defaultValueForFiltr) {
        this.defaultValueForFiltr = new Date();
    }
    
    @Override
    public Date getDefaultValueForFiltr() {
       this.defaultValueForFiltr = new Date();
       return new Date();
    }
}