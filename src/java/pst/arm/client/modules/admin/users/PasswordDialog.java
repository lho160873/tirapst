package pst.arm.client.modules.admin.users;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.modules.admin.lang.AdminConstants;
import pst.arm.client.modules.admin.lang.AdminMessages;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author vorontsov
 */
public class PasswordDialog extends Window {

    protected GWTUserServiceAsync service;
    protected ArmImages images;
    protected User user;
    protected TextField<String> password1;
    protected TextField<String> password2;
    protected AdminConstants constants;
    protected AdminMessages messages;

    public PasswordDialog(User user) {
        constants = (AdminConstants) GWT.create(AdminConstants.class);
        messages = (AdminMessages) GWT.create(AdminMessages.class);
        images = (ArmImages) GWT.create(ArmImages.class);
        service = (GWTUserServiceAsync) GWT.create(GWTUserService.class);

        this.user = user;

        setHeight(150);
        setWidth(400);
        setModal(true);
        setLayout(new FitLayout());
        setIcon(images.user_go());
        setHeading(messages.passDlgHeader(user.getUserLogin()));

        Button okButton = new Button(constants.ok(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                setPassword();
            }
        });


        Button cancelButton = new Button(constants.cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        FormData formData = new FormData("-30"); // #NLS
        password1 = new TextField<String>();
        password1.setFieldLabel(constants.password());
        password1.setPassword(true);
        password1.setAllowBlank(false);

        password2 = new TextField<String>();
        password2.setFieldLabel(constants.passwordConfirm());
        password2.setPassword(true);
        password2.setAllowBlank(false);
        
        FormLayout formLayout = new FormLayout(FormPanel.LabelAlign.LEFT);
        formLayout.setLabelWidth(110);
        LayoutContainer lc = new LayoutContainer(formLayout);
        lc.add(password1, formData);
        lc.add(password2, formData);


        setButtonAlign(HorizontalAlignment.CENTER);

        add(lc, new FitData(10, 10, 0, 10));
        addButton(okButton);
        addButton(cancelButton);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        setFocusWidget(password1);
    }


    protected void setPassword() {
        List<String> errors = validateData();
        String errorsText = "";
        if ((errors == null) || (errors.isEmpty())) {
            user.setSecurityData(password1.getRawValue());
            service.setPassword(user, new AsyncCallback<Long>() {

                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert(messages.passDlgHeader(user.getUserLogin()), constants.passwordNotChanged(), null);
                    hide();

                }

                @Override
                public void onSuccess(Long result) {
                    MessageBox.info(messages.passDlgHeader(user.getUserLogin()), constants.passwordChanged(), null);
                    hide();
                }
            });
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

    protected List<String> validateData() {
        password1.validate();
        password2.validate();

        List<String> errors = new ArrayList<String>();
        String pass1 = password1.getRawValue();
        if ((pass1 == null) || (pass1.trim().isEmpty())) {
            errors.add(constants.emptyPassWarn());
            setFocusWidget(password1);
        }

        String pass2 = password2.getRawValue();
        if ((pass2 == null) || (pass2.trim().isEmpty())) {
            errors.add(constants.emptyPassConfirmWarn());
            setFocusWidget(password2);
        }

        if ((pass1 != null) && (pass2 != null)) {
            if (!pass1.equals(pass2)) {
                errors.add(constants.passNotEqualWarn());
                setFocusWidget(password1);
            }
        }

        return errors;
    }
}
