package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.form.EditableForm;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderMultiForWorkPlan;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.lang.DataGridMessages;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridEditableForm extends LayoutContainer implements EditableForm<DDataGrid> {

    protected Map<SKeyForColumn, Field> fields;
    protected DTable table;
    protected GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    final protected DataGridMessages datagridMessages = GWT.create(DataGridMessages.class);
    protected EditState state;
    protected DCondition conditionMulti;
    protected Button b;

    public DCondition getConditionMulti() {
        return conditionMulti;
    }

    public void setConditionMulti(DCondition conditionMulti) {
        this.conditionMulti = conditionMulti;
    }

    public void setTable(DTable table) {
        this.table = table;
    }

    public void setState(EditState state) {
        this.state = state;
        setEditableFields();
        setIsReadOnly(state == Editable.EditState.VIEW);
    }

    public DataGridEditableForm(DTable table) {
        super();
        setTable(table);
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableForm(DTable table, EditState state) {
        super();
        setTable(table);
        this.state = state;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableForm() {
        super();
    }


    public DataGridEditableForm(DTable table, EditState state, DCondition cndMulti) {
        super();
        setTable(table);
        this.state = state;
        this.conditionMulti = cndMulti;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableForm(DTable table, EditState state, DCondition cndMulti, String customHeader) {
        super();
        setTable(table);
        this.state = state;
        this.conditionMulti = cndMulti;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    /*
     * TODO здесь можно выполнить все проверки 
     */
    @Override
    public Boolean validate() {
        Boolean isValid = Boolean.TRUE;
        for (Map.Entry fieldEntry : fields.entrySet()) {
            Field f = (Field) fieldEntry.getValue();

            if ((f instanceof TextField)) {
                if (!((TextField) f).getAllowBlank() && f.getRawValue().equals("")) {
                    isValid = Boolean.FALSE;
                }
            }

            if (f.isVisible() && !f.isValid()) {
                //SKeyForColumn ff = (SKeyForColumn)fieldEntry.getKey();
                isValid = Boolean.FALSE;
            }
        }
        if (!isValid) {
            MessageBox.alert(commonConstants.error(), datagridMessages.errorNotFieldsValid(), null).show();
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        for (Map.Entry fieldEntry : fields.entrySet()) {
            Field f = (Field) fieldEntry.getValue();
            f.clear();
        }
    }

    /*
     * заполняем поля ввода информацией из полученных данных (DDataGrid)
     *
     */
    @Override
    public void setDomain(DDataGrid domain) {
        //clear();
        // MessageBox.info("1","setDomain",null);
        if (domain == null) {
            clear();
            return;
        }
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                builder.setDomainValueToField(key, fields.get(key), domain);
            }
        }
    }

    /*
     * заполняем данные(DDataGrid) информацией из полей ввода
     * 
     */
    @Override
    public void fillDomain(DDataGrid domain) {
        domain.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                builder.setDomainValueFromField(key, fields.get(key), domain);
            }
        }
    }

    public DDataGrid createDomain() {
        DDataGrid domain = new DDataGrid();
        domain.setName(table.getQueryName());
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                builder.setDomainValueFromField(key, fields.get(key), domain);
            }
        }
        return domain;
    }

    public void setEditableFields() {
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            if (builder instanceof DColumnBuilderComboBoxAndCheckList) {
                continue;
            }
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                
                
                if (builder instanceof DColumnBuilderMultiForWorkPlan) {
                    builder.setEditableField(key, fields.get(key), state == EditState.NEW, state == EditState.VIEW);
                } else 
                    
                    if (builder instanceof DColumnBuilderMultiForCommander) {
                    if ((new SKeyForColumn("MAIN:POST_NAME").equals(key)) && state == EditState.NEW) {
                        builder.setEditableField(key, fields.get(key), true);
                    } else {
                        builder.setEditableField(key, fields.get(key), state == EditState.NEW, state == EditState.VIEW);
                    }
                } else {
                    builder.setEditableField(key, fields.get(key), state == EditState.NEW);
                }
            }
        }
    }
    

    public void setIsReadOnly(Boolean r) {
        for (Map.Entry fieldEntry : fields.entrySet()) {
            Field f = (Field) fieldEntry.getValue();
            if (!f.isReadOnly()) { //можем править это свойство если ранее оно уже не было установлено в недоступно
                FormHelper.setReadOnlyProp(f,r);
            }
        }        
        /*for (Map.Entry fieldEntry : fields.entrySet()) {
            Field f = (Field) fieldEntry.getValue();
            if (f.isEnabled()) { //можем править это свойство если ранее оно уже не было установлено в недоступно
                f.setReadOnly(r);
                if (r) {
                    f.setInputStyleAttribute("color", "gray");
                }
                //f.setEnabled(!r);
            }
        }*/
    }

    protected void initComponents() {

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
        setScrollMode(Scroll.AUTOY);
        setLayout(new RowLayout(Orientation.VERTICAL));

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            if (builder instanceof DColumnBuilderComboBoxAndCheckList)
                continue;
            if (conditionMulti != null && builder instanceof DColumnBuilderMulti) {
                ((DColumnBuilderMulti) builder).setAdditionalCondition(conditionMulti);
            } else if (conditionMulti != null && builder instanceof DColumnBuilderMultiForCommander) {
                ((DColumnBuilderMultiForCommander) builder).setAdditionalCondition(conditionMulti);
            }

            LayoutContainer simple = builder.createLayoutContainer(table.getLabelWidth());
            Map<SKeyForColumn, Field> flds;
            if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti")) {
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
    FormPanel formPanel;
    Button aSubmitButton;

    /**
     * Переводит строку вида "Фамилия Имя Отчество" (или "Фамилия-Фамилия Имя
     * Отчество") в "И.О.Фамилия" (или "И.О.Фамилия-Фамилия)"
     *
     * @param fio
     * @return И.О.Фамилия
     * @author Igor
     */
    static protected String convertFio(String fio) {
        final String UPPER_LETTERS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int iIndex = 0;
        int oIndex = 0;
        String[] fioArray = {"", "", ""};                           // ["Фамилия", "Имя", "Отчество"]

        fio = fio.replaceAll("\\s+", "");

        for (int k = fio.length() - 1; k >= 0; k--) {
            if (UPPER_LETTERS.indexOf(fio.charAt(k)) != -1) {
                oIndex = k;
                break;
            }
        }

        for (int k = oIndex - 1; k >= 0; k--) {
            if (UPPER_LETTERS.indexOf(fio.charAt(k)) != -1) {
                iIndex = k;
                break;
            }
        }

        try {
            fioArray[0] = fio.substring(0, iIndex);                 // Фамилия
            fioArray[1] = fio.substring(iIndex, oIndex);            // Имя
            fioArray[2] = fio.substring(oIndex, fio.length());      // Отчество
        } catch (StringIndexOutOfBoundsException e) {
            // Фамилия и/или Имя и/или Отчество написан(ы) с маленькой буквы
        }

        String result = fioArray[1].charAt(0) + "." + fioArray[2].charAt(0) + "." + fioArray[0];

        return result;
    }
}
