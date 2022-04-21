package pst.arm.client;

import com.extjs.gxt.ui.client.event.Events;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.admin.roles.AdminRolesPage;
import pst.arm.client.modules.admin.stat.AdminStatPage;
import pst.arm.client.modules.admin.users.UsersPage;
import pst.arm.client.modules.aiscontracts.AisContractPage;
import pst.arm.client.modules.aiscontracts.domain.AisContractType;
import pst.arm.client.modules.aiscontracts.sync.ContractsSyncPage;
import pst.arm.client.modules.controlproducton.ControlProductionPage;
import pst.arm.client.modules.datagrid.pages.*;
import pst.arm.client.modules.datagrid.widgets.expansion.DataGridPanelWorkshopLoadVO;
import pst.arm.client.modules.ganttchart.GanttChartPage;
import pst.arm.client.modules.mainpage.MainPageNew;
import pst.arm.client.modules.mymodule.MyModule;
import pst.arm.client.modules.nullpage.NullPage;
import pst.arm.client.modules.payment.PaymentPage;
import pst.arm.client.modules.powerproduction.PowerProductionPage;
import pst.arm.client.modules.samplegraphics.BaseImagePage;
import pst.arm.client.modules.sync1c.Sync1CPage;
import pst.arm.client.modules.tablegrid.TableGridPage;
import pst.arm.client.modules.technology.nomenclature.NomenclatureMatchingPage;
import pst.arm.client.modules.technology.nomenclature.NomenclatureMatchingResultsPage;
import pst.arm.client.modules.test.widgets.TestPage;
import pst.arm.client.modules.updateplanning.UpdatePlanningMartPage;
import pst.arm.client.modules.updateplanning.UpdatePlanningPriboyPage;

public class Interactive implements EntryPoint {

    public static final String LOCAL_HOST = "http://localhost:8080/";
    public static final String REMOTE_HOST = "http://109.74.139.148:9090/tirapst";

