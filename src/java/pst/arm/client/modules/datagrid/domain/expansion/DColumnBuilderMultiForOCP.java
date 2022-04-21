package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.lang.DataGridMessages;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wesStyle
 */
public class DColumnBuilderMultiForOCP extends DColumnBuilderMulti{

    protected String queryNameForRelation; //имя табдицы для получения доп. данных
    protected HashMap< SKeyForColumn, SKeyForColumn> relations;  //связь полей ключ по основной таблице
    protected Boolean isViewOnly = false; //открываем таблицу для выбора в режиме только на чтение

    protected String queryNameForRelationCo; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyRelationForIdCo; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyRelationForNameCo;//ключ к полю, которое хотим помещать в данные
    protected String queryNameForRelationFS; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyRelationForIdFS; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyRelationForNameFS;//ключ к полю, которое хотим помещать в данные

    private ArrayList<Button> btns = new ArrayList<Button>();
    private ArrayList<Field> flds = new ArrayList<Field>();

    public String getQueryNameForRelationFS() {
        return queryNameForRelationFS;
    }

    public void setQueryNameForRelationFS(String queryNameForRelationFS) {
        this.queryNameForRelationFS = queryNameForRelationFS;
    }

    public SKeyForColumn getKeyRelationForIdFS() {
        return keyRelationForIdFS;
    }

    public void setKeyRelationForIdFS(SKeyForColumn keyRelationForIdFS) {
        this.keyRelationForIdFS = keyRelationForIdFS;
    }

    public SKeyForColumn getKeyRelationForNameFS() {
        return keyRelationForNameFS;
    }

    public void setKeyRelationForNameFS(SKeyForColumn keyRelationForNameFS) {
        this.keyRelationForNameFS = keyRelationForNameFS;
    }

    public String getQueryNameForRelationCo() {
        return queryNameForRelationCo;
    }

    public void setQueryNameForRelationCo(String queryNameForRelationCo) {
        this.queryNameForRelationCo = queryNameForRelationCo;
    }

    public SKeyForColumn getKeyRelationForIdCo() {
        return keyRelationForIdCo;
    }

    public void setKeyRelationForIdCo(SKeyForColumn keyRelationForIdCo) {
        this.keyRelationForIdCo = keyRelationForIdCo;
    }

    public SKeyForColumn getKeyRelationForNameCo() {
        return keyRelationForNameCo;
    }

