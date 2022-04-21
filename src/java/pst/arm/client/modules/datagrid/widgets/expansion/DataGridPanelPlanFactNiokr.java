package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelPlanFactNiokr extends DataGridPanelNotEditable {

    public DataGridPanelPlanFactNiokr(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelPlanFactNiokr(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelPlanFactNiokr(String tableName) {
        super(tableName);
    }

    public DataGridPanelPlanFactNiokr(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelPlanFactNiokr(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }
    Integer monthNum;
    Integer yearNum;
    
    @Override
    public void putGridSubFilters(DDataGrid domain) {
        if (!isOpenAsSub) {
            return;
        }
        domainForSub = domain;
        if (domain == null)
            return;

        condition.getParams().clear();
        
        Date dateMonth = (Date) (domainForSub.getRows().get(new SKeyForColumn("MAIN:MONTH")).getVal());
        monthNum = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:MONTH_NUM")).getVal());
        yearNum = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:YEAR_NUM")).getVal());
        String pnDate = DateTimeFormat.getFormat("dd.MM.yyyy"). format(dateMonth);
        condition.getParams().put("%pnDate%", "'"+pnDate+"'");
        
        Date dateMonthNext = (Date)dateMonth.clone();
        CalendarUtil.addMonthsToDate(dateMonthNext, 1);
        String pnDateNext = DateTimeFormat.getFormat("dd.MM.yyyy").format(dateMonthNext);
        condition.getParams().put("%pnDateNext%", "'"+pnDateNext+"'");
        
        //MessageBox.info("","pnDate = "+pnDate.toString() , null);
        //MessageBox.info("","pnDateNext = "+pnDateNext.toString() , null);
        
        
        HashMap<SKeyForColumn, IRowColumnVal> filters = condition.getFilters();
        filters.clear();

        
        //если переданы нулевые данные то будем показывать пустую таблицу (в запрос передадим null в условия WHERE
        HashMap< SKeyForColumn, SKeyForColumn> relations = subRalationInfo.getRelations();
        passedFields = new HashMap<SKeyForColumn, IRowColumnVal>();
        for (Map.Entry key : relations.entrySet()) {
            SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
            SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
            IRowColumnVal val = null;
            if (domainForSub != null) {
                val = domainForSub.getRows().get(key_1);
            }
            filters.put(key_2, val);
        }
        if (subRalationInfo.getPassedFieds() != null) {
            for (Map.Entry key : subRalationInfo.getPassedFieds().entrySet()) {
                SKeyForColumn key_1 = (SKeyForColumn) key.getKey(); //ключ по переданной таблице
                SKeyForColumn key_2 = (SKeyForColumn) key.getValue();
                IRowColumnVal val = null;
                if (domainForSub != null) {
                    val = domainForSub.getRows().get(key_1);
                }
                passedFields.put(key_2, val);
            }
        }
        getAndSetColumnsVisibility();
    }
    
    @Override
    protected void showSummary (String reportType, int version) {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (domain == null) {
            MessageBox.info("Внимание!", "Этап не выбран!", null);
            return;
        }

        Integer departId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal());
  
        
        StringBuilder sb = new StringBuilder("01.");
        if (monthNum < 10) {
            sb.append("0");
        }
        sb.append(monthNum);
        sb.append(".");
        sb.append(yearNum);
        String dateNum = sb.toString();
        String reportTemplate = version==2?"PlanFactNiokrPlanWorksDop2":"PlanFactNiokrPlanWorksDop";
        HashMap<String, Object> params = new HashMap<String, Object>();
        
        params.put("departId", departId); 
        params.put("dateNum", dateNum);
        
        getReportService().generateJasperReport(reportTemplate, reportType, params, condition, table, new FileReportServiceCallback(this));
    }
 
}
