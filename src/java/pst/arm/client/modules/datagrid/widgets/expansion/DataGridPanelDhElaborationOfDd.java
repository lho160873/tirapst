package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValDate;
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
public class DataGridPanelDhElaborationOfDd extends DataGridPanel {

    private Button btnOgt, btnTechn, btnSetters, btnNormative, btnState, btnStateCorrect;
    final private GWTDDataGridServiceAsync serviceDataGrid = GWT.create(GWTDDataGridService.class);
    private int currentIndex;
    private LoadListener newLoadListener;
    private Boolean isBtn = false;

    public DataGridPanelDhElaborationOfDd(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDhElaborationOfDd(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDhElaborationOfDd(String tableName) {
        super(tableName);
    }

    public DataGridPanelDhElaborationOfDd(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDhElaborationOfDd(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
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
        }
        else if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                for (BeanModel bm : models) {
                    DDataGrid dg = bm.getBean();
                    if (currentDomain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal().equals(dg.getRows().get(new SKeyForColumn("MAIN:ID")).getVal())) {
                        grid.getSelectionModel().select(models.indexOf(bm), false);
                        wasFound = true;
                        break;
                    }
                }
                if (!wasFound)
                    grid.getSelectionModel().select(0, false);
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

        btnOgt = new Button(datagridConstants.btnOgt());
        btnOgt.setIconAlign(Style.IconAlign.LEFT);
        btnOgt.setEnabled(false);
        btnOgt.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                showOgt();
            }
        });

