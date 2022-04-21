package pst.arm.client.config;

import com.google.gwt.i18n.client.Constants;

/**
 *
 * @author kozhin
 */
public interface Configuration extends Constants {
    
    @Key("modules.cards.location")
    String cardsLocation();

    @Key("modules.help.usermanual.src")
    String helpUserManualSrc();

    @Key("pageRecordCount")
    String pageRecordCount();
    
    @Key("modules.screenimages.contracts.src")
    String srcScreenimagesContracts();
}
