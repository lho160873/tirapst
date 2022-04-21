package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;

import java.util.Date;

/**
 *
 * @author wesStyle
 */
public class DataGridEditWindowWorkPlan extends DataGridEditWindow {
    private DDataGrid ns;

    private DDataGrid cpy;

    private boolean isFromCopy;

    public DataGridEditWindowWorkPlan(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }

    public DataGridEditWindowWorkPlan(DDataGrid domain, DDataGrid ns, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
        this.ns = ns;
    }

    public DataGridEditWindowWorkPlan(DDataGrid domain, DDataGrid ns, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
        this.ns = ns;
    }

    public DataGridEditWindowWorkPlan(int companyId, DDataGrid domain, DDataGrid ns, DTable table, EditState state, EWindowType windowType, DCondition cnd, Boolean isFromCopy) {
        super(domain, table, state, windowType, cnd);

        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormWorkPlan(table, state, cnd, companyId);
        registerEditForm(pnlEdit);

        this.ns = ns;
        this.isFromCopy = isFromCopy;
    }

    public DataGridEditWindowWorkPlan(int companyId, DDataGrid domain, DDataGrid ns, DTable table, EditState state, EWindowType windowType, DCondition cnd, Boolean isFromCopy, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);

        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormWorkPlan(table, state, cnd, companyId);
        registerEditForm(pnlEdit);

        this.ns = ns;
        this.isFromCopy = isFromCopy;
    }

    public DataGridEditWindowWorkPlan(DDataGrid domain, DDataGrid ns, DTable table, EditState state, EWindowType windowType, DCondition cnd, Boolean isFromCopy, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
        this.ns = ns;
        this.isFromCopy = isFromCopy;
    }

    @Override
    protected void closeWindow() {
        checkUnsavedChanged(new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeAllListeners();
                hide();
                if (wnd != null) {
                    wnd.removeAllListeners();
                    wnd.hide();
                }
                notifyWindowHide();
            }
        });
    }

    @Override
    protected Boolean hasUnsavedChanges() {
        DDataGrid empty = (domain != null) ? domain.newInstance() : createDomain();
        if (empty == null || domain  == null) {
            return false;
        }
        editForms.fillDomain(empty);
        return (!isDomainNotChanged(empty, domain, table));
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

        SKeyForColumn avgsalKey = new SKeyForColumn("MAIN:AVERAGE_SALARY");
        Double avgsal = (Double) ns.getRows().get(avgsalKey).getVal();

        SKeyForColumn pllKey = new SKeyForColumn("MAIN:PLAN_LABOUR");
        Double pll = (Double) getDomain().getRows().get(pllKey).getVal();


        final Double costNorm = avgsal * pll;
        getDomain().getRows().get(new SKeyForColumn("MAIN:PLAN_COST_NORM")).setVal(costNorm);

        AsyncCallback<DDataGrid> callback = new AsyncCallback<DDataGrid>() {
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
        };

        String message = checkBegEndDates();
        String message2 = checkDatesForSameMonth();
        if (message != null || message2 != null) {
            getEditPanel().unmask();
            if (message != null)
                MessageBox.alert(commonConstants.error(), message, null);
            else
                MessageBox.alert(commonConstants.error(), message2, null);
        } else {

            if (table.getName().equals("DEPART_EXECUTOR_PLAN_VO")) {
                SKeyForColumn begKey = new SKeyForColumn("MAIN:BEG_DATE");
                SKeyForColumn endKey = new SKeyForColumn("MAIN:END_DATE");
                Date begDate = (Date) domain.getRows().get(begKey).getFormatVal(begKey, table.getColumnBuilder(begKey));
                Date endDate = (Date) domain.getRows().get(endKey).getFormatVal(endKey, table.getColumnBuilder(endKey));

                if (begDate.after(endDate)) {
                    getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                    getEditPanel().unmask();
                    MessageBox.alert("Ошибка", "Дата начала работы не может быть больше даты окончания работы.", null);
                } else {
                    if (!isFromCopy)
                        ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, (state == EditState.NEW), callback);
                    else
                        ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, true, callback);
                }
            } else {
                if (!isFromCopy)
                    ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, (state == EditState.NEW), callback);
                else ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, true, callback);
            }
        }
    }

    String checkDatesForSameMonth() {

        Date d1 = (Date) getDomain().getRows().get(new SKeyForColumn("MAIN:BEG_DATE")).getVal();
        Date d2 = (Date) getDomain().getRows().get(new SKeyForColumn("MAIN:END_DATE")).getVal();

        if ((d1.getMonth() == d2.getMonth()) && (d1.getYear() == d2.getYear()))
            return null;
        else
            return "«Дата начала» и «Дата окончания» должны относиться к одному календарному месяцу";
    }
}
