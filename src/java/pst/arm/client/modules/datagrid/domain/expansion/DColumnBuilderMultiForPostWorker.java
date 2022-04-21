package pst.arm.client.modules.datagrid.domain.expansion;
import com.extjs.gxt.ui.client.widget.form.Field;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderMulti;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderMultiForPostWorker extends DColumnBuilderMulti{
       @Override
    public void setEditableField(SKeyForColumn key, Field field, Boolean isInserted) {
          if (key.equals(new SKeyForColumn("MAIN:PERS_NUMBER")) || key.equals(new SKeyForColumn("MAIN:DATE_IN"))) {
            FormHelper.setReadOnlyProp(field,false);
            //field.setEnabled(true);
        } else {
            FormHelper.setReadOnlyProp(field,true);
            // field.setEnabled(false);
        }
    }
}
