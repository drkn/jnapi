package jnapi.utils;

import java.util.Collection;

/**
 * Collections related utils
 *
 * @author Maciej Dragan
 */
public class CollectionsUtils {

    /**
     * Join collection using coma delimiter
     *
     * @param collection A collection to join
     * @return Joined collection as string
     */
    public static String join(Collection<?> collection) {
        return join(collection, ",");
    }

    /**
     * Join collection using custom delimiter
     *
     * @param collection A collection to join
     * @param delimiter  Custom join delimiter
     * @return Joined collection as string
     */
    public static String join(Collection<?> collection, String delimiter) {
        if (collection == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Object element : collection) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            builder.append(element != null ? element.toString() : "");
        }
        return builder.toString();
    }

}
