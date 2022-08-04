
package com.basic.tools.data;

import com.basic.tools.file.CloseUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <pre>
 *     desc   : 克隆相关工具类

 *     time   : 2018/4/30 下午12:18
 * </pre>
 */
public final class CloneUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private CloneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static <T> T deepClone(final Serializable data) {
        if (data == null) {
            return null;
        }
        return (T) bytes2Object(serializable2Bytes(data));
    }

    private static byte[] serializable2Bytes(final Serializable serializable) {
        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(oos);
        }
    }

    private static Object bytes2Object(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(ois);
        }
    }
}
