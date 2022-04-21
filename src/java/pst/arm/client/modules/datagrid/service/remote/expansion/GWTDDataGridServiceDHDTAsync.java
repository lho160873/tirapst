package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;

import java.util.List;
import java.util.Map;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public interface GWTDDataGridServiceDHDTAsync {

    void updateJobs(Integer orderID, AsyncCallback<String> async);

    void calcCosts(Integer interactingSystId, Integer orderID, AsyncCallback<Boolean> async);

}

