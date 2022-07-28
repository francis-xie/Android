package com.basic.http2.cache.key;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 将Object转化为String的工具类
 * @date 2018/4/7 下午7:01
 */
public final class Strings {

    private static IObjectSerializer sISerializer;

    private Strings() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置Object转化为String的序列化器
     *
     * @param sISerializer Object转化为String的序列化器
     */
    public static void setISerializer(@NonNull IObjectSerializer sISerializer) {
        Strings.sISerializer = sISerializer;
    }

    /**
     * Object转化为String
     *
     * @param obj 转化对象
     * @return 转化结果
     */
    public static String toString(Object obj) {
        if (sISerializer == null) {
            sISerializer = new DefaultObjectSerializer();
        }
        return sISerializer.toString(obj);
    }

    /**
     * 内部方法，默认实现方式
     *
     * @param obj 对象
     * @return 转化结果
     */
    static String _toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof CharSequence) {
            return '"' + printableToString(obj.toString()) + '"';
        }

        Class<?> cls = obj.getClass();
        if (Byte.class == cls) {
            return byteToString((Byte) obj);
        }

        if (cls.isArray()) {
            return arrayToString(cls.getComponentType(), obj);
        }
        return obj.toString();
    }

    private static String printableToString(String string) {
        int length = string.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ) {
            int codePoint = string.codePointAt(i);
            switch (Character.getType(codePoint)) {
                case Character.CONTROL:
                case Character.FORMAT:
                case Character.PRIVATE_USE:
                case Character.SURROGATE:
                case Character.UNASSIGNED:
                    switch (codePoint) {
                        case '\n':
                            builder.append("\\n");
                            break;
                        case '\r':
                            builder.append("\\r");
                            break;
                        case '\t':
                            builder.append("\\t");
                            break;
                        case '\f':
                            builder.append("\\f");
                            break;
                        case '\b':
                            builder.append("\\b");
                            break;
                        default:
                            builder.append("\\u").append(String.format("%04x", codePoint).toUpperCase(Locale.US));
                            break;
                    }
                    break;
                default:
                    builder.append(Character.toChars(codePoint));
                    break;
            }
            i += Character.charCount(codePoint);
        }
        return builder.toString();
    }

    private static String arrayToString(Class<?> cls, Object obj) {
        if (byte.class == cls) {
            return byteArrayToString((byte[]) obj);
        }
        if (short.class == cls) {
            return Arrays.toString((short[]) obj);
        }
        if (char.class == cls) {
            return Arrays.toString((char[]) obj);
        }
        if (int.class == cls) {
            return Arrays.toString((int[]) obj);
        }
        if (long.class == cls) {
            return Arrays.toString((long[]) obj);
        }
        if (float.class == cls) {
            return Arrays.toString((float[]) obj);
        }
        if (double.class == cls) {
            return Arrays.toString((double[]) obj);
        }
        if (boolean.class == cls) {
            return Arrays.toString((boolean[]) obj);
        }
        return arrayToString((Object[]) obj);
    }

    /**
     * A more human-friendly version of Arrays#toString(byte[]) that uses hex representation.
     */
    private static String byteArrayToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(byteToString(bytes[i]));
        }
        return builder.append(']').toString();
    }

    private static String byteToString(Byte b) {
        if (b == null) {
            return "null";
        }
        return "0x" + String.format("%02x", b).toUpperCase(Locale.US);
    }

    private static String arrayToString(Object[] array) {
        StringBuilder buf = new StringBuilder();
        arrayToString(array, buf, new HashSet<Object[]>());
        return buf.toString();
    }

    private static void arrayToString(Object[] array, StringBuilder builder, Set<Object[]> seen) {
        if (array == null) {
            builder.append("null");
            return;
        }

        seen.add(array);
        builder.append('[');
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            Object element = array[i];
            if (element == null) {
                builder.append("null");
            } else {
                Class elementClass = element.getClass();
                if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
                    Object[] arrayElement = (Object[]) element;
                    if (seen.contains(arrayElement)) {
                        builder.append("[...]");
                    } else {
                        arrayToString(arrayElement, builder, seen);
                    }
                } else {
                    builder.append(_toString(element));
                }
            }
        }
        builder.append(']');
        seen.remove(array);
    }
}
