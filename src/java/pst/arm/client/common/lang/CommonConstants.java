package pst.arm.client.common.lang;

import com.google.gwt.i18n.client.Constants;

/**
 * Language interface for common components
 * 
 * @author Alexandr Kozhin
 * @since 0.11.0
 */
public interface CommonConstants extends Constants {

    @Key("loading")
    String loading();
    
     @Key("errorLoading")
    String errorLoading();

    @Key("saving")
    String saving();

    @Key("add")
    String add();

    @Key("summary")
    String summary();

    @Key("addNear")
    String addNear();
    
    @Key("reset")
    String reset();
    
    @Key("open")
    String open();
    
    @Key("edit")
    String edit();
    
    @Key("send")
    String send();

    @Key("copy")
    String copy();
    
    @Key("delete")
    String delete();
    
    @Key("save")
    String save();

    @Key("saveadd")
    String saveadd();
    
    @Key("saveclose")
    String saveclose();
    
    @Key("saveChanges")
    String saveChanges();
    
    @Key("cancel")
    String cancel();

    @Key("view")
    String view();
    
     @Key("exit")
    String exit();

    @Key("error")
    String error();
    
    @Key("confirm")
    String confirm();

    @Key("controls.DateEditField.dayLabel")
    String controlsDateEditFieldDayLabel();

    @Key("controls.DateEditField.monthLabel")
    String controlsDateEditFieldMonthLabel();

    @Key("controls.DateEditField.yearLabel")
    String controlsDateEditFieldYearLabel();

    @Key("controls.DatePeriodField.dateFrom")
    String controlsDatePeriodFieldDateFrom();

    @Key("controls.DatePeriodField.dateTo")
    String controlsDatePeriodFieldDateTo();
    
    @Key("date.onlyDate")
    String dateOnlyDate();


    @Key("date.dateTime")
    String dateDateTime();
    
    @Key("showfiltr")
    String btnShowFiltr();

    @Key("hidefiltr")
    String btnHideFiltr();

    @Key("attantion")
    String attantion();

    @Key("alert")
    String alert();

    @Key("notFieldsValid")
    String notFieldsValid();

    @Key("warning")
    String warning();

    @Key("info")
    String info();

    @Key("notValid")
    String notValid();

    @Key("notready")
    String notReady();

    @Key("pageRecordCount")
    String recordCount();

    @Key("minHeight")
    String filtrpanelMinHeight();

    @Key("search")
    String search();

    @Key("clean")
    String clean();

    @Key("notComboBoxValid")
    String notComboBoxValid();
    
    @Key("userId")
    String userId();

    @Key("createDate")
    String createDate();

    @Key("choice")
    String choice();
    
    @Key("errAskAdmin")
    String errAskAdmin();
    
    @Key("columnsVisibility")
    String columnsVisibility();
    
    @Key("columnsVisibilityDel")
    String columnsVisibilityDel();
    
    @Key("invalidFormatDate")
    String invalidFormatDate();
}
