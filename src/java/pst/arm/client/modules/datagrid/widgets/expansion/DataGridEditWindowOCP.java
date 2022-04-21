package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;

/**
 *
 * @author wesStyle
 */
public class DataGridEditWindowOCP extends DataGridEditWindow{
    IRowColumnVal ocpId;

    public DataGridEditWindowOCP(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }

    public DataGridEditWindowOCP(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
    }

    @Override
    protected void editDomain() {
        SKeyForColumn keyForColumn = new SKeyForColumn("MAIN:OCP_ID");
        ocpId = domain.getRows().get(keyForColumn);
;       domain.getRows().remove(keyForColumn);
    }

    @Override
    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }

        if (ocpId == null) {
            DDataGrid d = getDomain();
            editForms.fillDomain(getDomain());

            d.getRows().get(new SKeyForColumn("MAIN:CONTRACT_ID"))
                    .setVal(null);

            final DDataGrid oldDomain = domain.newInstance();
            oldDomain.copy(domain);

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
        } else {
            editForms.fillDomain(getDomain());
            getDomain().getRows().put(new SKeyForColumn("MAIN:OCP_ID"), ocpId);
            getDomain().getRows().put(new SKeyForColumn("MAIN:LINE_AIS"), null);
            ocpId = null;
//            HashMap<SKeyForColumn, IRowColumnVal> rws = new HashMap<SKeyForColumn, IRowColumnVal>();
//            rws.put(new SKeyForColumn("MAIN:OCP_ID"), ocpId);
//            IRowColumnVal val = (IRowColumnVal) domain.getRows().get(new SKeyForColumn("MAIN:CONTRACT_STAGE_ID")).getVal();
//            rws.put(new SKeyForColumn("MAIN:CONTRACT_STAGE_ID"), val);
//            getDomain().setRows(rws);

            final DDataGrid oldDomain = domain.newInstance();
            oldDomain.copy(domain);

            String message = checkBegEndDates();
            if (message != null) {
                getEditPanel().unmask();
                MessageBox.alert(commonConstants.error(), message, null);
            } else {
                ((GWTDDataGridServiceAsync) getService()).save(tableName, getDomain(), oldDomain, true, new AsyncCallback<DDataGrid>() {
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

}
