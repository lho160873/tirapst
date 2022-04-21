package pst.arm.server.modules.samplegraphics.web;


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
@RequestMapping("/secure/generalgraphic.htm")
public class GeneralGraphicController extends ModuleController {

    
    private String moduleName = "generalgraphic";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        return new HashMap<String, String>();
    }
}