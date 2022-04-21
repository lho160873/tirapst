package pst.arm.server.modules.ganttchart.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

/**
 *
 * @author nikita
 */
@Controller
@RequestMapping("/secure/ganttchart.htm")
public class GanttChartController extends ModuleController {

    private String moduleName = "ganttchart";

    @Override
    protected String getModuleName() {

        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {

        return new HashMap<String, String>();
    }
}