    public void setKeyRelationForNameCo(SKeyForColumn keyRelationForNameCo) {
        this.keyRelationForNameCo = keyRelationForNameCo;
    }


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
        FieldSet fieldSet = new FieldSet();
        fieldSet.setBorders(true);
        fieldSet.setHeading(caption);
        //fieldSet.setLayout(new FormLayout());
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);
        fieldSet.setLayout(layout);
        fieldSet.setStyleAttribute("padding", "5px");
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
    public void initData(GWTDDataGridService service) {
        try {
            List<DDataGrid> listData = service.getAllDataGrid(queryNameForRelationCo);
            createList(listData, new SKeyForColumn("MAIN:COMPANY_ID"));

            listData = service.getAllDataGrid(queryNameForRelationFS);
            createList(listData, new SKeyForColumn("MAIN:FUNDING_SOURCE_ID"));

            isInit = true;
        } catch (Exception ex) {
            isInit = false;
        }
    }
    
    protected void setValToFields(HashMap<SKeyForColumn, Field> fields, HashMap<SKeyForColumn, IRowColumnVal> rows, DTable table) {
        for (Map.Entry colEntry : relations.entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            SKeyForColumn key_r = (SKeyForColumn) colEntry.getValue();
            if (fields.get(key).getClass() == SimpleComboBox.class)
                ((SimpleComboBox)fields.get(key)).setSimpleValue(String.valueOf(rows.get(key_r).getFormatVal(key_r, table.getColumnBuilder(key_r))));
            else
                fields.get(key).setValue(rows.get(key_r).getFormatVal(key_r, table.getColumnBuilder(key_r)));
        }
    }
    
    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {

        DataGridConstants constants = GWT.create(DataGridConstants.class);
        DataGridImages images = GWT.create(DataGridImages.class);

        final DataGridWindow page = new DataGridWindow(queryNameForRelation, isViewOnly, true);
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

                        FieldEvent e = new FieldEvent(fields.get(key));
                        e.setValue("");
                        fields.get(key).fireEvent(Events.Change, e);
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

        boolean isAddBtn = false;
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            final SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            Boolean isNotNull = this.getIsNotNull(key);
            final Field field = this.getColumns().get(key).getColumnProperty().createField(isNotNull);


            if (isNotNull) {
                FormHelper.setNotEmptyFieldLabel(this.getColumn(key).getCaption(), field);
            } else {
                field.setFieldLabel(this.getColumn(key).getCaption());
            }
            field.setVisible(this.getColumn(key).getIsVisibleEdit());

            if (key.equals(new SKeyForColumn("MAIN:ORDER_ID"))) {
                FormHelper.setReadOnlyProp(field, true);
                //field.setEnabled(false);
                field.setFireChangeEventOnSetValue(true);
                field.addListener(Events.Change, new Listener<FieldEvent>() {
                    @Override
                    public void handleEvent(FieldEvent fe) {
                        if (fe.getValue().toString() == "" || fe.getValue() == null) {
                            for (Field f : flds) {
                                FormHelper.setReadOnlyProp(f, false);
                                //f.setEnabled(true);
                            }
                            for (Button b : btns) {
                                b.setEnabled(true);
                            }
                        } else {
                            for (Field f : flds) {
                                FormHelper.setReadOnlyProp(f, true);
                                //f.setEnabled(false);
                            }
                            for (Button b : btns) {
                                b.setEnabled(false);
                            }
                        }
                    }
                });
            } else if (key.equals(new SKeyForColumn("MAIN:COMPANY_ID")) || key.equals(new SKeyForColumn("MAIN:FUNDING_SOURCE_ID"))
                    || key.equals(new SKeyForColumn("MAIN:WORK_NAME")) || key.equals(new SKeyForColumn("CONTRACT:CONTRACT_NUMB"))) {
                flds.add(field);
                FormHelper.setReadOnlyProp(field, false);
                //field.setEnabled(true);
            } else {
                FormHelper.setReadOnlyProp(field, true);
                //field.setEnabled(false);            
            }

            if (!(key.equals(new SKeyForColumn("MAIN:COMPANY_ID")) || key.equals(new SKeyForColumn("MAIN:FUNDING_SOURCE_ID"))) && (
                    isAddBtn || (!isAddBtn && !this.getColumn(key).getIsVisibleEdit()))) {
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

                if (key.equals(new SKeyForColumn("MAIN:COMPANY_ID")) || key.equals(new SKeyForColumn("MAIN:FUNDING_SOURCE_ID"))) {
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

                    Button bRefreshDataGrid = new Button();
                    bRefreshDataGrid.setIcon(AbstractImagePrototype.create(images.reset()));
                    bRefreshDataGrid.addSelectionListener(listener);
                    bRefreshDataGrid.setItemId("refresh");
                    bRefreshDataGrid.setEnabled(isEnabled);

                    Button bClear = new Button();
                    bClear.setIcon(AbstractImagePrototype.create(images.cancel()));
                    bClear.addSelectionListener(listener);
                    bClear.setItemId("clear");
                    bClear.setEnabled(isEnabled);

                    btns.add(bClear);
                    btns.add(bRefreshDataGrid);

                    if (!this.getColumn(key).getIsEditable() || !this.getColumn(key).getIsEditableAdd()) {
                        bRefreshDataGrid.setEnabled(false);
                        bClear.setEnabled(false);
                    }
                    fieldSetRow.add(bRefreshDataGrid, dataBtn);
                    fieldSetRow.add(bClear, dataBtn32);
                } else {
                    if (!this.getColumn(key).getIsEditable() || !this.getColumn(key).getIsEditableAdd()) {
                        bOpenDataGrid.setEnabled(false);
                        bClearData.setEnabled(false);
                    }
                    fieldSetRow.add(bOpenDataGrid, dataBtn);
                    fieldSetRow.add(bClearData, dataBtn32);
                }

            }
            fields.put(key, field);
        }

        return fields;
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
                createList(listData, key);
                updateField(key, field);
                isInit = true;
            }
        };
        if (key.equals(new SKeyForColumn("MAIN:COMPANY_ID")))
            service.getAllDataGrid(queryNameForRelationCo, callback);
        else
            service.getAllDataGrid(queryNameForRelationFS, callback);
    }

    public void updateField(SKeyForColumn key, Field field) {
        try {
            ((IColumnPropertyComboBox) this.getColumns().get(key).getColumnProperty()).updateData(field);
        } catch (Exception e) {
        }
    }

    protected void createList(List<DDataGrid> listData, SKeyForColumn cKey) {
        ArrayList<SListVal> list = new ArrayList<SListVal>();
        //заполняем массив данными
        if (cKey.equals(new SKeyForColumn("MAIN:COMPANY_ID"))) {
            for (DDataGrid domain : listData) {
                HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                if (rows.containsKey(keyRelationForIdCo) && rows.containsKey(keyRelationForNameCo)) {
                    list.add(new SListVal(rows.get(keyRelationForIdCo).getVal(), rows.get(keyRelationForNameCo).getVal()));
                }
            }
        } else {
            for (DDataGrid domain : listData) {
                HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
                if (rows.containsKey(keyRelationForIdFS) && rows.containsKey(keyRelationForNameFS)) {
                    list.add(new SListVal(rows.get(keyRelationForIdFS).getVal(), rows.get(keyRelationForNameFS).getVal()));
                }
            }
        }

        //передаем этот список полю описывающему свойства колонки
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            if (key.equals(cKey)) {
                IColumnProperty property = this.getColumns().get(key).getColumnProperty();
                try {
                    ((IColumnPropertyComboBox) property).initList(list);
                } catch (Exception e) {
                }
            }
        }
    }


    @Override
    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted) {
        if (key.equals(new SKeyForColumn("MAIN:COMPANY_ID")) || key.equals(new SKeyForColumn("MAIN:FUNDING_SOURCE_ID"))
                || key.equals(new SKeyForColumn("MAIN:WORK_NAME")) || key.equals(new SKeyForColumn("CONTRACT:CONTRACT_NUMB")))
        {
            FormHelper.setReadOnlyProp(field, false);
            //field.setEnabled(true);
        } else {
            FormHelper.setReadOnlyProp(field,true);
            //field.setEnabled(false);
        }
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
        if (field.getClass() == SimpleComboBox.class) {
            ((SimpleComboBox) field).setSimpleValue(String.valueOf(domain.getRows().get(key).getFormatVal(key, this)));
        } else
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
         if (field.getClass() == SimpleComboBox.class) {
             val.setValFromFormat(key, field.getRawValue(), this);
         } else
            val.setValFromFormat(key, field.getValue(), this);
    }
}