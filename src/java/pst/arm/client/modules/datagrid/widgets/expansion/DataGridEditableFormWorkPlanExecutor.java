package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderComboBoxAndCheckList;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderMultiConditionList;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.ESubCaptonTextColor;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;

/**
 *
 * @author n
 */
public class DataGridEditableFormWorkPlanExecutor extends DataGridEditableForm {

    protected Date parentBeginDate;
    protected Date parentEndDate;
    protected List<DCondition> conditions;

    public List<DCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DCondition> conditions) {
        this.conditions = conditions;
    }

    public DataGridEditableFormWorkPlanExecutor(DTable table, Editable.EditState state, List<DCondition> conditions, Date planBegDate, Date planEndDate) {
        super();
        setTable(table);
        this.state = state;
        this.conditions = conditions;
        fields = new HashMap<SKeyForColumn, Field>();
        parentBeginDate = planBegDate;
        parentEndDate = planEndDate;
        initComponents();
    }

    @Override
    protected void initComponents() {

        ComponentPlugin redCaption = new ComponentPlugin() {
            @Override
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {
                    @Override
                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-form-element", 3);
                        if (elem != null) {
                            elem.appendChild(XDOM.create("<div style='color: #ff0000; padding: 1 0 2 0px;'>" + be.getComponent().getData("subCaptionText") + "</div>"));
                        }
                    }
                });
            }
        };

        ComponentPlugin blackCaption = new ComponentPlugin() {
            @Override
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {
                    @Override
                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-form-element", 3);
                        if (elem != null) {
                            elem.appendChild(XDOM.create("<div style='color: #000000; padding: 1 0 2 0px;'>" + be.getComponent().getData("subCaptionText") + "</div>"));
                        }
                    }
                });
            }
        };

        setStyleAttribute("padding-left", "5px");
        setStyleAttribute("padding-top", "10px");
        setStyleAttribute("padding-bottom", "10px");
        setScrollMode(Style.Scroll.AUTOY);
        setLayout(new RowLayout(Style.Orientation.VERTICAL));

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            if (builder instanceof DColumnBuilderComboBoxAndCheckList) {
                continue;
            }
            if (conditions != null && builder instanceof DColumnBuilderMultiConditionList) {
                if (((DColumnBuilderMultiConditionList) builder).getBuilderName().equals("PREVIOUS_WORK")) {
                    ((DColumnBuilderMultiConditionList) builder).setConditions(conditions);
                }
            }

            LayoutContainer simple = builder.createLayoutContainer(table.getLabelWidth());
            Map<SKeyForColumn, Field> flds;
            if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti")) {
                DColumnBuilderMulti ba = (DColumnBuilderMulti) builder;
                flds = ba.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
            } else if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMultiConditionList")) {
                DColumnBuilderMultiConditionList ba = (DColumnBuilderMultiConditionList) builder;
                flds = ba.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
            } else {
                flds = builder.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth());
            }

            for (Map.Entry ent : flds.entrySet()) {
                Field fld = (Field) ent.getValue();
                SKeyForColumn key = (SKeyForColumn) ent.getKey();

                if (table.getColumnBuilder(key).getColumns().get(key).getSubCaption() != null) {
                    if (table.getColumnBuilder(key).getColumns().get(key).getSubCaptionTextColor() == ESubCaptonTextColor.RED) {
                        fld.addPlugin(redCaption);
                    } else if (table.getColumnBuilder(key).getColumns().get(key).getSubCaptionTextColor() == ESubCaptonTextColor.BLACK) {
                        fld.addPlugin(blackCaption);
                    }
                }
            }

            fields.putAll(flds);
            add(simple, new RowData(1, -1));
        }
        setEditableFields();
        setIsReadOnly(state == Editable.EditState.VIEW);

//        if ("WORKER".equals(table.getName())) {
//            b = new Button();
//            b.setText("???????????????????????? ????????????????");
//            b.addSelectionListener(new SelectionListener() {
//                @Override
//                public void componentSelected(ComponentEvent ce) {
//                    SKeyForColumn nameKey = new SKeyForColumn("MAIN:NAME");
//                    String fio = fields.get(nameKey).getRawValue();
//
//                    SKeyForColumn shortNameKey = new SKeyForColumn("MAIN:SHORT_NAME");
//
//                    // ?????????????????? ???????????? ???? ???????????????????????? ???????? "  ??????????????   ??????      ???????????????? " 
//                    // ?????? " ??????????????-??????????????      ?????? ????????????????" (??????????????, ?????????????? ????????????????)
//
//                    final String PATTERN = "^[\\s]*[A-Z??-??][a-z??-??]+([-|-][A-Z??-??][a-z??-??]+)?[\\s]+[A-Z??-??][a-z??-??]+[\\s]+[A-Z??-??][a-z??-??]+[\\s]*$";
//
//                    if (!fio.matches(PATTERN)) {
//                        MessageBox.alert("?????????????????????? ?????????????????? ????????", "??????????????, ?????? ?? ???????????????? ???????????? ???????????????????? ?? ?????????????????? ??????????, ?????????? ??????????????"
//                                + " ???????????????? ?????????? (????????????: ???????????? ???????? ????????????????)", null);
//                    } else {
//                        String cfio = convertFio(fio);
//                        fields.get(shortNameKey).setValue(cfio);
//                    }
//                }
//            });
//            this.add(b);
//        }

        final DateField dateField = (DateField) fields.get(new SKeyForColumn("WORK_PLAN_EXECUTOR_FOR_PREVIOUS:END_DATE"));

        if (dateField != null) {
            dateField.addListener(Events.Change, new Listener<FieldEvent>() {
                @Override
                public void handleEvent(FieldEvent fe) {
                    DateField beginDateField = (DateField) fields.get(new SKeyForColumn("MAIN:BEG_DATE"));
                    DateField endDateField = (DateField) fields.get(new SKeyForColumn("MAIN:END_DATE"));
                    if (beginDateField != null && dateField != null && endDateField != null) {
                        if (dateField.getValue() != null) {
                            Date newDate = dateField.getValue();
                            if (newDate.after(parentBeginDate) && newDate.before(parentEndDate)) {
                                //if (beginDateField.getValue() != null) {
                                beginDateField.setValue(newDate);
                                beginDateField.setMinValue(newDate);
                                //} else {

                                //}
                                if (endDateField.getValue() != null) {
                                    if (endDateField.getValue().before(newDate)) {
                                        endDateField.setValue(newDate);
                                    }
                                }
                                endDateField.setMinValue(newDate);
                            }
                        } else {
                            beginDateField.setMinValue(parentBeginDate);
                            endDateField.setMinValue(parentBeginDate);
                        }
                    }
                }
            });
        }

    }
}
