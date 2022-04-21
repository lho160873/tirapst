package pst.arm.client.common.ui.controls.imagenavigator;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.rg.ImageResourceGenerator;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.imagenavigator.lang.ImageNavigatorConstants;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author s_smirnov
 */
public class RootPanel extends ContentPanel {
    //protected ImageNavigatorMessages messages = (ImageNavigatorMessages) GWT.create(ImageNavigatorMessages.class);

    protected ImageNavigatorConstants constants = (ImageNavigatorConstants) GWT.create(ImageNavigatorConstants.class);
    private ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    private String baseCaption = "";
    private ToolButton tbtnScreen = new ToolButton("x-tool-maximize");
    private ToolButton tbtnClose = new ToolButton("x-tool-close");
    private Boolean useTbtnClose = false;
    private ToolBar tlbTop = new ToolBar();
    private Label lblImgNum = new Label(constants.pnlRootLblImgNum());
    private Label lblImgCount = new Label("");
    private Label lblImgTitle = new Label("");
    private NumberField nfImgNum = new NumberField();
    private Button btnNavFirst = new Button("", images.nav_first());
    private Button btnNavPrevious = new Button("", images.nav_previous());
    private Button btnNavNext = new Button("", images.nav_next());
    private Button btnNavLast = new Button("", images.nav_last());
    private Button btnSave = new Button("", images.save());
    private Button btnFitToScreen = new Button("", images.arrow_inout());
    private Button btnRealSize = new Button("", images.arrow_out());
    private Button btnZoomIn = new Button("", images.zoom_in());
    private Button btnZoomOut = new Button("", images.zoom_out());
    private Button btnRotateRight = new Button("", images.arrow_rotate_clockwise());
    private Button btnRotateLeft = new Button("", images.arrow_rotate_anticlockwise());
    private ImagePanel pnlImage = new ImagePanel();
    private PreviewPanel pnlPreview;
    private ListStore<ImageConfig> ds = null;
    private StoreListener<ImageConfig> listenerStore = null;
    private Boolean isInitialized = false;
    private BorderLayoutData westData;
    private String imageSaveRelUrl = "fundsImageSave.htm";

