package pst.arm.client.modules.admin.stat;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import java.util.*;
import pst.arm.client.modules.admin.stat.domain.StatItem;
import pst.arm.client.modules.admin.stat.lang.AdminStatConstants;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author Alexandr Kozhin
 */
public class AdminStatPanel extends LayoutContainer {

    protected AdminStatConstants constants = GWT.create(AdminStatConstants.class);
    protected ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    protected DateField date;
    protected List<StatItem> stat = null;
    protected TabPanel tabs;
    protected ContentPanel center;
    protected ToolBar toolbar_main;
    protected Map<String, TabItem> openedTabs;
    protected ToggleButton btnActivity,btnPages,btnServices;

    public AdminStatPanel() {
        openedTabs = new HashMap<String, TabItem>();
        initView();
    }

    private void initView() {
        toolbar_main = new ToolBar();
        
        date = new DateField();
        date.setWidth(90);
        date.setPropertyEditor(new DateTimePropertyEditor("dd.MM.yyyy"));
        date.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                if (value.length() > 6 && value.length() < 10) {
                    return value + " недопустимая дата - она должна быть в формате DD.MM.YYYY";
                }
                return null;
            }
        });
        date.setEditable(false);
        date.setValue(new Date());
                            
        center = new ContentPanel(new FitLayout());
        center.setHeaderVisible(false);
        center.setBodyBorder(false);
        center.setBorders(false); 
        
        tabs = new TabPanel();        
        tabs.setBodyBorder(false);
        tabs.setBorders(false);      
        tabs.setCloseContextMenu(true);
        tabs.addListener(Events.BeforeRemove, new Listener<TabPanelEvent>() {

            @Override
            public void handleEvent(TabPanelEvent be) {
                TabItem item = be.getItem();
                if (openedTabs.containsValue(item)) {
                    Set entries = openedTabs.entrySet();
                    Iterator it = entries.iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (((TabItem) entry.getValue()).getId().equals(item.getId())) {
                            openedTabs.remove((String) entry.getKey());
                            break;
                        }
                    }
                }
            }
        });
    }

    private void addChart(String type, Date value) {
        String strDate = DateTimeFormat.getFormat("dd.MM.yyyy").format(value);
        if (!openedTabs.containsKey(type + strDate)) {
            BaseChart chart = null;
            if (type.equals("activity")) {
                chart = new ActivityChart(stat, value);
            } else if (type.equals("pages")) {
                chart = new PagesChart(stat, value, "PAGE");
            } else if (type.equals("services")) {
                chart = new PagesChart(stat, value, "SERVICE");
            }else if (type.equals("sessions")) {
                chart = new SessionChart();
            }
            if (chart != null) {
                chart.prepareStat();
                TabItem item = new TabItem(constants.reportTree().get("report." + type) + " - " + strDate);
                item.setLayout(new FitLayout());
                item.setClosable(true);
                item.add(chart);

                openedTabs.put(type + strDate, item);
                tabs.add(item);
                tabs.setSelection(item);
            }
        } else {
            tabs.setSelection(openedTabs.get(type + strDate));
        }
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        //setStyleAttribute("padding", "5px");
        toolbar_main.add(date);
        SelectionListener<ButtonEvent> toggListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (btnActivity.equals(ce.getButton())) {
                    addChart("activity", date.getValue());
                    btnPages.toggle(Boolean.FALSE);
                    btnServices.toggle(Boolean.FALSE);
                } else if (btnPages.equals(ce.getButton())) {
                    addChart("pages", date.getValue());
                    btnActivity.toggle(Boolean.FALSE);
                    btnServices.toggle(Boolean.FALSE);

                } else if (btnServices.equals(ce.getButton())) {
                    addChart("services", date.getValue());
                    btnActivity.toggle(Boolean.FALSE);
                    btnPages.toggle(Boolean.FALSE);
                }
            }
        };
         toolbar_main.add(new SeparatorToolItem());
        btnActivity = new ToggleButton((String) constants.reportTree().get("report.activity"), toggListener);
        btnActivity.toggle(Boolean.TRUE);
        toolbar_main.add(btnActivity);
        toolbar_main.add(new SeparatorToolItem());
        btnPages = new ToggleButton((String) constants.reportTree().get("report.pages"), toggListener);
        btnPages.toggle(Boolean.FALSE);
        toolbar_main.add(btnPages);
        toolbar_main.add(new SeparatorToolItem());
        btnServices = new ToggleButton((String) constants.reportTree().get("report.services"), toggListener);
        btnServices.toggle(Boolean.FALSE);
        toolbar_main.add(btnServices);
        
        center.setTopComponent(toolbar_main);
        center.add(tabs);

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));
        add(center, centerData);

        addChart("activity", date.getValue());
    }

    void setStat(List<StatItem> result) {
        this.stat = result;
    }
}
