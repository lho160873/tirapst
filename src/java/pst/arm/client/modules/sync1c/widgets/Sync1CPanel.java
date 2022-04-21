package pst.arm.client.modules.sync1c.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.ui.grid.GridTools;
import pst.arm.client.modules.sync1c.domain.Object1C;
import pst.arm.client.modules.sync1c.domain.Sync1CResult;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class Sync1CPanel extends LayoutContainer {

    protected final CommonImages images = GWT.create(CommonImages.class);
    protected BaseConstants baseConstants = (BaseConstants) GWT.create(BaseConstants.class);
    private FormPanel filesUploadPanel;
    private FileUploadField fileXml;
    private Button btnAttach;
    private TextArea taResults;
    private TabPanel tabResults;
    private HiddenField<String> hfTypes;
    private HiddenField<Boolean> hfShowOnlyChanged;
    private Radio rbShowOnlyChanged;
    private CheckBox cbPosts;
    private CheckBox cbDepartments;
    private CheckBox cbWorkers;
    private HashMap<Object1C.ObjctType, Grid> gridMap = new HashMap<Object1C.ObjctType, Grid>();

    public Sync1CPanel() {
        initControls();
    }

    protected void initControls() {
        initUploadPanel();
        initResultPanel();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new BorderLayout());
        setBorders(false);
        add(filesUploadPanel, new BorderLayoutData(Style.LayoutRegion.NORTH));
        add(tabResults, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void initUploadPanel() {

        filesUploadPanel = new FormPanel();
        filesUploadPanel.setWidth(600);
        filesUploadPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                fileXml.clear();
                parseResponseAndAddResults(be);
                unmaskPanel();
            }

        });

        filesUploadPanel.setHeading(baseConstants.headerSync1c());
        filesUploadPanel.setBodyBorder(false);
        filesUploadPanel.setFrame(true);
        filesUploadPanel.setLabelAlign(FormPanel.LabelAlign.LEFT);
        filesUploadPanel.setLabelWidth(200);
        filesUploadPanel.setAction(GWT.getHostPageBaseURL() + "sync1cupload.htm");
        filesUploadPanel.setMethod(FormPanel.Method.POST);
        filesUploadPanel.setEncoding(FormPanel.Encoding.MULTIPART);
        filesUploadPanel.setButtonAlign(Style.HorizontalAlignment.LEFT);

        hfTypes = new HiddenField<String>();
        hfTypes.setName("types");

        hfShowOnlyChanged = new HiddenField<Boolean>();
        hfShowOnlyChanged.setName("showOnlyChanged");

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

        cbPosts = new CheckBox();
        cbPosts.setBoxLabel("Должности");
//        cbPosts.setValue(true);
        cbDepartments = new CheckBox();
        cbDepartments.setBoxLabel("Подразделения");
//        cbDepartments.setValue(true);
        cbWorkers = new CheckBox();
        cbWorkers.setBoxLabel("Сотрудники");
