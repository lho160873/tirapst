/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pst.arm.client.common.lang;

/**
 *
 * @author vorontsov
 */
public interface BaseMessages extends com.google.gwt.i18n.client.Messages {
    @Key("header.toppanel.areyousureexit")
    String areYouSureToExit();

    @Key("header.toppanel.youenteras")
    String youEnterAs();

    @Key("header.toppanel.mainCaption")
    String mainCaption(String version);
    
    @Key("err.report.generation")
    String errReportGeneration();
    
    
    @Key("btn.info.in")
    String btnMsgIn();

    @Key("btn.info.reglament")
    String btnMsgReglament();

    @Key("btn.info.out")
    String btnMsgOut();

    @Key("msg.info.out")
    String msgInfoOut();

    @Key("msg.info.in")
    String msgInfoIn();

    @Key("msg.info.type")
    String msgType();

    @Key("msg.info.from")
    String msgFrom();
    
}
