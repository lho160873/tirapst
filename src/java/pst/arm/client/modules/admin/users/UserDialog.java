package pst.arm.client.modules.admin.users;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.domain.Company;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.admin.lang.AdminConstants;
import pst.arm.client.modules.admin.lang.AdminMessages;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class UserDialog extends Window {

    protected FormPanel fp;
    protected Button saveUserButton;
    protected Button cancelButton;
    protected GWTUserServiceAsync service;
    protected ArmImages images;
    protected AdminConstants constants;
    protected AdminMessages messages;
    protected ListLoader<ListLoadResult<ModelData>> loader;
    protected ListLoader<ListLoadResult<ModelData>> loaderCompanies;
    protected TextField<String> login;
    protected TextField<String> password1;
    protected TextField<String> password2;
    protected CheckBox disableCheck;
    protected NumberField archiveId;
    protected NumberField userId;
    protected DateField date;
    protected TextField<String> userName;
    protected TextField<String> workerName;
    protected NumberField workerId;
    protected TextArea description;
    protected Grid<BeanModel> rolesGrid;
    protected Grid<BeanModel> companiesGrid;
    protected UsersResultPanel userPanel;
    protected User user;
    protected boolean editMode = false;
    protected boolean dataChanged = false;
    private FormPanel panel;

    public UserDialog(UsersResultPanel panel) {
        service = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
        constants = (AdminConstants) GWT.create(AdminConstants.class);
        messages = (AdminMessages) GWT.create(AdminMessages.class);
        images = (ArmImages) GWT.create(ArmImages.class);

        this.userPanel = panel;

        setHeight(580);
        setWidth(680);
        setModal(true);
        setMaximizable(true);
        setLayout(new FitLayout());
        setIcon(images.user_add());
        setHeading(constants.newUser());

        fp = initForm();

        add(fp);
        saveUserButton = new Button(constants.createUser(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                addUser();
            }
        });
        saveUserButton.setWidth(150);

        cancelButton = new Button(constants.cancel(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                cancel();
            }
        });

        setButtonAlign(HorizontalAlignment.RIGHT);
        addButton(saveUserButton);
        addButton(cancelButton);
    }

    public UserDialog(UsersResultPanel panel, User existedUser) {
        this(panel);
        user = existedUser;
        editMode = true;
        saveUserButton.setText(constants.saveChanges());
        setHeading(messages.userDlgEditHeader(user.getUserLogin()));
        setIcon(images.user_edit());

        initFormData();
    }

    private FormPanel initForm() {
        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(new RowLayout(Orientation.HORIZONTAL));

        FormData formData = new FormData("-15"); //#NLS

        login = new TextField<String>();
        login.setInputStyleAttribute("text-transform", "lowercase");
        //login.setFieldLabel(constants.loginField());
        FormHelper.setNotEmptyFieldLabel(constants.loginField(),login);
        login.setAllowBlank(false);
        login.setMaxLength(100);

        password1 = new TextField<String>();
        //password1.setFieldLabel(constants.password());
        FormHelper.setNotEmptyFieldLabel(constants.password(),password1);
        password1.setPassword(true);
        password1.setAllowBlank(false);
        password1.setMaxLength(100);

        password2 = new TextField<String>();
        //password2.setFieldLabel(constants.passwordConfirm());
        FormHelper.setNotEmptyFieldLabel(constants.passwordConfirm(),password2);
        password2.setPassword(true);
        password2.setAllowBlank(false);
        password2.setMaxLength(100);

        disableCheck = new CheckBox();
        disableCheck.setBoxLabel(constants.disableField());
        CheckBoxGroup checkGroup = new CheckBoxGroup();
        checkGroup.setFieldLabel(constants.additionalField());
        checkGroup.add(disableCheck);

        archiveId = new NumberField();
        archiveId.setFieldLabel(constants.archiveIdField());
        archiveId.setEnabled(false);
        archiveId.setValue(Integer.parseInt(ConfigurationManager.getGlobalProperty("currentArchiveId")));
        archiveId.setVisible(true);

        userId = new NumberField();
        userId.setFieldLabel(constants.userIdField());
        userId.setEnabled(false);

        date = new DateField();
        date.setFieldLabel(constants.dateField());
        date.setEnabled(false);
        date.setPropertyEditor(new DateTimePropertyEditor(constants.timeformat()));

        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(150);
        FieldSet basicDataFieldSet = new FieldSet();
        basicDataFieldSet.setHeading(constants.commonDataFieldSet());
        basicDataFieldSet.setLayout(formLayout);
        basicDataFieldSet.add(login, formData);
        basicDataFieldSet.add(password1, formData);
        basicDataFieldSet.add(password2, formData);
        basicDataFieldSet.add(checkGroup, formData);
        basicDataFieldSet.add(archiveId, formData);
        basicDataFieldSet.add(userId, formData);
        basicDataFieldSet.add(date, formData);

        userName = new TextField<String>();
        //userName.setFieldLabel(constants.userNameField());
        FormHelper.setNotEmptyFieldLabel(constants.userNameField(),userName);
        userName.setAllowBlank(false);

        description = new TextArea();
        description.setFieldLabel(constants.descriptionField());

        FormLayout privateFormLayout = new FormLayout();
        privateFormLayout.setLabelWidth(110);
        FieldSet privateDataFieldSet = new FieldSet();
        privateDataFieldSet.setHeading(constants.privateDataFieldSet());
        privateDataFieldSet.setLayout(privateFormLayout);
        privateDataFieldSet.add(userName, formData);
        privateDataFieldSet.add(description, new FormData("-15 15%")); // #NLS

        //Сотрудник ГПК -----------------
        workerId = new NumberField();
        workerId.setFieldLabel("workerId");
        workerId.setVisible(false);

        workerName = new TextField<String>();
        workerName.setEnabled(false);
        workerName.setFieldLabel(constants.userWorkerField());

        LayoutContainer fieldSetBox = new LayoutContainer();
        fieldSetBox.setBorders(false);
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(110);
        fieldSetBox.setLayout(layout);
        FormData data = new FormData("100%");
        fieldSetBox.add(workerName, data);

        FieldSet fieldSetRow = new FieldSet();
        fieldSetRow.setBorders(Boolean.FALSE);
        fieldSetRow.setLayout(new HBoxLayout());
        fieldSetRow.setStyleAttribute("padding", "0px");
        HBoxLayoutData dataBox = new HBoxLayoutData();
        dataBox.setMargins(new Margins(0, 0, 0, 0));
        dataBox.setFlex(1);
        fieldSetRow.add(fieldSetBox, dataBox);

        fieldSetRow.add(new Button("", images.book(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                final Window w = new Window();
                w.setLayout(new FitLayout());
                DataGridPanel dataGridPanel = new DataGridPanel("worker");
                dataGridPanel.setIsBtnSendEnabled(true);
                dataGridPanel.addDataGridListener(new Listener<DataGridEvent>() {
                    @Override
                    public void handleEvent(DataGridEvent event) {
                        w.hide();
                        DDataGrid domain = event.getDomain();
                        HashMap<SKeyForColumn, IRowColumnVal> rows = event.getDomain().getRows();
                        workerName.setRawValue(rows.get(new SKeyForColumn("MAIN", "NAME")).getVal().toString());
                        workerId.setValue((Integer) rows.get(new SKeyForColumn("MAIN", "WORKER_ID")).getVal());
                    }
                });
                w.setHeading(constants.userWorkerField());
                w.setModal(true);
                w.setSize(900, 550);
                w.add(dataGridPanel);
                w.show();
                w.center();
            }
        }));
        fieldSetRow.add(new Button("", images.cancel(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                workerName.clear();
                workerId.clear();
            }
        }));

        privateDataFieldSet.add(workerId, formData);
        privateDataFieldSet.add(fieldSetRow, formData);//new FormData("80%"));

        //--------------------------
        rolesGrid = initRolesGrid();
        FieldSet userRolesFieldSet = new FieldSet();
        userRolesFieldSet.setHeading(constants.rolseFieldSet());
        userRolesFieldSet.setLayout(new FitLayout());
        userRolesFieldSet.add(rolesGrid, new FitData(new Margins(5)));

        //--------------------------
        companiesGrid = initCompaniesGrid();
        FieldSet userCompaniesFieldSet = new FieldSet();
        userCompaniesFieldSet.setHeading(constants.companiesFieldSet());
        userCompaniesFieldSet.setLayout(new FitLayout());
        userCompaniesFieldSet.setScrollMode(Style.Scroll.AUTOY);
        userCompaniesFieldSet.add(companiesGrid, new FitData(new Margins(5)));

        LayoutContainer lc = new LayoutContainer(new RowLayout(Orientation.VERTICAL));
        lc.add(basicDataFieldSet, new RowData(1.0, 215, new Margins(0, 10, 0, 0)));
        lc.add(privateDataFieldSet, new RowData(1.0, 1.0, new Margins(10, 10, 0, 0)));

        LayoutContainer lc2 = new LayoutContainer(new RowLayout(Orientation.VERTICAL));
        lc2.add(userRolesFieldSet, new RowData(1.0, 315, new Margins(0, 10, 0, 0)));
        lc2.add(userCompaniesFieldSet, new RowData(1.0, 1.0, new Margins(10, 10, 0, 0)));

        panel.add(lc, new RowData(0.5, 1.0));
        panel.add(lc2, new RowData(0.5, 1.0));

        return panel;
    }

    private void initFormData() {
        login.setValue(user.getUserLogin());
        password1.setValue("******"); // #NLS
        password2.setValue("******"); // #NLS
        disableCheck.setValue(user.isAccountDisabled());
        archiveId.setValue(user.getArchiveId());
        userId.setValue(user.getId());
        date.setValue(user.getDateCreated());
        userName.setValue(user.getUserName());
        workerName.setValue(user.getWorkerName());
        workerId.setValue(user.getWorkerId());
        description.setValue(user.getDescription());
        //login.setEnabled(false);
        password1.setEnabled(false);
        password2.setEnabled(false);
    }

    private void initUserRoles() {
        if (editMode == false) {
            return;
        }

        List<BeanModel> allRoles = rolesGrid.getStore().getModels();
        List<Role> userRoles = user.getRoles();

        if ((allRoles != null) && (userRoles != null)) {
            for (BeanModel beanModel : allRoles) {
                Role gridRole = (Role) beanModel.getBean();
                for (Role userRole : userRoles) {
                    Long userRoleId = userRole.getRoleId();
                    Long gridRoleId = gridRole.getRoleId();
                    if (userRoleId.equals(gridRoleId)) {
                        rolesGrid.getSelectionModel().select(beanModel, true);
                    }
                }
            }
        }
    }

    private void initUserCompanies() {
        if (editMode == false) {
            return;
        }

        List<BeanModel> allCompanies = companiesGrid.getStore().getModels();
        List<Company> userCompanies = user.getCompanies();

        if ((allCompanies != null) && (userCompanies != null)) {
            for (BeanModel beanModel : allCompanies) {
                Company gridCompany = (Company) beanModel.getBean();
                for (Company userCompany : userCompanies) {
                    Integer userCompanyId = userCompany.getCompanyId();
                    Integer gridCompanyId = gridCompany.getCompanyId();
                    if (userCompanyId.equals(gridCompanyId)) {
                        companiesGrid.getSelectionModel().select(beanModel, true);
                    }
                }
            }
        }
    }

    private Grid<BeanModel> initRolesGrid() {
        RpcProxy<List<Role>> proxy = new RpcProxy<List<Role>>() {
            @Override
            public void load(Object loadConfig, AsyncCallback<List<Role>> callback) {
                service.getRoles(callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
        store.addStoreListener(new StoreListener<BeanModel>() {
            @Override
            public void handleEvent(StoreEvent<BeanModel> e) {
                initUserRoles();
            }
        });

        CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(sm.getColumn());
        columns.add(new ColumnConfig("roleName", constants.roleColumn(), 100)); // #NLS
        columns.add(new ColumnConfig("description", constants.roleDescriptionColumn(), 300)); // #NLS
        ColumnModel cm = new ColumnModel(columns);
        rolesGrid = new Grid<BeanModel>(store, cm);
        rolesGrid.setAutoExpandColumn("description"); // #NLS
        rolesGrid.getView().setForceFit(true);
        rolesGrid.setBorders(true);
        rolesGrid.setLoadMask(true);
        rolesGrid.setStripeRows(true);
        rolesGrid.addPlugin(sm);
        rolesGrid.setSelectionModel(sm);
        rolesGrid.addStyleName("multilineColumn"); // #NLS

        return rolesGrid;
    }

    private Grid<BeanModel> initCompaniesGrid() {
        RpcProxy<List<Company>> proxy = new RpcProxy<List<Company>>() {
            @Override
            public void load(Object loadConfig, AsyncCallback<List<Company>> callback) {
                service.getCompanies(callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        loaderCompanies = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        ListStore<BeanModel> storeCompanies = new ListStore<BeanModel>(loaderCompanies);
        storeCompanies.addStoreListener(new StoreListener<BeanModel>() {
            @Override
            public void handleEvent(StoreEvent<BeanModel> e) {
                initUserCompanies();
            }
        });

        CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(sm.getColumn());

        //columns.add(new ColumnConfig("name", constants.companyColumn(), 300)); // #NLS
        columns.add(new ColumnConfig("shortName", constants.companyColumn(), 300)); // #NLS
        ColumnModel cm = new ColumnModel(columns);
        companiesGrid = new Grid<BeanModel>(storeCompanies, cm);
        companiesGrid.getView().setForceFit(true);

        companiesGrid.setBorders(true);
        companiesGrid.setLoadMask(true);
        companiesGrid.setStripeRows(true);
        companiesGrid.addPlugin(sm);
        companiesGrid.setSelectionModel(sm);
        companiesGrid.addStyleName("multilineColumn"); // #NLS

        return companiesGrid;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        if (editMode) {
            setFocusWidget(login);
        }

        loader.load();
        loaderCompanies.load();
        rolesGrid.mask(constants.rolesLoadMessage());
        companiesGrid.mask(constants.rolesLoadMessage());
    }

    private void addUser() {
        List<String> errors = validateData();
        String errorsText = "";
        if ((errors == null) || (errors.isEmpty())) {
            save();
        } else {
            for (String error : errors) {
                errorsText += error + "<br>"; // #NLS
            }

            MessageBox messageBox = new MessageBox();
            messageBox.setMinWidth(500);
            messageBox.setTitle(constants.incorrectData());
            messageBox.setMessage(errorsText);
            messageBox.setIcon(MessageBox.ERROR);
            messageBox.setType(MessageBox.MessageBoxType.ALERT);
            messageBox.show();
        }
    }

    private void cancel() {
        hide();
    }

    public void save() {
        panel.mask();
        user = getUser();
        service.saveUser(user, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                panel.unmask();
                //String errorMessage = messages.createUserErrorMessage(user.getUserLogin());
                //errorMessage += caught.getMessage();
                //MessageBox.info(constants.createUser(), errorMessage, null);
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Long result) {
                panel.unmask();
                userPanel.setUser(user);
                userPanel.loadData();

                Listener<MessageBoxEvent> userCreatedListner = new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent ce) {
                        if (ce.getButtonClicked().getItemId().equals("ok") == true) { // #NLS
                            if (!editMode) {
                                clearFields();
                            }

                        }
                    }
                };
                String message = "";
                String title = "";
                if (editMode) {
                    title = messages.userDlgEditHeader(user.getUserLogin());
                    message = messages.userDataChangedMessage(user.getUserLogin());
                } else {
                    title = constants.newUser();
                    message = messages.userCreatedMessage(user.getUserLogin());
                }

                //MessageBox.info(title, message, userCreatedListner);
                hide();
            }
        });
    }


    /*
     * return if some erroros return errorMessage, otherwise return null
     */
    private List<String> validateData() {
        List<String> errors = new ArrayList<String>();
        String loginValue = login.getRawValue();
        if ((loginValue == null) || (loginValue.trim().isEmpty())) {
            errors.add(constants.emptyLoginWarn());
            login.markInvalid(constants.emptyLoginWarn());
            setFocusWidget(login);
        } else {
            loginValue = loginValue.toLowerCase();
            login.setValue(loginValue);
            if (editMode) {
                if (!loginValue.equalsIgnoreCase(user.getUserLogin())) {
                    if (userPanel.isUserExist(loginValue)) {
                        errors.add(constants.existLoginWarn());
                        setFocusWidget(login);
                    }
                }
            } else {
                if (userPanel.isUserExist(loginValue)) {
                    errors.add(constants.existLoginWarn());
                    setFocusWidget(login);
                }
            }
        }

        String pass1 = password1.getRawValue();
        if ((pass1 == null) || (pass1.trim().isEmpty())) {
            errors.add(constants.emptyPassWarn());
            password1.validate();
            setFocusWidget(password1);
        }

        String pass2 = password2.getRawValue();
        if ((pass2 == null) || (pass2.trim().isEmpty())) {
            errors.add(constants.emptyPassConfirmWarn());
            password2.validate();
        }

         String name = userName.getRawValue();
        if ((name == null) || (name.trim().isEmpty())) {
            errors.add(constants.emptyUserName());
            userName.validate();
            setFocusWidget(userName);
        }
        
        if ((pass1 != null) && (pass2 != null)) {
            if (!pass1.equals(pass2)) {
                errors.add(constants.passNotEqualWarn());
            }
        }

        if ((getSelectedRoles() == null) || (getSelectedRoles().isEmpty())) {
            errors.add(constants.noRolesSelectedWarn());
        }

        //if (!login.isValid() || !password1.isValid() || !password2.isValid()) {
        //    errors.add(constants.incorrectData());
        //}

        return errors;
    }

    private void clearFields() {
        login.setRawValue(null);
        password1.setRawValue(null);
        password2.setRawValue(null);
        date.setRawValue(null);
        disableCheck.setValue(null);
        userName.setRawValue(null);
        description.setRawValue(null);
        workerId.setRawValue(null);
        workerName.setRawValue(null);

        rolesGrid.getSelectionModel().deselectAll();
        companiesGrid.getSelectionModel().deselectAll();

        setFocusWidget(login);
    }

    private User getUser() {
        User ret = new User();
        ret.setUserLogin(login.getRawValue());
        ret.setSecurityData(password1.getRawValue());
        ret.setEnabled(!disableCheck.getValue().booleanValue());
        ret.setArchiveId(archiveId.getValue().intValue()); // must have
        ret.setId(userId.getValue() == null ? null : userId.getValue().longValue());
        ret.setUserName(userName.getRawValue());
        ret.setDescription(description.getRawValue());
        ret.setRoles(getSelectedRoles());
        ret.setCompanies(getSelectedCompanies());
        ret.setWorkerId(workerId.getValue() == null ? null : workerId.getValue().intValue());
        ret.setWorkerName(workerName.getRawValue());

        return ret;
    }

    private List<Role> getSelectedRoles() {
        List<Role> roles = new ArrayList<Role>();
        List<BeanModel> beanModels = rolesGrid.getSelectionModel().getSelectedItems();
        if (beanModels != null) {
            for (BeanModel model : beanModels) {
                Role role = (Role) model.getBean();
                roles.add(role);
            }
        }

        return roles;
    }

    private List<Company> getSelectedCompanies() {
        List<Company> companies = new ArrayList<Company>();
        List<BeanModel> beanModels = companiesGrid.getSelectionModel().getSelectedItems();
        if (beanModels != null) {
            for (BeanModel model : beanModels) {
                Company company = (Company) model.getBean();
                companies.add(company);
            }
        }

        return companies;
    }
}
