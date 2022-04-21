package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.HashMap;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelWithSummary;
import pst.arm.client.modules.datagrid.widgets.DataGridWindowPanel;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelOcpService extends DataGridPanelOcpOuter {

    private Button btnSummary;

    public DataGridPanelOcpService(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOcpService(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOcpService(String tableName) {
        super(tableName);
    }

    public DataGridPanelOcpService(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOcpService(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    protected void showSummary() {
        HashMap<Integer, SKeyForColumn> map = new HashMap<Integer, SKeyForColumn>();
        map.put(4, new SKeyForColumn("MAIN:SUM_PLAN_COST1"));

        DataGridPanelWithSummary summary = new DataGridPanelWithSummary("OCP_SERVICE_SUM", map, NumberFormat.getFormat("#,##0.00"), 3);
        IRowColumnVal v = new DRowColumnValNumber();
        v.setVal(condition.getFilters().get(new SKeyForColumn("SCS:OCP_ID")).getVal());
        summary.getCondition().getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), v);

        DataGridWindowPanel window = new DataGridWindowPanel(summary, "Итоги");
        window.show();
    }

}
