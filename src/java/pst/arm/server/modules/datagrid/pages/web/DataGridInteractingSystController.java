package pst.arm.server.modules.datagrid.pages.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;
import pst.arm.server.common.web.LoginController;

/**
 *
 * @author I. Gaiduk
 */

@Controller
@RequestMapping("/secure/datagridinteractingsyst.htm")
public class DataGridInteractingSystController extends ModuleController {
    
    private String moduleName = "datagridinteractingsyst";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
    
}
