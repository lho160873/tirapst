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
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.datepicker.client.CalendarUtil;
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
public class DataGridEditableFormWorkPlanExecutorNew extends DataGridEditableForm {

    protected Date parentBeginDate;
    protected Date parentEndDate;
    protected List<DCondition> conditions;

    public List<DCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DCondition> conditions) {
        this.conditions = conditions;
    }

    public DataGridEditableFormWorkPlanExecutorNew(DTable table, Editable.EditState state, List<DCondition> conditions, Date planBegDate, Date planEndDate) {
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
//            b.setText("Сформировать инициалы");
//            b.addSelectionListener(new SelectionListener() {
//                @Override
//                public void componentSelected(ComponentEvent ce) {
//                    SKeyForColumn nameKey = new SKeyForColumn("MAIN:NAME");
//                    String fio = fields.get(nameKey).getRawValue();
//
//                    SKeyForColumn shortNameKey = new SKeyForColumn("MAIN:SHORT_NAME");
//
//                    // Проверяем строку на соответствие виду "  Фамилия   Имя      Отчество " 
//                    // или " Фамилия-Фамилия      Имя Отчество" (неважно, сколько пробелов)
//
//                    final String PATTERN = "^[\\s]*[A-ZА-Я][a-zа-я]+([-|-][A-ZА-Я][a-zа-я]+)?[\\s]+[A-ZА-Я][a-zа-я]+[\\s]+[A-ZА-Я][a-zа-я]+[\\s]*$";
//
//                    if (!fio.matches(PATTERN)) {
//                        MessageBox.alert("Неправильно заполнено поле", "Фамилия, имя и отчество должны начинаться с заглавной буквы, далее следуют"
//                                + " строчные буквы (пример: Иванов Иван Иванович)", null);
//                    } else {
//                        String cfio = convertFio(fio);
//                        fields.get(shortNameKey).setValue(cfio);
//                    }
//                }
//            });
//            this.add(b);
//        }

        final DateField dateField = (DateField) fields.get(new SKeyForColumn("WORK_PLAN_EXECUTOR_NEW_FOR_PREVIOUS:END_DATE"));

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
                                beginDateField.setValue(newDate);
                                beginDateField.setMinValue(newDate);
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

    public void updateMinBegDate(Date newDate, boolean storeIsEmpty) {
        if (fields != null) {
            DateField beginDateField = (DateField) fields.get(new SKeyForColumn("MAIN:BEG_DATE"));
            DateField endDateField = (DateField) fields.get(new SKeyForColumn("MAIN:END_DATE"));
            
            if (beginDateField != null && endDateField != null) {                
                int days = CalendarUtil.getDaysBetween(beginDateField.getValue(), endDateField.getValue());     
                Date newEndDate = (Date)newDate.clone();
                CalendarUtil.addDaysToDate(newEndDate, days);
                if (storeIsEmpty) {
                    beginDateField.setMinValue(newDate);
                    endDateField.setMinValue(newDate);
                } else {
                    if (( newDate.equals(parentBeginDate) || newDate.after(parentBeginDate)) && (newDate.before(parentEndDate) || newDate.equals(parentEndDate))) {
                        beginDateField.setMinValue(newDate);
                        if (beginDateField.getValue() != null) {
                            if (beginDateField.getValue().before(newDate)) {
                                beginDateField.setValue(newDate);
                            }
                        } else {
                            beginDateField.setValue(newDate);                            
                        }
                        beginDateField.setMinValue(newDate);                        
                        endDateField.setMinValue(newDate);
                        if (endDateField.getValue() != null) {
                            if (endDateField.getValue().before(newDate)) {
                                endDateField.setValue(newDate);
                            }
                            if (state == Editable.EditState.EDIT && newEndDate.after(newDate)) {
                                if ((newEndDate.before(parentEndDate) || newEndDate.equals(parentEndDate))) {
                                    endDateField.setValue(newEndDate);
                                }                            
                            }
                        } else {
                            endDateField.setValue(newDate);
                        }
                        endDateField.setMinValue(newDate);
                    }
                }
            }
        }
    }

    public void updateMinEndDate(Date newDate, boolean storeIsEmpty) {
        if (fields != null) {
            DateField endDateField = (DateField) fields.get(new SKeyForColumn("MAIN:END_DATE"));
            
            if (endDateField != null) {
                if (storeIsEmpty) {
                    endDateField.setMinValue(newDate);
                } else {
                    if (( newDate.equals(parentBeginDate) || newDate.after(parentBeginDate)) && (newDate.before(parentEndDate) || newDate.equals(parentEndDate))) {
                        endDateField.setMinValue(newDate);
                        if (endDateField.getValue() != null) {
                            if (endDateField.getValue().before(newDate)) {
                                endDateField.setValue(newDate);
                            }
                        } else {
                            endDateField.setValue(newDate);
                        }
                        endDateField.setMinValue(newDate);
                    }
                }
            }
        }
    }
}
