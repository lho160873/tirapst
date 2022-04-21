package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.AppHelper;
import pst.arm.client.modules.datagrid.DataGridWindow;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.lang.DataGridConstants;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionService;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author LKHorosheva
 */
public class DataGridPanelUserDepart extends DataGridPanel {

    private Button btnAddAllDepartForUser, btnDelAllDepartForUser;
    private Listener<DataGridEvent> listenerGetDomain,listenerGetDomainForDel;
    private DataGridWindow pageUser = null;
    private final GWTDDataGridExpansionServiceAsync serviceExp = GWT.create(GWTDDataGridExpansionService.class);
   

    public DataGridPanelUserDepart(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
        initPageUser();
    }

    public DataGridPanelUserDepart(DTable table, String tn) {
        super(table, tn);
        initPageUser();
    }

    public DataGridPanelUserDepart(String tableName) {
        super(tableName);
        initPageUser();
    }

    public DataGridPanelUserDepart(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
        initPageUser();
    }

    public DataGridPanelUserDepart(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
        initPageUser();
    }
    //создаем панель с кнопками управления

    @Override
    protected ToolBar createToolBar() {        
        ToolBar tb = super.createToolBar();
         DataGridConstants constants = GWT.create(DataGridConstants.class);
        btnAddAllDepartForUser = new Button(constants.btnAddAllDepartForUser()); //Расчитать затраты
        btnAddAllDepartForUser.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onAddAllDepartForUser();
            }
        });
        
        btnDelAllDepartForUser = new Button(constants.btnDelAllDepartForUser()); //Расчитать затраты
        btnDelAllDepartForUser.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onDelAllDepartForUser();
            }
        });

        tb.remove(ftiFiltr);
        tb.remove(sprHelpManual);
        tb.remove(btnHelpManual);
        tb.remove(sprFiltr);
        tb.remove(btnFiltr);

        tb.add(new SeparatorToolItem());
        tb.add(btnAddAllDepartForUser);
        tb.add(new SeparatorToolItem());
        tb.add(btnDelAllDepartForUser);
        tb.add(new SeparatorToolItem());

        tb.add(ftiFiltr);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);

        tb.add(sprFiltr);
        tb.add(btnFiltr);

        tb.add(ftiFiltr);
        
        tb.remove(sprColumnsVisibility);
        tb.remove(btnColumnsVisibility);
        tb.add(sprColumnsVisibility);
        tb.add(btnColumnsVisibility);        
        tb.remove(sprColumnsVisibilityDel);
        tb.remove(btnColumnsVisibilityDel);
        tb.add(sprColumnsVisibilityDel);
        tb.add(btnColumnsVisibilityDel);
        tb.add(sprHelpManual);
        tb.add(btnHelpManual);
        tb.add(sprFiltr);
        tb.add(btnFiltr);

        enabledBtn();

        return tb;
    }

    private void initPageUser() {
            listenerGetDomainForDel = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(final DataGridEvent be) {
                pageUser.hide();
                pageUser.removeDataGridListener(listenerGetDomainForDel);
                Integer id = (Integer) be.getDomain().getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                serviceExp.insertOrDeleteUserDepart( id,  false, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable thrwbl) {
                        AppHelper.showMsgRpcServiceError(thrwbl);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        reloadGrid();
                    }                
            });
        }};
                
        listenerGetDomain = new Listener<DataGridEvent>() {
            @Override
            public void handleEvent(final DataGridEvent be) {
                pageUser.hide();
                pageUser.removeDataGridListener(listenerGetDomain);
                Integer id = (Integer) be.getDomain().getRows().get(new SKeyForColumn("MAIN:USER_ID")).getVal();
                serviceExp.insertOrDeleteUserDepart( id,  true, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable thrwbl) {
                        AppHelper.showMsgRpcServiceError(thrwbl);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        reloadGrid();
                    }                
            });
        }};
        
        pageUser = new DataGridWindow("USERS_FOR_OCP_RIGHTS_FOR_REC_IG2", true, true);
    }

    private void onAddAllDepartForUser() {
        pageUser.addDataGridListener(listenerGetDomain);
        pageUser.show();
    }
    
    private void onDelAllDepartForUser() {
        pageUser.addDataGridListener(listenerGetDomainForDel);
        pageUser.show();
    }
    
  
}
