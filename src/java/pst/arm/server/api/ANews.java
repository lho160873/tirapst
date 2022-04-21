/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;

@Controller
public class ANews {

    private static final Logger log = Logger.getLogger("ANews");
    private DDataGridDAO dao;

    @Autowired
    public void setDataGridDAO(DDataGridDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/api/getnews.htm")
    //@RequestMapping(value="/api/mytest.htm", method=RequestMethod.GET)   
    @ResponseBody
    public void getNews(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        //response.setStatus(HttpServletResponse.SC_OK);        
        List<JSONObject> responseJSONObject = new LinkedList<JSONObject>();
        //Boolean isCorrectId = false;
        Integer requestIdType = Integer.valueOf(request.getParameter("id_type"));
        Integer requestIdNews = Integer.valueOf(request.getParameter("id_news"));

        log.warn("TEST requestTypeId = " + requestIdType);
        DataGridSearchCondition condition = new DataGridSearchCondition();
        SKeyForColumn key = new SKeyForColumn("MAIN:NEWS_TYPE_ID");
        DRowColumnValNumber val = new DRowColumnValNumber();
        val.setVal(requestIdType);
        condition.getFilters().put(key, val);
        if (requestIdNews != -1) {
            SKeyForColumn key_id = new SKeyForColumn("MAIN:NEWS_ID");
            DRowColumnValNumber val_id = new DRowColumnValNumber();
            val_id.setVal(requestIdNews);
            condition.getFilters().put(key_id, val_id);
        }
        

        List<DDataGrid> table = dao.select("NEWS_HLV", condition, false);       
        
        for (int i = 0; i < table.size(); i++) {
            HashMap<SKeyForColumn, IRowColumnVal> row = table.get(i).getRows();
            String news = (String) row.get(new SKeyForColumn("MAIN", "COMMENT")).getVal();
            Integer idNews = (Integer) row.get(new SKeyForColumn("MAIN", "NEWS_ID")).getVal();
            Date dateNews = (Date) row.get(new SKeyForColumn("MAIN", "DATE_NEWS")).getVal();

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String strDateNews = sdf.format(dateNews);

            log.warn("TEST NEWS" + news);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id_news", idNews);
            jsonObj.put("name", news);
            jsonObj.put("date_news", strDateNews);
            responseJSONObject.add(jsonObj);
        }

        /*JSONObject jsonObj_1 = new JSONObject();
         jsonObj_1.put("id", request.getParameter("id"));
         jsonObj_1.put("name", request.getParameter("name"));
         responseJSONObject.add(jsonObj_1);

         JSONObject jsonObj_2 = new JSONObject();
         jsonObj_2.put("id", "2");
         jsonObj_2.put("name", "test2");
         responseJSONObject.add(jsonObj_2);*/
        PrintWriter out = null;


        try {
            log.warn(request.getParameterMap());
            out = response.getWriter();
            out.write(responseJSONObject.toString());
            out.flush();
            log.warn("NEWS_OK");
        } catch (Exception ex) {
            log.error("NEWS_FAILED", ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

