package pst.arm.server.common.dao;

import java.util.List;
import java.util.Map;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;

/**
 * @author vmaslov
 * @version 1.2.0
 */
public interface FileDAO {

    public Integer attachFile(FileObjectDescriptor fileObj);

    public FileObjectDescriptor getFile(Integer fileId);

    public void deleteFile(Integer fileId);

    public Integer storeFile(FileObjectDescriptor fileObj);

    public Integer placeStoreItem(StoreItem item);

    public StoreItem getStoreItem(Integer storeItemId);

    public StoreItem getStoreItem(String entityKind, Integer entityId);

    public Integer replaceStoreItem(StoreItem item);

    public List<Integer> getStoreItemIds(String entityKind, Integer entityId);
    
    public String getFilePath();
    
    public List<Map<String, Object>> getFileHelpManualPath(String origfilename);
}
