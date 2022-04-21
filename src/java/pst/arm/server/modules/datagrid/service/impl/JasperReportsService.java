package pst.arm.server.modules.datagrid.service.impl;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.server.common.DTableMapManager;
import pst.arm.server.common.reports.conditionbased.Report;

/**
 * Created by wesStyle on 08.07.2015.
 */
@Service
public class JasperReportsService {

    private List data;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("JasperReportsService");
//    @Autowired
//    private DDataGridService dDataGridService;
    @Autowired
    ServletContext context;

    public JasperReportsService() {
    }

    public FileObjectDescriptor genetateReport(String tableName, ArrayList<HashMap<String, String>> domainHashMapList, Report.ReportType reportType) {
        try {
            DTable table = DTableMapManager.getInstance().getTable(tableName);

            FastReportBuilder drb = new FastReportBuilder();

            Style titleStyle = new Style("titleStyle");
            Style subtitleStyle = new Style("subtitleStyle");
            Style headerStyle = new Style("headerStyle");
            Style detailStyle = new Style("detailStyle");


            data = domainHashMapList;

            if (data.get(0) != null) {
                HashMap<String, String> map = (HashMap) data.get(0);
                addExtraConditions(tableName);

                if ("DH_ELABORATION_OF_DD_HLV".equals(tableName.toUpperCase())) {
                    headerStyle.getFont().setBold(false);

                    ColumnBuilder[] columns = new ColumnBuilder[14];

                    for (IColumnBuilder builder : table.getColumnBuilders()) {
                        for (Map.Entry column : builder.getColumns().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) column.getKey();
                            String valKey = key.getTableAlias() + "-" + key.getColumnName();
                            /*if (map.containsKey(valKey)
                                    && !key.equals(new SKeyForColumn("MAIN:REGISTERED"))
                                    && !key.equals(new SKeyForColumn("MAIN:TECHNOLOGIST_PLAN"))
                                    && !key.equals(new SKeyForColumn("MAIN:SETTERS_PLAN"))
                                    && !key.equals(new SKeyForColumn("MAIN:SETTERS_PLAN"))
                                    && !key.equals(new SKeyForColumn("MAIN:NORMATIVE_PLAN"))
                                    && !key.equals(new SKeyForColumn("MAIN:NORMATIVE"))
                                    && !key.equals(new SKeyForColumn("MAIN:URGENCY"))
                                    && !key.equals(new SKeyForColumn("MAIN:TECHNOLOGIST"))
                                    && !key.equals(new SKeyForColumn("MAIN:SETTERS"))
                                    && !key.equals(new SKeyForColumn("MAIN:LEADS_CONTRACT"))
                                    && !key.equals(new SKeyForColumn("MAIN:ID_STATUS"))
                                    && !key.equals(new SKeyForColumn("MAIN:AMAUNT"))) {*/
                                DColumn col = (DColumn) column.getValue();

                                if (key.equals(new SKeyForColumn("MAIN:DATEDOC"))) {
//                                    drb.addColumn(col.getCaption(), valKey, String.class.getName(), 120);
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(85);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[1] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:STATUS_TIME"))) {
//                                    drb.addColumn(col.getCaption(), valKey, String.class.getName(), 120);
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(85);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[2] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:INFO"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(240);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[4] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:URGENCY_CODE2"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Срочн.");
//                                    colm.setWidth(builder.getColumn(key).getColumnProperty().getWidthColumn());   
                                    colm.setWidth(80);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[6] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:AMAUNT"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Объем, л");
//                                    colm.setWidth(builder.getColumn(key).getColumnProperty().getWidthColumn());   
                                    colm.setWidth(90);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[7] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:E_DATEDOC"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Дата поступл. КД");
                                    colm.setWidth(110);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[3] = colm;
                                } else if (key.equals(new SKeyForColumn("STATUS:NAME"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(110);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[11] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:TECHNOLOGIST_FACT"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Технол.");
                                    colm.setWidth(90);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[8] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:CUSTOMER"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(150);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[5] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:SETTERS_FACT"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Нормир.");
                                    colm.setWidth(95);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[9] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:NORMATIVE_FACT"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Нормат.");
                                    colm.setWidth(90);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[10] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:OGT_DEADLINE"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle("Срок устан. ОГТ");
                                    colm.setWidth(75);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[13] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:MEMO_DEADLINE"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(80);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    colm.setTextFormatter(new StringToShortDateStringFormatter());
                                    columns[12] = colm;
                                } else if (key.equals(new SKeyForColumn("MAIN:NUMDOC"))) {
                                    ColumnBuilder colm = ColumnBuilder.getNew();
                                    colm.setTitle(col.getCaption());
                                    colm.setWidth(builder.getColumn(key).getColumnProperty().getWidthColumn() * 2);
                                    colm.setColumnProperty(valKey, String.class.getName());
                                    columns[0] = colm;
                                }
                            //}
                        }
                    }
                    for (ColumnBuilder column : columns) {
                        if (column != null) {
                            drb.addColumn(column.build());
                        }
                    }
                } 
                else {
                    for (IColumnBuilder builder : table.getColumnBuilders()) {
                        for (Map.Entry column : builder.getColumns().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) column.getKey();
                            String valKey = key.getTableAlias() + "-" + key.getColumnName();
                            if (map.containsKey(valKey)) {
                                DColumn col = (DColumn) column.getValue();
                                drb.addColumn(col.getCaption(), valKey, String.class.getName(), builder.getColumn(key).getColumnProperty().getWidthColumn());
                            }
                            /*if (builder.getColumn(key).getIsVisible()) {
                             DColumn col = (DColumn) column.getValue();
                             String valKey = key.getTableAlias() + "-" + key.getColumnName();
                             drb.addColumn(col.getCaption(), valKey, String.class.getName(), builder.getColumn(key).getColumnProperty().getWidthColumn());
                             }*/
                        }
                    }
                }
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            if ("plan_nto_year_vo".equals(tableName)) {
                drb.setTitle(table.getCaption())
                        .setPrintBackgroundOnOddRows(false)
                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setUseFullPageWidth(true)
                        .setAllowDetailSplit(false)
                        .setIgnorePagination(true)
                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template_datagridplanntoyear.jrxml"));
            } else if ("DH_ELABORATION_OF_DD_HLV".equals(tableName.toUpperCase())) {
                drb.setTitle(table.getCaption())
                        .setSubtitle("Создано " + sf.format(new Date()))
                        .setPrintBackgroundOnOddRows(false)
                        .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle)
                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setUseFullPageWidth(true)
                        .setAllowDetailSplit(false)
                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template_another_font.jrxml"));
