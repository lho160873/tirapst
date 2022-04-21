package pst.arm.client.modules.payment;


import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.event.ChartEvent;
import com.extjs.gxt.charts.client.event.ChartListener;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.FilledBarChart;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.dots.SolidDot;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;
import pst.arm.client.modules.payment.service.remote.GWTPaymentService;
import pst.arm.client.modules.payment.service.remote.GWTPaymentServiceAsync;
import pst.arm.client.modules.images.ArmImages;
import pst.arm.client.modules.payment.domain.Payment;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PaymentPage extends BasePageNew {

    protected LayoutContainer main;
    protected ContentPanel panel;
    protected ToolBar toolbar;
    protected Chart chart;
    protected Button btnUpdateChart;
    protected ListStore<BeanModel> store;
    protected Grid<BeanModel> grid;
    protected ArmImages images = GWT.create(ArmImages.class);
   // protected ContentPanel panelChart;
    private GWTPaymentServiceAsync service = GWT.create(GWTPaymentService.class);
    private ListLoader<BeanModel> loader;
    private SpinnerField fldYear;
    private DGComboBox<Integer> cbCompany;
    private DGComboBox<Integer> cbStat;
    private DGComboBox<Integer> cbType;
    private Boolean isFirstLoad = Boolean.TRUE;
     //private Boolean isFirstWork = Boolean.TRUE;
     //private Boolean isFirstClose = Boolean.TRUE;
     //private Boolean isFirstPlan = Boolean.TRUE;
     //private Boolean isFirstCancle = Boolean.TRUE;
    //private static final Integer DEFAULT_ORG_ID = 1; //РИМР
    private static final Integer DEFAULT_COMPANY_ID = 1; //РИМР
    private static final Integer DEFAULT_STAT_ID = 1; //Договор на поставку
    private static final Integer DEFAULT_TYPE_ID = 0;
    
    private static final int STAT_WORK_ID = 1;
    private static final int STAT_CLOSE_ID = 2;
    private static final int STAT_PLAN_ID = 3;
    private static final int STAT_CANCEL_ID = 4;

    /* #a3d1d1 //зеленый (действующий)
     #d1d1d1 //серый (завершен)
     #ccccff //фиолетовый (планируемый)
     #ffccff //розовый (отемененный)*/
    private static final String STAT_WORK = "#a3d1d1";
    private static final String STAT_CLOSE = "#d1d1d1";
    private static final String STAT_PLAN = "#ccccff";
    private static final String STAT_CANCEL = "#ffccff";

    //инициализация хранилища данных для грида
    protected void initStore() {
        RpcProxy<List<Payment>> proxy = new RpcProxy<List<Payment>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Payment>> callback) {

                PaymentSearchCondition condition = new PaymentSearchCondition();
                if (fldYear.getValue().toString().isEmpty()) {
                    String year = DateTimeFormat.getFormat("yyyy").format(new Date());
                    fldYear.setValue(Integer.valueOf(year));
                }
                condition.setYear(fldYear.getValue().toString());
                service.getPayment(condition, callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        loader = new BaseListLoader(proxy, reader);
        store = new ListStore<BeanModel>(loader);


        loader.addLoadListener(new LoadListener() {
            @Override
            public void handleEvent(LoadEvent e) {
                loader.setSortField((String) grid.getState().get("sortField"));
                super.handleEvent(e);
                grid.unmask();
                chart.setChartModel(getChartModel());                
                //MessageBox.info("handleEvent",String.valueOf(store.getCount()),null);

            }

            @Override
            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);

            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }
        });
        loader.setRemoteSort(true);

    }

    @Override
    protected LayoutContainer getContentPanel() {

        main = new LayoutContainer(new FitLayout());//new BorderLayout());
        main.setBorders(false);

        panel = new ContentPanel(new BorderLayout());
        main.add(panel);

        toolbar = new ToolBar();
        toolbar.setSpacing(6);
        btnUpdateChart = new Button("Перестроить график", images.chart_bar(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                updateChart();
            }
        });
        toolbar.add(btnUpdateChart);
        toolbar.add(new SeparatorToolItem());
        
        Label lbl = new Label("Год:");
        lbl.setAutoWidth(true);
        toolbar.add(lbl);
        fldYear = new SpinnerField();
        fldYear.setPropertyEditorType(Integer.class);
        String year = DateTimeFormat.getFormat("yyyy").format(new Date());
        fldYear.setValue(Integer.valueOf(year));
        fldYear.setWidth(60);
        toolbar.add(fldYear);

        Label lblOrg = new Label("Огранизация:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblOrg);
        //cbOrgExecutor = new DGComboBox<Integer>("HLV_CB_ORGANIZATION", "MAIN:ORGANIZATION_ID", "MAIN:SHORT_NAME");
        //cbOrgExecutor.setValidateOnBlur(Boolean.TRUE);
        //cbOrgExecutor.setValueId(DEFAULT_ORG_ID); //РИМР
        //toolbar.add(cbOrgExecutor);
        cbCompany = new DGComboBox<Integer>("COMPANY", "MAIN:COMPANY_ID", "MAIN:SHORT_NAME");
        cbCompany.setValidateOnBlur(Boolean.TRUE);        
        cbCompany.setValueId(DEFAULT_COMPANY_ID); //РИМР
        toolbar.add(cbCompany);


        /*Label lblType = new Label("Тип договора:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblType);
        cbType = new DGComboBox<Integer>("CONTRACT_TYPE", "MAIN:CONTRACT_TYPE_ID", "MAIN:NAME");
        cbType.setValidateOnBlur(Boolean.TRUE);
        cbType.setValueId(DEFAULT_TYPE_ID);
        toolbar.add(cbType);


        Label lblStat = new Label("Статус договора:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblStat);
        cbStat = new DGComboBox<Integer>("CONTRACT_STAT", "MAIN:CONTRACT_STAT_ID", "MAIN:NAME");
        cbStat.setValidateOnBlur(Boolean.TRUE);
        cbStat.setValueId(DEFAULT_STAT_ID);
        toolbar.add(cbStat);*/



        panel.setTopComponent(toolbar);

        //panelChart = new ContentPanel();
        //panelChart.setHeaderVisible(false);
        //HBoxLayout hBoxLayoutPanelChart = new HBoxLayout();
        //hBoxLayoutPanelChart.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.BOTTOM);
        //panelChart.setLayout(hBoxLayoutPanelChart);
        //panelChart.setScrollMode(Style.Scroll.AUTOY);
        //panel.add(panelChart, new BorderLayoutData(Style.LayoutRegion.CENTER)); //помещаем в центр
        String url = "../chart/open-flash-chart.swf";
        chart = new Chart(url);        
         BorderLayoutData borderData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        borderData.setMargins(new Margins(5));   
        panel.add(chart, borderData); //помещаем в центр     
        
        initGrid();
        loadStore();
        chart.setChartModel(getChartModel());
       
        return main;
    }

    public ChartModel getChartModel() {

        if (fldYear.getValue().toString().isEmpty()) {
            String year = DateTimeFormat.getFormat("yyyy").format(new Date());
            fldYear.setValue(Integer.valueOf(year));
        }
        Integer filtrYear = fldYear.getValue().intValue();
        
        
        
        ChartModel cm = new ChartModel("", "font-size: 33px; font-family: Verdana;");
        cm.setBackgroundColour("#ffffff");
        cm.setDecimalSeparatorComma(true);
        
        XAxis xa = new XAxis();
        xa.getLabels().setColour("#000000");
        xa.setGridColour("#cccccc");
        xa.setColour("#000000");
        xa.setLabels(monthsFull);
        xa.setRange(1, 12);
        cm.setXAxis(xa);
        
        Legend legend = new Legend(Position.TOP, false);
        legend.setShadow(false);
        legend.setPadding(10);
        cm.setLegend(legend);
        
        List<BeanModel> models = store.getModels();
        long max = 0;
        for (BeanModel bean : models) {
            Payment item = ((Payment) bean.getBean());
            if (item.getSummPlan() != null) {
                long s = Math.round(item.getSummPlan());
                max = (max < s) ? s : max;
            }
            if (item.getSummFact() != null) {
                long s = Math.round(item.getSummFact());
                max = (max < s) ? s : max;
            }
        }
        long delta = max/10;
        
        YAxis ya = new YAxis();
        ya.setRange(0, max+delta);
        ya.setSteps(delta);
        ya.setGridColour("#cccccc");
        ya.setColour("#000000");
        
        cm.setYAxis(ya);      
        
                
        FilledBarChart bchartSummPlan = new FilledBarChart("#0000bf","#0000bf");//, CLR_FILL_AREA);
        bchartSummPlan.setText("Запланированные оплаты");
        FilledBarChart bchartSummFact = new FilledBarChart("#ec0000","#ec0000");//, CLR_FILL_AREA);
        bchartSummFact.setText("Фактические оплаты");
        Integer month = 1;
        Integer k = 0;
        //for (Integer month = 1; month < 13; month++) {
        for (BeanModel bean : models) {
            //MessageBox.info("","1",null);
            //BeanModel bean = models.get(k);
            //if ( bean == null ) {continue;}
            
            Payment item = ((Payment) bean.getBean());
            //if ( item == null ) {continue;}
            while( month < item.getMonth() )
            {
                month++;
                bchartSummPlan.addNullValue();
                bchartSummFact.addNullValue();
            }
            
            
            long valPlan = 0;
            if ( item.getSummPlan()!=null && item.getMonth() == month) {
                valPlan = Math.round(item.getSummPlan());
            }
            com.extjs.gxt.charts.client.model.charts.BarChart.Bar bar = new com.extjs.gxt.charts.client.model.charts.BarChart.Bar(valPlan, "#0000bf");
            bchartSummPlan.addBars(bar);
            
            long valFact = 0;
            if ( item.getSummFact()!=null && item.getMonth() == month) {
                valFact = Math.round(item.getSummFact());
            }
            com.extjs.gxt.charts.client.model.charts.BarChart.Bar barFact = new com.extjs.gxt.charts.client.model.charts.BarChart.Bar(valFact, "#ec0000");
            bchartSummFact.addBars(barFact);
            
            //k++;
            month++;            
        }

        cm.addChartConfig(bchartSummPlan);
         cm.addChartConfig(bchartSummFact);
        
        return cm;
    }

    public void updateChart() {
        
       
        loadStore();                        
        chart.setChartModel(getChartModel());
    }

    public void initGrid() {

        initStore();

        List<ColumnConfig> col = new ArrayList<ColumnConfig>();


        ColumnConfig column = new ColumnConfig();
        column.setId("strMonth");
        column.setWidth(300);
        column.setHeader("Месяц");
        column.setMenuDisabled(true);
        column.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                //throw new UnsupportedOperationException("Not supported yet.");
                Payment p = (Payment) model.getBean();
                return monthsFull[p.getMonth()-1];
            }
        });
        
        col.add(column);
        
        column = new ColumnConfig();
        column.setId("summPlan");
        column.setHeader("Плановая сумма");
        column.setWidth(100);
        column.setMenuDisabled(true);
        column.setNumberFormat(NumberFormat.getCurrencyFormat());
        
        col.add(column);
        
        column = new ColumnConfig();
        column.setId("summFact");
        column.setHeader("Фактическая сумма");
        column.setWidth(100);
        column.setMenuDisabled(true);
        column.setNumberFormat(NumberFormat.getCurrencyFormat());
        
        col.add(column);

        ColumnModel cm = new ColumnModel(col);

        grid = new Grid<BeanModel>(store, cm);
        grid.setStripeRows(true);
        grid.setLoadMask(true);
        //grid.setAutoExpandColumn("name");
        grid.setBorders(true);

        grid.getView().setForceFit(true);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.MULTI);

        HBoxLayoutData hBoxLayoutDataPanelChart = new HBoxLayoutData(new Margins(20, 20, 20, 20));
        hBoxLayoutDataPanelChart.setFlex(2);

        grid.setHeight(400);
        BorderLayoutData d = new BorderLayoutData(Style.LayoutRegion.SOUTH);
        d.setMargins(new Margins(0, 0, 3, 0));
        panel.add(grid, d); //помещаем вниз
        d.setSplit(true);
    }

    private void loadStore() {
        loader.load();

    }
    private static final String[] monthsFull = new String[]{
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь",
        "Декабрь"};
}
