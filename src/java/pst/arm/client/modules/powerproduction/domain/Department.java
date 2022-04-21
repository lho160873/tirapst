package pst.arm.client.modules.powerproduction.domain;


import com.extjs.gxt.ui.client.data.BaseModel;
import java.io.Serializable;
import java.util.Date;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class Department implements Serializable, EditableDomain<Department> {

    private Integer id = null;  //Код договора
    private String name;        //Номер договора 
    private Integer place; //Кол-во рабочих мест
    private Integer placeWork; //Кол-во занятых рабочих мест
   
   
    public double getPercentWork()
    {
        double a = (double)(place.intValue());
        double b = (double)(placeWork.intValue());
        return (b/a)*100.0;
        //return placeWork/place*100.0;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlaceWork(Integer placeWork) {
        this.placeWork = placeWork;
    }

    public Integer getPlaceWork() {
        return placeWork;
    }
    
    public String getName() {
        return name;
    }
   
    public void setName(String name) {
        this.name = name;
    }

   

    @Override
    public Department newInstance() {
        return new Department();
    }

    @Override
    public void copy(Department domain) {
        this.id = domain.id;
        this.name = domain.name;
       
    }

    @Override
    public Boolean isDomainFull() {
        return true;
    }

    @Override
    public Long getDomainId() {
        return new Long(getId());
    }

    @Override
    public Boolean isDomainEquals(Department domain) {

        if (domain == null) {
            return false;
        }
        return (DataUtil.compare(this.id, domain.id)
                && DataUtil.compare(this.name, domain.name));
               
    }

    
    
}
