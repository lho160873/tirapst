package pst.arm.client.common.ui.grid.lang;

import java.util.Map;

/**
 *
 * @author kozhin
 */
public interface GridConstants extends com.google.gwt.i18n.client.Constants {
    @Key("warn")
    String warn();

    @Key("toArchivistSign")
    String toArchivistSign();

    @Key("grid.viewMode")
    String gridViewMode();
    
    @Key("grid.htmlMode")
    String gridHtmlMode();
    
    @Key("grid.fullContentMode")
    String gridFullContentMode();
    
    @Key("inventory.createInventory2RtfProgress")
    String createInventory2RtfProgress();
    
    @Key("inventory.rtfgenerror")
    String inventoryRtfGenError();
    
    @Key("funds.heading")
    String heading();

    @Key("funds.emptyText")
    String emptyText();

    @Key("funds.serverText")
    String serverText();

    @Key("funds.serverHint")
    String serverHint();

    @Key("funds.btnInfoText")
    String btnInfoText();

    @Key("funds.btnContentText")
    String btnContentText();

    @Key("funds.btnParentInfo")
    String btnParentInfo();

    @Key("funds.btnGoFund")
    String btnGoFund();

    @Key("funds.btnGoInv")
    String btnGoInv();

    @Key("funds.btnGoUnit")
    String btnGoUnit();

    @Key("funds.btnSave")
    String btnSave();
    
    @Key("funds.btnSaveSelectedResults")
    String btnSaveSelectedResults();

    @Key("funds.btnSaveAllResults")
    String btnSaveAllResults();

    @Key("funds.btnViewAllResults")
    String btnViewAllResults();
    
    @Key("funds.btnPrintInventory")
    String btnPrintInventory();

    @Key("funds.btnCloseAllTabsText")
    String btnCloseAllTabsText();

    @Key("funds.btnFundInfoText")
    String btnFundInfoText();

    @Key("funds.btnInventoryInfoText")
    String btnInventoryInfoText();

    @Key("funds.btnUnitInfoText")
    String btnUnitInfoText();

    @Key("funds.fundGrid.heading")
    String fundGridHeading();

    @Key("funds.fundGrid.columns")
    Map fundGridColumns();

    @Key("funds.fundGrid.toolbar.btnGoFund")
    String fundGridToolBarBtnGoFund();

    @Key("funds.fundGrid.toolbar.btnClear")
    String fundGridToolBarBtnClear();

    @Key("funds.fundGrid.toolbar.fundNumText")
    String fundGridToolBarFundNumText();

    @Key("funds.presence")
    String presence();

    @Key("funds.progress")
    String progress();
    
    @Key("funds.reportGeneration")
    String reportGeneration();

    @Key("funds.reportgenerror")
    String reportGenError();

    @Key("funds.detail.parents.fund")
    String detailParentsFund();

    @Key("funds.detail.parents.inv")
    String detailParentsInv();

    @Key("funds.detail.parents.unit")
    String detailParentsUnit();

    @Key("funds.detail.common.movementNote")
    String detailCommonMovementNote();

    @Key("funds.detail.nsa.annotate")
    String detailNsaAnnotate();

    @Key("funds.detail.nsa.additionalNsa")
    String detailNsaAdditionalNsa();

    @Key("funds.detail.nsa.note")
    String detailNsaNote();

    @Key("funds.fundDetail.tabPointers.heading")
    String fundDetailTabPointersHeading();

    @Key("funds.fundDetail.tabCommon.heading")
    String fundDetailTabCommonHeading();

    @Key("funds.fundDetail.tabCommon.num")
    String fundDetailTabCommonNum();

    @Key("funds.fundDetail.tabCommon.name")
    String fundDetailTabCommonName();

    @Key("funds.fundDetail.tabCommon.nameShort")
    String fundDetailTabCommonNameShort();

    @Key("funds.fundDetail.tabCommon.category")
    String fundDetailTabCommonCategory();

    @Key("funds.fundDetail.tabCommon.startYear")
    String fundDetailTabCommonStartYear();

    @Key("funds.fundDetail.tabCommon.endYear")
    String fundDetailTabCommonEndYear();

    @Key("funds.fundDetail.tabNsa.heading")
    String fundDetailTabNsaHeading();

    @Key("funds.fundDetail.tabNsa.annotate")
    String fundDetailTabNsaAnnotate();

