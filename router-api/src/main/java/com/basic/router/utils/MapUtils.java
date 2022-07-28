
package com.basic.router.utils;

import java.util.Map;

/**
 * Map工具类
 */
public final class MapUtils {

    private MapUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map  the map to check, may be null
     * @return true if non-null and non-empty
     */
    public static boolean isNotEmpty(final Map<?,?> map) {
        return !isEmpty(map);
    }

    /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map  the map to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }
}
