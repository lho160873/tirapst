package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;
import java.util.HashMap;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
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
public class DataGridPanelPlanNiokrImpl extends DataGridPanel {

    private final GWTDDataGridExpansionServiceAsync serviceExp = GWT.create(GWTDDataGridExpansionService.class); //сервис отвечающий за обработку данных
    //private Button btnApproved, btnNotApproved;
     private Button btnPlan, btnDop1;
    //protected SeparatorToolItem sprApproved, sprNotApproved;
    public DataGridPanelPlanNiokrImpl(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        initBtn();
    }

    public DataGridPanelPlanNiokrImpl(DTable table, String tn) {
        super(table, tn);
        initBtn();
    }

    public DataGridPanelPlanNiokrImpl(String tableName) {
        super(tableName);
        initBtn();
    }

    public DataGridPanelPlanNiokrImpl(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        initBtn();
    }

    public DataGridPanelPlanNiokrImpl(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        initBtn();
    }
    
   
 
     //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb =  super.createToolBar();
        
        btnPlan = new Button("План");
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
        
        btnDop1 = new Button("Дополнение 1");        
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

        MenuItem itemPdf = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdf.setId("0");
        itemPdf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("PDF");
            }
        });


        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);
        
        tb.add(new SeparatorToolItem());
        tb.add(btnPlan);
        tb.add(new SeparatorToolItem());
        tb.add(btnDop1);

        tb.add(ftiFiltr);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        tb.add(sprFiltr);
        tb.add(btnFiltr);

        tb.add(ftiFiltr);
        tb.remove(btnColumnsVisibility);
        tb.remove(sprColumnsVisibility);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        tb.remove(btnColumnsVisibilityDel);
        tb.remove(sprColumnsVisibilityDel);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        tb.add(sprFiltr);
        tb.add(btnFiltr);


        enabledBtn();

        return tb;
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
        String reportTemplate = "PlanFactNiokrImpl";
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
    
    
    protected void onDop1(String reportType) {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

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
         
        //MessageBox.info("","dateMonth = "+dateMonth.toString() , null);
        
        String reportTemplate = "PlanFactNiokrImplDop1";
        //String reportExportFormat = "doc";
        //String reportExportFormat = "pdf";
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("planNiokrId", planNiokrId);
        params.put("departId", departId); 
        
        String pnDate = DateTimeFormat.getFormat("dd.MM.yyyy"). format(dateMonth);
        params.put("pnDate",pnDate);//DateTimeFormat.getFormat("dd.MM.yyyy").format(dateMonth) );
        
        Date dateMonthNext = (Date)dateMonth.clone();
        CalendarUtil.addMonthsToDate(dateMonthNext, 1);        
        String pnDateNext = DateTimeFormat.getFormat("dd.MM.yyyy").format(dateMonthNext);
        params.put("pnDateNext",pnDateNext);
        
        //MessageBox.info("","departId = "+departId.toString() , null);
        //MessageBox.info("","planNiokrId = "+planNiokrId.toString() , null);
        //MessageBox.info("","pnDate = "+pnDate , null);
        //MessageBox.info("","pnDateNext = "+pnDateNext , null);
        
     
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

        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);

        sprSend.setVisible(false);

        updateToolBar();
    }
}
