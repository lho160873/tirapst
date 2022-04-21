package pst.arm.server.common.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import pst.arm.client.common.domain.PortletLink;
import pst.arm.client.modules.admin.stat.domain.StatAction;
import pst.arm.client.modules.admin.stat.domain.StatItem;
import pst.arm.server.common.service.CommonService;


/**
 *
 * @author Alexandr Kozhin
 */
public class CommonServiceImpl implements CommonService {

    private MessageSource messageSource;
    private static Logger statLog = Logger.getLogger("StatisticLogger");

    public List<PortletLink> getPortletLinks() {
        try {
            File linkFile = new File(messageSource.getMessage("config.application.resources.linksFile", null, null));
            List<String> contents = FileUtils.readLines(linkFile, "UTF-8");
            List<PortletLink> list = new ArrayList<PortletLink>();
            for (String line : contents) {
                PortletLink pl = new PortletLink();
                pl.setText(line.split(";")[0]);
                if (line.split(";").length > 1 && !line.split(";")[1].trim().isEmpty()) {
                    pl.setHref(line.split(";")[1].trim());
                } else {
                    pl.setHref(null);
                }
                list.add(pl);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the messageSource
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * @param messageSource the messageSource to set
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void stat(String message) {
        statLog.info(message, null);
    }

    public List<StatItem> getStatistic(){
        List<StatItem> list = new ArrayList<StatItem>();
        try {
            File statDir = new File(messageSource.getMessage("config.application.resources.statLogDir", null, null));
            FileFilter fileFilter = new RegexFileFilter(".*\\.log(\\.\\d{4}-\\d{2}){1}");
            File[] files = statDir.listFiles(fileFilter);
            for (int i = 0; i < files.length; i++) {
                list.addAll(parseStatFile(files[i]));
            }

            fileFilter = new SuffixFileFilter(".log");
            files = statDir.listFiles(fileFilter);
            if (1 == files.length) {
                list.addAll(parseStatFile(files[0]));
            }

            return list;
        } catch (Exception e) {
            return null;
        }
    }

    protected List<StatItem> parseStatFile(File statFile) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<StatItem> list = new ArrayList<StatItem>();
        StatItem item = null;
        StatAction action;
        String[] arr;
        try {
            List<String> contents = FileUtils.readLines(statFile, "UTF-8");
            for (String line : contents) {
                arr = line.split(" ");
                if (list.isEmpty() || !list.get(list.size() - 1).getSessionId().equals(arr[3])) {
                    if (!list.isEmpty()) {
                        item.setStartDate(list.get(list.size() - 1).getActions().get(0).getDate());
                        item.setEndDate(list.get(list.size() - 1).getActions().get(list.get(list.size() - 1).getActions().size() - 1).getDate());
                    }

                    item = new StatItem(arr[3], arr[4], arr[6]);
                    list.add(item);
                } else {
                    item = list.get(list.size() - 1);
                }

                String url = (arr[2].equals("PAGE")) ? arr[7] : arr[5];
                String date = arr[0].substring(1) + " " + arr[1].substring(0, arr[1].length() - 1);
                action = new StatAction(date, arr[2], arr[5], url);
                action.setDate(df.parse(date));
                item.getActions().add(action);
            }

            item.setStartDate(list.get(list.size() - 1).getActions().get(0).getDate());
            item.setEndDate(list.get(list.size() - 1).getActions().get(list.get(list.size() - 1).getActions().size() - 1).getDate());
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * Функция возвращает содержание файла с информацией о последних изменениях 
     * имя файла задается в параметре application.config.application.resources.changeLogFile файла с настройками compile-*.prooerties
     */
    @Override
    public String getChangeLog() {
        try {
            String changeLog = FileUtils.readFileToString(new File(messageSource.getMessage("config.application.resources.changeLogFile", null, null)),
                                                            messageSource.getMessage("config.application.resources.changeLogFileEncoding", null, null));
                     
            return changeLog;
        } catch (Exception e) {
            return null;
            //return e.getMessage();
        }
    }
}
