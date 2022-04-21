package pst.arm.client.common.utils.xmlhelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс предоставялет набор статичных методов для обработки строк, содержащих xml-разметку.
 * Методы класса предполагают, что везде в виде строки передается один корректный xml-элемент (для методов getXmlElements и getNearXmlElement) 
 * может передаваться несколько корректных xml-элементов (без "мусора" - лишних символов до, после или между корневыми xml-элементами), 
 * [предполагают] что значения атрибутов не содержат недопустимых символов (включая: кавычки, угловые скобки, пробел, знак равенства), а также 
 * что значения xml-элементов (тело, исключая дочерние xml-элементы) не содержат недопустимых символов (включая, угловые скобки). Любые 
 * передаваемые строки не должны содержать символов переноса строки
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public final class XMLHelper {
    
    /**
     * Метод позволяет получить коллекцию xml-тэгов
     * @param xmlText Строка, содержащая xml-тэги. Предполагается, что данная строка содержит корректные xml-тэги и только корректные xml-тэги 
     * (и ничего больше)
     * @return Коллекция xml-тэгов
     * @throws ParseException Исключение бросается, если входная строка содержит что-либо, кроме корректных xml-тэгов и произошла ошибка парсинга
     */
    public static List<XMLElement> getXmlElements(String xmlText) throws ParseException {
        List<XMLElement> collection = new ArrayList<XMLElement>();
        int offset = 0;
        try {
            while (!xmlText.isEmpty()) {
                String str = XMLHelper.getNearXmlElement(xmlText);
                int firstSpace = Integer.MAX_VALUE;
                int temp = str.indexOf(' ');
                if (temp > 0 && temp < firstSpace) firstSpace = temp;
                temp = str.indexOf('>');
                if (temp > 0 && temp < firstSpace) firstSpace = temp;
                temp = str.indexOf('/');
                if (temp > 0 && temp < firstSpace) firstSpace = temp;
                String nameOfElement = str.substring(1, firstSpace);
                collection.add(new XMLElement(nameOfElement, str));
                offset += str.length();
                xmlText = xmlText.substring(str.length());
            }
        } catch (Exception exception) {
            throw new ParseException(exception.getMessage(), offset);
        }
        return collection;
    }
    
    /**
     * Метод позволяет выбрать из текстовой строки один (первый по порядку) xml-элемент
     * @param xmlText Строка, из которой необходимо выбрать xml-элемент. Предполагается, что строка содержит хотя бы один xml-элемент, что
     * строка начинается с xml-элемента (первый символ строки - открывающая угловая скобка), xml-элемент полностью корректен. При нарушении 
     * данных требований результат работы метода не определен
     * @return Строка, содержащая текст только первого xml-элемента. Пример: входные данные - 
     * "{@code <tt pp="vv"><ll/><cc>content</cc></tt><ss/>}", метод вернет - "{@code <tt pp="vv"><ll/><cc>content</cc></tt>}"
     * @throws ParseException Исключение может бросаться, если входная строка не содержит корректный xml-элемент или начинается не с него
     * @exception IndexOutOfBoundsException Исключение может бросаться, если входная строка не содержит корректный xml-элемент или начинается не 
     * с него
     */
    public static String getNearXmlElement(String xmlText) throws ParseException {
        int firstSpace = Integer.MAX_VALUE;
        int temp = xmlText.indexOf(' ');
        if (temp > 0 && temp < firstSpace) firstSpace = temp;
        temp = xmlText.indexOf('>');
        if (temp > 0 && temp < firstSpace) firstSpace = temp;
        temp = xmlText.indexOf('/');
        if (temp > 0 && temp < firstSpace) firstSpace = temp;
        String nameOfElement = xmlText.substring(1, firstSpace);
        String p = "<" + nameOfElement + "([^>]*?/|.*?</" + nameOfElement + ")>";
        Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(xmlText);
        if (!matcher.find())
            throw new ParseException("There is not any correct xml elements in " + xmlText, 0);
        return matcher.group();
    }
    
    /**
     * Метод позволяет получить коллекцию атрибутов xml-элемента
     * @param element Xml-элемент. Предполагается, что элемент корректен, а также, что значения атрибутов не могут
     *  содержать символов '>' и ' ' (пробел)
     * @return Коллекция xml-атрибутов
     * @throws ParseException Исключение бросается, в случае если xml-элемент имеет некорректное строковое представление
     */
    public static List<XMLAttribute> getXMLAttributes(XMLElement element) throws ParseException {
        return getXMLAttributes(element.getElement());
    }
    
    /**
     * Метод позволяет получить коллекцию атрибутов xml-элемента
     * @param xmlElement Строковое представление xml-элемента. Предполагается, что элемент корректен, а также, что значения атрибутов не могут
     *  содержать символов '>' и ' ' (пробел)
     * @return Коллекция xml-атрибутов
     * @throws ParseException Исключение бросается, в случае если входная строка (xml-элемент или его xml-атрибуты) имеют некорректный формат
     */
    public static List<XMLAttribute> getXMLAttributes(String xmlElement) throws ParseException {
        try {
            String body = xmlElement.substring(1, xmlElement.indexOf('>'));
            if (body.charAt(body.length() - 1) == '/')
                body = body.substring(0, body.length() - 1);
            String[] parts = body.split(" ");
            List<XMLAttribute> attributes = new ArrayList<XMLAttribute>(4);
            for (int i = 1; i < parts.length; i++) {
                if (!parts[i].equals(""))
                    attributes.add(new XMLAttribute(parts[i]));
            }
            return attributes;
        } catch (Exception e) {
            ParseException exception = new ParseException("Incorrect xml element (can't extract attributes) :" + xmlElement, 0);
            exception.initCause(e.getCause());
            throw exception;
        }
    }
    
    /**
     * Метод позволяет узнать - имеются ли у заданного xml-элемента дочерние xml-элементы
     * @param element Xml-элемент
     * @return true, если дочерние элементы имеются; false - иначе
     */
    public static Boolean hasChildElements(XMLElement element) {
        return hasChildElements(element.getElement());
    }
    
    /**
     * Метод позволяет узнать - имеются ли у заданного xml-элемента дочерние xml-элементы
     * @param xmlElement Строка, представляющая xml-элемент
     * @return true, если дочерние элементы имеются; false - иначе
     */
    public static Boolean hasChildElements(String xmlElement) {
        String content = getContent(xmlElement);
        return (content.contains("<"));
    }
    
    /**
     * Метод позволяет получить коллекцию дочерних xml-элементов
     * @param element Xml-элемент, коллекцию дочерних элементов которого необходимо получить
     * @return Коллекция дочерних xml-элементов
     * @throws ParseException Исключение бросается, если какие-либо из xml-элементов некорректны
     */
    public static List<XMLElement> getChildElements(XMLElement element) throws ParseException {
        return getChildElements(element.getElement());
    }
    
    /**
     * Метод позволяет получить коллекцию дочерних xml-элементов
     * @param xmlElement Строковое представление xml-элемента, коллекцию дочерних элементов которого необходимо получить
     * @return Коллекция дочерних xml-элементов
     * @throws ParseException Исключение бросается, если какие-либо из xml-элементов некорректны
     */
    public static List<XMLElement> getChildElements(String xmlElement) throws ParseException {
        if (!hasChildElements(xmlElement))
            return new ArrayList<XMLElement>(0);
        String content = getContent(xmlElement);
        StringBuilder childs = new StringBuilder();
        while (true) {
            if (content.indexOf('<') != 0) {
                content = content.substring(content.indexOf('<'));
            }
            String element = getNearXmlElement(content);
            content = content.substring(element.length());
            childs.append(element);
            if (content.isEmpty() || content.indexOf('<') == -1)
                break;
        }
        return getXmlElements(childs.toString());
    }
    
    /**
     * Метод позволяет узнать - имеется ли у заданного xml-элемента значение
     * @param element Xml-элемент, который необходимо проверить на наличие значения
     * @return true, если xml-элемент имеет значение; false - иначе
     * @throws ParseException Исключение бросается, если содержимое xml-элемента некорректно
     */
    public static Boolean hasValue(XMLElement element) throws ParseException {
        return hasValue(element.getElement());
    }
    
    /**
     * Метод позволяет узнать - имеется ли у заданного xml-элемента значение
     * @param xmlElement Строковое представление xml-элемента, который необходимо проверить на наличие значения
     * @return true, если xml-элемент имеет значение; false - иначе
     * @throws ParseException Исключение бросается, если содержимое заданного xml-элемента некорректно
     */
    public static Boolean hasValue(String xmlElement) throws ParseException {
        return (getValue(xmlElement).equals(""));
    }
    
    /**
     * Метод позволяет получить значение (тело) xml-элемента, исключая дочерние xml-элементы
     * @param element Xml-элемент
     * @return Строка, содержащая значене xml-элемента
     * @throws ParseException Исключение бросается, если xml-элемент содержит дочерние xml-элементы и среди них есть некорректный элемент, либо
     *  если значение xml-элемента содержит недопустимый символ ('<')
     */
    public static String getValue(XMLElement element) throws ParseException {
        return getValue(element.getElement());
    }
    
    /**
     * Метод позволяет получить значение (тело) xml-элемента, исключая дочерние xml-элементы
     * @param xmlElement Строковое представление xml-элемента
     * @return Строка, содержащая значене xml-элемента. Пример: xml-элемент - {@code <element paramName="value">Content<child/></element>},
     *  возвращаемое значение - {@code Content}
     * @throws ParseException Исключение бросается, если xml-элемент содержит дочерние xml-элементы и среди них есть некорректный элемент, либо
     *  если значение xml-элемента содержит недопустимый символ ('<')
     */
    public static String getValue(String xmlElement) throws ParseException {
        String content = getContent(xmlElement);
        StringBuilder value = new StringBuilder();
        while (content.length() != 0) {
            if (content.charAt(0) == '<') {
                String element = getNearXmlElement(content);
                content = content.substring(element.length());
            } else {
                if (content.indexOf('<') == -1) {
                    value.append(content);
                    content = "";
                } else {
                    value.append(content.substring(0, content.indexOf('<')));
                    content = content.substring(content.indexOf('<'));
                }
            }
        }
        return value.toString();
    }
    
    /**
     * Метод позволяет получить содержимое xml-элемента
     * @param element Xml-элемент
     * @return Тело xml-элемента
     * @exception IndexOutOfBoundsException Исключение бросается, если входная строка не содержит корректного xml-элемента
     */
    private static String getContent(XMLElement element) {
        return getContent(element.getElement());
    }
    
    /**
     * Метод позволяет получить содержимое xml-элемента
     * @param xmlElement Строковое представление xml-элемента
     * @return Тело xml-элемента. Пример: xml-элемент - {@code <element paramName="value">Content<child/></element>}, возвращаемое значение - 
     * {@code Content<child/>}
     * @exception IndexOutOfBoundsException Исключение бросается, если входная строка не содержит корректного xml-элемента
     */
    private static String getContent(String xmlElement) {
        if (xmlElement.charAt(xmlElement.length() - 2) == '/')
            return ""; else
            return xmlElement.substring(xmlElement.indexOf('>') + 1, xmlElement.lastIndexOf('<'));
    }
    
}