package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author Admin
 */
public class DColumnBuilderComboBoxDBExt extends IColumnBuilder implements Serializable {

    protected String queryNameForRelation; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyRelationForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyRelationForName;//ключ к полю, которое хотим помещать в данные 
    
     
    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw ) {
        
      HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();

        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();

            IColumnProperty property = this.getColumns().get(key).getColumnProperty();

            Boolean isNotNull = this.getIsNotNull(key);
            //Field field = this.getColumns().get(key).getColumnProperty().createField(isNotNull);
            DGComboBox<String> field = new DGComboBox<String>(queryNameForRelation, keyRelationForId, keyRelationForName);

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
    public void setValueToField(SKeyForColumn key, Field field, IRowColumnVal val) {
        if (field.getClass() != DGComboBox.class) {
            field.clear();
            return;
        }
        if (val.getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        String sval = String.valueOf(val.getFormatVal(key, this));
        ((DGComboBox) field).setValueId(sval);
    }
    
    //заполняет соответсвующее поля переданным значением
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (field.getClass() != DGComboBox.class) {
            field.clear();
            return;
        }
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
            String sval = String.valueOf(this.getColumns().get(key).getColumnProperty().getFormatFromValue(this.getColumns().get(key).getColumnProperty().getDefaultValue()));
            ((DGComboBox) field).setValueId(sval);           
            return;*/
        }
        if (domain.getRows().get(key).getFormatVal(key, this) == null) {
            field.clear();
            return;
        }

        String val = String.valueOf(domain.getRows().get(key).getFormatVal(key, this));
        ((DGComboBox) field).setValueId(val);
    }

    //заполняет данные из полей ввода
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid object) {
        IRowColumnVal val = this.createRowColumnVal(key);
        setValueFromField(key, field, val);
        object.getRows().put(key, val);
    }

    //заполняет значения из полей ввода
     @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        if (field.getClass() == DGComboBox.class) {
            val.setValFromFormat(key, ((DGComboBox) field).getValueId(), this);
        }
        //val.setColumnBuilder(this);

    }


    public void setQueryNameForRelation(String t) {
        queryNameForRelation = t;
    }

    public String getQueryNameForRelation() {
        return queryNameForRelation;
    }

    public SKeyForColumn getKeyRelationForId() {
        return keyRelationForId;
    }

    public void setKeyRelationForId(SKeyForColumn key) {
        keyRelationForId = key;
    }

    public SKeyForColumn getKeyRelationForName() {
        return keyRelationForName;
    }

    public void setKeyRelationForName(SKeyForColumn key) {
        keyRelationForName = key;
    }
}
