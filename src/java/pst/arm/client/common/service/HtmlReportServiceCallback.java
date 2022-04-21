package pst.arm.client.common.service;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;

/**
 * If report generation success open report in new window.
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public class HtmlReportServiceCallback extends ReportServiceCallback{

    public HtmlReportServiceCallback(Component parent) {
        super(parent);
    }

    @Override
    protected void doReportRequest(ReportGeneratorResult result) {
        Window.open(GWT.getHostPageBaseURL() + "report.htm?fileId=" + result.getId(), "_blank",
                "width=800,height=600,directories=0,titlebar=0,toolbar=0,location=0,status=0,menubar=0,resizable=1,scrollbars=1");
    }
    
}
