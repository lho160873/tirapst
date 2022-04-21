/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.common.ui.controls.imagenavigator;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.SwallowEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.ui.controls.imagenavigator.lang.ImageNavigatorConstants;

/**
 *
 * @author s_smirnov
 */
class PreviewPanel extends ContentPanel {
    protected ImageNavigatorConstants constants = (ImageNavigatorConstants) GWT.create(ImageNavigatorConstants.class);
    private ListView view;
    private ListStore<ImageConfig> ds;

    public PreviewPanel(ListStore<ImageConfig> store) {
        //init view
        setHeading(constants.pnlPreviewTitle());
        setLayout(new FitLayout());
        setBodyBorder(false);
        
        //init model
        ds = store;
        view = new ListView() {

            @Override
            protected ModelData prepareData(ModelData model) {
                return super.prepareData(model);
            }

        };
        view.setId("imgNavigator-img-chooser-view");
        view.setStore(ds);
        view.setTemplate(getTemplate());
        view.setBorders(false);
        view.setItemSelector("div.thumb-wrap");
        view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        view.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<ImageConfig>>() {

                    public void handleEvent(SelectionChangedEvent<ImageConfig> se) {
                        onSelectionChange(se);
                    }
                });
        add(view);
    }

    protected void onSelectionChange(SelectionChangedEvent<ImageConfig> se) {
    }

    private native String getTemplate()/*-{
    return ['<tpl for=".">',
    '<div class="thumb-wrap" id="{id}" style="border: 1px solid white">',
    '<div class="thumb"><img src="{previewUrl}" title="{previewTitle}" oncontextmenu="return false;"></div>',
    '<span class="x-editable">{previewTitle}</span></div>',
    '</tpl>',
    '<div class="x-clear"></div>'].join("");
    }-*/;

   
    
    /**
     * @return the view
     */
    public ListView getView() {
        return view;
    }
}
