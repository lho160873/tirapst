package pst.arm.server.modules.datagrid.service.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridReportService;
import pst.arm.server.common.reports.conditionbased.Report.ReportType;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.DDataGridReportService;
import pst.arm.server.utils.ReportUtils;

/**
 *
 * @author Ratmir Slepenkov, Mikhail Zakharov, wesStyle
 * @version 0.17.0
 */
@Service("GWTDDataGridReportService")
public class GWTDDataGridReportServiceImpl extends GWTController implements GWTDDataGridReportService {

    public enum TypeOfClientsAnalysisReports {

        Report1DoesNotNeverTreated,
        Report2CurrentlyScheduled,
        Report3TransferredToRequest,
        Report4CurrentlyClient,}
    private DDataGridReportService reportService;
    private static Logger log = Logger.getLogger("GWTDDataGridReportServiceImpl");

    @Autowired
    public void setReportService(DDataGridReportService service) {
        log.warn("GWTDDataGridReportServiceImpl::setReportService");
        this.reportService = service;
    }

    @Override
    public ReportGeneratorResult generateHtmlArchiveStoreEntityReport(String tableName, DataGridSearchCondition condition, String reportType) {
        //return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, condition, ReportType.PDF),
        //        getSession());
        return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, condition, ReportType.valueOf(reportType)),
                getSession());
    }

    @Override
    public ReportGeneratorResult generateHtmlArchiveStoreEntityReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, String reportType) {
        //return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, condition, ReportType.PDF),
        //        getSession());
        return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, domainHashMapList, ReportType.valueOf(reportType)),
                getSession());
    }

    @Override
    public ReportGeneratorResult generateHtmlArchiveStoreEntityReportWithConnection(String tableName, HashMap<String, String> hashMap, String reportType) {
        //return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, condition, ReportType.PDF),
        //        getSession());
        return ReportUtils.putReportIntoSession(reportService.generateReportWithConnection(tableName, hashMap, ReportType.valueOf(reportType)),
                getSession());
    }

    @Override
    public ReportGeneratorResult generateHtmlArchiveStoreEntityReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, String reportType) {
        //return ReportUtils.putReportIntoSession(reportService.generateReport(tableName, condition, ReportType.PDF),
        //        getSession());
        return ReportUtils.putReportIntoSession(reportService.generateReportWorkshopLoad(tableName, headers, domainHashMapList, ReportType.valueOf(reportType)),
                getSession());
    }

    @Override
    public ReportGeneratorResult generateJasperReport(String reportTemplate, String format, HashMap<String, Object> params, DataGridSearchCondition condition, DTable table) {
        log.warn("GWTDDataGridReportServiceImpl::generateJasperReport");
        return ReportUtils.putReportIntoSession(reportService.generateJasperReport(params, condition, reportTemplate, format, table), getSession());
    }
}