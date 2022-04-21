package pst.arm.server.modules.ganttchart.dao;

import java.util.List;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;

public interface GanttChartMartDAO {

    public List<Order> getAllOrders();

    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition);

    public List<Job> getAllJobs(GanttChartSearchCondition condition);

    public List<Job2> getAllJobs2(GanttChartSearchCondition condition);
}
