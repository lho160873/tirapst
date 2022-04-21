package pst.arm.client.modules.aiscontracts;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.event.ChartEvent;
import com.extjs.gxt.charts.client.event.ChartListener;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;
import pst.arm.client.modules.aiscontracts.service.remote.GWTAisContractService;
import pst.arm.client.modules.aiscontracts.service.remote.GWTAisContractServiceAsync;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class AisContractPage extends BasePageNew {

    protected LayoutContainer main;
    protected ContentPanel panel;
    protected ToolBar toolbar;
    protected Chart chart;
    protected Button btnUpdateChart;
    protected ListStore<BeanModel> store;
    protected Grid<BeanModel> grid;
    protected ArmImages images = GWT.create(ArmImages.class);
    protected ContentPanel panelChart;
    private GWTAisContractServiceAsync service = GWT.create(GWTAisContractService.class);
    private ListLoader<BeanModel> loader;
    private SpinnerField fldYear;
    private DGComboBox<Integer> cbCompany;
    //private DGComboBox<Integer> cbOrgExecutor;
    private DGComboBox<Integer> cbStat;
    private DGComboBox<Integer> cbType;
    private Boolean isFirstLoad = Boolean.TRUE;
     //private Boolean isFirstWork = Boolean.TRUE;
     //private Boolean isFirstClose = Boolean.TRUE;
     //private Boolean isFirstPlan = Boolean.TRUE;
     //private Boolean isFirstCancle = Boolean.TRUE;
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
        RpcProxy<List<AisContract>> proxy = new RpcProxy<List<AisContract>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<AisContract>> callback) {

                AisContractSearchCondition condition = new AisContractSearchCondition();
                if (fldYear.getValue().toString().isEmpty()) {
                    String year = DateTimeFormat.getFormat("yyyy").format(new Date());
                    fldYear.setValue(Integer.valueOf(year));
                }
                condition.setYear(fldYear.getValue().toString());
                //condition.setIsDateNotNull(Boolean.TRUE);
                condition.getFilters().clear();
                //if (cbOrgExecutor.getValueId() != null && !cbOrgExecutor.getValueId().toString().isEmpty()) {
                //    condition.getFilters().put("orgExecutorId", cbOrgExecutor.getValueId());
                //} 
                if (cbCompany.getValueId() != null && !cbCompany.getValueId().toString().isEmpty()) {
                    condition.getFilters().put("companyId", cbCompany.getValueId());
                }
                else if (isFirstLoad) {
                    condition.getFilters().put("companyId", DEFAULT_COMPANY_ID);
                }
                if (cbStat.getValueId() != null && !cbStat.getValueId().toString().isEmpty()) {
                    condition.getFilters().put("contractStatId", cbStat.getValueId());
                } else if (isFirstLoad) {
                    condition.getFilters().put("contractStatId", DEFAULT_STAT_ID);
                }
                if (cbType.getValueId() != null && !cbType.getValueId().toString().isEmpty()) {
                    condition.getFilters().put("contractTypeId", cbType.getValueId());
                } else if (isFirstLoad) {
                    condition.getFilters().put("contractTypeId", DEFAULT_TYPE_ID);
                }
                service.getContract(condition, Boolean.FALSE, callback);
                isFirstLoad = Boolean.FALSE;
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

        
        Label lblType = new Label("Тип договора:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblType);
        cbType = new DGComboBox<Integer>("CONTRACT_TYPE_VO_ENABLED", "MAIN:CONTRACT_TYPE_ID", "MAIN:NAME");
        cbType.setValidateOnBlur(Boolean.TRUE);
        cbType.setValueId(DEFAULT_TYPE_ID);
        toolbar.add(cbType);


        Label lblStat = new Label("Статус договора:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblStat);
        cbStat = new DGComboBox<Integer>("CONTRACT_STAT_SIMPLE", "MAIN:CONTRACT_STAT_ID", "MAIN:NAME");
        cbStat.setValidateOnBlur(Boolean.TRUE);
        cbStat.setValueId(DEFAULT_STAT_ID);
        toolbar.add(cbStat);



        panel.setTopComponent(toolbar);

        panelChart = new ContentPanel();
        panelChart.setHeaderVisible(false);
        HBoxLayout hBoxLayoutPanelChart = new HBoxLayout();
        hBoxLayoutPanelChart.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.BOTTOM);
        panelChart.setLayout(hBoxLayoutPanelChart);
        panelChart.setScrollMode(Style.Scroll.AUTOY);
        panel.add(panelChart, new BorderLayoutData(Style.LayoutRegion.CENTER)); //помещаем в центр
        String url = "../chart/open-flash-chart.swf";
        chart = new Chart(url);        
        initGrid();
        loadStore();
        
        return main;
    }

    public ChartModel getChartModel() {
        if (fldYear.getValue().toString().isEmpty()) {
            String year = DateTimeFormat.getFormat("yyyy").format(new Date());
            fldYear.setValue(Integer.valueOf(year));
        }
        Integer filtrYear = fldYear.getValue().intValue();
        
        ChartModel cm = new ChartModel("", "font-size: 16px; font-family: Verdana;");
        cm.setBackgroundColour("#ffffff");
        cm.setEnableEvents(true);
         
        Legend legend = new Legend(Position.RIGHT, false);
        legend.setShadow(false);
        legend.setPadding(10);        
        cm.setLegend(legend);
        
        List<BeanModel> models = store.getModels();

        Integer countContract = models.size();//storeData.size();

        chart.setHeight((countContract + 3) * 35);
        chart.refresh();

        YAxis ya = new YAxis();
        ya.setRange(0, countContract + 3);
        ya.setSteps(1);
        ya.setColour("#000000");
        ya.setGridColour("#cccccc");
        cm.setYAxis(ya);

        XAxis xa = new XAxis();
        xa.setColour("#000000");
        xa.setRange(0, 11);
        xa.setSteps(1);
        xa.setLabels(monthsFull);
        xa.setTickHeight(5);
        xa.setGridColour("#cccccc");
        
        cm.setXAxis(xa);

        ArrayList<String> chartLabels = new ArrayList<String>();
        chartLabels.add("");
        Boolean isFirst = true;
        Boolean isFirstWork = true;
        Boolean  isFirstClose = true;
        Boolean isFirstPlan = true;
        Boolean isFirstCancle = true;
        Integer n = 1;
        for (BeanModel bean : models) {
            AisContract item = ((AisContract) bean.getBean());
            Date begin = item.getContractDateBegin();
            Integer yearBegin = 0;
            Integer monthBegin = 0;
            if (begin != null) {
                yearBegin = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(begin));
                monthBegin = Integer.valueOf(DateTimeFormat.getFormat("MM").format(begin));
            }
            Date end = (Date) item.getContractDateEnd();
            Integer yearEnd = filtrYear;
            Integer monthEnd = 12;
            if (end != null) {
             yearEnd = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(end));
             monthEnd = Integer.valueOf(DateTimeFormat.getFormat("MM").format(end));
            }
            //пока считаем что все договора выполнены до сегодня
            //TODO где то брать фактическую дату выполнения договора            

            Date factEnd = end;//(Date) item.getContractDateEnd();          
            Integer yearFactEnd = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(factEnd));
            Integer monthFactEnd = Integer.valueOf(DateTimeFormat.getFormat("MM").format(factEnd));

            LineChart line_fact = null;
          
            if (yearFactEnd >= filtrYear) {
                //линия для фактически выполненной части по договору
                line_fact = new LineChart();
                line_fact.setAnimateOnShow(false);
                line_fact.setColour("#000066");
                line_fact.setWidth(1);
                if (isFirst) {
                    line_fact.setText("% выполнения");
                    isFirst = false;
                }
            }

             LineChart line = new LineChart();
            line.setEnableEvents(true);
          
             line.addChartListener(new ChartListener() {
                 @Override
                public void chartClick(ChartEvent ce) 
                 {
                      MessageBox.info("","chartClick",null);
                 }
            });
            
              
            line.setAnimateOnShow(false);

            Integer stat = (Integer) item.getContractStatId();

            switch (stat) {
                case STAT_WORK_ID:
                    line.setColour(STAT_WORK);
                    if (isFirstWork) {
                        line.setText("Действующий");
                        isFirstWork = false;
                    }
                    break;
                case STAT_CLOSE_ID:
                    line.setColour(STAT_CLOSE);
                    if (isFirstClose) {
                        line.setText("Завершенный");
                        isFirstClose = false;
                    }
                    break;
                case STAT_PLAN_ID:
                    line.setColour(STAT_PLAN);
                    if (isFirstPlan) {
                        line.setText("Планируемый");
                        isFirstPlan = false;
                    }
                    break;
                case STAT_CANCEL_ID:
                    line.setColour(STAT_CANCEL);
                    if (isFirstCancle) {
                        line.setText("Отмененный");
                        isFirstCancle = false;
                    }
                    break;
                default:
                    line.setColour(STAT_WORK);
                    break;
            }
          
            line.setWidth(12);
            SolidDot d_1 = new SolidDot();
            d_1.setHaloSize(0);
            
            SolidDot d_1_fact = new SolidDot();
            d_1_fact.setHaloSize(0);
            if (yearBegin < filtrYear) {
                d_1.setXY(-0.5, n);
                d_1.setSize(5);
                if (line_fact != null) {
                    d_1_fact.setXY(-0.5, n);
                    d_1_fact.setSize(1);
                }

            } else {
                d_1.setXY(monthBegin - 1, n);
                d_1.setSize(5);
                if (line_fact != null) {
                    d_1_fact.setXY(monthBegin - 1, n);
                    d_1_fact.setSize(1);
                }
            }
            if (begin != null) {
                d_1.setTooltip(DateTimeFormat.getFormat("dd.MM.yyyy").format(begin));
            } else {
                d_1.setTooltip("не задано");
            }
            line.addDots(d_1);
            if (line_fact != null) {
                d_1_fact.setTooltip("");
                line_fact.addDots(d_1_fact);
            }

            SolidDot d_2 = new SolidDot();
            d_2.setHaloSize(0);
             if (end != null) {
                d_2.setTooltip(DateTimeFormat.getFormat("dd.MM.yyyy").format(end));
            } else {
                d_2.setTooltip("не задано");
            }

            if (yearEnd > filtrYear) {
                d_2.setXY(11.5, n);
                d_2.setSize(5);
            } else {
                d_2.setXY(monthEnd - 1, n);
                d_2.setSize(5);
            }
            line.addDots(d_2);

            //первую точку поставили, можно и вторую ставить
            if (line_fact != null) {
                if (yearFactEnd >= filtrYear) {
                    SolidDot d_2_fact = new SolidDot();
                    d_2_fact.setHaloSize(0);
                    if (yearFactEnd > filtrYear) {
                        d_2_fact.setXY(11.5, n);
                        d_2_fact.setSize(0);
                    } else if (yearFactEnd.equals(filtrYear)) {
                        d_2_fact.setXY(monthFactEnd - 1, n);
                        d_2_fact.setSize(0);
                    }
                    
                    if (end != null) {
                        d_2_fact.setTooltip(DateTimeFormat.getFormat("dd.MM.yyyy").format(factEnd));
                    } else {
                        d_2_fact.setTooltip("не задано");
                    }
                    line_fact.addDots(d_2_fact);
                }
            }

            cm.addChartConfig(line);
            if (line_fact != null) {
                cm.addChartConfig(line_fact);
            }
            
            if (item.getContractNumb() != null && !item.getContractNumb().isEmpty()) {
                chartLabels.add(item.getContractNumb());
            } else {
                chartLabels.add("не известен");
            }
            n++;

        }
        //добавляем надписи с номерами договоров
        ya.setLabels(chartLabels);

        Date now = new Date();
        Integer yearNow = Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(now));

        if (yearNow.equals(filtrYear)) {
            //рисуем линию текущего дня        
            LineChart lineNow = new LineChart();
            lineNow.setAnimateOnShow(false);
            lineNow.setColour("#ff3300");
            SolidDot dn_1 = new SolidDot();
            dn_1.setSize(0);

            Integer m = Integer.valueOf(DateTimeFormat.getFormat("MM").format(now));
            dn_1.setTooltip("Сегодня " + DateTimeFormat.getFormat("dd.MM.yyyy").format(now));
            dn_1.setXY(m - 1, 0);
            lineNow.addDots(dn_1);
            SolidDot dn_2 = new SolidDot();
            dn_2.setSize(0);
            dn_2.setXY(m - 1, countContract + 3);
            dn_2.setTooltip("Сегодня " + DateTimeFormat.getFormat("dd.MM.yyyy").format(now));
            lineNow.addDots(dn_2);
            cm.addChartConfig(lineNow);
        }

        //перерисовываем панель с графиком
        panelChart.removeAll();
        HBoxLayoutData hBoxLayoutDataPanelChart = new HBoxLayoutData(new Margins(20, 20, 20, 20));
        hBoxLayoutDataPanelChart.setFlex(2);
        panelChart.add(chart, hBoxLayoutDataPanelChart);
        panelChart.layout();

        //опускаем скроллбар вниз, что бы была видна нижняя ось графика
        if (panelChart.getLayoutTarget() != null) {
            Integer scrollHeight = Integer.valueOf(DOM.getElementProperty(panelChart.getLayoutTarget().dom, "scrollHeight"));
            Integer clientHeight = Integer.valueOf(DOM.getElementProperty(panelChart.getLayoutTarget().dom, "clientHeight"));
            Integer scrollTopPosition = scrollHeight - clientHeight;
            panelChart.setVScrollPosition(scrollTopPosition);
        }
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
        column.setId("contractNumb");
        column.setWidth(300);
        column.setHeader("Договор");
        column.setMenuDisabled(true);
        col.add(column);

        column = new ColumnConfig();
        column.setId("contractDate");
        column.setHeader("Дата договора");
        column.setWidth(100);
        column.setMenuDisabled(true);
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        col.add(column);

        column = new ColumnConfig();
        column.setId("contractDateBegin");
        column.setHeader("Дата начала");
        column.setWidth(100);
        column.setMenuDisabled(true);
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        col.add(column);

        column = new ColumnConfig();
        column.setId("contractDateEnd");
        column.setHeader("Дата окончания");
        column.setWidth(100);
        column.setMenuDisabled(true);
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        col.add(column);

        column = new ColumnConfig();
        column.setId("cost");
        column.setHeader("Стоимость без НДС");
        column.setWidth(100);
        column.setMenuDisabled(true);
        //column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        col.add(column);

        column = new ColumnConfig();
        column.setId("custNds");
        column.setHeader("Стоимость с НДС");
        column.setWidth(100);
        column.setMenuDisabled(true);
        //column.setDateTimeFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        col.add(column);


        ColumnModel cm = new ColumnModel(col);

        grid = new Grid<BeanModel>(store, cm);
        grid.setStripeRows(true);
        grid.setLoadMask(true);
        grid.setAutoExpandColumn("name");
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
