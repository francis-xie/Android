package com.basic.log.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO相关
 */
final class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}