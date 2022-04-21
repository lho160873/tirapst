/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyTextField;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author Igor
 */
public class DColumnPropertyStatusForWorkshop extends DColumnPropertyTextField {
    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> statusRenderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();

                SKeyForColumn key = new SKeyForColumn(property);
                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {

                    Double dflDivDrl = 0.0;
                    SKeyForColumn dflDivDrlKey = new SKeyForColumn("MAIN", "DFL_DIV_DRL");

                    if (row.getRows().get(dflDivDrlKey).getVal() != null) {
                        dflDivDrl = (Double) row.getRows().get(dflDivDrlKey).getVal();
                    }
                    String color = "";
                    String qtip = "";
                    QuickTip tip = new QuickTip(grid);

                    if (dflDivDrl < 90) {
                        color = "#FF0000";
                        qtip = "Недокомплект";
                    } else if ((dflDivDrl >= 90) && (dflDivDrl <= 105)) {
                        color = "#00FF00";
                        qtip = "Укомплектован";
                    } else if (dflDivDrl > 105) {
                        color = "#0000FF";
                        qtip = "Превышение штатов";
                    }

                    String html = "<span " + "qtip='" + qtip + "'" + ">" + "<div style='color: " + color + "; text-align: center; font-size: 14pt'> &#9679; </div>" + "</span>";
                    row.getRows().get(new SKeyForColumn("MAIN:STATUS")).setVal(qtip);
                    return html;
                }
                return "";
            }
        };
        return statusRenderer;
    }

    @Override
    public ColumnConfig createColumnConfig() //построитель для полей отображения в таблице
    {
        ColumnConfig config = new ColumnConfig("", "", 16);
        config.setWidth(getWidthColumn());
        //config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        //config.setFixed(true);
        return config;
    }
    
    @Override
    public String valueToString( DDataGrid domain, String value) {
        String str = null;
        SKeyForColumn dflDivDrlKey = new SKeyForColumn("MAIN", "DFL_DIV_DRL");
        
        if (!domain.getRows().isEmpty()
                && domain.getRows().containsKey(dflDivDrlKey)) {

            Double d = 0d;

            if (domain.getRows().get(dflDivDrlKey).getVal() != null) {
                d = (Double) domain.getRows().get(dflDivDrlKey).getVal();
            }

            if (d < 90) {
                str = "Недокомплект";
            } else if (d>=90&&d<=105) {
                str = "Укомплектован";
            } else {
                str = "Превышение штатов";
            }
        }
        return str;
    }
}
