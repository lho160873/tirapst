package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
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
public class DataGridEditWindowDhJobRev extends DomainEditPanelSimple<DDataGrid>{
    String customHeader = "";
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    private DataGridEditableForm pnlEdit;
    protected DTable table;
    final protected DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected String tableName = ""; //add LKHorosheva
    public static CommonConstants commonConstants = (CommonConstants) GWT.create(CommonConstants.class);

    protected void editDomain() {}

    public DataGridEditWindowDhJobRev(DDataGrid domain, DTable table, EditState state, EWindowType windowType)
    {
        super(domain,state,windowType);
        setTable(table);
        pnlEdit = new DataGridEditableForm(table, state);
        registerEditForm(pnlEdit);
    }

    public DataGridEditWindowDhJobRev(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, state, windowType);
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableForm(table, state, cnd);
        registerEditForm(pnlEdit);
    }

    public DataGridEditWindowDhJobRev(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, state, windowType);
        this.customHeader = customHeader;
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableForm(table, state, cnd, customHeader);
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
    protected GWTDDataGridServiceAsync getService() {
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
            if (secondDomain.getRows().containsKey(key)) {
                if (!(firstDomain.getRows().get(key).isNotChanges(key, table.getColumnBuilder(key), secondDomain.getRows().get(key).getVal()))) {
                    return false;
                }
            } else {
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
                if ( (val1 != null) && (val2 != null) ) {

                    Date beg = null;
                    Date end = null;
                    if (val1.getClass() == String.class) {
                        if (!((String) val1).isEmpty()) {
                            String format = table.getColumnBuilder(tk.getFirstKey()).getColumn(tk.getFirstKey()).getColumnProperty().getFormat();
                            beg = DateTimeFormat.getFormat(format).parse((String) val1);
                        }
                    } else if (val1.getClass() == Date.class) {
                        beg = (Date) val1;
                    }

                    if (val2.getClass() == String.class) {
                        if (!((String) val2).isEmpty()) {
                            String format = table.getColumnBuilder(tk.getSecondKey()).getColumn(tk.getSecondKey()).getColumnProperty().getFormat();
                            end = DateTimeFormat.getFormat(format).parse((String) val2);
                        }
                    } else if (val2.getClass() == Date.class) {
                        end = (Date) val2;
                    }

                    if (beg != null && end != null) {
                        if (beg.after(end)) {
                            return tk.getMessage();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }

        final DDataGrid oldDomain = domain.newInstance();
        oldDomain.copy(domain);

        editForms.fillDomain(getDomain());




        java.util.Date utilDate = (Date) domain.getRows().get(new SKeyForColumn("MAIN:DATE_START")).getVal();

        utilDate.setHours(12);
        utilDate.setMinutes(1);
        utilDate.setSeconds(1);

        Date curr = new Date();
        curr.setHours(12);
        curr.setMinutes(1);
        curr.setSeconds(0);


            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Integer oId = (Integer) domain.getRows().get(new SKeyForColumn("MAIN:id_order")).getVal();
            Integer intId = 4;

            String proc = "exec [dbo].[DH_JOB_PLAN_REV] '" + sqlDate + "'," + oId + "," + intId;

            getService().execProc(proc, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    getEditPanel().unmask();
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        getEditPanel().unmask();
                        MessageBox.alert("Внимание", "Ошибка. Обратитесь к Администратору", null);
                    }
                    else {
                        getEditPanel().unmask();
                        notifyDomainSaveSuccesed();
                        closeWindow();
                    }
                }
            });

/*
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