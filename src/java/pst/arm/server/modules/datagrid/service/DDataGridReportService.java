package pst.arm.server.modules.datagrid.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.common.reports.conditionbased.Report.ReportType;

/**
 *
 * @author Ratmir Slepenkov, Mikhail Zakharov, wesStyle
 * @version 0.17.0
 */
public interface DDataGridReportService {

    FileObjectDescriptor generateReport(String tableName, DataGridSearchCondition condition, ReportType reportType);

    FileObjectDescriptor generateReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, ReportType reportType);

    FileObjectDescriptor generateReportWithConnection(String tableName, HashMap<String, String> hashMap, ReportType reportType);

    FileObjectDescriptor generateReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, ReportType reportType);

    FileObjectDescriptor generateJasperReport(HashMap<String, Object> params, DataGridSearchCondition condition, String reportTemplate, String format, DTable table);

    public Map<String, Object> getRhUserForm(); //пример формирования отчета на основе word-го файла шаблона ( TODO потом вынести в другое место)

    DDataGridService getService();
}