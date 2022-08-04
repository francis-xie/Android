
package com.basic.job.utils;

import com.basic.job.logger.TaskLogger;

import java.util.Collection;
import java.util.Map;

/**
 * 普通工具类
 */
public final class CommonUtils {

    private static final String TAG = TaskLogger.getLogTag("CommonUtils");

    private CommonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 类型强转
     *
     * @param object 需要强转的对象
     * @param clazz  需要强转的类型
     * @param <T>
     * @return 类型强转结果
     */
    public static <T> T cast(final Object object, Class<T> clazz) {
        return clazz != null && clazz.isInstance(object) ? (T) object : null;
    }

    /**
     * 类型强转
     *
     * @param object       需要强转的对象
     * @param defaultValue 强转的默认值
     * @param <T>
     * @return 类型强转结果
     */
    public static <T> T cast(Object object, T defaultValue) {
        if (defaultValue == null) {
            return null;
        } else if (object == null) {
            return null;
        } else {
            return defaultValue.getClass() == object.getClass() ? (T) object : defaultValue;
        }
    }

    /**
     * 判断字符串是否为 null 或长度为 0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return true: 为空，false：不为空
     */
    public static <E> boolean isEmpty(final Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param obj Map
     * @return true: 为空，false：不为空
     */
    public static boolean isEmpty(final Map obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 不能为null
     *
     * @param object  对象
     * @param message 错误信息
     * @param <T>
     * @return
     */
    public static <T> T requireNonNull(final T object, final String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 获取集合的大小
     *
     * @param collection 集合
     * @return 集合的大小
     */
    public static <E> int getSize(final Collection<E> collection) {
        return collection != null ? collection.size() : 0;
    }

}
