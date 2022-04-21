/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

import java.util.*;

/**
 *
 * @author Igor
 */
public class DataGridPanelOfficeDocContrIg extends DataGridPanel {

    int currentIndex = 0; // индекс текущей строки, на которой должно оставаться выделение после проставления отметки о выполнении
//    List <Selection> listSelection;
//    ModelData selectedItem;

    public DataGridPanelOfficeDocContrIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        disableButtons();
    }

    public DataGridPanelOfficeDocContrIg(DTable table, String tn) {
        super(table, tn);
        disableButtons();
    }

    public DataGridPanelOfficeDocContrIg(String tableName) {
        super(tableName);
        disableButtons();
    }
    private Button btnCancell;
    private MenuItem menuItemCancell;

    public DataGridPanelOfficeDocContrIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        disableButtons();
    }

    public DataGridPanelOfficeDocContrIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        disableButtons();
    }

    private void disableButtons() {
        btnAdd.removeFromParent();
        menuItemAdd.removeFromParent();
        btnEdit.removeFromParent();
        menuItemEdit.removeFromParent();
        btnDelete.removeFromParent();
        menuItemDelete.removeFromParent();
        btnCancell.removeFromParent();
        menuItemCancell.removeFromParent();
    }

    /*@Override
     protected void onAdd() {
     final AsyncCallback <List<DDataGrid>> callback_getDocType = new AsyncCallback <List<DDataGrid>>() {
     @Override
     public void onSuccess(final List<DDataGrid> result1) {
     DDataGrid domain = new DDataGrid();

     if (condition.getFilters().size() > 0) {
     domain.setName(table.getQueryName());
     for (Map.Entry filtr : condition.getFilters().entrySet()) {
     SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
     IRowColumnVal val = (IRowColumnVal) filtr.getValue();
     domain.getRows().put(key, val);
     }
     }
     if (condition.getValDefault().size() > 0) {
     for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
     SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
     IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
     domain.getRows().put(key, val);
     }
     }

     domain.getRows().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), result1.get(0).getRows().get(new SKeyForColumn("MAIN:CONTRACT_STAGE_ID"))); //hlv - код тоже подтягиваем

     String strVal = domain.getRows().get(new SKeyForColumn("SCS:OCP_ID")).getStringFromVal(new SKeyForColumn("SCS:OCP_ID"), table.getColumnBuilder(new SKeyForColumn("SCS:OCP_ID")));
     DCondition cnd = new DCondition(new SKeyForColumn("MAIN:OCP_ID"), strVal);

     DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.NEW, EWindowType.EWT_WINDOW, cnd);
     window.addDomainSaveSuccesedListener(saveDataGridListener);
     window.show();
     }

     @Override
     public void onFailure(Throwable caught) {

     }
     };

     DataGridSearchCondition condition1 = new DataGridSearchCondition();
     SKeyForColumn kk = new SKeyForColumn("DOC_TYPE:DOC_TYPE_ID");
     IRowColumnVal vv = new DRowColumnValNumber();
     vv.setVal(2);
     condition1.getFilters().put(kk, vv);

     getService().getDataGrid("DOC_TYPE_VO", condition1, callback_getDocType);
     }
     */
    //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();

        btnCancell = new Button(commonConstants.cancel());
        btnCancell.setIcon(AbstractImagePrototype.create(commonImages.cancel()));
        btnCancell.setIconAlign(Style.IconAlign.LEFT);
        btnCancell.addSelectionListener(tbListener);

        btnAdd = new Button(commonConstants.add());
        btnAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnAdd.setIconAlign(Style.IconAlign.LEFT);
        btnAdd.addSelectionListener(tbListener);
        menuItemAdd = new MenuItem(commonConstants.add());
        menuItemAdd.setIcon(AbstractImagePrototype.create(commonImages.add()));
        menuItemAdd.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
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
                onEdit();
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

        menuItemCancell = new MenuItem(commonConstants.cancel());
        menuItemCancell.setIcon(AbstractImagePrototype.create(commonImages.cancel()));
        menuItemCancell.setEnabled(false);
        menuItemCancell.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onCancell();
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

        btnMark = new Button(datagridConstants.btnMark());
        btnMark.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnMark.setIconAlign(Style.IconAlign.LEFT);
        btnMark.setEnabled(true);
        btnMark.setVisible(true);
        btnMark.addSelectionListener(tbListener);
        menuItemMark = new MenuItem(datagridConstants.btnMark());
        menuItemMark.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        menuItemMark.setEnabled(true);
        menuItemMark.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                mark();
            }
        });

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка прав доступа
        tb.add(sprEdit);
        //tb.add(btnAdd);
        //tb.add(btnEdit);
        //tb.add(btnDelete);
        //tb.add(btnCancell);
        tb.add(btnView);

        sprSend = new SeparatorToolItem();
        sprSend.setVisible(false);
        tb.add(sprSend);
        tb.add(btnSend);

        sprSub = new SeparatorToolItem();
        sprSub.setVisible(false);
        tb.add(sprSub);
        tb.add(btnSub);

        tb.add(sprSub);

        sprMark = new SeparatorToolItem();
        sprMark.setVisible(false);
        tb.add(sprMark);
        tb.add(btnMark);

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
//        tb.add(btnReportSplit);

        //tb.add(new SeparatorToolItem());

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
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        menu.add(menuItemMark);
        //menu.add(menuItemReport);

        enabledBtn();

        return tb;
    }

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        super.enabledBtn();
        
        isDictView = true;
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);
        
        btnCancell.setVisible(isDictEdit);
        menuItemCancell.setVisible(isDictEdit);
        
        btnView.setVisible(isDictView);
        menuItemView.setVisible(isDictView);        
        
        sprEdit.setVisible(isSprDictEdit);
        sprMenuItemEdit.setVisible(isSprDictEdit);
      
        updateToolBar();
    } 
    
    @Override
    public void updateToolBar() {
        super.updateToolBar();
        if (toolBar == null) {
            return;
        }

        removeOrAddBtnOnToolBar(isDictEdit, btnCancell);
    }

    @Override
    protected void gridSelectionChanged() {

        if (isOpenAsSub) {
            btnRefr.setEnabled(domainForSub != null);
            btnFiltr.setEnabled(domainForSub != null);
            menuItemRefr.setEnabled(domainForSub != null);
        }


        btnView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
        menuItemView.setEnabled(grid.getSelectionModel().getSelection().size() == 1);
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
    protected void initListeners() {
        tbListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnAdd) {
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
                } else if (ce.getButton() == btnCancell) {
                    onCancell();
                } else if (ce.getButton() == btnEdit) {
                    onEdit();
                } else if (ce.getButton() == btnView) {
                    onView();
                } else if (ce.getButton() == btnSend) {
                    onSend();
                } else if (ce.getButton() == btnFiltr) {
                    setIsShowPanelFiltr(!panelFiltr.isVisible());
                } else if (ce.getButton() == btnDelete) {
                    onDelete();
                } else if (ce.getButton() == btnMark) {
                    mark();
                } else if (ce.getButton() == btnHelpManual) {
                    showHelpManual();
                }
            }
        };

        saveDataGridListener = new DomainSaveSuccesedListener<DDataGrid>() {
            @Override
            public void onDomainSaveSucceed(DDataGrid domain) {
                BeanModelFactory factory = (BeanModelFactory) BeanModelLookup.get().getFactory(DDataGrid.class);
                ModelData model = factory.createModel(domain);
                if (grid.getStore() == null) {
                    return;
                }
                if (grid.getStore() == null) {
                    return;
                }
                if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null && (((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean() == domain)) {

                    grid.getStore().update(model);
                    grid.getSelectionModel().select(model, true);
                } else {
                    grid.getStore().insert(model, grid.getStore().getCount());
                    grid.getSelectionModel().select(model, true);
                }
                reloadGrid();
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }

    protected void onAdd() {
        DDataGrid domain = new DDataGrid();
        domain.setName(table.getQueryName());

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

        IRowColumnVal val = new DRowColumnValNumber();
        val.setVal(2);
        domain.getRows().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), val);

        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        DataGridEditWindow window = new DataGridEditWindow(domain, table, EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();
    }

    protected void onCancell() {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        final SKeyForColumn key = new SKeyForColumn("MAIN:DATE_CANCELLED");
        if (domain.getRows().get(key).getVal() == null) {
            MessageBox.confirm(commonConstants.alert(), datagridMessages.confirmCancelDoc(), new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                        IRowColumnVal val = new DRowColumnValDate();
                        String format = "dd.MM.yyyy";
                        Date date = DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(new Date()));
                        val.setVal(date);

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
        } else {
            MessageBox.alert("Предупреждение!", "Документ уже отменен. Повторная отмена невозможна", null);
        }
    }

    protected void mark() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

//         Определение индекса текущей строки
        List<BeanModel> models = grid.getStore().getModels();

        if (grid.getSelectionModel() != null) {
            for (BeanModel bm : models) {
                DDataGrid dg = bm.getBean();
                if (dg.isDomainEquals(currentDomain)) {
                    currentIndex = models.indexOf(bm);
                    break;
                }
            }
        }

        DDataGrid oldDomain = new DDataGrid();
        oldDomain.copy(domain);

        String format = "dd.MM.yyyy";
        Date date = DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(new Date()));

        SKeyForColumn key = new SKeyForColumn("MAIN:DATE_FACT");
        IRowColumnVal val = new DRowColumnValDate();

