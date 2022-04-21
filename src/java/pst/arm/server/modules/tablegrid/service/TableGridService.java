package pst.arm.server.modules.tablegrid.service;

import java.util.List;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface TableGridService {
    public List<TableGrid> getTableGrid(TableGridSearchCondition condition);
    public List<TableGrid> getAllTableGrid();
    public String myMethod(String s);
    public int getTableGridCount(TableGridSearchCondition condition);
    
    public TableGrid saveTableGrid(TableGrid domain, Boolean isNew);
    public TableGrid getTableGrid(Long id);
    public Boolean deleteTableGrid(List<Integer> ids);


   } 