package pst.arm.client.modules.datagrid.widgets.expansion;

import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;


/**
 *
 * @author  LKHorosheva
 * @date    29.05.2017
 */
public class DataGridEditableFormDhElaborationOfDd extends DataGridEditableForm {

/* public DataGridEditableFormDhElaborationOfDd(DTable table) {
        super(table);
        
    }*/

    public DataGridEditableFormDhElaborationOfDd(DTable table, EditState state) {
        super(table,state);
    }
    
    
  
   /* public void enditableField(String strKey) {
        SKeyForColumn currKey = new SKeyForColumn(strKey);
        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                if (currKey.equals(key)) {
                    //builder.setEditableField(key, fields.get(key), false);
                    FormHelper.setReadOnlyProp(fields.get(key), true);
                }
            }
        }
    }

    public void enbl() {
        enditableField("MAIN:LEADS_CONTRACT");
        enditableField("MAIN:INFO");
        enditableField("MAIN:CUSTOMER");
        enditableField("MAIN:URGENCY");
        enditableField("MAIN:AMAUNT");
        enditableField("MAIN:MEMO_DEADLINE"); //и начальнику МЗК
    }*/


   
}
