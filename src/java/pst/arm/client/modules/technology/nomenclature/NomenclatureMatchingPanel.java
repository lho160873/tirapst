package pst.arm.client.modules.technology.nomenclature;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.technology.nomenclature.events.NomenclatureMatchingListener;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.ui.FilterField;

/**
 * @author Alexandr Kozhin
 */
public class NomenclatureMatchingPanel extends LayoutContainer implements NomenclatureMatchingListener {

    protected final CommonImages images = GWT.create(CommonImages.class);
    protected BaseConstants baseConstants = (BaseConstants) GWT.create(BaseConstants.class);
    private FormPanel filesUploadPanel;
    private FileUploadField fileXml;
    private Button btnAttach;
    private Grid<BeanModel> searchGrid;
    private final NomenclatureMatchingListener matchingListener;
    private ContentPanel gridContentPanel;

    public NomenclatureMatchingPanel() {
        matchingListener = this;
        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setBorders(false);
        initUploadPanel();
        initNomenclatureSearchGrid();
        add(filesUploadPanel, new BorderLayoutData(Style.LayoutRegion.NORTH, 110));
        add(gridContentPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void unmaskPanel() {
        unmask();
    }

    private void maskPanel() {
        mask("Загрузка ...", "x-mask-loading");
    }

    private void parseResponseAndAddFiles(FormEvent be) {
        JSONValue value = JSONParser.parseStrict(be.getResultHtml());
        JSONObject responseJSONObject = value.isObject();
        JSONArray filesJSONArray = responseJSONObject.get("nomenclatures").isArray();

        if (filesJSONArray != null) {
            searchGrid.getStore().removeAll();
            for (int i = 0; i < filesJSONArray.size(); i++) {
                JSONObject nomenclatureJSONObj = filesJSONArray.get(i).isObject();
                searchGrid.getStore().add(BeanModelLookup.get().getFactory(Nomenclature.class).createModel(
                        new Nomenclature(nomenclatureJSONObj.get("id").isString().stringValue(),
                                nomenclatureJSONObj.get("name").isString().stringValue())));
            }
        }
    }

    private List<ColumnConfig> getGridColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig colCfg = new ColumnConfig("id", "Код", 200);
        colCfg.setFixed(true);
        colCfg.setResizable(false);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);

        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("name", "Название", 600);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);
        columnConfigs.add(colCfg);

