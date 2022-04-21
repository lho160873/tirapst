package pst.arm.client.common.barcodeservice;

/**
 * Класс представляет перечисление типов команд, передаваемых через штрих-коды, и методы для работы с ним
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public enum TypeOfBarCodeCommand {
    
    /**
     * Команда ссылка (позволяет создать/открыть/удалить документ/страницу/карту/...)
     */
    LINK,
    /**
     * Команда вызова
     */
    INVOKE,
    /**
     * Текстовая команда
     */
    TEXT,
    /**
     * Команда для работы с базой данных
     */
    SQL,
    /**
     * Пользовательская команда
     */
    CUSTOM,
    /**
     * Команда обратного управления (позволяет изменить что-либо в программе)
     */
    BACK_CONTROL;

    /**
     * Метод позволяет преобразовать символ (представляющий значение перечисления) к значению перечисления
     * @param appropriateSymbol Символ, который необходимо преобразовать
     * @return Значение перечисления, к которому был преобразован символ
     * @exception UnsupportedBarCodeOperationException Исключение бросается, если невозможно преобразовать заданный символ к значению перечисления. 
     * Данному символу не соответствует ни одно из значений перечисления
     */
    public static TypeOfBarCodeCommand convertFromChar(char appropriateSymbol) {
        switch (Character.toLowerCase(appropriateSymbol)) {
            case 'l':
                return LINK;
            case 'i':
                return INVOKE;
            case 't':
                return TEXT;
            case 's':
                return SQL;
            case 'c':
                return CUSTOM;
            case 'b':
                return BACK_CONTROL;
            default:
                throw new UnsupportedBarCodeOperationException("This symbol doesn't represent QR code command");
        }
    }

    /**
     * Метод позволяет получить символьное представление данного значение представления
     * @return Символьное значение представления
     * @exception UnsupportedBarCodeOperationException Исключение бросается, если для данного значения представления не задано 
     * символьное представление. Скорее всего перечисление модифицировалось, и программист забыл обновить этот (toChar) метод
     */
    public char toChar() {
        switch (this) {
            case BACK_CONTROL:
                return 'b';
            case CUSTOM:
                return 'c';
            case INVOKE:
                return 'i';
            case LINK:
                return 'l';
            case SQL:
                return 's';
            case TEXT:
                return 't';
            default:
                throw new UnsupportedBarCodeOperationException("Unknown command");
        }
    }
        
}