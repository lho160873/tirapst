package pst.arm.server.common.web;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;
import pst.arm.server.common.service.FileService;
import pst.arm.server.utils.Translit;

/**
 *
 * @author vmaslov
 * @since 1.2.0
 */
@Controller
public class FileController {

    @Autowired
    private FileService service;

    @RequestMapping(value = "/secure/fileupload.htm", method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(
            @RequestParam(value = "entityKind", required = true) String entityKind,
            @RequestParam(value = "entityId", required = true) Integer entityId,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            StoreItem item = new StoreItem();
            item.setEntityKind(entityKind);
            item.setEntityId(entityId);
            item.setFileBytes(file.getBytes());
            item.setFormat(FilenameUtils.getExtension(file.getOriginalFilename()));
            item.setSourceName(FilenameUtils.getBaseName(file.getOriginalFilename()));
            
            
            Integer storeItemId = service.placeStoreItem(item);

            if (storeItemId != null) {
                return storeItemId.toString();
            }
        }
        return "null";
    }

    @RequestMapping(value = "/secure/filedownload.htm", method = RequestMethod.GET)
    public void fileDownload(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "id", required = true) Integer fileId) throws Exception {
        byte[] bytes = null;
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        FileObjectDescriptor file = service.getFile(fileId);
        if (file != null) {
            response.setContentType(file.getFileContentType());
            bytes = file.getFileContent();
            String name = Translit.toTranslit(file.getFileName());
            if (name != null) {
                response.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
            }
        }
        // flush it in the response
        ServletOutputStream responseOutputStream = response.getOutputStream();
        if (bytes != null) {
            responseOutputStream.write(bytes);
        } else {
            responseOutputStream.write("File answer is null".getBytes());
        }
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * Resource handler. Get resource by store item id.
     *
     * @param request
     * @param response
     * @param id Store item id (table STORE)
     * @throws IOException
     * @since 0.7.0
     */
    @RequestMapping("/secure/resource.htm")
    public void sendResource(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "storeItemId", required = true) Integer storeItemId) throws IOException {

        ServletOutputStream responseOutputStream;
        try {

            StoreItem item = service.getStoreItem(storeItemId);
            if (item != null && item.getFileBytes() != null) {
                String format = item.getFormat();

                if (format != null) {
                    if (format.equalsIgnoreCase("doc")) {
                        response.setContentType("application/msword");
                    } else if (format.equalsIgnoreCase("xls")) {
                        response.setContentType("application/x-excel");
                    } else if (format.equalsIgnoreCase("pdf") || format.equalsIgnoreCase("rtf") || format.equalsIgnoreCase("zip")) {
                        response.setContentType("application/" + format);
                    } else if (format.equalsIgnoreCase("htm") || format.equalsIgnoreCase("html")) {
                        response.setContentType("text/html");
                    } else if (format.equalsIgnoreCase("txt")) {
                        response.setContentType("text/plain");
                    } else if (format.equals("jpe") || format.equals("jpg") || format.equals("jpeg")) {
                        response.setContentType("image/jpeg");
                    } else if (format.equals("tif") || format.equals("tiff")) {
                        response.setContentType("image/x-tiff");
                    } 
//                    else {
//                        response.setContentType("image/" + format);
//                    }
                }
                String name = Translit.toTranslit(item.getSourceName());
                if (name != null) {
                    response.addHeader("Content-Disposition", "attachment; filename=\"" + name +"."+ format + "\"");
                }

                responseOutputStream = response.getOutputStream();
                responseOutputStream.write(item.getFileBytes());
                responseOutputStream.flush();
                responseOutputStream.close();
            } else {
                String error = "Файл с id=" + storeItemId + " не существует";
                responseOutputStream = response.getOutputStream();
                responseOutputStream.write(error.getBytes());
                responseOutputStream.flush();
                responseOutputStream.close();

//                }
            }
        } catch (IOException e) {
            responseOutputStream = response.getOutputStream();
            String error = "Ошибка при получении ресурса. Обратитесь к администратору.";
            responseOutputStream.write(error.getBytes());
            responseOutputStream.flush();
            responseOutputStream.close();
        }
    }
}
