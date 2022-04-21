package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author wesStyle
 */

public class DColumnBuilderDateStatus extends DColumnBuilderString implements Serializable
{
    private SKeyForColumn mainDate;
    private SKeyForColumn secondDate;

    public SKeyForColumn getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(SKeyForColumn secondDate) {
        this.secondDate = secondDate;
    }     

    public SKeyForColumn getMainDate() {
        return mainDate;
    }
    

    public void setMainDate(SKeyForColumn mainDate) {
        this.mainDate = mainDate;
    }

    @Override
    //построитель для полей ввода на панели фильтра
    public BoxComponent createFieldForFiltr(SKeyForColumn key, LayoutContainer fieldSet, final GWTDDataGridServiceAsync service, Integer lw) {
        if (!this.getColumns().get(key).getIsFiltr()) {
            return null;
        }
        final SimpleComboBox combo = new SimpleComboBox<String>();
        combo.setEmptyText(null);
        combo.setAllowBlank(true);
        combo.setTriggerAction(ComboBox.TriggerAction.ALL);
        

        combo.setFieldLabel("Состояние");
        combo.add("<div style='color: #000000; text-align: center; font-size: 14pt'> &#9679; </div>");
        combo.add("<div style='color: #FF0000; text-align: center; font-size: 14pt'> &#9679; </div>");
        combo.add("<div style='color: #00FF00; text-align: center; font-size: 14pt'> &#9679; </div>");
        combo.add("<div style='color: #0000FF; text-align: center; font-size: 14pt'> &#9679; </div>");
        //combo.setRawValue("<div style='color: #0000FF; text-align: center; font-size: 14pt'>jui &#9679; </div>");

        combo.addSelectionChangedListener(new SelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent se) {
                if (se.getSelectedItem().get("value").equals("<div style='color: #000000; text-align: center; font-size: 14pt'> &#9679; </div>")) {
                    combo.setRawValue("Назначено. Срок выполнения не прошел");
                } else if (se.getSelectedItem().get("value").equals("<div style='color: #FF0000; text-align: center; font-size: 14pt'> &#9679; </div>")) {
                    combo.setRawValue("Не выполнено. Срок исполнения прошел");
                } else if (se.getSelectedItem().get("value").equals("<div style='color: #00FF00; text-align: center; font-size: 14pt'> &#9679; </div>")) {
                    combo.setRawValue("Выполнено в срок");
                } else if (se.getSelectedItem().get("value").equals("<div style='color: #0000FF; text-align: center; font-size: 14pt'> &#9679; </div>")) {
                    combo.setRawValue("Выполнено с нарушением срока");
                }
            }
        });

        LayoutContainer lc = createLayoutContainer(lw);
        lc.setStyleAttribute("padding", "0px");
        FormData data = new FormData("100%");
        data.setMargins(new Margins(0, 16, 0, 0));
        fieldSet.add(lc, data);

        LayoutContainer fieldSetBox = new LayoutContainer();
        fieldSetBox.setBorders(false);
        //fieldSetBox.setLayout(new FormLayout());
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(lw);
        fieldSetBox.setLayout(layout);

        FormData data2 = new FormData("100%");
        data2.setMargins(new Margins(0, 16, 0, 0));
        fieldSetBox.add(combo, data2);

        HBoxLayoutData dataBox = new HBoxLayoutData();
        dataBox.setMargins(new Margins(0, 0, 0, 0));
        dataBox.setFlex(1);
        fieldSet.add(fieldSetBox, dataBox);
        return combo;
    }
    //заполняет значения из полей ввода

    @Override
    public void setValueFromField(SKeyForColumn key, Field field, IRowColumnVal val) {
        if (field instanceof SimpleComboBox) {
            SKeyForColumn keyFact = getSecondDate();
            SKeyForColumn keyPlan = getMainDate();
            String currStr = DateTimeFormat.getFormat("dd.MM.yyyy").format(new Date());
            SimpleComboBox cb = (SimpleComboBox) field;
            if (cb.getRawValue().equals("Назначено. Срок выполнения не прошел")) {
                val.setVal(keyPlan.getTableAlias() + "." + keyPlan.getColumnName() + " >= " + "'" + currStr + "'"
                        + " AND " + keyFact.getTableAlias() + "." + keyFact.getColumnName() + " is null ");
            } else if (cb.getRawValue().equals("Не выполнено. Срок исполнения прошел")) {
                val.setVal(keyPlan.getTableAlias() + "." + keyPlan.getColumnName() + " < " + "'" + currStr + "'"
                        + " AND " + keyFact.getTableAlias() + "." + keyFact.getColumnName() + " is null ");
            } else if (cb.getRawValue().equals("Выполнено в срок")) {
                val.setVal(keyPlan.getTableAlias() + "." + keyPlan.getColumnName() + " >= " + keyFact.getTableAlias() + "." + keyFact.getColumnName());
            } else if (cb.getRawValue().equals("Выполнено с нарушением срока")) {
                val.setVal(keyPlan.getTableAlias() + "." + keyPlan.getColumnName() + " < " + keyFact.getTableAlias() + "." + keyFact.getColumnName());
            }
        } else {
            val.setValFromFormat(key, field.getValue(), this);
        }
    }

    
    
    @Override
    public String getSqlWhereSearch(SKeyForColumn key, String val) {
        return val;
    }
}


