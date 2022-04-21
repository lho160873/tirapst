package pst.arm.client.common.ui.controls.datagrid;

import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum EDGComboBox {

    TripMode("TRIP_MODE", "MAIN:TRIP_MODE_ID", "MAIN:TRIP_MODE_NAME"),
    TranspCategory("TRANSP_CATEGORY", "MAIN:CC", "MAIN:NAME"),
    UsersLastName("USERS","MAIN:USER_ID","MAIN:LAST_NAME"),
    CountryFullName("COUNTRY","MAIN:COUNTRY_CODE","MAIN:FULL_NAME"),
    CountryShortName("COUNTRY","MAIN:COUNTRY_CODE","MAIN:SHORT_NAME"),
    CountryAlpha2("COUNTRY","MAIN:COUNTRY_CODE","MAIN:ALPHA_2"),
    CountryAlpha2Alpha2("COUNTRY","MAIN:ALPHA_2","MAIN:ALPHA_2"),
    AddressPlace("ADDRESS_PLACE", "MAIN:PLACE", "MAIN:PLACE"),
    AddressPostIndex("ADDRESS_POST_INDEX", "MAIN:POST_INDEX", "MAIN:POST_INDEX"),
    AddressStreet("ADDRESS_STREET", "MAIN:STREET", "MAIN:STREET"),
    AddressHouse("ADDRESS_HOUSE", "MAIN:HOUSE", "MAIN:HOUSE"),
    AddressOffice("ADDRESS_OFFICE", "MAIN:OFFICE", "MAIN:OFFICE"),
    AddressZoneNumber("ADDRESS_ZONE_NUMBER", "MAIN:ZONE_NUMBER", "MAIN:ZONE_NUMBER"),
    ClientCompanyName("CB_CLIENT","MAIN:CLIENT_ID","MAIN:COMPANY_NAME"),
    ClientsRepr("CB_CLIENTS_REPR","MAIN:CLIENTS_REPR_ID","MAIN:LAST_NAME"), 
    DesiredTransit("DESIRED_TRANSIT","MAIN:DESIRED_TRANSIT_ID","MAIN:D_T_NAME"),
    Incoterms("INCOTERMS","MAIN:INCOTERMS","MAIN:INCOTERMS"),
    RoutePointSt("ROUTE_POINT_ST","MAIN:ROUTE_POINT_ST_ID","MAIN:NAME"),
    RoutePointStExt("ROUTE_POINT_ST","MAIN:ROUTE_POINT_ST_ID","MAIN:FULL_NAME"),
    DangerClass("DANGER_CLASS","MAIN:DANGER_CLASS_ID","MAIN:NAME"), 
    Currency("CURRENCY","MAIN:CURRENCY_ID","MAIN:NAME"),
    DepartmentType("CB_DEPARTMENT_TYPE","MAIN:DD","MAIN:NAME"),
    DepLocation("CB_DEP_LOCATION","MAIN:LL","MAIN:NAME"),
    Functionary("CB_DEP_FUNCTIONARY","MAIN:S","MAIN:S"),
    TranspDirection("TRANSP_DIRECTION","MAIN:T","MAIN:NAME"),
    CompanyTypeShort("COMPANY_TYPE","MAIN:COMPANY_TYPE_ID","MAIN:SHORT_NAME"),
    FreightType("FREIGHT_TYPE","MAIN:FREIGHT_TYPE_ID", "MAIN:NAME"),
    InfSource("INF_SOURCE","MAIN:INF_SOURCE_ID","MAIN:INF_SOURCE"),
    CustomRoutine("CUSTOM_ROUTINE", "MAIN:C_R_NUMBER", "MAIN:C_R_NAME"),
    RoutePointStX("ROUTE_POINT_ST1","MAIN:ROUTE_POINT_ST_ID","MAIN:NAME"),
    Performer("PERFORMER", "MAIN:USER_ID", "MAIN:FIO");
    
    protected String tableName;
    private SKeyForColumn keyForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    private SKeyForColumn keyForName;//ключ к полю, которое хотим помещать в данные 

    private EDGComboBox() {
        tableName = "";
        keyForId = null;
        keyForName = null;
    }

    private EDGComboBox(String tn, String tableValueField, String tableDisplayField) {
        tableName = tn;
        keyForId = new SKeyForColumn(tableValueField);
        keyForName = new SKeyForColumn(tableDisplayField);
    }

    public String tableName() {
        return tableName;
    }

    public SKeyForColumn keyForId() {
        return keyForId;
    }

    public SKeyForColumn keyForName() {
        return keyForName;
    }
}
