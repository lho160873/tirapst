package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.ChartConfig;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.charts.client.model.charts.dots.SolidDot;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pst.arm.client.modules.admin.stat.domain.StatAction;
import pst.arm.client.modules.admin.stat.domain.StatItem;

/**
 *
 * @author Alexandr Kozhin
 */
public class PagesChart extends BaseChart {

    protected Integer colorIndex = -1;
    protected List<PagesChartData> chartData;
    private List<String> chartLabels;
    private Set<String> pageNames;
    private Map<String, LineChart> pageCharts;
    private String type = "PAGE"; // PAGE || SERVICE
    private Map<String, Integer> totalStat;

    public PagesChart() {
        super();
    }

    public PagesChart(List<StatItem> stat, Date date, String type) {
        super(stat, date);
        this.type = type;
        chartData = new ArrayList<PagesChartData>();
        chartLabels = new ArrayList<String>();
        pageNames = new HashSet<String>();
    }

    public ChartModel getChartModel() {
        ChartModel cm = new ChartModel();//"","font-size: 14px; font-family: Verdana;"
        cm.setBackgroundColour("#eeffee");
        cm.setDecimalSeparatorComma(true);

        Legend legend = new Legend(Position.RIGHT, false);
        legend.setShadow(false);
        legend.setPadding(10);

        cm.setLegend(legend);

        XAxis xa = new XAxis();
        xa.setLabels(chartLabels);
        xa.getLabels().setColour("#00000");
        xa.setGridColour("#D8D8D8 ");
        xa.setColour("#00000");
        cm.setXAxis(xa);
        YAxis ya = new YAxis();
        ya.setSteps(10);
        ya.setGridColour("#D8D8D8 ");
        ya.setColour("#000000");
        cm.setYAxisLabelStyle(10, "#00000");
        cm.setYAxis(ya);
        LineChart chart = null;
        pageCharts = new HashMap<String, LineChart>();
        for (String name : pageNames) {
            chart = new LineChart();
            chart.setColour(getColor());
            chart.setText(name);
            pageCharts.put(name, chart);
            chartConfigs.put(name, chart);
        }

        Integer range = 0;
        Integer value = null;
        for (PagesChartData item : chartData) {
            for (String name : pageNames) {
                value = item.getPageStat(name);
                pageCharts.get(name).addDots(new SolidDot(value));
                if (value > range) {
                    range = value;
                }
            }
        }
        ya.setRange(0, range + 5);
        for (String name : pageNames) {
            cm.addChartConfig(pageCharts.get(name));
        }
        return cm;
    }

    @Override
    protected Boolean hasAdditionalChart() {
        return Boolean.TRUE;
    }

