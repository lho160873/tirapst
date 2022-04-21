package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 * 
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 */
@RemoteServiceRelativePath("service/fileService")
public interface GWTFileService extends RemoteService {
    public void deleteFile(Integer fileId);
    public List<Integer> getStoreItemIds(String entityKind,Integer entityId);
}
