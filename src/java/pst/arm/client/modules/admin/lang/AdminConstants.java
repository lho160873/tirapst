package pst.arm.client.modules.admin.lang;

/**
 *
 * @author vorontsov
 */
public interface AdminConstants extends com.google.gwt.i18n.client.Constants {
    /*---Common---*/

    @Key("admin.confirm")
    String confirm();

    @Key("admin.warn")
    String warn();

    @Key("admin.info")
    String info();

    @Key("admin.error")
    String error();

    @Key("admin.ok")
    String ok();

    @Key("admin.cancel")
    String cancel();

    @Key("admin.create")
    String create();

    @Key("admin.edit")
    String edit();

    @Key("admin.setPassword")
    String setPassword();

    @Key("admin.delete")
    String delete();

    @Key("admin.password")
    String password();

    @Key("admin.search")
    String search();

    @Key("admin.clean")
    String clean();

    @Key("admin.passwordConfirm")
    String passwordConfirm();

    @Key("admin.timeformat")
    String timeformat();

    @Key("admin.incorrectData")
    String incorrectData();

    @Key("admin.emptyLoginWarn")
    String emptyLoginWarn();
    
     @Key("admin.emptyUserName")
    String emptyUserName();


    @Key("admin.existLoginWarn")
    String existLoginWarn();

    @Key("admin.emptyPassWarn")
    String emptyPassWarn();

    @Key("admin.emptyPassConfirmWarn")
    String emptyPassConfirmWarn();

    @Key("admin.passNotEqualWarn")
    String passNotEqualWarn();


    /*---PasswordDialog---*/
    @Key("admin.users.passwordNotChanged")
    String passwordNotChanged();

    @Key("admin.users.passwordChanged")
    String passwordChanged();


    /*---UserDialog---*/
    @Key("admin.users.newUser")
    String newUser();

    @Key("admin.users.createUser")
    String createUser();

    @Key("admin.users.saveChanges")
    String saveChanges();

    @Key("admin.users.loginField")
    String loginField();

    @Key("admin.users.additionalField")
    String additionalField();

    @Key("admin.users.disableField")
    String disableField();

    @Key("admin.users.archiveIdField")
    String archiveIdField();

    @Key("admin.users.userIdField")
    String userIdField();

    @Key("admin.users.userWorkerField")
    String userWorkerField();

    @Key("admin.users.dateField")
    String dateField();

    @Key("admin.users.fioField")
    String fioField();

    @Key("admin.users.userNameField")
    String userNameField();

    @Key("admin.users.adresField")
    String adresField();

    @Key("admin.users.descriptionField")
    String descriptionField();

    @Key("admin.users.commonDataFieldSet")
    String commonDataFieldSet();

    @Key("admin.users.privateDataFieldSet")
    String privateDataFieldSet();

    @Key("admin.users.rolesFieldSet")
    String rolseFieldSet();

    @Key("admin.users.companiesFieldSet")
    String companiesFieldSet();

    @Key("admin.users.showDisabledField")
    String showDisabledField();

    @Key("admin.users.roleColumn")
    String roleColumn();

    @Key("admin.users.roleDescriptionColumn")
    String roleDescriptionColumn();
    
    @Key("admin.users.rolesLoadMessage")
    String rolesLoadMessage();

    @Key("admin.users.noRolesSelectedWarn")
    String noRolesSelectedWarn();


    /*---UsersPanel---*/
    @Key("admin.users.titleConditionPanel")
    String titleConditionPanel();

    @Key("admin.users.archiveIdColumn")
    String archiveIdColumn();

    @Key("admin.users.userIdColumn")
    String userIdColumn();

    @Key("admin.users.userNameColumn")
    String userNameColumn();

    @Key("admin.users.userFioColumn")
    String userFioColumn();

    @Key("admin.users.dateCreatedColumn")
    String dateCreatedColumn();

    @Key("admin.users.descriptionColumn")
    String descriptionColumn();

    @Key("admin.users.enabledColumn")
    String enabledColumn();

    @Key("admin.users.loadUsersMask")
    String loadUsersMask();

    @Key("admin.users.userPanelHeader")
    String userPanelHeader();

    @Key("admin.users.deleteMyselfWarn")
    String deleteMyselfWarn();

    @Key("admin.users.deleteUserConfirmation")
    String deleteUserConfirmation();

    @Key("admin.users.errorDeleteUser")
    String errorDeleteUser();

    @Key("admin.users.deleteUserTitle")
    String deleteUserTitle();

    @Key("admin.users.companyColumn")
    String companyColumn();
}
