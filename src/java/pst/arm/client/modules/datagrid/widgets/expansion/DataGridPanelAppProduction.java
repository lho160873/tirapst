package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.DRowColumnValString;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IColumnBuilder;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelAppProduction extends DataGridPanel {

    private Button btnTechn, btnSetters, btnNormative, btnProduction, btnAddFromCopy;
    private MenuItem menuAddFromCopy;
    final private GWTDDataGridServiceAsync serviceDataGrid = GWT.create(GWTDDataGridService.class);
    private int currentIndex;
    private LoadListener newLoadListener;
    private Boolean isBtn = false;

    public DataGridPanelAppProduction(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelAppProduction(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelAppProduction(String tableName) {
        super(tableName);
    }

    public DataGridPanelAppProduction(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelAppProduction(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    class DDataGridOrderNumComparator implements Comparator<DDataGrid> {

        private int compareJobs(Object o1JobNum, Object o2JobNum) {
            if (o1JobNum == null) {
                if (o2JobNum == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (o2JobNum == null) {
                    return 1;
                } else {
                    return o1JobNum.toString().compareTo(o2JobNum.toString());
                }
            }
        }

        private int compareOrders(Object o1OrderNum, Object o2OrderNum, Object o1JobNum, Object o2JobNum) {

            int result = 0;

            if ((result = o1OrderNum.toString().compareTo(o2OrderNum.toString())) == 0) {
                return compareJobs(o1JobNum, o2JobNum);
            } else {

                return result;

            }
        }

        @Override
        public int compare(DDataGrid o1, DDataGrid o2) {
            Object o1FullName = o1.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();
            Object o2FullName = o2.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();
            Object o1JobNum = o1.getRows().get(new SKeyForColumn("MAIN:SEQ_NUMBER")).getVal();
            Object o2JobNum = o2.getRows().get(new SKeyForColumn("MAIN:SEQ_NUMBER")).getVal();
            Object o1OrderNum = o1.getRows().get(new SKeyForColumn("MAIN:ORDER_NUM")).getVal();
            Object o2OrderNum = o2.getRows().get(new SKeyForColumn("MAIN:ORDER_NUM")).getVal();

            if (o1FullName == null) {
                if (o2FullName == null) {
                    return compareOrders(o1OrderNum, o2OrderNum, o1JobNum, o2JobNum);
                } else {
                    if (o2FullName.toString().equals("3") || o2FullName.toString().equals("1") || o2FullName.toString().equals("2")) {
                        return 1;
                    } else {
                        return compareOrders(o1OrderNum, o2OrderNum, o1JobNum, o2JobNum);
                    }
                }
            } else {
                if (o2FullName == null) {
                    if (o1FullName.toString().equals("3") || o1FullName.toString().equals("1") || o1FullName.toString().equals("2")) {
                        return -1;
                    } else {
                        return compareOrders(o1OrderNum, o2OrderNum, o1JobNum, o2JobNum);
                    }
                } else {

                    int result = 0;

                    if ((result = o1FullName.toString().compareTo(o2FullName.toString())) == 0) {
                        return compareOrders(o1OrderNum, o2OrderNum, o1JobNum, o2JobNum);
                    } else {

                        if (o1FullName.toString().equals("3")) {
                            return -1;
                        } else if (o1FullName.toString().equals("1")) {

                            if (o2FullName.toString().equals("3")) {
                                return 1;
                            } else {
                                return -1;
                            }

                        } else if (o1FullName.toString().equals("2")) {

                            if (o2FullName.toString().equals("3") || o2FullName.toString().equals("1")) {
                                return 1;
                            } else {
                                return -1;
                            }

                        } else {

                            if (o2FullName.toString().equals("3") || o2FullName.toString().equals("1") || o2FullName.toString().equals("2")) {
                                return 1;
                            } else {
                                return compareOrders(o1OrderNum, o2OrderNum, o1JobNum, o2JobNum);
                            }

                        }

                    }
                }
            }

        }
    }

    @Override
    protected ArrayList convertDomainToListOfHashMaps(List<DDataGrid> domains, DTable table) {
        DDataGrid[] domainsArray = domains.toArray(new DDataGrid[domains.size()]);
        Comparator<DDataGrid> dcomp = new DDataGridOrderNumComparator();
//        for (DDataGrid domain : domainsArray) {
//        for (Map.Entry entry : domain.getRows().entrySet()) {
//                SKeyForColumn key = (SKeyForColumn) entry.getKey();
//                String valKey = key.getTableAlias() + "-" + key.getColumnName();
//                MessageBox.alert("", valKey, null);
//                
//        }
//        }
        
        Arrays.sort(domainsArray, dcomp);

        String orgName = null;

        ArrayList data = new ArrayList();
        //PagingLoadConfig config = new BasePagingLoadConfig();
        //Map<String, Object> state = grid.getState(); //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
        for (DDataGrid domain : domainsArray) {
            Map map = new HashMap();
            Object oFullName = domain.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal();

            Map mapN = new HashMap();

            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                mapN.put(valKey, "");
            }

            if (oFullName == null) {

                if (!"".equals(orgName)) {

                    mapN.put("MAIN-DESCRIPTION", "Коммерческие");
                    data.add(mapN);
                    orgName = "";
                }

            } else {

                if (oFullName.toString().equals("3")) {
                    if (!"МАРТ".equals(orgName)) {
                        mapN.put("MAIN-DESCRIPTION", "МАРТ");
                        data.add(mapN);
                        orgName = "МАРТ";
                    }
                } else if (oFullName.toString().equals("1")) {
                    if (!"РИМР".equals(orgName)) {
                        mapN.put("MAIN-DESCRIPTION", "РИМР");
                        data.add(mapN);
                        orgName = "РИМР";
                    }
                } else if (oFullName.toString().equals("2")) {
                    if (!"Прибой".equals(orgName)) {
                        mapN.put("MAIN-DESCRIPTION", "Прибой");
                        data.add(mapN);
                        orgName = "Прибой";
                    }
                } else {
                    if (!"".equals(orgName)) {
                        mapN.put("MAIN-DESCRIPTION", "Коммерческие");
                        data.add(mapN);
                        orgName = "";
                    }
                }

            }

            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                IColumnBuilder builder = table.getColumnBuilder(key);
                //смотрим не убирал ли пользователь колонку из таблицы или может быть какую-нибудь добавил
                Boolean isVisibleInTable = builder.getColumn(key).getIsVisible();
                //смотрим не убирал ли пользователь колонку из таблицы или может быть какую-нибудь добавил
                //убрала это условия для отчета в журнале "Заявки проработки КД на возможность изготовления"(DH_ELABORATION_OF_DD_HLV)
                if (!table.getQueryName().equals("DH_ELABORATION_OF_DD_HLV")&&!table.getQueryName().equals("APP_PRODUCTION_HLV")) {
                    //String keyForState = "hidden" + key.getTableAlias() + ":" + key.getColumnName();//hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
                    String keyForState_1 = key.getTableAlias() + ":" + key.getColumnName();
                   //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (новое)
                    if (grid.getColumnModel().getColumnById(keyForState_1) != null) {
                        if (grid.getColumnModel().getColumnById(keyForState_1).isHidden()) 
                        {
                            isVisibleInTable = false;
                        } else {
                            isVisibleInTable = true;
                        }
                    }
                    //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
                    /*if (state.containsKey(keyForState)) {
                        //MessageBox.info("",keyForState,null);
                        if (state.get(keyForState).toString().equals("true")) {
                            isVisibleInTable = false;
                        }
                        if (state.get(keyForState).toString().equals("false")) {
                            isVisibleInTable = true;
                        }
                    }*/
                }                
                String valStr;
                if (isVisibleInTable) {
                    valStr = ((IRowColumnVal) entry.getValue()).getStringFromVal(domain, key, table.getColumnBuilder(key));
                    if (valStr == null) {
                        valStr = "";
                    }
                    map.put(valKey, valStr);

//                MessageBox.alert(valKey, valStr, null);
                }

            }
            data.add(map);
        }

        return data;
    }

//перезагрузка данных 
    @Override
    public void reloadGrid() {
        isBtn = false;
        super.reloadGrid();
    }

    @Override
    public void selectCurrentRow() {
        List<BeanModel> models = grid.getStore().getModels();
        Boolean wasFound = false;
        if (currentDomain == null) {
            grid.getSelectionModel().select(0, false);
        } else if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                for (BeanModel bm : models) {
                    DDataGrid dg = bm.getBean();
                    if (currentDomain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal().equals(dg.getRows().get(new SKeyForColumn("MAIN:ID")).getVal())) {
                        grid.getSelectionModel().select(models.indexOf(bm), false);
                        wasFound = true;
                        break;
                    }
                }
                if (!wasFound) {
                    grid.getSelectionModel().select(0, false);
                }
            }
        } else {
            setAllSubDomain(null);
        }
    }

    @Override
    protected void initStore() {
        super.initStore();
        newLoadListener = new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                panelGrid.unmask();
            }

            @Override
            public void handleEvent(LoadEvent le) {
                super.handleEvent(le);
                resizePanelFiltr();
                if (isBtn) {
                    grid.getSelectionModel().select(currentIndex, false);
                } else {
                    selectCurrentRow();
                }
            }

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                if (grid.getSelectionModel() != null) {
                    currentRow = grid.getStore().indexOf(grid.getSelectionModel().getSelectedItem());
                }
                super.loaderBeforeLoad(le);
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                panelGrid.unmask();
                AppHelper.showMsgRpcServiceError(le.exception);
                super.loaderLoadException(le);
            }
        };
        loader.removeLoadListener(mainLoadListener);
        loader.addLoadListener(newLoadListener);
    }

    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = super.createToolBar();

        btnTechn = new Button(datagridConstants.btnTechn());
        btnTechn.setIconAlign(Style.IconAlign.LEFT);
        btnTechn.setEnabled(false);
        btnTechn.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setDone("technologist", "APP_PRODUCTION_FOR_TECHNOLOGIST_HLV");
            }
        });


        btnSetters = new Button(datagridConstants.btnSetters());
        btnSetters.setIconAlign(Style.IconAlign.LEFT);
        btnSetters.setEnabled(false);
        btnSetters.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setDone("setters", "APP_PRODUCTION_FOR_SETTERS_HLV");
            }
        });

        btnNormative = new Button(datagridConstants.btnNormative());
        btnNormative.setIconAlign(Style.IconAlign.LEFT);
        btnNormative.setEnabled(false);
        btnNormative.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setDone("normative", "APP_PRODUCTION_FOR_NORMATIVE_HLV");
            }
        });


        btnProduction = new Button(datagridConstants.btnProduction());
        btnProduction.setIconAlign(Style.IconAlign.LEFT);
        btnProduction.setEnabled(false);
        btnProduction.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setDone("production", "APP_PRODUCTION_FOR_PRODUCTION_HLV");
            }
        });

        btnAddFromCopy = new Button(datagridConstants.btnAddFromCopy());
        btnAddFromCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAddFromCopy.setIconAlign(Style.IconAlign.LEFT);
        btnAddFromCopy.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                onAddFromCopy();
            }
        });

        menuAddFromCopy = new MenuItem(datagridConstants.btnAddFromCopy());
        menuAddFromCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuAddFromCopy.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onAddFromCopy();
            }
        });

        tb.remove(btnEdit);
        tb.remove(btnDelete);
        tb.remove(btnView);
        tb.remove(sprSend);
        tb.remove(btnSend);
        tb.remove(sprSub);
        tb.remove(btnSub);
        tb.remove(sprReport);
        tb.remove(btnReportSplit);


        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);

        tb.add(btnAddFromCopy);
        tb.add(btnEdit);
        tb.add(btnDelete);
        tb.add(btnView);
        tb.add(sprSend);
        tb.add(btnSend);
        tb.add(sprSub);
        tb.add(btnSub);
        tb.add(sprReport);
        tb.add(btnReportSplit);


        tb.add(new SeparatorToolItem());
        tb.add(btnTechn);
        tb.add(new SeparatorToolItem());
        tb.add(btnSetters);
        tb.add(new SeparatorToolItem());
        tb.add(btnNormative);
        tb.add(new SeparatorToolItem());
        tb.add(btnProduction);
        tb.add(new SeparatorToolItem());

        tb.add(ftiFiltr);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        tb.add(sprFiltr);
        tb.add(btnFiltr);

        tb.add(ftiFiltr);
        tb.remove(sprColumnsVisibility);
        tb.remove(btnColumnsVisibility);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        tb.remove(sprColumnsVisibilityDel);
        tb.remove(btnColumnsVisibilityDel);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        menu.remove(menuItemEdit);
        menu.remove(menuItemDelete);
        menu.remove(menuItemView);
        menu.add(menuAddFromCopy);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);

        enabledBtn();

        return tb;
    }
