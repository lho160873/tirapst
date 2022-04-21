package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 * 
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 */
public interface GWTFileServiceAsync {
    public void getStoreItemIds(String entityKind,Integer entityId, AsyncCallback<List<Integer>> asyncCallback);
    public void deleteFile(Integer fileId, AsyncCallback<Void> asyncCallback);
}
