package pst.arm.server.modules.updateplanning.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author Igor
 * @since 0.0.1
 */
@Controller
@RequestMapping("/secure/updateplanningpriboy.htm")
public class UpdatePlanningPriboyController extends ModuleController {

    
    private String moduleName = "updateplanningpriboy";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