    @Override
    protected Widget getAdditionalChart() {
        PieChart pie = new PieChart();
        pie.setAlpha(0.8f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent# ( #val# )");
        List<String> pieColors = new ArrayList<String>();
        for (String name : pageNames) {
            pieColors.add(pageCharts.get(name).getColour());
        }
        pie.setColours(pieColors);
        pie.setGradientFill(true);

        ChartModel cm = new ChartModel("",
                "font-size: 14px; font-family: Verdana; text-align: center;");
        cm.setBackgroundColour("#fffff5");
        Legend lg = new Legend(Position.RIGHT, true);
        lg.setPadding(10);
        cm.setLegend(lg);
        for (String name : pageNames) {
            pie.addSlices(new PieChart.Slice(totalStat.get(name) / chartData.size(), name));
        }
        cm.addChartConfig(pie);

        String url = "../chart/open-flash-chart.swf";
        pieChart = new Chart(url);
        pieChart.setBorders(true);
        pieChart.setChartModel(cm);

        return pieChart;
    }

    @Override
    protected void updateChart() {
        super.updateChart();
        PieChart pie = new PieChart();
        pie.setAlpha(0.8f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent# ( #val# )");
        List<String> pieColors = new ArrayList<String>();
        for (ModelData modelData : grid.getSelectionModel().getSelection()) {
            pieColors.add(pageCharts.get(modelData.get("id")).getColour());
        }
        pie.setColours(pieColors);
        pie.setGradientFill(true);
        for (ModelData modelData : grid.getSelectionModel().getSelection()) {
            pie.addSlices(new PieChart.Slice(totalStat.get(modelData.get("id")) / chartData.size(), (String) modelData.get("id")));
        }
        List<ChartConfig> confs = new ArrayList<ChartConfig>();
        confs.add(pie);
        pieChart.getChartModel().setChartConfigs(confs);
        pieChart.refresh();
    
    }

    @Override
    public void prepareStat() {
        chartData.clear();
        chartLabels.clear();
        pageNames.clear();

        DateTimeFormat df = DateTimeFormat.getFormat("dd/MM");
        String key = "";
        PagesChartData dataItem = null;
        Boolean lastIter = false;
        if (getStat() != null) {
            StatItem item;
            for (int i = this.startStatIndex; i < stat.size(); i++) {
                item = stat.get(i);
                if (chartData.isEmpty() || !key.equals(df.format(item.getStartDate()))) {
                    if (lastIter) {
                        break;
                    }
                    lastIter = (df.format(item.getStartDate()).equals(df.format(date)));

                    key = df.format(item.getStartDate());
                    dataItem = new PagesChartData(key, new HashMap<String, Integer>());
                    chartData.add(dataItem);
                    chartLabels.add(key);
                }
                for (StatAction action : item.getActions()) {
                    if (action.getType().equals(this.type)) {
                        pageNames.add(action.getUrl());
                        dataItem.addPage(action.getUrl());
                    }
                }
            }
            loadStore();
        }
    }

    private Number getMaxStatValue() {
        Integer max = 100;
        for (PagesChartData item : chartData) {
        }
        return max + 10;
    }

    private void loadStore() {
        DateTimeFormat df = DateTimeFormat.getFormat("dd/MM");
        totalStat = new HashMap<String, Integer>();
        for (String name : pageNames) {
            totalStat.put(name, 0);
        }

        PagesChartData todayStat = new PagesChartData();
        for (PagesChartData item : chartData) {
            for (String name : pageNames) {
                totalStat.put(name, totalStat.get(name) + item.getPageStat(name));
            }
            if (item.getStrDate().equals(df.format(date))) {
                todayStat = item;
            }
        }

        List<ChartData> storeData = new ArrayList<ChartData>();
        for (String name : pageNames) {
            storeData.add(new ChartData(name, name, todayStat.getPageStat(name), totalStat.get(name) / chartData.size()));
        }

        store.removeAll();
        store.add(storeData);
        store.sort("count", SortDir.DESC);

        grid.getSelectionModel().selectAll();
    }

    private String getColor() {
        if (++this.colorIndex == this.colors.length) {
            this.colorIndex = 0;
        }
        return this.colors[this.colorIndex];
    }

    private class PagesChartData {

        private String strDate = null;
        private Map<String, Integer> pagesStat;

        public PagesChartData() {
            this.pagesStat = new HashMap<String, Integer>();
        }

        public PagesChartData(String strDate, Map<String, Integer> map) {
            this.strDate = strDate;
            this.pagesStat = map;
        }

        public Integer getPageStat(String name) {
            if (pagesStat.containsKey(name)) {
                return pagesStat.get(name);
            }
            return 0;
        }

        public void addPage(String name) {
            if (pagesStat.containsKey(name)) {
                pagesStat.put(name, pagesStat.get(name) + 1);
            } else {
                pagesStat.put(name, 1);
            }
        }

        /**
         * @return the pagesStat
         */
        public Map<String, Integer> getPagesStat() {
            return pagesStat;
        }

        /**
         * @param pagesStat the pagesStat to set
         */
        public void setPagesStat(Map<String, Integer> pagensStat) {
            this.pagesStat = pagesStat;
        }

        /**
         * @return the strDate
         */
        public String getStrDate() {
            return strDate;
        }

        /**
         * @param strDate the strDate to set
         */
        public void setStrDate(String strDate) {
            this.strDate = strDate;
        }
    }
}
