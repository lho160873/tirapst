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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class DataGridPanelOrderBlankWork5 extends DataGridPanel {

    private Button btnCsv, btnIsCh, btnIsAll, btnDelIs;
    private SeparatorToolItem sprCsv, sprIsCh;
    private MenuItem menuItemCsv, menuItemIsCh, menuItemIsAll;
    private int currentIndex;
    private Boolean isBtn = false;
    private LoadListener newLoadListener;

    public DataGridPanelOrderBlankWork5(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOrderBlankWork5(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOrderBlankWork5(String tableName) {
        super(tableName);
    }

    public DataGridPanelOrderBlankWork5(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOrderBlankWork5(DTable table, String tableName, SRelationInfo relationInfo) {
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
        ToolBar tb = super.createToolBar();
        
        //создаем новае кнопки
        sprIsCh = new SeparatorToolItem();
        sprIsCh.setVisible(false);
        tb.add(sprIsCh);
        btnIsCh = new Button(datagridConstants.btnBCh());
        btnIsCh.setIconAlign(Style.IconAlign.LEFT);
        btnIsCh.setEnabled(false);
        btnIsCh.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setIssue();
            }
        });

        btnDelIs = new Button(datagridConstants.btnBDel());
        btnDelIs.setIconAlign(Style.IconAlign.LEFT);
        btnDelIs.setEnabled(false);
        btnDelIs.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                delIssue();
            }
        });

        tb.remove(btnEdit);
        tb.remove(btnDelete);
        tb.remove(btnView);
        tb.remove(sprSend);
        tb.remove(btnSend);
        tb.remove(sprSub);
        tb.remove(btnSub);
        tb.remove(sprReport);
        tb.remove(btnReportSplit);
        tb.remove(ftiFiltr);
        tb.remove(sprColumnsVisibility);
        tb.remove(btnColumnsVisibility);
        tb.remove(sprColumnsVisibilityDel);
        tb.remove(btnColumnsVisibilityDel);       
        
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);
        menu.remove(menuItemEdit);
        menu.remove(menuItemDelete);
        menu.remove(menuItemView);
        
        tb.add(sprSub);
        tb.add(btnSub);
        //вставляем новые кнопки
        tb.add(btnIsCh);
        tb.add(btnDelIs);                       

        tb.add(sprReport);
        tb.add(btnReportSplit);
        
        tb.add(ftiFiltr); 
        
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);        
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        enabledBtn();

        return tb;
       }


    private void setIssue() {
        if (  !ConfigurationManager.getPropertyAsBoolean("issue") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на согласование!", null);
            return;
        }
        
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

        String proc = "exec [dbo].[ORDER_BLANK_WORK] " + getInsertString(domain);
        getService().execProc(proc, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    MessageBox.alert("Внимание!", "Уже согласовано", null);
                }
                reloadGrid();
            }
        });

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

        String sKeyFactLabour = "MAIN:ALL_FACT_LABOUR";
        key = new SKeyForColumn(sKeyFactLabour);
        val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyFactLabour = val.getVal().toString();
            } else {
                sKeyFactLabour = "NULL";
            }
        } else {
            sKeyFactLabour = "NULL";
        }

        String sKeyFactCost = "MAIN:ALL_FACT_COST";
        key = new SKeyForColumn(sKeyFactCost);
        val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyFactCost = val.getVal().toString();
            } else {
                sKeyFactCost = "NULL";
            }
        } else {
            sKeyFactCost = "NULL";
        }

        String sKeyFactDate = "MAIN:FACT_DATE";
        key = new SKeyForColumn(sKeyFactDate);
        val = domain.getRows().get(key);
        if (val != null) {
            if (val.getVal() != null) {
                sKeyFactDate = "'" + DateTimeFormat.getFormat(format).format((Date) val.getVal()) + "'";
            } else {
                sKeyFactDate = "NULL";
            }
        } else {
            sKeyFactDate = "NULL";
        }

        String result = sKeyWorkPlanId + ", "
                + sKeyUserId + ", 5, "
                + sKeyPlanLabour + ", "
                + sKeyPlanCost + ", "
                + sKeyBegDate + ", "
                + sKeyEndDate + ", "
                + sKeyFactLabour + ", "
                + sKeyFactCost + ", "
                + sKeyFactDate;

        return result;
    }

    private void delIssue() {
        if (  !ConfigurationManager.getPropertyAsBoolean("delIssue") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на отмену согласования!", null);
            return;
        }
        
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

        String sKeyWorkPlanId = "MAIN:WORK_PLAN_ID";
        SKeyForColumn key = new SKeyForColumn(sKeyWorkPlanId);
        IRowColumnVal val = domain.getRows().get(key);
        sKeyWorkPlanId = val.getVal().toString();

        String sKeyUserId = ConfigurationManager.getUserId().toString();

        String proc = "exec [dbo].[ORDER_BLANK_WORK_DEL] 'Согласование отменено', " + sKeyUserId + ", " + sKeyWorkPlanId + ", 5";
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

    }
   
    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();
        btnIsCh.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        //btnIsAll.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnDelIs.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
    }

    @Override
    protected void showReport(final String reportType) {

        HashMap<SKeyForColumn, IRowColumnVal> searches = condition.getSearches();

        String format = "yyyyMMdd HH:mm:ss";

        String departId,departId_1;
        if (searches.get(new SKeyForColumn("DEPART2:DEPART_ID")) == null||searches.get(new SKeyForColumn("DEPART1:DEPART_ID")) == null) {
            MessageBox.info("Внимание!", "Бланк заказ формируется для определенных подразделений. Выберите \"Головное подразделение\" и \"Подразделение исполнитель\" и нажмите кнопку \"Поиск\"", null);
            return;
        }

        if ( searches.get(new SKeyForColumn("DEPART2:DEPART_ID")).getVal() == null || searches.get(new SKeyForColumn("DEPART1:DEPART_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "Бланк заказ формируется для определенных подразделений. Выберите \"Головное подразделение\" и \"Подразделение исполнитель\" и нажмите кнопку \"Поиск\"", null);
            return;
        }
        departId = searches.get(new SKeyForColumn("DEPART2:DEPART_ID")).getVal().toString();
        departId_1 = searches.get(new SKeyForColumn("DEPART1:DEPART_ID")).getVal().toString();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("departId", departId);
        params.put("departId_1", departId_1);

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

        //params.put("userId", ConfigurationManager.getUserId().toString());

        getReportService().generateJasperReport("OrderBlankWork5", reportType, params, condition, table, new FileReportServiceCallback(this));

    }
}
