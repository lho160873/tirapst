package pst.arm.client.common;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class DataUtil {

    /**
     * Compare two objects on equal with null check
     *
     * @param <X> Type of comparing objects (auto)
     * @param left First object to compare
     * @param right Second object to compare
     * @return True if object are equal or both equal null
     */
    public static <X extends Object> Boolean compare(X left, X right) {
        return (left == null ? right == null : left.equals(right));
    }

    public static Integer signToInteger(boolean sign) {
        return (sign) ? 1 : 0;
    }

    public static String getValueOrNull(String v) {
        if (v.isEmpty()) {
            String nullVal = null;
            return nullVal;
        } else {
            return v;
        }
    }

    public static String timeFormat(Date d) {
        String ret = "";
        if (d != null) {

            ret = DateTimeFormat.getFormat("HH:mm").format(d);
        }
        return ret;
    }

    public static String dateFormat(Date d) {
        String ret = "";
        if (d != null) {
            //Date newDate = new Date(d.getTime() - getDefaultTimeZoneOffset(d)); old david's hardcode
            ret = DateTimeFormat.getFormat("dd.MM.yyyy  HH:mm").format(d);
        }
        return ret;
    }

    public static String shortDateFormat(Date d) {
        String ret = "";
        if (d != null) {
            ret = DateTimeFormat.getFormat("dd.MM.yyyy").format(d);
        }
        return ret;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
