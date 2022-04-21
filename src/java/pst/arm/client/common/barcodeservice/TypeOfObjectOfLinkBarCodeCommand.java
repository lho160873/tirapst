package pst.arm.client.common.barcodeservice;

/**
 * Класс представляет перечисление типов объектов, на которые могут указывать команды-ссылки, и методы для работы с ним
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public enum TypeOfObjectOfLinkBarCodeCommand {
    
    /**
     * Команда-ссылка указывает на журнал
     */
    Journal,
    /**
     * Команда-ссылка указывает на справочник
     */
    Reference,
    /**
     * Команда-ссылка указывает на документ
     */
    Document,
    /**
     * Команда-ссылка указывает на карточку
     */
    Card,
    /**
     * Команда-ссылка указывает на отчет
     */
    Report;

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
    public static TypeOfObjectOfLinkBarCodeCommand convertFromNumberOrString(String value) throws IllegalArgumentException, NullPointerException {
        if (value == null) {
            throw new NullPointerException("value can't be null");
        }
        if (value.equals("0") || value.equals("Journal")) {
            return Journal;
        } else if (value.equals("1") || value.equals("Reference")) {
            return Reference;
        } else if (value.equals("2") || value.equals("Document")) {
            return Document;
        } else if (value.equals("3") || value.equals("Card")) {
            return Card;
        } else if (value.equals("4") || value.equals("Report")) {
            return Report;
        }
        throw new IllegalArgumentException("Can't convert " + value + " to TypeOfObjectOfLinkBarCodeCommand. Correct value is number from 0 to 4 or one of string representation of enumeration values");
    }

    /**
     * Метод позволяет преобразовать заданный объект перечисления к числу
     * @param value Экземпляр, который необходимо преобразовать к числу
     * @return Числовое значение заданного объекта
     */
    public static Integer convertToNumber(TypeOfObjectOfLinkBarCodeCommand value) {
        return value.toNumber();
    }
        
}