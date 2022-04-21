package pst.arm.client.modules.mainpage;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

/**
 *
 * @author vorontsov
 */
public class MainPageNew extends BasePageNew {

    protected BaseConstants constants;
    protected LayoutContainer lc;
    protected ContentPanel panel;
    private final GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных

    @Override
    protected LayoutContainer getContentPanel() {
        constants = (BaseConstants) GWT.create(BaseConstants.class);

        lc = new LayoutContainer();
        lc.setWidth("100%");
        lc.setLayout(new FitLayout());

        panel = new ContentPanel();
        panel.setFrame(true);
        panel.setLayout(new RowLayout(Orientation.VERTICAL));
        panel.setHeading("Главная");
        panel.setBodyStyle("font-size: 12px");
        panel.setScrollMode(Style.Scroll.AUTO);

        lc.add(panel);
        showMain();

        return lc;
    }

    protected String getHomeUrl() {
        return GWT.getHostPageBaseURL() + "main.htm?" + AppHelper.getInstance().debugUrl();
    }

    protected void showHelpManual() {
        service.openHelpManual("main.pdf", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "main.pdf");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла main.pdf Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
    protected void showHelpLevelTask() {
        service.openHelpManual("level_task.pdf", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "level_task.pdf");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла level_task.pdf Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
    protected void showPhone() {
        service.openHelpManual("phone.doc", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "phone.doc");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла phone.doc Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
     protected void showAlert() {
        service.openHelpManual("alert.pdf", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "alert.pdf");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла alert.pdf Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
    protected void showHelpSettindsBrowser() {
        service.openHelpManual("firefox_doc.pdf", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "firefox_doc.pdf");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла firefox_doc.pdf Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
        protected void showHelpResponOcp() {
        service.openHelpManual("respon_ocp.pdf", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught, "Ошибка открытия файла");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + "respon_ocp.pdf");
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла respon_ocp.pdf Обратитесь к Администратору.", null);
                }
            }
        });
    }

