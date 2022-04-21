package pst.arm.client.modules.admin.roles;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pst.arm.client.common.domain.Facility;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTUserService;
import pst.arm.client.common.service.remote.GWTUserServiceAsync;
import pst.arm.client.modules.admin.roles.lang.AdminRoleConstants;
import pst.arm.client.modules.images.ArmImages;

public class FacilitiesTreePanel extends ContentPanel {

    protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected AdminRoleConstants constants = GWT.create(AdminRoleConstants.class);
    protected GWTUserServiceAsync service = GWT.create(GWTUserService.class);
    private TreeStore<FacilityTreeModel> treeStore;
    private TreePanel<FacilityTreeModel> tree;
    private StoreFilterField<FacilityTreeModel> filter;
    private HashMap<Long, FacilityTreeModel> modelsMap;
    private HashMap<Long, Facility> facilitiesMap;
    private List<Facility> chekedFacilities = new ArrayList<Facility>();
    private ArmImages images = (ArmImages) GWT.create(ArmImages.class);

    public FacilitiesTreePanel() {
        initTree();
        setLayout(new FitLayout());
        setHeading(constants.facilitiesHeading());
        ToolBar toolBar = new ToolBar();
        toolBar.add(filter);
        
        Button btnCollapseAll = new Button(null, new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce) {
                tree.collapseAll();
            }
        });
        btnCollapseAll.setIcon(images.tree_minus());
        toolBar.add(btnCollapseAll);

        Button btnExpandAll = new Button(null, new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce) {
                tree.expandAll();
            }
        });
        btnExpandAll.setIcon(images.tree_plus());
        toolBar.add(btnExpandAll);        
        
        setTopComponent(toolBar);
        add(tree);
    }

    public void onChange() {
    }

    public void filterClear() {
        filter.clear();
    }

    private void initTree() {
        filter = new StoreFilterField<FacilityTreeModel>() {

            @Override
            protected boolean doSelect(Store<FacilityTreeModel> store, FacilityTreeModel parent,
                    FacilityTreeModel record, String property, String filter) {
                FacilityTreeModel ftm = record;
                FacilityTreeModel ftp = ftm.getParent();

                if (ftm.isSelected(filter) || ((ftp != null) && ftp.isSelected(filter))) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onFilter() {
                super.onFilter();
                markCheckBoxes();
            }
        };
        //filter.setEmptyText(constants.facilityTreeFilterText());
        filter.setWidth(215);
        treeStore = new TreeStore<FacilityTreeModel>();
        filter.bind(treeStore);

        tree = new TreePanel<FacilityTreeModel>(treeStore) {

            @Override
            protected void onCheckClick(TreePanelEvent tpe, TreePanel.TreeNode node) {
                super.onCheckClick(tpe, node);

                FacilityTreeModel ftm = (FacilityTreeModel) node.getModel();
                boolean check = isChecked(ftm);
                if (check) {
                    while (node != null) {
                        ftm = (FacilityTreeModel) node.getModel();
                        ftm.setChecked(true);
                        Facility f = facilitiesMap.get(ftm.getFacilityId());
                        if (!chekedFacilities.contains(f)) {
                            chekedFacilities.add(f);
                        }
                        node = node.getParent();
                    }
                } else {
                    ftm = (FacilityTreeModel) node.getModel();
                    ftm.setChecked(false);
                    Facility f = facilitiesMap.get(ftm.getFacilityId());
                    chekedFacilities.remove(f);
                    if (treeStore.hasChildren(ftm)) {
                        for (FacilityTreeModel subFtm : treeStore.getChildren(ftm, true)) {
                            subFtm.setChecked(false);
                            f = facilitiesMap.get(subFtm.getFacilityId());
                            chekedFacilities.remove(f);
                        }
                    }
                }
                onChange();
            }
        };
        tree.setAutoExpand(true);
        new QuickTip(tree); // necessary for the tooltip
        tree.setLabelProvider(new ModelStringProvider<FacilityTreeModel>() {

            @Override
            public String getStringValue(FacilityTreeModel model, String property) {
                String name = model.get("name");
                String description = model.get("description");
                return "<span qtip='" + name + "' qtitle='" + constants.facilityTreeQtitle() + "'>" + description + "<span>";
            }
        });

        tree.setAutoLoad(true);
        tree.setBorders(false);
        tree.setDisplayProperty("name");
        tree.setCheckable(true);
        tree.addStyleName("treepanel-requisites");
    }

    public void checkFacilities(List<Facility> facilities) {
        uncheckAllFacilities();
        chekedFacilities.addAll(facilities);
        List<FacilityTreeModel> checkedModels = new ArrayList<FacilityTreeModel>();
        if (facilities != null) {
            for (Facility f : facilities) {
                FacilityTreeModel model = modelsMap.get(f.getFacilityId());
                if (model != null) {
                    model.setChecked(true);
                    checkedModels.add(model);
                    tree.setChecked(model, true);
                }
            }
        }
        tree.setCheckedSelection(checkedModels);
    }

    public void uncheckAllFacilities() {
        for (FacilityTreeModel model : tree.getCheckedSelection()) {
            model.setChecked(false);
            tree.setChecked(model, false);
        }
        chekedFacilities.clear();
    }

    public List<Facility> getCheckedFacilities() {
        return chekedFacilities;
    }

    private void markCheckBoxes() {
        List<Facility> marks = new ArrayList<Facility>();
        marks.addAll(chekedFacilities);
        checkFacilities(marks);
    }

    public Boolean fillTree(List<Facility> result) {
        modelsMap = new HashMap<Long, FacilityTreeModel>();

        if (result != null) {
            treeStore.removeAll();

            facilitiesMap = new HashMap<Long, Facility>();
            LinkedHashMap<String, FacilityTreeModel> rootFacilities = new LinkedHashMap<String, FacilityTreeModel>();
            for (Facility facility : result) {
                FacilityTreeModel model = null;
                if (facility.getType().equalsIgnoreCase("module")) {
                    model = new FacilityTreeModel(
                            facility.getModule(), facility.getModule(), facility.getDescription(), facility.getFacilityId());
                    rootFacilities.put(facility.getModule(), model);
                } else if (facility.getType().equalsIgnoreCase("module_operation")) {
                    FacilityTreeModel parent = (FacilityTreeModel) rootFacilities.get(facility.getModule());
                    if (parent != null) {
                        model = new FacilityTreeModel(
                                facility.getModule() + "_" + facility.getName(), facility.getName(), facility.getDescription(), facility.getFacilityId());
                        parent.add(model);
                    }
                }
                modelsMap.put(facility.getFacilityId(), model);
                facilitiesMap.put(facility.getFacilityId(), facility);
            }
            treeStore.add(new ArrayList<FacilityTreeModel>(rootFacilities.values()), true);
            tree.expandAll();
        }
        return true;
    }

    public final class FacilityTreeModel extends BaseTreeModel {

        public FacilityTreeModel(String id, String name, String description, Long facilityId) {
            set("id", id);
            set("name", name);
            set("description", description);
            set("facilityId", facilityId);
            set("parent", "");
        }

        public FacilityTreeModel(String id, String name, String description, Long facilityId, BaseTreeModel[] children) {
            this(id, name, description, facilityId);
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    children[i].set("parent", id);
                    add(children[i]);
                }
            }
        }

        public Long getFacilityId() {
            return get("facilityId");
        }

        public void setChecked(Boolean checked) {
            set("checked", checked);

        }

        public boolean isChecked() {
            Boolean checked = get("checked");
            if (checked == null) {
                return false;
            }
            return checked.booleanValue();
        }

        public String getName() {
            return get("name");
        }

        public String getDescription() {
            return get("description");
        }

        public boolean isSelected(String selected) {
            if (getName().toLowerCase().contains(selected.toLowerCase())
                    || getDescription().toLowerCase().contains(selected.toLowerCase())) {
                return true;
            }
            return false;
        }

        @Override
        public FacilityTreeModel getParent() {
            return (FacilityTreeModel) super.getParent();
        }
    }
}
