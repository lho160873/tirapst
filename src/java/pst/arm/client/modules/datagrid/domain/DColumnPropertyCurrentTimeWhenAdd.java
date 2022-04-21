package pst.arm.client.modules.datagrid.domain;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 * @author LKHorosheva
 */
public class DColumnPropertyCurrentTimeWhenAdd extends DColumnPropertyDateField {

    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy HH:mm";

    public DColumnPropertyCurrentTimeWhenAdd() {
        format = DATETIME_FORMAT_DEFAULT;
        type = EColumnType.DATE;
    }
    
    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {        
        DRowColumnValDate val = new DRowColumnValDate();
        val.setVal( new Date() );   
        return val;
    }
}
