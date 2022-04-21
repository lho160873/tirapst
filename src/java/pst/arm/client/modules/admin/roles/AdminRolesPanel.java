package pst.arm.client.modules.admin.roles;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.domain.Role;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.common.ui.controls.MaxLengthTextField;
import pst.arm.client.modules.admin.roles.lang.AdminRoleConstants;
import pst.arm.client.modules.images.ArmImages;

class AdminRolesPanel extends ContentPanel {

    protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected AdminRoleConstants constants = GWT.create(AdminRoleConstants.class);
    protected ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    protected GWTUserServiceAsync service = GWT.create(GWTUserService.class);
    protected BaseConstants baseConstants = GWT.create(BaseConstants.class);
    protected ColumnModel cm;
    protected ListStore<BeanModel> gridStore;
    protected ListLoader<BeanModel> gridLoader;
    protected EditorGrid<BeanModel> grid;
    private BeanModel currBeanModel;
    private FacilitiesTreePanel facilityPanel;
    private ToolBar tb;
    private Button btnAdd;
    private Button btnDel;
    private Button btnSave;
    private Button btnCansel;
    private Boolean isBtnPress;

    public AdminRolesPanel() {
        isBtnPress = false;
        setBodyBorder(false);
        setBorders(true);
        getHeader().hide();
        initToolBar();
        initGrid();
        setTopComponent(tb);

        this.mask(commonConstants.loading());
        service.getFacilities(new AsyncCallback<List<Facility>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<Facility> result) {
                gridLoader.load();
                facilityPanel.fillTree(result);
            }
        });

        facilityPanel = new FacilitiesTreePanel() {

            @Override
            public void onChange() {
                refreshRoleByChangedFacility();
            }
        };
        facilityPanel.disable();

        BorderLayoutData gridData = new BorderLayoutData(LayoutRegion.CENTER);
        gridData.setMargins(new Margins(0, 3, 0, 0));
        gridData.setFloatable(true);
        BorderLayoutData treeData = new BorderLayoutData(LayoutRegion.EAST);
        treeData.setCollapsible(false);
        treeData.setCollapsible(true);
        treeData.setMinSize(250);
        treeData.setMaxSize(800);
        treeData.setSize(0.4f);
        treeData.setSplit(true);
        treeData.setMargins(new Margins(0, 0, 0, 0));
        setLayout(new BorderLayout());
        ContentPanel gridPanel = new ContentPanel(new FitLayout());
        gridPanel.setBodyBorder(true);
        gridPanel.setBorders(false);
        gridPanel.setHeading(constants.heading());
        gridPanel.add(grid);
        add(gridPanel, gridData);
        add(facilityPanel, treeData);
    }

    private void initToolBar() {

        tb = new ToolBar();
        tb.setBorders(false);

        btnAdd = new Button(constants.toolbarAdd(), images.add());
        btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                addRole();
            }
        });
        tb.add(btnAdd);

        btnDel = new Button(constants.toolbarDelete(), images.delete());
        btnDel.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                deleteRole();
            }
        });
        tb.add(btnDel);

        btnSave = new Button(constants.toolbarSave(), images.save());
        btnSave.setEnabled(false);
        btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                saveRoles();
            }
        });
        tb.add(btnSave);

        btnCansel = new Button(constants.toolbarCansel(), images.cancel());
        btnCansel.setEnabled(false);
        btnCansel.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                rejectChenges();
            }
        });
        tb.add(btnCansel);
    }

    private void initGrid() {
        RpcProxy<BeanModel> proxy = new RpcProxy<BeanModel>() {

            @Override
            public void load(Object loadConfig, AsyncCallback callback) {
                service.getRoles(callback);
            }
        };

        BeanModelReader reader = new BeanModelReader();
        gridLoader = new BaseListLoader(proxy, reader) {

            @Override
            protected void onLoadSuccess(Object loadConfig, Object data) {
                super.onLoadSuccess(loadConfig, data);
                grid.getSelectionModel().select(0, false);
            }
        };
        gridLoader.addLoadListener(new LoadListener() {

            @Override
            public void loaderLoadException(LoadEvent le) {
                super.loaderLoadException(le);
                unmask();
            }

            @Override
            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);
                unmask();
            }
        });

        gridStore = new ListStore<BeanModel>(gridLoader);
        gridStore.addFilter(new StoreFilter<BeanModel>() {

            @Override
            public boolean select(Store<BeanModel> store, BeanModel parent, BeanModel item, String property) {
                if (property.equals("delete")) {
                    return gridStore.getRecord(item).get("delete") == null
                            ? true
                            : !(Boolean) gridStore.getRecord(item).get("delete");
                }
                return true;
            }
        });
        gridStore.applyFilters("delete");

        List<ColumnConfig> colConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("roleName");
        column.setHeader(constants.columtRole());
        column.setWidth(60);
        MaxLengthTextField text = new MaxLengthTextField(40);
        text.setAllowBlank(false);
        text.setAutoValidate(true);
        text.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (value != null) {
                    for (BeanModel bm : gridStore.getModels()) {
                        if (currBeanModel != bm
                                && value.trim().equalsIgnoreCase(((String) bm.get("roleName")).trim())) {
                            return "Роль существует";
                        }
                    }
                }
                return null;
            }
        });
        column.setEditor(new CellEditor(text));
        colConfigs.add(column);

        column = new ColumnConfig();
        column.setId("description");
        column.setHeader(constants.columtDescription());
        column.setWidth(120);
        text = new MaxLengthTextField(110);
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        colConfigs.add(column);

        cm = new ColumnModel(colConfigs);

        grid = new EditorGrid<BeanModel>(gridStore, cm);
        grid.setStripeRows(true);
        grid.setBorders(false);
        grid.getView().setForceFit(true);
        grid.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        grid.stopEditing();
        grid.setSelectionModel(new GridSelectionModel<BeanModel>());
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                selectRole(se.getSelectedItem());
            }
        });
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gridStore.addListener(Store.Update, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (!isBtnPress) {
                    btnSave.setEnabled(gridStore.getModifiedRecords() != null && !gridStore.getModifiedRecords().isEmpty());
                    btnCansel.setEnabled(gridStore.getModifiedRecords() != null && !gridStore.getModifiedRecords().isEmpty());
                }
            }
        });
    }

    /**
     * check changing facilities
     * <p/>
     * @param chekingFasilities - facilities from grid beanmodel
     */
    private void refreshRoleByChangedFacility() {
        if (currBeanModel != null) {
            Record rec = gridStore.getRecord(currBeanModel);
            List<Facility> originalFacilities = new ArrayList<Facility>();

            originalFacilities.addAll((List<Facility>) rec.get("fasilities"));

            List<Facility> updatedFacilities = facilityPanel.getCheckedFacilities();

            if (!equalsFasilities(originalFacilities, updatedFacilities)) {
                List<Facility> def = (List<Facility>) rec.getChanges().get("fasilities");
                if (rec.isModified("fasilities") && def != null && equalsFasilities(def, updatedFacilities)) {
                    rec.set("fasilities", def);
                } else {
                    rec.set("fasilities", updatedFacilities);
                }
            }
        }
    }

    private void setFacilitiesFromRole(BeanModel bm) {
        List<Facility> roleFasilities = ((Role) bm.getBean()).getFasilities();
        facilityPanel.checkFacilities(roleFasilities);
    }

    private void selectRole(BeanModel selRole) {
        facilityPanel.uncheckAllFacilities();
        currBeanModel = selRole;
        if (currBeanModel != null) {
            facilityPanel.enable();
            setFacilitiesFromRole(currBeanModel);
        } else {
            facilityPanel.disable();
        }
    }

    private void rejectChenges() {
        facilityPanel.filterClear();

        isBtnPress = true;

        BeanModel bm = grid.getSelectionModel().getSelectedItem();
        grid.getSelectionModel().deselectAll();
        gridStore.rejectChanges();

        List<BeanModel> list = gridStore.findModels("add", Boolean.TRUE);
        for (BeanModel b : list) {
            gridStore.remove(b);
        }

        grid.getSelectionModel().select(bm, false);
        gridStore.applyFilters("delete");

        isBtnPress = false;
        btnSave.setEnabled(false);
        btnCansel.setEnabled(false);
    }

    private void saveRoles() {
        final MessageBox mbWait = MessageBox.wait(constants.waitHeader(),
                constants.waitMessage(), constants.waitProgress());
        isBtnPress = true;

        List<Role> updRoles = new ArrayList<Role>();
        List<Role> addRoles = new ArrayList<Role>();
        List<Role> delRoles = new ArrayList<Role>();

        long uniqueIdForSaving = 1;

        for (Record r : gridStore.getModifiedRecords()) {

            Role role = (Role) ((BeanModel) r.getModel()).getBean();

            if (role.getDelete() != null && role.getDelete()) {
                if (role.getAdd() == null || !role.getAdd()) {
                    delRoles.add(role);
                }
            } else if (role.getAdd() != null && role.getAdd()) {
                addRoles.add(role);
                role.setAdd(false);
            } else {
                updRoles.add(role);
            }
        }

        service.saveRoles(updRoles, addRoles, delRoles, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                mbWait.close();
                isBtnPress = false;
                btnSave.setEnabled(true);
                btnCansel.setEnabled(true);
                MessageBox.alert("obtainUserInfo()::onFailure", caught.getMessage(), null);
            }

            @Override
            public void onSuccess(Void result) {
                gridStore.commitChanges();

                List<BeanModel> list = gridStore.findModels("delete", Boolean.TRUE);
                for (BeanModel b : list) {
                    gridStore.remove(b);
                }
                mbWait.close();
                isBtnPress = false;
            }
        });
        btnSave.setEnabled(false);
        btnCansel.setEnabled(false);
    }

    private void addRole() {
        Role role = new Role();
        role.setAdd(Boolean.TRUE);
        role.setRoleId(null);
        role.setRoleName("");
        role.setDescription("");
        role.setFasilities(new ArrayList<Facility>());
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Role.class);
        BeanModel bm = factory.createModel(role);
        selectRole(bm);

        grid.stopEditing();
        gridStore.insert(bm, gridStore.getCount());
        gridStore.getRecord(bm).set("roleName", generateRoleName());
        gridStore.getRecord(bm).set("description", constants.newRoleDescription());
        grid.getSelectionModel().select(bm, false);
        grid.startEditing(gridStore.indexOf(bm), 0);
    }

    private String generateRoleName() {
        String roleName = constants.newRoleName();
        int number = 0;
        for (;;) {
            boolean stop = true;
            for (BeanModel bmStore : gridStore.getModels()) {
                if (roleName.trim().equalsIgnoreCase(((String) bmStore.get("roleName")).trim())) {
                    stop = false;
                    roleName = constants.newRoleName() + " (" + (number++) + ")";
                    break;
                }
            }
            if (stop) {
                break;
            } else {
                continue;
            }
        }
        return roleName;
    }

    private void deleteRole() {
        Record record = gridStore.getRecord(currBeanModel);
        if (record.get("add") != null && (Boolean) record.get("add")) {
            gridStore.remove(currBeanModel);
        } else {
            record.set("delete", Boolean.TRUE);
            gridStore.applyFilters("delete");
        }
    }

    private boolean equalsFasilities(List<Facility> first, List<Facility> second) {
        return first.size() == second.size()
                && first.containsAll(second)
                && second.containsAll(first);
    }

    private class RolesGridRender implements GridCellRenderer<BeanModel> {

        @Override
        public Object render(BeanModel model, String property, ColumnData config,
                int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
            if (store.getRecord(model).isModified(property)) {
                return constants.renderColumtFacilitiesChange();
            }
            return store.getRecord(model).isModified(property) ? constants.renderColumtFacilitiesChange() : "";
        }
    }
}
