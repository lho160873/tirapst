package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderMultiWorkPlanRot;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
public class DataGridEditableFormWorkPlan extends DataGridEditableForm {

    protected int companyId;

    public DataGridEditableFormWorkPlan(DTable table, Editable.EditState state, DCondition cndMulti, int companyId) {
        super();
        this.companyId = companyId;

        setTable(table);
        this.state = state;
        this.conditionMulti = cndMulti;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents2();
    }


    protected void initComponents2() {

        ComponentPlugin redCaption = new ComponentPlugin() {
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {
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
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {
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
            if (conditionMulti != null && builder instanceof DColumnBuilderMulti) {
                ((DColumnBuilderMulti) builder).setAdditionalCondition(conditionMulti);
            } else if (conditionMulti != null && builder instanceof DColumnBuilderMultiForCommander) {
                ((DColumnBuilderMultiForCommander) builder).setAdditionalCondition(conditionMulti);
            }

            LayoutContainer simple = builder.createLayoutContainer(table.getLabelWidth());
            Map<SKeyForColumn, Field> flds;
            if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderMultiWorkPlanRot")) {
                DColumnBuilderMultiWorkPlanRot ba = (DColumnBuilderMultiWorkPlanRot) builder;
                flds = ba.createFields(companyId, simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
            }
            else if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti")) {
                DColumnBuilderMulti ba = (DColumnBuilderMulti) builder;
                flds = ba.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
            } else if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMultiForCommander")) {
                DColumnBuilderMultiForCommander bb = (DColumnBuilderMultiForCommander) builder;
                flds = bb.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
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
                //fld.setFieldLabel("LABEL");
            }

            fields.putAll(flds);
            add(simple, new RowData(1, -1));
        }
        setEditableFields();
        setIsReadOnly(state == Editable.EditState.VIEW);

        if ("WORKER".equals(table.getName())) {
            b = new Button();
            b.setText("Сформировать инициалы");
            b.addSelectionListener(new SelectionListener() {
                @Override
                public void componentSelected(ComponentEvent ce) {
                    SKeyForColumn nameKey = new SKeyForColumn("MAIN:NAME");
                    String fio = fields.get(nameKey).getRawValue();

                    SKeyForColumn shortNameKey = new SKeyForColumn("MAIN:SHORT_NAME");

                    // Проверяем строку на соответствие виду "  Фамилия   Имя      Отчество "
                    // или " Фамилия-Фамилия      Имя Отчество" (неважно, сколько пробелов)

                    final String PATTERN = "^[\\s]*[A-ZА-Я][a-zа-я]+([-|-][A-ZА-Я][a-zа-я]+)?[\\s]+[A-ZА-Я][a-zа-я]+[\\s]+[A-ZА-Я][a-zа-я]+[\\s]*$";

                    if (!fio.matches(PATTERN)) {
                        MessageBox.alert("Неправильно заполнено поле", "Фамилия, имя и отчество должны начинаться с заглавной буквы, далее следуют"
                                + " строчные буквы (пример: Иванов Иван Иванович)", null);
                    } else {
                        String cfio = convertFio(fio);
                        fields.get(shortNameKey).setValue(cfio);
                    }
                }
            });
            this.add(b);
        }
    }
}
