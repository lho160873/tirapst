/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import static pst.arm.client.common.ui.controls.editdomain.DomainEditPanelSimple.Save;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DColumnBuilderDate;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyDateField;
import pst.arm.client.modules.datagrid.domain.DColumnPropertyMonthYearField;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import static pst.arm.client.modules.datagrid.widgets.DataGridEditWindow.commonConstants;
import pst.arm.client.modules.massmailingig.service.remote.GWTMassMailingIgService;
import pst.arm.client.modules.massmailingig.service.remote.GWTMassMailingIgServiceAsync;

/**
 *
 * @author Igor
 */
public class DataGridEditWindowMassMailingIg extends DataGridEditWindow {
    
    Integer massMailingId;

    public DataGridEditWindowMassMailingIg(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
    }
    
    public DataGridEditWindowMassMailingIg(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType, Integer massMailingId) {
        super(domain, table, state, windowType);
        this.massMailingId = massMailingId;
    }

    public DataGridEditWindowMassMailingIg(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
    }

    public DataGridEditWindowMassMailingIg(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
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
        for (Map.Entry key : getDomain().getRows().entrySet()) {
            SKeyForColumn keyS = (SKeyForColumn) key.getKey();
            if (table.getColumnBuilder(keyS) instanceof DColumnBuilderDate) {
                if (table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyDateField
                        || table.getColumnBuilder(keyS).getColumn(keyS).getColumnProperty() instanceof DColumnPropertyMonthYearField) {
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
                    AppHelper.showMsgRpcServiceError(thrwbl);
                }

                @Override
                public void onSuccess(DDataGrid t) {
                    if (t == null) {
                        onFailure(null);
                    } else {

                        addWithCopy();

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

    private void addWithCopy() {     
        GWTMassMailingIgServiceAsync massMailingService = GWT.create(GWTMassMailingIgService.class);
        
        massMailingService.addWithCopy(ConfigurationManager.getUserId().intValue(), massMailingId, new AsyncCallback<DDataGrid>() {

            @Override
            public void onFailure(Throwable caught) {
               AppHelper.showMsgRpcServiceError(caught); 
            }

            @Override
            public void onSuccess(DDataGrid result) {
                
            }
        });
    }
    
    @Override
    protected String getHeaderCreate() {
        return table.getCaption() + " - " + datagridConstants.headerAddWithCopy();
    }
}
