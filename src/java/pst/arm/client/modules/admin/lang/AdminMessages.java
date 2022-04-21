package pst.arm.client.modules.admin.lang;

/**
 *
 * @author vorontsov
 */
public interface AdminMessages extends com.google.gwt.i18n.client.Messages {
    /*---PasswordDialog---*/
    @Key("admin.users.passDlgHeader")
    String passDlgHeader(String userName);

    /*---UserDialog---*/
    @Key("admin.users.userDlgEditHeader")
    String userDlgEditHeader(String userName);

    @Key("admin.users.createUserErrorMessage")
    String createUserErrorMessage(String userName);


    @Key("admin.users.userDataChangedMessage")
    String userDataChangedMessage(String userName);

    @Key("admin.users.userCreatedMessage")
    String userCreatedMessage(String userName);

    @Key("admin.users.userDeletedMessage")
    String userDeletedMessage(String userName);

}
