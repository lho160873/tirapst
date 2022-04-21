package pst.arm.client.common.barcodeservice;

import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.*;
import com.google.zxing.qrcode.decoder.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.logging.*;
import jssc.*;

/**
 * Класс позволяет работать с штрих-кодами и командами, связанными с ними 
 * (генерировать QR-коды, считывать QR-коды и исполнять команды, заложенные в них).
 * Класс реализвет паттерн "Одиночка" ("Singleton")
 * @author Mikhail Zakharov <zmish93@ya.ru>
 * @since 0.0.1
 */
public class BarCodeServiceImpl implements BarCodeService, SerialPortEventListener {

    /**
     * Поле содержит значение - через какой промежуток времени в милисекундах после безуспешной попытки получить значение с COM-порта, 
     * сервис будет генерировать исключение
     */
    private final int timeoutInMs = 5000;
    /**
     * Поле содержит единственный экземпляр данного класса
     */
    private static BarCodeServiceImpl instance;
    /**
     * Поле содержит порт, с которым взаимодействует сервис
     */
    private SerialPort serialPort;
    
    /**
     * Конструктор по умолчанию
     */
    private BarCodeServiceImpl() {
    }
    
    /**
     * Метод позволяет сгенерировать изображение QR-кода, содержащего текстовое представление заданных команд. 
     * Стандартный размер изображения равен 45. Стандартный уровень коррекции ошибок у QR-кода является LOW
     * @param commands Объект, хранящий список команд и их текстовое представление
     * @return QR-код (изображение). В случае любой ошибки будет возвращен null
     */
    @Override
    public Image generateQRCode(BarCode commands) {
        String content = commands.getStringRepresentation();
        try {
            return createQRImage(content);
        }
        catch (Exception exception) {
            Logger.getLogger(BarCodeServiceImpl.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }

    /**
     * Метод позволяет сгенерировать (с расширенными настройками) изображение QR-кода, содержащего текстовое представление заданных команд 
     * @param commands Объект, хранящий список команд и их текстовое представление
     * @param size Размер изображения (не QR-кода)
     * @param errorCorrectionLevel Уровень коррекции ошибок у QR-кода
     * @return QR-код (изображение)
     */
    @Override
    public Image generateQRCode(BarCode commands, int size, ErrorCorrectionLevel errorCorrectionLevel) {
        String content = commands.getStringRepresentation();
        try {
            return createQRImage(content, size, errorCorrectionLevel);
        }
        catch (Exception exception) {
            Logger.getLogger(BarCodeServiceImpl.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }
        
    /**
     * Метод позволяет открыть соединение по заданному порту и ожидать приема данных (зарегистрировать событие приема данных). 
     * Если до этого соединение уже открывалось, его необходимо закрыть (метод stopReading)
     * @param port Название порта, который необходимо открыть
     * @throws SerialPortException Исключение бросается, если предыдущее соединение не было закрыто, если невозможно открыть заданный порт 
     * или инициализировать обработку события приема данных
     */
    @Override
    public void startReading(String port) throws SerialPortException {
        if (serialPort != null) {
            throw new SerialPortException(port, "start", "Port is already open");
        }
        try {
            serialPort = new SerialPort(port);
            serialPort.openPort();
            serialPort.setParams(9600, 8, 1, 0);
            int mask = SerialPort.MASK_RXCHAR/* + SerialPort.MASK_CTS + SerialPort.MASK_DSR + SerialPort.MASK_RLSD*/;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(this);
        } catch (SerialPortException exception) {
            stopReading();
            throw exception;
        }
    }

    /**
     * Метод позволяет открыть соединение по заданному порту, настроить его и ожидать приема данных 
     * (зарегистрировать слушателя события приема данных).
     *  Если до этого соединение уже открывалось, его необходимо закрыть (метод stopReading)
     * @param port Название порта, который необходимо открыть
     * @param boudRate Скорость передачи данных
     * @param dataBits Количество битов данных
     * @param stopBits Количество стоп-битов
     * @param parity Четность
     * @param mask Набор (побитовое сложение) масок определяющий, какие события требуется отлавливать
     * @param setRTS Начальное состояние линии RTS
     * @param setDTR Начальное состояние линии DTR
     * @throws SerialPortException сключение бросается, если предыдущее соединение не было закрыто, если невозможно открыть заданный порт или 
     * инициализировать обработку события приема данных
     */
    @Override
    public void startReading(String port, int boudRate, int dataBits, int stopBits, 
            int parity, int mask, Boolean setRTS, Boolean setDTR) throws SerialPortException {
        if (serialPort != null) {
            throw new SerialPortException(port, "start", "Port is already open");
        }
        try {
            serialPort = new SerialPort(port);
            serialPort.openPort();
            serialPort.setParams(boudRate, dataBits, stopBits, parity, setRTS, setDTR);
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(this);
        } catch (SerialPortException exception) {
            stopReading();
            throw exception;
        }
    }

    /**
     * Метод возвращает значение - был ли инициализирован (подготовлен к работе) класс для считывания штрих-кодов
     * @return true, если порт был открыт и зарегистрирован обработчик события приема данных; false - иначе
     */
    @Override
    public Boolean isInitializedReading() {
        return serialPort != null && serialPort.isOpened();
    }
    
    /**
     * Метод удаляет зарегистрированный обработчик события приема данных и закрывает порт
     */
    @Override
    public void stopReading() {
        if (serialPort != null) {
            if (serialPort.isOpened()) {
                try {
                    serialPort.closePort();
                } catch (SerialPortException exception) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Can't close port: {0}", exception.getMessage());
                    try {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Trying to remove event handler");
                        serialPort.removeEventListener();
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Done! Event handler removed");
                    } catch (SerialPortException newException) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Can't remove event listener: {0}", newException.getMessage());
                    }
                }
            }
            serialPort = null;
        }
    }
    
    /**
     * Метод позволяет получить существующие COM-порты (последовательные serial порты)
     * @return Массив названий существующих портов
     */
    @Override
    public String[] getAvailablePorts() {
        return SerialPortList.getPortNames();
    }
    
    /**
     * Метод позволяющий получить экземпляр данного класса
     * @return Экземпляр данного класса
     */
    public static BarCodeService getBarCodeService() {
        if (instance == null) {
            instance = new BarCodeServiceImpl();
        }
        return instance;
    }

    /**
     * Обработчик (слушатель) события приема данных с COM-порта. Метод считывает данные с порта, очищает порт, пытается распарсить принятые данные
     *  и выполнить переданные таким образом команды
     * @param serialPortEvent Аргумент события (содержит информацию о событии, а также принятые данные)
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        try {
            BarCode code = BarCode.createExtendedBarCode(serialPort.readString(serialPortEvent.getEventValue(), timeoutInMs));
            serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
            code.execute();
        } catch (SerialPortException ex) {
            Logger.getLogger(BarCodeServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SerialPortTimeoutException ex) {
            Logger.getLogger(BarCodeServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Метод позволяет создать изображение QR-кода с заданными характеристиками, и представляющее заданный текст
     * @param qrCodeText Текст сообщения, которое QR-код должен предсатвить
     * @param size Размер изображения
     * @param level Уровень коррекции ошибок QR-кода
     * @return Изображение QR-кода
     * @throws WriterException Исключение бросается, если по каким-то причинам не удалось закодировать заданный текст в QR-коде
     */
    private Image createQRImage(String qrCodeText, int size, ErrorCorrectionLevel level) throws WriterException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        HashMap hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, level);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }
    
    /**
     * Метод позволяет создать изображение QR-кода,представляющее заданный текст
     * @param qrCodeText Текст сообщения, которое QR-код должен предсатвить
     * @return Изображение QR-кода
     * @throws WriterException Исключение бросается, если по каким-то причинам не удалось закодировать заданный текст в QR-коде
     */
    private Image createQRImage(String qrCodeText) throws WriterException {
        return createQRImage(qrCodeText, 45, ErrorCorrectionLevel.L);
    }
    
}