//        Если отметка о выполнении пуста, то вписываем, если уже есть, то не вписываем
        if (domain.getRows().get(key).getVal() == null) {

            val.setVal(date);
            domain.getRows().put(key, val);
            AsyncCallback<DDataGrid> callback = new AsyncCallback<DDataGrid>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(DDataGrid result) {
                    //          Переопределяем loader для работы с выбранной строкой
                    loader.addLoadListener(new LoadListener() {
                        @Override
                        public void loaderLoad(LoadEvent le) {
                            panelGrid.unmask();
                        }

                        @Override
                        public void handleEvent(LoadEvent le) {
                            super.handleEvent(le);
                            resizePanelFiltr();
                            grid.getSelectionModel().select(currentIndex, false);
                        }

                        @Override
                        public void loaderBeforeLoad(LoadEvent le) {
                            if (grid.getSelectionModel() != null) {
                                currentRow = currentIndex;
                            }
                            super.loaderBeforeLoad(le);
                        }

                        @Override
                        public void loaderLoadException(LoadEvent le) {
                            panelGrid.unmask();
                           AppHelper.showMsgRpcServiceError(le.exception);
                            super.loaderLoadException(le);
                        }
                    });

                    loader.load();
                }
            };

            getService().save(tableName, domain, oldDomain, false, callback);
        }
    }
}
