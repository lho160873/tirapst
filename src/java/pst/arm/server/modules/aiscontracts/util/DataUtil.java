package pst.arm.server.modules.aiscontracts.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.apache.log4j.Logger;
import pst.arm.client.modules.aiscontracts.domain.Field;
import pst.arm.client.modules.aiscontracts.domain.FieldType;

/**
 * Created by akozhin on 13.12.14.
 */
public class DataUtil {

    private static Logger log = Logger.getLogger("DataUtil");

    private static final String WRONG_DATE = "01.01.0001 0:00:00";
    private static final String DEFAULT_DATE = "01.01.1900 0:00:00";
    private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");// H:mm:ss

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

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static Object getCastedValue(FieldType type, String stringValue) {
        if (stringValue != null && !stringValue.trim().isEmpty()) {
            try {
                Object value = null;
                switch (type) {
                    case String:
                        value = stringValue;
                        break;
                    case Boolean:
                        if (stringValue != null && stringValue.equalsIgnoreCase("Да")) {
                            value = Boolean.TRUE;
                        } else {
                            value = Boolean.valueOf(stringValue);
                        }
                        break;
                    case BooleanInt:
                        value = Integer.parseInt(stringValue);
                        break;
                    case Integer:
                        value = Integer.parseInt(stringValue);
                        break;
                    case Double:
                        value = Double.parseDouble(stringValue);
                        break;
                    case Date:
                        value = df.parse(stringValue);
                        break;
                }
                return value;
            } catch (Exception e) {
                log.warn("Error casting value " + stringValue + " to " + type.name());
            }
        }
        return null;
    }

    public static String prepareValue(Field field, String value) {
        if (field != null && isNotBlank(value)) {
            switch (field.getType()) {
                case String:
                    if (value.length() > field.getMaxLength()) {
                        value = value.substring(0, field.getMaxLength());
                    }
                    break;
                case Double:
                    value = value.replaceAll("[^\\d,]", "").replaceAll(",", ".");
                    break;
                case BooleanInt:
                    value = value.equalsIgnoreCase("true") ? "1" : "0";
                    break;
                case Date:
                    if (value.equals(WRONG_DATE)) {
                        value = DEFAULT_DATE;
                    }
                    break;
            }
            return value;
        }
        return null;
    }
}
