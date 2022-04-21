/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.pages.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author Igor
 */
@Controller
@RequestMapping("/secure/datagridworker1c.htm")
public class DataGridWorker1cController extends ModuleController {

    private String moduleName = "datagridworker1c";

    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}
