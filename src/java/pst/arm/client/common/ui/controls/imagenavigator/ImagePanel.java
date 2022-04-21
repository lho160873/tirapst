//TODO Remove fit to screen after resize
//TODO Bug. Make real size after rotation forward to wrong canvas size. Repair by implementing new method fitCanvasToRectangle
package pst.arm.client.common.ui.controls.imagenavigator;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Element;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;
import pst.arm.client.common.ui.controls.imagenavigator.lang.ImageNavigatorConstants;

/**
 *
 * @author s_smirnov
 */
public class ImagePanel extends ContentPanel {

    private static final double ZOOM_STEP = 0.2;
    private static final int ROTATION_STEP = 90;
    private DrawingArea canvas = new DrawingArea(0, 0);
    private Image img = null;
    private com.google.gwt.user.client.ui.Image actualImg = null;
    private com.google.gwt.user.client.ui.Image loaderImg = null;
    private ImageConfig imgConfig;
     protected ImageNavigatorConstants constants = (ImageNavigatorConstants) GWT.create(ImageNavigatorConstants.class);

    public ImagePanel() {
        setHeaderVisible(false);
        setLayout(new FillLayout());
        setFrame(true);
        setScrollMode(Scroll.AUTO);
        canvas.getElement().setAttribute("oncontextmenu", "return false;");
        this.add(canvas);
        loaderImg = new com.google.gwt.user.client.ui.Image();
        loaderImg.setVisible(false);
        loaderImg.addLoadHandler(new LoadHandler() {

            /**
             * Before execute image has been already downloaded.
             * Create a new Image object from preloaded image.
             */
            public void onLoad(LoadEvent event) {
//                com.google.gwt.user.client.ui.Image im = new com.google.gwt.user.client.ui.Image(actualImg.getUrl());
//                Window.alert("loaded" + im.getUrl() + " " + im.getHeight() + " " + im.getWidth());
                actualImg = new com.google.gwt.user.client.ui.Image(loaderImg.getUrl());
                //Window.alert("loaded" + actualImg.getUrl() + " " + actualImg.getHeight() + " " + actualImg.getWidth());
                img = new Image(0, 0, 0, 0, imgConfig.getUrl());
                fitCanvasToScreen();
                canvas.add(img);
                fitImageToScreen();
                unmask();
            }
        });
        loaderImg.addErrorHandler(new ErrorHandler() {
         

            public void onError(ErrorEvent event) {            
                MessageBox.info("Error",constants.pnlRootErrorLoad()+" "+loaderImg.getUrl(),null);
                unmask();
            }
        });
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        add(loaderImg);
    }

    @Override
    protected void onLayoutExcecuted(Layout layout) {
        super.onLayoutExcecuted(layout);
        fitCanvasToScreen();
        if (actualImg != null) {
            fitImageToScreen();
        }
    }

    public void setImage(ImageConfig imc) {
        imgConfig = imc;
        if (img != null) {
            canvas.remove(img);
        }

        //load image to research it's params
        this.mask(GXT.MESSAGES.loadMask_msg());
        loaderImg.setUrl(imgConfig.getUrl());
    }

    /**
     * Set size for image
     * @param w
     * @param h
     */
    private void setImageSize(int w, int h) {
        img.setHeight(h);
        img.setWidth(w);
    }

    /**
     * Set size both for canvas
     * @param w
     * @param h
     */
    private void setCanvasSize(int w, int h) {
        canvas.setHeight(h);
        canvas.setWidth(w);
    }

    private void fitToRectangle(int w, int h) {
        int imgWidth = actualImg.getWidth();
        int imgHeight = actualImg.getHeight();
        if (!isHorisontalPosition()) {
            imgWidth = actualImg.getHeight();
            imgHeight = actualImg.getWidth();
        }
//        Window.alert("height" + Integer.toString(h) + " / " + Integer.toString(imgHeight) + " = " + ((float) h / imgHeight));
//        Window.alert("width" + Integer.toString(w) + " / " + Integer.toString(imgWidth) + "=" + ((float) w / imgWidth));
        float scale = Math.min((float) w / imgWidth, (float) h / imgHeight);

        int newWidth = Math.round(actualImg.getWidth() * scale);
        int newHeight = Math.round(actualImg.getHeight() * scale);
        setImageSize(newWidth, newHeight);
        if (isHorisontalPosition()) {
            img.setX(0);
            img.setY(0);
        } else {
            img.setY((img.getWidth() - img.getHeight()) / 2);
            img.setX((img.getHeight() - img.getWidth()) / 2);
        }
    }

    private void fitCanvasToScreen() {
        setCanvasSize(getInnerWidth(), getInnerHeight());
    }

    private void fitImageToScreen() {
        fitToRectangle(getInnerWidth(), getInnerHeight());
    }

    public void removeImage() {
        canvas.remove(img);
    }

    public void rotateRight() {
        fitCanvasToScreen();
        img.setRotation(img.getRotation() + ImagePanel.ROTATION_STEP);
        fitImageToScreen();
    }

    public void rotateLeft() {
        fitCanvasToScreen();
        img.setRotation(img.getRotation() - ImagePanel.ROTATION_STEP);
        fitImageToScreen();
    }

    public void fitToScreen() {
        fitCanvasToScreen();
        fitImageToScreen();
    }

    public void showRealSize() {
        setCanvasSize(actualImg.getWidth(), actualImg.getHeight());
        setImageSize(actualImg.getWidth(), actualImg.getHeight());
    }

    public void zoomIn() {
        //calculate new size
        int newImgWidth = (int) (img.getWidth() + img.getWidth() * ImagePanel.ZOOM_STEP);
        int newImgHeight = (int) (img.getHeight() + img.getHeight() * ImagePanel.ZOOM_STEP);

        if (isHorisontalPosition()) {
            setCanvasSize(newImgWidth, newImgHeight);
        } else {
            setCanvasSize(newImgHeight, newImgWidth);
        }

        setImageSize(newImgWidth, newImgHeight);

        //move coordinates of image
        if (isHorisontalPosition()) {
            img.setX(0);
            img.setY(0);
        } else {
            img.setY((img.getWidth() - img.getHeight()) / 2);
            img.setX((img.getHeight() - img.getWidth()) / 2);
        }
    }

    public void zoomOut() {
        //calculate new size
        int newImgWidth = (int) (img.getWidth() - img.getWidth() * ImagePanel.ZOOM_STEP);
        int newImgHeight = (int) (img.getHeight() - img.getHeight() * ImagePanel.ZOOM_STEP);

        setImageSize(newImgWidth, newImgHeight);

        //move coordinates
        if (isHorisontalPosition()) {
            img.setX(0);
            img.setY(0);
        } else {
            img.setY((img.getWidth() - img.getHeight()) / 2);
            img.setX((img.getHeight() - img.getWidth()) / 2);
        }

        if (isHorisontalPosition()) {
            setCanvasSize(newImgWidth, newImgHeight);
        } else {
            setCanvasSize(newImgHeight, newImgWidth);
        }
    }

    /**
     * Check for horisontal position of image.
     * Result depends on the rotation.
     * @return Boolean position
     */
    private Boolean isHorisontalPosition() {
        return ((img.getRotation() / ImagePanel.ROTATION_STEP % 2) == 0);
    }
}
