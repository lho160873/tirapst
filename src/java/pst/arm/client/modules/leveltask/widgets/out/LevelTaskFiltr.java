package pst.arm.client.modules.leveltask.widgets.out;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.HashMap;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.form.FormHelper;
import pst.arm.client.common.ui.form.PanelFiltrSimple;
import pst.arm.client.modules.leveltask.domain.ETaskState;
import pst.arm.client.modules.leveltask.domain.search.LevelTaskSearchCondition;
import pst.arm.client.modules.leveltask.lang.LevelTaskConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class LevelTaskFiltr extends PanelFiltrSimple {

    private CheckBox cbIsAll;
    private DateField dfBegDate, dfEndDate;
    private TextField<String> tfUserNameTo;
    private DGComboBox<Integer> cbTaskName, cbTaskState;
  

    public LevelTaskFiltr(LevelTaskPanel resPanel) {
        super(resPanel);
    }

    @Override
    protected void initComponents() {
        LevelTaskConstants constants = GWT.create(LevelTaskConstants.class);
        LayoutContainer column1 = createColumn();
        column1.setWidth(350);

        FormData formData = new FormData("100%");
        formData.setMargins(new Margins(0, 16, 0, 0));

        dfBegDate = FormHelper.createDateField(constants.columnBegDate());
        dfBegDate.setEmptyText(null);
        fields.add(dfBegDate);
        column1.add(dfBegDate, formData);

        dfEndDate = FormHelper.createDateField(constants.columnEndDate());
        dfEndDate.setEmptyText(null);
        fields.add(dfEndDate);
        column1.add(dfEndDate, formData);

        CommonImages images = GWT.create(CommonImages.class);

        LayoutContainer fsTaskName = new LayoutContainer();
        fsTaskName.setBorders(Boolean.FALSE);
        fsTaskName.setLayout(new HBoxLayout());
        //fsTaskName.setStyleAttribute("padding", "0px");
        column1.add(fsTaskName, formData);
        cbTaskName = new DGComboBox<Integer>("TASK_TYPE", "MAIN:TASK_TYPE_ID", "MAIN:NAME");
        cbTaskName.setFieldLabel(constants.columnTaskName());
        cbTaskName.setValidateOnBlur(Boolean.TRUE);
        fields.add(cbTaskName);
        LayoutContainer lcTaskName = new LayoutContainer();
        lcTaskName.setBorders(false);
        FormLayout layoutName = new FormLayout(FormPanel.LabelAlign.LEFT);
        layoutName.setLabelWidth(115);
        lcTaskName.setLayout(layoutName);
        lcTaskName.add(cbTaskName, formData);
        HBoxLayoutData hbldTaskName = new HBoxLayoutData();
        hbldTaskName.setMargins(new Margins(0, 0, 0, 0));
        hbldTaskName.setFlex(1);
        fsTaskName.add(lcTaskName, hbldTaskName);
        Button bRefreshTaskName = new Button();        
        bRefreshTaskName.setIcon(AbstractImagePrototype.create(images.reset()));        
        bRefreshTaskName.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                cbTaskName.initStore();
            }
        });
        HBoxLayoutData hblcTaskName = new HBoxLayoutData();
        hblcTaskName.setMargins(new Margins(0, 0, 0, 5));
        hblcTaskName.setFlex(0);
        fsTaskName.add(bRefreshTaskName, hblcTaskName);

        LayoutContainer column2 = createColumn();
        column2.setWidth(350);

        tfUserNameTo = new TextField<String>();
        tfUserNameTo.setEmptyText(null);
        tfUserNameTo.setFieldLabel(constants.columnUserTo());
        fields.add(tfUserNameTo);
        column2.add(tfUserNameTo, formData);
            
        LayoutContainer fsTaskState = new LayoutContainer();
        fsTaskState.setBorders(Boolean.FALSE);
        fsTaskState.setLayout(new HBoxLayout());
        //fsTaskState.setStyleAttribute("padding", "0px");
        column2.add(fsTaskState, formData);
         cbTaskState = new DGComboBox<Integer>("TASK_STATE", "MAIN:TASK_STATE", "MAIN:NAME");
        cbTaskState.setFieldLabel(constants.columnTaskState());
        cbTaskState.setValidateOnBlur(Boolean.TRUE);        
        fields.add(cbTaskState);
        LayoutContainer lcTaskState = new LayoutContainer();
        lcTaskState.setBorders(false);
        FormLayout layoutState = new FormLayout(FormPanel.LabelAlign.LEFT);
        layoutState.setLabelWidth(115);
        lcTaskState.setLayout(layoutState);
        lcTaskState.add(cbTaskState, formData);
        HBoxLayoutData hbldTaskState = new HBoxLayoutData();
        hbldTaskState.setMargins(new Margins(0, 0, 0, 0));
        hbldTaskState.setFlex(1);
        fsTaskState.add(lcTaskState, hbldTaskState);
        Button bRefreshTaskState = new Button();        
        bRefreshTaskState.setIcon(AbstractImagePrototype.create(images.reset()));        
        bRefreshTaskState.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {
                cbTaskState.initStore();
            }
        });
        HBoxLayoutData hblcTaskState = new HBoxLayoutData();
        hblcTaskState.setMargins(new Margins(0, 0, 0, 5));
        hblcTaskState.setFlex(0);
        fsTaskState.add(bRefreshTaskState, hblcTaskState);

        cbIsAll = createCheckBox(constants.filtrIsAll());
        column2.add(cbIsAll, formData);
        
           for (Field<?> field : fields) {
            field.addKeyListener(new KeyListener() {
                @Override
                public void componentKeyUp(ComponentEvent event) {
                    if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                        search();
                    }
                }
            });
        }
    }

    @Override
    protected void search() {
        if (dfBegDate.getValue() != null && dfEndDate.getValue() != null) {
            if (dfBegDate.getValue().after(dfEndDate.getValue())) {
                MessageBox.alert("Внимание", "Дата \"от\" не может быть больше даты \"до\".", null);
                return;
            }
        }
        
        resPanel.getCondition().getSearches().clear();

        if (dfBegDate.getValue() != null) {
            //resPanel.getCondition().getSearches().put("begDate", AppHelper.stringToDate(AppHelper.dateToString(dfBegDate.getValue())));
            resPanel.getCondition().getSearches().put("begDate", AppHelper.dateToString(dfBegDate.getValue()));
        }

        if (dfEndDate.getValue() != null) {
            //resPanel.getCondition().getSearches().put("endDate", AppHelper.stringToDate(AppHelper.dateToString(dfEndDate.getValue())));
            resPanel.getCondition().getSearches().put("endDate", AppHelper.dateToString(dfEndDate.getValue()));
        }

        if (!cbTaskName.getRawValue().isEmpty()) {
            resPanel.getCondition().getSearches().put("taskName", cbTaskName.getRawValue());
        }

        if (tfUserNameTo.getValue() != null) {
            resPanel.getCondition().getSearches().put("userNameTo", tfUserNameTo.getValue());
        }

        ((LevelTaskSearchCondition) resPanel.getCondition()).setIsAll(cbIsAll.getValue());

        if (!cbTaskState.getRawValue().isEmpty()) {
            resPanel.getCondition().getSearches().put("stateName", cbTaskState.getRawValue());
            if (cbTaskState.getValueId() >= 3) {
                ((LevelTaskSearchCondition) resPanel.getCondition()).setIsAll(Boolean.TRUE);
            }
        }
        
        
        
        
        

       
        resPanel.search();
    }

    @Override
    protected void clean() {
        FormHelper.clearFields(fields);
        cbIsAll.setValue(Boolean.FALSE);

        resPanel.getCondition().getSearches().clear();
        ((LevelTaskSearchCondition) resPanel.getCondition()).setIsAll(cbIsAll.getValue());
        resPanel.search();
    }

    @Override
    public void fillPanel() {
        FormHelper.clearFields(fields);
        HashMap<String, Object> map = resPanel.getCondition().getSearches();
        if (map == null) {
            resPanel.search();
            return;
        }
        //if (map.containsKey("begDate") && map.get("begDate") != null) {
        //    dfBegDate.setValue(Date.valueOf(map.get("begDate").toString()));
        //}

    }
}
