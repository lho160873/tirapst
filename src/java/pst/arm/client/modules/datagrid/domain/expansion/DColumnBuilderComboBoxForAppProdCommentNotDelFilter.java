package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderComboBox;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.io.Serializable;

/**
 *
 * @author Igor
 * @since 0.0.1
 * for PROD_CALENDAR_IG.xml
 */
public class DColumnBuilderComboBoxForAppProdCommentNotDelFilter extends DColumnBuilderComboBox implements Serializable {

    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        if (val.equals("0")) {
            return " 1=1 ";
        } else
            return " MAIN.DATE_DELETE IS NULL ";
    }

    // Когда эти методы переопределены пустыми, то при закрытии окна редактирования предлагал сохранять изменения, даже если ничего не изменялось
//    @Override
//    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
//       //if (field.getClass() == SimpleComboBox.class) {
//            //val.setValFromFormat(key, field.getRawValue(), this);
//       // }
//    }

  /*  @Override
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
    }*/
}