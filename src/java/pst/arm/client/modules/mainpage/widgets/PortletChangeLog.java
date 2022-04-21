package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.service.remote.GWTCommonService;
import pst.arm.client.common.service.remote.GWTCommonServiceAsync;

/**
 * Portlet with info about version chages(changelog)
 *
 * @author Alexandr Kozhin
 * @since 0.9.0
 */
public class PortletChangeLog extends Portlet{
    private GWTCommonServiceAsync service = GWT.create(GWTCommonService.class);
    private final Html htmlChangeLog;
    protected BaseConstants constants = GWT.create(BaseConstants.class);
    public PortletChangeLog() {
        setHeading(constants.portletChangeLogCaption());
        setCollapsible(true);
        htmlChangeLog = new Html();
        add(htmlChangeLog);
        service.getChangeLog(new AsyncCallback<String>() {

            @Override public void onFailure(Throwable caught){    
                  MessageBox.alert("onFailure", "Error getChangeLog", null);
            }
            @Override public void onSuccess(String result) {
                if (result != null) {
                    htmlChangeLog.setHtml(result);
                }
            }
        });
    }
}
