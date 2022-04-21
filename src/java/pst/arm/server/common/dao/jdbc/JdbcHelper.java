package pst.arm.server.common.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class JdbcHelper {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat sdfl = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");

    public static Integer getInteger(Object val) {
        if (val == null) {
            return null;
        }
        //return ((BigDecimal) val).intValue();
        return ((Integer) val).intValue();
    }

    public static Double getDouble(Object val) {
        if (val == null) {
            return null;
        }
        return ((BigDecimal) val).doubleValue();
    }

    public static Long getLong(Object val) {
        if (val == null) {
            return null;
        }
        return ((BigDecimal) val).longValue();
    }

    public static String getStringFromDate(Date date) {
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    public static String getStirngForFiltr(String field, Object val) {
        String result = " ";
        if (val != null) {
            result += field + "='" + String.valueOf(val) + "' ";
        } else {
            result += field + " IS NULL ";

        }
        return result;

    }

    public static String dateFormat(java.util.Date d) {
        String ret = "";
        if (d != null) {

            ret = sdfl.format(d);
        }
        return ret;
    }

    public static String timeFormat(java.util.Date d) {
        String ret = "";
        if (d != null) {

            ret = sdft.format(d);
        }
        return ret;
    }

    public static String shortDateFormat(java.util.Date d) {
        String ret = "";
        if (d != null) {
            ret = sdf.format(d);
        }
        return ret;
    }
}
