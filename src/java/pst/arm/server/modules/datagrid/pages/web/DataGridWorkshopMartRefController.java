package pst.arm.server.modules.datagrid.pages.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author igor
 */
@Controller
@RequestMapping("/secure/datagridworkshopmartref.htm")

public class DataGridWorkshopMartRefController extends ModuleController {

    private String moduleName = "datagridworkshopmartref";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }

    
}
