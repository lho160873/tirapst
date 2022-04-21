package pst.arm.client.modules.datagrid;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import java.util.List;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataBasePanelEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.widgets.DataBasePanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridWindow extends Window {

    protected DataBasePanel gridPanel;
    private LayoutContainer main;

    public DataBasePanel getGridPanel() {
        return gridPanel;
    }

    public DataGridWindow(String queryString, final Boolean isViewOnly, final Boolean isForSend ) {
        setLayout(new FitLayout());
        setModal(true);
        setSize(680, 580);
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
            @Override
            public void handleEvent(DataBasePanelEvent be) {
                gridPanel = be.getPanel();
                main.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
                setIsForSend(isForSend);
                if ( isViewOnly !=null && isViewOnly ) {
                    gridPanel.setMode(EditMode.VIEWONLY);
                    gridPanel.enabledBtn();
                }
            }
        };

        DataGridPanelBuilder.createDataGridOrTreePanel(queryString, createListener);
        add(main);
    }

    public DataGridWindow(IRowColumnVal val, String queryString, final Boolean isViewOnly, final Boolean isForSend ) {
        setLayout(new FitLayout());
        setModal(true);
        setSize(680, 580);
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
            @Override
            public void handleEvent(DataBasePanelEvent be) {
                gridPanel = be.getPanel();
                main.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
                setIsForSend(isForSend);
                if ( isViewOnly !=null && isViewOnly ) {
                    gridPanel.setMode(EditMode.VIEWONLY);
                    gridPanel.enabledBtn();
                }
            }
        };

        DataGridPanelBuilder.createDataGridCustomCnd(queryString, createListener, val);
        add(main);
    }

    public DataGridWindow(String queryString, final Boolean isViewOnly, final Boolean isForSend, DCondition addCnd) {
        setLayout(new FitLayout());
        setModal(true);
        setSize(680, 580);
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
            @Override
            public void handleEvent(DataBasePanelEvent be) {
                gridPanel = be.getPanel();
                main.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
                setIsForSend(isForSend);
                if (isViewOnly != null && isViewOnly) {
                    gridPanel.setMode(EditMode.VIEWONLY);
                    gridPanel.enabledBtn();
                }
            }
        };

        DataGridPanelBuilder.createDataGridOrTreePanel(queryString, createListener, addCnd);
        add(main);
    }
    
    public DataGridWindow(String queryString, final Boolean isViewOnly, final Boolean isForSend, List<DCondition> conditions) {
        setLayout(new FitLayout());
        setModal(true);
        setSize(680, 580);
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
            @Override
            public void handleEvent(DataBasePanelEvent be) {
                gridPanel = be.getPanel();
                main.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
                setIsForSend(isForSend);
                if (isViewOnly != null && isViewOnly) {
                    gridPanel.setMode(EditMode.VIEWONLY);
                    gridPanel.enabledBtn();
                }
            }
        };

        DataGridPanelBuilder.createDataGridOrTreePanel(queryString, createListener, conditions);
        add(main);
    }

        public DataGridWindow(String queryString, final Boolean isViewOnly, final Boolean isForSend, final Boolean IsShowFiltr ) {
        setLayout(new FitLayout());
        setModal(true);
        setSize(680, 580);
        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);

        Listener<DataBasePanelEvent> createListener = new Listener<DataBasePanelEvent>() {
            @Override
            public void handleEvent(DataBasePanelEvent be) {
                gridPanel = be.getPanel();
                main.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
                setIsForSend(isForSend);
                gridPanel.setIsShowPanelFiltr(IsShowFiltr);
                if ( isViewOnly !=null && isViewOnly ) {
                    gridPanel.setMode(EditMode.VIEWONLY);
                    gridPanel.enabledBtn();
                }
            }
        };

        DataGridPanelBuilder.createDataGridOrTreePanel(queryString, createListener);
        add(main);
    }

    
    public void addDataGridListener(Listener<? extends DataGridEvent> listener) {
        gridPanel.addDataGridListener(listener);
    }

    public void removeDataGridListener(Listener<? extends DataGridEvent> listener) {
        gridPanel.removeDataGridListener(listener);
    }

    public void setIsForSend(Boolean b) {
        if (b != null) {
            gridPanel.setIsBtnSendEnabled(b);
        }
    }
}
