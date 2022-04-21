package pst.arm.client.modules.admin.roles.lang;

/**
 *
 * @author kozhin
 */
public interface AdminRoleConstants extends com.google.gwt.i18n.client.Constants {

    @Key("adminrole.heading")
    String heading();

    @Key("adminrole.facilitiesheading")
    String facilitiesHeading();

    @Key("adminrole.toolbar.cansel")
    String toolbarCansel();

    @Key("adminrole.toolbar.save")
    String toolbarSave();

    @Key("adminrole.toolbar.add")
    String toolbarAdd();

    @Key("adminrole.toolbar.delete")
    String toolbarDelete();

    @Key("adminrole.column.role")
    String columtRole();

    @Key("adminrole.column.description")
    String columtDescription();

    @Key("adminrole.column.facilities")
    String columtFacilities();

    @Key("adminrole.render.column.facilities.change")
    String renderColumtFacilitiesChange();

    @Key("newrole.name")
    String newRoleName();

    @Key("newrole.description")
    String newRoleDescription();

    @Key("facility.treepanel.qtitle")
    String facilityTreeQtitle();

    @Key("adminrole.wait.header")
    String waitHeader();

    @Key("adminrole.wait.message")
    String waitMessage();

    @Key("adminrole.wait.progress")
    String waitProgress();
}
