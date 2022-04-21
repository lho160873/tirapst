package pst.arm.client.common.barcodeservice;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import pst.arm.client.common.utils.xmlhelper.*;

/**
 * Класс представляет пользовательскую (специальную) команду
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class CustomBarCodeCommand extends BarCodeCommand {

    /**
     * Константа определяет тип данной команды
     */
    private static final TypeOfBarCodeCommand type = TypeOfBarCodeCommand.CUSTOM;
    /**
     * Поле содержит тип действия пользовательской команды
     */
    private TypeOfCustomBarCodeCommand action;
    /**
     * Поле содержит хэш-таблицу (название/значение), содержащую параметры данной команды
     */
    private final HashMap<String, String> parameters = new HashMap<String, String>(2);
    
    /**
     * Конструктор позволяет создать пользовательскую команду с указанием ее действия
     * @param action 
     */
    public CustomBarCodeCommand(TypeOfCustomBarCodeCommand action) {
        this.action = action;
    }
    
    /**
     * Метод позволяет создать пользовательскую команду из строки
     * @param str Строка, представляющая пользовательскую команду
     * @return Созданная пользовательская команда
     * @throws ParseException Исключение бросается, если переданная строка не представляет пользовательскую команду или если она (строка) некорректна
     */
    public static BarCodeCommand parseFromString(String str) throws ParseException {
        if (!XMLHelper.getXmlElements(str).get(0).getElementName().equals(String.valueOf(type.toChar()))) {
            throw new ParseException("It is not a custom command", 0);
        }
        List<XMLAttribute> attributes = XMLHelper.getXMLAttributes(str);
        if (attributes.size() != 1) {
            throw new ParseException("Incorrect number of attributes", 0);
        }
        XMLAttribute attribute = attributes.get(0);
        if (!attribute.getName().equals("a")) {
            throw new ParseException("Incorrect attribute. There is no attribute with name \"a\"", 0);
        }
        CustomBarCodeCommand command = new CustomBarCodeCommand(TypeOfCustomBarCodeCommand.convertFromNumberOrString(attribute.getValue()));
        List<XMLElement> childElements = XMLHelper.getChildElements(str);
        for (XMLElement child : childElements) {
            if (!child.getElementName().equals("p")) {
                throw new ParseException("Incorrect child elements. Only \"p\" elements are valid", 0);
            }
            attributes = XMLHelper.getXMLAttributes(child);
            if (attributes.size() != 1) {
                throw new ParseException("Incorrect number of attributes of child xml-element", 0);
            }
            attribute = attributes.get(0);
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
        parameters.put(key, value);
    }
    
    /**
     * Метод позволяет получить тип данной команды
     * @return Тип данной команды (CUSTOM)
     */
    @Override
    public TypeOfBarCodeCommand getTypeOfCommand() {
        return type;
    }

    /**
     * Метод позволяет получить строковое представление данной команды
     * @return Строка, представляющая данную команду. 
     * Примеры: 
     * {@code <c a="3"><p n="key">value</p><p n="key2">value2</p></c>}, 
     * {@code <c a="0"/>}
     */
    @Override
    public String getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append('<');
        builder.append(getTypeOfCommand().toChar());
        builder.append(" a=\"");
        builder.append(action.toNumber());
        builder.append('"');
        if (!parameters.isEmpty()) {
            builder.append('>');
            for (String key : parameters.keySet()) {
                builder.append("<p n=\"");
                builder.append(key);
                builder.append("\">");
                builder.append((String)parameters.get(key));
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
        throw new UnsupportedBarCodeOperationException("Not supported execution of custom command");
    }
    
    /**
     * Метод позволяет установить новое действие данной команды
     * @param newAction Новое действие команды
     */
    public void setAction(TypeOfCustomBarCodeCommand newAction) {
        action = newAction;
    }
    
    /**
     * Метод позволяет удалить параметр команды с заданным названием
     * @param key Название параметра, который будет удален
     */
    public void removeParameter(String key) {
        parameters.remove(key);
    }
    
}