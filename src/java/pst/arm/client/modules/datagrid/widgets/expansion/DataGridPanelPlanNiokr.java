package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.Date;
import java.util.HashMap;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionService;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelPlanNiokr extends DataGridPanel {

    private final GWTDDataGridExpansionServiceAsync serviceExp = GWT.create(GWTDDataGridExpansionService.class); //сервис отвечающий за обработку данных
    private Button btnApproved, btnNotApproved, btnPlan, btnPlanApproved, btnDop1;
    protected SeparatorToolItem sprApproved, sprNotApproved;
    private Boolean isApproved=null,isNotApproved=null;
    public DataGridPanelPlanNiokr(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        initBtn();
    }

    public DataGridPanelPlanNiokr(DTable table, String tn) {
        super(table, tn);
        initBtn();
    }

    public DataGridPanelPlanNiokr(String tableName) {
        super(tableName);
        initBtn();
    }

    public DataGridPanelPlanNiokr(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        initBtn();
    }

    public DataGridPanelPlanNiokr(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        initBtn();
    }
    
    @Override
    public void enabledBtn() {
        super.enabledBtn();

        isApproved = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictExpApproved"));    
        isNotApproved = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictExpNotApproved"));        
        isSprDictEdit = (isApproved || isNotApproved || isDictAdd || isDictEdit || isDictDelete || isDictView);
        
        sprApproved.setVisible(isApproved);
        btnApproved.setVisible(isApproved);
        
        btnNotApproved.setVisible(isNotApproved);
        sprNotApproved.setVisible(isNotApproved);
        
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
      
        updateToolBar();
    }
    
    @Override
    public void updateToolBar() {
        super.updateToolBar();
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(isApproved, btnApproved);
         removeOrAddBtnOnToolBar(isNotApproved, btnNotApproved);
    }
 
     //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();


        btnApproved = new Button(datagridConstants.btnАpproved());
        btnApproved.addSelectionListener(tbListener);
        
        btnNotApproved = new Button(datagridConstants.btnNotАpproved());
        btnNotApproved.addSelectionListener(tbListener);
        
        btnPlan = new Button("План");
        //btnPlan.addSelectionListener(tbListener);
        MenuItem itemPdfPlan = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdfPlan.setId("0");
        itemPdfPlan.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlan("PDF");
            }
        });
        MenuItem itemRtfPlan = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtfPlan.setId("1");
        itemRtfPlan.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlan("RTF");
            }
        });

        MenuItem itemXlsPlan = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXlsPlan.setId("2");
        itemXlsPlan.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlan("XLS");
            }
        });
        
        Menu reportMenuSplitPlan = new Menu();
        reportMenuSplitPlan.insert(itemXlsPlan, 0);
        reportMenuSplitPlan.insert(itemRtfPlan, 0);
        reportMenuSplitPlan.insert(itemPdfPlan, 0);

        
        btnPlan.setMenu(reportMenuSplitPlan);
  

//beg button "Утвержденный план"        

        btnPlanApproved = new Button("Утвержденный план");
        
        MenuItem itemPdfPlanApproved = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdfPlanApproved.setId("0");
        itemPdfPlanApproved.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlanApproved("PDF");
            }
        });
        MenuItem itemRtfPlanApproved = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtfPlanApproved.setId("1");
        itemRtfPlanApproved.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlanApproved("RTF");
            }
        });

        MenuItem itemXlsPlanApproved = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXlsPlanApproved.setId("2");
        itemXlsPlanApproved.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onPlanApproved("XLS");
            }
        });
        
        Menu reportMenuSplitPlanApproved = new Menu();
        reportMenuSplitPlanApproved.insert(itemXlsPlanApproved, 0);
        reportMenuSplitPlanApproved.insert(itemRtfPlanApproved, 0);
        reportMenuSplitPlanApproved.insert(itemPdfPlanApproved, 0);

        
        btnPlanApproved.setMenu(reportMenuSplitPlanApproved);
  