    public RootPanel(ListStore<ImageConfig> store, String baseCaption, Boolean useTbtnClose) {
        this.ds = store;
        this.useTbtnClose = useTbtnClose;
        //listener init
        initListeners();
        ds.addStoreListener(listenerStore);

        //view init
        setBaseCaption(baseCaption);
        if (this.baseCaption == null || this.baseCaption.isEmpty()) {
            setHeaderVisible(Boolean.FALSE);
        } else {
            setHeading(this.baseCaption);
        }
        setLayout(new BorderLayout());
        westData = new BorderLayoutData(LayoutRegion.WEST);
        westData.setSplit(true);
        westData.setCollapsible(true);
        tbtnScreen.setTitle(constants.pnlRootTltFullScreen());
        this.getHeader().addTool(tbtnScreen);
        tbtnClose.setTitle(constants.pnlRootTltFullScreen());

        //top toolbar
        tlbTop.setSpacing(2);
        nfImgNum.setWidth(30);
        nfImgNum.setAllowDecimals(false);
        nfImgNum.setAllowNegative(false);
        nfImgNum.addKeyListener(new KeyListener() {

            @Override
            public void componentKeyUp(ComponentEvent event) {
                super.componentKeyUp(event);
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    onImageNumEntered(event);
                }
            }
        });
//        lblImgCount.setText(constants.pnlRootLblImgCountPrefix() + " " + ds.getCount());
        btnNavFirst.setToolTip(constants.pnlRootBtnNavFirstTlt());
        btnNavFirst.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                switchToFirst();
            }
        });
        btnNavPrevious.setToolTip(constants.pnlRootBtnNavPreviousTlt());
        btnNavPrevious.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                switchToPrevious();
            }
        });
        btnNavNext.setToolTip(constants.pnlRootBtnNavNextTlt());
        btnNavNext.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                switchToNext();
            }
        });
        btnNavLast.setToolTip(constants.pnlRootBtnNavLastTlt());
        btnNavLast.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                switchToLast();
            }
        });
        btnSave.setToolTip(constants.pnlRootBtnSaveTlt());
        btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                saveImage();
            }
        });
        btnFitToScreen.setToolTip(constants.pnlRootBtnFitToScreenTlt());
        btnFitToScreen.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.fitToScreen();
            }
        });
        btnRealSize.setToolTip(constants.pnlRootBtnRealSizeTlt());
        btnRealSize.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.showRealSize();
            }
        });
        btnZoomIn.setToolTip(constants.pnlRootBtnZoomInTlt());
        btnZoomIn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.zoomIn();
            }
        });
        btnZoomOut.setToolTip(constants.pnlRootBtnZoomOutTlt());
        btnZoomOut.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.zoomOut();
            }
        });
        btnRotateRight.setToolTip(constants.pnlRootBtnRotateRightTlt());
        btnRotateRight.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.rotateRight();
            }
        });
        btnRotateLeft.setToolTip(constants.pnlRootBtnRotateLeftTlt());
        btnRotateLeft.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                pnlImage.rotateLeft();
            }
        });

        //add buttons to toolbar
        tlbTop.add(btnNavFirst);
        tlbTop.add(btnNavPrevious);
        tlbTop.add(lblImgNum);
        tlbTop.add(nfImgNum);
        tlbTop.add(lblImgCount);
        tlbTop.add(btnNavNext);
        tlbTop.add(btnNavLast);
        //if (Boolean.valueOf(ConfigurationManager.getProperty("controls.imagenavigator.saveImage.visible"))) {
            tlbTop.add(new SeparatorToolItem());
            tlbTop.add(btnSave);
        //}
        //        tlbTop.add(new FillToolItem());
        tlbTop.add(new SeparatorToolItem());
        tlbTop.add(btnFitToScreen);
        tlbTop.add(btnRealSize);
        tlbTop.add(new SeparatorToolItem());
        tlbTop.add(btnZoomIn);
        tlbTop.add(btnZoomOut);
        tlbTop.add(new SeparatorToolItem());
        tlbTop.add(btnRotateRight);
        tlbTop.add(btnRotateLeft);
        tlbTop.add(new SeparatorToolItem());
        tlbTop.add(lblImgTitle);
        this.setTopComponent(tlbTop);

        //panels
        pnlPreview = new PreviewPanel(ds) {

            @Override
            protected void onSelectionChange(SelectionChangedEvent<ImageConfig> se) {
                if (se.getSelectedItem() != null) {
                    onPreviewSelectionChange(se.getSelectedItem());

                    pnlPreview.getView().getItemCount();
                }
            }
        };
        
      
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        add(pnlPreview, westData);
       add(pnlImage, new BorderLayoutData(LayoutRegion.CENTER));
    }

    /**
     * @return the baseCaption
     */
    public String getBaseCaption() {
        return baseCaption;
    }

    /**
     * @param baseCaption the baseCaption to set
     */
    public void setBaseCaption(String baseCaption) {
        this.baseCaption = baseCaption;
    }

    /**
     * @return the tbScreenMode
     */
    public ToolButton getTbtnScreen() {
        return tbtnScreen;
    }

    /**
     * @return the tbScreenMode
     */
    public ToolButton getTbtnClose() {
        return tbtnClose;
    }

    public void setTbtnScreenFullMode() {
        this.tbtnScreen.setTitle(constants.pnlRootTltFullScreen());
        this.tbtnScreen.changeStyle("x-tool-maximize");
        if (useTbtnClose) {
            this.getHeader().removeTool(tbtnClose);
        }
    }

    public void setTbtnScreenCollapseMode() {
        this.tbtnScreen.setTitle(constants.pnlRootTltCollapseScreen());
        this.tbtnScreen.changeStyle("x-tool-restore");
        if (useTbtnClose) {
            this.getHeader().addTool(tbtnClose);
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (!isInitialized && ds.getCount() > 0) {
            init();
            switchToFirst();
        }
    }

    private void init() {
        lblImgCount.setText(constants.pnlRootLblImgCountPrefix() + " " + ds.getCount());
        isInitialized = true;
    }

    protected void initListeners() {
        listenerStore = new StoreListener<ImageConfig>() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void storeBeforeDataChanged(StoreEvent<ImageConfig> se) {
                onBeforeDataChanged(se);
            }
//
//            @Override
//            public void storeClear(StoreEvent<ImageConfig> se) {
//                onClear(se);
//            }
//

            @Override
            public void storeDataChanged(StoreEvent<ImageConfig> se) {
                onDataChanged(se);
            }

//
//            @Override
//            public void storeFilter(StoreEvent<ImageConfig> se) {
//                onDataChanged(se);
//            }
//
            @Override
            public void storeRemove(StoreEvent<ImageConfig> se) {
                onBeforeDataChanged(se);
            }
//
//            @Override
//            public void storeUpdate(StoreEvent<ImageConfig> se) {
//                onUpdate(ds, se.getModel());
//            }
        };
    }

    protected void onBeforeDataChanged(StoreEvent<ImageConfig> se) {
        if (!this.isMasked()) {
            this.mask(GXT.MESSAGES.loadMask_msg());
        }
    }

    protected void onDataChanged(StoreEvent<ImageConfig> se) {
        if (this.isMasked()) {
            if (this.isEnabled()) {
                this.unmask();
            } else {
                this.mask();
            }
        }
        init();
        switchToFirst();
    }

    ListStore<ImageConfig> getStore() {
        return ds;
    }

    private void onPreviewSelectionChange(ImageConfig imgCfg) {
        pnlImage.setImage(imgCfg);
        int indexNum = ds.indexOf(imgCfg) + 1;
        nfImgNum.setValue(indexNum);
        lblImgTitle.setText("<b>" + imgCfg.getTitle() + "</b>");
        resetNavButtons();
    }

    private void switchToIndex(int index) {
        //showImage(ds.getAt(index));
        pnlPreview.getView().getSelectionModel().select(index, false);
    }

    private void switchToFirst() {
        switchToIndex(0);
    }

    private void switchToLast() {
        switchToIndex(ds.getCount() - 1);
    }

    private void switchToNext() {
        ImageConfig imgCfg = (ImageConfig) pnlPreview.getView().getSelectionModel().getSelectedItem();
        switchToIndex(ds.indexOf(imgCfg) + 1);
    }

    private void switchToPrevious() {
        ImageConfig imgCfg = (ImageConfig) pnlPreview.getView().getSelectionModel().getSelectedItem();
        switchToIndex(ds.indexOf(imgCfg) - 1);
    }

    private void onImageNumEntered(ComponentEvent event) {
        switchToIndex(nfImgNum.getValue().intValue() - 1);
    }

    private void resetNavButtons() {
        ImageConfig imgCfg = (ImageConfig) pnlPreview.getView().getSelectionModel().getSelectedItem();
        if (ds.getCount() == 0) {
            btnNavFirst.disable();
            btnNavPrevious.disable();
            btnNavNext.disable();
            btnNavLast.disable();
        } else if (ds.indexOf(imgCfg) == ds.getCount() - 1) {
            btnNavFirst.enable();
            btnNavPrevious.enable();
            btnNavNext.disable();
            btnNavLast.disable();
        } else if (ds.indexOf(imgCfg) == 0) {
            btnNavFirst.disable();
            btnNavPrevious.disable();
            btnNavNext.enable();
            btnNavLast.enable();
        } else {
            btnNavFirst.enable();
            btnNavPrevious.enable();
            btnNavNext.enable();
            btnNavLast.enable();
        }
    }

    private void saveImage() {

        ImageConfig imgCfg = (ImageConfig) pnlPreview.getView().getSelectionModel().getSelectedItem();

        if (imgCfg.getUrl() != null && !imgCfg.getUrl().isEmpty()) {
            String[] prms = imgCfg.getUrl().split("\\?");
            MessageBox.info("",GWT.getHostPageBaseURL() + getImageSaveRelUrl(),null);
            if (prms.length >= 2) {
                Window.Location.assign(GWT.getHostPageBaseURL() + getImageSaveRelUrl() + "?" + prms[1]);
            }
        }
    }

    /**
     * @return the imageSaveRelUrl
     */
    public String getImageSaveRelUrl() {
        return imageSaveRelUrl;
    }

    /**
     * @param imageSaveRelUrl the imageSaveRelUrl to set
     */
    public void setImageSaveRelUrl(String imageSaveRelUrl) {
        this.imageSaveRelUrl = imageSaveRelUrl;
    }
}
