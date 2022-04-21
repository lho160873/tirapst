package pst.arm.client.common.ui.controls.datagrid;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

import java.util.Date;

/**
 * Created by wesStyle on 23/09/14.
 */
public class DMonthYearField extends com.extjs.gxt.ui.client.widget.form.DateField {
    private DMonthYearMenu menu;
    private Date minValue;
    private Date maxValue;

    public DMonthYearPicker getDateMYPicker() {
        if (menu == null) {
            menu = new DMonthYearMenu();

            menu.getDatePicker().addListener(Events.Select, new Listener<ComponentEvent>() {
                public void handleEvent(ComponentEvent ce) {
                    focusValue = getValue();
                    setValue(menu.getDate());
                    menu.hide();
                }
            });
            menu.addListener(Events.Hide, new Listener<ComponentEvent>() {
                public void handleEvent(ComponentEvent be) {
                    focus();
                }
            });
        }
        return menu.getDatePicker();
    }


    protected void expand() {
        DMonthYearPicker picker = getDateMYPicker();

        Object v = getValue();
        Date d = null;
        if (v instanceof Date) {
            d = (Date) v;
        } else {
            d = new Date();
        }

        picker.setMinDate(minValue);
        picker.setMaxDate(maxValue);
        picker.setValue(d, true);

        // handle case when down arrow is opening menu
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                menu.show(el().dom, "tl-bl?");
                menu.getDatePicker().focus();
            }
        });
    }

    @Override
    protected void onKeyDown(FieldEvent fe) {
        super.onKeyDown(fe);
        if (fe.getKeyCode() == KeyCodes.KEY_DOWN) {
            fe.stopEvent();
            if (menu == null || !menu.isAttached()) {
                expand();
            }
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        new KeyNav<FieldEvent>(this) {

            @Override
            public void onEsc(FieldEvent fe) {
                if (menu != null && menu.isAttached()) {
                    menu.hide();
                }
            }
        };

        if (GXT.isAriaEnabled()) {
            getInputEl().dom.setAttribute("title", getMessages().getAriaText());
        }
    }

    @Override
    protected boolean validateBlur(DomEvent e, Element target) {
        return menu == null || (menu != null && !menu.isVisible());
    }


}
