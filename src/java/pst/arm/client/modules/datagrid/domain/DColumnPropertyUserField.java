package pst.arm.client.modules.datagrid.domain;

import pst.arm.client.common.ConfigurationManager;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyUserField extends DColumnPropertyNumberField{
    
    
    /*
     * функция возвращает оригинальные данные из отформатированных
     */    
    @Override
    public Number getValueFromFormat(Number valueFormat)
    {       
        Class<?> type = this.type.getTypeClass();
        return returnTypedValue(ConfigurationManager.getUserId().intValue(), type);
    }
    
    @Override
    public boolean isNotChanges(Number val_1, Number val_2) {
        return true;
    }
}
