package pst.arm.client.modules.leveltask.widgets.in;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.form.EditableForm;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.expansion.DataGridEditWindowOfficeDocContrIg;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.lang.LevelTaskConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class LevelTaskInEditableForm extends LayoutContainer implements EditableForm<LevelTask> {

    private Integer taskStateOld;
    private NumberField nfId, nfUserIdTo, nfUserIdFrom;
    private TextField tfUserNameFrom, tfLl, tfDd, tfTaskName, tfTaskStateName;
    private DGComboBox<Integer> cbTaskId, cbTaskState;
    private TextArea taDescription, taReply;
    private CheckBox chbPriority;
    private Button btnCurrentId;
    private TextField tfEventTime, tfCreateDate;
    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    final protected LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
    private LayoutContainer main, mainBorder;
    private List<Field<?>> fields = new ArrayList<Field<?>>();
    private EditState state;
    private Boolean isSend = Boolean.FALSE;
    private Integer currentId = null;
    private String module;
    final private GWTDDataGridServiceAsync serviceDataGrid = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    private BorderLayoutData layoutDataNorth;
    protected EventBus eventBus;

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setIsSend(Boolean s) {
        isSend = s;
    }

    public void setState(EditState state) {
        this.state = state;

    }

    public LevelTaskInEditableForm(EditState state) {
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
        FormHelper.setFieldValue(tfUserNameFrom, domain.getUserNameFrom());

        FormHelper.setFieldValue(tfDd, domain.getDd());
        FormHelper.setFieldValue(tfLl, domain.getLl());
        taDescription.setValue(domain.getDescription());
        if (domain.getPriority() != null && domain.getPriority() == 1) {
            chbPriority.setValue(Boolean.TRUE);
        } else {
            chbPriority.setValue(Boolean.FALSE);
        }

        cbTaskId.setValueId(domain.getTaskId());

        FormHelper.setFieldValue(tfTaskName, domain.getTaskName());
        cbTaskState.setValueId(domain.getTaskState());
        taskStateOld = domain.getTaskState();
        FormHelper.setFieldValue(tfTaskStateName, domain.getStateName());

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
            btnCurrentId.setEnabled(true);
        } else {
            btnCurrentId.setEnabled(false);
        }
        module = domain.getModule();
        enableComponent(domain);
    }

    @Override
    public void fillDomain(LevelTask domain) {

        domain.setId(FormHelper.getIntValue(nfId));
        domain.setUserIdFrom(FormHelper.getIntValue(nfUserIdFrom));
        domain.setUserIdTo(FormHelper.getIntValue(nfUserIdTo));
        domain.setLl(tfLl.getRawValue());
        domain.setDd(tfDd.getRawValue());
        domain.setTaskId(cbTaskId.getValueId());
        domain.setTaskName(cbTaskId.getRawValue());

        if (taskStateOld.equals(ETaskState.CREATE.getTaskState()) || taskStateOld.equals(ETaskState.IN_WORK.getTaskState())) {
            if (isSend) {
                domain.setTaskState(ETaskState.EXECUTED.getTaskState());
            } else {
                domain.setTaskState(ETaskState.IN_WORK.getTaskState());
            }
        } else {
            domain.setTaskState(taskStateOld);
        }

        domain.setDescription(taDescription.getValue());

        if (chbPriority.getValue()) {
            domain.setPriority(1);
        } else {
            domain.setPriority(0);
        }
        domain.setReply(taReply.getRawValue());       
    }

    private void enableComponent(LevelTask domain) {
        if ((domain.getTaskState().equals(ETaskState.CLOSE.getTaskState())) || (domain.getTaskState().equals(ETaskState.CANCEL.getTaskState()))) {
            FormHelper.setReadOnlyProp(taReply, true);
        }
    }

    public LevelTask createLevelTask() {
        LevelTask domain = new LevelTask();
        fillDomain(domain);
        return domain;
    }

    private void initComponents() {
        //Тип и названии компании показываем наверху перед таб-панелью
        FieldSet fieldSetNorth = new FieldSet();
        fieldSetNorth.setBorders(false);
        fieldSetNorth.setLayout(new FormLayout());
        fieldSetNorth.setScrollMode(Style.Scroll.AUTO);

        layoutDataNorth = new BorderLayoutData(LayoutRegion.NORTH, 0.5f);
        layoutDataNorth.setMargins(new Margins(0, 0, 0, 0));
        layoutDataNorth.setSize(200);
        layoutDataNorth.setSplit(true);
        mainBorder.add(fieldSetNorth, layoutDataNorth);
        
        //BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.CENTER);//new BorderLayoutData(LayoutRegion.NORTH, 300);
        //layoutData.setSplit(true);
        //mainBorder.add(fieldSetNorth, layoutData);

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
        cbTaskId.setReadOnly(Boolean.TRUE);
        cbTaskId.setVisible(Boolean.FALSE);

        tfTaskName = new TextField();
        tfTaskName.setEmptyText(null);
        tfTaskName.setFieldLabel(constants.columnTaskName());
        fields.add(tfTaskName);
        fieldSetNorth.add(tfTaskName, formData);
        FormHelper.setReadOnlyProp(tfTaskName, true);

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

        tfUserNameFrom = new TextField();
        tfUserNameFrom.setEmptyText(null);
        tfUserNameFrom.setFieldLabel(constants.columnUserFrom());
        fields.add(tfUserNameFrom);
        fieldSetNorth.add(tfUserNameFrom, formData);
        FormHelper.setReadOnlyProp(tfUserNameFrom, true);

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

        chbPriority = new CheckBox();
        chbPriority.setFieldLabel(constants.columnPriority());
        chbPriority.setBoxLabel(constants.columnPriorityName());
        fields.add(chbPriority);
        fieldSetNorth.add(chbPriority, formData);
        //FormHelper.setReadOnlyProp(chbPriority, true);
        chbPriority.setEnabled(false);

        tfCreateDate = new TextField();
        tfCreateDate.setEmptyText(null);
        tfCreateDate.setFieldLabel(constants.columnCreateDate());
        fields.add(tfCreateDate);
        fieldSetNorth.add(tfCreateDate, formData);
        tfCreateDate.setVisible(Boolean.FALSE);
        FormHelper.setReadOnlyProp(tfCreateDate, true);

        cbTaskState = new DGComboBox<Integer>("TASK_STATE", "MAIN:TASK_STATE", "MAIN:NAME");
        cbTaskState.setIsValidate(false);
        cbTaskState.setAllowBlank(true); //позволять пусто - нет(false) 
        FormHelper.setNotEmptyFieldLabel(constants.columnTaskState(), cbTaskState);
        cbTaskState.setValidateOnBlur(Boolean.TRUE);
        fields.add(cbTaskState);
        fieldSetNorth.add(cbTaskState, formData);
        cbTaskState.setVisible(Boolean.FALSE);
        FormHelper.setReadOnlyProp(cbTaskState, true);

        tfTaskStateName = new TextField();
        tfTaskStateName.setEmptyText(null);
        tfTaskStateName.setFieldLabel(constants.columnTaskState());
        fields.add(tfTaskStateName);
        fieldSetNorth.add(tfTaskStateName, formData);
        FormHelper.setReadOnlyProp(tfTaskStateName, true);

        tfEventTime = new TextField();
        tfEventTime.setEmptyText(null);
        tfEventTime.setFieldLabel(constants.columnEventTime());
        fields.add(tfEventTime);
        fieldSetNorth.add(tfEventTime, formData);
        tfEventTime.setVisible(Boolean.FALSE);
        FormHelper.setReadOnlyProp(tfEventTime, true);

        btnCurrentId = new Button(constants.btnСurrentTask(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onCurrentTask();
            }
        });
        
        FormData formDataBtn = new FormData("100%");
        formDataBtn.setMargins(new Margins(16, 32, 0, 0));        
        fieldSetNorth.add(btnCurrentId, formDataBtn);
        
        
        taDescription = new TextArea();
        taDescription.setFieldLabel(constants.columnDescription());
        taDescription.setMaxLength(1000);
        taDescription.setInputStyleAttribute("padding", "4px"); 
        fields.add(taDescription);
        //fieldSetNorth.add(taDescription, formData);
        FormHelper.setReadOnlyProp(taDescription, true);
        
        taReply = new TextArea();
        taReply.setEmptyText(null);
        taReply.setMaxLength(1000);
        taReply.setInputStyleAttribute("padding", "4px"); 
        taReply.setFieldLabel(constants.columnReply());
        fields.add(taReply);
        //fieldSetNorth.add(taReply, formData);
        taReply.setVisible(Boolean.TRUE);
        
        
        FieldSet fieldSetD = new FieldSet();
        fieldSetD.setBorders(Boolean.FALSE);
        fieldSetD.setLayout(new FillLayout());
        fieldSetD.setHeading(constants.columnDescription());
        fieldSetD.setStyleAttribute("padding", "3px 0px 0px 0px");
        fieldSetD.add(taDescription,formData);

        FieldSet fieldSetR = new FieldSet();
        fieldSetR.setBorders(Boolean.FALSE);
        fieldSetR.setLayout(new FillLayout());
        fieldSetR.setHeading(constants.columnReply());

        fieldSetR.setStyleAttribute("padding", "3px 0px 0px 0px");
        fieldSetR.add(taReply,formData);
        //fieldSetR.setVisible(false);

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

    }

    private void onCurrentTask() {
        if (currentId == null) {
            return;
        }
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                final DTable table = result;
                final DDataGrid domainData = new DDataGrid();
                domainData.setName(table.getQueryName());
                for (IColumnBuilder builder : table.getColumnBuilders()) {
                    for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        IRowColumnVal val = builder.createRowColumnVal(key);
                        if (builder.getColumn(key).getIsKey()) {
                            val.setVal(currentId);
                        }
                        domainData.getRows().put(key, val);
                    }
                }
                serviceDataGrid.getDataGridById(module, domainData, new AsyncCallback<DDataGrid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(DDataGrid t) {
                        DDataGrid domainData = new DDataGrid();
                        domainData.copy(t);
                        DataGridEditWindow window;
                        if (module.equals("OFFICE_DOC_CONTR_IG")) {
                            window = new DataGridEditWindowOfficeDocContrIg(domainData, table, EditState.VIEW, EWindowType.EWT_WINDOW);
                        } else {
                            window = new DataGridEditWindow(domainData, table, EditState.VIEW, EWindowType.EWT_WINDOW);

                        }
                        window.show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        serviceDataGrid.getTable(module, callback_getTable);
    }
     public void setFocusReply()
    {
        taReply.setCursorPos(0);
        taReply.focus();
    }
     public void setFocusDescription()
    {
        taDescription.setCursorPos(0);
        taDescription.focus();
    }
}
