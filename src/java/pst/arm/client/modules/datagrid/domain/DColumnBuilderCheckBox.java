package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderCheckBox extends IColumnBuilder implements Serializable{

    public DColumnBuilderCheckBox()
    {
       super();
     }
    @Override
     public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw ) {
 
        HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();

        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            Boolean isNotNull = this.getIsNotNull(key);
            Field field = this.getColumns().get(key).getColumnProperty().createField(isNotNull);

            if (isNotNull) {
                FormHelper.setNotEmptyFieldLabel(this.getColumn(key).getCaption(), field);
            } else {
                field.setFieldLabel(this.getColumn(key).getCaption());
            }
            field.setVisible(this.getColumn(key).getIsVisibleEdit());
            FormHelper.setReadOnlyProp(field,!this.getColumn(key).getIsEditable());            
            //field.setEnabled(this.getColumn(key).getIsEditable());
            FormData data = new FormData("100%");
            data.setMargins(new Margins(0, 32, 0, 0));
            fieldSet.add(field,data );
            fields.put(key, field);
        }
        return fields;    
    }
    
    
    //заполняет поля ввода переданными данными
    @Override
    public  void setValueToField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
        if (field.getClass() != CheckBox.class)
        {
            field.clear();
            return;
        }
        if (val.getVal() == null) {
            field.clear();
            return;
        }
        field.setValue((Boolean) val.getVal());
    }
    
    //заполняет соответсвующее поля переданным значением
    @Override
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (field.getClass() != CheckBox.class) {
            field.clear();
            return;
        }
        if (!domain.getRows().containsKey(key)) {
            field.clear();
            return;
        }
         if (domain.getRows().get(key).getVal() == null) {
            field.clear();
            return;
        }
        
          field.setValue((Boolean) domain.getRows().get(key).getVal());
    }

    //заполняет данные из полей ввода
    @Override
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid domain) {
        
        IRowColumnVal val = this.createRowColumnVal(key);
        setValueFromField(key, field, val);

        domain.getRows().put(key, val);
    }

    //заполняет соответсвующее поля переданным значением
    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        if (field.getValue()==null)
        {
           val.setVal((Boolean)null); 
        }
        else{
        val.setVal((Boolean)field.getValue());
        }
    }
    
    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        if (val.isEmpty()) {
            return " ";
        }
        String vs = "0";
        Boolean b = Boolean.valueOf(val);
        if (b) {
            vs = "1";
        }
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return sqlForCol + "='" + vs + "'";
        }

        return key.getTableAlias() + "." + this.getColumn(key).getName() + "='" + vs + "'";

    }
    
}