        return columnConfigs;

    }

    @Override
    public void onNomenclatureMatched(Nomenclature n) {
        searchGrid.mask();
        BeanModel selectedItem = searchGrid.getSelectionModel().getSelectedItem();
        Nomenclature nomenclature = (Nomenclature) selectedItem.getBean();
        String info = nomenclature.getName();
        searchGrid.getStore().remove(selectedItem);
        searchGrid.unmask();
        Info.display("Отождествление номенклатуры", "Позиция '" + info + "' успешно привязана.");
    }

    private void initUploadPanel() {

        filesUploadPanel = new FormPanel();
        filesUploadPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                unmaskPanel();
                parseResponseAndAddFiles(be);
                fileXml.clear();
            }
        });
        filesUploadPanel.setHeading("Отождествление номенклатуры");
        filesUploadPanel.setBodyBorder(false);
        filesUploadPanel.setFrame(true);
        filesUploadPanel.setLabelAlign(FormPanel.LabelAlign.LEFT);
        filesUploadPanel.setLabelWidth(80);
        filesUploadPanel.setAction(GWT.getHostPageBaseURL() + "nomenclatureupload.htm");
        filesUploadPanel.setMethod(FormPanel.Method.POST);
        filesUploadPanel.setEncoding(FormPanel.Encoding.MULTIPART);
        filesUploadPanel.setButtonAlign(Style.HorizontalAlignment.LEFT);
        fileXml = new FileUploadField();
        fileXml.setWidth(400);
        fileXml.getMessages().setBrowseText("Обзор");
        fileXml.setName("fileXml");
        fileXml.setId("fileXml");
        fileXml.setFieldLabel("XML файл");
        fileXml.setAllowBlank(false);
        fileXml.setRegex(".*\\.[xX][mM][lL]");
        fileXml.getMessages().setRegexText("неверный формат файла");
        fileXml.setAutoValidate(true);

        btnAttach = new Button("Загрузить");
        btnAttach.setWidth(100);
        btnAttach.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (fileXml.validate()) {
                    filesUploadPanel.submit();
                    maskPanel();
                }
            }
        });
        filesUploadPanel.setWidth(500);
        filesUploadPanel.add(fileXml);
        filesUploadPanel.addButton(btnAttach);
    }

    private void initNomenclatureSearchGrid() {
        gridContentPanel = new ContentPanel();
        gridContentPanel.setLayout(new FitLayout());
        gridContentPanel.setHeaderVisible(false);

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button("Привязать", AbstractImagePrototype.create(images.sync()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                matchNomenclature();
            }
        }));
        toolBar.add(new FillToolItem());
        FilterField filterField = new FilterField() {

            @Override
            public void changeFilterValue(final String value) {
                searchGrid.getStore().filter("name", value);                
//                searchGrid.getStore().addFilter(new StoreFilter<BeanModel>() {
//
//                    @Override
//                    public boolean select(Store<BeanModel> store, BeanModel parent, BeanModel item, String property) {
//                        Nomenclature bean = (Nomenclature) item.getBean();
//                        return bean.getId().contains(value)
//                                || bean.getName().contains(value);
//                    }
//                });
            }

        };
        filterField.setWidth(300);
        filterField.setEmptyText("Поиск...");
        toolBar.add(filterField);

        searchGrid = new Grid<BeanModel>(new ListStore<BeanModel>(), new ColumnModel(getGridColumns()));
        searchGrid.setHeight("100%");
        searchGrid.getView().setAutoFill(true);
        searchGrid.setBorders(false);
        searchGrid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        searchGrid.setStripeRows(true);
        searchGrid.setAutoExpandColumn("name");
        searchGrid.enableEvents(true);
        searchGrid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {

            @Override
            public void handleEvent(GridEvent<BeanModel> be) {
                matchNomenclature();
            }
        });
        gridContentPanel.setTopComponent(toolBar);
        gridContentPanel.add(searchGrid, new FitData(new Margins(0)));
    }

    private void matchNomenclature() {
        BeanModel selectedItem = searchGrid.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            NomenclatureMatchingWindow w = new NomenclatureMatchingWindow((Nomenclature) selectedItem.getBean());
            w.setListener(matchingListener);
            w.show();
        }
    }

    private GridCellRenderer getMatchRenderer() {
        GridCellRenderer<BeanModel> buttonRenderer = new GridCellRenderer<BeanModel>() {
            private boolean init;

            @Override
            public Object render(final BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<BeanModel> store, Grid<BeanModel> grid) {

                if (!init) {
                    init = true;
                    grid.addListener(Events.ColumnResize, new Listener<GridEvent<BeanModel>>() {

                        public void handleEvent(GridEvent<BeanModel> be) {
                            for (int i = 0; i < be.getGrid().getStore().getCount(); i++) {
                                if (be.getGrid().getView().getWidget(i, be.getColIndex()) != null
                                        && be.getGrid().getView().getWidget(i, be.getColIndex()) instanceof BoxComponent) {
                                    ((BoxComponent) be.getGrid().getView().getWidget(i, be.getColIndex())).setWidth(be.getWidth() - 10);
                                }
                            }
                        }
                    });
                }

                Button b = new Button("Привязать", new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        NomenclatureMatchingWindow w = new NomenclatureMatchingWindow((Nomenclature) model.getBean());
                        w.setListener(matchingListener);
                        w.show();
                    }
                });
                b.setWidth(grid.getColumnModel().getColumnWidth(colIndex) - 10);
                return b;
            }
        };
        return buttonRenderer;
    }

}
/*
 tfFilter = new TextField<String>();
 btn = new Button("Фильтровать", new SelectionListener<ButtonEvent>() {

 @Override
 public void componentSelected(ButtonEvent ce) {
 searchGrid.getStore().filter("name", tfFilter.getValue());
 searchGrid.getStore().addFilter(new StoreFilter<BeanModel>() {

 @Override
 public boolean select(Store<BeanModel> store, BeanModel parent, BeanModel item, String property) {
 Nomenclature bean = (Nomenclature) item.getBean();
 return bean.getId().contains(tfFilter.getValue())
 || bean.getName().contains(tfFilter.getValue());
 }
 });
 }
 });

 */
