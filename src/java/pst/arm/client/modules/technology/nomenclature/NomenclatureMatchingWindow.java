package pst.arm.client.modules.technology.nomenclature;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.modules.technology.nomenclature.events.NomenclatureMatchingListener;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;
import pst.arm.client.modules.technology.nomenclature.service.remote.GWTNomenclatureService;
import pst.arm.client.modules.technology.nomenclature.service.remote.GWTNomenclatureServiceAsync;
import pst.arm.client.modules.technology.nomenclature.ui.FilterField;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
class NomenclatureMatchingWindow extends Window {
    private static final String LOADING_MASK_CSS = "x-mask-loading";
    protected final CommonImages images = GWT.create(CommonImages.class);
    private GWTNomenclatureServiceAsync service = GWT.create(GWTNomenclatureService.class);
    private TabPanel panel;
    private Button btnMatch;

    private final Nomenclature nomenclatureSearch;
    private final Map<TabItem, Grid<BeanModel>> tabGridMap;
    private NomenclatureMatchingListener listener;
    private ContentPanel rootPanel;
    private LayoutContainer lc;
    private ToolBar toolBar;

    NomenclatureMatchingWindow(Nomenclature nomenclature) {
        this.nomenclatureSearch = nomenclature;
        this.tabGridMap = new HashMap<TabItem, Grid<BeanModel>>();
        setHeading("Отождествление номенклатуры");
        setModal(true);
        setSize(800, 600);
        setMinHeight(600);
        initUI();
    }

    private void initUI() {
        toolBar = new ToolBar();
        toolBar.add(new Button("Привязать", AbstractImagePrototype.create(images.sync()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                matchSelectedNomenclature();
            }
        }));
        toolBar.add(new FillToolItem());
        FilterField filterField = new FilterField() {

            @Override
            public void changeFilterValue(String value) {
                tabGridMap.get(panel.getSelectedItem()).getStore().filter("name", value);
            }
        };
        filterField.setWidth(300);
        filterField.setEmptyText("Поиск...");
        toolBar.add(filterField);
        panel = new TabPanel();
        panel.setHeight(500);
        panel.setPlain(true);
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setAnimScroll(true);
        panel.setTabScroll(true);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        LayoutContainer lc = new LayoutContainer(new RowLayout(Style.Orientation.VERTICAL));
        lc.add(toolBar, new RowData(1, -1));
        lc.add(panel, new RowData(1, 1));
        setLayout(new FitLayout());
        add(lc);        
        getData();
    }

    private void getData() {
        panel.mask("Загрузка ...",LOADING_MASK_CSS);
        service.getNomenclatures(new NomenclatureSearchCondition(nomenclatureSearch.getName()), new AsyncCallback<Map<Integer, List<Nomenclature>>>() {

            @Override
            public void onFailure(Throwable caught) {
                panel.unmask();
                MessageBox.alert("Ошибка", "Ошибка поиска номенклатур для привязки", null);
            }

            @Override
            public void onSuccess(Map<Integer, List<Nomenclature>> result) {
                panel.unmask();
                createTabs(result);
            }

        });
    }

    private void matchSelectedNomenclature() {
        Grid<BeanModel> grid = tabGridMap.get(panel.getSelectedItem());
        final BeanModel selectedItem = grid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            service.matchNomenclatures(nomenclatureSearch, (Nomenclature) selectedItem.getBean(), new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("Ошибка", "Ошибка привязки номенклатуры", null);
                }

                @Override
                public void onSuccess(Void result) {
                    if (listener != null) {
                        listener.onNomenclatureMatched((Nomenclature) selectedItem.getBean());
                    }
                    hide();
                }
            });
        }

    }

    private void createTabs(Map<Integer, List<Nomenclature>> result) {
        for (Map.Entry<Integer, List<Nomenclature>> entry : result.entrySet()) {
            panel.add(getNewTab(entry.getKey(), entry.getValue()));
        }
        if (panel.getItems().size() > 0) {
            panel.setSelection(panel.getItems().get(0));
        }
        layout(true);
    }

    private TabItem getNewTab(Integer n, List<Nomenclature> nomenclatures) {
        TabItem tab = new TabItem("Совпадение " + n + " позиций");
        tab.setLayout(new FitLayout());
        List<ColumnConfig> columns = getGridColumns();
        Grid<BeanModel> grid = new Grid<BeanModel>(new ListStore<BeanModel>(), new ColumnModel(columns));
        grid.setHeight("100%");
        grid.getView().setAutoFill(true);
        grid.setBorders(false);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("name");
        grid.getView().setForceFit(true);
        grid.getView().setAutoFill(true);
        tab.add(grid);
        grid.getStore().add(BeanModelLookup.get().getFactory(Nomenclature.class).createModel(nomenclatures));

        tabGridMap.put(tab, grid);
        return tab;
    }

    private List<ColumnConfig> getGridColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        ColumnConfig columnConfig = new ColumnConfig("name", "Название", 400);
        columnConfig.setMenuDisabled(true);
        columnConfig.setSortable(true);
        columnConfig.setAlignment(HorizontalAlignment.LEFT);
        columnConfigs.add(columnConfig);
        return columnConfigs;
    }

    /**
     * @return the listener
     */
    public NomenclatureMatchingListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(NomenclatureMatchingListener listener) {
        this.listener = listener;
    }
}