//        cbWorkers.setValue(true);
        CheckBoxGroup cbgParams = new CheckBoxGroup();
        cbgParams.setFieldLabel("Обрабатывать");
        cbgParams.add(cbPosts);
        cbgParams.add(cbDepartments);
        cbgParams.add(cbWorkers);

        rbShowOnlyChanged = new Radio();
        rbShowOnlyChanged.setBoxLabel("Да");

        Radio radio2 = new Radio();
        radio2.setBoxLabel("Нет");
        radio2.setValue(true);
        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setFieldLabel("Показывать только измененные");
        radioGroup.add(rbShowOnlyChanged);
        radioGroup.add(radio2);

        btnAttach = new Button("Загрузить");
        btnAttach.setWidth(100);
        btnAttach.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (formValid()) {
                    hfTypes.setValue(getTypes());
                    hfShowOnlyChanged.setValue(rbShowOnlyChanged.getValue());
                    filesUploadPanel.submit();
                    maskPanel();
                }
            }

            private boolean formValid() {
                return fileXml.validate()
                        && (cbPosts.getValue() || cbDepartments.getValue() || cbWorkers.getValue());
            }
        });
        filesUploadPanel.add(fileXml);
        filesUploadPanel.add(hfTypes);
        filesUploadPanel.add(hfShowOnlyChanged);
        filesUploadPanel.add(cbgParams);
        filesUploadPanel.add(radioGroup);
        filesUploadPanel.addButton(btnAttach);
    }

    private String getTypes() {
        String str = "";
        if (cbPosts.getValue()) {
            str += "posts;";
        }
        if (cbDepartments.getValue()) {
            str += "departments;";
        }
        if (cbWorkers.getValue()) {
            str += "workers;";
        }
        return str;
    }

    private void unmaskPanel() {
        unmask();
    }

    private void maskPanel() {
        mask("Загрузка ...", "x-mask-loading");
    }

    private void parseResponseAndAddResults(FormEvent be) {
        taResults.clear();
        for (Grid grid : gridMap.values()) {
            grid.getStore().removeAll();
        }
        taResults.setValue(be.getResultHtml());
        JSONValue value = JSONParser.parseStrict(be.getResultHtml());
        JSONObject responseJSONObject = value.isObject();
        for (Object1C.ObjctType type : Object1C.ObjctType.values()) {
            if (responseJSONObject.containsKey(type.name())) {
                JSONArray filesJSONArray = responseJSONObject.get(type.name()).isArray();
                if (filesJSONArray != null) {
                    for (int i = 0; i < filesJSONArray.size(); i++) {
                        JSONObject o = filesJSONArray.get(i).isObject();
                        Sync1CResult result = new Sync1CResult();
                        result.setStatus(Sync1CResult.SyncStatus.valueOf(o.get("status").isString().stringValue()));
                        result.setText(o.get("text").isString().stringValue());
                        if (result.getStatus().equals(Sync1CResult.SyncStatus.Error)) {
                            result.setError(o.get("error").isString().stringValue());
                        }
                        gridMap.get(type).getStore().add(BeanModelLookup.get().getFactory(Sync1CResult.class).createModel(result));
                    }
                }
            }
        }
    }

    private Grid<BeanModel> createGrid() {
        Grid<BeanModel> grid = new Grid<BeanModel>(new ListStore<BeanModel>(), new ColumnModel(getGridColumns()));
        GridTools.enableGridMultiColumn(grid);
        grid.setHeight("100%");
        grid.getView().setAutoFill(true);
        grid.setBorders(false);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("text");

        return grid;
    }

    private List<ColumnConfig> getGridColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig colCfg = new ColumnConfig("status", "Статус", 100);
        colCfg.setFixed(true);
        colCfg.setResizable(false);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);
        colCfg.setRenderer(new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                Sync1CResult result = (Sync1CResult) model.getBean();

                if (result.getStatus().equals(Sync1CResult.SyncStatus.Equals)) {
                    return "Существует";

                } else {
                    String color = "white", tip = "";

                    if (result.getStatus().equals(Sync1CResult.SyncStatus.Error)) {
                        color = "red";
                        tip = "Ошибка";
                    } else if (result.getStatus().equals(Sync1CResult.SyncStatus.Created)) {
                        color = "green";
                        tip = "Создание";
                    } else if (result.getStatus().equals(Sync1CResult.SyncStatus.Updated)) {
                        color = "blue";
                        tip = "Обновление";
                    }

                    return "<div style='background-color: " + color + ";'>" + tip + "</div>";
                }
            }
        });

        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("text", "Наименование", 300);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("error", "Ошибки", 200);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(false);
        columnConfigs.add(colCfg);

        return columnConfigs;

    }

    private void initResultPanel() {
        tabResults = new TabPanel();
        for (Object1C.ObjctType type : Object1C.ObjctType.values()) {
            TabItem tabItem = new TabItem(getTitle(type));
            tabItem.setLayout(new FitLayout());
            Grid<BeanModel> grid = createGrid();
            tabItem.add(grid);
            tabResults.add(tabItem);
            gridMap.put(type, grid);
        }

        TabItem tabItem = new TabItem("JSON");
        tabItem.setLayout(new FitLayout());
        taResults = new TextArea();
        tabItem.add(taResults);
        tabResults.add(tabItem);
    }

    private String getTitle(Object1C.ObjctType type) {
        switch (type) {
            case Department:
                return "Подразделения";

            case Post:
                return "Должности";

            case Worker:
                return "Сотрудники";
        }
        return "";
    }
}
