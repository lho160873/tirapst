package pst.arm.client.modules.technology.nomenclature.ui;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;
import pst.arm.client.modules.technology.nomenclature.service.remote.GWTNomenclatureService;
import pst.arm.client.modules.technology.nomenclature.service.remote.GWTNomenclatureServiceAsync;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class NomenclatureMatchingResults extends ContentPanel {

    protected final CommonImages images = GWT.create(CommonImages.class);
    protected BaseConstants baseConstants = (BaseConstants) GWT.create(BaseConstants.class);
    protected GWTNomenclatureServiceAsync service = GWT.create(GWTNomenclatureService.class);
    protected Grid<BeanModel> grid;
    protected Button btnRemove;
    private FilterField filterField;

    public NomenclatureMatchingResults() {
        initUI();
        initData(new NomenclatureSearchCondition());
    }

    private void initUI() {
        setLayout(new FitLayout());
        setHeading("Результат отождесталения");

        ToolBar toolBar = new ToolBar();
        toolBar.add(new Button("Удалить привязку", AbstractImagePrototype.create(images.delete()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                removeMatchedNomenclature();
            }
        }));
        toolBar.add(new Button("Обновить", AbstractImagePrototype.create(images.reset()), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                filterField.clear();
                initData(new NomenclatureSearchCondition());
            }
        }));
        toolBar.add(new FillToolItem());
        filterField = new FilterField() {

            @Override
            public void changeFilterValue(final String value) {
                initData(new NomenclatureSearchCondition(value));
            }

        };
        filterField.setWidth(300);
        filterField.setEmptyText("Поиск...");
        toolBar.add(filterField);

        grid = new Grid<BeanModel>(new ListStore<BeanModel>(), new ColumnModel(getGridColumns()));
        grid.setHeight("100%");
        grid.getView().setAutoFill(true);
        grid.setBorders(false);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("name");
        grid.enableEvents(true);
        setTopComponent(toolBar);
        add(grid, new FitData(new Margins(0)));
    }

    private List<ColumnConfig> getGridColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig colCfg = new ColumnConfig("id", "Код Search", 200);
        colCfg.setFixed(true);
        colCfg.setResizable(false);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);
        columnConfigs.add(colCfg);

        colCfg = new ColumnConfig("name", "Название Pmasc", 600);
        colCfg.setMenuDisabled(true);
        colCfg.setSortable(true);
        columnConfigs.add(colCfg);

        return columnConfigs;

    }

    private void initData(NomenclatureSearchCondition condition) {
        gridMask();
        service.getMatchedNomenclatures(condition, new AsyncCallback<List<Nomenclature>>() {

            @Override
            public void onFailure(Throwable caught) {
                grid.unmask();
                MessageBox.alert("Ошибка", "Возникла ошибка при получении данных.", null);
            }

            @Override
            public void onSuccess(List<Nomenclature> result) {
                grid.getStore().removeAll();
                grid.getStore().add(BeanModelLookup.get().getFactory(Nomenclature.class).createModel(result));
                grid.unmask();
            }
        });
    }

    private void removeMatchedNomenclature() {
        MessageBox.confirm("Удаление привязки", "Вы действительно хотите удалить привязку?", new Listener<MessageBoxEvent>() {

            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                    final BeanModel selectedItem = grid.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        service.removeNomenclatureAssociation((Nomenclature) selectedItem.getBean(), new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                MessageBox.alert("Ошибка", "Возникла ошибка при удалении привязки.", null);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                gridMask();
                                grid.getStore().remove(selectedItem);
                                grid.unmask();
                                Info.display("Удаление привязки", "Привязка успешно удалена");
                            }
                        });
                    }
                }
            }
        });
    }

    private void gridMask() {
        grid.mask("Загрузка", "x-mask-loading");
    }
}