//            } else if ("APP_PRODUCTION_HLV".equals(tableName.toUpperCase())) {
//                drb.setTitle(table.getCaption())
//                        .setSubtitle("Создано " + sf.format(new Date()))
//                        .setPrintBackgroundOnOddRows(false)
//                        .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle)
//                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
//                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
//                        .setUseFullPageWidth(true)
//                        .setAllowDetailSplit(false)
//                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template_another_font_1.jrxml"));
            } else {
                drb.setTitle(table.getCaption())
                        .setSubtitle("Создано " + sf.format(new Date()))
                        .setPrintBackgroundOnOddRows(false)
                        .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle)
                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setUseFullPageWidth(true)
                        .setAllowDetailSplit(false)
                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template.jrxml"));
                // .setIgnorePagination(true) // - игнорировать страничную организацию
            }




            JRDataSource ds = new JRBeanCollectionDataSource(data);

            DynamicReport dr = drb.build();
            Map params = new HashMap();
            JasperReport jr;

            if ("APP_PRODUCTION_HLV".equals(tableName.toUpperCase())) {
                params.put("myCaption", table.getCaption());
                params.put("mySubtitle", "Создано " + sf.format(new Date()));
                JasperDesign jasperDesign = createDesign();
                jr = JasperCompileManager
                        .compileReport(jasperDesign);
            } else {
                jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);
            }

            jr.getDetailSection().getBands()[0].setSplitType(SplitTypeEnum.PREVENT);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, ds);

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String monthName(int m) {
        switch (m) {
            case 1:
                return "январь";
            case 2:
                return "февраль";
            case 3:
                return "март";
            case 4:
                return "апрель";
            case 5:
                return "май";
            case 6:
                return "июнь";
            case 7:
                return "июль";
            case 8:
                return "август";
            case 9:
                return "сентябрь";
            case 10:
                return "октябрь";
            case 11:
                return "ноябрь";
            case 12:
                return "декабрь";
        }
        return "";
    }

    public FileObjectDescriptor genetateReportWorkshopLoad(String tableName, ArrayList<String> headers, ArrayList<HashMap<String, String>> domainHashMapList, Report.ReportType reportType) {
        try {
            DTable table = DTableMapManager.getInstance().getTable(tableName);


            table.getColumnBuilder(new SKeyForColumn("MAIN:M1")).getColumn(new SKeyForColumn("MAIN:M1")).setCaption(headers.get(0));
            table.getColumnBuilder(new SKeyForColumn("MAIN:M2")).getColumn(new SKeyForColumn("MAIN:M2")).setCaption(headers.get(1));
            table.getColumnBuilder(new SKeyForColumn("MAIN:M3")).getColumn(new SKeyForColumn("MAIN:M3")).setCaption(headers.get(2));
            table.getColumnBuilder(new SKeyForColumn("MAIN:M4")).getColumn(new SKeyForColumn("MAIN:M4")).setCaption(headers.get(3));
            table.getColumnBuilder(new SKeyForColumn("MAIN:M5")).getColumn(new SKeyForColumn("MAIN:M5")).setCaption(headers.get(4));
            table.getColumnBuilder(new SKeyForColumn("MAIN:M6")).getColumn(new SKeyForColumn("MAIN:M6")).setCaption(headers.get(5));

            FastReportBuilder drb = new FastReportBuilder();

            Style titleStyle = new Style("titleStyle");
            Style subtitleStyle = new Style("subtitleStyle");
            Style headerStyle = new Style("headerStyle");
            Style detailStyle = new Style("detailStyle");


            data = domainHashMapList;
            if (data.get(0) != null) {
                HashMap<String, String> map = (HashMap) data.get(0);
                addExtraConditions(tableName);
                for (IColumnBuilder builder : table.getColumnBuilders()) {
                    for (Map.Entry column : builder.getColumns().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) column.getKey();
                        String valKey = key.getTableAlias() + "-" + key.getColumnName();
                        if (map.containsKey(valKey)) {
                            DColumn col = (DColumn) column.getValue();
                            drb.addColumn(col.getCaption(), valKey, String.class.getName(), builder.getColumn(key).getColumnProperty().getWidthColumn());
                        }
                        /*if (builder.getColumn(key).getIsVisible()) {
                         DColumn col = (DColumn) column.getValue();
                         String valKey = key.getTableAlias() + "-" + key.getColumnName();
                         drb.addColumn(col.getCaption(), valKey, String.class.getName(), builder.getColumn(key).getColumnProperty().getWidthColumn());
                         }*/
                    }

                }
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            if ("plan_nto_year_vo".equals(tableName)) {
                drb.setTitle(table.getCaption())
                        .setPrintBackgroundOnOddRows(false)
                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setUseFullPageWidth(true)
                        .setAllowDetailSplit(false)
                        .setIgnorePagination(true)
                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template_datagridplanntoyear.jrxml"));
            } else {
                drb.setTitle(table.getCaption())
                        .setSubtitle("Создано " + sf.format(new Date()))
                        .setPrintBackgroundOnOddRows(false)
                        .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle)
                        .setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setUseFullPageWidth(true)
                        .setAllowDetailSplit(false)
                        .setTemplateFile(context.getRealPath("/WEB-INF/reports/generic_template.jrxml"));
                // .setIgnorePagination(true) // - игнорировать страничную организацию
            }




            JRDataSource ds = new JRBeanCollectionDataSource(data);

            DynamicReport dr = drb.build();
            Map params = new HashMap();

            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);
            jr.getDetailSection().getBands()[0].setSplitType(SplitTypeEnum.PREVENT);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, ds);

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

        return null;
    }

    private FileObjectDescriptor generateCSV(JasperPrint jp) throws JRException {
        final ByteArrayOutputStream csvStream = new ByteArrayOutputStream();
        //byte-order marker (BOM)
        byte b[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        try {
            //insert BOM byte array into outputStream to correctly display UTF-8
            csvStream.write(b);
        } catch (IOException ex) {
            Logger.getLogger(JasperReportsService.class.getName()).log(Level.SEVERE, null, ex);
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

    private List convertDomainToListOfHashMaps(List<DDataGrid> domains, DTable table) {
        List data = new ArrayList();

        for (DDataGrid domain : domains) {
            Map map = new HashMap();
            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                String valStr;
                if (entry.getValue() instanceof DRowColumnValDate
                        && table.getColumnBuilder(key).getColumn(key).getColumnProperty() instanceof DColumnPropertyDateField) {
                    DRowColumnValDate val = (DRowColumnValDate) entry.getValue();
                    DColumnPropertyDateField prop = (DColumnPropertyDateField) table.getColumnBuilder(key).getColumn(key).getColumnProperty();
                    Date valDate = val.getVal();
                    DateFormat df = new SimpleDateFormat(prop.getFormat());
                    valStr = df.format(valDate);
                } else {
                    valStr = ((IRowColumnVal) entry.getValue()).getStringFromVal(key, table.getColumnBuilder(key));
                }
                if (valStr == null) {
                    valStr = "";
                }
                map.put(valKey, valStr);
            }
            data.add(map);
        }

        return data;
    }

    // Добавляем специальные условия для каких-то отдельных таблиц
    private void addExtraConditions(String tableName) {
        if ("plan_nto_year_vo".equals(tableName)) {
            for (int i = 0; i < data.size(); i++) {
                HashMap<String, String> map = (HashMap<String, String>) data.get(i);
                String workNumber = map.get("WORK_PLAN-WORK_NUMBER");
                map.put("WORK_PLAN-WORK_NUMBER", workNumber + ".");
                data.set(i, map);
            }
        }
    }

    private static class StringToShortDateStringFormatter extends Format {

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return source.substring(0, 6) + source.substring(8);
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            StringBuffer sb = new StringBuffer();
            if (((String) obj).length() == 10) {
                sb.append(((String) obj).substring(0, 6));
                sb.append(((String) obj).substring(8));
            } else {
                sb.append((String) obj);
            }
            return sb;

//            return new StringBuffer((String)obj);
        }
    }

    public static class Storage {

        public static String add(Map<String, String> map, String key, String value) {
            map.put(key, value);
            return value;
        }
    }

    public JasperDesign createDesign() throws JRException {
        JRDesignStaticText staticText = null;
        JRDesignTextField textField = null;
        JRDesignBand band = null;
        JRDesignExpression expression = null;

        JRDesignField field = null;
        JRDesignConditionalStyle conditionalStyle = null;
        JRLineBox lineBox = null;

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("dynamicReport");
        jasperDesign.setLanguage("groovy");
        jasperDesign.setPageWidth(842);
        jasperDesign.setPageHeight(595);
        jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
        jasperDesign.setColumnWidth(802);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);
        jasperDesign.setProperty("ireport.zoom", "1.6105100000000008");
        jasperDesign.setProperty("ireport.x", "0");
        jasperDesign.setProperty("ireport.y", "0");
        jasperDesign.addImport("pst.arm.server.modules.datagrid.service.impl.JasperReportsService.Storage");

        JRDesignStyle baseStyle = new JRDesignStyle();
        baseStyle.setName("baseStyle");
        baseStyle.setDefault(true);
        baseStyle.setFontName("Times New Roman");
        baseStyle.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        baseStyle.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        baseStyle.setFontSize(10f);
        baseStyle.setBold(false);
        baseStyle.setItalic(false);
        baseStyle.setUnderline(false);
        baseStyle.setStrikeThrough(false);
        jasperDesign.addStyle(baseStyle);

        JRDesignStyle titleStyle = new JRDesignStyle();
        titleStyle.setName("titleStyle");
        titleStyle.setParentStyle(baseStyle);
        titleStyle.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        titleStyle.setFontSize(16f);
        titleStyle.setBold(true);
        jasperDesign.addStyle(titleStyle);

        JRDesignStyle subtitleStyle = new JRDesignStyle();
        subtitleStyle.setName("subtitleStyle");
        subtitleStyle.setParentStyle(baseStyle);
        subtitleStyle.setFontSize(14f);
        lineBox = subtitleStyle.getLineBox();
        lineBox.setBottomPadding(30);
        jasperDesign.addStyle(subtitleStyle);

        JRDesignStyle headerStyle = new JRDesignStyle();
        headerStyle.setName("headerStyle");
        headerStyle.setParentStyle(baseStyle);
        headerStyle.setFontSize(8f);
        headerStyle.setBold(true);
        lineBox = headerStyle.getLineBox();
        lineBox.setTopPadding(2);
        lineBox.setLeftPadding(2);
        lineBox.setBottomPadding(2);
        lineBox.setRightPadding(2);
        lineBox.getPen().setLineWidth(0.5f);
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineWidth(1f);
        lineBox.getRightPen().setLineWidth(0.5f);
        jasperDesign.addStyle(headerStyle);

        JRDesignStyle detailStyle = new JRDesignStyle();
        detailStyle.setName("detailStyle");
        detailStyle.setParentStyle(baseStyle);
        detailStyle.setFill(FillEnum.SOLID);
        detailStyle.setVerticalAlignment(VerticalAlignEnum.TOP);
        detailStyle.setFontSize(10f);
        lineBox = detailStyle.getLineBox();
        lineBox.setTopPadding(2);
        lineBox.setLeftPadding(2);
        lineBox.setBottomPadding(2);
        lineBox.setRightPadding(2);
        lineBox.getPen().setLineWidth(0.5f);
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineWidth(0.5f);
        jasperDesign.addStyle(detailStyle);

        JRDesignStyle detailBoldStyle = new JRDesignStyle();
        detailBoldStyle.setName("detailBoldStyle");
        detailBoldStyle.setParentStyle(detailStyle);
        conditionalStyle = new JRDesignConditionalStyle();
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-DESCRIPTION}.equals(\"Коммерческие\")"
                + " || $F{MAIN-DESCRIPTION}.equals(\"Прибой\")"
                + " || $F{MAIN-DESCRIPTION}.equals(\"МАРТ\")"
                + " || $F{MAIN-DESCRIPTION}.equals(\"РИМР\")");
        conditionalStyle.setConditionExpression(expression);
        conditionalStyle.setBold(true);
        detailBoldStyle.addConditionalStyle(conditionalStyle);
        jasperDesign.addStyle(detailBoldStyle);

        JRDesignParameter myCaptionParameter = new JRDesignParameter();
        myCaptionParameter.setName("myCaption");
        myCaptionParameter.setValueClass(String.class);
        jasperDesign.addParameter(myCaptionParameter);

        JRDesignParameter mySubtitleParameter = new JRDesignParameter();
        mySubtitleParameter.setName("mySubtitle");
        mySubtitleParameter.setValueClass(String.class);
        jasperDesign.addParameter(mySubtitleParameter);

        field = new JRDesignField();
        field.setName("MAIN-SEQ_NUMBER");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-ORDER_NUM");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-OUT_NUMBER");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-APP_DATE");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-AUTHOR");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-DESCRIPTION");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-URGENCY");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);
        
        field = new JRDesignField();
        field.setName("MAIN-AMAUNT");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-DATE_IN");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-OPPSP_FACT");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-TECHNOLOGIST_PLAN");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-TECHNOLOGIST_FACT");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-SETTERS_PLAN");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-SETTERS_FACT");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-NORMATIVE_PLAN");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-NORMATIVE_FACT");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("MAIN-TO_PRODUCTION_PLAN");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        band = new JRDesignBand();
        band.setSplitType(SplitTypeEnum.STRETCH);
        jasperDesign.setBackground(band);
        band = new JRDesignBand();
        band.setSplitType(SplitTypeEnum.STRETCH);
        jasperDesign.setBackground(band);

        band = new JRDesignBand();
        band.setHeight(56);
        band.setSplitType(SplitTypeEnum.STRETCH);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(titleStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(0);
        textField.setY(0);
        textField.setWidth(802);
        textField.setHeight(56);
        textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        expression = new JRDesignExpression();
        expression.setText("$P{myCaption}");
        textField.setExpression(expression);
        band.addElement(textField);

        jasperDesign.setTitle(band);

        band = new JRDesignBand();
        band.setHeight(35);
        band.setSplitType(SplitTypeEnum.STRETCH);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(subtitleStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(0);
        textField.setY(0);
        textField.setWidth(802);
        textField.setHeight(35);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$P{mySubtitle}");
        textField.setExpression(expression);
        band.addElement(textField);

        jasperDesign.setPageHeader(band);

        band = new JRDesignBand();
        band.setHeight(37);
        band.setSplitType(SplitTypeEnum.STRETCH);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(0);
        staticText.setY(0);
        staticText.setWidth(45);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("№ задания");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(45);
        staticText.setY(0);
        staticText.setWidth(40);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("№ заказа");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(85);
        staticText.setY(0);
        staticText.setWidth(40);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Исх. №");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(125);
        staticText.setY(0);
        staticText.setWidth(40);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Дата");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(165);
        staticText.setY(0);
        staticText.setWidth(60);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("От кого");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(225);
        staticText.setY(0);
        staticText.setWidth(105);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Содержание");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(330);
        staticText.setY(0);
        staticText.setWidth(57);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Срок");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(387);
        staticText.setY(0);
        staticText.setWidth(55);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Дата получения заявки");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(442);
        staticText.setY(0);
        staticText.setWidth(50);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Вып. ОППиСП");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(492);
        staticText.setY(0);
        staticText.setWidth(40);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("План технол.");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(532);
        staticText.setY(0);
        staticText.setWidth(40);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Вып. технол");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(572);
        staticText.setY(0);
        staticText.setWidth(45);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("План нормир.");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(617);
        staticText.setY(0);
        staticText.setWidth(45);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Вып. нормир.");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(662);
        staticText.setY(0);
        staticText.setWidth(45);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("План нормат.");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(707);
        staticText.setY(0);
        staticText.setWidth(45);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("Вып. нормат.");
        band.addElement(staticText);

        staticText = new JRDesignStaticText();
        staticText.setKey("staticText");
        staticText.setStyle(headerStyle);
        staticText.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        staticText.setX(752);
        staticText.setY(0);
        staticText.setWidth(50);
        staticText.setHeight(37);
        staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        staticText.setText("План передачи в пр-во");
        band.addElement(staticText);

        jasperDesign.setColumnHeader(band);

        band = new JRDesignBand();
        band.setHeight(16);
        band.setSplitType(SplitTypeEnum.STRETCH);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(0);
        textField.setY(0);
        textField.setWidth(45);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-SEQ_NUMBER}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(45);
        textField.setY(0);
        textField.setWidth(40);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-ORDER_NUM}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(85);
        textField.setY(0);
        textField.setWidth(40);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-OUT_NUMBER}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(125);
        textField.setY(0);
        textField.setWidth(40);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-APP_DATE}.length() == 10)? ($F{MAIN-APP_DATE}.substring(0, 6) + $F{MAIN-APP_DATE}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(165);
        textField.setY(0);
        textField.setWidth(60);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-AUTHOR}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailBoldStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(225);
        textField.setY(0);
        textField.setWidth(105);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-DESCRIPTION}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(330);
        textField.setY(0);
        textField.setWidth(57);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$F{MAIN-URGENCY}");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(387);
        textField.setY(0);
        textField.setWidth(55);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-DATE_IN}.length() == 10)? ($F{MAIN-DATE_IN}.substring(0, 6) + $F{MAIN-DATE_IN}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(442);
        textField.setY(0);
        textField.setWidth(50);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-OPPSP_FACT}.length() == 10)? ($F{MAIN-OPPSP_FACT}.substring(0, 6) + $F{MAIN-OPPSP_FACT}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(492);
        textField.setY(0);
        textField.setWidth(40);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-TECHNOLOGIST_PLAN}.length() == 10)? ($F{MAIN-TECHNOLOGIST_PLAN}.substring(0, 6) + $F{MAIN-TECHNOLOGIST_PLAN}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(532);
        textField.setY(0);
        textField.setWidth(40);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-TECHNOLOGIST_FACT}.length() == 10)? ($F{MAIN-TECHNOLOGIST_FACT}.substring(0, 6) + $F{MAIN-TECHNOLOGIST_FACT}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(572);
        textField.setY(0);
        textField.setWidth(45);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-SETTERS_PLAN}.length() == 10)? ($F{MAIN-SETTERS_PLAN}.substring(0, 6) + $F{MAIN-SETTERS_PLAN}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(617);
        textField.setY(0);
        textField.setWidth(45);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-SETTERS_FACT}.length() == 10)? ($F{MAIN-SETTERS_FACT}.substring(0, 6) + $F{MAIN-SETTERS_FACT}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(662);
        textField.setY(0);
        textField.setWidth(45);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-NORMATIVE_PLAN}.length() == 10)? ($F{MAIN-NORMATIVE_PLAN}.substring(0, 6) + $F{MAIN-NORMATIVE_PLAN}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(707);
        textField.setY(0);
        textField.setWidth(45);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-NORMATIVE_FACT}.length() == 10)? ($F{MAIN-NORMATIVE_FACT}.substring(0, 6) + $F{MAIN-NORMATIVE_FACT}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(detailStyle);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setX(752);
        textField.setY(0);
        textField.setWidth(50);
        textField.setHeight(16);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("($F{MAIN-TO_PRODUCTION_PLAN}.length() == 10)? ($F{MAIN-TO_PRODUCTION_PLAN}.substring(0, 6) + $F{MAIN-TO_PRODUCTION_PLAN}.substring(8)) : (\"\")");
        textField.setExpression(expression);
        band.addElement(textField);

        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

        band = new JRDesignBand();
        band.setHeight(45);
        band.setSplitType(SplitTypeEnum.STRETCH);

        textField = new JRDesignTextField();
        textField.setBlankWhenNull(false);
        textField.setKey("textField");
        textField.setStyle(baseStyle);
        textField.setX(0);
        textField.setY(10);
        textField.setWidth(89);
        textField.setHeight(18);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("\"Страница \" + $V{PAGE_NUMBER} + \" из \"");
        textField.setExpression(expression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setBlankWhenNull(false);
        textField.setEvaluationTime(EvaluationTimeEnum.REPORT);
        textField.setKey("textField");
        textField.setStyle(baseStyle);
        textField.setX(89);
        textField.setY(10);
        textField.setWidth(89);
        textField.setHeight(18);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        expression = new JRDesignExpression();
        expression.setText("$V{PAGE_NUMBER}");
        textField.setExpression(expression);
        band.addElement(textField);

        jasperDesign.setColumnFooter(band);

        return jasperDesign;
    }
}
