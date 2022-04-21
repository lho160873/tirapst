package pst.arm.client.modules.leveltask.widgets.out;

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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.form.PanelFiltrSimple;
import pst.arm.client.common.ui.grid.BaseGridSimple;
import pst.arm.client.common.ui.grid.GridColumnConfig;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.LevelTask;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
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
public class LevelTaskPanel extends BaseGridSimple<LevelTask> {
   
    protected GWTLevelTaskServiceAsync service = GWT.create(GWTLevelTaskService.class);
    protected final LevelTaskConstants contractConstants = GWT.create(LevelTaskConstants.class);
     protected final LevelTaskConstants levelTaskConstants = GWT.create(LevelTaskConstants.class);
    protected Button btnCancelClose, btnDoAnswer;
    protected FloatNavigationPanel navigationPanel = null;
    protected MenuItem menuCancelClose,menuDoAnswer;
    protected DoAnswerListener doAnswerListener;
    protected DoCancelCloseListener doCancelCloseListener;

    public LevelTaskPanel() {
        super();
        addBtn();
        tbPaging.setPageSize(Integer.parseInt(contractConstants.recordCount()));
    }

    public LevelTaskPanel(FloatNavigationPanel pnl,String currentId) {
        super();
        navigationPanel = pnl;
        addBtn();
        tbPaging.setPageSize(Integer.parseInt(contractConstants.recordCount()));
        this.currentId = currentId;
    }

    public LevelTaskPanel(EWindowType windowType) {
        super(windowType);
        addBtn();
        tbPaging.setPageSize(Integer.parseInt(contractConstants.recordCount()));
    }
    public LevelTaskPanel(EWindowType windowType,FloatNavigationPanel pnl) {
        super(windowType);
        navigationPanel = pnl;
        addBtn();
        tbPaging.setPageSize(Integer.parseInt(contractConstants.recordCount()));
    }
    
