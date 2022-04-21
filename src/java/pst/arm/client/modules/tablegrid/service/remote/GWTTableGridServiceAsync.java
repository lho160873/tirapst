package pst.arm.client.modules.tablegrid.service.remote;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.domain.search.TableGridSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface GWTTableGridServiceAsync extends GWTEditServiceAsync<TableGrid> {

    public void getTableGrid(TableGridSearchCondition condition, AsyncCallback<List<TableGrid>> callback);

    public void getAllTableGrid(AsyncCallback<List<TableGrid>> callback);

    public void myMethod(String s, AsyncCallback<String> callback);

    public void getPage(TableGridSearchCondition condition, AsyncCallback<PagingLoadResult<TableGrid>> callback);
}
