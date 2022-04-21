package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.protobuf.Message;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.expansion.STwoKeys;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridEditWindowPostWorkerN extends DataGridEditWindow{

    private final GWTDDataGridServiceAsync service2 = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных

    public DataGridEditWindowPostWorkerN(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }

    public DataGridEditWindowPostWorkerN(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
    }

    public DataGridEditWindowPostWorkerN(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
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

        SKeyForColumn intKeyPost = new SKeyForColumn("POST:INTERACTING_SYST_ID");
        SKeyForColumn intKey = new SKeyForColumn("MAIN:INTERACTING_SYST_ID");
        SKeyForColumn companyKey = new SKeyForColumn("MAIN:COMPANY_ID");
        SKeyForColumn companyDepartKey = new SKeyForColumn("COMPANY:COMPANY_ID");
        DataGridSearchCondition  cnd = new DataGridSearchCondition();
        cnd.getFilters().put(intKey, domain.getRows().get(intKeyPost));
        cnd.getFilters().put(companyKey, domain.getRows().get(companyDepartKey));
        service2.getDataGrid("COMP_INT_SYST_VO", cnd, new AsyncCallback<List<DDataGrid>>() {
            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
                getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                getEditPanel().unmask();
            }

            @Override
            public void onSuccess(List<DDataGrid> result) {
                if (result.size() > 0) {
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
                else {
                    MessageBox.alert("Ошибка", "Предприятие выбранной должности отлично от выбранного подразделения", null);
                    getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                    getEditPanel().unmask();
                }
            }
        });
    }
}
