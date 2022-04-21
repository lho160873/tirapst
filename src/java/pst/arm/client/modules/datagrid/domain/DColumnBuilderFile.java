package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wesStyle
 */
public class DColumnBuilderFile extends IColumnBuilder implements Serializable
{       
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
            field.setName(key.getTableAlias() + "-" + key.getColumnName());
             FormHelper.setReadOnlyProp(field,!this.getColumn(key).getIsEditable()); 
            //field.setEnabled(this.getColumn(key).getIsEditable());
            FormData data = new FormData("100%");
            data.setMargins(new Margins(0, 32, 0, 0));
            fieldSet.add(field,data );
            fields.put(key, field);
        }
        return fields;    
    }
    
    //заполняет соответсвующее поля переданным значением
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (!domain.getRows().containsKey(key)) {
            field.clear();
            return;            
            /*if (this.getColumns().get(key).getColumnProperty().getDefaultValue() == null) {
                field.clear();
                return;
            }
            if (this.getColumns().get(key).getColumnProperty().getFormatFromValue(this.getColumns().get(key).getColumnProperty().getDefaultValue()) == null) {
                field.clear();
                return;
            }
            field.setValue(this.getColumns().get(key).getColumnProperty().getFormatFromValue(this.getColumns().get(key).getColumnProperty().getDefaultValue()));
            return;*/
        }
        if (domain.getRows().get(key).getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        field.setValue(domain.getRows().get(key).getFormatVal(key, this));
    }

    //заполняет данные из полей ввода
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid domain) {
        
        IRowColumnVal val = this.createRowColumnVal(key);
        setValueFromField(key, field, val);
        domain.getRows().put(key, val);
    }

    //заполняет соответсвующее поля переданным значением
     @Override
    public   void setValueFromField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
         val.setValFromFormat(key, field.getValue(),this);
                
    }
     
    
}


