package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.images.DataGridImages;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.lang.DataGridMessages;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridReportService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridReportServiceAsync;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;

/**
 *
 * @author LKHorosheva, wesStyle, igor
 * @since 0.0.1
 */
public abstract class DataBasePanel extends ContentPanel {

    protected final GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
    private final GWTDDataGridReportServiceAsync reportService = GWT.create(GWTDDataGridReportService.class);
    protected final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected final DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected final DataGridMessages datagridMessages = GWT.create(DataGridMessages.class);
    protected final CommonImages commonImages = GWT.create(CommonImages.class);
    protected final DataGridImages datagridImages = GWT.create(DataGridImages.class);
    protected String tableName = "";
    protected DTable table = null;
    protected BorderLayoutData requestPanelData;
    protected DataGridFiltr panelFiltr = null; //панель с полями поиска
    protected LayoutContainer main; //основной лейоут
    //protected Boolean isStartWithShowPanelFiltr = Boolean.TRUE;  ///<признак того показывать ли панель с фильтром (если она предусмотрена) сразу или нет
    protected Button btnSub, btnFiltr;
    //protected ToggleButton btnFiltr;
    public SeparatorToolItem sprEdit, sprFiltr, sprSend, sprReport, sprSub, sprMark, sprHelpManual, sprColumnsVisibility,sprColumnsVisibilityDel;
    protected SeparatorMenuItem sprMenuItemEdit, sprMenuItemFiltr, sprMenuItemSend, sprMenuItemReport, sprMenuItemMark;
    public FillToolItem ftiFiltr;
    protected Menu menu, reportMenuSplit;
    protected Boolean isBtnSendEnabled = false; //доступна ли передача данных
    public Button btnAdd, btnView, btnDelete, btnEdit, btnSend, btnRefr, btnMark, btnDelegate, btnReport, btnHelpManual, btnAddWithCopy, btnColumnsVisibility,btnColumnsVisibilityDel;  // btnReport теперь не используется, её заменила btnReportSplit
    public Button btnReportSplit;
    protected MenuItem menuItemAdd, menuItemView, menuItemDelete, menuItemEdit, menuItemRefr, menuItemReport, menuItemMark, menuItemDelegate, menuItemAddWithCopy;
    protected Editable.EditMode mode = Editable.EditMode.VIEWEDIT;
    protected DataGridSearchCondition condition; //условия фильтрации данных
    protected HashMap<SKeyForColumn, IRowColumnVal> passedFields;
    protected ToolBar toolBar;    
    protected Boolean  isDictAdd=null, isDictEdit=null, isDictDelete=null, isDictView=null, isSprDictEdit=null;
    

    public void clearSelectionModel() {

    }

    public void setTableName(String name) {
        //tableName = name.toUpperCase();
        tableName = name;
    }

    public String getTableName() {
        return tableName;
    }

    public DTable getTable() {
        return table;
    }

    public void resizePanelFiltr() {
        if (panelFiltr.isVisible()) {
            requestPanelData.setSize(panelFiltr.getEnoughHeight());
            main.layout(true); //перерисовываем               
        }
    }

    public void initIsFiltrShow() {
        if (table == null) {
            return;
        }
              
        btnFiltr.setVisible(table.getIsFiltrShow());
        sprFiltr.setVisible(table.getIsFiltrShow());

        setIsShowPanelFiltr(table.getIsFiltrShow() && table.getIsFilterShowAtStart());
    }
    
    public void initIsHelpManualShow() {
        if (table == null) {
            return;
        }
        if (btnHelpManual == null || sprHelpManual == null) {
            return;
        }
        Boolean isVisible = (table.getHelpManual()!=null && !table.getHelpManual().isEmpty());
        
        btnHelpManual.setVisible(isVisible);
        sprHelpManual.setVisible(isVisible);
        
        
        removeOrAddBtnOnToolBar(isVisible, btnHelpManual);
        removeOrAddBtnOnToolBar(isVisible, sprHelpManual);
        
    }

    public void setIsShowPanelFiltr(Boolean isShow) {
        if (isShow) {
            panelFiltr.show();
            resizePanelFiltr();
        } else {
            panelFiltr.hide();
        }
        //btnFiltr.toggle(isShow);
        if (!isShow) {
            btnFiltr.setToolTip(datagridConstants.btnShowFiltr());
        } else {
            btnFiltr.setToolTip(datagridConstants.btnHideFiltr());
        }
    }

    public String getCaption() {
        if (table == null) {
            return "";
        }
        return table.getCaption();
    }

