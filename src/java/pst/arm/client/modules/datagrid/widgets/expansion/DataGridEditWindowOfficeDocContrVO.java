package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.expansion.STwoKeys;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridEditableForm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridEditWindowOfficeDocContrVO extends DomainEditPanelSimple<DDataGrid> {
    String customHeader = "";
    private GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class);
    private DataGridEditableFormOfficeDocContrVO pnlEdit;
    protected DTable table;
    final protected DataGridConstants datagridConstants = GWT.create(DataGridConstants.class);
    protected String tableName = ""; //add LKHorosheva
    public static CommonConstants commonConstants = (CommonConstants) GWT.create(CommonConstants.class);

    protected void editDomain() {}

    public DataGridEditWindowOfficeDocContrVO(DDataGrid domain, DTable table, DTable tablePeriod, EditState state, EWindowType windowType)
    {
        super(domain, state, windowType);
        setTable(table);
        pnlEdit = new DataGridEditableFormOfficeDocContrVO(table, tablePeriod, state);
        registerEditForm(pnlEdit);
    }

    public DataGridEditWindowOfficeDocContrVO(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, state, windowType);
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormOfficeDocContrVO(table, state, cnd);
        registerEditForm(pnlEdit);
    }

    public DataGridEditWindowOfficeDocContrVO(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, state, windowType);
        this.customHeader = customHeader;
        editDomain();
        setTable(table);
        pnlEdit = new DataGridEditableFormOfficeDocContrVO(table, state, cnd, customHeader);
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
        for (Map.Entry key : getDomain().getRows().entrySet()) {
            SKeyForColumn keyS = (SKeyForColumn)key.getKey();
            if (table.getColumnBuilder(keyS) instanceof DColumnBuilderDate) {
                if (table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyDateField
                        || table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyMonthYearField) {
                    Date dd = (Date)getDomain().getRows().get(keyS).getVal();
                    /*if (dd != null) {
                        Date ndd = addHoursToDate(12, dd);
                        getDomain().getRows().get(keyS).setVal(ndd);
                    }*/
                }
            }
        }

        String message = checkBegEndDates();

        if (message != null) {
            getEditPanel().unmask();
            MessageBox.alert(commonConstants.error(), message, null);
        } else {
            if (pnlEdit.getCb().getValue()) {

                        Integer periodVal = (Integer)pnlEdit.getVal().getValue();
                        String typeStr = pnlEdit.getType().getRawValue();
                        Integer type = -1;

                        if (typeStr.equals("год")) type = 1;
                        if (typeStr.equals("месяц")) type = 2;
                        if (typeStr.equals("неделя")) type = 3;

                        SKeyForColumn key = new SKeyForColumn("MAIN:DATE_PLAN");
                        Date plan = (Date)(getDomain().getRows().get(key).getVal());
                        Integer day = Integer.parseInt(DateTimeFormat.getFormat("dd-MM-yyyy").format(plan).split("-")[0]);
                        if (type == 1 && ((periodVal == null) || (periodVal != 1))) {
                            getEditPanel().unmask();
                            MessageBox.alert(commonConstants.error(), "Периодичность типа \"Год\" должна быть только 1 год", null);
                            return;
                        } else if (type == 2 && ((periodVal == null) || !((periodVal == 1) || (periodVal == 2) || (periodVal == 3) || (periodVal == 4) || (periodVal == 5) || (periodVal == 6)))) {
                            getEditPanel().unmask();
                            MessageBox.alert(commonConstants.error(), "Периодичность типа \"Месяц\" может быть только 1, 2, 3, 4, 5, 6 месяцев", null);
                            return;
                        } else if (type == 3 && ((periodVal == null) || !((periodVal == 1) || (periodVal == 2)))) {
                            getEditPanel().unmask();
                            MessageBox.alert(commonConstants.error(), "Периодичность типа \"Неделя\" может быть только 1, 2 недель", null);
                            return;
                        } else if ((type != 3) && (day >= 29) && (day <= 31)) {
                            getEditPanel().unmask();
                            MessageBox.alert(commonConstants.error(), "Периодичность типа \"Месяц\" не может быть применена к назначенной дате с числами дня 29, 30 , 31", null);
                            return;
                        } else if (type == -1) {
                            getEditPanel().unmask();
                            MessageBox.alert(commonConstants.error(), "Поле ТИП ПЕРИОДА должно быть заполнено", null);
                            return;
                        }

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

                                    DDataGrid domain2 = new DDataGrid();
                                    IRowColumnVal val;
                                    SKeyForColumn key;

                                    val = new DRowColumnValNumber();
                                    val.setVal(null);

                                    key = new SKeyForColumn("MAIN:DOC_EXEC_PERIOD_ID");
                                    domain2.getRows().put(key, val);

                                    key = new SKeyForColumn("MAIN:PARENT_DOC_CONTR_ID");
                                    domain2.getRows().put(key, val);

                                    key = new SKeyForColumn("MAIN:RECIPIENT_ID");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:OFFICE_DOC_CONTR_ID");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:OFFICE_DOC_ID");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:DOC_LINE");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:DATE_PLAN");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:POSS_STEP_ID");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:COMMENT");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:USER_ID");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    key = new SKeyForColumn("MAIN:DATE_CORR");
                                    domain2.getRows().put(key, t.getRows().get(key));

                                    Integer periodVal = (Integer)pnlEdit.getVal().getValue();
                                    String typeStr = pnlEdit.getType().getRawValue();
                                    Integer type = -1;

                                    if (typeStr.equals("год")) type = 1;
                                    if (typeStr.equals("месяц")) type = 2;
                                    if (typeStr.equals("неделя")) type = 3;

                                    key = new SKeyForColumn("MAIN:PERIOD_TYPE_ID");
                                    val = new DRowColumnValNumber();
                                    val.setVal(type);
                                    domain2.getRows().put(key, val);

                                    key = new SKeyForColumn("MAIN:PERIOD_VALUE");
                                    val = new DRowColumnValNumber();
                                    val.setVal(periodVal);
                                    domain2.getRows().put(key, val);

                                    val = new DRowColumnValDate();
                                    key = new SKeyForColumn("MAIN:DATE_PLAN");

                                    Date next1 = (Date)(t.getRows().get(key).getVal());
                                    Date next = (Date)next1.clone();

                                    if (type == 1) {
                                        CalendarUtil.addMonthsToDate(next, 12);
                                    } else if (type == 2) {
                                        CalendarUtil.addMonthsToDate(next, periodVal);
                                    } else if (type == 3) {
                                        CalendarUtil.addDaysToDate(next, periodVal * 7);
                                    }

                                    val.setVal(next);
                                    key = new SKeyForColumn("MAIN:DATE_NEXT");
                                    domain2.getRows().put(key, val);


                                    getDomain().copy(t);
                                    ((GWTDDataGridServiceAsync) getService()).save("DOC_EXEC_PERIOD_VO", domain2, domain2, (state == EditState.NEW), new AsyncCallback<DDataGrid>() {

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
                                }
                            }
                        });
            }
            else {
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
            }
        }
    }

    public void setTableName(String name) {
        tableName = name;
    }


    public String getTableName() {
        return tableName;
    }


}
