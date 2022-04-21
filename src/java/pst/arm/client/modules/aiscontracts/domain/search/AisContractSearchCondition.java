/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.aiscontracts.domain.search;

import pst.arm.client.common.domain.search.SearchConditionSimple;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class AisContractSearchCondition extends SearchConditionSimple {

    private Boolean isAll = Boolean.FALSE;
    private String year;
    private Boolean isDateNotNull = Boolean.FALSE;
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getIsAll() {
        return isAll;
    }

    public void setIsAll(Boolean isAll) {
        this.isAll = isAll;
    }
    
    public Boolean getIsDateNotNull() {
        return isDateNotNull;
    }

    public void setIsDateNotNull(Boolean isDateNotNull) {
        this.isDateNotNull = isDateNotNull;
    }
}
