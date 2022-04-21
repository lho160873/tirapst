package pst.arm.server.modules.datagrid.service.gwt;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.server.common.DTableMapManager;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.DDataGridService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.commons.io.IOUtils;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import static pst.arm.server.utils.ServerUtil.isNotEmpty;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
@Service("GWTDDataGridService")
public class GWTDDataGridServiceImpl extends GWTController implements GWTDDataGridService {

    private DDataGridService service;
    private static Logger log = Logger.getLogger("GWTDDataGridServiceImpl");

    @Autowired
    public void setDataGridService(DDataGridService service) {
        this.service = service;
    }

    @Override
    public List<DDataGrid> getDataGrid(String tableName, DataGridSearchCondition condition) throws RpcServiceException {
        try {
            log.warn("GWTDDataGridServiceImpl::getDataGrid");

            return service.getDataGrid(tableName, condition);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getDataGrid", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public List<DDataGrid> getAllDataGrid(String tableName) throws RpcServiceException {
        try {
            return service.getAllDataGrid(tableName);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getAllDataGrid", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public Integer updateOfficeDocPeriods() {

        List<DDataGrid> result = service.getAllDataGrid("DOC_EXEC_PERIOD_VO_JOIN_OFFICE_DOC");
        Integer updatedPeriods = 0;
        for (DDataGrid domain : result) {
            Timestamp planTS = (Timestamp) (domain.getRows().get(new SKeyForColumn("MAIN:DATE_PLAN")).getVal());
            Date plan = new Date(planTS.getTime());
            Date curr = new Date();
            if (plan.before(curr)) {
                DDataGrid targetDom = new DDataGrid();

                SKeyForColumn key;
                IRowColumnVal val;

                val = new DRowColumnValNumber();
                val.setVal(null);
                key = new SKeyForColumn("MAIN:OFFICE_DOC_CONTR_ID");
                targetDom.getRows().put(key, val);

                //key = new SKeyForColumn("MAIN:PARENT_DOC_CONTR_ID");
                //targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:OFFICE_DOC_ID");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:RECIPIENT_ID");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:DOC_LINE");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:POSS_STEP_ID");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:COMMENT");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:USER_ID");
                targetDom.getRows().put(key, domain.getRows().get(key));

                key = new SKeyForColumn("MAIN:DATE_PLAN");
                SKeyForColumn nKey = new SKeyForColumn("MAIN:DATE_NEXT");
                targetDom.getRows().put(key, domain.getRows().get(nKey));

                key = new SKeyForColumn("MAIN:DATE_CORR");
                val = new DRowColumnValDate();
                val.setVal(curr);
                targetDom.getRows().put(key, val);

                key = new SKeyForColumn("MAIN:DATE_FACT");
                val = new DRowColumnValDate();
                val.setVal(null);
                targetDom.getRows().put(key, val);

                service.saveDataGrid("OFFICE_DOC_CONTR_VO", targetDom, targetDom, true);

                key = new SKeyForColumn("MAIN:PERIOD_TYPE_ID");
                Integer type = (Integer) domain.getRows().get(key).getVal();

                key = new SKeyForColumn("MAIN:PERIOD_VALUE");
                Integer periodVal = (Integer) domain.getRows().get(key).getVal();

                key = new SKeyForColumn("MAIN:DATE_PLAN");
                domain.getRows().get(key).setVal(domain.getRows().get(nKey).getVal());

                Date next1 = (Date) (domain.getRows().get(nKey).getVal());
                Date next = (Date) next1.clone();

                if (type == 1) {
                    CalendarUtil.addMonthsToDate(next, 12);
                } else if (type == 2) {
                    CalendarUtil.addMonthsToDate(next, periodVal);
                } else if (type == 3) {
                    CalendarUtil.addDaysToDate(next, periodVal * 7);
                }

                key = new SKeyForColumn("MAIN:DATE_NEXT");
                domain.getRows().get(key).setVal(next);

                service.saveDataGrid("DOC_EXEC_PERIOD_VO", domain, domain, false);
                updatedPeriods++;
            }
        }

        return updatedPeriods;
    }

//используется
    @Override
    public PagingLoadResult<DDataGrid> getPage(String tableName, DataGridSearchCondition condition) throws RpcServiceException {
        try {
            List<DDataGrid> res = service.getDataGridPage(tableName, condition);
            int totalCount = service.getDataGridCount(tableName, condition);
            return new BasePagingLoadResult<DDataGrid>(res, condition.getOffset(), totalCount);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getPage", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public PagingLoadResult<DDataGrid> getPage(String tableName, DataGridSearchCondition condition, DCondition addCnd) throws RpcServiceException {
        try {
            if (addCnd != null) {
                IRowColumnVal val = new IRowColumnVal();
                val.setVal(addCnd.getVal());
                condition.getFilters().put(addCnd.getKey(), val);
            }

            List<DDataGrid> res = service.getDataGridPage(tableName, condition);
            int totalCount = service.getDataGridCount(tableName, condition);
            return new BasePagingLoadResult<DDataGrid>(res, condition.getOffset(), totalCount);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getPage", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public List<DDataGrid> getTreeChildAllParents(DDataGrid row, String tableName) {
        log.warn("GWTDDataGridServiceImpl :: getTreeChildAllParents");
        List<DDataGrid> all = service.getAllDataGrid(tableName);
        List<DDataGrid> parentList = new ArrayList<DDataGrid>();

        DTable table = DTableMapManager.getInstance().getTable(tableName);

        IRowColumnVal pid = row.getRows().get(table.getParentKeyID());
        while (pid.getVal() != null) {
            for (DDataGrid el : all) {
                if (el.getRows().get(table.getKeyID()).getVal().equals(pid.getVal())) {
                    parentList.add(el);
                    pid = el.getRows().get(table.getParentKeyID());
                    break;
                }
            }
        }
        return parentList;
    }

    @Override
    public List<DDataGrid> getTreeChildrenData(DDataGrid row, String tableName, DataGridSearchCondition condition) throws RpcServiceException {
        try {
            log.warn("GWTDDataGridServiceImpl::getTreeChildrenData begin");
            List<DDataGrid> res = service.getDataGrid(tableName, condition);
            List<DDataGrid> all = service.getAllDataGrid(tableName);

            List<DDataGrid> children = new ArrayList<DDataGrid>();

            DTable table = DTableMapManager.getInstance().getTable(tableName);


            SKeyForColumn key = table.getKeyID();
            SKeyForColumn parentKey = table.getParentKeyID();

            if (row == null) {
                for (DDataGrid r : all) {
                    if (r.getRows().containsKey(parentKey)) {
                        IColumnBuilder builder = table.getColumnBuilder(parentKey);
                        String parentKeyVal = r.getRows().get(parentKey).getStringFromVal(parentKey, builder);

                        if (parentKeyVal == null) {
                            r.setHasChildren(hasChildren(table, r, all));
                            children.add(r);
                        }
                    }
                }
            } else {
                IColumnBuilder builderCurrent = table.getColumnBuilder(key);
                String currentKeyVal = row.getRows().get(key).getStringFromVal(key, builderCurrent);

                for (DDataGrid r : all) {
                    if (r.getRows().containsKey(parentKey)) {
                        IColumnBuilder builder = table.getColumnBuilder(parentKey);
                        String parentKeyVal = r.getRows().get(parentKey).getStringFromVal(parentKey, builder);

                        if (parentKeyVal != null && currentKeyVal.equals(parentKeyVal)) {
                            r.setHasChildren(hasChildren(table, r, all));
                            children.add(r);
                        }
                    }
                }
            }
            for (DDataGrid r : children) {
                log.warn("getName = " + r.getName());
                log.warn("getHasChildren = " + r.getHasChildren().toString());
            }
            log.warn("GWTDDataGridServiceImpl::getTreeChildrenData end");
            if ((condition.getSearches().size() != 0) || (condition.getFilters().size() != 0)) {
                Boolean flag;
                List<DDataGrid> hasFilteredRows = new ArrayList<DDataGrid>();
                for (DDataGrid p : children) {
                    flag = false;
                    IColumnBuilder builderP = table.getColumnBuilder(table.getPk());
                    String val = p.getRows().get(table.getPk()).getStringFromVal(table.getPk(), builderP);

                    for (DDataGrid f : res) {
                        IColumnBuilder builder2 = table.getColumnBuilder(table.getPk());
                        String val2 = f.getRows().get(table.getPk()).getStringFromVal(table.getPk(), builder2);
                        if (val.equals(val2)) {
                            hasFilteredRows.add(p);
                            flag = true;
                            break;
                        }
                    }


                    if (table.getQueryName().equals("DEPART_STRUCTURE_N")) {
                        if (condition.getFilters().get(new SKeyForColumn("MAIN:DEPART_ID")) != null) {
                            Integer va1 = (Integer) p.getRows().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal();
                            if (va1.equals(Integer.parseInt((String) condition.getFilters().get(new SKeyForColumn("MAIN:DEPART_ID")).getVal()))) {
                            } else {
                                if (!flag && hasFiltered(p, all, res, table)) {
                                    hasFilteredRows.add(p);
                                }
                            }
                        }
                    } else {
                        if (!flag && hasFiltered(p, all, res, table)) {
                            hasFilteredRows.add(p);
                        }
                    }
                }
                return hasFilteredRows;
            } else {
                return children;
            }

        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getTreeChildrenData", ex.getMessage(), tableMainName);
        }
    }

    private Boolean hasFiltered(DDataGrid p, List<DDataGrid> list, List<DDataGrid> filtered, DTable table) {
        SKeyForColumn parentKey = table.getParentKeyID();
        SKeyForColumn key = table.getKeyID();
        IColumnBuilder builderCurrent = table.getColumnBuilder(key);
        String currentKeyVal = p.getRows().get(key).getStringFromVal(key, builderCurrent);

        for (DDataGrid el : list) {
            IColumnBuilder builder = table.getColumnBuilder(parentKey);
            String parentKeyVal = el.getRows().get(parentKey).getStringFromVal(parentKey, builder);

            if (parentKeyVal != null && parentKeyVal.equals(currentKeyVal)) {
                IColumnBuilder builderEl = table.getColumnBuilder(table.getPk());
                String val = el.getRows().get(table.getPk()).getStringFromVal(table.getPk(), builderEl);
                for (DDataGrid f : filtered) {
                    IColumnBuilder builder2 = table.getColumnBuilder(table.getPk());
                    String val2 = f.getRows().get(table.getPk()).getStringFromVal(table.getPk(), builder2);
                    if (val.equals(val2)) {
                        return true;
                    }
                }

                if (hasFiltered(el, list, filtered, table)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Boolean hasChildren(DTable table, DDataGrid row, List<DDataGrid> rows) {
        SKeyForColumn parentKey = table.getParentKeyID();
        SKeyForColumn key = table.getKeyID();
        IColumnBuilder builderCurrent = table.getColumnBuilder(key);
        String currentKeyVal = row.getRows().get(key).getStringFromVal(key, builderCurrent);

        for (DDataGrid g : rows) {
            IColumnBuilder builder = table.getColumnBuilder(parentKey);
            String parentKeyVal = g.getRows().get(parentKey).getStringFromVal(parentKey, builder);

            if (parentKeyVal != null && parentKeyVal.equals(currentKeyVal)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public DDataGrid save(DDataGrid domain, Boolean isNew) {
        throw new UnsupportedOperationException("Not supported yet.");
    } 
    
    @Override
    public Boolean insert(String tableName, List<DDataGrid> domains) throws RpcServiceException{
        try {
            return service.insertDataGrid(tableName, domains);
        } catch (DuplicateKeyException dk) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            throw new RpcServiceException((table.getDuplicateKeyError() == null || table.getDuplicateKeyError().isEmpty()) ? "Ошибка сохранения" : table.getDuplicateKeyError(), dk.getMessage(), table.getName());
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            throw new RpcServiceException("Error save", ex.getMessage(), table.getName());
            //     throw new RpcServiceException(getOracleError(ex, tableName), ex.getMessage());
        }
    }

    @Override
    public DDataGrid save(String tableName, DDataGrid domain, DDataGrid oldDomain, Boolean isNew) throws RpcServiceException {
        try {
            DDataGrid c = service.saveDataGrid(tableName, domain, oldDomain, isNew);
            c = service.getDataGridById(tableName, domain); //после добавления(редактирования) получаем данные в полном объеме 
            return c;
        } catch (DuplicateKeyException dk) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            throw new RpcServiceException((table.getDuplicateKeyError() == null || table.getDuplicateKeyError().isEmpty()) ? "Ошибка сохранения" : table.getDuplicateKeyError(), dk.getMessage(), table.getName());
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            throw new RpcServiceException("Error save", ex.getMessage(), table.getName());
            //     throw new RpcServiceException(getOracleError(ex, tableName), ex.getMessage());
        }
    }
    

    @Override
    public DDataGrid getDomainById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDataGrid getDataGridById(String tableName, DDataGrid domain) throws RpcServiceException {
        try {
            return service.getDataGridById(tableName, domain);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getDomainById", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public DDataGrid getDomainById(String tableName, Long id) throws RpcServiceException {
        try {
            return service.getDataGrid(tableName, id);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getDomainById", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public Boolean deleteDomains(String tableName, List<DDataGrid> domains) throws RpcServiceException {
        try {
            log.warn("GWTDDataGridServiceImpl :: deleteDomains start");
            return service.deleteDataGrid(tableName, domains);
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();

            if (ex.getCause() instanceof SQLException) {
                throw new RpcServiceException("Error deleteDomains", ex.getMessage(), tableMainName,
                        new Integer(((SQLException) ex.getCause()).getErrorCode()));
            } else {
                throw new RpcServiceException("Error deleteDomains", ex.getMessage(), tableMainName);
            }
        }
    }

    @Override
    public Boolean deleteDomainsByIds(List<Integer> ids) {
        throw new UnsupportedOperationException("Not supported yet.");

    }
    
  

    @Override
    public DTable getTable(String tableName) throws RpcServiceException {
        try {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            table.initBuilders(this);
            return table;
        } catch (Exception ex) {
            DTable table = DTableMapManager.getInstance().getTable(tableName);
            String tableMainName = table.getName();
            throw new RpcServiceException("Error getTable", ex.getMessage(), tableMainName);
        }
    }

    @Override
    public Map< String, DTable> getTableMap() throws RpcServiceException {
        try {
            return DTableMapManager.getInstance().getTableMap();
        } catch (Exception ex) {

            throw new RpcServiceException("Error getTable", ex.getMessage());
        }

    }

    @Override
    public Boolean openHelpManual(String filename) throws RpcServiceException {
        try {
            log.warn("GWTDDataGridServiceImpl::openHelpManual filename = " + filename);

            List<Map<String, Object>> list = service.getFullFileNameHelpManual(filename);
            if (!isNotEmpty(list)) {
                return false;
            }
            String path = (String) list.get(0).get("SRVR_NAME") + "/" + (String) list.get(0).get("ROOT_PATH");
            String name = (String) list.get(0).get("FILENAME");
            String ext = (String) list.get(0).get("EXTENSION");

            log.warn("GWTDDataGridServiceImpl::openHelpManual path = " + path);
            log.warn("GWTDDataGridServiceImpl::openHelpManual name = " + name);
            log.warn("GWTDDataGridServiceImpl::openHelpManual ext = " + ext);
            String fullFileName = path + "/" + name + ".dat";
            SmbFile smbFile = new SmbFile("smb://" + fullFileName);
            //SmbFile smbFile = new SmbFile("smb://" + server + "/" + path + "/" + filename );
            SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFile);

            FileObjectDescriptor fod = new FileObjectDescriptor(IOUtils.toByteArray(smbFileInputStream));
            fod.setFileName(name);
            fod.setFileExt(ext.replace(".", ""));
            smbFileInputStream.close();

            getSession().setAttribute(filename, fod);
            return true;


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            throw new RpcServiceException("Error openHelpManual " + filename, ex.getMessage(),filename);
        }
    }

    @Override
    public Boolean execProc(String sql) throws RpcServiceException {
        try{
        return service.execProcedure(sql);
        }
        catch (Exception ex) {
            throw new RpcServiceException("Error execProc", ex.getMessage(),sql);
        }
    }

    @Override
    public Boolean execProc(String sql, String dbName) throws RpcServiceException {
        try
        {
        return service.execProcedure(sql, dbName);
        }
        catch (Exception ex) {
            throw new RpcServiceException("Error execProc", ex.getMessage(), sql);
        }
    }

    @Override
    public DDataGrid getDataGridSumm(String tableName, DataGridSearchCondition condition) throws RpcServiceException {
        try {
            log.warn("GWTDDataGridServiceImpl::getDataGridSumm");

            return service.getDataGridSumm(tableName, condition);
        } catch (Exception ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            String error = errors.toString();
            log.warn("Error getDataGridSumm stacktrace: " + error);
            throw new RpcServiceException("Error getDataGridSumm", ex.getMessage(),tableName);
        }
    }
    
//    @Override
//    public Boolean getCheckBoxState(int userId, String tablesNames) throws RpcServiceException {
//        log.warn("GWTDDataGridServiceImpl::getCheckBoxState");
//        return service.getCheckBoxState(userId, tablesNames);
//    }
    
    @Override
    public void updateColumnsState (int userId, String tablesNames, String columnsStates)  throws RpcServiceException {
        log.warn("GWTDDataGridServiceImpl::updateColumnsState");
        service.updateColumnsState(userId, tablesNames, columnsStates);
    }
    
     @Override
    public void deleteColumnsState (int userId, String tablesNames)  throws RpcServiceException {
        log.warn("GWTDDataGridServiceImpl::deleteColumnsState");
        service.deleteColumnsState(userId, tablesNames);
    }
    
    @Override
    public String getColumnsState (int userId, String tablesNames)  throws RpcServiceException {
        log.warn("GWTDDataGridServiceImpl::getColumnsState");
        return service.getColumnsState(userId, tablesNames);
    }
}
