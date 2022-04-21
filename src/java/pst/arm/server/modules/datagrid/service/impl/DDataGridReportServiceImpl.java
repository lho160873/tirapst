package pst.arm.server.modules.datagrid.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.common.reports.conditionbased.Report.ReportType;
import pst.arm.server.common.service.BaseReportService;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;
import pst.arm.server.modules.datagrid.reportgenerator.DDataGridStoreExtractor;
import pst.arm.server.modules.datagrid.reportgenerator.UserFormGenerator;
import pst.arm.server.modules.datagrid.service.DDataGridReportService;
import pst.arm.server.modules.datagrid.service.DDataGridService;
import pst.arm.server.utils.ReportUtils;

/**
 *
 * @author Ratmir Slepenkov, Mikhail Zakharov, wesStyle
 * @version 0.17.0
 */
@Service("ddataGridReportService")
public class DDataGridReportServiceImpl extends BaseReportService implements DDataGridReportService {

    private static Logger log = Logger.getLogger("DDataGridReportServiceImpl");
    private DDataGridService dDataGridService;
    @Autowired
    private JasperReportsService jasperReportsService;
    @Autowired
    private JRjavaXmlService javaXMLService;

    @Autowired
    public void setDDataGridService(DDataGridService service) {
        log.warn("DDataGridReportServiceImpl::setDDataGridService");
        this.dDataGridService = service;
    }

    @Override
    public FileObjectDescriptor generateReport(String tableName, DataGridSearchCondition condition, ReportType reportType) {
        log.warn("DDataGridReportServiceImpl::generateReport BEGIN");
        if (dDataGridService == null) {
            log.warn("DDataGridReportServiceImpl::generateReport NULL");
        } else {
            log.warn("DDataGridReportServiceImpl::generateReport NOT NULL");
        }

        DDataGridStoreExtractor d = new DDataGridStoreExtractor(tableName, dDataGridService, condition);
        log.warn("DDataGridReportServiceImpl::generateReport 1");
        return genetateReport(d, reportType, "archive_store_entity_report");

        //return genetateReport(new DDataGridStoreExtractor(dDataGridService, condition), reportType, "archive_store_entity_report");
        //throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public FileObjectDescriptor generateReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, ReportType reportType) {
        return jasperReportsService.genetateReport(tableName, domainHashMapList, reportType);
    }

    @Override
    public FileObjectDescriptor generateReportWithConnection(String tableName, HashMap<String, String> hashMap, ReportType reportType) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            FileObjectDescriptor result = javaXMLService.genetateReportWithConnection(tableName, hashMap, reportType, connection);
            return result;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                log.warn(ex.getMessage());
            }
        }
    }

    @Override
    public FileObjectDescriptor generateReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, ReportType reportType) {
        return jasperReportsService.genetateReportWorkshopLoad(tableName, headers, domainHashMapList, reportType);
    }
    @Autowired
    @Qualifier("dsArm")
    DataSource dataSource;
    @Autowired
    ServletContext context;
    @Autowired
    DDataGridDAO dao;

    public FileObjectDescriptor generateJasperReport(HashMap<String, Object> params, DataGridSearchCondition condition, String reportTemplate, String format, DTable table) {
        try {
            JasperPrint report;

            log.warn("DDataGridReportServiceImpl::connecting and filling report...");
            Connection connection = dataSource.getConnection();

            StringBuilder sb = new StringBuilder();
            sb.append(dao.getQueryWhere(table, condition));
            if (sb.length() > 0) {
                sb.insert(0, "  AND ");
                params.put("searchers", sb.toString().replace("WHERE", " "));
            }

            report = JasperFillManager.fillReport(context.getRealPath("/WEB-INF/reports/" + reportTemplate + ".jasper"), params, connection);
            log.warn("DDataGridReportServiceImpl::done");

            FileObjectDescriptor fod = null;

            if (format.toUpperCase().equals("RTF")) {
                fod = generateRTF(report);
            } else if (format.toUpperCase().equals("XLS")) {
                fod = generateXLS(report);
            } else if (format.toUpperCase().equals("PDF")) {
                fod = generatePDF(report);
            }

            connection.close();
            return fod;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private FileObjectDescriptor generateXLS(JasperPrint jp) throws JRException {
        JRXlsExporter xlsExporter = new JRXlsExporter();
        final ByteArrayOutputStream xlsStream = new ByteArrayOutputStream();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        xlsExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsStream);
        xlsExporter.exportReport();

        FileObjectDescriptor fod = new FileObjectDescriptor(xlsStream.toByteArray());
        //fod.setFileName("jasperreport-xls");
        fod.setFileContentType("application/vnd.ms-excel");
        fod.setFileExt("xls");
        return fod;
    }

    private FileObjectDescriptor generatePDF(JasperPrint jp) throws JRException {
        byte[] binar = JasperExportManager.exportReportToPdf(jp);
        FileObjectDescriptor fod = new FileObjectDescriptor(binar);
        fod.setFileContentType("application/pdf");
        fod.setFileExt("pdf");
        return fod;
    }

    private FileObjectDescriptor generateRTF(JasperPrint jp) throws JRException {
        JRRtfExporter rtfExporter = new JRRtfExporter();
        final ByteArrayOutputStream rtfStream = new ByteArrayOutputStream();
        rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        rtfExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        rtfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, rtfStream);
        rtfExporter.exportReport();

        FileObjectDescriptor fod = new FileObjectDescriptor(rtfStream.toByteArray());
        //fod.setFileName("jasperreport-rtf");
        fod.setFileContentType("application/rtf");
        fod.setFileExt("rtf");
        return fod;
    }

    //TODO не забыть убрать в другое место
    @Override
    public Map<String, Object> getRhUserForm() {
        Map<String, Object> result = new HashMap<String, Object>();

        UserFormGenerator generator = new UserFormGenerator();
        FileObjectDescriptor fod = generator.generateReport();
        List<String> errors = generator.getMessages();

        result = ReportUtils.createResultMap(fod, errors);

        return result;
    }

    @Override
    public DDataGridService getService() {
        return dDataGridService;
    }
}