package pst.arm.client.modules.datagrid.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by wesStyle on 14.03.2015.
 */

public interface GWTDataGridFileOpenServiceAsync {
    void openFile(String fid, AsyncCallback<Boolean> callback);
    void deleteFile(String fid, AsyncCallback<Boolean> callback);
}
