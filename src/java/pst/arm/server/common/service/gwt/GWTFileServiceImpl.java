package pst.arm.server.common.service.gwt;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.service.remote.GWTFileService;
import pst.arm.server.common.service.FileService;
import pst.arm.server.common.web.GWTController;

@Service("GWTFileService")
public class GWTFileServiceImpl extends GWTController implements GWTFileService {

    @Autowired
    protected FileService fileService;

    @Override
    public void deleteFile(Integer fileId) {
        fileService.deleteFile(fileId);
    }

    @Override
    public List<Integer> getStoreItemIds(String entityKind, Integer entityId) {
        return fileService.getStoreItemIds(entityKind,entityId);
    }
}
