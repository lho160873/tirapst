package pst.arm.client.common.service;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;

/**
 * If report generation success download report file.
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public class FileReportServiceCallback extends ReportServiceCallback{

    public FileReportServiceCallback(Component parent) {
        super(parent);
    }
    
    @Override
    protected void doReportRequest(ReportGeneratorResult result) {
        Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + result.getId());
    }
}
