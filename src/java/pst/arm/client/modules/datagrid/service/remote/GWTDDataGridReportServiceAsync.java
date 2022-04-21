package pst.arm.client.modules.datagrid.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
public interface GWTDDataGridReportServiceAsync {

    void generateHtmlArchiveStoreEntityReport(String tableName, DataGridSearchCondition condition, String reportType, AsyncCallback<ReportGeneratorResult> callback);

    void generateHtmlArchiveStoreEntityReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, String reportType, AsyncCallback<ReportGeneratorResult> callback);

    void generateHtmlArchiveStoreEntityReportWithConnection(String tableName, HashMap<String, String> hashMap, String reportType, AsyncCallback<ReportGeneratorResult> callback);

    void generateHtmlArchiveStoreEntityReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, String reportType, AsyncCallback<ReportGeneratorResult> callback);

    void generateJasperReport(String reportTemplate, String reportExportFormat, HashMap reportParams, DataGridSearchCondition condition, DTable table, AsyncCallback<ReportGeneratorResult> callback);
}