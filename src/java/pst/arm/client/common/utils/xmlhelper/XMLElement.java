package pst.arm.client.common.utils.xmlhelper;

/**
 * Класс представляет xml-элемент
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class XMLElement {
    
    /**
     * Поле содержит текстовое представление xml-элемента
     */
    private final String element;
    /**
     * Поле содержит название элемента
     */
    private final String elementName;
    
    /**
     * Конструктор создает экземпляр класса (заданный xml-элемент с заданным названием)
     * @param name Название xml-элемента. Пример: {@code <exampl class="java6Ex">ex</exampl>} - данный xml-элемент имеет название exampl
     * @param element Xml-элемент
     */
    public XMLElement(String name, String element) {
        this.element = element;
        elementName = name;
    }
    
    /**
     * Метод позволяет получить строковое представление xml-элемента
     * @return Строка, представляющая xml-элемент
     */
    public String getElement() {
        return element;
    }
    
    /**
     * Метод позволяет получить название xml-элемента
     * @return Имя XML-элемента
     */
    public String getElementName() {
        return elementName;
    }
    
}