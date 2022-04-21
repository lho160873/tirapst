package pst.arm.client.modules.tablegrid;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.user.client.Element;
import pst.arm.client.modules.tablegrid.widgets.TableGridFiltr;
import pst.arm.client.modules.tablegrid.widgets.TableGridPanel;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridPage extends BasePageNew {

    private static class gridFiltr {

        public gridFiltr() {
        }
    }
    
    private TableGridFiltr gridFiltr;
    private TableGridPanel gridPanel;
    //private Button b;
    @Override
    protected LayoutContainer getContentPanel() {
        return getPanel();
    }
     private LayoutContainer getPanel() {
        
        gridPanel = new TableGridPanel();
        gridFiltr = new TableGridFiltr( gridPanel );
       
        LayoutContainer main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);
        
        BorderLayoutData requestPanelData = new BorderLayoutData(LayoutRegion.NORTH);
        BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        requestPanelData.setCollapsible(true);

        main.add(gridFiltr, requestPanelData);
        main.add(gridPanel, resultPanelData);
        
        return main;
    }
    

}
