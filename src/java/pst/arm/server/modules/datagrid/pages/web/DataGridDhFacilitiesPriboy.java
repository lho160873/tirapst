package pst.arm.server.modules.datagrid.pages.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pst.arm.server.common.web.ModuleController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Igor
 */
@Controller
@RequestMapping("/secure/datagriddhfacilitiespriboy.htm")
public class DataGridDhFacilitiesPriboy extends ModuleController {

    
    private String moduleName = "datagriddhfacilitiespriboy";
    
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    @Override
    protected Map<String, String> getModuleConfig() {
        /*setConfig("controls.add.visible", getFacility("add").toString());
        if (getFacility("add") || getFacility("edit") || getFacility("editFull")) {
            setConfig("controls.edit.visible", "true"); //кнопка сохранить на форме редактирования
        }
        setConfig("controls.editBtn.visible", getFacility("edit").toString()); //кнопка редактирования на форме с таблицей
        setConfig("controls.delete.visible", getFacility("delete").toString());
        setConfig("controls.delete.visible", getFacility("editFull").toString());*/
        return new HashMap<String, String>();
    }
}

