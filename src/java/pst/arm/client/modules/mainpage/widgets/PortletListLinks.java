package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author s_smirnov
 */
public class PortletListLinks extends Portlet {

    protected ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    protected PortletButton statCardsButton;
    protected Layout layout;
    private ListStore<PortletLink> ds;
    private ListView view;
    protected BaseConstants constants;

    public PortletListLinks(ListStore<PortletLink> store) {
        constants = (BaseConstants) GWT.create(BaseConstants.class);

        //init view
        setHeading(constants.headerGroupLinks());
        setFrame(true);

        //init model
        ds = store;
        view = new ListView() {

            @Override
            protected ModelData prepareData(ModelData model) {
                PortletLink pl = (PortletLink) model;
                if(pl.getHref() != null) {
                    pl.setText("<span style=\"color: blue;\">" + pl.getText() + "</span>");
                }
                return model;
            }

        };
        view.setId("portletListLinks-link-chooser-view");
        view.setStore(ds);
        view.setTemplate(getTemplate());
        view.setItemSelector("div.thumb-wrap");
        view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        view.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<PortletLink>>() {

                    public void handleEvent(SelectionChangedEvent<PortletLink> se) {
                        onSelectionChange(se);
                    }
                });
        add(view);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setLayout(new FitLayout());
        add(view);
    }

    private void onSelectionChange(SelectionChangedEvent<PortletLink> se) {
        PortletLink link = se.getSelectedItem();
        if (link.getHref() != null) {
            String goToURL = link.getHref();
            if (link.getHref().indexOf("/") == -1) {
                goToURL = GWT.getHostPageBaseURL() + goToURL;
            }
            Window.Location.assign(goToURL);
        }
    }

    private native String getTemplate() /*-{
    return ['<tpl for=".">',
    '<div class="thumb-wrap" style="border: 1px solid white">',
    '<div class="thumb">{text}</div></div>',
    '</tpl>',
    '<div class="x-clear"></div>'].join("");

    }-*/;
}
