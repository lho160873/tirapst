package pst.arm.client.modules.datagrid.widgets;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.common.AppHelper;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.event.DataBasePanelEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.pages.DataGridPanelChoice;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.expansion.*;

/**
 * createDataGridPanel_test
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DataGridPanelBuilder {

    static public DataBasePanel createDataGridPanel(String tableName) {
        String name = tableName.toLowerCase();
        return new DataGridPanel(name);
    }

    static public DataBasePanel createDataGridPanel(String tableName, SRelationInfo relationInfo) {
        String name = tableName.toLowerCase();
        if (name.equals("SERV_CONTRACT_STAGE_VO_FOR_OCP".toLowerCase())) {
            return new DataGridPanelOCP(name, relationInfo);
        } else if (name.equals("OCP_VO".toLowerCase())) {
            return new DataGridPanelOCPMain(name, relationInfo);
        } else if (name.equals("WORK_PLAN_VO".toLowerCase())) {
            return new DataGridPanelWorkPlan(name, relationInfo);
        } else if (name.equals("DEPART_EXECUTOR_PLAN_VO".toLowerCase())) {
            return new DataGridPanelWD(name, relationInfo);
        } else if (name.equals("CONTRACTS_VO_DOP".toLowerCase())) {
            return new DataGridPanelContractDop(name, relationInfo);
        } else if (name.equals("PLAN_FACT_NIOKR".toLowerCase())) {
            return new DataGridPanelNotEditable(name, relationInfo);
        } else if (name.equals("PLAN_FACT_NIOKR_DOP1".toLowerCase())) {
            return new DataGridPanelPlanFactNiokr(name, relationInfo);
        } else if (name.equals("OCP_OUTER".toLowerCase())) {
            return new DataGridPanelOcpOuter(name, relationInfo);
        } else if (name.equals("OCP_SERVICE".toLowerCase())) {
            return new DataGridPanelOcpService(name, relationInfo);
        } else if (name.equals("IG_OCP_PKI".toLowerCase())) {
            return new DataGridPanelOcpPki(name, relationInfo);
        } else if (name.equals("OCP_TRIP_VO".toLowerCase())) {
            return new DataGridPanelTrip(name, relationInfo);
        } else if (name.equals("OFFICE_DOC_VO".toLowerCase())) {
            return new DataGridPanelOfficeDoc(name, relationInfo);
        } else if (name.equals("OFFICE_DOC_CONTR_IG".toLowerCase())) {
            return new DataGridPanelOfficeDocContrIg(name, relationInfo);
        } else if (name.equals("DOC_FILE_VO".toLowerCase())) {
            return new DataGridPanelDocFile(name, relationInfo);
        } else if (name.equals("DOC_FILE_OCP_VO".toLowerCase())) {
            return new DataGridPanelDocFile(name, relationInfo);
        } else if (name.equals("DOC_FILE_IG".toLowerCase())) {
            return new DataGridPanelDocFileIg(name, relationInfo);
        }  else if (name.equals("DOC_FILE_DH_ELABORATION_OF_DD_HLV".toLowerCase()) || name.equals("DOC_FILE_APP_PRODUCTION_HLV".toLowerCase()) ) {
            return new DataGridPanelDocFileElaboration(name, relationInfo);
        }  else if (name.equals("DOC_FILE_LEVEL_TASK_HLV".toLowerCase()) ) {
            return new DataGridPanelDocFileLevelTask(name, relationInfo);
        }  else if (name.equals("OCP_RIGHTS_FOR_REC_IG".toLowerCase())) {
            return new DataGridPanelOcpRightsForRecIg(name, relationInfo);
        } else if (name.equals("OFFICE_DOC_CONTR_COLLAB_IG".toLowerCase())) {
            return new DataGridPanelOfficeDocContrCollabIg(name, relationInfo);
        } else if (name.equals("DEPART_POST".toLowerCase())) {
            return new DataGridPanelDepartPostIg(name, relationInfo);
        } else if (name.equals("DH_ORDER_PRIBOY_HLV".toLowerCase())) {
            return new DataGridPanelDhOrderPriboy(name, relationInfo);
        } else if (name.equals("DH_ORDER_MART_HLV".toLowerCase())) {
            return new DataGridPanelDhOrderMart(name, relationInfo);
        } else if (name.equals("OFFICE_DOC_CONTR_VO".toLowerCase())) {
            return new DataGridPanelOfficeDocContrVO(name, relationInfo);
        } else if (name.equals("DOC_EXEC_PERIOD_FULL_VO".toLowerCase())) {
            return new DataGridPanelExecPeriod(name, relationInfo);
        } else if (name.equals("POST_WORKER_N_VO".toLowerCase())) {
            return new DataGridPanelPostWorkerN(name, relationInfo);
        } else if (name.equals("PLAN_NTO_YEAR_VO".toLowerCase())) {
            return new DataGridPanelPlanNTOYear(name, relationInfo);
        } else if (name.equals("ORDER_BLANK_WORK".toLowerCase())) {
            return new DataGridPanelOrderBlankWork(name, relationInfo);
        } else if (name.equals("ORDER_BLANK_WORK2".toLowerCase())) {
            return new DataGridPanelOrderBlankWork2(name, relationInfo);
        } else if (name.equals("ORDER_BLANK_WORK3".toLowerCase())) {
            return new DataGridPanelOrderBlankWork3(name, relationInfo);
        } else if (name.equals("ORDER_BLANK_WORK4".toLowerCase())) {
            return new DataGridPanelOrderBlankWork4(name, relationInfo);
        } else if (name.equals("ORDER_BLANK_WORK5".toLowerCase())) {
            return new DataGridPanelOrderBlankWork5(name, relationInfo);
        }  else if (name.equals("COMMANDER".toLowerCase())) {
            return new DataGridPanelCommander(name, relationInfo);
        } else if (name.equals("DH_FACILITIES_PRIBOY_IG".toLowerCase()) || name.equals("DH_FACILITIES_MART_IG".toLowerCase())) {
            return new DataGridPanelDhFacilities(name, relationInfo);
        } else if (name.equals("OFFICE_DOC_EXEC_IG".toLowerCase())) {
            return new DataGridPanelOfficeDocExecIg(name, relationInfo);
        } else if (name.equals("DH_ORDER_1_VO".toLowerCase())) {
            return new DataGridPanelDhOrder1(name, relationInfo);
        } else if (name.equals("DH_JOB_1_HLV".toLowerCase())) {
            return new DataGridPanelDhJob1(name, relationInfo);
        } else if (name.equals("MASS_MAILING_LIST_IG".toLowerCase())) {
            return new DataGridPanelMassMailingListIg(name, relationInfo);
        } else if (name.equals("MASS_MAILING_IG".toLowerCase())) {
            return new DataGridPanelMassMailingIg(name, relationInfo);
        } else if (name.equals("DEPART_EXECUTOR_FACT".toLowerCase())) {
            return new DataGridPanelDepartExecutorFactIg(name, relationInfo);
        } else if (name.equals("WORKSHOP_LOAD_VO".toLowerCase())) {
            return new DataGridPanelWorkshopLoadVO(name, relationInfo);
        } else if (name.equals("DEPART_POST_WORKER_VO".toLowerCase())) {
            return new DataGridPanelDepartPostWorker(name, relationInfo);
        } else if (name.equals("ORDER_D_IG".toLowerCase())) {
            return new DataGridPanelOrderDIg(name, relationInfo);
        }  else if (name.equals("HLV_USER_DEPART".toLowerCase())) {
            return new DataGridPanelUserDepart(name, relationInfo);
        } else if (name.equals("DH_ELABORATION_OF_DD_HLV".toLowerCase())) {
            return new DataGridPanelDhElaborationOfDd(name, relationInfo);
        } else if (name.equals("DT_ELABORATION_OF_DD_HLV".toLowerCase())|| name.equals("APP_PROD_COMMENT_HLV".toLowerCase())) {
            return new DataGridPanelDtElaborationOfDd(name, relationInfo);
        } else if (name.equals("APP_PRODUCTION_HLV".toLowerCase())) {
            return new DataGridPanelAppProduction(name, relationInfo);
        } else if (name.equals("CHOICENORM_AV_SALARY".toLowerCase())) {
            return new DataGridPanelChoice(name, relationInfo);
        } else if (name.equals("LABOR_AND_PAYROLL_ANALYTICS".toLowerCase())) {
            return new DataGridPanelLaborAndPayrollAnalytics(name, relationInfo);
        } else if (name.equals("WORK_PLAN_EXECUTOR".toLowerCase())) {
            return new DataGridPanelWorkPlanExecutor(name, relationInfo);
        } else if (name.equals("WORK_PLAN_EXECUTOR_NEW".toLowerCase())) {
            return new DataGridPanelWorkPlanExecutorNew(name, relationInfo);
        } else if (name.equals("WP_EXECUTORS".toLowerCase())) {
            return new DataGridPanelExecutors(name, relationInfo);
        } else if (name.equals("WP_EXECUTORS_NEW".toLowerCase())) {
            return new DataGridPanelExecutorsNew(name, relationInfo);
        } else {
            return new DataGridPanel(name, relationInfo);
        }
    }

    static public void createDataGridOrTreePanel(final String tableName, final Listener listener) {
        GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            @Override
            public void onSuccess(DTable result) {
                DataBasePanel panel;
                if (tableName.toLowerCase().equals("CONTRACTS_VO_DOP".toLowerCase())) {
                    panel = new DataGridPanelContractDop(result, tableName);
                } else if (result.getParentKeyID() == null) {
                    if (tableName.equals("PLAN_NIOKR".toLowerCase())) {
                        panel = new DataGridPanelPlanNiokr(result, tableName);
                    } else if (tableName.equals("PLAN_NIOKR_IMPL".toLowerCase())) {
                        panel = new DataGridPanelPlanNiokrImpl(result, tableName);
                    } else if (tableName.equals("OCP_VO".toLowerCase())) {
                        panel = new DataGridPanelOCPMain(result, tableName);
                    } else if (tableName.equals("OFFICE_DOC_VO".toLowerCase())) {
                        panel = new DataGridPanelOfficeDoc(result, tableName);
                    } else if (tableName.equals("OFFICE_DOC_CONTR_IG".toLowerCase())) {
                        panel = new DataGridPanelOfficeDocContrIg(result, tableName);
                    } else if (tableName.equals("DEPART_POST".toLowerCase())) {
                        panel = new DataGridPanelDepartPostIg(result, tableName);
                    } else if (tableName.equals("DH_ORDER_PRIBOY_HLV".toLowerCase())) {
                        panel = new DataGridPanelDhOrderPriboy(result, tableName);
                    } else if (tableName.equals("DH_ORDER_MART_HLV".toLowerCase())) {
                        panel = new DataGridPanelDhOrderMart(result, tableName);
                    } else if (tableName.equals("OFFICE_DOC_CONTR_VO".toLowerCase())) {
                        panel = new DataGridPanelOfficeDocContrVO(result, tableName);
                    } else if (tableName.equals("PLAN_NTO_YEAR_VO".toLowerCase())) {
                        panel = new DataGridPanelPlanNTOYear(result, tableName);
                    } else if (tableName.equals("ORDER_BLANK_WORK".toLowerCase())) {
                        panel = new DataGridPanelOrderBlankWork(result, tableName);
                    } else if (tableName.equals("ORDER_BLANK_WORK2".toLowerCase())) {
                        panel = new DataGridPanelOrderBlankWork2(result, tableName);
                    } else if (tableName.equals("ORDER_BLANK_WORK3".toLowerCase())) {
                        panel = new DataGridPanelOrderBlankWork3(result, tableName);
                    } else if (tableName.equals("ORDER_BLANK_WORK4".toLowerCase())) {
                        panel = new DataGridPanelOrderBlankWork4(result, tableName);
                    } else if (tableName.equals("ORDER_BLANK_WORK5".toLowerCase())) {
                        panel = new DataGridPanelOrderBlankWork5(result, tableName);
                    } else if (tableName.equals("COMMANDER".toLowerCase())) {
                        panel = new DataGridPanelCommander(result, tableName);
                    } else if (tableName.equals("DH_FACILITIES_PRIBOY_IG".toLowerCase()) || tableName.equals("DH_FACILITIES_MART_IG".toLowerCase())) {
                        panel = new DataGridPanelDhFacilities(result, tableName);
                    } else if (tableName.equals("DH_ORDER_1_VO".toLowerCase())) {
                        panel = new DataGridPanelDhOrder1(result, tableName);
                    } else if (tableName.equals("DH_JOB_1_HLV".toLowerCase())) {
                        panel = new DataGridPanelDhJob1(result, tableName);
                    } else if (tableName.equals("MASS_MAILING_LIST_IG".toLowerCase())) {
                        panel = new DataGridPanelMassMailingListIg(result, tableName);
                    } else if (tableName.equals("MASS_MAILING_IG".toLowerCase())) {
                        panel = new DataGridPanelMassMailingIg(result, tableName);
                    } else if (tableName.equals("DEPART_EXECUTOR_FACT".toLowerCase())) {
                        panel = new DataGridPanelDepartExecutorFactIg(result, tableName);
                    } else if (tableName.equals("WORKSHOP_LOAD_VO".toLowerCase())) {
                        panel = new DataGridPanelWorkshopLoadVO(result, tableName);
                    } else if (tableName.equals("DEPART_POST_WORKER_VO".toLowerCase())) {
                        panel = new DataGridPanelDepartPostWorker(result, tableName);
                    } else if (tableName.equals("HLV_USER_DEPART".toLowerCase())) {
                        panel = new DataGridPanelUserDepart(result, tableName);
                    } else if (tableName.equals("DH_ELABORATION_OF_DD_HLV".toLowerCase())) {
                        panel = new DataGridPanelDhElaborationOfDd(result, tableName);
                    } else if (tableName.equals("DT_ELABORATION_OF_DD_HLV".toLowerCase()) || tableName.equals("APP_PROD_COMMENT_HLV".toLowerCase())) {
                        panel = new DataGridPanelDtElaborationOfDd(result, tableName);
                    } else if (tableName.equals("APP_PRODUCTION_HLV".toLowerCase())) {
                        panel = new DataGridPanelAppProduction(result, tableName);
                    } else if (tableName.equals("CHOICENORM_AV_SALARY".toLowerCase())) {
                        panel = new DataGridPanelChoice(result, tableName);
                    } else if (tableName.equals("LABOR_AND_PAYROLL_ANALYTICS".toLowerCase())) {
                        panel = new DataGridPanelLaborAndPayrollAnalytics(result, tableName);
                    } else if (tableName.equals("WP_EXECUTORS".toLowerCase())) {
                        panel = new DataGridPanelExecutors(result, tableName);
                    } else if (tableName.equals("WP_EXECUTORS_NEW".toLowerCase())) {
                        panel = new DataGridPanelExecutorsNew(result, tableName);
                    } else {
                        panel = new DataGridPanel(result, tableName);
                    }
                } else {
                    panel = new DataTreePanel(result, tableName);
                }
                DataBasePanelEvent event = new DataBasePanelEvent(this, panel);
                event.setType(DataGridEvents.DataBasePanelCreate);
                listener.handleEvent(event);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        service.getTable(tableName, callback_getTable);
    }

    static public void createDataGridCustomCnd(final String tableName, final Listener listener, final IRowColumnVal val) {
        GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            @Override
            public void onSuccess(DTable result) {
                DataBasePanel panel;
                panel = new DataGridPanel(result, tableName);

                if (tableName.equals("NORM_COST_WORK_VO")) {
                    panel.getCondition().getFilters().put(new SKeyForColumn("MAIN:COMPANY_ID"), val);
                }

                DataBasePanelEvent event = new DataBasePanelEvent(this, panel);
                event.setType(DataGridEvents.DataBasePanelCreate);
                listener.handleEvent(event);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        service.getTable(tableName, callback_getTable);
    }

    static public void createDataGridOrTreePanel(final String tableName, final SRelationInfo relationInfo, final Listener listener) {
        GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            @Override
            public void onSuccess(DTable result) {
                DataBasePanel panel;
                if (result.getParentKeyID() == null) {
                    panel = new DataGridPanel(result, tableName, relationInfo);
                } else {
                    panel = new DataTreePanel(result, tableName);
                }
                DataBasePanelEvent event = new DataBasePanelEvent(this, panel);
                event.setType(DataGridEvents.DataBasePanelCreate);
                listener.handleEvent(event);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        service.getTable(tableName, callback_getTable);
    }

    static public void createDataGridOrTreePanel(final String tableName, final Listener listener, final DCondition cnd) {
        GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            @Override
            public void onSuccess(DTable result) {
                DataBasePanel panel;
                if (result.getParentKeyID() == null) {
                    panel = new DataGridPanel(result, tableName, cnd);
                } else {
                    panel = new DataTreePanel(result, tableName);
                    if (cnd != null
                            && !result.getQueryName().equals("DEPART_STRUCTURE_OCP")) {
                        panel = new DataTreePanel(result, tableName, cnd);
                    }
                }
                DataBasePanelEvent event = new DataBasePanelEvent(this, panel);
                event.setType(DataGridEvents.DataBasePanelCreate);
                listener.handleEvent(event);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        service.getTable(tableName, callback_getTable);
    }
    
    static public void createDataGridOrTreePanel(final String tableName, final Listener listener, final List<DCondition> conditions) {
        GWTDDataGridServiceAsync service = GWT.create(GWTDDataGridService.class); //сервис отвечающий за обработку данных
        final AsyncCallback<DTable> callback_getTable = new AsyncCallback<DTable>() {

            @Override
            public void onSuccess(DTable result) {
                DataBasePanel panel;
                if (result.getParentKeyID() == null) {
                    panel = new DataGridPanel(result, tableName, conditions);
                } else {
                    panel = new DataTreePanel(result, tableName);
                    if (conditions != null
                            && !result.getQueryName().equals("DEPART_STRUCTURE_OCP")) {
                        panel = new DataTreePanel(result, tableName, conditions);
                    }
                }
                DataBasePanelEvent event = new DataBasePanelEvent(this, panel);
                event.setType(DataGridEvents.DataBasePanelCreate);
                listener.handleEvent(event);
            }

            @Override
            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }
        };
        service.getTable(tableName, callback_getTable);
    }
}
