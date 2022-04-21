package pst.arm.client.modules.datagrid.domain.search;

import java.io.Serializable;
import java.util.HashMap;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridSearchCondition implements Serializable{
    private Integer offset = 0;
    private Integer limit = 50;
    private SortDir sortDir = SortDir.NONE;
    private String sortFieldId = null;
    private HashMap<SKeyForColumn, IRowColumnVal> searches; //массив со значениями поисков
    private HashMap<SKeyForColumn, IRowColumnVal> filters; //массив со значениями фильтров
    private HashMap<SKeyForColumn, IRowColumnVal> valDefault; //массив со значениями по умолчанию
    private HashMap<String, String> params; //массив со значениями параметров для замены
    private HashMap<Integer, IRowColumnVal> functionParams; // параметры для функции
    
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

    public DataGridSearchCondition()
    {
        searches = new  HashMap<SKeyForColumn, IRowColumnVal>();
        filters = new HashMap<SKeyForColumn, IRowColumnVal>();
        valDefault = new HashMap<SKeyForColumn, IRowColumnVal>();
        params = new HashMap<String, String>();
        functionParams = new HashMap<Integer, IRowColumnVal>();
    }

    public HashMap<Integer, IRowColumnVal> getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(HashMap<Integer, IRowColumnVal> functionParams) {
        this.functionParams = functionParams;
    }

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

    public HashMap<SKeyForColumn, IRowColumnVal> getFilters() {
        return filters;
    }
    
    public  void setFilters(HashMap<SKeyForColumn, IRowColumnVal> f) {
        filters = f;
    }
    
     public HashMap<SKeyForColumn, IRowColumnVal> getValDefault() {
        return valDefault;
    }
    
    public  void setValDefault(HashMap<SKeyForColumn, IRowColumnVal> v) {
        valDefault = v;
    }
    
    
    public HashMap<SKeyForColumn, IRowColumnVal> getSearches() {
        return searches;
    }
    
    public  void setSearches(HashMap<SKeyForColumn, IRowColumnVal> s) {
        searches = s;
    }
    
    public HashMap<String, String> getParams() {
        return params;
    }
    
    public  void setParams(HashMap<String, String> s) {
        params = s;
    }
    
    
}
