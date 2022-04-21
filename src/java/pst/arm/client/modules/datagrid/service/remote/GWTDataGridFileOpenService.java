package pst.arm.client.modules.datagrid.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by wesStyle on 14.03.2015.
 */
@RemoteServiceRelativePath("service/ddatagridfileopenservice")
public interface GWTDataGridFileOpenService extends RemoteService{
    Boolean openFile(String fid);
    Boolean deleteFile(String fid);
}
