package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.expansion.STwoKeys;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

import java.util.Date;
import java.util.Map;

/**
 * @author wesStyle
 */
public class DataGridEditWindowDocFile extends DomainEditPanelSimple<DDataGrid>{
    String customHeader = "";
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    private DataGridEditableFormDocFile pnlEdit;
    protected DTable table;
    final protected DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected String tableName = ""; //add LKHorosheva
    public static CommonConstants commonConstants = (CommonConstants) GWT.create(CommonConstants.class);
    FormPanel formPanel;

    protected void editDomain() {}

    public DDataGrid getDomain() {
        return domain;
    }

    protected void notifyDomainSaveSuccesed(String fid) {
        for (DomainSaveSuccesedListener<DDataGrid> listener : domainSaveSuccesedListeners) {
            getDomain().getRows().get(new SKeyForColumn("MAIN:FILE_ID")).setVal(fid);
            listener.onDomainSaveSucceed(getDomain());
        }
    }

    public DataGridEditWindowDocFile(DDataGrid domain, DTable table, EditState state, EWindowType windowType)
    {
        super(domain,state,windowType);
        setTable(table);
        pnlEdit = new DataGridEditableFormDocFile(table, state);
        formPanel = pnlEdit.getFormPanel();

        formPanel.addListener(Events.Submit, new Listener<FormEvent>() {
            public void handleEvent(FormEvent arg0) {
                if (!arg0.getResultHtml().equals("fail")) {
                    notifyDomainSaveSuccesed(arg0.getResultHtml());
                    wnd.removeAllListeners();
                    wnd.hide();
                }
                else {
                    MessageBox.alert("Ошибка", "Ошибка сохранения", null);
                }
                getEditPanel().unmask();
            }
        });

        registerEditForm(pnlEdit);
    }

    public void setTable( DTable table )
    {
        this.table = table;
        this.tableName = table.getQueryName();
    }


    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setSize(680,580);
    }


    @Override
    protected GWTEditServiceAsync getService() {
        return service;
    }


    @Override
    protected Component getEditPanel() {
        return pnlEdit;
    }

    @Override
    protected String getHeaderCreate() {
        return table.getCaption() + " - " + datagridConstants.headerCreate();
    }

    @Override
    protected String getHeaderViewEdit() {
        if (customHeader.length() > 0)
            return table.getCaption() + " - " + customHeader;
        else
            return table.getCaption() + " - " + datagridConstants.headerEdit();
    }

    @Override
    protected String getHeaderView() {
        return table.getCaption() + " - " + datagridConstants.headerView();
    }

    @Override
    protected DDataGrid createDomain() {
        DDataGrid domain = pnlEdit.createDomain();
        return domain;
    }

    @Override
    protected void focusInvalidField() {
        //TODO: add implementation
    }

    public Boolean isDomainNotChanged(DDataGrid firstDomain, DDataGrid secondDomain, DTable table) {
        if ((firstDomain.getRows() == null) != (secondDomain.getRows() == null)) {
            return false;
        }
        /*if (firstDomain.getRows().size() != secondDomain.getRows().size()) {
            return false;
        }*/
        if (!firstDomain.getName().equals(secondDomain.getName())) {
            return false;
        }
        for (Map.Entry rowEntry : firstDomain.getRows().entrySet()) {
            SKeyForColumn key = (SKeyForColumn) rowEntry.getKey();
            IRowColumnVal val = (IRowColumnVal) rowEntry.getValue();

            if (secondDomain.getRows().containsKey(key)) {
                if (!(firstDomain.getRows().get(key).isNotChanges(key, table.getColumnBuilder(key), secondDomain.getRows().get(key).getVal()))) {
                    return false;
                }
            }
            else {
                if (!(firstDomain.getRows().get(key).isNotChanges(key, table.getColumnBuilder(key), null))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Boolean hasUnsavedChanges() {
        DDataGrid empty = (domain != null) ? domain.newInstance() : createDomain();
        if (empty == null) {
            return false;
        }
        editForms.fillDomain(empty);
        return (!isDomainNotChanged(empty, domain, table));
    }

    private Date addHoursToDate(int hours, Date old){
        final long ONE_HOUR_IN_MS = 60000 * 60;
        long oldInMs = old.getTime();
        Date after = new Date(oldInMs + (hours * ONE_HOUR_IN_MS));
        return after;
    }

    protected String checkBegEndDates() {
        if (table.getDateBegEndPairs() != null) {
            for (STwoKeys tk : table.getDateBegEndPairs()) {
                Object val1 = getDomain().getRows().get(tk.getFirstKey()).getVal();
                Object val2 = getDomain().getRows().get(tk.getSecondKey()).getVal();

                if ((val1 != null) && (val2 != null)) {
                    Date beg = (Date) val1;
                    Date end = (Date) val2;
                    if (beg.after(end)) return tk.getMessage();
                }
            }
        }
        return null;
    }

    @Override
    protected void save(final Listener actionListener) {
        getEditPanel().mask();
        formPanel.submit();
       /* getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }

        final DDataGrid oldDomain = domain.newInstance();
        oldDomain.copy(domain);

        editForms.fillDomain(getDomain());
        for (Map.Entry key : getDomain().getRows().entrySet()) {
            SKeyForColumn keyS = (SKeyForColumn)key.getKey();
            if (table.getColumnBuilder(keyS) instanceof DColumnBuilderDate) {
                if (table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyDateField
                        || table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyMonthYearField) {
                    Date dd = (Date)getDomain().getRows().get(keyS).getVal();
                    *//*if (dd != null) {
                        Date ndd = addHoursToDate(12, dd);
                        getDomain().getRows().get(keyS).setVal(ndd);
                    }*//*
                }
            }
        }

        String message = checkBegEndDates();
        if (message != null) {
            getEditPanel().unmask();
            MessageBox.alert(commonConstants.error(), message, null);
        } else {
            ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, (state == EditState.NEW), new AsyncCallback<DDataGrid>() {

                @Override
                public void onFailure(Throwable thrwbl) {
                    getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                    getEditPanel().unmask();
                    //MessageBox.alert(constants.error(), getSaveDomainErrorMessage(), null);
                    AppHelper.showMsgRpcServiceError(thrwbl);
                }

                @Override
                public void onSuccess(DDataGrid t) {
                    if (t == null) {
                        onFailure(null);
                    } else {
                        getDomain().copy(t);
                        updateFormsFromDomain();
                        notifyDomainSaveSuccesed();
                        getEditPanel().unmask();
                        setHeading(getHeaderViewEdit());
                        if (state.equals(EditState.NEW)) {
                            state = EditState.EDIT;
                            updateControlsVisibility();
                        }
                        if (actionListener != null) {
                            actionListener.handleEvent(new BaseEvent(Save));
                        }
                    }
                }
            });
        }*/
    }

    public void setTableName(String name) {
        tableName = name;
    }


    public String getTableName() {
        return tableName;
    }


}