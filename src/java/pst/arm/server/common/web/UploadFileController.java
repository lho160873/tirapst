package pst.arm.server.common.web;

import com.gargoylesoftware.htmlunit.Page;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.server.common.service.FileService;
import pst.arm.server.utils.Translit;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author wesStyle
 */

@Controller
public class UploadFileController {
    @Autowired
    GWTDDataGridService service;

    private static Logger log = Logger.getLogger("UploadFileController");

    @RequestMapping(value="/secure/upload.htm", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("MAIN-DOC_ID") String docid,
                                                 @RequestParam("MAIN-COMMENT") String desc,
                                                 @RequestParam("MAIN-SRVR_NAME") String server,
                                                 @RequestParam("MAIN-ROOT_PATH") String path,
                                                 @RequestParam("MAIN-DOC_TYPE_ID") String dti,
                                                 @RequestParam("MAIN-CREATOR_ID") String cid,
                                                 @RequestParam("MAIN-FILE_ROOT_ID") String fid,
                                                 @RequestParam("MAIN-FILENAME") MultipartFile file) {
        List<DDataGrid> domains = new ArrayList<DDataGrid>();
        SmbFile smbFile = null;
        try {

            DTable table = service.getTable("DOC_FILE_FULL");
            DDataGrid domain = new DDataGrid();

            for (IColumnBuilder b : table.getColumnBuilders()) {
                for (Map.Entry e : b.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) e.getKey();
                    domain.getRows().put(key, b.getColumn(key).getColumnProperty().createRowColumnVal());
                }
            }

            IRowColumnVal val = new DRowColumnValNumber();
            docid = docid.replace("\u00a0","");
            val.setVal(docid);
            domain.getRows().put(new SKeyForColumn("MAIN:DOC_ID"), val);

            val = new DRowColumnValNumber();
            val.setVal(0);
            domain.getRows().put(new SKeyForColumn("MAIN:IS_CANCELLED"), val);

            val = new DRowColumnValString();
            val.setVal(desc);
            domain.getRows().put(new SKeyForColumn("MAIN:COMMENT"), val);

            val = new DRowColumnValString();
            val.setVal(server);
            domain.getRows().put(new SKeyForColumn("MAIN:SRVR_NAME"), val);

            val = new DRowColumnValString();
            val.setVal(path);
            domain.getRows().put(new SKeyForColumn("MAIN:ROOT_PATH"), val);

            val = new DRowColumnValString();
            val.setVal(file.getOriginalFilename());
            domain.getRows().put(new SKeyForColumn("MAIN:ORIG_FILENAME"), val);

            val = new DRowColumnValNumber();
            val.setVal(cid);
            domain.getRows().put(new SKeyForColumn("MAIN:CREATOR_ID"), val);

            val = new DRowColumnValNumber();
            val.setVal(dti);
            domain.getRows().put(new SKeyForColumn("MAIN:DOC_TYPE_ID"), val);

            val = new DRowColumnValNumber();
            val.setVal(fid);
            domain.getRows().put(new SKeyForColumn("MAIN:FILE_ROOT_ID"), val);

            Integer rand = new Random().nextInt(9999) + 1;
            String tempname = rand.toString() + "\\/";

            val = new DRowColumnValString();
            val.setVal(tempname);
            domain.getRows().put(new SKeyForColumn("MAIN:FILENAME"), val);

            service.save(table.getQueryName(), domain, domain, true);
            DataGridSearchCondition cnd = new DataGridSearchCondition();
            cnd.getFilters().put(new SKeyForColumn("MAIN:FILENAME"), val);

            domains = service.getDataGrid(table.getQueryName(), cnd);

            if ((domains == null) || (domains.size() == 0))
                throw new Exception("Ошибка получения данных");

            DDataGrid domain2 = new DDataGrid();
            domain2.copy(domains.get(0));


            String filename = (String) domain2.getRows().get(new SKeyForColumn("MAIN:FILE_ID")).getVal().toString();
            val = new DRowColumnValString();
            val.setVal(filename);
            domain2.getRows().put(new SKeyForColumn("MAIN:FILENAME"), val);

            String ext = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            val = new DRowColumnValString();
            val.setVal(ext);

            domain2.getRows().put(new SKeyForColumn("MAIN:EXTENSION"), val);

            byte[] bytes = file.getBytes();
            smbFile = new SmbFile("smb://" + server + "/" + path + "/" + filename + ".dat");
            SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
            smbFileOutputStream.write(bytes);
            smbFileOutputStream.close();

            service.save(table.getQueryName(), domain2, domains.get(0), false);

            if (!smbFile.exists())
                throw new Exception("Ошибка чтения записанного файла");
            return filename;
        } catch (Exception e) {
            if (domains.size() > 0) {
                try {
                    service.deleteDomains("DOC_FILE_VO", domains);
                } catch (Exception ee) {
                    log.warn(" --Ошибка удаления временной записи для файла--");
                    log.warn(e.getMessage());
                }
            }
            if (smbFile != null)
                try {
                    smbFile.delete();
                } catch (SmbException e1) {
                    log.warn(" --Ошибка удаления файла при исключении--");
                    log.warn(e.getMessage());
                }
            log.warn(" --Ошибка записи файла--");
            log.warn(e.getMessage());
            return "fail";
        }
    }
}
