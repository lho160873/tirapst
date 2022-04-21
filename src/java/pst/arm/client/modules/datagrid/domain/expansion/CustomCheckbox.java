package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.aria.FocusFrame;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.form.BooleanPropertyEditor;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Accessibility;

/**
 * Created by wesStyle on 10.06.2015.
 */


public class CustomCheckbox extends CheckBox {

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
    }

    private Timer t;
    private String boxLabel;

    public String getBoxLabel() {
        return boxLabel;
    }

    public void setBoxLabel(String boxLabel) {
        this.boxLabel = boxLabel;
        if (rendered) {
            boxLabelEl.update(getBoxLabel());
        }
    }

    protected void alignElements() {
        input.dom.getStyle().setProperty("left", "");
        input.dom.getStyle().setProperty("top", "");
        if (boxLabelEl != null) {
            boxLabelEl.dom.getStyle().setProperty("left", "");
            boxLabelEl.dom.getStyle().setProperty("top", "");
        }
        if (t != null) {
            t.cancel();
            t = null;
        }
        t = new Timer() {

            @Override
            public void run() {
                if (boxLabel == null) {
                    input.alignTo(getElement(), "c-c", null);
                    if (GXT.isIE || GXT.isOpera) {
                        input.alignTo(getElement(), "c-c", null);
                    }
                } else {
                    input.alignTo(getElement(), "l-l", new int[] {0, 0});
                    if (GXT.isIE || GXT.isOpera) {
                        input.alignTo(getElement(), "l-l", new int[] {0, 0});
                    }

                    boxLabelEl.alignTo(input.dom, "l-r", new int[] {5, GXT.isIE ? -1 : 0});
                    if (GXT.isIE || GXT.isOpera) {
                        boxLabelEl.alignTo(input.dom, "l-r", new int[] {5, GXT.isIE ? -1 : 0});
                    }
                }

                input.dom.getStyle().setProperty("left", "");
                input.dom.getStyle().setProperty("top", "");
                el().repaint();
                t = null;
            }
        };
        t.schedule(1);

    }
}
