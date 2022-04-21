package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.modules.admin.lang.AdminConstants;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderComboBoxForNoWorkingDays;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderComboBoxForOfficeDocFileFilter;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderComboBoxForWorkingDays;
import pst.arm.client.modules.datagrid.domain.expansion.STwoKeys;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.lang.DataGridMessages;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridFiltr extends FormPanel {

    private Map<SKeyForColumn, BoxComponent> fields;
    protected AdminConstants constants;
    private DataBasePanel resPanel;
    private LayoutContainer main, mainVerticval;
    private LayoutContainer bottom;
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    final protected DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    private Integer constFieldCount = 2; //кол-во полей поиска в колонке
    private static CommonConstants commonConstants = (CommonConstants) GWT.create(CommonConstants.class);
    private static DataGridMessages datagridMessages = GWT.create(DataGridMessages.class);

    public int getEnoughHeight() {
        mainVerticval.layout(true);
        int min = Integer.valueOf(datagridConstants.filtrpanelMinHeight());
        int top = mainVerticval.getHeight() + 5;// getAbsoluteTop();
        if (min != resPanel.getTable().getFiltrHeight()) {
            min = resPanel.getTable().getFiltrHeight();
        }
        return top > min ? top : min;//Integer.valueOf(datagridConstants.filtrpanelMinHeight());//height;
    }

    public DataGridFiltr(DataBasePanel resPanel) {
        this.resPanel = resPanel;
        // Установка заголовка
        constants = (AdminConstants) GWT.create(AdminConstants.class);

        fields = new HashMap<SKeyForColumn, BoxComponent>();
        initView();
    }

    public void repaintAllComponents() {
        initComponents();
    }

    private void initComponents() {
        if (resPanel.getTable() == null) {
            return;
        }
        constFieldCount = resPanel.getTable().getCountRowsInFiltrColumn();
        int countField = 1;
        LayoutContainer column = new LayoutContainer();
        column.setLayout(new FormLayout());
        column.setStyleAttribute("padding", "0px");
        column.setWidth(resPanel.getTable().getFilterColumnWidth());
        //column.setWidth(300);
        for (final IColumnBuilder builder : resPanel.getTable().getColumnBuilders()) {
            HashMap<SKeyForColumn, DColumn> cols = builder.getColumns();
            
            for (Map.Entry col : cols.entrySet()) {
                if ((constFieldCount == 1 && countField > 1) || (constFieldCount != 1 && (countField % constFieldCount == 1))  || builder.getIsNewColumn()) //переходим к следующей колонке
                {
                    if (column.getItemCount() > 0) {
                        main.add(column, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.TOP));
                    }
                    column = new LayoutContainer();
                    column.setLayout(new FormLayout());
                    column.setStyleAttribute("padding", "0px");
                    column.setWidth(resPanel.getTable().getFilterColumnWidth());
                    //column.setWidth(300);
                    //main.add(column, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.TOP));
                }

                SKeyForColumn key = (SKeyForColumn) col.getKey();
                BoxComponent field = builder.createFieldForFiltr(key, column, service, resPanel.getTable().getLabelFiltrWidth());
                
                //для особых типов полей (напр. для чеклиска выбора статуса) есть необходимость сразу после этого поля перейти на следующую колонку
                //для этого создаем видимость того, что в текущей колонке уже все поля созданы
                if (builder instanceof DColumnBuilderComboBoxAndCheckList) {
                    countField = countField+(constFieldCount-(countField % constFieldCount));
                }
                // Создаем видимость того, что это первое поле в колонке
                if (builder.getIsNewColumn()) {
                    countField = countField+(constFieldCount-(countField % constFieldCount)+1);
                }
                
                if (field != null) {
                /*hlv if (field instanceof CheckBoxListView) {
                    if (column.getWidget(countField - 1) instanceof ContentPanel);
                    {
                        ((ContentPanel) column.getWidget(countField - 1)).collapse();
                        ((ContentPanel) column.getWidget(countField - 1)).addListener(Events.Collapse, new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                resPanel.resizePanelFiltr();
                            }
                        });
                        ((ContentPanel) column.getWidget(countField - 1)).addListener(Events.Expand, new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                resPanel.resizePanelFiltr();
                            }
                        });
                    }
                }*/
//                if ((builder instanceof DColumnBuilderComboBoxForWorkingDays)
//                        || (builder instanceof DColumnBuilderComboBoxForNoWorkingDays)) {
//                    CheckBox cb = (CheckBox) field;
//                    IRowColumnVal val = resPanel.getTable().createRowColumnVal(key);
//                    cb.setValue(true);
//                    if (cb.getValue()) {
//                        val.setVal(1);
//                    } else {
//                        val.setVal(0);
//                    }
//                }

                
                    if (field instanceof Field) {
                        ((Field) field).addKeyListener(new KeyListener() {
                            @Override
                            public void componentKeyUp(ComponentEvent event) {
                                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                                    search();
                                }
                            }
                        });
                    
                    }
                    if (field instanceof CheckBoxListView) {
                        ((CheckBoxListView) field).addListener(Events.CheckChange, new Listener<BoxComponentEvent>() {
                            @Override
                            public void handleEvent(BoxComponentEvent fe) {
                               search();
                            }
                        });
                    }                   
                    fields.put(key, field);
                    countField++;
                }
            }
        }
        if (column.getItemCount() > 0) {
            main.add(column, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.TOP));
        }
        fillPanel();

        main.layout(true);
    }

    private void initView() {
        setBorders(true);
        setBodyBorder(false);
        setHeaderVisible(false);
        setLayout(new FitLayout());

        mainVerticval = new LayoutContainer();
        mainVerticval.setLayout(new RowLayout(Style.Orientation.VERTICAL));
        mainVerticval.setBorders(false);
        mainVerticval.setAutoHeight(true);
        add(mainVerticval);

        main = new LayoutContainer();
        main.setLayout(new TableRowLayout());
        main.setBorders(false);
        main.setScrollMode(Style.Scroll.AUTO);

        bottom = new LayoutContainer();
        HBoxLayout layoutBottom = new HBoxLayout();
        layoutBottom.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCHMAX);
        bottom.setLayout(layoutBottom);

        Button bSearch = new Button(constants.search());
        bSearch.addSelectionListener(new SearchSelectionListener());
        bSearch.setWidth(80);
        bSearch.setHeight(24);
        bottom.add(bSearch, new HBoxLayoutData(new Margins(0, 8, 0, 0)));

        Button bClean = new Button(constants.clean());
        bClean.addSelectionListener(new CleanSelectionListener());
        bClean.setWidth(80);
        bClean.setHeight(24);
        bottom.add(bClean, new HBoxLayoutData(new Margins(0, 8, 0, 0)));

        mainVerticval.add(main, new RowData(1, -1, new Margins(0, 0, 0, 0)));
        mainVerticval.add(bottom, new RowData(1, -1, new Margins(2, 0, 10, 0)));
    }

    protected Boolean isAllFieldValid() {

        Boolean isValid = Boolean.TRUE;
        for (Map.Entry fieldEntry : fields.entrySet()) {

            BoxComponent f = (BoxComponent) fieldEntry.getValue();

            if ((f instanceof TextField)) {
                if (!((TextField) f).getAllowBlank() && ((TextField)f).getRawValue().equals("")) {
                    isValid = Boolean.FALSE;
                }
            }
            if ((f instanceof Field)) {
                if (f.isVisible() && !((Field) f).isValid()) {
                    isValid = Boolean.FALSE;
                }
            }
        }
        if (!isValid) {
            MessageBox.alert(commonConstants.error(), datagridMessages.errorNotFieldsValid(), null).show();
            return false;
        }
        return true;
    }

    private void search() {
        if (!isAllFieldValid()) {
            return;
        }

        String message = checkBegEndDates();
        if (message != null) {
            MessageBox.alert(commonConstants.error(), message, null);
            return;
        }

        resPanel.getCondition().getSearches().clear();
        for (IColumnBuilder builder : resPanel.getTable().getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                if (!fields.containsKey(key)) {
                    continue;
                }
                //if ()
                BoxComponent field = fields.get(key);
                if (field instanceof Field) {
                    if ((((Field) field).getValue() == null && ((Field) field).getRawValue() == null) && !builder.getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()) {
                        continue;
                    }

                    IRowColumnVal val = resPanel.getTable().createRowColumnVal(key);

                    if (val == null && !builder.getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()) {
                        continue;
                    }
                    if ((builder instanceof DColumnBuilderComboBoxForOfficeDocFileFilter)
                            || (builder instanceof DColumnBuilderComboBoxForWorkingDays)
                            || (builder instanceof DColumnBuilderComboBoxForNoWorkingDays)) {
                        CheckBox cb = (CheckBox) field;
                        if (cb.getValue()) {
                            val.setVal(1);
                        } else {
                            val.setVal(0);
                        }

                    }
                    builder.setValueFromField(key, (Field) fields.get(key), val);
                    resPanel.getCondition().getSearches().put(key, val);
                } else if (field instanceof CheckBoxListView) {
                    if (builder instanceof DColumnBuilderComboBoxAndCheckList) {
                        //IRowColumnVal val = new DRowColumnValString();
                        IRowColumnVal val = resPanel.getTable().createRowColumnVal(key);
                        ((DColumnBuilderComboBoxAndCheckList) builder).setValueFromCheckBoxListView(key, (CheckBoxListView)fields.get(key), val);
                        resPanel.getCondition().getSearches().put(key, val);
                    }
                }
            }
        }
        resPanel.clearSelectionModel();

        if (resPanel instanceof DataGridPanel) {
            ((DataGridPanel) resPanel).setCurrentDomain(null);
            clearSelectionsRecursive((DataGridPanel) resPanel);
        }
        resPanel.search();

    }

    private void clearSelectionsRecursive(DataGridPanel p) {
        for (SRelationInfo subTable : p.getTable().getSubTables()) {
            String tabName = subTable.getQueryName();
            p.getSubDataGridPanels().get(tabName).clearSelectionModel();
            p.setCurrentDomain(null);
            clearSelectionsRecursive((DataGridPanel) p.getSubDataGridPanels().get(tabName));
        }
    }

    /* public Boolean validate() {
     Boolean isValid = Boolean.TRUE;
     for (Map.Entry fieldEntry : fields.entrySet()) {
     Field f = (Field) fieldEntry.getValue();

     if ((f instanceof TextField)) {
     if (!((TextField) f).getAllowBlank() && f.getRawValue().equals("")) {
     isValid = Boolean.FALSE;
     }
     }

     if (f.isVisible() && !f.isValid()) {
     //SKeyForColumn ff = (SKeyForColumn)fieldEntry.getKey();
     isValid = Boolean.FALSE;
     }
     }
     if (!isValid) {
     MessageBox.alert(commonConstants.error(), datagridMessages.errorNotFieldsValid(), null).show();
     return false;
     }
     return true;
     }*/
    private void clean() {
        for (Map.Entry colEntry : fields.entrySet()) {
            BoxComponent field = (BoxComponent) colEntry.getValue();
            if (field instanceof Field) {
                ((Field) field).clear();
            } else if (field instanceof CheckBoxListView) {
                List<BeanModel> model = ((CheckBoxListView)field).getChecked();
                for (BeanModel m : model) {
                    ((CheckBoxListView)field).setChecked(m, false);
                }
                ((CheckBoxListView)field).refresh();
                /*hlv if (  ((CheckBoxListView)field).getParent() instanceof ContentPanel)
                {
                   ((ContentPanel) ((CheckBoxListView)field).getParent()).collapse(); 
                }*/
                resPanel.resizePanelFiltr();                 
            }
        }
        resPanel.getCondition().getSearches().clear();

        for (IColumnBuilder builder : resPanel.getTable().getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                IRowColumnVal val = resPanel.getTable().createRowColumnVal(key);
                if (builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr() == null && !builder.getColumn(key).getColumnProperty().getIsNullWhenEmptySearch()) {
                    continue;
                }
                
                val.setVal(builder.getColumn(key).getColumnProperty().getDefaultValueForFiltr());
                resPanel.getCondition().getSearches().put(key, val);
            }
        }
        fillPanel();
        resPanel.search();
    }

    class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                search();
            }
        }
    };

    private class SearchSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            search();
        }
    }

    private class CleanSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            clean();
        }
    }

    //заполняем панель поиска данными из resPanel.getCondition().getSearches() если они есть
    private void fillPanel() {
        if (resPanel.getCondition().getSearches().isEmpty()) {
            return;
        }
        for (Map.Entry colEntry : fields.entrySet()) {
            if (colEntry instanceof Field) {
                ((Field) colEntry.getValue()).clear();
            }
        }
        for (IColumnBuilder builder : resPanel.getTable().getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();

               
                
                if (!resPanel.getCondition().getSearches().containsKey(key)) {//значения для заданного поля нет 
                    continue;
                }
                IRowColumnVal val = resPanel.getCondition().getSearches().get(key);
                if (val == null) {
                    continue;
                }
                if (!fields.containsKey(key)) {
                    continue;
                }
                if (fields.get(key) instanceof Field) {
                    builder.setValueToField(key, (Field) fields.get(key), val);
                }
                else if (fields.get(key) instanceof CheckBoxListView) {
                    if (builder instanceof DColumnBuilderComboBoxAndCheckList) {                       
                        ((DColumnBuilderComboBoxAndCheckList) builder).setValueToCheckBoxListView(key, (CheckBoxListView)fields.get(key), val);                        
                    }
                     //hlv resPanel.setIsShowPanelFiltr(true); //показываем панель фильтров
                }
            }
        }
    }

    protected String checkBegEndDates() {
        if (resPanel.getTable().getDateBegEndPairs() != null) {
            for (STwoKeys tk : resPanel.getTable().getDateBegEndPairs()) {
                Object val1 = null;
                Object val2 = null;
                if (fields.get(tk.getFirstKey()) instanceof Field) {
                    if (fields.containsKey(tk.getFirstKey())) {
                        val1 = ((Field) fields.get(tk.getFirstKey())).getValue();
                    }
                    if (fields.containsKey(tk.getSecondKey())) {
                        val2 = ((Field) fields.get(tk.getSecondKey())).getValue();
                    }
                }
                if ((val1 != null) && (val2 != null)) {
                    Date beg = null;
                    Date end = null;
                    if (val1.getClass() == String.class) {
                        if (!((String) val1).isEmpty()) {
                            String format = resPanel.getTable().getColumnBuilder(tk.getFirstKey()).getColumn(tk.getFirstKey()).getColumnProperty().getFormat();
                            beg = DateTimeFormat.getFormat(format).parse((String) val1);
                        }
                    } else if (val1.getClass() == Date.class) {
                        beg = (Date) val1;
                    }
                    if (val2.getClass() == String.class) {
                        if (!((String) val2).isEmpty()) {
                            String format = resPanel.getTable().getColumnBuilder(tk.getSecondKey()).getColumn(tk.getSecondKey()).getColumnProperty().getFormat();
                            end = DateTimeFormat.getFormat(format).parse((String) val2);
                        }
                    } else if (val2.getClass() == Date.class) {
                        end = (Date) val2;
                    }
                    if (beg != null && end != null) {
                        if (beg.after(end)) {
                            return tk.getMessage();
                        }
                    }
                }
            }
        }
        return null;
    }
}
