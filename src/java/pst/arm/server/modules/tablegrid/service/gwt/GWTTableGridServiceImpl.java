package pst.arm.server.modules.tablegrid.service.gwt;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;
import pst.arm.client.modules.tablegrid.service.remote.GWTTableGridService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.tablegrid.service.TableGridService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service("GWTTableGridService")
public class GWTTableGridServiceImpl extends GWTController implements GWTTableGridService{
    private TableGridService tableGridService;
    private static Logger log = Logger.getLogger("GWTTableGridServiceImpl");
    
    
    @Autowired
    public void setTableGridService(TableGridService tableGridService) {
        this.tableGridService = tableGridService;
    }

    /*@Override
    public PagingLoadResult<Dictionary> getPage(GridSearchCondition condition) {     
        List<Dictionary> dictionaries = dictionaryService.getDictionaries(condition);
        int limit = dictionaryService.getDictinariesCount();
        return new BasePagingLoadResult<Dictionary>(dictionaries, condition.getOffset(), limit);
    }

    @Override
    public Boolean deleteDictionary(Dictionary dictionary) {
        return dictionaryService.deleteDictionary(dictionary);
    }

    @Override
    public Dictionary save(Dictionary domain, SearchCondition condition) {
        return dictionaryService.saveDictionary(domain);
    }

    @Override
    public Dictionary getDomainById(Long id , SearchCondition condition) {
        return dictionaryService.getDictionary(id);
    }

    @Override
    public Boolean deleteDomainsByIds(List<Integer> ids) {
        //TODO: add implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    @Override
    public List<TableGrid> getTableGrid(TableGridSearchCondition condition) {
        return tableGridService.getTableGrid(condition);
    }
    
    @Override
    public List<TableGrid> getAllTableGrid(){
        return tableGridService.getAllTableGrid();
        /*try {          

        } catch (UsernameNotFoundException u) {           
            throw new BusinessException(u);
        }*/

         //throw new BusinessException("ERROR");
    }
    
    @Override
    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        //return "Server says: " + s;
        return tableGridService.myMethod(s);
    }
    
    @Override
    public PagingLoadResult<TableGrid> getPage(TableGridSearchCondition condition){     
        List<TableGrid> domain = tableGridService.getTableGrid(condition);
        int limit = tableGridService.getTableGridCount(condition);
        return new BasePagingLoadResult<TableGrid>(domain, condition.getOffset(), limit);
    }
    
    @Override
    public TableGrid save(TableGrid domain, Boolean isNew) {
        return tableGridService.saveTableGrid(domain, isNew);
    }

    @Override
    public TableGrid getDomainById(Long id) {
        return tableGridService.getTableGrid(id);
    }

    @Override
    public Boolean deleteDomainsByIds(List<Integer> ids) {
        return tableGridService.deleteTableGrid(ids);
    }
  
}
