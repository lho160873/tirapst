package pst.arm.client.modules.help;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author vorontsov
 */
public class AboutDialog extends Dialog {

    protected LabelField versionLabel;
    protected LabelField releaseDateLabel;
    protected AdapterField developerLabel;
    protected AdapterField techSupportLabel;
    protected Button developerButton;
    protected Button techSupportButton;
    protected SelectionListener<ButtonEvent> techSupportListner;
    protected SelectionListener<ButtonEvent> developerListner;
    protected FormData formData;
    protected BaseConstants constants;
    protected ArmImages images;

    public AboutDialog() {
        constants = (BaseConstants) GWT.create(BaseConstants.class);
        images = (ArmImages) GWT.create(ArmImages.class);

        FormLayout layout = new FormLayout(LabelAlign.LEFT);
        layout.setLabelWidth(130);
        setLayout(layout);
        
        setHeading(constants.aboutDialogTitle());
        setWidth(350);
        setResizable(false);
        setIcon(images.about());

        setButtons(Dialog.OK);
        setModal(true);
        setHideOnButtonClick(true);

        initListners();

        initControls();
    }

    protected void initListners() {
        techSupportListner = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Window.open(constants.aboutDialogLnkSupport(), "", "");
            }
        };

        developerListner = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Window.open(constants.aboutDialogLnkDeveloper(), "", "");
            }
        };
    }

    protected void initControls() {
        formData = new FormData("-2"); // #NLS
        formData.setMargins(new Margins(5, 5, 5, 5));

        versionLabel = new LabelField();
        versionLabel.setFieldLabel(getPad() + constants.aboutDialogLblVersion());
        versionLabel.setText(constants.aboutDialogValVersion());

        releaseDateLabel = new LabelField();
        releaseDateLabel.setFieldLabel(getPad() + constants.aboutDialogLblReleaseDate());
        releaseDateLabel.setText(constants.aboutDialogValReleaseDate());

        developerButton = new Button(constants.aboutDialogValDeveloper());
        developerButton.setAutoWidth(true);
        developerButton.addStyleName("button-link"); // #NLS
        developerButton.addSelectionListener(developerListner);

        developerLabel = new AdapterField(getLinkContainer(developerButton));
        developerLabel.setFieldLabel(getPad() + constants.aboutDialogLblDeveloper());
        
        techSupportButton = new Button(constants.aboutDialogValSupport());
        techSupportButton.setAutoWidth(true);
        techSupportButton.addStyleName("button-link"); // #NLS
        techSupportButton.addSelectionListener(techSupportListner);

        techSupportLabel = new AdapterField(getLinkContainer(techSupportButton));
        techSupportLabel.setFieldLabel(getPad() + constants.aboutDialogLblSupport());
    }

    protected LayoutContainer getLinkContainer(Button linkButton) {
        LayoutContainer lc = new LayoutContainer(new HBoxLayout());
        HBoxLayoutData flex = new HBoxLayoutData(new Margins(0, 0, 0, 0));
        flex.setFlex(1);
        lc.add(linkButton, new HBoxLayoutData(new Margins(0, 0, 0, 0)));
        lc.add(new Text(), flex);

        return lc;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        add(versionLabel, formData);
        add(releaseDateLabel, formData);
        add(developerLabel, formData);
//        add(techSupportLabel, formData);
    }

    protected String getPad() {
        return "&nbsp;&nbsp;"; // #NLS
    }
}
