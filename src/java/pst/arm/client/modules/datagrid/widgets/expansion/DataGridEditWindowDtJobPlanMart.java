package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValString;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataBasePanel;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;

/**
 *
 * @author LKHorosheva
 */
public class DataGridEditWindowDtJobPlanMart extends DataGridEditWindow {

    private final GWTDDataGridServiceAsync service2 = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных

    public DataGridEditWindowDtJobPlanMart(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
        initTableOdpt();
    }

    public DataGridEditWindowDtJobPlanMart(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
        initTableOdpt();
    }

    public DataGridEditWindowDtJobPlanMart(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
        initTableOdpt();
    }

    protected final void initTableOdpt() {
        setSize(800, 765);

        service2.getAllDataGrid("DEPART_FOR_PMASC", new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(List<DDataGrid> listData) {
                final DataBasePanel panelTableOdpt = DataGridPanelBuilder.createDataGridPanel("SPR_DEPART_MART_HLV");

                String id = domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal().toString();
                panelTableOdpt.getCondition().getParams().put("%orderId%", id);

                String filtr = "";
                Integer i = 0;
                for (DDataGrid domain : listData) {
                    if (domain.getRows().get(new SKeyForColumn("MAIN:PMASC_DEPART_CODE1")).getVal() != null) {
                        filtr += ((i == 0) ? "('" : ", '") + domain.getRows().get(new SKeyForColumn("MAIN:PMASC_DEPART_CODE1")).getVal().toString() + "'";
                        i++;
                    }
                }
                filtr += (i == 0) ? "" : ")";
                DRowColumnValString v = new DRowColumnValString();
                v.setVal(filtr);
                panelTableOdpt.getCondition().getFilters().put(new SKeyForColumn("MAIN:CODE"), v);

                BorderLayoutData layoutTableOdpt = new BorderLayoutData(LayoutRegion.SOUTH,250);
                layoutTableOdpt.setMargins(new Margins(6, 6, 6, 6));
                layoutTableOdpt.setSplit(true);

                main.add(panelTableOdpt, layoutTableOdpt);
                main.layout(true); //перерисовываем     
            }
        });
    }
}
