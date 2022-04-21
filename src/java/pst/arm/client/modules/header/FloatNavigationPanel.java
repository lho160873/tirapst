/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.header;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.List;
import org.atmosphere.gwt.client.AtmosphereClient;
import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.eventbus.CurrentIdEvent;
import pst.arm.client.common.eventbus.CurrentIdHandler;
import pst.arm.client.common.events.MessageEvent;
import pst.arm.client.common.events.MessageEvent.EMessageType;
import pst.arm.client.common.events.MessageEventSerializer;
import pst.arm.client.common.events.MessageListener;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.images.ArmImages;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskService;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskServiceAsync;
import pst.arm.client.modules.mainpage.widgets.MessagesInWindow;
import pst.arm.client.modules.mainpage.widgets.MessagesOutWindow;
import pst.arm.client.modules.mainpage.widgets.MessagesReglamentWindow;
/**
 *
 * @author root
 */

public class FloatNavigationPanel extends ContentPanel {

    protected BasePageNew basePageNew;
    protected ArmImages armImages;
    protected Button button1, button2, button3;
    private AtmosphereClient client;
    private BaseMessages baseConstants = (BaseMessages) GWT.create(BaseMessages.class);
    protected final CommonImages images = GWT.create(CommonImages.class);
    protected GWTLevelTaskServiceAsync service = GWT.create(GWTLevelTaskService.class);
    private EventBus eventBus;
    protected MessagesOutWindow wndOut;
     protected MessagesInWindow wndIn;
     protected String currentId;
     
    public FloatNavigationPanel() {
        this.armImages = (ArmImages) GWT.create(ArmImages.class);
        setLayout(new FitLayout());
        setCollapsible(true);
        initView();
        initializeAtmosphereClient();
        initAttantions();
        //hlv 05082013 initializeAtmosphereClient();
        //hlv 05082013 initAttantions();
       
    }
     public FloatNavigationPanel(BasePageNew basePageNew,EventBus eventBus) {
        this();
        this.basePageNew = basePageNew;
        this.eventBus = eventBus;
        initHandlers();

    }
    public AtmosphereClient getAtmosphereClient() {
        return client;
    }
    public void initializeAtmosphereClient() {
        AtmosphereGWTSerializer serializer = GWT.create(MessageEventSerializer.class);
        client = new AtmosphereClient(GWT.getModuleBaseURL() + "comet", serializer, new MessageListener() {
            @Override
            public void onMessage(List<?> messages) {
                StringBuilder result = new StringBuilder();
                for (Object obj : messages) {
                    result.append(obj.toString()).append("<br/>");
                    if (obj.getClass() == pst.arm.client.common.events.MessageEvent.class) {
                        Integer userIdMy = ConfigurationManager.getUserId().intValue();
                        if (((MessageEvent) obj).getMsgType() == EMessageType.MSG_IN) {
                            if (((MessageEvent) obj).getUserIdTo() == userIdMy) {
                                Info.display(baseConstants.msgInfoIn(), result.toString());
                                button1.setIcon(AbstractImagePrototype.create(images.attentionIn()));
                                break;
                            }
                        } else if (((MessageEvent) obj).getMsgType() == EMessageType.MSG_OUT) {
                            if (((MessageEvent) obj).getUserIdFrom() == userIdMy) {
                                Info.display(baseConstants.msgInfoIn(), result.toString());
                                button2.setIcon(AbstractImagePrototype.create(images.attentionOut()));
                                break;
                            }
                        }
                    }
                }
            }
        });
        client.start();
    }

   

    public void undoAattentionIn() {
        button1.setIcon(AbstractImagePrototype.create(images.readLetter()));
    }

    public void undoAattentionOut() {
        button2.setIcon(AbstractImagePrototype.create(images.letter()));
    }

    public void setAattentionIn()
    {
         button1.setIcon(AbstractImagePrototype.create(images.attentionIn()));
    }
    
    public void setAattentionOut()
    {
         button2.setIcon(AbstractImagePrototype.create(images.attentionOut()));
    }
    
    public void initView() {

        final int weight_height = 37;

        LayoutContainer lc = new LayoutContainer(new RowLayout(Style.Orientation.VERTICAL));
        //lc.setLayoutData( new RowData( -1, -1 ) );
        //button1 = new Button( "1");
        button1 = new Button("", AbstractImagePrototype.create(images.readLetter()));
        final FloatNavigationPanel parent = this;
        button1.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                button1.setIcon(AbstractImagePrototype.create(images.readLetter()));
                if (wndIn == null) {
                    wndIn = new MessagesInWindow(parent);
                    wndIn.getPanel().setEventBus(eventBus);
                }
                wndIn.show();
            }
        });
        button1.setSize(weight_height, weight_height);

        button2 = new Button("", AbstractImagePrototype.create(images.letter()));
        button2.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                button2.setIcon(AbstractImagePrototype.create(images.letter()));
                if (wndOut == null) {
                    wndOut = new MessagesOutWindow(parent, currentId);
                    wndOut.getPanel().setEventBus(eventBus);
                }
                wndOut.show();
            }
        });
        
        button2.setSize(weight_height, weight_height);

        button3 = new Button("3");//, armImages.gnome() );
        button3.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                //MessageBox.alert( "", constants.btnMsgOut(), null ); 
                MessagesReglamentWindow wnd = new MessagesReglamentWindow();
                wnd.show();
            }
        });
        button3.setSize(weight_height, weight_height);

        lc.add(button1);
        lc.add(button2);
       // lc.add(button3);
        add(lc);
    }
    
    private void initAttantions()
    {
        service.isHasNewMsgIn( ConfigurationManager.getUserId().intValue(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(Boolean b) {
                if (b) {
                    setAattentionIn();
                }
            }
        } );
        
         service.isHasNewMsgOut( ConfigurationManager.getUserId().intValue(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(Boolean b) {
                if (b) {
                    setAattentionOut();
                }
            }
        } );    
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
         initHandlers();
    }
    
     private void initHandlers() {                   
        if (getEventBus() != null) {
            getEventBus().addHandler(CurrentIdEvent.TYPE, new CurrentIdHandler() {
                @Override
                public void handleCurrentIdEvent(CurrentIdEvent event) {
                    currentId = event.getId();                 
                }
            });
        }
    }
    
}
