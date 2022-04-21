package pst.arm.server.modules.controlproduction.web;

import pst.arm.server.modules.powerproduction.web.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Controller
@RequestMapping("/secure/controlproduction.htm")
public class ControlProductionController extends ModuleController {

    
    private String moduleName = "controlproduction";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
