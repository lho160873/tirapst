package pst.arm.client.modules.datagrid.lang;

import com.google.gwt.i18n.client.LocalizableResource.Key;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DataGridMessages extends com.google.gwt.i18n.client.Messages {
   @Key("datagrid.info.loading")
    String infoLoading();
   
   @Key("datagrid.confirm.delete")
    String confirmDelete();

    @Key("datagrid.confirm.canceldoc")
    String confirmCancelDoc();
   
   @Key("datagrid.error.delete")
    String errorDelete();

   @Key("datagrid.info.notSelectRow")
    String infoNotSelectRow();
   
    @Key("datagrid.error.notUpdate")
    String errorUpdate();

    @Key("datagrid.error.notFieldsValid")
    String errorNotFieldsValid();
    
    
    @Key("datagrid.exp.info.calcOk")
    String expInfoCalcOk();
    @Key("datagrid.exp.error.calcError")
    String expErrCalcError();
    @Key("datagrid.exp.info.calc")
    String expInfoCalc();
    @Key("datagrid.exp.info.orderNull")
    String expInfoOrderNull();

    

        
}
