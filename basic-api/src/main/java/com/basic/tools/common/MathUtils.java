
package com.basic.tools.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数学工具类
 */
public final class MathUtils {

    private static final float FLOAT_SMALL_ENOUGH_NUM = 1.0E-7F;
    private static final double DOUBLE_SMALL_ENOUGH_NUM = 1.0E-7D;

    private MathUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isEqual(float f1, float f2) {
        return Math.abs(f1 - f2) < FLOAT_SMALL_ENOUGH_NUM;
    }

    public static boolean isEqual(double f1, double f2) {
        return Math.abs(f1 - f2) < DOUBLE_SMALL_ENOUGH_NUM;
    }

    public static boolean biggerOrEqual(float f1, float f2) {
        return isEqual(f1, f2) || f1 > f2;
    }

    public static boolean biggerOrEqual(double f1, double f2) {
        return isEqual(f1, f2) || f1 > f2;
    }

    public static boolean isNumber(String str) {
        if (!StringUtils.isEmpty(str)) {
            Matcher isNum = Pattern.compile("[0-9]*").matcher(str);
            return isNum.matches();
        } else {
            return false;
        }
    }

    public static int compare(long x, long y) {
        return x < y ? -1 : (x == y ? 0 : 1);
    }

}
