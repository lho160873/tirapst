package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.ChartConfig;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.dots.SolidDot;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.*;
import pst.arm.client.modules.admin.stat.domain.StatAction;
import pst.arm.client.modules.admin.stat.domain.StatItem;

public class ActivityChart extends BaseChart {

    protected List<ActivityChartData> chartData;
    private List<String> chartLabels;

    public ActivityChart() {
        super();
    }

    public ActivityChart(List<StatItem> stat, Date date) {
        super(stat, date);
        chartData = new ArrayList<ActivityChartData>();
        chartLabels = new ArrayList<String>();
    }

    @Override
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
        ya.setRange(0, getMaxStatValue());
        ya.setSteps(10);
        ya.setGridColour("#D8D8D8 ");
        ya.setColour("#000000");
        cm.setYAxisLabelStyle(10, "#00000");
        cm.setYAxis(ya);

        LineChart chartUsers = new LineChart();
        chartUsers.setColour("#00ffff");
        chartUsers.setText("Пользователи");

        LineChart chartIP = new LineChart();
        chartIP.setColour("#FF9900");
        chartIP.setText("IP адреса");

        LineChart chartSession = new LineChart();
        chartSession.setColour("#ff0000");
        chartSession.setText("Сессии");

        LineChart chartPage = new LineChart();
        chartPage.setColour("#00ff00");
        chartPage.setText("Просмотры");

        LineChart chartService = new LineChart();
        chartService.setColour("#0000ff");
        chartService.setText("Вызовы сервисов");


        for (ActivityChartData item : chartData) {
            chartSession.addDots(new SolidDot(item.getSessions()));
            chartPage.addDots(new SolidDot(item.getPages()));
            chartService.addDots(new SolidDot(item.getServices()));
            chartUsers.addDots(new SolidDot(item.getUsers().size()));
            chartIP.addDots(new SolidDot(item.getIps().size()));
        }

        chartConfigs.put("ip", chartIP);
        chartConfigs.put("user", chartUsers);
        chartConfigs.put("session", chartSession);
        chartConfigs.put("page", chartPage);
        chartConfigs.put("service", chartService);

        Iterator it = chartConfigs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            cm.addChartConfig((ChartConfig) pairs.getValue());
        }

        return cm;
    }

    @Override
    public void prepareStat() {
        chartData.clear();
        chartLabels.clear();

        DateTimeFormat df = DateTimeFormat.getFormat("dd/MM");
        String key = "";
        ActivityChartData dataItem = null;
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
                    dataItem = new ActivityChartData(key, 0, 0, 0);
                    chartData.add(dataItem);
                    chartLabels.add(key);
                }
                dataItem.addSession();
                dataItem.getUsers().add(item.getLogin());
                dataItem.getIps().add(item.getIp());
                for (StatAction action : item.getActions()) {
                    if (action.getType().equals("PAGE")) {
                        dataItem.addPage();
                    } else if (action.getType().equals("SERVICE")) {
                        dataItem.addService();
                    }
                }
            }
            loadStore();
        }
    }

    private Number getMaxStatValue() {
        Integer max = 0;
        for (ActivityChartData item : chartData) {
            if (item.getServices() > max) {
                max = item.getServices();
            }
            if (item.getPages() > max) {
                max = item.getPages();
            }
            if (item.getSessions() > max) {
                max = item.getSessions();
            }
        }
        return max + 10;
    }

    private void loadStore() {
        DateTimeFormat df = DateTimeFormat.getFormat("dd/MM");
        Integer totalUsers = 0;
        Integer totalIP = 0;
        Integer totalSession = 0;
        Integer totalPages = 0;
        Integer totalServices = 0;
        ActivityChartData todayStat = new ActivityChartData();
        for (ActivityChartData item : chartData) {
            totalIP += item.getIps().size();
            totalUsers += item.getUsers().size();
            totalSession += item.getSessions();
            totalPages += item.getPages();
            totalServices += item.getServices();
            if (item.getStrDate().equals(df.format(date))) {
                todayStat = item;
            }
        }

        List<ChartData> storeData = new ArrayList<ChartData>();
        storeData.add(new ChartData("user", "Пользователи", todayStat.getUsers().size(), totalUsers / chartData.size()));
        storeData.add(new ChartData("ip", "IP адреса", todayStat.getIps().size(), totalIP / chartData.size()));
        storeData.add(new ChartData("page", "Просмотры", todayStat.getPages(), totalPages / chartData.size()));
        storeData.add(new ChartData("session", "Сессии", todayStat.getSessions(), totalSession / chartData.size()));
        storeData.add(new ChartData("service", "Сервисы", todayStat.getServices(), totalServices / chartData.size()));

        store.removeAll();
        store.add(storeData);
        store.sort("count", SortDir.DESC);

        grid.getSelectionModel().selectAll();
    }

    private class ActivityChartData {

        private String strDate = null;
        private Integer sessions, pages, services;
        private Set<String> users, ips;

        public ActivityChartData() {
            this.sessions = 0;
            this.pages = 0;
            this.services = 0;
            users = new HashSet<String>();
            ips = new HashSet<String>();
        }

        public ActivityChartData(String strDate, Integer sessions, Integer pages, Integer services) {
            this.strDate = strDate;
            this.sessions = sessions;
            this.pages = pages;
            this.services = services;
            users = new HashSet<String>();
            ips = new HashSet<String>();
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

        /**
         * @return the sessions
         */
        public Integer getSessions() {
            return sessions;
        }

        /**
         * @param sessions the sessions to set
         */
        public void setSessions(Integer sessions) {
            this.sessions = sessions;
        }

        /**
         * @return the pages
         */
        public Integer getPages() {
            return pages;
        }

        /**
         * @param pages the pages to set
         */
        public void setPages(Integer pages) {
            this.pages = pages;
        }

        /**
         * @return the services
         */
        public Integer getServices() {
            return services;
        }

        /**
         * @param services the services to set
         */
        public void setServices(Integer services) {
            this.services = services;
        }

        public void addSession() {
            this.sessions++;
        }

        public void addPage() {
            this.pages++;
        }

        public void addService() {
            this.services++;
        }

        /**
         * @return the users
         */
        public Set<String> getUsers() {
            return users;
        }

        /**
         * @param users the users to set
         */
        public void setUsers(Set<String> users) {
            this.users = users;
        }

        /**
         * @return the ips
         */
        public Set<String> getIps() {
            return ips;
        }

        /**
         * @param ips the ips to set
         */
        public void setIps(Set<String> ips) {
            this.ips = ips;
        }
    }
}
