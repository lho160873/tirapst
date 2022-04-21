package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import java.util.Map;
import pst.arm.client.modules.datagrid.domain.*;

/**
 *
 * @author LKHorosheva
 * @date 31.05.2017
 */
public class DColumnBuilderStatForDhElaborationOfDd extends DColumnBuilderMulti {

    @Override
    public LayoutContainer createLayoutContainer(Integer lw) {
        Integer countVisibleField = 0;
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
            if (this.getColumn(key).getIsVisibleEdit()) {
                countVisibleField++;
            }
        }
        //проверяем есть ли хоть одно видимое поле
        Boolean isVisible = false;         
        for (Map.Entry colEntry : this.getColumns().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
           isVisible = ( isVisible || this.getColumn(key).getIsVisibleEdit() );        
        }
        if (countVisibleField > 1) //строим рамочку
        {
            FieldSet fieldSet = new FieldSet();
            fieldSet.setBorders(false);
            //fieldSet.setHeading(caption);
            //fieldSet.setLayout(new FormLayout());
            FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
            layout.setLabelWidth(lw);
            fieldSet.setLayout(layout);
            fieldSet.setStyleAttribute("padding", "5px");
            fieldSet.setVisible(isVisible);
            return fieldSet;
        } else {
            LayoutContainer fieldSet = new LayoutContainer();
            fieldSet.setBorders(false);
            FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
            layout.setLabelWidth(lw);
            fieldSet.setLayout(layout);
            fieldSet.setStyleAttribute("padding", "0px");
            fieldSet.setStyleAttribute("padding-left", "5px");
            fieldSet.setVisible(isVisible);
            return fieldSet;
        }
    }
     
}