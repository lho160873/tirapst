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
@RequestMapping("/secure/admroles.htm")
public class AdmRolesController extends ModuleController {
    private String moduleName = "admroles";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
