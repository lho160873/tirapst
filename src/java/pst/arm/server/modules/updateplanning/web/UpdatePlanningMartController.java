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
@RequestMapping("/secure/updateplanningmart.htm")
public class UpdatePlanningMartController extends ModuleController {

    
    private String moduleName = "updateplanningmart";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
