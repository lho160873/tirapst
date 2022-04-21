package pst.arm.client.modules.samplegraphics;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import pst.arm.client.common.ui.controls.imagenavigator.ImageConfig;
import pst.arm.client.common.ui.controls.imagenavigator.ImagePanel;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class BaseImagePage extends BasePageNew {

    private String urlPage;
    private String caption;
    public BaseImagePage(String urlPage,String caption) {
        this.urlPage = urlPage;
        this.caption = caption;
    }

    @Override
    protected LayoutContainer getContentPanel() {
        return getPanel();
    }

    private LayoutContainer getPanel() {
        LayoutContainer main = new ContentPanel(new BorderLayout());
        ((ContentPanel) main).setHeading(caption);
        ((ContentPanel) main).setHeaderVisible(true);
        main.setBorders(true);

        ImagePanel pnlImage = new ImagePanel();
        ImageConfig imgCfg = new ImageConfig("1", urlPage, "", urlPage, "");
        pnlImage.setImage(imgCfg);
        BorderLayoutData resultPanelDataCenter = new BorderLayoutData(Style.LayoutRegion.CENTER);
        main.add(pnlImage, resultPanelDataCenter);
        return main;

    }
}