    @Key("funds.fundDetail.tabNsa.historicalRef")
    String fundDetailTabNsaHistoricalRef();

    @Key("funds.fundDetail.tabNsa.note")
    String fundDetailTabNsaNote();

    @Key("funds.fundDetail.tabNsa.additionalNsa")
    String fundDetailTabNsaAdditionalNsa();

    @Key("funds.fundDetail.tabActs.heading")
    String fundDetailTabActsHeading();

    @Key("funds.fundDetail.tabStat.heading")
    String fundDetailTabStatHeading();

    @Key("funds.fundDetail.tabStat.catSys")
    String fundDetailTabStatCatSys();

    @Key("funds.fundDetail.tabStat.catName")
    String fundDetailTabStatCatName();

    @Key("funds.fundDetail.tabStat.catGeo")
    String fundDetailTabStatCatGeo();

    @Key("funds.fundDetail.tabStat.catSubj")
    String fundDetailTabStatCatSubj();

    @Key("funds.fundDetail.tabStat.catUnits")
    String fundDetailTabStatCatUnits();

    @Key("funds.fundDetail.tabStat.catHead")
    String fundDetailTabStatCatHead();

    @Key("funds.fundDetail.tabStat.totalHead")
    String fundDetailTabStatTotalHead();

    @Key("funds.invDetail.tabCommon.heading")
    String invDetailTabCommonHeading();

    @Key("funds.invDetail.tabCommon.num")
    String invDetailTabCommonNum();

    @Key("funds.invDetail.tabCommon.name")
    String invDetailTabCommonName();

    @Key("funds.invDetail.tabCommon.type")
    String invDetailTabCommonType();

    @Key("funds.invDetail.tabCommon.startYear")
    String invDetailTabCommonStartYear();

    @Key("funds.invDetail.tabCommon.endYear")
    String invDetailTabCommonEndYear();

    @Key("funds.invDetail.tabActs.heading")
    String invDetailTabActsHeading();

    @Key("funds.invDetail.tabStat.catHead")
    String invDetailTabStatCatHead();

    @Key("funds.invDetail.tabImages.heading")
    String invDetailTabImagesHeading();

    @Key("funds.invGrid.btnFundInfoText")
    String invGridBtnFundInfoText();

    @Key("funds.invGrid.columns")
    Map invGridColumns();

    @Key("funds.location.rubric")
    String locationRubric();

    @Key("funds.location.unitFrom")
    String locationUnitFrom();

    @Key("funds.location.unitTo")
    String locationUnitTo();

    @Key("funds.location.comment")
    String locationComment();

    @Key("funds.unitDetail.caption")
    String unitDetailCaption();

    @Key("funds.unitDetail.tabCommon.heading")
    String unitDetailTabCommonHeading();

    @Key("funds.unitDetail.tabCommon.fund")
    String unitDetailTabCommonFund();

    @Key("funds.unitDetail.tabCommon.inv")
    String unitDetailTabCommonInv();

    @Key("funds.unitDetail.tabCommon.num")
    String unitDetailTabCommonNum();

    @Key("funds.unitDetail.tabCommon.tom")
    String unitDetailTabCommonTom();

    @Key("funds.unitDetail.tabCommon.listCount")
    String unitDetailTabCommonListCount();

    @Key("funds.unitDetail.tabCommon.name")
    String unitDetailTabCommonName();

    @Key("funds.unitDetail.tabCommon.type")
    String unitDetailTabCommonType();

    @Key("funds.unitDetail.tabCommon.startYear")
    String unitDetailTabCommonStartYear();

    @Key("funds.unitDetail.tabCommon.endYear")
    String unitDetailTabCommonEndYear();

    @Key("funds.unitDetail.tabCommon.topography")
    String unitDetailTabCommonTopography();

    @Key("funds.unitDetail.tabCommon.allDate")
    String unitDetailTabCommonAllDate();

    @Key("funds.unitDetail.tabImages.heading")
    String unitDetailTabImagesHeading();

    @Key("funds.unitGrid.columns")
    Map unitGridColumns();

    @Key("funds.documentGrid.headingPart")
    String documentGridHeadingPart();

    @Key("funds.documentGrid.columns")
    Map documentGridColumns();

    @Key("funds.documentGrid.securChars")
    Map documentGridSecurChars();

    @Key("funds.docDetail.caption")
    String docDetailCaption();

