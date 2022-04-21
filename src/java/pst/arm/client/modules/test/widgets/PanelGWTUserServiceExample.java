package pst.arm.client.modules.test.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.List;
import pst.arm.client.common.domain.User;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PanelGWTUserServiceExample extends VerticalPanel {

    private Label lblServerReply = new Label();
    private Button btnSend = new Button("GetGWTUserService");
    
    public PanelGWTUserServiceExample() {

        add(btnSend);
        add(lblServerReply);

         // Create an asynchronous callback to handle the result.
        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {

            public void onSuccess(List<User> result) {
                String str = "";
                 for (User statItem : result) {
                        str = str + " " + statItem.getFio() ;
                    }
                 
                lblServerReply.setText( str );
                
            }
            
            public void onFailure(Throwable caught) {
                lblServerReply.setText("Communication failed");
            }
        };

        // Listen for the button clicks
        btnSend.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // Make remote call. Control flow will continue immediately and later
                // 'callback' will be invoked when the RPC completes.
                getService().getAllUsers( callback );
            }
        });
    }
    
    public static GWTUserServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTUserService.class);
    }
}
