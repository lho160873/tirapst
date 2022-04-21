package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;

public class TreeGrid2<M extends ModelData> extends TreeGrid<M> {

    private Boolean useKeyProvider = null;

    public TreeGrid2(TreeStore store, ColumnModel cm) {
        super(store, cm);
    }

    public TreeNode findNodeModel(M m) {
        if (m == null || useKeyProvider == null) return null;
        MessageBox.alert("keyprovider",useKeyProvider.toString(),null);
        return nodes.get(useKeyProvider ? generateModelId(m) : cache.get(m));
    }
}