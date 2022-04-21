package pst.arm.server.modules.nullpage.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;
import pst.arm.server.common.web.LoginController;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Controller
@RequestMapping("/secure/nullpage.htm")
public class NullPageController extends ModuleController {

    
    private String moduleName = "nullpage";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        //setConfig("controls.edit.visible", "true");
        return new HashMap<String, String>();
    }
}
