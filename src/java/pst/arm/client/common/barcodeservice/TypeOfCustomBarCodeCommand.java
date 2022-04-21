package pst.arm.client.common.barcodeservice;

/**
 * Класс представляет перечисление типов пользовательских команд и методы для работы с ним
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public enum TypeOfCustomBarCodeCommand {
    
    /**
     * Редактирование фактической операции
     */
    InsertFactOperationInJob,
    /**
     * Редактирование исполнителя операции
     */
    InsertWorkerInFactOperation,
    /**
     * Редактирование последней фактической операции
     */
    OpenLastFactOperationInJob,
    /**
     * Редактирование исполнителя последней фактической операции
     */
    OpenLastWorkerInFactOperation;

    /**
     * Метод позволяет получить числовое представление значения данного перечисления
     * @return Числовое представление данного объекта
     */
    public Integer toNumber() {
        return this.ordinal();
    }
    
    /**
     * Метод преобразует строку текста или строку, содержащую число, к данному перечислению
     * @param value Текстовое или числовое значение, которое необходимо преобразовать к данному перечислению. Допустимые значения: 
     * порядковый номер (в виде строки) значения перечисления или значение перечисления (в виде строки)
     * @return Значение перечисления (к которому была преобразована строка)
     * @throws IllegalArgumentException Исключение бросается, если передана некорректная строка (такого значения перечисления нет)
     * @throws NullPointerException Исключение бросается, если переданный аргумент равен null
     */
    public static TypeOfCustomBarCodeCommand convertFromNumberOrString(String value) throws IllegalArgumentException, NullPointerException {
        if (value == null) {
            throw new NullPointerException("value can't be null");
        }
        if (value.equals("0") || value.equals("InsertFactOperationInJob")) {
            return InsertFactOperationInJob;
        } else if (value.equals("1") || value.equals("InsertWorkerInFactOperation")) {
            return InsertWorkerInFactOperation;
        } else if (value.equals("2") || value.equals("OpenLastFactOperationInJob")) {
            return OpenLastFactOperationInJob;
        } else if (value.equals("3") || value.equals("OpenLastWorkerInFactOperation")) {
            return OpenLastWorkerInFactOperation;
        }
        throw new IllegalArgumentException("Can't convert " + value + " to TypeOfCustomBarCodeCommand. Correct value is number from 0 to 3 or one of string representation of enumeration values");
    }

    /**
     * Метод позволяет преобразовать заданный объект перечисления к числу
     * @param value Экземпляр, который необходимо преобразовать к числу
     * @return Числовое значение заданного объекта
     */
    public static Integer convertToNumber(TypeOfCustomBarCodeCommand value) {
        return value.toNumber();
    }
        
}