    @Override
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable arg0) {
                // gwt-log errors on null message;
                String message = arg0.getMessage() == null ? "No Message"
                        : arg0.getMessage();
                //Log.error(message);
            }
        });

        DeferredCommand.addCommand(new Command() {

            @Override
            public void execute() {
                onModuleLoad2();
            }
        });
    }

    public void onModuleLoad2() {
        try {
            Dictionary dictionary = Dictionary.getDictionary("Vars");
            String widget = dictionary.get("currentModule");
            BasePageNew loadableWidget = createLoadableWidget(widget);
            if (loadableWidget != null) {
                loadableWidget.setWidgetId(widget);
                RootPanel.get(getLoadID(widget)).setSize("100%", "100%");
                RootPanel.get(getLoadID(widget)).add(loadableWidget);
            } else {
                throw new Exception("Vars['widget_" + widget + "] => " + widget + " null or no match.");
            }
        } catch (Exception e) {
            Window.alert("e: " + e);
        }
    }

    private BasePageNew createLoadableWidget(String widget) {
        BasePageNew loadableWidget = null;
        String host = GWT.getHostPageBaseURL();
        String hostNew = host.replace("/secure", "");
        if (widget.equals("main")) {
            //if (ConfigurationManager.getUserId().toString().equals("50")||ConfigurationManager.getUserId().toString().equals("35"))//директор (временный вариант, надо с АВ обсудить как в БД хранить информацию о первоначальном модуле для пользователя
           // {
            //    loadableWidget = new ControlProductionPage();                
           // } else {
                loadableWidget = new MainPageNew();
            //}
        } else if (widget.equals("admstat")) {
            loadableWidget = new AdminStatPage();
        } else if (widget.equals("admroles")) {
            loadableWidget = new AdminRolesPage();
        } else if (widget.equals("admusers")) {
            loadableWidget = new UsersPage();
        } else if (widget.equals("test")) {
            loadableWidget = new TestPage();
        } else if (widget.equals("mymodule")) {
            loadableWidget = new MyModule();
        } else if (widget.equals("nullpage")) {
            loadableWidget = new NullPage();
        } else if (widget.equals("aiscontracts")) {
            loadableWidget = new AisContractPage();
        } else if (widget.equals("powerproduction")) {
            loadableWidget = new PowerProductionPage();
        } else if (widget.equals("controlproduction")) {
            loadableWidget = new ControlProductionPage();
        } else if (widget.equals("datagridcontractstat")) {
            loadableWidget = new DataGridContractStat();
        } else if (widget.equals("datagridordertype")) {
            loadableWidget = new DataGridOrderType();
        } else if (widget.equals("datagridmilitaryrepr")) {
            loadableWidget = new DataGridMilitaryRepr();
        } else if (widget.equals("datagridorganization")) {
            loadableWidget = new DataGridOrganization();
        } else if (widget.equals("datagridredfundingsource")) {
            loadableWidget = new DataGridRedFundingSource();
        } else if (widget.equals("datagridrednds")) {
            loadableWidget = new DataGridRedNds();
        } else if (widget.equals("datagridredevalofgetting")) {
            loadableWidget = new DataGridRedEvalOfGetting();
        } else if (widget.equals("datagridrednormavsalary")) {
            loadableWidget = new DataGridRedNormAvSalary();
        } else if (widget.equals("datagridchoicenormavsalary")) {
            loadableWidget = new DataGridChoiceNormAvSalary();
        } else if (widget.equals("datagridpost")) {
            loadableWidget = new DataGridPost();
        } else if (widget.equals("datagriddepartpost")) {
            loadableWidget = new DataGridDepartPost();
        } else if (widget.equals("datagridrednormavsalaryaudit")) {
            loadableWidget = new DataGridRedNormAvSalaryAudit();
        } else if (widget.equals("datagridreddepartpostaudit")) {
            loadableWidget = new DataGridRedDepartPostAudit();
        } else if (widget.equals("datagridredpostworkeraudit")) {
            loadableWidget = new DataGridRedPostWorkerAudit();
        } else if (widget.equals("datagridredpostworkernaudit")) {
            loadableWidget = new DataGridRedPostWorkerNAudit();
        } else if (widget.equals("datagridredservcontractstageaudit")) {
            loadableWidget = new DataGridRedServContractStageAudit();
        } else if (widget.equals("datagridredworkplanaudit")) {
            loadableWidget = new DataGridRedWorkPlanAudit();
        } else if (widget.equals("datagridredworkeraudit")) {
            loadableWidget = new DataGridRedWorkerAudit();
        } else if (widget.equals("datagridredworker1caudit")) {
            loadableWidget = new DataGridRedWorker1cAudit();
        } else if (widget.equals("datagridredpost1caudit")) {
            loadableWidget = new DataGridRedPost1cAudit();
        } else if (widget.equals("datagridredpostaudit")) {
            loadableWidget = new DataGridRedPostAudit();
        } else if (widget.equals("datagridreddepart1caudit")) {
            loadableWidget = new DataGridRedDepart1cAudit();
        } else if (widget.equals("datagridreddepartexecutorfactaudit")) {
            loadableWidget = new DataGridRedDepartExecutorFactAudit();
        } else if (widget.equals("datagridreddepartaudit")) {
            loadableWidget = new DataGridRedDepartAudit();
        } else if (widget.equals("datagridredcontractaudit")) {
            loadableWidget = new DataGridRedContractAudit();
        } else if (widget.equals("datagridredleveltaskaudit")) {
            loadableWidget = new DataGridRedLevelTaskAudit();
        } else if (widget.equals("datagridredorderdaudit")) {
            loadableWidget = new DataGridRedOrderDAudit();
        } else if (widget.equals("datagridredorganizationaudit")) {
            loadableWidget = new DataGridRedOrganizationAudit();
        } else if (widget.equals("datagridredocpaudit")) {
            loadableWidget = new DataGridRedOCPAudit();
        } else if (widget.equals("datagridrednotifopeningaudit")) {
            loadableWidget = new DataGridRedNotifOpeningAudit();
        } else if (widget.equals("datagridredofficedocaudit")) {
            loadableWidget = new DataGridRedOfficeDocAudit();
        } else if (widget.equals("datagridredofficedoccontraudit")) {
            loadableWidget = new DataGridRedOfficeDocContrAudit();
        } else if (widget.equals("datagridredusersloginaudit")) {
            loadableWidget = new DataGridRedUsersLoginAudit();
        } else if (widget.equals("datagridredappproductionaudit")) {
            loadableWidget = new DataGridRedAppProductionAudit();
        } else if (widget.equals("datagridreddhelaborationofddaudit")) {
            loadableWidget = new DataGridRedDhElaborationOfDdAudit();
        } else if (widget.equals("datagridredusersaudit")) {
            loadableWidget = new DataGridRedUsersAudit();
        } else if (widget.equals("datagriddepart")) {
            loadableWidget = new DataGridDepart();
        } else if (widget.equals("datagridcompany")) {
            loadableWidget = new DataGridCompany();
        } else if (widget.equals("datagridcontracttype")) {
            loadableWidget = new DataGridContractType();
        } else if (widget.equals("datagridworkers")) {
            loadableWidget = new DataGridWorkers();
        } else if (widget.equals("datagridcontracts")) {
            loadableWidget = new DataGridContracts();
        } else if (widget.equals("datagridcontractsdop")) {
            loadableWidget = new DataGridContractsDop();
        } else if (widget.equals("datagriddepworkload")) {
            loadableWidget = new DataGridDepWorkload();
        } else if (widget.equals("datagriddeparttype")) {
            loadableWidget = new DataGridDepartType();
        } else if (widget.equals("datagriddhorder1")) {
            loadableWidget = new DataGridDhOrder1();
        }else if (widget.equals("workcontent")) {
            loadableWidget = new BaseImagePage(hostNew + "images_doc/WorkContent.gif", "Трудоемкость");
            //loadableWidget = new BaseImagePage(AppHelper.getInstance().baseUrl() + "/images_doc/WorkContent.gif", "Трудоемкость");
        } else if (widget.equals("generalgraphic")) {
            loadableWidget = new BaseImagePage(hostNew + "/images_doc/GeneralGraph.gif", "Генеральный график");
            //loadableWidget = new BaseImagePage(AppHelper.getInstance().baseUrl() + "/images_doc/GeneralGraph.gif", "Генеральный график");
        } else if (widget.equals("controlandstate")) {
            loadableWidget = new BaseImagePage(hostNew + "/images_doc/ControlAndState.gif", "Контроль и состояние");
            //loadableWidget = new BaseImagePage(AppHelper.getInstance().baseUrl() + "/images_doc/ControlAndState.gif", "Контроль и состояние");
        } else if (widget.equals("financplangraphic")) {
            loadableWidget = new BaseImagePage(hostNew + "/images_doc/FinancialPlan.gif", "Диаграмма финансового плана");
            //loadableWidget = new BaseImagePage(AppHelper.getInstance().baseUrl() + "/images_doc/FinancialPlan.gif", "Диаграмма финансового плана");
        } else if (widget.equals("payment")) {
            loadableWidget = new PaymentPage();
        } else if (widget.equals("datagridnotifopening")) {
            loadableWidget = new DataGridNotifOpening();
        } else if (widget.equals("ganttchart")) {
            loadableWidget = new GanttChartPage();
        } else if (widget.equals("datagridocp")) {
            loadableWidget = new DataGridOCP();
        } else if (widget.equals("datagridplanniokr")) {
            loadableWidget = new DataGridPlanNiokr();
        } else if (widget.equals("datagridplanniokrimpl")) {
            loadableWidget = new DataGridPlanNiokrImpl();
        } else if (widget.equals("nomenclature")) {
            loadableWidget = new NomenclatureMatchingPage();
        } else if (widget.equals("nomenclatureresults")) {
            loadableWidget = new NomenclatureMatchingResultsPage();
        } else if (widget.equals("aiscontractssyncservices")) {
            loadableWidget = new ContractsSyncPage(AisContractType.Service);
        } else if (widget.equals("aiscontractssyncsupply")) {
            loadableWidget = new ContractsSyncPage(AisContractType.Supply);
        } else if (widget.equals("aissyncsupplyadditional")) {
            loadableWidget = new ContractsSyncPage(AisContractType.SupplyAdditional);
        } else if (widget.equals("datagridorderds")) {
            loadableWidget = new DataGridOrderDs();
        } else if (widget.equals("datagridinteractingsyst")) {
            loadableWidget = new DataGridInteractingSyst();
        } else if (widget.equals("datagridpossstep")) {
            loadableWidget = new DataGridPossStep();
        } else if (widget.equals("datagridfileroot")) {
            loadableWidget = new DataGridFileRoot();
        } else if (widget.equals("datagridfacilities")) {
            loadableWidget = new DataGridFacilities();
        } else if (widget.equals("tablegrid")) {
            loadableWidget = new TableGridPage();
        } else if (widget.equals("datagridcommander")) {
            loadableWidget = new DataGridCommander();
        } else if (widget.equals("datagridofficedoc")) {
            loadableWidget = new DataGridOfficeDoc();
        } else if (widget.equals("datagriddhorderpriboy")) {
            loadableWidget = new DataGridDhOrderPriboy();
        } else if (widget.equals("datagriddhordermart")) {
            loadableWidget = new DataGridDhOrderMart();
        } else if (widget.equals("datagriddoctypeig")) {
            loadableWidget = new DataGridDocTypeIg();
        } else if (widget.equals("datagridofficedoccontrig")) {
            loadableWidget = new DataGridOfficeDocContrIg();
        } else if (widget.equals("datagridpost1c")) {
            loadableWidget = new DataGridPost1c();
        } else if (widget.equals("datagridpost1c")) {
            loadableWidget = new DataGridPost1c();
        } else if (widget.equals("datagridworker1c")) {
            loadableWidget = new DataGridWorker1c();
        } else if (widget.equals("sync1c")) {
            loadableWidget = new Sync1CPage();
        } else if (widget.equals("datagriddepart1c")) {
            loadableWidget = new DataGridDepart1c();
        } else if (widget.equals("datagridocprightsforrecig2")) {
            loadableWidget = new DataGridOcpRightsForRecIg2();
        } else if (widget.equals("datagriddocperiodtypeig")) {
            loadableWidget = new DataGridDocPeriodTypeIg();
        } else if (widget.equals("datagridtypeofdepart")) {
            loadableWidget = new DataGridTypeOfDepart();
        } else if (widget.equals("datagriddhfacilitiespriboy")) {
            loadableWidget = new DataGridDhFacilitiesPriboy();
        } else if (widget.equals("datagriddhfacilitiesmart")) {
            loadableWidget = new DataGridDhFacilitiesMart();
        } else if (widget.equals("datagridworkshoppriboy")) {
            loadableWidget = new DataGridWorkshopPriboy();
        } else if (widget.equals("datagridworkshopmart")) {
            loadableWidget = new DataGridWorkshopMart();
        } else if (widget.equals("datagridplanntoyear")) {
            loadableWidget = new DataGridPlanNtoYear();
        } else if (widget.equals("datagridorderblankwork")) {
            loadableWidget = new DataGridOrderBlankWork();
        } else if (widget.equals("datagridorderblankwork2")) {
            loadableWidget = new DataGridOrderBlankWork2();
        } else if (widget.equals("datagridorderblankwork3")) {
            loadableWidget = new DataGridOrderBlankWork3();
        } else if (widget.equals("datagridorderblankwork4")) {
            loadableWidget = new DataGridOrderBlankWork4();
        } else if (widget.equals("datagridorderblankwork5")) {
            loadableWidget = new DataGridOrderBlankWork5();
        } else if (widget.equals("datagridworkshopmartref")) {
            loadableWidget = new DataGridWorkshopMartRef();
        } else if (widget.equals("datagridworkshoppriboyref")) {
            loadableWidget = new DataGridWorkshopPriboyRef();
        } else if (widget.equals("updateplanningpriboy")) {
            loadableWidget = new UpdatePlanningPriboyPage();
        } else if (widget.equals("updateplanningmart")) {
            loadableWidget = new UpdatePlanningMartPage();
        } else if (widget.equals("datagriddesignig")) {
            loadableWidget = new DataGridDesignIg();
        } else if (widget.equals("datagridoperationstypeig")) {
            loadableWidget = new DataGridOperationsTypeIg();
        } else if (widget.equals("datagriddepartmentsig")) {
            loadableWidget = new DataGridDepartmentsIg();
        } else if (widget.equals("datagridmassmailingig")) {
            loadableWidget = new DataGridMassMailingIg();
        } else if (widget.equals("datagridprodcalendarig")) {
            loadableWidget = new DataGridProdCalendarIg();
        } else if (widget.equals("datagriduserrolesig")) {
            loadableWidget = new DataGridUserRolesIg();
        } else if (widget.equals("datagridrolefacilitiesig")) {
            loadableWidget = new DataGridRoleFacilitiesIg();
        } else if (widget.equals("datagridworkshopload")) {
            loadableWidget = new DataGridWorkshopLoad();
        } else if (widget.equals("datagriddhelaborationofdd")) {
            loadableWidget = new DataGridDhElaborationOfDd();
        } else if (widget.equals("datagridappproduction")) {
            loadableWidget = new DataGridAppProduction();
        } else if (widget.equals("datagridstorepartrestpriboy")) {
            loadableWidget = new DataGridStorePartRestPriboy();
        } else if (widget.equals("datagridstorepartrestmart")) {
            loadableWidget = new DataGridStorePartRestMart();
        }
        else if (widget.equals("datagriddepartforprocess")) {
            loadableWidget = new DataGridDepartForProcess();
        } else if (widget.equals("datagriduserdepart")) {
            loadableWidget = new DataGridUserDepart();
        } else if (widget.equals("datagridnews")) {
            loadableWidget = new DataGridNews();
        }
        else if (widget.equals("datagridnewstype")) {
            loadableWidget = new DataGridNewsType();
        } 
        else if (widget.equals("datagridnotifclosingig")) {
            loadableWidget = new DataGridNotifClosingIg();
        }
        else if (widget.equals("datagridheaderspec")) {
            loadableWidget = new DataGridHeaderSpec();
        }
        else if (widget.equals("datagridlaborandpayrollanalytics")) {
            loadableWidget = new DataGridLaborAndPayrollAnalytics();
        }
        return loadableWidget;
    }

    public static String getRelativeURL(String url) {
        String realModuleBase;

        if (GWT.isScript()) {
            String moduleBase = GWT.getModuleBaseURL();
            realModuleBase = REMOTE_HOST;

            if (moduleBase.indexOf("localhost") != -1) {
                realModuleBase = LOCAL_HOST;
            }

        } else {
            realModuleBase = LOCAL_HOST;
        }

        return realModuleBase + url;
    }

    /**
     * Временное решение
     *
     * @param url
     * @return
     */
    public static String getRelativeURLForImageForReport(String url) {
        final String LOCAL_HOST = "http://localhost:8084/tirapst";
        final String REMOTE_HOST = "http://mm.elt-poisk.com/tirapst";

        String realModuleBase;
        if (GWT.isScript()) {
            String moduleBase = GWT.getModuleBaseURL();
            realModuleBase = REMOTE_HOST;
            if (moduleBase.indexOf("localhost") != -1) {
                realModuleBase = LOCAL_HOST;
            }
        } else {
            realModuleBase = LOCAL_HOST;
        }

        realModuleBase = REMOTE_HOST;

        return realModuleBase + url;
    }

    private static String getLoadID(String id) {
        return "gwt-slot-" + id;
    }
}
