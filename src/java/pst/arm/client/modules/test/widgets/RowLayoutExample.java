package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import java.util.HashMap;
import pst.arm.client.common.ui.controls.datagrid.DGComboBox;
import pst.arm.client.common.ui.controls.datagrid.DGComboBoxLazy;
import pst.arm.client.common.ui.controls.datagrid.EDGComboBox;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.test.DashboardExample;


/**
 *
 * @author Admin
 */
public class RowLayoutExample extends ContentPanel {
private DGComboBox cb1,cb3,cb2;
private DGComboBoxLazy cb4;
    public RowLayoutExample() {
        setScrollMode(Scroll.AUTOY);  
        
        this.setHeading("Заявка");
        //---------------------------------------------               

         //---------------------------------------------               
        ContentPanel panel_0 = new ContentPanel();
        panel_0.setHeading("Карточки Клиентов");
        panel_0.setLayout(new RowLayout(Orientation.VERTICAL));
        panel_0.setFrame(false);
        panel_0.setCollapsible(true);
        panel_0.setAnimCollapse(false);                 
        panel_0.getHeader().setStyleAttribute("backgroundImage", "none");
        panel_0.getHeader().setStyleAttribute("backgroundColor", "#DFE8F6");
        panel_0.getHeader().setStyleAttribute("border", "1px solid #7CA4D9");
        panel_0.setTitleCollapse(true);
        
        /*
        List<eMail> mails = new ArrayList<eMail>();
        mails.add(new eMail("arhi-t@yandex.ru","roma_yandex",""));
        mails.add(new eMail("zaytcev.roman@gmail.com","roma_google",""));
        final MailServiceUsageExample m        = new MailServiceUsageExample(mails);
        */
        Button btn0 = new Button("Карточки Клиентов", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                /*
                 * //создаем объект окна со списком клиентов final
                 * ClientCardWindow window = new ClientCardWindow(); //вызываем
                 * окно со списком клиентов для получения данных
                 * window.getPanel().setIsBtnSendEnabled(true); //данные поиска
                 * очищаем
                 * window.getPanel().getCondition().getSearches().clear();
                 * //передаем те значения для тех полей которые хотим найти в
                 * списке
                 * window.getPanel().getCondition().getSearches().put("clientName",
                 * "Контур"); //вызываем метод заполнения панели фильтров
                 * переданными значениями
                 * window.getPanel().fillPanel();//getPanelFiltr().fillPanel();
                 * //показываем окно со списком клиенто window.show();
                 *
                 *
                 * //добавляем слушателя на выбор в окне со списком клиентов
                 * кнопки "Передать"
                 * window.getPanel().getBtnSend().addSelectionListener(new
                 * SelectionListener<ButtonEvent>() { @Override public void
                 * componentSelected(ButtonEvent ce) { //кнопка "Передать" была
                 * выбрана, можем получить текущего клиента ClientCard a =
                 * window.getPanel().getSelectedDomain(); //закрываем окно
                 * со списком клиентов window.hide(); //далее делаем с
                 * полученным клиентом что хотим if (a != null) {
                 * MessageBox.info("", a.getClientName(), null); } }
                });
                 */
 
                //если не известен код клиента лучше выдать сообщение 
                //о необходимости предварительно выбрать клиента и не показывать окно с представителями
                Integer clietnId = 1;                
                if (clietnId == null)
                {
                    MessageBox.info("","Клиент не выбран, невозможно получить список предствителей",null);
                    return;
                }                
                final DataGridWindow reprWindow = new DataGridWindow("clients_repr",true,false);
                
                //reprWindow.getGridPanel().setIsBtnSendEnabled(true); //данные поиска
                String clietnName = "ООО Рога и копыта";
                reprWindow.setHeading(clietnName); //в имя заголовка окна хорошо бы передать информацию о клиенте
                //создаем слушателя на выбор кнопки передать
                Listener<DataGridEvent> listenerGetDomain = new Listener<DataGridEvent>() {

                    @Override
                    public void handleEvent(DataGridEvent be) {
                        reprWindow.hide();
                        HashMap<SKeyForColumn, IRowColumnVal> rows = be.getDomain().getRows();
                        MessageBox.info("", rows.get(new SKeyForColumn("MAIN", "CLIENT_ID")).getVal().toString(), null);
                    }
                };
                reprWindow.addDataGridListener(listenerGetDomain);

                //устанавливем на список фильр по переданному коду клиента (обязательно)
                /*reprWindow.getGridPanel().getCondition().getFilters().clear();
                SKeyForColumn key = new SKeyForColumn("MAIN", "CLIENT_ID");
                IRowColumnVal val = new DRowColumnValNumber();                
                val.setVal(clietnId);
                reprWindow.getGridPanel().getCondition().getFilters().put(key, val); //добавляем фильтр

                //устанавливаем на список поиск по переданной фамилии (не обязательно)
                 //передаем данные о фильтре тогда панель с фильтрами точно показываем (она по умолчанию может быть скрыта)
                reprWindow.getGridPanel().getCondition().getSearches().clear();
                reprWindow.getGridPanel().setIsStartWithShowPanelFiltr(true);
                SKeyForColumn keyName = new SKeyForColumn("MAIN", "LAST_NAME"); //передаем фамилию
                IRowColumnVal valName = new DRowColumnValString();
                valName.setVal("Петров");
                reprWindow.getGridPanel().getCondition().getSearches().put(keyName, valName); //вызываем метод заполнения панели фильтров переданными значениями
                */
                //показываем окно
                reprWindow.show();
                //DataGridWindow wnd = new DataGridWindow("on_route_tarif");
                //показываем окно
                //wnd.show();
                //Double value  = 0.143/0.001;
                
                //Integer v = value.intValue();
                //MessageBox.info("",v.toString(),null);
                

            }
        });
        panel_0.add(btn0, new RowData(-1, -1, new Margins(4)));
        
        
        Button btn_0_1 = new Button("Графики", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent be) {

              final Dialog simple = new Dialog();
                            simple.setHeadingText("Dialog Test");
                            simple.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
                            simple.setBodyStyleName("pad-text");
                            simple.add(new Label("sdafasfd"));
                            simple.getBody().addClassName("pad-text");
                            simple.setHideOnButtonClick(true);
                            //simple.setWidth(300);
                            DashboardExample pnl = new DashboardExample();
                            simple.add(pnl.asWidget());
                            simple.show();

            }
        });


        panel_0.add(btn_0_1, new RowData(-1, -1, new Margins(4)));
       
        
        add(panel_0, new FlowData(5));

        //---------------------------------------------               
        ContentPanel panel_1 = new ContentPanel();
        panel_1.setHeading("Контактная инфомация заказчика");
        panel_1.setLayout(new RowLayout(Orientation.VERTICAL));
        panel_1.setFrame(false);
        panel_1.setCollapsible(true);
        panel_1.setAnimCollapse(false);
        panel_1.getHeader().setStyleAttribute("backgroundImage", "none");
        panel_1.getHeader().setStyleAttribute("backgroundColor", "#DFE8F6");
        panel_1.getHeader().setStyleAttribute("border", "1px solid #7CA4D9");
        panel_1.setTitleCollapse(true);

        Text label1 = new Text("Название компании");
        label1.addStyleName("pad-text");
        label1.setStyleAttribute("backgroundColor", "white");
        label1.setBorders(true);

        Text label2 = new Text("Контактное лицо (ФИО)");
        label2.addStyleName("pad-text");
        label2.setStyleAttribute("backgroundColor", "white");
        label2.setBorders(true);

        Text label3 = new Text("e-mail");
        label3.addStyleName("pad-text");
        label3.setStyleAttribute("backgroundColor", "white");
        label3.setBorders(true);
        
        FormData formData = new FormData("-15"); 
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(110);
        
        FieldSet fieldSet = new FieldSet();
        fieldSet.setBorders( false );
        fieldSet.setLayout(formLayout);
        
        //Пример использования комбо бокса со списком заданного словарика
        cb1 = new DGComboBox<Integer>(EDGComboBox.UsersLastName);
        cb1.setFieldLabel("UsersLastName");
        fieldSet.add(cb1,formData);       
        Button btn1 = new Button("getId UsersLastName", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                Integer id = (Integer) cb1.getValueId();
                MessageBox.info("", id.toString(), null);
            }
        });
        fieldSet.add(btn1, formData);
                
        cb2 = new DGComboBox<String>(EDGComboBox.CountryAlpha2);
        cb2.setFieldLabel("CountryAlpha2");
        
        fieldSet.add(cb2,formData);   
        
        Button btn2 = new Button("getId AddressPlace", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                //SListVal val = new SListVal("04","Перевозки");
                cb2.setValueId(643);
                //cb1.setRawValue("Населенные пункты");
                String id = (String) cb2.getValueId();
                if ( id == null )
                    MessageBox.info("id", "NULL", null);
                else
                MessageBox.info("id", id, null);
                
                String val = (String) cb2.getRawValue();
                if ( val == null )
                    MessageBox.info("val", "NULL", null);
                else
                MessageBox.info("val", val, null);
            }
        });
        fieldSet.add(btn2, formData);
      
        cb3 = new DGComboBox<String>(EDGComboBox.CountryFullName);
        cb3.setFieldLabel("CountryFullName");
        fieldSet.add(cb3, formData);

        cb4 = new DGComboBoxLazy<String>(EDGComboBox.CountryFullName);
        cb4.setFieldLabel("CountryFullNameLazy");
        fieldSet.add(cb4, formData);
        Button btn4 = new Button("setRawValue CountryFullNameLazy", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                // cb4.setValueId("208");
                cb4.setRawValue("Королевство Дания");
                //String id = (String) cb4.getValueId();
                
                //MessageBox.info("", id, null);
            }
        });
        fieldSet.add(btn4, formData);

        Button btn5 = new Button("setValueId CountryFullNameLazy", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                 cb4.setValueId("208");
                //cb4.setRawValue("Королевство Дания");
                //String id = (String) cb4.getValueId();
                
                //MessageBox.info("", id, null);
            }
        });
        fieldSet.add(btn5, formData);
        Button btn6 = new Button("getId CountryFullNameLazy", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent be) {
                // cb4.setValueId("208");
                //cb4.setRawValue("Королевство Дания");
                String id = (String) cb4.getValueId();

                MessageBox.info("", id, null);
            }
        });
        fieldSet.add(btn6, formData);

        //panel_1.add(label1, new  RowData(-1,-1,new Margins(4)));
        panel_1.add(fieldSet, new  RowData(-1,-1,new Margins(4)));
        panel_1.add(label2, new  RowData(-1,-1,new Margins(0, 4, 0, 4)));
        panel_1.add(label3, new  RowData(-1,-1,new Margins(4)));
       
        add(panel_1,new FlowData(5));
  
        //---------------------------------------------               
        ContentPanel panel_2 = new ContentPanel();
        panel_2.setHeading("Перечень заказываемых услуг");
        panel_2.setLayout(new RowLayout(Orientation.HORIZONTAL));
        panel_2.setHeight(100);
        panel_2.setFrame(false);
        panel_2.setAnimCollapse(false);  
        panel_2.setCollapsible(true);
        panel_2.getHeader().setStyleAttribute("backgroundImage", "none");
        panel_2.getHeader().setStyleAttribute("backgroundColor", "#DFE8F6");
        panel_2.getHeader().setStyleAttribute("border", "1px solid #7CA4D9");
        panel_2.setTitleCollapse(true);
        
        label1 = new Text("Перечень заказываемых услуг");
        label1.addStyleName("pad-text");
        label1.setStyleAttribute("backgroundColor", "white");
        label1.setBorders(true);

        label2 = new Text("Перевозка груза");
        label2.addStyleName("pad-text");
        label2.setStyleAttribute("backgroundColor", "white");
        label2.setBorders(true);

        label3 = new Text("Хранение груза");
        label3.addStyleName("pad-text");
        label3.setStyleAttribute("backgroundColor", "white");
        label3.setBorders(true);

        panel_2.add(label1, new RowData(-1, -1, new Margins(4)));
        panel_2.add(label2, new RowData(-1, -1, new Margins(4, 0, 4, 0)));
        panel_2.add(label3, new RowData(-1, -1, new Margins(4)));

        add(panel_2,new FlowData(5));
        
        //------------------------------------------------
        ContentPanel panel_3 = new ContentPanel();
        panel_3.setHeading("Маршрут перевозки");
        panel_3.setLayout(new RowLayout(Orientation.VERTICAL));
        panel_3.setFrame(false);
        panel_3.setCollapsible(true);
        panel_3.setAnimCollapse(false);                 
        panel_3.getHeader().setStyleAttribute("backgroundImage", "none");
        panel_3.getHeader().setStyleAttribute("backgroundColor", "#DFE8F6");
        panel_3.getHeader().setStyleAttribute("border", "1px solid #7CA4D9");
        panel_3.setTitleCollapse(true);
                    
        Text label31 = new Text("Данные отправителя");
        label31.addStyleName("pad-text");
        label31.setStyleAttribute("backgroundColor", "white");
        label31.setBorders(true);

        Text label32 = new Text("Адрес отправителя");
        label32.addStyleName("pad-text");
        label32.setStyleAttribute("backgroundColor", "white");
        label32.setBorders(true);

        Text label33 = new Text("Адрес загрузки");
        label33.addStyleName("pad-text");
        label33.setStyleAttribute("backgroundColor", "white");
        label33.setBorders(true);

        panel_3.add(label31, new  RowData(-1,-1,new Margins(4)));
        panel_3.add(label32, new  RowData(-1,-1,new Margins(0, 4, 0, 4)));
        panel_3.add(label33, new  RowData(-1,-1,new Margins(4)));
       
        add(panel_3,new FlowData(5));
         //------------------------------------------------
        ContentPanel panel_4 = new ContentPanel();
        panel_4.setHeading("Параметры и характеристики груза");
        panel_4.setLayout(new RowLayout(Orientation.VERTICAL));
        panel_4.setFrame(false);
        panel_4.setCollapsible(true);
        panel_4.setAnimCollapse(false);                 
        panel_4.getHeader().setStyleAttribute("backgroundImage", "none");
        panel_4.getHeader().setStyleAttribute("backgroundColor", "#DFE8F6");
        panel_4.getHeader().setStyleAttribute("border", "1px solid #7CA4D9");
        panel_4.setTitleCollapse(true);
                    
        Text label41 = new Text("Условия Инкотермс");
        label41.addStyleName("pad-text");
        label41.setStyleAttribute("backgroundColor", "white");
        label41.setBorders(true);

        Text label42 = new Text("Тип груза");
        label42.addStyleName("pad-text");
        label42.setStyleAttribute("backgroundColor", "white");
        label42.setBorders(true);

        Text label43 = new Text("Наименование груза");
        label43.addStyleName("pad-text");
        label43.setStyleAttribute("backgroundColor", "white");
        label43.setBorders(true);

        panel_4.add(label41, new  RowData(-1,-1,new Margins(4)));
        panel_4.add(label42, new  RowData(-1,-1,new Margins(0, 4, 0, 4)));
        panel_4.add(label43, new  RowData(-1,-1,new Margins(4)));
       
        add(panel_4,new FlowData(5));        
    }            
}
