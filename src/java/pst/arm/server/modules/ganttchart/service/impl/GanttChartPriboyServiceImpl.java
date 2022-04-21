package pst.arm.server.modules.ganttchart.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;
import pst.arm.server.modules.ganttchart.dao.GanttChartPriboyDAO;
import pst.arm.server.modules.ganttchart.service.GanttChartPriboyService;

/**
 *
 * @author nikita
 */
@Service
public class GanttChartPriboyServiceImpl implements GanttChartPriboyService {

    private GanttChartPriboyDAO ganttChartPriboyDAO;
    private static Logger logger = Logger.getLogger("GanttChartPriboyServiceImpl");

    @Override
    public List<Order> getAllOrders() {
        logger.warn(ganttChartPriboyDAO.getAllOrders().size() + " ==========================================");
        return ganttChartPriboyDAO.getAllOrders();
    }

    @Override
    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition) {

        return ganttChartPriboyDAO.getAllJobPlans(condition);
    }

    @Override
    public List<Job> getAllJobs(GanttChartSearchCondition condition) {

        return ganttChartPriboyDAO.getAllJobs(condition);
    }

    @Autowired
    public void setGanttChartDAO(GanttChartPriboyDAO ganttChartDAO) {

        this.ganttChartPriboyDAO = ganttChartDAO;
    }

    @Override
    public List<Job2> getAllJobs2(GanttChartSearchCondition condition) {
        logger.warn(ganttChartPriboyDAO.getAllJobs2(condition).size() + " ==========================================");
        
        return ganttChartPriboyDAO.getAllJobs2(condition);
    }
}