    public void addDataGridListener(Listener<? extends DataGridEvent> listener) {
        this.addListener(DataGridEvents.DataGridSend, listener);
    }

    public void removeDataGridListener(Listener<? extends DataGridEvent> listener) {
        this.removeListener(DataGridEvents.DataGridSend, listener);
    }

    public void addDataGridListenerChanged(Listener<? extends DataGridEvent> listener) {
        //MessageBox.info("","addDataGridListenerChanged",null);
        this.addListener(DataGridEvents.DataGridChanged, listener);
    }

    public void removeDataGridListenerChanged(Listener<? extends DataGridEvent> listener) {
        this.removeListener(DataGridEvents.DataGridChanged, listener);
    }

    protected void fireDataGridEvent(EventType eventType, DDataGrid domain, DTable table) {
        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                DataGridEvent event = new DataGridEvent(this, domain, table);
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }
    
    //сигнал посылаеться(создается) и тут же обрабатываеться(handleEvent) только в том случае если на него были подписчики (this.addListener(eventType, listener);
    protected void fireDataGridEventLoad(EventType eventType) {
        List<Listener<? extends BaseEvent>> listeners = getListeners(eventType);
        if (listeners != null) {
            for (Listener listener : listeners) {
                BaseEvent event = new BaseEvent(this);
                event.setType(eventType);
                listener.handleEvent(event);
            }
        }
    }
    
     public void removeOrAddBtnOnToolBar(Boolean isBtnVisible, Component cmpnt) {
        if (toolBar == null) {
            return;
        }
        if (isBtnVisible == null) {
            return;
        }
        if (cmpnt == null) {
            return;
        }
        if (!isBtnVisible) {
            toolBar.remove(cmpnt);
        } else {
            if (toolBar.indexOf(cmpnt) == -1) {
                toolBar.add(cmpnt);
            }
        }
        toolBar.repaint();
    }
     
    public void setIsBtnSendEnabled(Boolean b) {
        isBtnSendEnabled = b;
        btnSend.setVisible(b);
        sprSend.setVisible(b);
        
        //removeOrAddBtnOnToolBar(b,btnSend);
        //removeOrAddBtnOnToolBar(b,sprSend);
    }

    public void setMode(Editable.EditMode mode) {
        this.mode = mode;
    }

    public DataGridSearchCondition getCondition() {
        return condition;
    }

    protected GWTDDataGridServiceAsync getService() {
        return service;
    }

    protected GWTDDataGridReportServiceAsync getReportService() {
        return reportService;
    }

    public ToolBar getTopToolbar() {
        return toolBar;
    }

 /**
 *  Показывает отчёт нужного типа: "PDF", "RTF", "XLS"
 * @author Igor
 * @param reportType
 *        Тип отчёта (допустимые типы: "PDF", "RTF", "XLS")
 */
    protected void showReport(String reportType) {
        //reportService.generateHtmlArchiveStoreEntityReport(condition, new HtmlReportServiceCallback(this));
        if (table.getReportTemplate() == null) {
            getReportService().generateHtmlArchiveStoreEntityReport(tableName, condition, reportType, new FileReportServiceCallback(this));
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (table.getReportParams() != null) {
                for (Map.Entry<String, String> tp : table.getReportParams().entrySet()) {
                    for (Map.Entry filtr : condition.getFilters().entrySet()) {
                        SKeyForColumn fKey = (SKeyForColumn) filtr.getKey();
                        IRowColumnVal fVal = (IRowColumnVal) filtr.getValue();
                        String fStr = fVal.getStringFromVal(fKey, table.getColumnBuilder(fKey));
                        if (fKey.equals(new SKeyForColumn(tp.getValue()))) {
                            if (fStr == null) {
                                params.put(tp.getKey(), "");
                            } else {
                                params.put(tp.getKey(), fStr);
                            }
                        }
                    }
                }
            }

            getReportService().generateJasperReport(table.getReportTemplate(), table.getReportExportFormat(), params, condition, table, new FileReportServiceCallback(this));
        }

    }

    protected void showHelpManual() {
        if (table.getHelpManual() == null) {
            MessageBox.info("", "Файл с документацией не найден", null);
        }
        final String file = table.getHelpManual();
        getService().openHelpManual(file, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
               AppHelper.showMsgRpcServiceError(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "download.htm?fileId=" + file);
                } else {
                    MessageBox.alert("Внимание", "Ошибка открытия файла " + file + " Обратитесь к Администратору.", null);
                }
            }
        });
    }
    
    public abstract void search();
    
    public abstract void enabledBtn();

    public abstract void reloadGrid();

    public abstract void clearStore();
    
    public void updateToolBar(){};
    
    
}
