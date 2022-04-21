package pst.arm.client.common.barcodeservice;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Image;
import jssc.SerialPortException;

/**
 * Интерфейс определяет способ взаимодействия с сервисом по работе с QR-кодами
 * <br/>
 * <br/>Примеры использования:
 * <br/>1) Генерация QR-кода.
 * <br/>{@code 
BarCodeService service = BarCodeServiceImpl.getBarCodeService();
BarCode code = new BarCode(new BackControlBarCodeCommand("ActionBackControl"));
code.addCommand(new CustomBarCodeCommand(TypeOfCustomBarCodeCommand.OpenLastWorkerInFactOperation));
LinkBarCodeCommand link = new LinkBarCodeCommand(TypeOfActionOfLinkBarCodeCommand.Open, TypeOfObjectOfLinkBarCodeCommand.Report, "name");
link.addParameter("param1", "value1");
link.addParameter("param2", "value2");
code.addCommand(link);
Image img = service.generateQRCode(code);
 * }
 * <br/>2) Подготовка к считыванию штрих-коди и исполнению.
 * <br/>{@code 
BarCodeService service = BarCodeServiceImpl.getBarCodeService();
service.startReading("COM2");
}
 * <br/>
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public interface BarCodeService {
    
    /**
     * Метод позволяет сгенерировать изображение QR-кода, содержащего текстовое представление заданных команд
     * @param commands Объект, хранящий список команд и их текстовое представление
     * @return QR-код (изображение)
     */
    Image generateQRCode(BarCode commands);
    
    /**
     * Метод позволяет сгенерировать (с расширенными настройками) изображение QR-кода, содержащего текстовое представление заданных команд 
     * @param commands Объект, хранящий список команд и их текстовое представление
     * @param size Размер изображения (не QR-кода)
     * @param errorCorrectionLevel Уровень коррекции ошибок у QR-кода
     * @return QR-код (изображение)
     */
    Image generateQRCode(BarCode commands, int size, ErrorCorrectionLevel errorCorrectionLevel);
    
    /**
     * Метод позволяет открыть соединение по заданному порту и ожидать приема данных (зарегистрировать событие приема данных)
     * @param port Название порта, который необходимо открыть
     * @throws SerialPortException Исключение бросается, если невозможно открыть заданный порт или инициализировать обработку события приема данных
     */
    void startReading(String port) throws SerialPortException;
    
    /**
     * Метод позволяет открыть соединение по заданному порту, настроить его и ожидать приема данных (зарегистрировать слушателя события приема данных)
     * @param port Название порта, который необходимо открыть
     * @param boudRate Скорость передачи данных
     * @param dataBits Количество битов данных
     * @param stopBits Количество стоп-битов
     * @param parity Четность
     * @param mask Набор (побитовое сложение) масок определяющий, какие события требуется отлавливать
     * @param setRTS Начальное состояние линии RTS
     * @param setDTR Начальное состояние линии DTR
     * @throws SerialPortException Исключение бросается, если невозможно открыть заданный порт или инициализировать обработку события приема данных
     */
    void startReading(String port, int boudRate, int dataBits, int stopBits, int parity, int mask, Boolean setRTS, Boolean setDTR) throws SerialPortException;
    
    /**
     * Метод возвращает значение - было ли инициализировано (подготовлено к работе) устройство и класс для считывания штрих-кодов
     * @return true, если порт был открыт и зарегистрирован обработчик события приема данных; false - иначе
     */
    Boolean isInitializedReading();
    
    /**
     * Метод удаляет зарегистрированный обработчик события приема данных и закрывает порт
     */
    void stopReading();
    
    /**
     * Метод позволяет получить существующие COM-порты (последовательные serial порты)
     * @return Массив названий существующих портов
     */
    String[] getAvailablePorts();
    
}