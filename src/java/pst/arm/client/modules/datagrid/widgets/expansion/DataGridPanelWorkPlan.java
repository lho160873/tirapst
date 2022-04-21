package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelWithSummary;
import pst.arm.client.modules.datagrid.widgets.DataGridWindowPanel;
import pst.arm.client.modules.ganttchart.portable.GanttChartPortablePanel;

/**
 * @author wesStyle
 */
public class DataGridPanelWorkPlan extends DataGridPanel {

    private Button btnAddFromCopy, btnSummary;
    private MenuItem menuAddFromCopy;
    private Boolean isWindowOpened = false;
    public Boolean isEmpty = false;
//    private ListStore ganttChartStore;
//    private ListLoader ganttChartLoader;
//    private RpcProxy<List<DDataGrid>> ganttChartProxy;
    private Timer t;

    public DataGridPanelWorkPlan(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelWorkPlan(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelWorkPlan(String tableName) {
        super(tableName);
    }

    public DataGridPanelWorkPlan(final String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);

//        ganttChartProxy = new RpcProxy<List<DDataGrid>>() {
//            @Override
//            protected void load(Object loadConfig, AsyncCallback<List<DDataGrid>> callback) {
//                getService().getDataGrid(tableName, condition, callback);
//            }
//        };
//        BeanModelReader ganttChartReader = new BeanModelReader();
//        if (ganttChartReader != null) {
//            ganttChartLoader = new BaseListLoader<ListLoadResult<ModelData>>(ganttChartProxy, ganttChartReader);
//            ganttChartStore = new ListStore<BeanModel>(ganttChartLoader);
//        } else {
//            ganttChartLoader = new BaseListLoader<ListLoadResult<ModelData>>(ganttChartProxy);
//            ganttChartStore = new ListStore<ModelData>(ganttChartLoader);
//        }
//        ganttChartLoader.addLoadListener(new LoadListener() {
//            @Override
//            public void loaderLoad(LoadEvent le) {
////                            super.loaderLoad(le);
//                GanttChartPortablePanel gcw = new GanttChartPortablePanel(ganttChartStore);
//                gcw.show();
//            }
//        });
//        ganttChartLoader.load();
//        ganttChartLoader.setRemoteSort(true);
    }

    public DataGridPanelWorkPlan(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    //очистка данных
    @Override
    public void clearStore() {
        btnAddFromCopy.setEnabled(false);
        btnSummary.setEnabled(false);
        btnAdd.setEnabled(false);
        btnRefr.setEnabled(false);
        btnFiltr.setEnabled(false);
        menuItemAdd.setEnabled(false);
        menuItemRefr.setEnabled(false);
        store.removeAll();
//        ganttChartStore.removeAll();
    }

    //перезагрузка данных
    @Override
    public void reloadGrid() {
        panelGrid.mask(datagridConstants.treeLoading());
        if (isOpenAsSub && domainForSub == null) {
            btnAdd.setEnabled(false);
            btnRefr.setEnabled(false);
            btnFiltr.setEnabled(false);
            menuItemAdd.setEnabled(false);
            menuItemRefr.setEnabled(false);
            btnAddFromCopy.setEnabled(false);
            btnSummary.setEnabled(false);
            store.removeAll();
//            ganttChartStore.removeAll();
//            store2.removeAll();
            panelGrid.unmask();
            return;
        }
        if (isOpenAsSub && domainForSub != null) {
            btnAdd.setEnabled(true);

            if (store.getCount() == 0) {
                btnAddFromCopy.setEnabled(false);
                menuAddFromCopy.setEnabled(false);
            } else {
                btnAddFromCopy.setEnabled(true);
                menuAddFromCopy.setEnabled(true);
            }

            btnSummary.setEnabled(true);
            btnRefr.setEnabled(true);
            btnFiltr.setEnabled(true);
            menuItemAdd.setEnabled(true);
            menuItemRefr.setEnabled(true);
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
            if (grid.getSelectionModel() != null) {
                currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
            }
        }
        loader.addLoadListener(ll);
        loader.load(config);

        PagingLoadConfig config2 = new BasePagingLoadConfig();
        config2.setOffset(0);
//        config2.setLimit(Integer.parseInt(datagridConstants.recordCount()));
        config2.setLimit(Integer.MAX_VALUE);
        config2.setSortField("");
        if (grid.isViewReady()) {
            Map<String, Object> state2 = grid.getState();
            if (state2.containsKey("offset")) {
//            int offset = (Integer) state2.get("offset");
//            int limit = (Integer) state2.get("limit");
//            config2.setOffset(offset);
//            config2.setLimit(limit);
            }
            if (state2.containsKey("sortField")) {
                config2.setSortField((String) state2.get("sortField"));
                config2.setSortDir(Style.SortDir.valueOf((String) state2.get("sortDir")));
            }

            if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null) {
                currentDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            }
        }
//        loader2.addLoadListener(ll);
//        loader2.load(config2);
//        ganttChartLoader.load();
    }
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

        btnSummary = new Button(commonConstants.summary());
        btnSummary.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnSummary.setIconAlign(Style.IconAlign.LEFT);
        btnSummary.addSelectionListener(tbListener);

        btnAddFromCopy = new Button(datagridConstants.btnAddFromCopy());
        btnAddFromCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAddFromCopy.setIconAlign(Style.IconAlign.LEFT);
        btnAddFromCopy.addSelectionListener(tbListener);
        menuAddFromCopy = new MenuItem(datagridConstants.btnAddFromCopy());
        menuAddFromCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuAddFromCopy.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAddFromCopy();
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
        tb.add(btnAddFromCopy);
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

        tb.add(sprSub);
        tb.add(btnSummary);

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

        tb.add(new SeparatorToolItem());

        final Button btnDG = new Button("Диаграмма Ганта");

        btnDG.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnDG) {
//                    ganttChartLoader.load();

                    getService().getDataGrid(tableName, condition, new AsyncCallback<List<DDataGrid>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                             AppHelper.showMsgRpcServiceError(caught,"Ошибка получения данных для построения диаграммы");
                        }

                        @Override
                        public void onSuccess(final List<DDataGrid> data) {
                            GanttChartPortablePanel gcw = new GanttChartPortablePanel(data, GanttChartPortablePanel.WORK_PLAN);
                            gcw.show();
                        }
                    });
                }
            }
        });

        final Button btnDG2 = new Button("Диаграмма Ганта по подразделению");
        btnDG2.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnDG2) {
                    MessageBox.alert("Тест", "Тест " + " ss", null);

                }
            }
        });
        tb.add(btnDG);
        //tb.add(btnDG2);

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
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuAddFromCopy);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

        enabledBtn();

        return tb;
    }

    @Override
    public void enabledBtn() {
        //настройка прав доступа
        super.enabledBtn();
        
        isDictView = true;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
         
        btnAddFromCopy.setVisible(isDictAdd);
        
        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);        
        
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
      
        updateToolBar();
    }

    @Override
    protected void gridSelectionChanged() {

        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        if (isOpenAsSub) {
            btnAddFromCopy.setEnabled(domainForSub != null);
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

        if (store.getCount() == 0) {
            btnAddFromCopy.setEnabled(false);
            menuAddFromCopy.setEnabled(false);
        } else {
            btnAddFromCopy.setEnabled(true);
            menuAddFromCopy.setEnabled(true);
        }

        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        fireDataGridEvent(DataGridEvents.DataGridSelectionChanged, domain, table);

        setAllSubDomain(domain);
    }

    @Override
    protected void onAdd() {

        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String) result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer) result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            if (dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal().equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) {
                            isValid = uid == userId;
                        }


                        if (isValid) {

                            // start

                            final AsyncCallback<List<DDataGrid>> callback_getSCS = new AsyncCallback<List<DDataGrid>>() {
                                @Override
                                public void onSuccess(final List<DDataGrid> result1) {
                                    if (result1.size() > 0) {
                                        final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
                                            @Override
                                            public void onSuccess(List<DDataGrid> result) {
                                                isWindowOpened = false;
                                                DDataGrid domain = new DDataGrid();

                                                SKeyForColumn nk = new SKeyForColumn("MAIN:AVERAGE_SALARY");
                                                final Double valnk = (Double) result.get(0).getRows().get(nk).getVal();
                                                final IRowColumnVal ival = new IRowColumnVal();
                                                ival.setVal(valnk);
                                                DDataGrid ns = new DDataGrid();
                                                ns.getRows().put(nk, ival);

                                                if (condition.getFilters().size() > 0) {
                                                    //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                                                    domain.setName(table.getQueryName());
                                                    for (Map.Entry filtr : condition.getFilters().entrySet()) {
                                                        SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                                                        //SKeyForColumn val = (SKeyForColumn) key.getValue();
                                                        IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                                                        domain.getRows().put(key, val);
                                                    }
                                                }

                                                if (condition.getValDefault().size() > 0) {
                                                    //если есть значения по умолчанию задаем их
                                                    //domain.setName(table.getQueryName());
                                                    for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                                                        SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
                                                        //SKeyForColumn val = (SKeyForColumn) key.getValue();
                                                        IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                                                        domain.getRows().put(key, val);
                                                    }
                                                }
                                                domain.getRows().put(new SKeyForColumn("MAIN:CONTRACT_STAGE_ID"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:CONTRACT_STAGE_ID"))); //hlv - код тоже подтягиваем
                                                domain.getRows().put(new SKeyForColumn("SCS:STAGE_NUMBER"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:STAGE_NUMBER")));
                                                //domain.getRows().put(new SKeyForColumn("MAIN:WORK_NAME"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:DESCRIPTION")));
                                                domain.getRows().put(new SKeyForColumn("MAIN:BEG_DATE"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:BEG_DATE")));
                                                domain.getRows().put(new SKeyForColumn("MAIN:END_DATE"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:END_DATE")));

                                                String strVal = domain.getRows().get(new SKeyForColumn("SCS:OCP_ID")).getStringFromVal(new SKeyForColumn("SCS:OCP_ID"), table.getColumnBuilder(new SKeyForColumn("SCS:OCP_ID")));
                                                DCondition cnd = new DCondition(new SKeyForColumn("MAIN:OCP_ID"), strVal);


                                                Integer cId = (Integer) passedFields.get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();
                                                DataGridEditWindow window = new DataGridEditWindowWorkPlan(cId, domain, ns, table, EditState.NEW, EWindowType.EWT_WINDOW, cnd, false);
                                                window.addDomainSaveSuccesedListener(saveDataGridListener);
                                                window.show();
                                            }

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                isWindowOpened = false;
                                            }
                                        };
                                        getService().getDataGrid("NORMATIVE_VALUES", new DataGridSearchCondition(), callback_getNorm);
                                    } else {
                                        MessageBox.alert("Внимание", "В «Ведомости исполнения» отсутствуют данные об этапах. Заполните «Ведомость исполнения»", null);
                                        isWindowOpened = false;
                                    }
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    isWindowOpened = false;
                                }
                            };

                            DataGridSearchCondition condition1 = new DataGridSearchCondition();
                            SKeyForColumn kk = new SKeyForColumn("MAIN:OCP_ID");
                            IRowColumnVal vv = condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID"));
                            condition1.getFilters().put(kk, vv);

                            if (!isWindowOpened) {
                                isWindowOpened = true;
                                getService().getDataGrid("SERV_CONTRACT_STAGE_VO", condition1, callback_getSCS);
                            }

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к "
                                    + userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                         AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal();

                DataGridSearchCondition cnd = new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd = new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    @Override
    protected void onEdit() {

        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String) result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer) result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            if (dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal().equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) {
                            isValid = uid == userId;
                        }


                        if (isValid) {

                            // start

                            DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

                            final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
                                @Override
                                public void onSuccess(List<DDataGrid> result) {
                                    isWindowOpened = false;
                                    SKeyForColumn nk = new SKeyForColumn("MAIN:AVERAGE_SALARY");
                                    final Double valnk = (Double) result.get(0).getRows().get(nk).getVal();
                                    final IRowColumnVal ival = new IRowColumnVal();
                                    ival.setVal(valnk);
                                    DDataGrid ns = new DDataGrid();
                                    ns.getRows().put(nk, ival);

                                    DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

                                    DDataGrid selectedDomain = new DDataGrid();
                                    if (condition.getFilters().size() > 0) {
                                        //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                                        selectedDomain.setName(table.getQueryName());
                                        for (Map.Entry filtr : condition.getFilters().entrySet()) {
                                            SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                                            IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                                            selectedDomain.getRows().put(key, val);
                                        }
                                    }

                                    String strVal = selectedDomain.getRows().get(new SKeyForColumn("SCS:OCP_ID")).getStringFromVal(new SKeyForColumn("SCS:OCP_ID"), table.getColumnBuilder(new SKeyForColumn("SCS:OCP_ID")));
                                    DCondition cnd = new DCondition(new SKeyForColumn("MAIN:OCP_ID"), strVal);

                                    Integer cId = (Integer) passedFields.get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();
                                    DataGridEditWindow window = new DataGridEditWindowWorkPlan(cId, domain, ns, table, EditState.EDIT, EWindowType.EWT_WINDOW, cnd, false);
                                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                                    window.show();
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    isWindowOpened = false;
                                }
                            };
                            if (!isWindowOpened) {
                                isWindowOpened = true;
                                getService().getDataGrid("NORMATIVE_VALUES", new DataGridSearchCondition(), callback_getNorm);
                            }

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к "
                                    + userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                         AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal();

                DataGridSearchCondition cnd = new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd = new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    protected void onDelete() {

        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String) result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer) result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            if (dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal().equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) {
                            isValid = uid == userId;
                        }


                        if (isValid) {

                            // start

                            MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                                        final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                                        List<DDataGrid> domains = new ArrayList<DDataGrid>();
                                        for (final BeanModel beanModel : selection) {
                                            DDataGrid domain = beanModel.getBean();
                                            domains.add(domain);
                                        }
                                        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                                            @Override
                                            public void onFailure(Throwable caught) {
                                                AppHelper.showMsgRpcServiceError(caught);
                                            }

                                            @Override
                                            public void onSuccess(Boolean result) {
                                                //reloadGrid();
                                                for (final BeanModel beanModel : selection) {
                                                    grid.getStore().remove(beanModel);
                                                }
//                                                loader2.load();
//                                                ganttChartLoader.load();
                                                selectCurrentRow();
                                            }
                                        };
                                        getService().deleteDomains(tableName, domains, callback);
                                    }
                                }
                            });

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к "
                                    + userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                         AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal();

                DataGridSearchCondition cnd = new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd = new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    protected void onSend() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        fireDataGridEvent(DataGridEvents.DataGridSend, domain, table);
    }

    protected void onAddFromCopy() {

        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String) result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer) result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            if (dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal().equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) {
                            isValid = uid == userId;
                        }


                        if (isValid) {

                            // start

                            final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
                                @Override
                                public void onSuccess(List<DDataGrid> result) {
                                    isWindowOpened = false;
                                    SKeyForColumn nk = new SKeyForColumn("MAIN:AVERAGE_SALARY");
                                    final Double valnk = (Double) result.get(0).getRows().get(nk).getVal();
                                    final IRowColumnVal ival = new IRowColumnVal();
                                    ival.setVal(valnk);
                                    DDataGrid ns = new DDataGrid();
                                    ns.getRows().put(nk, ival);

                                    DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

                                    DDataGrid selectedDomain = new DDataGrid();
                                    if (condition.getFilters().size() > 0) {
                                        //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                                        selectedDomain.setName(table.getQueryName());
                                        for (Map.Entry filtr : condition.getFilters().entrySet()) {
                                            SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                                            IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                                            selectedDomain.getRows().put(key, val);
                                        }
                                    }

                                    String strVal = selectedDomain.getRows().get(new SKeyForColumn("SCS:OCP_ID")).getStringFromVal(new SKeyForColumn("SCS:OCP_ID"), table.getColumnBuilder(new SKeyForColumn("SCS:OCP_ID")));
                                    DCondition cnd = new DCondition(new SKeyForColumn("MAIN:OCP_ID"), strVal);

                                    Integer cId = (Integer) passedFields.get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();
                                    DataGridEditWindow window = new DataGridEditWindowWorkPlan(cId, domain, ns, table, EditState.EDIT, EWindowType.EWT_WINDOW, cnd, true, "Добавление копированием");
                                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                                    window.show();
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    isWindowOpened = false;
                                }
                            };
                            if (!isWindowOpened) {
                                isWindowOpened = true;
                                getService().getDataGrid("NORMATIVE_VALUES", new DataGridSearchCondition(), callback_getNorm);
                            }

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к "
                                    + userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                         AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal();

                DataGridSearchCondition cnd = new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd = new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number) condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    private void showSummary() {

        //DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        //Integer ocpId =  (Integer)domain.getRows().get(new SKeyForColumn("SCS:OCP_ID")).getVal();

        HashMap<Integer, SKeyForColumn> map = new HashMap<Integer, SKeyForColumn>();
        map.put(3, new SKeyForColumn("MAIN:SUM_PLAN_LABOUR1"));
        map.put(4, new SKeyForColumn("MAIN:SUM_PLAN_COST1"));
        map.put(5, new SKeyForColumn("MAIN:SUM_PLAN_COST_NORM1"));

        DataGridPanelWithSummary summary = new DataGridPanelWithSummary("OCP_DEPART_SUM_VO", map, NumberFormat.getFormat("#,##0.00"), 2);
        IRowColumnVal v = new DRowColumnValNumber();
        v.setVal(condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        summary.getCondition().getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), v);

        String dateFrom, dateTo;

        if ( condition.getSearches().containsKey(new SKeyForColumn("MAIN:BEG_DATE_FROM"))) {
            summary.getCondition().getFunctionParams().put(0, condition.getSearches().get(new SKeyForColumn("MAIN:BEG_DATE_FROM")));
        }

        if ( condition.getSearches().containsKey(new SKeyForColumn("MAIN:BEG_DATE_TO"))) {
            summary.getCondition().getFunctionParams().put(1, condition.getSearches().get(new SKeyForColumn("MAIN:BEG_DATE_TO")));
        }


        DataGridWindowPanel window = new DataGridWindowPanel(summary, "Итоги");
        window.show();
    }

    @Override
    protected void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    onAdd();
                } else if (ce.getButton() == btnColumnsVisibility) {
                    saveColumnsVisibility();
                } else if (ce.getButton() == btnColumnsVisibilityDel) {
                    delColumnsVisibility();
                } else if (ce.getButton() == btnAddFromCopy) {
                    onAddFromCopy();
                } else if (ce.getButton() == btnRefr) {
                    //updateTableInfo();//hlv
                    reloadGrid();
                } else if (ce.getButton() == btnReport) {
//                    showReport();
                } else if (ce.getButton() == btnSummary) {
                    showSummary();
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
                } else if (ce.getButton() == btnHelpManual) {
                    showHelpManual();
                }
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
}
