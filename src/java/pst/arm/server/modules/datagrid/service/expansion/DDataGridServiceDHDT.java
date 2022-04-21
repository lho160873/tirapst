package pst.arm.server.modules.datagrid.service.expansion;

import java.util.List;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;

public interface DDataGridServiceDHDT {
    public String updateJobs(Integer orderID);
     public Boolean calcCosts(Integer interactingSystId, Integer orderID);
}
