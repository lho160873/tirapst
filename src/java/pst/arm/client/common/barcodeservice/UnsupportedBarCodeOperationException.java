package pst.arm.client.common.barcodeservice;

/**
 * Класс представляет исключение, что некоторая операция, связанная с работой со штрих-кодами и командами для них, не поддерживается. 
 * Класс создан для реализации строгой типизации исключений
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class UnsupportedBarCodeOperationException extends UnsupportedOperationException {

    /**
     * Конструктор позволяет создать экземпляр исключения без указания сообщения об ошибке
     */
    public UnsupportedBarCodeOperationException() {
    }

    /**
     * Конструктор позволяет создать экземпляр исключения с указанием сообщения об ошибке
     * @param msg Подробности о произошедшей ошибке
     */
    public UnsupportedBarCodeOperationException(String msg) {
        super(msg);
    }
}