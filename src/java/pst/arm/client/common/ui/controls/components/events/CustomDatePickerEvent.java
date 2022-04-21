package pst.arm.client.common.ui.controls.components.events;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import pst.arm.client.common.ui.controls.datagrid.DMonthYearPicker;

import java.util.Date;

/**
 * Created by wesStyle on 25/09/14.
 */
public class CustomDatePickerEvent extends ComponentEvent {

    private DMonthYearPicker datePicker;
    private Date date;

    public CustomDatePickerEvent(DMonthYearPicker datePicker) {
        super(datePicker);
        this.datePicker = datePicker;
    }

    /**
     * Returns the source date picker.
     *
     * @return the date picker
     */
    public DMonthYearPicker getDatePicker() {
        return datePicker;
    }

    /**
     * Returns the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
