package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;

// Author Igor

public class DataGridWindowPanelWithHeader {
    private Window w;
    private DataGridPanel dataGridPanel;
    private String header;

    public DataGridWindowPanelWithHeader(DataGridPanel dataGridPanel, String header) {
        this.dataGridPanel = dataGridPanel;

        this.header = header;
        initWindow();
    }

    private void initWindow() {
        w = new Window();
        w.setLayout(new FitLayout());
        w.setHeading(header);
        w.setModal(true);
        w.setSize(550, 550);
        w.add(dataGridPanel);
    }

    public void show() {
        w.show();
        w.center();
    }
}
