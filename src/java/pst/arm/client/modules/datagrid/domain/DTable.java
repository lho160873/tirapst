package pst.arm.client.modules.datagrid.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pst.arm.client.modules.datagrid.domain.expansion.STwoKeys;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DTable implements Serializable {
    private ArrayList<IColumnBuilder> columnBuilders; //список полей в таблице
    private ArrayList<STwoKeys> dateBegEndPairs; // Пары полей "Дата начала - Дата конца"
    private String caption;   ///< название(заголовок) таблицы (русское)
    private String sequence;  ///< название последовательности для получения значения автоинкрементного поля
    private String queryName; ///< должно быть уникальным именем для "запроса"
    private Boolean isFiltrShow = Boolean.TRUE; ///< будет ли отображаться панель фильтрации(поиска)
    private int filtrHeight = 120; // Для индивидуальной настройки высоты панели фильтра
    private Boolean isFilterShowAtStart = Boolean.FALSE; // Показывать ли панель поиска при открытии таблицы
    private Boolean isDistinct = false;
    private String strBeforSelect=null;  //например DISTINCT
    private String strOrderByStart=null;  //начальные условия соритровки (по возрастанию)
    private String strWhere=null;  //дополнительные условия поиска
    private Integer countRowsInFiltrColumn=2;///<количество строк в колонках на панели фильтров
    private Boolean isMulti=Boolean.FALSE;  ///<количество строк выбора в таблицеz
    private Boolean isShowReport=Boolean.TRUE;  ///<показывать ли кнопку с формированием отчета
    private Boolean isSubShow=Boolean.FALSE; ///< будет ли подчиненные данные отображаться сразу
    private float subSize = 0.5f; /// высота панели с подчиненными данными (от 0 до 1)

    private Boolean isViewOnly = Boolean.FALSE; /// Таблица в режиме только для чтения

    private Integer labelWidth = 150;///<ширина надписи полей на формер просмотра редактирования
    private Integer labelFiltrWidth = 100;///<ширина надписи полей на панеле фильтра
    private SKeyForColumn pk = null; // Ключ столбца основого ключа таблицы
    private SKeyForColumn keyID = null;
    private SKeyForColumn parentKeyID = null;
    private SKeyForColumn treeColumnID = null;
    private SKeyForColumn parentNameID = null;
    private Integer filterColumnWidth = 300;
    private String reportTemplate = null;
    private HashMap<String, String> reportParams = null;
    private String reportExportFormat = null;
    private boolean isGroupByAll = false;   
    private String duplicateKeyError;  ///< Сообщения об ошибках
    public final static String MAIN_TABLE = "MAIN";    
    private HashMap< String, String > tables, addTables;///< список таблиц (ключ по алиасу таблицы в запросе), при простом запросе может содержать тольк одну запись с ключом "MAIN"
    private ArrayList<DTableJoin> tableJoins; ///<список связей таблиц
    private ArrayList<SKeyForColumn> postFields; ///<список связей таблиц
    private ArrayList<SRelationInfo> subTables; //список подчиненных таблиц
    private ArrayList<SKeyForColumn> functionParams; //список входных в функцию параметров
    private ArrayList<SKeyForColumn> functionParamsFromSearch; //список входных в функцию параметров
    private String helpManual = null; ///<путь к файлу с документацией
    private String dataSourceName = "dataSourceArm";  ///<имя бина, который описывает настройки подключения к БД (см. в файле applicationContext-jdbc.xml)
    private Integer recordCount = 50; ///<кол-во записей на одной странице таблицы
    protected String denseRNUM = null; // если isDistinct, то указать уникальное поле для подсчета номеров строк

    public String getDenseRNUM() {
        return denseRNUM;
    }

    public void setDenseRNUM(String denseRNUM) {
        if (getIsDistinct())
            this.denseRNUM = denseRNUM;
    }

    public Boolean getIsViewOnly() {
        return isViewOnly;
    }

    public void setIsViewOnly(Boolean viewOnly) {
        isViewOnly = viewOnly;
    }

    public ArrayList<SKeyForColumn> getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(ArrayList<SKeyForColumn> functionParams) {
        this.functionParams = functionParams;
    }
    
     public ArrayList<SKeyForColumn> getFunctionParamsFromSearch() {
        return functionParamsFromSearch;
    }

    public void setFunctionParamsFromSearch(ArrayList<SKeyForColumn> functionParams) {
        this.functionParamsFromSearch = functionParams;
    }

    public Boolean getIsDistinct() {
        return isDistinct;
    }

    public void setIsDistinct(Boolean isDistinct) {
        this.isDistinct = isDistinct;
    }

    public void setDataSourceName(String name) {
        dataSourceName = name;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public boolean isGroupByAll() {
        return isGroupByAll;
    }

    public void setGroupByAll(boolean isGroupByAll) {
        this.isGroupByAll = isGroupByAll;
    }

    public String getReportExportFormat() {
        return reportExportFormat;
    }

    public void setReportExportFormat(String reportExportFormat) {
        this.reportExportFormat = reportExportFormat;
    }

    public HashMap<String, String> getReportParams() {
        return reportParams;
    }

    public void setReportParams(HashMap<String, String> reportParams) {
        this.reportParams = reportParams;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }
    
     public String getHelpManual() {
        return helpManual;
    }

    public void setHelpManual(String helpManual) {
        this.helpManual = helpManual;
    }

    public Boolean getIsFilterShowAtStart() {
        return isFilterShowAtStart;
    }

    public void setIsFilterShowAtStart(Boolean isFilterShowAtStart) {
        this.isFilterShowAtStart = isFilterShowAtStart;
    }
       
    public SKeyForColumn getParentNameID() {
        return parentNameID;
    }

    public void setParentNameID(SKeyForColumn parentNameID) {
        this.parentNameID = parentNameID;
    }

    public Integer getFilterColumnWidth() {
        return filterColumnWidth;
    }

    public void setFilterColumnWidth(Integer filterColumnWidth) {
        this.filterColumnWidth = filterColumnWidth;
    }

    public SKeyForColumn getPk() {
        return pk;
    }

    public void setPk(SKeyForColumn pk) {
        this.pk = pk;
    }

    public ArrayList<STwoKeys> getDateBegEndPairs() {
        return dateBegEndPairs;
    }

    public void setDateBegEndPairs(ArrayList<STwoKeys> dateBegEndPairs) {
        this.dateBegEndPairs = dateBegEndPairs;
    }

    public SKeyForColumn getTreeColumnID() {
        return treeColumnID;
    }

    public void setTreeColumnID(SKeyForColumn treeColumnID) {
        this.treeColumnID = treeColumnID;
    }

    public SKeyForColumn getKeyID() {
        return keyID;
    }

    public void setKeyID(SKeyForColumn keyID) {
        this.keyID = keyID;
    }

    public SKeyForColumn getParentKeyID() {
        return parentKeyID;
    }

    public void setParentKeyID(SKeyForColumn parentKeyID) {
        this.parentKeyID = parentKeyID;
    }

    public ArrayList<SKeyForColumn> getPostFields() {
        return postFields;
    }

    public void setPostFields(ArrayList<SKeyForColumn> postFields) {
        this.postFields = postFields;
    }
        
    public DTable() {
        columnBuilders = new ArrayList<IColumnBuilder>();
        caption = "";
        sequence = "";
        queryName = "";
        tables = new HashMap< String, String>();
        addTables = new HashMap<String, String>();
        tableJoins = new ArrayList<DTableJoin>();
        subTables = new ArrayList<SRelationInfo>();
        postFields = new ArrayList<SKeyForColumn>();
    }

    public HashMap<String, String> getAddTables() {
        return addTables;
    }

    public void setAddTables(HashMap<String, String> addTables) {
        this.addTables = addTables;
    }

    public void updateBuilder(Integer ind) {
        columnBuilders.get(ind).setIsInit(Boolean.FALSE);
    }

    public String getDuplicateKeyError() {
        return duplicateKeyError;
    }

    public void setDuplicateKeyError(String duplicateKeyError) {
        this.duplicateKeyError = duplicateKeyError;
    }
    
    public void setIsFiltrShow( Boolean isFiltrShow )
    {
        this.isFiltrShow = isFiltrShow;
    }
    public Boolean getIsFiltrShow()
    {
        return isFiltrShow;
    }
    
     public void setIsSubShow(Boolean isSubShow) {
        this.isSubShow = isSubShow;
    }

    public Boolean getIsSubShow() {
        return isSubShow;
    }
    
    public float getSubSize() {
        return subSize;
    }

    public void setSubSize(float subSize) {
        this.subSize = subSize;
    }
    
    public void setIsShowReport(Boolean isShowReport) {
        this.isShowReport = isShowReport;
    }

    public Boolean getIsShowReport() {
        return isShowReport;
    }
    public void setIsMulti( Boolean isMulti )
    {
        this.isMulti = isMulti;
    }
    public Boolean getIsMulti()
    {
        return isMulti;
    }
    //инициализируем все построители

    public void initBuilders(GWTDDataGridService service) {
        for (IColumnBuilder builder : columnBuilders) {
            if (pk == null) {
                for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                    SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                    if (builder.getColumn(key).getIsKey()) {
                        pk = key;
                        break;
                    }
                }
            }
            builder.initData(service);
        }
    }

    public void setStrOrderByStart(String strOrderByStart) {
        this.strOrderByStart = strOrderByStart;
    }

    public String getStrOrderByStart() {
        return strOrderByStart;
    }

    public void setStrBeforSelect(String strBeforSelect) {
        this.strBeforSelect = strBeforSelect;
    }

    public String getStrBeforSelect() {
        return strBeforSelect;
    }

    public void setStrWhere(String strWhere) {
        this.strWhere = strWhere;
    }

    public String getStrWhere() {
        return strWhere;
    }
    /*
     * возвращает имя главной таблицы (для которой потом будем делать insert
     * delete и update
     */
    public String getName() {
        //return name;
        if (tables.containsKey(DTable.MAIN_TABLE)) {
            return tables.get(DTable.MAIN_TABLE);
        } else {
            return "";
        }
    }

     public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String n) {
        this.queryName = n;
    }
   
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String s) {
        this.sequence = s;
    }
    
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public ArrayList<IColumnBuilder> getColumnBuilders() {
        return columnBuilders;
    }

    public void setColumnBuilders(ArrayList<IColumnBuilder> columns) {
        this.columnBuilders = columns;
    }
    
    public HashMap< String, String > getTables() {
        return tables;
    }

    public void setTables(HashMap< String, String > t) {
        this.tables = t;
    }
    
    public ArrayList<DTableJoin> getTableJoins() {
        return tableJoins;
    }

    public void setTableJoins(ArrayList<DTableJoin> t) {
        this.tableJoins = t;
    }
    
    public IRowColumnVal createRowColumnVal(SKeyForColumn key) {
        for (IColumnBuilder builder : columnBuilders) {
            if (builder.getColumns().containsKey(key)) {
                return builder.createRowColumnVal(key);
            }
        }
        return null;
    }
    
    public ArrayList<SRelationInfo> getSubTables()
    {
            return subTables;
    }
    
    public  void setSubTables( ArrayList<SRelationInfo> subs)
    {
            subTables = subs;
    }
    
    public IColumnBuilder getColumnBuilder(SKeyForColumn key) {
        for (IColumnBuilder builder : columnBuilders) {
            for ( Map.Entry colEntry :builder.getColumns().entrySet() )
            {
                SKeyForColumn k = (SKeyForColumn)colEntry.getKey();
                if (k.equals(key))
                {
                    return builder;
                }
            }
        }
        return null;
    }
    public void setCountRowsInFiltrColumn(Integer countRows) {
        countRowsInFiltrColumn = countRows;
    }
    
    public Integer getCountRowsInFiltrColumn() {
        return countRowsInFiltrColumn ;
    }
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
    
    public Integer getRecordCount() {
        return recordCount ;
    }
    
    public void setLabelWidth(Integer lw) {
        labelWidth = lw;
    }
    
    public Integer getLabelWidth() {
        return labelWidth ;
    }
     public void setLabelFiltrWidth(Integer lw) {
        labelFiltrWidth = lw;
    }
    
    public Integer getLabelFiltrWidth() {
        return labelFiltrWidth ;
    }
    
    public int getFiltrHeight() {
        return filtrHeight;
    }

    public void setFiltrHeight(int filtrHeight) {
        this.filtrHeight = filtrHeight;
    }
}
