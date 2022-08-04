
package com.basic.aop.util;

import java.security.MessageDigest;

/**
 * <pre>
 *     desc   :	MD5加密

 *     time   : 2018/4/23 下午9:51
 * </pre>
 */
public final class MD5Utils {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * MD5加密
     *
     * @param sourceStr 加密内容
     * @return 加密结果
     */
    public static String encode(String sourceStr) {
        try {
            byte[] btInput = sourceStr.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
