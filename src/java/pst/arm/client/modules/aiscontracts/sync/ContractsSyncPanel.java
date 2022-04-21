package pst.arm.client.modules.aiscontracts.sync;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.aiscontracts.domain.AisContractType;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class ContractsSyncPanel extends LayoutContainer {

    protected final CommonImages images = GWT.create(CommonImages.class);
    protected BaseConstants baseConstants = (BaseConstants) GWT.create(BaseConstants.class);
    private FormPanel filesUploadPanel;
    private FileUploadField fileXml;
    private Button btnAttach;
    private TextArea taResults;
    private final AisContractType contractType;

    ContractsSyncPanel(AisContractType contractType) {
        this.contractType = contractType;
        initControls();
    }

    protected void initControls() {
        initUploadPanel();
        taResults = new TextArea();
        taResults.setReadOnly(true);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index); //To change body of generated methods, choose Tools | Templates.
        setLayout(new BorderLayout());
        setBorders(false);
        add(filesUploadPanel, new BorderLayoutData(Style.LayoutRegion.NORTH, 110));
        add(taResults, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void initUploadPanel() {

        filesUploadPanel = new FormPanel();
        filesUploadPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                fileXml.clear();
                taResults.setValue(be.getResultHtml());
                unmaskPanel();
            }
        });
        filesUploadPanel.setHeading(contractType.name());
        filesUploadPanel.setBodyBorder(false);
        filesUploadPanel.setFrame(true);
        filesUploadPanel.setLabelAlign(FormPanel.LabelAlign.LEFT);
        filesUploadPanel.setLabelWidth(80);
        filesUploadPanel.setAction(GWT.getHostPageBaseURL() + "aiscontractssyncupload.htm");
        filesUploadPanel.setMethod(FormPanel.Method.POST);
        filesUploadPanel.setEncoding(FormPanel.Encoding.MULTIPART);
        filesUploadPanel.setButtonAlign(Style.HorizontalAlignment.LEFT);
        HiddenField<String> hfContractType = new HiddenField<String>();
        hfContractType.setName("contractType");
        hfContractType.setId("contractType");
        hfContractType.setValue(contractType.name());

        fileXml = new FileUploadField();
        fileXml.setWidth(400);
        fileXml.getMessages().setBrowseText("Обзор");
        fileXml.setName("fileXml");
        fileXml.setId("fileXml");
        fileXml.setFieldLabel("XML файл");
        fileXml.setAllowBlank(false);
        fileXml.setRegex(".*\\.[xX][mM][lL]");
        fileXml.getMessages().setRegexText("неверный формат файла");
        fileXml.setAutoValidate(true);

        btnAttach = new Button("Загрузить");
        btnAttach.setWidth(100);
        btnAttach.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (fileXml.validate()) {
                    maskPanel();
                    filesUploadPanel.submit();
                }
            }
        });
        filesUploadPanel.setWidth(500);
        filesUploadPanel.add(fileXml);
        filesUploadPanel.add(hfContractType);
        filesUploadPanel.addButton(btnAttach);
    }

    private void unmaskPanel() {
        unmask();
    }

    private void maskPanel() {
        mask("Загрузка ...", "x-mask-loading");
    }
}
