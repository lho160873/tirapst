package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridPanelOrderBlankWork extends DataGridPanel {

    private Button btnCsv, btnIsCh, btnIsAll, btnDelIs;
    private SeparatorToolItem sprCsv, sprIsCh;
    private MenuItem menuItemCsv, menuItemIsCh, menuItemIsAll;
    private int currentIndex;
    private Boolean isBtn = false;
    private LoadListener newLoadListener;

    public DataGridPanelOrderBlankWork(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOrderBlankWork(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOrderBlankWork(String tableName) {
        super(tableName);
    }

    public DataGridPanelOrderBlankWork(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOrderBlankWork(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
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
                    if (currentDomain.getRows().get(new SKeyForColumn("MAIN:DEP_EX_PLAN_ID")).getVal().equals(dg.getRows().get(new SKeyForColumn("MAIN:DEP_EX_PLAN_ID")).getVal())) {
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
    public void reloadGrid() {
        isBtn = false;
        super.reloadGrid();
//        if (isBtn) {
//                    grid.getSelectionModel().select(currentIndex, false);
//                } else {
//                    selectCurrentRow();
//                }

    }

    @Override
    protected void initStore() {
        super.initStore();
        newLoadListener = new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                panelGrid.unmask();
            }

            @Override
            public void handleEvent(LoadEvent le) {
                super.handleEvent(le);
                resizePanelFiltr();
                if (isBtn) {
                    grid.getSelectionModel().select(currentIndex, false);
                } else {
                    selectCurrentRow();
                }
            }

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                if (grid.getSelectionModel() != null) {
                    currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                super.loaderBeforeLoad(le);
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                panelGrid.unmask();
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }
        };
        loader.removeLoadListener(mainLoadListener);
        loader.addLoadListener(newLoadListener);
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        isDictAdd = false;
        isDictDelete = false;
        isDictEdit = false;
        isDictView = false;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);

        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);
        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);
        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        updateToolBar();
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

//        btnCsv = new Button(datagridConstants.btnCsv());
//        btnCsv.setIcon(AbstractImagePrototype.create(commonImages.view()));
//        btnCsv.setIconAlign(Style.IconAlign.LEFT);
//        btnCsv.addSelectionListener(new SelectionListener<ButtonEvent>() {
//            @Override
//            public void componentSelected(ButtonEvent ce) {
//                showReport("CSV");
//            }
//        });
//        menuItemCsv = new MenuItem(datagridConstants.btnCsv());
//        menuItemCsv.setIcon(AbstractImagePrototype.create(commonImages.view()));
//        menuItemCsv.setEnabled(true);
//        menuItemCsv.addSelectionListener(new SelectionListener<MenuEvent>() {
//            @Override
//            public void componentSelected(MenuEvent ce) {
//                showReport("CSV");
//            }
//        });


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

        sprCsv = new SeparatorToolItem();
        sprCsv.setVisible(false);
        tb.add(sprCsv);
//        tb.add(btnCsv);
        sprIsCh = new SeparatorToolItem();
        sprIsCh.setVisible(false);
        tb.add(sprIsCh);
        btnIsCh = new Button(datagridConstants.btnIsCh());
        btnIsCh.setIconAlign(Style.IconAlign.LEFT);
        btnIsCh.setEnabled(false);
        btnIsCh.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setIssue();
            }
        });
        btnIsAll = new Button(datagridConstants.btnIsAll());
        btnIsAll.setIconAlign(Style.IconAlign.LEFT);
        btnIsAll.setEnabled(false);
        btnIsAll.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setIssueAll();
            }
        });
        tb.add(btnIsAll);
        tb.add(btnIsCh);
        btnDelIs = new Button(datagridConstants.btnIsDel());
        btnDelIs.setIconAlign(Style.IconAlign.LEFT);
        btnDelIs.setEnabled(false);
        btnDelIs.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                delIssue();
            }
        });
        tb.add(btnDelIs);


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
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
//        menu.add(menuItemCsv);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);


//        menuItemIsCh = new MenuItem(datagridConstants.btnIsCh());
//        menuItemIsCh.setIcon(AbstractImagePrototype.create(commonImages.view()));
//        menuItemIsCh.setEnabled(true);
//        menuItemIsCh.addSelectionListener(new SelectionListener<MenuEvent>() {
//            @Override
//            public void componentSelected(MenuEvent ce) {
//                setIssue();
//            }
//        });
//        menu.add(menuItemIsCh);
        enabledBtn();
        return tb;
    }

    private void setIssue() {

        isBtn = true;
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        List<BeanModel> models = grid.getStore().getModels();
        if (grid.getSelectionModel() != null) {
            for (BeanModel bm : models) {
                DDataGrid dg = bm.getBean();
                if (dg.isDomainEquals(domain)) {
                    currentIndex = models.indexOf(bm);
                    break;
                }
            }
        }

//        SKeyForColumn key = new SKeyForColumn("AGREEMENT1:WORK_PLAN_ID");
//        if (domain.getRows().get(key).getVal() != null) {
//            MessageBox.alert("Внимание!", "Работа уже выдана", null);
//            return;
//        }

        String proc = "exec [dbo].[ORDER_BLANK_WORK] " + getInsertString(domain);
        getService().execProc(proc, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    MessageBox.alert("Внимание!", "Работа уже выдана", null);
                }
                reloadGrid();
            }
        });

