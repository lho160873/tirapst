package pst.arm.client.common.lang;

import java.util.Map;

/**
 *
 * @author vorontsov
 */
public interface BaseConstants extends com.google.gwt.i18n.client.Constants {
    /*
     * ---Change log---
     */

    @Key("portlet.changeLog.caption")
    String portletChangeLogCaption();

    @Key("portlet.messagesIn.caption")
    String portletMessagesInCaption();

    @Key("portlet.messagesOut.caption")
    String portletMessagesOutCaption();

    @Key("portlet.messagesReglament.caption")
    String portletMessagesReglamentCaption();

    /*
     * ---Common---
     */
    @Key("header.confirm")
    String confirm();

    @Key("header.caption")
    String caption();

    /*
     * ---Modules---
     */
    @Key("modules.nullpage")
    String nullpage();

    /*
     * ---TopPanelToolbar---
     */
    @Key("header.toppanel.btndictionary")
    String headerBtnDictionary();

    @Key("header.toppanel.groupgoto")
    String headerGroupGoTo();

    @Key("header.toppanel.groupadministration")
    String headerGroupAdministration();

    @Key("header.toppanel.grouphelp")
    String headerGroupHelp();

    @Key("header.toppanel.btnabout")
    String headerBtnAbout();

    @Key("header.toppanel.addapplication")
    String headerAddApplication();

    @Key("header.toppanel.addrequest")
    String headerAddRequest();

    @Key("header.toppanel.calculator")
    String headerCalculator();

    @Key("header.toppanel.addclientcard")
    String headerAddClientCard();

    @Key("header.toppanel.grouptechnology")
    String headerGroupTechnology();

    @Key("header.toppanel.grouptechnology.nomenclature_matching")
    String headerBtnNomenclatureMatching();

    @Key("header.toppanel.grouptechnology.nomenclature_matching_results")
    String headerBtnNomenclatureMatchingResults();

    /*
     * ---MainNavigationPanel---
     */
    @Key("header.toppanel.groupdictionary")
    String headerGroupDictionary();

    @Key("header.toppanel.btndepartmenttype")
    String headerBtnDepartmentType();

    @Key("header.toppanel.btndeplocation")
    String headerBtnDepLocation();

    @Key("header.toppanel.btndepartment")
    String headerBtnDepartment();

    @Key("header.toppanel.btnfreighttype")
    String headerBtnFreightType();

    @Key("header.toppanel.btnunnumber")
    String headerBtnUnNumber();

    @Key("header.toppanel.grouprequestparam")
    String headerGroupRequestParam();

    @Key("header.toppanel.btnaddresses")
    String headerBtnAddresses();

    @Key("header.toppanel.btnncotd")
    String headerBtnNcotd();

    @Key("header.toppanel.btnzonehub")
    String headerBtnZoneHub();

    @Key("header.toppanel.btnterminals")
    String headerBtnTerminals();

    @Key("header.toppanel.btnzoneshubs")
    String headerBtnZonesHubs();

    @Key("header.toppanel.btnindexesZoneHubs")
    String headerBtnIndexesZoneHubs();

    @Key("header.toppanel.groupaddresses")
    String headerGroupAddressess();

    @Key("header.toppanel.grouprequest")
    String headerGroupRequest();

    @Key("header.toppanel.groupdatagridadmin")
    String headerGroupDataGridAdmin();

    @Key("header.toppanel.groupjournals")
    String headerGroupJournals();

    @Key("header.toppanel.grouptariffs")
    String headerGroupTariffs();

    @Key("header.toppanel.groupquery")
    String headerGroupQuery();

    @Key("header.toppanel.grouptest")
    String headerGroupTest();

    @Key("header.toppanel.btnnull")
    String headerBtnNull();

    @Key("header.toppanel.groupfreight")
    String headerGroupFreight();

    @Key("header.toppanel.btndangerclass")
    String headerBtnDangerClass();

    @Key("header.toppanel.btnpackingtype")
    String headerBtnPackingType();

    @Key("header.toppanel.btntempcond")
    String headerBtnTempCond();

    @Key("header.toppanel.btnpallet")
    String headerBtnPallet();

    @Key("header.toppanel.btninquiryapp")
    String headerBtnInquiryApplicationJournal();

