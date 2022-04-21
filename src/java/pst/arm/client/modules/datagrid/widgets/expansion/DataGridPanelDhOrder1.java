package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.*;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDT;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDTAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author wesStyle
 */
public class DataGridPanelDhOrder1 extends DataGridPanel {
    private final GWTDDataGridServiceDHDTAsync dhdtService = GWT.create(GWTDDataGridServiceDHDT.class); //сервис отвечающий за обработку данных
    private Button btnLoad, btnUpdate, btnPlanSave;
    private Boolean isLoadUpdate=null;

    public DataGridPanelDhOrder1(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDhOrder1(String tableName) {
        super(tableName);
    }

    public DataGridPanelDhOrder1(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDhOrder1(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    public DataGridPanelDhOrder1(DTable table, String tn) {
        super(table, tn);
        page = new DataGridWindow("DH_ORDER_PRIBOY_HLV", true, true);
    }


    //очистка данных
    @Override
    public void clearStore() {
        btnLoad.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnAdd.setEnabled(false);
        btnRefr.setEnabled(false);
        btnFiltr.setEnabled(false);
        btnDelete.setEnabled(false);
        menuItemAdd.setEnabled(false);
        menuItemRefr.setEnabled(false);
        menuItemDelete.setEnabled(false);
        store.removeAll();
    }

    @Override
    public void reloadGrid() {
        panelGrid.mask(datagridConstants.treeLoading());
        if (isOpenAsSub && domainForSub == null) {
            btnLoad.setEnabled(false);
            btnAdd.setEnabled(false);
            btnRefr.setEnabled(false);
            btnFiltr.setEnabled(false);
            btnDelete.setEnabled(false);
            menuItemAdd.setEnabled(false);
            menuItemRefr.setEnabled(false);
            menuItemDelete.setEnabled(false);            
            store.removeAll();
            panelGrid.unmask();
            return;
        }
        if (isOpenAsSub && domainForSub != null) {
            btnLoad.setEnabled(true);
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnRefr.setEnabled(true);
            btnFiltr.setEnabled(true);
            btnDelete.setEnabled(false);
            menuItemAdd.setEnabled(false);
            menuItemRefr.setEnabled(true);
            menuItemDelete.setEnabled(false);  
        }
        if (grid == null) {
            return;
        }

        PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        //config.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        config.setLimit(table.getRecordCount());
        config.setSortField("");
        if (grid.isViewReady()) {
            Map<String, Object> state = grid.getState();
            if (state.containsKey("offset")) {
                int offset = (Integer) state.get("offset");
                int limit = (Integer) state.get("limit");
                config.setOffset(offset);
                config.setLimit(limit);
            }
            if (state.containsKey("sortField")) {
                config.setSortField((String) state.get("sortField"));
                config.setSortDir(Style.SortDir.valueOf((String) state.get("sortDir")));
            }

            if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null) {
                currentDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            }
        }
        loader.addLoadListener(ll);
        loader.load(config);
    }

    //private Button btnAddFromContract;
    LoadListener ll = new LoadListener() {
        @Override
        public void loaderLoad(LoadEvent le) {
            panelGrid.unmask();
        }
    };

    //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();


        btnLoad = new Button("Загрузить новый заказ");
        btnLoad.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnLoad.setIconAlign(Style.IconAlign.LEFT);
        btnLoad.addSelectionListener(tbListener);

        btnUpdate = new Button("Обновить данные по заказу");
        btnUpdate.setIcon(AbstractImagePrototype.create(commonImages.sync()));
        btnUpdate.setIconAlign(Style.IconAlign.LEFT);
        btnUpdate.addSelectionListener(tbListener);

        btnPlanSave = new Button("Сохранить план"); //Расчитать затраты
        btnPlanSave.setIconAlign(Style.IconAlign.LEFT);
        btnPlanSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onPlanSave();
            }
        });

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAdd();
            }
        });

        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(tbListener);
        menuItemDelete = new MenuItem(commonConstants.delete());
        menuItemDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        menuItemDelete.setEnabled(false);
        menuItemDelete.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDelete();
            }
        });

        btnEdit = new Button(commonConstants.edit());
        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnEdit.setIconAlign(Style.IconAlign.LEFT);
        btnEdit.setEnabled(false);
        btnEdit.addSelectionListener(tbListener);
        menuItemEdit = new MenuItem(commonConstants.edit());
        menuItemEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        menuItemEdit.setEnabled(false);
        menuItemEdit.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onEdit();
            }
        });

        btnView = new Button(commonConstants.view());
        btnView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnView.setIconAlign(Style.IconAlign.LEFT);
        btnView.setEnabled(false);
        btnView.addSelectionListener(tbListener);
        menuItemView = new MenuItem(commonConstants.view());
        menuItemView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuItemView.setEnabled(false);
        menuItemView.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onView();
            }
        });
        
        btnFiltr = new Button(); 
        //btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        //btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(false); //изначально делаем не видимойbtnFiltr

        btnSend = new Button(datagridConstants.btnSend());
        btnSend.setIcon(AbstractImagePrototype.create(datagridImages.check()));
        btnSend.setIconAlign(Style.IconAlign.LEFT);
        btnSend.setEnabled(false);
        btnSend.setVisible(false);
        btnSend.addSelectionListener(tbListener);


        btnSub = new Button(datagridConstants.btnSub());
        btnSub.setIcon(AbstractImagePrototype.create(datagridImages.form()));
        btnSub.setIconAlign(Style.IconAlign.LEFT);
        btnSub.setEnabled(false);
        btnSub.setVisible(false);
        btnSub.addSelectionListener(tbListener);


        btnRefr = new Button(datagridConstants.btnRefr());
        btnRefr.setIcon(AbstractImagePrototype.create(datagridImages.reset()));
        btnRefr.setIconAlign(Style.IconAlign.LEFT);
        btnRefr.setEnabled(true);
        btnRefr.setVisible(true);
        btnRefr.addSelectionListener(tbListener);
        menuItemRefr = new MenuItem(commonConstants.reset());
        menuItemRefr.setIcon(AbstractImagePrototype.create(commonImages.reset()));
        menuItemRefr.setEnabled(true);
        menuItemRefr.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                reloadGrid();
            }
        });

        btnReport = new Button(datagridConstants.btnReport());
        btnReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReport.setIconAlign(Style.IconAlign.LEFT);
        btnReport.setEnabled(true);
        btnReport.setVisible(false);
        btnReport.addSelectionListener(tbListener);
        menuItemReport = new MenuItem(datagridConstants.btnReport());
        menuItemReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        menuItemReport.setEnabled(true);
        menuItemReport.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
