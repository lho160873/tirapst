package pst.arm.client.common.barcodeservice;

/**
 * Класс представляет перечисление типов действия команд-ссылок и методы для работы с ним
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public enum TypeOfActionOfLinkBarCodeCommand {
    
    /**
     * Действие - создать что-то (документ, карту, ...)
     */
    Create,
    /**
     * Действие - открыть что-то (документ, карту, ...)
     */
    Open,
    /**
     * Действие - удалить что-то (документ, карту, ...)
     */
    Delete;

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
    public static TypeOfActionOfLinkBarCodeCommand convertFromNumberOrString(String value) throws IllegalArgumentException, NullPointerException {
        if (value == null) {
            throw new NullPointerException("value can't be null");
        }
        if (value.equals("0") || value.equals("Create")) {
            return Create;
        } else if (value.equals("1") || value.equals("Open")) {
            return Open;
        } else if (value.equals("2") || value.equals("Delete")) {
            return Delete;
        }
        throw new IllegalArgumentException("Can't convert " + value + " to TypeOfActionOfLinkBarCodeCommand. Correct value is number from 0 to 2 or one of string representation of enumeration values");
    }

    /**
     * Метод позволяет преобразовать заданный объект перечисления к числу
     * @param value Экземпляр, который необходимо преобразовать к числу
     * @return Числовое значение заданного объекта
     */
    public static Integer convertToNumber(TypeOfActionOfLinkBarCodeCommand value) {
        return value.toNumber();
    }
    
}