    @Key("header.toppanel.btninquiryreq")
    String headerBtnInquiryRequestJournal();

    @Key("header.toppanel.grouptransportation")
    String headerGroupTransportation();

    @Key("header.toppanel.btntripmode")
    String headerBtnTripMode();

    @Key("header.toppanel.btntransportkind")
    String headerBtnTransportKind();

    @Key("header.toppanel.btndesiredtransit")
    String headerBtnDesiredTransit();

    @Key("header.toppanel.btntranspdirection")
    String headerBtnTranspDirection();

    @Key("header.toppanel.btntracingtype")
    String headerBtnTracingType();

    @Key("header.toppanel.btncpointtype")
    String headerBtnCPointType();

    @Key("header.toppanel.btnppointtype")
    String headerBtnPPointType();

    @Key("header.toppanel.btnawningtype")
    String headerBtnAwningType();

    @Key("header.toppanel.btnloadingunloading")
    String headerBtnLoadingUnloading();

    @Key("header.toppanel.groupcustoms")
    String headerGroupCustoms();

    @Key("header.toppanel.btncustomvar")
    String headerBtnCustomVar();

    @Key("header.toppanel.btncustomroutinegr")
    String headerBtnCustomRoutineGR();

    @Key("header.toppanel.groupservice")
    String headerGroupService();

    @Key("header.toppanel.btnpaymentform")
    String headerBtnPaymentForm();

    @Key("header.toppanel.btnbasicservice")
    String headerBtnBasicService();

    @Key("header.toppanel.btncontracttype")
    String headerBtnContractType();

    @Key("header.toppanel.btnserviceform")
    String headerBtnServiceForm();

    @Key("header.toppanel.btncontractservice")
    String headerBtnContractService();

    @Key("header.toppanel.btntnvedpart")
    String headerBtnTnvedPart();

    @Key("header.toppanel.btncustservtype")
    String headerBtnCustServType();

    @Key("header.toppanel.groupofficework")
    String headerGroupOfficeWork();

    @Key("header.toppanel.btnextraservice")
    String headerBtnExtraService();

    @Key("header.toppanel.btntranspcategory")
    String headerBtnTranspCategory();

    @Key("header.toppanel.btndossierstatus")
    String headerBtnDossierStatus();

    @Key("header.toppanel.btninquirystatus")
    String headerBtnInquiryStatus();

    @Key("header.toppanel.btnrequeststatus")
    String headerBtnRequestStatus();

    @Key("header.toppanel.btnofferstatus")
    String headerBtnOfferStatus();

    @Key("header.toppanel.btntransporationstatus")
    String headerBtnTransportationStatus();

    @Key("header.toppanel.btndossiertype")
    String headerBtnDossierType();

    @Key("header.toppanel.btnoffertype")
    String headerBtnOfferType();

    @Key("header.toppanel.btntestrequeststatus")
    String headerBtnTestRequestStatus();

    @Key("header.toppanel.btnaddresseetype")
    String headerBtnAddresseeType();

    @Key("header.toppanel.btnsubcontrrole")
    String headerBtnSubContrRole();

    @Key("header.toppanel.btnaddresstype")
    String headerBtnAddressType();

    @Key("header.toppanel.btncompanytype")
    String headerBtnCompanyType();

    @Key("header.toppanel.btncountry")
    String headerBtnCountry();

    @Key("header.toppanel.groupcards")
    String headerGroupCards();

    @Key("header.toppanel.btnclientcard")
    String headerBtnClientCard();

    @Key("header.toppanel.btnsubclientcard")
    String headerBtnSubClientCard();

    @Key("header.toppanel.btncontract")
    String headerBtnContract();

    @Key("header.toppanel.groupwork")
    String headerGroupWork();

    @Key("header.toppanel.btnworkopen")
    String headerBtnWorkOpen();

    @Key("header.toppanel.btnworkmy")
    String headerBtnWorkMy();

    @Key("header.toppanel.btnworkdepartament")
    String headerBtnWorkDepartament();

    @Key("header.toppanel.grouptariffscompany")
    String headerGroupTarifsCompany();

    @Key("header.toppanel.btntarifstransport")
    String headerBtnTarifsTransport();

    @Key("header.toppanel.btntarifsto")
    String headerBtnTarifsTo();

