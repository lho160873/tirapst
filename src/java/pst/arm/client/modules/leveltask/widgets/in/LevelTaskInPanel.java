package pst.arm.client.modules.leveltask.widgets.in;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.events.MessageEvent;
import pst.arm.client.common.events.MessageEvent.EMessageType;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.form.PanelFiltrSimple;
import pst.arm.client.common.ui.grid.BaseGridSimple;
import pst.arm.client.common.ui.grid.GridColumnConfig;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenService;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenServiceAsync;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
import pst.arm.client.modules.leveltask.lang.LevelTaskConstants;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskService;
import pst.arm.client.modules.leveltask.service.remote.GWTLevelTaskServiceAsync;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class LevelTaskInPanel extends BaseGridSimple<LevelTask> {
   
    protected GWTLevelTaskServiceAsync service = GWT.create(GWTLevelTaskService.class);
    protected final LevelTaskConstants levelTaskConstants = GWT.create(LevelTaskConstants.class);
    protected final DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected Button btnAnswer, btnMark, btnOpenFile;
    private final static BaseMessages baseMessages = GWT.create(BaseMessages.class);
    //protected AtmosphereClient client;
    protected FloatNavigationPanel navigationPanel = null;
    protected MenuItem menuAnswer, menuMark, menuOpenFile;
    //protected final CommonImages commonImages = GWT.create(CommonImages.class);
    final GWTDataGridFileOpenServiceAsync fileOpenService = GWT.create(GWTDataGridFileOpenService.class);
   

    public LevelTaskInPanel() {
        super();
        addBtn();
        //initializeAtmosphereClient();
        tbPaging.setPageSize(Integer.parseInt(levelTaskConstants.recordCount()));
    }

    public LevelTaskInPanel(FloatNavigationPanel np) {
        super();
        navigationPanel = np;
        addBtn();
        //initializeAtmosphereClient();
        tbPaging.setPageSize(Integer.parseInt(levelTaskConstants.recordCount()));
    }

   
    
    public LevelTaskInPanel(EWindowType windowType) {
        super(windowType);
        addBtn();
        //initializeAtmosphereClient();
        tbPaging.setPageSize(Integer.parseInt(levelTaskConstants.recordCount()));
    }
     public LevelTaskInPanel(EWindowType windowType,FloatNavigationPanel np) {
        super(windowType);
        navigationPanel = np;
        addBtn();
        //initializeAtmosphereClient();
        tbPaging.setPageSize(Integer.parseInt(levelTaskConstants.recordCount()));
    }
     
     
    /*public void initializeAtmosphereClient() {
        AtmosphereGWTSerializer serializer = GWT.create(MessageEventSerializer.class);
        client = new AtmosphereClient(GWT.getModuleBaseURL() + "comet", serializer, new MessageListener() {
            @Override
            public void onMessage(List<?> messages) {
                return;
            }
        });
        client.start();

    }*/
    public void sendMessage(LevelTask t)
    {
         if (navigationPanel == null) return;
        Integer userIdTo = t.getUserIdTo();
        Integer userIdFrom = t.getUserIdFrom();//ConfigurationManager.getUserId().intValue();        
        String msgInfo = baseMessages.msgType() + " " + t.getTaskName() +" "  + baseMessages.msgFrom() + t.getUserNameFrom();
        //client.broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo,EMessageType.MSG_OUT));
        if (navigationPanel.getAtmosphereClient() == null) {
            MessageBox.info("", "Невозможно отправить оповещение", null);
        } else {
            navigationPanel.getAtmosphereClient().broadcast(new MessageEvent(0, msgInfo, userIdFrom, userIdTo,EMessageType.MSG_OUT));
        }
    }
    private void addBtn() {

        btnAnswer = new Button(levelTaskConstants.btnAnswer());
        btnAnswer.setIcon(AbstractImagePrototype.create(commonImages.msg_check()));         
        btnAnswer.setEnabled(true);
        btnAnswer.setVisible(true);
        btnAnswer.addSelectionListener(new SelectionListener(){
            @Override
            public void componentSelected(ComponentEvent ce) {
                onAnswer();
            }
        });
        
        menuAnswer = new MenuItem(levelTaskConstants.btnAnswer());
        menuAnswer.setIcon(AbstractImagePrototype.create(commonImages.msg_check()));   
        menuAnswer.setEnabled(true);
        menuAnswer.setVisible(true);
        menuAnswer.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAnswer();
            }
        });
        menu.add(menuAnswer);

        btnMark = new Button(datagridConstants.btnMark());
        btnMark.setIcon(AbstractImagePrototype.create(commonImages.msg_check()));        
        btnMark.setEnabled(true);
        btnMark.setVisible(true);
        btnMark.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                onMark();
            }
        });

        menuMark = new MenuItem(datagridConstants.btnMark());
        menuMark.setIcon(AbstractImagePrototype.create(commonImages.msg_check())); 
        menuMark.setEnabled(true);
        menuMark.setVisible(true);
        menuMark.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onMark();
            }
        });
        //menu.add(menuMark);
        
        
        btnOpenFile = new Button(levelTaskConstants.btnOpenFile());
        btnOpenFile.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnOpenFile.setIconAlign(Style.IconAlign.LEFT);
        btnOpenFile.setEnabled(false);
        btnOpenFile.addSelectionListener(tbListener);
       btnOpenFile.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                onOpenFile();
            }
        });
       
        menuOpenFile = new MenuItem(levelTaskConstants.btnOpenFile());
        menuOpenFile.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuOpenFile.setEnabled(false);
        menuOpenFile.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onOpenFile();
            }
        });
        menu.add(menuOpenFile);
        
        toolBar.remove(fillTool);
        toolBar.remove(sprFiltr);
        toolBar.remove(btnFiltr);


        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));        
        btnEdit.setText(levelTaskConstants.btnCreateAnswer());

        menuItemEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));   
        menuItemEdit.setText(levelTaskConstants.btnCreateAnswer());

        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAnswer);
        toolBar.add(new SeparatorToolItem());
        //toolBar.add(btnMark);
        //toolBar.add(new SeparatorToolItem());
        toolBar.add(btnOpenFile);
        toolBar.add(new SeparatorToolItem());



        toolBar.add(fillTool);
        toolBar.add(sprFiltr);
        toolBar.add(btnFiltr);

    }

    protected void onAnswer() {
       
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }

        List<BeanModel> beanModels = grid.getSelectionModel().getSelectedItems();
        if (beanModels == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }

        List<Integer> ids = new ArrayList<Integer>();
        for (BeanModel model : beanModels) {
            LevelTask domain = model.getBean();

            final Integer id = domain.getId();
            if (id == null) {
                continue;
            }
            ids.add(id);
        }
        if (ids.isEmpty()) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }
       final String strCount = String.valueOf(ids.size());
        service.sendAnswer(ids, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                //MessageBox.info(commonConstants.attantion(), "Обработано "+strCount+" сообщени(е/й).", null);
                reloadGrid();
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        });
    }
    
    

    protected void onMark() {        
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }

        List<BeanModel> beanModels = grid.getSelectionModel().getSelectedItems();
        if (beanModels == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }

        List<Integer> ids = new ArrayList<Integer>();
        for (BeanModel model : beanModels) {
            LevelTask domain = model.getBean();

            String module = domain.getModule();
            if (!module.equals("OFFICE_DOC_CONTR_IG")) {
                continue;
            }

            Integer currentId = domain.getCurrentId();
            if (currentId == null) {
                continue;
            }
            ids.add(currentId);
        }
        if (ids.isEmpty()) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }
       final String strCount = String.valueOf(ids.size());
        service.markOfficeDocContr(ids, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                MessageBox.info(commonConstants.attantion(), "Обработан(о) "+strCount+" приказ(ов).", null);
                reloadGrid();
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        });

    }

    protected void onOpenFile() {
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }

        List<BeanModel> beanModels = grid.getSelectionModel().getSelectedItems();
        if (beanModels == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }
        LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        Integer id = domain.getId();

        service.getDocFileId(id, new AsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer fileId) {
                if (fileId == -1) {
                    MessageBox.alert(commonConstants.attantion(),  levelTaskConstants.notFindFile(), null);
                    return;
                }
                final String fid = fileId.toString();

                fileOpenService.openFile(fid, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                         AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + fid);
                        } else {
                            MessageBox.alert(commonConstants.attantion(), datagridConstants.errOpenFile(), null);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        });

    }

    @Override
    protected void createHeader() {
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        setHeading(constants.headerIn());
    }

    @Override
    protected PanelFiltrSimple createPanelFiltr() {
        return new LevelTaskInFiltr(this);
    }

    @Override
    protected String getGridId() {
        return "leveltaskGrid";
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа

        sprEdit.setVisible(true);
        sprMenuItemEdit.setVisible(true);

        btnAdd.setVisible(false);
        menuItemAdd.setVisible(false);

        btnEdit.setVisible(true);
        menuItemEdit.setVisible(true);

        btnDelete.setVisible(false);
        menuItemDelete.setVisible(false);

        if (ConfigurationManager.isModuleAvailable("adminfield")) {
            btnDelete.setVisible(true);
            menuItemDelete.setVisible(true);
        }
        btnView.setVisible(false);
        menuItemView.setVisible(false);

    }

    @Override
    protected void createCondition() {
        if (condition != null) {
            return;
        }
        condition = new LevelTaskSearchCondition();
    }

    @Override
    protected void onAdd() {
        LevelTask domain = new LevelTask();
        LevelTaskInEditPanel window = new LevelTaskInEditPanel(domain, EditState.NEW, windowType, navigationPanel, eventBus);
        window.addWindowHideListener(windowHideListener);
        window.addDomainSaveSuccesedListener(saveDomainListener);
        window.show();
        firePanelChangeEvent(panelChangeEventType, window);
    }

    @Override
    protected void onEdit() {
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }
        currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
        LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        LevelTaskInEditPanel window = new LevelTaskInEditPanel(domain, EditState.EDIT, windowType,navigationPanel,eventBus);
        window.addDomainSaveSuccesedListener(saveDomainListener);
        window.addWindowHideListener(windowHideListener);
        window.show();
        firePanelChangeEvent(panelChangeEventType, window);
    }

    @Override
    protected void onDelete() {
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        MessageBox.confirm(commonConstants.confirm(), constants.confirmDelete(), new Listener<MessageBoxEvent>() {

            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                    List<Integer> ids = new ArrayList<Integer>();
                    for (final BeanModel beanModel : selection) {
                        LevelTask domain = beanModel.getBean();
                        ids.add(domain.getId().intValue());
                    }
                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            for (final BeanModel beanModel : selection) {
                                grid.getStore().remove(beanModel);
                            }
                            selectCurrentRow();
                        }
                    };
                    getService().deleteDomainsByIds(ids, callback);
                }
            }
        });
    }

    @Override
    protected void onView() {
        LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        LevelTaskInEditPanel window = new LevelTaskInEditPanel(domain, EditState.VIEW, windowType, navigationPanel, eventBus);
        
        window.addDomainSaveSuccesedListener(saveDomainListener);
        window.addWindowHideListener(windowHideListener);
        window.show();
        firePanelChangeEvent(panelChangeEventType, window);

    }

    @Override
    public void loadData(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BeanModel>> callback) {
        //если передана панель с управляющими кнопками, то всегда при перечитке убираем мигания если они были
        if (navigationPanel != null) {
            navigationPanel.undoAattentionIn();
        }
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);

        getCondition().getFilters().clear();
        getCondition().getFilters().put("userIdTo", ConfigurationManager.getUserId().intValue());
        //getCondition().getFilters().put("taskState", ETaskState.EXECUTED.getTaskState());
        config.setLimit(Integer.parseInt(constants.recordCount()));
        getCondition().setPagingConfig(config.getLimit(), config.getOffset(), config.getSortDir().name(), config.getSortField());
        service.getPage((LevelTaskSearchCondition) getCondition(), callback);

    }

    @Override
    public void createColumnsConfig() {
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        
        setGridColumnsConfig(new ArrayList<ColumnConfig>());
        
        ColumnConfig config = new ColumnConfig("nullCloumn", "", 16);
        config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        config.setFixed(true);
        getGridColumnsConfig().add(config);
        
   
        GridCellRenderer<BeanModel> renderPriority = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                LevelTask d = model.getBean();
                String txt = "";
                if (d.getPriority().equals(1)) {
                    txt = "Да";
                }
                if (!txt.isEmpty() && d.getTaskState() == ETaskState.CREATE.getTaskState()) {
                    txt = "<b>" + txt + "</b>";
                }
                return txt;
            }
        };
        GridCellRenderer<BeanModel> renderBoldDateTime = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                LevelTask d = model.getBean();
                Date createDate = d.getCreateDate();
                String txt = DateTimeFormat.getFormat(commonConstants.dateDateTime()).format(createDate);
                if (d.getTaskState() == ETaskState.CREATE.getTaskState()) {
                    return "<b>" + txt + "</b>";
                } else {
                    return txt;
                }
            }
        };

        GridCellRenderer<BeanModel> renderBold = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                LevelTask d = model.getBean();
                if (d.getTaskState() == ETaskState.CREATE.getTaskState()) {
                    return "<b>" + String.valueOf(model.get(property, "")) + "</b>";
                } else {
                    return model.get(property);
                }
            }
        };
        cbMultilineColumn = new CheckBoxSelectionModel<BeanModel>();

        cbMultilineColumn.setSelectionMode(Style.SelectionMode.MULTI);
        getGridColumnsConfig().add(cbMultilineColumn.getColumn());

        getGridColumnsConfig().add(new GridColumnConfig("createDate", constants.columnCreateDate(), 60, true, HorizontalAlignment.LEFT, renderBoldDateTime));
        getGridColumnsConfig().add(new GridColumnConfig("taskName", constants.columnTaskName(), 60, true, HorizontalAlignment.LEFT, renderBold));
        getGridColumnsConfig().add(new GridColumnConfig("userNameFrom", constants.columnUserFrom(), 60, true, HorizontalAlignment.LEFT, renderBold));
        getGridColumnsConfig().add(new GridColumnConfig("stateName", constants.columnTaskState(), 50, true, HorizontalAlignment.LEFT, renderBold));
        getGridColumnsConfig().add(new GridColumnConfig("priority", constants.columnPriorityName(), 40, true, HorizontalAlignment.LEFT, renderPriority));
        getGridColumnsConfig().add(new GridColumnConfig("descriptionShort", constants.columnDescription(), 200, true, HorizontalAlignment.LEFT, renderBold));
    }

    
     @Override
    protected void onDomainSaveOk(LevelTask domain) {
          
        BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(LevelTask.class);
        ModelData model = factory.createModel(domain);
        if (grid.getStore() == null) {
            return;
        }
        if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null && (((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean() == domain)) {           
            grid.getStore().update(model);
            grid.getSelectionModel().select(model, true);
        } else {
            grid.getStore().insert(model, grid.getStore().getCount());
            grid.getSelectionModel().select(model, true);
        }
        grid.getSelectionModel().select(0, true);
        tbPaging.first();
        //reloadGrid();
    }

    
    
    protected GWTLevelTaskServiceAsync getService() {
        return service;
    }
    
    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();

        if (grid.getSelectionModel().getSelection().size() <= 0) {
            btnAnswer.setEnabled(false);
            menuAnswer.setEnabled(false);
            //btnMark.setEnabled(false);
            //menuMark.setEnabled(false);
            btnOpenFile.setEnabled(false);
            menuOpenFile.setEnabled(false);
            return;
        }

        LevelTask currDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        if ((currDomain.getTaskState().equals(ETaskState.CLOSE.getTaskState())) || (currDomain.getTaskState().equals(ETaskState.CANCEL.getTaskState()))) {
            btnAnswer.setEnabled(false);
            menuAnswer.setEnabled(false);
            btnOpenFile.setEnabled(false);
            menuOpenFile.setEnabled(false);
        } else {
            btnAnswer.setEnabled(true);
            menuAnswer.setEnabled(true);
            //btnOpenFile.setEnabled(true);
            //menuOpenFile.setEnabled(true);
        }
      
        //btnMark.setEnabled(true);
        //menuMark.setEnabled(true);
        /*Integer currentId = currDomain.getCurrentId();
        String module = currDomain.getModule();
        Boolean isMark = (currentId != null && module.equals("OFFICE_DOC_CONTR_IG"));
        btnMark.setEnabled(isMark);
        menuMark.setEnabled(isMark);*/
        
        Integer currentId = currDomain.getCurrentId();
        String module = currDomain.getModule();
        Boolean isMark = (currentId != null && module.equals("OFFICE_DOC_CONTR_IG"));
        btnOpenFile.setEnabled(isMark);
        btnOpenFile.setEnabled(isMark);
    }

}
