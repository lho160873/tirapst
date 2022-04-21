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
import pst.arm.server.modules.ganttchart.dao.GanttChartMartDAO;
import pst.arm.server.modules.ganttchart.service.GanttChartPriboyService;
import pst.arm.server.modules.ganttchart.service.GanttChartMartService;

/**
 *
 * @author nikita
 */
@Service
public class GanttChartMartServiceImpl implements GanttChartMartService {

    private GanttChartMartDAO ganttChartMartDAO;
    private static Logger logger = Logger.getLogger("GanttChartMartServiceImpl");

    @Override
    public List<Order> getAllOrders() {
//        logger.warn(ganttChartMartDAO.getAllOrders().size() + " ==========================================");
        return ganttChartMartDAO.getAllOrders();
    }

    @Override
    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition) {

        return ganttChartMartDAO.getAllJobPlans(condition);
    }

    @Override
    public List<Job> getAllJobs(GanttChartSearchCondition condition) {

        return ganttChartMartDAO.getAllJobs(condition);
    }

    @Autowired
    public void setGanttChartDAO(GanttChartMartDAO ganttChartDAO) {

        this.ganttChartMartDAO = ganttChartDAO;
    }

    @Override
    public List<Job2> getAllJobs2(GanttChartSearchCondition condition) {
        logger.warn(ganttChartMartDAO.getAllJobs2(condition).size() + " ==========================================");
        
        return ganttChartMartDAO.getAllJobs2(condition);
    }
}
