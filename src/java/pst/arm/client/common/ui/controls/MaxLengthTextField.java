package pst.arm.client.common.ui.controls;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Element;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class MaxLengthTextField extends TextField<String> {

    public MaxLengthTextField(Integer len) {
        setMaxLength(len);

    }

    @Override
    public void setMaxLength(int m) {
        super.setMaxLength(m);
        if (rendered) {
            getInputEl().setElementAttribute("maxLength", m);
        }
    }

    /**
     * For purposes of max char to input we override this method
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength());
    }

    @Override
    protected void onBlur(ComponentEvent be) {
        super.onBlur(be);
        if (this.getValue() != null) {
            this.setValue(this.getValue().trim());
        }
    }
}
