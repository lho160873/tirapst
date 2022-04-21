package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;

public class DataGridWindowPanel {
    private Window w;
    private DataGridPanel dataGridPanel;
    private String header;

    public DataGridWindowPanel(DataGridPanel dataGridPanel, String header) {
        this.dataGridPanel = dataGridPanel;

        this.header = header;
        initWindow();
    }
    
    public DataGridWindowPanel(DataGridPanel dataGridPanel) {
        this.dataGridPanel = dataGridPanel;
        initWindow();
    }

    private void initWindow() {
        w = new Window();
        w.setLayout(new FitLayout());
        //w.setHeading(header);
        w.setModal(true);
        w.setSize(900, 550);
        w.add(dataGridPanel);
    }

    public void show() {
        w.show();
        w.center();
    }
}
