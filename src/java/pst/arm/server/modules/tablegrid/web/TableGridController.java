package pst.arm.server.modules.tablegrid.web;

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
@RequestMapping("/secure/tablegrid.htm")
public class TableGridController extends ModuleController {

    
    private String moduleName = "tablegrid";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        setConfig("controls.edit.visible", "true");
        return new HashMap<String, String>();
    }
}

