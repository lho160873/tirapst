package pst.arm.server.modules.datagrid.service.gwt;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenService;
import pst.arm.server.common.web.GWTController;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by wesStyle on 14.03.2015.
 */
@Service("GWTDDataGridFileOpenService")
public class GWTDDataGridFileOpenService extends GWTController implements GWTDataGridFileOpenService {
    @Autowired
    GWTDDataGridService service;
    @Override
    public Boolean openFile(String fid) {
        try {

            DTable table = service.getTable("DOC_FILE_VO");
            DDataGrid domain = new DDataGrid();
            for (IColumnBuilder b : table.getColumnBuilders()) {
                for (Map.Entry e : b.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn)e.getKey();
                    domain.getRows().put(key, b.getColumn(key).getColumnProperty().createRowColumnVal());
                }
            }

            DataGridSearchCondition cnd = new DataGridSearchCondition();
            DRowColumnValString stringVal = new DRowColumnValString();
            stringVal.setVal(fid);
            cnd.getFilters().put(new SKeyForColumn("MAIN:FILE_ID"), stringVal);

            List<DDataGrid> domains = service.getDataGrid(table.getQueryName(), cnd);

            if (domains.size() == 0)
                throw new Exception("Ошибка получения данных");

            String server = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:SRVR_NAME")).getVal();
            String path = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:ROOT_PATH")).getVal();
            String ext = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:EXTENSION")).getVal();
            String filename = (String) domains.get(0).getRows().get(new SKeyForColumn("MAIN:FILENAME")).getVal();
            
            SmbFile smbFile = new SmbFile("smb://" + server + "/" + path + "/" + filename + ".dat");
            SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFile);

            FileObjectDescriptor fod = new FileObjectDescriptor(IOUtils.toByteArray(smbFileInputStream));

            fod.setFileName(filename);
            fod.setFileExt(ext.replace(".", ""));
            smbFileInputStream.close();

            getSession().setAttribute(fid, fod);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (RpcServiceException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteFile(String fid) {
        try {
            DTable table = service.getTable("DOC_FILE_VO");
            DDataGrid domain = new DDataGrid();
            for (IColumnBuilder b : table.getColumnBuilders()) {
                for (Map.Entry e : b.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) e.getKey();
                    domain.getRows().put(key, b.getColumn(key).getColumnProperty().createRowColumnVal());
                }
            }

            DataGridSearchCondition cnd = new DataGridSearchCondition();
            DRowColumnValString stringVal = new DRowColumnValString();
            stringVal.setVal(fid);
            cnd.getFilters().put(new SKeyForColumn("MAIN:FILE_ID"), stringVal);

            List<DDataGrid> domains = service.getDataGrid(table.getQueryName(), cnd);

            if (domains.size() == 0)
                throw new Exception("Ошибка получения данных");

            /*String server = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:SRVR_NAME")).getVal();
            String path = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:ROOT_PATH")).getVal();
            String ext = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:EXTENSION")).getVal();
            String filename = (String)domains.get(0).getRows().get(new SKeyForColumn("MAIN:FILENAME")).getVal();

            SmbFile smbFile = new SmbFile("smb://" + server + "/" + path + "/" + filename + ".dat");
            smbFile.delete();*/

            service.deleteDomains(table.getQueryName(), domains);

            return true;
        } catch (RpcServiceException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
