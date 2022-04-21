package pst.arm.client.common.ui.controls.datagrid;

import pst.arm.client.modules.datagrid.domain.SKeyForColumn;

/**
 *
 * @author Михаил
 */
public enum EDGComboBox2DisplayFields
{
    CountryAlpha2("COUNTRY","MAIN:COUNTRY_CODE","MAIN:ALPHA_2","MAIN:SHORT_NAME"),
    CountryAlpha2Alpha2("COUNTRY","MAIN:ALPHA_2","MAIN:ALPHA_2","MAIN:SHORT_NAME");
    
    protected String tableName;
    private SKeyForColumn keyForId; //ключ к полю, которое хотим отображать из queryNameForRelation в выпадающем списке
    private SKeyForColumn keyForName;//ключ к полю, которое хотим помещать в данные 
    private SKeyForColumn keyForName2;// Второе отображаемое поле

    private EDGComboBox2DisplayFields()
    {
        tableName = "";
        keyForId = null;
        keyForName = null;
        keyForName2 = null;
    }

    private EDGComboBox2DisplayFields(String tn, String tableValueField,
            String tableDisplayField, String tableDisplayField2)
    {
        tableName = tn;
        keyForId = new SKeyForColumn(tableValueField);
        keyForName = new SKeyForColumn(tableDisplayField);
        keyForName2 = new SKeyForColumn(tableDisplayField2);
    }

    public String tableName()
    {
        return tableName;
    }

    public SKeyForColumn keyForId()
    {
        return keyForId;
    }

    public SKeyForColumn keyForName()
    {
        return keyForName;
    }
    
    public SKeyForColumn keyForName2()
    {
        return keyForName2;
    }
}