    @Key("header.toppanel.btntariftake")
    String headerBtnTarifTake();

    @Key("header.toppanel.btntariffdropoff")
    String headerBtnTariffDropOff();

    @Key("header.toppanel.btntarifftake")
    String headerBtnTariffTake();

    /*---Other form kav (NOT USED?)---*/
    @Key("header.toppanel.youenterasdefault")
    String headerYouEnterAsDefault();

    @Key("header.toppanel.btnmainpage")
    String headerBtnMainPage();

    @Key("header.toppanel.groupsearch")
    String headerGroupSearch();

    @Key("header.toppanel.btncardsearch")
    String headerBtnCardSearch();

    @Key("header.toppanel.btncardclss")
    String headerBtnCardClss();

    @Key("header.toppanel.groupcardfile")
    String headerGroupCardFile();

    @Key("header.toppanel.btncardfilesearch")
    String headerBtnCardFileSearch();

    @Key("header.toppanel.btnfundsearch")
    String headerBtnFundSearch();

    @Key("header.toppanel.groupfundsandinvs")
    String headerGroupFundsAndInvs();

    @Key("header.toppanel.btnafview")
    String headerBtnAFView();

    @Key("header.toppanel.btnfundsview")
    String headerBtnFundsView();

    @Key("header.toppanel.btnpointersview")
    String headerBtnPointersView();

    @Key("header.toppanel.grouprequests")
    String headerGroupRequests();

    @Key("header.toppanel.btnrequestsview")
    String headerBtnRequestsView();

    @Key("header.toppanel.groupsafetyfund")
    String headerGroupSafetyFund();

    @Key("header.toppanel.btnsafetyfundview")
    String headerBtnSafetyFundSearch();

    @Key("header.toppanel.btnsafetyfundbook")
    String headerBtnSafetyFundBook();

    @Key("header.toppanel.btnsafetyfundreports")
    String headerBtnSafetyFundReports();

    @Key("header.toppanel.groupreadinghole")
    String headerGroupReadingHole();

    @Key("header.toppanel.btnadmstat")
    String headerBtnAdmStat();

    @Key("header.toppanel.btnadmroles")
    String headerBtnAdmRoles();

    @Key("header.toppanel.btnadmusers")
    String headerBtnAdmUsers();

    @Key("header.toppanel.btnrhusers")
    String headerBtnRhUsers();

    @Key("header.toppanel.btnrequests")
    String headerBtnRequests();

    @Key("header.toppanel.groupexit")
    String headerGroupExit();

    @Key("header.toppanel.btnexit")
    String headerBtnExit();

    @Key("header.toppanel.groupstat")
    String headerGroupStat();

    @Key("header.toppanel.btnstatcards")
    String headerBtnStatCards();

    @Key("header.toppanel.btnHelpAbout")
    String headerBtnHelpAbout();

    @Key("header.toppanel.btnHelpUserManual")
    String headerBtnHelpUserManual();

    @Key("header.toppanel.grouplinks")
    String headerGroupLinks();

    @Key("header.toppanel.btndatagrid")
    String headerBtnDataGrid();

    @Key("header.toppanel.btntablegrid")
    String headerBtnTableGrid();

    @Key("header.toppanel.btnexpandall")
    String headerBtnExpandAll();

    @Key("header.toppanel.btncollapseall")
    String headerBtnСollapseAll();

    @Key("header.toppanel.navigation")
    String headerNavigation();

    /*---AboutDialog---*/
    @Key("aboutdialog.title")
    String aboutDialogTitle();

    @Key("aboutdialog.lblversion")
    String aboutDialogLblVersion();

    @Key("aboutdialog.valversion")
    String aboutDialogValVersion();

    @Key("aboutdialog.lblreleasedate")
    String aboutDialogLblReleaseDate();

    @Key("aboutdialog.valreleasedate")
    String aboutDialogValReleaseDate();

    @Key("aboutdialog.lbldeveloper")
    String aboutDialogLblDeveloper();

    @Key("aboutdialog.valdeveloper")
    String aboutDialogValDeveloper();

    @Key("aboutdialog.lnkdeveloper")
    String aboutDialogLnkDeveloper();

    @Key("aboutdialog.lblsupport")
    String aboutDialogLblSupport();

    @Key("aboutdialog.valsupport")
    String aboutDialogValSupport();

