package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.shared.impl.DateRecord;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDT;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDTAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridWindowPanel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelDhJob1 extends DataGridPanel {

    private Button btnCalcCosts, btnTimeForJob, btnPlan, btnPlanRev;
    private final GWTDDataGridServiceDHDTAsync dhdtService = GWT.create(GWTDDataGridServiceDHDT.class); //сервис отвечающий за обработку данных

    public DataGridPanelDhJob1(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDhJob1(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDhJob1(String tableName) {
        super(tableName);
    }

    public DataGridPanelDhJob1(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDhJob1(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
   
 //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = super.createToolBar();
        
        
        btnCalcCosts = new Button(datagridConstants.btnCalcCosts()); //Расчитать затраты
        btnCalcCosts.setIconAlign(Style.IconAlign.LEFT);
        btnCalcCosts.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                calcCosts();
            }
        });
        
        btnTimeForJob = new Button(datagridConstants.btnTimeForJob()); //Расчитать затраты
        btnTimeForJob.setIconAlign(Style.IconAlign.LEFT);
        btnTimeForJob.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                timeForJob();
            }
        });

        btnPlan = new Button("Прямое построение плана"); //Расчитать затраты
        btnPlan.setIconAlign(Style.IconAlign.LEFT);
        btnPlan.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                plan();
            }
        });

        btnPlanRev = new Button("Обратное построение плана"); //Расчитать затраты
        btnPlanRev.setIconAlign(Style.IconAlign.LEFT);
        btnPlanRev.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                planRev();
            }
        });
        
        
        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);
        
        tb.add(new SeparatorToolItem());
        tb.add(btnCalcCosts);
        tb.add(new SeparatorToolItem());
        tb.add(btnTimeForJob);
        tb.add(new SeparatorToolItem());
        tb.add(btnPlan);
        tb.add(btnPlanRev);
        tb.add(new SeparatorToolItem());

        tb.add(ftiFiltr);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        tb.add(sprFiltr);
        tb.add(btnFiltr);

        tb.add(ftiFiltr);
        tb.remove(sprColumnsVisibility);
        tb.remove(btnColumnsVisibility);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        tb.remove(sprColumnsVisibilityDel);
        tb.remove(btnColumnsVisibilityDel);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        tb.add(sprFiltr);
        tb.add(btnFiltr);


        enabledBtn();
        
        return tb;
    }
    @Override
    public void enabledBtn() {
        //настройка пров доступа
        isDictAdd = false;
        isDictDelete = false;
        isDictEdit = false;
        isDictView = true;
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
    
     private void calcCosts() {
        if (domainForSub == null) {
            MessageBox.info(commonConstants.attantion(),datagridMessages.expInfoOrderNull(),null);
        }
        Integer orderId = (Integer) (domainForSub.getRows().get(new SKeyForColumn("MAIN:ID")).getVal());
        Integer interactingSystId = (Integer) (domainForSub.getRows().get(new SKeyForColumn("MAIN:INTERACTING_SYST_ID")).getVal());
        
        panelGrid.mask(datagridMessages.expInfoCalc());
        
        
        
        dhdtService.calcCosts(interactingSystId, orderId,  new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                 panelGrid.unmask();
                 AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                panelGrid.unmask();
                if (result) {
                    reloadGrid();
                    MessageBox.alert("", datagridMessages.expInfoCalcOk(), null);                    
                } else {
                    MessageBox.alert("", datagridMessages.expErrCalcError(), null);
                }
            }
        });
    }

    private void plan() {
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        cnd.getFilters().put(new SKeyForColumn("MAIN:id_order"), condition.getFilters().get(new SKeyForColumn("MAIN:id_order")));
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal(4);

        cnd.getFilters().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), val);
        getService().getDataGrid("DH_JOB_1_FOR_PLAN_VO", cnd, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                if (result.size() > 0) {
                    getService().getTable("DH_JOB_1_PLAN_DATE_VO", new AsyncCallback<DTable>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(DTable result) {
                            DDataGrid domain = new DDataGrid();
                            domain.setName(result.getQueryName());
                            for (IColumnBuilder builder : result.getColumnBuilders()) {
                                for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                                    IRowColumnVal val = builder.createRowColumnVal(key);
                                    domain.getRows().put(key, val);
                                    //builder.setDomainValueFromField(key, fields.get(key), domain );
                                }
                            }

                            domain.getRows().put(new SKeyForColumn("MAIN:id_order"), condition.getFilters().get(new SKeyForColumn("MAIN:id_order")));

                            DRowColumnValDate d =  new DRowColumnValDate();

                            Date datestart = new Date();
                            if (passedFields.get(new SKeyForColumn("MAIN:DATESTART")).getVal() != null) {
                                String datestr = (String) passedFields.get(new SKeyForColumn("MAIN:DATESTART")).getVal();
                                datestart = DateTimeFormat.getFormat("dd.MM.yyyy").parse(datestr);
                                datestart.setHours(12);
                            }

                            d.setVal(datestart);

                            domain.getRows().put(new SKeyForColumn("MAIN:DATE_START"), d);

                            DataGridEditWindowDhJobPlan window = new DataGridEditWindowDhJobPlan(domain, result, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                            window.addDomainSaveSuccesedListener(new DomainSaveSuccesedListener<DDataGrid>() {
                                @Override
                                public void onDomainSaveSucceed(DDataGrid domain) {
                                    reloadGrid();
                                }
                            });
                            window.show();
                        }
                    });
                }
                else {
                    MessageBox.alert("Внимание", "Затраты по выбранному заказу не заданы. Построение плана невозможно", null);
                }
            }
        });

    }

    private void planRev() {
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        cnd.getFilters().put(new SKeyForColumn("MAIN:id_order"),  condition.getFilters().get(new SKeyForColumn("MAIN:id_order")));
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal(4);

        cnd.getFilters().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), val);
        getService().getDataGrid("DH_JOB_1_FOR_PLAN_VO", cnd, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                if (result.size() > 0) {
                    getService().getTable("DH_JOB_1_PLAN_DATE_REV_VO", new AsyncCallback<DTable>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(DTable result) {
                            DDataGrid domain = new DDataGrid();
                            domain.setName(result.getQueryName());
                            for (IColumnBuilder builder : result.getColumnBuilders()) {
                                for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                                    IRowColumnVal val = builder.createRowColumnVal(key);
                                    domain.getRows().put(key, val);
                                    //builder.setDomainValueFromField(key, fields.get(key), domain );
                                }
                            }

                            domain.getRows().put(new SKeyForColumn("MAIN:id_order"), condition.getFilters().get(new SKeyForColumn("MAIN:id_order")));

                            DRowColumnValDate d =  new DRowColumnValDate();

                            Date dateend= new Date();
                            if (passedFields.get(new SKeyForColumn("MAIN:DATEEND")).getVal() != null) {
                                String datestr = (String) passedFields.get(new SKeyForColumn("MAIN:DATEEND")).getVal();
                                dateend = DateTimeFormat.getFormat("dd.MM.yyyy").parse(datestr);
                                dateend.setHours(12);
                            }
                            d.setVal(dateend);
                            domain.getRows().put(new SKeyForColumn("MAIN:DATE_START"), d);

                            DataGridEditWindowDhJobRev window = new DataGridEditWindowDhJobRev(domain, result, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                            window.addDomainSaveSuccesedListener(new DomainSaveSuccesedListener<DDataGrid>() {
                                @Override
                                public void onDomainSaveSucceed(DDataGrid domain) {
                                    reloadGrid();
                                }
                            });
                            window.show();
                        }
                    });
                }
                else {
                    MessageBox.alert("Внимание", "Затраты по выбранному заказу не заданы. Построение плана невозможно", null);
                }
            }
        });

    }
     
    private void timeForJob() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridPanelNotEditable newWindow = new DataGridPanelNotEditable("TIME_FOR_JOB_HLV");
        IRowColumnVal v = new DRowColumnValNumber();
        v.setVal(domain.getRows().get(new SKeyForColumn("MAIN:ID_JOB_REC")).getVal());
        newWindow.getCondition().getFilters().put(new SKeyForColumn("MAIN:ID_JOB_REC"), v);

        IRowColumnVal v1 = new DRowColumnValNumber();
        v1.setVal(domain.getRows().get(new SKeyForColumn("MAIN:INTERACTING_SYST_ID")).getVal());
        newWindow.getCondition().getFilters().put(new SKeyForColumn("SPR_DEPART_1:INTERACTING_SYST_ID"), v1);

        //DataGridWindowPanelWithHeader window = new DataGridWindowPanelWithHeader(newWindow, "Детализация затрат задания");
        DataGridWindowPanel window = new DataGridWindowPanel(newWindow);
        window.show();
    }

}
