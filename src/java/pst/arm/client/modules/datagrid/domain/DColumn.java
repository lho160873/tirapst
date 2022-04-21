package pst.arm.client.modules.datagrid.domain;
import java.io.Serializable;


/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DColumn implements Serializable {

    protected Boolean isKey = Boolean.FALSE;            ///< признак того является ли поле ключевым
    protected Boolean isVisible = Boolean.TRUE;        ///< признак того является ли поле видимым в таблице
    protected Boolean isVisibleEdit = Boolean.TRUE;    ///< признак того является ли поле видимым на форме редактирования
    protected Boolean isFiltr = Boolean.TRUE;          ///< будет ли учавствовать в списке полей для фильтрации данных
    protected Boolean isAutoincrement = Boolean.FALSE;  ///< поле в БД автоинкрементное 
    protected String name = "";             ///< название колонки в таблице БД (англ.)  
    protected String caption = "";          ///< название(заголовок) колонки (русс.)    
    protected Boolean isNotNull = Boolean.FALSE;        ///< признак того что значение в данной колонке не может быть нулевым
    protected Boolean isEditable = Boolean.TRUE;       ///< признак того что поле может быть редактируемым при редактировании
    protected Boolean isInserted = Boolean.TRUE;       ///< Если FALSE, то признак того что поле будет отображаться при добавлении, но не будет доступно для редактирования и не будет добавлено в запрос
    protected Boolean isEditableAdd = Boolean.TRUE;     ///< признак того что поле будет отображаться при добавлении, но не будет доступно для редактирования
    protected IColumnProperty columnProperty = new DColumnPropertyTextField(); //свойства значений колонки(тип поля, свойства форматирования....)
    protected String sqlForColumn = null;           ///<запрос для колонки (если это поле пустое запрос строим из SKeyForColumn
    private Boolean isAlwaysUpdated = false;           // Поле вне зависимости от isEditable будет обнолвено в запросе
    private Boolean isHidden = false; ///< Поле буде скрыто совсем (не будет выдно в выпадающем списке возможных полей на отображение)
    protected String subCaption = null; // Комментарий к записи на форме редактирования/добавления. Выводится непосредственно под полем, если значение не null;
    protected ESubCaptonTextColor subCaptionTextColor = ESubCaptonTextColor.BLACK; // Цвет текста комментария к полям на форме редактирования
    protected Boolean isSumm = false; //Если TRUE, то можно будет получать сумму поля; при работе этого поля также не действует свойство sqlForCol

    public ESubCaptonTextColor getSubCaptionTextColor() {
        return subCaptionTextColor;
    }

    public void setSubCaptionTextColor(ESubCaptonTextColor subCaptionTextColor) {
        this.subCaptionTextColor = subCaptionTextColor;
    }

    public Boolean getIsAlwaysUpdated() {
        return isAlwaysUpdated;
    }

    public void setIsAlwaysUpdated(Boolean isAlwaysUpdated) {
        this.isAlwaysUpdated = isAlwaysUpdated;
    }

    public Boolean getIsEditableAdd() {
        return isEditableAdd;
    }

    public void setIsEditableAdd(Boolean isEditableAdd) {
        this.isEditableAdd = isEditableAdd;
    }

    public void setSqlForColumn(String sqlForColumn) {
        this.sqlForColumn = sqlForColumn;
    }

    public String getSqlForColumn() {
        return sqlForColumn;
    }
    public IColumnProperty getColumnProperty() 
    {  
        return columnProperty;  
    }  
    public void setColumnProperty(IColumnProperty cp) 
    {
        this.columnProperty = cp;
    }  
  
    public void setIsInserted(Boolean b) {
        isInserted = b;
    }

    public Boolean getIsInserted() {
        return isInserted;
    }
    
    public void  setIsEditable( Boolean b )
    {
        isEditable = b;
    }
    
    public Boolean getIsEditable()
    {
        return isEditable;
    }
   
    public DColumn()
    {
    }

    
    public DColumn( String n, String c )
    {
        this();
        name = n;
        caption = c;
    }

    public Boolean getIsKey() {
        return isKey;
    }

    public void setIsKey(Boolean k) {
        //если поле автоинкрементное то оно всегда и ключевое тоже
        if (this.isAutoincrement) {
            this.isKey = Boolean.TRUE;
        } else {
            this.isKey = k;
        }
    }
    
     public Boolean getIsNotNull() {
        return isNotNull;
    }

    public void setIsNotNull(Boolean n) {
       isNotNull = n;
    }

    public Boolean getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(Boolean a) {
        this.isAutoincrement = a;
        if (this.isAutoincrement) {
            this.isKey = Boolean.TRUE; //если поле автоинкрементное то оно всегда и ключевое тоже
        }
    }


    public String getSubCaption() {
        return subCaption;
    }

    public void setSubCaption(String subCaption) {
        this.subCaption = subCaption;
    }


    public void setIsFiltr(Boolean v) {
        this.isFiltr = v;
    }

    public Boolean getIsFiltr() {
        //return (isVisible && isFiltr); //поле должно быть видимое в таблице
        return isFiltr; 
    }

    
    public void setIsVisible(Boolean v) {
        this.isVisible = v;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }
    
     public void setIsHidden(Boolean h) {
        this.isHidden = h;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }
    public void setIsVisibleEdit( Boolean v ) {
        this.isVisibleEdit = v;
    }
     public Boolean getIsVisibleEdit() {
        return isVisibleEdit;
    }
    
    public String getName() {
        return name;
    }

    public void setName( String n ) {
        this.name = n;
    }
    
    public String getCaption() {
        return caption;
    }

    public void setCaption(String c) {
        this.caption = c;
    }
    
    public void setIsSumm(Boolean s) {
        this.isSumm = s;
    }

    public Boolean getIsSumm() {
        return isSumm;
    }

}
