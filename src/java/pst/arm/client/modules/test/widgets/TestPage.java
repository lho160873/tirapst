

package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;
import pst.arm.client.common.ui.form.VType;
import pst.arm.client.common.ui.form.VTypeValidator;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author vorontsov
 */
public class TestPage extends BasePageNew {
private NumberField field;
private TextField field1,field3;
private TextField field2;
    @Override
    protected LayoutContainer getContentPanel() {
        return getPanel(); 
    }

    private LayoutContainer getPanel() {        
        //final ContentPanel  panel_1 =new ContentPanel();        
         LayoutContainer panel_1 = new LayoutContainer();
        
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        
        panel_1.setLayout(layout);
        //fieldSet.setStyleAttribute("padding", "0px");
        //fieldSet.setStyleAttribute("padding-left", "5px");
        
        
        
        
        
        Button btn =  new Button("Очистить", null, new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                field1.clear();
                        field2.clear();
                        field3.clear();
                        field1.validate();
                        field2.validate();
                        field3.validate();
                //MapWindow  wnd =new MapWindow();
                //MapWindowControlCustom wnd =new MapWindowControlCustom();
                //wnd.show();
               // if (!field.isValid())
               //     MessageBox.info("","!field.isValid()",null);
               // else
              //  MessageBox.info("","ok",null);
               // field.isValid() ;
                // field.reset();
                //field.setInEditor(hidden);
                //MessageBox.info("",field.getValue().toString(),null);
            }
        });
        panel_1.add(btn);
        field = new NumberField();
        field.setFieldLabel("field");
        field.setFormat(NumberFormat.getCurrencyFormat());
        //field.setFormat(NumberFormat.getFormat("#,##0.00"));
        field.setValue(999999999999999999999999999999999999999999999999999.99);
        //field.setMaxValue(2000000);
         //field.setValidateOnBlur(Boolean.TRUE);
         //field.setInEditor(false);
        /// field.setAutoValidate(Boolean.TRUE);
         //field.setFireChangeEventOnSetValue(Boolean.TRUE);
        field.setEmptyText(null);    
        
        
         field1 = new TextField();
        field1.setFieldLabel("field1");
        field1.setValue("field1 setEnabled(false) ");
        field1.setEnabled(false);
        field1.setValidateOnBlur(false);
        field1.setValidator(new VTypeValidator(VType.DIGIT));

        field2 = new TextField();
        field2.setFieldLabel("<font color=\"red\"> field2 </font>");
        
        //field2.getElement().setInnerHTML("<div class='colortext'></div>");
        
         //field2.setInputStyleAttribute("background-color", "gray");
        //field2.setInputStyleAttribute("background-color", "red");
       
         //field2.setStyleName("colortext");
        //field2.setInputStyleAttribute("border-width","4");
         //field2.setInputStyleAttribute("border-color","red");
        field2.setHeight(100);
        //field2.setInputStyleAttribute("position", "relative");
        
        //field2.setInputStyleAttribute("background-position", "0%");
        //field2.setInputStyleAttribute("background-repeat", "background-repeat");
         field2.setInputStyleAttribute("color", "gray");
         //field2.setInputStyleAttribute("background-color", "#ffe");
         field2.setInputStyleAttribute("background-image", "none");
         field2.setInputStyleAttribute("background-color", "#f2f6fb");
         //field2.setStyleAttribute("background-image", "none");
        // field2.setStyleAttribute(width, width);
        // field2.setIntStyleAttribute(width, windowResizeDelay);
        //field2.setStyleAttribute("background-color", "red");
        //field2.setStyleAttribute("class","colortext");

        //field2.setReadOnly(true);
        field2.setValue("field2");

        //field2.setValidator(new VTypeValidator(VType.DIGIT));
        
       
        
         field3 = new TextField();
         //field3.setValidateOnBlur(true);
          field3.setFieldLabel("field3");
          field3.setValue( "field3" );
          field3.setValidator(new VTypeValidator(VType.ALPHABETNOTEMPTY));
        
        
          /*field.addListener(Events.OnKeyUp, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
                int k = field.getRawValue().length();
                int i = field.getCursorPos();
                field.setValue(field.getValue());
                int kk = field.getRawValue().length();
                if ((kk - k) == 3) {
                    i = i;                            
                } else {
                    if (kk > k) {
                        i = i + 1;
                    }
                    if (kk < k) {
                        i = i - 1;
                    }
                }
                field.setCursorPos(i);
            }
        });*/
       /*
        //field.onComponentEvent(null);
         field.addListener(Events.OnBlur, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
               // int i = field.getCursorPos();
               // field.setValue(field.getValue());
               // field.setCursorPos(i);
                 MessageBox.info("","OnBlur",null);
            }
        });
        
       field.addListener(Events.OnFocus, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
               // int i = field.getCursorPos();
               // field.setValue(field.getValue());
               // field.setCursorPos(i);
                 MessageBox.info("","OnFocus",null);
            }
        });
        
          field.addListener(Events.OnKeyDown, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
               // int i = field.getCursorPos();
               // field.setValue(field.getValue());
               // field.setCursorPos(i);
                 MessageBox.info("","OnKeyDown",null);
            }
        });
         
        field.addListener(Events.OnKeyUp, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
               // int i = field.getCursorPos();
               // field.setValue(field.getValue());
               // field.setCursorPos(i);
                 MessageBox.info("","OnKeyUp",null);
            }
        });
         
         field.addListener(Events.OnKeyPress, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
               // int i = field.getCursorPos();
               // field.setValue(field.getValue());
               // field.setCursorPos(i);
                 MessageBox.info("","OnKeyPress",null);
            }
        });
         
         field.addListener(Events.OnChange, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent fe) {
                //fe
                //field.setOriginalValue(field.getValue());
                  //field.reset();      
                //int i = field.getCursorPos();
                //field.setValue(field.getValue());
                //field.setCursorPos(i);
               
                // MessageBox.info("","OnChange",null);
                 // field.setFormat(NumberFormat.getFormat("#,##0.00"));
                 // field.repaint();
            }
        });*/
         
         
         /*
        
        field.addKeyListener(new KeyListener(){
            @Override
            public void componentKeyDown(ComponentEvent event) {
                 //super.onKeyDown(event);
                //event.getKeyCode() .getEventTypeInt();
                 //MessageBox.info("",String.valueOf(event.getKeyCode()),null);
                  //MessageBox.info("",field.getValue().toString(),null);
               //if (event.getKeyCode() == KeyCodes. .KEY_TAB){
                 //  MessageBox.info("","KEY_TAB",null);
                //int i = field.getCursorPos();
                //field.reset();
                //field.s
                //field.setValue(field.getValue());
               // field.
                //if (event.getKeyCode() == KeyCodes.KEY_BACKSPACE)
                //   i=i-1;
                //field.setCursorPos(i);
               //}
                  //field.setValue(field.getValue());
                 // field.updateOriginalValue(field.getValue());
              
            }
        });*/
        //field.setNumberFormat(NumberFormat.getFormat("#,##0.00"));
        // NumberPropertyEditor ed = new NumberPropertyEditor(NumberFormat.getCurrencyFormat());
        //ed.setFormat(NumberFormat.getFormat("#,##0.00"));
        //ed.setStripCurrencySymbol(true);
        //ed.setStripGroupSeparator(true);
        //field.setPropertyEditor(ed);
        //field.setPropertyEditorType(Integer.class);
         //MessageBox.info("getPattern",NumberFormat.getCurrencyFormat().getPattern(),null);
        panel_1.add(field);
        panel_1.add(field1);
         panel_1.add(field2);
           panel_1.add(field3);
       

        //final MapPanel  panel_1 =new MapPanel();
        //final RowLayoutExample panel_1 =new RowLayoutExample();
        //final GWTUserServiceExample panelUser = new GWTUserServiceExample();
        
        LayoutContainer main = new ContentPanel(new BorderLayout());
        ((ContentPanel)main).setHeading("Тестовая панель");
        ((ContentPanel)main).setHeaderVisible(true);
        main.setBorders(true);
        
        //BorderLayoutData requestPanelData = new BorderLayoutData(LayoutRegion.NORTH, 160);
        BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        //BorderLayoutData testPanelData = new BorderLayoutData(LayoutRegion.SOUTH,150);
        //BorderLayoutData eastPanelData = new BorderLayoutData(LayoutRegion.EAST,400);
         
        //requestPanelData.setCollapsible(true);


        //main.add(filtrPanel, requestPanelData);
        //main.add(gridPanel, resultPanelData);
        
        
        //eastPanelData.setCollapsible(true);
        
         //main.add(panel, eastPanelData);
         //main.add(panel_1, testPanelData);         
        main.add(panel_1, resultPanelData);
        
       //ChartGalleryExample e = new ChartGalleryExample();
       
          //main.add(e);
          
        return main;
        
        // создание объекта - горизонтальной панели
   /* HorizontalPanel hp = new HorizontalPanel();
    // установка ширины в пикселях
    hp.setWidth(300);
    // установка ширины встроенной таблицы
    hp.setTableWidth("100%");
    // добавление к панели компонента Label
    hp.add(new Label("Выровнено по центру"));
    // создание объекта TableData (содержащего дополнительные параметры раскладки)
    TableData td = new TableData();
    // задание выравнивания текста по правому краю
    td.setHorizontalAlign(HorizontalAlignment.RIGHT);
    // добавление к панели компонента Label с дополнительными параметрами по раскладке
    hp.add(new Label("Выровнено по правому краю"), td);*/
        //filtrPanel = new TestFiltrPanel();
        //gridPanel = new TestGridPanel();      
        //LayoutContainer main = new LayoutContainer(new BorderLayout());
        //main.setBorders(false);
         // добавление к панели компонента Label
        //hp.add(new Label("Выровнено по центру"));     
        //BorderLayoutData requestPanelData = new BorderLayoutData(LayoutRegion.NORTH, 120);
        //BorderLayoutData resultPanelData = new BorderLayoutData(LayoutRegion.CENTER);
        //requestPanelData.setCollapsible(true);
        //main.add(filtrPanel, requestPanelData);
        //main.add(gridPanel, resultPanelData);
        //return main;     
 
    }
}
