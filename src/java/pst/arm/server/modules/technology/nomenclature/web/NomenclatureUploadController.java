package pst.arm.server.modules.technology.nomenclature.web;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pst.arm.server.modules.technology.nomenclature.service.NomenclatureService;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Controller
public class NomenclatureUploadController {

    @Autowired
    NomenclatureService service;

    @RequestMapping(value = "/secure/nomenclatureupload.htm", method = RequestMethod.POST)
    public void handleFormUpload(
            @RequestParam("fileXml") MultipartFile fileXml, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        JSONObject responseJSONObject = new JSONObject();
        List<JSONObject> nomenclatures = new ArrayList<JSONObject>();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");

            InputStreamReader isr = new InputStreamReader(fileXml.getInputStream(), "UTF-8");
            String toString = IOUtils.toString(fileXml.getInputStream(), "UTF-8");
            Document document = reader.read(new InputStreamReader(fileXml.getInputStream(), "UTF-8"));
            Element root = document.getRootElement();
            //First XML version - nomenclaturegroup elemet
            if (root.element("nomenclaturegroup") != null) {
                for (Iterator i = root.element("nomenclaturegroup").elementIterator("nomenclature"); i.hasNext();) {
                    Element nomenclature = (Element) i.next();
                    if (service.isNomenclatureNotMatched(nomenclature.attributeValue("id"))) {
                        nomenclatures.add(getJSONObject(nomenclature));
                    }
                }
            } else {
                for (Iterator iter = document.selectNodes("//article").iterator(); iter.hasNext();) {
                    Element nomenclature = (Element) iter.next();
                    if (service.isNomenclatureNotMatched(nomenclature.attributeValue("id"))) {
                        nomenclatures.add(getJSONObject(nomenclature));
                    }
                }

            }
            responseJSONObject.put("nomenclatures", nomenclatures);
            out.write(responseJSONObject.toString());
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            out.close();
        } catch (DocumentException ex) {
            ex.printStackTrace(System.err);
            out.close();
        } catch (JSONException ex) {
            ex.printStackTrace(System.err);
            out.close();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private JSONObject getJSONObject(Element nomenclature) {
        JSONObject fileJSONObj = new JSONObject();
        fileJSONObj.put("id", nomenclature.attributeValue("id"));
        fileJSONObj.put("name", nomenclature.attributeValue("name"));
        return fileJSONObj;
    }
}