    @Key("funds.docDetail.tabCommon.heading")
    String docDetailTabCommonHeading();

    @Key("funds.docDetail.tabCommon.docNum")
    String docDetailTabCommonDocNum();

    @Key("funds.docDetail.tabCommon.docDate")
    String docDetailTabCommonDocDate();

    @Key("funds.docDetail.tabCommon.name")
    String docDetailTabCommonName();

    @Key("funds.docDetail.tabCommon.pageCount")
    String docDetailTabCommonPageCount();

    @Key("funds.docDetail.tabCommon.page")
    String docDetailTabCommonPage();

    @Key("funds.docDetail.tabCommon.pageFrom")
    String docDetailTabCommonPageFrom();

    @Key("funds.docDetail.tabCommon.pageTo")
    String docDetailTabCommonPageTo();

    @Key("funds.docDetail.tabCommon.securChar")
    String docDetailTabCommonSecurChar();

    @Key("funds.docDetail.tabCommon.kind")
    String docDetailTabCommonKind();

    @Key("funds.docDetail.tabCommon.eventPlace")
    String docDetailTabCommonEventPlace();

    @Key("funds.docDetail.tabCommon.eventDate")
    String docDetailTabCommonEventDate();

    @Key("funds.docDetail.tabCommon.note")
    String docDetailTabCommonNote();

    @Key("funds.docDetail.tabCommon.isOriginal")
    String docDetailTabCommonIsOriginal();

    @Key("funds.docDetail.tabCommon.isOriginal.values")
    Map docDetailTabCommonIsOriginalValues();

    @Key("funds.docDetail.tabCommon.enclosures")
    String docDetailTabCommonEnclosures();

    @Key("funds.docDetail.tabNsa.heading")
    String docDetailTabNsaHeading();

    @Key("funds.docDetail.tabNsa.annotate")
    String docDetailTabNsaAnnotate();

    @Key("funds.docDetail.tabNsa.additionalNsa")
    String docDetailTabNsaAdditionalNsa();

    @Key("funds.search.heading")
    String searchHeading();

    @Key("funds.search.btnSearchText")
    String btnSearchText();

    @Key("funds.search.ext.heading")
    String searchExtHeading();

    @Key("funds.search.emptyText")
    String searchEmptyText();

    @Key("funds.search.ext.startYear")
    String searchExtStartYear();

    @Key("funds.search.ext.endYear")
    String searchExtEndYear();

    @Key("funds.search.ext.anyWord")
    String searchExtAnyWord();

    @Key("funds.search.ext.allWord")
    String searchExtAllWord();

    @Key("funds.search.ext.exactPhrase")
    String searchExtExactPhrase();

    @Key("funds.search.ext.beginWord")
    String searchExtBeginWord();

    @Key("funds.search.ext.type")
    String searchExtType();

    @Key("funds.search.ext.mode")
    String searchExtMode();

    @Key("funds.search.ext.timeFrame")
    String searchExtTimeFrame();

    @Key("funds.search.ext.params")
    String searchExtParams();

    @Key("funds.search.ext.condition.caption")
    String searchExtConditionCaption();

    @Key("funds.search.ext.condition.tooltip")
    String searchExtConditionTooltip();

    @Key("funds.search.ext.direction")
    String searchExtDirection();

    @Key("funds.search.ext.direction.fund")
    String searchExtDirectionFund();

    @Key("funds.search.ext.direction.inv")
    String searchExtDirectionInv();

    @Key("funds.search.ext.direction.unit")
    String searchExtDirectionUnit();

    @Key("funds.search.ext.direction.doc")
    String searchExtDirectionDoc();

    @Key("funds.search.info.caption")
    String searchInfoCaption();

    @Key("funds.search.simple.heading")
    String searchSimpleHeading();

    @Key("funds.search.simple.startYear")
    String searchSimpleStartYear();

    @Key("funds.search.simple.endYear")
    String searchSimpleEndYear();

    @Key("funds.search.simple.timeFrame")
    String searchSimpleTimeFrame();

    @Key("funds.search.result.heading")
    String searchResultHeading();

    @Key("funds.search.result.fundListHeading")
    String searchResultFundListHeading();

    @Key("funds.search.result.invListHeading")
    String searchResultInvListHeading();

    @Key("funds.search.result.unitListHeading")
    String searchResultUnitListHeading();

    @Key("funds.search.result.documentListHeading")
    String searchResultDocumentListHeading();
}
