package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;


/**
 *
 * @author LKHorosheva
 */
public class DColumnBuilderComboBoxAndCheckList extends IColumnBuilder implements Serializable{
    protected String queryNameForDefautValue; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyDefautValue; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    
    protected String queryNameForRelation; //имя таблицы для получения доп. данных
    protected SKeyForColumn keyRelationForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    protected SKeyForColumn keyRelationForName;//ключ к полю, которое хотим помещать в данные    
    //protected String strDefVal;

    public DColumnBuilderComboBoxAndCheckList()
    {
       super();
    }
    
    public void updateDefaultValue(GWTDDataGridServiceAsync service,final CheckBoxListView field) {
        service.getAllDataGrid(queryNameForDefautValue, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(List<DDataGrid> listData) {
                createDefVal(listData);
                setCheckedToField(field);
                 BoxComponentEvent e = new BoxComponentEvent(field);
                field.fireEvent(Events.CheckChange,e);
                isInit = true;
            }
        });
    }

    @Override
    public BoxComponent createFieldForFiltr(final SKeyForColumn key, LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Integer lw) {
        if (!this.getColumns().get(key).getIsFiltr()) {
            return null;
        }

        LayoutContainer lc = new LayoutContainer();
        lc.setBorders(false);
        HBoxLayout layoutHBox = new HBoxLayout();
        lc.setLayout(layoutHBox);
        lc.setStyleAttribute("padding", "0px");
        lc.setStyleAttribute("padding-left", "5px");
        lc.setStyleAttribute("padding", "0px");
        FormData data = new FormData("100%");
        data.setMargins(new Margins(0, 16, 0, 0));

        fieldSet.add(lc, data);

        ContentPanel panel = new ContentPanel();
        panel.setAnimCollapse(false);
        panel.setFrame(true);
        panel.setHeading(this.getColumn(key).getCaption());
        panel.setBodyBorder(false);

        final BoxComponent field = this.getColumns().get(key).getColumnProperty().createFieldForFiltr(Boolean.FALSE); //для полей на панели фильтра разрешаем пустрые значения всегда

        panel.add(field);
        panel.setScrollMode(Style.Scroll.AUTO);
        HBoxLayoutData dataBox = new HBoxLayoutData();
        dataBox.setMargins(new Margins(0, 0, 0, 0));
        dataBox.setFlex(1);
        lc.add(panel, dataBox);

        Button bSaveDataGrid = new Button("Сохранить<br>статус");
        bSaveDataGrid.setHeight(35);
        bSaveDataGrid.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                service.getAllDataGrid(queryNameForDefautValue, new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(List<DDataGrid> listForDel) {

                        //удаляем старый список
                        service.deleteDomains(queryNameForDefautValue, listForDel, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(Boolean res) {
                                //создаем новый список

                                List<BeanModel> model = ((CheckBoxListView) field).getChecked();
                                if (model.size() <= 0) {
                                    MessageBox.info("", "Успешно сохранено!", null);
                                    updateDefaultValue(service, (CheckBoxListView) field);
                                    return;
                                }
                                List<DDataGrid> domains = new ArrayList<DDataGrid>();
                                for (BeanModel el : model) {

                                    if (el.getBean() instanceof DListValString) {
                                        DListValString l = ((DListValString) el.getBean());

                                        DDataGrid data = new DDataGrid();
                                        data.setName(queryNameForDefautValue);

                                        SKeyForColumn keyUserId = new SKeyForColumn("MAIN:USER_ID");
                                        DRowColumnValNumber valUserId = new DRowColumnValNumber();
                                        valUserId.setVal(ConfigurationManager.getUserId().intValue());
                                        data.getRows().put(keyUserId, valUserId);

                                        DRowColumnValNumber val = new DRowColumnValNumber();
                                        val.setVal(Integer.parseInt(l.getKey().trim()));
                                        data.getRows().put(keyDefautValue, val);
                                        domains.add(data);
                                    }
                                }
                                //сохраняем
                                service.insert(queryNameForDefautValue, domains, new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        AppHelper.showMsgRpcServiceError(caught);
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        MessageBox.info("", "Успешно сохранено!", null);
                                        updateDefaultValue(service, (CheckBoxListView) field);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        HBoxLayoutData dataBtn = new HBoxLayoutData();
        dataBtn.setMargins(new Margins(0, 16, 0, 5));
        dataBtn.setFlex(0);
        lc.add(bSaveDataGrid, dataBtn);

        return field;
    }

    
    
    //заполняет поля ввода переданными данными
    @Override
    public void setValueToField(SKeyForColumn key, Field field, IRowColumnVal val) {
    }

    //заполняет соответсвующее поля переданным значением
    @Override
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
    }

    //заполняет данные из полей ввода
    public void setDomainValueFromField(SKeyForColumn key, Field field, DDataGrid object) {
        /* IRowColumnVal val = this.createRowColumnVal(key);
         setValueFromField(key, field, val);
         object.getRows().put(key, val);*/
    }

    //заполняет значения из полей ввода
    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
       /* if (field.getClass() == SimpleComboBox.class) {
            val.setValFromFormat(key, ((SimpleComboBox) field).getRawValue(), this);
        }*/
    }
    
    //заполняет поля ввода переданными данными
    public void setValueToCheckBoxListView(SKeyForColumn key, CheckBoxListView field, IRowColumnVal val) {
        String defVal = (String) val.getVal();

        if (defVal == null) {
            return;
        }
        if (defVal.isEmpty()) {
            return;
        }

        String[] paramsDefVal = defVal.split(",");
        if (paramsDefVal.length <= 0) {
            return;
        }
        field.getChecked().clear();
        List<BeanModel> model = field.getStore().getModels();       
        for (BeanModel el : model) {
            if (el.getBean() instanceof DListValString) {
                DListValString l = ((DListValString) el.getBean());
                for (int i = 0; i < paramsDefVal.length; i++) {
                      String elList = l.getKey().trim();
                      String elDef = paramsDefVal[i].trim();
                    if (elList.equals(elDef)) {
                        field.setChecked(el, true);
                    }
                    
                }
            }
        }
    }
    
    public void setCheckedToField(CheckBoxListView field) {
        String defVal="";
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            IColumnProperty property = this.getColumns().get(key).getColumnProperty();
            defVal = (String) property.getDefaultValue();
        }

        if (defVal == null) {
            return;
        }
        if (defVal.isEmpty()) {
            return;
        }

        String[] paramsDefVal = defVal.split(",");
        if (paramsDefVal.length <= 0) {
            return;
        }
        field.getChecked().clear();
        List<BeanModel> model = field.getStore().getModels();
        for (BeanModel el : model) {
            if (el.getBean() instanceof DListValString) {
                DListValString l = ((DListValString) el.getBean());
                for (int i = 0; i < paramsDefVal.length; i++) {
                    String elList = l.getKey().trim();
                    String elDef = paramsDefVal[i].trim();
                    if (elList.equals(elDef)) {
                        field.setChecked(el, true);
                    } 
                }
            }
        }
    }

     @Override
    public void initData(GWTDDataGridService service) {
        try {
            List<DDataGrid> listData = service.getAllDataGrid(queryNameForRelation);
            createList(listData);
            List<DDataGrid> listDefData = service.getAllDataGrid(queryNameForDefautValue);
            createDefVal(listDefData);                
            isInit = true;
        } catch (Exception ex) {
            isInit = false;
        }
    }
    
    //создаем список возможных значений из переданных(listData) данных для комбо бокса
    protected void createDefVal(List<DDataGrid> listData) {
       
        String strVal =  new String();
        for (DDataGrid domain : listData) {
            HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
            if (rows.containsKey(keyDefautValue) ) {
                strVal += (!strVal.isEmpty() ? "," : "") + rows.get(keyDefautValue).getVal() ;
            }
        }        
        
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();          
            IColumnProperty property = this.getColumns().get(key).getColumnProperty();
            property.setDefaultValueForFiltr(strVal);
        }
      
    }
    
    public void  setValueFromCheckBoxListView(SKeyForColumn key, CheckBoxListView field, IRowColumnVal val) {        

        String str = new String();
        List<BeanModel> model = field.getChecked();
        for (BeanModel el : model) {
            if (el.getBean() instanceof DListValString) {
                DListValString l = ((DListValString) el.getBean());
                str += (!str.isEmpty() ? "," : "") + l.getKey();
            }
        }
        val.setVal(str);
         
    }
    
    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
      //return " ";
        if (val == null) {
            return " ";
        }
        if (val.isEmpty()) {
            return " ";
        }
       
        String[] params = val.split(",");
        if (params.length <= 0) {
            return " ";
        }
        String sqlForCol = " (";
        for (int i = 0; i < params.length; i++) {
            sqlForCol += ( i>0 ? " OR " :" ") + key.getTableAlias() + "." + this.getColumn(key).getName() + "='" + params[i] + "'";
        }
        sqlForCol += " ) ";
        return sqlForCol;

    }
    
     public void setQueryNameForDefautValue(String t) {
        queryNameForDefautValue = t;
    }

    public String getQueryNameForDefautValue() {
        return queryNameForDefautValue;
    }

    public SKeyForColumn getKeyDefautValue() {
        return keyDefautValue;
    }

    public void setKeyDefautValue(SKeyForColumn key) {
        keyDefautValue = key;
    }

    @Override
    public HashMap<SKeyForColumn, Field> createFields(LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Boolean isEnabled, Integer lw) {
        HashMap<SKeyForColumn, Field> fields = new HashMap<SKeyForColumn, Field>();

        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();          
            IColumnProperty property = this.getColumns().get(key).getColumnProperty();

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
            fieldSet.add(field, data);
            fields.put(key, field);
        }
        return fields;     
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
    
    //создаем список возможных значений из переданных(listData) данных для комбо бокса
    protected void createList(List<DDataGrid> listData) {
        ArrayList<SListVal<String, String>> list = new ArrayList<SListVal<String, String>>();
        //заполняем массив данными
        for (DDataGrid domain : listData) {
            HashMap<SKeyForColumn, IRowColumnVal> rows = domain.getRows();
            if (rows.containsKey(keyRelationForId) && rows.containsKey(keyRelationForName)) {
                list.add(new SListVal(rows.get(keyRelationForId).getVal().toString(), rows.get(keyRelationForName).getVal()));
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

    
}
