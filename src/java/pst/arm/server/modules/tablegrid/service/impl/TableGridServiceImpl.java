package pst.arm.server.modules.tablegrid.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;
import pst.arm.server.modules.tablegrid.dao.TableGridDAO;
import pst.arm.server.modules.tablegrid.service.TableGridService;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class TableGridServiceImpl implements TableGridService{
   
    private TableGridDAO tablegridDAO;

    @Autowired
    public void setTableGridDAO(TableGridDAO tablegridDAO) {
        this.tablegridDAO = tablegridDAO;
    }
       
   @Override
    public List<TableGrid> getAllTableGrid() {
        List<TableGrid> listTableGrid = tablegridDAO.getAllTableGrid();                
        return listTableGrid;
   }

   @Override
    public List<TableGrid> getTableGrid(TableGridSearchCondition condition) {
        List<TableGrid> listTableGrid = tablegridDAO.getTableGrid(condition);
                
        return listTableGrid;
    }
    @Override
    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        return "Server says: " + s;
        
    }

    @Override
    public int getTableGridCount(TableGridSearchCondition condition) {
        return tablegridDAO.getTotalCount(condition);
    }
    
    @Override
    public TableGrid saveTableGrid(TableGrid domain, Boolean isNew)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        //if(domain.getId() == null)
        if ( isNew )
        {
            return tablegridDAO.insert(domain);
        }
        else
        {
            tablegridDAO.update(domain);
            return domain;
        }
    }
    

    @Override
    public Boolean deleteTableGrid(List<Integer> ids)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        for(Integer id : ids){
           tablegridDAO.delete(id);
            // dictionaryContentService.deleteRow(id.longValue());
        }
        return true;
    }
    
    @Override
    public TableGrid getTableGrid(Long id)
    {
     //throw new UnsupportedOperationException("Not supported yet.");
       return tablegridDAO.getTableGrid(id);
    }
}
