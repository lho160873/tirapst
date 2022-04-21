package pst.arm.client.modules.payment.domain;


import java.io.Serializable;
import pst.arm.client.common.domain.EditableDomain;




/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class Payment implements Serializable , EditableDomain<Payment> {

    
    private Integer month = null;
    private Double summFact = null;
    private Double summPlan = null;

    public void setSummFact(Double s) {
        summFact = s;
    }

    public Double getSummFact() {
        return summFact;
    }
    
     public void setSummPlan(Double s) {
        summPlan = s;
    }

    public Double getSummPlan() {
        return summPlan;
    }
    
     public void setMonth(Integer m) {
        month = m;
    }

    public Integer getMonth() {
        return month;
    }
   
    @Override
    public Payment newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copy(Payment domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isDomainFull() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getDomainId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isDomainEquals(Payment domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}