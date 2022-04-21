package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.LineChart;

class SessionChart extends BaseChart {

    public ChartModel getChartModel() {
        
        ChartModel cd = new ChartModel("Длительность сессии", "font-size: 14px; font-family: Verdana;");
        cd.setBackgroundColour("#ffffff");

        YAxis ya = new YAxis();
        ya.setRange(0, 50);
        ya.setSteps(10);
        ya.setGridColour("#ffffff");
        ya.setColour("#000000");
        cd.setYAxis(ya);


        XAxis xa = new XAxis();
        xa.setLabels("s1", "s2", "s3", "s4", "s5", "s6", "s7");
        cd.setXAxis(xa);

        LineChart line = new LineChart();
        line.setAnimateOnShow(true);
        line.setColour("#ff00ff");

        line.addValues(10, 20, 30, 40, 50, 40, 30);

        cd.addChartConfig(line);
        return cd;
    }
}
