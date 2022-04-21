package pst.arm.client.modules.sync1c.domain.search;

import java.io.Serializable;
import pst.arm.client.modules.sync1c.domain.Worker1C;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class WorkerSearchCondition implements Serializable {

    public enum SearchType {

        base, extended
    }
    private Worker1C worker1C;
    private SearchType searchType = SearchType.base;
    private Integer postWorkerId;

    public WorkerSearchCondition() {
    }

    public WorkerSearchCondition(Worker1C worker1C, SearchType searchType) {
        this.worker1C = worker1C;
        this.searchType = searchType;

    }

    public WorkerSearchCondition(Worker1C worker1C) {
        this.worker1C = worker1C;
    }

    public Integer getPostWorkerId() {
        return postWorkerId;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Worker1C getWorker1C() {
        return worker1C;
    }

    public void setPostWorkerId(Integer postWorkerId) {
        this.postWorkerId = postWorkerId;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public void setWorker1C(Worker1C worker1C) {
        this.worker1C = worker1C;
    }

}
