package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 * createColumnConfig
 * 
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class IColumnBuilder  implements Serializable{
    //protected static int FORM_LABEL_WIDTH_DEFAULT = 150;
    protected HashMap<SKeyForColumn, DColumn> columns;//колонка (название)    
    protected Boolean isInit;
    protected Boolean isNotSearch = false; // Если true - не включать в WHERE
    protected String customSqlWhere = null;
    // Вставить это поле начиная с нового столбца в фильтре
    protected Boolean isNewColumn = false;

    public Boolean getIsNewColumn() {
        return isNewColumn;
    }

    public void setIsNewColumn(Boolean isNewColumn) {
        this.isNewColumn = isNewColumn;
    }    

    public Boolean getNotSearch() {
        return isNotSearch;
    }

    public void setNotSearch(Boolean notSearch) {
        isNotSearch = notSearch;
    }

    public String getCustomSqlWhere() {
        return customSqlWhere;
    }

    public void setCustomSqlWhere(String customSqlWhere) {
        this.customSqlWhere = customSqlWhere;
    }

    public void setIsInit(Boolean isInit)
    {
        this.isInit = isInit;
    }
    public IColumnBuilder()
    {
        isInit = Boolean.FALSE;
    }
    public void  setColumns( HashMap<SKeyForColumn,DColumn> c )
    {
        this.columns = c;
    }

    
    public HashMap<SKeyForColumn,DColumn> getColumns()
    {
        return this.columns;
    }
    
    public void  setColumn( SKeyForColumn key, DColumn c )
    {
        if (!this.columns.containsKey(key)) {
            this.columns.put(key, c);
        }
    }
    
    public DColumn getColumn(SKeyForColumn key)
    {
        return this.columns.get(key);
    }

    public void updateData(GWTDDataGridService service, HashMap<SKeyForColumn, Field> fields) {
        isInit = false;
        initData(service);
    }
     public void initData(GWTDDataGridService service)
    {
        if (!isInit) {
            isInit = true;
        }
    }
              
    public ColumnConfig createColumnConfig(SKeyForColumn key, final DTable table, final String id) //построитель для полей отображения в таблице
    {
        ColumnConfig config = this.getColumn(key).getColumnProperty().createColumnConfig();

        config.setId(key.getTableAlias() + ":" + key.getColumnName());
        config.setHeader(this.getColumn(key).getCaption());
        config.setHidden(!this.getColumn(key).getIsVisible());
        if (this.getColumns().get(key).getIsHidden()) {
            config.setHidden(true);
            config.setFixed(true);
        }
        GridCellRenderer<BeanModel> renderer = this.getColumn(key).getColumnProperty().createRenderer(table, id);
        config.setRenderer(renderer);
        return config;
    }
    
    public ColumnConfig createTreeColumnConfig(SKeyForColumn key, final DTable table, final String id) //построитель для полей отображения в таблице
    {
        ColumnConfig config = this.getColumn(key).getColumnProperty().createColumnConfig();

        config.setId(key.getTableAlias() + ":" + key.getColumnName());
        config.setHeader(this.getColumn(key).getCaption());
        config.setHidden(!this.getColumn(key).getIsVisible());
        if (this.getColumns().get(key).getIsHidden()) 
        {
            config.setHidden(true);
            config.setFixed(true);
        }
        SKeyForColumn treeKey = table.getTreeColumnID();
        if (key.equals(treeKey)) {
            config.setRenderer(this.getColumn(key).getColumnProperty().createTreeKeyRenderer(table, id));
        } else {
            config.setRenderer(this.getColumn(key).getColumnProperty().createTreeRenderer(table, id));
        }
        //TreeGridCellRenderer<BeanModel> renderer = this.getColumn(key).getColumnProperty().createTreeRenderer(table, id);
        //config.setRenderer(renderer);
        return config;
    }
    
          
    public IRowColumnVal createRowColumnVal(SKeyForColumn key)//построитель для значений данных   
    {
        if (!columns.containsKey(key)) {
            return null;
        }
        
         IRowColumnVal val = this.getColumns().get(key).getColumnProperty().createRowColumnVal();
         //val.setColumnBuilder(this);
         return val;
    }
    
     public LayoutContainer createLayoutContainer(Integer lw)
     {    
        LayoutContainer fieldSet = new LayoutContainer();
        fieldSet.setBorders(false);
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);
        fieldSet.setLayout(layout);
        fieldSet.setStyleAttribute("padding", "0px");
        fieldSet.setStyleAttribute("padding-left", "5px");
        //fieldSet.setStyleAttribute("padding", "5px");
        //проверяем есть ли хоть одно видимое поле
        Boolean isVisible = false;         
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
           isVisible = ( isVisible || this.getColumn(key).getIsVisibleEdit() );        
        }
        fieldSet.setVisible(isVisible);
        return fieldSet;
         
     };
     
    //построитель для полей ввода на форме редактирования
    public abstract HashMap<SKeyForColumn,Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw );
   // public abstract HashMap<SKeyForColumn,Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw, Boolean isAdd);

    //построитель для полей ввода на панели фильтра
   /* public HashMap<SKeyForColumn, Field> createFieldsForFiltr(LayoutContainer fieldSet) 
    {
        HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            if ( !this.getColumns().get(key).getIsFiltr() ) continue; 
            Boolean isNotNull = (this.getColumn(key).getIsNotNull() || this.getColumn(key).getIsKey());
            Field field = this.getColumns().get(key).getColumnProperty().createField(isNotNull);            
            field.setFieldLabel(this.getColumns().get(key).getCaption());
            field.setVisible(this.getColumn(key).getIsVisibleEdit());
            FormData data = new FormData("100%");
            data.setMargins(new Margins(0, 16, 0, 0));
            fieldSet.add(field, data);
            fields.put(key, field);
        }
        return fields;
    }*/
    
    //построитель для полей ввода на панели фильтра
    public BoxComponent createFieldForFiltr(SKeyForColumn key, LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Integer lw) {
        //HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();
        if (!this.getColumns().get(key).getIsFiltr()) {
            return null;
        }
        Field field = this.getColumns().get(key).getColumnProperty().createField(Boolean.FALSE); //для полей на панели фильтра разрешаем пустрые значения всегда
        field.setFieldLabel(this.getColumns().get(key).getCaption());
        
        
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);
        fieldSet.setLayout(layout);
        //fieldSet.setStyleAttribute("padding", "5px");
        
        FormData data = new FormData("100%");        
        data.setMargins(new Margins(0, 16, 0, 0));
        fieldSet.add(field, data);
        return field;     
       
        
    }
   
    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted) {
        if (isInserted) {
            if (this.getColumn(key).getIsInserted()) {
                FormHelper.setReadOnlyProp(field, !this.getColumn(key).getIsEditableAdd());
                //field.setEnabled(this.getColumn(key).getIsEditableAdd());
            } else {
                FormHelper.setReadOnlyProp(field, true);
                //field.setEnabled(false);
            }
        } else {
            FormHelper.setReadOnlyProp(field, !this.getColumn(key).getIsEditable());
            //field.setEnabled(this.getColumn(key).getIsEditable());
        }
    }

    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted, Boolean isView) {};
    
    //заполняет соответсвующее поля переданным значением
    public abstract  void setDomainValueToField( SKeyForColumn key, Field field, DDataGrid domain );
    
    //заполняет данные из полей ввода
    public abstract  void setDomainValueFromField( SKeyForColumn key, Field field, DDataGrid domain ); 
    
    //заполняет данные из полей ввода 
    public abstract  void setValueFromField( SKeyForColumn key, Field field, IRowColumnVal val );
    
    
    //заполняет поля ввода переданными данными
    public  void setValueToField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
         if (val.getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        field.setValue(val.getFormatVal(key, this));
    }

    public Boolean getIsNotNull(SKeyForColumn key) {
        return (this.getColumn(key).getIsNotNull() || (this.getColumn(key).getIsKey() && !this.getColumn(key).getIsAutoincrement()));
    }

    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        String upper = "";
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (this.getColumn(key).getColumnProperty() instanceof DColumnPropertyComboBoxInteger) {
            upper = val;
        } else {
            upper = "%" + val + "%";
        }

        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return " UPPER(" + sqlForCol + ") LIKE UPPER('" + upper + "') ";
        }
        return " UPPER(" + key.getTableAlias() + "." + this.getColumn(key).getName() + ") LIKE UPPER('" + upper + "') ";
    }
    
    public String getStirngForFiltr(SKeyForColumn key, String val) {
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol==null)
            sqlForCol = key.getTableAlias() + "." + this.getColumn(key).getName();
        String result = " ";
        if (val != null) {
            result += sqlForCol + "='" + val + "' ";
        } else {
            result += sqlForCol + " IS NULL ";

        }
        return result;
    }
    
}
