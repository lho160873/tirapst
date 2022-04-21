package pst.arm.client.modules.depart.widgets;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pst.arm.client.modules.depart.domain.Depart;
import pst.arm.client.modules.depart.service.remote.GWTDepartService;
import pst.arm.client.modules.depart.service.remote.GWTDepartServiceAsync;
import pst.arm.client.modules.images.ArmImages;

public class TreeGridExampleTest extends LayoutContainer {

    private static int ID = 0;
    private ArmImages images = (ArmImages) GWT.create(ArmImages.class);

    
    public TreeGridExampleTest() {
        setLayout(new FitLayout());
        DepartModelTree model = getTreeModel();

        TreeStore<ModelData> store = new TreeStore<ModelData>();
        store.add(model.getChildren(), true);

        ColumnConfig name = new ColumnConfig("name", "Подразделение", 100);
        name.setRenderer(new TreeGridCellRenderer<ModelData>());
        ColumnConfig code = new ColumnConfig("code", "Код подразделения", 100);
        ColumnModel cm = new ColumnModel(Arrays.asList(name,code));

        final TreeGrid<ModelData> tree = new TreeGrid<ModelData>(store, cm);
        tree.setBorders(true);
        //tree.getStyle().setLeafIcon(images.card());
        //tree.setAutoExpandColumn("name");
        tree.setTrackMouseOver(false);
     
        add(tree);

    }
    class DepartModelTree extends BaseTreeModel implements Serializable {


        public DepartModelTree(Integer id, String name, String code) {
            set("id", id);
            set("name", name);
            set("code", code);
        }

        public DepartModelTree(Integer id, String name, String code, DepartModelTree[] children) {
            this(id, name, code);
            for (int i = 0; i < children.length; i++) {
                add(children[i]);
            }
        }

        public Integer getId() {
            return (Integer) get("id");
        }

        public String getName() {
            return (String) get("name");
        }
        
        public String getCode() {
            return (String) get("code");
        }

        public String toString() {
            return getName();
        }
    }
  
    public DepartModelTree getTreeModel() {
        
        List<Depart> departs = new ArrayList<Depart>();
        //AsyncCallback<List<Depart>> callback;
        //getService().getDepart(callback);
        
        DepartModelTree[] folders = new DepartModelTree[]{
            new DepartModelTree(1, "Отдел 1", "00100",
            new DepartModelTree[]{
                new DepartModelTree(2, "Сектор 1", "00110",
                new DepartModelTree[]{
                    new DepartModelTree(3, "Участок 1", "00111"),
                    new DepartModelTree(4, "Участок 2", "00112"),
                    new DepartModelTree(5, "Участок 3", "00113")}),
                new DepartModelTree(6, "Сектор 2", "00120",
                new DepartModelTree[]{
                    new DepartModelTree(7, "Участок 1", "00121"),
                    new DepartModelTree(8, "Участок 2", "00122"),
                    new DepartModelTree(9, "Участок 3", "00123")})})};

        DepartModelTree root = new DepartModelTree(0, "root", "");
        for (int i = 0; i < folders.length; i++) {
            root.add((DepartModelTree) folders[i]);
        }

        return root;
    }
    
    public static GWTDepartServiceAsync getService() {
        return GWT.create(GWTDepartService.class);
    }

}
