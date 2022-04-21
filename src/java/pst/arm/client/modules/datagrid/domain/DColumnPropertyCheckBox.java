package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import java.io.Serializable;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyCheckBox extends IColumnProperty<Boolean,Boolean> implements Serializable {

    public DColumnPropertyCheckBox() {
        super();
        type = EColumnType.BOOLEAN;
    }

   
    @Override
    public Field createField(Boolean isNotNull) //простой построитель для полей ввода
    {
        CheckBox field = new CheckBox();
        field.setBoxLabel("   ");
        return field;
    }

    @Override
    public IRowColumnVal createRowColumnVal()//построитель для значений данных  
    {
        if (defaultValue == null) {
            return new DRowColumnValBoolean();
        }
        DRowColumnValBoolean val = new DRowColumnValBoolean();
        val.setVal( (Boolean)defaultValue );   
        return val;
    }
    
    @Override
    public ColumnConfig createColumnConfig() //построитель для полей отображения в таблице
    {
        CheckColumnConfig config = new CheckColumnConfig();
        config.setAlignment(Style.HorizontalAlignment.CENTER);
        config.setWidth(getWidthColumn());
        return config;
    }
    
    
    @Override
    public Boolean stringToValue(String str) {
        if (str == null) {
            Boolean rc = null;
            return rc;
        }
        Integer val = Integer.valueOf(str.trim());
        if (val == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    } 
    
    @Override
    public String valueToString(Boolean value) {
        if (value != null) {
            if (value) {
                return "1";
            } else {
                return "0";
            }
        }
        String str = null;
        return str;
    }

    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> rendererFprCheckBox = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    config.css = "x-grid3-check-col-td";
                    Boolean v = (row.getRows().get(key).getStringFromVal(key, builder) == "1");
                    String checked = (v != null && v) ? "-on" : "";
                    if (GXT.isAriaEnabled()) {
                        config.cellAttr = "aria-checked=" + (checked.equals("-on") ? "true" : "false");
                    }
                    return "<div class='x-grid3-check-col" + " x-grid3-check-col" + checked + " x-grid3-cc-" + id
                            + "'> </div>";
                } else {
                    return "";
                }
            }
        };
        return rendererFprCheckBox;
    }

    @Override
    public GridCellRenderer<BeanModel> createTreeRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> rendererFprCheckBox = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    config.css = "x-grid3-check-col-td";
                    Boolean v = (row.getRows().get(key).getStringFromVal(key, builder) == "1");
                    String checked = (v != null && v) ? "-on" : "";
                    if (GXT.isAriaEnabled()) {
                        config.cellAttr = "aria-checked=" + (checked.equals("-on") ? "true" : "false");
                    }
                    return "<div class='x-grid3-check-col" + " x-grid3-check-col" + checked + " x-grid3-cc-" + id
                            + "'> </div>";
                } else {
                    return "";
                }
            }
        };
        return rendererFprCheckBox;
    }
}