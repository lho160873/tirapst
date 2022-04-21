package pst.arm.client.modules.mainpage.renderers;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import pst.arm.client.modules.mainpage.lang.MainPageConstants;

/**
 *
 * @author Artem Vorontsov
 */
public class RequestStatusRenderer implements GridCellRenderer<BeanModel> {
    protected MainPageConstants constants = (MainPageConstants) GWT.create(MainPageConstants.class);

    @Override
    public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
        String requestStatus = (String) model.get(property);
        if(requestStatus.equals("executing")) {
            return constants.portletRequestGridStatusExecuting();
        } else if(requestStatus.equals("executed")) {
            return constants.portletRequestGridStatusExecuted();
        } if(requestStatus.equals("executedNoAnswer")) {
            return constants.portletRequestGridStatusExecutedNoAnswer();
        } else {
            return "";
        }
    }
}
