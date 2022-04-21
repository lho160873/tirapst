package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyDateField;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.ganttchart.portable.GanttChartPortablePanel;

public class DataGridPanelWorkPlanExecutor extends DataGridPanel {

    protected Date planBegDate;
    protected Date planBegEnd;

    public DataGridPanelWorkPlanExecutor(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        btnView.setVisible(false);
    }

    public DataGridPanelWorkPlanExecutor(DTable table, String tn) {
        super(table, tn);
        btnView.setVisible(false);
    }

    public DataGridPanelWorkPlanExecutor(String tableName) {
        super(tableName);
        btnView.setVisible(false);
    }

    public DataGridPanelWorkPlanExecutor(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        btnView.setVisible(false);
    }

    public DataGridPanelWorkPlanExecutor(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        btnView.setVisible(false);
    }

    @Override
    protected void onAdd() {
        DDataGrid domain = createDomain();

        if (condition.getFilters().size() > 0) {
            for (Map.Entry filtr : condition.getFilters().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) filtr.getKey();
                IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (isOpenAsSub && passedFields != null) {
            for (Map.Entry passField : passedFields.entrySet()) {
                SKeyForColumn key = (SKeyForColumn) passField.getKey();
                IRowColumnVal val = (IRowColumnVal) passField.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (condition.getValDefault().size() > 0) {
            for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) defaultVal.getKey();
                IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                domain.getRows().put(key, val);
            }
        }

        checkDates(domain);

        List<DCondition> locConditions = new ArrayList<DCondition>();

        String strVal2 = domain.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_ID")).getStringFromVal(new SKeyForColumn("MAIN:WORK_PLAN_ID"), table.getColumnBuilder(new SKeyForColumn("MAIN:WORK_PLAN_ID")));
        DCondition cnd2 = new DCondition(new SKeyForColumn("MAIN:WORK_PLAN_ID"), strVal2);
        locConditions.add(cnd2);

        DataGridEditWindow window = new DataGridEditWindowWorkPlanExecutor(domain, table, EditState.NEW, EWindowType.EWT_WINDOW, locConditions, planBegDate, planBegEnd);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    private void checkDates(DDataGrid domain) {
        SKeyForColumn begKey = new SKeyForColumn("MAIN:BEG_DATE");
        SKeyForColumn endKey = new SKeyForColumn("MAIN:END_DATE");

        DColumnPropertyDateField datePropBegin = (DColumnPropertyDateField) table.getColumnBuilder(begKey).getColumn(
                begKey).getColumnProperty();
        DColumnPropertyDateField datePropEnd = (DColumnPropertyDateField) table.getColumnBuilder(endKey).getColumn(
                endKey).getColumnProperty();

        planBegDate = (Date) domain.getRows().get(begKey).getFormatVal(begKey, table.getColumnBuilder(begKey));
        planBegEnd = (Date) domain.getRows().get(endKey).getFormatVal(endKey, table.getColumnBuilder(endKey));
        datePropBegin.setMinDate(planBegDate);
        datePropBegin.setMaxDate(planBegEnd);
        datePropEnd.setMinDate(planBegDate);
        datePropEnd.setMaxDate(planBegEnd);
    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DDataGrid domainForDates = createDomain();
        if (isOpenAsSub && passedFields != null) {
            for (Map.Entry passField : passedFields.entrySet()) {
                SKeyForColumn key = (SKeyForColumn) passField.getKey();
                IRowColumnVal val = (IRowColumnVal) passField.getValue();
                domainForDates.getRows().put(key, val);
            }
        }
        checkDates(domainForDates);

        List<DCondition> locConditions = new ArrayList<DCondition>();

        String strVal = domain.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getStringFromVal(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"), table.getColumnBuilder(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")));
        DCondition cnd = new DCondition(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"), strVal);
        locConditions.add(cnd);

        String strVal2 = domain.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_ID")).getStringFromVal(new SKeyForColumn("MAIN:WORK_PLAN_ID"), table.getColumnBuilder(new SKeyForColumn("MAIN:WORK_PLAN_ID")));
        DCondition cnd2 = new DCondition(new SKeyForColumn("MAIN:WORK_PLAN_ID"), strVal2);
        locConditions.add(cnd2);

        DataGridEditWindow window = new DataGridEditWindowWorkPlanExecutor(domain, table, EditState.EDIT, EWindowType.EWT_WINDOW, locConditions, planBegDate, planBegEnd);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
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
                } else if (ce.getButton() == btnRefr) {
                    reloadGrid();
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

                if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null && (((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean() == domain)) {
                    grid.getStore().update(model);
                    grid.getSelectionModel().select(model, true);
                    setAllSubDomain(domain);
                } else {
                    grid.getStore().insert(model, grid.getStore().getCount());
                    grid.getSelectionModel().select(model, true);
                }
                reloadGrid();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }

    @Override
    public void selectCurrentRow() {
        List<BeanModel> models = grid.getStore().getModels();
        Boolean wasFound = false;
        if (currentDomain == null) {
            grid.getSelectionModel().select(0, false);
        } else if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                for (BeanModel bm : models) {
                    DDataGrid dg = bm.getBean();
                    if (currentDomain.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal().equals(dg.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getVal())) {
                        grid.getSelectionModel().select(models.indexOf(bm), false);
                        wasFound = true;
                        break;
                    }
                }
                if (!wasFound) {
                    grid.getSelectionModel().select(0, false);
                }
            }
        } else {
            setAllSubDomain(null);
        }
    }

    @Override
    protected void onDelete() {
        MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    final BeanModel model = (BeanModel) grid.getSelectionModel().getSelectedItem();
                    DDataGrid domain = model.getBean();
                    String strVal = domain.getRows().get(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")).getStringFromVal(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID"), table.getColumnBuilder(new SKeyForColumn("MAIN:WORK_PLAN_EXECUTOR_ID")));

//                    final List<DDataGrid> domains = new ArrayList<DDataGrid>();
//                    domains.add(domain);

                    String proc = "EXEC dbo.WORK_PLAN_EXECUTOR_DEL " + strVal;

//                    final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
//                        @Override
//                        public void onFailure(Throwable caught) {
//                            AppHelper.showMsgRpcServiceError(caught);
//                            panelGrid.unmask();
//                        }
//
//                        @Override
//                        public void onSuccess(Boolean result) {
//                            grid.getStore().remove(model);
//                            tbPaging.refresh();
//                            selectCurrentRow();
//                        }
//                    };
                    panelGrid.mask();
                    getService().execProc(proc, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                            panelGrid.unmask();
                        }

                        @Override
                        public void onSuccess(Boolean result) {
//                            getService().deleteDomains(tableName, domains, callback);
                            grid.getStore().remove(model);
                            tbPaging.refresh();
                            selectCurrentRow();
                        }
                    });

                }
            }
        });
    }
    
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();
        
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
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(false); //изначально делаем не видимой btnFiltr
        
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
        
        final Button btnDG = new Button("Диаграмма Ганта");
//        btnDG.setVisible(false);

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
                            GanttChartPortablePanel gcw = new GanttChartPortablePanel(data, GanttChartPortablePanel.WORK_PLAN_EXECUTOR);
                            gcw.show();
                        }
                    });
                }
            }
        });
                
        tb.add(btnDG);
        

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
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();

        enabledBtn();
        
        return tb;
    }

}
