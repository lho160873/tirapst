package pst.arm.client.modules.ganttchart.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;

/**
 *
 * @author nikita
 */
public interface GWTGanttChartMartServiceAsync {

    public void getAllOrders(AsyncCallback<List<Order>> callback);

    public void getAllJobPlans(GanttChartSearchCondition condition, AsyncCallback<List<JobPlan>> callback);

    public void getAllJobs(GanttChartSearchCondition condition, AsyncCallback<List<Job>> callback);
    
    public void getAllJobs2(GanttChartSearchCondition condition, AsyncCallback<List<Job2>> callback);
}
