package pst.arm.client.common.barcodeservice;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import pst.arm.client.common.utils.xmlhelper.*;

/**
 * Класс представляет команду-ссылку (открыть какую-либо страницу или документ в приложении)
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class LinkBarCodeCommand extends BarCodeCommand {

    /**
     * Константа определяет тип данной команды
     */
    private static final TypeOfBarCodeCommand type = TypeOfBarCodeCommand.LINK;
    /**
     * Константа содержит тип действия команды-ссылки по умолчанию
     */
    private static final TypeOfActionOfLinkBarCodeCommand defaultAction = TypeOfActionOfLinkBarCodeCommand.Open;
    /**
     * Поле содержит тип действия команды-ссылки
     */
    private TypeOfActionOfLinkBarCodeCommand action;
    /**
     * Поле содержит тип объекта, на который указывает команда-ссылка
     */
    private TypeOfObjectOfLinkBarCodeCommand objectType;
    /**
     * Поле содержит название объекта, на который указывает команда-ссылка
     */
    private String objectName;
    /**
     * Поле содержит хэш-таблицу (название/значение), содержащую параметры данной команды
     */
    private final HashMap<String, String> objectParameters = new HashMap<String, String>(2);
    
    /**
     * Конструктор позволяет создать команду-ссылку с указанием действия команды, и объекта на который данная команда указывает
     * @param action Действие команды-ссылки
     * @param object Тип объекта, на который команда указывает
     * @param name Название объекта, на который команда указывает
     */
    public LinkBarCodeCommand(TypeOfActionOfLinkBarCodeCommand action, TypeOfObjectOfLinkBarCodeCommand object, String name) {
        this.action = action;
        this.objectType = object;
        this.objectName = name;
    }
    
    /**
     * Метод позволяет создать команду-ссылку из строки
     * @param str Строка, представляющая команду-ссылку
     * @return Созданная команда-ссылка
     * @throws ParseException Исключение бросается, если переданная строка не представляет команду-ссылку или если она (строка) некорректна
     */
    public static BarCodeCommand parseFromString(String str) throws ParseException {
        if (!XMLHelper.getXmlElements(str).get(0).getElementName().equals(String.valueOf(type.toChar()))) {
            throw new ParseException("It is not a link command", 0);
        }
        List<XMLAttribute> attributes = XMLHelper.getXMLAttributes(str);
        if (attributes.size() != 2 && attributes.size() != 3) {
            throw new ParseException("Incorrect number of attributes", 0);
        }
        Boolean wasSetTAttribute = false, wasSetNAttribute = false;
        TypeOfActionOfLinkBarCodeCommand action = defaultAction;
        TypeOfObjectOfLinkBarCodeCommand objectType = null;
        String objectName = null;
        for (XMLAttribute attribute : attributes) {
            if (!attribute.getName().equals("a") && !attribute.getName().equals("t") && !attribute.getName().equals("n")) {
                throw new ParseException("Incorrect attribute. In this command there are only attributes with name \"a\", \"t\" and \"n\"", 0);
            }
            if (attribute.getName().equals("a")) {
                action = TypeOfActionOfLinkBarCodeCommand.convertFromNumberOrString(attribute.getValue());
            }
            if (attribute.getName().equals("t")) {
                objectType = TypeOfObjectOfLinkBarCodeCommand.convertFromNumberOrString(attribute.getValue());
                wasSetTAttribute = true;
            }
            if (attribute.getName().equals("n")) {
                objectName = attribute.getValue();
                wasSetNAttribute = true;
            }
        }
        if (!wasSetNAttribute || !wasSetTAttribute) {
            throw new ParseException("Incorrect attributes. XML-element should have one attribute \"t\" and one attribute \"n\"", 0);
        }
        LinkBarCodeCommand command = new LinkBarCodeCommand(action, objectType, objectName);
        List<XMLElement> childElements = XMLHelper.getChildElements(str);
        for (XMLElement child : childElements) {
            if (!child.getElementName().equals("p")) {
                throw new ParseException("Incorrect child elements. Only \"p\" elements are valid", 0);
            }
            attributes = XMLHelper.getXMLAttributes(child);
            if (attributes.size() != 1) {
                throw new ParseException("Incorrect number of attributes of child xml-element", 0);
            }
            XMLAttribute attribute = attributes.get(0);
            if (!attribute.getName().equals("n")) {
                throw new ParseException("Incorrect attribute. This command require attribute with name \"n\"", 0);
            }
            command.addParameter(attribute.getValue(), XMLHelper.getValue(child));
        }
        return command;
    }
    
    /**
     * Метод позволяет добавить к данной команде параметр
     * @param key Ключ параметра (название параметра). У команды не может быть двух параметров с одинаковым названием
     * @param value Значение параметра
     */
    public void addParameter(String key, String value) {
        objectParameters.put(key, value);
    }
    
    /**
     * Метод позволяет получить тип данной команды
     * @return Тип данной команды (LINK)
     */
    @Override
    public TypeOfBarCodeCommand getTypeOfCommand() {
        return type;
    }

    /**
     * Метод позволяет получить строковое представление данной команды
     * @return Строка, представляющая данную команду. 
     * Примеры: 
     * {@code <l a="0" t="0" n="name"/>}, 
     * {@code <l t="0" n="name"><p n="key">value</p><p n="key2">value2</p></l>}
     */
    @Override
    public String getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append('<');
        builder.append(getTypeOfCommand().toChar());
        builder.append(" t=\"");
        builder.append(objectType.toNumber());
        builder.append("\" n=\"");
        builder.append(objectName);
        builder.append('"');
        if (action != defaultAction) {
            builder.append(" a=\"");
            builder.append(action.toNumber());
            builder.append('"');
        }
        if (!objectParameters.isEmpty()) {
            builder.append('>');
            for (String key : objectParameters.keySet()) {
                builder.append("<p n=\"");
                builder.append(key);
                builder.append("\">");
                builder.append((String)objectParameters.get(key));
                builder.append("</p>");
            }
            builder.append("</");
            builder.append(getTypeOfCommand().toChar());
            builder.append('>');
        }
        else {
            builder.append("/>");
        }
        return builder.toString();
    }

    /**
     * Метод позволяет выполнить данную команду
     * @exception UnsupportedBarCodeOperationException Метод не реализован
     */
    @Override
    public void execute() {
        throw new UnsupportedBarCodeOperationException("Not supported execution of link command");
    }
    
    /**
     * Метод позволяет установить новое действие данной команды
     * @param newAction Новое действие команды
     */
    public void setAction(TypeOfActionOfLinkBarCodeCommand newAction) {
        action = newAction;
    }
    
    /**
     * Метод позволяет установить новый тип объекта, на который будет указывать команда-ссылка
     * @param newObjectType Новый тип объекта
     */
    public void setObjectType(TypeOfObjectOfLinkBarCodeCommand newObjectType) {
        objectType = newObjectType;
    }
    
    /**
     * Метод позволяет установить название нового объекта, на который будет указывать команда-ссылка
     * @param newObject Новое название объекта
     */
    public void setObjectName(String newObject) {
        objectName = newObject;
    }
    
    /**
     * Метод позволяет удалить параметр команды с заданным названием
     * @param key Название параметра, который будет удален
     */
    public void removeParameter(String key) {
        objectParameters.remove(key);
    }
    
}