package pst.arm.client.common.domain.search;

    
import java.io.Serializable;
import java.util.HashMap;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class SearchConditionSimple implements Serializable{
    private Integer offset = 0;
    protected Integer limit = 50;
    private String sortDir = "NONE";

    private String sortField = null;
    private Integer rowFrom;
    private Integer rowTo;
    
    private HashMap<String, Object> filters = new HashMap<String, Object>();  //массив со значениями фильтров
    private HashMap<String, Object> searches = new HashMap<String, Object>(); //массив со значениями поисков

   
    
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

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public HashMap<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, Object> f) {
        filters = f;
    }

    public HashMap<String, Object> getSearches() {
        return searches;
    }

    public void setSearches(HashMap<String, Object> s) {
        searches = s;
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
    
     public void setPagingConfig(Integer limit, Integer offset, String sortDir, String sortField) {
        setLimit(limit);
        setOffset(offset);
        setSortDir(sortDir);
        setSortField(sortField);
        setRowFrom(offset);
        setRowTo(offset + limit);
    }
    
    public void setPagingConfig(Integer limit, Integer offset) {
        setPagingConfig(limit, offset, sortDir, sortField);
    }    
    
     /**
     * @return the rowFrom
     */
    public Integer getRowFrom() {
        return rowFrom;
    }


    public void setRowFrom(Integer rowFrom) {
        this.rowFrom = rowFrom;
    }

    public Integer getRowTo() {
        return rowTo;
    }


    public void setRowTo(Integer rowTo) {
        this.rowTo = rowTo;
    }
    

   

}

