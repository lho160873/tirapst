package pst.arm.client.modules.leveltask.widgets.out;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.form.EditableForm;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.leveltask.EModule;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.lang.LevelTaskConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class LevelTaskEditableForm extends LayoutContainer implements EditableForm<LevelTask> {

    private NumberField nfId, nfUserIdTo, nfUserIdFrom;
    private TextField tfUserNameTo, tfLl, tfDd;
    private DGComboBox<Integer> cbTaskId, cbTaskState;
    private TextArea taDescription, taReply;
    private CheckBox chbPriority, chbCurrentId;
    private Button btnGetUserTo;
    private TextField tfEventTime, tfCreateDate;
    private FieldSet fieldSetR;
    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    final protected LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
    private LayoutContainer main, mainBorder;
    private List<Field<?>> fields = new ArrayList<Field<?>>();
    private EditState state;
    private DataGridWindow pageUser = null;
    private BorderLayoutData layoutDataNorth;
    private Integer currentId;
    private Listener<DataGridEvent> listenerGetDomain;

    public void setState(EditState state) {
        this.state = state;

    }

    public void setIsReadOnly(Boolean r) {
        for (Field<?> field : fields) {
            FormHelper.setReadOnlyProp(field, r);
        }
        FormHelper.setReadOnlyProp(tfUserNameTo, true);
        btnGetUserTo.setEnabled(!r);
    }

    public LevelTaskEditableForm(EditState state) {
        super();
        this.state = state;

        initPanel();
        initComponents();
    }

    private void initPanel() {
        setLayout(new FitLayout());
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        mainBorder = new LayoutContainer(new BorderLayout());
        mainBorder.setBorders(false);
        mainBorder.setStyleAttribute("padding", "0px");

        BorderLayoutData layoutDataCenter = new BorderLayoutData(LayoutRegion.CENTER);
        layoutDataCenter.setMargins(new Margins(0));
        main.add(mainBorder, layoutDataCenter);

        add(main);
    }

    @Override
    public Boolean validate() {
        Boolean rc = FormHelper.isValid(fields);
        if (!rc) {
            MessageBox.alert(commonConstants.alert(), constants.errorNotFieldsValid(), null);
        }
        return rc;
    }

    @Override
    public void clear() {
        FormHelper.clearFields(fields);
    }

    /*
     * заполняем поля ввода информацией из полученных данных (ClientCard)
     *
     */
    @Override
    public void setDomain(LevelTask domain) {

        if (domain == null) {
            return;
        }
        FormHelper.setFieldValue(nfId, domain.getId());
        FormHelper.setFieldValue(nfUserIdFrom, domain.getUserIdFrom());
        FormHelper.setFieldValue(nfUserIdTo, domain.getUserIdTo());
        FormHelper.setFieldValue(tfUserNameTo, domain.getUserNameTo());

        FormHelper.setFieldValue(tfDd, domain.getDd());
        FormHelper.setFieldValue(tfLl, domain.getLl());
        taDescription.setValue(domain.getDescription());

        if (domain.getPriority() != null && domain.getPriority() == 1) {
            chbPriority.setValue(Boolean.TRUE);
        } else {
            chbPriority.setValue(Boolean.FALSE);
        }

        cbTaskId.setValueId(domain.getTaskId());
        cbTaskState.setValueId(domain.getTaskState());
        FormHelper.setFieldValue(taReply, domain.getReply());

        if (domain.getCreateDate() != null) {
            String createDate = DateTimeFormat.getFormat("dd.MM.yyyy  HH:mm").format(domain.getCreateDate());
            tfCreateDate.setValue(createDate);
        } else {
            tfCreateDate.clear();
        }

        if (domain.getEventTime() != null) {
            String eventDate = DateTimeFormat.getFormat("dd.MM.yyyy  HH:mm").format(domain.getEventTime());
            tfEventTime.setValue(eventDate);
        } else {
            tfEventTime.clear();
        }

        currentId = domain.getCurrentId();
        if (currentId != null) {

            String postfix = EModule.getModuleNameRus(ConfigurationManager.getModuleName());
            chbCurrentId.setBoxLabel(String.valueOf(currentId) + " " + postfix);
            chbCurrentId.setEnabled(true);
            if (state != EditState.NEW) {
                chbCurrentId.setValue(true);
            }
        } else {
            chbCurrentId.setEnabled(false);
        }
    }

    @Override
    public void fillDomain(LevelTask domain) {

        domain.setId(FormHelper.getIntValue(nfId));
        domain.setUserIdFrom(ConfigurationManager.getUserId().intValue());
        domain.setUserIdTo(FormHelper.getIntValue(nfUserIdTo));
        domain.setUserNameTo(tfUserNameTo.getRawValue());
        domain.setUserNameFrom(ConfigurationManager.getUserName());
        domain.setLl(tfLl.getRawValue());
        domain.setDd(tfDd.getRawValue());
        domain.setTaskId(cbTaskId.getValueId());
        domain.setTaskName(cbTaskId.getRawValue());
        //if (state == EditState.NEW) {
            domain.setTaskState(ETaskState.CREATE.getTaskState());
            domain.setStateName(ETaskState.getTaskStateName(ETaskState.CREATE));
        //} 

        domain.setDescription(taDescription.getValue());

        if (chbPriority.getValue()) {
            domain.setPriority(1);
        } else {
            domain.setPriority(0);
        }
        if (chbCurrentId.getValue()) {
            domain.setCurrentId(currentId);
            domain.setModule(ConfigurationManager.getModuleName());
        }
    }

    private void enableComponent() {
        if (state == EditState.NEW) {
            tfCreateDate.setVisible(Boolean.FALSE);
            cbTaskState.setVisible(Boolean.FALSE);
            taReply.setVisible(Boolean.FALSE);
            fieldSetR.setVisible(Boolean.FALSE);
            tfEventTime.setVisible(Boolean.FALSE);
        } else if (state == EditState.VIEW) {
            tfCreateDate.setVisible(Boolean.TRUE);
            cbTaskState.setVisible(Boolean.TRUE);
            taReply.setVisible(Boolean.TRUE);
            fieldSetR.setVisible(Boolean.TRUE);
            tfEventTime.setVisible(Boolean.TRUE);
        }

        if (layoutDataNorth != null) {
            if (state == EditState.NEW) {
                layoutDataNorth.setSize(160);
            } else {
                layoutDataNorth.setSize(255);
            }
            mainBorder.layout(true); //перерисовываем     
        }
    }

    public LevelTask createLevelTask() {
        LevelTask domain = new LevelTask();
        fillDomain(domain);
        return domain;
    }

    private void initPageUser() {
        if (pageUser != null) {
            return;
        }
        pageUser = new DataGridWindow("hlv_users", true, true, true);//WORK_PLACE_INDEX1");

        listenerGetDomain = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(DataGridEvent be) {
                pageUser.removeDataGridListener(listenerGetDomain);
                pageUser.hide();
                HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();

                SKeyForColumn key = new SKeyForColumn("MAIN", "USER_ID");
                if (rows.get(key).getVal() == null) {
                    nfUserIdTo.clear();
                } else {
                    nfUserIdTo.setValue((Integer) rows.get(key).getVal());
                }

                SKeyForColumn keyIdex = new SKeyForColumn("MAIN", "NAME");
                if (rows.get(keyIdex).getVal() == null) {
                    tfUserNameTo.clear();
                } else {
                    tfUserNameTo.setValue((String) rows.get(keyIdex).getVal());
                }

            }
        };
    }

    private void initComponents() {
        CommonImages images = GWT.create(CommonImages.class);

        //Тип и названии компании показываем наверху перед таб-панелью
        FieldSet fieldSetNorth = new FieldSet();
        fieldSetNorth.setBorders(false);
        fieldSetNorth.setLayout(new FormLayout());
        fieldSetNorth.setScrollMode(Style.Scroll.AUTO);
        fieldSetNorth.setStyleAttribute("padding", "6px 6px 0px 6px");

        layoutDataNorth = new BorderLayoutData(LayoutRegion.NORTH, 0.5f);
        layoutDataNorth.setMargins(new Margins(0, 0, 0, 0));
        layoutDataNorth.setSplit(true);
        mainBorder.add(fieldSetNorth, layoutDataNorth);

        FormData formData = new FormData("100%");
        formData.setMargins(new Margins(0, 32, 0, 0));

        FormData formDataFix = new FormData();
        formDataFix.setWidth(150);
        formDataFix.setMargins(new Margins(0, 32, 0, 0));

        nfId = new NumberField();
        nfId.setEmptyText(null);
        nfId.setFieldLabel(constants.columnId());
        fields.add(nfId);
        fieldSetNorth.add(nfId, formData);
        nfId.setVisible(Boolean.FALSE);

        cbTaskId = new DGComboBox<Integer>("TASK_TYPE", "MAIN:TASK_TYPE_ID", "MAIN:NAME");
        cbTaskId.setIsValidate(true);
        cbTaskId.setAllowBlank(false); //позволять пусто нет(false) 
        FormHelper.setNotEmptyFieldLabel(constants.columnTaskName(), cbTaskId);
        cbTaskId.setValidateOnBlur(Boolean.TRUE);
        fields.add(cbTaskId);
        fieldSetNorth.add(cbTaskId, formData);

        nfUserIdFrom = new NumberField();
        nfUserIdFrom.setEmptyText(null);
        nfUserIdFrom.setFieldLabel(constants.columnUserFrom());
        fields.add(nfUserIdFrom);
        fieldSetNorth.add(nfUserIdFrom, formData);
        nfUserIdFrom.setVisible(Boolean.FALSE);


        nfUserIdTo = new NumberField();
        nfUserIdTo.setEmptyText(null);
        nfUserIdTo.setFieldLabel(constants.columnUserTo());
        fields.add(nfUserIdTo);
        fieldSetNorth.add(nfUserIdTo, formData);
        nfUserIdTo.setVisible(Boolean.FALSE);

        tfUserNameTo = new TextField();
        tfUserNameTo.setEmptyText(null);
        tfUserNameTo.setFieldLabel(constants.columnUserTo());
        tfUserNameTo.setAllowBlank(false);
        //tfUserNameTo.setReadOnly(true);
        //FormHelper.setReadOnlyProp(tfUserNameTo, true);
        FormHelper.setNotEmptyFieldLabel(constants.columnUserTo(), tfUserNameTo);
        fields.add(tfUserNameTo);
        fieldSetNorth.add(tfUserNameTo, formData);

        tfDd = new TextField();
        tfDd.setEmptyText(null);
        tfDd.setFieldLabel("DD");
        fields.add(tfDd);
        fieldSetNorth.add(tfDd, formData);
        tfDd.setVisible(Boolean.FALSE);

        tfLl = new TextField();
        tfLl.setEmptyText(null);
        tfLl.setFieldLabel("LL");
        fields.add(tfLl);
        fieldSetNorth.add(tfLl, formData);
        tfLl.setVisible(Boolean.FALSE);

        btnGetUserTo = new Button("", AbstractImagePrototype.create(images.book()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                pageUser.addDataGridListener(listenerGetDomain);
                pageUser.show();
            }
        });

        btnGetUserTo.setToolTip(constants.btnOpenUser());
        btnGetUserTo.setSize(22, 22);
        btnGetUserTo.setItemId("check");

        //Организуем вставку кнопки около поля выбора пользователя
        LayoutContainer fieldSetRow = new LayoutContainer();
        fieldSetRow.setBorders(Boolean.FALSE);
        fieldSetRow.setLayout(new HBoxLayout());
        fieldSetRow.setStyleAttribute("padding", "0px");

        FormData data = new FormData("100%");
        fieldSetNorth.add(fieldSetRow, formData);

        LayoutContainer fieldSetBox = new LayoutContainer();
        fieldSetBox.setBorders(false);
        fieldSetBox.setLayout(new FormLayout());
        fieldSetBox.add(tfUserNameTo, data);

        HBoxLayoutData dataBox = new HBoxLayoutData();
        dataBox.setMargins(new Margins(0, 0, 0, 0));
        dataBox.setFlex(1);
        fieldSetRow.add(fieldSetBox, dataBox);

        HBoxLayoutData dataBtn = new HBoxLayoutData();
        dataBtn.setMargins(new Margins(0, 0, 0, 21));
        dataBtn.setFlex(0);

        fieldSetRow.add(btnGetUserTo, dataBtn);
        //---------------------------------------

        chbPriority = new CheckBox();
        chbPriority.setFieldLabel(constants.columnPriority());
        chbPriority.setBoxLabel(constants.columnPriorityName());
        fields.add(chbPriority);
        fieldSetNorth.add(chbPriority, formData);

        tfCreateDate = new TextField();
        tfCreateDate.setEmptyText(null);
        tfCreateDate.setFieldLabel(constants.columnCreateDate());
        fields.add(tfCreateDate);
        fieldSetNorth.add(tfCreateDate, formData);
        tfCreateDate.setVisible(Boolean.FALSE);
        tfCreateDate.setReadOnly(true);

        cbTaskState = new DGComboBox<Integer>("TASK_STATE", "MAIN:TASK_STATE", "MAIN:NAME");
        cbTaskState.setIsValidate(false);
        cbTaskState.setAllowBlank(true); //позволять пусто - нет(false) 
        FormHelper.setNotEmptyFieldLabel(constants.columnTaskState(), cbTaskState);
        cbTaskState.setValidateOnBlur(Boolean.TRUE);
        fields.add(cbTaskState);
        fieldSetNorth.add(cbTaskState, formData);
        cbTaskState.setVisible(Boolean.FALSE);

        tfEventTime = new TextField();
        tfEventTime.setEmptyText(null);
        tfEventTime.setFieldLabel(constants.columnEventTime());
        fields.add(tfEventTime);
        fieldSetNorth.add(tfEventTime, formData);
        tfEventTime.setVisible(Boolean.FALSE);
        tfEventTime.setReadOnly(true);

        chbCurrentId = new CheckBox();
        chbCurrentId.setFieldLabel(constants.columnCurrentId());
        fields.add(chbCurrentId);
        chbCurrentId.setBoxLabel(" ");
        fieldSetNorth.add(chbCurrentId, formData);

        taDescription = new TextArea();
        taDescription.setFieldLabel(constants.columnDescription());
        taDescription.setMaxLength(1000);
        taDescription.setInputStyleAttribute("padding", "4px"); 
        
        fields.add(taDescription);

        taReply = new TextArea();
        taReply.setEmptyText(null);
        taReply.setFieldLabel(constants.columnReply());
        taReply.setMaxLength(1000);
        taReply.setInputStyleAttribute("padding", "4px"); 
        fields.add(taReply);
        taReply.setVisible(Boolean.FALSE);

        FieldSet fieldSetD = new FieldSet();
        fieldSetD.setBorders(Boolean.FALSE);
        fieldSetD.setLayout(new FillLayout());
        fieldSetD.setHeading(constants.columnDescription());
        fieldSetD.setStyleAttribute("padding", "3px 0px 0px 0px");
        fieldSetD.add(taDescription,formData);

        fieldSetR = new FieldSet();
        fieldSetR.setBorders(Boolean.FALSE);
        fieldSetR.setLayout(new FillLayout());
        fieldSetR.setHeading(constants.columnReply());

        fieldSetR.setStyleAttribute("padding", "3px 0px 0px 0px");
        fieldSetR.add(taReply,formData);
        fieldSetR.setVisible(false);

        LayoutContainer layoutContainerText = new LayoutContainer(new BorderLayout());
        layoutContainerText.setBorders(false);

        BorderLayoutData layoutDataD = new BorderLayoutData(LayoutRegion.CENTER);
        layoutDataD.setMargins(new Margins(0, 0, 0, 0));
        layoutDataD.setCollapsible(false); //возможность закрыть панель до заголовка
        layoutDataD.setSize(200);

        BorderLayoutData layoutDataR = new BorderLayoutData(LayoutRegion.SOUTH, 0.5f);
        layoutDataR.setMargins(new Margins(6, 0, 0, 0));
        layoutDataR.setCollapsible(false); //возможность закрыть панель до заголовка
        layoutDataR.setSplit(true);

        layoutContainerText.add(fieldSetD, layoutDataD);
        layoutContainerText.add(fieldSetR, layoutDataR);

        BorderLayoutData layoutDataText = new BorderLayoutData(LayoutRegion.CENTER);
        layoutDataText.setMargins(new Margins(0, 0, 0, 0));
        mainBorder.add(layoutContainerText, layoutDataText);

        setIsReadOnly(state == EditState.VIEW);

        enableComponent();
        initPageUser();

    }

    public void setFocusReply() {
        taReply.setCursorPos(0);
        taReply.focus();
    }

    public void setFocusDescription() {
        taDescription.setCursorPos(0);
        taDescription.focus();
    }
}