    @Key("aboutdialog.lnksupport")
    String aboutDialogLnkSupport();

    /*---AppCache---*/
    @Key("appchche.ekdi")
    String appcacheEkdi();

    @Key("appchche.ekdicommon")
    String appcacheEkdiCommon();

    @Key("appchche.ekdipeoples")
    String appcacheEkdiPeoples();

    @Key("appchche.ekdiplace")
    String appcacheEkdiPlace();

    @Key("appchche.ekdilangs")
    String appcacheEkdiLangs();

    @Key("appchche.ekdispec")
    String appcacheEkdiSpec();

    @Key("appchche.sek")
    String appcacheSek();

    @Key("appchche.sekcommon")
    String appcacheSekCommon();

    @Key("appchche.sekpeoples")
    String appcacheSekPeoples();

    @Key("appchche.sekplace")
    String appcacheSekPlace();

    @Key("appchche.seklangs")
    String appcacheSekLangs();

    @Key("appchche.sekspece")
    String appcacheSekSpecE();

    @Key("appchche.sekspecf")
    String appcacheSekSpecF();

    @Key("appchche.sekspeczhz")
    String appcacheSekSpecZhZ();

    @Key("appchche.sekspecl")
    String appcacheSekSpecL();

    @Key("appchche.sekspecya")
    String appcacheSekSpecYa();

    @Key("appchche.cgia")
    String appcacheCgia();

    @Key("appchche.akfullname")
    String appcacheAkFullName();

    @Key("appchche.akshortname")
    String appcacheAkShortName();

    @Key("appchche.cgafullname")
    String appcacheCgaFullName();

    @Key("appchche.cgashortname")
    String appcacheCgaShortName();

    @Key("appchche.cgiafullname")
    String appcacheCgiaFullName();

    @Key("appchche.cgiashortname")
    String appcacheCgiaShortName();

    @Key("appchche.cgalifullname")
    String appcacheCgaliFullName();

    @Key("appchche.cgalishortname")
    String appcacheCgaliShortName();

    @Key("appchche.namecatlog")
    String appcacheNameCatalog();

    @Key("appchche.themecatlog")
    String appcacheThemeCatalog();

    @Key("appchche.geocatlog")
    String appcacheGeoCatalog();

    @Key("appchche.systemcatlog")
    String appcacheSystemCatalog();

    @Key("modules.funds.jump.default.fundNum1")
    String fundsJumpDefaultFundNum1();

    @Key("breadcrumbsDelimiter")
    String breadcrumbsDelimiter();

    /*---AppHelper---*/
    @Key("archive.shortname")
    Map archiveShortname();

    @Key("toolbar.reload")
    String toolbarReload();

    @Key("toolbar.edit")
    String toolbarEdit();

    @Key("toolbar.add")
    String toolbarAdd();

    @Key("toolbar.delete")
    String toolbarDelete();

    @Key("toolbar.report")
    String toolbarReport();

    @Key("toolTipShowFiltr")
    String toolTipShowFiltr();

    @Key("toolTipHideFiltr")
    String toolTipHideFiltr();

    @Key("msgSaveState")
    String msgSaveState();

    @Key("header.toppanel.groupais")
    String headerGroupAis();

    @Key("header.toppanel.btnais.contract")
    String headerBtnAisContractrs();
    
    @Key("header.toppanel.depworkload")
    String headerDepWorkload();

    @Key("header.toppanel.facilitiesaandworkers")
    String headerFacilitiesAndWorkers();

    @Key("header.toppanel.workers")
    String headerWorkers();

    @Key("header.toppanel.orgexecutor")
    String headerOrgExecutor();

    @Key("header.toppanel.contractstat")
    String headerContractStat();

    @Key("header.toppanel.contracttype")
    String headerContractType();

    @Key("header.toppanel.post")
    String headerPost();

    @Key("header.toppanel.departpost")
    String headerDepartPost();

    @Key("header.toppanel.depart")
    String headerDepart();

    @Key("header.toppanel.company")
    String headerCompany();

    @Key("header.toppanel.ordertype")
    String headerOrderType();

    @Key("header.toppanel.militaryrepr")
    String headerMilitaryRepr();

    @Key("header.toppanel.organization")
    String headerOrganization();

    @Key("header.toppanel.departtype")
    String departType();

