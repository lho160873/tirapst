package pst.arm.client.modules.tablegrid.service.remote;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.service.remote.GWTEditService;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/tableGridService")
public interface GWTTableGridService extends RemoteService, GWTEditService<TableGrid> {

    public List<TableGrid> getTableGrid (TableGridSearchCondition condition);
    public List<TableGrid> getAllTableGrid();//throw SQLException;
    public String myMethod(String s);
    
    public PagingLoadResult<TableGrid> getPage(TableGridSearchCondition condition);
    
    
}
    