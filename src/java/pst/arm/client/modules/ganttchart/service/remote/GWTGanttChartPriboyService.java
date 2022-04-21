package pst.arm.client.modules.ganttchart.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;

/**
 *
 * @author nikita
 */
@RemoteServiceRelativePath("service/ganttChartPriboyService")
public interface GWTGanttChartPriboyService extends RemoteService {

    public List<Order> getAllOrders() throws RpcServiceException;

    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition) throws RpcServiceException;

    public List<Job> getAllJobs(GanttChartSearchCondition condition) throws RpcServiceException;
    
    public List<Job2> getAllJobs2(GanttChartSearchCondition condition) throws RpcServiceException;
}