    @Key("header.toppanel.redfundingsource")
    String headerRedFundingSource();

    @Key("header.toppanel.rednds")
    String headerRedNds();

    @Key("header.toppanel.redevalofgetting")
    String headerRedEvalOfGetting();

    @Key("header.toppanel.rednormavsalary")
    String headerRedNormAvSalary();
    
    @Key("header.toppanel.choicenormavsalary")
    String headerChoiceNormAvSalary();

    @Key("header.toppanel.redcontractaudit")
    String headerRedContractAudit();

    @Key("header.toppanel.reddepartpostaudit")
    String headerRedDepartPostAudit();

    @Key("header.toppanel.redleveltaskaudit")
    String headerRedLevelTaskAudit();

    @Key("header.toppanel.redorderdaudit")
    String headerRedOrderDAudit();

    @Key("header.toppanel.rednormavsalaryaudit")
    String headerRedNormAvSalaryAudit();

    @Key("header.toppanel.redorganizationaudit")
    String headerRedOrganizationAudit();

    @Key("header.toppanel.redpostworkeraudit")
    String headerRedPostWorkerAudit();

    @Key("header.toppanel.redpostworkernaudit")
    String headerRedPostWorkerNAudit();

    @Key("header.toppanel.redservcontractstageaudit")
    String headerRedServContractStageAudit();

    @Key("header.toppanel.redworkplanaudit")
    String headerRedWorkPlanAudit();

    @Key("header.toppanel.redworkeraudit")
    String headerRedWorkerAudit();

    @Key("header.toppanel.redworker1caudit")
    String headerRedWorker1cAudit();

    @Key("header.toppanel.redpost1caudit")
    String headerRedPost1cAudit();

    @Key("header.toppanel.redpostaudit")
    String headerRedPostAudit();

    @Key("header.toppanel.reddepart1caudit")
    String headerRedDepart1cAudit();

    @Key("header.toppanel.reddepartaudit")
    String headerRedDepartAudit();

    @Key("header.toppanel.reddepartexecutorfactaudit")
    String headerRedDepartExecutorFactAudit();

    @Key("header.toppanel.redocpaudit")
    String headerRedOCPAudit();

    @Key("header.toppanel.rednotifopeningaudit")
    String headerRedNotifOpeningAudit();

    @Key("header.toppanel.redofficedocaudit")
    String headerRedOfficeDocAudit();

    @Key("header.toppanel.redofficedoccontraudit")
    String headerRedOfficeDocContrAudit();

    @Key("header.toppanel.redusersloginaudit")
    String headerRedUsersLoginAudit();

    @Key("header.toppanel.redappproductionaudit")
    String headerRedAppProductionAudit();

    @Key("header.toppanel.reddhelaborationofddaudit")
    String headerRedDhElaborationOfDdAudit();

    @Key("header.toppanel.redusersaudit")
    String headerRedUsersAudit();

    @Key("header.toppanel.journals")
    String headerJournals();

    @Key("header.toppanel.contracts")
    String headerContracts();

    @Key("header.toppanel.contractsdop")
    String headerContractsDop();

    @Key("header.toppanel.notifopening")
    String headerNotifOpening();
    
    @Key("header.toppanel.notifclosingig")
    String headerNotifClosingIg();

    @Key("header.toppanel.ocp")
    String headerOCP();

    @Key("header.toppanel.ganttchart")
    String headerGanttChart();

    @Key("header.toppanel.grouptechnology.aiscontractssync")
    String headerBtnAISContractsSync();

    @Key("header.toppanel.grouptechnology.aiscontractssync.supply")
    String headerBtnAISContractsSyncSupply();

    @Key("header.toppanel.grouptechnology.aiscontractssync.supply.additional")
    String headerBtnAISContractsSyncSupplyAdditional();

    @Key("header.toppanel.grouptechnology.aiscontractssync.services")
    String headerBtnAISContractsSyncServices();

    @Key("header.toppanel.planniokr")
    String headerPlanNiokr();

    @Key("header.toppanel.planniokrimpl")
    String headerPlanNiokrImpl();

    @Key("header.toppanel.planntoyear")
    String headerPlanNtoYear();
    
    @Key("header.toppanel.orderblank")
    String headerOrderBlank();
    
