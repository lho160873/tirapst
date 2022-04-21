package pst.arm.server.modules.datagrid.service.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.server.common.dao.FileDAO;
import pst.arm.server.modules.datagrid.dao.DDataGridDAO;
import pst.arm.server.modules.datagrid.service.DDataGridService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class DDataGridServiceImpl implements DDataGridService {

    private DDataGridDAO dao;
    private FileDAO fileDao;
    private static Logger log = Logger.getLogger("GWTDDataGridServiceImpl");

    @Autowired
    public void setDataGridDAO(DDataGridDAO dao) {
        this.dao = dao;
    }

    @Autowired
    public void setFileDao(FileDAO fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public List<DDataGrid> getAllDataGrid(String tableName) {
        log.warn("DDataGridServiceImpl::getAllDataGrid");
        List<DDataGrid> data = dao.select(tableName);
        return data;
    }

    @Override
    public int getDataGridCount(String tableName, DataGridSearchCondition condition) {
        return dao.count(tableName, condition);
    }

    @Override
    public Boolean execProcedure(String sql) {
        Boolean isError = false;
        try {
            dao.execProcedure(sql);
        }
        catch (Exception e) {
            isError = true;
        }
        return isError;
    }

    @Override
    public Boolean execProcedure(String sql, String dbName) {
        Boolean isError = false;
        try {
            dao.execProcedure(sql, dbName);
        }
        catch (Exception e) {
            isError = true;
        }
        return isError;
    }

    @Override
    public List<DDataGrid> getDataGrid(String tableName, DataGridSearchCondition condition) {
        log.warn("DDataGridServiceImpl::getDataGrid");
        List<DDataGrid> data = dao.select(tableName, condition, false);
        return data;
    }

    @Override
    public List<DDataGrid> getDataGridPage(String tableName, DataGridSearchCondition condition) {
        log.warn("DDataGridServiceImpl::getDataGrid");
        List<DDataGrid> data = dao.select(tableName, condition, true);
        return data;
    }

    @Override
    public DDataGrid saveDataGrid(String tableName, DDataGrid domain, DDataGrid oldDomain, Boolean isNew) {
        if (isNew) {
            return dao.insert(tableName, domain);
        } else {
            dao.update(tableName, domain, oldDomain);
            return domain;
        }
    }
    
    @Override
    public Boolean insertDataGrid(String tableName, List<DDataGrid> domains) {
         for (DDataGrid domain : domains) {    
             dao.insert(tableName, domain);
         }
          return true;
    }

    @Override
    public Boolean deleteDataGrid(String tableName, List<DDataGrid> domains) {
        for (DDataGrid domain : domains) {
            dao.delete(tableName, domain);
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> getFullFileNameHelpManual(String filename) {
        //return fileDao.getFilePath() + '/' + filename;
        return fileDao.getFileHelpManualPath(filename);
    }

    @Override
    public DDataGrid getDataGrid(String tableName, Long id) {
        return dao.selectRow(tableName, id);
    }

    @Override
    public DDataGrid getDataGridById(String tableName, DDataGrid domain) {
        return dao.selectRow(tableName, domain);
    }

    @Override
    public DDataGrid getDataGridSumm(String tableName, DataGridSearchCondition condition) {
        return dao.selectSumm(tableName, condition);
    }
    
//    @Override
//    public Boolean getCheckBoxState(int userId, String tablesNames) {
//        return dao.getCheckBoxState(userId, tablesNames);
//    }
    
    @Override
    public void updateColumnsState (int userId, String tablesNames, String columnsStates) {
        dao.updateColumnsState(userId, tablesNames, columnsStates);
    }
    
    @Override
    public void deleteColumnsState (int userId, String tablesNames) {
        dao.deleteColumnsState(userId, tablesNames);
    }
    
    @Override
    public String getColumnsState (int userId, String tablesNames) {
        return dao.getColumnsState(userId, tablesNames);
    }
}
