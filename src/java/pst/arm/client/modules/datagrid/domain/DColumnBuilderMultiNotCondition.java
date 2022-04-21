package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderMultiNotCondition extends IColumnBuilder implements Serializable {

    protected String queryNameForRelation; //имя табдицы для получения доп. данных
    protected HashMap< SKeyForColumn, SKeyForColumn> relations;  //связь полей ключ по основной таблице
    protected Boolean isViewOnly = false; //открываем таблицу для выбора в режиме только на чтение


    protected String caption;

    public void setCaption(String t) {
        caption = t;
    }

    public String getCaption() {
        return caption;
    }
    
    public void setIsViewOnly(Boolean isViewOnly) {
        this.isViewOnly = isViewOnly;
    }

    public Boolean getIsViewOnly() {
        return isViewOnly;
    }

    public void setQueryNameForRelation(String t) {
        queryNameForRelation = t;
    }

    public String getQueryNameForRelation() {
        return queryNameForRelation;
    }

    public void setRelations(HashMap< SKeyForColumn, SKeyForColumn> r) {
        relations = r;
    }

    public HashMap< SKeyForColumn, SKeyForColumn> getRelations() {
        return relations;
    }

    @Override
    public LayoutContainer createLayoutContainer(Integer lw) {
        Integer countVisibleField = 0;
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            if (this.getColumn(key).getIsVisibleEdit()) {
                countVisibleField++;
            }
        }
        //проверяем есть ли хоть одно видимое поле
        Boolean isVisible = false;         
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
           isVisible = ( isVisible || this.getColumn(key).getIsVisibleEdit() );        
        }
        if (countVisibleField > 1) //строим рамочку
        {
            FieldSet fieldSet = new FieldSet();
            fieldSet.setBorders(true);
            fieldSet.setHeading(caption);
            //fieldSet.setLayout(new FormLayout());
            FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
            layout.setLabelWidth(lw);
            fieldSet.setLayout(layout);
            fieldSet.setStyleAttribute("padding", "5px");
            fieldSet.setVisible(isVisible);
            return fieldSet;
        } else {
            LayoutContainer fieldSet = new LayoutContainer();
            fieldSet.setBorders(false);
            FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
            layout.setLabelWidth(lw);
            fieldSet.setLayout(layout);
            fieldSet.setStyleAttribute("padding", "0px");
            fieldSet.setStyleAttribute("padding-left", "5px");
            fieldSet.setVisible(isVisible);
            return fieldSet;
        }
    }
    
    protected void setValToFields(HashMap<SKeyForColumn, Field> fields, HashMap<SKeyForColumn, IRowColumnVal> rows, DTable table) {
        for (Map.Entry colEntry : relations.entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            SKeyForColumn key_r = (SKeyForColumn) colEntry.getValue();
            fields.get(key).setValue(rows.get(key_r).getFormatVal(key_r, table.getColumnBuilder(key_r)));
        }
    }
    
    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled,Integer lw ) {

        DataGridConstants constants = GWT.create(DataGridConstants.class);
        DataGridImages images = GWT.create(DataGridImages.class);
        final DataGridWindow page;
            page = new DataGridWindow(queryNameForRelation, isViewOnly, true);
        /*page.setIsForSend(true);
        if ( isViewOnly ) {
            page.getGridPanel().setMode(EditMode.VIEWONLY);
            page.getGridPanel().enabledBtn();
        }*/
        final HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();

        SelectionListener<ButtonEvent> buttonEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                String id = be.getButton().getItemId();
                if (id.equals("open")) {
                    Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {
                        @Override
                        public void handleEvent(DataGridEvent be) {
                            page.hide();
                            HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                            DTable table = be.getTable();
                            setValToFields(fields,rows, table);                            
                        }
                    };
                    page.addDataGridListener(listenerGetDomain);
                    page.show();
                }
                if (id.equals("clear")) {
                    for (Map.Entry colEntry : relations.entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        SKeyForColumn key_r = (SKeyForColumn) colEntry.getValue();
                        fields.get(key).clear();
                        fields.get(key).validate();                        
                    }
                }
            }
        };

        LayoutContainer bottom = new LayoutContainer();
        HBoxLayout layoutBottom = new HBoxLayout();
        layoutBottom.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layoutBottom.setPack(BoxLayoutPack.END);
        bottom.setLayout(layoutBottom);

        //Button bOpenDataGrid = new Button(constants.btnOpenDataGrid(), AbstractImagePrototype.create(images.book()) ,buttonEventListener);
        Button bOpenDataGrid = new Button("", AbstractImagePrototype.create(images.book()), buttonEventListener);
        bOpenDataGrid.setToolTip(constants.btnOpenDataGrid());
        bOpenDataGrid.setSize(22, 22);
        bOpenDataGrid.setItemId("open");
        bOpenDataGrid.setEnabled(isEnabled);

        // Button bClearData = new Button(constants.btnClearDataGrid(), AbstractImagePrototype.create(images.cancel()) ,buttonEventListener);
        Button bClearData = new Button("", AbstractImagePrototype.create(images.cancel()), buttonEventListener);
        bClearData.setToolTip(constants.btnClearDataGrid());
        bClearData.setSize(22, 22);
        bClearData.setItemId("clear");
        bClearData.setEnabled(isEnabled);
         
        /*HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 0, 5, 5));
        bottom.add(bOpenDataGrid, layoutData);
        bottom.add(bClearData, layoutData);

        FormData dataBottom = new FormData("100%");
        dataBottom.setMargins(new Margins(0, 32, 0, 0));

        fieldSet.add(bottom, dataBottom);        
        */
        boolean isAddBtn = false;              
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
            //field.setAutoValidate(true);
            FormHelper.setReadOnlyProp(field,true);
            //field.setEnabled(false);
            if (isAddBtn || (!isAddBtn && !this.getColumn(key).getIsVisibleEdit())) {
                FormData data = new FormData("100%");
                data.setMargins(new Margins(0, 32, 0, 0));
                fieldSet.add(field, data);
            } else if (this.getColumn(key).getIsVisibleEdit()) {
                isAddBtn = true;

                LayoutContainer fieldSetRow = new LayoutContainer();
                fieldSetRow.setBorders(Boolean.FALSE);
                fieldSetRow.setLayout(new HBoxLayout());
                fieldSetRow.setStyleAttribute("padding", "0px");
               
                FormData data = new FormData("100%");
                fieldSet.add(fieldSetRow, data);

                LayoutContainer fieldSetBox = new LayoutContainer();
                fieldSetBox.setBorders(false);
                //fieldSetBox.setLayout(new FormLayout());
                FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
                layout.setLabelWidth(lw);
                fieldSetBox.setLayout(layout);
                
                fieldSetBox.add(field, data);

                HBoxLayoutData dataBox = new HBoxLayoutData();
                dataBox.setMargins(new Margins(0, 0, 0, 0));
                dataBox.setFlex(1);
                fieldSetRow.add(fieldSetBox, dataBox);

                HBoxLayoutData dataBtn = new HBoxLayoutData();
                dataBtn.setMargins(new Margins(0, 0, 0, 21));
                dataBtn.setFlex(0);
                
                HBoxLayoutData dataBtn32 = new HBoxLayoutData();
                dataBtn32.setMargins(new Margins(0, 32, 0, 5));
                dataBtn32.setFlex(0);

                if (!this.getColumn(key).getIsEditable() || !this.getColumn(key).getIsEditableAdd()) {
                    bOpenDataGrid.setEnabled(false);
                    bClearData.setEnabled(false);
                }

                fieldSetRow.add(bOpenDataGrid, dataBtn);
                fieldSetRow.add(bClearData, dataBtn32);


            }
            fields.put(key, field);
        }
        return fields;
    }
    
    @Override
    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted) {
        FormHelper.setReadOnlyProp(field, true);
        //field.setEnabled(false);
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
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        val.setValFromFormat(key, field.getValue(), this);

    }
 
}