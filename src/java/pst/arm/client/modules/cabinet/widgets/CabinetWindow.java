package pst.arm.client.modules.cabinet.widgets;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppCache;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.modules.cabinet.event.CabinetEvents;
import pst.arm.client.modules.cabinet.event.UserEvent;
import pst.arm.client.modules.cabinet.lang.CabinetConstants;
import pst.arm.client.modules.cabinet.lang.CabinetMessages;

/**
 * Base window of module "cabinet"
 * @author Ratmir Slepenkov
 * @since 0.15.2
 */
public class CabinetWindow extends Dialog {

    private static final GWTUserServiceAsync USER_SERVICE = GWT.create(GWTUserService.class);
    private static final CabinetConstants CABINET_CONSTANTS = GWT.create(CabinetConstants.class);
    private static final CommonConstants COMMON_CONSTANTS = GWT.create(CommonConstants.class);
    private static final CabinetMessages CABINET_MESSAGES = GWT.create(CabinetMessages.class);
    
    private SelectionListener<ButtonEvent> btnListener;
    private Listener<BaseEvent> tfChangeListener;
    private Listener<WindowEvent> windowListener;
    private Listener<UserEvent> saveUserListener;

    private Button btnSave, btnClose;
    private FormPanel mainPanel;
    //main info
    private TextField<String> tfLogin;
    private TextField<String> tfPassword;
    private TextField<String> tfPasswordConfirm;
    //personal info
    private TextField<String> tfUserName;
    private TextField<String> taDescription;

    public CabinetWindow() {
        setHeading(CABINET_CONSTANTS.header());
        setWidth(450);
        setAutoHeight(true);
        setLayout(new FitLayout());
        getButtonBar().removeAll();

        initListeners();
        initComponents();
        initData();
        addListener(Events.BeforeHide, windowListener);
    }

