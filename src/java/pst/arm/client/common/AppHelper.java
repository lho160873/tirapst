package pst.arm.client.common;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Window;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.common.lang.BaseConstants;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.config.Configuration;

import java.util.Date;
import pst.arm.client.modules.admin.lang.AdminConstants;

/**
 * 
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since 0.0.1
 */
public class AppHelper {

    private volatile static AppHelper instance;
    private Configuration configuration = (Configuration) GWT.create(Configuration.class);
    public static BaseConstants constants = (BaseConstants) GWT.create(BaseConstants.class);
    public static CommonConstants commonConstants = (CommonConstants) GWT.create(CommonConstants.class);
    private TimeZone defaultTimeZone;
    private static Date today = new Date();
    public static AdminConstants admConstants = (AdminConstants) GWT.create(AdminConstants.class);

    private AppHelper() {
    }

    public static AppHelper getInstance() {
        if (instance == null) {
            synchronized (AppHelper.class) {
                if (instance == null) {
                    instance = new AppHelper();
                }
            }
        }
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String baseUrl() {
        String host = Window.Location.getHost();
        if (host.startsWith("http://") == false) {
            host = "http://" + host;
        }

        return host;
    }

    public String debugUrl() {
        if (!GWT.isProdMode()) {
            return "gwt.codesvr=127.0.0.1:9997&";
        } else {
            return "";
        }
    }

    public String getConfigProperty(String key) {
        Dictionary dictionary = Dictionary.getDictionary("Vars");
        return dictionary.get(key);
    }

    /**
     * Process value of owner name based on availability of User property and
     * owner archive id.
     * Displayed value depends on user present in Owner object.
     * If it's absent, then we display archive name with user id. e.q. "ARCHIVE (id)"
     * If user doesn't have real name then login will be returned.
     * @param owner
     * @return String owner name
     */
    /*static public String determineOwnerName(Owner owner) {
        String display = "";
        if (owner.getUser() != null) {
            display = owner.getUser().getFioShort();
            if (display == null) {
                display = owner.getUser().getUserName();
            }
        } else {
            String archiveName = getArchiveShortName(owner.getArchiveId());
            display = archiveName + " (" + owner.getId() + ")";
        }
        return display;
    }*/

    /**
     * Return archive short name by archive id.
     * @param archiveId
     * @return 
     */
    static public String getArchiveShortName(Integer archiveId) {
        String archiveName = archiveId.toString();
        try {
            archiveName = (String) constants.archiveShortname().get("archive.shortname." + archiveId.toString());
        } catch (Exception e) {
        }
        return archiveName;
    }

    public TimeZone getDefaultTimeZone() {
        if (defaultTimeZone == null) {
            defaultTimeZone = TimeZone.createTimeZone(today.getTimezoneOffset());
        }
        return defaultTimeZone;
    }

    public String dateFormat(Date d, String format) {
        return DateTimeFormat.getFormat(format).format(d, getDefaultTimeZone());
    }

    private long getDefaultTimeZoneOffset(Date d) {
        return getDefaultTimeZone().getOffset(d) * 60000;
    }

    public String timeFormat(Date d) {
        String ret = "";
        if (d != null) {
            
            ret = DateTimeFormat.getFormat("HH:mm").format(d);
        }
        return ret;
    }
    
    public String dateFormat(Date d) {
        String ret = "";
        if (d != null) {
            //Date newDate = new Date(d.getTime() - getDefaultTimeZoneOffset(d)); old david's hardcode
            ret = DateTimeFormat.getFormat("dd.MM.yyyy  HH:mm").format(d);
        }
        return ret;
    }

    public String shortDateFormat(Date d) {
        String ret = "";
        if (d != null) {
            ret = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
        }
        return ret;
    }

    public String getDateFormat() {
        return commonConstants.dateOnlyDate();
    }

    public static Date stringToDate(String date) {
        try {
            Date parse = DateTimeFormat.getFormat("dd.MM.yyyy").parse(date);
            return parse;
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString(Date d) {
        String ret = null;
        if (d != null) {
            ret = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
        }
        return ret;
    }

    public static void showMsgRpcServiceError(Throwable caught) {
        showMsgRpcServiceError(caught, "");
    }

    public static void showMsgRpcServiceError(Throwable caught, String strError) {
        Boolean isErrAskAdmin = true;
        //Если в сообщении была точка удаляем ее
        String caughtMessage = caught.getMessage().trim();
        Integer len = caughtMessage.length();
        if (caughtMessage.substring(len - 1,len) == ".") {
            caughtMessage = caughtMessage.substring(0, len - 1);
        }
                
        String message = strError + ( strError.isEmpty() ? " " :" (") + caughtMessage+( strError.isEmpty() ? "." :").");
        //if (Boolean.valueOf(ConfigurationManager.getProperty("showErrHint"))) {
        //надо что-то придумать с ролью - показывать ли текст ошибки, 
        //не хочеться эту роль назначать для каждого модуля, хочется сделать глобальную роль для всех модулей - роль администратора,
        //пока не заню как, буду показывать тем пользователям которые имеют доступ к модулю adminmsg 
        if (caught instanceof RpcServiceException) {
                String info = ((RpcServiceException) caught).getInfo();
                String hint = ((RpcServiceException) caught).getHint();
                //MessageBox.info("hint",hint,null);    
                 //MessageBox.info("info",info,null);    
                if ((hint != null) && (!hint.isEmpty())) {
                    String mess = "";
                    if (((RpcServiceException) caught).getErrCode() != null) {
                        //MessageBox.info("","getErrCode = "+((RpcServiceException) caught).getErrCode().toString(),null);
                        switch (((RpcServiceException) caught).getErrCode()) {
                            case 1:
                                //нарушено ограничение уникальности
                                mess = "Запись с таким кодом уже существует";
                                if (info.equals("UN_NUMBER")) {                             //для некоторых таблиц может потребоваться свое сообщение
                                    mess = "Такое значение UN-номера уже есть в Системе.";  //уникальное сообщение
                                }
                                else if (info.equals("END_POINT_FREIGHT")) {                                     
                                     mess = "Для данного пункта выгрузки уже определен этот груз. Отредактируйте его количество.";  //уникальное сообщение
                                }
                                break;
                            case 51:
                                //блокировка по времени при ожидании ресурса
                                mess = "Блокировка по времени при ожидании ресурса.";
                                break;
                            case 61:
                                //произведен откат транзакции при возникновении тупика, или взаимоблокировки транзакций
                                mess = "Произведён откат транзакции.";
                                break;
                             case 1400:
                                //запрещенна операция над курсором
                                mess = "Не все поля введены корректно.";
                                break;
                            case 1001:
                                //запрещенна операция над курсором
                                mess = "Запрещённая операция над курсором.";
                                break;
                            case 1012:
                                //отсутствует соединение с Oracle
                                mess = "Отсутствует связь с базой данных.";
                                break;
                            case 1017:
                                //неверное имя/пароль пользователя
                                mess = "Неверное имя/пароль пользователя.";
                                break;
                            case 1403:
                                //данные не найдены
                                mess = "Данные не найдены.";
                                break;
                            case 1422:
                                //оператор SELECT...INTO возвращае более одной строки
                                mess = "Оператор SELECT...INTO возвращае более одной строки.";
                                break;
                            case 1476:
                                //деление на ноль о_О
                                mess = "Деление на ноль.";
                                break;
                            case 1722:
                                //неудачна попытка преобразования к типу NUMBER; например 1А является запрещённым значением
                                mess = "Данный тип данных невозможно представить ввиде числа.";
                                break;
                            case 515:
                                //нарушено ограничение целостности существуют связанные записи
                                mess = "Запись не удалось удалить, так как существуют связанные данные.";
                                break;
                            case 547:
                                //нарушено ограничение целостности существуют связанные записи
                                mess = "Запись не удалось удалить, так как существуют связанные данные.";
                                break;
                            case 8152:
                                //нарушено ограничение целостности существуют связанные записи
                                mess = "Запись не удалось удалить, так как существуют связанные данные.";
                                break;
                            case 2290:
                                //нарушено ограничение целостности существуют связанные записи
                                mess = "Нарушено ограничение целостности.";
                                break;
                            case 6500:
                                //внутренняя ошибка PL/SQL; устанавливается если PL/SQL недостаточно памяти
                                mess = "Базе данных требуется больше памяти.";
                                break;
                            case 6501:
                                //внутренняя ошибка PL/SQL
                                mess = "Внутреняя ошибка базы данных.";
                                break;
                            case 6502:
                                //ошибка усечения, арифметическая ошибка или ошибка преобразования
                                mess = "Ошибка усечения, арифметическая ошибка или ошибка преобразования.";
                                break;
                            case 6504:
                                //базовая курсорная переменная и курсорная переменная PL/SQL имеют несовместимые типы строк
                                mess = "Базовая курсорная переменная и курсорная переменная PL/SQL имеют несовместимые типы строк.";
                                break;
                            case 6511:
                                //попытка открыть курсор который уже открыт
                                mess = "Попытка открыть курсор который уже открыт.";
                                break;
                            case 6530:
                                //попытка присвоить значение атрибуту NULL-объекта
                                mess = "Попытка присвоить отсутствующее значение атрибуту.";
                                break;
                            case 6531:
                                //попытка использовать для таблицы или изменяемого массива PL/SQL типа NULL метод сборных конструкций, отличный от EXISTS
                                mess = "Попытка использовать для таблицы или изменяемого массива PL/SQL типа NULL метод сборных конструкций, отличный от EXISTS.";
                                break;
                            case 6532:
                                //ссылка на индекс таблицы или изменяемого массива, лежащий вне объявленного диапазона (например -1)
                                mess = "Ошибка ссылки по адресу";
                                break;
                            case 6533:
                                //ссылка на индекс таблицы или изменяемого массива, больший, чем число элементов данной сборной конструкции
                                mess = "Ссылка на индекс таблицы или изменяемого массива, больший, чем число элементов данной сборной конструкции.";
                                break;
                            case 947:
                                mess = "Ошибка в синтаксисе SQL выражения, не хватает значений для данных.";
                                break;
                            default:
                                mess = message;
                    }
                    } else if (hint.contains("Конфликт инструкции UPDATE с ограничением REFERENCE")) {
                        mess = "Этот код нельзя сменить - имеются зависимые записи в других таблицах.";
                    } else if (hint.contains("XPKDEPART_TYPE")) {
                        mess = "Такая запись уже есть.";
                    } else if (hint.contains("Не удается вставить повторяющийся ключ в объект")) {   
                        if ("Ошибка сохранения.".equals(message.trim())||"Error save.".equals(message.trim())) {
                            mess =  "Запись с таким кодом уже существует.";
                            if (info.contains("NORM_COST_WORK")) {
                                mess =  "Для данного предприятия на данную дату норматив уже есть.";
                            }
                        } else {
                            mess =  message;
                        }                        
                    } else if (hint.contains("Для данного типа документов путь к файлам уже определен.")) {
                        mess = "Для данного типа документов путь к файлам уже определен.";
                    } else if (hint.contains("Такой план уже сформирован.")) {                        
                        mess = "Такой план уже есть в Журнале. Повторное создание невозможно!";
                        isErrAskAdmin = false;
                    } else if (hint.contains("USERS_EXISTSLOGIN")) {
                        mess = admConstants.existLoginWarn();
                    }                      
                    else {
                        mess = message;
                    }

                    message = (!mess.isEmpty()) ? mess : message;
                    //             message += " HINT: " + hint;
                    // если админ, то дописываем ему полное сообщение об ошибке
                    if (ConfigurationManager.isModuleAvailable("adminmsg")) {
                      
                        message += "<br>HINT: " + hint;
                    }
                    else
                    {
                       if (isErrAskAdmin)
                       {
                        message += commonConstants.errAskAdmin(); 
                       }
                    }
                }
        }
        MessageBox.alert(commonConstants.error(), message, null).setIcon(MessageBox.ERROR);
    }


    
    public String getStringNotNull(String val) {
        if (val != null) {
            return val;
        } else {
            return "";
        }
    }
}
