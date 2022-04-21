package pst.arm.client.common.ui.controls.imagenavigator;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import pst.arm.client.common.ui.controls.editdomain.WindowClosable;

/**
 *
 * @author s_smirnov
 */
public class ImageNavigator extends LayoutContainer {

    private RootPanel pnlRoot;
    private Window winFullScr;

    public ImageNavigator(ListStore<ImageConfig> store, String baseCaption, final WindowClosable owner) {
        //full screen window
        winFullScr = new Window();
        winFullScr.setMaximizable(true);
        winFullScr.setClosable(false);
        winFullScr.setResizable(false);
        winFullScr.setModal(true);
        winFullScr.setLayout(new FillLayout());
        winFullScr.setHeaderVisible(false);
        //TODO Remove hardcoded window size. It's done to prevent loosing toolbars items after resizing.
        winFullScr.setSize(1000, 500);

        //root panel
        pnlRoot = new RootPanel(store, baseCaption, owner != null);
        pnlRoot.getTbtnScreen().addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                changeScreenMode();
            }
        });

        //root panel header
        pnlRoot.getHeader().addListener(Events.OnClick, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {
                changeScreenMode();
            }
        });

        pnlRoot.getTbtnClose().addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                if (owner != null) {
                    changeScreenMode();
                    owner.doClose();
                }
            }
        });



    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FillLayout());
        add(pnlRoot);
    }

    public void setHeading(String heading) {
        pnlRoot.setHeading(heading);
    }

    /**
     * Change screen mode handler
     */
    private void changeScreenMode() {
        if (winFullScr.isVisible()) {
            pnlRoot.setTbtnScreenFullMode();
            winFullScr.hide();
            winFullScr.remove(pnlRoot);
            this.add(pnlRoot);
            this.layout();
        } else {
            this.remove(pnlRoot);
            pnlRoot.setTbtnScreenCollapseMode();
            winFullScr.add(pnlRoot);
            winFullScr.show();
            winFullScr.maximize();
            winFullScr.layout();
        }

    }

    public void maximize() {
        changeScreenMode();
    }
    
    public void setImageSaveRelUrl(String url){
        if( pnlRoot != null ){
            pnlRoot.setImageSaveRelUrl(url);
        }
    }
}