    private void initListeners() {
        btnListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getSource() == btnSave) {
                    saveUser();
                } else {
                    CabinetWindow.this.hide();
                }
            }
        };

        tfChangeListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                TextField<String> source = (TextField<String>) be.getSource();
                source.setValue(source.getValue().trim());
            }
        };

        windowListener = new Listener<WindowEvent>() {

            @Override
            public void handleEvent(WindowEvent be) {
                if(hasChanges() || isPasswordChanged()){
                    be.setCancelled(true);
                    
                    addSaveUserListener(new Listener<UserEvent>() {

                        @Override
                        public void handleEvent(UserEvent be) {
                            removeSaveUserListener(this);
                            hide();
                        }
                    });
                    
                    MessageBox box = new MessageBox();
                    box.setButtons(MessageBox.YESNOCANCEL);
                    box.setIcon(MessageBox.QUESTION);
                    box.setTitle(COMMON_CONSTANTS.save());
                    box.setMessage(COMMON_CONSTANTS.saveChanges());
                    box.addCallback(new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(final MessageBoxEvent be) {

                            if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                saveUser();
                            } else if (be.getButtonClicked().getItemId().equals(Dialog.NO)) {
                                initData();
                                hide();
                            }
                        }
                    });
                    box.show();
                }
            }
        };
    }

    private void saveUser() {
        if ((hasChanges() || isPasswordChanged()) && mainPanel.isValid()) {
            AsyncCallback<Long> callback = new AsyncCallback<Long>() {

                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert(COMMON_CONSTANTS.error(), CABINET_MESSAGES.errCantSaveUser(), null);
                    CabinetWindow.this.unmask();
                }

                @Override
                public void onSuccess(Long result) {
                    initData();
                    fireSaveUserEvent(AppCache.getInstance().getUser());
                    CabinetWindow.this.unmask();
                }
            };


            User user = AppCache.getInstance().getUser();

            CabinetWindow.this.mask();
            user.setUserName(tfUserName.getValue());
            user.setDescription(taDescription.getValue());
            user.setSecurityData(tfPassword.getValue());

            USER_SERVICE.saveUserWithPassword(user, callback);
        }
    }

    private void initComponents() {
        mainPanel = new FormPanel();
        mainPanel.setBodyBorder(false);
        mainPanel.setBorders(false);
        mainPanel.setHeaderVisible(false);
        mainPanel.setAutoHeight(true);

        tfLogin = new TextField<String>();
        tfLogin.setInputStyleAttribute("text-transform", "lowercase");
        tfLogin.setFieldLabel(CABINET_CONSTANTS.tfLogin());
        tfLogin.setAllowBlank(false);
        tfLogin.addListener(Events.Change, tfChangeListener);
        tfLogin.setEnabled(false);

        tfPassword = new TextField<String>();
        tfPassword.setFieldLabel(CABINET_CONSTANTS.tfPassword());
        tfPassword.setPassword(true);
        tfPassword.addListener(Events.Change, tfChangeListener);

        tfPasswordConfirm = new TextField<String>();
        tfPasswordConfirm.setFieldLabel(CABINET_CONSTANTS.tfPasswordConfirm());
        tfPasswordConfirm.setPassword(true);
        tfPasswordConfirm.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (tfPassword != null && !tfPassword.getValue().equals(tfPasswordConfirm.getValue())) {
                    return CABINET_MESSAGES.errPasswordsAreNotEquals();
                }
                return null;
            }
        });
        tfPasswordConfirm.addListener(Events.Change, tfChangeListener);

        tfUserName = new TextField<String>();
        tfUserName.setFieldLabel(CABINET_CONSTANTS.tfUserName());
        tfUserName.addListener(Events.Change, tfChangeListener);

        taDescription = new TextArea();
        taDescription.setFieldLabel(CABINET_CONSTANTS.taDescription());
        taDescription.addListener(Events.Change, tfChangeListener);

        btnSave = new Button(COMMON_CONSTANTS.save());
        btnSave.addSelectionListener(btnListener);

        btnClose = new Button(CABINET_CONSTANTS.btnClose());
        btnClose.addSelectionListener(btnListener);
    }

    /**
     * Set default data:
     * <ul>
     * <li>User fields fills from current user.</li>
     * <li>Password and confirm password are clear.</li>
     * </ul>
     */
    private void initData() {
        User user = AppCache.getInstance().getUser();

        tfLogin.setValue(user.getUserLogin());
        tfPassword.clear();
        tfPasswordConfirm.clear();
        tfUserName.setValue(user.getUserName());
        taDescription.setValue(user.getDescription());
    }

    @Override
    protected void onRender(Element parent, int pos) {
        addButton(btnSave);
        addButton(btnClose);
        super.onRender(parent, pos);
        mainPanel.setLayout(new RowLayout(Orientation.VERTICAL));

        //main info
        FormData formData = new FormData("100%");
        formData.setMargins(new Margins(0, 16, 0, 0));

        FieldSet fsMainInfo = new FieldSet();
        fsMainInfo.setHeading(CABINET_CONSTANTS.fsMainInfo());
        fsMainInfo.setLayout(new FormLayout());

        fsMainInfo.add(tfLogin, formData);
        fsMainInfo.add(tfPassword, formData);
        fsMainInfo.add(tfPasswordConfirm, formData);

        mainPanel.add(fsMainInfo, new RowData(1, -1));

        //personal info
        formData = new FormData("100%");
        formData.setMargins(new Margins(0, 16, 0, 0));

        FieldSet fsPersonalInfo = new FieldSet();
        fsPersonalInfo.setLayout(new FormLayout());
        fsPersonalInfo.setHeading(CABINET_CONSTANTS.fsPersonalInfo());

        fsPersonalInfo.add(tfUserName, formData);
        fsPersonalInfo.add(taDescription,new FormData());//, new FormData("-15 25%"));

        mainPanel.add(fsPersonalInfo, new RowData(1, -1));
        add(mainPanel);
    }

    private boolean hasChanges() {
        User user = AppCache.getInstance().getUser();
        if (!isStringsEquals(user.getUserLogin(), tfLogin.getValue())) {
            return true;
        }

        if (!isStringsEquals(user.getUserName(), tfUserName.getValue())) {
            return true;
        }

        if (!isStringsEquals(user.getDescription(), taDescription.getValue())) {
            return true;
        }

        return false;
    }

    private boolean isPasswordChanged() {
        return tfPassword.getValue() != null && tfPassword.getValue().length() > 0;
    }

    private boolean isStringsEquals(String s1, String s2) {
        return AppHelper.getInstance().getStringNotNull(s1).equals(AppHelper.getInstance().getStringNotNull(s2));
        //return s1 == null && s2 == null
        //        || s1 != null && s1.equals(s2);
    }
    
    public void addSaveUserListener(Listener<UserEvent> listener){
        addListener(CabinetEvents.UserSaved, listener);                
    }
    
    public void removeSaveUserListener(Listener<UserEvent> listener){
        removeListener(CabinetEvents.UserSaved, listener);
    }
    
    public void fireSaveUserEvent(User user){
        UserEvent event = new UserEvent(CabinetEvents.UserSaved);
        event.setUser(user);
        fireEvent(CabinetEvents.UserSaved, event);
    }
}
