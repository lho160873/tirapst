package pst.arm.server.modules.datagrid.reportgenerator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.common.DTableMapManager;
import pst.arm.server.common.reports.conditionbased.BaseExtractor;
import pst.arm.server.common.reports.conditionbased.Report;
import pst.arm.server.common.reports.conditionbased.ReportColumns;
import pst.arm.server.common.reports.conditionbased.ReportData;
import pst.arm.server.common.reports.conditionbased.ReportDataRow;
import pst.arm.server.modules.datagrid.service.DDataGridService;

/**
 *
 * @author Artem Vorontsov, wesStyle
 */
public class DDataGridStoreExtractor  extends BaseExtractor {
    protected DDataGridService asservice;
    protected DataGridSearchCondition dgCondition;
    protected List elements;
    protected ReportColumns columns;
    protected ReportData data;
    protected String tableName;
    private static Logger log = Logger.getLogger("DDataGridStoreExtractor");
    
  
  public DDataGridStoreExtractor(String tn, DDataGridService service, DataGridSearchCondition condition) {
        log.warn("DDataGridStoreExtractor::DDataGridStoreExtractor");   
        dgCondition = (DataGridSearchCondition)condition;
        asservice = (DDataGridService)service;
        tableName = tn;
        //condition = rcondition;
    }

    @Override
    public Report generateReport() {
        log.warn("DDataGridStoreExtractor::generateReport");   
        elements = null;
        columns = null;
        return getReportForReqs();
    }

    protected Report getReportForReqs() {
        log.warn("DDataGridStoreExtractor::getReportForReqs");   
        
        DTable table = DTableMapManager.getInstance().getTable(tableName);
        //elements = asservice.getAllDataGrid(tableName);
        elements = asservice.getDataGrid(tableName, dgCondition);
        
        columns = getColumns(table);
        data = getReportData(table, elements);
        //createData(table, elements);
        
        Report report = new Report();
        

        String catpion = table.getCaption();        
        report.setCaption( catpion );//TODO//getRes("reportgenerator.rextractor.textCaption"));
        //report.setText("TODO condition");//condition2text()); TODO condition
        report.setColumns(columns);
        report.setData(data);
        log.warn("DDataGridStoreExtractor::getReportForReqs END"); 
        return report;
    }

    @Override
    protected String condition2text() {
         log.warn("DDataGridStoreExtractor::condition2text");   
        StringBuilder sb = new StringBuilder(1000);
/*
        SemanticType unitSemanticType = rcondition.getUnitSemanticType();
        SemanticType docSemanticType = rcondition.getDocSemanticType();

        Boolean searchInDocs = rcondition.getSearchInDocs();
        sb.append(getRes("reportgenerator.rextractor.textSearchDirection"));
        if(rcondition.getDocSemType() != null) {
            searchInDocs = true;
            sb.append(getRes("reportgenerator.rextractor.textSearchDirectionUnit"));
        } else {
            sb.append(getRes("reportgenerator.rextractor.textSearchDirectionDoc"));
        }
        sb.append("\n");

        sb.append(getRes("reportgenerator.rextractor.textSemanticType"));
        if(searchInDocs) {
            sb.append(docSemanticType.getTitle());
            if(rcondition.getUnitSemType() != null) {
                sb.append(" (").append(unitSemanticType.getTitle()).append(")");
            }
        } else {
            sb.append(unitSemanticType.getTitle());
        }
        sb.append("\n");


        //adding query params
        sb.append(getRes("reportgenerator.rextractor.textSearchParams")).append(unitSemanticType.getTitle()).append("\n");
        for(Requisite req : unitSemanticType.getRequisites()) {
            if(req.getValue() != null) {
                sb.append("    ").append(req.getTitle()).append(": ");
                sb.append(req.getValue()).append("\n");
            }
        }

        if (searchInDocs) {
            sb.append("  ").append(docSemanticType.getTitle()).append("\n");
            for (Requisite req : docSemanticType.getRequisites()) {
                if (req.getValue() != null) {
                    sb.append("    ").append(req.getTitle()).append(": ");
                    sb.append(req.getValue()).append("\n");
                }
            }
        }
        */
        return sb.toString();
    }
     
   
   
       
       
    protected ReportColumns getColumns(DTable table) {
        log.warn("DDataGridStoreExtractor::getColumns BEGIN");   
        ReportColumns requsiteColumns = new ReportColumns();
        
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                DColumn col = (DColumn)colEntry.getValue();
                String colName = col.getCaption();
                
                if(table.getCaption().contains("Доставка до Терминала") || table.getCaption().contains("Доставка от Терминала"))
                {
                        requsiteColumns.addColumn(colName, 0.2f);
                }
                else 
                {
                    if (col.getIsVisible()){
                        requsiteColumns.addColumn(colName, 0.2f);
                    }
                }
            }
        }


        return requsiteColumns;
    }

    private ReportData getReportData(DTable table, List elements) {
        log.warn("DDataGridStoreExtractor::getReportData");   
       ReportData reportData = new ReportData();
        
        List<DDataGrid> records = elements;
       
        for (DDataGrid record : records) {
            ReportDataRow row = new ReportDataRow(); //строка в отчете            
            for (IColumnBuilder builder : table.getColumnBuilders()) {
                for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                    DColumn col = (DColumn) colEntry.getValue();
                    if (col.getIsVisible()) {
                        if ((col.getColumnProperty() instanceof DColumnPropertyDateField)
                                || (col.getColumnProperty() instanceof DColumnPropertyCurrentTimeWhenEdit)
                                || (col.getColumnProperty() instanceof DColumnPropertyMonthYearField)) {
                            Date date = (Date) record.getRows().get(key).getFormatVal(key, builder);
                            String fdate;
                            if (date != null) {
                                fdate = new SimpleDateFormat(col.getColumnProperty().getFormat()).format(date);
                            } else {
                                fdate = "";
                            }
                            //fdate = "Format: "+col.getColumnProperty().getFormat()+" "+fdate;
                            row.add(fdate);
                        } else if ((col.getColumnProperty() instanceof DColumnPropertyNumberField)) {
                            String format = col.getColumnProperty().getFormat();
                            NumberFormat f;
                            String fnumb;
                            if (record.getRows().get(key).getFormatVal(key, builder) != null) {
                                if (format.length() > 0) {
                                    f = new DecimalFormat(format);
                                    fnumb = f.format(record.getRows().get(key).getFormatVal(key, builder));
                                } else {
                                    f = new DecimalFormat("###");
                                    fnumb = f.format(record.getRows().get(key).getFormatVal(key, builder));
                                }
                            } else {
                                fnumb = "";
                            }
                            row.add(fnumb);
                        } else {
                            row.add(record.getRows().get(key).getFormatVal(key, builder));
                        }
                    }
                }
            }
            reportData.addRow(row);
        }


        return reportData;
    }
}
