package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author n
 */
public class DColumnBuilderMultiConditionList extends DColumnBuilderMulti {

    protected String builderName = "Default"; // Имя билдера, для использования одного билдера в разных запросах
    protected List<DCondition> conditions;

    public String getBuilderName() {
        return builderName;
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }

    public List<DCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw, Boolean isAdd) {
        DataGridConstants constants = GWT.create(DataGridConstants.class);
        DataGridImages images = GWT.create(DataGridImages.class);
        final DataGridWindow page;
        if (conditions != null) {
            page = new DataGridWindow(queryNameForRelation, isViewOnly, true, conditions);
        } else {
            page = new DataGridWindow(queryNameForRelation, isViewOnly, true);
        }

        final HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();

        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                String id = be.getButton().getItemId();
                if (id.equals("open")) {
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {
                            page.hide();

                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            setValToFields(fields, rows, table);
                        }
                    };
                    page.addDataGridListener(listenerGetDomain);
                    page.show();
                }
                if (id.equals("clear")) {
                    for (Map.Entry colEntry : relations.entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        fields.get(key).clear();
                        fields.get(key).validate();

                        FieldEvent e = new FieldEvent(fields.get(key));
                        e.setValue(null);
                        fields.get(key).fireEvent(Events.Change, e);
                    }
                }
            }
        };

        LayoutContainer bottom = new LayoutContainer();
        HBoxLayout layoutBottom = new HBoxLayout();
        layoutBottom.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.MIDDLE);
        layoutBottom.setPack(BoxLayout.BoxLayoutPack.END);
        bottom.setLayout(layoutBottom);

        Button bOpenDataGrid = new Button("", AbstractImagePrototype.create(images.book()), buttonEventListener);
        bOpenDataGrid.setToolTip(constants.btnOpenDataGrid());
        bOpenDataGrid.setSize(22, 22);
        bOpenDataGrid.setItemId("open");
        bOpenDataGrid.setEnabled(isEnabled);

        Button bClearData = new Button("", AbstractImagePrototype.create(images.cancel()), buttonEventListener);
        bClearData.setToolTip(constants.btnClearDataGrid());
        bClearData.setSize(22, 22);
        bClearData.setItemId("clear");
        bClearData.setEnabled(isEnabled);

        boolean isAddBtn = false;
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
            FormHelper.setReadOnlyProp(field, isReadOnly);
            if (isAddBtn || (!isAddBtn && !this.getColumn(key).getIsVisibleEdit())) {
                FormData data = new FormData("100%");
                data.setMargins(new Margins(0, 32, 0, 0));
                fieldSet.add(field, data);
            } else if (this.getColumn(key).getIsVisibleEdit()) {
                isAddBtn = true;

                LayoutContainer fieldSetRow = new LayoutContainer();
                fieldSetRow.setBorders(Boolean.FALSE);
                fieldSetRow.setLayout(new HBoxLayout());
                fieldSetRow.setStyleAttribute("padding", "0px");

                FormData data = new FormData("100%");
                fieldSet.add(fieldSetRow, data);

                LayoutContainer fieldSetBox = new LayoutContainer();
                fieldSetBox.setBorders(false);
                FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
                layout.setLabelWidth(lw);
                fieldSetBox.setLayout(layout);

                fieldSetBox.add(field, data);

                HBoxLayoutData dataBox = new HBoxLayoutData();
                dataBox.setMargins(new Margins(0, 0, 0, 0));
                dataBox.setFlex(1);
                fieldSetRow.add(fieldSetBox, dataBox);

                HBoxLayoutData dataBtn = new HBoxLayoutData();
                dataBtn.setMargins(new Margins(0, 0, 0, 21));
                dataBtn.setFlex(0);

                HBoxLayoutData dataBtn32 = new HBoxLayoutData();
                dataBtn32.setMargins(new Margins(0, 32, 0, 5));
                dataBtn32.setFlex(0);

                if (!this.getColumn(key).getIsEditable() || (!this.getColumn(key).getIsEditableAdd() && isAdd)) {
                    bOpenDataGrid.setEnabled(false);
                    bClearData.setEnabled(false);
                }

                if (isAdd && this.getColumn(key).getIsEditableAdd()) {
                    bOpenDataGrid.setEnabled(true);
                    bClearData.setEnabled(true);
                }

                fieldSetRow.add(bOpenDataGrid, dataBtn);
                fieldSetRow.add(bClearData, dataBtn32);

            }
            fields.put(key, field);
        }
        return fields;
    }

    @Override
    protected void setValToFields(HashMap<SKeyForColumn, Field> fields, HashMap<SKeyForColumn, IRowColumnVal> rows, DTable table) {
        for (Map.Entry colEntry : relations.entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            SKeyForColumn key_r = (SKeyForColumn) colEntry.getValue();
            fields.get(key).setValue(rows.get(key_r).getFormatVal(key_r, table.getColumnBuilder(key_r)));
            if ("WORK_PLAN_EXECUTOR_FOR_PREVIOUS.END_DATE".equals(key.toString()) || "WORK_PLAN_EXECUTOR_NEW_FOR_PREVIOUS.END_DATE".equals(key.toString())) {
                FieldEvent e = new FieldEvent(fields.get(key));
                e.setValue(rows.get(key_r).getFormatVal(key_r, table.getColumnBuilder(key_r)));
                fields.get(key).fireEvent(Events.Change, e);
            }
            
        }
    }
}
