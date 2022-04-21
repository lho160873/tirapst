package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceOcp;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceOcpAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;
import pst.arm.client.modules.datagrid.widgets.DataGridWindowPanelWithHeader;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelOCPMain extends DataGridPanel {

    protected Button btnReportSub, btnOcpCopy;
    protected MenuItem subOcpCopy;
    protected Menu subReportMenu;
    private final GWTDDataGridServiceOcpAsync ocpService = GWT.create(GWTDDataGridServiceOcp.class); //сервис отвечающий за обработку данных

    public DataGridPanelOCPMain(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelOCPMain(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelOCPMain(String tableName) {
        super(tableName);
    }

    public DataGridPanelOCPMain(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelOCPMain(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
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

        btnOcpCopy = new Button("Добавить копированием");
        btnOcpCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        btnOcpCopy.setIconAlign(Style.IconAlign.LEFT);
        btnOcpCopy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onOcpCopy();
            }
        });
        subOcpCopy = new MenuItem("Добавить копированием");
        subOcpCopy.setIcon(AbstractImagePrototype.create(commonImages.add()));
        subOcpCopy.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onOcpCopy();
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

        btnDelegate = new Button(datagridConstants.btnDelegate());
        btnDelegate.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnDelegate.setIconAlign(Style.IconAlign.LEFT);
        btnDelegate.setEnabled(true);
        btnDelegate.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onDelegate();
            }
        });
        menuItemDelegate = new MenuItem(datagridConstants.btnDelegate());
        menuItemDelegate.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        menuItemDelegate.setEnabled(true);
        menuItemDelegate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onDelegate();
            }
        });

        tb.add(btnRefr);
        sprEdit = new SeparatorToolItem();

        //настройка пров доступа
        tb.add(sprEdit);
        tb.add(btnAdd);
        tb.add(btnOcpCopy);
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

        btnReportSub = new Button(datagridConstants.btnReportSub());//"Сформировать ОКП")
        btnReportSub.setIcon(AbstractImagePrototype.create(datagridImages.view()));
        btnReportSub.setIconAlign(Style.IconAlign.LEFT);
        //btnReportSub.setEnabled(false);
        //btnReportSub.setVisible(false);
        //btnReportSub.addSelectionListener(tbListener);
        subReportMenu = new Menu();

//        MenuItem editItem1 = new MenuItem(datagridConstants.mnuOcpFullForContractCost());//"ОКП для обоснования проекта договорной цены
//        editItem1.setId("0");
//        editItem1.addSelectionListener(new SelectionListener<MenuEvent>() {
//            @Override
//            public void componentSelected(MenuEvent ce) {
//                onReport("OcpFullForContractCost", "1");
//            }
//        });
//
//
//        MenuItem editItem11 = new MenuItem(datagridConstants.mnuOcpFullForContractCostExp());//"ОКП для обоснования проекта договорной цены (без согласования с НТО)");
//        editItem11.setId("1");
//        editItem11.addSelectionListener(new SelectionListener<MenuEvent>() {
//            @Override
//            public void componentSelected(MenuEvent ce) {
//                onReport("OcpFullForContractCostExp", "2");
//            }
//        });

        MenuItem editItem2 = new MenuItem(datagridConstants.mnuOcpFullForPlanOrg());//"ОКП для включения работы в План предприятия");
        editItem2.setId("2");
        editItem2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onReport("OcpFullForPlanOrg", "3");
            }
        });


        MenuItem editItem3 = new MenuItem(datagridConstants.mnuOcpFullForMeansOrg());//"ОКП работы, финансируемой за счет средств предприятия");
        editItem3.setId("3");
        editItem3.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onReport("OcpFullForMeansOrg", "4");
            }
        });

        subReportMenu.insert(editItem3, 0);
        subReportMenu.insert(editItem2, 0);
//        subReportMenu.insert(editItem11, 0);
//        subReportMenu.insert(editItem1, 0);



        btnReportSub.setMenu(subReportMenu);
        tb.add(btnReportSub);

        tb.add(btnDelegate);

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
        menu.add(subOcpCopy);
        menu.add(menuItemEdit);
        menu.add(menuItemDelete);
        menu.add(menuItemView);
        sprMenuItemReport = new SeparatorMenuItem();
        //menu.add(sprMenuItemReport);
        //menu.add(menuItemReport);
        menu.add(menuItemDelegate);


        enabledBtn();

        return tb;
    }

    private void onOcpCopy() {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        final Integer ocpId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal();
        final Integer userId = ConfigurationManager.getUserId().intValue();

        MessageBox.confirm(commonConstants.confirm(), "Создать запись на основе копирования?", new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    panelGrid.mask();
                    ocpService.copyOcp(ocpId, userId, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            panelGrid.unmask();
                            AppHelper.showMsgRpcServiceError(caught);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            panelGrid.unmask();
                            if (result) {
                                reloadGrid();
                                MessageBox.alert("", "Запись успешно скопирована", null);
                            } else {
                                MessageBox.alert("", "Ошибка копирования записи", null);
                            }
                        }
                    });
                }
            }
        });
    }

