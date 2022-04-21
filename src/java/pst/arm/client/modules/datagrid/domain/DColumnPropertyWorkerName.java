package pst.arm.client.modules.datagrid.domain;

import pst.arm.client.common.ConfigurationManager;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyWorkerName extends DColumnPropertyTextField{
    
    
    /*
     * функция возвращает оригинальные данные из отформатированных
     */    
    @Override
    public String getValueFromFormat(String valueFormat)
    {       
        return ConfigurationManager.getWorkerName();
    }
    
    @Override
    public boolean isNotChanges(String val_1, String val_2) {
        return true;
    }
}
