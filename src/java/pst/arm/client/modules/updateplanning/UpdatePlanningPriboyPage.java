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
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
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
public class UpdatePlanningPriboyPage extends BasePageNew {

    private GWTDUpdatePlanningServiceAsync service = GWT.create(GWTDUpdatePlanningService.class); //сервис отвечающий за обработку данных
    private Button btnUpdatePlanningPriboy;
    private Button btnUpdateDesignPriboy;
    private Button btnUpdateDesignVerPriboy;
    private Button btnAddDesignPriboy;
    private Button btnAddDesignVerPriboy;
    public static final Integer PRIBOY_INTERACTING_SYST_ID = 4;
    public static final String DATASOURCE_PRIBOY = "dataSourcePriboy";

    @Override
    protected LayoutContainer getContentPanel() {
        LayoutContainer main = new LayoutContainer(new FitLayout());//new BorderLayout());
        main.setBorders(false);

        ContentPanel panel = new ContentPanel(new BorderLayout());
        panel.setHeading("Обновление справочников для планирования - Прибой");
        main.add(panel);

        LayoutContainer container = new LayoutContainer();
        container.setBorders(false);
        container.setStyleAttribute("padding", "8px");

        VBoxLayout vLayout = new VBoxLayout();
        vLayout.setPadding(new Padding(8));
        vLayout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
        container.setLayout(vLayout);

        BorderLayoutData borderData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        borderData.setMargins(new Margins(8));
        panel.add(container, borderData);

        btnUpdatePlanningPriboy = new Button();
        btnUpdatePlanningPriboy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updatePlanningSprDepart(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Состояние обновления данных о подразделениях", "Ошибка выполнения " + caught.getMessage(), null);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        MessageBox.alert("Состояние обновления данных о подразделениях", "Обновление данных о подразделениях завершено", null);
                    }
                });
                service.updatePlanningSprOperkind(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
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

        btnUpdatePlanningPriboy.setSize(400, 50);
        btnUpdatePlanningPriboy.setText("Обновить данные о подразделениях и видах операций");

        btnUpdateDesignPriboy = new Button();
        btnUpdateDesignPriboy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updateDesign(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
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
        btnUpdateDesignPriboy.setSize(400, 50);
        btnUpdateDesignPriboy.setText("Полностью обновить данные о чертежах");

        btnUpdateDesignVerPriboy = new Button();
        btnUpdateDesignVerPriboy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.updateDesignVer(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
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
        btnUpdateDesignVerPriboy.setSize(400, 50);
        btnUpdateDesignVerPriboy.setText("Полностью обновить данные о версиях чертежей");

        btnAddDesignPriboy = new Button();
        btnAddDesignPriboy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.addDesign(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
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
        btnAddDesignPriboy.setSize(400, 50);
        btnAddDesignPriboy.setText("Добавить новые записи о чертежах");

        btnAddDesignVerPriboy = new Button();
        btnAddDesignVerPriboy.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                service.addDesignVer(PRIBOY_INTERACTING_SYST_ID, DATASOURCE_PRIBOY, new AsyncCallback() {
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
        btnAddDesignVerPriboy.setSize(400, 50);
        btnAddDesignVerPriboy.setText("Добавить новые записи о версиях чертежей");

        container.add(btnUpdatePlanningPriboy, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnUpdateDesignPriboy, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnUpdateDesignVerPriboy, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnAddDesignPriboy, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        container.add(btnAddDesignVerPriboy, new VBoxLayoutData(new Margins(0, 0, 20, 0)));

        return main;
    }
}
