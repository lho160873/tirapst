package pst.arm.client.modules.datagrid.domain.expansion;

import pst.arm.client.modules.datagrid.domain.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
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
public class DColumnBuilderGroupForOGT  extends DColumnBuilderGroup {
    
    @Override
    public void setColumns(HashMap<SKeyForColumn, DColumn> c) {
        this.columns = c;
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            DColumn col = (DColumn) colEntry.getValue();
            String colName = key.getTableAlias() + "." + this.getColumn(key).getName();
            if (col.getColumnProperty().getFormat().equals("dd.MM.yyyy HH:mm")) {
                col.setSqlForColumn("( CONVERT(VARCHAR," + colName + ",104)+' ' +LEFT(CONVERT(VARCHAR," + colName + ",8),5) ) ");
            } else {
                col.setSqlForColumn(" CONVERT(VARCHAR," + colName + ",104) ");
            }
        }
    }


    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
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
            field.setName(key.getTableAlias() + "-" + key.getColumnName());
            FormHelper.setReadOnlyProp(field, !this.getColumn(key).getIsEditable());
            //field.setEnabled(this.getColumn(key).getIsEditable());
            FormData data = new FormData("100%");
            data.setMargins(new Margins(0, 32, 0, 0));
            fieldSet.add(field, data);
            fields.put(key, field);
        }
        return fields;
    }

    //заполняет соответсвующее поля переданным значением
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (!domain.getRows().containsKey(key)) {
            field.clear();
            return;            
        }
        if (domain.getRows().get(key).getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        field.setRawValue((String)domain.getRows().get(key).getFormatVal(key, this));
    }
    
    //заполняет соответсвующее поля переданным значением
     @Override
    public   void setValueFromField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
         val.setValFromFormat(key, field.getRawValue(),this);                                
    }
           @Override
    public  void setValueToField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
         if (val.getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        field.setRawValue((String)val.getFormatVal(key, this));
    } 
         
    
    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return sqlForCol + "='" + val + "'";
        }
        return key.getTableAlias() + "." + this.getColumn(key).getName() + "='" + val + "'";
    }
    
}
