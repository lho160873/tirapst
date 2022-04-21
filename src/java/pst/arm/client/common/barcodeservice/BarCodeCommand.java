package pst.arm.client.common.barcodeservice;

import java.text.ParseException;

/**
 * Класс определяет функциональность команд, которые могут быть переданы через QR-коды
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public abstract class BarCodeCommand {
    
    /**
     * Метод позволяет получить тип конкретной команды
     * @return Тип данной команды
     */
    public abstract TypeOfBarCodeCommand getTypeOfCommand();
    
    /**
     * Метод позволяет получить строковое представление команды
     * @return Строковое представление команды
     */
    public abstract String getStringRepresentation();
    
    /**
     * Метод выполняет команду
     */
    public abstract void execute();
    
    /**
     * Метод позволяет преобразовать строку в команду (обратное методу getStringRepresentation преобразование)
     * @param representationString Строка, представляющая команду
     * @param type Тип команды
     * @return Команда
     * @throws ParseException Исключение бросается, если не удалось преобразовать строку к команде заданного типа
     * @exception UnsupportedBarCodeOperationException Исключение бросается, если преобразование строки к команде заданного типа не реализовано
     */
    public static BarCodeCommand getBarCodeCommand(String representationString, TypeOfBarCodeCommand type) throws ParseException {
        switch (type) {
            case BACK_CONTROL:
                return BackControlBarCodeCommand.parseFromString(representationString);
            case CUSTOM:
                return CustomBarCodeCommand.parseFromString(representationString);
            case LINK:
                return LinkBarCodeCommand.parseFromString(representationString);
            case INVOKE:
            case SQL:
            case TEXT:
            default:
                throw new UnsupportedBarCodeOperationException("Unknown qr command");
        }
    }
    
}