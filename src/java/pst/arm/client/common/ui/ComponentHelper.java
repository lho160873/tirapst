package pst.arm.client.common.ui;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Created by wesStyle on 16.07.2015.
 */
public class ComponentHelper {
    private static void safeFunctionCallOn(final Component c, final Function f) {
        c.enableEvents(true);
        if (c.isRendered()) {
            f.execute();
        } else {
            final Listener lsnr = new Listener() {
                @Override
                public void handleEvent(BaseEvent be) {
                    f.execute();
                }
            };
            c.addListener(Events.Render, lsnr);
        }
    }

    public static void setFieldAttr(final Field<?> field, final String attr, final String val) {
        safeFunctionCallOn(field, new Function() {
            @Override
            public void execute() {
                field.el().firstChild().setStyleAttribute(attr, val);
            }
        });
    }
}
