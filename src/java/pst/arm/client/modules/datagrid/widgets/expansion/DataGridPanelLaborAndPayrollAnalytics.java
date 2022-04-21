package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.Date;
import java.util.HashMap;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

public class DataGridPanelLaborAndPayrollAnalytics extends DataGridPanel {
    
    
    protected Button btnReportSub;
    protected Menu subReportMenu;

    public DataGridPanelLaborAndPayrollAnalytics(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelLaborAndPayrollAnalytics(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelLaborAndPayrollAnalytics(String tableName) {
        super(tableName);
    }

    public DataGridPanelLaborAndPayrollAnalytics(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelLaborAndPayrollAnalytics(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
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
        
        
        btnReportSub = new Button("Итоги по предприятию/подразделению");//"Сформировать ОКП")
        btnReportSub.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSub.setIconAlign(Style.IconAlign.LEFT);
        subReportMenu = new Menu();
        
        MenuItem editItem = new MenuItem("Общие итоги");//"ОКП для включения работы в План предприятия");
        editItem.setId("1");
        editItem.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                getReport("");
            }
        });
        
        MenuItem editItem2 = new MenuItem("Итоги в разрезе источников финансирования (в рублях)");//"ОКП для включения работы в План предприятия");
        editItem2.setId("2");
        editItem2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                getReport("2");
            }
        });

        MenuItem editItem3 = new MenuItem("Итоги в разрезе источников финансирования (в чел/мес)");//"ОКП работы, финансируемой за счет средств предприятия");
        editItem3.setId("3");
        editItem3.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                getReport("3");
            }
        });
        
        MenuItem editItem4 = new MenuItem("Итоги в разрезе реализации (в рублях)");//"ОКП для включения работы в План предприятия");
        editItem4.setId("4");
        editItem4.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                getReport("");
            }
        });

        //subReportMenu.insert(editItem4, 0);
        subReportMenu.insert(editItem3, 0);
        subReportMenu.insert(editItem2, 0);
        subReportMenu.insert(editItem, 0);

        btnReportSub.setMenu(subReportMenu);
        tb.add(btnReportSub);

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
//        menu.add(sprMenuItemEdit);
//        menu.add(menuItemAdd);
//        menu.add(menuItemEdit);
//        menu.add(menuItemDelete);
//        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        
        enabledBtn();
        
        return tb;
    }
    
    @Override
    public void updateToolBar() {
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(false, btnAdd);
        removeOrAddBtnOnToolBar(false, btnEdit);
        removeOrAddBtnOnToolBar(false, btnDelete);
        removeOrAddBtnOnToolBar(false, btnView);
        removeOrAddBtnOnToolBar(false, sprEdit);

    }
    
    private void getReport(String ver) {

        HashMap<SKeyForColumn, IRowColumnVal> searches = condition.getSearches();

        String format = "yyyyMMdd HH:mm:ss";

        String companyId;
        if (searches.get(new SKeyForColumn("COMPANY:COMPANY_ID_CB")) == null) {
            MessageBox.info("Внимание!", "Выберите предприятие ГПК и нажмите кнопку \"Поиск\"", null);
            return;
        }

        if (searches.get(new SKeyForColumn("COMPANY:COMPANY_ID_CB")).getVal() == null) {
            MessageBox.info("Внимание!", "Выберите предприятие ГПК и нажмите кнопку \"Поиск\"", null);
            return;
        }
        companyId = searches.get(new SKeyForColumn("COMPANY:COMPANY_ID_CB")).getVal().toString();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        if (searches.get(new SKeyForColumn("DEPART:CODE2")) != null) {
            if (searches.get(new SKeyForColumn("DEPART:CODE2")).getVal() != null) {
                params.put("departCode", searches.get(new SKeyForColumn("DEPART:CODE2")).getVal().toString());
            } else {
                params.put("departCode", null);
            }
        } else {
            params.put("departCode", null);
        }
        if (searches.get(new SKeyForColumn("ORDER_D:ORDER_NUMBER")) != null) {
            if (searches.get(new SKeyForColumn("ORDER_D:ORDER_NUMBER")).getVal() != null) {
                params.put("orderNumber", searches.get(new SKeyForColumn("ORDER_D:ORDER_NUMBER")).getVal().toString());
            } else {
                params.put("orderNumber", null);
            }
        } else {
            params.put("orderNumber", null);
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
        getReportService().generateJasperReport("LaborAndPayrollAnalytics"+ver, "pdf", params, condition, table, new FileReportServiceCallback(this));

    }
}
