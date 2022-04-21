package pst.arm.server.modules.aiscontracts.util;

import java.math.BigDecimal;
import pst.arm.client.modules.aiscontracts.domain.FieldType;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class FieldsUtil {

    public static Object getValue(FieldType type, String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
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
                        value = Boolean.valueOf(stringValue);
                        break;
                    case Integer:
                        value = Integer.parseInt(stringValue.replaceAll("\\D", ""));
                        break;
                    case Double:
                        value = BigDecimal.valueOf(Double.parseDouble(stringValue.replaceAll("\\D", "")));
                        break;
                    case Date:
                        value = stringValue.substring(0, 10);
                        break;
                }
                return value;
            } catch (Exception e) {
                System.out.println("Error casting value");
                e.printStackTrace(System.out);
            }
        }
        return null;
    }
}
