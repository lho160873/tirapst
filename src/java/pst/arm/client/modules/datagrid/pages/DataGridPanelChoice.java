package pst.arm.client.modules.datagrid.pages;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.Date;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;

import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.expansion.DataGridEditWindowChoice;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelChoice extends DataGridPanel {

    private int currentIndex;
    private LoadListener newLoadListener;
    private Boolean isNew = false;
    DDataGrid newDomain;

    public DataGridPanelChoice(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelChoice(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelChoice(String tableName) {
        super(tableName);
    }

    public DataGridPanelChoice(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelChoice(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    public void reloadGrid() {
        super.reloadGrid();
    }

    @Override
    protected void onDelete() {
        super.onDelete();
        isNew = false;
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
                if (newDomain != null) {
                    List<BeanModel> models = grid.getStore().getModels();

                    Date var2 = (Date) newDomain.getRows().get(new SKeyForColumn("MAIN:BEG_DATE")).getVal();

                    String str2 = var2.getYear() + "." + var2.getMonth() + "." + var2.getDate() + ":"
                            + newDomain.getRows().get(new SKeyForColumn("MAIN:ACTIVITY_SIGN")).getVal().toString() + ":"
                            + newDomain.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal().toString() + "\n";
                    String str = "";
                    int i = 0;
                    for (BeanModel bm : models) {
                        DDataGrid dg = bm.getBean();
                        Date var = (Date) dg.getRows().get(new SKeyForColumn("MAIN:BEG_DATE")).getVal();

                        str = var.getYear() + "." + var.getMonth() + "." + var.getDate() + ":"
                                + dg.getRows().get(new SKeyForColumn("MAIN:ACTIVITY_SIGN")).getVal().toString() + ":"
                                + dg.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal().toString() + "\n";
                        if (str.equals(str2)) {
                            currentIndex = i;
                            break;
                        }
                        i++;
                    }

                }

                if (isNew) {
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

    //создаем панель с кнопками управления
    @Override
    protected ToolBar createToolBar() {
        ToolBar tb = new ToolBar();

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
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);

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

        DataGridEditWindow window = new DataGridEditWindowChoice(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();

        isNew = true;
    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();


        DataGridEditWindow window = new DataGridEditWindowChoice(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();


        isNew = true;
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
                newDomain = domain;

                if (grid.getStore() == null) {
                    return;
                }

                if (grid.getSelectionModel() != null && grid.getSelectionModel().getSelectedItem() != null && (((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean() == domain)) {

                    grid.getStore().update(model);
                    reloadGrid();
                    grid.getSelectionModel().select(model, true);
                    setAllSubDomain(domain);
                } else {
                    grid.getStore().insert(model, grid.getStore().getCount());
                    reloadGrid();
                    grid.getSelectionModel().select(model, true);
                }
//                reloadGrid();

//                String str = "";
//                int i = 0;
//                Date var = (Date) domain.getRows().get(new SKeyForColumn("MAIN:BEG_DATE")).getVal();
//
//                str += i++ + ":" + var.getYear() + "." + var.getMonth() + "." + var.getDate() + ":"
//                        + domain.getRows().get(new SKeyForColumn("MAIN:ACTIVITY_SIGN")).getVal().toString() + ":"
//                        + domain.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal().toString() + "\n";
//                MessageBox.alert("onDomainSave", str, null);
                fireDataGridEvent(DataGridEvents.DataGridChanged, domain, table);
            }
        };
    }
}
