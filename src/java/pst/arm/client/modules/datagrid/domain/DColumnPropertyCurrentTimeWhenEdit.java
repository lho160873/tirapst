package pst.arm.client.modules.datagrid.domain;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 * @author wesStyle,LKHorosheva
 */
public class DColumnPropertyCurrentTimeWhenEdit extends DColumnPropertyDateField {

    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy HH:mm";

    public DColumnPropertyCurrentTimeWhenEdit() {
        format = DATETIME_FORMAT_DEFAULT;
        type = EColumnType.DATE;
    }
    
    /*
     * функция возвращает оригинальные данные из отформатированных
     */
    @Override
    public Date getValueFromFormat(Date valueFormat) {
        //return  DateTimeFormat.getFormat(DATETIME_FORMAT_DEFAULT).parse(DateTimeFormat.getFormat(DATETIME_FORMAT_DEFAULT).format( new Date()));
        return  DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format( new Date()));
    }
    
    @Override
    public boolean isNotChanges(Date val_1, Date val_2) {        
        return true;
    }
    
    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {        
        DRowColumnValDate val = new DRowColumnValDate();
        val.setVal( new Date() );   
        return val;
    }
}
