package pst.arm.client.modules.tablegrid.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import pst.arm.client.modules.admin.lang.AdminConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridFiltr  extends FormPanel {
    
    protected AdminConstants constants;
    private TextField<String> tfName;
    private NumberField tfId;
    private TableGridPanel resPanel;
    
    public TableGridFiltr( TableGridPanel resPanel )
    {
         this.resPanel = resPanel;
        // Установка заголовка
        setHeading("Данные / Параметры поиска");
        constants = (AdminConstants) GWT.create(AdminConstants.class);
        initView();
    } 
    
    private void initView() {
     
        setLayout(new RowLayout(Style.Orientation.VERTICAL));
       
        LayoutContainer main = new LayoutContainer();  
        main.setLayout(new ColumnLayout());  
   
        LayoutContainer column1 = new LayoutContainer();  
        column1.setStyleAttribute("paddingRight", "10px");  
        FormLayout layout = new FormLayout(); 
        column1.setLayout(layout);                  
        
        tfName = new TextFieldSearch();
        tfName.setFieldLabel("name");
        tfName.setEmptyText(null);
        column1.add(tfName, new FormData("100%"));  
        
        tfId = new NumberField();
        tfId.setFieldLabel("id");
        Integer i = 2147483647;
        //MessageBox.info("",i.toString(), null);
        tfId.setMinValue(Integer.MIN_VALUE+1);
       // tfId.setMaxValue(100000000);
         //NumberField field = new NumberField();
        tfId.setPropertyEditorType(Integer.class);
        //tfId.setEmptyText(null);
        column1.add(tfId, new FormData("100%"));  
       
        LayoutContainer column2 = new LayoutContainer();  
        column2.setStyleAttribute("paddingLeft", "10px");  
        layout = new FormLayout(); 
        column2.setLayout(layout);  
        
        /*TextFieldSearch tfName1 = new TextFieldSearch();
        tfName1.setFieldLabel("name");
        tfName1.setEmptyText(null);
        column2.add(tfName1, new FormData("100%"));  */
   
        main.add(column1, new ColumnData(.5));  
        main.add(column2, new ColumnData(.5));    
        
        //button
        LayoutContainer bottom = new LayoutContainer();  
        HBoxLayout layoutBottom = new HBoxLayout();  
        layoutBottom.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCHMAX);  
        bottom.setLayout(layoutBottom);  
   
        Button bSearch = new Button(constants.search());
        bSearch.addSelectionListener(new SearchSelectionListener());
        bSearch.setWidth(80);
        bSearch.setHeight(24);
        bottom.add(bSearch, new HBoxLayoutData(new Margins(0, 5, 0, 0)));  
        
        Button bClean = new Button(constants.clean());
        bClean.addSelectionListener(new CleanSelectionListener());
        bClean.setWidth(80);
        bClean.setHeight(24);
        bottom.add(bClean, new HBoxLayoutData(new Margins(0, 5, 0, 0))); 
                       
        add(main,new RowData(1, 1));//, new RowData(250, 1.0, new Margins(2, 10, 0, 10)));
        add(bottom,new RowData(1, -1));//, new RowData(250, 1.0, new Margins(2, 10, 0, 10)));
    }
    

    private void search() {
       MessageBox.info("","1",null);
        MessageBox.info("",tfId.getValue().toString(),null);
        MessageBox.info("","2",null);
        resPanel.getCondition().setName(tfName.getValue());

        resPanel.search();
    }

    private void clean() {
        MessageBox.info("",String.valueOf(((NumberField)tfId).getValue()),null);
           MessageBox.info("",String.valueOf(((NumberField)tfId).getMinValue()),null);
        if (!tfId.validate())
            MessageBox.info("","not valid",null);
        else
             MessageBox.info("","valid",null);
        
        
        tfName.setValue(null);
        resPanel.search();
    }
    
    class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                search();
            }
        }
    };
    private class SearchSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            search();
        }
    }

    private class CleanSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            clean();
        }
    }
}
