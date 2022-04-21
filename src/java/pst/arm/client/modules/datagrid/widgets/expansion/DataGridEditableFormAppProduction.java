package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Field;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderMultiForAppProductionJobNum;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;


/**
 *
 * @author  LKHorosheva
 * @date    29.05.2017
 */
public class DataGridEditableFormAppProduction extends DataGridEditableForm {

 public DataGridEditableFormAppProduction(DTable table) {
        super(table);
    }

    public DataGridEditableFormAppProduction(DTable table, EditState state) {
        super(table,state);
    }

    public DataGridEditableFormAppProduction() {
        super();
    }


    public DataGridEditableFormAppProduction(DTable table, EditState state, DCondition cndMulti) {
        super(table,state,cndMulti);
    }

    public DataGridEditableFormAppProduction(DTable table, EditState state, DCondition cndMulti, String customHeader) {
        super(table,state,cndMulti,customHeader);
    }


    @Override
    protected void initComponents() {
        super.initComponents();


        final Field fldOrderId = (Field) fields.get(new SKeyForColumn("MAIN:ORDER_ID"));
        if (fldOrderId != null) {


            fldOrderId.addListener(Events.Change, new Listener<FieldEvent>() {
                @Override
                public void handleEvent(FieldEvent fe) {
                    DColumnBuilderMultiForAppProductionJobNum builderJob = (DColumnBuilderMultiForAppProductionJobNum) table.getColumnBuilders().get(3);
                    Field fldJobId = (Field) fields.get(new SKeyForColumn("MAIN:JOB_ID"));
                    Field fldJobNum = (Field) fields.get(new SKeyForColumn("MAIN:JOB_NUM"));

                    if (builderJob == null || fldJobId == null || fldJobNum == null) {
                        return;
                    }
                    fldJobId.clear();
                    fldJobNum.clear();

                    builderJob.setOrderId(-1);
                    if (fldOrderId.getValue() == null) {
                        return;
                    }
                    if (!fldOrderId.getValue().toString().isEmpty()) {
                        builderJob.setOrderId(Integer.valueOf(fldOrderId.getValue().toString()));
                    }
                }
            });
        }       
    }
}
