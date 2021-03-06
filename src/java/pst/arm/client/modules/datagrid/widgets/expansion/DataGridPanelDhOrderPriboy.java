package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;
import pst.arm.client.modules.ganttchart.portable.GanttChartPortablePanel;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartPriboyService;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartPriboyServiceAsync;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelDhOrderPriboy extends DataGridPanel {

    private Button btnJob, btnDG, btnDG2, pieChartButton;
    final private GWTDDataGridServiceAsync serviceDataGrid = GWT.create(GWTDDataGridService.class);
    private GWTGanttChartPriboyServiceAsync service = GWT.create(GWTGanttChartPriboyService.class);

    public DataGridPanelDhOrderPriboy(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDhOrderPriboy(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDhOrderPriboy(String tableName) {
        super(tableName);
    }

    public DataGridPanelDhOrderPriboy(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDhOrderPriboy(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    //?????????????? ???????????? ?? ???????????????? ????????????????????
    @Override
    protected ToolBar createToolBar() {

        ToolBar tb = super.createToolBar();

        btnJob = new Button(datagridConstants.btnShowJob());
        btnJob.setIconAlign(Style.IconAlign.LEFT);
        btnJob.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                showJob();
            }
        });



        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);

        pieChartButton = new Button("???????????? ???? ?????????????? ????????????????????");
        pieChartButton.setIcon(AbstractImagePrototype.create(commonImages.view()));
        pieChartButton.setIconAlign(Style.IconAlign.LEFT);
        pieChartButton.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                showPieChart();
            }
        });



        tb.add(btnJob);
        tb.add(pieChartButton);

        btnDG = new Button("?????????????????? ??????????");

        btnDG.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnDG) {
                    getService().getDataGrid("DH_ORDER_PRIBOY_HLV", condition, new AsyncCallback<List<DDataGrid>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught,"???????????? ?????????????????? ???????????? ?????? ???????????????????? ??????????????????");
                        }

                        @Override
                        public void onSuccess(final List<DDataGrid> data) {
                            GanttChartPortablePanel gcw = new GanttChartPortablePanel(data);
                            gcw.show();
                        }
                    });
                }
            }
        });
        tb.add(btnDG);

        btnDG2 = new Button("?????????????????? ???????? ???????????????????? ????????????");
        btnDG2.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnDG2) {
                    GanttChartSearchCondition jsc = new GanttChartSearchCondition();
                    DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
                    final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
                    final Integer typeExec = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:TYPE_EXEC")).getVal();
                    final String text = (String) domain.getRows().get(new SKeyForColumn("MAIN:NUMDOC")).getVal();
//                    
//                    final String numDoc = (String) domain.getRows().get(new SKeyForColumn("MAIN:NUMDOC")).getVal();
//                    

                    if (currentId == null) {
                        MessageBox.info("", "?????????? ???? ????????????", null);
                        return;
                    }
                    if (typeExec == null) {
                        MessageBox.info("", "?????????????? ???????????????????? ???????????? ???? ??????????????????", null);
                        return;
                    } else {
                        if (typeExec == 1) {
                            MessageBox.info("", "?????????? ??????????????????????????????", null);
                            return;
                        }
                        if (typeExec == 2) {
                            MessageBox.info("", "?????????? ???? ??????????????", null);
                            return;
                        }
                    }

//                    if (numDoc != null) {
//                        MessageBox.info("", "???????????????????????? = " + numDoc, null);
//                    }
//                        MessageBox.info("?????????????? ???????????????????? ????????????"+typeExec, "id ???????????? = " + currentId, null);
                    jsc.setId(currentId);
                    service.getAllJobs2(jsc, new AsyncCallback<List<Job2>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            AppHelper.showMsgRpcServiceError(caught,"???????????? ?????????????????? ???????????? ?????? ???????????????????? ??????????????????");
                        }

                        @Override
                        public void onSuccess(List<Job2> result) {
                            if (result.isEmpty()) {
                                MessageBox.info("???????????????????????????? ??????????????????", "?? ?????????????????? ???????? ???????????? ?????? ???? ???????????? ???????????????????????? ??????????????", null);
                                return;
                            }
                            GanttChartPortablePanel gcw = new GanttChartPortablePanel(result, text);
//                            MessageBox.info("Size = "+result.size(), "Panel = " + gcw, null);
                            gcw.show();
                        }
                    });
                }
            }
        });
        tb.add(btnDG2);

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
        
        

        pieChartButton.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictExpChart")));

        btnDG.setVisible((mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictExpDg")));


        return tb;
    }

    protected void showPieChart() {
        final Window complex = new Window();
        complex.setMaximizable(true);
        complex.setHeading("???????????? ???? ?????????????? ???????????????????? (???????????? ?? ???????????????????? ??????????????)");
        complex.setWidth(1000);
        complex.setHeight(600);

        String url = "../chart/open-flash-chart.swf";
        final Chart chart = new Chart(url);
        chart.setBorders(true);

        getService().getDataGrid("DH_ORDER_PRIBOY_HLV", condition, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught,"???????????? ?????????????????? ???????????? ?????? ???????????????????? ??????????????????");
            }

            @Override
            public void onSuccess(final List<DDataGrid> data) {
                getService().getAllDataGrid("SYS_ENUMERATION_TYPE_EXEC_PROBOY_HLV", new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught,"???????????? ?????????????????? ???????????? ?????? ???????????????????? ??????????????????");
                    }

                    @Override
                    public void onSuccess(List<DDataGrid> types) {
                        chart.setChartModel(getPieChartData(data, types));
                        complex.add(chart);
                        chart.refresh();
                        complex.show();
                    }
                });
            }
        });
    }

    private ChartModel getPieChartData(List<DDataGrid> data, List<DDataGrid> types) {

        ChartModel cm = new ChartModel("",
                "font-size: 14px; font-family: Verdana; text-align: center;");
        cm.setBackgroundColour("#fffff5");
        Legend lg = new Legend(Legend.Position.RIGHT, true);
        lg.setPadding(10);
        cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        //pie.setNoLabels(true);

        pie.setTooltip("#label# - #val# ???? #total#");
        pie.setColours("#ff0000", "#00aa00", "#0000ff", "#ff9900", "#ff00ff", "#ff22ff");

        int[] val = new int[types.size()];

        for (DDataGrid d : data) {
            for (int j = 0; j < types.size(); j++) {
                SKeyForColumn keyType = new SKeyForColumn("MAIN:TYPE_EXEC");
                SKeyForColumn keyLine = new SKeyForColumn("MAIN:LINENO");

                Integer val1 = (Integer) d.getRows().get(keyType).getVal();
                Integer val2 = (Integer) types.get(j).getRows().get(keyLine).getVal();

                if (val1 != null && val2 != null && val1.equals(val2)) {
                    val[j]++;
                    break;
                }
            }
        }

        for (int i = 0; i < types.size(); i++) {
            pie.addSlices(new PieChart.Slice(val[i], (String) types.get(i).getRows().get(new SKeyForColumn("MAIN:VALUE")).getVal() + " - " + val[i], (String) types.get(i).getRows().get(new SKeyForColumn("MAIN:VALUE")).getVal()));
        }

        cm.addChartConfig(pie);
        return cm;
    }

    private void showJob() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "???? ???????????? ??????????.", null);
            return;
        }

        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                final DTable table = result;
                final DDataGrid domainData = new DDataGrid();
                domainData.setName(table.getQueryName());
                for (IColumnBuilder builder : table.getColumnBuilders()) {
                    for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        IRowColumnVal val = builder.createRowColumnVal(key);
                        if (builder.getColumn(key).getIsKey()) {
                            val.setVal(currentId);
                        }
                        domainData.getRows().put(key, val);
                    }
                }

                serviceDataGrid.getDataGridById("DT_JOB_PLAN_PRIBOY_HLV", domainData, new AsyncCallback<DDataGrid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(DDataGrid t) {
                        DDataGrid domainData = new DDataGrid();
                        domainData.copy(t);
                        DataGridEditWindow window;
                        window = new DataGridEditWindowDtJobPlanPriboy(domainData, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
                        window.setWidth(680);
                        window.setHeight(580);
                        window.show();
                    }
                });

            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        serviceDataGrid.getTable("DT_JOB_PLAN_PRIBOY_HLV", callback_getTable);
    }

    @Override
    protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.setWidth(680);
        window.setHeight(580);
        window.show();
    }
    //?????????????? ???????????? 
    /*@Override
     public void clearStore() {
     super.clearStore();
     btnJob.setEnabled(false);
     btnDG.setEnabled(false);
     }*/
}