//        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                AppHelper.showMsgRpcServiceError(caught);
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                reloadGrid();
//            }
//        };
//
//        String sql = "INSERT INTO AGREEMENT (WORK_PLAN_ID, USER_ID, DATE_ACT, FORM_ORDER_ACT_TYPE_ID, "
//                + "IS_ACTUAL, PLAN_LABOUR, PLAN_COST, BEG_DATE1, END_DATE1) VALUES"
//                + getInsertString(domain) + ";";
//
//        getService().execProc(sql, callback);

    }

    private String getInsertString(DDataGrid domain) {

        String sKeyWorkPlanId = "MAIN:WORK_PLAN_ID";
        SKeyForColumn key = new SKeyForColumn(sKeyWorkPlanId);
        IRowColumnVal val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyWorkPlanId = val.getVal().toString();
            } else {
                sKeyWorkPlanId = "NULL";
            }
        } else {
            sKeyWorkPlanId = "NULL";
        }

        String sKeyUserId = ConfigurationManager.getUserId().toString();

        String format = "yyyyMMdd HH:mm:ss";

        String sKeyPlanLabour = "MAIN:PLAN_LABOUR";
        key = new SKeyForColumn(sKeyPlanLabour);
        val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyPlanLabour = val.getVal().toString();
            } else {
                sKeyPlanLabour = "NULL";
            }
        } else {
            sKeyPlanLabour = "NULL";
        }

        String sKeyPlanCost = "MAIN:PLAN_COST";
        key = new SKeyForColumn(sKeyPlanCost);
        val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyPlanCost = val.getVal().toString();
            } else {
                sKeyPlanCost = "NULL";
            }
        } else {
            sKeyPlanCost = "NULL";
        }

        String sKeyBegDate = "MAIN:BEG_DATE";
        key = new SKeyForColumn(sKeyBegDate);
        val = domain.getRows().get(key);
        sKeyBegDate = "'" + DateTimeFormat.getFormat(format).format((Date) val.getVal()) + "'";


        String sKeyEndDate = "MAIN:END_DATE";
        key = new SKeyForColumn(sKeyEndDate);
        val = domain.getRows().get(key);
        sKeyEndDate = "'" + DateTimeFormat.getFormat(format).format((Date) val.getVal()) + "'";

//        String result = "("
//                + sKeyWorkPlanId + ", "
//                + sKeyUserId + ", GETDATE(), 1, 1, "
//                + sKeyPlanLabour + ", "
//                + sKeyPlanCost + ", "
//                + sKeyBegDate + ", "
//                + sKeyEndDate + ")";

        String result = sKeyWorkPlanId + ", "
                + sKeyUserId + ", 1, "
                + sKeyPlanLabour + ", "
                + sKeyPlanCost + ", "
                + sKeyBegDate + ", "
                + sKeyEndDate + ", NULL, NULL, NULL";

        return result;
    }

    private void delIssue() {

        //Определение индекса текущей строки

        isBtn = true;

        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        List<BeanModel> models = grid.getStore().getModels();
        if (grid.getSelectionModel() != null) {
            for (BeanModel bm : models) {
                DDataGrid dg = bm.getBean();
                if (dg.isDomainEquals(domain)) {
                    currentIndex = models.indexOf(bm);
                    break;
                }
            }
        }

//        SKeyForColumn key = new SKeyForColumn("AGREEMENT1:WORK_PLAN_ID");
//
//        if (domain.getRows().get(key).getVal() == null) {
//            return;
//        }
        String sKeyWorkPlanId = "MAIN:WORK_PLAN_ID";
        SKeyForColumn key = new SKeyForColumn(sKeyWorkPlanId);
        IRowColumnVal val = domain.getRows().get(key);
        sKeyWorkPlanId = val.getVal().toString();

        String sKeyUserId = ConfigurationManager.getUserId().toString();
        
        String proc = "exec [dbo].[ORDER_BLANK_WORK_DEL] 'Выдача отозвана', " + sKeyUserId + ", " + sKeyWorkPlanId + ", 1";
        getService().execProc(proc, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Boolean result) {              
                reloadGrid();
            }
        });

