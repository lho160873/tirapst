package pst.arm.client.modules.imagegallery;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import java.util.HashMap;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.imagenavigator.BaseImageConfig;
import pst.arm.client.common.ui.controls.imagenavigator.ImageConfig;
import pst.arm.client.common.ui.controls.imagenavigator.ImageNavigator;

/**
 *
 * @author s_smirnov
 */
class ImageGalleryContainer extends LayoutContainer {

    private ImageNavigator imageNavigator;
    private ListStore<ImageConfig> ds;
    private HashMap<String, BaseImageConfig[]> data;
    private HashMap<String, String> title;
    private String type = ImageGalleryPage.TYPE_CGA_MAPS_SCHEMAS;

    public ImageGalleryContainer() {
        //init view
        setLayout(new FillLayout());

        //init model
        initData();

        //fill store with data
        ds = new ListStore<ImageConfig>();
        for (BaseImageConfig imc : data.get(type)) {
            ds.add(imc);
        }

        imageNavigator = new ImageNavigator(ds, title.get(type), null);

        add(imageNavigator);
    }

    /**
     * Data initialization
     */
    private void initData() {
       // String baseUrl = AppHelper.getInstance().baseUrl()
       //         + "/arm_resources/cga/maps_petersburg/";
        //String baseUrl = "D:/web_java_proj/images/";
         String baseUrl = AppHelper.getInstance().baseUrl()
                + "/tirapst/images_doc/";
         // String baseUrl = AppHelper.getInstance().baseUrl()
        //        + AppHelper.getInstance().getConfiguration().srcScreenimagesContracts()+"";
        MessageBox.info("initData", baseUrl, null);
        title = new HashMap<String, String>();
        title.put(ImageGalleryPage.TYPE_CGA_MAPS_SCHEMAS, "Карты (схемы) Петрограда-Ленинграда-Санкт-Петербурга 1917-1990гг.");
        data = new HashMap<String, BaseImageConfig[]>();
        BaseImageConfig[] value = {
            new BaseImageConfig(
            "1",
            baseUrl + "1.jpg",
            "Png 1",
            baseUrl + "view_1.jpg",
            "Png 1"),
            new BaseImageConfig(
            "2",
            baseUrl + "2.jpg",
            "Png 2",
             baseUrl + "view_2.jpg",
            "Png 2")};
        /*BaseImageConfig[] value = {
            new BaseImageConfig(
            "1",
            baseUrl + "1917.jpg",
            "К февралю 1917г.1917",
            baseUrl + "view_1917.jpg",
            "1917"),
            new BaseImageConfig(
            "2",
            baseUrl + "1917-1919.jpg",
            "С февраля 1917г. до 25 июня 1919г.",
            baseUrl + "view_1917-1919.jpg",
            "1917-1919",
            new BaseImageConfig(
            "3",
            baseUrl + "1919-1922.jpg",
            "С 25 июня 1919г. до июля 1922г.",
            baseUrl + "view_1919-1922.jpg",
            "1919-1922"),
            new BaseImageConfig(
            "4",
            baseUrl + "1922-1930.jpg",
            "С июля 1922г. до июля 1930г.",
            baseUrl + "view_1922-1930.jpg",
            "1922-1930"),
            new BaseImageConfig(
            "4",
            baseUrl + "1922-1930.jpg",
            "С июля 1922г. до июля 1930г.",
            baseUrl + "view_1922-1930.jpg",
            "1922-1930"),
            new BaseImageConfig(
            "5",
            baseUrl + "1930-1936.jpg",
            "С июля 1930г. до апреля 1936г.",
            baseUrl + "view_1930-1936.jpg",
            "1930-1936"),
            new BaseImageConfig(
            "6",
            baseUrl + "1936-1961.jpg",
            "С апреля 1936г. до апреля 1961г.",
            baseUrl + "view_1936-1961.jpg",
            "1936-1961"),
            new BaseImageConfig(
            "7",
            baseUrl + "1961-1973.jpg",
            "С апреля 1961г. до апреля 1973г.",
            baseUrl + "view_1961-1973.jpg",
            "1961-1973"),
            new BaseImageConfig(
            "8",
            baseUrl + "1973-1990.jpg",
            "С апреля 1973г. до апреля 1990г.",
            baseUrl + "view_1973-1990.jpg",
            "1973-1990"),
            new BaseImageConfig(
            "9",
            baseUrl + "2003.jpg",
            "Санкт-Петербург, градостроительная ситуация на 2003г.",
            baseUrl + "view_2003.jpg",
            "2003")
        };*/
        data.put(ImageGalleryPage.TYPE_CGA_MAPS_SCHEMAS, value);
    }
}
