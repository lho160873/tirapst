package pst.arm.client.modules.leveltask.widgets.in;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.HashMap;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.events.MessageEvent;
import pst.arm.client.common.events.MessageEvent.EMessageType;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.lang.LevelTaskConstants;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskService;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskServiceAsync;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class LevelTaskInEditPanel extends DomainEditPanelSimple<LevelTask> {

    protected final LevelTaskConstants levelTaskConstants = GWT.create(LevelTaskConstants.class);
    protected final static CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected final CommonImages commonImages = GWT.create(CommonImages.class);
    private final static BaseMessages baseMessages = GWT.create(BaseMessages.class);
    private GWTLevelTaskServiceAsync service = GWT.create(GWTLevelTaskService.class);
    private LevelTaskInEditableForm pnlEdit;
    //private AtmosphereClient client;    
    private Button btnSaveAndSend;
    protected FloatNavigationPanel navigationPanel = null;
   protected TabPanel tabPanelSub; //панель с закладкой для отображения прикрепленных файлов
    private DataGridPanel panelSub;
    private ToggleButton btnShowSub;
    boolean isShowSub;
   
    

    public LevelTaskInEditPanel(LevelTask domain, EditState state, EWindowType windowType, FloatNavigationPanel navigationPanel,EventBus eventBus) {
        super(domain, state, windowType,eventBus);
        isSaveEnabled = false;
        pnlEdit = new LevelTaskInEditableForm(state);
        pnlEdit.setEventBus(eventBus);
        registerEditForm(pnlEdit);
        this.navigationPanel = navigationPanel;
        //this.client = client;
        setSize( 620, 650 );
        addBtn();
        initPanelSub();
    }
      
    protected final void initPanelSub() {
        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        tabPanelSub.setVisible(true);
        BorderLayoutData panelDataSub = new BorderLayoutData(Style.LayoutRegion.SOUTH, 180);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        main.add(tabPanelSub, panelDataSub);
        //for (SRelationInfo subTable : table.getSubTables()) {
        SRelationInfo subTable = new SRelationInfo();
        subTable.setQueryName("DOC_FILE_LEVEL_TASK_HLV");
        subTable.setCaption("Файлы");
        HashMap< SKeyForColumn, SKeyForColumn> relations = new HashMap< SKeyForColumn, SKeyForColumn>();
        relations.put(new SKeyForColumn("MAIN:ID"), new SKeyForColumn("MAIN:DOC_ID"));
        subTable.setRelations(relations);

        final String tabName = "DOC_FILE_LEVEL_TASK_HLV";
        final String caption = "Файлы";
        panelSub = (DataGridPanel) DataGridPanelBuilder.createDataGridPanel(tabName, subTable);
        TabItem item = new TabItem();
        item.setClosable(false);
        item.setBorders(false);
        item.setItemId(tabName);
        item.setText(caption);
        item.setLayout(new FitLayout());
        item.add(panelSub);
        tabPanelSub.add(item);
        setPanelSubDomain();
    }
    
    private void setPanelSubDomain() {
        if (state != EditState.NEW) {
            DDataGrid domainLevelTask = new DDataGrid();
            domainLevelTask.setName("LEVEL_TASK");
            SKeyForColumn key = new SKeyForColumn("MAIN:ID");
            DRowColumnValNumber oId = new DRowColumnValNumber();
            oId.setVal(domain.getDomainId());
            domainLevelTask.getRows().put(key, oId);
            panelSub.putGridSubFilters(domainLevelTask);
        } else {
            panelSub.putGridSubFilters(null);
        }
    }
    
     
      
    private void addBtn() {
        btnShowSub = new ToggleButton(levelTaskConstants.btnShowFile(), AbstractImagePrototype.create(images.add_file()));
        btnShowSub.setItemId("btnShowSub");
        isShowSub = true;
        btnShowSub.toggle(isShowSub);        
        btnShowSub.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                isShowSub = !isShowSub;
                btnShowSub.toggle(isShowSub);
                tabPanelSub.setVisible(isShowSub);                
            }
        });
        
        btnSaveAndSend = new Button(levelTaskConstants.btnSaveSend());
        btnSaveAndSend.setIcon(AbstractImagePrototype.create(commonImages.msg_check()));        
        btnSaveAndSend.setItemId("saveandsend");
        btnSaveAndSend.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                pnlEdit.setIsSend(Boolean.TRUE);
                
                save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                removeAllListeners();
                                hide();
                                if (wnd != null) {
                                    wnd.removeAllListeners();
                                    wnd.hide();
                                }
                                notifyWindowHide();
                            }
                        });

            }
        });
      
        btnCancel.setText(constants.exit());
        btnSaveClose.setIcon(AbstractImagePrototype.create(commonImages.msg_read()));        
        btnSaveClose.setText(levelTaskConstants.read());
        ((ToolBar) getTopComponent()).remove(btnCancel);
        ((ToolBar) getTopComponent()).add(btnSaveAndSend);
        ((ToolBar) getTopComponent()).add(btnShowSub);
        ((ToolBar) getTopComponent()).add(btnCancel);
        
    }
    
    @Override
    public void setState(EditState state) {
        this.state = state;
        notifyEditStateChanged();
        pnlEdit.setState(state);
    }
   
    @Override
    protected GWTEditServiceAsync getService() {
        return service;
    }

    @Override
    protected Component getEditPanel() {
        return pnlEdit;
    }

    @Override
    protected String getHeaderView() {
        return levelTaskConstants.headerInView();
    }

    @Override
    protected String getHeaderCreate() {
        return levelTaskConstants.headerInCreate();
    }

    @Override
    protected String getHeaderViewEdit() {
        return levelTaskConstants.headerInEdit();
    }

    @Override
    protected LevelTask createDomain() {
        LevelTask d = pnlEdit.createLevelTask();
        return d;
    }

    @Override
    protected void focusInvalidField() {
        //TODO: add implementation
    }
    
    @Override
    protected void closeWindow() {
        checkUnsavedChanged(new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {

                if (domain.getTaskState().equals(ETaskState.CREATE.getTaskState())) {
                    domain.setTaskState(ETaskState.IN_WORK.getTaskState());
                    domain.setSendSign(1);
                    getService().save(domain, Boolean.FALSE, new AsyncCallback<LevelTask>() {
                        @Override
                        public void onFailure(Throwable thrwbl) {

                            AppHelper.showMsgRpcServiceError(thrwbl);
                        }

                        @Override
                        public void onSuccess(LevelTask t) {
                            if (t == null) {
                                onFailure(null);
                            } else {
                                getDomain().copy(t);
                                //updateFormsFromDomain();
                                notifyDomainSaveSuccesed();
                                removeAllListeners();
                                hide();
                                if (wnd != null) {
                                    wnd.hide();
                                }
                                //notifyWindowHide();
                            }
                        }
                    });
                } else {
                    removeAllListeners();
                    hide();
                    if (wnd != null) {
                        wnd.removeAllListeners();
                        wnd.hide();
                    }
                    notifyWindowHide();
                }
            }
        });
    }

    @Override
    public void enabledBtn() {
        super.enabledBtn();
        if ((domain.getTaskState().equals(ETaskState.CLOSE.getTaskState())) || (domain.getTaskState().equals(ETaskState.CANCEL.getTaskState()))) {
            btnSaveAndSend.setEnabled(false);
        }
        
        
        
    }

    @Override
    protected void updateBtnSaveInfo() {
        super.updateBtnSaveInfo();
        btnCancel.setText(constants.exit());
        btnSaveClose.setIcon(AbstractImagePrototype.create(commonImages.msg_read()));   
        btnSaveClose.setText(levelTaskConstants.read());
    }
 
    @Override
    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }
        final LevelTask oldDomain = domain.newInstance();
        oldDomain.copy(domain);

        editForms.fillDomain(getDomain());
        getDomain().setSendSign(1);
        getService().save(getDomain(), (state == EditState.NEW), new AsyncCallback<LevelTask>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                getEditPanel().unmask();
                AppHelper.showMsgRpcServiceError(thrwbl);
            }
            @Override
            public void onSuccess(LevelTask t) {
                if (t == null) {
                    onFailure(null);
                } else {
                    if (t.getTaskState().equals(ETaskState.EXECUTED.getTaskState())) {
                        sendMessage(t);
                    }
                    getDomain().copy(t);
                    updateFormsFromDomain();
                    notifyDomainSaveSuccesed();
                    getEditPanel().unmask();
                    setHeading(getHeaderViewEdit());
                    if (state.equals(EditState.NEW)) {
                        state = EditState.EDIT;
                        updateControlsVisibility();
                    }
                    if (actionListener != null) {
                        actionListener.handleEvent(new BaseEvent(Save));
                    }

                }
            }
        });
    }

    public void sendMessage(LevelTask t) {
        if (navigationPanel == null) {
            return;
        }
        Integer userIdTo = t.getUserIdTo();
        Integer userIdFrom = t.getUserIdFrom();//ConfigurationManager.getUserId().intValue();        
        String msgInfo = baseMessages.msgType() + " " + t.getTaskName() + "  " + baseMessages.msgFrom() + t.getUserNameFrom();
        //client.broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo,EMessageType.MSG_OUT));
        
        if (navigationPanel.getAtmosphereClient() == null) {
            MessageBox.info("", "Невозможно отправить оповещение", null);
        } else {
            if (navigationPanel.getAtmosphereClient().isRunning()) {
                navigationPanel.getAtmosphereClient().broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo, EMessageType.MSG_OUT));
            } else {
                MessageBox.info("", "Невозможно отправить оповещение", null);
            }
        }
    }
@Override
    public void show() {
        if (windowType == EWindowType.EWT_WINDOW) {
            wnd = new Window();
            wnd.setMaximizable(Boolean.TRUE);
            wnd.setClosable(Boolean.TRUE);
            wnd.setPlain(Boolean.TRUE);
            wnd.setModal(Boolean.TRUE);
            wnd.setLayout(new FitLayout());
            this.setHeaderVisible(false);
            this.setBorders(false);
            wnd.add(this);
            wnd.setModal(true);
            wnd.setSize(this.width,this.height);  
            wnd.setFocusWidget(this);
            initHeading();
             wnd.addListener(Events.Show, new Listener() {
                @Override
                public void handleEvent(BaseEvent be) {
                    pnlEdit.setFocusReply();
                }
            });
            wnd.addListener(Events.BeforeHide, new Listener() {
                @Override
                public void handleEvent(BaseEvent be) {
                    be.setCancelled(true);
                    closeWindow();
                }
            });
            wnd.show();
        } else {
            super.show();
        }
    }

 
}

