package pst.arm.client.modules.cabinet.lang;

/**
 *
 * @author Ratmir Slepenkov
 * @since 0.15.2
 */
public interface CabinetMessages extends com.google.gwt.i18n.client.Messages {
    @Key("cabinet.error.cant.save.user")
    String errCantSaveUser();
    
    @Key("cabinet.error.passwords.are.not.equals")
    String errPasswordsAreNotEquals();

    @Key("cabinet.error.cant.save.password")
    public String errCantSavePass();
}
