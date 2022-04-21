package pst.arm.server.modules.ganttchart.service.gwt;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.ganttchart.domain.Job;
import pst.arm.client.modules.ganttchart.domain.Job2;
import pst.arm.client.modules.ganttchart.domain.JobPlan;
import pst.arm.client.modules.ganttchart.domain.Order;
import pst.arm.client.modules.ganttchart.domain.search.GanttChartSearchCondition;
import pst.arm.client.modules.ganttchart.service.remote.GWTGanttChartPriboyService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.ganttchart.service.GanttChartPriboyService;

/**
 *
 * @author nikita
 */
@Service("GWTGanttChartPriboyService")
public class GWTGanttChartPriboyServiceImpl extends GWTController implements GWTGanttChartPriboyService {

    private GanttChartPriboyService ganttChartService;
    private static Logger log = Logger.getLogger("GWTGanttChartPriboyServiceImpl");

    @Override
    public List<Order> getAllOrders() throws RpcServiceException {
        try {
//            log.warn(ganttChartService.getAllOrders()+"---------------------------------------------");
            return ganttChartService.getAllOrders();
        } catch (Exception ex) {
//            log.warn("++++++++++++++++++++" + ex.getMessage());
            throw new RpcServiceException("pst.arm.server.modules.ganttchart.service.gwt.GWTGanttChartPriboyServiceImpl Ошибка при получении списка заказов", ex.getMessage());
        }
    }

    @Override
    public List<JobPlan> getAllJobPlans(GanttChartSearchCondition condition) throws RpcServiceException {
        try {

            return ganttChartService.getAllJobPlans(condition);
        } catch (Exception ex) {
//            log.warn("++++++++++++++++++++" + ex.getMessage());
            throw new RpcServiceException("pst.arm.server.modules.ganttchart.service.gwt.GWTGanttChartPriboyServiceImpl Ошибка при получении списка плановых операций", ex.getMessage());
        }
    }

    @Override
    public List<Job> getAllJobs(GanttChartSearchCondition condition) throws RpcServiceException {
        try {

            return ganttChartService.getAllJobs(condition);
        } catch (Exception ex) {
//            log.warn("++++++++++++++++++++" + ex.getMessage());
            throw new RpcServiceException("pst.arm.server.modules.ganttchart.service.gwt.GWTGanttChartPriboyServiceImpl Ошибка при получении списка работ", ex.getMessage());
        }
    }
    
    @Override
    public List<Job2> getAllJobs2(GanttChartSearchCondition condition) throws RpcServiceException {
        try {

            return ganttChartService.getAllJobs2(condition);
        } catch (Exception ex) {
            log.warn("++++++++++++++++++++" + ex.getMessage());
            throw new RpcServiceException("pst.arm.server.modules.ganttchart.service.gwt.GWTGanttChartPriboyServiceImpl Ошибка при получении списка работ", ex.getMessage());
        }
    }

    @Autowired
    public void setGanttChartService(GanttChartPriboyService ganttChartService) {

        this.ganttChartService = ganttChartService;
    }
}
