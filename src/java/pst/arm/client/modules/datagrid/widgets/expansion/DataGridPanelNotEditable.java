package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
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
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelNotEditable extends DataGridPanel {


    public DataGridPanelNotEditable(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        initBtn();
    }

    public DataGridPanelNotEditable(DTable table, String tn) {
        super(table, tn);
        initBtn();
    }

    public DataGridPanelNotEditable(String tableName) {
        super(tableName);
        initBtn();
    }

    public DataGridPanelNotEditable(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        initBtn();
    }

    public DataGridPanelNotEditable(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        initBtn();
    }
    private void initBtn() {
        isDictAdd = false;
        isDictEdit = false;
        isDictDelete = false;
        isDictView = false;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);

        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);

        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);

        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);

        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        sprEdit.setVisible(false);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        updateToolBar();
    }
    
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
        
        Button btnSummary = new Button("Итоги");
        btnSummary.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnSummary.setIconAlign(Style.IconAlign.LEFT);
        btnSummary.setEnabled(true);
        btnSummary.setVisible(true);

        MenuItem itemPdfSummary = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdfSummary.setId("0");
        itemPdfSummary.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("PDF", 0);
            }
        });
        MenuItem itemRtfSummary = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtfSummary.setId("1");
        itemRtfSummary.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("RTF", 0);
            }
        });

        MenuItem itemXlsSummary = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXlsSummary.setId("2");
        itemXlsSummary.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("XLS", 0);
            }
        });
        
        Menu reportMenuSummary = new Menu();
        reportMenuSummary.insert(itemXlsSummary, 0);
        reportMenuSummary.insert(itemRtfSummary, 0);
        reportMenuSummary.insert(itemPdfSummary, 0);

        
        btnSummary.setMenu(reportMenuSummary);
        tb.add(btnSummary);
        
        
        Button btnSummary2 = new Button("Итоги по этапам");
        btnSummary2.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnSummary2.setIconAlign(Style.IconAlign.LEFT);
        btnSummary2.setEnabled(true);
        btnSummary2.setVisible(true);

        MenuItem itemPdfSummary2 = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdfSummary2.setId("0");
        itemPdfSummary2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("PDF", 2);
            }
        });
        MenuItem itemRtfSummary2 = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtfSummary2.setId("1");
        itemRtfSummary2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("RTF", 2);
            }
        });

        MenuItem itemXlsSummary2 = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXlsSummary2.setId("2");
        itemXlsSummary2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("XLS", 2);
            }
        });
        
        Menu reportMenuSummary2 = new Menu();
        reportMenuSummary2.insert(itemXlsSummary2, 0);
        reportMenuSummary2.insert(itemRtfSummary2, 0);
        reportMenuSummary2.insert(itemPdfSummary2, 0);

        
        btnSummary2.setMenu(reportMenuSummary2);
        tb.add(btnSummary2);
        
        
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
                showSummary("PDF", 0);
            }
        });


        MenuItem itemRtf = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtf.setId("1");
        itemRtf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("RTF", 0);
            }
        });

        MenuItem itemXls = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXls.setId("2");
        itemXls.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showSummary("XLS", 0);
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
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

        enabledBtn();
        
        return tb;
    }
    
    protected void showSummary (String reportType, int version) {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (domain == null) {
            MessageBox.info("Внимание!", "Этап не выбран!", null);
            return;
        }

        Integer departId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal());
        Integer monthNum = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:MONTH_NUM")).getVal());
        Integer yearNum = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:YEAR_NUM")).getVal());
        String reportTemplate = version==2?"PlanFactNiokrPlanWorks2":"PlanFactNiokrPlanWorks";
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("departId", departId);        
        params.put("monthNum", monthNum);
        params.put("yearNum", yearNum);
        getReportService().generateJasperReport(reportTemplate, reportType, params, condition, table, new FileReportServiceCallback(this));
    }
}
