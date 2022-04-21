package pst.arm.server.modules.datagrid.pages.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

@Controller
@RequestMapping("/secure/datagridlaborandpayrollanalytics.htm")
public class DataGridLaborAndPayrollAnalyticsController extends ModuleController {
    
    private String moduleName = "datagridlaborandpayrollanalytics";

    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
    
}
