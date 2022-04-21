package pst.arm.client.modules.datagrid.domain;

import pst.arm.client.modules.datagrid.domain.expansion.DColumnBuilderMultiForDepartList;

import java.io.Serializable;


/*
 * @author LKHorosheva
 * @since 0.0.1
 * T тип данных полученных из БД
 * D тип данных отформатированных для отображения (например из БД получаем int (0,1) а показываем String ("да", "нет")
 */
public class IRowColumnVal<T,D> implements Serializable{
   
    protected T val; //фактическое значение выбранное из БД (оригинальные данные)
  
    public IRowColumnVal()
    {
           val = null;
    }

    /*
     * функция заполняет оригинальные данные в том виде, в котором они полученны из БД 
     */
    public void setVal(T val) 
    {
        this.val = val;
    }
     /*
     * функция возвращает оригинальные данные в том виде, в котором они полученны из БД 
     */
    public T getVal() 
    {
       return val;
    }
    
    /*
     * функция преобразуте строку значения полученную из БД БЕЗ ФОРМАТИРОВАНИЯ в
     * фактическое значение тоже без форматирования (используеться в маппере)
     */
    public void setValFromString(SKeyForColumn key, String str, IColumnBuilder columnBuilder) {
        val = (T) columnBuilder.getColumn(key).getColumnProperty().stringToValue(str);
    }

    /*
     * функция возвращает строку ОТФОРМАТИРОВАННЫХ ( функцией getFormatVal() ) данных (используется в отображения в таблице)
     */
    public String getStringFromVal(SKeyForColumn key, IColumnBuilder columnBuilder) {
        return columnBuilder.getColumn(key).getColumnProperty().valueToString(val);
    }
    
    /*
     * функция возвращает строку ОТФОРМАТИРОВАННЫХ ( функцией getFormatVal() ) данных (используется в отображения в таблице)
     */
    public String getStringFromVal(DDataGrid domain, SKeyForColumn key, IColumnBuilder columnBuilder) {
        return columnBuilder.getColumn(key).getColumnProperty().valueToString(domain, val);
    }
   
    
    
    
    /*
     * функция возвращает отформатированные данные из оригинальных(полученных из БД в запросе)
     */
    public D getFormatVal(SKeyForColumn key,IColumnBuilder columnBuilder) 
    {
        return (D)columnBuilder.getColumn(key).getColumnProperty().getFormatFromValue(val);
    }
    
    /*
     * функция заполняет оригинальные данные из отформатированных
     */
    public void setValFromFormat(SKeyForColumn key, D valFormat, IColumnBuilder columnBuilder) {
        this.val = (T) columnBuilder.getColumn(key).getColumnProperty().getValueFromFormat(valFormat);
    }

    public EColumnType getType(SKeyForColumn key, IColumnBuilder columnBuilder) {
        return columnBuilder.getColumn(key).getColumnProperty().getType();
    }
    
    public boolean isNotChanges(SKeyForColumn key, IColumnBuilder columnBuilder, T val) {

        if (columnBuilder instanceof DColumnBuilderMultiForDepartList) {
            if (key.equals(new SKeyForColumn("MAIN:DEPART_ID2")) || key.equals(new SKeyForColumn("DEPART:SHORT_NAME_FILTR")) || key.equals(new SKeyForColumn("DEPART:SHORT_NAME")) || key.equals(new SKeyForColumn("MAIN:DEPART_ID")))
                return true;
            return columnBuilder.getColumn(key).getColumnProperty().isNotChanges(Integer.parseInt((String)this.val), val);
        }

        return columnBuilder.getColumn(key).getColumnProperty().isNotChanges(this.val, val);        
    }
    
}