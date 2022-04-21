package pst.arm.server.common.service;

import java.util.List;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;

/**
 *
 * @author vmaslov
 */
public interface FileService {
    public FileObjectDescriptor getFile(Integer fileId);
    public Integer saveFile(FileObjectDescriptor fileObj);
    public void deleteFile(Integer fileId);

    public Integer placeStoreItem(StoreItem item);

    public StoreItem getStoreItem(Integer storeItemId);

    public List<Integer> getStoreItemIds(String entityKind, Integer entityId);
    
}
