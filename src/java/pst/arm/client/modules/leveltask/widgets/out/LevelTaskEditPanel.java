package pst.arm.client.modules.leveltask.widgets.out;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.events.MessageEvent;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple;
import pst.arm.client.common.ui.controls.editdomain.EViewType;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.WindowHideListener;
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
import pst.arm.client.modules.leveltask.listener.DoAnswerListener;
import pst.arm.client.modules.leveltask.listener.DoCancelCloseListener;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskService;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskServiceAsync;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class LevelTaskEditPanel extends DomainEditPanelSimple<LevelTask> {

    protected final LevelTaskConstants levelTaskConstants = GWT.create(LevelTaskConstants.class);
    protected final static CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected final CommonImages commonImages = GWT.create(CommonImages.class);
    private final static BaseMessages baseMessages = GWT.create(BaseMessages.class);
    private GWTLevelTaskServiceAsync service = GWT.create(GWTLevelTaskService.class);
    private LevelTaskEditableForm pnlEdit;
    protected FloatNavigationPanel navigationPanel = null;
    private Button btnDoAnswer,btnCancelClose;    
    protected List<DoAnswerListener> doAnswerListeners = new ArrayList<DoAnswerListener>();
    protected List<DoCancelCloseListener> doCancelCloseListeners = new ArrayList<DoCancelCloseListener>();
    protected TabPanel tabPanelSub; //панель с закладкой для отображения прикрепленных файлов
    private DataGridPanel panelSub;
    private ToggleButton btnShowSub;
    boolean isShowSub;

    public LevelTaskEditPanel(LevelTask domain, EditState state, EWindowType windowType, FloatNavigationPanel navigationPanel) {
        super(domain, state, windowType);
        //isSaveEnabled = false;
        isSaveEnabled = false;
        pnlEdit = new LevelTaskEditableForm(state);
        this.navigationPanel = navigationPanel;
        registerEditForm(pnlEdit);

        //initializeAtmosphereClient();

        if (state == EditState.NEW) {
            setSize(620, 650);
        } else {
            setSize(620, 650);
        }

        addBtn();
        
        initPanelSub();

    }
    
    protected final void initPanelSub() {
        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        tabPanelSub.setVisible(isShowSub);
        BorderLayoutData panelDataSub = new BorderLayoutData(LayoutRegion.SOUTH, 180);
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

    @Override
    protected void updateControlsVisibility() {
        super.updateControlsVisibility();
        
        if (state == EditState.VIEW) {
            if (domain != null) {
                if (domain.getTaskState() == ETaskState.IN_CREATE.getTaskState()) {
                    btnSaveClose.show(); //отправить
                    btnSaveClose.setEnabled(true);
                    //enabledBtn(); //доступность кнопок - провеки прав доступа
                    layout();

                }
            }
        }

        
        btnShowSub.setEnabled(true);
        isShowSub = (state != EditState.NEW);
        btnShowSub.toggle(isShowSub);
        tabPanelSub.setVisible(isShowSub); 
        
        setPanelSubDomain();
        panelSub.reloadGrid();
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
                     pnlEdit.setFocusDescription();
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
    /*    public void initializeAtmosphereClient() {
     AtmosphereGWTSerializer serializer = GWT.create(MessageEventSerializer.class);
     client = new AtmosphereClient(GWT.getModuleBaseURL() + "comet", serializer, new MessageListener() {
     @Override
     public void onMessage(List<?> messages) {
     return;
     }
     });
     client.start();

     }*/

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
        return levelTaskConstants.headerView();
    }

    @Override
    protected String getHeaderCreate() {
        return levelTaskConstants.headerCreate();
    }

    @Override
    protected String getHeaderViewEdit() {
        return levelTaskConstants.headerEdit();
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
 protected void initListeners() {
        toolBarEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String id = ce.getButton().getItemId();
                if (id.equals("cancel")) {                     
                    if (viewType == EViewType.EVT_GRIDEDIT) {
                        closeWindow();
                    } else {
                        updateFormsFromDomain();
                    }
                } else if (id.equals("saveclose")) {
                    if (isValid()) {
                        saveclose(new Listener() {
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
                } else if (id.equals("save")) {
                    if (isValid()) {
                        save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                if ( viewType == EViewType.EVT_EDITGRID ) {
                                    setState(EditState.VIEW);
                                    updateControlsVisibility();
                                } else {
                                    setState(EditState.EDIT);
                                    updateControlsVisibility();
                                }
                            }
                        });
                    }
                }
                else if (id.equals("getdomainlists")) {
                    onChangeDomainId();
                    
                }
                else if (id.equals("delete")) {
                    onDelete();                    
                }
                else if (id.equals("edit")) {
                    onEdit();                    
                }
                 else if (id.equals("add")) {
                    onAdd();                    
                }
            }
        };

        addListener(Events.BeforeHide, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                be.setCancelled(true);
                closeWindow();
            }
        });
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
        domain.setTaskState(ETaskState.IN_CREATE.getTaskState());
        domain.setStateName(ETaskState.getTaskStateName(ETaskState.IN_CREATE));
        //getDomain().setSendSign(1);
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
                    //sendMessage(t);
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

    protected void saveclose(final Listener actionListener) {
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
                    sendMessage(t);
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
    private void sendMessage(LevelTask t) {
        //пока посылаем информацию о новых сообщениях
        if (navigationPanel == null) {
            return;
        }
        if (t.getTaskState() != ETaskState.CREATE.getTaskState()) {
            return;
        }
        Integer userIdTo = t.getUserIdTo();
        Integer userIdFrom = t.getUserIdFrom();//ConfigurationManager.getUserId().intValue();
        String msgInfo = baseMessages.msgType() + " " + t.getTaskName() + " " + baseMessages.msgFrom() + t.getUserNameFrom();
        //client.broadcast(new MessageEvent(0, baseMessages.btnMsgOut(),  usetIdFrom,usetIdTo));
        //client.broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo, MessageEvent.EMessageType.MSG_IN));
        if (navigationPanel.getAtmosphereClient() == null) {
            MessageBox.info("", "Невозможно отправить оповещение", null);
        } else {
            if (navigationPanel.getAtmosphereClient().isRunning()) {
                navigationPanel.getAtmosphereClient().broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo, MessageEvent.EMessageType.MSG_IN));
            } else {
                MessageBox.info("", "Невозможно отправить оповещение", null);
            }
        }
    }

    public void addDoAnswerListener(DoAnswerListener listener) {
        doAnswerListeners.add(listener);
    }
    
     public void addDoCancelCloseListener(DoCancelCloseListener listener) {
        doCancelCloseListeners.add(listener);
    }

    protected void notifyDoAnswer() {
        for (DoAnswerListener listener : doAnswerListeners) {
            listener.onDoAnswer();
        }
    }
    
     protected void notifyDoCancelClose() {
        for (DoCancelCloseListener listener : doCancelCloseListeners) {
            listener.onDoCancelClose();
        }
    }

    private void doAnswer() {
        notifyDoAnswer();
        closeWindow();   
    }
    
     private void doCancelClose() {
        notifyDoCancelClose();
        closeWindow();   
    }

    private void addBtn() {
        btnDoAnswer = new Button(levelTaskConstants.btnDoAnswer(), AbstractImagePrototype.create(images.msg_rotate()));
        btnDoAnswer.setItemId("doAnswer");
        btnDoAnswer.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                doAnswer();
            }
        });
        
        btnShowSub = new ToggleButton(levelTaskConstants.btnShowFile(), AbstractImagePrototype.create(images.add_file()));
        btnShowSub.setItemId("btnShowSub");
        isShowSub = (state != EditState.NEW);
            
        btnShowSub.toggle(isShowSub);        
        btnShowSub.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                if (!isValid())
                {
                    btnShowSub.toggle(false);
                    return;
                }
                
                isShowSub = !isShowSub;
                btnShowSub.toggle(isShowSub);
                if (isShowSub)
                {
                     if (isValid()) {
                        if (state ==EditState.NEW)
                        {
                         save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                if ( viewType == EViewType.EVT_EDITGRID ) {
                                    setState(EditState.VIEW);
                                    updateControlsVisibility();
                                } else {
                                    setState(EditState.EDIT);
                                    updateControlsVisibility();
                                }
                            }
                        });
                        }
                    }
                }
                tabPanelSub.setVisible(isShowSub);                
            }
        });
        
        btnCancelClose = new Button(levelTaskConstants.btnCloser(), AbstractImagePrototype.create(commonImages.msg_close()));
        btnCancelClose.setEnabled(true);
        btnCancelClose.setVisible(true);
        btnCancelClose.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                doCancelClose();
            }
        });
        btnCancel.setText(constants.exit());        

        btnSaveClose.setIcon(AbstractImagePrototype.create(commonImages.msg_send()));   
        btnSaveClose.setText(levelTaskConstants.btnSend());
        
        ((ToolBar) getTopComponent()).remove(btnCancel);
        ((ToolBar) getTopComponent()).add(btnDoAnswer);
        ((ToolBar) getTopComponent()).add(btnShowSub);
         //((ToolBar) getTopComponent()).add(btnCancelClose);
        ((ToolBar) getTopComponent()).add(btnCancel);
    }
    
    @Override 
    protected void updateBtnSaveInfo() {
        super.updateBtnSaveInfo();
        btnCancel.setText(constants.exit());        
        btnSaveClose.setIcon(AbstractImagePrototype.create(commonImages.msg_send()));   
        btnSaveClose.setText(levelTaskConstants.btnSend());
    }
    
    @Override
    public void enabledBtn() {
        super.enabledBtn();
        if (domain==null||state == null||domain.getTaskState() == null)
        {
            btnDoAnswer.setVisible(false);
            return;
        }   
        if (domain.getTaskState().equals(ETaskState.EXECUTED.getTaskState()) && state == EditState.VIEW) {
            btnDoAnswer.setVisible(true);
        } else {
            btnDoAnswer.setVisible(false);
        }   
        
      
        
    }
    
   

}
