package pst.arm.server.modules.ganttchart.dao.jdbc;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;
import pst.arm.server.modules.ganttchart.dao.GanttChartMartDAO;
import pst.arm.server.modules.ganttchart.dao.jdbc.mappers.Job2GanttChartMapper;
import pst.arm.server.modules.ganttchart.dao.jdbc.mappers.JobGanttChartMapper;
import pst.arm.server.modules.ganttchart.dao.jdbc.mappers.JobPlanGanttChartMapper;
import pst.arm.server.modules.ganttchart.dao.jdbc.mappers.OrderGanttChartMapper;

public class GanttChartMartDAOJdbcImpl extends JdbcDaoSupport implements GanttChartMartDAO {

    private static Logger log = Logger.getLogger("GanttChartMartDAOJdbcImpl");
    private TransactionTemplate txTemplate;

    public void setTransactionManager(PlatformTransactionManager txManager) {

        txTemplate = new TransactionTemplate(txManager);
        txTemplate.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
    }

    @Override
    public List<Order> getAllOrders() {
//        sql = "SELECT DH_ORDER.ID AS id,(select numdoc from dh_contract where dh_contract.id=DH_ORDER.id_contract) AS contract,DH_ORDER.numdoc, "
//                + " DH_ORDER.datestart,  DH_ORDER.dateend, SPR_DEPART.name AS companyname, "
//                + " DH_ORDER.sumdoc, SPR_NOMENCLATURE.name AS ordername, SPR_DEPART_1.name AS departname, DH_ORDER.type_exec AS typeexec,DH_ORDER.info,DH_ORDER.urgency"
//                + " FROM ((DH_ORDER LEFT JOIN SPR_DEPART ON DH_ORDER.id_depart = SPR_DEPART.id) "
//                + " LEFT JOIN SPR_NOMENCLATURE ON DH_ORDER.id_nomenclature = SPR_NOMENCLATURE.id)"
//                + " LEFT JOIN SPR_DEPART AS SPR_DEPART_1 ON DH_ORDER.id_customer = SPR_DEPART_1.id"
//                + " WHERE (((DH_ORDER.type_exec)<4)AND DH_ORDER.IS_INTERNAL=0 AND DH_ORDER.ID  in (select distinct ID_order from DH_job )) order by DH_ORDER.numdoc;";
        String sql = "SELECT DH_ORDER.id, SPR_NOMENCLATURE.name, DH_ORDER.datestart,  DH_ORDER.dateend, DH_ORDER.id_depart, "
                + "DH_ORDER.type_exec, DH_ORDER.quantity_plan, DH_ORDER.cost, DH_ORDER.is_internal, "
                + "DH_ORDER.is_complete FROM DH_ORDER LEFT JOIN SPR_NOMENCLATURE ON DH_ORDER.id_nomenclature = SPR_NOMENCLATURE.id "
                + "WHERE DH_ORDER.id  in (select distinct id_order from DH_JOB) order by DH_ORDER.id;";
        // DH_ORDER.type_exec < 4 AND DH_ORDER.is_internal=0 AND 
//        log.warn("GanttChartDAOJdbcImpl::getAllOrders  = " + sql);
        return getJdbcTemplate().query(sql, new OrderGanttChartMapper());
    }

