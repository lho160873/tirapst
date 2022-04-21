package pst.arm.client.modules.datagrid.domain.expansion;

import java.util.Date;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyDateField;
import pst.arm.client.modules.datagrid.domain.DRowColumnValDate;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;

/**
 * @author LKHorosheva
 */
public class DColumnPropertyDatePlan extends DColumnPropertyDateField {

   /* public DColumnPropertyDatePlan() {
        //значение по улочанию для даты = текущий день плюс три дня
        Date today = new Date();
        //Date today = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm").parse(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm").format( new Date()));
     
        
        final long ONE_DAY_IN_MS = 24 * 60000 * 60;
        long oldInMs = today.getTime();
        defaultValue = new Date(oldInMs + (3 * ONE_DAY_IN_MS));
    }*/
    
    
    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        DRowColumnValDate val = new DRowColumnValDate();
        Date today = new Date();
        final long ONE_DAY_IN_MS = 24 * 60000 * 60;
        long oldInMs = today.getTime();
        val.setVal(new Date(oldInMs + (3 * ONE_DAY_IN_MS)));
        return val;
    }
   
   /* private Date addHoursToDate(int hours, Date old) {
        final long ONE_HOUR_IN_MS = 60000 * 60;
        long oldInMs = old.getTime();
        Date after = new Date(oldInMs + (hours * ONE_HOUR_IN_MS));
        return after;
    }
    
   
    @Override
    public Date getValueFromFormat(Date valueFormat) {
        Date oDate = DateTimeFormat.getFormat("dd.MM.yy").parse(DateTimeFormat.getFormat("dd.MM.yy").format(valueFormat));
        oDate = addHoursToDate(12, oDate);
        return oDate;
    }*/
}
