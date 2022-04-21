package pst.arm.client.modules.admin.stat.lang;

import java.util.Map;

/**
 *
 * @author kozhin
 */
public interface AdminStatConstants extends com.google.gwt.i18n.client.Constants {

    @Key("adminstat.heading")
    String heading();

    @Key("adminstat.logSourceHeading")
    String logSourceHeading();

    @Key("adminstat.statHeading")
    String statHeading();

    @Key("adminstat.report.heading")
    String reportHeading();

    @Key("adminstat.report.tree")
    Map reportTree();

    @Key("adminstat.report.data.heading")
    String reportDataHeading();

    @Key("adminstat.report.chart.heading")
    String reportChartHeading();

    @Key("adminstat.report.chart.btnupdate")
    String reportChartBtnUpdate();

    @Key("adminstat.report.data.columns")
    Map reportDataColumns();
}