    @Key("header.toppanel.orderblankwork")
    String headerOrderBlankWork();
    
    @Key("header.toppanel.orderblankwork2")
    String headerOrderBlankWork2();
    
    @Key("header.toppanel.orderblankwork3")
    String headerOrderBlankWork3();

    @Key("header.toppanel.orderblankwork4")
    String headerOrderBlankWork4();
    
    @Key("header.toppanel.orderblankwork5")
    String headerOrderBlankWork5();
     
    @Key("header.toppanel.officedoc")
    String headerOfficeDoc();

    @Key("header.toppanel.orderds")
    String headerOrderDs();

    @Key("header.toppanel.interactingsyst")
    String headerInteractingSyst();

    @Key("header.toppanel.commander")
    String headerCommander();

    @Key("header.toppanel.doctypeig")
    String headerDocTypeIg();

    @Key("header.toppanel.officedoccontrig")
    String headerOfficeDocContrIg();

    @Key("header.toppanel.post1c")
    String headerPost1c();

    @Key("header.toppanel.datafrom1c")
    String headerDataFrom1c();

    @Key("header.toppanel.worker1c")
    String headerWorker1c();

    @Key("header.toppanel.sync1c")
    String headerSync1c();

    @Key("header.toppanel.fileroot")
    String headerFileRoot();

    @Key("header.toppanel.possstep")
    String headerPossStep();
    
    @Key("header.toppanel.facilities")
    String headerFacilities();

    @Key("header.toppanel.depart1c")
    String headerDepart1c();

    @Key("header.toppanel.productionpriboy")
    String headerProductionPriboy();

    @Key("header.toppanel.productionmart")
    String headerProductionMart();

    @Key("header.toppanel.planningcatalogs")
    String headerPlanningCatalogs();
        
    @Key("header.toppanel.dhorderpriboy")
    String headerDhOrderPriboy();

    @Key("header.toppanel.dhordermart")
    String headerDhOrderMart();
    
    @Key("header.toppanel.ocprightsforrecig2")
    String headerOcpRightsForRecIg2();
    
    @Key("header.toppanel.userdepart")
    String headerUserDepart();

    @Key("header.toppanel.datagriddepartforprocess")
    String headerDepartForProcess();

    @Key("header.toppanel.docperiodtypeig")
    String headerDocPeriodTypeIg();
    
    @Key("header.toppanel.typeofdepart")
    String headerTypeOfDepart();
    
    @Key("header.toppanel.dhfacilitiespriboy")
    String headerDhFacilitiesPriboy();

    @Key("header.toppanel.dhfacilitiesmart")
    String headerDhFacilitiesMart();
    
    @Key("header.toppanel.workshoppriboy")
    String headerWorkshopPriboy();
    
    @Key("header.toppanel.workshopmart")
    String headerWorkshopMart();

    @Key("env.buildtime")
    String projectBuildTime();

    @Key("env.buildnumber")
    String projectBuildNumber();
    
    @Key("header.toppanel.workshoppriboyref")
    String headerWorkshopPriboyRef();
    
    @Key("header.toppanel.workshopmartref")
    String headerWorkshopMartRef();
    
    @Key("header.toppanel.designig")
    String headerDesignIg();
    
    @Key("header.toppanel.operationstypeig")
    String headerOperationsTypeIg();
    
    @Key("header.toppanel.departmentsig")
    String headerDepartmentsIg();
    
    @Key("header.toppanel.massmailingig")
    String headerMassMailingIg();
    
    @Key("header.toppanel.prodcalendarig")
    String headerProdCalendarIg();
    
    @Key("header.toppanel.news")
    String headerNews();
    
    @Key("header.toppanel.newstype")
    String headerNewsType();
    
    @Key("header.toppanel.headerspec")
    String headerHeaderSpec();
    
    @Key("header.toppanel.userrolesig")
    String headerUserRolesIg();
    
    @Key("header.toppanel.rolefacilitiesig")
    String headerRoleFacilitiesIg();
    
    @Key("header.toppanel.dhelaborationofdd")
    String headerDhElaborationOfDd();
    
    @Key("header.toppanel.appproduction")
    String headerAppProduction();
    
    @Key("header.toppanel.datagridlaborandpayrollanalytics")
    String headerLaborAndPayrollAnalytics();
}
