package pst.arm.server.common.reports.conditionbased;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Artem Vorontsov
 */
public class BaseExtractor {

    protected static ResourceBundle res;
    protected static ResourceBundle res2;

    static {
        res2 = ResourceBundle.getBundle("application");
        res = ResourceBundle.getBundle("/modules/reportgenerator/reportgenerator",
                new Locale(res2.getString("config.defaultLang"), res2.getString("config.defaultCountry")));
    }
    
    public BaseExtractor() {
     }

    public Report generateReport() {
        return null;
    }

    public ReportKpTariffs generateReportKpTariffs() {
        return null;
    }
    
    protected Report postProcessData() {
        return null;
    }

    protected String condition2text() {
        return "";
    }

    public static String getRes(String resId) {
        return res.getString(resId);
    }
}
