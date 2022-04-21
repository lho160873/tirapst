package pst.arm.client.modules.header;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppCache;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.modules.cabinet.widgets.CabinetWindow;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author vorontsov
 */
public class TopPanelToolbar extends ContentPanel {

    private ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    private Label lblUserLogin;
    private Button btnUserCabinet;
    protected BaseConstants constants;
    protected BaseMessages messages;
    protected Button exitButton, userManualButton;

    public TopPanelToolbar() {
        setBorders(false);
        constants = (BaseConstants) GWT.create(BaseConstants.class);
        messages = (BaseMessages) GWT.create(BaseMessages.class);

        lblUserLogin = new Label(messages.youEnterAs() + " ");
        
        btnUserCabinet = new Button(ConfigurationManager.getGlobalProperty("currentUser"));
        btnUserCabinet.setStyleName("button-link");
        btnUserCabinet.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                new CabinetWindow().show();
            }
        });

        if (AppCache.getInstance().getUser() != null) {
            btnUserCabinet.setText(AppCache.getInstance().getUser().getFio());
        } else {
            AppCache.getInstance().obtainUserInformation(new AsyncCallback<User>() {

                @Override
                public void onFailure(Throwable caught) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void onSuccess(User result) {
                    if (result != null) {
                        String fio = result.getFio();
                        if (fio != null && !fio.isEmpty()) {
                            btnUserCabinet.setText(result.getFio());
                        }
                    }
                }
            });
        }
        
        lblUserLogin.setStyleAttribute("fontSize", "80%");
        //lblUserLogin.setStyleAttribute("color", "#15428b");

        btnUserCabinet.setStyleAttribute("fontSize", "100%");
        //btnUserCabinet.setStyleAttribute("color", "#15428b");
        btnUserCabinet.setStyleAttribute("padding-right", "10");
        btnUserCabinet.setStyleAttribute("margin-top", "-4");
        btnUserCabinet.setHeight(20);

        exitButton = new Button("<div style='margin-top:-3;'>" + constants.headerBtnExit() + "</div>", images.toolbar_exit());
        exitButton.setIconAlign(IconAlign.LEFT);
        exitButton.setHeight(16);
        exitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {

                    @Override
                    public void handleEvent(MessageBoxEvent ce) {
                        if (ce.getButtonClicked().getItemId().equals("yes") == true) {
                            Window.Location.assign( GWT.getHostPageBaseURL() + "../j_acegi_logout" );
                            MainNavigationPanel.clearTreeState();
                        }
                    }
                };
                MessageBox.confirm(constants.confirm(), messages.areYouSureToExit(), l);
            }
        });

        layout();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setLayout(new BorderLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeading("<div class='main-link'><a href=\"" + getHomeUrl() + "\">" + constants.caption() + " - версия " + constants.projectBuildNumber().replace("M","") + " - от " + constants.projectBuildTime() + "</a></div>");
        getHeader().addTool(lblUserLogin);
        getHeader().addTool(btnUserCabinet);
        getHeader().addTool(exitButton);
        
    }

    protected String getHomeUrl() {
        return GWT.getHostPageBaseURL() + "main.htm?" + AppHelper.getInstance().debugUrl();
    }
}



