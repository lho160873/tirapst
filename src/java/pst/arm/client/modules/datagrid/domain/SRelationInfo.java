package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class SRelationInfo implements Serializable {
    private String queryName; ///<имя связанной таблицы
    private String caption;   ///< заголовок связанной таблицы (русское)
    private HashMap< SKeyForColumn, SKeyForColumn> relations;  //отношение полей (ключ по полю основной таблицы)
    private HashMap<SKeyForColumn, SKeyForColumn> passedFieds; // передача значений полей для инициации ими полей подчиненной таблицы при добавлении
    public SRelationInfo()
    {
     queryName = "";
     relations = new HashMap< SKeyForColumn, SKeyForColumn>();
    }

    public HashMap<SKeyForColumn, SKeyForColumn> getPassedFieds() {
        return passedFieds;
    }
    public void setPassedFieds(HashMap<SKeyForColumn, SKeyForColumn> passedFieds) {
        this.passedFieds = passedFieds;
    }
    public void setCaption( String c )
    {
        caption = c;
    }
    public String getCaption( )
    {
        return caption;
    }
    public void setQueryName( String name )
    {
        queryName = name;
    }
    public String getQueryName( )
    {
        return queryName;
    }
    public void setRelations( HashMap< SKeyForColumn, SKeyForColumn> r )
    {
        relations = r;
    }
    public HashMap< SKeyForColumn, SKeyForColumn> getRelations( )
    {
        return relations;
    } 
}
