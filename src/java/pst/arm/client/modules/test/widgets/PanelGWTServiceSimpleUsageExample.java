package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import java.util.List;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.test.service.remote.GWTServiceSimple;
import pst.arm.client.modules.test.service.remote.GWTServiceSimpleAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PanelGWTServiceSimpleUsageExample extends ContentPanel {

    private Label lblServerReply = new Label();
    private TextBox txtUserInput = new TextBox();
    private Button btnSend = new Button("Send to server (get template)");
    private Label lblGetUser = new Label();
    private Button btnGetUser = new Button("GetUser");
    private Label lblGetDict = new Label();
    private Button btnGetDict = new Button("GetDict");
    private Label lblGetTableName = new Label();
    private Button btnGetTableName = new Button("GetTableName");
    
    public PanelGWTServiceSimpleUsageExample() {
        add(new Label("Input your text: "));
        add(txtUserInput);
        add(btnSend);
        add(lblServerReply);
        add(btnGetUser);
        add(lblGetUser);
        add(btnGetDict);
        add(lblGetDict);
         add(btnGetTableName);
        add(lblGetTableName);

        // Create an asynchronous callback to handle the result.
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(String result) {
                lblServerReply.setText(result);
                
            }
            
            public void onFailure(Throwable caught) {
                lblServerReply.setText("Communication failed:" +caught.getMessage());
            }
        };

        // Listen for the button clicks
        btnSend.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // Make remote call. Control flow will continue immediately and later
                // 'callback' will be invoked when the RPC completes.
                getService().myMethod(txtUserInput.getText(), callback);
            }
        });
        
        
         // Create an asynchronous callback to handle the result.
        final AsyncCallback<List<String>> callback_getUser = new AsyncCallback<List<String>>() {

            public void onSuccess(List<String> result) {
                String str = "";
                 for (String statItem : result) {
                        str = str + " " + statItem ;
                    }
                 
                lblGetUser.setText( str );
                
            }
            
            public void onFailure(Throwable caught) {
                lblGetUser.setText("Communication failed:" +caught.getMessage());
            }
        };

        // Listen for the button clicks
        btnGetUser.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Make remote call. Control flow will continue immediately and later
                // 'callback' will be invoked when the RPC completes.
                getService().myMethodGetAllUsers(callback_getUser);
            }
        });
        
        
          // Create an asynchronous callback to handle the result.
        final AsyncCallback<List<DDataGrid>> callback_getDict = new AsyncCallback<List<DDataGrid>>() {

            public void onSuccess(List<DDataGrid> result) {
                String str = "";
                 for (DDataGrid statItem : result) {
                        //str += " FIELD VALUE: " + statItem.getRows().get(1).getVal().toString()+" FIELD_NAME: "+statItem.getRows().get(1).getColumnBuilder().getColumn().getName();
                
                    }
                 
                lblGetUser.setText( str );
                
            }
            
            public void onFailure(Throwable caught) {
                lblGetUser.setText("Communication failed:" +caught.getMessage());
            }
        };
        
        
          // Create an asynchronous callback to handle the result.
        final AsyncCallback<PagingLoadResult<DDataGrid>> callback_getDictPage = new AsyncCallback<PagingLoadResult<DDataGrid>>() {

            public void onSuccess(PagingLoadResult<DDataGrid> result) {

                 String str = "";
                 for (DDataGrid statItem : result.getData()) 
                 {
                      //str += " FIELD VALUE: " + statItem.getRows().get(1).getVal().toString()+" FIELD_NAME: "+statItem.getRows().get(1).getColumnBuilder().getColumn().getName();                
                    }
                 
                lblGetUser.setText( str );
                
            }
            
            public void onFailure(Throwable caught) {
                lblGetUser.setText("Communication failed:" +caught.getMessage());
            }
        };

        // Listen for the button clicks
        btnGetDict.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                //getServiceDict().getAllDataGrid(callback_getDict);
                DataGridSearchCondition condition = new DataGridSearchCondition();
                condition.setOffset(0);
                condition.setLimit(10);
                 getServiceDict().getPage( "ARCHIVE", condition, callback_getDictPage );
            }
        });
    
    
    
    
     // Create an asynchronous callback to handle the result.
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            public void onSuccess(DTable result) {
                String name = result.getCaption();
                lblGetTableName.setText(name);
                
            }
            
            public void onFailure(Throwable caught) {
                lblGetTableName.setText("Communication failed:" +caught.getMessage());
            }
        };
        
       
               


        // Listen for the button clicks
        btnGetTableName.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {            
                    getServiceDict().getTable( txtUserInput.getText(), callback_getTable);             
                //getServiceDict().getQuery( txtUserInput.getText(), callback_getQuery);             
            }
        });
        
    }
    
    public static GWTServiceSimpleAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTServiceSimple.class);
    }
    
    public static GWTDDataGridServiceAsync getServiceDict() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTDDataGridService.class);
    }
}
