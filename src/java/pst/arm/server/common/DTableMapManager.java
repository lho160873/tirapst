/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.common;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.server.common.service.StaticContextHolder;

/**
 * формируем массив запросов для отображения в таблицах
 * 
 * @author LKHorosheva
 * @since 0.0.1
 */

public class DTableMapManager {        
    private volatile static DTableMapManager instance = null;
    private Map< String, DTable > tableMap = new HashMap< String, DTable >(); //массив таблиц 
    private StaticContextHolder context;
    private static final Logger log = Logger.getLogger("ConfigurationManager");

   
    private DTableMapManager() {
    }

    public static DTableMapManager getInstance() {
        
        if (instance == null) {
            synchronized (DTableMapManager.class) {
                if (instance == null) {
                    instance = new DTableMapManager();
                }
            }
        }
        return instance;
    }
        
    public Map< String, DTable > getTableMap()
    {
        return tableMap;
    }
    
    //вернет таблицу по ключу (по ее названию)
    public DTable getTable(String key) {
        String tableName = key.toUpperCase();
        if (!tableMap.containsKey(tableName)) //не нашли загрузим из бина
        {
            DTable t = (DTable) context.getBean(tableName);
            //t.initBuilders();
            tableMap.put(tableName, t);
        }
        return tableMap.get(tableName);
    }

    public void addTable( DTable table )
    {
        String key = table.getQueryName().toUpperCase();
        if (!tableMap.containsKey(key))
        {
          //table.initBuilders();
            tableMap.put(key, table);
        }
    }
    
   
    public void setDataMap( Map< String, DTable > tableMap )
    {        
        this.tableMap = tableMap;
    }
    
    
   @Autowired
   public void setContext(StaticContextHolder context) {
      //  logger.warn("GWTServiceSimpleImpl::setTestService");
        this.context = context;        
    }
 
   
   
}
