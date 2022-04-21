package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author wesStyle
 * @since 0.0.1
 */
public interface GWTDDataGridServiceOcpAsync {
    void copyOcp(Integer ocpId, Integer userId, AsyncCallback<Boolean> async);
}

