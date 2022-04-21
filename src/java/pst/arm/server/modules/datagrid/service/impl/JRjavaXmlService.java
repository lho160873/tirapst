package pst.arm.server.modules.datagrid.service.impl;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.server.common.DTableMapManager;
import pst.arm.server.common.reports.conditionbased.Report;
import pst.arm.server.modules.reportbuilder.object.Row;
import pst.arm.server.modules.reportbuilder.object.search.ReportBuilderSearchCondition;
import pst.arm.server.modules.reportbuilder.service.ReportBuilderService;

@Service
public class JRjavaXmlService {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("JasperReportsService");
    private List rowsArray[][] = new ArrayList[3][3];
    private int xAlbumArray[] = {10, 270, 550};
    private int xArray[] = {15, 0, 330};
    private int yArray[] = {10, 0, 0, 0};
    @Autowired
    ServletContext context;
    @Autowired
    private ReportBuilderService reportBuilderService;

    public JRjavaXmlService() {
    }

    public FileObjectDescriptor genetateReportWithConnection(String tableName, HashMap<String, String> map, Report.ReportType reportType, Connection connection) {

        String companyId;
        companyId = map.get("cmpId");
        ReportBuilderSearchCondition condition = new ReportBuilderSearchCondition();
        condition.setCompanyId(companyId);
        List rows = reportBuilderService.getRows(condition);

        try {
            DTable table = DTableMapManager.getInstance().getTable(tableName);

            FastReportBuilder drb = new FastReportBuilder();

            String ocpId;
            String fileNumber;
            String workName;


            ocpId = map.get("ocpId");
            fileNumber = map.get("fileNumber");
            workName = map.get("workName");


            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            DynamicReport dr = drb.build();
            Map params = new HashMap();
            JasperReport jr;


            boolean isNewCell = false;
            List tempList = new ArrayList();
            int id = - 1;
            int x = -1;
            int y = -1;

            int yOffset = 0;
            int yMax = 0;

            for (Object object : rows) {
                Row row = (Row) object;
                if (id != row.getHeaderSpecId() && id != -1) {
                    isNewCell = true;
                }

                if (isNewCell) {
                    if (y != row.getY()) {
                        if (yMax < yOffset) {
                            yMax = yOffset;
                        }
                        yArray[row.getY()] = yMax;
                        yMax = 0;
                        yOffset = 0;
                    }
                    if (x != row.getX()) {
                        if (yMax < yOffset) {
                            yMax = yOffset;
                        }
                        yOffset = 0;
                    }

                    rowsArray[x][y] = tempList;
                    tempList = new ArrayList();
                    isNewCell = false;
                }

                tempList.add(row);
                yOffset += row.getFontSize() + 5;
                id = row.getHeaderSpecId();
                x = row.getX() - 1;
                y = row.getY() - 1;
            }

            if (yMax < yOffset) {
                yMax = yOffset;
            }
            yArray[y + 1] = yMax;
            rowsArray[x][y] = tempList;

            yArray[1] += yArray[0] + 10;
            yArray[2] += yArray[1] + 10;
            yArray[3] += yArray[2] + 10;
            yArray[3] = yArray[y + 1];

            log.error(Arrays.toString(xAlbumArray));
            log.error(Arrays.toString(xArray));
            log.error(Arrays.toString(yArray));

            JasperReport template1= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail1.jrxml"));
            JasperReport template1last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail1last.jrxml"));
            JasperReport tempalte2= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail2.jrxml"));
            JasperReport template2last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail2last.jrxml"));
            JasperReport tempalte3= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail3.jrxml"));
            JasperReport template3last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail3last.jrxml"));
            JasperReport tempalte4= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail4.jrxml"));
            JasperReport template4last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail4last.jrxml"));
            JasperReport tempalte5= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail5.jrxml"));
            JasperReport template5last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail5last.jrxml"));
            JasperReport template6last= JasperCompileManager.compileReport(context.getRealPath("/WEB-INF/reports/subreports/OcpFullForMeansOrgDetail6last.jrxml"));
            
            if ("OCP_VO".equals(tableName.toUpperCase()) && "3".equals(fileNumber)) {
                params.put("myCaption", table.getCaption());
                params.put("mySubtitle", "Создано1 " + sf.format(new Date()));
                params.put("ocpId", Integer.parseInt(ocpId));
                params.put("fileNumber", fileNumber);
                params.put("template1", template1);
                params.put("template1last", template1last);
                params.put("template2", tempalte2);
                params.put("template2last", template2last);
                params.put("template3", tempalte3);
                params.put("template3last", template3last);
                params.put("template4", tempalte4);
                params.put("template4last", template4last);
                params.put("template5", tempalte5);
                params.put("template5last", template5last);
                params.put("template6last", template6last);
                params.put("workName", workName);
                params.put("cmpId", companyId);
                JasperDesign jasperDesign = createDesignOCP("/WEB-INF/reports/OcpFullForPlanOrg.jrxml");
                jr = JasperCompileManager
                        .compileReport(jasperDesign);
            } else if ("OCP_VO".equals(tableName.toUpperCase()) && "4".equals(fileNumber)) {
                params.put("myCaption", table.getCaption());
                params.put("mySubtitle", "Создано2 " + sf.format(new Date()));
                params.put("ocpId", Integer.parseInt(ocpId));
                params.put("fileNumber", fileNumber);
                params.put("template1", template1);
                params.put("template1last", template1last);
                params.put("template2", tempalte2);
                params.put("template2last", template2last);
                params.put("template3", tempalte3);
                params.put("template3last", template3last);
                params.put("template4", tempalte4);
                params.put("template4last", template4last);
                params.put("template5", tempalte5);
                params.put("template5last", template5last);
                params.put("template6last", template6last);
                params.put("workName", workName);
                params.put("cmpId", companyId);
                JasperDesign jasperDesign = createDesignOCP("/WEB-INF/reports/OcpFullForMeansOrg.jrxml");
                jr = JasperCompileManager
                        .compileReport(jasperDesign);
            } else {
                jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);

                jr.getDetailSection().getBands()[0].setSplitType(SplitTypeEnum.PREVENT);
            }
            JasperPrint jp = JasperFillManager.fillReport(jr, params, connection);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    rowsArray[i][j] = null;
                }
            }

            if (reportType == Report.ReportType.PDF) {
                return generatePDF(jp);
            }
            if (reportType == Report.ReportType.RTF) {
                return generateRTF(jp);
            }
            if (reportType == Report.ReportType.XLS) {
                return generateXLS(jp);
            }
            if (reportType == Report.ReportType.CSV) {
                return generateCSV(jp);
            }


            return null;
        } catch (JRException e) {
            log.error(e.getMessage(), e.getCause());
        }

        return null;
    }

    private FileObjectDescriptor generateCSV(JasperPrint jp) throws JRException {
        final ByteArrayOutputStream csvStream = new ByteArrayOutputStream();
        byte b[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        try {
            csvStream.write(b);
        } catch (IOException ex) {
            Logger.getLogger(JRjavaXmlService.class.getName()).log(Level.SEVERE, null, ex);
        }

        JRCsvExporter csvExporter = new JRCsvExporter();


        csvExporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        csvExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        csvExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, csvStream);
        csvExporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ";");
        csvExporter.exportReport();

        FileObjectDescriptor fod = new FileObjectDescriptor(csvStream.toByteArray());
        fod.setFileContentType("application/vnd.ms-excel");
        fod.setFileExt("csv");
        return fod;
    }

    private FileObjectDescriptor generateXLS(JasperPrint jp) throws JRException {
        JRXlsExporter xlsExporter = new JRXlsExporter();
        final ByteArrayOutputStream xlsStream = new ByteArrayOutputStream();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        xlsExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsStream);
        xlsExporter.exportReport();

        FileObjectDescriptor fod = new FileObjectDescriptor(xlsStream.toByteArray());
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
        fod.setFileContentType("application/rtf");
        fod.setFileExt("rtf");
        return fod;
    }

    private JRDesignStaticText getStaticText(int x, int y, String content, int fontSize, int fat, int alignment) {
        HorizontalAlignEnum align;
        switch (alignment) {
            case 0:
                align = HorizontalAlignEnum.LEFT;
                break;
            case 1:
                align = HorizontalAlignEnum.CENTER;
                break;
            case 2:
                align = HorizontalAlignEnum.RIGHT;
                break;
            default:
                align = HorizontalAlignEnum.LEFT;
        }
        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(216);
        staticText.setHeight(fontSize + 4);
        staticText.setHorizontalAlignment(align);
        staticText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        staticText.setFontName("DejaVu Serif");
        staticText.setFontSize(fontSize + 0f);
        staticText.setBold(fat == 1);
        staticText.setText(content);
        return staticText;
    }

    public JasperDesign createDesignAlbumOCP(String jrxmlPath) throws JRException {
//        JRDesignStaticText staticText;
//        JRDesignTextField textField;
        JRDesignBand band;
//        JRDesignExpression expression;

        JasperDesign jasperDesign = JRXmlLoader.load(new File(context.getRealPath(jrxmlPath)));

        band = (JRDesignBand) jasperDesign.getTitle();
        band.setHeight(yArray[3]);
        band.setSplitType(SplitTypeEnum.STRETCH);

        int y = 0;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (rowsArray[j][i] != null) {
                    for (Object object : rowsArray[j][i]) {
                        Row row = (Row) object;
                        band.addElement(getStaticText(xAlbumArray[j], y + yArray[i], row.getContent(), row.getFontSize(), row.getFat(), row.getAlignment()));
                        y += row.getFontSize() + 5;
                    }
                    y = 0;
                }
            }

        }

