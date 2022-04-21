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
 * @author Evgeni Rusakov
 */
public class DColumnPropertyFactLabourForPlanFactNiokr extends DColumnPropertyTextField {
    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> statusRenderer;
        statusRenderer = new GridCellRenderer<BeanModel>() {
@Override
public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
DDataGrid row = model.getBean();

SKeyForColumn key = new SKeyForColumn(property);
if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {

Double dflDivDrl  = 0.0;
Double dflDivDrl0 = 0.0;
SKeyForColumn dflDivDrlKey0 = new SKeyForColumn("MAIN", "PLAN_LABOUR");
SKeyForColumn dflDivDrlKey = new SKeyForColumn("MAIN", "FACT_LABOUR");

if (row.getRows().get(dflDivDrlKey0).getVal() != null) {
dflDivDrl0 = (Double) row.getRows().get(dflDivDrlKey0).getVal();
}
if (row.getRows().get(dflDivDrlKey).getVal() != null) {
dflDivDrl = (Double) row.getRows().get(dflDivDrlKey).getVal();
}
String color = "";
String qtip = "";
QuickTip tip = new QuickTip(grid);

if (dflDivDrl.compareTo(dflDivDrl0) == -1) {
color = "yellow";
qtip = "Списано меньше запланированного";
} else if (dflDivDrl.compareTo(dflDivDrl0) == 1) {
//color = "red";
color = "pink";
qtip = "Списано больше запланированного";
}
String dDD = "";
if (row.getRows().get(dflDivDrlKey).getVal() != null) {
    
dDD = dflDivDrl.toString();
dDD = dDD.replace(".", ",");
if (dDD.indexOf(",") == -1 && dDD.length()>0){
    dDD = dDD + ",00";
}
else if (dDD.substring(dDD.indexOf(",")+1).length() == 1) {
    dDD = dDD + "0";
}
}
String html = "<span " + "qtip='" + qtip + "'" + ">" + "<div style='background: " + color + "; '> " + dDD + " </div>" + "</span>";
row.getRows().get(new SKeyForColumn("MAIN:FACT_LABOUR_COLOR")).setVal(qtip);
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
    public String valueToString(DDataGrid domain, String value) {
       String str = "";
        Double dflDivDrl  = 0.0;
        Double dflDivDrl0 = 0.0;
        SKeyForColumn dflDivDrlKey0 = new SKeyForColumn("MAIN", "PLAN_LABOUR");
        SKeyForColumn dflDivDrlKey = new SKeyForColumn("MAIN", "FACT_LABOUR");

        if (domain.getRows().get(dflDivDrlKey0).getVal() != null) {
            dflDivDrl0 = (Double) domain.getRows().get(dflDivDrlKey0).getVal();
        }
        if (domain.getRows().get(dflDivDrlKey).getVal() != null) {
            dflDivDrl = (Double) domain.getRows().get(dflDivDrlKey).getVal();
        }

        if (dflDivDrl.compareTo(dflDivDrl0) == -1) {
            str = "Списано меньше запланированного";
        } else if (dflDivDrl.compareTo(dflDivDrl0) == 1) {
            str = "Списано больше запланированного";
        }
return str;
    }
}
