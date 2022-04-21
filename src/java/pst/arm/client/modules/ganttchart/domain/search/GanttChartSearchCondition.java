package pst.arm.client.modules.ganttchart.domain.search;

import pst.arm.client.common.domain.search.SearchConditionSimple;

/**
 *
 * @author nikita
 */
public class GanttChartSearchCondition extends SearchConditionSimple {

    private Integer id;

    /**
     * @return the id
     */
    public Integer getId() {

        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {

        this.id = id;
    }
}
