package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridMessages;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Igor
 * Builds combobox field for filter panel without refresh button
 * The instance of this class must always contain only one column i.e. array 
 * "HashMap<SKeyForColumn, DColumn> columns" should contain only one element and this element represents the column
 * for the drop-down list with data from database
 */
public class DColumnBuilderComboBoxDBNoRefresh extends IColumnBuilder implements Serializable {

    protected String queryNameForRelation; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyRelationForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyRelationForName;//ключ к полю, которое хотим помещать в данные 
    
     
    @Override
    public LayoutContainer createLayoutContainer(Integer lw) {
        LayoutContainer fieldSet = new LayoutContainer();
        fieldSet.setBorders(false);

        HBoxLayout layoutHBox = new HBoxLayout();
        fieldSet.setLayout(layoutHBox);
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
    }
    
    @Override
    //построитель для полей ввода на панели фильтра
    public BoxComponent createFieldForFiltr(SKeyForColumn key, LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Integer lw) {
        if (!this.getColumns().get(key).getIsFiltr()) {
            return null;
        }
        LayoutContainer lc = createLayoutContainer(lw);
        lc.setStyleAttribute("padding", "0px");
        //lc.setWidth(200);
        FormData data = new FormData("100%");
//        data.setMargins(new Margins(0, 16, 0, 0));
        fieldSet.add(lc,data);
        return createField( key, lc, service, Boolean.FALSE, Boolean.TRUE,lw,Boolean.TRUE );
    }
    
    
    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
        final HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            final SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            Field field = createField(key, fieldSet, service, Boolean.TRUE, isEnabled,lw, Boolean.FALSE);
            fields.put(key, field);

        }
        return fields;
    }
    
   private Field createField(final SKeyForColumn key, LayoutContainer fieldSet,final GWTDDataGridServiceAsync service,Boolean isNotForFiltr, Boolean isEnabled, Integer lw, Boolean isForFiltr)
   {
       Boolean isNotNull = false;
       if (isNotForFiltr) {
           isNotNull = this.getIsNotNull(key);
       }
       final Field field = this.getColumns().get(key).getColumnProperty().createField((isNotForFiltr) ? isNotNull : Boolean.FALSE);

       if (isNotNull) {
           FormHelper.setNotEmptyFieldLabel(this.getColumn(key).getCaption(), field);
       } else {
           field.setFieldLabel(this.getColumn(key).getCaption());
       }
       field.setVisible( (isForFiltr) ? this.getColumn(key).getIsFiltr() : this.getColumn(key).getIsVisibleEdit());
       Boolean isEnbld = (isNotForFiltr) ? this.getColumn(key).getIsEditable() : Boolean.TRUE;
       FormHelper.setReadOnlyProp(field,!isEnbld);         
       //field.setEnabled((isNotForFiltr) ? this.getColumn(key).getIsEditable() : Boolean.TRUE);
       
       //field.setAutoValidate(true);
       LayoutContainer fieldSetBox = new LayoutContainer();
       fieldSetBox.setBorders(false);       
       //fieldSetBox.setLayout(new FormLayout());
       FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
       layout.setLabelWidth(lw);
       fieldSetBox.setLayout(layout);
       
       FormData data = new FormData("100%");
//       data.setMargins(new Margins(0, 16, 0, 0));
       fieldSetBox.add(field, data);

       HBoxLayoutData dataBox = new HBoxLayoutData();
       dataBox.setMargins(new Margins(0, 0, 0, 0));
       dataBox.setFlex(1);
       fieldSet.add(fieldSetBox, dataBox);

       SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

           @Override
           public void componentSelected(ButtonEvent be) {
               String id = be.getButton().getItemId();
               if (id.equals("refresh")) {
                   updateDataAndField(key, field, service);
               } else if (id.equals("clear")) {
                   field.clear();
                   field.validate();
                }

            }
        };

        DataGridImages images = GWT.create(DataGridImages.class);

