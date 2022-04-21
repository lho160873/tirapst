package pst.arm.server.modules.datagrid.dao;

import java.util.List;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DDataGridDAO {

    public List<DDataGrid> select(String tableName);

    public List<DDataGrid> select(String tableName, DataGridSearchCondition condition, Boolean isPaging);

    public DDataGrid selectRow(String tableName, long id);

    public DDataGrid selectRow(String tableName, DDataGrid domain);

    public int count(String tableName, DataGridSearchCondition condition);

    public DDataGrid insert(String tableName, DDataGrid domain);

    public boolean update(String tableName, DDataGrid domain, DDataGrid oldDomain);

    public boolean delete(String tableName, DDataGrid domain);

    public DDataGrid selectSumm(String tableName, DataGridSearchCondition condition);

    public String getQueryWhere(DTable table, DataGridSearchCondition condition);

    public void execProcedure(String sql);
    public void execProcedure(String sql, String dbName);
    
//    public Boolean getCheckBoxState(int userId, String tablesNames);
    public void updateColumnsState (int userId, String tablesNames, String columnsStates);
    
    public void deleteColumnsState (int userId, String tablesNames);
    
    public String getColumnsState (int userId, String tablesNamen);
}