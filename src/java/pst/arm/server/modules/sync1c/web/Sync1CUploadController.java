package pst.arm.server.modules.sync1c.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Object1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Sync1CResult;
import pst.arm.client.modules.sync1c.domain.SyncConfig;
import pst.arm.client.modules.sync1c.domain.Worker1C;
import pst.arm.server.modules.sync1c.service.Sync1CService;
import pst.arm.server.modules.sync1c.util.Sync1CParser;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Controller
public class Sync1CUploadController {

    private static final Logger log = Logger.getLogger(Sync1CUploadController.class);
    @Autowired
    Sync1CService syncService;

    @RequestMapping(value = "/secure/sync1cupload.htm", method = RequestMethod.POST)
    public void handleFormUpload(
            @RequestParam(value = "fileXml", required = true) MultipartFile fileXml,
            @RequestParam(value = "types", required = true) String types,
            @RequestParam(value = "showOnlyChanged", required = true) Boolean showOnlyChanged,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        SyncConfig syncConfig = new SyncConfig();
        syncConfig.setShowOnlyChanged(showOnlyChanged);
        JSONObject responseJSONObject = new JSONObject();
        PrintWriter out = null;
        try {
            if (types.contains("posts")) {
                List<Sync1CResult> posts = handlePosts(fileXml, syncConfig);
                if (!posts.isEmpty()) {
                    responseJSONObject.put(Object1C.ObjctType.Post.name(), convertResultsToJSON(posts));
                }
            }
            if (types.contains("departments")) {
                List<Sync1CResult> departments = handleDepartments(fileXml, syncConfig);
                if (!departments.isEmpty()) {
                    responseJSONObject.put(Object1C.ObjctType.Department.name(), convertResultsToJSON(departments));
                }
            }
            if (types.contains("workers")) {
                List<Sync1CResult> workers = handleWorkers(fileXml, syncConfig);
                if (!workers.isEmpty()) {
                    responseJSONObject.put(Object1C.ObjctType.Worker.name(), convertResultsToJSON(workers));
                }
            }
            out = response.getWriter();
            out.write(responseJSONObject.toString());
            out.flush();

        } catch (Exception ex) {
            log.error("Handle data from 1C failed", ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private List<Sync1CResult> handlePosts(MultipartFile fileXml, SyncConfig syncConfig) {
        try {
            List<Post1C> posts = Sync1CParser.getPosts(new BOMInputStream(fileXml.getInputStream()));
            return syncService.updatePosts(posts, syncConfig);
        } catch (Exception ex) {
            log.error("Update posts failed", ex);
        }
        return Collections.EMPTY_LIST;
    }

    private List<Sync1CResult> handleWorkers(MultipartFile fileXml, SyncConfig syncConfig) {
        try {
            List<Worker1C> workers = Sync1CParser.getWorkers(new BOMInputStream(fileXml.getInputStream()));
            return syncService.updateWorkers(workers, syncConfig);
        } catch (Exception ex) {
            log.error("Update workers failed", ex);
        }
        return Collections.EMPTY_LIST;
    }

    private List<Sync1CResult> handleDepartments(MultipartFile fileXml, SyncConfig syncConfig) {
        try {
            List<Department1C> departments = Sync1CParser.getDepartments(new BOMInputStream(fileXml.getInputStream()));
            return syncService.updateDepartments(departments, syncConfig);
        } catch (Exception e) {
            log.error("Update departments failed", e);
        }
        return Collections.EMPTY_LIST;
    }

    private List<JSONObject> convertResultsToJSON(List<Sync1CResult> results) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Sync1CResult result : results) {
            JSONObject json = new JSONObject();
            json.put("status", result.getStatus().name());
            json.put("text", result.getObject().getText());
            json.put("error", result.getError());
            list.add(json);
        }
        return list;
    }
}
