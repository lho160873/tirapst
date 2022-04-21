package pst.arm.client.modules.technology.nomenclature.ui;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.google.gwt.event.dom.client.KeyCodes;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public abstract class FilterField extends StoreFilterField<ModelData> {
    private String lastValue = "";

    protected FilterField() {
        setAutoValidate(false);
        addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    validate();
                }
            }
        });
    }

    @Override
    protected boolean doSelect(Store<ModelData> store, ModelData parent, ModelData record, String property, String filter) {
        return false;
    }

    @Override
    protected boolean validateValue(String value) {
        boolean ret = super.validateValue(value);
        if (value == null) {
            value = "";
        }
        if (!value.equalsIgnoreCase(lastValue)) {
            lastValue = value;
            changeFilterValue(getFilterValue());
        }
        return ret;
    }

    public abstract void changeFilterValue(String value);

    public String getFilterValue() {
        return lastValue.isEmpty() ? null : lastValue;
    }
}