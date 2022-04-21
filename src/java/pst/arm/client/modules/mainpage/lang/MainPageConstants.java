package pst.arm.client.modules.mainpage.lang;

import com.google.gwt.i18n.client.Constants;

/**
 * Language interface for common components
 * 
 * @author Artem Vorontsov
 * @since 0.15.0
 */
public interface MainPageConstants extends Constants {
    @Key("portlet.request.header")
    String portletRequestHeader();
    
    @Key("portlet.request.grid.forme")
    String portletRequestGridForMe();
    
    @Key("portlet.request.grid.forall")
    String portletRequestGridForAll();
    
    @Key("portlet.request.grid.status")
    String portletRequestGridStatus();

    
    @Key("portlet.request.grid.statusExecuting")
    String portletRequestGridStatusExecuting();

    @Key("portlet.request.grid.statusExecuted")
    String portletRequestGridStatusExecuted();

    @Key("portlet.request.grid.executedNoAnswer")
    String portletRequestGridStatusExecutedNoAnswer();
}