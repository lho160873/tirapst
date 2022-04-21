package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 *
 * @author vorontsov
 */
public class PortletButton extends Button {
    // setIcon(AbstractImagePrototype);
    // private ArmImages images = (ArmImages) GWT.create(ArmImages.class);
    private String goToURL;

    public PortletButton(String buttonText, AbstractImagePrototype buttonImage, String goToURL) {
        setIconAlign(IconAlign.TOP);
        setIcon(buttonImage);
        String str = "<div style=\"margin-Top:60px;\"><b>" + buttonText + "</b></div>";
        setText(str);
        this.goToURL = GWT.getHostPageBaseURL() + goToURL;
        setHeight("100px");
        setWidth("130px");

    }

    @Override
    protected void onClick(ComponentEvent ce) {
        super.onClick(ce);
        Window.Location.assign(goToURL);
    }
}