//        String sql = "UPDATE AGREEMENT SET COMMENT='Выдача отозвана',"
//                + " IS_ACTUAL=0, DATE_DEL=GETDATE(), DEL_USER_ID=" + sKeyUserId
//                + " WHERE AGREEMENT_ID=" + sKeyAgreementId + ";";
//
//        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                AppHelper.showMsgRpcServiceError(caught);
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                reloadGrid();
//            }
//        };
//        getService().execProc(sql, callback);
    }

    private void setIssueAll() {

        isBtn = true;
//
//        String sql = "INSERT INTO AGREEMENT (WORK_PLAN_ID, USER_ID, DATE_ACT, FORM_ORDER_ACT_TYPE_ID, "
//                + "IS_ACTUAL, PLAN_LABOUR, PLAN_COST, BEG_DATE1, END_DATE1) VALUES";
//            SKeyForColumn key = new SKeyForColumn("AGREEMENT1:WORK_PLAN_ID");

//            if (dg.getRows().get(key).getVal() == null) {
//                sql += getInsertString(dg) + ",";
//            }

        List<BeanModel> models = grid.getStore().getModels();

        int size = models.size();
        int count = 1;
        for (BeanModel bm : models) {

            DDataGrid dg = bm.getBean();

            String proc = "exec [dbo].[ORDER_BLANK_WORK] " + getInsertString(dg);
            if (count == size) {
                getService().execProc(proc, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        reloadGrid();
                    }
                });
            } else {
                getService().execProc(proc, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                    }
                });
            }

            ++count;
        }

//        sql = sql.substring(0, sql.length() - 1) + ";";
//
//        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                AppHelper.showMsgRpcServiceError(caught);
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                reloadGrid();
//            }
//        };
//        getService().execProc(sql, callback);
    }

    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();
        btnIsCh.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnIsAll.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnDelIs.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
    }

    @Override
    protected void showReport(final String reportType) {

        HashMap<SKeyForColumn, IRowColumnVal> searches = condition.getSearches();

        String format = "yyyyMMdd HH:mm:ss";

        String departId;
        if (searches.get(new SKeyForColumn("DEPART2:DEPART_ID")) == null) {
            MessageBox.info("Внимание!", "Бланк заказ формируется для определенного подразделения. Выберите подразделение и нажмите кнопку \"Поиск\"", null);
            return;
        }

        if (searches.get(new SKeyForColumn("DEPART2:DEPART_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "Бланк заказ формируется для определенного подразделения. Выберите подразделение и нажмите кнопку \"Поиск\"", null);
            return;
        }
        departId = searches.get(new SKeyForColumn("DEPART2:DEPART_ID")).getVal().toString();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("departId", departId);
        if (searches.get(new SKeyForColumn("ORDER_D:ORDER_ID")) != null) {
            if (searches.get(new SKeyForColumn("ORDER_D:ORDER_ID")).getVal() != null) {
                params.put("orderId", searches.get(new SKeyForColumn("ORDER_D:ORDER_ID")).getVal().toString());
            } else {
                params.put("orderId", null);
            }
        } else {
            params.put("orderId", null);
        }
        if (searches.get(new SKeyForColumn("MAIN:BEG_DATE_TO")) != null) {
            if (searches.get(new SKeyForColumn("MAIN:BEG_DATE_TO")).getVal() != null) {
                params.put("dateTo", DateTimeFormat.getFormat(format).format((Date) searches.get(new SKeyForColumn("MAIN:BEG_DATE_TO")).getVal()).substring(0, 9)+ "23:59:59");
            } else {
                params.put("dateTo", null);
            }
        } else {
            params.put("dateTo", null);
        }
        if (searches.get(new SKeyForColumn("MAIN:BEG_DATE_FROM")) != null) {
            if (searches.get(new SKeyForColumn("MAIN:BEG_DATE_FROM")).getVal() != null) {
                params.put("dateFrom", DateTimeFormat.getFormat(format).format((Date) searches.get(new SKeyForColumn("MAIN:BEG_DATE_FROM")).getVal()).substring(0, 9)+ "00:00:00");
            } else {
                params.put("dateFrom", null);
            }
        } else {
            params.put("dateFrom", null);
        }

        params.put("userId", ConfigurationManager.getUserId().toString());
//        for (String k : params.keySet()) {
//            MessageBox.alert(k, (params.get(k) == null) ? "null" : params.get(k).toString(), null);
//        }

        getReportService().generateJasperReport("OrderBlankWork", reportType, params, condition, table, new FileReportServiceCallback(this));

    }
}
