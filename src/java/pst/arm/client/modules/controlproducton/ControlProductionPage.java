package pst.arm.client.modules.controlproducton;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.controlproducton.domain.CompanyCapacity;
import pst.arm.client.modules.controlproducton.domain.ECompany;
import pst.arm.client.modules.controlproducton.service.remote.GWTDepartCapacityService;
import pst.arm.client.modules.controlproducton.service.remote.GWTDepartCapacityServiceAsync;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.expansion.DataGridPanelOfficeDocContrIgControl;

/**
 *
 * @author LKHorosheva, Igor
 * @since 0.0.1
 */
public class ControlProductionPage extends BasePageNew {

    protected CommonImages images = GWT.create(CommonImages.class);

    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    private GWTDepartCapacityServiceAsync capacityService = GWT.create(GWTDepartCapacityService.class);

    private Map<ECompany, CompanyCapacity> companyCapacityMap;
    private Map<ECompany, Button> companyButtonMap = new HashMap<ECompany, Button>();
    private final NumberFormat percent = NumberFormat.getPercentFormat();

    Button btnOfficeDocContr;

    @Override
    protected LayoutContainer getContentPanel() {

        initData();

        LayoutContainer main = new LayoutContainer(new FitLayout());//new BorderLayout());
        main.setBorders(false);

        ContentPanel panel = new ContentPanel(new BorderLayout());
        panel.setHeading("Контроль");
        main.add(panel);

        ToolBar toolbar = new ToolBar();
        toolbar.setSpacing(6);
        //panel.setTopComponent(toolbar);

        Label lblDate = new Label("Дата:");
        lblDate.setAutoWidth(true);
        toolbar.add(lblDate);

        DateField fldDate = new DateField();
        fldDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
        fldDate.setValue(new Date());
         fldDate.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (value.length() > 6 && value.length() < 10) {
                    return value + " недопустимая дата - она должна быть в формате DD.MM.YYYY";
                }
                return null;
            }
        });
        toolbar.add(fldDate);

        Button btnReload = new Button("Обновить информацию", AbstractImagePrototype.create(images.reset()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                reload();
            }
        });
        toolbar.add(btnReload);

        LayoutContainer fieldSet = new LayoutContainer();
        fieldSet.setBorders(false);
        fieldSet.setStyleAttribute("padding", "8px");
        VBoxLayout vLayout = new VBoxLayout();
        vLayout.setPadding(new Padding(8));
        vLayout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
        fieldSet.setLayout(vLayout);

        BorderLayoutData borderData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        borderData.setMargins(new Margins(8));
        panel.add(fieldSet, borderData);

        btnOfficeDocContr = new Button();
        btnOfficeDocContr.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                //MessageBox.info("","Обработка находится в разработке",null);
                showOfficeDocContr();
            }
        });
        btnOfficeDocContr.setSize(800, 90);

        String str = createText("Количество невыполненных приказов и распоряжений", 0);
        btnOfficeDocContr.setText(str);

        if (ConfigurationManager.getPropertyAsBoolean("officeDocContr")) {
            fieldSet.add(btnOfficeDocContr, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
        }

        for (ECompany company : ECompany.values()) {
            Button btn = createCapacityButton(company);
            if (ConfigurationManager.getPropertyAsBoolean("capacity" + company.name())) {
                fieldSet.add(btn, new VBoxLayoutData(new Margins(0, 0, 20, 0)));
            }
        }
        reload();
        return main;
    }

    private void showOfficeDocContr() {
        final Window w = new Window();
        w.setLayout(new FitLayout());
        DataGridPanel dataGridPanel = new DataGridPanelOfficeDocContrIgControl("OFFICE_DOC_CONTR_IG_CONTROL");
        dataGridPanel.setHeaderVisible(false);
        w.setHeading("Невыполненные приказы и распоряжения");//constants.userWorkerField());
        w.setModal(true);
        w.setSize(680, 580);
        w.add(dataGridPanel);
        w.show();
        w.center();
    }

    private String createText(String caption, Integer val) {
        String color = val > 0 ? "red" : "green";
        return createText(caption, val, color);
    }

    private String createText(String caption, Integer val, String color) {
        String text;
        if (val > 0) {
            text = String.valueOf(val);
        } else {
            text = "нет";
        }
        return createText(caption, text, color);
    }

    private String createText(String caption, String text, String color) {

        String str = "<table  border=\"0\"  width=\"800\"  height=\"80\" bgcolor=\""
                + color
                + "\" align=\"center\">"
                + "<tr>"
                + "<td width=\"70%\" height=\"80\">"
                + "<b>"
                + "&nbsp&nbsp&nbsp&nbsp&nbsp" + caption
                + "</b>"
                + "</td>"
                + "<td align=\"center\" width=\"30%\" height=\"80\">"
                + "<b>"
                + text
                + "</b>"
                + "</td>"
                + "</tr> </table>";
        return str;
    }

    private void reload() {
        //получаем количество не выполненных приказов на выбранную дату
        service.getAllDataGrid("OFFICE_DOC_CONTR_HLV", new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(List<DDataGrid> listData) {
                Integer count = listData.size();
                String str = createText("Количество невыполненных приказов и распоряжений", count);
                btnOfficeDocContr.setText(str);

            }
        });

    }

    public void initData() {
        capacityService.getCompaniesCapacity(ECompany.values(), new AsyncCallback<Map<ECompany, CompanyCapacity>>() {

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Map<ECompany, CompanyCapacity> result) {
                companyCapacityMap = result;
                updateButtonState();
            }

            private void updateButtonState() {
                for (Map.Entry<ECompany, CompanyCapacity> companyCapacity : companyCapacityMap.entrySet()) {
                    ECompany company = companyCapacity.getKey();
                    Double capacity = companyCapacity.getValue().avg();

                    String color = "green";
                    if (capacity > DepartCapacityWindow.CAPACITY_MAX) {
                        color = "red";
                    } else if (capacity < DepartCapacityWindow.CAPACITY_MIN) {
                        color = "yellow";
                    }
                    String str = createText("Текущая загруженность " + company.getName(), percent.format(capacity), color);
                    companyButtonMap.get(company).setText(str);

                }

            }
        });
    }

    public void updCaption() {
        for (Map.Entry<ECompany, CompanyCapacity> companyCapacity : companyCapacityMap.entrySet()) {
            ECompany company = companyCapacity.getKey();
            Double capacity = companyCapacity.getValue().avg();

            String color = "green";
            if (capacity > DepartCapacityWindow.CAPACITY_MAX) {
                color = "red";
            } else if (capacity < DepartCapacityWindow.CAPACITY_MIN) {
                color = "yellow";
            }
            String str = createText("ТЕСТ Текущая загруженность " + company.getName(), percent.format(capacity), color);
            companyButtonMap.get(company).setText(str);

        }
    }
    
    private Button createCapacityButton(final ECompany company) {
        Button btn = new Button();
        final ControlProductionPage cpPage = this;
        btn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                initData();
                DepartCapacityWindow w = new DepartCapacityWindow(companyCapacityMap.get(company),cpPage);
                w.show();
            }
        });
        btn.setSize(800, 90);
        String str = createText("Текущая загруженность " + company.getName(), "-", "white");
        btn.setText(str);

        companyButtonMap.put(company, btn);
        return btn;
    }
    public Map<ECompany, CompanyCapacity> getCompanyCapacityMap()
            {
                return companyCapacityMap;
            }
}