    protected void showMore(final Integer typeNews) {
        DataGridSearchCondition condition = new DataGridSearchCondition();

        service.getDataGrid("NEWS_HLV", condition, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                ArrayList<String> lstText;
                lstText = new ArrayList<String>();
                for (DDataGrid dd : result) {
                    if (dd.getRows().get(new SKeyForColumn("MAIN:NEWS_TYPE_ID")).getVal() == null) {
                        continue;
                    }
                    Integer type = (Integer) dd.getRows().get(new SKeyForColumn("MAIN:NEWS_TYPE_ID")).getVal();
                    if (type.equals(typeNews)) {
                        Date d = (Date) dd.getRows().get(new SKeyForColumn("MAIN:DATE_NEWS")).getVal();
                        String strDate = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
                        String text = "<div class='info_date'>" + strDate + "</div>";
                        if (dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal() != null) {
                            text += (String) dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal();
                        } else {
                            text += "&nbsp";
                        }
                        lstText.add(text);
                    }
                }
                createMore(lstText, typeNews);
            }
        });

    }

    protected void createDetail(String text, final Integer typeNews) {
        panel.removeAll();

        Text label1 = new Text("");
        label1.addStyleName("pad-text");
        label1.setBorders(false);
        label1.setStyleAttribute("padding", "0px");
        label1.setText("<div class='info_go'><a href=\"#\">Назад</a></div>");
        label1.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (typeNews.equals(-1))
                {
                    showMain();
                }
                else
                {
                showMore(typeNews);
                }
            }
        });

        Text label2 = new Text("");
        label2.addStyleName("pad-text");
        label2.setStyleAttribute("backgroundColor", "white");
        label2.setBorders(false);
        label2.setStyleAttribute("padding-top", "10px");
        label2.setStyleAttribute("padding-right", "30px");
        label2.setStyleAttribute("padding-bottom", "10px");
        label2.setStyleAttribute("padding-left", "30px");
        label2.setText(text);

        Text label3 = new Text("");
        label3.addStyleName("pad-text");
        label3.setBorders(false);
        label3.setStyleAttribute("padding", "0px");
        label3.setText("<div class='info_go'><a href=\"#\">Назад</a></div>");
        label3.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                
                if (typeNews.equals(-1))
                {
                    showMain();
                }
                else
                {
                showMore(typeNews);
                }
            }
        });

        panel.add(label1, new RowData(1, -1, new Margins(5, 30, 10, 30)));
        panel.add(label2, new RowData(1, -1, new Margins(10, 30, 10, 30)));
        panel.add(label3, new RowData(1, -1, new Margins(10, 30, 10, 30)));
        lc.layout(true);
    }

    protected void createMore(ArrayList<String> lstText, Integer typeNews) {
        panel.removeAll();

        Text label1 = new Text("");
        label1.addStyleName("pad-text");
        label1.setBorders(false);
        label1.setStyleAttribute("padding", "0px");
        label1.setText("<div class='info_go'><a href=\"#\">Назад</a></div>");
        label1.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showMain();
            }
        });
        panel.add(label1, new RowData(1, -1, new Margins(5, 30, 10, 30)));
        if (typeNews.equals(1)) {
            createLabelCaption("<div class='info_caption'>Информация от разработчиков</div><br>",typeNews);
        } else if (typeNews.equals(2)) {
            createLabelCaption("<div class='info_caption'>Новости компании</div><br>",typeNews);
        }

        for (String str : lstText) {
            createLabelInfo(str, typeNews);
        }
        createLabelLast(typeNews);
        Text label2 = new Text("");
        label2.addStyleName("pad-text");
        label2.setBorders(false);
        label2.setStyleAttribute("padding", "0px");
        label2.setText("<div class='info_go'><a href=\"#\">Назад</a></div>");
        label2.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showMain();
            }
        });

        panel.add(label2, new RowData(1, -1, new Margins(10, 30, 10, 30)));
        lc.layout(true);

    }

    private void createMain(ArrayList<String> lstDev, Boolean isDevMore, ArrayList<String> lstFirm, Boolean isFirmMore) {
        panel.removeAll();

        Text label1 = new Text("");
        label1.addStyleName("pad-text");
        label1.setBorders(false);
        label1.setStyleAttribute("padding-top", "0px");
        label1.setStyleAttribute("padding-right", "30px");
        label1.setStyleAttribute("padding-bottom", "10px");
        label1.setStyleAttribute("padding-left", "30px");
        label1.setText("<i>Если некоторые пункты меню у Вас работают некорректно или не открываются, то, прежде чем обратиться к Администратору Системы нажмите одновременно клавиши <b class='info_text'>\"Ctrl\"</b> и <b class='info_text'>\"F5\"</b>. Это обеспечивает очистку памяти браузера, из которой берутся данные для ускорения работы (системного кэша). Это действие рекомендуется выполнять периодически и, в особенности, после обновления очередной версии Системы.</i>");
        panel.add(label1, new RowData(1, -1, new Margins(5, 30, 10, 30)));

        String textInfo = "<div class='info_caption'>Информация от разработчиков</div>";        
        createLabelCaption(textInfo,-1);        
        
        String textDev = "<i>Текущий номер сборки системы </i><b class='info_text'>" + constants.projectBuildNumber().replace("M", "") + " </b><br><i>Последнее обновление системы выполнено </i><b class='info_text'>" + constants.projectBuildTime() + "</b>";        
        createLabelText(textDev,-1);
        
        for (String str : lstDev) {
            createLabelInfo(str,-1);
        }
        if (isDevMore) {
            createLabelShowMore("<a href=\"#\">Вся информация</a>", 1);
        } else {
            createLabelLast(-1);
        }

        createLabelCaption("<div class='info_caption'>Новости компании</div>",2);
        for (String str : lstFirm) {
            createLabelInfo(str,2);
        }
        if (isFirmMore) {
            createLabelShowMore("<a href=\"#\">Все новости</a>", 2);
        } else {
            createLabelLast(2);
        }

        createLabelCaption("<div class='info_caption'>Полезные ресурсы</div><div class='info_caption_1'>Документация</div>",-1);

        Text label5 = createLabelA("<a href=\"#\">Описание интерфейса Системы ИСУП ГПК и принципы ее работы</a>");        
        label5.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showHelpManual();
            }
        });
        Text label511 = createLabelA("<a href=\"#\">Панель задач (сообщений)</a>");
        label511.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showHelpLevelTask();
            }
        });               

        Text label51 = createLabelA("<a href=\"#\">Руководство ответственным по работе с ОКП и планами работ НТО (КО)</a>");
        label51.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showHelpResponOcp();
            }
        });
        
        
        Text label6 = createLabelA("<a href=\"#\">Настройки браузера Firefox</a>");
        label6.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showHelpSettindsBrowser();
            }
        });
        Text label7 = createLabelA("<a href=\"#\">Телефонный справочник</a>");
        label7.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showPhone();
            }
        });
                
        Text label8 = createLabelA("<a href=\"#\">Система оповещения</a>");        
        label8.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showAlert();
            }
        });
        
        Text label9 = new Text("");
        label9.addStyleName("pad-text");
        label9.setStyleAttribute("backgroundColor", "white");
        label9.setBorders(false);
        label9.setStyleAttribute("padding-top", "0px");
        label9.setStyleAttribute("padding-right", "30px");
        label9.setStyleAttribute("padding-bottom", "10px");
        label9.setStyleAttribute("padding-left", "30px");
        
        label9.setText("<div class='info_caption_1'>Полезные ссылки</div>"
                + "<div class='info_a'><a href=http://www.tira.ru/ target=\"_blank\">ООО \"Корпорация \"ТИРА\"</a><br>"
                + "<a href=http://www.rimr.ru/ target=\"_blank\">АО \"РИМР\" - Российский институт мощного радиостроения</a><br>"
                + "<a href= http://www.rimr.ru/pro-id-7.html target=\"_blank\">ПАО \"Прибой\"</a><br>"
                + "<a href= http://martspb.ru/ target=\"_blank\">АО \"МАРТ\" - Мощная Аппаратура Радиовещания и Телевидения</a><br>"
                + "<a href= http://passat.spb.ru/ target=\"_blank\">ООО \"Пассат\"</a></div>");

        panel.add(label9, new RowData(1, -1, new Margins(0, 30, 0, 30)));

        lc.layout(true);
    }

    private void createLabelCaption(String text, final Integer typeNews) {
        Text label = new Text("");
        if (typeNews==2)
        {
        label.addStyleName("info_background_caption");
        }
        else
        {
        label.addStyleName("pad-text");
        label.setStyleAttribute("backgroundColor", "white");
        }
        
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "10px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "0px");
        label.setStyleAttribute("padding-left", "30px");
        label.setText(text);
        panel.add(label, new RowData(1, -1, new Margins(10, 30, 0, 30)));
    }
    
    private void createLabelText(String text, final Integer typeNews) {
        Text label = new Text("");
        if (typeNews==2)
        {
        label.addStyleName("info_background_info");
        }
        else
        {
        label.addStyleName("pad-text");
        label.setStyleAttribute("backgroundColor", "white");
        }
        
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "10px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "0px");
        label.setStyleAttribute("padding-left", "30px");
        label.setText(text);
        panel.add(label, new RowData(1, -1, new Margins(0, 30, 0, 30)));
    }

   

    private void createLabelInfo(final String text, final Integer typeNews) {
        Text label = new Text("");
        if (typeNews==2)
        {
        label.addStyleName("info_background_info");
        }
        else
        {
        label.addStyleName("pad-text");
        label.setStyleAttribute("backgroundColor", "white");
        }
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "0px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "0px");
        label.setStyleAttribute("padding-left", "30px");

        String textLabel = text.substring(0, 500);
        if (text.length() > 500) {
            textLabel += "...&nbsp;&nbsp;<a href=\"#\">Подробнее</a><br>";
        }
        label.setText(textLabel);

        if (text.length() > 500) {

            label.addListener(Events.OnClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    createDetail(text, typeNews);
                }
            });
        }
        panel.add(label, new RowData(1, -1, new Margins(0, 30, 0, 30)));
    }

    private void createLabelLast(final Integer typeNews) {
        Text label = new Text("");
        if (typeNews==2)
        {
        label.addStyleName("info_background_last");
        }
        else
        {
        label.addStyleName("pad-text");
        label.setStyleAttribute("backgroundColor", "white");
        }
        
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "0px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "0px");
        label.setStyleAttribute("padding-left", "30px");
        label.setText("&nbsp;");
        panel.add(label,
                new RowData(1, -1, new Margins(0, 30, 0, 30)));
    }
    
    private Text createLabelA(final String text)
    {        
        Text label = new Text("");
        label.addStyleName("info_a");
        //label.addStyleName("pad-text");
        //label.setStyleAttribute("backgroundColor", "white");
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "0px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "0px");
        label.setStyleAttribute("padding-left", "30px");
        label.setText(text);
        panel.add(label, new RowData(1, -1, new Margins(0, 30, 0, 30)));
        return label;
    }
    

    private void createLabelShowMore(final String text, final Integer typeNews) {
        Text label = new Text("");
        if (typeNews==2)
        {
        label.addStyleName("info_background_more");
        }
        else
        {
        label.addStyleName("pad-text");
        label.setStyleAttribute("backgroundColor", "white");
        }
        
        label.setBorders(false);
        label.setStyleAttribute("padding-top", "10px");
        label.setStyleAttribute("padding-right", "30px");
        label.setStyleAttribute("padding-bottom", "10px");
        label.setStyleAttribute("padding-left", "30px");
        label.setText(text);
        label.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showMore(typeNews);
            }
        });
        panel.add(label,
                new RowData(1, -1, new Margins(0, 30, 0, 30)));
    }

    private void showMain() {
        DataGridSearchCondition condition = new DataGridSearchCondition();
        service.getDataGrid("NEWS_HLV", condition, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                Boolean isDevMore = false;
                Boolean isFirmMore = false;
                Integer nDev = 0;
                Integer nFirm = 0;
                ArrayList<String> lstDev;
                lstDev = new ArrayList<String>();
                ArrayList<String> lstFirm;
                lstFirm = new ArrayList<String>();

                for (DDataGrid dd : result) {
                    if (dd.getRows().get(new SKeyForColumn("MAIN:NEWS_TYPE_ID")).getVal() == null) {
                        continue;
                    }
                    Integer type = (Integer) dd.getRows().get(new SKeyForColumn("MAIN:NEWS_TYPE_ID")).getVal();
                    if (type.equals(1))//новости разработчика
                    {
                        if (nDev <= 2) {
                            Date d = (Date) dd.getRows().get(new SKeyForColumn("MAIN:DATE_NEWS")).getVal();
                            String strDate = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
                            //String textDev = "<font color=#445daf><b>" + strDate + "</b></font><br>";
                            String textDev = "<div class='info_date'>" + strDate + "</div>";
                            if (dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal() != null) {
                                textDev += (String) dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal();
                            }
                             else
                            {
                                textDev += "&nbsp";
                            }
                            lstDev.add(textDev);
                        }
                        nDev++;

                    }
                    if (type.equals(2))//новости компании
                    {
                        if (nFirm <= 2) {
                            Date d = (Date) dd.getRows().get(new SKeyForColumn("MAIN:DATE_NEWS")).getVal();
                            String strDate = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
                            //String textFirm = "<font color=#445daf><b>" + strDate + "</b></font><br>";
                            String textFirm = "<div class='info_date'>" + strDate + "</div>";
                            if (dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal() != null) {
                                textFirm += (String) dd.getRows().get(new SKeyForColumn("MAIN:COMMENT")).getVal();
                            }
                            else
                            {
                                textFirm += "&nbsp";
                            }

                            lstFirm.add(textFirm);
                        }
                        nFirm++;
                    }
                    if (nDev > 2 && nFirm > 2) {
                        break;
                    }
                }
                if (nDev > 2) {
                    isDevMore = true;
                }
                if (nFirm > 2) {
                    isFirmMore = true;
                }
                createMain(lstDev, isDevMore, lstFirm, isFirmMore);
            }
        });
    }
}