@Override
    protected void onAdd() {
       DDataGrid domain = createDomain();//new DDataGrid();
        //domain.setName(table.getQueryName());

        if (condition.getFilters().size() > 0) {
            //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
            //domain.setName(table.getQueryName());
            for (Map.Entry filtr : condition.getFilters().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (isOpenAsSub && passedFields != null) {
            for (Map.Entry passField : passedFields.entrySet()) {
                SKeyForColumn key = (SKeyForColumn) passField.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) passField.getValue();
                domain.getRows().put(key, val);
            }
        }

        if (condition.getValDefault().size() > 0) {
            //если есть значения по умолчанию задаем их
            //domain.setName(table.getQueryName());
            for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
                //SKeyForColumn val = (SKeyForColumn) key.getValue();
                IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                domain.getRows().put(key, val);
            }
        }

        DataGridEditWindow window = new DataGridEditWindowAppProduction(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

   

    @Override
    protected void onEdit() {
          DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
         
        
        DataGridEditWindow window = new DataGridEditWindowAppProduction(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }
    
        protected void onView() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.VIEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onAddFromCopy() {
        DDataGrid oldDomain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DDataGrid domain = new DDataGrid();
        domain.copy(oldDomain);

        IRowColumnVal val_int = new DRowColumnValNumber();
        val_int.setVal(null);
        domain.getRows().put(new SKeyForColumn("MAIN:ID"), val_int);
        domain.getRows().put(new SKeyForColumn("MAIN:SEQ_NUMBER"), val_int);

        IRowColumnVal val_str = new DRowColumnValString();
        val_str.setVal(null);
        domain.getRows().put(new SKeyForColumn("MAIN:OPPSP_FACT"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TECHNOLOGIST_PLAN"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TECHNOLOGIST_FACT"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TECHNOLOGIST"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:SETTERS_PLAN"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:SETTERS_FACT"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:SETTERS"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:NORMATIVE_PLAN"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:NORMATIVE_FACT"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:NORMATIVE"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TO_PRODUCTION_PLAN"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TO_PRODUCTION_FACT"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:TO_PRODUCTION"), val_str);
        domain.getRows().put(new SKeyForColumn("MAIN:UPDATE_NAME"), val_str);

        IRowColumnVal val_user = new DRowColumnValString();
        val_user.setVal(ConfigurationManager.getUserName());
        domain.getRows().put(new SKeyForColumn("MAIN:CREAT_NAME"), val_user);

        DataGridEditWindow window = new DataGridEditWindowAppProduction(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    @Override
    protected void gridSelectionChanged() {
        super.gridSelectionChanged();
        btnTechn.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSetters.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnNormative.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnProduction.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
    }

    private void setDone(String role, final String tabName) {
        if (!ConfigurationManager.getPropertyAsBoolean(role)) {
            MessageBox.alert("Внимание!", "У Вас нет прав на отметку!", null);
            return;
        }
        isBtn = true;
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "Не выбрано задание.", null);
            return;
        }
        //Определение индекса текущей строки
        List<BeanModel> models = grid.getStore().getModels();
        if (grid.getSelectionModel() != null) {
            for (BeanModel bm : models) {
                DDataGrid dg = bm.getBean();
                if (dg.isDomainEquals(domain)) {
                    currentIndex = models.indexOf(bm);
                    break;
                }
            }
        }

        final DomainSaveSuccesedListener<DDataGrid> listener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid d) {
                reloadGrid();
            }
        };

        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {
            @Override
            public void onSuccess(DTable result) {
                final DTable table = result;
                final DDataGrid domainData = new DDataGrid();
                domainData.setName(table.getQueryName());
                for (IColumnBuilder builder : table.getColumnBuilders()) {
                    for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                        IRowColumnVal val = builder.createRowColumnVal(key);
                        if (builder.getColumn(key).getIsKey()) {
                            val.setVal(currentId);
                        }
                        domainData.getRows().put(key, val);
                    }
                }
                serviceDataGrid.getDataGridById(tabName, domainData, new AsyncCallback<DDataGrid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(DDataGrid t) {
                        DDataGrid domainData = new DDataGrid();
                        domainData.copy(t);
                        DataGridEditWindow window;
                        window = new DataGridEditWindow(domainData, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
                        window.setWidth(500);
                        if (tabName.equals("APP_PRODUCTION_FOR_PRODUCTION_HLV"))
                        {
                           window.setHeight(300);  
                        }
                        else
                        {
                        window.setHeight(210);
                        }
                        window.addDomainSaveSuccesedListener(listener);
                        window.show();
                    }
                });

            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        serviceDataGrid.getTable(tabName, callback_getTable);

    }
}
