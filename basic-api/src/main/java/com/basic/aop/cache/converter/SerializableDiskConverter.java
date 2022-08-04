
package com.basic.aop.cache.converter;

import com.basic.aop.logger.XLogger;
import com.basic.aop.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 序列化对象的转换器

 *     time   : 2018/4/23 下午10:14
 * </pre>
 */
public class SerializableDiskConverter implements IDiskConverter {

    @Override
    public <T> T load(InputStream source, Type type) {
        //序列化的缓存不需要用到clazz
        T value = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(source);
            value = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            XLogger.e(e);
        } finally {
            Utils.closeIO(ois);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(sink);
            oos.writeObject(data);
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            XLogger.e(e);
            return false;
        } finally {
            Utils.closeIO(oos);
        }
    }

}
