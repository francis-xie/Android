package com.basic.http.utils;

import java.util.Objects;

public class StringUtils {

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Object object) {
        if (null != object) {
            return isEmpty(String.valueOf(object));
        }
        return true;
    }

    public static String join(String delimiter, Iterable<? extends String> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : elements) {
            stringBuilder.append(value);
            stringBuilder.append(delimiter);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            String[] var2 = values;
            int var3 = values.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String value = var2[var4];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }
}
