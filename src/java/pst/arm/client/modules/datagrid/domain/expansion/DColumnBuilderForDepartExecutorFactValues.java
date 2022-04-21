package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderForDepartExecutorFactValues extends IColumnBuilder implements Serializable {

    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
        final HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();
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
            fieldSet.add(field, data);
            fields.put(key, field);

            if (key.equals(new SKeyForColumn("MAIN:FACT_LABOUR"))) {

                field.setFireChangeEventOnSetValue(true);
                field.addListener(Events.Change, new Listener<FieldEvent>() {
                    @Override
                    public void handleEvent(FieldEvent fe) {
                        if (fe.getValue() == null) {
                            fields.get(new SKeyForColumn("MAIN:FACT_COST")).setValue(0.0);
                            return;
                        }

                        Double factLabour = 0.0;
                        Double planCost = 0.0;
                        Double planLabour = 0.0;
                        Double factCost = 0.0;

                        factLabour = Double.valueOf(fe.getValue().toString());
                        if (fields.get(new SKeyForColumn("WORK_PLAN:PLAN_COST")).getValue() != null) {
                            planCost = Double.valueOf(fields.get(new SKeyForColumn("WORK_PLAN:PLAN_COST")).getValue().toString());
                        }
                        if (fields.get(new SKeyForColumn("WORK_PLAN:PLAN_LABOUR")).getValue() != null) {
                            planLabour = Double.valueOf(fields.get(new SKeyForColumn("WORK_PLAN:PLAN_LABOUR")).getValue().toString());
                        }

                        if (planLabour != 0) {
                            factCost = (planCost / planLabour) * factLabour;
                        }
                        fields.get(new SKeyForColumn("MAIN:FACT_COST")).setValue(factCost);
                    }
                });

            }
        }
        return fields;
    }

    @Override
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (!domain.getRows().containsKey(key)) {
            field.clear();
            //field.setValue("");
            return;
        }
        if (domain.getRows().get(key).getFormatVal(key, this) == null) {
            field.clear();
            //field.setValue("");
            return;
        }
        field.setValue(domain.getRows().get(key).getFormatVal(key, this));
    }

    @Override
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid domain) {
        IRowColumnVal val = this.createRowColumnVal(key);
        setValueFromField(key, field, val);
        domain.getRows().put(key, val);
    }

    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        val.setValFromFormat(key, field.getValue(), this);
    }

    @Override
    public LayoutContainer createLayoutContainer(Integer lw) {
        LayoutContainer fieldSet = new LayoutContainer();
        fieldSet.setBorders(false);
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);
        fieldSet.setLayout(layout);
        fieldSet.setStyleAttribute("padding", "0px");
        fieldSet.setStyleAttribute("padding-left", "5px");
        //fieldSet.setStyleAttribute("padding", "5px");
        //проверяем есть ли хоть одно видимое поле
        Boolean isVisible = false;         
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
           isVisible = ( isVisible || this.getColumn(key).getIsVisibleEdit() );        
        }
        fieldSet.setVisible(isVisible);

        return fieldSet;
    }
}
