package pst.arm.server.modules.mymodule.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wesStyle on 01/04/14.
 */
@Controller
@RequestMapping("/secure/mymodule.htm")
public class MyModuleController extends ModuleController {
    private String moduleName = "mymodule";

    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
