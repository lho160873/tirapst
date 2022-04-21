package pst.arm.server.modules.aiscontracts.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Controller
@RequestMapping("/secure/aiscontractssyncsupply.htm")
public class AisContractsSyncSupplyController extends ModuleController {

    private String moduleName = "aiscontractssyncsupply";

    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
