package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.charts.ChartConfig;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.util.SwallowEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.modules.admin.stat.domain.StatItem;
import pst.arm.client.modules.admin.stat.lang.AdminStatConstants;
import pst.arm.client.modules.images.ArmImages;

public abstract class BaseChart extends LayoutContainer {

    protected AdminStatConstants constants = GWT.create(AdminStatConstants.class);
    protected ArmImages images = GWT.create(ArmImages.class);
    protected List<StatItem> stat = null;
    protected Date date;
    protected ListStore<ModelData> store;
    protected Grid<ModelData> grid;
    protected Chart chart, pieChart;
    protected Map<String, ChartConfig> chartConfigs = null;
    protected ToolBar chartToolbar;
    protected Button btnUpdateChart;
    protected ToggleButton btnFull, btnMonth, btnThreeMonth;
    protected Integer period = 1, prevPeriod = 1;
    protected Integer startStatIndex = 0;
    protected String[] colors = {
        "#CC0033", "#FF0099", "#999999", "#9900CC",
        "#006633", "#66CC00", "#66CC66", "#00FF99",
        "#00CCFF", "#3300CC", "#6600CC", "#CC00CC",
        "#FF0066", "#FF3300", "#663300"};

    public BaseChart() {
        initGrid();
        this.chartConfigs = new HashMap<String, ChartConfig>();
    }

    public BaseChart(List<StatItem> stat, Date date) {
        this.stat = stat;
        this.date = date;
        this.chartConfigs = new HashMap<String, ChartConfig>();
        initGrid();
        findStartIndex();
    }

    abstract public ChartModel getChartModel();

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        VBoxLayout layout = new VBoxLayout();
        layout.setPadding(new Padding(5));
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        setLayout(new BorderLayout());
        VBoxLayoutData flex = new VBoxLayoutData(new Margins(0, 0, 5, 0));
        flex.setFlex(1);
        ContentPanel cp = new ContentPanel(new FitLayout());
        cp.setHeading(constants.reportDataHeading());
        cp.add(grid);

        BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
        bld.setCollapsible(true);
        bld.setMargins(new Margins(5));
        add(cp, bld);

        VBoxLayoutData flex2 = new VBoxLayoutData(new Margins(0));
        flex2.setFlex(4);
        cp = new ContentPanel();

        HBoxLayout hlayout = new HBoxLayout();
        hlayout.setPadding(new Padding(5));
        hlayout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
        hlayout.setPack(BoxLayoutPack.START);
        cp.setLayout(hlayout);

        HBoxLayoutData hlayoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
        hlayoutData.setFlex(6);

        cp.setHeading(constants.reportChartHeading());

