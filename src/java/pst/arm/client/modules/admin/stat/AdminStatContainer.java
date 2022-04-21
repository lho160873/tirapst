package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.common.service.remote.GWTCommonServiceAsync;
import pst.arm.client.modules.admin.stat.domain.StatItem;
import pst.arm.client.modules.admin.stat.lang.AdminStatConstants;

/**
 *
 * @author kozhin
 */
class AdminStatContainer extends ContentPanel {

    protected AdminStatConstants constants = GWT.create(AdminStatConstants.class);
    protected GWTCommonServiceAsync service = GWT.create(GWTCommonService.class);
    protected BaseConstants baseConstants = (BaseConstants) GWT.create(BaseConstants.class);
    protected TabPanel tabPanel;
    protected TabItem tabLogSource, tabStat;
    protected AdminStatPanel pnlStat;
    protected TextArea taLogSource;

    public AdminStatContainer() {
        setHeading(baseConstants.headerGroupAdministration() + baseConstants.breadcrumbsDelimiter() + constants.heading());

        pnlStat = new AdminStatPanel();
        tabPanel = new TabPanel();
        tabPanel.setBorders(false);
        tabPanel.setBodyBorder(false);

        tabPanel.setTabPosition(TabPanel.TabPosition.BOTTOM);
        tabLogSource = new TabItem(constants.logSourceHeading());
        tabLogSource.setBorders(false);
        
        tabStat = new TabItem(constants.statHeading());
        tabStat.setEnabled(false);
        tabStat.setBorders(false);
        
        taLogSource = new TextArea();
        taLogSource.setReadOnly(true);
        taLogSource.mask("Loading ...");
        service.getStatistic(new AsyncCallback<List<StatItem>>() {

            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("onFailure", caught.getMessage(), null);
                taLogSource.unmask();
            }

            @Override
            public void onSuccess(List<StatItem> result) {
                if (result != null) {
                    pnlStat.setStat(result);
                    StringBuilder str = new StringBuilder();
                    for (StatItem statItem : result) {
                        str.append(statItem.toString());
                    }
                    taLogSource.setValue(str.toString());
                } else {
                    taLogSource.setValue("");
                }
                taLogSource.unmask();
                tabStat.setEnabled(true);
                tabPanel.setSelection(tabStat);

            }
        });
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        tabPanel.add(tabLogSource);
        tabLogSource.setLayout(new FitLayout());
        tabLogSource.add(taLogSource);
        tabStat.setLayout(new FitLayout());
        tabStat.add(pnlStat);
       
        tabPanel.add(tabStat);
               
        add(tabPanel);
        setBorders(false);
        //setBodyBorder(false);
    }
}
