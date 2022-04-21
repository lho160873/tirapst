package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.io.Serializable;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.DataUtil;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class IColumnProperty<T,D> implements Serializable{

    protected Integer widthColumn = 80; //ширина отображаемой колонки
    protected String format; //строка формата для отображения данные в поле ввода (например число 0,00)
    protected EColumnType type = EColumnType.STRING;
    protected Boolean isNullWhenEmptySearch = Boolean.FALSE; // Поле будет равно NULL в запросе, если при поиске не введено его значение
    protected T defaultValue = null;
    protected T defaultValueForFiltr = null;
    protected Boolean allowEmptyString = Boolean.TRUE; 

    public Boolean getAllowEmptyString() {
        return allowEmptyString;
    }

    public void setAllowEmptyString(Boolean allowEmptyString) {
        this.allowEmptyString = allowEmptyString;
    }
    
    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T getDefaultValueForFiltr() {
        return defaultValueForFiltr;
    }

    public void setDefaultValueForFiltr(T defaultValueForFiltr) {
        this.defaultValueForFiltr = defaultValueForFiltr;
    }
   

    public IColumnProperty() {
        format = "";
    }
    public EColumnType getType() {
        return type;
    }

    public void setType(EColumnType type) {
        this.type = type;
    }

   
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
    public Integer getWidthColumn() {
        return widthColumn;
    }

    public void setWidthColumn(Integer w) {
        this.widthColumn = w;
    }
    
    /*
     * функция без форматирования преобразует переданные данные в строку
     */
    public String valueToString(T value) {
        if (value != null) {
            return value.toString();
        }
        String str = null;
        return str;
    }
    
    /*
     * функция без форматирования преобразует переданные данные в строку
     */
    public String valueToString(DDataGrid domain, T value) {
        return valueToString(value);
    }

    /*
     * функция без форматирования преобразует строку в данные заданного типа
     */
    public T stringToValue(String str) {
        if (str == null) {
            T rc = null;
            return rc;
        }
        return (T) str;
    } 
   
    
     /*
     * функция возвращает отформатированные данные из оригинальных(полученных из БД в запросе), т.е. которые необходимо отображать в полях
     */
    public D getFormatFromValue(T value) 
    {
        return (D)value;
    }

    /*
     * функция возвращает оригинальные данные из отформатированных
     */    
    public T getValueFromFormat(D valueFormat) 
    {
        return (T)valueFormat;
    }
      
    public abstract Field createField(Boolean isNotNull); //построитель для полей ввода
    
    public BoxComponent createFieldForFiltr(Boolean isNotNull) //построитель для полей ввода
    {
        return createField(isNotNull);
    }
    
    public  IRowColumnVal createRowColumnVal(){ return new IRowColumnVal<T,D>();};//построитель для значений данных   
    
   

    
    public ColumnConfig createColumnConfig( ) //построитель для полей отображения в таблице
    {
         ColumnConfig config = new ColumnConfig("", "", 16); 
         config.setWidth(getWidthColumn());
         config.setNumberFormat(NumberFormat.getDecimalFormat());
         return config;
    }

    public Boolean getIsNullWhenEmptySearch() {
        return isNullWhenEmptySearch;
    }

    public void setIsNullWhenEmptySearch(Boolean isNullWhenEmptySearch) {
        this.isNullWhenEmptySearch = isNullWhenEmptySearch;
    }
    
    public boolean isNotChanges(T val_1, T val_2) {
        return DataUtil.compare(val_1, val_2);
    }
    
 public GridCellRenderer<BeanModel> createTreeRenderer(final DTable table, final String id) {
       GridCellRenderer<BeanModel> renderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    return AppHelper.getInstance().getStringNotNull(row.getRows().get(key).getStringFromVal(key, builder));
                } else {
                    return "";
                }
            }
        };
       return renderer;
 }
    
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> renderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    if (row.getRows().get(key).getType(key, builder) == EColumnType.DATE) {
                        return row.getRows().get(key).getStringFromVal(key, builder);
                        //return DateTimeFormat.getFormat("dd.MM.yyyy").format((Date)row.getRows().get(key).getFormatVal(key, builder));
                    } else {
                        //return row.getRows().get(key).getFormatVal(key, builder);
                        return row.getRows().get(key).getStringFromVal(key, builder);
                    }
                } else {
                    return "";
                }
            }
        };
        return renderer;
    }
    
    public TreeGridCellRenderer<BeanModel> createTreeKeyRenderer(final DTable table, final String id) {
        TreeGridCellRenderer<BeanModel> treeRenderer = new TreeGridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                config.css = "x-treegrid-column";
                assert grid instanceof TreeGrid : "TreeGridCellRenderer can only be used in a TreeGrid";
                TreeGrid tree = (TreeGrid) grid;
                TreeStore ts = tree.getTreeStore();
                int level = ts.getDepth(model);
                AbstractImagePrototype icon = calculateIconStyle(tree, model, property, rowIndex, colIndex);
                TreePanel.Joint j = calcualteJoint(tree, model, property, rowIndex, colIndex);
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);
                String id = getId(tree, model, property, rowIndex, colIndex);
                String text = "";
                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);
                    text = AppHelper.getInstance().getStringNotNull(row.getRows().get(key).getStringFromVal(key, builder));
                }
                return tree.getTreeView().getTemplate(model, id, text, icon, false, j, level - 1);
            }
        };
        return treeRenderer;
    }
}