//        staticText = new JRDesignStaticText();
//        staticText.setKey("staticText");
//        staticText.setX(143);
//        staticText.setY(yArray[3]);
//        staticText.setWidth(533);
//        staticText.setHeight(18);
//        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//        staticText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
//        staticText.setFontName("DejaVu Serif");
//        staticText.setFontSize(12.0f);
//        staticText.setBold(true);
//        staticText.setText("ОПЕРАТИВНО – КАЛЕНДАРНЫЙ ПЛАН");
//        band.addElement(staticText);
//
//        staticText = new JRDesignStaticText();
//        staticText.setKey("staticText");
//        staticText.setX(143);
//        staticText.setY(yArray[3]+18);
//        staticText.setWidth(533);
//        staticText.setHeight(15);
//        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//        staticText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
//        staticText.setFontName("DejaVu Serif");
//        staticText.setFontSize(10.0f);
//        staticText.setText("для включения работы в План предприятия");
//        band.addElement(staticText);
//
//        textField = new JRDesignTextField();
//        textField.setStretchWithOverflow(true);
//        textField.setBlankWhenNull(true);
//        textField.setKey("textField");
//        textField.setX(312);
//        textField.setY(yArray[3]+33);
//        textField.setWidth(364);
//        textField.setHeight(15);
//        textField.setFontName("DejaVu Serif");
//        expression = new JRDesignExpression();
//        expression.setText("$F{WORK_NAME}");
//        textField.setExpression(expression);
//        band.addElement(textField);
//
//        staticText = new JRDesignStaticText();
//        staticText.setKey("staticText");
//        staticText.setX(143);
//        staticText.setY(yArray[3]+33);
//        staticText.setWidth(169);
//        staticText.setHeight(15);
//        staticText.setFontName("DejaVu Serif");
//        staticText.setText("Наименование и шифр темы:");
//        band.addElement(staticText);

        return jasperDesign;
    }
    
    public JasperDesign createDesignOCP(String jrxmlPath) throws JRException {
//        JRDesignStaticText staticText;
//        JRDesignTextField textField;
        JRDesignBand band;
//        JRDesignExpression expression;

        JasperDesign jasperDesign = JRXmlLoader.load(new File(context.getRealPath(jrxmlPath)));

        band = (JRDesignBand) jasperDesign.getTitle();
        band.setHeight(yArray[3]);
        band.setSplitType(SplitTypeEnum.STRETCH);

        int y = 0;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (rowsArray[j][i] != null && j != 1) {
                    for (Object object : rowsArray[j][i]) {
                        Row row = (Row) object;
                        band.addElement(getStaticText(xArray[j], y + yArray[i], row.getContent(), row.getFontSize(), row.getFat(), row.getAlignment()));
                        y += row.getFontSize() + 5;
                    }
                    y = 0;
                }
            }

        }



        return jasperDesign;
    }
}
