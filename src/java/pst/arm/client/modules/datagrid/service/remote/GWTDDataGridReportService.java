package pst.arm.client.modules.datagrid.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.HashMap;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;

/**
 *
 * @author Ratmir Slepenkov, Mikhail Zakharov, wesStyle
 * @version 0.17.0
 */
@RemoteServiceRelativePath("service/ddataGridReportService")
public interface GWTDDataGridReportService extends RemoteService {

    ReportGeneratorResult generateHtmlArchiveStoreEntityReport(String tableName, DataGridSearchCondition condition, String reportType);

    ReportGeneratorResult generateHtmlArchiveStoreEntityReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, String reportType);

    ReportGeneratorResult generateHtmlArchiveStoreEntityReportWithConnection(String tableName, HashMap<String, String> hashMap, String reportType);

    ReportGeneratorResult generateHtmlArchiveStoreEntityReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, String reportType);

    ReportGeneratorResult generateJasperReport(String reportTemplate, String reportExportFormat, HashMap<String, Object> params, DataGridSearchCondition condition, DTable table);
}