//end button "Утвержденный план        

        btnDop1 = new Button("Дополнение 1");
        //btnDop1.addSelectionListener(tbListener);
        
                MenuItem itemPdfDop1 = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdfDop1.setId("0");
        itemPdfDop1.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDop1("PDF");
            }
        });
        MenuItem itemRtfDop1 = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtfDop1.setId("1");
        itemRtfDop1.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDop1("RTF");
            }
        });

        MenuItem itemXlsDop1 = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXlsDop1.setId("2");
        itemXlsDop1.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDop1("XLS");
            }
        });
        
        Menu reportMenuSplitDop1 = new Menu();
        reportMenuSplitDop1.insert(itemXlsDop1, 0);
        reportMenuSplitDop1.insert(itemRtfDop1, 0);
        reportMenuSplitDop1.insert(itemPdfDop1, 0);

        
        btnDop1.setMenu(reportMenuSplitDop1);


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
        
        sprApproved = new SeparatorToolItem();
        tb.add(sprApproved);        
        tb.add(btnApproved);
        
        sprNotApproved = new SeparatorToolItem();
        tb.add(sprNotApproved);
        tb.add(btnNotApproved);
        
        tb.add(new SeparatorToolItem());
        tb.add(btnDelete);
        //tb.add(btnView);
        
        tb.add(new SeparatorToolItem());
        tb.add(btnPlan);
        tb.add(new SeparatorToolItem());
        tb.add(btnPlanApproved);
        tb.add(new SeparatorToolItem());
        tb.add(btnDop1);
        
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
        //tb.add(sprReport);
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
//        tb.add(btnReportSplit);

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

    protected void onApprived(final Boolean isApproved) {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
     
        
        if (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }

          if ( isApproved && domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(1)) {
              MessageBox.info("Внимание!", "План уже утвержден!", null);
              return;
          }
        
          if ( !isApproved && domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(0)) {
              MessageBox.info("Внимание!", "План еще не утвержден!", null);
              return;
          }
        
        
        Integer id = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal());
        panelGrid.mask("Выполняется...");
        serviceExp.updatePlanNiokrApproved(id, isApproved, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                panelGrid.unmask();
                AppHelper.showMsgRpcServiceError(thrwbl);
            }
            @Override
            public void onSuccess(Boolean result) {
                panelGrid.unmask();
                BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(DDataGrid.class);
                if (isApproved) {
                    domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).setVal(1);
                } else {
                    domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).setVal(0);
                }
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
                gridSelectionChanged();                
            }
        });

    }
    
    
    protected void onPlan(String reportType) {        
         
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        
        if (domain == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }

        if (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }
                
        Integer planNiokrId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal());
        Integer departId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal());
        Date m = (Date) (domain.getRows().get(new SKeyForColumn("MAIN:MONTH")).getVal());
        Integer mounth = Integer.valueOf(DateTimeFormat.getFormat("MM").format(m));
        Integer year = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(m));
        Date dateCorr = (Date) (domain.getRows().get(new SKeyForColumn("MAIN:DATE_CORR")).getVal());
        String reportTemplate = "PlanFactNiokr";
        //String reportExportFormat = "xls";
        //String reportExportFormat = "pdf";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("planNiokrId", planNiokrId);
        params.put("departId", departId);
        params.put("pnMonth", mounth);
        params.put("pnYear", year);
        //MessageBox.info("","planNiokrId = "+planNiokrId.toString()+" departId = "+departId.toString()+" "+
        //               " pnYear = "+year.toString()+" pnMonth = "+mounth.toString(),null);
        String strWhere = " ";
        if (domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(1)) {
            strWhere = " AND PFN.DATE_CORR<='" + DateTimeFormat.getFormat("dd.MM.yyyy HH:mm").format(dateCorr) + "'";
            //MessageBox.info("","strWhere = "+strWhere,null);
        }
        
        params.put("strWhere", strWhere);
        getReportService().generateJasperReport(reportTemplate, reportType, params, condition, table, new FileReportServiceCallback(this));
    }
    
   protected void onPlanApproved(String reportType) {
 
       final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        
        if (domain == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }

        if (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }
                
        if (domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(0)) {
            MessageBox.info("Внимание!", "План еще не утвержден!", null);
            return;
          }
        
        //MessageBox.info("","В разработке пока!!!",null);
        
        Integer planNiokrId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal());
        Integer departId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal());
        Date m = (Date) (domain.getRows().get(new SKeyForColumn("MAIN:MONTH")).getVal());
        Integer mounth = Integer.valueOf(DateTimeFormat.getFormat("MM").format(m));
        Integer year = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(m));
        Date dateCorr = (Date) (domain.getRows().get(new SKeyForColumn("MAIN:DATE_CORR")).getVal());
        
        String reportTemplate = "PlanFactNiokrApproved";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("planNiokrId", planNiokrId);
        params.put("departId", departId);
        params.put("pnMonth", mounth);
        params.put("pnYear", year);
        //MessageBox.info("","planNiokrId = "+planNiokrId.toString()+" departId = "+departId.toString()+" "+
        //               " pnYear = "+year.toString()+" pnMonth = "+mounth.toString(),null);
        String strWhere = " ";
        if (domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(1)) {
            strWhere = " AND PFN.DATE_CORR<='" + DateTimeFormat.getFormat("dd.MM.yyyy HH:mm").format(dateCorr) + "'";
            //MessageBox.info("","strWhere = "+strWhere,null);
        }
       
        params.put("strWhere", strWhere);
        getReportService().generateJasperReport(reportTemplate, reportType, params, condition, table, new FileReportServiceCallback(this));

    }

    protected void onDop1(String reportType) {

        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (domain == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }

        if (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }

        Integer planNiokrId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:PLAN_NIOKR_ID")).getVal());
        Integer departId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal());
        Date dateMonth = (Date) (domain.getRows().get(new SKeyForColumn("MAIN:MONTH")).getVal());
        String reportTemplate = "PlanFactNiokrDop1";
        //String reportExportFormat = "doc";
        //String reportExportFormat = "pdf";

        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("planNiokrId", planNiokrId);
        params.put("departId", departId);        
        String pnDate = DateTimeFormat.getFormat("dd.MM.yyyy"). format(dateMonth);
        params.put("pnDate",pnDate);//DateTimeFormat.getFormat("dd.MM.yyyy").format(dateMonth) );
       // MessageBox.info("","planNiokrId = "+planNiokrId.toString()+" departId = "+departId.toString()+" "+
       //                " pnDate = "+pnDate.toString(),null);
        getReportService().generateJasperReport(reportTemplate, reportType, params, condition, table, new FileReportServiceCallback(this));
    }

    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        if (domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(0)) {
            DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.EDIT, EWindowType.EWT_WINDOW);
            window.addDomainSaveSuccesedListener(saveDataGridListener);
            window.show();
        } else {
            MessageBox.alert("Внимание!", "План утвержден. Редактирование возможно только после отмены утверждения.", null);
        }
    }

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
                } else if (ce.getButton() == btnApproved) {
                    onApprived(true);
                } else if (ce.getButton() == btnNotApproved) {
                    onApprived(false);
                } 
                else if (ce.getButton() == btnRefr) {
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
    
  private void initBtn()
    {
        isDictView = false;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
        
        btnView.setVisible(isDictView); 
        menuItemView.setVisible(isDictView);
        
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
    }

    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();

        if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelection().size() > 0) {
            DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
            if (domain.getRows().get(new SKeyForColumn("MAIN:APPROVED")).getVal().equals(0)) {
                btnPlanApproved.setEnabled(false);
                btnNotApproved.setEnabled(false);
                btnApproved.setEnabled(true);
            } else {
                btnPlanApproved.setEnabled(true);
                btnNotApproved.setEnabled(true);
                btnApproved.setEnabled(false);
            }
            btnPlan.setEnabled(true);
            btnDop1.setEnabled(true);
        }
        else {
            btnPlanApproved.setEnabled(false);
            btnNotApproved.setEnabled(false);
            btnApproved.setEnabled(false);
            btnPlan.setEnabled(false);
            btnDop1.setEnabled(false);
        }
    }
}
