package pst.arm.client.common.barcodeservice;

import pst.arm.client.common.utils.xmlhelper.XMLHelper;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import pst.arm.client.common.utils.xmlhelper.XMLElement;

/**
 * Класс представляет собой набор команд, который может быть преобразован в строку
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class BarCode {
    
    /**
     * Поле, содержащее строковое представление списка команд
     */
    private String strRepresentation;
    /**
     * Поле, содержащее значение - является ли strRepresentation строковым представлением данного объекта таким, каким он является сейчас
     */
    private Boolean isStringRepresentationUpToDate = false;
    /**
     * Поле, содержащее значение - является ли данный экземпляр расширенной командой 
     * (которая имеет только строковое представление и не может быть разложена на список команд, отредактирована или исполнена)
     */
    private Boolean isExtendedString = false;
    /**
     * Поле является списком команд
     */
    private final List<BarCodeCommand> commands = new ArrayList<BarCodeCommand>(1);
    
    /**
     * Конструктор по умолчанию
     */
    public BarCode() {
    }
    
    /**
     * Конструктор позволяет создать список команд, сразу инициализированный одной заданной командой
     * @param oneCommand Команда, которая должна быть добавлена в созданный список команд
     */
    public BarCode(BarCodeCommand oneCommand) {
        addCommand(oneCommand);
    }
    
    /**
     * Метод позволяет создать список команд, распарсив его из строки. Если распарсить строку не удастся, 
     * будет создана расширенная команда (isExtendedString)
     * @param string Строка, из которой будет создан список команд
     * @return Экземпляр данного класса (содержащий список команд или его строковое представление)
     */
    public static BarCode createExtendedBarCode(String string) {
        return new BarCode(string);
    }
    
    /**
     * Констркутор позволяет создать список команд, распарсив его из строки. Если распарсить строку не удастся, 
     * будет создана расширенная команда (isExtendedString)
     * @param string Строка, из которой будет создан список команд
     */
    private BarCode(String string) {
        isStringRepresentationUpToDate = isExtendedString = true;
        strRepresentation = string;
        try {
            parseString();
            isExtendedString = false;
        } catch (ParseException exception) {
        }
    }
    
    /**
     * Метод пытается преобразовать строковое представление списка команд (strRepresentation) в список команд (commands)
     * @throws ParseException Исключение бросается, если распарсить строковое представление списка команд не удалось и 
     * список команд создан/заполнен не был
     */
    private void parseString() throws ParseException {
        List<BarCodeCommand> temp = parseString(strRepresentation);
        commands.addAll(temp);
    }
    
    /**
     * Метод пытается преобразовать строковое представление списка команд в список команд
     * @param string Строковое представление списка команд
     * @return Список команд
     * @throws ParseException Исключение бросается, если распарсить строковое представление списка команд не удалось
     */
    public List<BarCodeCommand> parseString(String string) throws ParseException {
        if (string.length() < 9)
            throw new ParseException("This string doesn't represent QR Code", 0);
        string = string.trim();
        string = string.substring(4, string.length() - 5);
        List<BarCodeCommand> temp = new ArrayList<BarCodeCommand>(1);
        try {
            List<XMLElement> list = XMLHelper.getXmlElements(string);
            for (XMLElement element : list) {
                BarCodeCommand command = BarCodeCommand.getBarCodeCommand(element.getElement(), TypeOfBarCodeCommand.convertFromChar(element.getElementName().charAt(0)));
                temp.add(command);
            }
        } catch (ParseException exception) {
            throw new ParseException(exception.getMessage(), exception.getErrorOffset() + 4);
        }
        return temp;
    }
    
    /**
     * Метод добавляет команду к списку команд, если данный объект не является расширенной командой (isExtendedString)
     * @param command Команда, которую необходимо добавить
     */
    public void addCommand(BarCodeCommand command) {
        if (!isExtendedString) {
            commands.add(command);
            isStringRepresentationUpToDate = false;
        }
    }
    
    /**
     * Метод позволяет получить строковое представление списка команд
     * @return Строковое представление списка команд
     */
    public String getStringRepresentation() {
        if (isExtendedString) {
            return strRepresentation;
        } else {
            if (!isStringRepresentationUpToDate) {
                buildStringRepresentation();
            }
            return strRepresentation;
        }
    }
    
    /**
     * Метод позволяет выполнить все команды в списке по очереди
     */
    public void execute() {
        for (BarCodeCommand command : commands) {
            command.execute();
        }
    }
    
    /**
     * Метод преобразует список команд в его строковое представление
     */
    private void buildStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("<qr>");
        for (BarCodeCommand command : commands) {
            builder.append(command.getStringRepresentation());
        }
        builder.append("</qr>");
        strRepresentation = builder.toString();
            isStringRepresentationUpToDate = true;
    }
    
    /**
     * Метод позволяет получить значение - является ли данный экземпляр расширенной командой 
     * (которая имеет только строковое представление и не может быть разложена на список команд, отредактирована или исполнена)
     * @return true, если данный экземпляр является расширенной командой; false - иначе
     */
    public Boolean getIsExtendedStringCommand() {
        return isExtendedString;
    }
    
}