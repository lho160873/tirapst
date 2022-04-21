package pst.arm.server.modules.tablegrid.dao;

import java.util.List;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface TableGridDAO {
    public List<TableGrid> getTableGrid(TableGridSearchCondition condition);
    public List<TableGrid> getAllTableGrid();
    
    
    
    public TableGrid insert(TableGrid domain);
    public boolean update(TableGrid domain);
    public boolean delete(long id);
    
    public TableGrid getTableGrid(long id);

    public int getTotalCount(TableGridSearchCondition condition);
}