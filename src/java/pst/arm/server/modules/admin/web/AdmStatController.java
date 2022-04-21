package pst.arm.server.modules.admin.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author Sergey
 */
@Controller
@RequestMapping("/secure/admstat.htm")
public class AdmStatController extends ModuleController {
    private String moduleName = "admstat";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
