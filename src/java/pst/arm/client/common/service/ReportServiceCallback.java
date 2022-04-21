package pst.arm.client.common.service;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;
import pst.arm.client.common.lang.BaseMessages;
import pst.arm.client.common.lang.CommonConstants;

/**
 * <p>Unmask parent component and send request to FileDownloadController if no generation 
 * errors.</p>
 * <p>If report generation finished with errors show error messages to user.</p>
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public abstract class ReportServiceCallback implements AsyncCallback<ReportGeneratorResult> {

    private final static CommonConstants COMMON_CONSTANTS = GWT.create(CommonConstants.class);
    private final static BaseMessages BASE_MESSAGES = GWT.create(BaseMessages.class);
    Component parent;

    public ReportServiceCallback(Component parent) {
        this.parent = parent;
    }

    @Override
    public void onFailure(Throwable caught) {
        unmask();
        MessageBox.alert(COMMON_CONSTANTS.error(), BASE_MESSAGES.errReportGeneration(), null);
    }

    @Override
    public void onSuccess(ReportGeneratorResult result) {
        unmask();
        if ( result.isSuccess() && result.getId() != null ) {
            doReportRequest(result);
        } else if( result.isSuccess() && result.getId() == null ){
            MessageBox.alert(COMMON_CONSTANTS.error(), "no id", null);
        }else {
            MessageBox.alert(COMMON_CONSTANTS.error(), result.getMessages(), null);
        }
    }
    
    private void unmask(){
        if(parent != null){
            parent.unmask();
        }
    }
    
    protected abstract void doReportRequest(ReportGeneratorResult result);
}