        SelectionListener<ButtonEvent> toggListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (btnMonth.equals(ce.getButton())) {
                    btnThreeMonth.toggle(Boolean.FALSE);
                    btnFull.toggle(Boolean.FALSE);
                    setPeriod(1);
                } else if (btnThreeMonth.equals(ce.getButton())) {
                    btnMonth.toggle(Boolean.FALSE);
                    btnFull.toggle(Boolean.FALSE);
                    setPeriod(3);
                } else if (btnFull.equals(ce.getButton())) {
                    btnMonth.toggle(Boolean.FALSE);
                    btnThreeMonth.toggle(Boolean.FALSE);
                    setPeriod(-1);
                }
            }
        };
        chartToolbar = new ToolBar();
        btnMonth = new ToggleButton("За месяц", toggListener);
        btnMonth.toggle(Boolean.TRUE);
        chartToolbar.add(btnMonth);

        btnThreeMonth = new ToggleButton("За 3 месяца", toggListener);
        chartToolbar.add(btnThreeMonth);

        btnFull = new ToggleButton("За все время", toggListener);
        chartToolbar.add(btnFull);

        chartToolbar.add(new SeparatorToolItem());

        btnUpdateChart = new Button(constants.reportChartBtnUpdate(), images.chart_bar(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                updateChart();
            }
        });
        chartToolbar.add(btnUpdateChart);
        cp.setTopComponent(chartToolbar);

        String url = "../chart/open-flash-chart.swf";
        chart = new Chart(url);
        chart.setBorders(true);
        chart.setChartModel(getChartModel());
        cp.add(chart, hlayoutData);
        if (hasAdditionalChart()) {
            hlayoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
            hlayoutData.setFlex(2);
            cp.add(getAdditionalChart(), hlayoutData);
        }
        bld = new BorderLayoutData(LayoutRegion.CENTER);
        bld.setMargins(new Margins(5));
        add(cp, bld);
    }

    protected void updateChart() {
        if (!grid.getSelectionModel().getSelection().isEmpty()) {
            if (period != prevPeriod) {
                findStartIndex();
                prepareStat();
                prevPeriod = period;

                chart.setChartModel(getChartModel());
            } else {
                List<ChartConfig> confs = new ArrayList<ChartConfig>();
                for (ModelData modelData : grid.getSelectionModel().getSelection()) {
                    confs.add(chartConfigs.get(modelData.get("id")));
                }
                chart.getChartModel().setChartConfigs(confs);
            }

            chart.refresh();
        }
    }

    private void initGrid() {
        store = new ListStore<ModelData>();

        final CheckBoxSelectionModel<ModelData> sm = new CheckBoxSelectionModel<ModelData>();
        sm.setSelectionMode(SelectionMode.MULTI);
        List<ColumnConfig> col = new ArrayList<ColumnConfig>();

        col.add(sm.getColumn());

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setWidth(300);
        column.setHeader((String) constants.reportDataColumns().get("data.name"));
        column.setMenuDisabled(true);
        col.add(column);

        column = new ColumnConfig();
        column.setId("count");
        column.setHeader((String) constants.reportDataColumns().get("data.count"));
        column.setWidth(100);
        column.setMenuDisabled(true);
        col.add(column);

        column = new ColumnConfig();
        column.setId("avg");
        column.setHeader((String) constants.reportDataColumns().get("data.avg"));
        column.setWidth(100);
        column.setMenuDisabled(true);
        col.add(column);

        ColumnModel cm = new ColumnModel(col);

        grid = new Grid<ModelData>(store, cm);
        grid.setSelectionModel(sm);
        grid.setStripeRows(true);
        grid.setLoadMask(true);
        grid.setAutoExpandColumn("name");
        grid.setBorders(false);
        grid.getView().setForceFit(true);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

    }

    public void prepareStat() {
    }

    ;

    /**
     * @return the stat
     */
    public List<StatItem> getStat() {
        return stat;
    }

    /**
     * @param stat the stat to set
     */
    public void setStat(List<StatItem> stat) {
        this.stat = stat;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    protected Boolean hasAdditionalChart() {
        return Boolean.FALSE;
    }

    protected Widget getAdditionalChart() {
        return null;
    }

    /**
     * @return the period
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    protected void findStartIndex() {
        startStatIndex = 0;
        if (period == -1) {
            return;
        }
        DateTimeFormat mf = DateTimeFormat.getFormat("MM");
        DateTimeFormat yf = DateTimeFormat.getFormat("yyyy");
        DateTimeFormat f = DateTimeFormat.getFormat("yyyyMM");

        Integer month = Integer.parseInt(mf.format(date));
        Integer year = Integer.parseInt(yf.format(date));
        period++;
        if (month - (period) <= 0) {
            month = 12 - (period - month);
            year--;
        } else {
            month -= period;
        }
        period--;
        String strDateFrom = "" + year + NumberFormat.getFormat("00").format(month);
        for (int i = stat.size() - 1; i >= 0; i--) {
            StatItem item = stat.get(i);
            if (f.format(item.getStartDate()).equals(strDateFrom)) {
                startStatIndex = i + 1;
                return;
            }
        }

    }

    protected class ChartData extends BeanModel {

        public ChartData(String id, String name, Integer count, Integer avg) {
            set("id", id);
            set("name", name);
            set("count", count);
            set("avg", avg);
        }
    }
}
