package pst.arm.client.common.ui.controls.store;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class FileUploadWindow extends Window {

    /**
     * @return the storeEventListener
     */
    public StoreEventListener getStoreEventListener() {
        return storeEventListener;
    }

    /**
     * @param storeEventListener the storeEventListener to set
     */
    public void setStoreEventListener(StoreEventListener storeEventListener) {
        this.storeEventListener = storeEventListener;
    }

    public interface StoreEventListener {

        void onStoreItemAdded(Integer storeItemId);
    }
    protected SelectionListener<ButtonEvent> btnListener;
    private StoreEventListener storeEventListener;
    protected Listener<FormEvent> pnlUploadListener;
    protected FormPanel pnlUpload;
    protected HiddenField<Long> hfDictId;
    protected FileUploadField fuDocument;
    protected Button btnOk, btnCancel;
    private String entityKind;
    private Integer entityId;
    private HiddenField<String> hfEntityKind;
    private HiddenField<Integer> hfEntityId;

    public FileUploadWindow(String entityKind, Integer entityId) {
        this.entityKind = entityKind;
        this.entityId = entityId;

        setHeading("Загрузка");
        setModal(true);

        initListeners();
        initComponents();
    }

    protected void initListeners() {
        btnListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (ce.getButton() == btnOk) {
                    pnlUpload.submit();
                } else {
                    hide();
                }
            }
        };

        pnlUploadListener = new Listener<FormEvent>() {
            @Override
            public void handleEvent(FormEvent be) {
                if (!be.getResultHtml().isEmpty() && !be.getResultHtml().equalsIgnoreCase("null")) {
                    if (getStoreEventListener() != null) {
                        getStoreEventListener().onStoreItemAdded(Integer.parseInt(be.getResultHtml()));
                        hide();
                    }
                }
            }
        };
    }

    protected void initComponents() {
        fuDocument = new FileUploadField();
        fuDocument.setName("file");
        fuDocument.setFieldLabel("Файл для загрузки");

        fuDocument.getMessages().setBrowseText("Обзор");

        btnOk = new Button("Загрузить");
        btnOk.setId("submit_button");
        btnOk.addSelectionListener(btnListener);

        btnCancel = new Button("Отмена");
        btnCancel.setId("cancel_button");
        btnCancel.addSelectionListener(btnListener);
    }

    protected void arrangeComponents() {
        setLayout(new BorderLayout());

        hfEntityKind = new HiddenField<String>();
        hfEntityKind.setName("entityKind");
        hfEntityKind.setValue(entityKind);

        hfEntityId = new HiddenField<Integer>();
        hfEntityId.setName("entityId");
        hfEntityId.setValue(entityId);

        pnlUpload = new FormPanel();
        pnlUpload.setHeaderVisible(false);
        pnlUpload.setBodyBorder(false);
        pnlUpload.setLabelAlign(FormPanel.LabelAlign.TOP);
        pnlUpload.setAction(GWT.getHostPageBaseURL() + "fileupload.htm");
        pnlUpload.setMethod(FormPanel.Method.POST);
        pnlUpload.setEncoding(FormPanel.Encoding.MULTIPART);
        pnlUpload.addListener(Events.Submit, pnlUploadListener);

        pnlUpload.add(hfEntityKind);
        pnlUpload.add(hfEntityId);
        pnlUpload.add(fuDocument, new FormData("100%"));


        pnlUpload.addButton(btnOk);
        pnlUpload.addButton(btnCancel);

        add(pnlUpload, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setSize(400, 135);

        arrangeComponents();
    }

    /**
     * @return the entityKind
     */
    public String getEntityKind() {
        return entityKind;
    }

    /**
     * @param entityKind the entityKind to set
     */
    public void setEntityKind(String entityKind) {
        this.entityKind = entityKind;
    }

    /**
     * @return the entityId
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
}
