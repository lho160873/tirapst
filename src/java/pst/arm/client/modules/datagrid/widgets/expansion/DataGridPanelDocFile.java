package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenService;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author wesStyle
 */
public class DataGridPanelDocFile extends DataGridPanel {

  
    final GWTDataGridFileOpenServiceAsync fileOpenService = GWT.create(GWTDataGridFileOpenService.class);

    private Button btnCancell;
    private MenuItem menuItemCancell;

    private Button btnOpenFile;
    private MenuItem menuOpenFile;
    
   private Boolean isCancell = null, isOpenFile=null;
    
    public DataGridPanelDocFile(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDocFile(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDocFile(String tableName) {
        super(tableName);
    }

    public DataGridPanelDocFile(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDocFile(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();

        btnCancell = new Button(commonConstants.cancel());
        btnCancell.setIcon(AbstractImagePrototype.create(commonImages.cancel()));
        btnCancell.setIconAlign(Style.IconAlign.LEFT);
        btnCancell.addSelectionListener(tbListener);
        menuItemCancell = new MenuItem(commonConstants.cancel());
        menuItemCancell.setIcon(AbstractImagePrototype.create(commonImages.cancel()));
        menuItemCancell.setEnabled(false);
        menuItemCancell.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onCancell();
            }
        });

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                    onAddOcp();
                } else
                    onAdd();
            }
        });

        btnDelete = new Button(commonConstants.delete());
        btnDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        btnDelete.setIconAlign(Style.IconAlign.LEFT);
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(tbListener);
        menuItemDelete = new MenuItem(commonConstants.delete());
        menuItemDelete.setIcon(AbstractImagePrototype.create(commonImages.delete()));
        menuItemDelete.setEnabled(false);
        menuItemDelete.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                    onDeleteOcp();
                } else
                    onDelete();
            }
        });

        btnEdit = new Button(commonConstants.edit());
        btnEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnEdit.setIconAlign(Style.IconAlign.LEFT);
        btnEdit.setEnabled(false);
        btnEdit.addSelectionListener(tbListener);
        menuItemEdit = new MenuItem(commonConstants.edit());
        menuItemEdit.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        menuItemEdit.setEnabled(false);
        menuItemEdit.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                    onEditOcp();
                } else
                    onEdit();
            }
        });

        btnOpenFile = new Button("Открыть файл");
        btnOpenFile.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnOpenFile.setIconAlign(Style.IconAlign.LEFT);
        btnOpenFile.setEnabled(false);
        btnOpenFile.addSelectionListener(tbListener);
        menuOpenFile = new MenuItem("Открыть файл");
        menuOpenFile.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuOpenFile.setEnabled(false);
        menuOpenFile.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onOpenFile();
            }
        });

        btnView = new Button(commonConstants.view());
        btnView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        btnView.setIconAlign(Style.IconAlign.LEFT);
        btnView.setEnabled(false);
        btnView.addSelectionListener(tbListener);
        menuItemView = new MenuItem(commonConstants.view());
        menuItemView.setIcon(AbstractImagePrototype.create(commonImages.view()));
        menuItemView.setEnabled(false);
        menuItemView.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onView();
            }
        });
        
        btnFiltr = new Button(); 
        //btnFiltr = new ToggleButton();//datagridConstants.btnShowFiltr());
        btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        btnFiltr.setIcon(AbstractImagePrototype.create(datagridImages.find()));
        btnFiltr.setIconAlign(Style.IconAlign.LEFT);
        btnFiltr.setEnabled(true);
        //btnFiltr.toggle(true);
        btnFiltr.addSelectionListener(tbListener);
        btnFiltr.setVisible(false); //изначально делаем не видимойbtnFiltr

        btnSend = new Button(datagridConstants.btnSend());
        btnSend.setIcon(AbstractImagePrototype.create(datagridImages.check()));
        btnSend.setIconAlign(Style.IconAlign.LEFT);
        btnSend.setEnabled(false);
        btnSend.setVisible(false);
        btnSend.addSelectionListener(tbListener);


        btnSub = new Button(datagridConstants.btnSub());
        btnSub.setIcon(AbstractImagePrototype.create(datagridImages.form()));
        btnSub.setIconAlign(Style.IconAlign.LEFT);
        btnSub.setEnabled(false);
        btnSub.setVisible(false);
        btnSub.addSelectionListener(tbListener);


        btnRefr = new Button(datagridConstants.btnRefr());
        btnRefr.setIcon(AbstractImagePrototype.create(datagridImages.reset()));
        btnRefr.setIconAlign(Style.IconAlign.LEFT);
        btnRefr.setEnabled(true);
        btnRefr.setVisible(true);
        btnRefr.addSelectionListener(tbListener);
        menuItemRefr = new MenuItem(commonConstants.reset());
        menuItemRefr.setIcon(AbstractImagePrototype.create(commonImages.reset()));
        menuItemRefr.setEnabled(true);
        menuItemRefr.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                reloadGrid();
            }
        });

        btnReport = new Button(datagridConstants.btnReport());
        btnReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReport.setIconAlign(Style.IconAlign.LEFT);
        btnReport.setEnabled(true);
        btnReport.setVisible(false);
        btnReport.addSelectionListener(tbListener);
        menuItemReport = new MenuItem(datagridConstants.btnReport());
        menuItemReport.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        menuItemReport.setEnabled(true);
        menuItemReport.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
