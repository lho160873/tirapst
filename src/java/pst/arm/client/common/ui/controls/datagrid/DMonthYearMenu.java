package pst.arm.client.common.ui.controls.datagrid;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import pst.arm.client.common.ui.controls.components.events.CustomDatePickerEvent;

import java.util.Date;

/**
 * Created by wesStyle on 23/09/14.
 */
public class DMonthYearMenu extends Menu {
    protected DMonthYearPicker pickerMY;
    //protected DatePicker pickerMY;

    public DMonthYearMenu() {
        pickerMY = new DMonthYearPicker();
        pickerMY.addListener(Events.Select, new Listener<CustomDatePickerEvent>() {
            public void handleEvent(CustomDatePickerEvent be) {
                onPickerSelect(be);
            }
        });
        add(pickerMY);
        addStyleName("x-date-menu");
        setAutoHeight(true);
        plain = true;
        showSeparator = false;
        setEnableScrolling(false);
    }

    @Override
    public void focus() {
        super.focus();
        pickerMY.el().focus();

    }

    /**
     * Returns the selected date.
     *
     * @return the date
     */
    public Date getDate() {
        return pickerMY.getValue();
    }

    /**
     * Returns the date picker.
     *
     * @return the date picker
     */
//    public DMonthYearPicker getDatePicker() {
//        return pickerMY;
//    }
    public DMonthYearPicker getDatePicker() {
        return pickerMY;
    }

    /**
     * Sets the menu's date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        pickerMY.setValue(date);
    }

    protected void onPickerSelect(CustomDatePickerEvent be) {
        MenuEvent e = new MenuEvent(this);
        e.setDate(be.getDate());
        fireEvent(Events.Select, e);
    }
}
