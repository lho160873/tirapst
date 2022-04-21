/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.updateplanning;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDUpdatePlanningService;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDUpdatePlanningServiceAsync;

/**
 *
 * @author Igor
 */
public class UpdatePlanningMartPage extends BasePageNew {

    private GWTDUpdatePlanningServiceAsync service = GWT.create(GWTDUpdatePlanningService.class); //сервис отвечающий за обработку данных
    private Button btnUpdatePlanningMart;
    private Button btnUpdateDesignMart;
    private Button btnUpdateDesignVerMart;
    private Button btnAddDesignMart;
    private Button btnAddDesignVerMart;
    public static final Integer MART_INTERACTING_SYST_ID = 5;
    public static final String DATASOURCE_MART = "dataSourceMart";

    @Override
    protected LayoutContainer getContentPanel() {
        LayoutContainer main = new LayoutContainer(new FitLayout());//new BorderLayout());
        main.setBorders(false);

        ContentPanel panel = new ContentPanel(new BorderLayout());
        panel.setHeading("Обновление справочников для планирования - МАРТ");
        main.add(panel);

        LayoutContainer container = new LayoutContainer();
        container.setBorders(false);
        container.setStyleAttribute("padding", "8px");

        VBoxLayout vLayout = new VBoxLayout();
        vLayout.setPadding(new Padding(8));
        vLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.CENTER);
        container.setLayout(vLayout);

        BorderLayoutData borderData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        borderData.setMargins(new Margins(8));
        panel.add(container, borderData);

        btnUpdatePlanningMart = new Button();
        btnUpdatePlanningMart.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updatePlanningSprDepart(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние обновления данных о подразделениях", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние обновления данных о подразделениях", "Обновление данных о подразделениях завершено", null);
                    }
                });
                service.updatePlanningSprOperkind(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние обновления данных об операциях", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние обновления данных об операциях", "Обновление данных об операциях завершено", null);
                    }
                });
            }
        });
        btnUpdatePlanningMart.setSize(400, 50);
        btnUpdatePlanningMart.setText("Обновить данные о подразделениях и видах операций");

        btnUpdateDesignMart = new Button();
        btnUpdateDesignMart.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updateDesign(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние обновления данных о чертежах", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние обновления данных о чертежах", "Обновление данных о чертежах завершено", null);
                    }
                });
            }
        });
        btnUpdateDesignMart.setSize(400, 50);
        btnUpdateDesignMart.setText("Полностью обновить данные о чертежах");

        btnUpdateDesignVerMart = new Button();
        btnUpdateDesignVerMart.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updateDesignVer(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние обновления данных о версиях чертежей", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние обновления данных о версиях чертежей", "Обновление данных о версиях чертежей завершено", null);
                    }
                });
            }
        });
        btnUpdateDesignVerMart.setSize(400, 50);
        btnUpdateDesignVerMart.setText("Полностью обновить данные о версиях чертежей");

        btnAddDesignMart = new Button();
        btnAddDesignMart.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.addDesign(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние добавления данных о чертежах", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние добавления данных о чертежах", "Добавление данных о чертежах завершено", null);
                    }
                });
            }
        });
        btnAddDesignMart.setSize(400, 50);
        btnAddDesignMart.setText("Добавить новые записи о чертежах");

        btnAddDesignVerMart = new Button();
        btnAddDesignVerMart.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.addDesignVer(MART_INTERACTING_SYST_ID, DATASOURCE_MART, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние добавления данных о версиях чертежей", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние добавления данных о версиях чертежей", "Добавление данных о версиях чертежей завершено", null);
                    }
                });
            }
        });
        btnAddDesignVerMart.setSize(400, 50);
        btnAddDesignVerMart.setText("Добавить новые записи о версиях чертежей");

        container.add(btnUpdatePlanningMart, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnUpdateDesignMart, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnUpdateDesignVerMart, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnAddDesignMart, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnAddDesignVerMart, new VBoxLayoutData(new Margins(0, 0, 20, 0)));

        return main;
    }
}
