package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import java.util.HashMap;
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
public class DataGridEditableFormDocFile extends LayoutContainer implements EditableForm<DDataGrid> {

    private Map<SKeyForColumn, Field> fields;
    protected DTable table;
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    final protected DataGridMessages datagridMessages = GWT.create(DataGridMessages.class);
    private EditState state;
    private DCondition conditionMulti;
    private Button b;

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
        setIsReadOnly(state == EditState.VIEW);
    }

    public DataGridEditableFormDocFile(DTable table) {
        super();
        setTable(table);
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableFormDocFile(DTable table, EditState state) {
        super();
        setTable(table);
        this.state = state;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableFormDocFile(DTable table, EditState state, DCondition cndMulti) {
        super();
        setTable(table);
        this.state = state;
        this.conditionMulti = cndMulti;
        fields = new HashMap<SKeyForColumn, Field>();
        initComponents();
    }

    public DataGridEditableFormDocFile(DTable table, EditState state, DCondition cndMulti, String customHeader) {
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
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                if (builder instanceof DColumnBuilderMultiForWorkPlan) {
                    builder.setEditableField(key, fields.get(key), state == EditState.NEW, state == EditState.VIEW);
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

    private void initComponents() {

        setStyleAttribute("padding", "10px");
        setScrollMode(Scroll.AUTOY);
        setLayout(new RowLayout(Orientation.VERTICAL));

        formPanel = new FormPanel();

        formPanel.setBodyBorder(false);
        formPanel.setHeaderVisible(false);
        formPanel.setAction(GWT.getHostPageBaseURL() + "upload.htm");
        formPanel.setEncoding(FormPanel.Encoding.MULTIPART);
        formPanel.setMethod(FormPanel.Method.POST);
        formPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            if (conditionMulti != null && builder instanceof DColumnBuilderMulti) {
                ((DColumnBuilderMulti) builder).setAdditionalCondition(conditionMulti);
            }

            LayoutContainer simple = builder.createLayoutContainer(table.getLabelWidth());
            Map<SKeyForColumn, Field> flds;
            if (builder.getClass().getName().equals("pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti")) {
                DColumnBuilderMulti b = (DColumnBuilderMulti) builder;
                flds = b.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth(), state == Editable.EditState.NEW);
            } else {
                flds = builder.createFields(simple, service, state != Editable.EditState.VIEW, table.getLabelWidth());
            }

            for (Map.Entry ent : flds.entrySet()) {
                Field fld = (Field) ent.getValue();
                SKeyForColumn key = (SKeyForColumn) ent.getKey();

                //fld.setFieldLabel("LABEL");
            }

            fields.putAll(flds);
        }
        setEditableFields();
        setIsReadOnly(state == Editable.EditState.VIEW);

        for (Map.Entry f : fields.entrySet()) {
            if (table.getPostFields().contains((SKeyForColumn)f.getKey())) {
                if (f.getKey().equals(new SKeyForColumn("MAIN:ORIG_FILENAME"))) {
                    Field fi = (Field)f.getValue();
                    fi.setName("MAIN-FILENAME");
                }
                formPanel.add((Field)f.getValue());
            }
        }

        aSubmitButton = new Button("OK");
        aSubmitButton.setEnabled(true);
        aSubmitButton.setId("submit_button");
        aSubmitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent inButtonEvent) {
                formPanel.submit();
                formPanel.mask();
            }
        });

        this.add(formPanel);
    }

    FormPanel formPanel;

    public FormPanel getFormPanel() {
        return formPanel;
    }

    Button aSubmitButton;
}
