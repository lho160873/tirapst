package pst.arm.client.modules.powerproduction;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.axis.RadarAxis;
import com.extjs.gxt.charts.client.model.axis.RadarAxis.RadarLabels;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.AreaChart;
import com.extjs.gxt.charts.client.model.charts.BarChart;

import com.extjs.gxt.charts.client.model.charts.FilledBarChart;
import com.extjs.gxt.charts.client.model.charts.dots.Dot;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.chart.client.chart.axis.RadialAxis;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.test.model.RadarData;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.grid.GridTools;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.images.ArmImages;
import pst.arm.client.modules.powerproduction.domain.Department;
import pst.arm.client.modules.test.widgets.examples.TestData;
//import com.extjs.gxt.charts.client.model.axis.Label;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PowerProductionPage extends BasePageNew {

    protected LayoutContainer main;
    protected ContentPanel panel;
    protected ToolBar toolbar;
    protected Chart chart,barСhart;
    protected Button btnUpdateChart;
    protected ListStore<BeanModel> store;
    protected EditorGrid<BeanModel> grid;
    protected ArmImages images = GWT.create(ArmImages.class);
    protected ContentPanel panelChart;
    private DGComboBox<Integer> cbOrgExecutor;
    private CheckBox cbLoad;
    private Boolean isFirstLoad = Boolean.TRUE;
    private Boolean isFirstWork = Boolean.TRUE;
    private Boolean isFirstClose = Boolean.TRUE;
    private Boolean isFirstPlan = Boolean.TRUE;
    private Boolean isFirstCancle = Boolean.TRUE;
    private static final Integer DEFAULT_ORG_ID = 3; //РИМР
    private ContentPanel pnlForGrid;
    private Button btnDelete, btnAdd;
    private TabPanel tabPanel;
    private TabItem itemChart,itemBarChart;

    /* #a3d1d1 //зеленый (действующий)
     #d1d1d1 //серый (завершен)
     #ccccff //фиолетовый (планируемый)
     #ffccff //розовый (отемененный)*/
    
    private static final String CLR_AREA = "#fbca04";//#fae803";//#400080";
    private static final String CLR_FILL_AREA = "#ffff04";//"#ffff80";
    
    private static final String  CLR_WORK= "#b70000";//#ce0000";//#e80000";//"#880088";
    private static final String  CLR_FILL_WORK = "#ea0000";//#e10000";//#f90000";//#ff6a6a";
    
    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    final protected CommonImages commonImages = GWT.create(CommonImages.class);
    private List<Department> departments = new ArrayList<Department>(); //Список подразделений

    protected class DepartmentData extends BeanModel {

        public DepartmentData(Integer id, String name, Integer place, Integer placeWork) {
            set("id", id);
            set("name", name);
            set("place", place);
            set("placeWork", placeWork);
        }

        public DepartmentData(Integer id) {
            set("id", id);
            set("name", null);
            set("place", null);
            set("placeWork", null);
        }
    }

    private void initGrid() {
        pnlForGrid = new ContentPanel();
        pnlForGrid.setLayout(new FitLayout());
        pnlForGrid.setSize(400, 400);
        pnlForGrid.setHeaderVisible(false);

        store = new ListStore<BeanModel>();
        List<ColumnConfig> col = new ArrayList<ColumnConfig>();
        ColumnConfig colum1 = new ColumnConfig();
        colum1.setId("name");
        colum1.setWidth(300);
        colum1.setHeader("Подразделение");
        colum1.setMenuDisabled(true);
        TextField<String> text1 = new TextField<String>();
        text1.setAllowBlank(false);
        colum1.setEditor(new CellEditor(text1));
        col.add(colum1);

        ColumnConfig column2 = new ColumnConfig();
        column2.setId("place");
        column2.setWidth(300);
        column2.setHeader("Число работников");
        column2.setMenuDisabled(true);
        NumberField text2 = new NumberField();
        text2.setPropertyEditorType(Integer.class);
        text2.setAllowBlank(false);
        column2.setEditor(new CellEditor(text2));
        col.add(column2);

        ColumnConfig column3 = new ColumnConfig();
        column3.setId("placeWork");
        column3.setWidth(300);
        column3.setHeader("Число работников занятых в данный момент");
        column3.setMenuDisabled(true);
        NumberField text3 = new NumberField();
        text3.setPropertyEditorType(Integer.class);
        text3.setAllowBlank(false);
        column3.setEditor(new CellEditor(text3));
        col.add(column3);

        ColumnModel cm = new ColumnModel(col);
        grid = new EditorGrid<BeanModel>(store, cm);
        GridTools.enableGridMultiHeading(grid);
        GridTools.enableGridMultiColumn(grid);
        grid.setWidth(100);
        grid.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        grid.setStripeRows(true);
        grid.getView().setForceFit(true);
        grid.setStateful(true);
        grid.setBorders(true);
        grid.stopEditing();
        grid.setSelectionModel(new GridSelectionModel<BeanModel>());
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);

        pnlForGrid.add(grid);

        ToolBar tb = new ToolBar();
        SelectionListener<ButtonEvent> tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnDelete) {
                    onDelete();
                } else if (ce.getButton() == btnAdd) {
                    onAdd();
                }
            }
        };

        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.addSelectionListener(tbListener);

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);

        tb.add(btnAdd);
        tb.add(btnDelete);

        pnlForGrid.setTopComponent(tb);
    }

    private void onDelete() {

        BeanModel bm = grid.getSelectionModel().getSelectedItem();
        store.remove(bm);
    }

    private void onAdd() {
        grid.stopEditing();
        grid.getSelectionModel().deselectAll();
        int count = store.getCount();
        DepartmentData newRec = new DepartmentData(count + 1);
        store.insert(newRec, count);
        grid.getSelectionModel().select(newRec, false);
        grid.startEditing(store.indexOf(newRec), 0);
    }

    private void updateDepartments() {
        grid.stopEditing();
        store.commitChanges();
        departments.clear();
        List<BeanModel> models = store.getModels();
        for (BeanModel bean : models) {
            Department d = new Department();
            if ((bean.get("place") == null) || (bean.get("placeWork") == null)) {
                continue;
            }
            d.setName(bean.get("name").toString());
            d.setPlace((Integer) bean.get("place"));
            d.setPlaceWork((Integer) bean.get("placeWork"));
            departments.add(d);
        }
        store.commitChanges();
    }

    private void initDepartments() {
        departments.clear();

        Department d1 = new Department();
        d1.setName("Радиотехническая лаборатория ЦЕХ N4");
        d1.setPlace(50);
        d1.setPlaceWork(45);
        departments.add(d1);

        Department d2 = new Department();
        d2.setName("Трансформаторный участок ЦЕХ N7");
        d2.setPlace(15);
        d2.setPlaceWork(10);
        departments.add(d2);

        Department d3 = new Department();
        d3.setName("Сборочно-монтажный ЦЕХ N7");
        d3.setPlace(22);
        d3.setPlaceWork(18);
        departments.add(d3);

        Department d4 = new Department();
        d4.setName("Малярно-гальванический ЦЕХ N7");
        d4.setPlace(8);
        d4.setPlaceWork(6);
        departments.add(d4);

        Department d5 = new Department();
        d5.setName("Упаковки готовой продукции ЦЕХ N10");
        d5.setPlace(30);
        d5.setPlaceWork(28);
        departments.add(d5);

        Department d6 = new Department();
        d6.setName("Механо-заготовительный ЦЕХ N12");
        d6.setPlace(16);
        d6.setPlaceWork(10);
        departments.add(d6);

        List<DepartmentData> storeData = new ArrayList<DepartmentData>();
        Integer id = 1;

        for (Department d : departments) {
            storeData.add(new DepartmentData(id, d.getName(), d.getPlace(), d.getPlaceWork()));
            id++;
        }
        store.removeAll();
        store.add(storeData);
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

        Label lblOrg = new Label("Огранизация:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblOrg);
        cbOrgExecutor = new DGComboBox<Integer>("HLV_CB_ORGANIZATION", "MAIN:ORGANIZATION_ID", "MAIN:SHORT_NAME");
        cbOrgExecutor.setValidateOnBlur(Boolean.TRUE);
        cbOrgExecutor.setValueId(DEFAULT_ORG_ID);
        toolbar.add(cbOrgExecutor);

        Label lblLoad = new Label("Загрузка цехов:");
        lblLoad.setAutoWidth(true);
        toolbar.add(lblLoad);
        cbLoad = new CheckBox();
        toolbar.add(cbLoad);
        cbLoad.addListener(Events.Change, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                updateChart();
            }
        });
        panel.setTopComponent(toolbar);


        
        tabPanel = new TabPanel();
        tabPanel.setBorders(false);
        tabPanel.setBodyBorder(false);
        tabPanel.setAnimScroll(true);
        tabPanel.setTabScroll(true);
        BorderLayoutData borderData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        borderData.setMargins(new Margins(5));        
        panel.add(tabPanel, borderData); //помещаем в центр      
        
        itemChart = new TabItem();
        itemChart.setClosable(false);
        itemChart.setBorders(false);
        itemChart.setItemId("chart");
        itemChart.setText("Радиальная диаграмма");//tableName);
        itemChart.setLayout(new FitLayout());
        itemChart.setStyleAttribute("backgroundColor", "#DFE8F6");
        tabPanel.add(itemChart);
        
        itemBarChart = new TabItem();
        itemBarChart.setClosable(false);
        itemBarChart.setBorders(false);
        itemBarChart.setItemId("chart");
        itemBarChart.setText("Столбчатая диаграмма");//tableName);
        itemBarChart.setLayout(new FitLayout());
        itemBarChart.setStyleAttribute("backgroundColor", "#DFE8F6");
        tabPanel.add(itemBarChart);

        
        String url = "../chart/open-flash-chart.swf";
        chart = new Chart(url);
        chart.setHeight(100);        
        itemChart.add(chart);
        
        barСhart = new Chart(url);
        barСhart.setHeight(100);        
        itemBarChart.add(barСhart);        
        
        
        initGrid();
        initDepartments();
        chart.setChartModel(getChartModel());
        barСhart.setChartModel(getBarChartModel());

        BorderLayoutData d = new BorderLayoutData(Style.LayoutRegion.EAST);
        d.setMargins(new Margins(0, 0, 3, 0));
        d.setSplit(true);
        panel.add(pnlForGrid, d); //помещаем вниз

        return main;
    }

    public ChartModel getBarChartModel() {
        ChartModel cm = new ChartModel("", "font-size: 33px; font-family: Verdana;");
        cm.setBackgroundColour("#ffffff");
        cm.setDecimalSeparatorComma(true);
        XAxis xa = new XAxis();
        xa.getLabels().setColour("#000000");
        xa.setGridColour("#cccccc");
        xa.setColour("#000000");
        cm.setXAxis(xa);
         
        Integer max = 0;
        for (Department d : departments) {
            max = (max < d.getPlace()) ? d.getPlace() : max;
        }
        
        YAxis ya = new YAxis();
        ya.setRange(0, max+10);
        ya.setSteps(5);
        ya.setGridColour("#cccccc");
        ya.setColour("#000000");
        
        //cm.setYAxisLabelStyle(10, "#009900");
        cm.setYAxis(ya);
        
        Legend legend = new Legend(Position.TOP, false);
        legend.setShadow(false);
        legend.setPadding(10);
        cm.setLegend(legend);
        
        ArrayList<String> chartLabels = new ArrayList<String>();
 
        FilledBarChart bchartPlace = new FilledBarChart(CLR_AREA,CLR_AREA);//new FilledBarChart("#ffff06","#ffff06");//CLR_WORK, CLR_FILL_WORK);        
        //bchartPlace.setTooltip("#val# работников");
          bchartPlace.setText("Число работников");
        
        FilledBarChart bchartPlaceWork = new FilledBarChart(CLR_WORK,CLR_WORK);//new FilledBarChart("#ff0909","#ff0909");//, CLR_FILL_AREA);
        //bchartPlaceWork.setTooltip("#val#");
          // bchartPlaceWork.setTooltip("#val#  занятых в данный момент работников");
        bchartPlaceWork.setText("Число работников занятых в данный момент");

        for (Department d : departments) {
            //chartLabels.add(d.getName());
            com.extjs.gxt.charts.client.model.axis.Label l = new com.extjs.gxt.charts.client.model.axis.Label(d.getName(), 10);
            //l.setSize(10);
            l.setColour("#000000");
            xa.addLabels(l);
            //bchartPlaceWork.addValues(d.getPlaceWork());
            //bchartPlace.addValues(d.getPlace());

            com.extjs.gxt.charts.client.model.charts.BarChart.Bar barWork = new com.extjs.gxt.charts.client.model.charts.BarChart.Bar(d.getPlaceWork(),CLR_FILL_WORK);//"#ff0909");
            barWork.setTooltip(d.getPlaceWork().toString() + " из " + d.getPlace().toString() + " занятых в данный момент работников");
            bchartPlaceWork.addBars(barWork);
            
            com.extjs.gxt.charts.client.model.charts.BarChart.Bar bar = new com.extjs.gxt.charts.client.model.charts.BarChart.Bar(d.getPlace(),CLR_FILL_AREA);//"#ffff06");
            bar.setTooltip(d.getPlace().toString() + " работников");
            bchartPlace.addBars(bar);
            
            
            //bchartPlace.setTooltip(d.getPlace().toString() + " работников");
            //bchartPlaceWork.setTooltip(d.getPlaceWork().toString() + " из " + d.getPlace().toString() + " занятых в данный момент работников");

        }
        //xa.setLabels(chartLabels);
         
        
        cm.addChartConfig(bchartPlace);
        if (cbLoad.getValue()) {
        cm.addChartConfig(bchartPlaceWork);
        }
        return cm;
    }
    
    
    public ChartModel getChartModel() {
        ChartModel cm = new ChartModel("", "font-size: 33px; font-family: Verdana;");
        cm.setBackgroundColour("#ffffff");


        Legend legend = new Legend(Position.TOP, false);
        legend.setShadow(false);
        legend.setPadding(10);
        cm.setLegend(legend);


        RadarAxis ra = new RadarAxis();       
        ra.setMax(100);
        ra.setSteps(10);
        ra.setStroke(3); //ширина линий секторов
        ra.setColour("#040404"); //текст
        ra.setGridColour("#040404");//("#99ccff"); //линии
        cm.setRadarAxis(ra);
        
        ArrayList<String> chartLabels = new ArrayList<String>();
        AreaChart area = new AreaChart();
        //area.setFillAlpha(0.3f);
        area.setFillAlpha(0.9f);
        area.setColour(CLR_AREA);//темно 
        area.setFillColour(CLR_FILL_AREA);
        area.setLoop(true);
        area.setText("Число работников");

        AreaChart areaWork = new AreaChart();
        //areaWork.setFillAlpha(0.3f);
        areaWork.setFillAlpha(0.8f);
        areaWork.setColour(CLR_WORK);
        areaWork.setFillColour(CLR_FILL_WORK);
        areaWork.setLoop(true);
        areaWork.setText("Число работников занятых в данный момент");

        for (Department d : departments) {

            
            chartLabels.add(d.getName());
            Dot dot = new Dot();
            dot.setValue(100);
            dot.setHaloSize(0);
            //dot.setSize(0);
            dot.setTooltip(d.getPlace().toString() + " работников");
            area.addDots(dot);

            Dot dotWork = new Dot();
            dotWork.setValue(d.getPercentWork());
            dotWork.setHaloSize(0);
            //dotWork.setSize(0);
            dotWork.setTooltip(d.getPlaceWork().toString() + " из " + d.getPlace().toString() + " занятых в данный момент работников");
            areaWork.addDots(dotWork);
        }

        cm.addChartConfig(area);
        if (cbLoad.getValue()) {
            cm.addChartConfig(areaWork);
        }
        ra.setSpokeLabels(chartLabels);
                
        return cm;
    }

    public void updateChart() {
        updateDepartments();
        chart.setChartModel(getChartModel());
         barСhart.setChartModel(getBarChartModel());
    }
}
