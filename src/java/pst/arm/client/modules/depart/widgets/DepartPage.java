package pst.arm.client.modules.depart.widgets;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.modules.BasePageNew;






/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DepartPage extends BasePageNew {

    protected LayoutContainer main;
    protected ContentPanel panel;
    protected ToolBar toolbar;
    private DGComboBox<Integer> cbCompany;
    
    @Override
    protected LayoutContainer getContentPanel() {
        main = new LayoutContainer(new FitLayout());//new BorderLayout());
        main.setBorders(false);

        panel = new ContentPanel(new BorderLayout());
        main.add(panel);

        toolbar = new ToolBar();
        toolbar.setSpacing(6);

        Label lblOrg = new Label("Предприятие:");
        lblOrg.setAutoWidth(true);
        toolbar.add(lblOrg);
        cbCompany = new DGComboBox<Integer>("COMPANY", "MAIN:COMPANY_ID", "MAIN:NAME");
        cbCompany.setValidateOnBlur(Boolean.TRUE);
        //cbCompany.setValueId(DEFAULT_ORG_ID); //РИМР
        toolbar.add(cbCompany);

        panel.setTopComponent(toolbar);
        
        TreeGridExampleTest tree = new TreeGridExampleTest();
        panel.add(tree, new BorderLayoutData(Style.LayoutRegion.CENTER)); //помещаем в центр
       
        
        return main;
    }

   
}
