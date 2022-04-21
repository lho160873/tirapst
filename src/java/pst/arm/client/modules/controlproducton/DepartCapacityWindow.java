package pst.arm.client.modules.controlproducton;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.modules.controlproducton.domain.CompanyCapacity;
import pst.arm.client.modules.controlproducton.domain.DepartCapacity;
import pst.arm.client.modules.controlproducton.domain.ECompany;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class DepartCapacityWindow extends Window {

    public static final Double CAPACITY_MIN = 0.9;
    public static final Double CAPACITY_MAX = 1.1;
    final NumberFormat number = NumberFormat.getFormat("######0.00");
    final NumberFormat percent = NumberFormat.getPercentFormat();
    private final CompanyCapacity companyCapacity;
    private Grid<BeanModel> grid;
    private ControlProductionPage cpPage;
    protected ToolBar toolbar;
    protected CommonImages images = GWT.create(CommonImages.class);
    protected CommonConstants constants = GWT.create(CommonConstants.class);

    DepartCapacityWindow(CompanyCapacity companyCapacity,ControlProductionPage cpPage) {
        this.companyCapacity = companyCapacity;
        this.cpPage = cpPage;
        setHeading("Текущая загруженность " + companyCapacity.getCompany().getName());
        setModal(true);
        setSize(680, 580);
        initUI();
    }



    private void initUI() {

        List<ColumnConfig> columns = getGridColumns();
        grid = new Grid<BeanModel>(new ListStore<BeanModel>(), new ColumnModel(columns));
        grid.setHeight("100%");
        grid.getView().setAutoFill(true);
        grid.setBorders(false);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("name");
        grid.getView().setForceFit(true);
        grid.getView().setAutoFill(true);

        grid.getStore().removeAll();
        grid.getStore().add(BeanModelLookup.get().getFactory(DepartCapacity.class).createModel(companyCapacity.getDepartmentsCapacityList()));
        
        
        Button addRefr = new Button(constants.reset());
        addRefr.setIcon(AbstractImagePrototype.create(images.reset()));
        addRefr.setIconAlign(IconAlign.LEFT);
        addRefr.setScale(ButtonScale.SMALL);       
        
        addRefr.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                cpPage.initData();
                ECompany company = companyCapacity.getCompany();
                grid.getStore().removeAll();
                grid.getStore().add(BeanModelLookup.get().getFactory(DepartCapacity.class).createModel(cpPage.getCompanyCapacityMap().get(company).getDepartmentsCapacityList()));
            }
        });

        toolbar = new ToolBar();
        toolbar.add(addRefr);
        
        setLayout(new FitLayout());
        setTopComponent(toolbar);        
        add(grid);
    }

    private List<ColumnConfig> getGridColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig colCfg = new ColumnConfig("departCode", "Код подразделения", 200);
        colCfg.setMenuDisabled(true);
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig();
        colCfg.setHeader("Состояние");
        colCfg.setWidth(100);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DepartCapacity departCapacity = (DepartCapacity) model.getBean();
                String color = "green";
                String tip = "Нагрузка в норме";
                if (departCapacity.getCapacity() > CAPACITY_MAX) {
                    color = "red";
                    tip = "Подразделение перегружено";
                } else if (departCapacity.getCapacity() < CAPACITY_MIN) {
                    color = "yellow";
                    tip = "Подразделение недогружено";
                }

                return "<span " + "title='" + tip + "'" + ">" + "<div style='color: " + color + "; text-align: center; font-size: 14pt'> &#9679; </div></span>";
            }
        });
        colCfg.setMenuDisabled(true);
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig();
        colCfg.setHeader("Загруженность, %");
        colCfg.setWidth(200);
        colCfg.setMenuDisabled(true);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DepartCapacity departCapacity = (DepartCapacity) model.getBean();
                return percent.format(departCapacity.getCapacity());
            }
        });
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("plan", "План, чел/мес.", 200);
        colCfg.setMenuDisabled(true);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DepartCapacity departCapacity = (DepartCapacity) model.getBean();
                return number.format(departCapacity.getPlan());
            }
        });
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("additional", "Дополнение, чел/мес.", 200);
        colCfg.setMenuDisabled(true);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DepartCapacity departCapacity = (DepartCapacity) model.getBean();
                final NumberFormat number = NumberFormat.getFormat("0.00");
                return number.format(departCapacity.getAdditional());
            }
        });
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("monthResource", "Месячный ресурс, чел/мес.", 200);
        colCfg.setMenuDisabled(true);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DepartCapacity departCapacity = (DepartCapacity) model.getBean();
                final NumberFormat number = NumberFormat.getFormat("0.00");
                return number.format(departCapacity.getMonthResource());
            }
        });
        columnConfigs.add(colCfg);

        return columnConfigs;

    }
   
}
