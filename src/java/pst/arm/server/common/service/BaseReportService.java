package pst.arm.server.common.service;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.server.common.reports.conditionbased.BaseExtractor;
import pst.arm.server.common.reports.conditionbased.Report;
import pst.arm.server.common.reports.conditionbased.ReportGenerator;

/**
 *
 * @author Mikhail Zakharov, Ratmir Slepenkov
 * @version 0.17.0
 */
public abstract class BaseReportService {

    private static Logger log = Logger.getLogger("DDataGridReportServiceImpl");

    protected FileObjectDescriptor genetateReport(BaseExtractor extractor, Report.ReportType reportType, String fileName) {
        try {
            log.warn("BaseReportService::genetateReport BEGIN");
            ReportGenerator generator = new ReportGenerator();
            Report report = extractor.generateReport();
            log.warn("BaseReportService::genetateReport 1");
            report.setReportType(reportType);
            log.warn("BaseReportService::genetateReport 2");
            if (report != null) {
                log.warn("BaseReportService::genetateReport 3");
                FileObjectDescriptor fod = generator.generateReport(report);
                log.warn("BaseReportService::genetateReport 4");
                if (StringUtils.isEmpty(fileName)) {
                    log.warn("BaseReportService::genetateReport 5");
                    fod.setFileName(fileName);
                }
                log.warn("BaseReportService::genetateReport END");
                return fod;
            }
        } catch (IOException ex) {
        } catch (DocumentException ex) {
        }
        return null;
    }
}