package pst.arm.client.modules.payment.domain.search;


import pst.arm.client.common.domain.search.SearchConditionSimple;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class PaymentSearchCondition extends SearchConditionSimple {

    private String year;
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
