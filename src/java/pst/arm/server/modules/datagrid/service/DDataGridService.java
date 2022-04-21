package pst.arm.server.modules.datagrid.service;

import java.util.List;
import java.util.Map;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DDataGridService {

    public Boolean execProcedure(String sql);

    public Boolean execProcedure(String sql, String dbName);

    public List<DDataGrid> getDataGrid(String tableName, DataGridSearchCondition condition);

    public List<DDataGrid> getDataGridPage(String tableName, DataGridSearchCondition condition);

    public List<DDataGrid> getAllDataGrid(String tableName);
    //public String myMethod(String s);

    public int getDataGridCount(String tableName, DataGridSearchCondition condition);

    public DDataGrid saveDataGrid(String tableName, DDataGrid domain, DDataGrid oldDomain, Boolean isNew);

    public DDataGrid getDataGrid(String tableName, Long id);

    public DDataGrid getDataGridById(String tableName, DDataGrid domain);

    public Boolean deleteDataGrid(String tableName, List<DDataGrid> domains);

    public List<Map<String, Object>> getFullFileNameHelpManual(String filename);

    public DDataGrid getDataGridSumm(String tableName, DataGridSearchCondition condition);

//    public Boolean getCheckBoxState (int userId, String tablesNames);
    
    public void updateColumnsState (int userId, String tablesNames, String columnsStates);
    
    public void deleteColumnsState (int userId, String tablesNames);
    
    public String getColumnsState (int userId, String tablesNames);
    
    public Boolean insertDataGrid(String tableName,  List<DDataGrid> domains);
}
