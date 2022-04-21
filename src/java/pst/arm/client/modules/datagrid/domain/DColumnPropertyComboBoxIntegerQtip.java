package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import com.google.gwt.i18n.client.DateTimeFormat;
import pst.arm.client.common.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1getFormatFromValue
 */
public class DColumnPropertyComboBoxIntegerQtip extends DColumnPropertyComboBoxInteger{
    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> statusRenderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);

                    String qtip = "";
                    QuickTip tip = new QuickTip(grid);
                    IRowColumnVal value = row.getRows().get(key);

                    ArrayList<DListValIntegerString> list = ((DColumnPropertyComboBoxIntegerQtip)builder.getColumn(key).getColumnProperty()).getList();
                    String val1="";

                    for (DListValIntegerString val : list) {
                        if ((value != null) && ((Integer)value.getVal() == val.getKey().intValue())) {
                            qtip = val.getVal();
                            val1 = val.getKey().toString();
                            break;
                        }
                    }

                    String html = "<span " + "qtip='" + qtip + "'" + ">"  + val1 + "</span>";
                    return html;
                }
                return "";
            }
        };
        return statusRenderer;
    }
}
