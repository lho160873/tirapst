package pst.arm.client.modules.imagegallery;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author kozhin
 */
public class ImageGalleryPage  extends BasePageNew {
    public static final String TYPE_CGA_MAPS_SCHEMAS = "karti";
    @Override
    protected LayoutContainer getContentPanel() {
        return new ImageGalleryContainer();
    }
}