//                showReport();
            }
        });

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка пров доступа
        tb.add(sprEdit);
        tb.add(btnAdd);
        tb.add(btnEdit);
        tb.add(btnDelete);
        tb.add(btnOpenFile);
        tb.add(btnCancell);
        tb.add(btnView);

        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        tb.add(sprSend);
        tb.add(btnSend);

        sprSub = new SeparatorToolItem();
        sprSub.setVisible(false);
        tb.add(sprSub);
        tb.add(btnSub);

        sprReport = new SeparatorToolItem();
        sprReport.setVisible(false);
        tb.add(sprReport);
        //Кнопка для выбора типа сформированного отчета (PDF, DOC или XLS)
        btnReportSplit = new Button(datagridConstants.btnReport());//"Сформировать отчёт")
        btnReportSplit.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSplit.setIconAlign(Style.IconAlign.LEFT);
        btnReportSplit.setEnabled(true);
        btnReportSplit.setVisible(true);

        MenuItem itemPdf = new MenuItem(datagridConstants.menuPdf());// Сформировать PDF
        itemPdf.setId("0");
        itemPdf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("PDF");
            }
        });


        MenuItem itemRtf = new MenuItem(datagridConstants.menuRtf());//Сформировать DOC
        itemRtf.setId("1");
        itemRtf.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("RTF");
            }
        });

        MenuItem itemXls = new MenuItem(datagridConstants.menuXls());//"ОКП для включения работы в План предприятия");
        itemXls.setId("2");
        itemXls.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                showReport("XLS");
            }
        });
        
        reportMenuSplit = new Menu();
        reportMenuSplit.insert(itemXls, 0);
        reportMenuSplit.insert(itemRtf, 0);
        reportMenuSplit.insert(itemPdf, 0);

        
        btnReportSplit.setMenu(reportMenuSplit);
        
        // Добавляем split-кнопку для формирования разных типов отчетов
        tb.add(btnReportSplit);

        ftiFiltr = new FillToolItem();
        tb.add(ftiFiltr);
        
        btnColumnsVisibility = new Button();
        btnColumnsVisibility.setToolTip(commonConstants.columnsVisibility());
        btnColumnsVisibility.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibility()));
        btnColumnsVisibility.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibility.addSelectionListener(tbListener);
        sprColumnsVisibility = new SeparatorToolItem();
        sprColumnsVisibility.setVisible(true);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);
        
        btnColumnsVisibilityDel = new Button();
        btnColumnsVisibilityDel.setToolTip(commonConstants.columnsVisibilityDel());
        btnColumnsVisibilityDel.setIcon(AbstractImagePrototype.create(commonImages.columnsVisibilityDel()));
        btnColumnsVisibilityDel.setIconAlign(Style.IconAlign.LEFT);
        btnColumnsVisibilityDel.addSelectionListener(tbListener);
        sprColumnsVisibilityDel = new SeparatorToolItem();
        sprColumnsVisibilityDel.setVisible(true);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);

        btnHelpManual = new Button();
        btnHelpManual.setToolTip(datagridConstants.btnHelpManual());
        btnHelpManual.setIcon(AbstractImagePrototype.create(datagridImages.helpmanual()));
        btnHelpManual.setIconAlign(Style.IconAlign.LEFT);
        btnHelpManual.setEnabled(true);
        btnHelpManual.addSelectionListener(tbListener);
        btnHelpManual.setVisible(false); //изначально делаем не видимой

        sprHelpManual = new SeparatorToolItem();
        sprHelpManual.setVisible(false);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        
        sprFiltr = new SeparatorToolItem();
        sprFiltr.setVisible(false);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        menu = new Menu();
        menu.add(menuItemRefr);
        sprMenuItemEdit = new SeparatorMenuItem();
        menu.add(sprMenuItemEdit);
        menu.add(menuItemAdd);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemCancell);
        menu.add(menuItemView);
        menu.add(menuOpenFile);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

        enabledBtn();
        return tb;
    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        super.enabledBtn();

        String datagridName = tableName.toUpperCase();
        isCancell = (mode != Editable.EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictCancel")
                || ConfigurationManager.getPropertyAsBoolean("dictCancel_" + datagridName));

        btnCancell.setVisible(isCancell);
        menuItemCancell.setVisible(isCancell);
        
        /*isOpenFile = ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictView")
                || ConfigurationManager.getPropertyAsBoolean("dictView_" + datagridName);
       
        btnOpenFile.setVisible(isOpenFile);
        menuOpenFile.setVisible(isOpenFile);
        */

        updateToolBar();
    }
    
    @Override
    public void updateToolBar() {
        super.updateToolBar();
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(isCancell, btnCancell);
        removeOrAddBtnOnToolBar(isOpenFile, btnOpenFile);
    }

    protected void onCancell() {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final SKeyForColumn key = new SKeyForColumn("MAIN:IS_CANCELLED");
        MessageBox.confirm(commonConstants.alert(), datagridMessages.confirmCancelDoc(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    IRowColumnVal val = new DRowColumnValNumber();
                    val.setVal(1);

                    DDataGrid oldDomain = new DDataGrid();
                    oldDomain.copy(domain);
                    domain.getRows().put(key, val);

                    ((GWTDDataGridServiceAsync) getService()).save(tableName, domain, oldDomain, false, new AsyncCallback<DDataGrid>() {
                        @Override
                        public void onFailure(Throwable thrwbl) {
                             AppHelper.showMsgRpcServiceError(thrwbl);
                        }

                        @Override
                        public void onSuccess(DDataGrid t) {
                            reloadGrid();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void gridSelectionChanged() {

        btnDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);

        if (isOpenAsSub) {
            btnAdd.setEnabled(domainForSub != null);
            btnRefr.setEnabled(domainForSub != null);
            btnFiltr.setEnabled(domainForSub != null);
            menuItemAdd.setEnabled(domainForSub != null);
            menuItemRefr.setEnabled(domainForSub != null);
        }

        btnCancell.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemCancell.setEnabled(grid.getSelectionModel().getSelection().size() == 1);

        menuItemDelete.setEnabled(grid.getSelectionModel().getSelection().size() > 0);
        btnEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemEdit.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnOpenFile.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuOpenFile.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSub.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        btnSend.setEnabled(isBtnSendEnabled && grid.getSelectionModel().getSelection().size() == 1);

        DDataGrid domain = null;
        if (grid.getSelectionModel().getSelection().size() > 0) {
            domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        }
        fireDataGridEvent(DataGridEvents.DataGridSelectionChanged, domain, table);

        setAllSubDomain(domain);


    }

    @Override
    protected void gridRowDoubleClick(GridEvent event) {
        if (btnSend.isVisible() && btnSend.isEnabled()) {
            onSend();
        } else if (btnEdit.isVisible() && btnEdit.isEnabled()) {
            onEdit();
        } else if (btnView.isVisible() && btnView.isEnabled()) {
            onView();
        }
    }

    @Override
    protected void onDelete() {
        MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    Integer val = (Integer)getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal();
                    final String fid = val.toString();

                    fileOpenService.deleteFile(fid, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                             AppHelper.showMsgRpcServiceError(caught,"Ошибка удаления файла");
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result)
                                reloadGrid();
                            else
                                MessageBox.alert("Внимание", "Ошибка удаления файла. Обратитесь к Администратору.", null);
                        }
                    });
                }
            }
        });
    }

    protected void onOpenFile() {
        Integer val = (Integer)getSelectedDomain().getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal();
        final String fid = val.toString();

        fileOpenService.openFile(fid, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                 AppHelper.showMsgRpcServiceError(caught,"Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result)
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + fid);
                else
                    MessageBox.alert("Внимание", "Ошибка открытия файла. Обратитесь к Администратору.", null);
            }
        });
    }

    @Override
    protected void onAdd() {
        final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(List<DDataGrid> result) {
                if (result.size() != 1) {
                    MessageBox.alert("Ошибка", "Не определен путь к файлам документов. Обатитесь к Администратору", null);
                } else {
                    DDataGrid domain = createDomain();//new DDataGrid();

                    DRowColumnValString valStr = new DRowColumnValString();
                    valStr.setVal((String)result.get(0).getRows().get(new SKeyForColumn("MAIN:SRVR_NAME")).getVal());
                    domain.getRows().put(new SKeyForColumn("MAIN:SRVR_NAME"),
                            valStr);

                    valStr = new DRowColumnValString();
                    valStr.setVal((String)result.get(0).getRows().get(new SKeyForColumn("MAIN:ROOT_PATH")).getVal());
                    domain.getRows().put(new SKeyForColumn("MAIN:ROOT_PATH"),
                            valStr);

                    DRowColumnValNumber valNumb = new DRowColumnValNumber();
                    valNumb.setVal((Number)result.get(0).getRows().get(new SKeyForColumn("MAIN:FILE_ROOT_ID")).getVal());
                    domain.getRows().put(new SKeyForColumn("MAIN:FILE_ROOT_ID"),valNumb);

                    valNumb = new DRowColumnValNumber();
                    valNumb.setVal(ConfigurationManager.getUserId().intValue());
                    domain.getRows().put(new SKeyForColumn("MAIN:CREATOR_ID"), valNumb);

                    if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                        IRowColumnVal val = new DRowColumnValNumber();
                        val.setVal(1);
                        domain.getRows().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), val);
                    }

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

                    DataGridEditWindowDocFile window = new DataGridEditWindowDocFile(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                    window.show();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        DataGridSearchCondition cnd = new DataGridSearchCondition();
        if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
            IRowColumnVal val = new DRowColumnValNumber();
            val.setVal(1);
            cnd.getFilters().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), val);
        } else
            cnd.getFilters().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), passedFields.get(new SKeyForColumn("MAIN:DOC_TYPE_ID")));
        getService().getDataGrid("FILE_ROOT_VO", cnd, callback_getNorm);
    }

    protected void onAddOcp() {
        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String)result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer)result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                           Integer val = (Integer)dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                            if ((val != null) && val.equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) isValid = uid == userId;

                        if (isValid) {

                            // start

                            onAdd();

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " +
                                    userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal();

                DataGridSearchCondition cnd =  new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd =  new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    protected void onEditOcp() {
        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String)result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer)result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            Integer val = (Integer)dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                            if ((val != null) && val.equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) isValid = uid == userId;

                        if (isValid) {

                            // start

                            onEdit();

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " +
                                    userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal();

                DataGridSearchCondition cnd =  new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd =  new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    protected void onDeleteOcp() {
        final int uid = ConfigurationManager.getUserId().intValue();
        final AsyncCallback<List<DDataGrid>> callback_getOcp = new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onSuccess(final List<DDataGrid> result) {

                final AsyncCallback<List<DDataGrid>> callback_getUsers = new AsyncCallback<List<DDataGrid>>() {
                    @Override
                    public void onSuccess(List<DDataGrid> result1) {
                        String userName = null;
                        int userId = -1;
                        if (result.size() > 0) {
                            userName = (String)result.get(0).getRows().get(new SKeyForColumn("USERS:NAME")).getVal();
                            userId = (Integer)result.get(0).getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                        }
                        boolean isValid = false;
                        for (DDataGrid dd : result1) {
                            Integer val = (Integer)dd.getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                            if ((val != null) && val.equals(uid)) {
                                isValid = true;
                                break;
                            }
                        }
                        if (!isValid) isValid = uid == userId;

                        if (isValid) {

                            // start

                            onDelete();

                            // end

                        } else {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " +
                                    userName, null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        AppHelper.showMsgRpcServiceError(caught);
                    }
                };

                final int ocpId = (Integer)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal();

                DataGridSearchCondition cnd =  new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal(ocpId);
                cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);
                getService().getDataGrid("OCP_RIGHTS_FOR_REC_VO", cnd, callback_getUsers);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };

        DataGridSearchCondition cnd =  new DataGridSearchCondition();
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal((Number)condition.getFilters().get(new SKeyForColumn("MAIN:DOC_ID")).getVal());
        cnd.getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), val);

        getService().getDataGrid("OCP_VO", cnd, callback_getOcp);
    }

    @Override
    protected void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
                    if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                        onAddOcp();
                    } else
                        onAdd();
                } else if (ce.getButton() == btnColumnsVisibility) {
                    saveColumnsVisibility();
                } else if (ce.getButton() == btnColumnsVisibilityDel) {
                    delColumnsVisibility();
                } else if (ce.getButton() == btnRefr) {
                    //updateTableInfo();//hlv
                    reloadGrid();
                } else if (ce.getButton() == btnReport) {
//                    showReport();
                } else if (ce.getButton() == btnEdit) {
                    if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                        onEditOcp();
                    } else
                        onEdit();
                } else if (ce.getButton() == btnCancell) {
                    onCancell();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnOpenFile) {
                    onOpenFile();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                } else if (ce.getButton() == btnDelete) {
                    if (table.getQueryName().equals("DOC_FILE_OCP_VO")) {
                        onDeleteOcp();
                    } else
                        onDelete();
                }
                else if (ce.getButton() == btnHelpManual) {
                    showHelpManual();
                }
            }
        };

        saveDataGridListener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid domain) {
                if (domain.getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal() instanceof Integer)
                    fileId = Integer.toString((Integer)domain.getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal());
                else
                    fileId = (String)domain.getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal();
                reloadGrid();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }

    protected String fileId = null;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void selectCurrentRow() {
        List<BeanModel> models = grid.getStore().getModels();
        Boolean wasFound = false;
        if (fileId == null) {
            grid.getSelectionModel().select(0, false);
        }
        else if (models != null && models.size() > 0) {
            if (grid.getSelectionModel() != null) {
                for (BeanModel bm : models) {
                    DDataGrid dg = bm.getBean();

                    String cFileId = Integer.toString((Integer)dg.getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal());

                    if (cFileId.equals(fileId)) {
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
}