//                showReport();
            }
        });

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка пров доступа
        tb.add(sprEdit);
        tb.add(btnAdd);
        tb.add(btnLoad);
        tb.add(btnUpdate);
        tb.add(btnEdit);
        tb.add(btnDelete);
        tb.add(btnView);

        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        tb.add(sprSend);
        tb.add(btnSend);

        sprSub = new SeparatorToolItem();
        sprSub.setVisible(false);
        tb.add(sprSub);
        tb.add(btnSub);

        tb.add(new SeparatorToolItem());
        tb.add(btnPlanSave);

        sprReport = new SeparatorToolItem();
        sprReport.setVisible(false);
        tb.add(sprReport);
        //Кнопка для выбора типа сформированного отчета (PDF, DOC или XLS)
        btnReportSplit = new Button(datagridConstants.btnReport());//"Сформировать отчёт")
        btnReportSplit.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSplit.setIconAlign(Style.IconAlign.LEFT);
        btnReportSplit.setEnabled(true);
        btnReportSplit.setVisible(true);

        MenuItem itemPdf = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdf.setId("0");
        itemPdf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("PDF");
            }
        });


        MenuItem itemRtf = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtf.setId("1");
        itemRtf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("RTF");
            }
        });

        MenuItem itemXls = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXls.setId("2");
        itemXls.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("XLS");
            }
        });

        reportMenuSplit = new Menu();
        reportMenuSplit.insert(itemXls, 0);
        reportMenuSplit.insert(itemRtf, 0);
        reportMenuSplit.insert(itemPdf, 0);


        btnReportSplit.setMenu(reportMenuSplit);

        // Добавляем split-кнопку для формирования разных типов отчетов
        tb.add(btnReportSplit);

        ftiFiltr = new FillToolItem();
        tb.add(ftiFiltr);
        
        btnColumnsVisibility = new Button();
        btnColumnsVisibility.setToolTip(commonConstants.columnsVisibility());
        btnColumnsVisibility.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibility()));
        btnColumnsVisibility.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibility.addSelectionListener(tbListener);
        sprColumnsVisibility = new SeparatorToolItem();
        sprColumnsVisibility.setVisible(true);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        
        btnColumnsVisibilityDel = new Button();
        btnColumnsVisibilityDel.setToolTip(commonConstants.columnsVisibilityDel());
        btnColumnsVisibilityDel.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibilityDel()));
        btnColumnsVisibilityDel.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibilityDel.addSelectionListener(tbListener);
        sprColumnsVisibilityDel = new SeparatorToolItem();
        sprColumnsVisibilityDel.setVisible(true);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);

        btnHelpManual = new Button();
        btnHelpManual.setToolTip(datagridConstants.btnHelpManual());
        btnHelpManual.setIcon(AbstractImagePrototype.create(datagridImages.helpmanual()));
        btnHelpManual.setIconAlign(Style.IconAlign.LEFT);
        btnHelpManual.setEnabled(true);
        btnHelpManual.addSelectionListener(tbListener);
        btnHelpManual.setVisible(false); //изначально делаем не видимой

        sprHelpManual = new SeparatorToolItem();
        sprHelpManual.setVisible(false);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(false);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        menu = new Menu();
        menu.add(menuItemRefr);
        sprMenuItemEdit = new SeparatorMenuItem();
        //menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemEdit);
        //menu.add(menuItemDelete);
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

        enabledBtn();

        return tb;
    }

    public void enabledBtn() {
        //настройка пров доступа
        super.enabledBtn();
       
        String datagridName = tableName.toUpperCase();
        isDictAdd = false;
        isDictEdit=false;
        isLoadUpdate = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName));
        isSprDictEdit = (isLoadUpdate ||isDictAdd || isDictEdit || isDictDelete || isDictView);
        
        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);
        


        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);


        boolean isPlanSave = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictPlanSave")
                || ConfigurationManager.getPropertyAsBoolean("dictPlanSave_" + datagridName));
        btnPlanSave.setVisible(isPlanSave);


        boolean isLoad = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictLoad")
                || ConfigurationManager.getPropertyAsBoolean("dictLoad_" + datagridName));

        btnLoad.setVisible(isLoad);

        boolean isUpdate = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictUpdate")
                || ConfigurationManager.getPropertyAsBoolean("dictUpdate_" + datagridName));
        btnUpdate.setVisible(isUpdate);

        boolean isDelete = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName));
        btnDelete.setVisible(isDelete);

        updateToolBar();
    }

    @Override
    public void updateToolBar() {
        super.updateToolBar();
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(isLoadUpdate, btnLoad);
        removeOrAddBtnOnToolBar(isLoadUpdate, btnUpdate);
    }
            
    protected void gridSelectionChanged() {

        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        if (isOpenAsSub) {
            btnLoad.setEnabled(domainForSub != null);
            btnUpdate.setEnabled(domainForSub != null);
            btnAdd.setEnabled(domainForSub != null);
            btnRefr.setEnabled(domainForSub != null);
            btnFiltr.setEnabled(domainForSub != null);
            menuItemAdd.setEnabled(domainForSub != null);
            menuItemRefr.setEnabled(domainForSub != null);
        }

        menuItemDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSub.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSend.setEnabled(isBtnSendEnabled && grid.getSelectionModel().getSelection().size() == 1);

        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        fireDataGridEvent(DataGridEvents.DataGridSelectionChanged, domain, table);

        setAllSubDomain(domain);
    }

    protected void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    onAdd();
                } else if (ce.getButton() == btnColumnsVisibility) {
                    saveColumnsVisibility();
                }  else if (ce.getButton() == btnColumnsVisibilityDel) {
                    delColumnsVisibility();
                } else if (ce.getButton() == btnLoad) {
                    onBtnLoad();
                } else if (ce.getButton() == btnUpdate) {
                    onBtnUpdate();
                } else if (ce.getButton() == btnRefr) {
                    //updateTableInfo();//hlv
                    reloadGrid();
                } else if (ce.getButton() == btnReport) {
//                    showReport();
                } else if (ce.getButton() == btnEdit) {
                    onEdit();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                } else if (ce.getButton() == btnDelete) {
                    onDelete();
                }
                else if (ce.getButton() == btnHelpManual) {
                    showHelpManual();
                }
            }
        };

        listenerGetDomain = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(final DataGridEvent be) {
                page.hide();

                DataGridSearchCondition condition = new DataGridSearchCondition();
                DRowColumnValNumber oId = new DRowColumnValNumber();
                oId.setVal((Integer)be.getDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal());
                DRowColumnValNumber intId = new DRowColumnValNumber();
                intId.setVal(4);

                condition.getFilters().put(new SKeyForColumn("MAIN:id"), oId);
                condition.getFilters().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), intId);

                getService().getDataGrid("DH_ORDER_1_FULL_VO", condition, new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Integer orderId = (Integer)be.getDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
                        dhdtService.updateJobs(orderId, new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("good")) {
                                    MessageBox.alert("", "Успех", null);
                                    reloadGrid();
                                }
                                else
                                    MessageBox.alert("",result, null);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(List<DDataGrid> result) {
                        if (result.size() > 0)
                            MessageBox.confirm("Предупреждение", "Данные по выбранному заказу уже загружены. Если для заказа есть проект плана, он будет удален. Хотите обновить?", new Listener<MessageBoxEvent>() {
                                @Override
                                public void handleEvent(MessageBoxEvent b) {
                                    if (b.getButtonClicked() == b.getDialog().getButtonById(Dialog.YES)) {
                                        Integer orderId = (Integer)be.getDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
                                        dhdtService.updateJobs(orderId, new AsyncCallback<String>() {
                                            @Override
                                            public void onFailure(Throwable caught) {
                                                AppHelper.showMsgRpcServiceError(caught);
                                            }

                                            @Override
                                            public void onSuccess(String result) {
                                                panelGrid.unmask();
                                                if (result.equals("good")) {
                                                    MessageBox.alert("", "Успех", null);
                                                    reloadGrid();
                                                }
                                                else
                                                    MessageBox.alert("",result, null);
                                            }
                                        });
                                        panelGrid.mask("Загрузка. При большом количестве работ может занять много времени");
                                    }
                                }
                            });
                        else {
                            Integer orderId = (Integer)be.getDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
                            dhdtService.updateJobs(orderId, new AsyncCallback<String>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    AppHelper.showMsgRpcServiceError(caught);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    panelGrid.unmask();
                                    if (result.equals("good")) {
                                        MessageBox.alert("", "Успех", null);
                                        reloadGrid();
                                    }
                                    else
                                        MessageBox.alert("",result, null);
                                }
                            });
                            panelGrid.mask("Загрузка. При большом количестве работ может занять много времени");
                        }
                    }
                });
            }
        };

        saveDataGridListener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid domain) {
                BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(DDataGrid.class);
                ModelData model = factory.createModel(domain);
                if (grid.getStore() == null) {
                    return;
                }
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
                reloadGrid();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }

    private Listener<DataGridEvent> listenerGetDomain;
    private DataGridWindow page = null;

    private void onBtnLoad() {
        page.addDataGridListener(listenerGetDomain);
        page.show();
    }

    private void onBtnUpdate() {
        MessageBox.confirm("Предупреждение", "Если для заказа есть проект плана, он будет удален. Хотите обновить?", new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent b) {
                if (b.getButtonClicked() == b.getDialog().getButtonById(Dialog.YES)) {

                    DataGridSearchCondition condition = new DataGridSearchCondition();
                    DRowColumnValNumber oId = new DRowColumnValNumber();
                    oId.setVal((Integer)getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal());
                    condition.getFilters().put(new SKeyForColumn("MAIN:ID"), oId);

                    getService().getDataGrid("DH_ORDER_FULL_VO", condition, new AsyncCallback<List<DDataGrid>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            MessageBox.alert("Сообщение","Заказ в Pmasc не найден. Обновление невозможно", null);
                        }

                        @Override
                        public void onSuccess(List<DDataGrid> result) {
                            Integer orderId = (Integer)getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
                            dhdtService.updateJobs(orderId, new AsyncCallback<String>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    AppHelper.showMsgRpcServiceError(caught);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    panelGrid.unmask();
                                    if (result.equals("good")) {
                                        MessageBox.alert("", "Успех", null);
                                        reloadGrid();
                                    }
                                    else
                                        MessageBox.alert("",result, null);
                                }
                            });
                            panelGrid.mask("Загрузка. При большом количестве работ может занять много времени");
                        }
                    });
                }
            }
        });
    }

    private void onPlanSave() {
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        DDataGrid curr = getSelectedDomain();
        cnd.getFilters().put(new SKeyForColumn("MAIN:id_order"), curr.getRows().get(new SKeyForColumn("MAIN:ID")));
        cnd.getFilters().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), curr.getRows().get(new SKeyForColumn("MAIN:INTERACTING_SYST_ID")));
        getService().getDataGrid("DH_JOB_1_FOR_ORDER1_VO", cnd, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("Внимание!", "Планирование заказа не выполнено. Сохранять нечего", null);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                if (result.size() > 0) {
                    String sql = "exec [dbo].[DH_ORDER_SAVE_PLAN] ";
                    sql += getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:ID")).getVal().toString();
                    sql += ", ";
                    sql += getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:INTERACTING_SYST_ID")).getVal().toString();
                    sql += ", ";
                    sql += "'{db}'";

                    getService().execProc(sql,"dataSourcePriboy", new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught,"Ошибка сохранения");
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            MessageBox.alert("Внимание!", "Успех", null);
                        }
                    });
                }
                else
                    onFailure(new Exception());
            }
        });
    }
}