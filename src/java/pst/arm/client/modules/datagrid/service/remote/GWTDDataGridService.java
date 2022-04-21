package pst.arm.client.modules.datagrid.service.remote;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.service.remote.GWTEditService;
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
@RemoteServiceRelativePath("service/ddataGridService")
public interface GWTDDataGridService extends RemoteService, GWTEditService<DDataGrid> {

    public List<DDataGrid> getDataGrid(String tableName, DataGridSearchCondition condition) throws RpcServiceException;

    public List<DDataGrid> getAllDataGrid(String tableName) throws RpcServiceException;//throw SQLException;

    public Integer updateOfficeDocPeriods();

    public PagingLoadResult<DDataGrid> getPage(String tableName, DataGridSearchCondition condition) throws RpcServiceException;

    public PagingLoadResult<DDataGrid> getPage(String tableName, DataGridSearchCondition condition, DCondition addCnd) throws RpcServiceException;

    public List<DDataGrid> getTreeChildrenData(DDataGrid row, String tableName, DataGridSearchCondition condition) throws RpcServiceException;

    public List<DDataGrid> getTreeChildAllParents(DDataGrid row, String tableName) throws RpcServiceException;

    public DTable getTable(String tableName) throws RpcServiceException;

    public Map< String, DTable> getTableMap() throws RpcServiceException;

    public DDataGrid save(String tableName, DDataGrid domain, DDataGrid oldDomain, Boolean isNew) throws RpcServiceException;

    public DDataGrid getDomainById(String tableName, Long id) throws RpcServiceException;

    public Boolean deleteDomains(String tableName, List<DDataGrid> domains) throws RpcServiceException; //LKHorosheva

    public DDataGrid getDataGridById(String tableName, DDataGrid domain) throws RpcServiceException;
    // public void updateTableBuilder(String tableName,int ind);

    public Boolean openHelpManual(String filename) throws RpcServiceException;

    public Boolean execProc(String sql) throws RpcServiceException;

    public Boolean execProc(String sql, String dbName) throws RpcServiceException;

    public DDataGrid getDataGridSumm(String tableName, DataGridSearchCondition condition) throws RpcServiceException;

//    public Boolean getCheckBoxState(int userId, String tablesNames) throws RpcServiceException; 
    
    public void updateColumnsState (int userId, String tablesNames, String columnsStates) throws RpcServiceException;
    
    public void deleteColumnsState (int userId, String tablesNames) throws RpcServiceException;
    
    public String getColumnsState (int userId, String tablesNames) throws RpcServiceException;
    
    public Boolean insert(String tableName, List<DDataGrid> domain) throws RpcServiceException;
}
