package pst.arm.client.common.ui.grid.lang;

/**
 *
 * @author kozhin
 */
public interface GridMessages extends com.google.gwt.i18n.client.Messages {

    @Key("funds.fundDetail.heading")
    String fundDetailHeading(String num);

    @Key("funds.error.caption")
    String errorCaption();

    @Key("funds.err.getFund")
    String errGetFund(String errText);

    @Key("funds.err.save")
    String errSave();

    @Key("funds.invGrid.heading")
    String invGridHeading(String fund);

    @Key("funds.unitGrid.heading")
    String unitGridHeading(String fund, String inv);

    @Key("funds.search.err.emptyQuery")
    String errEmptyQuery();

    @Key("funds.search.err.emptyDirection")
    String errEmptyDirection();

    @Key("funds.search.result.noFunds")
    String searchResultNoFunds();

    @Key("funds.search.result.noInventories")
    String searchResultNoInventories();

    @Key("funds.search.result.noUnits")
    String searchResultNoUnits();

    @Key("funds.search.result.noDocuments")
    String searchResultNoDocuments();

    @Key("funds.search.result.getFundFail")
    String searchResultGetFundFail(String err);

    @Key("funds.search.result.getInvFail")
    String searchResultGetInvFail(String err);

    @Key("funds.search.result.getUnitFail")
    String searchResultGetUnitFail(String err);

    @Key("funds.search.result.getDocFail")
    String searchResultGetDocFail(String err);

    @Key("funds.search.result.reportgentext")
    String searchResultReportGenText(Integer count, Integer countTotal);

    @Key("funds.search.result.reportlimitwarn")
    String searchResultReportLimitWarn(Integer count);

    @Key("search.searchInFunds")
    String searchInFunds(String id);

    @Key("search.searchInInventories")
    String searchInInventories(String id);

    @Key("search.searchInUnits")
    String searchInUnits(String id);

    @Key("search.searchInDocuments")
    String searchInDocuments(String id);
    
    @Key("inventory.createInventory2RtfText")
    String createInventory2RtfText();

    @Key("funds.search.result.deniedToFunds")
    String searchResultDeniedToFunds();

    @Key("funds.search.result.deniedToInvs")
    String searchResultDeniedToInvs();

    @Key("funds.search.result.deniedToUnits")
    String searchResultDeniedToUnits();

    @Key("funds.search.result.deniedToDocs")
    String searchResultDeniedToDocs();
}
