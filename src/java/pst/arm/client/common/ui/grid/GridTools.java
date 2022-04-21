package pst.arm.client.common.ui.grid;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.grid.Grid;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class GridTools {

    private static final String MULTI_LINE_COMMON = "multiline_common";
    private static final String MULTI_HEADER_GRID_STYLE = "multi_grid_header";
    private static final String MULTI_GRID_COLUMN="multilineColumn";

    public static void enableGridMultiHeading(final Grid grid) {
        if (grid != null) {
            if (GXT.isIE) {
                grid.addListener(Events.Render, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {
                        int totalColumns = grid.getColumnModel().getColumnCount();
                        for (int i = 0; i < totalColumns; i++) {
                            com.google.gwt.dom.client.Element elem = (com.google.gwt.dom.client.Element) grid.getView().getHeaderCell(i).getFirstChild();
                            elem.addClassName(MULTI_LINE_COMMON);
                        }
                    }
                });
            } else {
                grid.addStyleName(MULTI_HEADER_GRID_STYLE);
            }
        }
    }
    
    public static void enableGridMultiColumn(Grid grid){
        grid.addStyleName(MULTI_GRID_COLUMN);
    }
}
