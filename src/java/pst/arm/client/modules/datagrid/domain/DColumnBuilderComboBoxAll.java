package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnBuilderComboBoxAll extends DColumnBuilderComboBox {

    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        if (val.equals("0")) {
            return " 0=0";
        }
        return super.getSqlWhereSearch(key, val);
    }
}
