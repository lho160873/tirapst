package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import java.util.HashMap;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 * @author LKHorosheva
 */
public class DColumnBuilderMultiForAppProductionOrderNum extends DColumnBuilderMulti {
    
            
    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted) {
        if (key.equals(new SKeyForColumn("MAIN:ORDER_NUM"))) {
            FormHelper.setReadOnlyProp(field, false);
        } else {
            FormHelper.setReadOnlyProp(field, true);
        }
    }

    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
        final HashMap<SKeyForColumn, Field> rc = super.createFields(fieldSet, service, isEnabled, lw);
        rc.get(new SKeyForColumn("MAIN:ORDER_ID")).addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
                if (fe.getValue() == null || fe.getValue().toString().isEmpty()) {
                    FormHelper.setReadOnlyProp(rc.get(new SKeyForColumn("MAIN:ORDER_NUM")), false);
                } else {
                    FormHelper.setReadOnlyProp(rc.get(new SKeyForColumn("MAIN:ORDER_NUM")), true);
                }
            }
        });

        return rc;
    }
}