    @Override
    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition) {
//        String sql = "SELECT DT_JOB_PLAN.id, DT_JOB_PLAN.[lineno], DT_JOB_PLAN.price, DT_JOB_PLAN.date_start AS datestart, "
//                + "DT_JOB_PLAN.date_end AS dateend, DT_JOB_PLAN.preparetime, DT_JOB_PLAN.ntime,SPR_OPERKIND.Name AS name, SPR_OPERKIND.id_depart AS iddepart, "
//                + "DH_JOB.quantity_plan AS quantityplan, DT_JOB_PLAN.id_operkind AS idoperkind, SPR_DEPART.parent_id AS parentid "
//                + "FROM (DH_JOB INNER JOIN (DT_JOB_PLAN INNER JOIN SPR_OPERKIND ON DT_JOB_PLAN.id_operkind = SPR_OPERKIND.id) "
//                + "ON DH_JOB.id = DT_JOB_PLAN.id) "
//                + "INNER JOIN SPR_DEPART ON SPR_OPERKIND.id_depart = SPR_DEPART.id "
//                + "WHERE DH_JOB.id_order = " + id
//                + " ORDER BY DT_JOB_PLAN.id, DT_JOB_PLAN.[lineno];";
//        String sql = "SELECT DT_JOB_PLAN.id, DT_JOB_PLAN.[lineno], DT_JOB_PLAN.price, "
//                + "DT_JOB_PLAN.date_start, DT_JOB_PLAN.date_end, DT_JOB_PLAN.ntime, SPR_OPERKIND.Name AS name, DT_JOB_PLAN.preparetime, "
//                + "DT_JOB_PLAN.unittime, DT_JOB_PLAN.id_worker "
//                + "FROM DH_JOB INNER JOIN (DT_JOB_PLAN INNER JOIN SPR_OPERKIND ON DT_JOB_PLAN.id_operkind = SPR_OPERKIND.id) ON DH_JOB.id = DT_JOB_PLAN.id "
//                + "WHERE DH_JOB.id_order = "
//                + ((condition != null) ? condition.getId() : 0)
//                + " ORDER BY DT_JOB_PLAN.id, DT_JOB_PLAN.[lineno];";
        String sql = "SELECT DT_JOB_FACT.id, DT_JOB_FACT.[lineno], DT_JOB_FACT.price, DT_JOB_FACT_WORKER.datestart AS date_start, "+
                    "DT_JOB_FACT_WORKER.dateend AS date_end, DT_JOB_FACT.preparetime, DT_JOB_FACT.ntime,SPR_OPERKIND.Name AS name, SPR_OPERKIND.id_depart AS iddepart, "+
                    "DH_JOB.quantity_plan AS quantityplan, DT_JOB_FACT.id_operkind AS idoperkind, SPR_DEPART.parent_id AS parentid, DT_JOB_FACT.unittime, DT_JOB_FACT_WORKER.id_worker "+
                    "FROM (DH_JOB INNER JOIN (DT_JOB_FACT INNER JOIN SPR_OPERKIND ON DT_JOB_FACT.id_operkind = SPR_OPERKIND.id) "+
                    "ON DH_JOB.id = DT_JOB_FACT.id) "+
                    "INNER JOIN SPR_DEPART ON SPR_OPERKIND.id_depart = SPR_DEPART.id "+
                    "INNER JOIN DT_JOB_FACT_WORKER ON (DT_JOB_FACT.id=DT_JOB_FACT_WORKER.id AND DT_JOB_FACT.[lineno] = DT_JOB_FACT_WORKER.line_operation AND DT_JOB_FACT_WORKER.line=1) "+
                    "WHERE DH_JOB.id_order = "+ ((condition != null) ? condition.getId() : 0) +
                    " ORDER BY DT_JOB_FACT.id, DT_JOB_FACT.[lineno];";
                
               
//        log.warn("GanttChartDAOJdbcImpl::getAllJobPlans  = " + sql);
        return getJdbcTemplate().query(sql, new JobPlanGanttChartMapper());
    }

    @Override
    public List<Job> getAllJobs(GanttChartSearchCondition condition) {
//        String sql = "SELECT DH_JOB.ID AS id, DH_JOB.numdoc, DH_JOB.id_nomenclature AS idnomenclature, DH_JOB.ID_DESIGNVERSION AS iddesignversion, DH_JOB.id_order AS idorder, "
//                + "DH_JOB.ID_depart AS iddepart, DH_JOB.id_parentjob AS idparentjob, SPR_NOMENCLATURE.name, DH_JOB.quantity_plan AS quantityplan, DH_JOB.date_start AS datestart, "
//                + "DH_JOB.date_end AS dateend, DH_JOB.info, DH_JOB.sumdoc, DT_JOB_INTERNAL_RELATIONS.id_internalJob AS idinternaljob,DH_JOB.urgency "
//                + "FROM ((DH_JOB LEFT JOIN SPR_NOMENCLATURE ON DH_JOB.id_nomenclature = SPR_NOMENCLATURE.id) "
//                + "LEFT JOIN SPR_DEPART ON DH_JOB.id_depart = SPR_DEPART.id) LEFT JOIN DT_JOB_INTERNAL_RELATIONS ON "
//                + "DH_JOB.id = DT_JOB_INTERNAL_RELATIONS.id_relatedJob "
//                + "WHERE (((DH_JOB.id_order)=" + id + ")) order by  DH_JOB.id, DH_JOB.id_parentjob;";
        String sql = "SELECT DH_JOB.id, DH_JOB.id_order, DH_JOB.id_depart, DH_JOB.id_parentjob, "
                + "DH_JOB.quantity_plan, DH_JOB.date_start, DH_JOB.date_end, SPR_NOMENCLATURE.name, DH_JOB.sumdoc, DH_JOB.type_exec "
                + "FROM DH_JOB LEFT JOIN SPR_NOMENCLATURE ON DH_JOB.id_nomenclature = SPR_NOMENCLATURE.id WHERE DH_JOB.id_order="
                + ((condition != null) ? condition.getId() : 0)
                + " order by  DH_JOB.id, DH_JOB.id_parentjob;";
//        log.warn("GanttChartDAOJdbcImpl::getAllJobs  = " + sql);
        return getJdbcTemplate().query(sql, new JobGanttChartMapper());
    }
    
     @Override
    public List<Job2> getAllJobs2(GanttChartSearchCondition condition) {
//        String sql = "SELECT DH_JOB.ID AS id, DH_JOB.numdoc, DH_JOB.id_nomenclature AS idnomenclature, DH_JOB.ID_DESIGNVERSION AS iddesignversion, DH_JOB.id_order AS idorder, "
//                + "DH_JOB.ID_depart AS iddepart, DH_JOB.id_parentjob AS idparentjob, SPR_NOMENCLATURE.name, DH_JOB.quantity_plan AS quantityplan, DH_JOB.date_start AS datestart, "
//                + "DH_JOB.date_end AS dateend, DH_JOB.info, DH_JOB.sumdoc, DT_JOB_INTERNAL_RELATIONS.id_internalJob AS idinternaljob,DH_JOB.urgency "
//                + "FROM ((DH_JOB LEFT JOIN SPR_NOMENCLATURE ON DH_JOB.id_nomenclature = SPR_NOMENCLATURE.id) "
//                + "LEFT JOIN SPR_DEPART ON DH_JOB.id_depart = SPR_DEPART.id) LEFT JOIN DT_JOB_INTERNAL_RELATIONS ON "
//                + "DH_JOB.id = DT_JOB_INTERNAL_RELATIONS.id_relatedJob "
//                + "WHERE (((DH_JOB.id_order)=" + id + ")) order by  DH_JOB.id, DH_JOB.id_parentjob;";
        String sql = "SELECT DH_JOB.id, DH_JOB.id_order, DH_JOB.id_depart, DH_JOB.id_parentjob, DH_JOB.quantity_plan, SPR_NOMENCLATURE.name, DH_JOB.sumdoc, DH_JOB.numdoc, DH_JOB.type_exec, " +
                     "max(dateAdd(minute, DT_JOB_FACT_WORKER.ntime, DT_JOB_FACT_WORKER.dateEnd)) AS date_end, " +
                     "min(DT_JOB_FACT_WORKER.dateStart) AS date_start, (SELECT JOB.numdoc FROM DH_JOB AS JOB WHERE JOB.id = DH_JOB.id_parentjob) AS numdoc2, SPR_DEPART.name AS name2 " +
                     "FROM DH_JOB LEFT JOIN SPR_NOMENCLATURE ON DH_JOB.id_nomenclature = SPR_NOMENCLATURE.id LEFT JOIN DT_JOB_FACT_WORKER ON DT_JOB_FACT_WORKER.id = DH_JOB.id LEFT JOIN SPR_DEPART ON SPR_DEPART.id = DH_JOB.id_depart " +
                     "WHERE DH_JOB.id_order=" + ((condition != null) ? condition.getId() : 0) + " AND DH_JOB.type_exec = 3 " +
                     "group by DH_JOB.id, DH_JOB.id_order, DH_JOB.id_depart, DH_JOB.id_parentjob, DH_JOB.quantity_plan, SPR_NOMENCLATURE.name, DH_JOB.sumdoc, DH_JOB.numdoc, DH_JOB.type_exec, SPR_DEPART.name " +
                     "order by  DH_JOB.id, DH_JOB.id_parentjob;";
        
//        String sql2 = "SELECT max(dateAdd(minute, DT_JOB_FACT_WORKER.ntime, DT_JOB_FACT_WORKER.dateEnd)) FROM DT_JOB_FACT_WORKER WHERE DT_JOB_FACT_WORKER.id = 14746";
//        
//        log.warn(getJdbcTemplate().queryForRowSet(sql2)+"dfgdfgdfgdfgdf");
        log.warn("!!!!!!!!!!!!!!!!!!!!!!!GanttChartDAOJdbcImpl::getAllJobs  = " + sql);
        return getJdbcTemplate().query(sql, new Job2GanttChartMapper());
       
    }
}
