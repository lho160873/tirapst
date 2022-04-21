package pst.arm.client.common.utils.xmlhelper;

import java.text.ParseException;

/**
 * Класс представляет xml-атрибут
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class XMLAttribute {
    
    /**
     * Поле содержит текстовое представление xml-атрибута (так как он представлен в xml-элемента: {@code name="value"})
     */
    private final String xmlAttribute;
    /**
     * Поле содержит имя атрибута
     */
    private String name;
    /**
     * Поле содержит значение арибута
     */
    private String value;
    
    /**
     * Конструктор создает экземпляр класса из текстовой строки, автоматически парся строку и выдергивая из нее имя и значение атрибута
     * @param xmlAttribute Строка, представляющая xml-атрибут. Формат строки: {@code name="value"}. В случае, если строка имеет другой формат -
     *  результат парсинга (как и работы конструктора) не определен. Значение атрибута не может содержать специальные символы: >, <, =, "
     * @throws ParseException Исключение бросается, если формат строки не соответствует требуемому и конструктору не удалось ее (строку)
     *  распарсить. В случае, если формат строки не соответствует требуемому, исключение может и не бросаться (но парсинг пройдет с ошибкой)
     */
    public XMLAttribute(String xmlAttribute) throws ParseException  {
        this.xmlAttribute = xmlAttribute;
        Proceed();
    }
    
    /**
     * Метод парсит строковое представление xml-атрибута и заполняет поля класса (имя и значение атрибута)
     * @throws ParseException Исключение бросается в частных случаях несоблюдения формата строки атрибута (если строковое представление атрибута 
     * содержит не один знак равенства или если длина подстроки после равенства (включая необходимые кавычки) меньше 2
     */
    private void Proceed() throws ParseException {
        String[] parts = xmlAttribute.split("=", 2);
        if (parts.length != 2)
            throw new ParseException("Incorrect attribute: " + xmlAttribute, 0);
        name = parts[0];
        if (value.length() < 2)
            throw new ParseException("Incorrect attribte (can't parse value of attribute) : " + xmlAttribute, name.length());
        value = parts[1].substring(1, parts[1].length() - 1);
    }
    
    /**
     * Метод позволяет получить имя атрибута
     * @return Имя атрибута
     */
    public String getName() {
        return name;
    }
    
    /**
     * Метод возвращает значение атрибута
     * @return Значение атрибута
     */
    public String getValue() {
        return value;
    }
    
}