    private void addBtn() {
       btnDoAnswer = new Button(contractConstants.btnDoAnswer(), AbstractImagePrototype.create(commonImages.msg_rotate()));
        btnDoAnswer.setItemId("doAnswer");
        btnDoAnswer.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                onAnswer();
            }
        });
        
        menuDoAnswer = new MenuItem(contractConstants.btnDoAnswer());
        menuDoAnswer.setIcon(AbstractImagePrototype.create(commonImages.msg_rotate()));
        menuDoAnswer.setEnabled(true);
        menuDoAnswer.setVisible(true);
        menuDoAnswer.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onAnswer();
            }
        });
        menu.add(menuDoAnswer);
        
        btnCancelClose = new Button(contractConstants.btnCancelCloser(), AbstractImagePrototype.create(commonImages.msg_close()));
        btnCancelClose.setEnabled(true);
        btnCancelClose.setVisible(true);
        btnCancelClose.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                onCancelClose();
            }
        });
        
        
        menuCancelClose = new MenuItem(contractConstants.btnCancelCloser());
        menuCancelClose.setIcon(AbstractImagePrototype.create(commonImages.msg_close()));
        menuCancelClose.setEnabled(true);
        menuCancelClose.setVisible(true);
        menuCancelClose.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                onCancelClose();
            }
        });
        menu.add(menuCancelClose);
        
        
        toolBar.remove(fillTool);
        toolBar.remove(sprFiltr);
        toolBar.remove(btnFiltr);

        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDoAnswer);
        toolBar.add(btnCancelClose);
        
        toolBar.add(fillTool);
        toolBar.add(sprFiltr);
        toolBar.add(btnFiltr);
        
        doAnswerListener = new DoAnswerListener() {
            @Override
            public void onDoAnswer() {
                onAnswer();
            }
        };
        
        doCancelCloseListener = new DoCancelCloseListener() {
            @Override
            public void onDoCancelClose() {
                onCancelClose();
            }
        };
        
    }

    protected void onAnswer() {
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }
        currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
        final LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        Integer taskState = domain.getTaskState();
        if (taskState.equals(ETaskState.CREATE.getTaskState()) || taskState.equals(ETaskState.IN_WORK.getTaskState())) {
            domain.setTaskState(ETaskState.CANCEL.getTaskState());
        } else if (taskState.equals(ETaskState.EXECUTED.getTaskState())) {
            domain.setTaskState(ETaskState.CLOSE.getTaskState());
        }
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
                    reloadGrid();
                    LevelTask domain = new LevelTask();
                                               
                  
                    String    sR = "\n----------------------------------"
                                + "\nДата: " + DateTimeFormat.getFormat(commonConstants.dateDateTime()).format(t.getEventTime())
                                + "\nКому: " + t.getUserNameFrom()
                                + "\n" + t.getUserNameTo() + " пишет:"
                                + "\n" + ( ( t.getReply() != null ) ? t.getReply().toString():"" );

                    
                   
                    String    sD = "\n----------------------------------"
                                + "\nДата: " + DateTimeFormat.getFormat(commonConstants.dateDateTime()).format(t.getCreateDate())
                                + "\nКому: " + t.getUserNameTo()
                                + "\n" + t.getUserNameFrom() + " пишет:"
                                + "\n" + ( ( t.getDescription() != null ) ? t.getDescription().toString():"" ); 
                    
                    String descript = sR + sD;
                    domain.setDescription(descript);
                    domain.setUserIdTo(t.getUserIdTo());
                    domain.setUserNameTo(t.getUserNameTo());
                    LevelTaskEditPanel window = new LevelTaskEditPanel(domain, EditState.NEW, windowType, navigationPanel);
                    window.addDomainSaveSuccesedListener(saveDomainListener);
                    window.addWindowHideListener(windowHideListener);
                    window.show();
                    firePanelChangeEvent(panelChangeEventType, window);
                }
            }
        });
    }

    protected void onCancelClose() {
         if (grid.getSelectionModel() == null && grid.getStore() == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }

        List<BeanModel> beanModels = grid.getSelectionModel().getSelectedItems();
        if (beanModels == null) {
            MessageBox.info(commonConstants.attantion(), levelTaskConstants.notSelectMsg(), null);
            return;
        }

        List<Integer> ida = new ArrayList<Integer>();
        List<Integer> idc = new ArrayList<Integer>();
        for (BeanModel model : beanModels) {
            LevelTask domain = model.getBean();
            Integer taskState = domain.getTaskState();

            final Integer id = domain.getId();
            if (id == null) {
                continue;
            }
            if (taskState.equals(ETaskState.CREATE.getTaskState()) || taskState.equals(ETaskState.IN_WORK.getTaskState()) || taskState.equals(ETaskState.IN_CREATE.getTaskState()))   {
                ida.add(id);
                //domain.setTaskState(ETaskState.CANCEL.getTaskState());
            } else if (taskState.equals(ETaskState.EXECUTED.getTaskState())) {
                idc.add(id);
                //domain.setTaskState(ETaskState.CLOSE.getTaskState());
            }
        }

        if (ida.isEmpty() && idc.isEmpty()) {
                MessageBox.info(commonConstants.attantion(), levelTaskConstants.allClose(), null);
                return;
        }

        final String strCount = String.valueOf(ida.size() + idc.size());     
        service.sendCancelClose( ida, idc, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                //MessageBox.info(commonConstants.attantion(), "Обработано " + strCount + " сообщени(е/й).", null);
                reloadGrid();
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        });

        
        /*
        
        if (grid.getSelectionModel() == null && grid.getStore() == null) {
            return; //TODO здесь можно выдать сообщение что строка не выделена редактировать нечего
        }
        currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
        final LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        Integer taskState = domain.getTaskState();
        if (taskState.equals(ETaskState.CREATE.getTaskState()) || taskState.equals(ETaskState.IN_WORK.getTaskState())) {
            domain.setTaskState(ETaskState.CANCEL.getTaskState());
        } else if (taskState.equals(ETaskState.EXECUTED.getTaskState())) {
            domain.setTaskState(ETaskState.CLOSE.getTaskState());
        }
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
                    reloadGrid();
                }
            }
        });*/
    }

    @Override
    protected void createHeader() {
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        setHeading(constants.header());
    }

    @Override
    protected PanelFiltrSimple createPanelFiltr() {
        return new LevelTaskFiltr(this);
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

        btnAdd.setVisible(true);
        menuItemAdd.setVisible(true);

        btnEdit.setVisible(false);
        menuItemEdit.setVisible(false);

        btnDelete.setVisible(false);
        menuItemDelete.setVisible(false);
        
        if (ConfigurationManager.isModuleAvailable("adminfield")) {

            btnEdit.setVisible(true);
            menuItemEdit.setVisible(true);

            btnDelete.setVisible(true);
            menuItemDelete.setVisible(true);
        }
        
        btnView.setVisible(true);
        menuItemView.setVisible(true);
               
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
        if (currentId != null) {
            domain.setCurrentId(Integer.valueOf(currentId));
        }
        LevelTaskEditPanel window = new LevelTaskEditPanel(domain, EditState.NEW, windowType,navigationPanel);
        window.addDomainSaveSuccesedListener(saveDomainListener);
        window.addWindowHideListener(windowHideListener);       
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
        LevelTaskEditPanel window = new LevelTaskEditPanel(domain, EditState.EDIT, windowType,navigationPanel);
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
        LevelTaskEditPanel window = new LevelTaskEditPanel(domain, EditState.VIEW, windowType,navigationPanel);
        
        window.addDomainSaveSuccesedListener(saveDomainListener);
        window.addWindowHideListener(windowHideListener);
        window.addDoAnswerListener(doAnswerListener);
        window.addDoCancelCloseListener(doCancelCloseListener);                  
        window.show();
        firePanelChangeEvent(panelChangeEventType, window);        
    }

    @Override
    public void loadData(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BeanModel>> callback) {     
          //если передана панель с управляющими кнопками, то всегда при перечитке убираем мигания если они были
        if (navigationPanel != null) {
            navigationPanel.undoAattentionOut();
        }
        
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        
        getCondition().getFilters().clear();
        getCondition().getFilters().put("userIdFrom", ConfigurationManager.getUserId().intValue());
        //getCondition().getFilters().put("taskState", ETaskState.CLOSE.getTaskState());
        
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

        GridCellRenderer<BeanModel> renderBoldDateTime = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                LevelTask d = model.getBean();
                Date createDate = d.getCreateDate();
                String txt = DateTimeFormat.getFormat(commonConstants.dateDateTime()).format(createDate);
                if (d.getTaskState() == ETaskState.EXECUTED.getTaskState()) {
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
                if (d.getTaskState() == ETaskState.EXECUTED.getTaskState()) {
                    return "<b>" + String.valueOf(model.get(property, "")) + "</b>";
                } else {
                    return model.get(property);
                }
            }
        };
        
         
          GridCellRenderer<BeanModel> renderPriority = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                LevelTask d = model.getBean();
                String txt = "";
                if (d.getPriority().equals(1)) {
                    txt = "Да";
                }
                if (!txt.isEmpty() && d.getTaskState() == ETaskState.EXECUTED.getTaskState()) {
                    txt = "<b>" + txt + "</b>";
                }
                return txt;
            }
        };
        //getGridColumnsConfig().add(new GridColumnConfig("id", constants.columnId(), 100, true, HorizontalAlignment.LEFT));
        //getGridColumnsConfig().add(new GridColumnConfig("userIdFrom", constants.columnUserFrom(), 100, true, HorizontalAlignment.LEFT));
        //getGridColumnsConfig().add(new GridColumnConfig("userIdTo", constants.columnUserTo(), 100, true, HorizontalAlignment.LEFT));
        
        cbMultilineColumn = new CheckBoxSelectionModel<BeanModel>();
        
        cbMultilineColumn.setSelectionMode(Style.SelectionMode.MULTI);
        getGridColumnsConfig().add(cbMultilineColumn.getColumn());
          
        getGridColumnsConfig().add(new GridColumnConfig("createDate", constants.columnCreateDate(), 60, true, HorizontalAlignment.LEFT,renderBoldDateTime));
        getGridColumnsConfig().add(new GridColumnConfig("taskName", constants.columnTaskName(), 60, true, HorizontalAlignment.LEFT,renderBold));
        
        getGridColumnsConfig().add(new GridColumnConfig("userNameTo", constants.columnUserTo(), 60, true, HorizontalAlignment.LEFT,renderBold));
        getGridColumnsConfig().add(new GridColumnConfig("stateName", constants.columnTaskState(), 50, true, HorizontalAlignment.LEFT,renderBold));
        //getGridColumnsConfig().add(new GridColumnConfig("userNameFrom", constants.columnUserFrom(), 100, true, HorizontalAlignment.LEFT));
        getGridColumnsConfig().add(new GridColumnConfig("priority", constants.columnPriorityName(), 40, true, HorizontalAlignment.LEFT,renderPriority));
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
            //grid.getStore().insert(model, grid.getStore().getCount());
            grid.getStore().insert(model, 0);
            grid.getSelectionModel().select(0, true);
            tbPaging.first();
        }
         
    }

    
    
    protected GWTLevelTaskServiceAsync getService() {
        return service;
    }
    
    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();
        
        if (grid.getSelectionModel().getSelectedItem() == null) {
            btnDoAnswer.setEnabled(false);
            menuDoAnswer.setEnabled(false);
            return;
        }
        LevelTask domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (domain == null || domain.getTaskState() == null) {
            btnDoAnswer.setEnabled(false);
            menuDoAnswer.setEnabled(false);
            return;
        }

        if (domain.getTaskState().equals(ETaskState.EXECUTED.getTaskState()) && grid.getSelectionModel().getSelection().size() == 1) {
            btnDoAnswer.setEnabled(true);
            menuDoAnswer.setEnabled(true);
        } else {
            btnDoAnswer.setEnabled(false);
            menuDoAnswer.setEnabled(false);
        }
    }
    
    

   
}
