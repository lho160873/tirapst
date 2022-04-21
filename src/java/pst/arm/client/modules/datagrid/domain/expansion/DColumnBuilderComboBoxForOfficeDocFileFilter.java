package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderComboBox;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyComboBoxInteger;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderComboBoxForOfficeDocFileFilter extends DColumnBuilderComboBox implements Serializable {



    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {

        /*String upper = "";
        String sqlForCol = this.getColumn(key).getSqlForColumn();
        if (this.getColumn(key).getColumnProperty() instanceof DColumnPropertyComboBoxInteger) {
            upper = val;
        } else {
            upper = "%" + val + "%";
        }

        if (sqlForCol != null && !sqlForCol.isEmpty()) {
            return " UPPER(" + sqlForCol + ") LIKE UPPER('" + upper + "') ";
        }
        return " UPPER(" + key.getTableAlias() + "." + this.getColumn(key).getName() + ") LIKE UPPER('" + upper + "') ";*/

        if (val.equals("1")) {
            return "OFFICE_DOC_ID not in (select DOC_ID from DOC_FILE)";
        } else
            return " MAIN.DATE_CANCELLED IS NULL ";
    }

    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
       //if (field.getClass() == SimpleComboBox.class) {
            //val.setValFromFormat(key, field.getRawValue(), this);
       // }
    }

    @Override
    public BoxComponent createFieldForFiltr(SKeyForColumn key, LayoutContainer fieldSet, GWTDDataGridServiceAsync service, Integer lw) {

        if (!this.getColumns().get(key).getIsFiltr()) {
            return null;
        }

        Field field = new CustomCheckbox();
        field.setFieldLabel(this.getColumns().get(key).getCaption());

        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);

        fieldSet.setLayout(layout);
        //fieldSet.setStyleAttribute("padding", "5px");

        FormData data = new FormData("10%");

        data.setMargins(new Margins(0, 16, 0, 0));


        fieldSet.add(field, data);
        return field;
    }
}