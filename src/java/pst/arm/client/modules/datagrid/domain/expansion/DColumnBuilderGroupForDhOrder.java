package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderGroupForDhOrder  extends DColumnBuilderGroup {

    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
        HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();
        FormData data = new FormData("100%");
        data.setMargins(new Margins(0, 32, 0, 0));
        
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
             Boolean isNotNull = this.getIsNotNull(key);
            Field field = this.getColumns().get(key).getColumnProperty().createField(isNotNull);
            if (isNotNull) {
                FormHelper.setNotEmptyFieldLabel(this.getColumn(key).getCaption(), field);
            } else {
                field.setFieldLabel(this.getColumn(key).getCaption());
            }
            field.setVisible(this.getColumn(key).getIsVisibleEdit());
            FormHelper.setReadOnlyProp(field, !this.getColumn(key).getIsEditable());
            //field.setEnabled(this.getColumn(key).getIsEditable());

            if (this.getColumns().get(key).getName().equals("VAL2")) {
                LabelField lbl = new LabelField();
                lbl.setFieldLabel("В том числе:");
                fieldSet.add(lbl, data);
            }
            fieldSet.add(field, data);
            fields.put(key, field);
        }
        return fields;
    }

  
}
