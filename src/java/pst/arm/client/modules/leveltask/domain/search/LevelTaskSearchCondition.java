package pst.arm.client.modules.leveltask.domain.search;

import pst.arm.client.common.domain.search.SearchConditionSimple;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class LevelTaskSearchCondition extends SearchConditionSimple {

    public LevelTaskSearchCondition() {
        super();
        limit = 5;
    }
    private Boolean isAll = Boolean.FALSE;
   
   
    public Boolean getIsAll() {
        return isAll;
    }
     public  void setIsAll( Boolean isAll) {
        this.isAll = isAll ;
    }
    
}