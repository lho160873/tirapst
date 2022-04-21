package pst.arm.server.utils;

import java.util.Collection;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class ServerUtil {

    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <T> boolean isNotNull(T t) {
        return t != null;
    }
}
