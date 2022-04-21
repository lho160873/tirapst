package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import java.util.HashMap;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 * @author LKHorosheva
 */
public class DColumnBuilderUserName extends DColumnBuilderString {
    
    //заполняет соответсвующее поля переданным значением
    @Override
    public void setDomainValueToField(SKeyForColumn key, Field field, DDataGrid domain) {
       if (!field.isReadOnly())
       {
         field.setValue(ConfigurationManager.getUserName());
         FormHelper.setReadOnlyProp(field, true);
       }
       else
       {
           super.setDomainValueToField(key, field, domain);
       }
    }
}