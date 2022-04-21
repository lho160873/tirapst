package pst.arm.server.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;
import pst.arm.server.common.dao.FileDAO;
import pst.arm.server.common.service.FileService;

/**
 * 
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    FileDAO fileDAO;

    @Override
    public FileObjectDescriptor getFile(Integer fileId) {
        return fileDAO.getFile(fileId);
    }

    @Override
    public Integer saveFile(FileObjectDescriptor fileObj) {
        return fileDAO.attachFile(fileObj);
    }

    @Override
    public void deleteFile(Integer fileId) {
        fileDAO.deleteFile(fileId);
    }

    @Override
    public Integer placeStoreItem(StoreItem item) {
        StoreItem searchedItem = fileDAO.getStoreItem(item.getEntityKind(), item.getEntityId());
        if ( searchedItem != null) {
            item.setStoreId(searchedItem.getStoreId());
            item.setPath(searchedItem.getPath());
            return fileDAO.replaceStoreItem(item);
        }
        return fileDAO.placeStoreItem(item);
    }

    @Override
    public StoreItem getStoreItem(Integer storeItemId) {
        StoreItem item = fileDAO.getStoreItem(storeItemId);
        if (item != null) {
            String path = item.getPath();
            File f = new File(path);
            if (f.exists()) {
                try {
                    byte[] bytes = null;
                    bytes = FileUtils.readFileToByteArray(f);


                    item.setFileBytes(bytes);
                } catch (IOException ex) {
                }
            }
        }
        return item;
    }

    @Override
    public List<Integer> getStoreItemIds(String entityKind, Integer entityId) {
        return fileDAO.getStoreItemIds(entityKind,entityId);
    }
}
