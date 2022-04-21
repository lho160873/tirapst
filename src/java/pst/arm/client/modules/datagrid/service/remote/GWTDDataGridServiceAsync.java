package pst.arm.client.modules.datagrid.service.remote;

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
public interface GWTDDataGridServiceAsync extends GWTEditServiceAsync<DDataGrid> {

    public void getDataGrid(String tableName, DataGridSearchCondition condition, AsyncCallback<List<DDataGrid>> callback);

    public void getAllDataGrid(String tableName, AsyncCallback<List<DDataGrid>> callback);

    public void updateOfficeDocPeriods(AsyncCallback<Integer> async);

    public void getPage(String tableName, DataGridSearchCondition condition, AsyncCallback<PagingLoadResult<DDataGrid>> callback);

    public void getPage(String tableName, DataGridSearchCondition condition, DCondition addCnd, AsyncCallback<PagingLoadResult<DDataGrid>> callback);

    public void getTreeChildrenData(DDataGrid row, String tableName, DataGridSearchCondition condition, AsyncCallback<List<DDataGrid>> callback);

    public void getTreeChildAllParents(DDataGrid row, String tableName, AsyncCallback<List<DDataGrid>> callback);

    public void getTable(String tableName, AsyncCallback<DTable> callback);

    public void getTableMap(AsyncCallback<Map< String, DTable>> callback);

    public void save(String tableName, DDataGrid domain, DDataGrid oldDomain, Boolean isNew, AsyncCallback<DDataGrid> callback);

    public void getDomainById(String tableName, Long id, AsyncCallback<DDataGrid> callback);

    public void deleteDomains(String tableName, List<DDataGrid> domains, AsyncCallback<Boolean> callback); //LKHorosheva

    public void getDataGridById(String tableName, DDataGrid domain, AsyncCallback<DDataGrid> callback);

    //public void updateTableBuilder(String tableName,int ind,AsyncCallback<Void> callback );
    public void openHelpManual(String filename, AsyncCallback<Boolean> callback);

    public void getDataGridSumm(String tableName, DataGridSearchCondition condition, AsyncCallback<DDataGrid> callback);

    public void execProc(String sql, AsyncCallback<Boolean> callback);
    public void execProc(String sql, String dbName, AsyncCallback<Boolean> callback);

//    public void getCheckBoxState(int userId, String tablesNames, AsyncCallback<Boolean> callback); 
    
    public void updateColumnsState (int userId, String tablesNames, String columnsStates, AsyncCallback<DDataGrid> callback);
    
    public void deleteColumnsState (int userId, String tablesNames, AsyncCallback<DDataGrid> callback);
    
    public void getColumnsState (int userId, String tablesNames, AsyncCallback<String> callback);
    
    public void insert(String tableName, List<DDataGrid> domain, AsyncCallback<Boolean> callback);
    
}
