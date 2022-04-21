package pst.arm.client.modules.tablegrid.domain.search;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class GridSearchCondition implements Serializable{
    private Integer offset = 0;
    private Integer limit = 50;
    private SortDir sortDir = SortDir.NONE;
    private String sortFieldId = null;
    private Map<String, Object> filters = new HashMap<String, Object>();

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public SortDir getSortDir() {
        return sortDir;
    }

    public void setSortDir(SortDir sortDir) {
        this.sortDir = sortDir;
    }

    public String getSortFieldId() {
        return sortFieldId;
    }

    public void setSortFieldId(String sortFieldId) {
        this.sortFieldId = sortFieldId;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }
    
    public static enum SortDir{
        NONE(null),
        ASC("ASC"),
        DESC("DESC");
        private String dir;
        
        SortDir(String dir){
            this.dir = dir;
        }
        
        public String getDir(){
            return dir;
        }
    }
}
