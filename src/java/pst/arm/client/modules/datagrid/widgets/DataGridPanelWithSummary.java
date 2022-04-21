package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.HashMap;
import pst.arm.client.modules.datagrid.domain.*;

/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
public class DataGridPanelWithSummary extends DataGridPanel {

    HashMap<Integer, SKeyForColumn> summaries;
    NumberFormat format;
    Integer labelColumn;

    public DataGridPanelWithSummary(String tableName, HashMap<Integer, SKeyForColumn> summaries, NumberFormat format, Integer labelColumn) {
        super(tableName);
        this.summaries = summaries;
        this.format = format;
        this.labelColumn = labelColumn;
    }

    @Override
    protected void setAggregations(ColumnConfig cc) {
        summary.setSummaryType(cc.getId(), SummaryType.SUM);
        summary.setRenderer(cc.getId(), new AggregationRenderer<BeanModel>() {
            public Object render(Number value, int colIndex, Grid<BeanModel> grid, ListStore<BeanModel> store) {
                if (colIndex == labelColumn)
                    return "Сумма:";

                if (value != null) {
                    Double sum = 0.;
                    boolean wasCalculated = false;
                    for (BeanModel bm : store.getModels()) {
                        DDataGrid dg = bm.getBean();
                        if (dg.getRows().containsKey(summaries.get(new Integer(colIndex)))) {
                            if (dg.getRows().get(summaries.get(new Integer(colIndex))).getVal() != null)
                                sum += (Double) dg.getRows().get(summaries.get(new Integer(colIndex))).getVal();
                            wasCalculated = true;
                        }
                    }
                    String result = "";
                    if (wasCalculated)
                        result = format.format(sum);
                    return result;
                }
                return "";
            }
        });
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        isDictAdd = false;
        isDictDelete = false;
        isDictEdit = false;
        isDictView = false;
        isSprDictEdit=false;

        btnAdd.setVisible(isDictAdd);
        menuItemAdd.setVisible(isDictAdd);
        
        btnEdit.setVisible(isDictEdit);
        menuItemEdit.setVisible(isDictEdit);
        
        btnDelete.setVisible(isDictDelete);
        menuItemDelete.setVisible(isDictDelete);

        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);
        
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
      
        updateToolBar();        
    }
}