        btnState = new Button(datagridConstants.btnState());
        btnState.setIconAlign(Style.IconAlign.LEFT);
        btnState.setEnabled(false);
        btnState.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                showState();
            }
        });
        
        btnStateCorrect = new Button(datagridConstants.btnStateCorrect());
        btnStateCorrect.setIconAlign(Style.IconAlign.LEFT);
        btnStateCorrect.setEnabled(false);
        btnStateCorrect.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                showStateCorrect();
            }
        });

        btnTechn = new Button(datagridConstants.btnTechn());
        btnTechn.setIconAlign(Style.IconAlign.LEFT);
        btnTechn.setEnabled(false);
        btnTechn.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setTechn();
            }
        });

        btnSetters = new Button(datagridConstants.btnSetters());
        btnSetters.setIconAlign(Style.IconAlign.LEFT);
        btnSetters.setEnabled(false);
        btnSetters.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setSetters();
            }
        });

        btnNormative = new Button(datagridConstants.btnNormative());
        btnNormative.setIconAlign(Style.IconAlign.LEFT);
        btnNormative.setEnabled(false);
        btnNormative.addSelectionListener(new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                setNormative();
            }
        });

        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);

        tb.add(new SeparatorToolItem());
        tb.add(btnOgt);
        tb.add(new SeparatorToolItem());
        tb.add(btnState);
        tb.add(btnStateCorrect);
        tb.add(new SeparatorToolItem());
        tb.add(btnTechn);
        tb.add(new SeparatorToolItem());
        tb.add(btnSetters);
        tb.add(new SeparatorToolItem());
        tb.add(btnNormative);
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

        enabledBtn();
        
         btnStateCorrect.setVisible((mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("statusCorrect")));
        
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

        DRowColumnValString val = new DRowColumnValString();
        val.setVal(ConfigurationManager.getWorkerName());
        SKeyForColumn key = new SKeyForColumn("MAIN:REGISTERED");
        domain.getRows().put(key, val);

        DataGridEditWindow window = new DataGridEditWindowDhElaborationOfDd(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    @Override
    protected void onDelete() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        SKeyForColumn keyStatus = new SKeyForColumn("MAIN:ID_STATUS");
        Integer idStatus = (Integer) domain.getRows().get(keyStatus).getVal();
        if (idStatus != 0) {
            MessageBox.alert("Внимание!", "Данную заявку удалять нельзя.", null);
            return;
        }

        super.onDelete();
    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        SKeyForColumn key = new SKeyForColumn("MAIN:REGISTERED_USER_ID");
        Integer userId = (Integer) domain.getRows().get(key).getVal();
        Integer currUserId = ConfigurationManager.getUserId().intValue();
        
        if ( (!currUserId.equals(userId)) && (!ConfigurationManager.getPropertyAsBoolean("editElaborationOfDd")) )        
        {   
            MessageBox.alert("Внимание!", "Вы не имеете права редактировать данную заявку.", null);
            return;
        }

        DataGridEditWindow window = new DataGridEditWindowDhElaborationOfDd(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    @Override
    protected void gridSelectionChanged() {

        super.gridSelectionChanged();
        btnOgt.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnState.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnStateCorrect.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnTechn.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSetters.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnNormative.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
    }


  
    
    private void showState() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "Не выбран заказ.", null);
            return;
        }

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

                serviceDataGrid.getDataGridById("DH_ELABORATION_OF_DD_FOR_STATUS_HLV", domainData, new AsyncCallback<DDataGrid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(DDataGrid t) {
                        DDataGrid domainData = new DDataGrid();
                        domainData.copy(t);
                        DataGridEditWindow window;

                        String strVal = domainData.getRows().get(new SKeyForColumn("MAIN:ID_STATUS")).getStringFromVal(new SKeyForColumn("MAIN:ID_STATUS"), table.getColumnBuilder(new SKeyForColumn("MAIN:ID_STATUS")));
                        DCondition cnd = new DCondition(new SKeyForColumn("STATUS:ID_STATUS_PREV"), strVal);
                        window = new DataGridEditWindow(domainData, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW, cnd);
                        window.setWidth(700);
                        window.setHeight(160);
                        window.addDomainSaveSuccesedListener(new DomainSaveSuccesedListener<DDataGrid>() {
                            @Override
                            public void onDomainSaveSucceed(DDataGrid domain) {
                                reloadGrid();
                            }
                        });
                        window.show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        serviceDataGrid.getTable("DH_ELABORATION_OF_DD_FOR_STATUS_HLV", callback_getTable);
    }
 
    
    private void showStateCorrect() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "Не выбран заказ.", null);
            return;
        }
        if (  !ConfigurationManager.getPropertyAsBoolean("statusCorrect") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку статуса!", null);
            return;
        }
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

                serviceDataGrid.getDataGridById("DH_ELABORATION_OF_DD_FOR_STATUS_CORRECT_HLV", domainData, new AsyncCallback<DDataGrid>() {
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
                        window.setWidth(700);
                        window.setHeight(160);
                        window.addDomainSaveSuccesedListener(new DomainSaveSuccesedListener<DDataGrid>() {
                            @Override
                            public void onDomainSaveSucceed(DDataGrid domain) {
                                reloadGrid();
                            }
                        });
                        window.show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        serviceDataGrid.getTable("DH_ELABORATION_OF_DD_FOR_STATUS_CORRECT_HLV", callback_getTable);
    }

    private void showOgt() {
        if (  !ConfigurationManager.getPropertyAsBoolean("ogt") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку ОГТ!", null);
            return;
        }
        isBtn = true;
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "Не выбран заказ.", null);
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
                serviceDataGrid.getDataGridById("DH_ELABORATION_OF_DD_FOR_OGT_HLV", domainData, new AsyncCallback<DDataGrid>() {
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
                        window.setWidth(400);
                        window.setHeight(210);
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

        serviceDataGrid.getTable("DH_ELABORATION_OF_DD_FOR_OGT_HLV", callback_getTable);
    }

    private void setDate(final String sKeyDate, final String sKeyName) {
        isBtn = true;
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        final Integer currentId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:ID")).getVal();
        if (currentId == null) {
            MessageBox.info("", "Не выбрана заявка.", null);
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
        final DDataGrid domainData = new DDataGrid();
        domainData.setName(tableName.toUpperCase());
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
        //получаем свежие данные
        serviceDataGrid.getDataGridById(tableName, domainData, new AsyncCallback<DDataGrid>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(DDataGrid newDomain) {
                DDataGrid oldDomain = new DDataGrid();
                oldDomain.copy(newDomain);

                String format = "dd.MM.yyyy HH:mm";
                Date date = DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(new Date()));

                SKeyForColumn key = new SKeyForColumn(sKeyDate);
                IRowColumnVal val = new DRowColumnValDate();
                if (newDomain.getRows().get(key).getVal() == null) {
                    val.setVal(date);
                } else {
                    val.setVal(null);
                }
                newDomain.getRows().put(key, val);

                key = new SKeyForColumn(sKeyName);
                val = new DRowColumnValString();
                if (newDomain.getRows().get(key).getVal() == null) {
                    val.setVal(ConfigurationManager.getWorkerName());
                } else {
                    val.setVal(null);
                }
                newDomain.getRows().put(key, val);

                AsyncCallback<DDataGrid> callback = new AsyncCallback<DDataGrid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }

                    @Override
                    public void onSuccess(DDataGrid result) {
                        reloadGrid();
                    }
                };
                getService().save(tableName, newDomain, oldDomain, false, callback);
            }
        });

    }

    private void setTechn() {
         if (  !ConfigurationManager.getPropertyAsBoolean("techn") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку!", null);
            return;
        }
        setDate("MAIN:TECHNOLOGIST_FACT", "MAIN:TECHNOLOGIST");
    }

    private void setSetters() {
         if (  !ConfigurationManager.getPropertyAsBoolean("setters") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку!", null);
            return;
        }
        setDate("MAIN:SETTERS_FACT", "MAIN:SETTERS");
    }

    private void setNormative() {
         if (  !ConfigurationManager.getPropertyAsBoolean("normative") )
        {
            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку!", null);
            return;
        }
        setDate("MAIN:NORMATIVE_FACT", "MAIN:NORMATIVE");
    }
    
    protected ArrayList convertDomainToListOfHashMaps(List<DDataGrid> domains, DTable table) {
        ArrayList data = new ArrayList();
        //PagingLoadConfig config = new BasePagingLoadConfig();
        //Map<String, Object> state = grid.getState(); //hlv 16.12.2019 изменила алгоритм определения спрятанных/показанных колонок (старое)
        for (DDataGrid domain : domains) {
            Map map = new HashMap();
            for (Map.Entry entry : domain.getRows().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) entry.getKey();
                String valKey = key.getTableAlias() + "-" + key.getColumnName();
                IColumnBuilder builder = table.getColumnBuilder(key);
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
                if (isVisibleInTable || key.equals(new SKeyForColumn("MAIN:URGENCY_CODE2"))) {
                    valStr = ((IRowColumnVal) entry.getValue()).getStringFromVal(domain, key, table.getColumnBuilder(key));
                    if (valStr == null) {
                        valStr = "";
                    }
                    map.put(valKey, valStr);
                }
            }
            data.add(map);
        }
        return data;
    }

}