//        Button bRefreshDataGrid = new Button();
//        bRefreshDataGrid.setIcon(AbstractImagePrototype.create(images.reset()));
//        bRefreshDataGrid.addSelectionListener(listener);
//        bRefreshDataGrid.setItemId("refresh");
//        bRefreshDataGrid.setEnabled(isEnabled);
        
        HBoxLayoutData dataBtn = new HBoxLayoutData();
        dataBtn.setMargins(new Margins(0, 0, 0, 5));
        dataBtn.setFlex(0);
        HBoxLayoutData dataBtn32 = new HBoxLayoutData();
        dataBtn32.setMargins(new Margins(0, 32, 0, 5));
        dataBtn32.setFlex(0);
        
        if (isNotForFiltr) {
            Button bClear = new Button();
            bClear.setIcon(AbstractImagePrototype.create(images.cancel()));
            bClear.addSelectionListener(listener);
            bClear.setItemId("clear");
            bClear.setEnabled(isEnabled);

            if (!this.getColumn(key).getIsEditable() || !this.getColumn(key).getIsEditableAdd()) {
//                bRefreshDataGrid.setEnabled(false);
                bClear.setEnabled(false);
            }

//            fieldSet.add(bRefreshDataGrid, dataBtn);
            fieldSet.add(bClear, dataBtn32);
        }
        else
//            fieldSet.add(bRefreshDataGrid, dataBtn);
                
        fieldSet.setVisible((isForFiltr) ? this.getColumn(key).getIsFiltr() : this.getColumn(key).getIsVisibleEdit());
        return field;
    }

    //заполняет соответсвующее поля переданным значением
    @Override
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
        if (field.getClass() != SimpleComboBox.class) {
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
            ((SimpleComboBox) field).setSimpleValue(sval);           
            return;*/
        }
        
        if (domain.getRows().get(key).getFormatVal(key, this) == null) {
            field.clear();
            return;
        }
        ((SimpleComboBox) field).setSimpleValue(String.valueOf(domain.getRows().get(key).getFormatVal(key, this)));
    }
    
    
    //заполняет поля ввода переданными данными
    @Override
    public  void setValueToField( SKeyForColumn key, Field field, IRowColumnVal val )
    {
        if (field.getClass() != SimpleComboBox.class)
        {
            field.clear();
            return;
        }        
        if (val.getFormatVal(key, this) == null) {
            field.clear();
            return;
        }        
        String sval = String.valueOf(val.getFormatVal(key, this));        
        ((SimpleComboBox) field).setSimpleValue(sval);
    }

    //заполняет данные из полей ввода
    @Override
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid object) {
        IRowColumnVal val = this.createRowColumnVal(key);
        setValueFromField(key, field, val);
        object.getRows().put(key, val);
    }

    //заполняет соответсвующее поля переданным значением
    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        if (field.getClass() == SimpleComboBox.class) {
            val.setValFromFormat(key, ((SimpleComboBox) field).getRawValue(), this);
        }
    }

    //обновляем поля ввода (fields)
    public void updateField(SKeyForColumn key, Field field) {
        try {
            ((IColumnPropertyComboBox) this.getColumns().get(key).getColumnProperty()).updateData(field);
        } catch (Exception e) {
        }
    }

    public void updateDataAndField(final SKeyForColumn key, final Field field, GWTDDataGridServiceAsync service) {
        isInit = false;
        AsyncCallback<List<DDataGrid>> callback = new AsyncCallback<List<DDataGrid>>() {

            @Override
            public void onFailure(Throwable caught) {
                CommonConstants commonConstants = GWT.create(CommonConstants.class);
                DataGridMessages messages = GWT.create(DataGridMessages.class);
                MessageBox.alert(commonConstants.error(), messages.errorUpdate(), null).show();
            }

            @Override
            public void onSuccess(List<DDataGrid> listData) {
                createList(listData);
                updateField(key, field);
                isInit = true;
            }
        };
        service.getAllDataGrid(queryNameForRelation, callback);
    }

    @Override
    public void initData(GWTDDataGridService service) {
        try {
            List<DDataGrid> listData = service.getAllDataGrid(queryNameForRelation);
            createList(listData);
            isInit = true;
        } catch (Exception ex) {
            isInit = false;
        }
    }

    //создаем список возможных значений из переданных(listData) данных для комбо бокса
    protected void createList(List<DDataGrid> listData) {
        ArrayList<SListVal<Integer, String>> list = new ArrayList<SListVal<Integer, String>>();
        //заполняем массив данными
        for (DDataGrid domain : listData) {
            HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
            if (rows.containsKey(keyRelationForId) && rows.containsKey(keyRelationForName)) {
                list.add(new SListVal(rows.get(keyRelationForId).getVal(), rows.get(keyRelationForName).getVal()));
            }
        }
        //передаем этот список полю описывающему свойства колонки
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            IColumnProperty property = this.getColumns().get(key).getColumnProperty();
            try {
                ((IColumnPropertyComboBox) property).initList(list);
            } catch (Exception e) {
            }
        }
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