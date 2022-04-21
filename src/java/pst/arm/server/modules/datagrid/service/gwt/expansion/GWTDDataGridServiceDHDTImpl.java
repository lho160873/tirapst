package pst.arm.server.modules.datagrid.service.gwt.expansion;

import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValNumber;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridServiceDHDT;
import pst.arm.client.modules.datagrid.service.remote.GWTDataGridFileOpenService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.datagrid.service.DDataGridService;
import pst.arm.server.modules.datagrid.service.expansion.DDataGridServiceDHDT;

@Service("GWTDDataGridServiceDHDT")
public class GWTDDataGridServiceDHDTImpl extends GWTController implements GWTDDataGridServiceDHDT {

    @Autowired
    DDataGridService gridService;
    
    @Autowired
    DDataGridServiceDHDT gridServiceDHDT;
    
    private static Logger log = Logger.getLogger("GWTDDataGridServiceDHDTImpl");
   
    @Override
    public String updateJobs(Integer orderID) {
        try {
            DataGridSearchCondition condition = new DataGridSearchCondition();
            DRowColumnValNumber oId = new DRowColumnValNumber();
            oId.setVal(orderID);
            condition.getFilters().put(new SKeyForColumn("MAIN:ID"), oId);

            DRowColumnValNumber recId = new DRowColumnValNumber();
            recId.setVal(0);

            List<DDataGrid> res = gridService.getDataGrid("DH_ORDER_FULL_VO", condition);

            DRowColumnValNumber intId = new DRowColumnValNumber();
            intId.setVal(4);
            res.get(0).getRows().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), intId);
            res.get(0).setName("DH_ORDER_1_FULL_VO");

            res.get(0).getRows().remove(new SKeyForColumn("MAIN:ID"));
            res.get(0).getRows().put(new SKeyForColumn("MAIN:id"), oId);
            res.get(0).getRows().put(new SKeyForColumn("MAIN:ID_ODER_REC"), recId);

            condition = new DataGridSearchCondition();
            condition.getFilters().put(new SKeyForColumn("MAIN:id"), oId);

            List<DDataGrid> res2 = gridService.getDataGrid("DH_ORDER_1_FULL_VO", condition);
            if (res2.size() > 0)
                gridService.deleteDataGrid("DH_ORDER_1_FULL_VO", res2);

            gridService.saveDataGrid("DH_ORDER_1_FULL_VO", res.get(0), res.get(0), true);

            condition = new DataGridSearchCondition();
            condition.getFilters().put(new SKeyForColumn("MAIN:id_order"), oId);

            res2 = gridService.getDataGrid("DH_JOB_1_VO", condition);
            if (res2.size() > 0)
                gridService.deleteDataGrid("DH_JOB_1_VO", res2);

            List<DDataGrid> jobs = gridService.getDataGrid("DH_JOB_VO", condition);
            for (DDataGrid d : jobs) {
                d.setName("DH_JOB_1_VO");
                d.getRows().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), intId);
                d.getRows().put(new SKeyForColumn("MAIN:ID_JOB_REC"), recId);

                gridService.saveDataGrid("DH_JOB_1_VO", d, d, true);

                condition = new DataGridSearchCondition();
                DRowColumnValNumber val = new DRowColumnValNumber();
                val.setVal((Integer) d.getRows().get(new SKeyForColumn("MAIN:id")).getVal());

                condition.getFilters().put(new SKeyForColumn("MAIN:id"), val);

                res2 = gridService.getDataGrid("DT_JOB_PLAN_1_VO", condition);
                if (res2.size() > 0)
                    gridService.deleteDataGrid("DT_JOB_PLAN_1_VO", res2);

                List<DDataGrid> jobPlans = gridService.getDataGrid("DT_JOB_PLAN_VO", condition);
                for (DDataGrid j : jobPlans) {
                    j.setName("DT_JOB_PLAN_1_VO");
                    j.getRows().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), intId);
                    j.getRows().put(new SKeyForColumn("MAIN:ID_REC"), recId);
                    gridService.saveDataGrid("DT_JOB_PLAN_1_VO", j, j, true);
                }

                res2 = gridService.getDataGrid("DT_JOB_FACT_1_VO", condition);
                if (res2.size() > 0)
                    gridService.deleteDataGrid("DT_JOB_FACT_1_VO", res2);

                List<DDataGrid> jobFacts = gridService.getDataGrid("DT_JOB_FACT_VO", condition);
                for (DDataGrid j : jobFacts) {
                    j.setName("DT_JOB_FACT_1_VO");
                    j.getRows().put(new SKeyForColumn("MAIN:INTERACTING_SYST_ID"), intId);
                    j.getRows().put(new SKeyForColumn("MAIN:ID_REC"), recId);
                    gridService.saveDataGrid("DT_JOB_FACT_1_VO", j, j, true);
                }
            }
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "good";
    }

    @Override
    public Boolean calcCosts(Integer interactingSystId, Integer orderID) throws RpcServiceException{
        try {
            log.warn("GWTDDataGridServiceDHDTImpl calcCosts " + interactingSystId.toString()+", "+orderID.toString());
            gridServiceDHDT.calcCosts(interactingSystId, orderID);           
        } catch (Exception e) {
             throw new RpcServiceException("Error calcCosts", e.getMessage());
        }
        return true;
    }
}
