package pst.arm.client.modules.tablegrid.domain;

import java.io.Serializable;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGrid implements Serializable, EditableDomain<TableGrid>{
    //REQUEST_STATUS
    private Long id;

    private String name;
    
    public TableGrid()
    {
        id = null;
        name = null;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId( Long i ) {
        id = i;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public Boolean isDomainEquals(TableGrid domain) {
        if (domain == null) {
            return false;
        }
        if ((this.name == null) ? (domain.name != null) : !this.name.equals(domain.name)) {
            return false;
        }
               
        return true;
    }
    
    @Override
    public Long getDomainId() {
        return id;
    }
    
    @Override
    public Boolean isDomainFull() {
        return true;
    }

    @Override
    public TableGrid newInstance() {
        return new TableGrid();
    }

    @Override
    public void copy(TableGrid domain) {
        id = domain.id;
        name = new String(domain.name);
       
    }
    
     public static class DataGridBuilder {

        private Long id;
        private String name;
 
        public DataGridBuilder() {
            id = null;
            name = null;
        }
        
        public DataGridBuilder(TableGrid domain) {            
            id = domain.id;
            name = domain.name;
        }
        
        public DataGridBuilder id(Long id){
            this.id = id;
            return this;
        }
        
        public DataGridBuilder name(String name){
            this.name = name;
            return this;
        }
                
        public TableGrid build(){
            TableGrid domain = new TableGrid();
            domain.id = id;
            domain.name = name;            
            return domain;
        }
        
    }
    
    
}