//    protected void onReport(String reportTemplate, String fileNumber) {
//        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
//        if (domain == null) {
//            MessageBox.info("Внимание!", "План не выбран!", null);
//            return;
//        }
//        if (domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal() == null) {
//            MessageBox.info("Внимание!", "План не выбран!", null);
//            return;
//        }
//        Integer ocpId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal()); 
//        //String reportExportFormat = "doc";
//        String reportExportFormat = "pdf";
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("ocpId", ocpId);
//        params.put("fileNumber", fileNumber);
//        String result = "";
//        for (String str : params.keySet()) {   
//            result += str + ":" + params.get(str) + "\n";
//        }
//            MessageBox.alert("TEST", result, null);    
//           
//        getReportService().generateJasperReport(reportTemplate, reportExportFormat, params, condition, table, new FileReportServiceCallback(this));
//    }
    protected void onReport(String reportTemplate, String fileNumber) {
        final DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();
        if (domain == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }
        if (domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal() == null) {
            MessageBox.info("Внимание!", "План не выбран!", null);
            return;
        }
        Integer ocpId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal());
        Integer cmpId = -1;
        if (domain.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")) != null) {
            cmpId = (Integer) (domain.getRows().get(new SKeyForColumn("MAIN:COMPANY_ID")).getVal());            
        }
        String workName = "";
        if (domain.getRows().get(new SKeyForColumn("MAIN:WORK_NAME")) != null) {
            workName = (String) (domain.getRows().get(new SKeyForColumn("MAIN:WORK_NAME")).getVal());           
        }
        
        //String reportExportFormat = "doc";
        final String reportExportFormat = "PDF";
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("ocpId", ocpId.toString());
        params.put("fileNumber", fileNumber);
        params.put("cmpId", cmpId.toString());
        params.put("workName", workName);


//        String result = "";
//        for (String str : params.keySet()) {
//            result += str + ":" + params.get(str) + "\n";
//        }
//        MessageBox.alert("TEST", result, null);

//        getReportService().generateJasperReport(reportTemplate, reportExportFormat, params, condition, table, new FileReportServiceCallback(this));


        final Component cmp = this;

        getService().getDataGrid(tableName, condition, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                getReportService().generateHtmlArchiveStoreEntityReportWithConnection(tableName, params, reportExportFormat, new FileReportServiceCallback(cmp));
            }
        });

    }

    @Override
    protected void onEdit() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (!checkUserId(domain)) {
            MessageBox.alert("Внимание!", "Реквизиты данного ОКП может корректировать только "
                    + domain.getRows().get(new SKeyForColumn("USERS:NAME"))
                    .getStringFromVal(new SKeyForColumn("USERS:NAME"), table.getColumnBuilder(new SKeyForColumn("USERS:NAME"))), null);
        } else {
            DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
            window.addDomainSaveSuccesedListener(saveDataGridListener);
            window.show();
        }
    }

    private boolean checkUserId(DDataGrid d) {
        String uid = ConfigurationManager.getUserId().toString();
        String cuid = d.getRows().get(new SKeyForColumn("MAIN:USER_ID"))
                .getStringFromVal(new SKeyForColumn("MAIN:USER_ID"), table.getColumnBuilder(new SKeyForColumn("MAIN:USER_ID")));

        return uid.equals(cuid);
    }

    @Override
    protected void onDelete() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        if (!checkUserId(domain)) {
            MessageBox.alert("Внимание!", "Реквизиты данного ОКП может корректировать только "
                    + domain.getRows().get(new SKeyForColumn("USERS:NAME"))
                    .getStringFromVal(new SKeyForColumn("USERS:NAME"), table.getColumnBuilder(new SKeyForColumn("USERS:NAME"))), null);
        } else {

            MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                        final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                        List<DDataGrid> domains = new ArrayList<DDataGrid>();
                        for (final BeanModel beanModel : selection) {
                            DDataGrid domain = beanModel.getBean();
                            domains.add(domain);
                        }
                        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                //MessageBox.alert(commonConstants.error(), datagridMessages.errorDelete(), null).show();
                                AppHelper.showMsgRpcServiceError(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                //reloadGrid();
                                for (final BeanModel beanModel : selection) {
                                    grid.getStore().remove(beanModel);
                                }
                                selectCurrentRow();
                            }
                        };
                        getService().deleteDomains(tableName, domains, callback);
                    }
                }
            });
        }
    }

    /**
     *
     * @author Igor
     */
    // Делегирование прав пользователя
    protected void onDelegate() {
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

        SKeyForColumn keyUserId = new SKeyForColumn("MAIN:USER_ID");

        // Проверяем текущего пользователя на совпадение с пользователем, создавшим запись
        if (String.valueOf(ConfigurationManager.getUserId())
                .equals(String.valueOf(domain.getRows().get(keyUserId).getVal()))) {

            // Если совпадает, то открываем окно делегирования прав, передавая OCP_ID
            DataGridPanel newWindow = (DataGridPanel) DataGridPanelBuilder.createDataGridPanel("OCP_RIGHTS_FOR_REC_IG", new SRelationInfo());
            IRowColumnVal v = new DRowColumnValNumber();
            v.setVal(domain.getRows().get(new SKeyForColumn("MAIN:OCP_ID")).getVal());
            newWindow.getCondition().getFilters().put(new SKeyForColumn("MAIN:OCP_ID"), v);
            DataGridWindowPanelWithHeader window = new DataGridWindowPanelWithHeader(newWindow, "Разрешение на редактирование данного ОКП");
            window.show();
        } else {
            SKeyForColumn keyName = new SKeyForColumn("USERS:NAME");
            MessageBox messageBox = new MessageBox();
            messageBox.setMinWidth(500);
//            messageBox.setTitle(constants.incorrectData());
            messageBox.setMessage("Разрешение на редактирование данного ОКП может предоставить только " + domain.getRows().get(keyName).getVal().toString());
            messageBox.setIcon(MessageBox.INFO);
            messageBox.setType(MessageBox.MessageBoxType.ALERT);
            messageBox.show();
        }
    }
}
