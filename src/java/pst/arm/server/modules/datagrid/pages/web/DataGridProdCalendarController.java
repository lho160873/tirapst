package pst.arm.server.modules.datagrid.pages.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;
import pst.arm.server.common.web.LoginController;

/**
 *
 * @author Igor
 * @since 0.0.1
 */
@Controller
@RequestMapping("/secure/datagridprodcalendarig.htm")
public class DataGridProdCalendarController extends ModuleController {

    
    private String moduleName = "datagridprodcalendarig";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}


