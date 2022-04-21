package pst.arm.server.web.controllers;

import com.stimulsoft.base.exception.StiException;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Контроллер для вызова вьювера отчетов Stimulsoft
 *
 * Created by wesStyle on 21/04/14.
 */

@Controller
public class ReportController {

    @Autowired
    ServletContext context;

    // TODO: добавить параметры вызова
    @RequestMapping("/secure/rept.htm")
    public ModelAndView showReportPage(ModelAndView model) {

        // TODO Добавить чтение файла по пути из файла сборки
        final String filePath = context.getRealPath("/WEB-INF/reports/Simple List 1.mdc");

        // Открытие файла
        StiReport reportObj = new StiReport();
        try {
            reportObj = StiSerializeManager.deserializeDocument(new File(filePath)).getReport();
            // Далее к объекту отчета можно добавлять базы, датасорсы, другие параметры
        } catch (StiException e) {
            e.printStackTrace();
        }

        model.addObject("reportObj", reportObj);
        model.setViewName("/secure/report");
        return model;
    }
}
